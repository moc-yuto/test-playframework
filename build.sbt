name := """test"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.scalikejdbc" %% "scalikejdbc-play-plugin" % "1.8.0",
  "com.h2database"  %  "h2" % "1.4.181"
)
