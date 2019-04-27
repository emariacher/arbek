package graphlayout

import java.awt.{Color, Graphics2D}

import graphlayout.Tableaux.tbx
import kebra.MyLog

import scala.collection.immutable.List
import scala.util.Random

class Fourmi(val anode: ANode) {
  var direction: Double = .0
  var jnode: JNode = _
  var state: FourmiStateMachine = FourmiStateMachine.cherche
  var log = List[(Int, Int, FourmiStateMachine)]()
  var index: Int = _

  override def toString = "[%.0f,%.0f]".format(anode.x, anode.y) + anode.tribu

  def avance = {
    val zeCarre = tbx.findCarre(anode.x, anode.y)
    val z = zeCarre.getVoisins.filter(c => {
      !c.depotPheromones.filter(_.tribu == anode.tribu).map(_.ph).isEmpty
    }).sortBy(c => c.depotPheromones.filter(_.tribu == anode.tribu).map(_.ph).head).reverse
    if (!z.isEmpty) {
      MyLog.myPrintln(toString, zeCarre, z.map(c => (c, c.depotPheromones.filter(_.tribu == anode.tribu).map( p => "%.1f".format(p.ph)).head)).mkString(","))
      // en repartant de la fourmiliere, la fourmi a X% de chance d'aller dans une case a pheronomes et 100-X% de change a aller dans une direction au hasard
      // sinon la fourmi a X% de chance d'aller dans une case a pheronomes , 99-X% de change a aller dans la meme direction et 1% de change a aller dans une direction au hasard
      // plus il y a de pheronomes, plus X est grand
      // si il y a plusieurs case a pheronomes, la probabilite de la fourmi de choisr l'une ou l'autre case est proportionnelle aux pheronomes contenues
    }

    anode.x += Math.sin(direction) * 2
    anode.y += Math.cos(direction) * 2
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

  def doZeJob: Unit = {
    state match {
      case FourmiStateMachine.cherche =>
        avance
        if (aDetecteLaNourriture(100)) {
          state = FourmiStateMachine.detecte
          direction = getNodeDirection(jnode)
        }
      case FourmiStateMachine.detecte =>
        avance
        if (aDetecteLaNourriture(20)) {
          state = FourmiStateMachine.retourne
          index = log.length - 2
        }
      case FourmiStateMachine.retourne =>
        if (rembobine == 1) {
          state = FourmiStateMachine.cherche
          direction = tbx.rnd.nextDouble() * Math.PI * 2
          log = List[(Int, Int, FourmiStateMachine)]()
        }
      case _ => MyLog.myErrPrintD(state + "\n")
    }
    redirige(tbx.zp.largeur, tbx.zp.hauteur, 10, tbx.rnd)
  }

  def aDetecteLaNourriture(limitDetection: Double) = (anode.dist(jnode) < limitDetection)

  def getNodeDirection(n: Node) = {
    val deltaX = n.x - anode.x
    val deltaY = n.y - anode.y
    Math.atan(deltaX / deltaY)
  }

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
          pheronomeD = 2
          fourmiL = 9
          fourmiH = 14
        case FourmiStateMachine.retourne =>
          pheronomeD = 3
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


