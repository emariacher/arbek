package bugstats

import java.util.Calendar
import kebra.MyLog._


class Tests(bugs: List[Defect]) {
	def testGetState = {
		val bug = bugs.find(_.number=="5087") match {
		case Some(b) => b
		case _ => new Defect(Map[String, String](), List[DefectEvent]())
		}
		println(bug)
		val c = Calendar.getInstance
		c.set(2012,Calendar.FEBRUARY,15,10,37)
		println(bug.m.getOrElse("number","NOGOOD!")+printZisday(c," MM/dd/yyyy HH:mm:ss a ")+bug.getStatus(c))
		require(bug.getStatus(c)==(DefectState.DONTCARE,""))
		c.set(2012,Calendar.MARCH,22,10,37)
		println(bug.m.getOrElse("number","NOGOOD!")+printZisday(c," MM/dd/yyyy HH:mm:ss a ")+bug.getStatus(c))
		require(bug.getStatus(c)==(DefectState.INQA,""))
		c.set(2012,Calendar.MARCH,22,13,23)
		println(bug.m.getOrElse("number","NOGOOD!")+printZisday(c," MM/dd/yyyy HH:mm:ss a ")+bug.getStatus(c))
		require(bug.getStatus(c)==(DefectState.CLOSED,""))
		c.set(2012,Calendar.MAY,22,13,23)
		println(bug.m.getOrElse("number","NOGOOD!")+printZisday(c," MM/dd/yyyy HH:mm:ss a ")+bug.getStatus(c))
		require(bug.getStatus(c)==(DefectState.CLOSED,""))
	}
	

}