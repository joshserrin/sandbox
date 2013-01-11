import AssemblyKeys._
import com.cra.sbt.SbtCommon

seq(assemblySettings: _*)

//seq(SbtCommon.commonSettings: _*)

//seq(SbtCommon.Formatter.settings: _*)

seq(SbtCommon.Eclipse.settings: _*)

name := "sandbox"

scalaVersion := "2.10.0-RC1"

resolvers += "Activate Framework Repo" at "http://fwbrasil.net/maven"

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "1.3",
  "org.apache.commons" % "commons-math" % "2.2",
  "jfree" % "jfreechart" % "1.0.13",
  //"net.fwbrasil" %% "activate-core" % "1.1",
  //"net.fwbrasil" %% "activate-jdbc" % "1.1",
  //"org.squeryl" %% "squeryl" % "0.9.5-2",
  "com.typesafe" % "slick_2.10.0-RC1" % "0.11.2", // use the right version here
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.apache.derby" % "derby" % "10.9.1.0"
  )
