package fastlogsparsers
/**
 * 1 reading a log file line by line, and do some "char" token parsing on some line (JavaTokenParsers). List[String] --> List[LineTest2]
 * 2 do some "LineTest2" token parsing. List[LineTest2] --> List[Test2]
 *  Eric Mariacher 2013
 *  Thanks to seth@tisue.net https://github.com/NetLogo/NetLogo/blob/headless/src/main/org/nlogo/parse/StructureCombinators.scala
 */

import scala.util.parsing.combinator.JavaTokenParsers
import LineDeux._

case class LineTest2(val tpe: TokenType, val line: String) extends JavaTokenParsers {
    val start = Int.MaxValue
    var name: String = _ // ControllerTests.SystemTests.UseCaseMonaural.Fitting
    var result: String = _ // "Error"
    var timeStamp = new TimeStamp // 14:08:04.592
    var msg: String = _ // disabled sniffers due to toggling (AA-00-8E)
    var origin: String = _ // Domain\Binaination_hlp.c:1172:
    var side: String = _ // (Left)
    var lctrl = List[String]()
    tpe match {
        case TokenType.TestName => name = line
        case TokenType.TestEnd  => result = line
        case TokenType.TestLog =>
            parseAll(ztestline, line) // 14:08:04.592 - TraceLog - TRACE -  UART> (Left) Domain\Binaination_hlp.c:1172: disabled sniffers due to toggling (AA-00-8E)
            val traceType: String = lctrl.head // TraceLog
            lctrl = lctrl.tail
            val traceLevel: String = lctrl.head // TRACE
            lctrl = lctrl.tail
            val traceSource: String = lctrl.head // UART
            lctrl = lctrl.tail
            side = if (!lctrl.isEmpty) { // (Left)
                lctrl.head
            } else {
                null
            }
        case _ =>
    }
    System.err.println(toString)

    def ztestline = ztimeStamp ~ "-" ~ zword ~ "-" ~ zword ~ "-" ~ zword ~ ">" ~ opt("(" ~ zword ~ ")") ~ opt(zorigin) ~ zmsg
    def ztimeStamp = """\d\d\:\d\d\:\d\d\.\d+""".r ^^ { t => timeStamp = new TimeStamp(t) } // 14:08:04.592
    def zword = """\w+""".r ^^ { t => lctrl = lctrl :+ t }
    def zorigin = """.+\.c\:\d+\:""".r ^^ { t => origin = t } // Domain\Binaination_hlp.c:1172:
    def zmsg = """[^\\]+""".r ^^ { t => msg = t } // disabled sniffers due to toggling (AA-00-8E)

    override def toString: String = {
        tpe match {
            case TokenType.TestName => "Test ["+name+"]" // ***** ControllerTests.SystemTests.UseCaseMonaural.Fitting
            case TokenType.TestEnd  => "RESULT ["+result+"]" // Test finished in 00:00:01.1727224 seconds with "Error"
            case TokenType.TestLog  => "Line ["+timeStamp+","+msg+","+side+","+origin+"]" // 14:08:04.592 - TraceLog - TRACE -  UART> (Left) Domain\Binaination_hlp.c:1172: disabled sniffers due to toggling (AA-00-8E)
            case TokenType.Eof      => "Eof"
        }
    }
}

object LineDeux {
    val r_digit = """(\d.+)""".r
    val r_alpha = """(\p{Alpha}.+)""".r
    val r_test = """\*\*\*\*\* (.+)""".r // ***** ControllerTests.SystemTests.UseCaseMonaural.Fitting

    val r_timeStamp = """(\d\d\:\d\d\:\d\d)\.(\d+)""".r // 15:17:25.168 -    JTagJLink - DEBUG - TALIO> (Left) Opening device #0 for WriteMem
    val r_testEnd = """Test [^\"]+\"(\w+)\"""".r // Test finished in 00:00:00.0078374 seconds with "Error"

    def getTests2(lines: List[String]): List[LineTest2] = {
        val l2 = getLines2(lines)
        val lt = parse(l2.toIterator)
        println(lt.filter(!_.isVoid).toList.mkString("\n"))
        l2
    }

    def getLines2(lines: List[String]): List[LineTest2] = {
        var l2 = List.empty[LineTest2]
        lines.foreach((si: String) => {
            println(si)
            si match {
                case r_test(n)    => l2 = l2 :+ new LineTest2(TokenType.TestName, n)
                case r_digit(s)   => l2 = l2 :+ new LineTest2(TokenType.TestLog, s)
                case r_testEnd(r) => l2 = l2 :+ new LineTest2(TokenType.TestEnd, r)
                case _            =>
            }
        })
        l2
    }

