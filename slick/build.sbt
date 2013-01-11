name := "slick-example"

scalaVersion := "2.10.0-RC1"

resolvers += "Activate Framework Repo" at "http://fwbrasil.net/maven"

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "1.3",
  "org.apache.commons" % "commons-math" % "2.2",
  "jfree" % "jfreechart" % "1.0.13",
  "com.typesafe" % "slick_2.10.0-RC1" % "0.11.2", // use the right version here
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.apache.derby" % "derby" % "10.9.1.0"
  )
