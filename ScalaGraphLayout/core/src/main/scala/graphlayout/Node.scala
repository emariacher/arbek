package graphlayout

import java.awt.Graphics2D

abstract class Node {
  var x = .0
  var y = .0

  var slidingAverageMultiplier = 3
  var slidingAverageDeltax = .0
  var slidingAverageDeltay = .0
  var slidingAverageDeltaxOld = .0
  var slidingAverageDeltayOld = .0

  override def equals(any: Any): Boolean = {
    any match {
      case g: Node => hashCode.equals(g.hashCode)
      case _ => false
    }
  }

  def getSign(d: Double) = {
    if (d > 0) 1
    else -1
  }

  def updateAverageX(dx: Double) = {
    slidingAverageDeltax = ((slidingAverageMultiplier * slidingAverageDeltaxOld + dx)) / (slidingAverageMultiplier + 1)
    if (getSign(slidingAverageDeltax) != getSign(slidingAverageDeltaxOld)) {
      slidingAverageDeltax = slidingAverageDeltax / 2
    }
    slidingAverageDeltaxOld = slidingAverageDeltax
    slidingAverageDeltax
  }

  def updateAverageY(dy: Double) = {
    slidingAverageDeltay = ((slidingAverageMultiplier * slidingAverageDeltayOld + dy)) / (slidingAverageMultiplier + 1)
    if (getSign(slidingAverageDeltay) != getSign(slidingAverageDeltayOld)) {
      slidingAverageDeltay = slidingAverageDeltay / 2
    }
    slidingAverageDeltayOld = slidingAverageDeltay
    slidingAverageDeltay
  }

  def getID: String

  def paint(g: Graphics2D)

  def pasLoin(mx: Int, my: Int): Double = {
    //MyLog.myPrintIt(toString, mx, x, my, y, Math.sqrt(((mx - x) * (mx - x)) + ((my - y) * (my - y))))
    Math.sqrt(((mx - x) * (mx - x)) + ((my - y) * (my - y)))
  }

  def remetsDansLeTableau(largeur: Int, hauteur: Int, border: Int): Unit = {
    x = Math.max(Math.min(largeur - border, x), border)
    y = Math.max(Math.min(hauteur - border, y), border)
  }
}
