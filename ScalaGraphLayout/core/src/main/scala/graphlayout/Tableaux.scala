package graphlayout

import java.awt.Dimension
import java.util.Calendar

import kebra._

import scala.collection.immutable._
import scala.util.Random

object Tableaux {
  var tbx: Tableaux = _

  def newTbx(zp: ZePanel, maxRC: RowCol, size: Dimension, origin: Dimension, graph: GraphAbstract) {
    tbx = new Tableaux(zp, maxRC, size, origin, graph)
  }
}

class Tableaux(val zp: ZePanel, val maxRC: RowCol, val size: Dimension, val origin: Dimension, val graph: GraphAbstract) {
  val maxRow = maxRC.r
  val maxCol = maxRC.c
  //val seeds = List(0, -1258712602, -2003116831, -2000188942, -2003116831, -1172155944) // valeurs interessantes pour un 10X10
  val seeds = (0 until 11).toList :+ (-1171074276)
  // valeurs interessantes pour un 20X20
  val maxWorkers = 5
  val mperfs = scala.collection.mutable.Map.empty[Int, List[Double]]
  var state = StateMachine.reset
  var oldstate = StateMachine.reset
  var seedIndex = 0
  var seed: Int = _
  var rnd: Random = _
  var countGenere = 0
  var countAvance = 0
  var lc = List.empty[Carre]

  var ltimestamps = List[Long](0)
  var t_startAkka: Calendar = _
  var nrOfWorkers = 4


  def doZeJob(command: String, graphic: Boolean) {
    //l.myPrintDln(state + " cg: " + countGenere + " ca: " + countAvance + " " + command)
    if (graphic) {
      zp.lbl.text = "Seed: " + seed + ", CountSteps: " + countGenere + " " + state
    }
    state match {
      case StateMachine.rassemble =>
        state = graph.rassemble
        countGenere += 1
      case StateMachine.nettoie => state = nettoie
      case StateMachine.avance =>
        state = avance
        countAvance += 1
      case StateMachine.reset => state = graph.reset
      case StateMachine.termine =>
        if (graphic) {
          LL.l.myErrPrintln(zp.lbl.text)
          if ((command == "step") || zp.run) {
            zp.pause = false
          } else {
            zp.pause = true
          }
        }
        state = StateMachine.reset
      case _ =>
    }
  }

  def nettoie: StateMachine = {
    lc.foreach(_.nettoie)

    StateMachine.avance
  }

  def avance: StateMachine = {
    StateMachine.avance
  }

  def getNextSeed: Int = {
    seedIndex += 1
    if (seedIndex < seeds.size) {
      seeds.apply(seedIndex - 1)
    } else {
      Calendar.getInstance.getTimeInMillis.toInt
    }
  }


  def updateStats(lrjc: List[(Couleur, Int, Int)]) {
    if (seedIndex > 10) {
      val ljArrivesAuBout = lrjc.filter((ci: (Couleur, Int, Int)) => ci._2 > 0 && ci._2 < zp.limit).map((ci: (Couleur, Int, Int)) => ci._2)
      val timeStamp = MyLog.timeStamp(t_startAkka)
      val perf = ljArrivesAuBout.sum * 1000.0 / (nrOfWorkers * ljArrivesAuBout.length * timeStamp)
      mperfs(nrOfWorkers) = mperfs.getOrElse(nrOfWorkers, List.empty[Double]) :+ perf
      //L.myPrintDln(seedIndex+" "+mperfs)
    }
    state = StateMachine.termine
  }

  def findCarre(rc2f: RowCol) = {
    var z = lc.find(_.rc.equals(rc2f))
    if (z.isEmpty) {
      null
    } else {
      z.head
    }
  }
}

case class StateMachine private(state: String) {
  override def toString = "State_" + state
}

object StateMachine {
  val rassemble = StateMachine("rassemble")
  val getJetons = StateMachine("getJetons")
  val nettoie = StateMachine("nettoie")
  val avance = StateMachine("avance")
  val attend = StateMachine("attend")
  val termine = StateMachine("termine")
  val reset = StateMachine("reset")
}

case class MouseStateMachine private(state: String) {
  override def toString = "MouseState_" + state
}

object MouseStateMachine {
  val drag = MouseStateMachine("drag")
  val reset = MouseStateMachine("reset")
}

object CompareStatJeton extends Ordering[(Couleur, StatJeton)] {
  def compare(x: (Couleur, StatJeton), y: (Couleur, StatJeton)): Int = {
    if (y._2.max == x._2.max) y._2.min - x._2.min
    else y._2.max - x._2.max
  }
}
