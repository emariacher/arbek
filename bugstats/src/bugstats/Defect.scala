package bugstats

import java.util.Calendar
import DefectState._

class Defect(val m: Map[String, String], val h: List[DefectEvent]) {
	val product = m.getOrElse("product","NOGOOD!")
			val priority = m.getOrElse("priority","NOGOOD!")
			val dtype = m.getOrElse("type","NOGOOD!")
			val number = m.getOrElse("number","NOGOOD!")
			val summary = m.getOrElse("summary","NOGOOD!")

			def getStatus(c: Calendar): (DefectState,String) = {
		val z = h.filter(e => {
			//println(MyLog.printZisday(c,"  MM/dd/yyyy HH:mm a ")+MyLog.printZisday(e.c,"MM/dd/yyyy HH:mm a ")+e.c.compareTo(c)+" "+e.d)
			e.c.compareTo(c)<=0
		}).sortBy{_.c.getTimeInMillis}
		if(z.isEmpty) {
			(DefectState.DONTCARE,"")
		} else {
			z.last.d match {
			case DefectState.OPENED => (DefectState.OPENED,priority)
			case _ => (z.last.d,"")
			}
		}
	}

	override def toString: String = m.toString+" "+h.toString
}