package graphlayout

import java.awt.{Color, Graphics2D}

import graphlayout.Tableaux.tbx
import kebra.MyLog._
import org.apache.commons.math3.distribution._

import scala.collection.immutable.List
import scala.util.Random

class Fourmi(val anode: ANode) {
  val CEstLaFourmiliere = 20.0
  var fourmiliere: Fourmiliere = _
  var estRevenueALaFourmiliere = 0
  val influenceDesPheromones = 40.0
  val angleDeReniflage = (Math.PI * 4 / 5)
  val tribu = anode.tribu
  var direction: Double = .0
  var jnode: JNode = _
  var state: FourmiStateMachine = FourmiStateMachine.cherche
  var log = List[(Int, Int, FourmiStateMachine)]()
  var index: Int = _
  val distributionAvecPheromone = new UniformRealDistribution()
  var oldDistance = .0
  var triggerTrace = true
  var logcarres = List[Carre]()
  var compteurDansLesPheromones = 0

  override def toString = "[%.0f,%.0f]".format(anode.x, anode.y) + tribu

  def avance(lc: List[Carre]) = {
    val listeDesCarresReniflables = lc.filter(c => anode.pasLoin(c.XY) < influenceDesPheromones & c.hasPheromone(tribu) > 0)
      .filter(c => (Math.abs(direction - anode.getNodeDirection(c.XY)) % (Math.PI * 2)) < angleDeReniflage)
    if (listeDesCarresReniflables.isEmpty) {
      avanceAPeuPresCommeAvant
      if ((triggerTrace) & (!lc.isEmpty)) {
        myErrPrintIt("\n", tribu, anode, tbx.findCarre(anode.x, anode.y), "d%.2f".format(direction))
      }
      compteurDansLesPheromones = 0
    } else {
      val listeDesCarresPasDejaParcourus = listeDesCarresReniflables.filter(c => !logcarres.contains(c))
      val lfedges = listeDesCarresPasDejaParcourus.map(c => {
        val e = new Edge(c.fn, anode)
        e.attraction = Math.min(c.hasPheromone(tribu), 5)
        e
      })
      val oldnode = new Node(anode.x, anode.y)
      if (triggerTrace) {
        myErrPrintIt("\n", tribu, anode, tbx.findCarre(anode.x, anode.y), "d%.2f".format(direction))
        myPrintln(listeDesCarresReniflables.map(c => (c, c.XYInt,
          "ph%.0f".format(c.hasPheromone(tribu)), "di%.2f".format(anode.pasLoin(c.XY)))).mkString("r{", ",", "}"),
          listeDesCarresReniflables.length)

        myPrintln("      ", listeDesCarresPasDejaParcourus.mkString("n{", ",", "}"), listeDesCarresPasDejaParcourus.length,
          lfedges.mkString("-------  e{", ",", "}"))
      }
      lfedges.foreach(_.getDist)
      //lfedges.foreach(_.opTimize) // ou rassemble
      lfedges.foreach(_.rassemble) // ou optimize
      direction = oldnode.getNodeDirection(anode)
      logcarres = (logcarres :+ tbx.findCarre(anode.x, anode.y)).distinct
      compteurDansLesPheromones += 1
      if (triggerTrace) {
        myPrintDln(tribu, anode, "d%.2f".format(direction), "\n")
      }
    }
  }

  def estALaFourmiliere = anode.pasLoin(fourmiliere.centre) < CEstLaFourmiliere

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
    if (index < 1) {
      myErrPrintIt(toString, index, compteurDansLesPheromones, estALaFourmiliere, state)
    } else {
      index -= 1
      anode.x = log.apply(index)._1
      anode.y = log.apply(index)._2
      val c = tbx.findCarre(anode.x, anode.y)
      c.updatePheromone(tribu)
    }
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
        if (aDetecteLaNourriture(500)) {
          state = FourmiStateMachine.detecte
          //myPrintIt(tribu)
          oldDistance = anode.dist(jnode)
          direction = anode.getNodeDirection(jnode)
        }
      case FourmiStateMachine.detecte =>
        avanceDroit
        val newDistance = anode.dist(jnode)
        /*if ((newDistance > oldDistance) && (triggerTrace)) {
          myErrPrintDln(tribu, "od %.1f, nd %.1f, d %.02f, ".format(oldDistance, newDistance, direction))
          triggerTrace = false
        }*/
        if (aDetecteLaNourriture(10)) {
          state = FourmiStateMachine.retourne
          index = log.length - 2
        }
      case FourmiStateMachine.retourne =>
        if ((rembobine == 0) || (estALaFourmiliere)) {
          if (triggerTrace) {
            myPrintIt("\n", toString, index, estALaFourmiliere, estRevenueALaFourmiliere)
          }
          if ((index == 1) & (!estALaFourmiliere)) {
            myErrPrintIt("\n", toString, index, estALaFourmiliere, fourmiliere.centre, "%.02f".format(anode.pasLoin(fourmiliere.centre)), estRevenueALaFourmiliere)
            myPrintDln(log.map(z => (z._1, z._2)).mkString(", "))
            val l = 0
            //anode.selected = true
          } else if (estRevenueALaFourmiliere > 0) {
            myPrintDln("Ici!", toString, index, estALaFourmiliere, fourmiliere.centre, "%.02f".format(anode.pasLoin(fourmiliere.centre)), estRevenueALaFourmiliere)
          }
          if (tbx.graph.triggerTraceNotAlreadyActivated) {
            triggerTrace = true
            myPrintIt("\n", toString, index, estALaFourmiliere, fourmiliere.centre, "%.02f, ".format(anode.pasLoin(fourmiliere.centre)), estRevenueALaFourmiliere)
          }
          state = FourmiStateMachine.cherche
          direction = direction * (-1) // essaye de reprendre le meme chemin
          log = List((anode.x.toInt, anode.y.toInt, state))
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
    if (anode.selected) {
      g.setColor(Color.red)
      g.drawOval(anode.x.toInt - 2, anode.y.toInt - 2, fourmiL + 4, fourmiH + 4)
    } else {
      g.setColor(Color.black)
    }
    g.drawOval(anode.x.toInt, anode.y.toInt, fourmiL, fourmiH)
    log = log :+ (anode.x.toInt, anode.y.toInt, state)
    if (triggerTrace) {
      myPrintDln(toString, state)
    }
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


