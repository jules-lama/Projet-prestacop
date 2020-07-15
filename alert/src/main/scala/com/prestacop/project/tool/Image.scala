package com.prestacop.project.tool

import java.awt.image.BufferedImage
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, File}

import javax.imageio.ImageIO


// --- transforme image en string pour stocker dans stream kafka --- //

object Image {

  def imgToArrayByte(path: String): Array[Byte] = {
    val img: BufferedImage = ImageIO.read(new File(path))
    val outputStream: ByteArrayOutputStream = new ByteArrayOutputStream()
    ImageIO.write(img, "jpg", outputStream)
    outputStream.toByteArray
  }

  def byteArrayToString(byteArray: Array[Byte]): String ={
    byteArray.mkString(",")
  }

  def stringToByteArray(byteString: String): Array[Byte] ={
    byteString.split(",").map(rgb => rgb.toByte)
  }

  def loadImgFromByteArray(imgBytes: Array[Byte], path: String): Unit ={
    val img: ByteArrayInputStream = new ByteArrayInputStream(imgBytes)
    val bufferedImage: BufferedImage = ImageIO.read(img)
    ImageIO.write(bufferedImage, "jpg", new File(path))
  }

}
