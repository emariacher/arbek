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
  var compteurSinceLastTourneEnRond = 0
  var lastlogcarre = new Carre(0, 0)
  var cptShortcut: Int = _

  def carre = tbx.findCarre(anode.x, anode.y)

  override def toString = "[%.0f,%.0f](%d)".format(anode.x, anode.y, logxys.length) + tribu + carre

  def avance(lc: List[Carre]) = {
    val listeDesCarresReniflables = lc.filter(c =>
      anode.pasLoin(c.XY) < Math.max(ParametresPourFourmi.influenceDesPheromones, tourneEnRond * tourneEnRond) & c.hasPheromone(tribu) > ParametresPourFourmi.depotEvaporeFinal)
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
        if (tbx.graph.triggerTraceNotAlreadyActivated) {
          anode.selected = true
        }
        tourneEnRond += 1
        state = FourmiStateMachine.tourneEnRond
      } else {
        tourneEnRond = 0
        state = FourmiStateMachine.surLaTrace
      }
      val oldnode = new Node(anode.x, anode.y)
      val oldcarre = tbx.findCarre(oldnode.x, oldnode.y)
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
      if (carre.dist(tbx.findCarre(oldnode.x, oldnode.y)) < 1) {
        direction = tbx.rnd.nextDouble() * Math.PI * 2
        /*myErrPrintDln(toString + " d%.2f  Stationnaire! ".format(direction) + tourneEnRond,
          lfedges1.mkString("\n--- e1r{", ",", "}"),
          lfedges2.mkString("\n--- e2n{", ",", "}"))*/
        selPrint(tribu, oldnode, oldcarre, " --> ", anode, carre, "d%.2f\n".format(direction))
        avanceAPeuPresCommeAvant
        selPrint(tribu, oldnode, oldcarre, " --> ", anode, carre, "d%.2f\n".format(direction))
      }
      if (listeDesCarresPasDejaParcourus.length < 3) {
        selPrint(tourneEnRond, tbx.ts, listeDesCarresReniflables.mkString("lcr", ", ", ""))
        selPrint(logcarres.mkString("lc", ", ", ""))
        selPrint(listeDesCarresPasDejaParcourus.mkString("lpdp", ", ", ""))
        selPrint(tribu, oldnode, oldcarre, " --> ", anode, carre, "d%.2f\n".format(direction))
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
    logcarres = List[Carre]()
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
    /*old trouve les raccourcis
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
    anode.selected = false
    FourmiStateMachine.cherche
  }

  def lisseLeRetour = {
    val jaffeC = logxys.last
    val fourmiliereC = (fourmiliere.c, FourmiStateMachine.cherche)
    if (ParametresPourFourmi.raccourci > 0) { // detecte les raccourcis
      var cptRaccourcis = 0
      val oldlength = logxys.length
      //var logxystemp = List[(Carre, FourmiStateMachine)]()
      var logxystemp = logxys.zipWithIndex
      var indexLogxys = 0
      while (indexLogxys < logxystemp.length) {
        val avantlength = logxystemp.length
        val c = logxystemp.apply(indexLogxys)._1._1
        val lf = logxystemp.filter(_._1._1.dist(c) < ParametresPourFourmi.raccourci).sortBy(_._2)
        if (lf.last._2 - lf.head._2 > 10) {
          cptRaccourcis += 1
          logxystemp = logxystemp.filter(logitem => logitem._2 <= lf.head._2 | logitem._2 > lf.last._2)
        }
        indexLogxys += 1
      }
      logxys = logxystemp.sortBy(_._2).map(_._1)
      selPrint(toString + " <-- raccourci -- " + oldlength + " cptRaccourcis=" + cptRaccourcis)
    }
    if (ParametresPourFourmi.simplifieLissage > 0) { // décime!
      val oldlength = logxys.length
      logxys = logxys.zipWithIndex.filter(_._2 % (ParametresPourFourmi.simplifieLissage + tbx.rnd.nextInt(2)) == 1).map(_._1)
      selPrint(toString + " <--   trop    -- " + oldlength)
    }
    // remet la fourmiliere au début et la jaffe a la fin si jamais on simplifie abusivement
    logxys = fourmiliereC :: logxys
    logxys = logxys :+ jaffeC
    if (ParametresPourFourmi.sautsTropGrandsLissage > 0) { // sauts trop grands / rajoute quand il n'y en a pas assez
      var cptPasAssez = 0
      val oldlength = logxys.length
      var lsauts = logxys.zipWithIndex.sliding(2).toList.map(l => (l.head._1._1.dist(l.last._1._1),
        l.head._1._1, l.last._1._1, l.head._2)).filter(_._1 > ParametresPourFourmi.sautsTropGrandsLissage)
      while (!lsauts.isEmpty) {
        cptPasAssez += 1
        val llissage = lsauts.map(s => (s._2, s._2.milieu(s._3), s._3, s._4))
        llissage.reverse.foreach(toBeInserted => {
          logxys = insert(logxys, toBeInserted._4 + 1, (toBeInserted._2, FourmiStateMachine.lisse))
        })
        lsauts = logxys.zipWithIndex.sliding(2).toList.map(l => (l.head._1._1.dist(l.last._1._1),
          l.head._1._1, l.last._1._1, l.head._2)).filter(_._1 > ParametresPourFourmi.sautsTropGrandsLissage)
      }
      selPrint(toString + " <--pas assez-- " + oldlength + " cptPasAssez=" + cptPasAssez + " max=" +
        logxys.zipWithIndex.sliding(2).toList.map(l => (l.head._1._1.dist(l.last._1._1),
          l.head._1._1, l.last._1._1, l.head._2)).map(_._1).max)
    }
  }

  def cherche(lc: List[Carre]) = {
    avance(lc)
    redirige(tbx.zp.largeur, tbx.zp.hauteur, 10, tbx.rnd)
    if (aDetecteLaNourriture(ParametresPourFourmi.limiteDetectionNourriture)) {
      state = FourmiStateMachine.detecte
      direction = anode.getNodeDirection(jnode)
    } else if ((estALaFourmiliere) & (logxys.length > 100)) { // si jamais tu repasses a la fourmiliere, remets les compteurs a zero
      state = AuxAlentoursDeLaFourmiliere
    }
  }

  def doZeJob(lc: List[Carre]): Unit = {
    previousState = state
    compteurSinceLastTourneEnRond += 1
    state match {
      case FourmiStateMachine.cherche => cherche(lc)
      case FourmiStateMachine.surLaTrace => cherche(lc)
      case FourmiStateMachine.tourneEnRond => cherche(lc)
        if (tourneEnRond > 5) {
          compteurSinceLastTourneEnRond = 0
        }
      case FourmiStateMachine.detecte =>
        avanceDroit
        if (aDetecteLaNourriture(10)) {
          state = FourmiStateMachine.retourne
          lisseLeRetour
          cptShortcut = 0
          indexlog = logxys.length - 1
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
    g.drawString(logxys.length + " " + logcarres.length, anode.x.toInt, anode.y.toInt)
    g.drawOval(anode.x.toInt, anode.y.toInt, fourmiL, fourmiH)
    if (!carre.egal(lastlogcarre)) {
      logxys = logxys :+ (carre, state)
      lastlogcarre = carre
    }
    /*if (triggerTrace) {
      myPrintDln(toString, state)
    }*/
  }

  def selPrint(a: Any): Unit = {
    if (anode.selected) {
      myPrintD(a + "\n")
    }
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


