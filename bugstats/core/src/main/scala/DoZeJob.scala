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

class DoZeJob(val dbtype: TTPro) {
  val f = (new MyFileChooser("GetBugsLog")).justChooseFile("xml");

  val bugs = dbtype.ParseXml(f)
  printIt(bugs.mkString("\n"))

  val today = Calendar.getInstance
  val latestActivityInInputFile = bugs.map(_.h.last.c).sorted.last
  myPrintln("latestActivityInInputFile: " + printZisday(latestActivityInInputFile, "dMMMyyyy"))
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
  myPrintln("\nendDate: " + printZisday(endDate, "dMMMyyyy"))

  val projets = bugs.map(_.product).distinct.map(new Projet(_, bugs, endDate, dbtype.rangeeMaitresse)).filter(_.isAlive).sortBy(_.activity).reverse
  
  dbtype.doZeHtml(endDate, projets, bugs)
}
