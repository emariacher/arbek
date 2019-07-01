package graphlayout

import java.awt.{Color, Graphics2D}

import graphlayout.Tableaux.tbx
import kebra.MyLog._
import org.apache.commons.math3.distribution._

import scala.collection.immutable.List
import scala.util.Random

class Fourmi(val anode: ANode) {
  var fourmiliere: Fourmiliere = _
  val tribu = anode.tribu
  var direction: Double = .0
  var jnode: JNode = _
  var state: FourmiStateMachine = FourmiStateMachine.cherche
  var previousState = state
  var stateCompteur = 0
  var logxys = List[(Carre, FourmiStateMachine)]()
  var indexlog: Int = _
  var triggerTrace = false
  var logcarres = List[Carre]()
  var compteurDansLesPheromones = 0
  var compteurPasDansLesPheromones = 0
  var lcompteurState = scala.collection.mutable.Map[FourmiStateMachine, Int]()
  var tourneEnRond = 0
  var lastlogcarre = new Carre(0, 0)
  var cptShortcut: Int = _

  def carre = tbx.findCarre(anode.x, anode.y)

  override def toString = "[%.0f,%.0f](%d)".format(anode.x, anode.y, logxys.length) + tribu + carre

  def avance(lc: List[Carre]) = {
    val listeDesCarresReniflables = lc.filter(c =>
      anode.pasLoin(c.XY) < Math.max(ParametresPourFourmi.influenceDesPheromones, tourneEnRond * tourneEnRond) & c.hasPheromone(tribu) > 0)
    val listeDesCarresPasDejaParcourus = listeDesCarresReniflables.filter(c => !logcarres.contains(c))
      .filter(c => (Math.abs(direction - anode.getNodeDirection(c.XY)) % (Math.PI * 2)) < ParametresPourFourmi.angleDeReniflage)
    if (listeDesCarresReniflables.isEmpty | tourneEnRond > ParametresPourFourmi.tourneEnRondLimite) {
      avanceAPeuPresCommeAvant
      compteurDansLesPheromones = 0
      compteurPasDansLesPheromones += 1
      if (compteurPasDansLesPheromones > ParametresPourFourmi.compteurPasDansLesPheromonesLimite) tourneEnRond = 0
      logcarres = List[Carre]()
      state = FourmiStateMachine.cherche
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
          ParametresPourFourmi.suisLeChemin1 + (tourneEnRond * tourneEnRond) +
            (listeDesCarresReniflables.length - listeDesCarresPasDejaParcourus.length) * ParametresPourFourmi.suisLeChemin2
        ) // quand ca tourne en rond, force la sortie
        e
      })
      if (listeDesCarresPasDejaParcourus.isEmpty) {
        tourneEnRond += 1
        state = FourmiStateMachine.tourneEnRond
      } else {
        tourneEnRond = 0
        state = FourmiStateMachine.surLaTrace
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

  def estALaFourmiliere = anode.pasLoin(fourmiliere.centre) < ParametresPourFourmi.CEstLaFourmiliere

  def avanceAPeuPresCommeAvant = {
    direction = new NormalDistribution(direction, ParametresPourFourmi.avanceAPeuPresCommeAvantDispersion).sample
    anode.x += Math.sin(direction) * ParametresPourFourmi.avanceAPeuPresCommeAvantVitesse
    anode.y += Math.cos(direction) * ParametresPourFourmi.avanceAPeuPresCommeAvantVitesse
  }

  def avanceDroit = {
    anode.x += Math.sin(direction) * ParametresPourFourmi.avanceDroitVitesse
    anode.y += Math.cos(direction) * ParametresPourFourmi.avanceDroitVitesse
  }


  def rembobine: Int = {
    myAssert3(indexlog < 1, false, toString + " " + indexlog + " " + previousState + "[" + stateCompteur + "]")
    /*myAssert3(indexlog < logxys.length, true, toString + " " + indexlog + "<" + logxys.length +
      " " + previousState + "[" + stateCompteur + "] fmcentre[" + fourmiliere.c + "] cptShortcut=" + cptShortcut
      + fourmiliere.retoursFourmiliere.mkString("\n  rf(", ", ", ")")
      + "\n  " + logxys.length + logxys.mkString(" - logxys(", ", ", ")\n")
    )*/
    if (indexlog > logxys.length) {
      myErrPrintDln(toString + " " + indexlog + "<" + logxys.length +
        " " + previousState + "[" + stateCompteur + "] fmcentre[" + fourmiliere.c + "] cptShortcut=" + cptShortcut
        + fourmiliere.retoursFourmiliere.mkString("\n  rf(", ", ", ")")
        + "\n  " + logxys.length + logxys.mkString(" - logxys(", ", ", ")\n"))
      indexlog = logxys.length - 2
    }
    indexlog -= 1
    val c = logxys.apply(indexlog)._1
    anode.moveTo(c.fn)
    myAssert3(c == null, false, toString)
    c.updatePheromone(tribu)
    /*logxys.take(indexlog).indexWhere(logitem => logitem._1.egal(c)) match {
    logxys.take(indexlog).indexWhere(logitem => logitem._1.dist(c) < ParametresPourFourmi.raccourci) match {
      //logxys.take(index).indexWhere(logitem => logitem._1.get9Voisins.contains(c)) match {
      case x if x > -1 =>
        //myPrintDln("Going back taking a shortcut! " + toString, index + " --> " + x)
        cptShortcut += 1
        indexlog = x // prend un raccourci si jamais t'es deja passe par la
      case _ =>
    }*/
    indexlog
  }

  def redirige(largeur: Int, hauteur: Int, border: Int, rnd: Random): Unit = {
    if (anode.remetsDansLeTableau(largeur, hauteur, border)) {
      direction = rnd.nextDouble() * Math.PI * 2
    }
  }

  def AuxAlentoursDeLaFourmiliere: FourmiStateMachine = {
    /*myPrintDln(Console.BOLD + "Ligne: " + ouiMaisDOu + " A la fourmiliere !", toString, index, estALaFourmiliere, fourmiliere.centre,
      "%.02f".format(anode.pasLoin(fourmiliere.centre)) + Console.RESET)*/
    anode.moveTo(fourmiliere.centre) // teleporte toi au centre de la fourmiliere
    direction = direction * (-1) // essaye de reprendre le meme chemin
    logxys = List((fourmiliere.c, state))
    indexlog = 0
    logcarres = List(fourmiliere.c)
    fourmiliere.retour(state)
    tourneEnRond = 0
    FourmiStateMachine.cherche
  }

  def lisseLeRetour = {
    if (ParametresPourFourmi.raccourci > 1) { // detecte les raccourcis
      val oldlength = logxys.length
      var zindexlog = logxys.length - 1
      while (zindexlog > 0) {
        logxys.take(zindexlog).indexWhere(logitem => logitem._1.dist(logxys.apply(zindexlog)._1) < ParametresPourFourmi.raccourci) match {
          case x if x > -1 =>
            cptShortcut += 1
            zindexlog = x // prend un raccourci si jamais t'es deja passe par la
          case _ => zindexlog -= 1
        }
      }
      myPrintDln(toString + " <-- raccourci -- " + oldlength)
    }
    if (ParametresPourFourmi.simplifieLissage > 1) { // décime!
      val oldlength = logxys.length
      logxys = logxys.zipWithIndex.filter(_._2 % ParametresPourFourmi.simplifieLissage == 1).map(_._1)
      myPrintDln(toString + " <--   trop    -- " + oldlength)
    }
    if (ParametresPourFourmi.sautsTropGrandsLissage > 0) { // sauts trop grands / rajoute quand il n'y en a pas assez
      var lsauts = logxys.zipWithIndex.sliding(2, 2).toList.map(l => (l.head._1._1.dist(l.last._1._1),
        l.head._1._1, l.last._1._1, l.head._2)).filter(_._1 > ParametresPourFourmi.sautsTropGrandsLissage)
      while (!lsauts.isEmpty) {
        val oldlength = logxys.length
        val llissage = lsauts.map(s => (s._2, s._2.milieu(s._3), s._3, s._4))
        llissage.reverse.foreach(toBeInserted => {
          logxys = insert(logxys, toBeInserted._4 + 1, (toBeInserted._2, FourmiStateMachine.lisse))
        })
        /*val lsauts2 = logxys.zipWithIndex.sliding(2, 2).toList.map(l => (l.head._1._1.dist(l.last._1._1),
          l.head._1._1, l.last._1._1, l.head._2)).filter(_._1 > ParametresPourFourmi.sautsTropGrandsLissage)
        myPrintDln(toString + " <--pas assez-- " + oldlength + "[ " + lsauts2.map(_._1).max + " <-- " + lsauts.map(_._1).max + " ]")*/
        myPrintDln(toString + " <--pas assez-- " + oldlength + "[ " + lsauts.map(_._1).max + " ]")
        lsauts = logxys.zipWithIndex.sliding(2, 2).toList.map(l => (l.head._1._1.dist(l.last._1._1),
          l.head._1._1, l.last._1._1, l.head._2)).filter(_._1 > ParametresPourFourmi.sautsTropGrandsLissage)
      }
    }
    if (ParametresPourFourmi.filtrePattern > -1) { // pattern escalier
      val escalier = new PatternCarre(List(new Carre(0, 0), new Carre(1, 0), new Carre(2, 0), new Carre(2, 1), new Carre(3, 1), new Carre(4, 1)), 3)
      val oldlength = logxys.length
      val lslide = logxys.sliding(escalier.lc.length, 1)
      lslide.foreach(tranche => if (escalier.similaire(tranche.map(_._1))) {
        myErrPrintDln(tranche.mkString(". "))
        myAssert2(true, false)
      })
      //myPrintDln(lslide.toList.map(_.mkString(". ")).mkString("\n  "))
      myPrintDln(toString + " <--escalier-- " + oldlength)
    }
  }

  def cherche(lc: List[Carre]) = {
    avance(lc)
    redirige(tbx.zp.largeur, tbx.zp.hauteur, 10, tbx.rnd)
    if (aDetecteLaNourriture(200)) {
      state = FourmiStateMachine.detecte
      direction = anode.getNodeDirection(jnode)
    } else if ((estALaFourmiliere) & (logxys.length > 100)) { // si jamais tu repasses a la fourmiliere, remets les compteurs a zero
      state = AuxAlentoursDeLaFourmiliere
    }
  }

  def doZeJob(lc: List[Carre]): Unit = {
    previousState = state
    state match {
      case FourmiStateMachine.cherche => cherche(lc)
      case FourmiStateMachine.surLaTrace => cherche(lc)
      case FourmiStateMachine.tourneEnRond => cherche(lc)
      case FourmiStateMachine.detecte =>
        avanceDroit
        if (aDetecteLaNourriture(10)) {
          state = FourmiStateMachine.retourne
          lisseLeRetour
          cptShortcut = 0
          indexlog = logxys.length - 2
        } else {
          redirige(tbx.zp.largeur, tbx.zp.hauteur, 10, tbx.rnd)
        }
      case FourmiStateMachine.retourne =>
        if ((rembobine == 0) || (estALaFourmiliere)) {
          state = AuxAlentoursDeLaFourmiliere
        }
        redirige(tbx.zp.largeur, tbx.zp.hauteur, 10, tbx.rnd)
      case _ => myErrPrintDln(state)
    }
    lcompteurState(state) = lcompteurState.getOrElse(state, 0) + 1
    if (previousState != state) {
      stateCompteur = 0
    } else {
      stateCompteur += 1
    }

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
        case FourmiStateMachine.surLaTrace =>
          pheronomeD = 2
          fourmiL = 7
          fourmiH = 12
        case FourmiStateMachine.tourneEnRond =>
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
  override def toString = state
}

object FourmiStateMachine {
  val cherche = FourmiStateMachine("cherche")
  val surLaTrace = FourmiStateMachine("surLaTrace")
  val tourneEnRond = FourmiStateMachine("tourneEnRond")
  val detecte = FourmiStateMachine("detecte")
  val retourne = FourmiStateMachine("retourne")
  val lisse = FourmiStateMachine("lisse")
  val ratioTourneEnRondSurLaTrace = FourmiStateMachine("ratioTourneEnRondSurLaTrace")
}


