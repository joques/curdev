lazy val root = (project in file(".")).settings(
    name := "yester",
    version := "0.1.0",
    organization := "NUST - Programme Development Unit & FCI",
    scalaVersion := "2.11.0",
    autoScalaLibrary := false,
    scalacOptions := Seq("-unchecked", "-deprecation"),
    mainClass in (Compile) := Some("yester.Yester")
)

// resolvers ++= Seq("ReactiveCouchbase Releases" at "https://raw.github.com/ReactiveCouchbase/repository/master/releases/")

resolvers += "reactive-couchbase-rs-releases" at "https://raw.github.com/ReactiveCouchbase/reactivecouchbase-rs-core/master/repository/releases"

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-library" % "2.11.0",
    "io.reactivex" % "rxscala_2.11" % "0.26.0",
    "org.apache.kafka" % "kafka-clients" % "0.9.0.1",
    "org.apache.kafka" % "kafka_2.11" % "0.9.0.1"
    exclude("javax.jms", "jms")
    exclude("com.sun.jdmk", "jmxtools")
    exclude("com.sun.jmx", "jmxri"),
    "com.typesafe" % "config" % "1.2.1",
    "com.typesafe.akka" % "akka-actor_2.11" % "2.3.3",
    "com.typesafe.play" % "play-json_2.11" % "2.4.0-M2",
    "org.reactivecouchbase" %% "reactivecouchbase-rs-core" % "1.0.0",
    "io.lamma" %% "lamma" % "2.2.2",
    "io.leonard" %% "play-json-traits" % "1.2.1"
)

assemblyMergeStrategy  in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case PathList("lo4j.Properties") => MergeStrategy.last
    case x => MergeStrategy.first
}
