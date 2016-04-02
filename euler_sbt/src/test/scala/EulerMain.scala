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

    def genere(ls: List[String]): List[String] = ls.map(s => List(s + "L", s + "A", s + "O").filter(good(_))).flatten

    var prevL1 = 0
    def doZeJob3(e: Int) = {
      var ls = List("")
      while (ls.head.length < e) {
        ls = genere(ls)
      }
      val L0 = ls.filter(countL(_) == 0).length
      val L1 = ls.filter(countL(_) == 1).length
      println(ls.length, L0, L1, 1.0 * L1 / prevL1, L1-(2*prevL1), new EulerDiv(L1).primes, new EulerDiv(prevL1).primes)
      prevL1 = L1
      ls.length
    }

    var l = List(1, 1, 2)
    var i = 0;
    while (i < 30) {
      l = l :+ l.reverse.take(3).sum
      i += 1
    }
    println(l.zipWithIndex)

    doZeJob(3) should be === 19
    doZeJob(4) should be === 43
    doZeJob3(4) should be === 43
    doZeJob3(8) should be === doZeJob(8)

    var t_la0 = timeStamp(t_start, "la0! ")
    doZeJob(8)
    var t_la1 = timeStamp(t_la0, "la1! ")
    var t_la2 = timeStamp(t_la1, "la2! ")
    doZeJob3(8)
    var t_la3 = timeStamp(t_la2, "la3! **************************************")

    (3 to 19).foreach(e => {
      println("\n")
      var t_ici = timeStamp(t_start, "ici!")
      println(e, powl(3, e))
      /*var z = doZeJob(e)
      println(z, z - (y * 2))
      y = z*/
      var z2 = 0
      var z3 = 0
      var t_la = timeStamp(t_ici, "la! " + e)
      if (e < 12) {
        z2 = doZeJob(e)
        //var t_la2 = timeStamp(t_la, "la2! " + e + " " + z2)
      }
      //var t_laz = timeStamp(t_la, "laz! " + e)
      z3 = doZeJob3(e)
      var t_la3 = timeStamp(t_la, "la3! " + e + " " + z3)
      if (z2 != 0) {
        z3 should be === z2
      }
      println("z3", z3, l.apply(e + 1))
    })

    val result = 0
    println("Euler191[" + 0 + "]")
    result should be === 0
  }
}