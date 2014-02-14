package piezo1

import kebra._
import kebra.MyLog._
import Math._
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit
import FFT._
import PhysiologicalConstants._
import scalation.stat.StatVector
import java.io.File

object Pz5 {
    def sample(l: List[(Double, Int)], sampling: Int) = (l.filter(_._2.toInt % sampling == 0))
    def onlyLowerIndices(l: List[(Int, Int)], indexHigh: Int) = l.filter(_._2 <= indexHigh)
    def removeExtremSamples(lsamplesIn: List[Sample], lcorrelationsIn: List[Double], correlationStats: StatVector, stdDevMulLimit: Double) = {
        val outsiders = lcorrelationsIn.zipWithIndex.filter(z => abs(z._1 - correlationStats.mean) > (correlationStats.stddev * stdDevMulLimit)).map(_._2)
        lsamplesIn.zipWithIndex.filter(z => !outsiders.contains(z._2)).map(_._1)
    }
}
class Piezo5NoGraphic(val l: List[Double], val ttick: Duration, val curveName: String) {
    def this(samplesAndTick: (List[Double], Duration), curveName: String) = this(samplesAndTick._1, samplesAndTick._2, curveName: String)

    myErrPrintln(curveName+" *************************************************")
    val decimation = l.size / PhysiologicalConstants.sampleSize
    val sampleDuration = decimation * sampleSize * ttick
    myPrintln(curveName, ttick.toString, Duration.apply((sampleDuration).toSeconds, SECONDS).toString, decimation, l.size)
    val ldecimed = Pz5.sample(l.zipWithIndex, decimation)
    val ffted6 = fftD(ldecimed.map(_._1))
    val lowestFreqIndex = (lowestBpm * sampleDuration.toMillis / 1.minute.toMillis).toInt
    val greatestFreqIndex = (greatestBpm * sampleDuration.toMillis / 1.minute.toMillis).toInt
    myPrintIt(lowestFreqIndex, greatestFreqIndex)
    val ffted7 = filtre(ffted6, lowestFreqIndex, greatestFreqIndex)
    val filtered = ffted7.map(_.absolute.toInt).zipWithIndex.filter(_._1 > 0)
    var picFreq = (0, 0)
    var bpm = 0.0
    var iffted = List.empty[(Int, List[(Double, Int)])]
    var pics: Pics = _
    var normalizedGoodSamples = List.empty[Sample]
    var fit = new Fit(normalizedGoodSamples)
    var correlationStats = new StatVector(List.empty[Double].toArray)
    var lcorrelations = List.empty[Double]

    /** functions **/

    def getPicFreqIndex(filtered: List[(Int, Int)], sampleDuration: Duration) = {
        val sorted = filtered.sortBy(_._1)
        val picMax = sorted.last
        val sortedUntilMax = Pz5.onlyLowerIndices(sorted, picMax._2)
        myPrintIt(curveName, func(1), sortedUntilMax.takeRight(10).mkString("\n"))

        val picFreqIndexTemp = filtered.indexWhere(z => z._1 > picMax._1 * 0.72) // 0.70 trop petit pour eric25_raw1, 0.76 trop grand pour eric25_raw2
        val picFreq = filtered.apply(picFreqIndexTemp)
        val picFreqIndex = picFreq._2

        // compute "quality" pic found vx max and pic vs following lower
        val found_pic = (filtered.apply(picFreqIndexTemp)._1 * 1.0) / sorted.last._1
        val lowerThanFound = Pz5.onlyLowerIndices(sorted, picFreqIndex)
        val nextBelowFound = if (lowerThanFound.size > 1) {
            lowerThanFound.reverse.tail.head
        } else {
            (0, 0)
        }
        val found_nextfound = (nextBelowFound._1 * 1.0) / (picFreq._1 * 1.0)
        myPrintIt(picMax._2, picFreqIndex, nextBelowFound._2)
        myPrintIt(picMax._2, picFreqIndex, "%3.2f %3.2f (greater is better 1.0 is max)".format(found_pic, found_nextfound))
        picFreq
    }

