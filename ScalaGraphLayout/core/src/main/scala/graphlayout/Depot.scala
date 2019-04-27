/**
  * Created by mariachere on 22.04.2015.
  */
package graphlayout

object Depot {
  val valeurDepot = 100.0
  val display = valeurDepot / 10
  val evaporation = 0.99
}

class Depot(var ph: Double, val tribu: Tribu) {
  def update(d: Double) = ph += d

  def evapore = ph = ph * Depot.evaporation
}
