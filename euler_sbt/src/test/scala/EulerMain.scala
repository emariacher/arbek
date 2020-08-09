import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {


  "Euler243" should "be OK" in {
    println("Euler243")
    val premiers = (new CheckEulerPrime(2000000, 10000)).premiers

    def resilience(d: Int) = {
      var t_ici = Calendar.getInstance()
      val primes = new EulerDiv2(d, premiers, false).primes
      val divisors = new EulerDivisors(primes).divisors
      val resil = Range(1, d, 2).filter(i => {
        //divisors.filter(div => i % div == 0).isEmpty
        divisors.find(div => i % div == 0) match {
          case Some(i) => false
          case _ => true
        }
      })
      val pgcd_primes = new EulerDiv(resil.length).primes intersect primes
      println("" + d + "\t" + resil.length + "/" + (d - 1) + "\t" + (1.0 * resil.length / (d - 1)),
        "ici " + timeStampD(t_ici, "ici", false))
      //println(resil.toList)
      (d, "" + resil.length + "/" + (d - 1), (1.0 * resil.length / (d - 1)), resil.toList)
    }

    def resilience3(l: List[BigInt]) = {
      var t_ici = Calendar.getInstance()
      val d = l.product
      val ld = l.distinct
      val lp1 = List(BigInt(1)) ++ premiers.takeWhile(_ < d).filter(bi => !ld.contains(bi))
      val lp2 = lp1.takeWhile(p => (p * p) <= d).flatMap(p => {
        //println("in lp2", p, lp1.takeWhile(bi => bi.toDouble < d.toDouble / p.toDouble).map(_ * p))
        lp1.takeWhile(bi => bi.toDouble < d.toDouble / p.toDouble).map(_ * p)
      }).sorted.distinct
      val lp3 = lp2.takeWhile(p => (p * p * p) <= d).flatMap(p => {
        //println("in lp3", p, lp2.takeWhile(bi => bi.toDouble < d.toDouble / p.toDouble).map(_ * p))
        lp2.takeWhile(_.toDouble < d.toDouble / p.toDouble).map(_ * p)
      }).sorted.distinct
      val lp4 = lp3.takeWhile(p => (p * p * p * p) <= d).flatMap(p => {
        //println("in lp4", p, lp3.takeWhile(bi => bi.toDouble < d.toDouble / p.toDouble).map(_ * p))
        lp3.takeWhile(_.toDouble < d.toDouble / p.toDouble).map(_ * p)
      }).sorted.distinct
      /*println("  lp1", lp1)
      println("  lp2", lp2)
      println("  lp3", lp3)
      println("  lp4", lp4)*/
      val ls = lp4
      println("" + d + "\t" + ls.size + "/" + (d - 1) + "\t" + 1.0 * ls.size / (d.toDouble - 1),
        "ici3 " + timeStampD(t_ici, "ici", false))
      if (lp4.diff(lp3).nonEmpty) {
        val pp = lp2.tail.head
        println(pp, math.pow(pp.toDouble, 4), d, "lp4.diff(lp3)", lp4.diff(lp3).map(bi => (bi, new EulerDiv(bi).primes)).take(2))
        (math.pow(pp.toDouble, 4) < d.toDouble) shouldEqual true
        (math.pow(pp.toDouble, 5) < d.toDouble) shouldEqual false
      }
      (d, "" + ls.size + "/" + (d - 1), 1.0 * ls.size / (d.toDouble - 1), ls.sorted)
    }


    def resilience4(l: List[BigInt]) = {
      var t_ici = Calendar.getInstance()
      val d = l.product
      print("" + d, l.last, "")
      val ld = l.distinct
      var lp = List(BigInt(1)) ++ premiers.takeWhile(_ < d).filter(bi => !ld.contains(bi))
      var oldlp = List[BigInt]()
      var exponent = 2
      while (lp.diff(oldlp).nonEmpty) {
        oldlp = lp
        lp = lp.takeWhile(p => math.pow(p.toDouble, exponent) <= d.toDouble).flatMap(p => {
          val lp2 = lp.takeWhile(bi => bi.toDouble < d.toDouble / p.toDouble).map(_ * p)
          //println("  in while", exponent, p, lp.takeWhile(bi => bi.toDouble < d.toDouble / p.toDouble).map(_ * p))
          lp2
        }).sorted.distinct
        exponent += 1
      }
      val ls = lp
      println("\t" + ls.size + "/" + (d - 1) + "\t" + 1.0 * ls.size / (d.toDouble - 1), exponent,
        "ici4 " + timeStampD(t_ici, "ici", false))
      (d, "" + ls.size + "/" + (d - 1), 1.0 * ls.size / (d.toDouble - 1), ls.sorted)
    }

    var t_la = Calendar.getInstance()
    //resilience(12) shouldEqual(4, 11)
    println(1.0 * 15499 / 94744)

    resilience(2 * premiers.take(5).product.toInt)
    resilience(2 * 2 * 3 * 3 * premiers.take(5).product.toInt)
    var lresilience = List[Double]()
    (3 to 7).foreach(i => {
      val z = resilience(premiers.take(i).product.toInt)
      val y = resilience4(premiers.take(i).toList)
      val ldiff = z._4.diff(y._4)
      //println(premiers.take(i).product.toInt, "ldiff", ldiff.size, ldiff.map(bi => (bi, new EulerDiv(bi).primes)).take(40))
      z._4.diff(y._4).size shouldEqual 0
      val z3 = resilience(premiers.take(i).product.toInt * 3)
      val y3 = resilience4(premiers.take(i).toList :+ 3)
      val ldiff3 = z3._4.diff(y3._4)
      //println(premiers.take(i).product.toInt * 3, "ldiff3", ldiff3.size, ldiff3.map(bi => (bi, new EulerDiv(bi).primes)).take(40))
      z3._4.diff(y3._4).size shouldEqual 0
      lresilience = lresilience :+ y._3
    })
    t_la = timeStamp(t_la, "là 1")
    println(premiers.take(9).product.toInt)
    println(lresilience)
    println(lresilience.sliding(2).map(c => c.head / c.last).toList)
    println(lresilience.sliding(2).map(c => c.head / c.last).toList.sliding(2).map(c => c.head / c.last).toList)

    val result = 0
    println("Euler243[" + result + "]")
    result shouldEqual 0
  }

}