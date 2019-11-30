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
      println("   ", (a, b, c), (a + 1, b + 1, c + 1), (b + 1).toDouble / (a + 1).toDouble, r)
      println("           ", ((new EulerDiv(a + 1)).primes, (new EulerDiv(b + 1)).primes, (new EulerDiv(c + 1)).primes))
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

    def S2(n: Int) = {
      var s = S(n)
      var lp: Set[BigInt] = Set()
      s._2.foreach(z => {
        println(z._1)
        z._3.foreach(a => {
          val b = a._1
          val d: List[(BigInt, BigInt, List[BigInt])] = b.map(c => (c, c + 1, (new EulerDiv(c + 1)).primes))
          lp = lp ++ d.map(_._3).flatten
          println("   ", a, d)
        })
      })
      println(lp.toList.sorted)
      s
    }


    def T(a: BigInt, n: Double, prems: List[BigInt]): (BigInt, BigInt, List[BigInt]) = {
      //val t_iciS = timeStamp(t_start, "")
      val a1 = a.toDouble + 1.0
      val z = prems.filter(b => {
        if (b > a) {
          val b1 = b.toDouble + 1.0
          val ratio = b1 / a1
          val c: Double = (b1 * ratio) - 1.0
          if (c < n) {
            if (c % 1 <= 0.00001) {
              //println("    ", a, b, c)
              (prems.contains(BigDecimal(c).setScale(0, BigDecimal.RoundingMode.HALF_DOWN).toBigInt))
            } else if (c % 1 >= 0.9999) {
              //println("    ", a, b, c)
              (prems.contains(BigDecimal(c).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt))
            } else false
          } else false
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

    def W(a: BigInt, n: Double, prems: List[BigInt]): (BigInt, BigInt, List[BigInt]) = {
      val a1 = a.toDouble + 1.0
      val limit = BigDecimal(math.sqrt(a1 * n)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
      val z = prems.dropWhile(_ <= a).takeWhile(_ <= limit).filter(b => {
        val b1 = b.toDouble + 1.0
        val ratio = b1 / a1
        val c: Double = (b1 * ratio) - 1.0
        if (c % 1 <= 0.00001) {
          (prems.contains(BigDecimal(c).setScale(0, BigDecimal.RoundingMode.HALF_DOWN).toBigInt))
        } else if (c % 1 >= 0.9999) {
          (prems.contains(BigDecimal(c).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt))
        } else false
      })
      (a, z.length, z)
    }

    def X(n: BigInt, prems: List[BigInt]): List[(BigInt, BigInt, List[BigInt])] = {
      val premsn: List[BigInt] = prems.takeWhile(_ < n)
      val z: List[(BigInt, BigInt, List[BigInt])] = premsn.map(a => {
        W(a, n.toDouble, premsn)
      })
      z.filter(!_._3.isEmpty)
    }

    def Y(n: BigInt, prems: List[BigInt]): BigInt = {
      X(n, prems).map(z => {
        z._3.map(y => {
          //println(z, y, ((y + 1) * (y + 1) / (z._1 + 1)) - 1, z._1 + y + ((y + 1) * (y + 1) / (z._1 + 1)) - 1)
          z._1 + y + ((y + 1) * (y + 1) / (z._1 + 1)) - 1
        }).sum
      }).sum
    }


    YesV(37, 151, 607) shouldEqual true
    YesV(71, 83, 97) shouldEqual true
    YesV(337, 389, 449) shouldEqual true
    YesV(449, 509, 577) shouldEqual true
    YesV(2887, 2963, 3041) shouldEqual true
    YesV(397, 1193, 3581) shouldEqual true
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
    var S1000 = S2(1000)
    println("********************************")
    var prems: List[BigInt] = EulerPrime.premiers10000.toList
    println(U(100, prems))
    V(100, prems) shouldEqual 1035
    S1000._1 shouldEqual V(1000, prems)
    S1000._1 shouldEqual Y(1000, prems)

    val t_iciV = timeStamp(t_start, "")
    var j = 2000
    (1 until j).foreach(i => {
      V(i, prems)
    })
    val t_iciY = timeStamp(t_iciV, "la! V(" + j + ")")
    (1 until j).foreach(i => {
      Y(i, prems)
    })
    val t_laY = timeStamp(t_iciY, "la! Y(" + j + ")")
    j = 10000
    V(j, prems)
    var t_la = timeStamp(t_laY, "la! V(" + j + ")")
    Y(j, prems)
    t_la = timeStamp(t_la, "la! Y(" + j + ")")
    prems = EulerPrime.premiers100000.toList
    j = 50000
    println(2, j, W(2, j.toDouble, prems)._3)
    println(3, j, W(3, j.toDouble, prems)._3)
    println(5, j, W(5, j.toDouble, prems)._3)
    println(17, j, W(17, j.toDouble, prems)._3)
    println(19, j, W(19, j.toDouble, prems)._3)
    println(47, j, W(47, j.toDouble, prems)._3)
    t_la = timeStamp(t_la, "la! Y(" + j + ")")
    j = 100000
    Y(j, prems)
    t_la = timeStamp(t_la, "la! Y(" + j + ")")
    var i = 2
    var w = W(i, j.toDouble, prems)._3
    println(i, j, w)
    YesV(i, w.last, ((w.last + 1) * (w.last + 1) / (i + 1)) - 1) shouldEqual true
    println(i, j, BigDecimal(math.sqrt((i + 1) * j)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt)

    println(3, j, W(3, j.toDouble, prems)._3)
    println(5, j, W(5, j.toDouble, prems)._3)
    println(17, j, W(17, j.toDouble, prems)._3)
    println(19, j, W(19, j.toDouble, prems)._3)

    i = 47
    w = W(i, j.toDouble, prems)._3
    println(i, j, w)
    YesV(i, w.last, ((w.last + 1) * (w.last + 1) / (i + 1)) - 1) shouldEqual true
    println(i, j, BigDecimal(math.sqrt((i + 1) * j)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt)

    j = 4000
    S2(j)
    t_la = timeStamp(t_la, "la! Y(" + j + ")")
    var result = 0
    println("Euler518[" + result + "]")
    result shouldEqual 0

  }
}