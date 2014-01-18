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
    val lines = clipboard.split("\n").toList

    val tests = LineDeux.getTests2(lines)
    new DetectBoot(tests)
    toFileAndDisplay("tests_"+new SimpleDateFormat("ddMMMyy_HH_mm").format(Calendar.getInstance.getTime)+"_1.html", toStringHtml1(tests))
    toFileAndDisplay("tests_"+new SimpleDateFormat("ddMMMyy_HH_mm").format(Calendar.getInstance.getTime)+"_2.html", toStringHtml2(tests))

    def toStringHtml1(lt: List[Test2]) = {
        var out = "<!DOCTYPE html>\n<html>\n"
        out += "<title>Ceci est un titre 1</title>\n"
        out += "<link href=\"http://cdn.kendostatic.com/2013.3.1119/styles/kendo.common.min.css\" rel=\"stylesheet\" />\n"
        out += "<link href=\"http://cdn.kendostatic.com/2013.3.1119/styles/kendo.default.min.css\" rel=\"stylesheet\" />\n"
        out += "<script src=\"http://cdn.kendostatic.com/2013.3.1119/js/jquery.min.js\"></script>\n"
        out += "<script src=\"http://cdn.kendostatic.com/2013.3.1119/js/kendo.all.min.js\"></script>\n"
        out += copyFromFile("kebra1.css")
        out += "</head><body>\n"
        out += "<p>Here!</p>\n"
        out += "\n<div id=\"example\" class=\"k-content\">\n<div id=\"tests\">\n<div id=\"tabstrip\">\n"
        out += lt.map(_.name).mkString("<ul>\n  <li class=\"k-state-active\">", "</li>\n  <li>", "</li>\n</ul>")
        out += lt.map(_.toStringHtml).mkString("\n")
        out += "\n</div>\n</div>\n"
        out += "\n<script>\n"
        out += "  $(document).ready(function() {$(\"#tabstrip\").kendoTabStrip({animation: { open: { effects: \"fadeIn\"}}});});\n"
        out += "</script></div>\n"
        out += "</body></html>"
        out
    }

    def toStringHtml2(lt: List[Test2]) = {
        var out = "<!DOCTYPE html>\n<html>\n"
        out += "<html dir=\"ltr\"><head><link rel=\"stylesheet\" type=\"text/css\" href=\"http://ajax.googleapis.com/ajax/libs/dojo/1.5/dijit/themes/claro/claro.css\" />\n"
        out += "<style type=\"text/css\">body, html { font-family:helvetica,arial,sans-serif; font-size:90%; }</style>\n"
        out += "<title>Ceci est un titre 2</title>\n"
        out += copyFromFile("kebra1.css")
        out += "</head><body>\n"
        out += "<p>Here!</p>\n"
        out += copyFromFile("htmlheader.html")
        out += "var storeData = { \"identifier\": \"tidtoc\", \"label\": \"msg\", \"items\": [\n"
        out += "  { \"type\":\"title\", \"msg\":\"Display_All\", \"tidtoc\": \"Display_All\"},\n"
        out += hierarchy(lt, 0)
        out += "]}\n</script>\n"
        out += lt.map(_.toStringHtml).mkString("\n")
        out += "\n</div>\n</div>\n"
        out += "</body></html>"
        out
    }

    def hierarchy(lt: List[Test2], depth: Int): String = {
        val indent = (0 to depth).map(z => "  ").mkString
        lt.groupBy(_.path.apply(depth)).map(z => {
            var s = ""
            if (z._2.size == 1) {
                z._2.map(y => indent+"{ \"type\":\"title\", \"msg\":\""+y.path.drop(depth).mkString("_")+"\", \"tidtoc\": \""+y.name2+"\"},").mkString("\n")+"\n"
            } else {
                s += indent+"{ \"tidtoc\": \""+z._1+"\", \"msg\":\""+z._1+"\", \"type\":\"title\",\"children\":[\n"
                s += hierarchy(z._2, depth + 1)
                s += indent+"]},\n"
                s
            }
        }).mkString
    }

    def toFileAndDisplay(fileName: String, htmlString: String) {
        val filo = new File(fileName)
        Some(new PrintWriter(filo)).foreach { p => p.write(htmlString); p.close }
        java.awt.Desktop.getDesktop().browse(new java.net.URI("file:///"+filo.getCanonicalPath().replaceAll("\\\\", "/")))
    }

    def copyFromFile(fileName: String): String = Some(scala.io.Source.fromFile(fileName)).map(p => { val s = p.mkString; p.close; s }).mkString
    def copy2File(fileName: String, s: String) = Some(new PrintWriter(fileName)).foreach { p => p.write(s); p.close }
}

