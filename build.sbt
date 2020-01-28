lazy val root = project
    .in(file("."))
    .enablePlugins(JavaAppPackaging)
    .settings(
        // common
    name := "yester",
    version := "0.2.5",
    organization := "NUST - Programme Development Unit; FCI",
    scalaVersion := "2.11.0",
    autoScalaLibrary := false,
    scalacOptions := Seq("-unchecked", "-deprecation"),
    mainClass in (Compile) := Some("yester.Yester"),
    resolvers ++= Seq(
        Resolver.bintrayRepo("cakesolutions", "maven"),
        Resolver.bintrayRepo("mathieuancelin", "reactivecouchbase-maven")
    ),
    libraryDependencies ++= Seq(
        "org.scala-lang" % "scala-library" % "2.13.1",
        "io.reactivex" % "rxscala_2.11" % "0.26.0",
        "net.cakesolutions" %% "scala-kafka-client" % "2.3.1",
        "net.cakesolutions" %% "scala-kafka-client-akka" % "2.3.1",
        "com.typesafe" % "config" % "1.2.1",
        "com.typesafe.play" % "play-json_2.11" % "2.4.0-M2",
        "org.reactivecouchbase" %% "reactivecouchbase-rs-core" % "1.2.1",
        "io.lamma" %% "lamma" % "2.2.2",
        "io.leonard" %% "play-json-traits" % "1.2.1"
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
