package piezo1

import kebra._
import kebra.MyLog._
import FFT._

object Fit {
    def correlate(l1: List[Double], l2: List[Double]) = {
        myRequire2(l1.size, l2.size)
        ((l1 zip l2).map(z => Math.abs(z._1 - z._2)).sum / l1.size) * 100 / Math.max(l1.max - l1.min, l2.max - l2.min)
    }
}

class Fit(normalizedGoodSamples: List[Sample]) {
    var zeFitCurveRaw = List.empty[(Double, Int)]
    var zeFitCurveNo50Hz = List.empty[(Double, Int)]
    var ffted = List.empty[Complex]

    if (!normalizedGoodSamples.isEmpty) {
        zeFitCurveRaw = Sample.zeBaseCurve
        normalizedGoodSamples.foreach(z => {
            zeFitCurveRaw = z.normalized.zip(zeFitCurveRaw).map(y => (y._1._1 + y._2._1, y._1._2))
        })
        zeFitCurveRaw = zeFitCurveRaw.map(pt => (pt._1 / normalizedGoodSamples.size, pt._2))
        ffted = fftD(zeFitCurveRaw.map(_._1))
        if (needs50HzFiltering) {
            myErrPrintDln("Need to filter 50Hz!")
            val fftedNo50Hz = ffted.zipWithIndex.map(z => if (z._2 > Sample.cutIndex50Hz) Complex(0.0, 0.0) else z._1)
            zeFitCurveNo50Hz = ifft4display(fftedNo50Hz).zipWithIndex
            val lmap1 = zeFitCurveNo50Hz.map(_._1)
            val lmin = lmap1.min
            val lmax = lmap1.max - lmap1.min
            zeFitCurveNo50Hz = zeFitCurveNo50Hz.map(z => ((z._1 - lmin) * Sample.max / lmax, z._2))
        }
    }

    def needs50HzFiltering = {
        val splitted = ffted.splitAt(Sample.cutIndex50Hz)
        myPrintDln(splitted._1.map(_.absolute).sum.toInt, splitted._2.take(20).map(_.absolute).sum.toInt)
        splitted._1.map(_.absolute).sum.toInt < (10 * splitted._2.take(20).map(_.absolute).sum.toInt)
    }

    override def toString = "Fit["+normalizedGoodSamples.size+" samples]["+Sample.normalizedNumSamples+" points]"

    def displayPatterns(title: String) = {
        if (isNotEmpty) {
            var rangeesIn = List(Sample.zeBaseCurve.map(_._2.toString), zeFitCurveRaw.map(_._1.toInt.toString))
            var rangeeMaitresse = List("pattern")
            if (!zeFitCurveNo50Hz.isEmpty) {
                rangeeMaitresse = rangeeMaitresse :+ "patternNo50Hz"
                rangeesIn = rangeesIn :+ zeFitCurveNo50Hz.map(_._1.toInt.toString)
            }
            val rangeesOut = MyLog.inverseMatrix(rangeesIn).asInstanceOf[List[List[String]]]
            GoogleCharts.lineChart("Patterns"+title, rangeesOut, rangeeMaitresse, true, 1, 0)
        }
    }

    def getCorrelations(raw: Boolean) = {
        val zeFitCurve = if (raw) zeFitCurveRaw else zeFitCurveNo50Hz
        //normalizedGoodSamples.map(_.normalized.zip(zeFitCurve).map(z => Math.abs(z._1._1 - z._2._1)).sum / zeFitCurve.size)
        normalizedGoodSamples.map(z => Fit.correlate(z.normalized.map(_._1), zeFitCurve.map(_._1)))
    }

    def getFitCurve = if (zeFitCurveNo50Hz.isEmpty) zeFitCurveRaw else zeFitCurveNo50Hz

    def correlate(other: Fit) = Fit.correlate(getFitCurve.map(_._1), other.getFitCurve.map(_._1))

    def isNotEmpty = !ffted.isEmpty
}