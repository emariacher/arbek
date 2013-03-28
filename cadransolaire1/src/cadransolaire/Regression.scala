package cadransolaire

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfter
import java.util.Calendar
import _root_.scalaSci.math.plot.plotTypes._
import _root_.scalaSci.math.plot.plot._
import _root_.scalaSci.math.plot.canvas._
import java.awt.Color
import java.awt.Font
import java.util.Date
import scala.collection.immutable.ListSet
import kebra.MyLog._
import kebra.MyPlotUtils._
import kebra.ZeParameters
import kebra.MyParameter
import java.io.File
import language.postfixOps


// scala org.scalatest.tools.Runner -p . -o -s cadransolaire.MainCadranSolaire

// parameters.getValue("45zob").toInt should equal (1)
// projects.map(_.getDatabase.getName).contains("COCO") should be (false)

class Regression extends FunSuite with ShouldMatchers {
	newMyLog(this.getClass.getName,new File("cowabunga"),"log")
	val s_file = new File("out\\cadransolaire_input2.csv")
	
	test("dernierDimancheDOctobre") {
		val year = 11
				var dernierDimancheDOctobre = { 
				var c = Calendar.getInstance
						c.set(year+2000,Calendar.OCTOBER,31)
						while(c.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY) {
							c.roll(Calendar.DAY_OF_MONTH,-1)
						}
				c.getTimeInMillis

		}
		println(printZisday(dernierDimancheDOctobre))
		printZisday(dernierDimancheDOctobre).indexOf("30Oct") should equal (0)
	}
	test("heure d hiver") {
		var c = Calendar.getInstance
				val origTime = c.getTimeInMillis
				val (latit,longit) = (46.762472,6.410565)
				val SolarStdTime = origTime-TimeConstants.uneHeureMillis+(longit.toLong*TimeConstants.uneHeureMillis/TimeConstants.uneHeureDegres)
				println("origTime: "+printZisday(origTime)+" "+origTime)
				println("SolarStdTime: "+printZisday(SolarStdTime)+" "+SolarStdTime)
				true should be (true)
	}

	ignore("hauteurDuSoleilAMidi") {
		val i = new Instant(0,new XY(0,0))

		val jours = MyStatistics.inc(0, 1, 365)
		val hauteurDuSoleilAMidi = jours.map((jour: Double) => TimeConstants.hauteurDuSoleilAMidi(i.latit,jour.toInt))

		figure(1)
		title("hauteurDuSoleilAMidi")
		linePlotsOn
		plot(jours, hauteurDuSoleilAMidi, Color.RED,"hauteurDuSoleilAMidi en deg")
		xlabel("x - Jour de l'annee")
		ylabel("y - hauteurDuSoleilAMidi en deg")
		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose

	}

	ignore("equationDuTemps") {
		var c = Calendar.getInstance
				var origTime = c.getTimeInMillis
				var equationDuTemps = TimeConstants.equationDuTemps(origTime)
				println("equationDuTemps: "+printZisday(origTime)+": "+(equationDuTemps/TimeConstants.uneMinuteMillis)+" minutes")				
				c.set(2011,Calendar.AUGUST,1)
				origTime = c.getTimeInMillis
				equationDuTemps = TimeConstants.equationDuTemps(origTime)
				println("equationDuTemps: "+printZisday(origTime)+": "+(equationDuTemps/TimeConstants.uneMinuteMillis)+" minutes")

				val jan01 = Calendar.getInstance
				jan01.set(2012,Calendar.JANUARY,1)
				c.set(2011,Calendar.JANUARY,1)
				var jours = List[Long]()
				while(c.compareTo(jan01) < 0) {
					c.add(Calendar.DAY_OF_MONTH,1)
					jours = jours :+ c.getTimeInMillis
				}

		val valeursDuTemps = jours.map(TimeConstants.equationDuTemps(_).toDouble/TimeConstants.uneMinuteMillis)
				figure(1)
				title("equationDuTemps")
				linePlotsOn
				plot(jours.map(TimeConstants.getDayOfYear(_)toDouble).toArray, valeursDuTemps.toArray, Color.RED,"equationDuTemps en minutes")
		xlabel("x - Jour de l'annee")
		ylabel("y - equationDuTemps en minutes")
		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose

	}

	test("premierDeChaqueMois") {
		println(TimeConstants.premierDeChaqueMois.map(printZisday(_)))
		printZisday(TimeConstants.premierDeChaqueMois.apply(6)).indexOf("01Jul") should equal (0)
		TimeConstants.premierDeChaqueMois.size should equal (12)
	}

