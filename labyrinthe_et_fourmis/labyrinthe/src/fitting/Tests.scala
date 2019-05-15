package fitting

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import labyrinthe.LL._
import kebra.MyLog._
import java.io.File
import statlaby._
import scalation.stat._
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import kebra.MyPlotUtils._
import kebra.LinearRegression

// scala org.scalatest.tools.Runner -o -s fitting.FittingTest

class FittingTest extends FunSuite with ShouldMatchers {
    newMyLL(this.getClass.getName, new File("out\\cowabunga"), "htm", true)

    test("Poisson best fits Poisson") {
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
        myPrintIt(bestFit100)
        bestFit100._1.mean should equal(4.0)
        bestFit100._1.toString should equal("scalation.stat.Poisson(4.000)")
        math.abs(bestFit100._2.R2 - 1.0) < 0.01 should be(true)
        math.abs(bestFit100._2.beta1 - 1.0) < 0.02 should be(true)

        val l50 = serieY.zipWithIndex.map((di: (Double, Int)) => (0 until (di._1 * 50).toInt).map((i: Int) => di._2.toDouble)).flatten
        val fp50 = new FittingVariate(lfunc, l50)
        val bestFit50 = fp50.llr.head
        myPrintIt(bestFit50)
        bestFit50._1.mean should equal(4.0)
        math.abs(bestFit50._2.R2 - 1.0) < 0.01 should be(true)
    }

    test("Normal best fits Normal") {
        var lfunc = FittingVariate.lfunc

        val nbrePoints = 100
        val PointMin = 0.0
        val PointMax = 300.0
        val serieX = serieXDouble(PointMin, PointMax, nbrePoints)
        val func = new Normal(179.56, 800)
        val serieY = serieZ(serieX, func.pf)

        myPrintIt("\n" + serieY.zipWithIndex.map((di: (Double, Int)) => (0 until (di._1 * 1000).toInt).map((i: Int) => di._2)).mkString("\n", "\n", "\n"))
        val l100 = serieY.zipWithIndex.map((di: (Double, Int)) => (0 until (di._1 * 1000).toInt).map((i: Int) => di._2.toDouble)).flatten
        val fp100 = new FittingVariate(lfunc, l100)
        val bestFit100 = fp100.llr.head
        myPrintIt(bestFit100)
        bestFit100._1.mean should equal(4.0)
        math.abs(bestFit100._2.R2 - 1.0) < 0.01 should be(true)
        math.abs(bestFit100._2.beta1 - 1.0) < 0.02 should be(true)

        val l50 = serieY.zipWithIndex.map((di: (Double, Int)) => (0 until (di._1 * 50).toInt).map((i: Int) => di._2.toDouble)).flatten
        val fp50 = new FittingVariate(lfunc, l50)
        val bestFit50 = fp50.llr.head
        myPrintIt(bestFit50)
        bestFit50._1.mean should equal(4.0)
        math.abs(bestFit50._2.R2 - 1.0) < 0.01 should be(true)
    }

    //waiting(30 seconds)
    L.closeFiles
}
