package graphlayout

import java.awt.{Color, Graphics2D}

import graphlayout.Tableaux.tbx

class JNode(tribu: Tribu) extends ANode(tribu) { // noeud de jaffe de la tribu
  var c = tbx.findCarre(x, y)

  override def toString: String = "[(%.0f,%.0f) ".format(x, y) + tribu + "]"

  override def paint(g: Graphics2D): Unit = {
    c = tbx.findCarre(x, y)
    g.setColor(tribu.c.color)
    g.fillRect(x.toInt, y.toInt, 20, 20)
    g.setColor(Color.black)
    g.drawRect(x.toInt, y.toInt, 20, 20)
    g.drawRect(x.toInt + 2, y.toInt + 2, 16, 16)
    g.drawString(c.toString, x.toInt, y.toInt)

  }
}



