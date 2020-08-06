import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {


  "Euler243" should "be OK" in {
    println("Euler243")
    val premiers = (new CheckEulerPrime(1540000, 10000)).premiers

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

    def resilience2(l: List[BigInt]) = {
      var t_ici = Calendar.getInstance()
      val d = l.product
      val ld = l.distinct
      val lp1 = List(BigInt(1)) ++ premiers.takeWhile(_ < d).filter(bi => !ld.contains(bi))
      val dsq2 = math.sqrt(d.toDouble).floor.toLong
      val le2 = premiers.takeWhile(_ <= dsq2).filter(bi => !ld.contains(bi)).toList
      println("l", l.product, l)
      val lp2 = le2.map(p => {
        val max = d / p
        val lg = premiers.dropWhile(_ < p).takeWhile(_ <= max)
        lg.map(_ * p)
      })
      val dsq3 = math.pow(d.toDouble, 1.0 / 3.0).floor.toLong
      val le3 = premiers.takeWhile(_ <= dsq3).filter(bi => !ld.contains(bi)).toList
      val lp3 = le3.map(p => {
        val max = d / p
        val lg = premiers.dropWhile(_ < (p * p)).takeWhile(_ <= max)
        lg.map(_ * p)
      })
      val lp31: List[List[BigInt]] = le3.combinations(2).map(l => {
        val p = l.product
        val max = d / p
        val lg = le2.takeWhile(_ <= max)
        lg.map(_ * p)
      }).toList
      val lp32 = le3.map(p => {
        val max = math.pow(d.toDouble / p.toDouble, 1.0 / 2.0).floor.toLong
        val lg = le2.takeWhile(_ <= max)
        lg.map(bi => bi * bi * p)
      })
      val lp4 = le3.map(p => {
        val max = d / (p * p)
        val lg = le2.takeWhile(_ <= max)
        lg.map(_ * p * p)
      })
      println(math.pow(d.toDouble, 1.0 / 4.0).floor.toLong, l.sorted.last, math.pow(d.toDouble, 1.0 / 4.0).floor.toLong < l.sorted.last)
      println("  dsq2", dsq2, "  le2", le2)
      println("  dsq3", dsq3, "  le3", le3, "  lp3", lp3.mkString("\n  ", "\n  ", ""))
      println("  dsq3", dsq3, "  le3", le3, "  lp31", lp31.mkString("\n  ", "\n  ", ""))
      println("  dsq3", dsq3, "  le3", le3, "  lp32", lp32.mkString("\n  ", "\n  ", ""))
      println("  dsq3", dsq3, "  le3", le3, "  lp4", lp4.mkString("\n  ", "\n  ", ""))
      var lp5 = List[BigInt]()
      if (le2.size > 2) {
        val lp5z = le2.takeWhile(bi => (bi * bi) < d).combinations(3).toList.filter(_.product < d)
        println("  lp5z", lp5z.mkString(","))
        lp5 = lp5z.map(_.product)
      }
      val ls = (lp1 ++ lp2.flatten ++ lp3.flatten ++ lp31.flatten ++ lp32.flatten ++ lp4.flatten ++ lp5).distinct
      println("" + d + "\t" + ls.size + "/" + (d - 1) + "\t" + 1.0 * ls.size / (d.toDouble - 1),
        "ici2 " + timeStampD(t_ici, "ici", false))
      //println(ls.sorted)
      (d, "" + ls.size + "/" + (d - 1), 1.0 * ls.size / (d.toDouble - 1), ls.sorted)
    }

    def resilience3(l: List[BigInt]) = {
      var t_ici = Calendar.getInstance()
      val d = l.product
      val ld = l.distinct
      val lp1 = List(BigInt(1)) ++ premiers.takeWhile(_ < d).filter(bi => !ld.contains(bi))
      val lp2 = lp1.map(p => {
        lp1.takeWhile(_.toDouble < d.toDouble / p.toDouble).map(_ * p)
      }).flatten
      val lp3 = lp2.map(p => {
        lp2.takeWhile(_.toDouble < d.toDouble / p.toDouble).map(_ * p)
      }).flatten
      val lp4 = lp3.map(p => {
        lp3.takeWhile(_.toDouble < d.toDouble / p.toDouble).map(_ * p)
      }).flatten
      val ls = (lp1 ++ lp2 ++ lp3 ++ lp4).distinct
      println("" + d + "\t" + ls.size + "/" + (d - 1) + "\t" + 1.0 * ls.size / (d.toDouble - 1),
        "ici3 " + timeStampD(t_ici, "ici", false))
      //println(ls.sorted)
      (d, "" + ls.size + "/" + (d - 1), 1.0 * ls.size / (d.toDouble - 1), ls.sorted)
    }

    var t_la = Calendar.getInstance()
    //resilience(12) shouldEqual(4, 11)
    println(1.0 * 15499 / 94744)
    println(94745, new EulerDiv(94745).primes)

    resilience(12)._3 shouldEqual resilience2(List(2, 2, 3))._3
    resilience(2 * premiers.take(5).product.toInt)
    resilience(2 * 2 * 3 * 3 * premiers.take(5).product.toInt)
    (3 to 7).map(i => {
      val z = resilience(premiers.take(i).product.toInt)
      val y = resilience3(premiers.take(i).toList)
      val ldiff = z._4.diff(y._4)
      println(premiers.take(i).product.toInt, "ldiff", ldiff.size, ldiff.map(bi => (bi, new EulerDiv(bi).primes)).take(40))
      println(y._1, y._2, y._3)
      z._4.diff(y._4).size shouldEqual 0
      val z3 = resilience(premiers.take(i).product.toInt * 3)
      val y3 = resilience3(premiers.take(i).toList :+ 3)
      val ldiff3 = z3._4.diff(y3._4)
      println(premiers.take(i).product.toInt * 3, "ldiff3", ldiff3.size, ldiff3.map(bi => (bi, new EulerDiv(bi).primes)).take(40))
      println(y3._1, y3._2, y3._3)
      z3._4.diff(y3._4).size shouldEqual 0
    })
    t_la = timeStamp(t_la, "là")

    val result = 0
    println("Euler243[" + result + "]")
    result shouldEqual 0
  }

}