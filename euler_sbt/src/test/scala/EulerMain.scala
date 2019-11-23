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
      val y = z.map(l => (l, div(l))).groupBy(_._1.head).toList.sortBy(_._1).map(q => (q._1, q._2.length, q._2))
      println("S(" + n + ")", prems.length, z.length, z.flatten.sum,
        y.mkString("\n   ", "\n   ", "\n   "), z.map(_.sum).sorted)
      (z.flatten.sum, y)
    }

    def T(a: BigInt, n: Double, prems: List[BigInt]): (BigInt, BigInt, List[BigInt]) = {
      //val t_iciS = timeStamp(t_start, "")
      val a1 = a.toDouble + 1.0
      val z = prems.filter(_ > a).filter(b => {
        val b1 = b.toDouble + 1.0
        val ratio = b1 / a1
        val c: Double = (b1 * ratio) - 1.0
        if ((c % 1 <= 0.00001) && (c < n)) {
          //println("    ", a, b, c)
          (prems.contains(BigDecimal(c).setScale(0, BigDecimal.RoundingMode.HALF_DOWN).toBigInt))
        } else false
      })
      //timeStamp(t_iciS, "la! T(" + a + ")")
      //println(a, n, z)
      (a, z.length, z)
    }

    def U(n: BigInt, prems: List[BigInt]): List[(BigInt, BigInt, List[BigInt])] = {
      val premsn: List[BigInt] = prems.takeWhile(_ < n)
      val z: List[(BigInt, BigInt, List[BigInt])] = premsn.map(a => {
        T(a, n.toDouble, premsn)
      })
      z.filter(!_._3.isEmpty)
    }

    def V(n: BigInt, prems: List[BigInt]): BigInt = {
      U(n, prems).map(z => {
        z._3.map(y => {
          //println(z, y, ((y + 1) * (y + 1) / (z._1 + 1)) - 1, z._1 + y + ((y + 1) * (y + 1) / (z._1 + 1)) - 1)
          z._1 + y + ((y + 1) * (y + 1) / (z._1 + 1)) - 1
        }).sum
      }).sum
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
    println("********************************")
    val prems: List[BigInt] = EulerPrime.premiers10000.toList
    println(U(100, prems))
    V(100, prems) shouldEqual 1035
    val i = 450
    println("\ni=" + i)
    println(U(i, prems).mkString("\n   ", "\n   ", "\n   "))
    S(i)._1 shouldEqual V(i, prems)

    /*(1 until 1000).foreach(i => {
      println("i=" + i)
      S(i)._1 shouldEqual V(i, prems)
    })*/


    var result = 0
    println("Euler518[" + result + "]")
    result shouldEqual 0

  }
}