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
        lines = copyFromFile("log2.log").split("\n").toList.map(s => s.substring(0, s.length - 1))
    }

    val tests = LineDeux.getTests2(lines)
    new DetectBoot(tests)
    toFileAndDisplay("tests_"+new SimpleDateFormat("ddMMMyy_HH_mm").format(Calendar.getInstance.getTime)+"_1.html", toStringHtml1(tests))
    toFileAndDisplay("tests_"+new SimpleDateFormat("ddMMMyy_HH_mm").format(Calendar.getInstance.getTime)+"_2.html", toStringHtml2(tests))
    toFileAndDisplay("tests_"+new SimpleDateFormat("ddMMMyy_HH_mm").format(Calendar.getInstance.getTime)+"_3.html", toStringHtml3(tests))

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

    def toStringHtml3(lt: List[Test2]) = {
        val lFilterNames = List(("C_Files", 2), ("LogMsg", 4))
        var out = "<!DOCTYPE html>\n<html>\n"
        out += "<head>\n<link rel=\"stylesheet\" type=\"text/css\" href=\"http://dojotoolkit.org/reference-guide/1.9/_static/js/dijit/themes/claro/claro.css\" />\n"
        out += "<style type=\"text/css\">body, html { font-family:helvetica,arial,sans-serif; font-size:90%; }</style>\n"
        out += "<script>dojoConfig = {async: true}</script>\n"
        out += "<script src=\"http://ajax.googleapis.com/ajax/libs/dojo/1.9.2/dojo/dojo.js\"></script>\n"
        out += "<script>require([\n"
        out += "\"dojo/_base/window\", \"dojo/store/Memory\",\"dijit/tree/ObjectStoreModel\", \"dijit/Tree\",\"dojo/domReady!\"\n"
        out += "], function(win, Memory, ObjectStoreModel, Tree){\n"
        out += "var myStore = new Memory({\n"
        out += "data: [\n"
        out += "{ \"id\": \"world\", \"name\":\"Tests\", \"type\":\"title\"},\n"
        out += hierarchy3(lt, 0, "world")
        out += "],\n"
        out += copyFromFile("htmlheader3.html")
        out += "\n<title>Ceci est un titre 3</title>\n"
        out += copyFromFile("kebra1.css")
        out += lFilterNames.map(f => stringFilterPart1Javascript(f._1, f._2)).mkString("\n")
        out += getTraceLevelCheckBoxesPart1Javascript(lt)
        out += "</head><body class=\"claro\">\n"
        out += "<p>Here!</p>\n"
        out += stringFilterPart2Html(lFilterNames.map(_._1))
        out += getTraceLevelCheckBoxesPart2Html(lt)
        out += lt.map(_.toStringHtml).mkString("\n")
        out += "</body></html>"
        out
    }

    def stringFilterPart1Javascript(name: String, cellIndexInRow: Int) = {
        var out = "\n<script>\n"
        out += "require([\"dijit/form/TextBox\", \"dojo/domReady!\"], function(TextBox){\n"
        out += "  var "+name+" = new TextBox({ name: \""+name+"\", value: \"\",\n placeHolder: \"type in filtering string 4 "+name+"\", onChange: function(filter){\n"
        out += "    console.log(\"filter"+name+" "+cellIndexInRow+"[\" + filter+\"]\");\n"
        out += "    tr = document.getElementsByTagName(\"tr\");\n"
        out += "    if(filter.length==0) {\n"
        out += "      for (i = 0; i < tr.length; i++) {\n"
        out += "         tr[i].style.display = \"table-row\";\n"
        out += "    }} else {\n"
        out += "      for (i = 0; i < tr.length; i++) {\n"
        out += "         if(tr[i].className.length<2) {\n"
        out += "           tr[i].style.display = \"table-row\";\n"
        out += "         } else {\n"
        out += "           if(tr[i].cells["+cellIndexInRow+"].innerHTML.indexOf(filter)>=0) {\n"
        out += "             tr[i].style.display = \"table-row\";\n"
        out += "           } else {\n"
        out += "             tr[i].style.display = \"none\";\n"
        out += "           }\n"
        out += "         }\n"
        out += "      }\n"
        out += "    }}}, \""+name+"\");\n"
        out += "});</script>\n"
        out
    }

    def stringFilterPart2Html(lFilterNames: List[String]) = {
        var out = "\n<table border= \"1\"><tr>\n"
        lFilterNames.map(tl => {
            out += "  <td><input id=\""+tl+"\" /> <label for=\""+tl+"\">"+tl+"</label></td>\n"
        })
        out += "<td><button>Filter!</button></td></tr></table>\n"
        out
    }

    def getTraceLevelCheckBoxesPart1Javascript(lt: List[Test2]) = {
        var out = "\n<script>\n"
        out += "require([\"dijit/form/CheckBox\", \"dojo/domReady!\"], function(CheckBox){\n"
        lt.flatMap(_.lines.filter(_.traceLevel != "None").map(_.traceLevel)).distinct.map(tl => {
            out += "  var chkbox"+tl+" = new CheckBox({ name: \"chkbox"+tl+"\", checked: true, onChange: function(b){\n"
            out += "    trs = document.getElementsByClassName(\"tr"+tl+"\");\n"
            out += "    if(b) {\n"
            out += "      for (i = 0; i < trs.length; i++) {\n"
            out += "         trs[i].style.display = \"table-row\";\n"
            out += "    }} else {\n"
            out += "      for (i = 0; i < trs.length; i++) {\n"
            out += "         trs[i].style.display = \"none\";\n"
            out += "    }}}}, \"chkbox"+tl+"\");\n"
        })
        out += "});</script>\n"
        out
    }

    def getTraceLevelCheckBoxesPart2Html(lt: List[Test2]) = {
        var out = "\n<table border= \"1\"><tr>\n"
        lt.flatMap(_.lines.filter(_.traceLevel != "None").map(_.traceLevel)).distinct.map(tl => {
            out += "  <td class=\"td"+tl+"\"><input id=\"chkbox"+tl+"\" /> <label for=\"chkbox"+tl+"\">"+tl+"</label></td>\n"
        })
        out += "</tr></table>\n"
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

    def hierarchy3(lt: List[Test2], depth: Int, parent: String): String = {
        val indent = (0 to depth).map(z => "  ").mkString
        lt.groupBy(_.path.apply(depth)).map(z => {
            var s = ""
            if (z._2.size == 1) {
                z._2.map(y => indent+"{ \"type\":\"test"+depth+"\", \"name\":\""+y.path.drop(depth).mkString("_")+"\", \"parent\":\""+parent+"\", \"id\": \""+y.name2+"\"},").mkString("\n")+"\n"
            } else {
                val myId = z._2.head.path.take(depth + 1).mkString("_")
                s += indent+"{ \"id\": \""+myId+"\", \"name\":\""+z._1+"\", \"parent\":\""+parent+"\", \"type\":\"title"+depth+"\"},\n"
                s += hierarchy3(z._2, depth + 1, myId)
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

