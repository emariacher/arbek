import java.util.Calendar

import org.scalatest._
import Euler._

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler191" should "be OK" in {
    println("Euler191")
    var y = (0, 0, 0, 0, 0, 0, 0)


    def decode(j: Int) = {
      (j % 3) match {
        case 0 => "L"
        case 1 => "A"
        case 2 => "O"
      }
    }
    def good(s: String) = s.count(_ == 'L') < 2 && !s.contains("AAA")
    def goodA(s: String) = !s.contains("AAA")
    def goodL(s: String) = s.count(_ == 'L') < 2
    def countL(s: String) = s.count(_ == 'L')

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
      val z = (0 until powl(3, e).toInt).map(i => {
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
      val AL = z.filter(good(_)).length
      val L = z.filter(goodL(_)).length
      val A = z.filter(goodA(_)).length
      //println(e, powl(3, e).toInt, y.length, y)
      println(z.filter(good(_)).length, z.length, ("\nA", A, new EulerDiv(A).primes),
        z.take(5))
      /*(0 to e).foreach(u => {
        println(u, z.count(v => countL(v) == u), new EulerDiv(z.count(v => countL(v) == u)).primes)
      })*/
      val L0 = powl(2, e)
      val L1 = L0 * e / 2
      //println(L0, L1, L0 + L1)
      (L0 + L1) should be === L
      println("avant", y)
      println(AL, L, L - AL, 2 * (L - AL))
      println(L - AL - y._3, (L - AL - y._3) - (2 * y._4), (L - AL - y._3) - (2 * y._4) - y._5, (L - AL - y._3) - (2 * y._4) - y._5 - y._6)
      y = (L, L - AL, 2 * (L - AL), L - AL - y._3, (L - AL - y._3) - (2 * y._4), (L - AL - y._3) - (2 * y._4) - y._5, 0)

      z.filter(good(_)).length
    }

    doZeJob(3) should be === 19
    doZeJob(4) should be === 43
    doZeJob2(4) should be === 43


    (7 to 15).foreach(e => {
      println("\n")
      var t_ici = timeStamp(t_start, "ici!")
      println(e, powl(3, e).toInt)
      /*var z = doZeJob(e)
      println(z, z - (y * 2))
      y = z*/
      var t_la = timeStamp(t_ici, "la! " + e)
      var z2 = doZeJob2(e)
      var t_la2 = timeStamp(t_la, "la2! " + e)
      //z2 should be === z
    })

    val result = 0
    println("Euler191[" + 0 + "]")
    result should be === 0
  }
}