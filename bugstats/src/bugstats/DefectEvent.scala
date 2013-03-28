package bugstats
import java.util.Calendar
import DefectState._
import kebra.MyLog._


class DefectEvent(val c: Calendar, val d: DefectState) {
	c.clear(Calendar.HOUR)
	override def toString: String = "["+printZisday(c,"dMMMyyyy")+": "+d+"]"
}