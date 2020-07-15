package com.prestacop.project


import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.WriteConfig
import com.prestacop.project.bean.{Configuration, Record}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.bson.Document

// ---- insère les record dans la base mongo ---- //

object MongoDao{

  var conf: Configuration = _

  val spark: SparkSession = SparkSession
    .builder()
    .getOrCreate()

  def getWriteHeartBeatConfig: WriteConfig = {
    WriteConfig(Map("spark.mongodb.output.uri" -> String.format("%s%s", conf.mongoUrl, conf.mongoHBCol)))
  }


  def insertInRecords(df: Dataset[String]): Unit ={
    // TODO ne pas passer par le rdd
    val documents: RDD[Document] = df.rdd.map(record => Document.parse(record))
    MongoSpark.save(documents, getWriteHeartBeatConfig)

  }


  def getHeartBeatJson(df: Dataset[Record.Record]): Dataset[String] ={
    import spark.implicits._
    df.map(record => Record.objToHBString(record))
  }


  def manageBatch(df: Dataset[Record.Record]): Unit ={

    insertInRecords(getHeartBeatJson(df))

  }


}