    def getBpm: Double = {
        picFreq = getPicFreqIndex(filtered, sampleDuration)
        bpm = (picFreq._2 * 1.minute).toMillis.toDouble / sampleDuration.toMillis.toDouble
        myErrPrintln(curveName, func(1), "%3.2f bpm".format(bpm))
        bpm
    }

    def getDecimationAndSampleSize(lSize: Int, periodSample: Duration) = {
        var decimation = min(lSize, 8092)
        while ((decimation * periodSample) >= 10.millis) {
            decimation = (decimation / 1.5).toInt
        }
        val sampleSize = lSize / decimation
        myAssert((decimation * periodSample) < 10.millis)
        myAssert(sampleSize >= 1024)
        (decimation, sampleSize)
    }

    def getFittingCurve(bpm: Double) {
        myPrintDln(curveName, func(1), sampleSize, decimation, "In: %3.2f bpm".format(bpm))

        //iffted = List(4, 8, 16).map(i => (i, ifft4display(filtre(ffted6, lowestFreqIndex, sampleSize / i)).zipWithIndex))

        if (bpm > PhysiologicalConstants.lowestBpm) {
            pics = new Pics(ldecimed, ttick, bpm, decimation)

            if (!pics.goodSampleRoots.isEmpty) {
                normalizedGoodSamples = new GoodSamples(l.zipWithIndex, pics.goodSampleRoots, !pics.directionUp).normalizedGoodSamples
                fit = new Fit(normalizedGoodSamples)
                lcorrelations = fit.getCorrelations(true)
                if (!lcorrelations.isEmpty) {
                    correlationStats = new StatVector(lcorrelations.toArray)
                    println(curveName + correlationStats.labels)
                    myErrPrintln(curveName + correlationStats.toString)
                }
            } else {
                myErrPrintDln(curveName+" Unable 2 fit :(")
            }
            myErrPrintln(curveName, " %3.2f bpm".format(bpm))

            normalizedGoodSamples = Pz5.removeExtremSamples(normalizedGoodSamples, lcorrelations, correlationStats, 2)
            fit = new Fit(normalizedGoodSamples)
            lcorrelations = fit.getCorrelations(true)
            if (!lcorrelations.isEmpty) {
                val correlationStats2 = new StatVector(lcorrelations.toArray)
                println(correlationStats2.labels)
                myErrPrintln(correlationStats2.toString)
            }
        }
    }

    def displaySamples(lsamples: List[Sample], lcorrelations: List[Double], title: String) = {
        var lcurves = List(("pulse", ldecimed)) ++ iffted.map(z => ("low pass at maxfreq/"+z._1, z._2.map(y => (y._1 + (25 * z._1), y._2))))
        //        var lTime = lcurves.head._2.map(z => (z._2 * ttick.toMillis.toInt).toString)
        var lTime = lcurves.head._2.map(_._2)
        if (!lsamples.isEmpty) {
            var queueCorr = lcorrelations
            var queueSamples = lsamples
            var prev = 0.0
            var prev2 = 0.0
            var lastIndex = 0
            val corrDurList = lTime.map(z => if (!queueSamples.isEmpty) {
                if (z.toInt < lastIndex) {
                    ((prev * 20, z.toInt), (prev2, z.toInt))
                } else if (z.toInt >= queueSamples.head.startIndex) {
                    prev = queueCorr.head
                    prev2 = queueSamples.head.lsize
                    lastIndex = queueSamples.head.l.last._2
                    queueSamples = queueSamples.tail
                    queueCorr = queueCorr.tail
                    ((prev * 20, z.toInt), (prev2, z.toInt))
                } else {
                    ((0.0, z.toInt), (0.0, z.toInt))
                }
            } else {
                ((0.0, z.toInt), (0.0, z.toInt))
            })
            val durations = corrDurList.map(_._2)
            val durmin = durations.map(_._1).filter(_ > 0.0).min
            val durmax = durations.map(_._1).max
            val correlationsList = corrDurList.map(_._1)
            lcurves = lcurves :+ ("correlations (lower is better) [ %1.1f, %1.1f, %1.1f]".format(correlationStats.min, correlationStats.mean, correlationStats.max), correlationsList) :+
                ("sample duration[ "+tick2Millis(durmin.toInt)+" ms, "+tick2Millis(durmax.toInt)+" ms]", durations.map(z => (z._1 / 10, z._2)))
        }
        val rangees = MyLog.inverseMatrix(List(lTime.map(z => tick2Millis(z.toInt).toString)) ++ lcurves.map(_._2.map(_._1.toInt.toString)))
        GoogleCharts.lineChart(curveName + title, rangees.asInstanceOf[List[List[String]]], lcurves.map(_._1), true, 1, 0)
    }

