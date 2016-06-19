import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler518" should "be OK" in {
    println("Euler518")

    def Yes(a: BigInt, b: BigInt, c: BigInt) = {
      var r = EulerPrime.isPrime(a) && EulerPrime.isPrime(b) && EulerPrime.isPrime(c) && (b + 1).toDouble / (a + 1).toDouble == (c + 1).toDouble / (b + 1).toDouble
      //if(r) println("           ", (a, b, c), (a + 1, b + 1, c + 1), (b + 1) / (a + 1), r)
      r
    }

    def YesV(a: BigInt, b: BigInt, c: BigInt) = {
      var r = EulerPrime.isPrime(a) && EulerPrime.isPrime(b) && EulerPrime.isPrime(c) && (b + 1).toDouble / (a + 1).toDouble == (c + 1).toDouble / (b + 1).toDouble
      println("           ", (a, b, c), (a + 1, b + 1, c + 1), (b + 1).toDouble / (a + 1).toDouble, r)
      r
    }

    def Yes2(l: List[BigInt]) = Yes(l.sorted.head, l.sorted.apply(1), l.sorted.last)

    def S(n: Int) = {
      val t_iciS = timeStamp(t_start, "")
      val prems = EulerPrime.premiers10000.filter(_ < n).toList
      val z = prems.combinations(3).filter(Yes2(_)).toList
      println("S(" + n + ")", prems.length, z.length, z.mkString("\n  ", " - ", "\n  "), z.map(_.sum).sorted, z.flatten.sum)
      val t_laS = timeStamp(t_iciS, "la! S(" + n + ")")
      z.flatten.sum
    }

    def S2(n: Int, i: Int) = {
      val t_iciS = timeStamp(t_start, "")
      val p = EulerPrime.premiers10000.filter(_ < n).toList
      val y = p.sliding(i).map(slide => slide.combinations(3).filter(Yes2(_)).toList).flatten.toList
      val z = (ListSet.empty[List[BigInt]] ++ y).toList.sortBy(_.apply(1)).sortBy(_.head)
      //println("S2 y(" + n + "," + i + ")", y.mkString("\n  ", "\n  ", "\n  "), y.map(_.sum), y.flatten.sum)
      println("S2(" + n + "," + i + ")", p.length, z.length, z.mkString("\n  ", " - ", "\n  "), z.map(_.sum), z.map(_.sum).sum)
      val t_laS = timeStamp(t_iciS, "la! S2(" + n + ")")
      z.map(_.sum).sum
    }

    def T(n: Int) = {
      val p = EulerPrime.premiers10000.filter(_ < n).toList
      val y = p.combinations(3).filter(Yes2(_)).toList.flatten
      val z = (ListSet.empty[BigInt] ++ y).toList.sorted
      println("T(" + n + ")", p.length, z.length, "\n" + z, "\n" + p)
      z
    }

    def S3(n: Int) = {
      val t_iciS = timeStamp(t_start, "")
      val prems = EulerPrime.premiers10000.filter(_ < n).toList
      var pg = ListSet[BigInt](2)
      var z: BigInt = 0
      var l = ListSet.empty[List[BigInt]]
      prems.foreach(p => {
        if (pg.contains(p)) {
          val x = prems.toList.combinations(2).toList.filter(c => Yes2(List(p, c.head, c.last))).map(c => {
            List(p, c.head, c.last).sorted
          })
          l = l ++ x
          val y = (ListSet.empty[BigInt] ++ x.flatten).toList.sorted
          val u = x.map(c => c.sum)
          pg = pg ++ y
          //println("*1*", p, x, y, u, pg.toList.sorted, "\n  ", l)
        } else {
          val x = pg.toList.combinations(2).toList.filter(c => Yes2(List(p, c.head, c.last))).map(c => {
            List(p, c.head, c.last).sorted
          })
          l = l ++ x
          val y = (ListSet.empty[BigInt] ++ x.flatten).toList.sorted
          val u = x.map(c => c.sum)
          pg = pg ++ y
          //println("*2*", p, x, y, u, pg.toList.sorted, "\n  ", l)
          if (pg.contains(p)) {
            val x = prems.toList.combinations(2).toList.filter(c => Yes2(List(p, c.head, c.last))).map(c => {
              List(p, c.head, c.last).sorted
            })
            l = l ++ x
            val y = (ListSet.empty[BigInt] ++ x.flatten).toList.sorted
            val u = x.map(c => c.sum)
            pg = pg ++ y
            //println("*3*", p, x, y, u, pg.toList.sorted, "\n  ", l)
          }
        }
      })
      z = l.toList.flatten.sum
      val t_laS = timeStamp(t_iciS, "la! S3(" + n + ")")
      println(pg.toList.sorted)
      println("S3(" + n + ")", prems.length, l.toList.length, l.toList.sortBy(_.apply(1)).sortBy(_.head).mkString("\n  ", " - ", "\n  "), l.toList.map(_.sum).sorted, l.toList.flatten.sum)
      z
    }

    def S4(n:Int) = {
      val t_iciS = timeStamp(t_start, "")
      val prems = EulerPrime.premiers10000.filter(_ < n)
      val z = prems.map(p => (p,p+1,prems.toList.map(p2 => (p2,(p2+1).toDouble/(p+1).toDouble)))).toList.sortBy(_._1)
      println(z.mkString("\n  ", "\n  ", "\n  "))
      //println("S4(" + n + ")", prems.length, z.length, z.mkString("\n  ", " - ", "\n  "), z.map(_.sum).sorted, z.flatten.sum)
      val t_laS = timeStamp(t_iciS, "la! S4(" + n + ")")

    }

    YesV(37, 151, 607) should be === true
    YesV(71, 83, 97) should be === true
    Yes(2, 5, 11) should be === true
    Yes(2, 5, 13) should be === false
    Yes(31, 47, 71) should be === true
    Yes(31, 53, 71) should be === false
    Yes(2, 6, 11) should be === false
    Yes(5, 2, 11) should be === false
    Yes2(List(5, 2, 11)) should be === true
    println("********************************")
    S(100) should be === 1035
    /*S2(100, 19) should be === 1035
    S(200) should be === S2(200, 43)
    T(100)
    T(200)
    S(60) should be === S3(60)
    S(50) should be === S3(50)
    S(200) should be === S3(200)
    S(1000) should be === S3(1000)*/
    S3(200)
    S4(100)

    var result = 0
    println("Euler518[" + result + "]")
    result should be === 0

  }
}