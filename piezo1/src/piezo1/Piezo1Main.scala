package piezo1

import kebra._
import kebra.MyLog._
import _root_.scalaSci.math.plot.plotTypes._
import _root_.scalaSci.math.plot.plot._
import _root_.scalaSci.math.plot.canvas._
import java.awt.Color

object Piezo1Main extends App {
    myPrintln("Hello World!")
    val f = (new MyFileChooser("GetArduinoLogs")).justChooseFile("log");
    val s = copyFromFile(f.getCanonicalPath)
    val fig1 = figure()
    title("Hello World1!")
    myPrintln("  fig(" + fig1 + ")")
    val x = List[Double](1, 3, 2, 5)
    val y = List[Double](1, 4, 10, 3)
    linePlotsOn
    plot(x.toArray, y.toArray, Color.red, "yo")
    toFileAndDisplay("zob.html", s)
}