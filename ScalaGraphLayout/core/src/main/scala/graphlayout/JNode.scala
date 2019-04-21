package graphlayout

import java.awt.{Color, Graphics2D}

class JNode(tribu: Tribu) extends ANode(tribu) {

  override def toString = "[%.0f,%.0f]".format(x, y)
  override def paint(g: Graphics2D) {
    g.setColor(tribu.c.color)
    g.fillRect(x.toInt, y.toInt, 20, 20)
    g.setColor(Color.darkGray)
    g.drawRect(x.toInt, y.toInt, 20, 20)
    g.drawRect(x.toInt+2, y.toInt+2, 16, 16)
  }
}



