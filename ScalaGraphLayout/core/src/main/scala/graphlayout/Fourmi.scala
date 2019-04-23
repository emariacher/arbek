package graphlayout

import java.awt.{Color, Graphics2D}

import scala.util.Random

class Fourmi(val anode: ANode) {
  var direction: Double = .0

  override def toString = "[%.0f,%.0f]".format(anode.x, anode.y) + anode.tribu

  def avance = {
    anode.x += Math.sin(direction) * 2
    anode.y += Math.cos(direction) * 2
  }

  def redirige(largeur: Int, hauteur: Int, border: Int, rnd: Random): Unit = {
    if (anode.remetsDansLeTableau(largeur, hauteur, border)) {
      direction = rnd.nextDouble() * Math.PI * 2
    }
  }

  def paint(g: Graphics2D) {
    g.setColor(anode.tribu.c.color)
    anode.log.foreach(p => g.fillOval(p._1, p._2, 3, 3))
    g.fillOval(anode.x.toInt, anode.y.toInt, 7, 12)
    g.setColor(if (anode.selected) {
      Color.red
    } else {
      Color.black
    })
    g.drawOval(anode.x.toInt, anode.y.toInt, 7, 12)
    anode.log = anode.log :+ (anode.x.toInt, anode.y.toInt)
  }
}
