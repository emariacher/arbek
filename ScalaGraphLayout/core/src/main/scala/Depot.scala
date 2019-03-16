/**
 * Created by mariachere on 22.04.2015.
 */
package graphlayout

class Depot(val ts: Int, val ph: Pheromone.Value, val fourmi: Jeton) {

}

object Pheromone extends Enumeration {
  type Pheronome = Value
  val CHERCHE, RAMENE, REVIENS, MORT = Value
  Pheromone.values foreach println
}
