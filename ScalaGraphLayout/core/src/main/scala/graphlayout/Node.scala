package graphlayout

import java.awt.Graphics2D

import kebra.MyLog

import scala.util.Random

class Node(var x: Double, var y: Double) {

  var slidingAverageMultiplier = 3
  var mouvement = .0
  var slidingAverageDeltax = .0
  var slidingAverageDeltay = .0
  var slidingAverageDeltaxOld = .0
  var slidingAverageDeltayOld = .0

  def this() = this(.0, .0)

  def this(coord: (Double, Double)) = this(coord._1, coord._2)

  override def toString: String = "[%.0f,%.0f]".format(x, y)

  override def equals(any: Any): Boolean = {
    any match {
      case g: Node => hashCode.equals(g.hashCode)
      case _ => false
    }
  }

  def update(ux: Double, uy: Double, umouvement: Double) {
    if ((ux.isNaN) || (uy.isNaN)) {
      MyLog.myPrintIt(ux.isNaN, uy.isNaN)
    }
    x = ux
    y = uy
    mouvement = umouvement
  }

  def dist(n: Node) = {
    val deltaX = n.x - x
    val deltaY = n.y - y
    Math.sqrt((deltaX * deltaX) + (deltaY * deltaY))
  }

  def getSign(d: Double) = {
    if (d > 0) 1
    else -1
  }

  def updateAverageX(dx: Double) = {
    slidingAverageDeltax = (((slidingAverageMultiplier * slidingAverageDeltaxOld) + dx)) / (slidingAverageMultiplier + 1)
    if (getSign(slidingAverageDeltax) != getSign(slidingAverageDeltaxOld)) {
      slidingAverageDeltax = slidingAverageDeltax / 2
    } else if (Math.abs(slidingAverageDeltax - slidingAverageDeltaxOld) < 0.2) {
      slidingAverageDeltax = slidingAverageDeltax / 4
    }
    slidingAverageDeltaxOld = slidingAverageDeltax
    slidingAverageDeltax
  }

  def updateAverageY(dy: Double) = {
    slidingAverageDeltay = (((slidingAverageMultiplier * slidingAverageDeltayOld) + dy)) / (slidingAverageMultiplier + 1)
    if (getSign(slidingAverageDeltay) != getSign(slidingAverageDeltayOld)) {
      slidingAverageDeltay = slidingAverageDeltay / 2
    } else if (Math.abs(slidingAverageDeltay - slidingAverageDeltayOld) < 0.2) {
      slidingAverageDeltay = slidingAverageDeltay / 4
    }
    slidingAverageDeltayOld = slidingAverageDeltay
    slidingAverageDeltay
  }

  def getID: String = "[%.0f,%.0f]".format(x, y)

  def paint(g: Graphics2D) = {}

  def pasLoin(mx: Int, my: Int): Double = {
    //MyLog.myPrintIt(toString, mx, x, my, y, Math.sqrt(((mx - x) * (mx - x)) + ((my - y) * (my - y))))
    Math.sqrt(((mx - x) * (mx - x)) + ((my - y) * (my - y)))
  }

  def remetsDansLeTableau(largeur: Int, hauteur: Int, border: Int): Boolean = {
    val newx = Math.max(Math.min(largeur - border, x), border)
    val tx = (newx != x)
    if (tx) x = newx
    val newy = Math.max(Math.min(hauteur - border, y), border)
    val ty = (newy != y)
    if (ty) y = newy
    tx || ty
  }

  def desempile(lnodes: List[Node], largeur: Int, hauteur: Int, rnd: Random): Unit = {
    while (!lnodes.filter(n => n.hashCode != hashCode & n.x.toInt == x.toInt & n.y.toInt == y.toInt).isEmpty) {
      x = (rnd.nextInt(largeur))
      y = (rnd.nextInt(hauteur))
    }
  }

  def getNodeDirection(n: Node) = {
    val deltaX = n.x - x
    val deltaY = n.y - y
    val atan = Math.atan(deltaX / deltaY)
    val atan2 = Math.atan2(deltaX, deltaY)
    //MyLog.myPrintIt(toString, "(%.02f,%.02f)".format(deltaX, deltaY), "[a %.02f, a2 %.02f]".format(atan, atan2))
    if (atan == atan2) {
      atan
    } else {
      atan + Math.PI
    }
  }
}
