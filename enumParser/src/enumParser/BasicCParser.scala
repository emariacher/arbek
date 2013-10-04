package enumParser

import scala.util.parsing.combinator.JavaTokenParsers
import kebra.MyLog

class BasicCParser(arg: String) extends JavaTokenParsers {
    val L = MyLog.getMylog
    val parsed = parseAll(cvalue, arg).get

    def getEnums: List[ParsedEnum] = {

        //get enums
        val parsedEnumList = parsed.sliding(4).filter(_.head match {
            case t: Typedef => true
            case _          => false
        }).filter(_.tail.head match {
            case t: Enum => true
            case _       => false
        }).map((quarte: List[Any]) => new ParsedEnum(quarte)).toList

        // process each enum
        parsedEnumList.filter(!_.fields.filter(_.value == Field.invalid).isEmpty).foreach((pe: ParsedEnum) => {
            var current_value = pe.fields.head.value
            if (current_value == Field.invalid) {
                current_value = 0
                pe.fields.head.value = 0
            }
            pe.fields.tail.foreach((f: Field) => {
                if (f.value == Field.invalid) {
                    current_value += 1
                    f.value = current_value
                } else {
                    current_value = f.value
                }
            })
        })
        parsedEnumList
    }

    def getFunctionList: List[ParsedFunction] = {

        //get Functions
        val parsedFunctionList = parsed.sliding(3).filter(_.head match {
            case t: String => t.split(" ").toList.size == 2
            case _         => false
        }).filter(_.tail.head match {
            case t: Parentheses => true
            case _              => false
        }).filter(_.tail.tail.head match {
            case t: Block => true
            case _        => false
        }).map((trio: List[Any]) => new ParsedFunction(trio)).toList

        parsedFunctionList
    }

    /**** Ze entry parser */
    def cvalue: Parser[List[Any]] = rep(cTypedef | cEnum | cPointVirg | cComS | cPar | cVirg | cBlock | """[^{};,()]+""".r) ^^ { t =>
        // drop comments
        val pointVirg = """(;)""".r
        val commentStart = """(CommentS)""".r
        val commentEnd = """(CommentE)""".r
        var drop = false;
        val nocomments = t.filter((s: Any) => {
            s.toString match {
                case pointVirg(a)    => drop = false
                case commentEnd(a)   => drop = false
                case commentStart(a) => drop = true
                case _               =>
            }
            !drop
        })

        // drop ,;
        val virgpointvirg = """([,;]+)""".r
        nocomments.filter((s: Any) => s.toString match {
            case virgpointvirg(a) => false
            case _                => true
        })
    }
    def cPointVirg: Parser[String] = ";" ^^ { t => t.toString }
    def cVirg: Parser[String] = "," ^^ { t => t.toString }
    def cBlock: Parser[Block] = "{" ~ cvalue ~ "}" ^^ { t => new Block(t._1._2) }
    def cPar: Parser[Parentheses] = "(" ~ cvalue ~ ")" ^^ { t => new Parentheses(t._1._2) }
    def cComS: Parser[String] = """/[/\*]+""".r ^^ { t => "CommentS" }
    def cComE: Parser[String] = """\*/""".r ^^ { t => "CommentE" }

    def cTypedef: Parser[Typedef] = "typedef" ^^ { t => new Typedef }
    def cEnum: Parser[Enum] = "enum" ^^ { t => new Enum }
}

case class Typedef
case class Enum
case class Block(lines: List[Any]) { override def toString: String = "Block{\n  "+lines.mkString("\n  ")+"\n  }" }
case class Parentheses(lines: List[Any]) { override def toString: String = "Parentheses( "+lines.mkString(" ")+" )" }

