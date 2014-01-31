package piezo1

import kebra._
import kebra.MyLog._
import Math._
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit
import FFT._
import PhysiologicalConstants._

class Piezo4NoGraphic {
    myAssert2(128, FFT.findPow2(130))

    myPrintln("Hello World!")
    val f = (new MyFileChooser("GetArduinoLogs")).justChooseFile("log");
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
    val picFreq = getPicFreqIndex(filtered, sampleDuration)
    val bpm = getBpm(picFreq._2, sampleDuration)
    myPrintIt(sampleSize, decimation, "%3.2f bpm".format(bpm))

    val iffted8 = FFT.ifft(filtre(ffted6, lowestFreqIndex, sampleSize / 2)).map(_.absolute).zipWithIndex
    val iffted9 = FFT.ifft(filtre(ffted6, lowestFreqIndex, sampleSize / 4)).map(_.absolute).zipWithIndex

    val pics = new Pics(ldecimed, ttick, bpm, decimation)

    val goodSample = new GoodSamples(l.zipWithIndex, pics.goodSampleRoots)

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
        val nextBelowFound = lowerThanFound.reverse.tail.head
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
}