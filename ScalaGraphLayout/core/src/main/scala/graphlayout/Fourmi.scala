package graphlayout

import java.awt.{Color, Graphics2D}

import scala.util.Random

class Fourmi(val anode: ANode) {
  var direction: Double = .0
  var jnode: JNode = _
  var state: FourmiStateMachine = FourmiStateMachine.cherche

  override def toString = "[%.0f,%.0f]".format(anode.x, anode.y) + anode.tribu

  def avance = {
    anode.x += Math.sin(direction) * 2
    anode.y += Math.cos(direction) * 2
  }

  def redirige(largeur: Int, hauteur: Int, border: Int, rnd: Random): Unit = {
    if (anode.remetsDansLeTableau(largeur, hauteur, border)) {
      direction = rnd.nextDouble() * Math.PI * 2
    }
  }

  def doZeJob: Unit = {
    state match {
      case FourmiStateMachine.cherche =>
        if (aDetecteLaNourriture(100)) {
          state = FourmiStateMachine.detecte
          direction = getNodeDirection(jnode)
        }
      case FourmiStateMachine.detecte =>
        if (aDetecteLaNourriture(20)) {
          state = FourmiStateMachine.retourne
        }
      case FourmiStateMachine.retourne =>

    }
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
    state match {
      case FourmiStateMachine.cherche =>
        pheronomeD = 1
        fourmiL = 7
        fourmiH = 12
      case FourmiStateMachine.detecte =>
        pheronomeD = 1
        fourmiL = 8
        fourmiH = 14
      case FourmiStateMachine.retourne =>
        pheronomeD = 3
        fourmiL = 10
        fourmiH = 16
    }
    anode.log.foreach(p => g.fillOval(p._1, p._2, pheronomeD, pheronomeD))
    g.fillOval(anode.x.toInt, anode.y.toInt, fourmiL, fourmiH)
    g.setColor(if (anode.selected) {
      Color.red
    } else {
      Color.black
    })
    g.drawOval(anode.x.toInt, anode.y.toInt, fourmiL, fourmiH)
    anode.log = anode.log :+ (anode.x.toInt, anode.y.toInt)
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


