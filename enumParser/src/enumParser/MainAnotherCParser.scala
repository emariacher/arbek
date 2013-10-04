package enumParser

import kebra.MyLog
import kebra.MyFileChooser

object MainAnotherCParser extends App {
    val mfc = new MyFileChooser("TestOutput.txt")
    val f = mfc.justChooseFile("c")
    val L = MyLog.newMyLog(this.getClass.getName, f, "html")
    val source = scala.io.Source.fromFile(f, "utf-8")
    val lines = source.getLines.toList
    source.close

    val parsed = new BasicCParser(lines.mkString(";"))

    L.myPrintDln(parsed.getFunctionList.toString)

    L.closeFiles()
}