//Input
/**
14:08:04.493 -     TraceLog -  INFO -  UART> (Left) Main\main_hlp.c:504: BOOT(0x39): Preparing HLP (AA-01-23)
14:08:04.493 -     TraceLog -  INFO -  UART> (Left) Main\main_hlp.c:507: BOOT(0x3A): Fully booted and ready to communicate (AA-01-24)
**********************************************************************************************************************
***** ControllerTests.SystemTests.UseCaseMonaural.FmGmrToggleButton
**********************************************************************************************************************
15:17:26.115 -     Resetter -  INFO - TALIO> (Left) PowerOn Reset
15:17:26.115 -    JTagJLink - DEBUG - TALIO> (Left) Opening device #0 for WriteMem
14:08:04.592 -     TraceLog - TRACE -  UART> (Left) Domain\BinaeMachine_hlp.c:111: Q_INIT_SIG: bc_qsSm_super (AA-00-CF)
15:17:26.119 -    JTagJLink - ERROR - TALIO> Error on opening J-Link: "Can not connect to J-Link via USB."
Test finished in 00:00:00.3240307 seconds with "Error"
14:08:04.493 -     TraceLog -  INFO -  UART> (Left) Main\main_hlp.c:504: BOOT(0x39): Preparing HLP (AA-01-23)
14:08:04.493 -     TraceLog -  INFO -  UART> (Left) Main\main_hlp.c:507: BOOT(0x3A): Fully booted and ready to communicate (AA-01-24)
* **********************************************************************************************************************
***** ControllerTests.SystemTests.UseCaseMonaural.FmGmrTriggersPriorities
**********************************************************************************************************************
15:17:26.628 -     Resetter -  INFO - TALIO> (Left) Performing safe reset with normal (full) boot 
15:17:26.628 -     Resetter - DEBUG - TALIO> (Left) Reset VdigStd/Mem to default
15:17:26.628 -    JTagJLink - DEBUG - TALIO> (Left) Opening device #0 for WriteMem
14:08:04.592 -     TraceLog - TRACE -  UART> (Left) Domain\BinaeMachine_hlp.c:111: Q_INIT_SIG: bc_qsSm_super (AA-00-CF)
14:08:04.592 -     TraceLog - TRACE -  UART> (Left) Domain\BinaeMachine_hlp.c:313: Q_INIT_S1G: bc_qsSm_super (AA-00-CF)
14:08:04.493 -     TraceLog -  INFO -  UART> (Left) Main\main_hlp.c:504: BOOT(0x39): Preparing HLP (AA-01-23)
15:17:26.628 -    JTagJLink - DEBUG - TALIO> (Left) For device #0, connect to serial number 58004688
15:17:26.633 -    JTagJLink - ERROR - TALIO> Error on opening J-Link: "Can not connect to J-Link via USB."
Test finished in 00:00:00.0095908 seconds with "NOGOOD"
14:08:04.493 -     TraceLog -  INFO -  UART> (Left) Main\main_hlp.c:504: BOOT(0x39): Preparing HLP (AA-01-23)
14:08:04.493 -     TraceLog -  INFO -  UART> (Left) Main\main_hlp.c:507: BOOT(0x3A): Fully booted and ready to communicate (AA-01-24)
**********************************************************************************************************************
***** ControllerTests.SystemTests.ZobiLaMouche.FmGmrToggleButton
**********************************************************************************************************************
15:17:26.115 -     Resetter -  INFO - TALIO> (Left) PowerOn Reset
15:17:26.115 -    JTagJLink - DEBUG - TALIO> (Left) Opening device #0 for WriteMem
15:17:26.119 -    JTagJLink - ERROR - TALIO> Error on opening J-Link: "Can not connect to J-Link via USB."
Test finished in 00:00:00.3240307 seconds with "Error"
14:08:04.493 -     TraceLog -  INFO -  UART> (Left) Main\main_hlp.c:504: BOOT(0x39): Preparing HLP (AA-01-23)
14:08:04.493 -     TraceLog -  INFO -  UART> (Left) Main\main_hlp.c:507: BOOT(0x3A): Fully booted and ready to communicate (AA-01-24)
* **********************************************************************************************************************
***** ControllerTests.Zarbi.UseCaseMonaural.FmGmrTriggersPriorities
**********************************************************************************************************************
15:17:26.628 -     Resetter -  INFO - TALIO> (Left) Performing safe reset with normal (full) boot 
15:17:26.628 -     Resetter - DEBUG - TALIO> (Left) Reset VdigStd/Mem to default
15:17:26.628 -    JTagJLink - DEBUG - TALIO> (Left) Opening device #0 for WriteMem
15:17:26.628 -    JTagJLink - DEBUG - TALIO> (Left) For device #0, connect to serial number 58004688
15:17:26.633 -    JTagJLink - ERROR - TALIO> Error on opening J-Link: "Can not connect to J-Link via USB."
Test finished in 00:00:00.0095908 seconds with "Good"
**********************************************************************************************************************
***** ControllerTests.ZobiLaMouche.FmGmrToggleButton
**********************************************************************************************************************
15:17:26.115 -     Resetter -  INFO - TALIO> (Left) Unique head line
15:17:26.115 -    JTagJLink - DEBUG - TALIO> (Left) Opening device #0 for WriteMem
14:08:04.592 -     TraceLog - TRACE -  UART> (Left) Domain\BinaeMachine_hlp.c:111: Q_INIT_SIG: bc_qsSm_super (AA-00-CF)
15:17:26.119 -    JTagJLink - ERROR - TALIO> Error on opening J-Link: "Can not connect to J-Link via USB."
Test finished in 00:00:00.3240307 seconds with "Error"
14:08:04.493 -     TraceLog -  INFO -  UART> (Left) Main\main_hlp.c:504: BOOT(0x39): Preparing HLP (AA-01-23)
14:08:04.493 -     TraceLog -  INFO -  UART> (Left) Main\main_hlp.c:507: BOOT(0x3A): Fully booted and ready to communicate (AA-01-24)
* */

//Output
/**
Successfull Parsing!
--{ControllerTests.SystemTests.UseCaseMonaural.FmGmrToggleButton: Error, 4}--
--{ControllerTests.SystemTests.UseCaseMonaural.FmGmrTriggersPriorities: NOGOOD, 8}--
--{ControllerTests.SystemTests.ZobiLaMouche.FmGmrToggleButton: Error, 3}--
--{ControllerTests.Zarbi.UseCaseMonaural.FmGmrTriggersPriorities: Good, 5}--
--{ControllerTests.ZobiLaMouche.FmGmrToggleButton: Error, 4}--
*/

