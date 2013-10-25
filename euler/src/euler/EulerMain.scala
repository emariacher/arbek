package euler

import org.scalatest.FunSuite
import org.scalatest._
import org.scalatest.BeforeAndAfter
import scala.collection.immutable.ListSet
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import java.awt.Color
import scala.collection.immutable.TreeSet
import scala.math.Ordering.StringOrdering
import Math._
import scala.collection.mutable.ListMap
import EulerPrime._
import kebra.MyLog._
import language.postfixOps

// scala org.scalatest.tools.Runner -p . -o -s euler.Euler

// paramebiss.getValue("45zob").toInt should equal (1)
// projects.map(_.getDatabase.getName).contains("COCO") should be (false)

object Euler {
    val t_start = Calendar.getInstance()
    def printZisday(zisday: Calendar, fmt: String): String = printZisday(zisday.getTime(), fmt)
    def printZisday(date: Date, fmt: String): String = new String(new SimpleDateFormat(fmt).format(date))
    def timeStamp(c_t_start: Calendar, s_title: String): Calendar = {
        val t_end = Calendar.getInstance()
        println("t_now: " + printZisday(t_end, "ddMMMyy_HH_mm_ss_SSS [") + s_title +
            "] t_diff: " + (t_end.getTimeInMillis() - c_t_start.getTimeInMillis()))
        t_end
    }
    def timeStampS(c_t_start: Calendar, s_title: String): (Calendar, String) = {
        val t_end = Calendar.getInstance()
        (t_end, "t_now: " + printZisday(t_end, "ddMMMyy_HH_mm_ss_SSS [") + s_title +
            "] t_diff: " + (t_end.getTimeInMillis() - c_t_start.getTimeInMillis()))
    }
    def waiting(n: Int) {
        val t0 = System.currentTimeMillis()
        var t1 = t0
        do {
            t1 = System.currentTimeMillis()
        } while (t1 - t0 < n)
    }

    def powl(pn: BigInt, n: Int): BigInt = {
        val div = 40
        val z2 = new Range(1, (n / div) + 1, 1).toList.map((i: Int) => pn).product
        val z4 = new Range(1, (n % div) + 1, 1).toList.map((i: Int) => pn).product
        val z5 = new Range(1, div + 1, 1).toList.map((i: Int) => z2).product
        if (n % div == 0) {
            z5
        } else {
            z5 * z4
        }
    }
}

class Euler extends FunSuite with Matchers with BeforeAndAfter {

    before {
        Euler
        println("\n****avant****")
    }

    after {
        Euler.timeStamp(Euler.t_start, "Euler")
        println("****apres****")
    }