	ignore("Analemne") {
		val i = new Instant(0,new XY(0,0))

		val jours = MyStatistics.inc(0, 1, 365)
		val hauteurDuSoleilAMidi = jours.map((jour: Double) => TimeConstants.hauteurDuSoleilAMidi(i.latit,jour.toInt))
		val valeursDuTemps = jours.map((d: Double) => TimeConstants.equationDuTemps(d)/TimeConstants.uneMinuteMillis)

		figure(1)
		title("Analemne")
		linePlotsOn
		plot(valeursDuTemps, hauteurDuSoleilAMidi, Color.RED,"hauteurDuSoleilAMidi en deg")
		xlabel("x - equationDuTemps en minutes")
		ylabel("y - hauteurDuSoleilAMidi en deg")

		val quadruplet = TimeConstants.premierDeChaqueMois.map(
				(premierDuMoisL: Long) => { 
					val premierDuMoisD = TimeConstants.getDayOfYear(premierDuMoisL).toDouble
							(
									TimeConstants.equationDuTemps(premierDuMoisD)/TimeConstants.uneMinuteMillis,
									TimeConstants.hauteurDuSoleilAMidi(i.latit,premierDuMoisD.toInt),
									printZisday(premierDuMoisL).substring(2,3)
									)})

									scatterPlotsOn
									plot(quadruplet.map(_._1).toArray, quadruplet.map(_._2).toArray, Color.BLUE,"premierDeChaqueMois")
		quadruplet.foreach((t: (Double,Double,String)) => mark(t._3.apply(0), t._1  , t._2, Color.GREEN, new Font("Arial", Font.BOLD, 20)))
		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose
	}

	test("rect2polar") {
		var x = 0
				var y = 1
				(math.hypot(x,y),math.atan2(y,x)) should equal ((1,math.Pi/2))
	}

	test("linearRegression") {
		val pairs0 = List((0.0,1.0),(1.0,2.0))
				val lr0 = new LinearRegression(pairs0)
		println(pairs0)
		println("  "+new Correlation(pairs0, new BetaFunction(1.0, 1.0)))
		println("  * "+new Correlation(pairs0, new BetaFunction(0.9, 1.0)))
		println("  * "+new Correlation(pairs0, new BetaFunction(0.95, 1.0)))
		println("  * "+new Correlation(pairs0, new BetaFunction(1.05, 1.0)))
		println("  * "+new Correlation(pairs0, new BetaFunction(1.1, 1.0)))
		println("  + "+new Correlation(pairs0, new BetaFunction(1.0, 0.9)))
		println("  + "+new Correlation(pairs0, new BetaFunction(1.0, 0.95)))
		println("  + "+new Correlation(pairs0, new BetaFunction(1.0, 1.05)))
		println("  + "+new Correlation(pairs0, new BetaFunction(1.0, 1.1)))
		println("  - "+new Correlation(pairs0, new BetaFunction(0.05, 1.0)))
		println("  - "+new Correlation(pairs0, new BetaFunction(0.1, 1.0)))
		println("  - "+new Correlation(pairs0, new BetaFunction(0.5, 1.0)))
		println("  - "+new Correlation(pairs0, new BetaFunction(0.7, 1.0)))
		println("  - "+new Correlation(pairs0, new BetaFunction(0.8, 1.0)))
		println("  % "+new Correlation(pairs0, new BetaFunction(0.9, 0.9)))
		println("  % "+new Correlation(pairs0, new BetaFunction(0.0, 0.0)))
		println("  % "+new Correlation(pairs0, new BetaFunction(-1.0, 0.0)))
		println("  % "+new Correlation(pairs0, new BetaFunction(0.0, -1.0)))
		println("  % "+new Correlation(pairs0, new BetaFunction(-1.0, -1.0)))


		println(new LinearRegression(List((0,1),(1,2),(-3,-2))))
		val pairs1 = List((0.0,1.0),(1.0,2.0),(-3.0,-1.0))
		val lr1 = new LinearRegression(pairs1)
		println(lr1)
		println(new LinearRegression(List((0,1),(1,2),(-3,-1),(-5,1))))

		val cr1 = new Correlation(pairs1, lr1.func)
		println(pairs1)
		println("  "+cr1)
		cr1.R2 should equal (lr1.R2)


		println("  "+new Correlation(pairs1, new BetaFunction(0.731, 1.154)))
		println("  "+new Correlation(pairs1, new BetaFunction(0.7, 1.154)))
		println("  "+new Correlation(pairs1, new BetaFunction(0.731, 1.1)))


		List((0,1),(1,2),(-3,-1),(-5,1)).map(_._1).min should equal (-5)
	}

	test ("inc") {
		val t1 = MyStatistics.inc(-2.0, 1.0, 10.0)
				println("t1: "+t1.toList)
				t1.length should equal (13)
		t1.apply(5) should equal (3.0)
		t1.apply(1) should equal (-1.0)
		intercept[AssertionError] {
			MyStatistics.inc(0.0, -1.0, 2.0)
		}
		intercept[AssertionError] {
			MyStatistics.inc(0.0, 0.0, 2.0)
		}
		intercept[AssertionError] {
			MyStatistics.inc(0.0, 3.0, 2.0)
		}
		intercept[AssertionError] {
			MyStatistics.inc(2.0, 1.0, 0.0)
		}
		intercept[AssertionError] {
			MyStatistics.inc(0.0, 1.0, 1.0)
		}
	}

