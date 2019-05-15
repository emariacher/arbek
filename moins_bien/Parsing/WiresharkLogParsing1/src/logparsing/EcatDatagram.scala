package logparsing

import kebra.MyLog
import kebra.MyLog._

class EcatDatagram(val hex: List[Int], val startIndex: Int, val direction: Direction, val timestamp: TimeStamp) {
  val command = EcatDatagram.dgType.getOrElse(hex.apply(startIndex + EcatDatagram.indexCommand), "EC_DATAGRAM_NONE")
  val command2 = EcatDatagram.dgType2.getOrElse(hex.apply(startIndex + EcatDatagram.indexCommand), ec_datagram_type_t.EC_DATAGRAM_NONE)
  var index = hex.apply(startIndex + EcatDatagram.indexIndex)
  var slaveAddr = 0
  var offset = 0
  var length = 0
  var data = List(0)
  var endIndex = 0
  var coesdo = new CoESdo
  var isPdo = false

  //myErrPrintDln(command+" "+command2+" {"+hex.map("0x%02X".format(_)).mkString(", ")+"}")
  //myErrPrintDln(startIndex+" {"+hex.drop(startIndex).map("0x%02X".format(_)).mkString(", ")+"}")
  if (command2 != ec_datagram_type_t.EC_DATAGRAM_NONE) { // padding stuff
    index = hex.apply(startIndex + EcatDatagram.indexIndex)
    slaveAddr = BeLeConv.read16(hex, startIndex + EcatDatagram.indexSlaveAddr)
    offset = BeLeConv.read16(hex, startIndex + EcatDatagram.indexOffset)
    length = BeLeConv.read16(hex, startIndex + EcatDatagram.indexLength, 0x7FF)
    data = hex.slice(startIndex + 10, startIndex + 10 + length)
    endIndex = startIndex + 12 + length
  } else {
    myPrintDln("Padding! "+index+" "+startIndex+" {"+hex.drop(startIndex).map("0x%02X".format(_)).mkString(", ")+
      "}\n{"+hex.map("0x%02X".format(_)).mkString(", ")+"}")
    endIndex = hex.size
  }

  command2 match {
    case ec_datagram_type_t.EC_DATAGRAM_LRW  => isPdo = true // parsed later when Pdo mappings are defined
    case ec_datagram_type_t.EC_DATAGRAM_NPWR => if (length >= 16) coesdo = new CoESdo(data)
    case _                                   =>
  }
  
  override def toString = {
      command2 match {
    case ec_datagram_type_t.EC_DATAGRAM_LRW  => "["+command+" "+command2+" "+length+" {"+data.map("0x%02X".format(_)).mkString(", ")+"}"+isPdo+"]"
    case ec_datagram_type_t.EC_DATAGRAM_NPWR => "["+command+" "+command2+" "+length+" {"+data.map("0x%02X".format(_)).mkString(", ")+"}"+coesdo+"]"
    case _                                   => "["+command+" "+command2+" "+length+" {"+data.map("0x%02X".format(_)).mkString(", ")+"}]"
  }

  }
    
    
    
    
    
    
    
    
}

object EcatDatagram {
  val indexCommand = 0
  val indexIndex = 1
  val indexSlaveAddr = 2
  val indexOffset = 4
  val indexLength = 6
  val dgType2 = Map(0x00 -> ec_datagram_type_t.EC_DATAGRAM_NONE, 0x01 -> ec_datagram_type_t.EC_DATAGRAM_APRD, 0x02 -> ec_datagram_type_t.EC_DATAGRAM_APWR, 0x04 -> ec_datagram_type_t.EC_DATAGRAM_NPRD, 0x05 -> ec_datagram_type_t.EC_DATAGRAM_NPWR, 0x07 -> ec_datagram_type_t.EC_DATAGRAM_BRD, 0x08 -> ec_datagram_type_t.EC_DATAGRAM_BWR, 0x0A -> ec_datagram_type_t.EC_DATAGRAM_LRD, 0x0B -> ec_datagram_type_t.EC_DATAGRAM_LWR, 0x0C -> ec_datagram_type_t.EC_DATAGRAM_LRW)
  val dgType = Map(0x00 -> "EC_DATAGRAM_NONE", 0x01 -> "APRD", 0x02 -> "APWR", 0x04 -> "NPRD", 0x05 -> "NPWR", 0x07 -> "BRD", 0x08 -> "BWR", 0x0A -> "LRD", 0x0B -> "LWR", 0x0C -> "LRW")
}

object ec_datagram_type_t extends Enumeration {
  val EC_DATAGRAM_NONE = 0x00
  val EC_DATAGRAM_APRD = 0x01
  val EC_DATAGRAM_APWR = 0x02
  val EC_DATAGRAM_NPRD = 0x04
  val EC_DATAGRAM_NPWR = 0x05
  val EC_DATAGRAM_BRD = 0x07
  val EC_DATAGRAM_BWR = 0x08
  val EC_DATAGRAM_LRD = 0x0A
  val EC_DATAGRAM_LWR = 0x0B
  val EC_DATAGRAM_LRW = 0x0C
}

