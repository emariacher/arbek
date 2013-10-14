package enumParser

import scala.util.parsing.combinator.JavaTokenParsers
import scala.util.parsing.combinator.lexical._
import scala.util.parsing.combinator.syntactical._
import kebra._

class BasicCParser(arg: String) extends JavaTokenParsers {
  val L = MyLog.getMylog
  var parsedEnumList = List[ParsedEnum]()
  var parsedExecBlockList = List[ParsedExecBlock]()
  val parsed = parseAll(cvalue, arg).get

    /**** Ze entry parser */
    def cvalue: Parser[List[Any]] = rep(cTypDefEnum | cExecBlock | cTypedef | cEnum |
        cPointVirg | cComS | cComE | cPar | cVirg | cBlock |
        cOther) ^^ { t =>
        // drop comments
        var drop = false;
        val nocomments = t.filter((s: Any) => {
            s match {
                case c: PointVirg    => drop = false
                case c: CommentEnd   => drop = false
                case c: CommentStart => drop = true
                case _               =>
            }
            !drop
        })

        // drop ,;
        nocomments.filter(_ match {
            case c: PointVirg => false
            case c: Virg      => false
            case _            => true
        })
    }
    def cPointVirg: Parser[PointVirg] = ";" ^^ { t => new PointVirg }
    def cVirg: Parser[Virg] = "," ^^ { t => new Virg }
    def cBlock: Parser[Block] = "{" ~> cvalue <~ "}" ^^ { t => new Block(t) }
    def cPar: Parser[Parentheses] = "(" ~> cvalue <~ ")" ^^ { t => new Parentheses(t) }
    def cComS: Parser[CommentStart] = """/[/\*]+""".r ^^ { t => new CommentStart }
    def cComE: Parser[CommentEnd] = """\*/""".r ^^ { t => new CommentEnd }
    def cTypedef: Parser[Typedef] = "typedef" ^^ { t => new Typedef }
    def cEnum: Parser[Enum] = "enum" ^^ { t => new Enum }
    def cTypDefEnum: Parser[Any] = cTypedef ~ cEnum ~ cPointVirg ~> cBlock ~ cOther ^^ {
        case cBlock ~ cOther => parsedEnumList = parsedEnumList :+ new ParsedEnum(cOther.toString, cBlock)
    }
    def cOther: Parser[Any] = """[^{};,()]+""".r ^^ { t => t }
    def cExecBlock: Parser[Any] = cOther ~ cPar ~ cPointVirg ~ cBlock ^^ {
        case cOther ~ cPar ~ cPointVirg ~ cBlock => parsedExecBlockList = parsedExecBlockList :+ new ParsedExecBlock(cOther.toString, cPar, cBlock)
    }
}

case class Typedef()
case class Enum()
case class CommentStart()
case class CommentEnd()
case class PointVirg()
case class Virg()
case class Block(val lines: List[Any]) { override def toString: String = "Block{\n  "+lines.mkString("\n  ")+"\n  }" }
case class Parentheses(val lines: List[Any]) { override def toString: String = "Parentheses( "+lines.mkString(" ")+" )" }


