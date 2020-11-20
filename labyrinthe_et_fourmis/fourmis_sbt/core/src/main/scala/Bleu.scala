package labyrinthe

import labyrinthe.FrontiereV._
import labyrinthe.Tableaux._

class Bleu(couleur: String, rayon: Int, fourmiliere: Fourmiliere) extends Jeton(couleur, rayon, fourmiliere) {
  val ordreChoix = new Circular(List(nord, est, sud, ouest), auHasard, auHasard)

  def explique: Unit = {
    println(couleur + " n'essaye pas de continuer dans la même direction que la dernière fois mais continue à parcourir les priorités[" + ordreChoix + "]")
  }

  def firstStep: RowCol = new RowCol(888, 888)

  def auHasard(c: Circular): Frontiere = {
    c.elements(tbx.rnd.nextInt(c.elements.size))
  }

}

class BleuClair(couleur: String, rayon: Int, fourmiliere: Fourmiliere) extends Bleu(couleur, rayon, fourmiliere) {
  override def explique: Unit = {
    println(couleur + " essaye de continuer dans la même direction que la dernière fois[" + ordreChoix + "]")
  }

  override def firstStep: RowCol = getNext(lastDirection, true, true)
}


