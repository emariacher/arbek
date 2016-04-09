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
      val L0 = ls.filter(countL(_) == 0)
      val L1 = ls.filter(countL(_) == 1)
      val L1C = (0 to e).map(i => {
        (L1.count(_.indexOf('L') == i), i)
      })
      println(ls.length, L0.length, L1.length, L1C)
      (ls.length, L0.length, L1.length, L1C)
    }

    var l = List(1, 1, 2)
    var i = 0;
    while (i < 30) {
      l = l :+ l.reverse.take(3).sum
      i += 1
    }
    println("l", l.zipWithIndex)

    var l3 = List(0, 2, 1)
    i = 3;
    while (i < 30) {
      l3 = l3 :+ l3.reverse.take(3).sum
      i += 1
    }
    println("l3 ", l3.zipWithIndex)

    var l4 = List(0, 1, 4)
    i = 3;
    while (i < 30) {
      l4 = l4 :+ l4.reverse.take(3).sum
      i += 1
    }
    println("l4 ", l4.zipWithIndex)

    var l5 = List(0, 4, 3)
    i = 3;
    while (i < 30) {
      l5 = l5 :+ l5.reverse.take(3).sum
      i += 1
    }
    println("l5 ", l5.zipWithIndex)

    doZeJob(4) should be === 43

    var z3 = 0
    var sum = 0
    (3 to 18).foreach(e => {
      println("\n")
      //var t_ici = timeStamp(t_start, "ici!")
      println(e, powl(3, e))
      var z2 = 0
      //var t_la = timeStamp(t_ici, "la! " + e)
      if (e < 12) {
        z2 = doZeJob(e)
        //var t_la2 = timeStamp(t_la, "la2! " + e + " " + z2)
      }
      //var t_laz = timeStamp(t_la, "laz! " + e)
      sum += z3
      val zz = doZeJob3(e)
      z3 = zz._1
      //var t_la3 = timeStamp(t_ici, "la3! " + e + " " + z3)
      if (z2 != 0) {
        z3 should be === z2
      }

      println(zz._4.apply(0), zz._4.apply(1), zz._4.apply(2))
      e match {
        case it if 0 to 4 contains it =>
        case 5 => (zz._4.apply(0)._1, zz._4.apply(1)._1, zz._4.apply(2)._1) should be ===(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + 1)
        case 6 => (zz._4.apply(0)._1, zz._4.apply(1)._1, zz._4.apply(2)._1) should be ===(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2))
        case 7 => (zz._4.apply(0)._1, zz._4.apply(1)._1, zz._4.apply(2)._1) should be ===(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2))
        case 8 => (zz._4.apply(0)._1, zz._4.apply(1)._1, zz._4.apply(2)._1) should be ===(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + 1)
        case _ => (zz._4.apply(0)._1, zz._4.apply(1)._1, zz._4.apply(2)._1) should be ===(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + l.apply(e - 8))
      }
      if (e > 6) {
        zz._4.apply(3)._1 should be === zz._4.apply(1)._1 + l3.apply(e - 5)

        if (e > 8) {
          println(l4.apply(e - 8))
          if (e > 10) {
            println(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + l.apply(e - 8),
              l.apply(e) + l.apply(e - 4) + l3.apply(e - 5),
              l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8),
              l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8) + l5.apply(e - 10)
            )
            zz._4.apply(5)._1 should be === zz._4.apply(4)._1 + l5.apply(e - 10)
          } else {
            println(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + l.apply(e - 8),
              l.apply(e) + l.apply(e - 4) + l3.apply(e - 5),
              l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8)
            )
          }
          zz._4.apply(4)._1 should be === zz._4.apply(3)._1 + l4.apply(e - 8)
        }
      }

    })


    val result = 0
    println("Euler191[" + 0 + "]")
    result should be === 0
  }
}