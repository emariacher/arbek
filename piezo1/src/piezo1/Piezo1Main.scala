package piezo1

import kebra._
import kebra.MyLog._
import kebra.MyPlotUtils._
import _root_.scalaSci.math.plot.plotTypes._
import _root_.scalaSci.math.plot.plot._
import _root_.scalaSci.math.plot.canvas._
import java.awt.Color
import scala.concurrent.duration.Duration

object Piezo1Main extends App {
    val zorgl = (0 to 10).map(z => FFT.Complex(z)).toList
    myPrintIt(FFT.filtre(zorgl, 2, 7))

    var hue = 0.1.toFloat
    val incHue = 1.0.toFloat / 5

    //val pz = new Piezo3NoGraphic
    val fig1 = figure()
    title("Hello World1!")
    myPrintln("  fig(" + fig1 + ")")
    /*draw(pz.ld50k._1, "start")
  draw(pz.filtered, "pulses")
  draw(pz.iffted3, "iffted3")
  draw(pz.iffted11.map(z => (z._1 * 10.0, z._2)), "iffted11")*/

    val pz4 = new Piezo4NoGraphic

    draw(pz4.ldecimed.map(c => (c._1, c._2 * pz4.ttick)), "decimed")
    draw(pz4.iffted8.map(c => ((c._1 * 4).toDouble, (c._2 * pz4.decimation * pz4.ttick))), "filtered1")
    draw(pz4.iffted9.map(c => (((c._1 * 4) + 150).toDouble, c._2 * pz4.decimation * pz4.ttick)), "filtered2")
    scatterPlotsOn
    draw(pz4.pics.goodSampleRoots.map(c => (c._2.toDouble/10.0, c._1 * pz4.ttick)), "goodSampleRoots")

    def draw(l: List[(Double, Duration)], name: String) {
        val y = l.map(_._1)
        val x = l.map(_._2.toMillis.toDouble)
        plot(x.toArray, y.toArray, getColor(hue), name)
        hue += incHue
    }

}
