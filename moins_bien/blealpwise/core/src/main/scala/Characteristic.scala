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

  def toStringCfileCallback(title: String) = {
    var name1stlowcase = Utils.firstLetter2LowerCase(name)
    var s = ""
    s += "if( parms->parms.writeComplete.attribute == &(ble" + title + "Service." + name1stlowcase + "Attribute) ) {\n"
    s += "  " + name + "Type " + name1stlowcase + ";\n"

    var cpt = 0
    s += lfields.map(f => {
      var s2 = ""
      f.size match {
        case 1 => s2 += name1stlowcase + "." + f.name + "= (U8)(*(parms->parms.writeComplete.value + " + cpt + "));\n"
        case 2 => s2 += name1stlowcase + "." + f.name + "= (U8)(*(parms->parms.writeComplete.value + " + cpt +
          ")) | (U16)(*(parms->parms.writeComplete.value + " + (cpt + 1) + ") << 8);\n"
        case 4 => s2 += name1stlowcase + "." + f.name + "= (U8)(*(parms->parms.writeComplete.value + " + cpt +
          ")) | (U32)(*(parms->parms.writeComplete.value + " + (cpt + 1) + ") << 8) | (U32)(*(parms->parms.writeComplete.value + " +
          (cpt + 2) + ") << 16) | (U32)(*(parms->parms.writeComplete.value + " + (cpt + 3) + ") << 24);\n"
        case _ => "[" + f.size + "] NotYetSupported"
      }
      cpt += f.size
      s2
    }).mkString("\n", "\n  ", "\n")

    s += "\n  ble" + title + "Service.callback(BLE" + title.toUpperCase + "SERVICE_EVENT_" + name.toUpperCase + "UPDATE, parms->status, (void*)&(" + Utils.firstLetter2LowerCase(name) + "));\n"
    s += "}\n"
    s
  }

  def setFunctionName(title: String) = {
    "BleStatus " + title + "_SERVER_Set" + name + "(" + name + "Type *" + Utils.firstLetter2LowerCase(name) + ")\n"
  }

  def setFunctionBody(title: String) = {
    var name1stlowcase = Utils.firstLetter2LowerCase(name)
    var s = ""
    s += "  BleStatus status = BLESTATUS_FAILED;\n"
    s += "  U8 i;\n"
    s += "  AttDevice connHandles[BLE_NUM_MAX_CONNECTION];\n"
    s += "  U8        *value;\n"
    s += "  U16       length;\n"
    s += "  U8        numberOfActiveConnections;\n"
    s += "  \n"

    var cpt = 0
    s += lfields.map(f => {
      var s2 = "  ble" + title + "Service." + name1stlowcase + "Value[" + cpt + "] = (U8)" + name1stlowcase + "->" + f.name + ";\n"
      cpt += 1
      if (f.size > 1) {
        s2 += "  ble" + title + "Service." + name1stlowcase + "Value[" + cpt + "] = (U8)(" + name1stlowcase + "->" + f.name + " >> 8);\n"
        cpt += 1
      }
      if (f.size > 2) {
        s2 += "  ble" + title + "Service." + name1stlowcase + "Value[" + cpt + "] = (U8)(" + name1stlowcase + "->" + f.name + " >> 16);\n"
        cpt += 1
      }
      if (f.size > 3) {
        s2 += "  ble" + title + "Service." + name1stlowcase + "Value[" + cpt + "] = (U8)(" + name1stlowcase + "->" + f.name + " >> 24);\n"
        cpt += 1
      }
      s2
    }).mkString("\n", "\n", "\n")

    s += "  \n"
    s += "  ATT_SERVER_SecureDatabaseAccess();\n"
    s += "  // Write the value inside the attribute\n"
    s += "  status = ATT_SERVER_WriteAttributeValue(&(ble" + title + "Service." + name1stlowcase + "Attribute),\n"
    s += "                              (U8*)&(ble" + title + "Service." + name1stlowcase + "Value),\n"
    s += "                              " + name.toUpperCase + "_VALUE_LEN);\n"
    s += "  ATT_SERVER_ReleaseDatabaseAccess();\n"
    s += "  if (status != BLESTATUS_SUCCESS){\n"
    s += "      return status;\n"
    s += "  }\n"
    s += "  // The " + name + " has change, so now if this alarm characteristic SERVER supports \n"
    s += "  // indicating or notifying it, we shall send the notification and/or \n"
    s += "  // indication here\n"
    s += "  // Get the connected devices\n"
    s += "  numberOfActiveConnections = BLEGAP_GetNumberOfActiveConnections( (U16 *)&connHandles);\n"
    s += "  if( numberOfActiveConnections > 0 ){\n"
    s += "          // for each connected device, do it exist some device that have configured\n"
    s += "          // the battery level to be notified\n"
    s += "          for(i = 0; i < numberOfActiveConnections; i++){\n"
    s += "              ATT_SERVER_SecureDatabaseAccess();\n"
    s += "              if( ATT_SERVER_ReadInstantiatedAttributeValue(&(ble" + title + "Service." + name1stlowcase + "ClientConfigAttribute),\n"
    s += "                  connHandles[i],\n"
    s += "                  &value,\n"
    s += "                  &length) == BLESTATUS_SUCCESS){\n"
    s += "                  if( (LE_TO_U16(value) & ATT_CLIENTCONFIG_NOTIFICATION) != 0x0000 ){\n"
    s += "                      // this server has been configured to notify the Battery Level value by this client\n"
    s += "                      ATT_SERVER_SendNotification(&(ble" + title + "Service." + name1stlowcase + "Attribute),\n"
    s += "                          connHandles[i]);\n"
    s += "                  }\n"
    s += "              }\n"
    s += "              ATT_SERVER_ReleaseDatabaseAccess();\n"
    s += "          }\n"
    s += "      }\n"
    s += "  return status;\n"
    s
  }

}