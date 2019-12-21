import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {

  "Euler601" should "be OK" in {
    println("Euler601")

    def streak(n: BigInt): Int = {
      val z = stream_zero_a_linfini.takeWhile(i => (n + i) % (i + 1) == 0).toList
      z.foreach(i => print(", " + (n + i) + ": " + i))
      println(" - streak(" + n + "): " + z.length)
      z.length
    }

    def P(s: BigInt, N: BigInt): Int = {
      (2 until N.toInt).filter(streak(_) == s).length
    }

    streak(13) shouldEqual 4
    P(3, 14) shouldEqual 1
    P(6,1000000) shouldEqual 14286

    var result = 0
    println("Euler601[" + result + "]")
    result shouldEqual 0
  }

}