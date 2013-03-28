package bugstats
import java.util.Calendar
import scala.util.Random
import kebra.DateDSL._
import kebra.MyLog._
import kebra._
import kebra.MyLog
import language.postfixOps

object Projet {
	val rangeeMaitresse = List((DefectState.OPENED,"P0"), (DefectState.OPENED,"P1"), (DefectState.OPENED,"P2"), (DefectState.OPENED,"P3"), 
			(DefectState.INQA,""), (DefectState.CLOSED,""))  
}

class Projet(val p: String, val bugsi: List[Defect], val endDate: Calendar) {
	val L = getMylog
			var rangees = List[Rangee]()

			val bugs = p match {
			case "" => bugsi
			case _  => bugsi.filter(d => d.product == p)
	}

	val zendDate = new DateDSL(now is endDate)
			var ago = new DateDSL(now is endDate minus (26*7) days)
	L.myPrintln("      ago: "+ago+", endDate: "+MyLog.printZisday(endDate,"ddMMMyy")+", zendDate: "+zendDate)
	while(ago.before(zendDate)) {
		rangees = rangees :+ new Rangee(ago.cal,Projet.rangeeMaitresse.map(rm => bugs.filter(_.getStatus(ago.cal)==rm).size))
		ago plus 7 days
	}
	rangees = rangees :+ new Rangee(ago.cal,Projet.rangeeMaitresse.map(rm => bugs.filter(_.getStatus(ago.cal)==rm).size))
	rangees = rangees.filter(_.active)
	println("\n"+p+" "+Projet.rangeeMaitresse+"\n  "+rangees.mkString("\n  "))

	def isAlive: Boolean = rangees.last.r != rangees.head.r

}