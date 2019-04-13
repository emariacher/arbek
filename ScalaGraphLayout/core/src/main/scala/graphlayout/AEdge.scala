package graphlayout

import java.awt.{Color, Graphics2D}

class AEdge(from: ANode, to: ANode) extends Edge(from, to) {

  override def toString: String = from.getID + (from.x.toInt, from.y.toInt).toString + "->" + to.getID + (to.x.toInt, to.y.toInt).toString + "[%.2f".format(len) + "/%.2f".format(dist._1) + "/%.2f]".format(diff)

  def getNodes = List(from, to)

  def paint(g: Graphics2D): Unit = {
    if (Math.abs(dist._1.toInt - len.toInt) > 40) {
      g.setColor(Color.red)
    } else if (Math.abs(dist._1.toInt - len.toInt) > 10) {
      g.setColor(Color.orange)
    } else {
      g.setColor(Color.green)
    }
    g.drawLine(from.x.toInt, from.y.toInt, to.x.toInt, to.y.toInt)
    g.drawString(dist._1.toInt + "/" + len.toInt, (from.x.toInt + to.x.toInt) / 2, (from.y.toInt + to.y.toInt) / 2)
  }
}