    test("euler32") {
        val ldigits = (1 until 10)
        // x*y=z -> z has 4 digits min and max
        //1*6789 = 6789 > 5432
        val lz4combs1 = ldigits.combinations(4).filter(_.max > 6).toList
        printIt(lz4combs1)
        //46*589>7321
        val lz4combs2 = lz4combs1.map((z: IndexedSeq[Int]) => {
            val xy = ldigits filterNot ((d: Int) => z contains d)
            val biggestZ = z.reverse.mkString("").toInt
            val x1 = (xy.head * 10) + xy.apply(2)
            val y1 = (xy.apply(1) * 100) + (xy.apply(3) * 10) + xy.last
            val smallestXY1 = x1 * y1
            val x2 = xy.head
            val y2 = xy.tail.reverse.zipWithIndex.map((di: (Int, Int)) => di._1 * math.pow(10, di._2)).sum.toInt
            val smallestXY2 = x2 * y2
            //printIt(z, biggestZ, xy, x1, y1, smallestXY1, x2, y2, smallestXY2)
            (z, xy, biggestZ, math.min(smallestXY1, smallestXY2))
        }).filter((q: (IndexedSeq[Int], IndexedSeq[Int], Int, Int)) => q._3 > q._4)
        printIt(lz4combs2)

        val llz4perms1 = lz4combs2.map((q: (IndexedSeq[Int], IndexedSeq[Int], Int, Int)) => {
            (q._1.toList, q._2.toList, q._4, q._1.toList.permutations.toList.map((l: List[Int]) => l.mkString("").toInt))
        })
        println(llz4perms1.mkString("\n"))
        val llz4perms2 = llz4perms1.map((q: (List[Int], List[Int], Int, List[Int])) => {
            (q._1, q._2, q._3, q._4.filter(_ > q._3))
        })
        println(llz4perms2.mkString("\n"))
        val llz4perms3 = llz4perms2.map((q: (List[Int], List[Int], Int, List[Int])) => {
            q._4.map((i: Int) => (q._2, i))
        }).flatten
        println(llz4perms3.mkString("\n3 ") + "\n" + llz4perms3.length)
        val llz4perms4 = llz4perms3.map((p: (List[Int], Int)) => {
            (p._1, p._2, new EulerDivisors(new EulerDiv(p._2).primes).divisors)
        })
        println(llz4perms4.mkString("\n4 "))
        val llz4perms5 = llz4perms4.map((t: (List[Int], Int, List[BigInt])) => {
            (t._1, t._2, t._3.filter((bi: BigInt) => {
                val bis = bi.toString.toArray.map(_.toString.toInt).toList.sorted
                bis.intersect(t._1) == bis
            }))
        })
        println(llz4perms5.mkString("\n5 "))
        //                         . at least 2 numbers  . there should be at least 5 digits. 
        val llz4perms6 = llz4perms5.filter(_._3.length > 1).filter(_._3.mkString("").length > 4)
            //.one of them being at least 3 digits long                        
            .filter(!_._3.map(_.toString.length).filter(_ >= 3).isEmpty)
        println(llz4perms6.mkString("\n6 ") + "\n" + llz4perms6.length)
        // all xy digits must exist at least once
        val llz4perms7 = llz4perms6.filter((t: (List[Int], Int, List[BigInt])) => {
            val sdivs = t._3.mkString("")
            t._1.map((i: Int) => sdivs.indexOf(i.toString)).min >= 0
        })
        println(llz4perms7.mkString("\n7 ") + "\n" + llz4perms7.length)
        // when a xy digit exist only once, test the division
        val llz4perms8 = llz4perms7.filter((t: (List[Int], Int, List[BigInt])) => {
            val ldivs = t._3.mkString("").toArray.map(_.toString.toInt).toList
            val lonlyOne = t._1.map((i: Int) => ldivs.count(_ == i))
            if (lonlyOne.filter(_ == 1).isEmpty) {
                true
            } else {
                val ldivs2 = t._3.map(_.toString)
                val d = t._1.apply(lonlyOne.indexOf(1)).toString
                val x = ldivs2.find((s: String) => s.indexOf(d) >= 0).head.toInt //TODO
                val y = t._2 / x
                val OK = t._3.contains(y)
                println(t, ldivs, d, ldivs2, x, y, OK)
                OK
            }
        })
        println(llz4perms8.mkString("\n8 ") + "\n" + llz4perms8.length)
            /* test the division with xy with 3 or more digits
    val llz4perms9 = llz4perms8.map((t: (List[Int], Int, List[BigInt])) => {
      //val z = t._3.filter(_>100).map(_.toInt).map((i: Int) => (i,t._2/i,t._3.contains(t._2/i),toStr(i).intersect(toStr(t._2/i)).isEmpty))
      val z = t._3.filter(_> BigInt(100)).map(_.toInt).filter((i: Int) => t._3.contains(t._2/i) & toStr(i).intersect(toStr(t._2/i)).isEmpty ).map((i: Int) => (i,t._2/i,t._2))
      //println(t,z)
    (t,z)
    }).filter(!_._2.isEmpty)
    println(llz4perms9.mkString("\n9 ")+"\n"+llz4perms9.length)
    // filter the results that don't fit
    val llz4perms10 = llz4perms9.filter((p:  ((List[Int], Int, List[BigInt]), List[(Int, Int, Int)])) => {
      val q = p._2.map((t: (Int, Int, Int)) => (toStr(t._1)++toStr(t._2)++toStr(t._3)).map(_.toInt).sorted)
      !q.filter(_==ldigits.toList).isEmpty
    })
    println(llz4perms10.mkString("\n10 ")+"\n"+llz4perms10.length)
    
    val result = llz4perms10.map(_._1._2).sum
    printIt(llz4perms10.map(_._1._2).sum)*/

            def printl(l: List[Any]) = printIt(l.length + " " + l)
            def toStr(i: Int) = i.toString.toArray.map(_.toString).toList

    }

    test("euler243") {
        /* 15499/94744*/
        var minimum = 2.0
        var minindex = 0
        someStuff
        (1 until 106).map(euler243flat(_))
        minimum = 2.0
        someStuff
        (105 until 4000 by 105).map(euler243flat(_))
        someStuff

            def euler243flat(n: Int): (Int, Int) = {
                val r = 1 until n
                //println(r)
                val divn = new EulerDiv(n)
                val divi = new EulerDivisors(divn)
                //println(divn.primes)
                //val z1 = r.filter((i: Int) => (divn.primes intersect new EulerDiv(i).primes).isEmpty).toList
                val z = (r.map(BigInt(_)).toList filterNot (divi.divisors contains)).filter((i: BigInt) => (divn.primes intersect new EulerDiv(i).primes).isEmpty)
                //require(z==z1)
                val ratio = z.size * 1.0 / (n - 1)
                if (ratio < minimum) {
                    minimum = ratio
                    minindex = n
                }
                //println(n+": "+z.size+"/"+(n-1)+" "+z)
                println(n + ": " + z.size + "/" + (n - 1) + " " + premiers1000.filter(_ < n).size + " (" + minindex + ", " + minimum + ")")
                (z.size, n)
            }

            def someStuff {
                println(new EulerDiv(94745).primes)
                println(new EulerDiv(94744).primes)
                println(new EulerDiv(15499).primes)
                println("15499/94744: " + 15499.0 / 94744.0)
            }
    }

}

