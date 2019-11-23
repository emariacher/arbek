import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler518" should "be OK" in {
    println("Euler518")

    def Yes(a: BigInt, b: BigInt, c: BigInt) = {
      var r = EulerPrime.isPrime(a) && EulerPrime.isPrime(b) && EulerPrime.isPrime(c) && (b + 1).toDouble / (a + 1).toDouble == (c + 1).toDouble / (b + 1).toDouble
      //if(r) println("           ", (a, b, c), (a + 1, b + 1, c + 1), (b + 1) / (a + 1), r)
      r
    }

    def YesV(a: BigInt, b: BigInt, c: BigInt) = {
      var r = EulerPrime.isPrime(a) && EulerPrime.isPrime(b) && EulerPrime.isPrime(c) && (b + 1).toDouble / (a + 1).toDouble == (c + 1).toDouble / (b + 1).toDouble
      println("           ", (a, b, c), (a + 1, b + 1, c + 1), (b + 1).toDouble / (a + 1).toDouble, r)
      r
    }

    def Yes2(l: List[BigInt]) = Yes(l.sorted.head, l.sorted.apply(1), l.sorted.last)

    def S(n: Int) = {
      val t_iciS = timeStamp(t_start, "")
      val prems = EulerPrime.premiers10000.filter(_ < n).toList
      val z = prems.combinations(3).filter(Yes2(_)).toList
      println("S(" + n + ")", prems.length, z.length, z.mkString("\n  ", " - ", "\n  "), z.map(_.sum).sorted, z.flatten.sum)
      val t_laS = timeStamp(t_iciS, "la! S(" + n + ")")
      z.flatten.sum
    }

    YesV(37, 151, 607) shouldEqual true
    YesV(71, 83, 97) shouldEqual true
    Yes(2, 5, 11) shouldEqual true
    Yes(2, 5, 13) shouldEqual false
    Yes(31, 47, 71) shouldEqual true
    Yes(31, 53, 71) shouldEqual false
    Yes(2, 6, 11) shouldEqual false
    Yes(5, 2, 11) shouldEqual false
    Yes2(List(5, 2, 11)) shouldEqual true
    println("********************************")
    S(100) shouldEqual 1035

    var result = 0
    println("Euler518[" + result + "]")
    result shouldEqual 0

  }
}