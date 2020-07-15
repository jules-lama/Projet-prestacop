package com.prestacop.project

import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.WriteConfig
import com.prestacop.project.bean._
import com.prestacop.project.tool._
import org.apache.spark.sql._
import org.bson.Document
import java.util.Calendar
import org.apache.spark.rdd.RDD

object BatchManager{

  var conf: Configuration = _

  val spark: SparkSession = SparkSession
    .builder()
    .getOrCreate()

  // ---- Mongo ---- //

  def getWriteHeartBeatConfig: WriteConfig = {
    WriteConfig(Map("spark.mongodb.output.uri" -> String.format("%s%s", conf.mongoUrl, conf.mongoHBCol)))
  }

  def getWriteAlertConfig: WriteConfig ={
    WriteConfig(Map("spark.mongodb.output.uri" -> String.format("%s%s", conf.mongoUrl, conf.mongoAlertCol)))
  }

  def insertInRecords(df: Dataset[Record.Record]): Unit ={

    // TODO ne pas passer par le rdd
    val documents: RDD[Document] = df.rdd.map(record => Document.parse(Record.objToHBString(record)))
    MongoSpark.save(documents, getWriteHeartBeatConfig)

  }

  def insertInAlerts(df: Dataset[Record.Record]): Unit ={

    // TODO ne pas passer par le rdd
    val documents: RDD[Document] = df.rdd.map(record => Document.parse(Record.objToString(record)))
    MongoSpark.save(documents, getWriteAlertConfig)

  }


  // ---- gestion images ---- //


  def getImageByteArray(byteStr: String): Array[Byte] ={
    Image.stringToByteArray(byteStr)
  }

  def getImagePath(label: String): String ={
    conf.imgPath + label.replaceAll(" ", "_") + "_" + Calendar.getInstance().getTimeInMillis.toString + ".jpg"
  }


  // ---- traitement ---- //

  def getAlerts(df: Dataset[Record.Record]): Dataset[Record.Record] ={
    df.filter(record => record.alert.code != 0)
  }


  def processAlert(df: Dataset[Record.Record]): Unit ={

    import spark.implicits._
    val alerts: Dataset[Record.Record] = df.map(record => {
      Record.Record(record.id, record.date, record.coordinate, record.battery,
        Record.Alert(record.alert.code,
          record.alert.label,
          null,
          getImageByteArray(record.alert.imgByteString),
          getImagePath(record.alert.label)))
    })

    alerts.foreach(alert => {

      Image.loadImgFromByteArray(alert.alert.byteImg, alert.alert.path)
      println(String.format("!ALERTE! Drone: %s, localisation:%s:%s infraction: %s",
        alert.id.toString,
        alert.coordinate.latitude.toString,
        alert.coordinate.longitude.toString,
        alert.alert.path))

    })

    insertInAlerts(alerts)
    insertInRecords(alerts)


  }


}