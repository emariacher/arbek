package fitting

import labyrinthe.LL._
import kebra.MyLog._
import scalation.stat._
import statlaby._
import _root_.scalaSci.math.plot.plotTypes._
import _root_.scalaSci.math.plot.plot._
import _root_.scalaSci.math.plot.canvas._
import java.io.File
import java.awt.Color
import kebra.MyPlotUtils._
import kebra.LinearRegression

object FittingVariate {
    var lfunc = List.empty[Variate]

            lfunc = lfunc ++ (3 to 5).map((i: Int) => new Poisson(i))
            lfunc = lfunc ++ List(0.5,1.0,2.0).map((alpha: Double) => (1 to 8).map((j: Int) => new Weibull(alpha,j*0.5))).flatten
            lfunc = lfunc :+ new Normal(6.0,3.0) :+ new Normal(5.0,1.0) 
            lfunc = lfunc :+ new Triangular(0.0,12.0) 
    
        def getSeries(li1: List[Double], li2: List[Double]): (List[Double],List[Double],List[Double]) = {
        var xmin = 0
                var xmax = 13
                var mean1 = li1.sum/li1.length
        val inc1 = 2 * mean1 / (xmax-xmin)

                    val serieX = (xmin to xmax).toList.map(_*inc1) 
                    var serieY1 = serieY(serieX, li1)
                    var serieY2 = serieY(serieX, li2)

        (serieX,serieY1,serieY2)
    }
}

class FittingVariate(val lfunc: List[Variate], val lin: List[Double]) {
    var llr = lfunc.map((func: Variate) => {
        val (_,serieY,variateY) = getSeries(lin,func)
                val lr = new LinearRegression(serieY.zip(variateY))
        //myPrintIt(func+" "+lr)
        (func,lr)              	
    })
    llr = llr.sortBy(_._2.R2)
    val bestFit = llr.last
    myPrintIt(llr.mkString("\n"))

    def getVariateXY(func: Variate, xmin: Int, xmax: Int, inc: Double): (List[Double],List[Double]) = {
        val variateXOrig = (xmin to xmax).toList.map(_*inc)
                val variateYOrig = serieZ(variateXOrig, func.pf)
                (variateXOrig,variateYOrig)
    }


    def getSeries(li: List[Double], func: Variate): (List[Double],List[Double],List[Double]) = {
        //myPrintIt("****** "+func)
        var xmin = 0
                var xmax = 13
                var incv = 1.0

                val variateXMean = func.mean
                var curveOK = true
                var variateXYOrig =  (List.empty[Double], List.empty[Double])
                var variateYMax = 0.0
                var variateYSignificantThreshold = 0.0
                var variateIndex4XMean = xmax+2
                do {
                    //myPrintIt(func, xmin, xmax, incv)
                    variateXYOrig = getVariateXY(func, xmin, xmax, incv)
                            variateYMax = variateXYOrig._2.max
                            variateYSignificantThreshold = 0.01*variateYMax
                            //val variateX4YMax = variateYOrig.indexOf(variateYMax)

                            val variate4XMean = variateXYOrig._1.find(_>=variateXMean) match {
                            case Some(z) => z
                            case _ =>
                    }
                    variateIndex4XMean = variateXYOrig._1.indexOf(variate4XMean)
                            //myPrintIt(func, variateXMean, variate4XMean, variateIndex4XMean)
                            curveOK = true
                            if(variateIndex4XMean<3) {
                                curveOK = false
                                        incv = incv/2
                            } else if(variateIndex4XMean>8) {
                                curveOK = false
                                        incv = incv*2
                            } /*else {
                        val overThreshold = variateXYOrig._2.zipWithIndex.filter((di: (Double, Int)) => di._1>variateYSignificantThreshold)
                                if(overThreshold.length<variateXYOrig._2.length-5) {
                                    //myPrintIt(variateXYOrig._2)
                                    myPrintIt(overThreshold)
                                    curveOK = false                                    
                                    var xDeltaAvant = overThreshold.head._2 - xmin
                                    var xDeltaApres = xmax - overThreshold.last._2
                                    if(xDeltaAvant<xDeltaApres) {
                                    	incv = incv/1.5
                                    }
                                }
                    }    */                
                } while(!curveOK)

                    myRequire(variateIndex4XMean>=3)
                    myRequire(variateIndex4XMean<=8)

                    /*var l = List.empty[Double]
                            func match {
                            case _: Normal => {
                            	val SignificantThreshold = li.max/100.0
                            	val overThreshold = li.filter(_>SignificantThreshold)
                            	val limin = overThreshold.min
                            	l = overThreshold.map(_ - limin)
                            }
                            case _ => l = li
                }*/
                    val l = li

                    var mean = l.sum/l.length
                    val inc = mean / variateXMean

                    val serieX = (xmin to xmax).toList.map(_*inc) 
                    var serieY1 = serieY(serieX, l)
                    val YMax = serieY1.max
                    serieY1 = serieY1.map(_*variateYMax/YMax)
                    val variateY = variateXYOrig._2
                    myAssert2(serieX.length, serieY1.length)
        myAssert2(serieX.length, variateY.length)

        (serieX,serieY1,variateY)
    }