	ignore("angleLigneDHeure") {
		val i = new Instant(0,new XY(0,0))
		TimeConstants.angleLigneDHeure(i.latit, 0, 0) should equal (0)
		simplifiedWhen0(i.latit, 0) should equal (0)
		TimeConstants.angleLigneDHeure(i.latit, 3.45, 0) should equal (simplifiedWhen0(i.latit, 3.45))
		println("TimeConstants.angleLigneDHeure(i.latit, 3.45, 0): "+TimeConstants.angleLigneDHeure(i.latit, 3.45, 0))
		println("simplifiedWhen0(i.latit, 3.45): "+simplifiedWhen0(i.latit, 3.45))

		TimeConstants.angleLigneDHeure(i.latit, 1, 0) should equal (simplifiedWhen0(i.latit, 1))
		println("TimeConstants.angleLigneDHeure(i.latit, 1, 0): "+TimeConstants.angleLigneDHeure(i.latit, 1, 0))
		println("simplifiedWhen0(i.latit, 1): "+simplifiedWhen0(i.latit, 1))
		math.abs(simplifiedWhen0(0, 1)-15)<0.0001 should be (true)
		math.abs(TimeConstants.angleLigneDHeure(0, 1, 0)-15)<0.0001 should be (true)

		def simplifiedWhen0(latit: Double, heure: Double): Double = {
			// http://en.wikipedia.org/wiki/Sundial#Vertical_declining_dials
			math.toDegrees(math.atan(math.cos(math.toRadians(latit))*math.tan(math.toRadians(15*heure))))
		}


		val angleDuMurs = MyStatistics.inc(-25, 5, 25)
				figure(1)
				title("angleLigneUneHeure a "+i.latit+"deg de latitude")
				linePlotsOn
				plot(angleDuMurs, angleDuMurs.map(TimeConstants.angleLigneDHeure(0, 1, _)), Color.BLUE,"angleLigneUneHeure a 0deg de latitude en deg")
		plot(angleDuMurs, angleDuMurs.map(TimeConstants.angleLigneDHeure(i.latit, 1, _)), Color.RED,"angleLigneUneHeure a "+i.latit+"deg de latitude en deg")
		plot(angleDuMurs, angleDuMurs.map(TimeConstants.angleLigneDHeure(i.latit, 4, _)), Color.ORANGE,"angleLigneQuatreHeure a "+i.latit+"deg de latitude en deg")
		plot(angleDuMurs, angleDuMurs.map(TimeConstants.angleLigneDHeure(i.latit, -4, _)), Color.MAGENTA,"angleLigneHuitHeure a "+i.latit+"deg de latitude en deg")
		xlabel("x - angleDuMur en deg")
		ylabel("y - angleLigneUneHeure en deg")

		val heures = MyStatistics.inc(-4, 0.1, 4)
		val couleurStartHSB = Color.RGBtoHSB(0,0,255,null)
		val hueInc = 1.toFloat/angleDuMurs.size.toFloat
		var hue = 0.toFloat
		figure(2)
		title("angleLigneHeures a "+i.latit+"deg de latitude")
		linePlotsOn
		angleDuMurs.foreach((angleDuMur: Double) => {
			plot(heures.map(TimeConstants.angleLigneDHeure(i.latit, _, angleDuMur)), heures, Color.getHSBColor(couleurStartHSB.apply(0)+hue,couleurStartHSB.apply(1),couleurStartHSB.apply(2)),"angleDuMur: "+angleDuMur+" deg")
			hue += hueInc
		}
				)
				xlabel("x - angleLigneHeure en deg")
				ylabel("y - heures")

				L.createGui(new ZeParameters(List(("",new MyParameter("")))))
				L.Gui.getAndClose


	}

	ignore("scalalabBug") {
		figure(2)
		title("scalalabBug")
		scatterPlotsOn

		plot(List(-5.0,9.0).toArray, List(6.0,-3.0).toArray, 
				Color.BLUE,			    
				"scalalabBug")

		xlabel("x - x")
		ylabel("y - y")
		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose

	}

	ignore("plotFunc") {

		val hueInc = 1.toFloat/5.2.toFloat
		var hue = 0.toFloat
		figure(1)
		title("cowabunga")

		var func = new BetaFunction(1.0,50.0)
		L.myPrintDln(func.toString)
		func.plotFunc((-200.0,200.0,-200.0,100.0),1.0,func.toString,hue)
		hue +=hueInc
		func = new BetaFunction(-1.0,50.0)
		L.myPrintDln(func.toString)
		func.plotFunc((-200.0,200.0,-200.0,100.0),1.0,func.toString,hue)
		hue +=hueInc
		func = new BetaFunction(-10.0,50.0)
		L.myPrintDln(func.toString)
		func.plotFunc((-200.0,200.0,-200.0,100.0),1.0,func.toString,hue)
		hue +=hueInc
		func = new BetaFunction(10.0,50.0)
		L.myPrintDln(func.toString)
		func.plotFunc((-200.0,200.0,-200.0,100.0),1.0,func.toString,hue)
		hue +=hueInc
		func = new BetaFunction(1000.0,50.0)
		L.myPrintDln(func.toString)
		func.plotFunc((-200.0,200.0,-200.0,100.0),1.0,func.toString,hue)
		hue +=hueInc

		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose
		L.closeFiles()		
	}

