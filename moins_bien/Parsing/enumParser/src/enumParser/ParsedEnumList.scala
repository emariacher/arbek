package enumParser

import scala.util.parsing.combinator._
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import kebra.MyLog

class ParsedEnumList(lines: List[String]) extends JavaTokenParsers {
    val L = MyLog.getMylog

    val parsedEnumList = getEnums(new BasicCParser(lines.mkString(";")).parsedEnumList)

    def getEnums(parsedEnumList: List[ParsedEnum]): List[ParsedEnum] = {
        // process/clean each enum
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

    def getE(name: String) = parsedEnumList.find(_.name == name)

    def getF(name: String): Field = {
        val a = parsedEnumList.map(_.fields.find(_.name == name))
        a.map(_ match {
            case Some(z) => z
            case _       => null
        }).filter(_ != null).head
    }

    /**** post process traces parser */
    def postProcess(arg: String): String = {
        //MyLog.waiting(1 second)
        L.myPrintDln("\ninput : "+arg)
        parseAll(ppvalue, arg).get
    }

    def ppvalue: Parser[String] = rep(ppmember | """[^()]+""".r) ^^ { t => t.mkString(" ") }

    def ppmember: Parser[String] = {
        val r_tildes = """\s*\~(.+)\~\~(.+)\s*""".r
        "(" ~ ppvalue ~ ")" ~ (floatingPointNumber | ppvalue) ^^ { u =>
            u.toString.replaceAll("[()]", "") match {
                case r_tildes(enumType, value) => {
                    getE(enumType.trim) match {
                        case Some(pe) => try {
                            pe.getN(value.toInt)
                        } catch {
                            case e: Exception => "("+enumType+")"+value.toString
                        }
                        case _ => "("+enumType+")"+value.toString
                    }
                }
                case _ => "() "+u.toString.replaceAll("\\~", "").replaceAll("[()]", "")
            }
        }
    }
}
