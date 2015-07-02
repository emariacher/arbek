package bugstats

import java.util.Calendar
import DefectState._
import kebra.MyLog._
import kebra._
import kebra.MyLog
import kebra.DateDSL._


class Requirement(override val m: Map[String, String], override val h: List[DefectEvent]) extends Defect(m, h) {

  override def getStatus(c: Calendar): (DefectState, String) = {
    val z = h.sortBy {
      _.c.getTimeInMillis
    }

    if (z.isEmpty) {
      (DefectState.DONTCARE, "")
    } else {
      if (z.head.c.compareTo(c) < 0) {
        if ((wdate1 is z.last.c).before((wdate2 is c) minus 1 month)) {
          (DefectState.STABLE, "")
        } else if ((wdate1 is z.last.c).before((wdate2 is c) minus 7 days)) {
          println("STABILIZING " + number + " " + ((wdate2 is c) minus 7 days) + " " + (wdate1 is z.last.c))
          (DefectState.STABILIZING, "")
        } else {
          println("MODIFYING " + number + " " + ((wdate2 is c) minus 7 days) + " " + (wdate1 is z.last.c))
          (DefectState.MODIFYING, "")
        }
      } else {
        (DefectState.DONTCARE, "")
      }
    }
  }
}