package blealpwise
import kebra.MyLog._

class SubField(val lc: List[String]) {
  var name = ""
  var bitmap = ""
  var comment = ""
  var comment2 = ""
  if (lc.length > 9) {
    name = lc.apply(5).trim
    bitmap = lc.apply(8).trim
    comment = lc.apply(9).trim
  }
  if (lc.length > 10) {
    comment2 = lc.apply(10).trim
  }
  myPrintIt(lc, name, bitmap)

  override def toString = (name, bitmap, comment).toString

  /*override def toString = name.length match {
    case 0 => ""
    case _ => (name, bitmap, comment).toString
  }*/

  def toStringHfileBitMap(mainName: String) = {
    var range = (bitmap match {
      case Int(x) => List(x)
      case _ => {
        bitmap.split(":").toList.map(z => z match {
          case Int(x) => x
          case _      => 999
        })
      }
    }).sorted.reverse

    var s = ""
    range.length match {
      case 1 => {
        s += "#define " + (mainName + "_" + range.head + "_" + name + "_" + comment).
          toUpperCase.replaceAll("\\W", "_").replaceAll("_+", "_") + " (0x00)\n"
        if (comment2.length > 0) {
          s += "#define " + (mainName + "_" + range.head + "_" + name + "_" + comment2).
            toUpperCase.replaceAll("\\W", "_").replaceAll("_+", "_") + " (0x01 << " + range.head + ")\n"
        }
      }
      case 2 => s += "#define " + (mainName + "_" + range.head + "_" + range.last + "_" + name + "_MASK").
        toUpperCase.replaceAll("\\W", "_").replaceAll("_+", "_") + "(x) ((x & " + getmask(range) + ") << " + range.last + ")\n"
      case _ => "Check your excel andd/or csv input"
    }
    s
  }

  def getmask(range: List[Int]) = {
    val diff = range.head - range.last
    myAssert2(range.last == range.head, false)
    myAssert2(range.length, 2)
    diff match {
      case 1 => "0x03"
      case 2 => "0x07"
      case 3 => "0x0f"
      case 4 => "0x1f"
      case 5 => "0x3f"
      case 6 => "0x7f"
      case 7 => "0xff"
      case _ => "NOGOOD!_" + diff
    }
  }
}



