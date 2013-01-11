name := "squeryl-example"

resolvers += "Activate Framework Repo" at "http://fwbrasil.net/maven"

libraryDependencies ++= Seq(
  "org.squeryl" %% "squeryl" % "0.9.5-2",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.apache.derby" % "derby" % "10.9.1.0"
  )
