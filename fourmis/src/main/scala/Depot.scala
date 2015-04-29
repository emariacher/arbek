/**
 * Created by mariachere on 22.04.2015.
 */
package labyrinthe

class Depot(val ts: Int, val ph: Pheronome.Value, val fourmi: Jeton) {

}

object Pheronome extends Enumeration {
  type Pheronome = Value
  val CHERCHE, RAMENE, REVIENS = Value
  Pheronome.values foreach println
}
