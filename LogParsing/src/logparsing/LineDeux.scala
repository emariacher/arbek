package logparsing

/**
 * 1 reading a log file line by line, and do some "char" token parsing on some line (JavaTokenParsers). List[String] --> List[LineTest2]
 * 2 do some "LineTest2" token parsing. List[LineTest2] --> List[Test2]
 *  Eric Mariacher 2013
 *  Thanks to seth@tisue.net https://github.com/NetLogo/NetLogo/blob/headless/src/main/org/nlogo/parse/StructureCombinators.scala
 */

import LineDeux._

import scala.util.parsing.input
import java.io.File

object LineDeux {
    val r_digit = """(\d+:.+)""".r
    val r_test = """\*\*\*\*\* (.+)""".r // ***** ControllerTests.SystemTests.UseCaseMonaural.Fitting
    val r_testEnd = """Test [^\"]+\"(\w+)\"""".r // Test finished in 00:00:00.0078374 seconds with "Error"
    val r_par2 = """\s*(\(.+\)\s*\d.+)""".r // (Left-SMI) 2526s 740ms 683us   AdapterSyncd
    val r_par3 = """\s*(\(.+\)\s*\p{Alpha}.+)""".r // (Left-SMI) assertCode = NONE, 

    def getTests2(lines: List[String]): List[Test2] = {
        val l2 = getLines2(lines)
        val lt = parse(l2.toIterator).filter(!_.isVoid).toList
        println(lt.mkString("\n"))
        lt
    }

    def getLines2(lines: List[String]): List[LineTest2] = {
        var l2 = List.empty[LineTest2]
        lines.foreach((si: String) => {
            println(si)
            si match {
                case r_test(n)    => l2 = l2 :+ new LineTest2(TokenType.TestName, n, List.empty[LineTest2])
                case r_digit(s)   => l2 = l2 :+ new LineTest2(TokenType.TestLog, s, List.empty[LineTest2])
                case r_par2(s)    => l2 = l2 :+ new LineTest2(TokenType.TestLog2, s, List.empty[LineTest2])
                case r_par3(s)    => l2 = l2 :+ new LineTest2(TokenType.TestLog3, s, List.empty[LineTest2])
                case r_testEnd(r) => l2 = l2 :+ new LineTest2(TokenType.TestEnd, r, List.empty[LineTest2])
                case _            => println("====>["+si+"]")
            }
        })
        l2
    }

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
                        new LineTest2(TokenType.Eof, "", List.empty[LineTest2])
                    }
                } else {
                    println("*3* NOGOOD Parsing! ["+msg+"] - rest.first: ["+rest.first.toString+"]")
                    rest.first
                }
                Seq[Test2]()
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
    override type Elem = LineTest2

    // top level entry point. output will be a Seq[Test2]
    def program: Parser[Seq[Test2]] = phrase(rep(testDeclaration | line_log)) ^^ { case decs => decs }
    def tokenType(expected: String, tpe: TokenType): Parser[LineTest2] = acceptMatch(expected, { case t @ LineTest2(`tpe`, _, _) => t })
    def lineTestName: Parser[LineTest2] = tokenType("NAME", TokenType.TestName)
    def lineLog: Parser[LineTest2] = tokenType("LOG", TokenType.TestLog)
    def lineLog2: Parser[LineTest2] = tokenType("LOG", TokenType.TestLog2) ~ opt(rep(lineLog3)) ^^ { t => new LineTest2(TokenType.TestLog2, t._1.toString, t._2 match { case Some(l) => l }) }
    def lineLog3: Parser[LineTest2] = tokenType("LOG", TokenType.TestLog3)
    def lineTestEnd: Parser[LineTest2] = tokenType("END", TokenType.TestEnd)
    def line_log: Parser[Test2] = lineLog ^^ { t => new Test2("", "", List.empty[LineTest2]) }
    // this is the test structure definition
    //def testDeclaration: Parser[Test2] = (lineTestName ~ rep(lineLog | lineLog2 | lineLog3) ~ lineTestEnd) ^^ { t => new Test2(t._1._1.name, t._2.result, t._1._2) }
    def testDeclaration: Parser[Test2] = (lineTestName ~ rep(lineLog | lineLog2 | lineLog3) ~ opt(lineTestEnd)) ^^ { t =>
        {
            val result = t._2 match {
                case Some(z) => z.result
                case _       => "NoGood1"
            }
            val logs = t._1._2 /*match {
                case Some(z) => {
                    System.err.println("Here! ["+t._1._1.name+"] ["+z+"]")
                    z
                }
                case _ => {
                    System.err.println("There! ["+t._1._1.name+"]")
                    List.empty[LineTest2]
                }
            }*/
            System.err.println("Here! ["+t._1._1.name+"] ["+result+"] ["+logs+"] ["+t+"]")
            new Test2(t._1._1.name, result, logs)
        }
    }

}

abstract sealed trait TokenType

object TokenType {
    case object TestName extends TokenType
    case object TestLog extends TokenType
    case object TestLog2 extends TokenType
    case object TestLog3 extends TokenType
    case object TestEnd extends TokenType
    case object Eof extends TokenType
}
