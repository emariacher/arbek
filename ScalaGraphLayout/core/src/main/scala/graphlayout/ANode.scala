package graphlayout

import java.awt.{Color, Graphics2D}

class ANode(val tribu: Tribu) extends Node {
  var selected = false

  def getID = hashCode.toString

  def paint(g: Graphics2D) {
    g.setColor(if (selected) {
      Color.red
    } else {
      Color.black
    })
    g.drawOval(x.toInt, y.toInt, 10, 10)
    g.setColor(tribu.couleur)
    g.fillOval(x.toInt, y.toInt, 10, 10)
  }
}

case class Tribu private(tribu: Color) {
  override def toString = "Tribu_" + tribu.toString

  def couleur = tribu
}

object Tribu {
  val Orange = Tribu(Color.orange)
  val Vert = Tribu(Color.green)
}

