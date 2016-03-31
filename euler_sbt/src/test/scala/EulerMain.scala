import java.util.Calendar

import org.scalatest._
import Euler._

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler191" should "be OK" in {
    println("Euler191")
    var y = (0, 0, 0, 0, 0, 0, 0, 0)


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
      println(L - AL - y._3, (L - AL - y._3) - (2 * y._4), (L - AL - y._3) - (2 * y._4) - y._5,
        (L - AL - y._3) - (2 * y._4) - y._5 - y._6,
        (L - AL - y._3) - (2 * y._4) - y._5 - y._6 - y._7, (L - AL - y._3) - (2 * y._4) - y._5 - y._6 - y._7 - y._8)
      y = (L, L - AL, 2 * (L - AL), L - AL - y._3, (L - AL - y._3) - (2 * y._4), (L - AL - y._3) - (2 * y._4) - y._5,
        (L - AL - y._3) - (2 * y._4) - y._5 - y._6,
        (L - AL - y._3) - (2 * y._4) - y._5 - y._6 - y._7)
      println("apres", y)


      z.filter(good(_)).length
    }

    def genere(ls: List[String]): List[String] = ls.map(s => List(s + "L", s + "A", s + "O").filter(good(_))).flatten

    def doZeJob3(e: Int) = {
      var ls = List("")
      while (ls.head.length < e) {
        ls = genere(ls)
      }
      ls.length
    }

    doZeJob(3) should be === 19
    doZeJob(4) should be === 43
    doZeJob2(4) should be === 43
    doZeJob3(4) should be === 43
    doZeJob3(8) should be === doZeJob(8)

    var t_la0 = timeStamp(t_start, "la0! ")
    doZeJob(8)
    var t_la1 = timeStamp(t_la0, "la1! ")
    doZeJob2(8)
    var t_la2 = timeStamp(t_la1, "la2! ")
    doZeJob3(8)
    var t_la3 = timeStamp(t_la2, "la3! ")

    //var x = (4418,156,63,25,10,5)
    var x0: BigInt = 33620
    var x1: BigInt = 4418
    var x2: BigInt = 156
    var x3 = 63
    var x4 = 25
    var x5 = 10
    var x6 = 5
    var x: BigInt = 27820
    (3 to 30).foreach(e => {
      println("\n")
      var t_ici = timeStamp(t_start, "ici!")
      println(e, powl(3, e))
      /*var z = doZeJob(e)
      println(z, z - (y * 2))
      y = z*/
      var z2 = 0
      var t_la = timeStamp(t_ici, "la! " + e)
      if (e < 16) {
        z2 = doZeJob2(e)
        var t_la2 = timeStamp(t_la, "la2! " + e + " " + z2)
      }
      if (e > 10) {
        var t_laz = timeStamp(t_la, "laz! " + e)
        var z3 = doZeJob3(e)
        var t_la3 = timeStamp(t_laz, "la3! " + e + " " + z3)
        if(z2!=0) {
          z3 should be === z2
        }
      }
      //z2 should be === z
      val L0 = powl(2, e)
      val L1 = L0 * e / 2
      if (e > 13) {
        x6 += 1
        x5 += x6
        x4 += x5
        x3 += x4
        x2 += x3
        x1 += x1 + x2
        x0 += x0 + x1
        x = L0 + L1 - x0
      }
      println("x", x, x0, x1, x2, x3, x4, x5, x6)
    })

    val result = 0
    println("Euler191[" + 0 + "]")
    result should be === 0
  }
}