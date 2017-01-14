name := "yester"
version := "0.1.0"
organization := "NUST - Programme Development Unit & FCI"
scalaVersion := "2.11.0"
autoScalaLibrary := false
scalacOptions := Seq("-unchecked", "-deprecation")
libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-library" % "2.11.0",
    "org.apache.kafka" % "kafka-clients" % "0.9.0",
    "org.apache.kafka" % "kafka_2.11" % "0.10.1.0"
    exclude("javax.jms", "jms")
    exclude("com.sun.jdmk", "jmxtools")
    exclude("com.sun.jmx", "jmxri"),
    "com.typesafe" % "config" % "1.2.1",
    "com.typesafe.play" % "play-json_2.10" % "2.4.0-M2"
)