	ignore("intersections") {
		new Intersections(List((1.0,50.0),(-1.0,50.0)).map(new BetaFunction(_))).toString should equal ((new XY(0.0,50.0),0.0).toString)

		val hueInc = 1.toFloat/7.3.toFloat
		var hue = 0.toFloat

		var lines = List((1.0,50.0),(-1.0,50.0),(-1.5,40.0),(0.5,30.0)).map(new BetaFunction(_))

		val intersectionsBar = new Intersections(lines)

		lines.foreach((line: BetaFunction) => {
			line.plotFunc((-50.0,30.0,0.0,80.0),1.0,line.toString,hue)
			hue += hueInc
		})

		L.myPrintDln(intersectionsBar.toString)
		scatterPlotsOn
		plot(List(intersectionsBar.bars.x).toArray, List(intersectionsBar.bars.y).toArray, Color.BLACK, intersectionsBar.toString)

		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose
		L.closeFiles()
	}


	test("Temps Solaire") {
		val instants = (new readCSV).instants

		L.myPrintDln(instants.map(_.toStringLong("ddMMM HH:mm")).mkString("\n"))


	}

	ignore("SolarStdTime") {
		var hue = 0.toFloat
		val timeType = "SolarStdTime"


		val instants = (new readCSV).instants
		val cadran = new CadranSolaireExperimental(instants, (-10, 5, 40), 0.01, timeType, "tout "+instants.size, null)
		plotUtils.angleDuMurPlot(cadran, timeType)

		val apres21juin = instants.filter(_.origTime > TimeConstants.juin21Millis)
		val cadranApres21juin = new CadranSolaireExperimental(apres21juin, (-20, 5, 40), 0.01, timeType, "apres21juin "+apres21juin.size, null)
		plotUtils.angleDuMurPlot(cadranApres21juin, timeType)

		val avant21juin = instants.filter(_.origTime < TimeConstants.juin21Millis)
		val cadranAvant21juin = new CadranSolaireExperimental(avant21juin, (-10, 5, 40), 0.01, timeType, "avant21juin "+avant21juin.size, null)
		plotUtils.angleDuMurPlot(cadranAvant21juin, timeType)


		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose
		L.closeFiles()
	}

	ignore("UNDERSTOOD!") {
		var hue = 0.toFloat
		val timeType = "SolarStdTime"


		val instants = (new readCSV).instants.filter(_.tempsDuJour(timeType) < -1)
		val cadran = new CadranSolaireExperimental(instants, (-10, 5, 40), 0.01, timeType, "tout "+instants.size, null)
		plotUtils.angleDuMurPlot(cadran, timeType)

		val apres21juin = instants.filter(_.origTime > TimeConstants.juin21Millis)
		val cadranApres21juin = new CadranSolaireExperimental(apres21juin, (-20, 5, 40), 0.01, timeType, "apres21juin "+apres21juin.size, null)
		plotUtils.angleDuMurPlot(cadranApres21juin, timeType)

		L.myErrPrintDln("****instants****")
	//	val intersectionsBar = new Intersections(instants.map((i: Instant) =>i.getLigneTheorique(timeType, cadran.angleDuMur)))
		L.myErrPrintDln("****Apres21juin****")
	//	val intersectionsBarApres21juin = new Intersections(apres21juin.map((i: Instant) =>i.getLigneTheorique(timeType, cadranApres21juin.angleDuMur)))



		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose
		L.closeFiles()
	}

	ignore("OKNOW!") {
		var hue = 0.toFloat

		val timeType = "SolarTime2"


		val instants = (new readCSV).instants

		val apres21juin = instants.filter(_.origTime > TimeConstants.juin21Millis)
		figure()
		val stitle = "variances"
		title(stitle)
		xlabel("x - "+stitle)
		ylabel("y - "+stitle)	  
		scatterPlotsOn
		val cadranApres21juin = new CadranSolaireExperimental(apres21juin, (-20, 5, 40), 0.01,timeType, "apres21juin "+apres21juin.size, plot)

		plotUtils.angleDuMurPlot(cadranApres21juin, timeType)
//		val intersectionsBar = new Intersections(apres21juin.map((i: Instant) =>i.getLigneTheorique(timeType, cadranApres21juin.angleDuMur)))
//		intersectionsBar.display

		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose
		L.closeFiles()
	}

