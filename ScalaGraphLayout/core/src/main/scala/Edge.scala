package graphlayout

import java.awt.{Color, Graphics2D}

class Edge(val from: GNode, val to: GNode) {
  var len = .0

  override def toString: String = from.lbl+(from.x.toInt, from.y.toInt).toString+ "->" + to.lbl+(to.x.toInt, to.y.toInt).toString

  def getNodes = List(from, to)

  def paint(g: Graphics2D): Unit = {
    g.setColor(Color.green)
    g.drawLine(from.x.toInt, from.y.toInt, to.x.toInt, to.y.toInt)
  }
}
