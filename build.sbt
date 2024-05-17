
ThisBuild / scalaVersion     := "3.4.1"
ThisBuild / version          := "0.1.0"
ThisBuild / organizationName := "example"
ThisBuild / testFrameworks   += new TestFramework("zio.test.sbt.ZTestFramework")
ThisBuild / organization := "com.example.sensor"
maintainer := "Raman Mishra"

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

dockerUsername := Some("raman08")

ThisBuild / assemblyMergeStrategy := {
  case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "unwanted.txt"                                => MergeStrategy.discard
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}

dockerBaseImage := "openjdk:17-jdk-slim"

lazy val root = (project in file("."))
  .settings(
    name := "humidity-sensor-app",
    assembly/assemblyJarName := "humidity-sensor-app.jar",
    libraryDependencies ++= Dependencies.all
  )

Compile / mainClass := Some("SensorApp")