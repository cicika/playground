package pg

import akka.actor._

import java.util.Date

object Main extends App {

	val n = 1000000

	val system = ActorSystem("pg-test")

	println("Starting actors...")
	val t1 = timestamp

	for(i <- 0 to n) {
		system.actorOf(Props[Worker], "worker-%d" format i)
	}
	val t2 = timestamp
	println("Actors created in %d millis ..." format (t2 - t1))

	println("Starting simulation...")
	val t3 = timestamp
	system.actorSelection("/user/worker-0") ! 0

	class Worker extends Actor {
		def receive = {
			case x: Int if x < n =>
				context.actorSelection("/user/worker-%d" format (x + 1)) ! (x + 1)
				println("Stopping ... %d" format x)
				context stop self
			case x: Int if x == n - 1 =>
				val t4 = timestamp
				println("Simulation completed in %d milis. Result %d" format ((t4 - t3), x))
				context stop self
		}
	}

	def timestamp = new Date().getTime()
}