    def tick2Millis(i: Int) = i * ttick.toMicros.toInt / 1000

    def validateFitting {
        val lNumsamples = List(1, 2, 5, 10)
        val fitList = lNumsamples.map(numSamples => new Fit(normalizedGoodSamples.take(numSamples)))
        fitList.foreach(fit => {
            myPrintDln("*******"+fit)
            val corrs = normalizedGoodSamples.zipWithIndex.take(10).map(sample => {
                sample._1.correlate(fit, true)
                myPrintDln("    "+fit+" "+sample._2+" %3.4f ".format(sample._1.coeffCorrelation))
                sample._1.coeffCorrelation
            })
            myPrintDln("  avgCorrelation[raw] %3.4f\n".format(corrs.sum / corrs.size))
        })

        fitList.takeRight(1).foreach(fit => {
            if (!fit.zeFitCurveNo50Hz.isEmpty) {
                myPrintDln("*******"+fit)
                val corrs = normalizedGoodSamples.zipWithIndex.take(10).map(sample => {
                    sample._1.correlate(fit, false)
                    myPrintDln("    "+fit+" "+sample._2+" %3.4f ".format(sample._1.coeffCorrelation))
                    sample._1.coeffCorrelation
                })
                myPrintDln("  avgCorrelation[No50Hz] %3.4f\n".format(corrs.sum / corrs.size))
            }
        })
    }

    def display = {
        var s = "<h2>"+curveName+": "+"%3.2f bpm".format(bpm)+"</h2>"
        s += "<p>"+normalizedGoodSamples.size+" processed samples<p>"
        if (pics != null) {
            s += "<p>fit precision: %3.4f ".format(pics.precision)+", fit mean Correlation: %3.4f (lower is better)</p> ".format(correlationStats.mean)
            s += displaySamples(normalizedGoodSamples, lcorrelations, "")
            s += fit.displayPatterns("")
        } else {
            val rangees = MyLog.inverseMatrix(List(l.zipWithIndex.map(z => tick2Millis(z._2.toInt).toString), l))
            s += GoogleCharts.lineChart(curveName, rangees.asInstanceOf[List[List[String]]], List(curveName), true, 1, 0)
        }
        s
    }

}

object RawList {
    def getSamplesAndTick(f: File) = {
        val lraw = copyFromFile(f.getCanonicalPath).split("\n").toList
        val lraw2 = (lraw.reverse.take(10) ++ lraw.reverse.drop(10).takeWhile(s => s.indexOf("t") < 0 && s.length > 2)).reverse
        val lpartition = lraw2.partition(s => s.indexOf("t") < 0 && s.length > 2)
        val headfoot = lpartition._2
        val l = lpartition._1.map(_.toDouble)
        myPrintIt(headfoot.mkString("\n"))
        val ttick = Duration.apply(headfoot.filter(_.indexOf("meant mus:") == 0).head.split(" ").toList.last.reverse.tail.reverse.toDouble, MICROSECONDS)
        (l, ttick)
    }
}