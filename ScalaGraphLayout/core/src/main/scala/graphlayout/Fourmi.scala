package graphlayout

import java.awt.{Color, Graphics2D}

import scala.util.Random

class Fourmi(val anode: ANode) extends ANode(anode.tribu) {
  var direction: Double = .0
  updateFromNode

  override def toString = "[%.0f,%.0f]".format(x, y) + tribu

  def avance = {
    anode.x += Math.sin(direction) * 2
    anode.y += Math.cos(direction) * 2
  }

  def redirige(largeur: Int, hauteur: Int, border: Int, rnd: Random): Unit = {
    if (anode.remetsDansLeTableau(largeur, hauteur, border)) {
      direction = rnd.nextDouble() * Math.PI * 2
    }
  }

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
