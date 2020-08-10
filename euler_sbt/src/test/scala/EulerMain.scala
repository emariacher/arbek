import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {


  "Euler493" should "be OK" in {
    println("Euler493")

    def dozejob1(s: String, c: Int) = {
      val z = s.toSeq.combinations(c).toList
      val y = z.map(e => e.toSeq.distinct.size).groupBy(u => u)
      val x = y.toList.map(a => (a._1, a._2.size)).sortBy(_._1)
      val w = x.map(a => a._1 * a._2).sum
      w shouldEqual z.map(e => e.toSeq.distinct.size).sum
      println(c, s, w.toDouble / z.size.toDouble, z.size, z.take(5))
      println("  " + x)
      x.map(_._2).sum shouldEqual z.size
    }

    var s = "1111122222333334444455555"
    (1 to 12).foreach(i => {
      dozejob1(s, i)
    })

    val result = 0
    println("Euler493[" + result + "]")
    result shouldEqual 0
  }

}