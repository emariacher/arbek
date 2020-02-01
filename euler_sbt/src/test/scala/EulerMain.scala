import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {

  "Euler23" should "be OK" in {
    println("Euler23")

    def sumdiv(i: Int) = {
      (List(BigInt(1)) ++ new EulerDivisors(new EulerDiv(i)).divisors.dropRight(1)).sum
    }

    sumdiv(12) shouldEqual 16
    sumdiv(28) shouldEqual 28

    var result = 0
    println("Euler23[" + result + "]")
    result shouldEqual 0
  }

}