    // def parse(tokens: Iterator[LineTest2]): Either[(String, LineTest2), Seq[Test2]] = { right and left
    def parse(tokens: Iterator[LineTest2]): Seq[Test2] = {
        val reader = new SeqReader[LineTest2](tokens.toStream, _.start)
        val combinators = new StructureCombinators4
        combinators.program(reader) match {
            case combinators.Success(testDeclarations, _) =>
                println("Successfull Parsing!")
                //Right(testDeclarations)
                testDeclarations
            case combinators.NoSuccess(msg, rest) =>
                val token = if (rest.atEnd) {
                    if (tokens.hasNext) {
                        println("*1* NOGOOD Parsing! ["+msg+"] hasnext")
                        tokens.next()
                    } else {
                        println("*2* NOGOOD Parsing! ["+msg+"] eof")
                        new LineTest2(TokenType.Eof, "")
                    }
                } else {
                    println("*3* NOGOOD Parsing! ["+msg+"] - rest.first: ["+rest.first.toString+"]")
                    rest.first
                }
                //Left((msg, token))
                Seq[Test2]()
        }
    }
}

class TimeStamp(val time: String) {
    def this() = this("")
    override def toString = time
}

import scala.util.parsing.input

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
    override type Elem = LineTest2

    // top level entry point. output will be a Seq[Test2]
    def program: Parser[Seq[Test2]] = phrase(rep(testDeclaration | linelog)) ^^ { case decs => decs }
    def tokenType(expected: String, tpe: TokenType): Parser[LineTest2] = acceptMatch(expected, { case t @ LineTest2(`tpe`, _) => t })
    def lineTestName: Parser[LineTest2] = tokenType("NAME", TokenType.TestName)
    def lineLog: Parser[LineTest2] = tokenType("LOG", TokenType.TestLog)
    def lineTestEnd: Parser[LineTest2] = tokenType("END", TokenType.TestEnd)
    def linelog: Parser[Test2] = lineLog ^^ { t => new Test2("", "",0) }
    def testDeclaration: Parser[Test2] = (lineTestName ~ rep(lineLog) ~ lineTestEnd) ^^ { t => new Test2(t._1._1.name, t._2.result, t._1._2.length) }
}

class Test2(val name: String, val result: String, val size: Int) {
    override def toString: String = "--{"+name+": "+result+", "+size+"}--"

    def isVoid = size==0
}

abstract sealed trait TokenType

object TokenType {
    case object TestName extends TokenType
    case object TestLog extends TokenType
    case object TestEnd extends TokenType
    case object Eof extends TokenType
}

//Input
/**
14:08:04.493 -     TraceLog -  INFO -  UART> (Left) Main\main_hlp.c:504: BOOT(0x39): Preparing HLP (AA-01-23)
14:08:04.493 -     TraceLog -  INFO -  UART> (Left) Main\main_hlp.c:507: BOOT(0x3A): Fully booted and ready to communicate (AA-01-24)
**********************************************************************************************************************
***** ControllerTests.SystemTests.UseCaseMonaural.FmGmrToggleButton
**********************************************************************************************************************
15:17:26.115 -     Resetter -  INFO - TALIO> (Left) PowerOn Reset
15:17:26.115 -    JTagJLink - DEBUG - TALIO> (Left) Opening device #0 for WriteMem
14:08:04.592 -     TraceLog - TRACE -  UART> (Left) Domain\BinaeMachine_hlp.c:111: Q_INIT_SIG: bc_qsSm_super (AA-00-CF)
15:17:26.119 -    JTagJLink - ERROR - TALIO> Error on opening J-Link: "Can not connect to J-Link via USB."
Test finished in 00:00:00.3240307 seconds with "Error"
14:08:04.493 -     TraceLog -  INFO -  UART> (Left) Main\main_hlp.c:504: BOOT(0x39): Preparing HLP (AA-01-23)
14:08:04.493 -     TraceLog -  INFO -  UART> (Left) Main\main_hlp.c:507: BOOT(0x3A): Fully booted and ready to communicate (AA-01-24)
* **********************************************************************************************************************
***** ControllerTests.SystemTests.UseCaseMonaural.FmGmrTriggersPriorities
**********************************************************************************************************************
15:17:26.628 -     Resetter -  INFO - TALIO> (Left) Performing safe reset with normal (full) boot 
15:17:26.628 -     Resetter - DEBUG - TALIO> (Left) Reset VdigStd/Mem to default
15:17:26.628 -    JTagJLink - DEBUG - TALIO> (Left) Opening device #0 for WriteMem
15:17:26.628 -    JTagJLink - DEBUG - TALIO> (Left) For device #0, connect to serial number 58004688
15:17:26.633 -    JTagJLink - ERROR - TALIO> Error on opening J-Link: "Can not connect to J-Link via USB."
Test finished in 00:00:00.0095908 seconds with "Error"
*/

//Output
/**
Successfull Parsing!
--{ControllerTests.SystemTests.UseCaseMonaural.FmGmrToggleButton: Error, 4}--
--{ControllerTests.SystemTests.UseCaseMonaural.FmGmrTriggersPriorities: Error, 5}--
*/

