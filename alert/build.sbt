name := "alertManager"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.0" % "compile"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.0" % "compile"
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.4.3" % "compile"
libraryDependencies += "com.google.code.gson" % "gson" % "1.7.1" % "compile"
libraryDependencies += "org.mongodb.spark" %% "mongo-spark-connector" % "2.2.8" % "compile"


assemblyMergeStrategy in assembly := {
  case "META-INF/services/org.apache.spark.sql.sources.DataSourceRegister" => MergeStrategy.concat
  case PathList("META-INF", xs @ _*) =>
    xs map {_.toLowerCase} match {
      case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) =>
        MergeStrategy.discard
      case "services" :: _ =>  MergeStrategy.filterDistinctLines
      case ps @ (x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
        MergeStrategy.discard
      case _ => MergeStrategy.first
    }
  case _ => MergeStrategy.first
}


mainClass in (Compile, run) := Some("com.prestacop.project.Main")
mainClass in assembly := Some("com.prestacop.project.Main")