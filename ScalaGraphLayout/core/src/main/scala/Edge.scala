package graphlayout

import java.awt.{Color, Graphics2D}

class Edge(val from: GNode, val to: GNode) {
  var len = .0

  override def toString: String = from.lbl + (from.x.toInt, from.y.toInt).toString + "->" + to.lbl + (to.x.toInt, to.y.toInt).toString + "[" + len + "]"

  def getNodes = List(from, to)

  def opTimize = {
    val deltaX = to.x - from.x
    val deltaY = to.y - from.y
    val ratio = len / Math.sqrt((deltaX * deltaX) + (deltaY * deltaY))
    to.x = to.x * (1 + ratio)
    from.x = from.x * (1 + ratio)
    to.y = to.y * (1 + ratio)
    from.y = from.y * (1 + ratio)
  }

  def paint(g: Graphics2D): Unit = {
    g.setColor(Color.green)
    g.drawLine(from.x.toInt, from.y.toInt, to.x.toInt, to.y.toInt)
  }
}
