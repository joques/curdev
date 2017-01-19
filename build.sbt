name := "yester"
version := "0.1.0"
organization := "NUST - Programme Development Unit & FCI"
scalaVersion := "2.11.0"
autoScalaLibrary := false
scalacOptions := Seq("-unchecked", "-deprecation")

resolvers ++= Seq("RethinkScala Repository" at "http://kclay.github.io/releases")

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-library" % "2.11.0",
    "io.reactivex" % "rxscala_2.11" % "0.26.0",
    "org.apache.kafka" % "kafka-clients" % "0.9.0.0",
    "org.apache.kafka" % "kafka_2.11" % "0.9.0.1"
    exclude("javax.jms", "jms")
    exclude("com.sun.jdmk", "jmxtools")
    exclude("com.sun.jmx", "jmxri"),
    "com.typesafe" % "config" % "1.2.1",
    "com.typesafe.play" % "play-json_2.11" % "2.4.0-M2",
    "com.rethinkscala" % "core" % "0.5.0"
)
