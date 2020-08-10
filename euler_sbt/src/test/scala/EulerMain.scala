import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {


  "Euler493" should "be OK" in {
    println("Euler493")

    def dozejob(s: String, c: Int) = {
      val z = s.toSeq.combinations(c).toList
      val y = z.map(e => e.toSeq.distinct.size).groupBy(u => u)
      val x = y.toList.map(a => (a._1, a._2.size)).sortBy(_._1)
      println(c, s, z.map(e => e.toSeq.distinct.size).sum.toDouble / z.size.toDouble, z.size, z.take(5))
      println("  " + x)
      x.map(_._2).sum shouldEqual z.size
    }

    dozejob("11223344", 4)
    dozejob("111222333444555", 3)
    dozejob("111222333444555", 4)
    dozejob("111222333444555", 5)

    val result = 0
    println("Euler493[" + result + "]")
    result shouldEqual 0
  }

}