import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler621" should "be OK" in {
    println("Euler621")
    val t10 = Euler.triangular(4)

    def G1(a: BigInt): BigInt = {
      val t = t10.takeWhile(_ <= a)
      val l1 = t.map(z => (z, 3 * z)).filter(_._2 == a)
      val l2 = t.combinations(2).map(y => List(y :+ y.head, y :+ y.last)).flatten.map(z => (z, z.sum)).filter(_._2 == a)
      val l3 = t.combinations(3).map(z => (z, z.sum)).filter(_._2 == a)
      println(l1.toList)
      println(l2.toList)
      println(l3.toList)
      BigInt(0)
    }

    println(t10)

    G1(7)
    G1(9) shouldEqual 7
    G1(1000) shouldEqual 78
    //G1(1000000) shouldEqual 2106

    var result = 0
    println("Euler621[" + result + "]")
    result shouldEqual 0

  }
}