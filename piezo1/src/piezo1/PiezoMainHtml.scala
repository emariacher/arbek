package piezo1

import kebra._
import kebra.MyLog._
import reflect.runtime.universe.Literal
import reflect.runtime.universe.Constant
import language.experimental.macros
import reflect.macros.Context
import scala.annotation.StaticAnnotation
import scala.reflect.runtime.{ universe => ru }

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
        toFileAndDisplay("zob2.html", pzl.nodeChart)
        s += lpzs.map(_.lpz).flatten.map(_.display)
    } else {
        //val pz5 = new Piezo5NoGraphic(RawList.getSamplesAndTick(f), f.getName)
        //pz5.doZeJob
        val pzs = new PiezoSlide(RawList.getSamplesAndTick(f), f.getName)
        val pzl = new PiezoList(pzs.lpz.toList)
        toFileAndDisplay("zob2.html", pzl.nodeChart)
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

object Zobi2 extends App {
    val nya = List(Literal(Constant(2)))
    myPrintIt(nya)
    //debug(nya)
    val l = "zob"
    val kol = 345
    var zub = List("2", 89)
    val zubi = List(zub, l, kol)

    printIt(l)
    myPrintIt(l, kol, (l, zub), zubi)
    val nyb = List(2)
    val nyc = List(Constant(2))

    myPrintIt(nyb)
    myPrintIt(nyc)
    myPrintIt(3)
    myAssert2(zubi.last.asInstanceOf[Int], kol + 2)
    myAssert(zubi.last.asInstanceOf[Int] == kol)
}