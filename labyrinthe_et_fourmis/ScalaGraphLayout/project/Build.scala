import sbt._
import Keys._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.scalamacros",
    version := "1.0.0",
    scalaVersion := "2.13.1",
    crossScalaVersions := Seq("2.12.6", "2.13.1"),
    resolvers += Resolver.sonatypeRepo("snapshots"),
    resolvers += Resolver.sonatypeRepo("releases"),
    logLevel := Level.Debug,
    scalacOptions ++= Seq("-feature", "-deprecation")
  )
}

object MyBuild extends Build {

  import BuildSettings._

  lazy val root: Project = Project(
    "ScalaGraphLayout",
    file("."),
    settings = buildSettings ++ Seq(
      logLevel := Level.Debug,
      run := run in Compile in core)
  ) aggregate(macros, core)

  lazy val macros: Project = Project(
    "macros",
    file("macros"),
    settings = buildSettings ++ Seq(
      libraryDependencies <+= (scalaVersion) ("org.scala-lang" % "scala-reflect" % _),
      logLevel := Level.Debug,
      libraryDependencies := {
        CrossVersion.partialVersion(scalaVersion.value) match {
          // if Scala 2.11+ is used, quasiquotes are available in the standard distribution
          case Some((2, scalaMajor)) if scalaMajor >= 13 =>
            libraryDependencies.value ++ Seq(
              "com.typesafe.akka" %% "akka-actor" % "2.5.25",
              "com.typesafe.akka" %% "akka-testkit" % "2.5.25",
              "org.scalatest" %% "scalatest" % "3.2.0-M1" % "test",
              "junit" % "junit" % "4.12" % "test",
              "com.novocode" % "junit-interface" % "0.11" % "test",
              "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
              "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
              "org.apache.commons" % "commons-math3" % "3.5",
              "com.lihaoyi" %% "sourcecode" % "0.1.7",
              "org.scala-lang.modules" %% "scala-swing" % "2.1.1")
          case Some((2, scalaMajor)) if scalaMajor >= 12 =>
            libraryDependencies.value ++ Seq(
              "com.typesafe.akka" %% "akka-actor" % "2.5.19",
              "com.typesafe.akka" %% "akka-testkit" % "2.5.19",
              "org.scalatest" %% "scalatest" % "3.0.5" % "test",
              "junit" % "junit" % "4.12" % "test",
              "com.novocode" % "junit-interface" % "0.11" % "test",
              "com.lihaoyi" %% "sourcecode" % "0.1.4",
              "org.scala-lang.modules" %% "scala-xml" % "1.1.1",
              "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.1",
              "org.apache.commons" % "commons-math3" % "3.5",
              "org.scala-lang.modules" %% "scala-swing" % "2.1.0")
          // in Scala 2.10, quasiquotes are provided by macro paradise
          case Some((2, 10)) =>
            libraryDependencies.value ++ Seq(
              compilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.full),
              "org.scalamacros" %% "quasiquotes" % "2.1.0-M5" cross CrossVersion.binary)
        }
      }
    )
  )

  lazy val core: Project = Project(
    "core",
    file("core"),
    settings = buildSettings ++ Seq(
      libraryDependencies := {
        CrossVersion.partialVersion(scalaVersion.value) match {
          // if scala 2.11+ is used, add dependency on scala-xml module
          case Some((2, scalaMajor)) if scalaMajor >= 11 =>
            libraryDependencies.value
          case _ =>
            // or just libraryDependencies.value if you don't depend on scala-swing
            libraryDependencies.value :+ "org.scala-lang" % "scala-swing" % scalaVersion.value
        }
      },logLevel := Level.Debug
    )
  ) dependsOn (macros)
}
