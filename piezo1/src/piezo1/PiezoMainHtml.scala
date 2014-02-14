package piezo1

import kebra._
import kebra.MyLog._

object PiezoMainHtml extends App {
    /*val pz4 = new Piezo4NoGraphic
    pz4.doZeJob*/
    val extensionLogFiles = "log"
    val mfc = new MyFileChooser("GetArduinoLogs")
    val f = mfc.justChooseFile(extensionLogFiles)
    var s = GoogleCharts.htmlHeaderJustZeGraphs
    s += printToday("dd_HH:mm_ss,SSS")

    if (f == null) {
        myPrintIt(mfc.s_directory)
        val lf = new java.io.File(mfc.s_directory).listFiles.filter(_.getName.endsWith(extensionLogFiles)).toList
        myPrintIt(lf.map(_.getName).mkString("\n"))
        //val lpzs = lf.par.map(zf => {
        val lpzs = lf.map(zf => {
            val pzs = new PiezoSlide(RawList.getSamplesAndTick(zf), zf.getName)
            pzs
        })
        val pzl = new PiezoList(lpzs.map(_.lpz).toList.flatten)
        if (!pzl.corrfits.isEmpty) {
            toFileAndDisplay("zob2.html", pzl.nodeChart)
        }
        s += lpzs.map(_.lpz).flatten.map(_.display)
    } else {
        //val pz5 = new Piezo5NoGraphic(RawList.getSamplesAndTick(f), f.getName)
        //pz5.doZeJob
        val pzs = new PiezoSlide(RawList.getSamplesAndTick(f), f.getName)
        val pzl = new PiezoList(pzs.lpz.toList)
        if (!pzl.corrfits.isEmpty) {
            toFileAndDisplay("zob2.html", pzl.nodeChart)
        }
        //s += pz5.display
        s += pzs.lpz.map(_.display)
        toFileAndDisplay("zob4.html",
            D3Charts.lineChart(Pz5.sample(pzs.lpz.head.l.zipWithIndex, pzs.lpz.head.decimation * 2).map(z => (z._2.toString, List(z._1))),
                List("time", pzs.lpz.head.curveName)))
    }
    s += "\n</body></html>"
    //copy2File("zob.html", s)
    toFileAndDisplay("zob.html", s)

}