/**
  * Created by mariachere on 22.04.2015.
  */
package graphlayout

class Depot(var ts: Int, var ph: Double, val tribu: Tribu) {
  def update(ts2: Int, d: Double) = {
    ts = ts2
    ph += d
  }
}
