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
    def doZeJob3(e: Int): (BigInt, BigInt, BigInt, List[(BigInt, Int)]) = {
      var ls = List("")
      while (ls.head.length < e) {
        ls = genere(ls)
      }
      val L0 = ls.filter(countL(_) == 0)
      val L1 = ls.filter(countL(_) == 1)
      val L1C = (0 to e).map(i => {
        (BigInt(L1.count(_.indexOf('L') == i)), i)
      })
      println("doZeJob3[" + e + "]", ls.length, L0.length, L1.length, L1C)
      (BigInt(ls.length), BigInt(L0.length), BigInt(L1.length), L1C.toList)
    }


    var l = List(BigInt(1), BigInt(1), BigInt(2))
    var i = 0;
    while (i < 30) {
      l = l :+ l.takeRight(3).sum
      i += 1
    }
    println("l", l.zipWithIndex)

    var l3 = List(0, 2, 1)
    i = 3;
    while (i < 30) {
      l3 = l3 :+ l3.takeRight(3).sum
      i += 1
    }
    println("l3 ", l3.zipWithIndex)

    var l4 = List(0, 1, 4)
    i = 3;
    while (i < 30) {
      l4 = l4 :+ l4.takeRight(3).sum
      i += 1
    }
    println("l4 ", l4.zipWithIndex)

    var l5 = List(0, 4, 3)
    i = 3;
    while (i < 30) {
      l5 = l5 :+ l5.takeRight(3).sum
      i += 1
    }
    println("l5 ", l5.zipWithIndex)

    var l6 = List(0, 8, 12)
    i = 3;
    while (i < 30) {
      l6 = l6 :+ l6.takeRight(3).sum
      i += 1
    }
    println("l6 ", l6.zipWithIndex)

    var l7 = List(0, 5, 13)
    i = 3;
    while (i < 30) {
      l7 = l7 :+ l7.takeRight(3).sum
      i += 1
    }
    println("l7 ", l7.zipWithIndex)

    def doZeJob4(e: Int, ul: List[(Int, List[(BigInt, Int)])]): (BigInt, BigInt, BigInt, List[(Int, List[(BigInt, Int)])]) = {
      var ls = List("")
      while (ls.head.length < e) {
        ls = genere(ls)
      }
      val L0 = l.apply(e + 1)
      var L1 = (0 to (e - 4)).map(i => ul.map(_._2.apply(i)._1).sum).zipWithIndex.toList
      L1 = L1 ++ L1.take(3).reverse
      val vl = ul.drop(1) :+(e, L1)
      println("L1",L1)
      println("doZeJob4[" + e + "]", L0 + L1.map(_._1).sum, L0, L1.map(_._1).sum, vl.mkString("\n  ", "\n  ", "\n  "))
      (L0 + L1.map(_._1).sum, L0, L1.map(_._1).sum, vl)
    }



    doZeJob(4) should be === 43

    var z3 = BigInt(0)
    var sum = BigInt(0)
    var zl = List.empty[(BigInt, BigInt, BigInt, List[(BigInt, Int)])]
    val limit = 18
    (3 to limit).foreach(e => {
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
      zl = zl :+ zz
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
          if (e < 10) {
            println(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + l.apply(e - 8),
              l.apply(e) + l.apply(e - 4) + l3.apply(e - 5),
              l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8)
            )
          } else {
            if (e < 12) {
              println(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + l.apply(e - 8),
                l.apply(e) + l.apply(e - 4) + l3.apply(e - 5),
                l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8),
                l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8) + l5.apply(e - 10)
              )
            } else {
              if (e < 14) {
                println(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + l.apply(e - 8),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8) + l5.apply(e - 10),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8) + l5.apply(e - 10) - l6.apply(e - 12)
                )
              } else {
                println(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + l.apply(e - 8),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8) + l5.apply(e - 10),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8) + l5.apply(e - 10) - l6.apply(e - 12),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8) + l5.apply(e - 10) - l6.apply(e - 12) + l7.apply(e - 14)
                )
                zz._4.apply(7)._1 should be === zz._4.apply(6)._1 + l7.apply(e - 14)
              }
              zz._4.apply(6)._1 should be === zz._4.apply(5)._1 - l6.apply(e - 12)
            }
            zz._4.apply(5)._1 should be === zz._4.apply(4)._1 + l5.apply(e - 10)
          }
          zz._4.apply(4)._1 should be === zz._4.apply(3)._1 + l4.apply(e - 8)
        }
      }

    })

    println(zl.mkString("\n"))
    val zl3 = zl.takeRight(4)
    zl3.take(3).map(_._4.head._1).sum should be === zl3.last._4.head._1
    zl3.take(3).map(_._4.apply(3)._1).sum should be === zl3.last._4.apply(3)._1

    zl.sliding(4).foreach(zk => {
      zk.take(3).map(_._4.apply(2)._1).sum should be === zk.last._4.apply(2)._1
    })

    val zlm1t3 = zl.takeRight(4).dropRight(1)
    println(zlm1t3.mkString("\n ", "\n ", "\n "))
    var u = (0 to (limit - 4)).map(i => {
      zlm1t3.take(3).map(_._4.apply(i)._1).sum
    }).zipWithIndex.toList
    u = u ++ u.take(4).reverse
    println(u)
    println(zl.last._4)

    //var ul = zl.take(5).drop(2).zipWithIndex.map(c => (c._2 + 5, c._1._4))
    var ul = zl.take(12).drop(9).zipWithIndex.map(c => (c._2 + 12, c._1._4))
    println(ul.mkString("\n ul ", "\n ul ", "\n ul "))
    doZeJob4(15, ul)
    doZeJob3(15)


    val result = 0
    println("Euler191[" + 0 + "]")
    result should be === 0
  }
}