import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler621" should "be OK" in {
    println("Euler621")
    val tr = Euler.triangular(50)

    def G1(a: BigInt, verbose: Boolean = false): (BigInt, BigInt, (BigInt, BigInt, BigInt)) = {
      val t = tr.takeWhile(_ <= a)
      val l1 = t.map(z => (z, 3 * z)).filter(_._2 == a).toList
      val l2 = t.combinations(2).map(y => List(y :+ y.head, y :+ y.last)).flatten.map(z => (z, z.sum)).filter(_._2 == a).toList
      val l3 = t.combinations(3).map(z => (z, z.sum)).filter(_._2 == a).toList
      val result = BigInt(l1.size + (l2.size * 3) + (l3.size * 6))
      if (verbose) {
        println(a, result, " ", (l1.size, l2.size, l3.size), l1, l2, l3)
      }
      (a, result, (l1.size, l2.size, l3.size))
    }

    println(tr)

    G1(7)
    G1(9)._2 shouldEqual 7
    G1(1000)._2 shouldEqual 78
    //G1(1000000)._4 shouldEqual 2106

    val j = 1000
    val a = (1 to j).map(G1(_)).groupBy(_._3).toList.
      sortBy(b => (b._1._1 * j * j) + (b._1._2 * j) + b._1._3).map(b => (b._1, b._2.map(_._1)))
    println(a.mkString("\n"))
    println(tr)
    (1 to j).map(G1(_, true))

    var result = 0
    println("Euler621[" + result + "]")
    result shouldEqual 0

  }
}