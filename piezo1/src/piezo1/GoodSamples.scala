package piezo1

import kebra._
import kebra.MyLog._
import FFT._

object Sample {
    val max = 100.0
    val normalizedNumSamples = 1023
    val zeBaseCurve = (0 to Sample.normalizedNumSamples).map((0.0, _)).toList
    val cutIndex50Hz = 40

}
class GoodSamples(l: List[(Double, Int)], goodSampleRoots: List[(Int, Int)]) {
    var goodSamples = List.empty[List[(Double, Int)]]
    var normalizedGoodSamples = List.empty[Sample]

    if (!goodSampleRoots.isEmpty) {
        goodSamples = goodSampleRoots.map(z => l.slice(z._1, z._1 + z._2))
        normalizedGoodSamples = goodSamples.map(new Sample(_))
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
        var rangeesIn = List(Sample.zeBaseCurve.map(_._2.toString), zeFitCurveRaw.map(_._1.toInt.toString))
        var rangeeMaitresse = List("pattern")
        if (!zeFitCurveNo50Hz.isEmpty) {
            rangeeMaitresse = rangeeMaitresse :+ "patternNo50Hz"
            rangeesIn = rangeesIn :+ zeFitCurveNo50Hz.map(_._1.toInt.toString)
        }
        val rangeesOut = MyLog.inverseMatrix(rangeesIn).asInstanceOf[List[List[String]]]
        GoogleCharts.lineChart("Patterns"+title, rangeesOut, rangeeMaitresse, true, 1, 0)
    }

    def getCorrelations(raw: Boolean) = {
        val zeFitCurve = if (raw) zeFitCurveRaw else zeFitCurveNo50Hz
        normalizedGoodSamples.map(_.normalized.zip(zeFitCurve).map(z => Math.abs(z._1._1 - z._2._1)).sum / zeFitCurve.size)
    }

}

class Sample(val l: List[(Double, Int)]) {
    val startIndex = l.head._2
    val lsize = l.size
    var coeffCorrelation = Double.MaxValue // lower is better
    var fit: Fit = _
    val lmap1 = l.map(_._1)
    val queue = lmap1.zipWithIndex
    val lmin = lmap1.min
    val lmax = lmap1.max - lmap1.min
    val ratio = l.size * 1.0 / Sample.normalizedNumSamples * 1.0
    var previndex = Sample.normalizedNumSamples + 777
    val normalized = Sample.zeBaseCurve.map(pt => {
        var index = (pt._2 * ratio).toInt
        var value2add = 0.0
        if (index == previndex) {
            value2add = (queue.apply(index)._1 + queue.apply(index + 1)._1) / 2
            previndex = index
        } else if (index < lsize) {
            value2add = queue.apply(index)._1
            previndex = index
        } else {
            value2add = queue.apply(previndex)._1
        }
        value2add = (value2add - lmin) * Sample.max / lmax
        (value2add, pt._2)
    })

    def correlate(fiti: Fit, raw: Boolean) {
        val zeFitCurve = if (raw) fiti.zeFitCurveRaw else fiti.zeFitCurveNo50Hz
        if (!normalized.isEmpty) {
            myRequire2(normalized.size, zeFitCurve.size)
            coeffCorrelation = normalized.zip(zeFitCurve).map(z => Math.abs(z._1._1 - z._2._1)).sum / zeFitCurve.size
        }
        fit = fiti
    }
}