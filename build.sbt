name := "yester"
version := "0.1.0"
organization := "NUST - Programme Development Unit & FCI"
scalaVersion := "2.12.1"
libraryDependencies ++= Seq(
    "org.apache.kafka" % "kafka_2.10" % "0.8.1",
    "com.typesafe" % "config" % "1.2.1",
    "com.typesafe.play" % "play-json_2.10" % "2.4.0-M2"
)
