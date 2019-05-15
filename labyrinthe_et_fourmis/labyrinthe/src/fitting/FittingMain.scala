package fitting

import labyrinthe.LL._
import kebra.MyLog._
import java.io.File
import statlaby._
import scalation.stat._
import _root_.scalaSci.math.plot.plotTypes._
import _root_.scalaSci.math.plot.plot._
import _root_.scalaSci.math.plot.canvas._
import java.awt.Color
import kebra.MyPlotUtils._
import kebra.LinearRegression

object AvecDesDes extends App {
    newMyLL(this.getClass.getName, new File("out\\cowabunga"), "htm", true)

    new FittingVariate(FittingVariate.lfunc, new RouleLesDes(6, 2, 0).getSerie(100)).plotBest("2Des6_100")
    new FittingVariate(FittingVariate.lfunc, new RouleLesDes(6, 2, 0).getSerie(1000)).plotBest("2Des6_1000")
    new FittingVariate(FittingVariate.lfunc, new RouleLesDes(6, 3, 0).getSerie(100)).plotBest("3Des6_100")
    new FittingVariate(FittingVariate.lfunc, new RouleLesDes(6, 4, 0).getSerie(100)).plotBest("4Des6_100")

}

object MemeDistriMaisSamplesDifferent extends App {
    val i = new Input
    val lrouge = i.ll.head._2.map(_.toDouble)
    val lvert = i.ll.apply(2)._2.map(_.toDouble)
    val (serieX, serieY, variateY) = FittingVariate.getSeries(lrouge, lvert)
    val lr = new LinearRegression(serieY.zip(variateY))
    plot(serieX.toArray, serieY.toArray, Color.red, "rouge " + lr)
    plot(serieX.toArray, variateY.toArray, Color.green, "vert " + lr)
    scatterPlotsOn
    plot(serieX.toArray, serieX.map((d: Double) => 0.0).toArray, Color.green, "X[" + serieX.length + "]")
}

object FittingMain extends App {
    newMyLL(this.getClass.getName, new File("out\\cowabunga"), "htm", true)

    var lRouge = (new Input).ll.head._2.map(_.toDouble)
    //new FittingPoisson(lRouge)

    val fit = new FittingVariate(FittingVariate.lfunc, lRouge)
    fit.plotBest("rouge")

    fitNormal(179.56, 800)

    fitNormal(100, 800) // plus une distribution demarre vite a partir de zero mieux elle fitte

    fitNormal(179.56, 1000) // une autre facon de le dire, c'est qu'il faut que la moyenne doit approximer 3 sigma

    fitPoisson

    def fitNormal(mu: Double, sigma2: Double) {
        val lfunc = FittingVariate.lfunc
        val nbrePoints = 100
        val PointMin = 0.0
        val PointMax = 300.0
        val serieX = serieXDouble(PointMin, PointMax, nbrePoints)
        val func = new Normal(mu, sigma2)
        val serieY = serieZ(serieX, func.pf)

        myPrintIt("\n" + serieY.zipWithIndex.map((di: (Double, Int)) => (0 until (di._1 * 1000).toInt).map((i: Int) => di._2)).mkString("\n", "\n", "\n"))
        val l100 = serieY.zipWithIndex.map((di: (Double, Int)) => (0 until (di._1 * 1000).toInt).map((i: Int) => di._2.toDouble)).flatten
        val fp100 = new FittingVariate(lfunc, l100)
        val bestFit100 = fp100.bestFit
        fp100.plotBest(func.toString)
    }

    def fitPoisson {
        var lfunc = FittingVariate.lfunc

        val nbrePoints = 20
        val PointMin = 0.0
        val PointMax = 20.0
        val serieX = serieXInt(PointMin, PointMax, nbrePoints)
        val func = new Poisson(4.0)
        val serieY = serieZ(serieX, func.pf)

        val l100 = serieY.zipWithIndex.map((di: (Double, Int)) => (0 until (di._1 * 100).toInt).map((i: Int) => di._2.toDouble)).flatten
        val fp100 = new FittingVariate(lfunc, l100)
        val bestFit100 = fp100.bestFit
        fp100.plotBest(func.toString)
    }

}

