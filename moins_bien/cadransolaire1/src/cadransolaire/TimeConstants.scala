package cadransolaire

import java.util.Calendar
import scala.math

object TimeConstants {
	val uneMinuteMillis = 60*1000
			val uneHeureMillis = 60*uneMinuteMillis
			val uneHeureDegres = 360/24
			val inclinaison = 23.44
			val juin21 = Calendar.getInstance
			juin21.set(2011, Calendar.JUNE, 21)
			val juin21Millis = TimeConstants.juin21.getTimeInMillis

			val equinoxeDePrintemps = Calendar.getInstance
			equinoxeDePrintemps.set(2011, Calendar.MARCH, 20)
			val equinoxeDePrintempsMillis = TimeConstants.equinoxeDePrintemps.getTimeInMillis

			var premierDeChaqueMois = List[Long]()
			val dec31 = Calendar.getInstance
			dec31.set(2011,Calendar.DECEMBER,31)
			val c = Calendar.getInstance
			c.set(2011, Calendar.JANUARY, 1)
			while(c.compareTo(dec31) < 0) {

				premierDeChaqueMois = premierDeChaqueMois :+ c.getTimeInMillis
						c.add(Calendar.MONTH,1)
			}

	def getDayOfYear(time: Long): Int = {
			val c = Calendar.getInstance
					c.setTimeInMillis(time)
					c.get(Calendar.DAY_OF_YEAR)
	}

	def hauteurDuSoleilAMidi(latit: Double, jour: Int): Double = {
	  // overshoots by 3deg http://www.imcce.fr/fr/ephemerides/phenomenes/rts/rts.php
			val j = (jour-getDayOfYear(equinoxeDePrintempsMillis))*2*math.Pi/365
					latit + (math.sin(j)*inclinaison)
	}

	def equationDuTemps(time: Long): Long = {
			equationDuTemps(getDayOfYear(time).toDouble).toLong
	}

	def equationDuTemps(dayOfYear: Double): Double = {
			// http://fr.wikipedia.org/wiki/Equation_du_temps
			val M = math.toRadians(357 + (0.9856 * dayOfYear))
					val C = 1.914 * math.sin(M) + (0.02 * math.sin(2*M))
					val L = math.toRadians(280 + C + (0.9856 * dayOfYear))
					val R = -(2.465 * math.sin(2*L)) + (0.053 * math.sin(4*L))
					((R+C)*4*TimeConstants.uneMinuteMillis)
	}

	def angleLigneDHeure(latit: Double, heure: Double, angleDuMur: Double): Double = {
			// http://en.wikipedia.org/wiki/Sundial#Vertical_declining_dials
			math.toDegrees(math.atan(math.cos(math.toRadians(latit))/((math.sin(math.toRadians(latit))*math.sin(math.toRadians(angleDuMur)))+(math.cos(math.toRadians(angleDuMur))/math.tan(math.toRadians(15*heure))))))
	}

	def angleHauteurGnomon(latit: Double, angleDuMur: Double): Double = {
			// http://en.wikipedia.org/wiki/Sundial#Vertical_declining_dials
			math.toDegrees(math.asin(math.cos(math.toRadians(latit)*math.cos(math.toRadians(angleDuMur)))))
	}

	def angleSousStylaireGnomon(latit: Double, angleDuMur: Double): Double = {
			// http://fr.wikipedia.org/wiki/Cadran_d%C3%A9clinant
			math.toDegrees(math.atan(math.sin(math.toRadians(angleDuMur))/math.tan(math.toRadians(latit))))
	}
	
	def angleSousStylaireGnomonRev(latit: Double, angleSousStylaire: Double): Double = {
			// http://fr.wikipedia.org/wiki/Cadran_d%C3%A9clinant
			math.toDegrees(math.asin(math.tan(math.toRadians(angleSousStylaire))*math.tan(math.toRadians(latit))))
	}

	def solarElevationAngle(latit: Double, heure: Double, dayOfYear: Int): Double = {
			val sunDec = sunDeclination(dayOfYear)
					// http://en.wikipedia.org/wiki/Solar_elevation_angle
					math.toDegrees(math.asin((math.cos(math.toRadians(latit))*math.cos(heure*math.toRadians(uneHeureDegres))*math.cos(math.toRadians(sunDec)))+
							(math.sin(math.toRadians(latit))*math.sin(math.toRadians(sunDec)))))
	}

	def sunDeclination(dayOfYear: Int): Double = {
			// http://en.wikipedia.org/wiki/Declination#Sun
	  // matches http://www.imcce.fr/fr/ephemerides/phenomenes/rts/rts.php & http://www.horca.net/graphs/sunplot.php
	  //	inclinaison*(-1)*math.cos((math.toRadians(360.0)/365.0)*(dayOfYear+10))
	  val mul = math.toRadians(360.0)/365.24
	  val mul2 = (math.toRadians(360.0)/math.Pi)*0.0167
			math.toDegrees(math.asin(0.39779*(math.cos((mul*(dayOfYear+10))+(mul2*math.sin(mul*(dayOfYear-2))))))*(-1))		
	}
	
					/* http://www.imcce.fr/fr/ephemerides/phenomenes/rts/rts.php
		     Lieu :  Jougne, Doubs, FR	06deg23'23" E / 46deg45'38" N
Date 	    		Lever 		Passage au meridien Coucher
Temps Universel 	heure 	azimut 	heure 	hauteur heure 	azimut
2012-01-09 			7.33 	122.66 	11.69 	21.09 	16.05 	237.43
2012-01-10 			7.32 	122.43 	11.70 	21.24 	16.07 	237.66
2012-01-11 			7.32 	122.19 	11.70 	21.39 	16.09 	237.90
2012-01-12 			7.31 	121.94 	11.71 	21.54 	16.11 	238.15
2012-01-18 			7.25 	120.22 	11.74 	22.63 	16.25 	239.90
2012-01-19 			7.24 	119.90 	11.75 	22.84 	16.27 	240.22
2012-01-20 			7.22 	119.57 	11.75 	23.05 	16.29 	240.56 */

	def traceCadran(latit: Double, angleDuMur: Double, hauteurGnomon: Double, jourAnnee: Int, heure: Double): XY = {
	  val hauteurDuSoleil = TimeConstants.solarElevationAngle(latit,heure,jourAnnee)
	  				val	projectionHauteurDuSoleil = new BetaFunction(0.0,-hauteurGnomon*math.tan(math.toRadians(hauteurDuSoleil)))
					val azimutDuSoleil = TimeConstants.angleLigneDHeure(latit, heure, angleDuMur)
/*					val ligneDHeure = centreDuCadran.getBetaFunction(azimutDuSoleil-90)

					projectionHauteurDuSoleil & ligneDHeure */
					new XY(0.0,0.0)
	}
}