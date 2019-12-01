import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler621" should "be OK" in {
    println("Euler621")
    val tr = Euler.triangular(50)

    def G1(a: BigInt): BigInt = {
      val t = tr.takeWhile(_ <= a)
      val l1 = t.map(z => (z, 3 * z)).filter(_._2 == a).toList
      val l2 = t.combinations(2).map(y => List(y :+ y.head, y :+ y.last)).flatten.map(z => (z, z.sum)).filter(_._2 == a).toList
      val l3 = t.combinations(3).map(z => (z, z.sum)).filter(_._2 == a).toList
      println(l1)
      println(l2)
      println(l3)
      println(l1.size, l2.size, l3.size)
      BigInt(l1.size + (l2.size * 3) + (l3.size * 6))
    }

    println(tr)

    G1(7)
    G1(9) shouldEqual 7
    G1(1000) shouldEqual 78
    G1(1000000) shouldEqual 2106

    var result = 0
    println("Euler621[" + result + "]")
    result shouldEqual 0

  }
}