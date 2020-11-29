package carte

import kebra.{LL, MyLog}

import scala.math._

class CarteBlanche {
  val lcarres = new ListCarres
  val lregions = new ListRegions

  def addCarre(timeout: Int): StateMachine = {
    var c: Carre = null
    var timeout_cpt = max(20 - timeout, 1)

    do {
      c = new Carre
      lcarres.lc = lcarres.lc :+ c
      timeout_cpt -= 1
      //MyLog.myPrintIt(timeout, timeout_cpt)
    } while ((!c.isLast) & (timeout_cpt > 0))
    //MyLog.myPrintIt(timeout, lcarres.lc.length)
    if (c.isLast) {
      StateMachine.repandRegions
    } else {
      StateMachine.generate
    }
  }

  def repandRegions: StateMachine = {
    lcarres.lc.find(!_.allCantonsKnown) match {
      case Some(c) => c.repandRegions
        StateMachine.repandRegions
      case _ => StateMachine.cleanRegions
    }
  }

  def cleanRegions: StateMachine = {
    lcarres.lc.foreach(_.cleanRegions)
    StateMachine.getRegions
  }

  def getRegions: StateMachine = {
    lcarres.lc.foreach(_.getRegions)
    MyLog.myPrintIt(lregions.lr.length)
    StateMachine.colorie
  }

  override def toString: String = {
    if (lregions.lr.isEmpty) {
      "(" + lregions.lr.size + ",)"
    } else {
      "(" + lregions.lr.size + "," + (lregions.lr.tail.foldLeft(lregions.lr.head.voisins.size)(_ + _.voisins.size)) + ")"
    }
  }
}