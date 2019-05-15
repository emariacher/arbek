package logparsing

import scala.util.parsing.combinator.JavaTokenParsers
import kebra.MyLog
import kebra.MyLog._

case class LineWsHdr(val tpe: TokenType, val line: String) extends JavaTokenParsers {
  val start = Int.MaxValue
  var index = 0
  var timeStamp = new TimeStamp // [14:08:04].592
  var source: String = _
  var dest: String = _
  var protocol: String = _ // "Error"
  var length = 0
  var info: String = _ // "Error"

  tpe match {
    case TokenType.WsHdr => parseAll(zWsHdr, line)
    case TokenType.HexData => 
    case _                 => throw new Exception("Unhandled TokenType : "+tpe)
  }

  def zWsHdr = zIndex ~ ztimeStamp ~ zSrc ~ zDest ~ zProt ~ zLen ~ zInfo

  //myPrintln(toString+" ["+info+"]")

  def zIndex = """\d+""".r ^^ { t => index = t.toInt } // [14:08:04].592
  def ztimeStamp = """\d+\.\d+""".r ^^ { t => timeStamp = new TimeStamp(t) } // [14:08:04].592
  def zSrc = """\S+""".r ^^ { t => source = t } // 14:08:04.[592]
  def zDest = """\S+""".r ^^ { t => dest = t } // 14:08:04.[592]
  def zProt = """\S+""".r ^^ { t => protocol = t } // 14:08:04.[592]
  def zLen = """\d+""".r ^^ { t => length = t.toInt } // 14:08:04.[592]
  def zInfo = """.+""".r ^^ { t => info = t } // 14:08:04.[592]

  override def toString: String = {
    tpe match {
      case TokenType.Eof     => "Eof"
      case TokenType.WsHdr => "Ecat["+line+"]"
      case TokenType.HexData => "HexData["+line+"]"
      case _                 => "[Here!]"
    }
  }

  def clickableSourceFile: String = {
    /**
     * firefox open local link to directory with explorer
     *
     * Create new boolean value network.protocol-handler.expose.file and set it to false
     * Create new boolean value network.protocol-handler.external.file and set it to true
     * Click on a link to a local folder.
     * In the following prompt, link to the explorer.exe in: C:\Windows\explorer.exe
     */

    /*if (origin != null) {
      val stuff = origin.replaceAll("\\\\", "/")
      "<td><a href=\""+stuff+"\">"+stuff+"</a></td><td>"+originl+"</td>"
    } else if (originl != 0) {
      "<td/><td>"+originl+"</td>"
    } else {
      "<td/><td/>"
    }*/
    ""
  }
}

class TimeStamp(val time: String) {
  def this() = this("")
  override def toString = time
  override def equals(arg0: Any): Boolean = toString == arg0.asInstanceOf[TimeStamp].toString
}

