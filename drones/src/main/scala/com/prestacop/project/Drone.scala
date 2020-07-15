package com.prestacop.project

import java.util.Calendar
import scala.util.Random
import com.prestacop.project.bean._

class Drone(val id: Int, val conf: Configuration) {

  // --- Hyper paramètres pour simuler déplacement drone --- //

  val speed: Double = Random.nextInt(20000) + 50000.0
  val timePerLat: Double = 111.0 / (speed / 3600.0) * 1000
  val timePerLong: Double = 84.0 / (speed / 3600.0) * 1000
  val heartBeatInterval: Double = 10000.0
  val cardinalDeplacement: Double = 0.001
  val maxLat: Double = 40.770
  val minLat: Double = 40.611
  val maxLong: Double = -73.715
  val minLong: Double= -74.031

  var latitude: Double = (Random.nextInt(40770 - 40611) + 40611) / 1000.0
  var longitude: Double = (Random.nextInt(74031 - 73715) - 74031) / 1000.0
  var battery = 100
  var deplacementFlag: Long = 0
  var heartBeatFlag: Long = 0
  var direction: Boolean = true

  // retourne timestamp actuel

  def getTimeInMillis: Long ={
    Calendar.getInstance().getTimeInMillis
  }

  // --- Heart Beat --- //


  def getJsonHeartBeat(date: Long): String ={
    val coordinate: Coordinate = new Coordinate(latitude, longitude)
    val alert: Alert = new Alert(0, "", "")
    new Record(id, date, coordinate, battery, alert).toString
  }


  def isToSendHB: Boolean={
    heartBeatFlag + heartBeatInterval < getTimeInMillis
  }


  def sendHeartBeat: Unit ={
    if(isToSendHB){
      Producer.produceHeartBeat(getJsonHeartBeat(getTimeInMillis))
      heartBeatFlag = getTimeInMillis
      log("Heartbeat envoyé")
    }
  }

  // --- Alertes  --- //

  def getAlert: String ={

    var alert: Alert = null
    Random.nextInt(3) match{
      case 0 => alert = new Alert(1, "stationnement gênant",
        Image.byteArrayToString(Image.imgToArrayByte(conf.sgPath)))
      case 1 => alert = new Alert(2, "suspucion de fuite après accident",
        Image.byteArrayToString(Image.imgToArrayByte(conf.sfPath)))
      case 2 => alert = new Alert(3, "léger embouteillage",
        Image.byteArrayToString(Image.imgToArrayByte(conf.lePath)))
    }

    val coordinate: Coordinate = new Coordinate(latitude, longitude)
    new Record(id, getTimeInMillis, coordinate, battery, alert).toString
  }

  def sendAlert: Unit ={
    if(Random.nextInt(conf.alertFrequency) == 1){
      Producer.produceAlert(getAlert)
      log("Alerte envoyée")
    }
  }

  // --- Déplacement --- //

  def isToMove: Boolean = {
    if (direction) deplacementFlag + timePerLat < getTimeInMillis else deplacementFlag + timePerLong < getTimeInMillis
  }

  def generateSens: Int ={
    if (Random.nextInt(2) == 1) -1 else 1
  }

  def decreaseBattery(): Unit={
    if(Random.nextInt(10) == 1) battery -= 1
  }

  def updateMove(): Unit={
    decreaseBattery()
    deplacementFlag = getTimeInMillis
    direction = !direction
  }

  def isOutOfLat(target: Double): Boolean={
    target < minLat || target > maxLat
  }

  def isOutOfLong(target: Double): Boolean={
    target < minLong || target > maxLong
  }

  def move: Unit ={

    if (isToMove && battery > 0) {
      val sens = generateSens

      if(direction && !isOutOfLat(latitude + (cardinalDeplacement * sens))) {
        latitude += (cardinalDeplacement * sens)
      }
      else if(!direction && !isOutOfLong(longitude + (cardinalDeplacement * sens))){
        longitude += (cardinalDeplacement * sens)
      }

      updateMove()
    }
  }

  // --- log --- //

  def log(message: String): Unit ={
    println(String.format("drone: %s -> %s", id.toString, message))
  }

}
