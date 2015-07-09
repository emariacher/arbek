package blealpwise
import kebra.MyLog._

class Field(val lc: List[String]) {
  var name = ""
  var size = 0
  var comment = ""
  var lsubfields = List.empty[SubField]
  myPrintIt(lc)
  if (lc.length > 9) {
    name = lc.apply(4).trim
    size = lc.apply(7) match {
      case Int(x) => x
      case _      => 0
    }
    comment = lc.apply(9).trim
  }

  override def toString = size match {
    case 0 => ""
    case _ => (name, size, comment, lsubfields.mkString("\n    ", "\n    ", "\n    ")).toString
  }

  def addSubField(lsub: List[String]) {
    val sub = new SubField(lsub)
    if (sub.name.length > 0) {
      lsubfields = lsubfields :+ new SubField(lsub)
    }
  }

  def toStringHfileStruct = {
    if (lsubfields.isEmpty) {
      "U" + (size * 8) + " " + Utils.firstLetter2LowerCase(name) + "; // Valid range " + comment + "\n"
    } else {
      name + "BitField " + name + "; /*" + lsubfields.map(sub => sub.bitmap + ": " + sub.name)
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