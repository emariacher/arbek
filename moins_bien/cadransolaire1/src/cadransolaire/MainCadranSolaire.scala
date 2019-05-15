package cadransolaire

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfter
import java.util.Calendar
import _root_.scalaSci.math.plot.plot._
import _root_.scalaSci.math.plot.canvas._
import java.awt.Color
import java.awt.Font
import java.util.Date
import scala.collection.immutable.ListSet
import kebra.MyLog._
import kebra.MyFileChooser
import java.io.File
import kebra.ZeParameters
import kebra.MyParameter

// scala org.scalatest.tools.Runner -p . -o -s cadransolaire.MainCadranSolaire

// parameters.getValue("45zob").toInt should equal (1)
// projects.map(_.getDatabase.getName).contains("COCO") should be (false)

class MainCadranSolaire extends FunSuite with ShouldMatchers {
	   newMyLog("Cadran Solaire", new File("cowabunga"),"log")
    val s_file = new File("out\\cadransolaire_input2.csv")

	test("SolarTime") {
		
		val timeType = "SolarTime"

		//		val instants = (new readCSV).instants.filter(_.tempsDuJour("SolarTime").abs < 2.0)
		val instants = (new readCSV).instants
		val cadran = new CadranSolaireExperimental(instants, (-40, 5, 40), 0.01, timeType, ""+instants.size, null)
				plotUtils.angleDuMurPlot(cadran, timeType)


		L.createGui(new ZeParameters(List(("",new MyParameter("")))))
		L.Gui.getAndClose
		L.closeFiles()
	}

}

class SandBox extends FunSuite with ShouldMatchers with BeforeAndAfter {

	test("angleSousStylaire") {
		val angleDuMur = -6.7
				val angleSousStylaire = TimeConstants.angleSousStylaireGnomon(46, angleDuMur)
				val angleDuMur2 = TimeConstants.angleSousStylaireGnomonRev(46, angleSousStylaire)
				(angleDuMur2-angleDuMur).abs<0.001 should be (true)
		val	angleHauteurGnomon = TimeConstants.angleHauteurGnomon(46, angleDuMur)
		println("angleDuMur: %2.2fdeg".format(angleDuMur)+", angleSousStylaire: %2.2fdeg".format(angleSousStylaire)+", angleHauteurGnomon: %2.2fdeg".format(angleHauteurGnomon))
		val angleHauteurGnomon2 = math.toDegrees(math.atan(30/31.8))
		println("angleHauteurGnomon2: %2.2fdeg".format(angleHauteurGnomon2))
	}


}
