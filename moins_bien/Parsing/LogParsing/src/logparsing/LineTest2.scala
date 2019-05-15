package logparsing

import scala.util.parsing.combinator.JavaTokenParsers

case class LineTest2(val tpe: TokenType, val line: String, lt3: List[LineTest2]) extends JavaTokenParsers {
    val start = Int.MaxValue
    var name: String = _ // ControllerTests.SystemTests.UseCaseMonaural.Fitting
    var result: String = _ // "Error"
    var timeStamp = new TimeStamp // [14:08:04].592
    var timeStampMs = 0 // 14:08:04.[592]
    var msg: String = _ // disabled sniffers due to toggling (AA-00-8E)
    var origin: String = _ // [Domain\Binaination_hlp.c]:1172:
    var originl = 0 // Domain\Binaination_hlp.c:[1172]:
    var side: String = "None" // (Left)
    var ttype: String = "None" // DataSlaveToMaster
    var lctrl = List[String]()
    var traceLevel: String = "None" // TRACE
    var duplicate = false // check if part of a common part at the beginning of each test
    tpe match {
        case TokenType.TestName => name = line
        case TokenType.TestEnd  => result = line
        case TokenType.TestLog =>
            parseAll(ztestline, line) // 14:08:04.592 - TraceLog - TRACE -  UART> (Left) Domain\Binaination_hlp.c:1172: disabled sniffers due to toggling (AA-00-8E)
            val traceType: String = lctrl.head // TraceLog
            lctrl = lctrl.tail
            traceLevel = lctrl.head // TRACE
            lctrl = lctrl.tail
            val traceSource: String = lctrl.head // UART
            lctrl = lctrl.tail
            side = if (!lctrl.isEmpty) { // (Left)
                lctrl.head
            } else {
                null
            }
        case TokenType.TestLog2 =>
            parseAll(ztestline2, line) // (Left-SMI) 2483s 252ms 660us   DataSlaveToMaster (HAPI) <-ind-- PowerStateChanged: 0B 0A A0 86 01 04 00 00 00 00 00
            if (!lctrl.isEmpty) {
                ttype = lctrl.head
            }
        case TokenType.TestLog3 =>
            parseAll(ztestline3, line) // (Right-SMI)                                                isDestination = FALSE,
        case _ =>
    }
    System.err.println(toString)

    // this is the test line definition
    def ztestline = ztimeStamp ~ "." ~ ztimeStampMs ~ "-" ~ zword ~ "-" ~ zword ~ "-" ~ zword ~ ">" ~ opt("(" ~ zword ~ ")") ~ opt(zorigin ~ ":" ~ zoriginl ~ ":") ~ zmsg
    def ztimeStamp = """\d\d\:\d\d\:\d\d""".r ^^ { t => timeStamp = new TimeStamp(t) } // [14:08:04].592
    def ztimeStampMs = """\d+""".r ^^ { t => timeStampMs = t.toInt } // 14:08:04.[592]
    def zword = """\w+""".r ^^ { t => lctrl = lctrl :+ t }
    def zorigin = """.+\.c""".r ^^ { t => origin = t } // [Domain\Binaination_hlp.c]:1172:
    def zoriginl = """\d+""".r ^^ { t => originl = t.toInt } // Domain\Binaination_hlp.c:[1172]:
    def zmsg = """[^\\]+""".r ^^ { t => msg = t } // disabled sniffers due to toggling (AA-00-8E)

    // this is a 2nd test line definition
    def ztestline2 = zpar ~ ztimeStamp2 ~ zword ~ opt(zpar2 ~ (rep(zmsg2 | zpar2 | ztime | zword)))
    def zpar = """\(([^\)]+)\)""".r ^^ { t => side = t } // (Left-SMI)
    def zpar2 = """\(([^\)]+)\)""".r ^^ { t => t } // (HAPI)
    def ztime = """(\d+[m|u]*s)""".r ^^ { t => t }
    def ztimeStamp2 = ztime ~ ztime ~ ztime ^^ { t => timeStamp = new TimeStamp(t._1._1+"_"+t._1._2+"_"+t._2) } // 2483s 252ms 749us
    def zmsg2 = """\S+.*""".r ^^ { t => msg = t.toString } // <-conf- SetWss: 02 0A A0 9F BD 00

    // this is a 3rd test line definition
    def ztestline3 = zpar ~ zmsg2

    override def toString: String = {
        tpe match {
            case TokenType.TestName => "Test ["+name+"]" // ***** ControllerTests.SystemTests.UseCaseMonaural.Fitting
            case TokenType.TestEnd  => "RESULT ["+result+"]" // Test finished in 00:00:01.1727224 seconds with "Error"
            case TokenType.TestLog  => "Line1 ["+timeStamp+","+msg+","+side+","+origin+"]" // 14:08:04.592 - TraceLog - TRACE -  UART> (Left) Domain\Binaination_hlp.c:1172: disabled sniffers due to toggling (AA-00-8E)
            case TokenType.TestLog2 => line // (Left-SMI) 2483s 252ms 749us   SmiMasterPushBack
            case TokenType.TestLog3 => line // (Right-SMI)                                                isDestination = FALSE,
            case TokenType.Eof      => "Eof"
        }
    }

    def toStringHtml: String = {
        tpe match {
            case TokenType.TestLog =>
                var s = "<tr class=\"tr"+traceLevel+"\"><td>"+timeStamp+"</td><td>"+timeStampMs+"</td>"+clickableSourceFile
                if (duplicate) {
                    s += "<td class=\"td"+traceLevel+"dup\">"+msg+"</td></tr>" // 14:08:04.592 - TraceLog - TRACE -  UART> (Left) Domain\Binaination_hlp.c:1172: disabled sniffers due to toggling (AA-00-8E)
                } else {
                    s += "<td class=\"td"+traceLevel+"\">"+msg+"</td></tr>" // 14:08:04.592 - TraceLog - TRACE -  UART> (Left) Domain\Binaination_hlp.c:1172: disabled sniffers due to toggling (AA-00-8E)
                }
                s
            case TokenType.TestLog2 =>
                var s = "<tr class=\"trDefault\"><td colspan=\"2\">"+timeStamp+"</td><td/><td/>"
                if (lt3.isEmpty) {
                    s += "<td class=\"tdDefault\">"+ttype+"</td></tr>" // (Left-SMI) 2483s 252ms 749us   SmiMasterPushBack
                } else {
                    s += "<td class=\"tdDefault\">"+ttype+" - "+msg+
                        "</td></tr><td colspan=\"4\" /><td><table border=\"0\"><tr class=\"trDefault\"><td>&nbsp;&nbsp;</td><td class=\"tdDefault\">"+
                        lt3.map(_.msg).mkString(" $ ")+"</td></tr></table></tr>" // (Left-SMI) 2483s 252ms 749us   SmiMasterPushBack
                }
                s
            case _ => ""
        }
    }

    def clickableSourceFile: String = {
        /**
         * firefox open local link to directory with explorer
         *
         * Create new boolean value network.protocol-handler.expose.file and set it to false
         * Create new boolean value network.protocol-handler.external.file and set it to true
         * Click on a link to a local folder.
         * In the following prompt, link to the explorer.exe in: C:\Windows\explorer.exe
         */

        if (origin != null) {
            val stuff = origin.replaceAll("\\\\", "/")
            "<td><a href=\""+stuff+"\">"+stuff+"</a></td><td>"+originl+"</td>"
        } else if (originl != 0) {
            "<td/><td>"+originl+"</td>"
        } else {
            "<td/><td/>"
        }
    }
}

class TimeStamp(val time: String) {
    def this() = this("")
    override def toString = time
    override def equals(arg0: Any): Boolean = toString == arg0.asInstanceOf[TimeStamp].toString
}

