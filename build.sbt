name := "BookCrawler"

organization := "org.kevin.app"

version := "0.0.1"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.12" % "3.0.1"
)

initialCommands := "import org.kevin.app.bookcrawler._"