	ignore("2lignesParalleles") {
		var hue = 0.toFloat

		val timeType = "SolarTime2"


		val instants = (new readCSV).instants

		val qquesminutes = instants.filter(_.origTime > TimeConstants.juin21Millis).filter((i: Instant) =>i.tempsDuJour(timeType)> 0 & i.tempsDuJour(timeType) < 1)
		val cadranqquesminutes = new CadranSolaireExperimental(qquesminutes, (-20, 5, 40), 0.01,timeType, "qquesminutes "+qquesminutes.size, null)

		plotUtils.angleDuMurPlot(cadranqquesminutes, timeType)
//		val intersectionsBar = new Intersections(qquesminutes.map((i: Instant) =>i.getLigneTheorique(timeType, cadranqquesminutes.angleDuMur)))
	//	intersectionsBar.display

		L.myPrintDln(qquesminutes.map(_.toStringLong("ddMMM HH:mm")).mkString("\n"))

		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose
		L.closeFiles()
	}

	ignore("displayInput") {
		// parsing simple csv file
		val l = scala.io.Source.fromFile(s_file).getLines.toList.foldLeft(List[List[String]]())(_:+_.split(",").toList)
		l.head.toString should equal ("List(date, heure, x, y)")
		val instants = l.tail.foldLeft(List[Instant]())(_:+ new Instant(_))
		L.myErrPrintDln(instants.mkString("\n"))

		/*		displayInput(instants, "origTime", "ddMMM", (i :Instant) => i.origTime)
		linePlotsOn
		plot(List(-100.0,0.0,100.0).toArray, List(-100.0,0.0,-100.0).toArray, Color.RED,"Just Trying...")
		displayInput(instants, "origTime", "HH", (i :Instant) => i.get("origTime","HH").toLong, displayGroup) */
		displayInput(instants, "SolarTime", "HH", (i :Instant) => i.get("SolarTime","HH").toLong, displayGroup2)

		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose

	}



	def displayInput(instants: List[Instant], timeType: String, by: String, 
			sortFunc: (Instant) => Long, displayFunc: ((String,List[Instant]), Float) => Int): List[(String, List[cadransolaire.Instant])] = {
							val groups = instants.groupBy(_.get(timeType,by)).toList.sortBy{(e: (String,List[Instant])) => sortFunc(e._2.head)}
					L.myErrPrintDln(groups.mkString("\n"))

					var hueInc = 1.toFloat/groups.size
					var hue = 0.toFloat
					figure()
					title(timeType +" by "+by)					
					groups.foreach((instantsDuGroupe: (String,List[Instant])) => {
						displayFunc(instantsDuGroupe, hue)
						hue += hueInc
					})
					xlabel("x - "+timeType)
					ylabel("y - "+timeType)
					groups
			}

			def displayGroup(instantsDuGroupe: (String,List[Instant]), hue: Float): Int = {	  
					var instantsDuGroupe2 = List[Instant]()
							if(instantsDuGroupe._2.size==2) { // scalalab bug
								instantsDuGroupe2 = instantsDuGroupe._2 :+ instantsDuGroupe._2.head
							} else {
								instantsDuGroupe2 = instantsDuGroupe._2
							}
					scatterPlotsOn
					plot(instantsDuGroupe2.map(_.xy.x).toArray, instantsDuGroupe2.map(_.xy.y).toArray, 
							getColor(hue),			    
							instantsDuGroupe._1+"["+instantsDuGroupe._2.size+"]")
					0
			}

			def displayGroup2(instantsDuGroupe: (String,List[Instant]), hue: Float): Int = {
							if(instantsDuGroupe._2.size>1) {
								val l = instantsDuGroupe._2.map((i :Instant) => (i.xy.x,i.xy.y))
										L.myPrintDln(l.toString)
										val lr = new LinearRegression(l)
								L.myPrintDln(instantsDuGroupe._1+" "+lr.s)
								if(lr.R2>0.7) {
									val x = MyStatistics.inc(math.min(l.map(_._1).min-10,0.0), 1, math.max(l.map(_._1).max+10,0.0))
											linePlotsOn
											plot(x, x.map(lr.func.func(_)), getColor(hue), lr.toString)	
									L.myPrintDln(instantsDuGroupe._1+" "+x.toString+" "+x.map(lr.func.func(_)).toString)
								}
							}
					displayGroup(instantsDuGroupe,hue)
					0
			}
}


class MainCadranSolaire2 extends FunSuite with ShouldMatchers {
	var figCounter = 0
    newMyLog(this.getClass.getName,new File("cowabunga"),"log")
    val s_file = new File("out\\cadransolaire_input2.csv")

			// parsing simple csv file
			val l = scala.io.Source.fromFile(s_file).getLines.toList.foldLeft(List[List[String]]())(_:+_.split(",").toList)
			l.head.toString should equal ("List(date, heure, x, y)")
	val instants = l.tail.foldLeft(List[Instant]())(_:+ new Instant(_))
	L.myErrPrintDln(instants.mkString("\n"))

	// validate 15deg = 1hour
	val lr = angleVsMinutes(instants, "SolarTime")
	//	angleVsMinutes(instants, "SolarTime2")

