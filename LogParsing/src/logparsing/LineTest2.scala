package logparsing

import scala.util.parsing.combinator.JavaTokenParsers

case class LineTest2(val tpe: TokenType, val line: String) extends JavaTokenParsers {
    val start = Int.MaxValue
    var name: String = _ // ControllerTests.SystemTests.UseCaseMonaural.Fitting
    var result: String = _ // "Error"
    var timeStamp = new TimeStamp // [14:08:04].592
    var timeStampMs = 0 // 14:08:04.[592]
    var msg: String = _ // disabled sniffers due to toggling (AA-00-8E)
    var origin: String = _ // [Domain\Binaination_hlp.c]:1172:
    var originl = 0 // Domain\Binaination_hlp.c:[1172]:
    var side: String = _ // (Left)
    var lctrl = List[String]()
    var traceLevel: String = _ // TRACE
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

    override def toString: String = {
        tpe match {
            case TokenType.TestName => "Test ["+name+"]" // ***** ControllerTests.SystemTests.UseCaseMonaural.Fitting
            case TokenType.TestEnd  => "RESULT ["+result+"]" // Test finished in 00:00:01.1727224 seconds with "Error"
            case TokenType.TestLog  => "Line ["+timeStamp+","+msg+","+side+","+origin+"]" // 14:08:04.592 - TraceLog - TRACE -  UART> (Left) Domain\Binaination_hlp.c:1172: disabled sniffers due to toggling (AA-00-8E)
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

