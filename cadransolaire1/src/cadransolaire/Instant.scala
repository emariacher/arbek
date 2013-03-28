package cadransolaire

import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date
import _root_.scalaSci.math.plot.plotTypes._
import _root_.scalaSci.math.plot.plot._
import _root_.scalaSci.math.plot.canvas._
import java.awt.Color
import kebra.MyLog._
import kebra.MyPlotUtils._

class Instant(val origTime: Long, val xy: XY) {
			val (latit,longit,gnomon) = (46.762472,6.410565,100.0)
			val SolarStdTime = getSolarStdTime
			val SolarTime = SolarStdTime - TimeConstants.equationDuTemps(SolarStdTime)
			val polar = (xy.hypot,math.atan2(xy.y,xy.x))
			val hauteurDuSoleil = TimeConstants.solarElevationAngle(latit,tempsDuJour("SolarTime"),jourAnnee)
			val times = List("origTime","SolarStdTime","SolarTime")
			var pointTheorique = new XY(0.0,0.0)
	var projectionHauteurDuSoleil = new BetaFunction(0.0,0.0)
	var ligneDHeure = new BetaFunction(0.0,0.0)


	def this(l: List[String]) = this(new SimpleDateFormat("M/d/yyy HH:mm:ss").parse(l.apply(0)+" "+l.apply(1)).getTime,new XY (l.apply(2).toInt,l.apply(3).toInt))
	//		def this(heure: Int) = this(new SimpleDateFormat("M/d/yyy HH:mm:ss").parse("1/12/2012 "+heure+":00:00").getTime,0,0)

	def jourAnnee: Int = {
		val c = Calendar.getInstance
				c.setTimeInMillis(origTime)
				c.get(Calendar.DAY_OF_YEAR)
	}

	def getSolarStdTime: Long = {
			val year = printZisday(new Date(origTime),"yyyy").toInt
					var dernierDimancheDeMars = { 
				var c = Calendar.getInstance
						c.set(year+2000,Calendar.MARCH,31)
						while(c.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY) {
							c.roll(Calendar.DAY_OF_MONTH,-1)
						}
				c.get(Calendar.DAY_OF_YEAR)
			}
			var dernierDimancheDOctobre = { 
				var c = Calendar.getInstance
						c.set(year+2000,Calendar.OCTOBER,31)
						while(c.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY) {
							c.roll(Calendar.DAY_OF_MONTH,-1)
						}
				c.get(Calendar.DAY_OF_YEAR)
			}
			if((jourAnnee<dernierDimancheDeMars)||(jourAnnee>dernierDimancheDOctobre)) {
				origTime-TimeConstants.uneHeureMillis+(longit.toLong*TimeConstants.uneHeureMillis/TimeConstants.uneHeureDegres)
			} else {
				origTime-(2*TimeConstants.uneHeureMillis)+(longit.toLong*TimeConstants.uneHeureMillis/TimeConstants.uneHeureDegres)
			}
		}

		override def toString: String = printZisday(origTime)+" "+xy
				def toStringLong(fmt: String): String = xy+" "+times.map((timeType: String) => timeType+" "+get(timeType, fmt))

				def now: String = printZisday(SolarTime)
				def latex: String = printZisday(origTime)+" "

				def ThetaVsHourString(timeType: String): String = {
					"["+Math.toDegrees(ThetaVsHour(timeType)._1).toInt+","+ThetaVsHour(timeType)._2.toLong+" _ "+printZisday(origTime)+" _ "+now+"]"
				}

				def ThetaVsHour(timeType: String) = (Math.toDegrees(polar._2)-90,12.0-tempsDuJour(timeType))

						def tempsDuJour(timeType: String): Double = {
					val c = Calendar.getInstance
							c.setTimeInMillis(getTimeType(timeType))
							(c.get(Calendar.HOUR_OF_DAY).toDouble-12.0) + (c.get(Calendar.MINUTE).toDouble/60.0)
				}

				def get(timeType: String, fmt: String): String = {
					printZisday(new Date(getTimeType(timeType)), fmt)
				}

				def getTimeType(timeType: String): Long = {
					timeType match {
					case "SolarStdTime" => SolarStdTime
					case "origTime" => origTime
					case "SolarTime" => SolarTime
					case _ => SolarTime
					}

				}

				def getLigneTheorique(timeType: String, angleDuMur: Double): BetaFunction = {
					val angle = TimeConstants.angleLigneDHeure(latit, tempsDuJour(timeType), angleDuMur)
							//							val betaFunction =		new BetaFunction(math.tan(angle.toRadians), xy.y - (xy.x*math.tan(angle.toRadians)))
							val betaFunction =	xy.getBetaFunction(angle-90)
//							L.myPrintDln(toString+" -- "+timeType+" "+get(timeType,"HH:mm")+" "+(" %4.2f" format tempsDuJour(timeType))+(" %4.3f" format angle)+"deg --- "+betaFunction.toString)
							betaFunction
				}

				override def equals(x: Any): Boolean = {
					toString.equals(x.asInstanceOf[Instant].toString)
				}

				def update(gf: GetFit): Double = {
					projectionHauteurDuSoleil = new BetaFunction(0.0,-gf.hauteurGnomon*math.tan(math.toRadians(hauteurDuSoleil)))
					val azimutDuSoleil = TimeConstants.angleLigneDHeure(latit, tempsDuJour("SolarTime"), gf.angleDuMur)
					ligneDHeure = gf.centreDuCadran.getBetaFunction(azimutDuSoleil-90)

					pointTheorique = projectionHauteurDuSoleil & ligneDHeure

//					L.myPrintDln(toString+", projectionHauteurDuSoleil: "+projectionHauteurDuSoleil+", ligneDHeure: "+ligneDHeure)
//					L.myPrintDln("hauteurDuSoleil:%2.2fdeg".format(hauteurDuSoleil)+", azimutDuSoleil:%2.2fdeg".format(azimutDuSoleil))
//					L.myPrintDln("    "+toString+" pointTheorique: "+pointTheorique+" vs "+xy+": %2.2f".format((pointTheorique-xy).hypot))
					
					(pointTheorique-xy).hypot
				}

				def plotInstant(timeType: String, angleDuMur: Double, hue: Float): BetaFunction = {
							val func = getLigneTheorique(timeType, angleDuMur)
							func.plotFunc((-200.0,200.0,-200.0,200.0),1.0,"",hue)
							scatterPlotsOn
							if(pointTheorique.hypot>0.0) {
								plot(List(pointTheorique.x, xy.x, xy.x).toArray,List(pointTheorique.y, xy.y, xy.y).toArray, getColor(hue),latex+"h: %2.2f".format((xy-pointTheorique).hypot))
							} else {
								plot(List(xy.x).toArray,List(xy.y).toArray, getColor(hue),latex)
							}
					func
				}

				def plotInstant2(timeType: String, angleDuMur: Double, hue: Float): BetaFunction = {
							val func = getLigneTheorique(timeType, angleDuMur)
							func.plotFunc((-200.0,200.0,-200.0,200.0),1.0,"",hue)
							scatterPlotsOn
							if(pointTheorique.hypot>0.0) {
								plot(List(pointTheorique.x, xy.x, xy.x).toArray,List(pointTheorique.y, xy.y, xy.y).toArray, getColor(hue),now+"h: %2.2fdeg".format(TimeConstants.angleLigneDHeure(latit, tempsDuJour(timeType), angleDuMur)))
							} else {
								plot(List(xy.x).toArray,List(xy.y).toArray, getColor(hue),latex)
							}
					func
				}

}