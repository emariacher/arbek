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
  val influenceDesPheromones = 40.0
  val angleDeReniflage = (Math.PI * 4 / 5)
  val tribu = anode.tribu
  var direction: Double = .0
  var jnode: JNode = _
  var state: FourmiStateMachine = FourmiStateMachine.cherche
  var previousState = state
  var logxys = List[(Carre, FourmiStateMachine)]()
  var index: Int = _
  var triggerTrace = false
  var logcarres = List[Carre]()
  var compteurDansLesPheromones = 0
  var compteurPasDansLesPheromones = 0
  var tourneEnRond = 0
  var lastlogcarre = new Carre(0, 0)

  def carre = tbx.findCarre(anode.x, anode.y)

  override def toString = "[%.0f,%.0f](%d)".format(anode.x, anode.y, logxys.length) + tribu + carre

  def avance(lc: List[Carre]) = {
    val listeDesCarresReniflables = lc.filter(c =>
      anode.pasLoin(c.XY) < Math.max(influenceDesPheromones, tourneEnRond * tourneEnRond) & c.hasPheromone(tribu) > 0)
    val listeDesCarresPasDejaParcourus = listeDesCarresReniflables.filter(c => !logcarres.contains(c))
      .filter(c => (Math.abs(direction - anode.getNodeDirection(c.XY)) % (Math.PI * 2)) < angleDeReniflage)
    if (listeDesCarresReniflables.isEmpty | tourneEnRond > 10) {
      avanceAPeuPresCommeAvant
      compteurDansLesPheromones = 0
      compteurPasDansLesPheromones += 1
      if (compteurPasDansLesPheromones > 30) tourneEnRond = 0
      logcarres = List[Carre]()
    } else {
      /*if (compteurPasDansLesPheromones > 300) {
        myPrintIt("\n" + Console.MAGENTA + toString, compteurPasDansLesPheromones, tourneEnRond,
          listeDesCarresReniflables.mkString("\n    r{", ",", "}"), listeDesCarresReniflables.length,
          listeDesCarresPasDejaParcourus.mkString(Console.CYAN + "\n    n{", ",", "}"), listeDesCarresPasDejaParcourus.length,
          Console.RESET)
      }*/
      val lfedges1 = listeDesCarresReniflables
        .filter(_.hasPheromone(tribu) > listeDesCarresReniflables.length) // si il y a beaucoup de carres reniflables, prends ceux qui ont le plus de pheromones
        .map(c => {
        val e = new Edge(c.fn, anode)
        e.attraction = Math.min(c.hasPheromone(tribu), 1) // quand meme un peu (1) attire par les carres deja parcourus
        e
      })
      val lfedges2 = listeDesCarresPasDejaParcourus.map(c => {
        val e = new Edge(c.fn, anode)
        e.attraction = Math.max(c.hasPheromone(tribu),
          10 + (tourneEnRond * tourneEnRond) +
            (listeDesCarresReniflables.length - listeDesCarresPasDejaParcourus.length) * 20
        ) // quand ca tourne en rond, force la sortie
        e
      })
      if (listeDesCarresPasDejaParcourus.isEmpty) {
        tourneEnRond += 1
      } else {
        tourneEnRond = 0
      }
      val oldnode = new Node(anode.x, anode.y)
      /*if (compteurPasDansLesPheromones > 300) {
        myErrPrintDln(tribu, anode, carre, " d%.2f".format(direction))
        myPrintln(listeDesCarresReniflables.map(c => (c, c.XYInt,
          "ph%.0f".format(c.hasPheromone(tribu)), "di%.2f".format(anode.pasLoin(c.XY)))).mkString("r{", ",", "}"),
          listeDesCarresReniflables.length)
        myPrintln("      ", listeDesCarresPasDejaParcourus.mkString("n{", ",", "}"), listeDesCarresPasDejaParcourus.length,
          lfedges2.mkString("-------  e{", ",", "}"))
      }*/
      lfedges1.foreach(_.rassemble)
      //myPrintDln("***********************************")
      lfedges2.foreach(_.rassemble)
      Edge.checkInside("" + (anode, listeDesCarresReniflables.map(_.fn).mkString("{", ",", "}")),
        listeDesCarresReniflables.map(_.fn) :+ oldnode, anode)
      direction = oldnode.getNodeDirection(anode)
      logcarres = (logcarres :+ carre).distinct
      compteurDansLesPheromones += 1
      compteurPasDansLesPheromones = 0
      if (anode.dist(oldnode) < 0.00001) {
        direction = tbx.rnd.nextDouble() * Math.PI * 2
        /*myErrPrintDln(toString + " d%.2f  Stationnaire! ".format(direction) + tourneEnRond,
          lfedges1.mkString("\n--- e1r{", ",", "}"),
          lfedges2.mkString("\n--- e2n{", ",", "}"))*/
        avanceAPeuPresCommeAvant
      }
      if (triggerTrace) {
        myPrintDln(tribu, anode, carre, "d%.2f".format(direction), tourneEnRond, "\n")
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

  def rembobine: Int = {
    myAssert3(index < 1, false, toString + " " + index)
    myAssert3(index < logxys.length, true, toString + " " + index + "<" + logxys.length + " " + previousState)
    index -= 1
    val c = logxys.apply(index)._1
    anode.moveTo(c.fn)
    myAssert3(c == null, false, toString)
    c.updatePheromone(tribu)
    logxys.take(index).indexWhere(logitem => logitem._1.egal(c)) match {
      case x if x > -1 =>
        //myPrintDln("Going back taking a shortcut! " + toString, index + " --> " + x)
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

  def AuxAlentoursDeLaFourmiliere(ouiMaisDOu: Int): FourmiStateMachine = {
    /*myPrintDln(Console.BOLD + "Ligne: " + ouiMaisDOu + " A la fourmiliere !", toString, index, estALaFourmiliere, fourmiliere.centre,
      "%.02f".format(anode.pasLoin(fourmiliere.centre)) + Console.RESET)*/
    anode.moveTo(fourmiliere.centre) // teleporte toi au centre de la fourmiliere
    direction = direction * (-1) // essaye de reprendre le meme chemin
    logxys = List((fourmiliere.c, state))
    logcarres = List(fourmiliere.c)
    fourmiliere.retour(ouiMaisDOu)
    tourneEnRond = 0
    FourmiStateMachine.cherche
  }

  def lisseLeRetour = {
    val lissage = 30
    val lsauts = logxys.zipWithIndex.sliding(2, 2).toList.map(l => (l.head._1._1.dist(l.last._1._1),
      l.head._1._1, l.last._1._1, l.head._2)).filter(_._1 > lissage)
    val llissage = lsauts.map(s => (s._2, s._2.milieu(s._3), s._3, s._4))
    llissage.reverse.foreach(toBeInserted => {
      logxys = insert(logxys, toBeInserted._4 + 1, (toBeInserted._2, FourmiStateMachine.lisse))
    })
  }

  def doZeJob(lc: List[Carre]): Unit = {
    previousState = state
    state match {
      case FourmiStateMachine.cherche =>
        avance(lc)
        if (aDetecteLaNourriture(200)) {
          state = FourmiStateMachine.detecte
          direction = anode.getNodeDirection(jnode)
        } else if ((estALaFourmiliere) & (logxys.length > 100)) { // si jamais tu repasses a la fourmiliere, remets les compteurs a zero
          state = AuxAlentoursDeLaFourmiliere(L_)
        }
      case FourmiStateMachine.detecte =>
        avanceDroit
        if (aDetecteLaNourriture(10)) {
          state = FourmiStateMachine.retourne
          lisseLeRetour
          index = logxys.length - 2
        }
      case FourmiStateMachine.retourne =>
        if ((rembobine == 0) || (estALaFourmiliere)) {
          state = AuxAlentoursDeLaFourmiliere(L_)
        }
      case _ => myErrPrintDln(state)
    }
    redirige(tbx.zp.largeur, tbx.zp.hauteur, 10, tbx.rnd)
  }

  def aDetecteLaNourriture(limitDetection: Double) = (anode.dist(jnode) < limitDetection)

  def insert[T](list: List[T], i: Int, values: T*): List[T] = {
    val (front, back) = list.splitAt(i)
    front ++ values ++ back
  }

  def paint(g: Graphics2D) {
    var pheronomeD = 0
    var fourmiL = 0
    var fourmiH = 0
    g.setColor(tribu.c.color)
    logxys.foreach(p => {
      p._2 match {
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
      g.fillOval(p._1.fn.x.toInt, p._1.fn.y.toInt, pheronomeD, pheronomeD)
    })

    g.fillOval(anode.x.toInt, anode.y.toInt, fourmiL, fourmiH)
    if (anode.selected) {
      g.setColor(Color.red)
      g.drawOval(anode.x.toInt - 2, anode.y.toInt - 2, fourmiL + 4, fourmiH + 4)
    } else {
      g.setColor(Color.black)
    }
    g.drawOval(anode.x.toInt, anode.y.toInt, fourmiL, fourmiH)
    if (!carre.egal(lastlogcarre)) {
      logxys = logxys :+ (carre, state)
      lastlogcarre = carre
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
  val lisse = FourmiStateMachine("lisse")
}


