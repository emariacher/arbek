package blealpwise

import kebra.MyLog._

class Characteristic(val name: String, val lfields: List[Field]) {
  myPrintln("********************************************************************************" + name)
  override def toString = if (lfields.isEmpty) {
    ""
  } else {
    (name, "\n  ", lfields).toString
  }

  def toStringHfileStruct = {
    lfields.map(_.toStringHfileStruct).mkString("\n  ", "  ", "\n")
  }

  def toStringHfileMemoryStruct = {
    var s = ""

    s += "Att128BitCharacteristicAttribute    " + name + "ValueCharacteristic;\n"
    s += "AttAttribute                        " + name + "Attribute;\n"
    s += "U8                                  " + name + "Value[" + name.toUpperCase + "_VALUE_LEN];\n"
    s += "AttInstantiatedAttribute            " + name + "ClientConfigAttribute;\n"
    s += "U8                                  " + name + "ClientConfigMemory[2*BLE_NUM_MAX_CONNECTION];\n"
    
    s
  }
}