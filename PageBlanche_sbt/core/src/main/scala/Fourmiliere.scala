package labyrinthe

import labyrinthe.RaceFourmi.RaceFourmi
import scala.swing.Label

/**
 * Created by ericm_000 on 13.03.2016.
 */
class Fourmiliere(val nid: RowCol, val couleur: Couleur, val raceFourmi: RaceFourmi) {
  val label = new Label {
    text = couleur.toString
    foreground = couleur.color
  }
  var cnt = 0
  var cntall = 0
  var cntmp = 0

  def this(nid: RowCol, s: String, raceFourmi: RaceFourmi) = this(nid, new Couleur(s), raceFourmi)

  override def toString: String = "  fourmiliere[" + couleur.toString + "," + nid.toString + "] "

  override def equals(x: Any): Boolean = hashCode.equals(x.asInstanceOf[Fourmiliere].hashCode)

  override def hashCode: Int = toString.hashCode

}

object RaceFourmi extends Enumeration {
  type RaceFourmi = Value
  val ROND, RECTROND, RECT = Value
  RaceFourmi.values foreach println
}