	val AngleDuMur = lr.func.revfunc(720)-90

	L.myErrPrintDln(("Angle du Mur: %3.3fdeg" format AngleDuMur))

	L.createGui(new ZeParameters(List(("",new MyParameter("")))))
	L.Gui.getAndClose
	true should be (true)
	L.closeFiles()

	def angleVsMinutes(instants: List[Instant], timeType: String): LinearRegression = {
		L.myErrPrintDln(instants.foldLeft(timeType+":\n")(_ + _.ThetaVsHourString(timeType)+", "))
		val ldeghour = instants.map((i: Instant) => i.ThetaVsHour(timeType))
		val lr = new LinearRegression(ldeghour)
		L.myErrPrintDln(lr.s)

		val x1 = MyStatistics.inc(ldeghour.map(_._1).min-1, 0.1, ldeghour.map(_._1).max+1)
		val y1 = x1 map lr.func.func


		val avant21juin = instants.filter(_.origTime < TimeConstants.juin21Millis).map((i: Instant) => i.ThetaVsHour(timeType))
		val apres21juin = instants.filter(_.origTime > TimeConstants.juin21Millis).map((i: Instant) => i.ThetaVsHour(timeType))

		figCounter += 1
		figure(figCounter)
		val latit = instants.head.latit
		title("angle vs minutes ["+timeType+"] a "+latit+"deg de latitude")
		linePlotsOn
		//		plot(x1, y1, Color.RED,"["+timeType+"] "+lr.toString())
		val angleDuMurs = MyStatistics.inc(-25, 5, 25)
		val heures = MyStatistics.inc(-4, 0.1, 4)
		val couleurStartHSB = Color.RGBtoHSB(0,0,255,null)
		val hueInc = 1.toFloat/angleDuMurs.size.toFloat
		var hue = 0.toFloat
		angleDuMurs.foreach((angleDuMur: Double) => {
			plot(heures.map(TimeConstants.angleLigneDHeure(latit, _, angleDuMur)), heures, Color.getHSBColor(couleurStartHSB.apply(0)+hue,couleurStartHSB.apply(1),couleurStartHSB.apply(2)),"angleDuMur: "+angleDuMur+" deg")
			hue += hueInc
		})

		scatterPlotsOn
		plot(avant21juin.map(_._1).toArray, avant21juin.map(_._2).toArray, Color.LIGHT_GRAY, "avant21juin")
		plot(apres21juin.map(_._1).toArray, apres21juin.map(_._2).toArray, Color.BLACK, "apres21juin")
		xlabel("x - Angle in deg")
		ylabel("y - DayTime in minutes")
		lr
	}

	test("SunDeclination") {	  
		val c = Calendar.getInstance
				c.set(2012,Calendar.MARCH,20)
				val sunDecl21mars = TimeConstants.sunDeclination(c.get(Calendar.DAY_OF_YEAR))
				println(printZisday(c,"ddMMMyy")+" "+c.get(Calendar.DAY_OF_YEAR)+" %2.2fdeg".format(sunDecl21mars))
				math.abs(sunDecl21mars)<0.25 should be (true)
		c.set(2012,Calendar.JUNE,21)
		val sunDecl21juin = TimeConstants.sunDeclination(c.get(Calendar.DAY_OF_YEAR))
		println(printZisday(c,"ddMMMyy")+" "+c.get(Calendar.DAY_OF_YEAR)+" %2.2fdeg".format(sunDecl21juin))
		math.abs(sunDecl21juin)-TimeConstants.inclinaison<0.25 should be (true)
		c.set(2012,Calendar.SEPTEMBER,22)
		val sunDecl21septembre = TimeConstants.sunDeclination(c.get(Calendar.DAY_OF_YEAR))
		println(printZisday(c,"ddMMMyy")+" "+c.get(Calendar.DAY_OF_YEAR)+" %2.2fdeg".format(sunDecl21septembre))
		math.abs(sunDecl21septembre)<0.25 should be (true)
		c.set(2012,Calendar.DECEMBER,21)
		val sunDecl21decembre = TimeConstants.sunDeclination(c.get(Calendar.DAY_OF_YEAR))
		println(printZisday(c,"ddMMMyy")+" "+c.get(Calendar.DAY_OF_YEAR)+" %2.2fdeg".format(sunDecl21decembre))
		math.abs(sunDecl21decembre)-TimeConstants.inclinaison<0.25 should be (true)
	}


	test("hauteurDuSoleilAMidi") {
		val i = new Instant(0,new XY(0,0))
		val jours = MyStatistics.inc(0, 10, 365)
		val hauteurDuSoleilAMidi = jours.map((jour: Double) => (jour,TimeConstants.hauteurDuSoleilAMidi(i.latit,jour.toInt),
				TimeConstants.solarElevationAngle(i.latit,0,jour.toInt)))
				println(hauteurDuSoleilAMidi.mkString("\n"))

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
	}

