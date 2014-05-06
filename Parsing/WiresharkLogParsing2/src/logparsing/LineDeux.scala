package logparsing

/**
 * 1 reading a log file line by line, and do some "char" token parsing on some line (JavaTokenParsers). List[String] --> List[LineWsHdr]
 * 2 do some "LineWsHdr" token parsing. List[LineWsHdr] --> List[EcatFrame]
 *  Eric Mariacher 2013
 *  Thanks to seth@tisue.net https://github.com/NetLogo/NetLogo/blob/headless/src/main/org/nlogo/parse/StructureCombinators.scala
 */

import LineDeux._
import scala.util.parsing.input
import java.io.File
import kebra.MyLog
import kebra.MyLog._
import java.util.Calendar
import java.io.PrintWriter

object LineDeux {
  var cptr = 0
  val r_data = """\p{XDigit}\p{XDigit}\p{XDigit}0  ([ \p{XDigit}]+).+""".r // 0010  0a 00 00 00 00 09 01 80 00 00 00 01 00 0c 02 00   ................
  val r_wsHdr = """\s*(\d+\s+\d+\.\d+\s+.+)""".r // 8368 10.762948000   Beckhoff_15:29:92     Beckhoff_01:00:00 ECAT 65 3 Cmds, 'LRD': len 1, 'LRW': len 10, 'BRD': len 2
  var t_start2 = timeStamp("LineDeux")

  def getTests2(lines: Stream[String]) = {
    var t_start = timeStamp("Here1!")
    var hdr = new LineWsHdr(TokenType.HexData, "")
    var hexdata = List.empty[String]
    var sdoDatagramsFromMasterNotNull = List.empty[EcatDatagram]
    var registerMappingRxOutputs = new CoEMappingRegister(new CoERegister)
    var registerMappingTxInputs = new CoEMappingRegister(new CoERegister)
    var csv = ""
    val printWriter = new PrintWriter("ecat.csv")
    lines.foreach(si => {
      si match {
        case r_data(s) => hexdata = hexdata :+ s
        case r_wsHdr(s) => {
          if (!hexdata.isEmpty) {
            // process frame
            val ecf = new EcatFrame(hdr, hexdata)
            if (PdoMappings.pdoMappingCounter == 2) { // now that PDO mapping is known process PDOs
              val pdos = ecf.datagrams.filter(_.isPdo).map(new PDO(_))
              if (!pdos.isEmpty) {
                csv = pdos.map(pdo => {
                  pdo.edg.timestamp + (pdo.edg.direction match {
                    case Direction.Rx_Outputs_MS => pdo.toStringValues(0, registerMappingTxInputs.l.size)
                    case Direction.Tx_Inputs_SM  => pdo.toStringValues(registerMappingRxOutputs.l.size, 0)
                  })
                }).mkString("", "\n", "\n")
                printWriter.append(csv)
              }
              cptr += 1
              if (cptr % 10001 == 0) {
                t_start2 = timeStamp(t_start2, "+ "+ecf.hdr.index)
              }

            } else if ((ecf.direction == Direction.Rx_Outputs_MS) && (!ecf.datagrams.filter(_.coesdo.isGood).isEmpty)) { // know PDO mapping
              val dgs = ecf.datagrams.filter(_.coesdo.data != 0)
              if (!dgs.isEmpty) {
                sdoDatagramsFromMasterNotNull = sdoDatagramsFromMasterNotNull ++ dgs
                val coesdo = dgs.last.coesdo
                myPrintln(coesdo)
                if ((coesdo.index == 0x1C12) || (coesdo.index == 0x1C13)) { // registers 1C12 && 1C13 are re-enabled
                  if ((coesdo.subIndex == 0) && (coesdo.data > 0)) {
                    PdoMappings.pdoMappingCounter += 1
                  }
                  if (PdoMappings.pdoMappingCounter == 2) {
                    timeStampIt(PdoMappings.initMappings(sdoDatagramsFromMasterNotNull))
                    registerMappingRxOutputs = PdoMappings.mappings.getMappingRegister(Direction.Rx_Outputs_MS)
                    registerMappingTxInputs = PdoMappings.mappings.getMappingRegister(Direction.Tx_Inputs_SM)
                    csv = "TimeStamp; "+registerMappingRxOutputs.l.map(o => "0x%04X".format(o._1)).mkString("", "; ", "; ") +
                      registerMappingTxInputs.l.map(i => "0x%04X".format(i._1)).mkString("", "; ", "")
                    printWriter.append(csv)
                    t_start = timeStamp(t_start, "PdoMappings")
                  }
                }
              }
            } else { // No valid PDO mappings found yet
              cptr += 1
              if (cptr % 10001 == 0) {
                t_start2 = timeStamp(t_start2, "_ "+ecf.hdr.index)
              }
            }
            //re-initialize 4 next frame
            hexdata = List.empty[String]
          }
          hdr = new LineWsHdr(TokenType.WsHdr, s)
        }
        case _ => //println("====>["+si+"]")
      }
    })
    t_start = timeStamp(t_start, "processing PDOs")
    //copy2File("ecat.csv", csv)
    printWriter.close
    printTimeStampsList
    if (PdoMappings.pdoMappingCounter == 2) {
      myPrintln("File processed. Have a look to the generated csv file.")
    } else {
      myErrPrintln("File processed. No valid PDO mappings found.")
    }
  }

