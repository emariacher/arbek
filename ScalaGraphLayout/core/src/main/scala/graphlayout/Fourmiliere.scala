package graphlayout

class Fourmiliere(val tribu: Tribu, val centre: FixedNode, val lfourmi: List[Fourmi]) {
  def faisSavoirAuxFourmisQuEllesFontPartieDeLaFourmiliere = lfourmi.foreach(_.fourmiliere = this)

  override def toString = tribu + " " + centre + " " + lfourmi.mkString("{", ",", "}")
}