    def plotBest(title1: String) {
        val func = bestFit._1
                val fig1 = figure()
                title(title1+" best Fitted with ["+func.toString+"] R2: %4.3f".format(bestFit._2.R2))
                linePlotsOn
                val (serieX,serieY,variateY) = getSeries(lin,func)
                myPrintIt(serieX)
                plot(serieX.toArray, serieY.toArray, Color.black, title1+" best Fitted with ["+func.toString+"] R2: %4.3f".format(bestFit._2.R2))
                plot(serieX.toArray, variateY.toArray, Color.red, "["+func.toString+"]")
                scatterPlotsOn
                plot(serieX.toArray, serieX.map((d: Double) => 0.0).toArray, Color.green, "X["+serieX.length+"]")
        myPrintIt(title1+" best Fitted with ["+bestFit+"]")
    }
}

object FuncVsPoisson extends App {
    newMyLL(this.getClass.getName,new File("out\\cowabunga"), "htm", true)

    var lfunc = FittingVariate.lfunc

    val nbrePoints = 20
    val PointMin = 0.0
    val PointMax = 20.0
    val serieX = serieXInt(PointMin,PointMax, nbrePoints)
    val func = new Poisson(4.0)
    val serieY = serieZ(serieX, func.pf)

    val l100 = serieY.zipWithIndex.map((di: (Double, Int)) => (0 until (di._1*100).toInt).map((i: Int) => di._2.toDouble)).flatten
    val fp100 = new FittingVariate(lfunc, l100)    
    val bestFit100 = fp100.llr.head
    myPrintIt(bestFit100)
    myAssert2(bestFit100._1.mean,4.0)
    myAssert(math.abs(bestFit100._2.R2-1.0)<0.01)    
    myAssert(math.abs(bestFit100._2.beta1-1.0)<0.02)
    fp100.plotBest("fp100")

    val l50 = serieY.zipWithIndex.map((di: (Double, Int)) => (0 until (di._1*50).toInt).map((i: Int) => di._2.toDouble)).flatten
    val fp50 = new FittingVariate(lfunc, l50)    
    val bestFit50 = fp50.llr.head
    myPrintIt(bestFit50)
    myAssert2(bestFit50._1.mean,4.0)
    myAssert(math.abs(bestFit50._2.R2-1.0)<0.01) 
    fp50.plotBest("fp50")

}

object drawWeibull extends App {
    newMyLL(this.getClass.getName,new File("out\\cowabunga"), "htm", true)

    var lfunc = (0 to 3).map((i: Int) => (0 to 3).map((j: Int) => new Weibull(0.5+(i*0.5),0.5+(j*0.5)))).flatten

    val nbrePoints = 100
    val PointMin = 0.0
    val PointMax = 5.0
    val serieX = serieXDouble(PointMin,PointMax, nbrePoints)
    val fig1 = figure()
    var hue = 0.1.toFloat
    val incHue = 1.0.toFloat/10
    title("Webull!")
    myPrintIt(fig1)
    myPrintIt(serieX)
    lfunc.toList.foreach((func: Variate) => {
        val funcY = serieZ(serieX, func.pf)
                myPrintIt(funcY)
                plot(serieX.toArray, funcY.toArray, getColor(hue), func.toString)
                hue += incHue
    })
}