  def getLines2(lines: Stream[String]): List[LineWsHdr] = {
    var l2 = List.empty[LineWsHdr]
    lines.foreach((si: String) => {
      //println(si)
      cptr += 1
      if (cptr % 1001 == 0) {
        t_start2 = timeStamp(t_start2, ""+l2.last.index)
      }
      si match {
        case r_data(s)  => l2 = l2 :+ new LineWsHdr(TokenType.HexData, s)
        case r_wsHdr(s) => l2 = l2 :+ new LineWsHdr(TokenType.WsHdr, s)
        case _          => //println("====>["+si+"]")
      }
    })
    l2
  }

  def parse(tokens: Iterator[LineWsHdr]): Seq[EcatFrame] = {
    val reader = new SeqReader[LineWsHdr](tokens.toStream, _.start)
    val combinators = new StructureCombinators4
    combinators.frame(reader) match {
      case combinators.Success(frameDecs, _) =>
        println("Successfull Parsing!")
        //Right(testDeclarations)
        frameDecs
      case combinators.NoSuccess(msg, rest) =>
        val token = if (rest.atEnd) {
          if (tokens.hasNext) {
            println("*1* NOGOOD Parsing! ["+msg+"] hasnext")
            tokens.next()
          } else {
            println("*2* NOGOOD Parsing! ["+msg+"] eof")
            new LineWsHdr(TokenType.Eof, "")
          }
        } else {
          println("*3* NOGOOD Parsing! ["+msg+"] - rest.first: ["+rest.first.toString+"]")
          rest.first
        }
        Seq[EcatFrame]()
    }
  }
}

class SeqReader[T](xs: Seq[T], fn: T => Int) extends input.Reader[T] {
  case class Pos(pos: Int) extends input.Position {
    def column = pos
    def line = 0
    def lineContents = ""
  }
  def atEnd = xs.isEmpty
  def first = xs.head
  def rest = new SeqReader(xs.tail, fn)
  def pos =
    if (atEnd) Pos(Int.MaxValue)
    else Pos(fn(xs.head))
}

class StructureCombinators4
    extends scala.util.parsing.combinator.Parsers {

  // specify what kind of input we take
  override type Elem = LineWsHdr

  // top level entry point. output will be a Seq[EcatFrame]
  def frame: Parser[Seq[EcatFrame]] = phrase(rep(frameDec)) ^^ { case decs => decs }
  def tokenType(expected: String, tpe: TokenType): Parser[LineWsHdr] = acceptMatch(expected, { case t @ LineWsHdr(`tpe`, _) => t })
  def lineWsHdr: Parser[LineWsHdr] = tokenType("HDR", TokenType.WsHdr)
  def lineData: Parser[LineWsHdr] = tokenType("DATA", TokenType.HexData)
  // this is the test structure definition
  //def testDeclaration: Parser[EcatFrame] = (lineTestName ~ rep(lineLog | lineLog2 | lineLog3) ~ lineTestEnd) ^^ { t => new EcatFrame(t._1._1.name, t._2.result, t._1._2) }
  def frameDec: Parser[EcatFrame] = (lineWsHdr ~ rep(lineData)) ^^ { t =>
    {
      //myPrintln("frameDec! 1["+t._1+"]\n2["+t._2+"]")
      new EcatFrame(t._1, t._2.map(_.line))
    }
  }

}

abstract sealed trait TokenType

object TokenType {
  case object HexData extends TokenType
  case object WsHdr extends TokenType
  case object Eof extends TokenType
}
