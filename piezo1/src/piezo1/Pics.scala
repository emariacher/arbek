package piezo1

import scalation.stat._
import kebra._
import kebra.MyLog._
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit

class Pics(l: List[(Double, Int)], ttick: Duration, bpm: Double, decimation: Int) {
    var precision = 0.07
    var goodSampleRoots = List.empty[(Int, Int)]
    val v = new StatVector(l.map(_._1).toArray)
    //println("v          = "+v)
    /*println("v.min      = "+v.min)
    println("v.max      = "+v.max)
    println("v.mean     = "+v.mean)
    println("v.variance = "+v.variance)
    println("v.stddev   = "+v.stddev)
    println("v.rms      = "+v.rms)
    println("v.interval = "+v.interval())*/
    println(v.labels)
    println(v.toString)

    val directionUp = v.max - v.mean > v.mean - v.min

    val threshold = 2 * v.stddev
    myPrintIt(directionUp, threshold)

    val lup = l.map(d => {
        if (directionUp) {
            if (d._1 > v.mean + threshold) { (d._1 - v.mean, d._2) } else { (0.0, d._2) }
        } else {
            if (d._1 < v.mean - threshold) { (v.mean - d._1, d._2) } else { (0.0, d._2) }
        }
    }).filter(_._1 > 0).map(z => (z._1.toInt, z._2))
    myPrintIt(lup.mkString("\n"))

    val sliding1 = lup.sliding(2).filter(z => z.head._2 + decimation != z.last._2).map(_.head).toList
    myPrintIt(sliding1.mkString("\n"))

    val sliding2 = sliding1.sliding(2).map(z => (z.head, z.last._2 - z.head._2)).toList
    myPrintIt(sliding2.mkString("\n"))

    val numTicks4OneBpm = (1 minute).toMicros / (ttick * bpm).toMicros.toInt
    myPrintIt(ttick, "%3.2f bpm".format(bpm), decimation, numTicks4OneBpm)

    while (goodSampleRoots.size < 7 && precision < 0.15) {
        goodSampleRoots = sliding2.filter(z => within(z._2, numTicks4OneBpm, precision)).map(z => (z._1._2, z._2))
        precision += 0.01
    }
    myPrintIt(goodSampleRoots.size, precision)
    if (goodSampleRoots.size < 7) {
        goodSampleRoots = List.empty[(Int, Int)]
    }

    def within(act: Double, exp: Double, precision: Double) = (act > (exp * (1 - precision))) && (act < (exp * (1 + precision)))

}