package blealpwise

import java.io.File
import java.util.Calendar
import java.text.SimpleDateFormat
import kebra.MyFileChooser
import kebra.MyLog._

object BleAlpwiseMain extends App {
  val f = (new MyFileChooser("SavedValues")).justChooseFile("csv");

  val lcharacteristics = ParseCsv(f).filter(!_.lfields.isEmpty)
  myPrintIt(lcharacteristics.mkString("\n"))

  val cout = new CFileOutput(lcharacteristics)

  toFileAndDisplay("tests_status_" + new SimpleDateFormat("ddMMMyy_HH_mm").format(Calendar.getInstance.getTime) + "_h.html",
    "<html><body><pre>" + cout.toStringHfile("TTouch") + "<pre></body></html>")
  toFileAndDisplay("tests_status_" + new SimpleDateFormat("ddMMMyy_HH_mm").format(Calendar.getInstance.getTime) + "_c.html",
    "<html><body><pre>" + cout.toStringCfile("TTouch") + "<pre></body></html>")

  def ParseCsv(f: File): List[Characteristic] = {
    var lfields = List.empty[Field]
    var lcharacteristics = List.empty[Characteristic]
    var name = ""

    for (line <- scala.io.Source.fromFile(f).getLines) {
      val lc = line.split(",").toList
      myPrintIt(lc.length, lc)
      if (lc.length > 7) {
        if (lc.apply(2).trim.length == 0) {
          if (lc.apply(4).trim.length > 0) {
            lfields = lfields :+ new Field(lc)
          }
          if (lc.apply(5).trim.length > 0) {
            lfields.last.addSubField(lc)
          }
        } else {
          if ((name.trim.length > 0) && (!lfields.isEmpty)) {
            lcharacteristics = lcharacteristics :+ new Characteristic(name, lfields.filter(_.size > 0))
          }
          lfields = List.empty[Field]
          name = lc.apply(2)
        }
      }
    }
    lcharacteristics
  }
}
