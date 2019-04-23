package graphlayout

import java.awt.{Color, Graphics2D}

class Fourmi(val anode: ANode) extends ANode(anode.tribu) {
  updateFromNode

  override def toString = "[%.0f,%.0f]".format(x, y) + tribu

  def updateFromNode: Unit = {
    x = anode.x
    y = anode.y
  }

  override def paint(g: Graphics2D) {
    g.setColor(tribu.c.color)
    g.fillOval(x.toInt, y.toInt, 7, 12)
    g.setColor(if (selected) {
      Color.red
    } else {
      Color.black
    })
    g.drawOval(x.toInt, y.toInt, 7, 12)
  }
}
