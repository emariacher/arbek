package graphlayout

import java.awt.Dimension
import java.util.Calendar

import graphlayout.Tableaux.tbx
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
  var ts = 0
  var lc = List.empty[Carre]

  var ltimestamps = List[Long](0)
  var t_startAkka: Calendar = _
  var nrOfWorkers = 4


  def doZeJob(command: String, graphic: Boolean) {
    //l.myPrintDln(state + " cg: " + countGenere + " ca: " + countAvance + " " + command)
    if (graphic) {
      zp.lbl.text = "Seed: " + seed + ", TimeStamp: " + ts + " " + state
    }
    state match {
      case StateMachine.rassemble =>
        state = graph.rassemble
      case StateMachine.ouestlajaffe =>
        state = graph.ouestlajaffe
      case StateMachine.travaille =>
        state = graph.travaille
      case StateMachine.reset => state = graph.reset
      case _ =>
    }
    ts += 1
  }

  def getNextSeed: Int = {
    seedIndex += 1
    if (seedIndex < seeds.size) {
      seeds.apply(seedIndex - 1)
    } else {
      Calendar.getInstance.getTimeInMillis.toInt
    }
  }


  def findCarre(rc2f: RowCol): Carre = {
    var z = lc.find(_.rc.equals(rc2f))
    if (z.isEmpty) {
      null
    } else {
      z.head
    }
  }

  def findCarre(x: Double, y: Double): Carre = findCarre(new RowCol(y.toInt * maxRow / tbx.zp.hauteur, x.toInt * maxCol / tbx.zp.largeur))
}

case class StateMachine private(state: String) {
  override def toString = "State_" + state
}

object StateMachine {
  val rassemble = StateMachine("rassemble")
  val ouestlajaffe = StateMachine("ouEstLaJaffe")
  val travaille = StateMachine("travaille")
  val reset = StateMachine("reset")
}

case class MouseStateMachine private(state: String) {
  override def toString = "MouseState_" + state
}

object MouseStateMachine {
  val drag = MouseStateMachine("drag")
  val reset = MouseStateMachine("reset")
}

