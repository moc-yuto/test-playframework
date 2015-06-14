name := """test"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.json4s" %% "json4s-native" % "3.2.10",
  "org.json4s" %% "json4s-jackson" % "3.2.10",
  "org.scalikejdbc" %% "scalikejdbc" % "2.0.5",
  "org.scalikejdbc" %% "scalikejdbc-play-plugin" % "2.3.0",
  "com.h2database"  %  "h2" % "1.4.181",
  "com.github.tototoshi" %% "play-flyway" % "1.2.1",
  "mysql" % "mysql-connector-java" % "5.1.6"
)
