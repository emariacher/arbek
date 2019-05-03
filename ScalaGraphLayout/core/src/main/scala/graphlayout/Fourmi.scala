package graphlayout

import java.awt.{Color, Graphics2D}

import graphlayout.Tableaux.tbx
import kebra.MyLog._
import org.apache.commons.math3.distribution._

import scala.collection.immutable.List
import scala.util.Random

class Fourmi(val anode: ANode) {
  val influenceDesPheromones = 40.0
  val angleDeReniflage = (Math.PI * 3 / 4) - .01
  val tribu = anode.tribu
  var direction: Double = .0
  var jnode: JNode = _
  var state: FourmiStateMachine = FourmiStateMachine.cherche
  var log = List[(Int, Int, FourmiStateMachine)]()
  var index: Int = _
  var estRevenueALaFourmiliere = 0
  val distributionAvecPheromone = new UniformRealDistribution()
  var oldDistance = .0
  var trigger = true
  var logcarres = List[Carre]()

  override def toString = "[%.0f,%.0f]".format(anode.x, anode.y) + tribu

  def avance(lc: List[Carre]) = {
    val listeDesCarresReniflables = lc.filter(c => anode.pasLoin(c.XY) < influenceDesPheromones & c.hasPheromone(tribu) > 0)
      .filter(c => (Math.abs(direction - anode.getNodeDirection(c.XY)) % (Math.PI * 2)) < angleDeReniflage)
    if (listeDesCarresReniflables.isEmpty) {
      avanceAPeuPresCommeAvant
      if (anode.selected) {
        myErrPrintIt(tribu, anode, "d%.2f".format(direction))
      }
    } else {
      val lfedges = listeDesCarresReniflables.map(c => {
        val e = new Edge(c.fn, anode)
        e.attraction = c.hasPheromone(tribu)
        e
      })
      val oldnode = new Node(anode.x, anode.y)
      if (anode.selected) {
        myPrintIt("\n", tribu, anode, tbx.findCarre(anode.x, anode.y), "d%.2f".format(direction))
        myPrintln(listeDesCarresReniflables.map(c => (c, c.XYInt,
          "ph%.0f".format(c.hasPheromone(tribu)), "di%.2f".format(anode.pasLoin(c.XY)))).mkString("r{", ",", "}"))
        myPrintln(lfedges.mkString("e{", ",", "}"))
      }
      lfedges.foreach(_.getDist)
      lfedges.foreach(_.opTimize)
      lfedges.foreach(_.getDist)
      lfedges.foreach(_.opTimize)
      lfedges.foreach(_.getDist)
      lfedges.foreach(_.opTimize)
      lfedges.foreach(_.getDist)
      lfedges.foreach(_.opTimize)
      direction = oldnode.getNodeDirection(anode)
      if (anode.selected) {
        myPrintln(tribu, anode, "%.2f".format(direction))
      }
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
    c.updatePheromone(tribu)
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
        if (aDetecteLaNourriture(300)) {
          state = FourmiStateMachine.detecte
          myPrintIt(tribu)
          oldDistance = anode.dist(jnode)
          direction = anode.getNodeDirection(jnode)
        }
      case FourmiStateMachine.detecte =>
        avanceDroit
        val newDistance = anode.dist(jnode)
        if ((newDistance > oldDistance) && (trigger)) {
          myErrPrintln(tribu, "od %.0f, nd %.0f, d %.02f, ".format(oldDistance, newDistance, direction))
          trigger = false
        }
        if (aDetecteLaNourriture(10)) {
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
      case _ => myErrPrintD(state + "\n")
    }
    redirige(tbx.zp.largeur, tbx.zp.hauteur, 10, tbx.rnd)
  }

  def aDetecteLaNourriture(limitDetection: Double) = (anode.dist(jnode) < limitDetection)

  def paint(g: Graphics2D) {
    var pheronomeD = 0
    var fourmiL = 0
    var fourmiH = 0
    g.setColor(tribu.c.color)
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
    logcarres = (logcarres :+ tbx.findCarre(anode.x, anode.y)).distinct
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


