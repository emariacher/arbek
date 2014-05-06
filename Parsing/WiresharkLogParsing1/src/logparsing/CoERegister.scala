package logparsing

import kebra.MyLog
import kebra.MyLog._

class CoERegister {
  var subIndexes = Map.empty[Int, Int]

  def update(sdo: CoESdo) = subIndexes = subIndexes + ((sdo.subIndex, sdo.data))

  override def toString = subIndexes.toList.sortBy(_._1).map(c => "0x%02X -> 0x%08X".format(c._1, c._2)).toString
}

class CoEMappingRegister(reg: CoERegister) {
  val lraw = reg.subIndexes.toList.sortBy(_._1)
  myAssert2(lraw.head._2, lraw.tail.size)
  val l = lraw.tail.map(c => (c._2 >> 16, c._2 & 0xffff))
  myPrintDln(l.map(c => " (0x%04X,%d)".format(c._1,c._2)))
}
