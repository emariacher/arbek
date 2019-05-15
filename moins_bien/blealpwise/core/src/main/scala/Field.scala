package blealpwise
import kebra.MyLog._

class Field {
  var name = ""
  var size = 0
  var comment = ""
  var lsubfields = List.empty[SubField]
  
  def ParseCsv(lc: List[String]) = {
    myPrintIt(lc)
    if (lc.length > 9) {
      name = lc.apply(4).trim
      size = lc.apply(7) match {
        case Int(x) => x
        case _      => 0
      }
      comment = lc.apply(9).trim
    }
    this
  }

  override def toString = size match {
    case 0 => ""
    case _ => (name, size, comment, lsubfields.mkString("\n    ", "\n    ", "\n    ")).toString
  }

  def addSubFieldCsv(lsub: List[String]) {
    val subf = (new SubField).ParseCsv(lsub)
    if (subf.name.length > 0) {
      lsubfields = lsubfields :+ subf
    }
  }

  def toStringHfileStruct = {
    if (lsubfields.isEmpty) {
      "U" + (size * 8) + " " + Utils.firstLetter2LowerCase(name) + "; // Valid range " + comment + "\n"
    } else {
      Utils.firstLetter2LowerCase(name) + "BitField " + name + "; /*" + lsubfields.map(sub => sub.bitmap + ": " + sub.name)
        .mkString("\n      ", "\n      ", "\n      */\n")
    }
  }
}

object Int {
  def unapply(s: String): Option[Int] = try {
    Some(s.toInt)
  } catch {
    case _: java.lang.NumberFormatException => None
  }
}