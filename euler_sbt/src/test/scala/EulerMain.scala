import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {


  "Euler243" should "be OK" in {
    println("Euler243")
    val premiers = EulerPrime.premiers100000

    def resilience(d: Int) = {
      var t_ici = Calendar.getInstance()
      val primes = new EulerDiv(d).primes
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
        timeStampD(t_ici, "ici", false))
      println(resil)
      (d, "" + resil.length + "/" + (d - 1), (1.0 * resil.length / (d - 1)))
    }

    def resilience2(l: List[BigInt]) = {
      var t_ici = Calendar.getInstance()
      val d = l.product
      val ld = l.distinct
      val lp1 = List(BigInt(1)) ++ premiers.takeWhile(_ < d).filter(bi => !ld.contains(bi))
      val dsq = math.sqrt(d.toDouble).floor.toLong
      var le = premiers.takeWhile(_ < dsq).filter(bi => !ld.contains(bi)).toList
      println("l", l.product, l, "dsq", dsq, "le", le)
      val lp2 = le.map(p => {
        val max = d / p
        val lg = premiers.dropWhile(_ < p).takeWhile(_ <= max)
        lg.map(_ * p)
      }).flatten.distinct
      //println("lp2", lp2)
      val dsq3 = math.pow(d.toDouble, 1.0 / 3.0).floor.toLong
      le = premiers.takeWhile(_ <= dsq3).filter(bi => !ld.contains(bi)).toList
      println("  dsq3", dsq3, "le", le)
      val lp3 = le.map(p => {
        val max = d / p
        val lg = premiers.dropWhile(_ < (p * p)).takeWhile(_ <= max)
        lg.map(_ * p) ++ List(p * p * p)
      }).flatten.distinct
      //println("lp3", lp3)
      println(math.pow(d.toDouble, 1.0 / 4.0).floor.toLong, l.sorted.last, math.pow(d.toDouble, 1.0 / 4.0).floor.toLong < l.sorted.last)
      val ls = (lp1 ++ lp2 ++ lp3).distinct
      println("" + d + "\t" + ls.size + "/" + (d - 1) + "\t" + 1.0 * ls.size / (d.toDouble - 1),
        timeStampD(t_ici, "ici", false))
      println(ls.sorted)
      (d, "" + ls.size + "/" + (d - 1), 1.0 * ls.size / (d.toDouble - 1))
    }

    var t_la = Calendar.getInstance()
    //resilience(12) shouldEqual(4, 11)
    println(1.0 * 15499 / 94744)
    println(94745, new EulerDiv(94745).primes)

    resilience(12)._3 shouldEqual resilience2(List(2, 2, 3))._3
    resilience(premiers.take(4).product.toInt)._3 shouldEqual resilience2(premiers.take(4).toList)._3
    resilience(premiers.take(4).product.toInt * 3)._3 shouldEqual resilience2(premiers.take(4).toList :+ 3)._3
    println("******", new EulerDiv(2197).primes)
    println("******", new EulerDiv(2249).primes)
    resilience(premiers.take(5).product.toInt)._3 shouldEqual resilience2(premiers.take(5).toList)._3
    resilience(2 * premiers.take(5).product.toInt)
    resilience(2 * 2 * 3 * 3 * premiers.take(5).product.toInt)
    println("*+****", new EulerDiv(5491).primes)
    (3 to 7).map(i => {
      val z = resilience(premiers.take(i).product.toInt)
      val y = resilience2(premiers.take(i).toList)
      y shouldEqual z
      z
    }).sliding(2).
      toList.map(l => {
      println("____ " + (l.head._3 / l.last._3) + " ____")
      (l.head._3 / l.last._3)
    }).sliding(2).
      toList.map(l => {
      println("_+_ " + (l.head / l.last) + " _+_")
    })
    t_la = timeStamp(t_la, "là")

    val result = 0
    println("Euler243[" + result + "]")
    result shouldEqual 0
  }

}