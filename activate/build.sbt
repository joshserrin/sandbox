name := "activate-example"

resolvers += "Activate Framework Repo" at "http://fwbrasil.net/maven"

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "1.3",
  "org.apache.commons" % "commons-math" % "2.2",
  "jfree" % "jfreechart" % "1.0.13",
  "net.fwbrasil" %% "activate-core" % "1.1",
  "net.fwbrasil" %% "activate-jdbc" % "1.1",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.apache.derby" % "derby" % "10.9.1.0"
  )