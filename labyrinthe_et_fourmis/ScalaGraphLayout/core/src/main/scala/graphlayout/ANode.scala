package graphlayout

import java.awt.{Color, Graphics2D}

class ANode(val tribu: Tribu) extends Node {
  var selected = false
  var log = List[(Int, Int)]()

  override def getID = hashCode.toString

  def toString2 = "%.2f".format(mouvement)

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

