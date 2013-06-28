name := "graphite-data-receiver"

scalaVersion := "2.10.2" 

resolvers += "spray" at "http://repo.spray.io/"

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "0.3.7",
  "jfree" % "jfreechart" % "1.0.13",
  "io.spray" %%  "spray-json" % "1.2.5",
  "org.scalaj"    % "scalaj-time_2.10.0-M7" % "0.6"
)