	ignore("heures_lever_coucher") {
		val i = new Instant(0,new XY(0,0))
		val heures = MyStatistics.inc(-5, 0.5, 5)
		val hauteursDuSoleil10jan2012 = heures.map((heure: Double) =>TimeConstants.solarElevationAngle(i.latit,heure,10))

		figure()
		title("hauteursDuSoleil10jan2012")
		linePlotsOn
		plot(heures, hauteursDuSoleil10jan2012, Color.RED,"hauteursDuSoleil10jan2012 en deg")
		xlabel("x - heure du jour")
		ylabel("y - hauteursDuSoleil10jan2012 en deg")

		/* http://www.imcce.fr/fr/ephemerides/phenomenes/rts/rts.php
		     Lieu :  Jougne, Doubs, FR	06deg23'23" E / 46deg45'38" N
Date 	    		Lever 		Passage au meridien Coucher
Temps Universel 	heure 	azimut 	heure 	hauteur heure 	azimut
2012-01-10 			7.32 	122.43 	11.70 	21.24 	16.07 	237.66
2012-01-11 			7.32 	122.19 	11.70 	21.39 	16.09 	237.90
2012-01-12 			7.31 	121.94 	11.71 	21.54 	16.11 	238.15
		 */

		val HauteurDuSoleil10jan2012_7h30_longit46deg = TimeConstants.solarElevationAngle(i.latit,7.5-12.0,10)
		println("Hauteur du Soleil le 10 janvier 2012 a 7h30: %2.2fdeg".format(HauteurDuSoleil10jan2012_7h30_longit46deg))
		math.abs(HauteurDuSoleil10jan2012_7h30_longit46deg)<2 should be (true)

		val HauteurDuSoleil12jan2012_7h30_longit46deg = TimeConstants.solarElevationAngle(i.latit,7.5-12.0,12)
		println("Hauteur du Soleil le 12 janvier 2012 a 7h30: %2.2fdeg".format(HauteurDuSoleil12jan2012_7h30_longit46deg))
		math.abs(HauteurDuSoleil12jan2012_7h30_longit46deg)<1.5 should be (true)

		val HauteurDuSoleil12jan2012_16h00_longit46deg = TimeConstants.solarElevationAngle(i.latit,4.0,12)
		println("Hauteur du Soleil le 12 janvier 2012 a 16h00: %2.2fdeg".format(HauteurDuSoleil12jan2012_16h00_longit46deg))
		math.abs(HauteurDuSoleil12jan2012_16h00_longit46deg)<3 should be (true)

		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose
	}

	ignore("azimuts_lever_coucher") {
		val i = new Instant(0,new XY(0,0))
		val heures = MyStatistics.inc(-6, 0.5, 6)
		val azimutsDuSoleil10jan2012 = heures.map((heure: Double) =>TimeConstants.angleLigneDHeure(i.latit, heure, 0.0))

		figure()
		title("azimutsDuSoleil10jan2012")
		linePlotsOn
		plot(heures, azimutsDuSoleil10jan2012, Color.RED,"azimutsDuSoleil10jan2012 en deg")
		xlabel("x - heure du jour")
		ylabel("y - azimutsDuSoleil10jan2012 en deg")

		/* http://www.imcce.fr/fr/ephemerides/phenomenes/rts/rts.php
		     Lieu :  Jougne, Doubs, FR	06deg23'23" E / 46deg45'38" N
Date 	    		Lever 		Passage au meridien Coucher
Temps Universel 	heure 	azimut 	heure 	hauteur heure 	azimut
2012-01-10 			7.32 	122.43 	11.70 	21.24 	16.07 	237.66
2012-01-11 			7.32 	122.19 	11.70 	21.39 	16.09 	237.90
2012-01-12 			7.31 	121.94 	11.71 	21.54 	16.11 	238.15
2012-02-28 			6.31 	101.31 	11.78 	35.15 	17.27 	258.94
2012-02-29 	 		6.28 	100.76 	11.78 	35.53 	17.30 	259.50
2012-03-01 			6.25 	100.20 	11.78 	35.91 	17.32 	260.06
2012-03-02 			6.22 	99.64 	11.77 	36.29 	17.34 	260.62
2012-03-03 			6.19 	99.08 	11.77 	36.68 	17.37 	261.18
2012-03-04 			6.16 	98.51 	11.77 	37.06 	17.39 	261.75
2012-03-05 			6.12 	97.95 	11.76 	37.45 	17.42 	262.32
2012-03-06 			6.09 	97.38 	11.76 	37.84 	17.44 	262.89
2012-03-07 			6.06 	96.81 	11.76 	38.22 	17.46 	263.46
2012-03-08 			6.03 	96.24 	11.75 	38.61 	17.49 	264.03
2012-03-09 			6.00 	95.67 	11.75 	39.01 	17.51 	264.60

		 */

		/*	val azimutDuSoleil10jan2012_7h30_longit46deg = TimeConstants.solarElevationAngle(i.latit,7.5-12.0,10)
		println("azimut du Soleil le 10 janvier 2012 a 7h30: %2.2fdeg".format(azimutDuSoleil10jan2012_7h30_longit46deg))
		math.abs(azimutDuSoleil10jan2012_7h30_longit46deg)<2 should be (true)

		val azimutDuSoleil12jan2012_7h30_longit46deg = TimeConstants.solarElevationAngle(i.latit,7.5-12.0,12)
		println("azimut du Soleil le 12 janvier 2012 a 7h30: %2.2fdeg".format(azimutDuSoleil12jan2012_7h30_longit46deg))
		math.abs(azimutDuSoleil12jan2012_7h30_longit46deg)<1.5 should be (true)

		val azimutDuSoleil12jan2012_16h00_longit46deg = TimeConstants.solarElevationAngle(i.latit,4.0,12)
		println("azimut du Soleil le 12 janvier 2012 a 16h00: %2.2fdeg".format(azimutDuSoleil12jan2012_16h00_longit46deg))
		math.abs(azimutDuSoleil12jan2012_16h00_longit46deg)<3 should be (true) */

		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose
	}

