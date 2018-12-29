

val buildSettings = Seq(
  organization := "org.scalamacros",
  version := "1.0.0",
  scalaVersion := "2.12.6",
  resolvers += Resolver.sonatypeRepo("snapshots"),
  resolvers += Resolver.sonatypeRepo("releases"),
  scalacOptions ++= Seq()
)


lazy val fourmis_sbt_root = (project in file("."))
  .aggregate(macros, core)
  .settings(
    buildSettings,
    name := "fourmis_sbt_root",
    run := run in Compile in core
  )

lazy val macros = (project in file("macros"))
  .settings(
    buildSettings,
    name := "macros",
    libraryDependencies += (scalaVersion) ("org.scala-lang" % "scala-reflect" % _).value,
    libraryDependencies := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        // if Scala 2.11+ is used, quasiquotes are available in the standard distribution
        case Some((2, scalaMajor)) if scalaMajor >= 11 =>
          libraryDependencies.value ++ Seq(
            "com.typesafe.akka" %% "akka-actor" % "2.3.9",
            "com.typesafe.akka" %% "akka-testkit" % "2.3.9",
            "org.scalatest" %% "scalatest" % "2.2.4" % "test",
            "junit" % "junit" % "4.12" % "test",
            "com.novocode" % "junit-interface" % "0.11" % "test"
          ) ++ Seq(
            "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
            "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3",
            "org.scala-lang.modules" %% "scala-swing" % "1.0.1")
        // in Scala 2.10, quasiquotes are provided by macro paradise
        case Some((2, 10)) =>
          libraryDependencies.value ++ Seq(
            compilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.full),
            "org.scalamacros" %% "quasiquotes" % "2.1.0-M5" cross CrossVersion.binary)
      }
    }
  )


lazy val core = (project in file("core"))
  .settings(
    buildSettings,
    name := "core",
    libraryDependencies := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        // if scala 2.11+ is used, add dependency on scala-xml module
        case Some((2, scalaMajor)) if scalaMajor >= 11 =>
          libraryDependencies.value
        case _ =>
          // or just libraryDependencies.value if you don't depend on scala-swing
          libraryDependencies.value :+ "org.scala-lang" % "scala-swing" % scalaVersion.value
      }
    }
  ).dependsOn(macros)
 
