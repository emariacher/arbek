package fitting

import labyrinthe.LL._
import kebra.MyLog._
import java.io.File
import statlaby._
import _root_.scalaSci.math.plot.plotTypes._
import _root_.scalaSci.math.plot.plot._
import _root_.scalaSci.math.plot.canvas._
import scalation.stat._
import java.awt.Color
import kebra.MyPlotUtils._
import kebra.LinearRegression

class FittingPoisson(l: List[Double]) {
    var llr = List.empty[(Double, LinearRegression)]
            val fig1 = figure()
            title("Courbes!")
            myPrintIt(fig1)
            linePlotsOn
            var hue = 0.1.toFloat
            val incHue = 1.0.toFloat/10
            (2 to 5).foreach((mean: Int) => courbes(l, new Poisson(mean)))

            val fig2 = figure()
            title("Droites!")
            myPrintIt(fig2)
            (2 to 5).foreach((mean: Int) => droites(l, new Poisson(mean)))
            llr = llr.sortBy(_._2.R2).reverse

            def getSeries(l: List[Double], func: Variate): (List[Double],List[Double],List[Double]) = {
        var mean = l.sum/l.length

        val poissonXOrig = (0 to 13).toList.map(_.toDouble)
        val poissonYOrig = serieZ(poissonXOrig, func.pf)
        val poissonYMax = poissonYOrig.max
        val poissonXMax = func.mean
        val poissonXMean = func.mean

        val inc = mean / poissonXMean

        /*myPrintIt(poissonXMean)
        myPrintIt(mean)
        myPrintIt(inc)*/

        val serieX = (0 to 13).toList.map(_*inc) //TODO verify integer vs milieu de l'incrément
        var serieY1 = serieY(serieX, l)
        val YMax = serieY1.max
        serieY1 = serieY1.map(_*poissonYMax/YMax)
        val poissonY = poissonYOrig
        /*myPrintIt(serieX)
        myPrintIt(serieY)
        myPrintIt(poissonY)*/
        myAssert2(serieX.length, serieY1.length)
        myAssert2(serieX.length, poissonY.length)

        (serieX,serieY1,poissonY)
    }


    def courbes(l: List[Double], func: Variate) {
        val (serieX,serieY,poissonY) = getSeries(l,func)
                hue += incHue
                plot(serieX.toArray, serieY.toArray, getColor(hue), "toBeFitted["+func.mean.toInt+"]")
        hue += incHue
        plot(serieX.toArray, poissonY.toArray, getColor(hue), "poisson["+func.mean.toInt+"]")
    }

    def droites(l: List[Double], func: Variate) {
        val (serieX,serieY,poissonY) = getSeries(l,func)
                hue += (2*incHue)
                scatterPlotsOn
                plot(serieY.toArray, poissonY.toArray, getColor(hue), "correlation["+func.mean.toInt+"]")
        val lr = new LinearRegression(serieY.zip(poissonY))
        llr = llr :+ (func.mean,lr)
        lr.plotFunc("correlation["+func.mean.toInt+(" %1.3f]" format lr.R2), hue)
        myPrintIt(func.mean.toInt+" --- "+lr)
    }

}

object PoissonVsPoisson extends App {
    newMyLL(this.getClass.getName,new File("out\\cowabunga"), "htm", true)

    val nbrePoints = 20
    val PointMin = 0.0
    val PointMax = 20.0
    val serieX = serieXInt(PointMin,PointMax, nbrePoints)
    val func = new Poisson(4.0)
    val serieY = serieZ(serieX, func.pf)

    val l100 = serieY.zipWithIndex.map((di: (Double, Int)) => (0 until (di._1*100).toInt).map((i: Int) => di._2.toDouble)).flatten
    val fp100 = new FittingPoisson(l100)    
    val bestFit100 = fp100.llr.head
    myPrintIt(bestFit100)
    myAssert2(bestFit100._1,4.0)
    myAssert(math.abs(bestFit100._2.R2-1.0)<0.01)    
    myAssert(math.abs(bestFit100._2.beta1-1.0)<0.02)

    val l50 = serieY.zipWithIndex.map((di: (Double, Int)) => (0 until (di._1*50).toInt).map((i: Int) => di._2.toDouble)).flatten
    val fp50 = new FittingPoisson(l50)    
    val bestFit50 = fp50.llr.head
    myPrintIt(bestFit50)
    myAssert2(bestFit50._1,4.0)
    myAssert(math.abs(bestFit50._2.R2-1.0)<0.01) 

}

object drawPoisson extends App {
    newMyLL(this.getClass.getName,new File("out\\cowabunga"), "htm", true)

    val nbrePoints = 20
    val PointMin = 0.0
    val PointMax = 20.0
    val serieX = serieXInt(PointMin,PointMax, nbrePoints)
    val fig1 = figure()
    var hue = 0.1.toFloat
    val incHue = 1.0.toFloat/10
    title("Poisson!")
    L.myPrintln("  fig("+fig1+")")
    L.myPrintln("  serieX: "+serieX)
    (1 until 10).toList.foreach((i: Int) => {
        draw(i)
        hue += incHue
    })

    def draw(mean: Double) {
        val func = new Poisson(mean)
        val poissonY = serieZ(serieX, func.pf)
        L.myPrintln("poisson: "+poissonY)
        plot(serieX.toArray, poissonY.toArray, getColor(hue), "poisson["+mean+"]")
    }
}
