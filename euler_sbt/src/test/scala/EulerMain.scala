import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler518" should "be OK" in {
    println("Euler518")

    def div(l: List[BigInt]) = (l.sorted.apply(1) + 1).toDouble / (l.sorted.head + 1).toDouble

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
      //val t_iciS = timeStamp(t_start, "")
      val prems: List[BigInt] = EulerPrime.premiers10000.filter(_ < n).toList
      val z = prems.combinations(3).filter(Yes2(_)).toList
      val lp = z.flatten.distinct.sorted
      val y = z.map(l => (l, div(l))).groupBy(_._1.head).toList.sortBy(_._1)
      println("S(" + n + ")", prems.length, z.length, z.flatten.sum,
        /*lp, "\n  ",
        prems.filter(p => !lp.contains(p)),*/
        y.mkString("\n   ", "\n   ", "\n   "), z.map(_.sum).sorted)
      //val t_laS = timeStamp(t_iciS, "la! S(" + n + ")")
      (z.flatten.sum, y)
    }

    def T(a: BigInt, n: Double, prems: List[BigInt]): List[BigInt] = {
      val t_iciS = timeStamp(t_start, "")
      val a1 = a.toDouble + 1.0
      val z = prems.filter(_ > a).filter(b => {
        val b1 = b.toDouble + 1.0
        val ratio = b1 / a1
        val c: Double = (b1 * ratio) - 1.0
        //println(a, b, c, (c % 1 <= 0.00001))
        if ((c % 1 <= 0.1) && (c < n)) {
          (prems.contains(BigDecimal(c).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt))
        } else false
      })
      timeStamp(t_iciS, "la! T(" + a + ")")
      z
    }

    def U(n: BigInt, prems: List[BigInt]): List[List[BigInt]] = {
      val premsn: List[BigInt] = prems.takeWhile(_ < n)
      val z: List[List[BigInt]] = premsn.map(a => {
        T(a, n.toDouble, premsn)
      })
      z
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
    S(100)._1 shouldEqual 1035
    /*println("********************************")
    S(16)
    S(32)
    S(64)
    S(128)
    S(256)
    S(512)
    S(1024)*/
    println("********************************")
    val prems: List[BigInt] = EulerPrime.premiers10000.toList
    println(T(2, 1000, prems))
    println(T(5, 1000, prems))
    println(T(7, 1000, prems))
    println(T(11, 1000, prems))
    println(U(100, prems).mkString("\n", "\n", "\n"))

    var result = 0
    println("Euler518[" + result + "]")
    result shouldEqual 0

  }
}