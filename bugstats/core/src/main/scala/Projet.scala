package bugstats

import java.util.Calendar
import bugstats.DefectState.DefectState

import scala.util.Random
import kebra.DateDSL._
import kebra.MyLog._
import kebra._
import kebra.MyLog
import language.postfixOps

class Projet(val p: String, val bugsi: List[Defect], val endDate: Calendar, val rangeeMaitresse: List[(DefectState, String)]) {
  val L = getMylog
  var rangees = List[Rangee]()

  val bugs = p match {
    case "" => bugsi
    case _ => bugsi.filter(d => d.product == p)
  }

  val zendDate = new DateDSL(now is endDate)
  var ago = new DateDSL(now is endDate minus (26 * 7) days)
  myPrintln("      ago: " + ago + ", endDate: " + MyLog.printZisday(endDate, "ddMMMyy") + ", zendDate: " + zendDate)
  while (ago.before(zendDate)) {
    rangees = rangees :+ new Rangee(ago.cal, rangeeMaitresse.map(rm => bugs.filter(_.getStatus(ago.cal) == rm).size))
    ago plus 7 days
  }
  rangees = rangees :+ new Rangee(ago.cal, rangeeMaitresse.map(rm => bugs.filter(_.getStatus(ago.cal) == rm).size))
  rangees = rangees.filter(_.active)
  println("\n" + p + " " + rangeeMaitresse + "\n  " + rangees.mkString("\n  "))

  def isAlive: Boolean = if (rangees.isEmpty) {
    false
  } else {
    rangees.last.r != rangees.head.r
  }

  def activity = if (rangees.isEmpty) {
    0
  } else {
    rangees.last.r.sum - rangees.dropRight(1).last.r.sum
  }

  //{"name": "AxisLayout", "size": 6725}
  def json(zdate: DateDSL) = {
    val rangee = (rangees.reverse.find(_.c before zdate) match {
      case Some(r) => r
      case _ => rangees.last
    })

    "{\"name\": \"" + p + "\", \"totalBugs\": \"" + rangee.r.sum + "\", \"openBugs\": \"" + rangee.r.take(4).sum +
      "\", \"openAndQaBugs\": \"" + rangee.r.take(5).sum + "\", \"openP0_P1Bugs\": \"" + rangee.r.take(2).sum + "\"}"
  }

}
