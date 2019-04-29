package graphlayout

import java.awt.{Color, Graphics2D}

import graphlayout.Tableaux.tbx
import kebra.MyLog
import org.apache.commons.math3.distribution._

import scala.collection.immutable.List
import scala.util.Random

class Fourmi(val anode: ANode) {
  var direction: Double = .0
  var jnode: JNode = _
  var state: FourmiStateMachine = FourmiStateMachine.cherche
  var log = List[(Int, Int, FourmiStateMachine)]()
  var index: Int = _
  var estRevenueALaFourmiliere = 0
  val distributionAvecPheromone = new UniformRealDistribution()
  val influenceDesPheromones = .05
  var oldDistance = .0
  var trigger = true

  override def toString = "[%.0f,%.0f]".format(anode.x, anode.y) + anode.tribu

  def avance(lc: List[Carre]) = {
    val zeCarre = tbx.findCarre(anode.x, anode.y)
    val lVoisinsAvecDepot = zeCarre.getVoisins(lc).filter(c => {
      c.getDepot(anode.tribu) != .0
    }).sortBy(c => c.getDepot(anode.tribu)).reverse
    if (!lVoisinsAvecDepot.isEmpty) {
      val sommeDesPheromones = lVoisinsAvecDepot.map(_.getDepot(anode.tribu)).sum
      var somme = influenceDesPheromones
      val lvoisinsAvecDepotIncremente: List[Double] = List(influenceDesPheromones) ++ lVoisinsAvecDepot.map(c => {
        somme += c.getDepot(anode.tribu) / (sommeDesPheromones * (1 + influenceDesPheromones))
        somme
      })
      val proba = distributionAvecPheromone.sample
      val ltake = lvoisinsAvecDepotIncremente.takeWhile(_ < proba)
      if (lVoisinsAvecDepot.length > 3) {
        MyLog.myPrintln("\n", toString, zeCarre, "%.1f".format(lVoisinsAvecDepot.map(_.getDepot(anode.tribu)).sum),
          lVoisinsAvecDepot.map(c => (c, "%.1f".format(c.getDepot(anode.tribu)))).mkString(","))
        MyLog.myPrintln(lvoisinsAvecDepotIncremente.map("%.3f".format(_)).mkString("-"))
        MyLog.myPrintln("%.3f + ".format(proba), ltake.map("%.3f".format(_)).mkString("-"), ltake.length)
        if (ltake.length > 1) {
          MyLog.myPrintIt(lVoisinsAvecDepot.apply(ltake.length - 1))
        }
      }

      if (ltake.length > 1) {
        val XYCaseAPheromoneChoisie = lVoisinsAvecDepot.apply(ltake.length - 1).getXY
        anode.x = XYCaseAPheromoneChoisie._1
        anode.y = XYCaseAPheromoneChoisie._2
      } else {
        avanceAPeuPresCommeAvant
      }
    } else {
      avanceAPeuPresCommeAvant
    }
  }

  def avanceAPeuPresCommeAvant = {
    direction = new NormalDistribution(direction, 0.1).sample
    anode.x += Math.sin(direction) * 2
    anode.y += Math.cos(direction) * 2
  }

  def avanceDroit = {
    anode.x += Math.sin(direction) * 3
    anode.y += Math.cos(direction) * 3
  }

  def rembobine = {
    anode.x = log.apply(index)._1
    anode.y = log.apply(index)._2
    val c = tbx.findCarre(anode.x, anode.y)
    c.updatePheronome(anode.tribu)
    index -= 1
    index
  }

  def redirige(largeur: Int, hauteur: Int, border: Int, rnd: Random): Unit = {
    if (anode.remetsDansLeTableau(largeur, hauteur, border)) {
      direction = rnd.nextDouble() * Math.PI * 2
    }
  }

  def doZeJob(lc: List[Carre]): Unit = {
    state match {
      case FourmiStateMachine.cherche =>
        avance(lc)
        if (aDetecteLaNourriture(100)) {
          state = FourmiStateMachine.detecte
          MyLog.myPrintIt(anode.tribu)
          oldDistance = anode.dist(jnode)
          direction = anode.getNodeDirection(jnode)
        }
      case FourmiStateMachine.detecte =>
        avanceDroit
        val newDistance = anode.dist(jnode)
        if ((newDistance > oldDistance) && (trigger)) {
          MyLog.myErrPrintln(anode.tribu, "od %.0f, nd %.0f, d %.02f, ".format(oldDistance, newDistance, direction))
          trigger = false
        }
        if (aDetecteLaNourriture(20)) {
          state = FourmiStateMachine.retourne
          index = log.length - 2
        }
      case FourmiStateMachine.retourne =>
        if (rembobine == 1) {
          state = FourmiStateMachine.cherche
          direction = tbx.rnd.nextDouble() * Math.PI * 2
          log = List[(Int, Int, FourmiStateMachine)]()
          estRevenueALaFourmiliere += 1
        }
      case _ => MyLog.myErrPrintD(state + "\n")
    }
    redirige(tbx.zp.largeur, tbx.zp.hauteur, 10, tbx.rnd)
  }

  def aDetecteLaNourriture(limitDetection: Double) = (anode.dist(jnode) < limitDetection)

  def paint(g: Graphics2D) {
    var pheronomeD = 0
    var fourmiL = 0
    var fourmiH = 0
    g.setColor(anode.tribu.c.color)
    log.foreach(p => {
      p._3 match {
        case FourmiStateMachine.cherche =>
          pheronomeD = 2
          fourmiL = 7
          fourmiH = 12
        case FourmiStateMachine.detecte =>
          pheronomeD = 4
          fourmiL = 9
          fourmiH = 14
        case FourmiStateMachine.retourne =>
          pheronomeD = 0
          fourmiL = 10
          fourmiH = 16
        case _ =>
      }
      g.fillOval(p._1, p._2, pheronomeD, pheronomeD)
    })

    g.fillOval(anode.x.toInt, anode.y.toInt, fourmiL, fourmiH)
    g.setColor(if (anode.selected) {
      Color.red
    } else {
      Color.black
    })
    g.drawOval(anode.x.toInt, anode.y.toInt, fourmiL, fourmiH)
    log = log :+ (anode.x.toInt, anode.y.toInt, state)
  }
}

case class FourmiStateMachine private(state: String) {
  override def toString = "FourmiState_" + state
}

object FourmiStateMachine {
  val cherche = FourmiStateMachine("cherche")
  val detecte = FourmiStateMachine("detecte")
  val retourne = FourmiStateMachine("retourne")
}


