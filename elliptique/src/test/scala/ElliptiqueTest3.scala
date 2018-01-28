import org.scalatest._

import scala.math.BigInt


/*
https://www.coindesk.com/math-behind-bitcoin/
https://crypto.stackexchange.com/questions/44304/understanding-elliptic-curve-point-addition-over-a-finite-field
https://fr.wikipedia.org/wiki/Courbe_elliptique
 */

class ElliptiqueTest3 extends FlatSpec with Matchers {
  "Teste la multiplication" should "be OK" in {
    val e = new Elliptique(67, 0, 7)
    (500 to 0).foreach(q => {
      e.mul((BigInt(0), BigInt(0)), q)._2 shouldEqual q
    })
  }
}
