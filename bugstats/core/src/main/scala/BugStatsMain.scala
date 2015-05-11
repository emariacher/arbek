package bugstats

import java.io.File
import java.util.Calendar
import scala.swing.TextField
import kebra.DateDSL._
import kebra.MyLog._
import kebra._
import kebra.MyLog
import kebra.MyFileChooser
import language.postfixOps
import java.text.SimpleDateFormat

object BugStatsMain extends App {
    val f = (new MyFileChooser("GetBugsLog")).justChooseFile("xml");

    val bugs = (new TTPro).ParseXml(f)
    //println(bugs.mkString("\n"))

    val today = Calendar.getInstance
    val latestActivityInInputFile = bugs.map(_.h.last.c).sorted.last
    myPrintln("latestActivityInInputFile: "+printZisday(latestActivityInInputFile, "dMMMyyyy"))
    var endDate = today
    if ((now is latestActivityInInputFile).before(Today minus 3 days)) {
        /*val L = MyLog.newMyLog(this.getClass.getName, f, "htm")
        //L.createGui(List(("endDate dd/MM/yy",printZisday(latestActivityInInputFile,"dd/MM/yy"),new TextField)))
        L.createGui(new ZeParameters(List((
            "endDate dd/MM/yy", new MyParameter(printZisday(latestActivityInInputFile, "dd/MM/yy"),
                printZisday(latestActivityInInputFile, "dd/MM/yy"), new TextField)))))
        val z = L.Gui.getAndClose.head._2.value
        endDate = MyLog.ParseDate(z, "dd/MM/yy")*/
        endDate = latestActivityInInputFile
    }

    endDate.clear(Calendar.HOUR)
    myPrintln("\nendDate: "+printZisday(endDate, "dMMMyyyy"))

    val projets = bugs.map(_.product).distinct.map(new Projet(_, bugs, endDate))

    var s = "<html><head>"+ChartsGoogle.htmlHeaderJustZeGraphs+"\n<title>"+printZisday(endDate, "dMMMyyyy")+" bug trends</title></head><body>\n"

    s += "\n<h2>Active Projects at "+printZisday(endDate, "dMMMyyyy")+"</h2>\n"+ChartsGoogle.columnChart("ActiveProjects",
        projets.filter(_.isAlive).map(p => (p.p, p.rangees.last.r)),
        Projet.rangeeMaitresse.map(c => c._1+" "+c._2))

    s += "\n<h2>Word Cloud</h2>\n"+ChartsGoogle.wordCloud(bugs.map(b => (b.product, b.summary)), "Word Cloud")

    s += projets.map(p => {
        "\n<h3>"+p.p+"</h3>\n"+
            ChartsGoogle.stackedCurve(p.p, p.rangees.map(r => {
                (r.c2string, r.r)
            }), Projet.rangeeMaitresse.map(c => c._1+" "+c._2))
    }).mkString("")

    s += "</body></html>"

    toFileAndDisplay("tests_status_"+new SimpleDateFormat("ddMMMyy_HH_mm").format(Calendar.getInstance.getTime)+".html", s)

    var sjson = "{ \"name\": \"flare\", \"children\":\n"
    val projectsbyType = projets.groupBy(p => p.p.substring(0, 2))
    myPrintDln(projectsbyType.map(z => z._1+" "+z._2.size))
    sjson += projectsbyType.map(z => {
        "{\"name\": \""+z._1+"\", \"children\":\n"+z._2.map(p => p.json(Today)).mkString("  [\n    ",",\n    ","\n  ]\n  }")
    }).mkString("[\n  ",",\n  ","\n]\n}")
    copy2File("flare.json",sjson)
    display(new File("treemap.html"))

}
