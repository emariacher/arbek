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

    def B(n: BigInt, verbose: Boolean = false): List[BigInt] = {
      val r = (0 to n.toInt).map(binomial(n, _)).toList
      if (verbose) {
        println("B(" + n + "): " + r.product, r)
      }
      r
    }

    def B2(n: BigInt, prevl: List[BigInt], verbose: Boolean = false): List[BigInt] = {
      var r: List[BigInt] = List(BigInt(1))
      r = r ++ prevl.sliding(2).map(_.sum) :+ BigInt(1)
      if (verbose) {
        println("B2(" + n + "): " + r)
      }
      r
    }

    B(5, true).product shouldEqual 2500

    var j = 5
    var prevl = List(BigInt(1), BigInt(1))
    (2 to j).foreach(n => {
      prevl = B2(n, prevl, true)
    })
    B(j, true).product shouldEqual prevl.product

    def D(n: BigInt, bn: List[BigInt], verbose: Boolean = false): List[BigInt] = {
      val r = ((new EulerDivisors(new EulerDiv(bn.product).primes).divisors) :+ BigInt(1)).sorted
      if (verbose) {
        println("D(" + n + "): " + r.sum, new EulerDiv(n).primes)
        println("    primbn[" + B(n) + "]", new EulerDiv(bn.product).primes.groupBy(u => u).toList.map(y => (y._1, y._2.length)).sortBy(_._1))
        println("    divbn", r)
      }
      r
    }

    prevl = List(BigInt(1), BigInt(1))
    (2 to j).foreach(n => {
      prevl = B2(n, prevl, true)
    })

    D(5, B(5)).sum shouldEqual 5467

    def S(n: BigInt): BigInt = {
      val r = (1 to n.toInt).map(o => D(o, B(o)))
      println("S(" + n + "): " + r.flatten.sum, r.flatten.groupBy(u => u).toList.map(y => (y._1, y._2.length)).sortBy(_._1))
      r.flatten.sum
    }

    S(5) shouldEqual 5736
    S(10) shouldEqual BigInt("141740594713218418")

    var result = 0
    println("Euler650[" + result + "]")
    result shouldEqual 0
  }
}