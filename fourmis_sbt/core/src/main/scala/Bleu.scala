package labyrinthe

import scala.collection.mutable.LinkedList
import labyrinthe.FrontiereV._
import scala.collection.mutable.Queue
import kebra._
import labyrinthe.Tableaux._

class Bleu(couleur: String, rayon: Int, fourmiliere: Fourmiliere) extends Jeton(couleur, rayon, fourmiliere) {
    val ordreChoix = new Circular(List(nord, est, sud, ouest), auHasard, auHasard)

    // n'essaye pas de continuer dans la meme direction que la derniere fois mais continue a parcourir les priorites
    def firstStep: RowCol = new RowCol(888, 888)

    def auHasard(c: Circular): Frontiere = {
        c.elements(tbx.rnd.nextInt(c.elements.size))
    }

}

class BleuClair(couleur: String, rayon: Int, fourmiliere: Fourmiliere) extends Bleu(couleur, rayon, fourmiliere) {
    // essaye de continuer dans la meme direction que la derniere fois
    override def firstStep: RowCol = getNext(lastDirection, true, true)
}


