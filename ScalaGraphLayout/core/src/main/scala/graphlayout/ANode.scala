package graphlayout

import java.awt.{Color, Graphics2D}

import scala.swing.Label

class ANode(val tribu: Tribu) extends Node {
  var selected = false
  var log = List[(Int, Int)]()

  override def getID = hashCode.toString

  override def toString = "%.2f".format(mouvement)

  override def paint(g: Graphics2D) {
    g.setColor(tribu.c.color)
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

case class Tribu private(c: Couleur) {
  var label = new Label {
    text = "*"
    foreground = c.color
  }
}

object Tribu {
  val tribus = List(new Tribu(new Couleur("orange")), new Tribu(new Couleur("vertFonce")),
    new Tribu(new Couleur("bleu")), new Tribu(new Couleur("rouge")), new Tribu(new Couleur("violet")),
    new Tribu(new Couleur("bleuClair")))
}

