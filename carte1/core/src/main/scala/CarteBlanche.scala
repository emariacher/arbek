package carte

import kebra.MyLog

import scala.math._

class CarteBlanche {
  val lcarres = new ListCarres
  val lregions = new ListRegions

  def addCarre(timeout: Int): StateMachine = {
    var c = new Carre
    var timeout_cpt = max(20-timeout,1)

    while ((!c.isLast)&(timeout_cpt>0)) {
      c = new Carre
      lcarres.lc = lcarres.lc :+ c
      //			System.out.println(MyLog.tag(1)+" lcarres.l: "+lcarres.lc)
      timeout_cpt -=1
      //MyLog.myPrintIt(timeout, timeout_cpt)
    }
    MyLog.myPrintIt(timeout, lcarres.lc.length)
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
    //				System.out.println(MyLog.tag(1)+" lregions.l: "+lregions.lr)
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