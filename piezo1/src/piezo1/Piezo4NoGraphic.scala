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

class Piezo4NoGraphic {
    myAssert2(128, FFT.findPow2(130))

    myPrintln("Hello World!")
    val extensionLogFiles = "log"
    val f = (new MyFileChooser("GetArduinoLogs")).justChooseFile(extensionLogFiles);
    val lraw = copyFromFile(f.getCanonicalPath).split("\n").toList
    val lraw2 = (lraw.reverse.take(10) ++ lraw.reverse.drop(10).takeWhile(s => s.indexOf("t") < 0 && s.length > 2)).reverse
    val lpartition = lraw2.partition(s => s.indexOf("t") < 0 && s.length > 2)
    val headfoot = lpartition._2
    val l = lpartition._1.map(_.toDouble)
    myPrintIt(headfoot.mkString("\n"))
    val ttick = Duration.apply(headfoot.filter(_.indexOf("meant mus:") == 0).head.split(" ").toList.last.reverse.tail.reverse.toDouble, MICROSECONDS)
    val curveName = f.getName
    val sampleSize = Math.pow(2, 11).toInt
    val decimation = l.size / sampleSize
    val sampleDuration = decimation * sampleSize * ttick
    myPrintln(f.getName)
    myPrintln(ttick.toString, Duration.apply((sampleDuration).toSeconds, SECONDS).toString, decimation)
    val ldecimed = sample(l.zipWithIndex, decimation)
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

    def sample(l: List[(Double, Int)], sampling: Int) = (l.filter(_._2.toInt % sampling == 0))

    def getPicFreqIndex(filtered: List[(Int, Int)], sampleDuration: Duration) = {
        val sorted = filtered.sortBy(_._1)
        val picMax = sorted.last
        val sortedUntilMax = onlyLowerIndices(sorted, picMax._2)
        myPrintIt(sortedUntilMax.takeRight(10).mkString("\n"))

        val picFreqIndexTemp = filtered.indexWhere(z => z._1 > picMax._1 * 0.72) // 0.70 trop petit pour eric25_raw1, 0.76 trop grand pour eric25_raw2
        val picFreq = filtered.apply(picFreqIndexTemp)
        val picFreqIndex = picFreq._2

        // compute "quality" pic found vx max and pic vs following lower
        val found_pic = (filtered.apply(picFreqIndexTemp)._1 * 1.0) / sorted.last._1
        val lowerThanFound = onlyLowerIndices(sorted, picFreqIndex)
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

    def onlyLowerIndices(l: List[(Int, Int)], indexHigh: Int) = l.filter(_._2 <= indexHigh)

    def getBpm(picFreqIndex: Int, sampleDuration: Duration): Double = (picFreqIndex * 1.minute).toMillis.toDouble / sampleDuration.toMillis.toDouble

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

    def doZeJob {
        picFreq = getPicFreqIndex(filtered, sampleDuration)
        bpm = getBpm(picFreq._2, sampleDuration)
        myPrintIt(sampleSize, decimation)
        myErrPrintln("%3.2f bpm".format(bpm))

        iffted = List(4, 8, 16).map(i => (i, ifft4display(filtre(ffted6, lowestFreqIndex, sampleSize / i)).zipWithIndex))

        pics = new Pics(ldecimed, ttick, bpm, decimation)

        if (!pics.goodSampleRoots.isEmpty) {
            normalizedGoodSamples = new GoodSamples(l.zipWithIndex, pics.goodSampleRoots).normalizedGoodSamples
            fit = new Fit(normalizedGoodSamples)
            lcorrelations = fit.getCorrelations(true)
            if (!lcorrelations.isEmpty) {
                correlationStats = new StatVector(lcorrelations.toArray)
                println(correlationStats.labels)
                myErrPrintln(correlationStats.toString)
            }
        } else {
            myErrPrintDln("Unable 2 fit :(")
        }
        myErrPrintln(f.getName, " %3.2f bpm".format(bpm))

        var s = GoogleCharts.htmlHeaderJustZeGraphs
        s += printToday("dd_HH:mm_ss,SSS")

        s += displaySamples(normalizedGoodSamples, lcorrelations, " 1st pass")
        s += fit.displayPatterns(" 1st pass")

        val normalizedGoodSamples2 = removeExtremSamples(normalizedGoodSamples, lcorrelations, correlationStats, 2)
        val fit2 = new Fit(normalizedGoodSamples2)
        val lcorrelations2 = fit2.getCorrelations(true)
        if (!lcorrelations.isEmpty) {
            val correlationStats2 = new StatVector(lcorrelations2.toArray)
            println(correlationStats2.labels)
            myErrPrintln(correlationStats2.toString)
        }

        s += displaySamples(normalizedGoodSamples2, lcorrelations2, " 2nd pass")
        s += fit2.displayPatterns(" 2nd pass")

        s += "\n</body></html>"
        copy2File("zob.html", s)
        //toFileAndDisplay("zob.html", s)

    }

    def removeExtremSamples(lsamplesIn: List[Sample], lcorrelationsIn: List[Double], correlationStats: StatVector, stdDevMulLimit: Double) = {
        myPrintIt((correlationStats.stddev * stdDevMulLimit), lcorrelationsIn.zipWithIndex.map(z => abs(z._1 - correlationStats.mean)))
        val outsiders = lcorrelationsIn.zipWithIndex.filter(z => abs(z._1 - correlationStats.mean) > (correlationStats.stddev * stdDevMulLimit)).map(_._2)
        lsamplesIn.zipWithIndex.filter(!outsiders.contains(_)).map(_._1)
    }

    def displaySamples(lsamples: List[Sample], lcorrelations: List[Double], title: String) = {
        var lcurves = List(("pulse", ldecimed)) ++ iffted.map(z => ("low pass at maxfreq/"+z._1, z._2.map(y => (y._1 + (25 * z._1), y._2))))
        var lTime = lcurves.head._2.map(_._2.toString)
        if (!lsamples.isEmpty) {
            var queueCorr = lcorrelations
            var queueSamples = lsamples
            var prev = 0.0
            var lastIndex = 0
            val correlationsList = lTime.map(z => if (!queueSamples.isEmpty) {
                if (z.toInt < lastIndex) {
                    (prev * 20, z.toInt)
                } else if (z.toInt >= queueSamples.head.startIndex) {
                    prev = queueCorr.head
                    lastIndex = queueSamples.head.l.last._2
                    queueSamples = queueSamples.tail
                    queueCorr = queueCorr.tail
                    (prev * 20, z.toInt)
                } else {
                    (0.0, z.toInt)
                }
            } else {
                (0.0, z.toInt)
            })
            lcurves = lcurves :+ ("correlations (lower is better)", correlationsList)
        }
        val rangees = MyLog.inverseMatrix(List(lTime) ++ lcurves.map(_._2.map(_._1.toInt.toString)))
        GoogleCharts.lineChart(curveName + title, rangees.asInstanceOf[List[List[String]]], lcurves.map(_._1), true, 1, 0)
    }

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

}