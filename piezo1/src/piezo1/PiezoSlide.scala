package piezo1

import kebra._
import kebra.MyLog._
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit

class PiezoSlide(val l: List[Double], val ttick: Duration, val curveName: String) {
    def this(samplesAndTick: (List[Double], Duration), curveName: String) = this(samplesAndTick._1, samplesAndTick._2, curveName: String)
    var lpz = List.empty[Piezo5NoGraphic]

    val numTicks4XSeconds = (Duration.apply(10.0, SECONDS) / ttick).toInt
    val lz = l.zipWithIndex
    //val groups = (lz.grouped(numTicks4XSeconds) ++ lz.drop(numTicks4XSeconds / 2).grouped(numTicks4XSeconds)).toList.par
    val groups = (lz.grouped(numTicks4XSeconds) ++ lz.drop(numTicks4XSeconds / 2).grouped(numTicks4XSeconds)).toList

    val pz5 = new Piezo5NoGraphic(l, ttick, curveName)
    pz5.getFittingCurve(pz5.getBpm)
    lpz = List(pz5)

    if ((!pz5.fit.isNotEmpty) || (pz5.bpm < PhysiologicalConstants.lowestBpm)) {
        lpz = lpz ++ groups.filter(_.size >= PhysiologicalConstants.sampleSize).map(lz => {
            val pz5l = new Piezo5NoGraphic(lz.map(_._1), ttick, curveName + "_" + lz.head._2)
            pz5l.getFittingCurve(pz5l.getBpm)
            if (!pz5l.fit.isNotEmpty) {
                pz5l.getFittingCurve(pz5.getBpm)
            }
            pz5l
        }).filter(pz => pz.bpm > PhysiologicalConstants.lowestBpm && pz.fit.isNotEmpty)
    }
    myAssert(!lpz.isEmpty)
}
