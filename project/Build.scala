import sbt._
import Keys._

object PG extends Build{

  val akkaVersion = "2.3.6"

  val sprayVersion = "1.3.2"

  val pgVersion = "1.0"  

  lazy val buildSettings = Seq(
    organization := "info.cicika",
    version := pgVersion,
    scalaVersion := "2.11.2"
  )

  lazy val root = Project(
    id = "pg",
    base = file("."),
    settings = defaultSettings)

  override lazy val settings = super.settings ++ buildSettings ++ Seq(
    resolvers += "Sonatype Snapshot Repo" at "https://oss.sonatype.org/content/repositories/snapshots/"
  )

  lazy val defaultSettings = Defaults.defaultSettings ++ Seq(
    libraryDependencies ++= {
      Seq(
        "com.typesafe.akka" %% "akka-actor" % akkaVersion % "compile",
        "com.typesafe.akka" %% "akka-kernel" % akkaVersion % "compile",
        "com.typesafe.akka" %% "akka-slf4j" % akkaVersion % "compile"
        )
    },

    resolvers ++= {
      Seq("Akka Releases" at "http://akka.io/repository",
          "Local Maven" at "file://" + Path.userHome.absolutePath + "/.m2/repo",
          "Local Ivy" at "file://" + Path.userHome.absolutePath + "/.ivy2/local")
    },

    credentials += Credentials("Sonatype Nexus Repository Manager", "10.55.53.4", "admin", "abudahu9"),

    scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked") ++ (
      if (true || (System getProperty "java.runtime.version" startsWith "1.7")) Seq() else Seq("-optimize")), // -optimize fails with jdk7

    javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation"),

    javaOptions ++= Seq("-Xmx12G"),

    ivyLoggingLevel in ThisBuild := UpdateLogging.Quiet,

    logLevel := Level.Info,

    maxErrors := 50,

    pollInterval := 1000,

    crossPaths := false,

    publishMavenStyle := true
  )
}
