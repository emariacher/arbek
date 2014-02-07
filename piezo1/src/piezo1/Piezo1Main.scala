package piezo1
/*
import kebra._
import kebra.MyLog._
import kebra.MyPlotUtils._
import _root_.scalaSci.math.plot.plotTypes._
import _root_.scalaSci.math.plot.plot._
import _root_.scalaSci.math.plot.canvas._
import java.awt.Color

object Piezo1Main extends App {
  val zorgl = (0 to 10).map(z => FFT.Complex(z)).toList
  myPrintIt(FFT.filtre(zorgl, 2, 7))

  var hue = 0.1.toFloat
  val incHue = 1.0.toFloat / 5

  val pz = new Piezo3NoGraphic
  val fig1 = figure()
  title("Hello World1!")
  myPrintln("  fig(" + fig1 + ")")
  draw(pz.ld50k._1, "start")
  draw(pz.filtered, "pulses")
  draw(pz.iffted3, "iffted3")
  draw(pz.iffted11.map(z => (z._1 * 10.0, z._2)), "iffted11")

  def draw(l: List[(Double, Int)], name: String) {
    val y = l.map(_._1)
    val x = l.map(_._2 * pz.periodSample)
    plot(x.toArray, y.toArray, getColor(hue), name)
    hue += incHue
  }

}
*/