package graphlayout

import java.awt.{Color, Graphics2D}

import graphlayout.Tableaux.tbx
import kebra.MyLog._
import org.apache.commons.math3.distribution._

import scala.collection.immutable.List
import scala.util.Random

class Fourmi(val anode: ANode) {
  val CEstLaFourmiliere = 10.0
  var fourmiliere: Fourmiliere = _
  var estRevenueALaFourmiliere = 0
  val influenceDesPheromones = 40.0
  val angleDeReniflage = (Math.PI * 4 / 5)
  val tribu = anode.tribu
  var direction: Double = .0
  var jnode: JNode = _
  var state: FourmiStateMachine = FourmiStateMachine.cherche
  var logxys = List[(Int, Int, FourmiStateMachine)]()
  var index: Int = _
  val distributionAvecPheromone = new UniformRealDistribution()
  var oldDistance = .0
  var triggerTrace = true
  var logcarres = List[Carre]()
  var compteurDansLesPheromones = 0
  var tourneEnRond = 0
  var lastlogcarre = new Carre(0, 0)

  override def toString = "[%.0f,%.0f]".format(anode.x, anode.y) + tribu

  def avance(lc: List[Carre]) = {
    val listeDesCarresReniflables = lc.filter(c =>
      anode.pasLoin(c.XY) < Math.max(influenceDesPheromones, tourneEnRond * tourneEnRond) & c.hasPheromone(tribu) > 0)
    val listeDesCarresPasDejaParcourus = listeDesCarresReniflables.filter(c => !logcarres.contains(c))
      .filter(c => (Math.abs(direction - anode.getNodeDirection(c.XY)) % (Math.PI * 2)) < angleDeReniflage)
    if (listeDesCarresReniflables.isEmpty | tourneEnRond > 10) {
      avanceAPeuPresCommeAvant
      if (!listeDesCarresReniflables.isEmpty) {
        myPrintDln(tribu, anode, tbx.findCarre(anode.x, anode.y), "d%.2f".format(direction)
          // , "\n    ",listeDesCarresReniflables.mkString("r{", ",", "}"), listeDesCarresReniflables.length
          // , "\n    ",listeDesCarresPasDejaParcourus.mkString("n{", ",", "}"), listeDesCarresPasDejaParcourus.length
          , listeDesCarresReniflables.length, listeDesCarresPasDejaParcourus.length
        )
      }
      compteurDansLesPheromones = 0
    } else {
      val lfedges1 = listeDesCarresReniflables.map(c => {
        val e = new Edge(c.fn, anode)
        e.attraction = Math.min(c.hasPheromone(tribu), 1) // quand meme un peu (1) attire par les carres deja parcourus
        e
      })
      val lfedges2 = listeDesCarresPasDejaParcourus.map(c => {
        val e = new Edge(c.fn, anode)
        e.attraction = Math.min(c.hasPheromone(tribu),
          10 + tourneEnRond + (listeDesCarresReniflables.length - listeDesCarresPasDejaParcourus.length)
        ) // quand ca tourne en rond, force la sortie
        e
      })
      if (listeDesCarresPasDejaParcourus.isEmpty) {
        tourneEnRond += 1
      } else {
        tourneEnRond = 0
      }
      val oldnode = new Node(anode.x, anode.y)
      myErrPrintIt("\n", tribu, anode, tbx.findCarre(anode.x, anode.y), "d%.2f".format(direction))
      myPrintln(listeDesCarresReniflables.map(c => (c, c.XYInt,
        "ph%.0f".format(c.hasPheromone(tribu)), "di%.2f".format(anode.pasLoin(c.XY)))).mkString("r{", ",", "}"),
        listeDesCarresReniflables.length)
      myPrintln("      ", listeDesCarresPasDejaParcourus.mkString("n{", ",", "}"), listeDesCarresPasDejaParcourus.length,
        lfedges2.mkString("-------  e{", ",", "}"))
      lfedges1.foreach(_.rassemble)
      myPrintDln("***********************************")
      lfedges2.foreach(_.rassemble)
      Edge.checkInside("" + (anode, listeDesCarresReniflables.map(_.fn).mkString("{", ",", "}")),
        listeDesCarresReniflables.map(_.fn) :+ oldnode, anode)
      direction = oldnode.getNodeDirection(anode)
      logcarres = (logcarres :+ tbx.findCarre(anode.x, anode.y)).distinct
      compteurDansLesPheromones += 1
      if (anode.dist(oldnode) < 0.00001) {
        myErrPrintDln(toString + " Stationnaire!")
        direction = tbx.rnd.nextDouble() * Math.PI * 2
        avanceAPeuPresCommeAvant
      }
      myPrintDln(tribu, anode, tbx.findCarre(anode.x, anode.y), "d%.2f".format(direction), tourneEnRond, "\n")
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

  def rembobine: Int = {
    myAssert3(index < 1, false, toString + " " + index)
    index -= 1
    anode.x = logxys.apply(index)._1
    anode.y = logxys.apply(index)._2
    val c = tbx.findCarre(anode.x, anode.y)
    myAssert3(c == null, false, toString)
    c.updatePheromone(tribu)
    logxys.take(index).indexWhere(logitem => logitem._1 == anode.x && logitem._2 == anode.y) match {
      case x if x > -1 =>
        myErrPrintDln("Going back taking a shortcut! ", anode, x, index)
        index = x // prend un raccourci si jamais t'es deja passe par la
      case _ =>
    }
    index
  }

  def redirige(largeur: Int, hauteur: Int, border: Int, rnd: Random): Unit = {
    if (anode.remetsDansLeTableau(largeur, hauteur, border)) {
      direction = rnd.nextDouble() * Math.PI * 2
    }
  }

  def AuxAlentoursDeLaFourmiliere(ouiMaisDOu: Int) = {
    if ((rembobine == 0) || (estALaFourmiliere)) {
      if (triggerTrace) {
        myPrintIt("\nLigne: " + ouiMaisDOu, toString, index, estALaFourmiliere, estRevenueALaFourmiliere)
      }
      if ((index == 1) & (!estALaFourmiliere)) {
        myErrPrintIt("\nLigne: " + ouiMaisDOu, toString, index, estALaFourmiliere, fourmiliere.centre,

          "%.02f".format(anode.pasLoin(fourmiliere.centre)), estRevenueALaFourmiliere)
        myPrintDln(logxys.map(z => (z._1, z._2)).mkString(", "))
        val l = 0
        //anode.selected = true
      } else if (estRevenueALaFourmiliere > 0) {
        myPrintDln("Ici!", toString, index, estALaFourmiliere, fourmiliere.centre,
          "%.02f".format(anode.pasLoin(fourmiliere.centre)), estRevenueALaFourmiliere)
      }
      if (tbx.graph.triggerTraceNotAlreadyActivated) {
        triggerTrace = true
        myPrintIt("\nLigne: ", ouiMaisDOu, toString, index, estALaFourmiliere, fourmiliere.centre,
          "%.02f, ".format(anode.pasLoin(fourmiliere.centre)), estRevenueALaFourmiliere)
      }
      state = FourmiStateMachine.cherche
      anode.moveTo(fourmiliere.centre) // teleporte toi au centre de la fourmiliere
      direction = direction * (-1) // essaye de reprendre le meme chemin
      logxys = List((anode.x.toInt, anode.y.toInt, state))
      logcarres = List(tbx.findCarre(anode.x, anode.y))
      estRevenueALaFourmiliere += 1
      tourneEnRond = 0
    }
  }

  def doZeJob(lc: List[Carre]): Unit = {
    state match {
      case FourmiStateMachine.cherche =>
        avance(lc)
        if (aDetecteLaNourriture(300)) {
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
          index = logxys.length - 2
        }
      case FourmiStateMachine.retourne =>
        AuxAlentoursDeLaFourmiliere(L_)
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
    logxys.foreach(p => {
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
    val c = tbx.findCarre(anode.x, anode.y)
    if (!c.egal(lastlogcarre)) {
      logxys = logxys :+ (c.fn.x.toInt, c.fn.y.toInt, state)
      lastlogcarre = c
    }
    /*if (triggerTrace) {
      myPrintDln(toString, state)
    }*/
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


