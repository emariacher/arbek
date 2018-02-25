import org.scalatest._

import scala.math.BigInt


/*
https://www.coindesk.com/math-behind-bitcoin/
https://crypto.stackexchange.com/questions/44304/understanding-elliptic-curve-point-addition-over-a-finite-field
https://fr.wikipedia.org/wiki/Courbe_elliptique
 */

class Nombrepremiersquimarchent extends FlatSpec with Matchers {
  val premiers = EulerPrime.premiers1000

  def Trouve_les_nombres_premiers_qui_pourraient_marcher(a: BigInt, b: BigInt) = {
    println("Trouve les nombres premiers qui pourraient marcher pour y2 = x3 + "+a+"x + "+b)
    println("y2 = x3 + "+a+"x + "+b,premiers.take(100).filter(modlo => {
      val e = new Elliptique(modlo, a, b)
      e.getDelta should not equal 0
      e.curve.size > modlo & e.curve.filter(p => p._1 * p._2 == 0).isEmpty
    }))

  }

  "Trouve les nombres premiers qui pourraient marcher pour y2 = x3 + 7" should "be OK" in {
    Trouve_les_nombres_premiers_qui_pourraient_marcher(0,7)
  }


  "Trouve les nombres premiers qui pourraient marcher pour y2 = x3 + 3x + 5" should "be OK" in {
    Trouve_les_nombres_premiers_qui_pourraient_marcher(3,5)
  }

}
