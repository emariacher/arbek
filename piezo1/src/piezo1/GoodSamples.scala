package piezo1

import kebra._
import kebra.MyLog._

class GoodSamples(l: List[(Double, Int)], goodSampleRoots: List[(Int, Int)]) {
    val goodSamples = goodSampleRoots.map(z => l.slice(z._1, z._1 + z._2))
    val sampleMax = goodSampleRoots.map(_._2).max.toDouble

    var zeCurve = (0 to sampleMax.toInt).map((0.0, _))
    goodSamples.take(2).foreach(z => {
        var queue = z.map(_._1).zipWithIndex.take(6)
        val ratio = z.size / sampleMax
        myPrintIt(z.size, sampleMax, "%3.2f".format(ratio), queue)
        zeCurve = zeCurve.take(5).map(pt => {
            queue = queue.dropWhile(y => {
                //myPrintln(y, y._2 * ratio, pt)
                y._2 * ratio <= pt._2
            })
            //myPrintIt(queue.head, pt, zeCurve)
            (pt._1 + queue.head._1, pt._2)
        })
        myPrintIt(zeCurve.take(7))
    })

}