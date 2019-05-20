package graphlayout

import java.awt.Graphics2D

import graphlayout.Tableaux.tbx

class Fourmiliere(val tribu: Tribu, val centre: FixedNode, val lfourmi: List[Fourmi]) {
  val c = tbx.findCarre(centre.x, centre.y)
  var retoursFourmiliere = scala.collection.mutable.Map[Int, Int]()

  def faisSavoirAuxFourmisQuEllesFontPartieDeLaFourmiliere = lfourmi.foreach(_.fourmiliere = this)

  def retour(ouiMaisDOu: Int) = {
    retoursFourmiliere(ouiMaisDOu) = retoursFourmiliere.getOrElse(ouiMaisDOu, 0) + 1
  }


  override def toString = tribu + " " + centre + " " + lfourmi.mkString("{", ",", "}")

  def paint(g: Graphics2D) {
    g.setColor(tribu.c.color)
    g.draw3DRect(centre.x.toInt, centre.y.toInt, 20, 20, true)
    g.drawString(retoursFourmiliere.mkString("[", ",", "]"), centre.x.toInt, centre.y.toInt)
  }
}
