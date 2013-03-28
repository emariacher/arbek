package cadransolaire
import java.awt.Color
import scala.collection.immutable.ListSet
import _root_.scalaSci.math.plot.plotTypes._
import _root_.scalaSci.math.plot.plot._
import _root_.scalaSci.math.plot.canvas._
import kebra.MyLog._
import kebra.MyFileChooser

class CadranSolaireExperimental(val instants: List[Instant], val bounds: (Double,Double, Double), val precision: Double, val timeType: String, val stitle: String, plot: (Array[Double], Array[Double], java.awt.Color, java.lang.String) =>
scalaSci.math.plot.PlotPanel) {
    myErrPrintDln("*******************"+stitle+" "+timeType)
    assume(instants.head.times.indexOf(timeType)>=0,"valid timetype")
    assume(instants.size>1,"instants.size>1, instants:\n"+instants.mkString("\n  "))
    var min = bounds._1
    var step = bounds._2
    var max = bounds._3
    assume(precision<step)
    assume(precision>0.0)
    assume(step>precision)
    var angleDuMurs = MyStatistics.inc(min, step, max)
    var best: GetBestFit = _
    do {
        //		myPrintDln("min: %2.2fdeg".format(min)+", step: %2.2fdeg".format(step)+", max: %2.2fdeg".format(max))
        best = new GetBestFit(instants, angleDuMurs)
        angleDuMurs = MyStatistics.inc(best.fit.angleDuMur-step, step/10.0, best.fit.angleDuMur+step)
        step = step/10.0
    } while (step>precision)
}

class GetBestFit(val instants: List[Instant], val angleDuMurs: Array[Double]){
    val fits = angleDuMurs.map((angleDuMur: Double) => new GetFit(instants, angleDuMur))
            val minAngleStdDev = fits.map(_.getResult._1).min
            val minPtsStdDev = fits.map(_.getResult._2).min
            val minDeltaSousStylaire = fits.map(_.getResult._3).min
            val minDeltaGnomon = fits.map(_.getResult._4).min
            val indexMinAngleStdDev = fits.indexWhere(_.getResult._1 == minAngleStdDev)
            val indexMinPtsStdDev = fits.indexWhere(_.getResult._2 == minPtsStdDev)
            val indexDeltaSousStylaire = fits.indexWhere(_.getResult._3 == minDeltaSousStylaire)
            val indexDeltaGnomon = fits.indexWhere(_.getResult._4 == minDeltaGnomon)
            val fit = fits.apply(indexDeltaSousStylaire)
            myErrPrintDln("+++"+toString)

            override def toString: String = fit.toString+ "( indexMinAngleStdDev: %d".format(indexMinAngleStdDev)+
            ", indexMinPtsStdDev: %d".format(indexMinPtsStdDev)+", indexDeltaSousStylaire: %d".format(indexDeltaSousStylaire)+
            ", indexDeltaGnomon: %d)".format(indexDeltaGnomon)
}

class GetFit(val instants: List[Instant], val angleDuMur: Double) {
    val (centreDuCadran, intersectionsStdDev) = new Intersections(instants.map(_.getLigneTheorique("SolarTime", angleDuMur))).getResult
            val	angleHauteurGnomon = TimeConstants.angleHauteurGnomon(instants.head.latit, angleDuMur)
            val	angleGnomon = TimeConstants.angleSousStylaireGnomon(instants.head.latit, angleDuMur)
            val	tailleGnomon = centreDuCadran.hypot/math.cos(math.toRadians(angleHauteurGnomon))
            val	hauteurGnomon = math.tan(math.toRadians(angleHauteurGnomon))*centreDuCadran.hypot
            val	pointsStdDev = instants.map(_.update(this)).sum / instants.size
            val angleSousStylaire1 = centreDuCadran.getAngle
            val DeltaSousStylaire = (angleGnomon-angleSousStylaire1).abs
            val DeltaGnomon = (hauteurGnomon-instants.head.gnomon).abs
            myPrintDln("-----"+toString)

    override def toString: String = "angleDuMur: %2.2fdeg ".format(angleDuMur)+centreDuCadran.toString+
    " (intersectsStdDev: %2.2f".format(intersectionsStdDev)+", pointsStdDev: %2.2f".format(pointsStdDev)+
    ", DeltaSousStylaire: %2.2fdeg".format(DeltaSousStylaire)+", DeltaGnomon: %2.2f mm)".format(DeltaGnomon)

    def getResult: (Double, Double, Double, Double) = (intersectionsStdDev, pointsStdDev, DeltaSousStylaire, DeltaGnomon)

    def gnomonToString: String = "ahGnomon: %2.2fdeg".format(angleHauteurGnomon)+"aGnomon: %2.2fdeg".format(angleGnomon)+", hGnomon:%2.2f mm".format(hauteurGnomon)

}

class readCSV {
    // parsing simple csv file
	val f = (new MyFileChooser("GetBugsLog")).justChooseFile("csv");
    val l = scala.io.Source.fromFile(f).getLines.toList.foldLeft(List[List[String]]())(_:+_.split(",").toList)
            require(l.head.toString=="List(date, heure, x, y)","expected format")
            val instants = l.tail.foldLeft(ListSet[Instant]())(_ + new Instant(_)).toList
            myErrPrintDln(instants.mkString("\n"))
}

object plotUtils {

    def angleDuMurPlot(cadran: CadranSolaireExperimental, timeType: String) = {
        val instants = cadran.instants
                val angleDuMur = cadran.best.fit.angleDuMur
                val hueInc = 1.toFloat/7.3.toFloat
                var hue = 0.toFloat
                figure()
        title(cadran.best.toString)
        xlabel("x - "+cadran.best.fit.toString)
        ylabel("y - "+cadran.best.fit.toString)	  


        var lines = List[BetaFunction]()
        instants.foreach((i: Instant) => {
            lines = lines :+ i.plotInstant(timeType, angleDuMur, hue)
                    hue += hueInc
        })
        linePlotsOn
        plot(List(0.0, 0.0).toArray, List(cadran.best.fit.centreDuCadran.x, cadran.best.fit.centreDuCadran.y).toArray, Color.BLACK, cadran.best.fit.gnomonToString)

    }

}

