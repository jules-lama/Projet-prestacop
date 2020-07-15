name := "drones"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.kafka" %% "kafka" % "2.1.0" % "compile"
libraryDependencies += "com.google.code.gson" % "gson" % "1.7.1" % "compile"

mainClass in (Compile, run) := Some("com.prestacop.project.Main")
