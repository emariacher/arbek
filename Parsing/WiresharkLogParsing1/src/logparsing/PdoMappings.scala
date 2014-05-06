package logparsing

import kebra.MyLog
import kebra.MyLog._

object PdoMappings {
  val mappingRegisters = List(0x1600, 0x1A000, 0x1601, 0x1A01, 0x1602, 0x1A002, 0x1603, 0x1A03, 0x1C12, 0x1C13)
  var mappings: PdoMappings = _

  def initMappings(ldatagrams: List[EcatDatagram]) = {
    mappings = new PdoMappings(ldatagrams)
  }
}

class PdoMappings(val ldatagrams: List[EcatDatagram]) {
  var registers = Map.empty[Int, CoERegister]

  ldatagrams.foreach(d => {
    val reg = registers.getOrElse(d.coesdo.index, new CoERegister)
    reg.update(d.coesdo)
    //myPrintDln(d, "0x%04X".format(d.coesdo.index), reg)
    registers = registers + ((d.coesdo.index, reg))
  })

  myErrPrintDln(toString)

  val reg1C12 = registers.getOrElse(0x1C12, new CoERegister)
  val rxPdo_160X_MS_Index = reg1C12.subIndexes.getOrElse(1, 0)
  myAssert((rxPdo_160X_MS_Index >= 0x1600) && (rxPdo_160X_MS_Index <= 0x1603))
  val rxPdo_160X_MS = new CoEMappingRegister(registers.getOrElse(rxPdo_160X_MS_Index, new CoERegister))

  val reg1C13 = registers.getOrElse(0x1C13, new CoERegister)
  val txPdo_1A0X_MS_Index = reg1C13.subIndexes.getOrElse(1, 0)
  myAssert((reg1C13.subIndexes.getOrElse(1, 0) >= 0x1A00) && (reg1C13.subIndexes.getOrElse(1, 0) <= 0x1A03))
  val txPdo_1A0X_MS = new CoEMappingRegister(registers.getOrElse(txPdo_1A0X_MS_Index, new CoERegister))

  def getMappingRegister(direction: Direction): CoEMappingRegister = direction match {
    case Direction.Rx_Outputs_MS => rxPdo_160X_MS
    case Direction.Tx_Inputs_SM  => txPdo_1A0X_MS
  }

  override def toString = registers.toList.sortBy(_._1).map(c => "0x%04X -> %s".format(c._1, c._2.toString)).mkString("\n  ", "\n  ", "\n  ")
}