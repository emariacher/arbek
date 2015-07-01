package bugstats

import java.util.Calendar
import DefectState._

class Requirement(override val m: Map[String, String], override val h: List[DefectEvent]) extends Defect(m,h) {

			override def getStatus(c: Calendar): (DefectState,String) = {
		val z = h.sortBy{_.c.getTimeInMillis}
		if(z.isEmpty) {
			(DefectState.DONTCARE,"")
		} else {
		  if(z.head.c.compareTo(c)<=0) {
		  if(z.last.c.compareTo(c)<=0) {
		    (DefectState.CLOSED,"")
		  } else  {
		    (DefectState.INQA,"")
		  }
		  } else {
		    (DefectState.DONTCARE,"")
		  }
		}
	}
}