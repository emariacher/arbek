/**
  * Created by mariachere on 22.04.2015.
  */
package graphlayout

object Depot {
  val valeurDepot = 100.0
  val display = valeurDepot / 10
  val evaporation = 0.99
}

class Depot(var ts: Int, var ph: Double, val tribu: Tribu) {
  def update(ts2: Int, d: Double) = {
    ts = ts2
    ph += d
  }

  def evapore = {
    ph = ph * Depot.evaporation
  }

 }
