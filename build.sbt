
name := """starterplay"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  javaJpa.exclude("org.hibernate.javax.persistence", "hibernate-jpa-2.0-api"),
  "org.hibernate" % "hibernate-entitymanager" % "4.3.10.Final",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.google.code.gson" % "gson" % "2.3.1",
  "org.im4java" % "im4java" % "1.4.0",
  "org.projectlombok" % "lombok" % "1.16.12",
  "com.google.guava" % "guava" % "19.0"
)

pipelineStages := Seq(digest, gzip)

//For JPA reasons
PlayKeys.externalizeResources := false
