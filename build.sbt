name := """play-chuncked"""
organization := "com.play.chuncked"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava, PlayNettyServer)
  .disablePlugins(PlayAkkaHttpServer)

PlayKeys.devSettings += "play.server.provider" -> "play.core.server.NettyServerProvider"

scalaVersion := "2.12.11"

val AkkaVersion = "2.5.31"


libraryDependencies ++= Seq(
  guice,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion
)