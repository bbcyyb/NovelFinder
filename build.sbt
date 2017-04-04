name := "BookCrawler"

organization := "org.kevin.app"

version := "0.0.1"

scalaVersion := "2.11.0"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1"
  //,"com.typesafe.akka" %% "akka-actor" % "2.4.17"
)

initialCommands := "import org.kevin.app.bookcrawler._"

