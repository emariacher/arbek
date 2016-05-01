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
      //println((a, b, c), (a + 1, b + 1, c + 1), (b + 1) / (a + 1), r)
      r
    }

    def Yes2(l: List[BigInt]) = Yes(l.head, l.apply(1), l.last)

    def S(n: Int) = {
      val t_iciS = timeStamp(t_start, "ici! S(" + n + ")")
      val z = EulerPrime.premiers10000.filter(_ < n).toList.combinations(3).filter(Yes2(_)).toList
      println("S(" + n + ")", z.mkString("\n  ", "\n  ", "\n  "), z.map(_.sum), z.flatten.sum)
      val t_laS = timeStamp(t_iciS, "la! S(" + n + ")")
      z.flatten.sum
    }

    def S2(n: Int, i: Int) = {
      val t_iciS = timeStamp(t_start, "ici! S2(" + n + ")")
      val p = EulerPrime.premiers10000.filter(_ < n).toList
      val y = p.sliding(i).map(slide => slide.combinations(3).filter(Yes2(_)).toList).flatten.toList
      val z = ListSet.empty[List[BigInt]] ++ y
      //println("S2 y(" + n + "," + i + ")", y.mkString("\n  ", "\n  ", "\n  "), y.map(_.sum), y.flatten.sum)
      println("S2(" + n + "," + i + ")", p.length, z.mkString("\n  ", "\n  ", "\n  "), z.map(_.sum), z.map(_.sum).sum)
      val t_laS = timeStamp(t_iciS, "la! S2(" + n + ")")
      z.map(_.sum).sum
    }

    Yes(2, 5, 11) should be === true
    Yes(2, 5, 13) should be === false
    Yes(31, 47, 71) should be === true
    Yes(31, 53, 71) should be === false
    Yes(2, 6, 11) should be === false
    Yes(5, 2, 11) should be === false
    println("********************************")
    S(100) should be === 1035
    S2(100, 19) should be === 1035

    var result = 0
    println("Euler518[" + result + "]")
    result should be === 0

  }
}