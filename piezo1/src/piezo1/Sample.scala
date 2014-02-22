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

class Sample(val l: List[(Double, Int)], mirror: Boolean) {
    val startIndex = l.head._2
    val lsize = l.size
    var coeffCorrelation = Double.MaxValue // lower is better
    var fit: Fit = _
    val normalized = doZeJob

    def doZeJob = {
        //val lmap1 = l.map(_._1)
        val queue = l.map(_._1).zipWithIndex
        //myPrintIt(queue.size, queue.take(28))
        val lmin = l.map(_._1).min
        val lmax = l.map(_._1).max - l.map(_._1).min
        val ratio = l.size * 1.0 / Sample.normalizedNumSamples * 1.0
        var previndex = Sample.normalizedNumSamples + 777
        var normalized = Sample.zeBaseCurve.map(pt => {
            var index = (pt._2 * ratio).toInt
            var value2add = 0.0
            if (index == previndex) {
                if (index < (queue.size - 1)) {
                    value2add = (queue.apply(index)._1 + queue.apply(index + 1)._1) / 2
                }
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
        if (mirror) {
            val zmax = normalized.map(_._1).max
            normalized = normalized.map(z => (zmax - z._1, z._2))
        }
        normalized
    }

    def correlate(fiti: Fit, raw: Boolean) {
        val zeFitCurve = if (raw) fiti.zeFitCurveRaw else fiti.zeFitCurveNo50Hz
        if (!normalized.isEmpty) {
            myRequire2(normalized.size, zeFitCurve.size)
            coeffCorrelation = normalized.zip(zeFitCurve).map(z => Math.abs(z._1._1 - z._2._1)).sum / zeFitCurve.size
        }
        fit = fiti
    }

    override def toString = "["+startIndex+", "+lsize+"]"
}