	test("QuadraticRoots") {
		val equations=Array(
				(1.0, 22.0, -1323.0),   // two distinct real roots
				(6.0, -23.0, 20.0),     //   with a != 1.0
				(1.0, -1.0e9, 1.0),     //   with one root near zero
				(1.0, 2.0, 1.0),        // one real root (double root)
				(1.0, 0.0, 1.0),        // two imaginary roots
				(1.0, 1.0, 1.0)         // two complex roots
				)

				equations.foreach{v =>
				val (a,b,c)=v
				println("a=%g   b=%g   c=%g".format(a,b,c))
				val roots=QuadraticRoots.solve(a, b, c)
				println(roots)
		}
	}

	test("angleSousStylaire") {
		val angleDuMur = 6
				val angleSousStylaire = TimeConstants.angleSousStylaireGnomon(46, angleDuMur)
				val angleDuMur2 = TimeConstants.angleSousStylaireGnomonRev(46, angleSousStylaire)
				angleDuMur2 should equal (angleDuMur)
	}


	test("SolarTime") {
		val timeType = "SolarTime"

		val instants = (new readCSV).instants.filter(_.tempsDuJour("SolarTime").abs < 2.0)
		val cadran = new CadranSolaireExperimental(instants, (-10, 5, 40), 0.01, timeType, ""+instants.size, null)
	/*	angleDuMurPlot(cadran, timeType)

		val angleDuMur1 = cadran.angleDuMur
		val angleSousStylaire1 = cadran.centreDuCadran.getAngle	
		
		cadran.update(TimeConstants.angleSousStylaireGnomonRev(instants.head.latit, angleSousStylaire1))
		angleDuMurPlot(cadran, timeType)

		val angleDuMur2 = cadran.angleDuMur	
		val angleSousStylaire2 = cadran.centreDuCadran.getAngle
		
		cadran.update(TimeConstants.angleSousStylaireGnomonRev(instants.head.latit, angleSousStylaire2))
		angleDuMurPlot(cadran, timeType)

		val angleDuMur3 = cadran.angleDuMur	
		val angleSousStylaire3 = cadran.centreDuCadran.getAngle
		L.myPrintDln("angleSousStylaire1 %2.2fdeg".format(angleSousStylaire1)+", angleDuMur1 %2.2fdeg".format(angleDuMur1))
		L.myPrintDln("angleSousStylaire2 %2.2fdeg".format(angleSousStylaire2)+", angleDuMur2 %2.2fdeg".format(angleDuMur2))
		L.myPrintDln("angleSousStylaire3 %2.2fdeg".format(angleSousStylaire3)+", angleDuMur3 %2.2fdeg".format(angleDuMur3))*/

		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose
		L.closeFiles()
	}
	
	ignore("MatchCentreDuCadran") {
		val timeType = "SolarTime"

		val instants = (new readCSV).instants.filter(_.tempsDuJour("SolarTime").abs < 2.0)
		val cadran = new CadranSolaireExperimental(instants, (-10, 5, 40), 0.01, timeType, ""+instants.size, null)
		/*angleDuMurPlot(cadran, timeType)

		val angle1 = cadran.centreDuCadran.getAngle	
		L.myPrintDln("angle1 %2.2fdeg".format(angle1))
		cadran.update(angle1)
		angleDuMurPlot(cadran, timeType)

		val angle4 = cadran.centreDuCadran.getAngle	
		L.myPrintDln("angle4 %2.2fdeg".format(angle4))


				val angle2 = TimeConstants.angleSousStylaireGnomon(instants.head.latit, cadran.angleDuMur)		
		L.myPrintDln("angle2 %2.2fdeg".format(angle2))


		val angle3 = TimeConstants.angleSousStylaireGnomon(instants.head.latit, angle1)		
		L.myPrintDln("angle3 %2.2fdeg".format(angle3))*/


		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose
		L.closeFiles()
	}
	
	

}
