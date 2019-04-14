package graphlayout

import java.awt.{Color, Graphics2D}

import scala.swing.Label

class ANode(val tribu: Tribu) extends Node {
  var selected = false
  var log = List[(Int, Int)]()

  def getID = hashCode.toString

  def paint(g: Graphics2D) {
    g.setColor(tribu.couleur)
    log.foreach(p => g.fillOval(p._1, p._2, 3, 3))
    g.fillOval(x.toInt, y.toInt, 10, 10)
    g.setColor(if (selected) {
      Color.red
    } else {
      Color.black
    })
    g.drawOval(x.toInt, y.toInt, 10, 10)
    log = log :+ (x.toInt, y.toInt)
  }
}

case class Tribu private(tribu: Color) {
  override def toString = "Tribu_" + tribu.toString

  var label = new Label {
    text = "*"
    foreground = tribu
  }

  def couleur = tribu
}

object Tribu {
  val tribus = List(Tribu(Color.orange), Tribu(Color.green), Tribu(Color.blue), Tribu(Color.red), Tribu(Color.cyan), Tribu(Color.yellow))
}

