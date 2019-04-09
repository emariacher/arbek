package graphlayout

import java.awt.Graphics2D

abstract class Node {
  var x = .0
  var y = .0

  override def equals(any: Any): Boolean = {
    any match {
      case g: Node => hashCode.equals(g.hashCode)
      case _ => false
    }
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
