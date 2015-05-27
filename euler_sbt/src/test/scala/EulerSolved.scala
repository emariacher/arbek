import org.scalatest._

import scala.collection.immutable.ListSet

class EulerSolved extends FlatSpec with Matchers {
  "Euler32" should "be OK" in {
    println("Euler32")
    val result = Range(13 * 245, 9876 + 1).filter(i => {
      val lz = i.toString.toList
      lz.length == (new ListSet() ++ lz).toList.length
    }).filter(c => {
      c.toString.indexOf("0") < 0
    }).map(i => {
      val divs = new EulerDivisors(new EulerDiv(i).primes).divisors
      (i, divs)
    }).filter(c => {
      if (c._2.length == 2) {
        val lz = (c._2.mkString("", "", "") + c._1).toList
        lz.length == (new ListSet() ++ lz).toList.length && lz.length == 5
      } else if (c._2.length > 0) {
        true
      } else {
        false
      }
    }).map(c => {
      val z1 = c._1.toString.toList
      val z2 = c._2.filter(d => {
        z1.intersect(d.toString.toList).isEmpty && d.toString.indexOf("0") < 0
      })
      (c._1, z2.toList)
    }).filter(c => {
      c._2.length > 1
    }).map(c => {
      (c._1, c._2.combinations(2).filter(
        cb => cb.head * cb.last == c._1 &&
          (cb.head.toString + cb.last.toString).length == 5
      ).toList)
    }).filter(c => {
      !c._2.isEmpty
    }).map(c => {
      (c._1, c._2.filter(
        cb => {
          val lz = (cb.head.toString + cb.last.toString + c._1.toString).toList
          lz.length == (new ListSet() ++ lz).toList.length
        }
      ))
    }).filter(c => {
      !c._2.isEmpty
    }).map(c => {
      println(c)
      c._1
    }).sum
    println("Euler32[" + result + "]")
    result should be === 45228
  }

  "Euler38" should "be OK" in {
    println("Euler38")
    val result = (Range(123, 988).toList.filter(
      i => isPanDigital(i.toString)
    ).map(i => {
      val r = Range(1, 4).toList
      val z = r.map(k => i * k).mkString("", "", "")
      (i, z, isPanDigital1to9(z))
    }).filter(
        _._3
      )

      ++

      Range(1234, 9877).toList.filter(
        i => isPanDigital(i.toString)
      ).map(i => {
        val r = Range(1, 3).toList
        val z = r.map(k => i * k).mkString("", "", "")
        (i, z, isPanDigital1to9(z))
      }).filter(
          _._3
        )).map(t => {
      println(t)
      t
    }).maxBy(_._2)._2

    println("Euler38[" + result + "]")
    result should be === "932718654"

  }

  def isPanDigital(s: String): Boolean = {
    val lz = s.toList
    lz.length == (new ListSet() ++ lz).toList.length && s.indexOf("0") < 0
  }

  def isPanDigital1to9(s: String): Boolean = {
    isPanDigital(s) && s.length == 9
  }

  "Euler46" should "be OK" in {
    println("Euler46")
    val limit = 10000
    println("Find < " + limit + " odd composite numbers")
    val premiers = EulerPrime.premiers100000.takeWhile(_ <= limit).toList.tail
    println(premiers)
    val odds = Range(3, limit, 2).toList
    val oddComposites = odds diff premiers
    println(oddComposites)
    var sqrtlimit = math.sqrt(limit / 2).toInt
    var doubleSquares = Range(1, sqrtlimit).map(i => i * i * 2).toList
    println(doubleSquares)

    val result = oddComposites.find(odc => {
      doubleSquares.find(ds => {
        val index = premiers.indexOf(odc - ds)
        if (index > 0) {
          println(odc + "-" + ds + "==" + premiers.apply(index))
        }
        index > 0
      }).isEmpty
    }) match {
      case Some(i) => i
      case _ =>
    }

    println("Euler46[" + result + "]")
    result should be === 5777
  }

}
