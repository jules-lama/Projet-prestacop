package com.prestacop.project

import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.WriteConfig
import org.apache.spark.sql._
import com.mongodb.spark.config._

object Main {

  def main(args: Array[String]): Unit ={
    val sparkRecords: SparkSession = SparkSession
      .builder()
      .master("local")
      .appName("testMongo")
      .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/prestacop.records")
      .getOrCreate()


    val df = sparkRecords.read.format("mongo").load()
    df.show()


    // TODO: 20 drones avec le moins de batteries

    //TODO: 20 drones avec les records les plus anciens

    //TODO: 20 drones les plus au nord ...


  }



}
