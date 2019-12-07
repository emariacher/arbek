import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler650" should "be OK" in {
    println("Euler650")

    def binomial(n: BigInt, k: BigInt): BigInt = {
      factorielle(n) / ((factorielle(k) * factorielle(n - k)))
    }

    binomial(5, 0) shouldEqual 1
    binomial(5, 1) shouldEqual 5
    binomial(5, 2) shouldEqual 10
    binomial(5, 3) shouldEqual 10
    binomial(5, 4) shouldEqual 5
    binomial(5, 5) shouldEqual 1

    def B(n: BigInt): BigInt = {
      (0 to n.toInt).map(binomial(n, _)).product
    }

    B(5) shouldEqual 2500

    def D(n: BigInt): List[BigInt] = {
      println(n, new EulerDiv(n).primes)
      println("    primbn[" + B(n) + "]", new EulerDiv(B(n)).primes.groupBy(u => u).toList.map(y => (y._1, y._2.length)).sortBy(_._1))
      println("    divbn", ((new EulerDivisors(new EulerDiv(B(n)).primes).divisors) :+ BigInt(1)).sorted)
      ((new EulerDivisors(new EulerDiv(B(n)).primes).divisors) :+ BigInt(1)).sorted
    }

    D(5).sum shouldEqual 5467

    def S(n: BigInt): BigInt = {
      val r = (1 to n.toInt).map(D(_))
      println("S(" + n + "): " + r.flatten.groupBy(u => u).toList.map(y => (y._1, y._2.length)).sortBy(_._1))
      r.flatten.sum
    }

    S(5) shouldEqual 5736
    S(10) shouldEqual BigInt("141740594713218418")

    //S(13)

    var result = 0
    println("Euler650[" + result + "]")
    result shouldEqual 0
  }
}