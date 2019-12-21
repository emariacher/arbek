import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {

  "Euler601" should "be OK" in {
    println("Euler601")

    def streak(n: BigInt): Unit = {
      stream_zero_a_linfini.takeWhile(i => (n + i) % (i + 1) == 0).toList.foreach(i => println("" + (n + i) + " " + i))
    }

    streak(13)
    var result = 0
    println("Euler601[" + result + "]")
    result shouldEqual 0
  }

}