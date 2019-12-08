import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler650" should "be OK" in {
    println("Euler650")

    // explore how modulo addition & multiplication work
    def m(bi: BigInt, mod: BigInt = BigInt(1000000007)): BigInt = bi % mod

    val mod = 23
    (m(27, mod) + m(37, mod)) shouldEqual m(27 + 37, mod)
    m(m(27, mod) * m(37, mod), mod) shouldEqual m(27 * 37, mod)
    EulerPrime.isPrime(1000000007) shouldBe true

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
      //val r = ((new EulerDivisors(new EulerDiv(bn.product).primes).divisors) :+ BigInt(1)).sorted
      val r = ((new EulerDivisors(bn.map(o => new EulerDiv(o).primes).flatten).divisors) :+ BigInt(1)).sorted
      if (verbose) {
        println("D(" + n + "): " + r.sum, r)
      }
      r
    }

    def D3(n: BigInt, bn: List[BigInt], verbose: Boolean = false): List[BigInt] = {
      val r = ((new EulerDivisors(bn.map(o => new EulerDiv(o).primes).flatten.map(m(_))).divisors.map(m(_))) :+ BigInt(1)).sorted
      if (verbose) {
        println("D(" + n + "): " + r.sum, r)
      }
      r
    }

    D(5, B(5)).sum shouldEqual 5467

    def S(n: BigInt): BigInt = {
      val r = (1 to n.toInt).map(o => D(o, B(o)))
      println("S(" + n + "): " + r.flatten.sum, r.flatten.groupBy(u => u).toList.map(y => (y._1, y._2.length)).sortBy(_._1))
      r.flatten.sum
    }

    S(5) shouldEqual 5736
    S(10) shouldEqual BigInt("141740594713218418")

    j = 15
    var t_la = Calendar.getInstance()
    var r: BigInt = 1
    prevl = List(BigInt(1), BigInt(1))
    (2 to j).foreach(n => {
      prevl = B2(n, prevl, n < 11)
      r += D(n, prevl, n < 11).sum
      timeStamp(t_la, "D & B2 " + j + " : " + n)
      n match {
        case 5 => r shouldEqual 5736
        case 10 => r shouldEqual BigInt("141740594713218418")
        case _ =>
      }
    })
    t_la = timeStamp(t_la, "D & B2 " + j)
    r = 1
    prevl = List(BigInt(1), BigInt(1))
    (2 to j).foreach(n => {
      prevl = B2(n, prevl, n < 11)
      r += m(D3(n, prevl, n < 11).sum)
      timeStamp(t_la, "D3 & B2 " + j + " : " + n)
      n match {
        case 5 => r shouldEqual 5736
        case 10 => m(r) shouldEqual m(BigInt("141740594713218418"))
        case _ =>
      }
    })
    t_la = timeStamp(t_la, "D3 & B2 " + j)

    var result = 0
    println("Euler650[" + result + "]")
    result shouldEqual 0
  }
}