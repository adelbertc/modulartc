lazy val buildSettings = List(
  organization := "com.adelbertc",
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  scalaVersion := "2.11.7",
  crossScalaVersions := List("2.10.6", scalaVersion.value),
  version := "0.1.0-SNAPSHOT"
)

lazy val commonSettings = List(
  scalacOptions ++= List(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard"
  ),
  libraryDependencies ++= List(
    compilerPlugin("com.github.mpilquist" %% "local-implicits" % "0.3.0")
  )
)

lazy val modulartcSettings = buildSettings ++ commonSettings

lazy val modulartc =
  project.in(file(".")).
  settings(name := "modular-tc").
  settings(description := "").
  settings(modulartcSettings)
