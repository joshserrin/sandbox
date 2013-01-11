name := "slick-example"

scalaVersion := "2.10.0-RC1"

resolvers += "Activate Framework Repo" at "http://fwbrasil.net/maven"

libraryDependencies ++= Seq(
  "com.typesafe" % "slick_2.10.0-RC1" % "0.11.2", // use the right version here
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.apache.derby" % "derby" % "10.9.1.0"
  )
