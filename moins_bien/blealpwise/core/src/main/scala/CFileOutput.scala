package blealpwise
import kebra.MyLog._

class CFileOutput(val lchars: List[Characteristic]) {

  def toStringCfileUUIDs(title: String) = {
    var s = ""
    s += title + "ServiceMemory          ble" + title + "Service;\n"
    s += title + "ServiceMemory* API_" + title + "_get_ble" + title + "Service(void){\n"
    s += "return &ble" + title + "Service;\n"
    s += "}\n"
    s += lchars.zipWithIndex.map(z => {
      var s2 = "\n// " + Utils.firstLetter2LowerCase(z._1.name) + ": 0x5D%02X\n".format(z._2)
      s2 += "CONST_DECL U8 " + Utils.firstLetter2LowerCase(z._1.name) + "UUID[16] = {\n"
      s2 += "   0X00, 0X00, 0x00, 0x00, 'O', 'S', 'S', 'I', 'T', 0X00, 0X00, 0X00, 0X%02X, 0X5D, 0X00, 0X00 }\n".format(z._2)
      s2
    }).mkString("\n", "", "\n")
    s
  }

  def toStringCfileFunctions(title: String) = {
    var s = ""
    s += lchars.map(c => {
      c.setFunctionName(title) + c.setFunctionBody(title)
    }).mkString("\n", "\n", "\n")
    s
  }

  def toStringCfileCallback(title: String) = {
    var s = ""
    s += ""
    s += lchars.map(_.toStringCfileCallback(title)).mkString("\n", "\n", "\n")
    s
  }

  def toStringCfile(title: String) = {
    var s = ""
    s += "#include \"BleTypes.h\"\n"
    s += "#include \"ATT/att.h\"\n\n"
    s += toStringCfileUUIDs(title)
    s += toStringCfileFunctions(title)
    s += toStringCfileCallback(title)
    s
  }

  def toStringHfileDecl = lchars.map(c => {
    var s = "\n/** " + c.name + " **/"

    s += c.lfields.filter(!_.lsubfields.isEmpty).map(f => {
      "typedef " + f.name + "BitField;\n" +
        f.lsubfields.map(_.toStringHfileBitMap(f.name)).mkString("\n", "\n", "\n")
    }).mkString("\n", "\n", "\n")
    s += "typedef __packed struct {"
    s += c.toStringHfileStruct
    s += "} " + c.name + "Type;\n"

    s += "#define " + c.name.toUpperCase + "_VALUE_LEN sizeof(" + c.name + "Type)\n"
    s
  }).mkString("\n")

  def toStringHfileMemoryStruct(title: String) = {
    var s = "typedef struct {"
    s += lchars.map(_.toStringHfileMemoryStruct).mkString("\n  ", "\n", "\n  ")
    s += title + "ServiceCallBack callback;\n"
    s += "} " + title + "ServiceMemory;\n"
    s += "\n// This function returns the pointer to the global variable containing the characteristics to be written in the flash memory\n"
    s += title + "ServiceMemory* API_" + title + "_get_ble" + title + "Service(void);\n\n"
    s += "/* @return The status of the operation:\n"
    s += " * BLESTATUS_SUCCESS indicates that the operation succeeded.\n"
    s += " * BLESTATUS_FAILED indicates that the operation has failed, merely because " + title + " Service is not registered by any profile.\n*/\n"
    s += lchars.map(_.setFunctionName(title) + ";").mkString("\n", "\n", "\n")
    s += "\n"
    s
  }

  def toStringHfile(title: String) = {
    var s = "#ifndef __" + title.toUpperCase + "_SERVICE_H\n"
    s += "#define __" + title.toUpperCase + "_SERVICE_H\n\n"
    s += "#include \"BleTypes.h\"\n"
    s += "#include \"ATT/att.h\"\n\n"
    s += "#if (" + title.toUpperCase + "_SUPPORT_SERVICE == 1)\n"
    s += "#define BLEINFOTYPE_" + title.toUpperCase + "_READINDEXCONTROLPOINT_CLIENTCONFIG    0xAB\n"
    s += "#define BLEINFOTYPE_" + title.toUpperCase + "_NOTIFY_CLIENTCONFIG                   0xAC\n\n"
    s += toStringHfileDecl
    s += toStringHfileMemoryStruct(title)
    s += "\n#endif // (" + title.toUpperCase + "_SUPPORT_SERVICE == 1)\n"
    s += "#endif // __" + title.toUpperCase + "_SERVICE_H\n"
    s
  }

}

object Utils {
  def firstLetter2LowerCase(s: String) = s.substring(0, 1).toLowerCase + s.substring(1)
}