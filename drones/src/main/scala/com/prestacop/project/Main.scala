package com.prestacop.project

import java.io.FileReader
import scala.collection.mutable.ListBuffer
import com.google.gson._
import com.prestacop.project.bean.Configuration


object Main {

  def main(args: Array[String]): Unit = {

    // --- lecture configuration --- //

    if(args.length != 1){

      println("Attend comme argument le chemin de configuration.json")

    }
    else{

      val gson: Gson = new Gson()
      val conf: Configuration = gson.fromJson(new FileReader(args(0)), classOf[Configuration])
      Producer.conf = conf

      println(String.format("Configuration des drones chargée. Lancement simulation de %s drones", conf.nbDrones.toString))

      val drones: List[Drone] = generateDrones(conf)
      run(drones)

    }

  }

  // --- simulation de drones --- //

  def generateDrones(conf: Configuration): List[Drone] ={

    var drones: ListBuffer[Drone] = new ListBuffer[Drone]
    for(i <- 0 until conf.nbDrones){
      drones += new Drone(i, conf)
    }
    drones.toList
  }

  def run(drones: List[Drone]): Unit ={
    for(drone <- drones){
      drone.move
      drone.sendHeartBeat
      drone.sendAlert
    }
    run(drones)
  }

}
