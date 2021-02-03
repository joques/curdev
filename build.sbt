    lazy val root = project
    .in(file("."))
    .enablePlugins(JavaAppPackaging)
    .settings(
        // common
    name := "yester",
    version := "0.2.5",
    organization := "NUST - Programme Development Unit; FCI",
    scalaVersion := "2.12.13",
    autoScalaLibrary := false,
    scalacOptions := Seq("-unchecked", "-deprecation", "-feature"),
    mainClass in (Compile) := Some("yester.Yester"),
    resolvers ++= Seq(
        Resolver.bintrayRepo("cakesolutions", "maven"),
        Resolver.bintrayRepo("mathieuancelin", "reactivecouchbase-maven")
    ),
    libraryDependencies ++= Seq(
        "org.scala-lang" % "scala-library" % "2.12.13",
        "io.reactivex" % "rxscala_2.12" % "0.27.0",
        "net.cakesolutions" %% "scala-kafka-client" % "2.3.1",
		"net.cakesolutions" %% "scala-kafka-client-akka" % "2.3.1",
		"org.apache.logging.log4j" % "log4j-core" % "2.7",
        "com.typesafe" % "config" % "1.4.1",
        "com.typesafe.play" % "play-json_2.12" % "2.9.1",
        "org.reactivecouchbase" %% "reactivecouchbase-rs-core" % "1.2.1",
        "io.lamma" %% "lamma" % "2.3.1",
        "io.leonard" %% "play-json-traits" % "1.5.1"
    ),
    // environment-specific
    mappings in Universal += {
        val confFile = buildEnv.value match {
            case BuildEnv.Development => "dev.conf"
            case BuildEnv.Production => "prod.conf"
        }
        ((resourceDirectory in Compile).value / confFile) -> "application.conf"
    }
)

assemblyMergeStrategy  in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case PathList("lo4j.Properties") => MergeStrategy.last
    case x => MergeStrategy.first
}
