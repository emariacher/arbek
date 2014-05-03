package enumParser

import scala.util.parsing.combinator._

object Field {
    val invalid = 0xbabac001
}

class Field(line: String) extends JavaTokenParsers {
    var name = ""
    var value = Field.invalid

    /**** process enum field declaration */
    parseAll(ppvalue, line).get

    def ppvalue: Parser[String] = rep(pegal | """[^=]+""".r) ^^ { t =>
        name = t.head.trim
        if (t.size == 3) {
            try {
                value = t.last.trim.toInt
            } catch {
                case e: Exception => throw new Exception("non numeric value: ["+t.last.trim+"] not yet supported")
            }
        }
        t.mkString(" ")
    }

    def pegal: Parser[String] = "="

    override def toString = name+" : "+value
}