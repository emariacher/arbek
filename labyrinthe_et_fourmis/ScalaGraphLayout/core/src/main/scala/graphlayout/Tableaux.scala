package graphlayout

import java.awt.Dimension
import java.util.Calendar

import graphlayout.Tableaux.tbx

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
  val maxWorkers = 5
  val mperfs = scala.collection.mutable.Map.empty[Int, List[Double]]
  var state = StateMachine.reset
  var oldstate = StateMachine.reset
  var seed: Int = _
  var rnd: Random = _
  var ts = 0
  var lc = List.empty[Carre]

  var ltimestamps = scala.collection.mutable.Map[StateMachine, Int]()
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
      case StateMachine.croisiere =>
        state = graph.travaille
      case StateMachine.reset => state = graph.reset
      case _ =>
    }
    ltimestamps(state) = ltimestamps.getOrElse(state, 0) + 1
    ts += 1
  }

  def getNextSeed: Int = Calendar.getInstance.getTimeInMillis.toInt

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
  override def toString = state
}

object StateMachine {
  val rassemble = StateMachine("rassemble")
  val ouestlajaffe = StateMachine("ouEstLaJaffe")
  val travaille = StateMachine("travaille")
  val croisiere = StateMachine("croisiere")
  val reset = StateMachine("reset")
}

case class MouseStateMachine private(state: String) {
  override def toString = "MouseState_" + state
}

object MouseStateMachine {
  val drag = MouseStateMachine("drag")
  val reset = MouseStateMachine("reset")
}

