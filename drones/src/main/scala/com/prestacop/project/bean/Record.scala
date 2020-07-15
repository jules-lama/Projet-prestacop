package com.prestacop.project.bean

class Record(id : Int, date : Long, coordinate: Coordinate, battery : Int, alert: Alert){

  override def toString: String ={
    String.format("{\"id\": %s, \"date\": %s, \"coordinate\": %s, \"battery\": %s, \"alert\": %s}",
      id.toString, date.toString, coordinate.toString,  battery.toString, alert.toString)
  }

}
