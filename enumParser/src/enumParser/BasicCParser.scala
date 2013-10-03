package enumParser

import scala.util.parsing.combinator.JavaTokenParsers

class BasicCParser(arg: String) extends JavaTokenParsers {
    val parsed = parseAll(cvalue, arg).get

    def getEnums: List[ParsedEnum] = {
        var parsedEnumList = List[ParsedEnum]()
        var parsedEnum = new ParsedEnum()

        //get enums
        var isEnum = false
        val typedefEnum = """(typedef enum)""".r
        var lf = parsed.foreach((a: Any) => a match {
            case z: String => z match {
                case typedefEnum(typdef) =>
                    isEnum = true
                    parsedEnum = new ParsedEnum()
                    parsedEnumList = parsedEnumList :+ parsedEnum
                case _ => if (isEnum) {
                    parsedEnum.updateName(z)
                    isEnum = false
                }
            }
            case _ => a.asInstanceOf[List[Any]].foreach(e => parsedEnum :+ new Field(e.toString))
        })

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
        var parsedFunctionList = List[ParsedFunction]()
        var parsedFunction = new ParsedFunction()

        //get Functions
        var isFunction = false
        val typedefEnum = """(typedef enum)""".r

        parsedFunctionList
    }

    /**** Ze entry parser */
    def cvalue: Parser[List[Any]] = rep(cPointVirg | cComS | cPar | cVirg | cBlock | """[^{};,()]+""".r) ^^ { t =>
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
    def cBlock: Parser[Any] = "{" ~ cvalue ~ "}" ^^ { t => t._1._2 }
    def cPar: Parser[Any] = "(" ~ cvalue ~ ")" ^^ { t => t }
    def cComS: Parser[String] = """/[/\*]+""".r ^^ { t => "CommentS" }
    def cComE: Parser[String] = """\*/""".r ^^ { t => "CommentE" }

}