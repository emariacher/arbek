import java.util.Calendar

import org.scalatest._
import Euler._

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler191" should "be OK" in {
    println("Euler191")

    def decode(j: Int) = {
      (j % 3) match {
        case 0 => "L"
        case 1 => "A"
        case 2 => "O"
      }
    }
    def good(s: String) = s.count(_ == 'L') < 2 && !s.contains("AAA")

    /*val z = (0 until 81).map(i => {
      var s = ""
      var j = i
      s += decode(j)
      j /= 3
      s += decode(j)
      j /= 3
      s += decode(j)
      j /= 3
      s += decode(j)
      println(i, s)
      s
    }).filter(good(_))
    println(z, z.length)
    z.length should be === 43*/

    def doZeJob(e: Int) = {
      val y = (0 until powl(3, e).toInt).map(i => {
        var s = ""
        var j = i
        s += decode(j)
        (1 until e).foreach(u => {
          j /= 3
          s += decode(j)
        })
        //println(i, s)
        s
      })
      val z = y.filter(good(_))
      println(z.length, z.take(5))

      z.length
    }

    def doZeJob2(e: Int) = {
      val y = (powl(3, e - 2).toInt until powl(3, e).toInt).map(i => {
        var s = ""
        var j = i
        s += decode(j)
        (1 until e).foreach(u => {
          j /= 3
          s += decode(j)
        })
        //println(i, s)
        s
      }).zipWithIndex
      val z = y.filter(u => good(u._1))
      //println(e, powl(3, e).toInt, y.length, y)
      println(z.length, z.take(5))

      z.length
    }

    doZeJob(3) should be === 19
    doZeJob(4) should be === 43
    doZeJob2(4) should be === 43

    var y = 0
    (3 to 15).foreach(e => {
      println("\n")
      var t_ici = timeStamp(t_start, "ici!")
      println(e, powl(3, e).toInt)
      var z = doZeJob(e)
      println(z, z - (y * 2))
      y = z
      var t_la = timeStamp(t_ici, "la! " + e)
      var z2 = doZeJob2(e)
      var t_la2 = timeStamp(t_la, "la2! " + e)
      z2 should be === z
    })

    val result = 0
    println("Euler191[" + 0 + "]")
    result should be === 0
  }
}