package graphlayout

import java.util.zip.CRC32

import kebra.MyLog._

object ParametresPourFourmi {

  var version = 7
  var hash = hashCode()
  var crcvar: Long = 0
  var nombreDefourmisParTribu = 5
  var stabilisationRassemble = 100

  var limiteArreteLeRun = 200

  var depotEvaporation = 0.995
  var depotEvaporeFinal = 4
  var depotDepotInitial = 100

  def printStuff = { // https://stackoverflow.com/questions/6756442/scala-class-declared-fields-and-access-modifiers
    getClass.getDeclaredFields.toList.foreach(f => {
      //f.setAccessible(true)
      myPrintDln("" + f.toString.split("\\.").last + " [" + f.get(this) + "]")
    })
    myPrintln(getFields)
    myPrintln(getValues)
  }

  def getFields = getClass.getDeclaredFields.toList.map(_.toString.split("\\.").last).mkString("", ", ", "")

  def getValues = {
    val crc = new CRC32
    crcvar = 0
    crc.update(getClass.getDeclaredFields.toList.map(_.get(this)).mkString("", ", ", "").getBytes)
    crcvar = crc.getValue
    val line = getClass.getDeclaredFields.toList.map(_.get(this)).mkString("", ", ", "")
    line
  }
}
