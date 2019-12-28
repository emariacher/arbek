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

    streak(13, true) shouldEqual 4
    P(3, 14) shouldEqual 1
    P(6, 1000000) shouldEqual 14286
    println((2 until 100).map(streak(_)).max)
    streak(61, true)
    println((1 until 10).map(i => ("P(" + i + ",100)", P(i, 100))).mkString(", "))

    def Q6(sn: Int, elimit: Int, verbose: Boolean = false) = {
      println("Q6", sn, elimit)
      var prev = BigInt(0)
      if (verbose) {
        println("  " + (2 until elimit).map(e => ("P(" + sn + ")," + math.pow(sn, e).toInt + ")[" + e + "]", P(sn, math.pow(sn, e).toInt))).mkString(", "))
        print("  ")
      }
      (2 until elimit).foreach(e => {
        prev = P6(sn, e, prev)
        val p = P(sn, math.pow(sn, e + 1).toInt)
        if (verbose) {
          print(" - ", sn, e, (e % sn), p, prev)
        }
        prev shouldEqual p
      })
      println("")
    }

    def P6(sn: Int, e: Int, prev: BigInt): BigInt = {
      (prev * sn) + ((e % 2) match {
        case 0 => 3
        case 1 => 0
        case _ => BigInt(999)
      })
    }

    def Q2(sn: Int, elimit: Int, verbose: Boolean = false) = {
      println("Q2", sn, elimit)
      var prev = BigInt(0)
      if (verbose) {
        println("  " + (2 until elimit).map(e => ("P(" + sn + ")," + math.pow(sn, e).toInt + ")[" + e + "]", P(sn, math.pow(sn, e).toInt))).mkString(", "))
        print("  ")
      }
      (1 until elimit).foreach(e => {
        prev = P2(sn, e, prev)
        val p = P(sn, math.pow(sn, e + 1).toInt)
        if (verbose) {
          print(" - ", sn, e, (e % sn), p, prev)
        }
        prev shouldEqual p
      })
      println("")
    }

    def P2(sn: Int, e: Int, prev: BigInt): BigInt = {
      (prev * sn) + ((sn % 2) match {
        case 0 => (e % 2)
        case 1 => ((e % 2) * 2) - 1
        case _ => BigInt(999)
      })
    }

    Q2(2, 15,true)
    Q2(3, 13,true)
    Q2(4, 8,true)
    //Q2(5, 7, true)
    Q6(6, 9, true)

    var result = 0
    println("Euler601[" + result + "]")
    result shouldEqual 0
  }

}