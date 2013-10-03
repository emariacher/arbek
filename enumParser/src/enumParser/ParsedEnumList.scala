package enumParser

import scala.util.parsing.combinator._
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import kebra.MyLog

class ParsedEnumList(lines: List[String]) extends JavaTokenParsers {
    val L = MyLog.getMylog

    val parsedEnumList = new BasicCParser(lines.mkString(";")).getEnums

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
