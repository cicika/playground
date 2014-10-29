package pg

import akka.actor._
import akka.actor.Actor._

import java.util.Date

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

	val n = 10000000

	val system = ActorSystem("pg-test")

	println("Starting actors...")
	val t1 = timestamp

	for(i <- 0 to n) {
		//This creates a race condition if the number of actors being started is small.
		//We've seen this happen when running tests with 1,000,000 actors
		//never with 10m
		Future{
			system.actorOf(Props[Worker], "worker-%d" format i)
		}
		if(i == n - 1) println("Actors created in %d millis ..." format (timestamp - t1))
	}	

	println("Starting simulation...")
	val t3 = timestamp
	system.actorSelection("/user/worker-0") ! 0

	class Worker extends Actor {
		def receive = {
		case x: Int =>
			if (x < n - 1) {
				context.actorSelection("/user/worker-%d" format (x + 1)) ! (x + 1)
				context stop self
			} else {
				val t4 = timestamp
				println("Simulation completed in %d milis. Result %d" format ((t4 - t3), x))
				context.system.shutdown
			}			
		case _ =>
		}
	}

	def timestamp = new Date().getTime()

}