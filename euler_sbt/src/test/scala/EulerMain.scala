import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {

  "Euler706" should "be OK" in {
    println("Euler706")

    def f(n: BigInt): BigInt = {
      val s = n.toString
      val l = s.length
      //val z = (1 to l).map(i => s.toSeq.combinations(i)).toList.flatten.filter(z => s.contains(z.mkString))
      val z = (1 to l).map(i => s.toSeq.sliding(i)).toList.flatten
      //println(n, z.filter(u => (u.sum % 3 == 0) & (u != 0)).length, z)
      z.filter(u => (u.sum % 3 == 0) & (u != 0)).length
    }

    def F(d: Int): BigInt = {
      val start = BigInt("1" + (2 to d).map(z => "0").mkString)
      val end = start * 10
      val z = (start until end).toList.filter(bi => f(bi) % 3 == 0).length
      println(d, (start until end), z)
      z
    }

    f(2573) shouldEqual 3
    val z = (2 to 6).map(i => (i, F(i)))
    z.apply(0) shouldEqual(2, 30)
    z.apply(4) shouldEqual(6, 290898)

    var result = 0
    println("Euler706[" + result + "]")
    result shouldEqual 0
  }

}