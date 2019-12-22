import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {

  "Euler601" should "be OK" in {
    println("Euler601")

    def streak(n: BigInt, verbose: Boolean = false): Int = {
      val z = stream_zero_a_linfini.takeWhile(i => (n + i) % (i + 1) == 0).toList
      if (verbose) {
        z.foreach(i => print(", " + (n + i) + ": " + (i + 1)))
        println(" - streak(" + n + "): " + z.length)
      }
      z.length
    }

    def P(s: BigInt, N: BigInt): Int = {
      (2 until N.toInt).filter(streak(_) == s).length
    }

    def P2(sn: Int, e: Int, prev: BigInt): BigInt = {
      sn match {
        case 2 => (prev * sn) + (e % sn)
        case _ => BigInt(0)
      }
    }

    streak(13, true) shouldEqual 4
    P(3, 14) shouldEqual 1
    P(6, 1000000) shouldEqual 14286
    println((2 until 100).map(streak(_)).max)
    streak(61, true)
    println((1 until 10).map(i => ("P(" + i + ",100)", P(i, 100))).mkString(", "))

    println((1 until 15).map(i => ("P(2," + math.pow(2, i).toInt + ")[" + i + "]", P(2, math.pow(2, i).toInt))).mkString(", "))
    var prev = BigInt(0)
    var e = 0
    var sn = 2
    (1 until 15).foreach(e => {
      prev = P2(sn, e, prev)
      val p = P(sn, math.pow(sn, e + 1).toInt)
      println(sn, e, p, prev)
      p shouldEqual prev
    })

    //println((1 until 15).map(i => ("P(3," + math.pow(3, i).toInt + ")[" + i + "]", P(3, math.pow(3, i).toInt))).mkString(", "))

    var result = 0
    println("Euler601[" + result + "]")
    result shouldEqual 0
  }

}