package logparsing

import kebra.MyLog
import kebra.MyLog._

class PDO(val edg: EcatDatagram) {
  val registerMapping = PdoMappings.mappings.getMappingRegister(edg.direction)
  var data = edg.data

  val values = registerMapping.l.map(c => {
    var value = 0
    c._2 match {
      case 0x20 =>
        value = BeLeConv.read32(data.take(4)); data = data.drop(4)
      case 0x10 =>
        value = BeLeConv.read16(data.take(2)); data = data.drop(2)
      case 0x8 =>
        value = data.head; data = data.tail
      case _ =>
        value = 0; data = List.empty[Int];
    }
    value
  })

  def toStringHex = {
    var s = edg.timestamp.toString+", "
    if (edg.direction == Direction.Rx_Outputs_MS) {
      s += "MS, "
    } else {
      s += "SM, "
    }

    val zipped = values zip registerMapping.l
    s += zipped.map(z => {
      z._2._2 match {
        case 0x20 =>
          "0x%08X".format(z._1)
        case 0x10 =>
          "0x%04X".format(z._1)
        case 0x8 =>
          "0x%02X".format(z._1)
        case _ => "Not yet handled mapping ["+z+"]";
      }
    }).mkString("", ", ", "")
    s
  }

  def toStringValues(before: Int, after: Int): String = {
    myPrintIt(before, after, toStringHex)
    var s = (0 until before).map(i => ";").mkString("")
    s += values.mkString(";", "; ", "")
    s += (0 until after).map(i => ";").mkString("")
    s
  }

}