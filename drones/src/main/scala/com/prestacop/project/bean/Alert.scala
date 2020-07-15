package com.prestacop.project.bean


class Alert(code: Int, label: String, imgByteString: String){

  override def toString: String ={
    String.format("{\"code\": %s, \"label\": \"%s\", \"imgByteString\": \"%s\"}",
      code.toString, label, imgByteString)
  }

}
