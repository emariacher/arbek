package graphlayout

import java.awt.Graphics2D

class Fourmiliere(val tribu: Tribu, val centre: FixedNode, val lfourmi: List[Fourmi]) {
  def faisSavoirAuxFourmisQuEllesFontPartieDeLaFourmiliere = lfourmi.foreach(_.fourmiliere = this)

  override def toString = tribu + " " + centre + " " + lfourmi.mkString("{", ",", "}")

  def paint(g: Graphics2D) {
    g.setColor(tribu.c.color)
    g.draw3DRect(centre.x.toInt, centre.y.toInt, 20, 20, true)
    g.drawString(lfourmi.map(_.estRevenueALaFourmiliere).sum.toString, centre.x.toInt, centre.y.toInt)
  }
}
