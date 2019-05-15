package logparsing

import kebra.MyLog
import kebra.MyLog._

class CoESdo(val hex: List[Int]) {
  def this() = this(List(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))

  val length = BeLeConv.read16(hex, CoESdo.indexLength)
  val sdoTyp = BeLeConv.read16(hex, CoESdo.indexSdotyp)
  val sdoTypCmd = hex.apply(CoESdo.indexSdotypCmd)
  val index = BeLeConv.read16(hex, CoESdo.indexIndex)
  val subIndex = hex.apply(CoESdo.indexSubIndex)
  val data = BeLeConv.read32(hex, CoESdo.indexData)
  //myErrPrintDln(length+" {"+hex.map("0x%02X".format(_)).mkString(", ")+"}")
  def isGood = length != 0

  override def toString = if (isGood) "[0x%04X-0x%02X".format(sdoTyp, sdoTypCmd)+" "+length+" 0x%04X".format(index)+":0x%02X".format(subIndex)+" {"+"0x%08X".format(data)+"} ]" else ""
}

object CoESdo {
  val indexLength = 0
  val indexSdotyp = 6
  val indexSdotypCmd = 8

  val indexIndex = 9
  val indexSubIndex = 11
  val indexData = 12
}

