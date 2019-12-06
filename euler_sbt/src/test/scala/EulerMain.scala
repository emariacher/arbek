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

    def D(n: BigInt): BigInt = {
      ((new EulerDivisors(new EulerDiv(B(n)).primes).divisors) :+ BigInt(1)).sum
    }

    D(5) shouldEqual 5467

    def S(n: BigInt): BigInt = {
      (1 to n.toInt).map(D(_)).sum
    }

    S(5) shouldEqual 5736
    S(10) shouldEqual BigInt("141740594713218418")

    (1 to 5).foreach(i => {
      val j = BigDecimal(math.pow(2, i)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
      println(i, j, S(j))
    })

    var result = 0
    println("Euler650[" + result + "]")
    result shouldEqual 0
  }
}