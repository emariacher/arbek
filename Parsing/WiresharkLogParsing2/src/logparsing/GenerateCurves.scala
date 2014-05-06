package logparsing

import kebra.MyLog
import kebra.MyLog._

class GenerateCurves(pdos: List[PDO]) {

  override def toString = {
    val registerMappingRxOutputs = PdoMappings.mappings.getMappingRegister(Direction.Rx_Outputs_MS)
    val registerMappingTxInputs = PdoMappings.mappings.getMappingRegister(Direction.Tx_Inputs_SM)
    var s = "TimeStamp; "+registerMappingRxOutputs.l.map(o => "0x%04X".format(o._1)).mkString("", "; ", "; ") +
      registerMappingTxInputs.l.map(i => "0x%04X".format(i._1)).mkString("", "; ", "")
    s += pdos.map(pdo => {
      pdo.edg.timestamp + (pdo.edg.direction match {
        case Direction.Rx_Outputs_MS => pdo.toStringValues(0, registerMappingTxInputs.l.size)
        case Direction.Tx_Inputs_SM  => pdo.toStringValues(registerMappingRxOutputs.l.size, 0)
      })
    }).mkString("\n  ", "\n  ", "\n  ")
    s
  }
  myPrintln(toString)
  copy2File("ecat.csv", toString)
}