package graphlayout

import java.awt.{Color, Graphics2D}

class GNode(val lbl: String) extends Node{
  override def toString: String = "{" + lbl + "}"

  override def hashCode: Int = lbl.hashCode

  override def getID = lbl

  override def paint(g: Graphics2D) {
    val w = g.getFontMetrics.stringWidth(lbl) + 10
    val h = g.getFontMetrics.getHeight() + 4
    g.setColor(Color.orange)
    g.fillRect(x.toInt - w / 2, y.toInt - h / 2, w, h)
    g.setColor(Color.black)
    g.drawRect(x.toInt - w / 2, y.toInt - h / 2, w - 1, h - 1)
    g.drawString(lbl, x.toInt - (w - 10) / 2, (y.toInt + (h - 8) / 2))
  }
}
