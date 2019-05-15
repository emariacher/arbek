package logparsing

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.io.FileOutputStream
import java.io.FileInputStream
import java.io.PrintWriter

object MainLogParsing extends App {
    //val L = MyLog.L
    println("Hello World!")

    val clipboard = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString
    var lines = clipboard.split("\n").toList

    if (lines.length < 10) {
        lines = copyFromFile("log3.log").split("\n").toList.map(s => s.substring(0, s.length - 1))
    }

    val tests = LineDeux.getTests2(lines)

    def toFileAndDisplay(fileName: String, htmlString: String) {
        val filo = new File(fileName)
        Some(new PrintWriter(filo)).foreach { p => p.write(htmlString); p.close }
        java.awt.Desktop.getDesktop().browse(new java.net.URI("file:///"+filo.getCanonicalPath().replaceAll("\\\\", "/")))
    }

    def copyFromFile(fileName: String): String = Some(scala.io.Source.fromFile(fileName)).map(p => { val s = p.mkString; p.close; s }).mkString
    def copy2File(fileName: String, s: String) = Some(new PrintWriter(fileName)).foreach { p => p.write(s); p.close }
}

