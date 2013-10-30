package euler

import kebra.MyLog._
import kebra.MyFileChooser
import scala.collection.immutable.TreeSet
import java.util.Calendar
import scala.language.postfixOps
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import scala.io.Source
import java.io.BufferedInputStream
import scala.collection.JavaConversions._
import scala.collection.immutable.ListSet
import Permutations._

object EulerMainNoScalaTest extends App {
    myPrintDln("Hello World!")
    new Euler26
}

class Euler26 {
    myPrintln(EulerPrime.premiers1000.toList.map(bi => new Calculate(bi.toInt)).mkString("\n"))
    val result26 = EulerPrime.premiers1000.toList.map(bi => new Calculate(bi.toInt)).maxBy(_.countPrecision)
    myPrintIt(result26)
    myAssert2(983, result26.countPrecision)

    class Calculate(val down: Int) {
        var countPrecision = 0
        val check = 1.0 / down.toDouble
        var lrest = List[Int]()
        val resultP = divSpecial(down)
        /*
        myPrintIt(check, Math.min(precision - 2, check.toString.length-1))
        myPrintIt(down, check.toString.substring(0, Math.min(9, check.toString.length)), resultP, resultP.indexOf(check.toString.substring(0, Math.min(precision - 4, check.toString.length-1))))
        myAssert(resultP.indexOf(check.toString.substring(0, Math.min(precision - 4, check.toString.length - 1))) == 0)
        */

        def divSpecial(down: Int) = {
            var z = 0
            var rest = 1
            var result = "0."
            var go_on = true
            while (go_on) {
                z = rest * 10 / down
                if (z == 0) {
                    result = result + "0"
                    rest = (rest * 10)
                } else {
                    rest = (rest * 10) - (z * down)
                    result = result + z.toString
                }
                countPrecision += 1
                if (rest == 0) {
                    go_on = false
                }
                lrest.find(_ == rest) match {
                    case Some(i) => go_on = false
                    case _       => lrest = lrest :+ rest
                }
            }
            result
        }
        override def toString = "[[" + down + "] " + countPrecision + ", " + resultP + ", " + lrest + "]"

    }
}

class Euler191 {
    3 until 14 map (i => myAssert2(new doZeJob191_1(i).result, new doZeJob191_2(i).result))
    //3 until 7 map (i => new doZeJob191_2(i))
    //myErrPrintDln(l191_1.mkString("\n  ", "\n  ", "\n  "))
    myErrPrintln("AAAOOL".permutations.toList.length, "AAAOOL".permutations.toList)
    myErrPrintln("AOL".permutations.toList.length, "AOL".permutations.toList)
    myErrPrintDln("Fini!")

    class doZeJob191_2(l: Int) {
        var t_start = Calendar.getInstance
        myPrintDln("*2* " + l + " ***********************************************************")
        var result = BigInt(0)
        val root1 = (0 to l).toList.map((i: Int) => "AOL").mkString("")
        val comb = root1.combinations(l).toList
        if (l < 7) {
            myPrintDln((l, comb.toList.length, comb))
        } else {
            myPrintIt((l, comb.toList.length))
        }
        val no2L = ListSet[String]() ++ comb.filter((s: String) => s.count(_ == 'L') < 2)

        val ou3A = no2L.partition((s: String) => s.count(_ == 'A') < 3)
        if (l < 7) {
            myPrintIt((l, ou3A))
        }

        result += ou3A._1.toList.map(permLength).sum
        printIt(result)

