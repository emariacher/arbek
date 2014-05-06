package logparsing

import kebra.MyLog
import kebra.MyLog._

class EcatFrame(val hdr: LineWsHdr, val hexdata: List[String]) {
  val hex = hexdata.map(_.split(" ").toList.filter(_.length == 2).map(Integer.parseInt(_, 16))).flatten
  val direction = if (hdr.dest.startsWith("Beckhoff")) Direction.Rx_Outputs_MS else Direction.Tx_Inputs_SM

  var datagrams = List.empty[EcatDatagram]
  if (checkEcat(false)) {
    var index = 16
    while ((index < hdr.length)) {
      //myPrintln(toString)
      val edg = new EcatDatagram(hex, index, direction, hdr.timeStamp)
      datagrams = datagrams :+ edg
      datagrams = datagrams.filter(dg => dg.coesdo.length>=10 || dg.isPdo)
      index = edg.endIndex
    }
  }

  //override def toString = "["+hdr.line+"\n   {"+hex.map("0x%02X".format(_)).mkString(", ")+"}\n   "+checkEcat(false)+"\n   "+datagrams.mkString("\n   ")+"]\n"
  override def toString = {
    var s = ""
    if (direction == Direction.Rx_Outputs_MS) {
      s += "M->S"
    } else {
      s += "M<-S"
    }
    s+"["+hdr.line+"\n   "+datagrams.mkString("\n   ")+"]\n"
  }

  def checkEcat(stop: Boolean): Boolean = {
    if (stop) {
      myAssert2(hex.size, hdr.length)
      myAssert(if (hdr.protocol == "ECAT") { hex.apply(12) == 0x88 && hex.apply(13) == 0xA4 } else false)
      true
    } else {
      if (hex.size != hdr.length) false
      else if (hdr.protocol == "ECAT") { hex.apply(12) == 0x88 && hex.apply(13) == 0xA4 } else false
    }
  }
}

abstract sealed trait Direction

object Direction {
  case object Rx_Outputs_MS extends Direction
  case object Tx_Inputs_SM extends Direction
}

