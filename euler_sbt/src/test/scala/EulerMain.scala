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

    var result = 0
    println("Euler650[" + result + "]")
    result shouldEqual 0
  }
}