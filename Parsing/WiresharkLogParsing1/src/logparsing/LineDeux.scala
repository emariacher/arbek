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

object LineDeux {
  val r_data = """\d\d\d\d  ([ \p{XDigit}]+).+""".r // 0010  0a 00 00 00 00 09 01 80 00 00 00 01 00 0c 02 00   ................
  val r_wsHdr = """\s*(\d+\s+\d+\.\d+\s+.+)""".r // 8368 10.762948000   Beckhoff_15:29:92     Beckhoff_01:00:00 ECAT 65 3 Cmds, 'LRD': len 1, 'LRW': len 10, 'BRD': len 2

  def getTests2(lines: List[String]): List[EcatFrame] = {
    val l2 = getLines2(lines)
    val lt = parse(l2.toIterator).toList
    //println(lt.mkString("\n"))
    myErrPrintln(lt.filter(_.direction == Direction.Rx_Outputs_MS).filter(!_.datagrams.filter(_.coesdo.isGood).isEmpty).mkString("\n"))
    lt
  }

  def getLines2(lines: List[String]): List[LineWsHdr] = {
    var l2 = List.empty[LineWsHdr]
    lines.foreach((si: String) => {
      println(si)
      si match {
        case r_data(s)  => l2 = l2 :+ new LineWsHdr(TokenType.HexData, s)
        case r_wsHdr(s) => l2 = l2 :+ new LineWsHdr(TokenType.WsHdr, s)
        case _          => println("====>["+si+"]")
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
