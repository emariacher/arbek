package enumParser

import kebra.MyLog
import kebra.MyFileChooser

object MainEnumParser extends App {

    val mfc = new MyFileChooser("TestOutput.txt")
    val f = mfc.justChooseFile("h")
    val L = MyLog.newMyLog(this.getClass.getName, f, "html")
    val source = scala.io.Source.fromFile(f, "utf-8")
    val lines = source.getLines.toList
    source.close

    val parsed = new BasicCParser(lines.mkString(";"))
    L.myPrintDln(parsed.parsed.mkString("\n"))

    val pel = new ParsedEnumList(lines)

    // do some checkings
    pel.getE("TRANSACTION_STATE_T") match {
        case Some(pe) => assert(pe.fields.length == 6)
        case _ => assert(false)
    }
    assert(pel.getF("ERROR_CODE_T_CRC").value == 15)
    assert(pel.getF("STATUS_T_NOT_AVAILABLE").value == 2)
    assert(pel.getF("ERROR_CODE_T_UNKNOWN").value == 12)
    L.myErrPrintDln("checkings OK!")

    L.myErrPrintDln(pel.postProcess("This is some average trace!"))
    L.myErrPrintDln(pel.postProcess("This is a trace where the enum value should be replaced expected: ERROR_CODE_T_CRC vs actual: (ERROR_CODE_T)15 "))
    L.myErrPrintDln(pel.postProcess(" asf (ERROR_CODE_T)343 bds (VNVM_T)17               VNVM_T_RATIO2_FULLCLK_BYPASSENABLED expected"))
    L.myErrPrintDln(pel.postProcess(" asf (CACBOUD)343 zobi la mouche"))
    L.myErrPrintDln(pel.postProcess(" asf (VNVM_T) 17 zobi la mouche"))
    L.myErrPrintDln(pel.postProcess(" asf (VNVM_T)zobi la mouche"))
    L.myErrPrintDln(pel.postProcess(" asf ( VNVM_T )17 la mouche"))
    L.myErrPrintDln(pel.postProcess(" asf () zobi la mouche"))
    L.myErrPrintDln(pel.postProcess(" asf ()zobi la mouche"))
    L.myErrPrintDln(pel.postProcess(" asf ( ) zobi la mouche"))
    L.myErrPrintDln(pel.postProcess(" asf ( )zobi la mouche"))
    
    L.closeFiles()
}
