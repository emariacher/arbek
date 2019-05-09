/**
  * Created by mariachere on 22.04.2015.
  */
package graphlayout

object Depot {
  val valeurDepot = 100.0
  val display = valeurDepot / 10
  val evaporation = 0.995
  val evapore = 2 // plus assez de pheronome pour que ca ait un effet
}

class Depot(var ph: Double, val tribu: Tribu) {
  def update(d: Double) = ph += d

  def evapore = ph = ph * Depot.evaporation
}
