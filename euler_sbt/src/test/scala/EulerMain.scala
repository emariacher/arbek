import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {


  "Euler243" should "be OK" in {
    println("Euler243")
    val premiers = EulerPrime.premiers1000

    def resilience(d: Int) = {
      var t_ici = Calendar.getInstance()
      val primes = new EulerDiv(d).primes
      val divisors = new EulerDivisors(primes).divisors
      val resil = Range(1, d, 2).filter(i => {
        //divisors.filter(div => i % div == 0).isEmpty
        divisors.find(div => i % div == 0) match {
          case Some(i) => false
          case _ => true
        }
      })
      val pgcd_primes = new EulerDiv(resil.length).primes intersect primes
      println("" + d + "\t" + resil.length + "/" + (d - 1) + "\t" + (1.0 * resil.length / (d - 1)) +
        "\t" + primes + "\t" + new EulerDiv(resil.length).primes + "\t" + pgcd_primes,
        timeStampD(t_ici, "ici", false))
      (d, "" + resil.length + "/" + (d - 1), (1.0 * resil.length / (d - 1)))
    }

    var t_la = Calendar.getInstance()
    //resilience(12) shouldEqual(4, 11)
    println(1.0 * 15499 / 94744)
    println(94745, new EulerDiv(94745).primes)

    resilience(premiers.take(4).product.toInt)
    resilience(premiers.take(5).product.toInt)
    resilience(2 * premiers.take(5).product.toInt)
    resilience(2 * 2 * 3 * 3 * premiers.take(5).product.toInt)
    (4 to 8).map(i => resilience(premiers.take(i).product.toInt)).sliding(2).
      toList.map(l => println("____ " + (l.head._3 / l.last._3) + " ____"))
    t_la = timeStamp(t_la, "là")

    val result = 0
    println("Euler243[" + result + "]")
    result shouldEqual 0
  }

}