        val pasTropDeA2 = ou3A._2.toList.map(s => {
            var lresult = 0
            val tda = s.partition(_ == 'A')

            signOf(tda._1.length - ((tda._2.length + 1) * 2)) match {
                case 0 =>
                    myPrintln(s, 1); lresult = tda._2.permutations.length
                case -1 =>
                    val t = s.replaceAll("AAA", "B")
                    if ((t.indexOf("A") < 0) && (t.count(_ == 'B') == 1)) {
                        myPrintDln((s, t, permLength(s), permLength(t), permLength(s) - permLength(t)))
                        lresult = (permLength(s) - permLength(t)).toInt
                    } else {
                        val p = s.permutations.toList
                        val q = p.filter(_.indexOf("AAA") < 0)
                        myErrPrintDln(s, t, p.length, q.length, q, p)
                        lresult = q.length
                    }
                case 1 => myPrintln(s, 0); lresult = 0
            }
            lresult
        })
        val pasTropDeA3 = ou3A._2.toList.map(s => {
            var lresult = 0
            val tda = s.partition(_ == 'A')

            signOf(tda._1.length - ((tda._2.length + 1) * 2)) match {
                case 0 =>
                    myPrintln(s, 1); lresult = tda._2.permutations.length
                case -1 =>
                    val t = s.replaceAll("AAA", "B")
                    if (t.count(_ == 'B') > 1) {
                        myErrPrintDln(s, t, "NOGOOD")
                        lresult = 0
                    } else if (t.indexOf("A") < 0) {
                        myPrintDln((s, t, permLength(s), permLength(t), permLength(s) - permLength(t)))
                        lresult = (permLength(s) - permLength(t)).toInt
                    } else {
                        val p = s.permutations.toList
                        val q = p.filter(_.indexOf("AAA") < 0)
                        myErrPrintDln(s, t, p.length, q.length, q, p)
                        lresult = q.length
                    }
                case 1 => myPrintln(s, 0); lresult = 0
            }
            (s, lresult)
        })
        myPrintIt((l, pasTropDeA3.toList.length, pasTropDeA3))

        val pasTropDeA = ou3A._2.filter(s => {
            val tda = s.partition(_ == 'A')
            tda._1.length <= ((tda._2.length + 1) * 2)
        })
        myPrintIt((l, pasTropDeA.toList.length, pasTropDeA))

        val noAAA = ListSet[String]() ++ pasTropDeA.map(_.permutations).flatten.filter((s: String) => s.indexOf("AAA") < 0)
        if (l < 7) {
            myPrintDln((l, noAAA.toList.length, noAAA))
        } else {
            myPrintIt((l, noAAA.toList.length))
        }

        val noAAA2 = ListSet[String]() ++ pasTropDeA.map(z => {
            val y = z.permutations.filter((s: String) => s.indexOf("AAA") < 0).toList
            (y.length, y)
        })
        myPrintDln((l, noAAA2.toList.length, noAAA2.toList.mkString("\n")))

        result += noAAA.toList.length
        myAssert2(noAAA.toList.length, pasTropDeA2.sum)
        myPrintln(getClass.getName, l, result, timeStamp(t_start, this.getClass.getName).getTimeInMillis - t_start.getTimeInMillis())
    }

    class doZeJob191_1(l: Int) {
        var t_start = Calendar.getInstance
        myPrintDln("*1* " + l + " ***********************************************************")
        val root1 = (0 to l).toList.map((i: Int) => "AOL").mkString("")
        val comb = root1.combinations(l).toList
        if (l < 6) {
            myPrintDln((l, comb.toList.length, comb))
        } else {
            myPrintIt((l, comb.toList.length))
        }
        val no2L = ListSet[String]() ++ comb.filter((s: String) => s.count(_ == 'L') < 2)
        val no2Lp = no2L.map(_.permutations).flatten
        if (l < 6) {
            myPrintDln((l, no2L.toList.length, no2L))
        } else {
            myPrintIt((l, no2L.toList.length))
        }
        val ou3A = no2L.partition((s: String) => s.count(_ == 'A') < 3)
        if (l < 6) {
            myPrintDln((l, ou3A))
        }
        //val noAAA = ListSet[String]() ++ no2Lp.filter((s: String) => s.indexOf("AAA") < 0)
        val noAAA = ListSet[String]() ++ ou3A._2.map(_.permutations).flatten.filter((s: String) => s.indexOf("AAA") < 0)
        if (l < 6) {
            myPrintDln((l, noAAA.toList.length, noAAA))
        } else {
            myPrintIt((l, noAAA.toList.length))
        }
        val no3A = ou3A._1.map(_.permutations).flatten
        if (l < 6) {
            myPrintDln((l, no3A.toList.length, no3A))
        } else {
            myPrintIt((l, no3A.toList.length))
        }

        var result = noAAA.toList.length + no3A.toList.length
        myPrintln(getClass.getName, l, result, timeStamp(t_start, this.getClass.getName).getTimeInMillis - t_start.getTimeInMillis())

        override def toString = "| %02d | %06d || %06d | %06d || %06d | %06d | ".format(l, comb.toList.length,
            no2L.toList.length, no2L.toList.length * 2,
            noAAA.toList.length, noAAA.toList.length * 2)
    }

    def signOf(i: Int): Int = {
        if (i > 0) {
            1
        } else if (i < 0) {
            (-1)
        } else {
            0
        }
    }
}