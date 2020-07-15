package com.prestacop.project

import java.io.FileReader
import com.prestacop.project.bean.{Configuration, Record}
import org.apache.spark.sql._
import com.google.gson.Gson

object Main{

  val spark: SparkSession = SparkSession
    .builder()
    .getOrCreate()

  def main(args: Array[String]): Unit ={

    // --- lecture configuration --- //

    if (args.length != 1) {

      println("Attend comme argument le chemin de configuration.json")

    }
    else {

      val gson: Gson = new Gson()
      val conf: Configuration = gson.fromJson(new FileReader(args(0)), classOf[Configuration])
      BatchManager.conf = conf
      run(conf)

    }

  }

  // --- lecture stream --- //
  def run(conf: Configuration): Unit ={

    val kafkaStreamDF = spark.readStream.format("kafka")
      .option("kafka.bootstrap.servers", String.format("%s:%s", conf.kafkaHost, conf.kafkaPort))
      .option("subscribe", conf.kafkaTopic)
      .option("startingOffsets", "earliest")
      .option("group.id", conf.kafkaGroup)
      .load().selectExpr("CAST(value AS STRING)")


    val query = kafkaStreamDF.writeStream.foreachBatch { (batchDF: DataFrame, batchId: Long) =>

      import spark.implicits._
      val batchRecord = batchDF.map(content => content.getString(0)).mapPartitions(Record.parseFromJson(_))
      BatchManager.processAlert(batchRecord)

    }
      .start()
    query.awaitTermination()

  }

}