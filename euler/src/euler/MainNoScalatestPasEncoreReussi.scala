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

object MainNoScalatestPasEncoreReussi extends App {
    new Euler191
    new Euler241
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

class Euler241 {
    val lj = List(
        (List(BigInt(2)), List(BigInt(100000)), List[BigInt](2)),
        (List(BigInt(4320)), List(BigInt(10000000)), List[BigInt](8, 9)),
        (List(BigInt(26208)), List(BigInt(1000000), BigInt(1000)), List[BigInt](32, 9, 13)),
        (List(BigInt(8910720)), List(BigInt(1000000), BigInt(10000)), List[BigInt](32, 9, 13, 35))
        //(List(BigInt(0)), List(BigInt(1000000), BigInt(1000)), List[BigInt](32, 9, 11)),
        //(List(BigInt(8910720)), List(BigInt(10000000), BigInt(10000)), List[BigInt](128, 9, 13, 35)),
        //(List[BigInt](0), List(BigInt(100000000), BigInt(10000)), List[BigInt](1024, 9, 13, 35)),
        //(List(BigInt(0)), List(BigInt(100000000), BigInt(10000)), List[BigInt](1024, 9, 11, 35)),
        //(List(BigInt(0)), List(BigInt(100000000), BigInt(10000)), List[BigInt](1024, 9, 17, 35)),
        //(List[BigInt](2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 5, 7, 13, 13, 31, 61), List(BigInt(100000000), BigInt(10000)), List[BigInt](2048, 9, 13, 35))
        //(List[BigInt](2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 5, 7, 13, 13, 31, 61), List(BigInt(100000000), BigInt(100000)), List[BigInt](2048, 9, 169, 35))
        )

    /* 
     * [206166804480,List(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 5, 7, 13, 13, 31, 61),4.5,158]
[8583644160,List(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 5, 7, 13, 23, 89),4.5,163]
[20427264,List(2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 11, 13, 31),3.5,79]
[17428320,List(2, 2, 2, 2, 2, 3, 3, 5, 7, 7, 13, 19),4.5,67]
[8910720,List(2, 2, 2, 2, 2, 2, 2, 3, 3, 5, 7, 13, 17),4.5,62]
[26208,List(2, 2, 2, 2, 2, 3, 3, 7, 13),3.5,36]
[4680,List(2, 2, 2, 3, 3, 5, 13),3.5,30]
[4320,List(2, 2, 2, 2, 2, 3, 3, 3, 5),3.5,24]
[24,List(2, 2, 2, 3),2.5,9]
[2,List(2),1.5,2]

     * 
     * */
    val result = ListSet[DoZeJob]() ++ lj.map(t => loop2(t._1, t._2, t._3)).toList.flatten
    myErrPrintln(result.mkString("\n"))

    class DoZeJob(val bi: BigInt) {
        val div = new EulerDiv241(bi)
        val sbi = sigma(bi, div)
        val pfbi = perfquot(bi, sbi)
        val spfbi = pfbi.toString
        val kp5 = spfbi.substring(spfbi.length - 2) == ".5"
        //myPrintln(bi, sbi, pfbi, kp5)

        override def toString = "[" + bi + "," + div.primes + ",\n" + (new EulerDivisors(div.primes).getFullDivisors).sorted + ",\n" + pfbi + "]"
        def toString2 = "[" + bi + "," + div.primes + "," + pfbi + "," + div.primes.sum + "]"
        override def equals(a: Any) = bi == a.asInstanceOf[DoZeJob].bi
        override def hashCode = bi.toInt
    }

    def loop(start: List[BigInt], end: List[BigInt], inc: List[BigInt]) = {
        var result = ListSet[DoZeJob]()
        var t_start1 = Calendar.getInstance
        printIt(start, end.mkString("*"), inc.mkString("*"))
        var count = start.product
        var count2 = 0
        val end1 = end.product
        val inc1 = inc.product
        while (count < end1) {
            val dzj = new DoZeJob(count)
            if (dzj.kp5) {
                myPrintln("\n    " + dzj)
                result = result + dzj
            }
            if (count2 % 1000 == 99) {
                print(".")
            }

            count += inc1
            count2 += 1
        }
        timeStamp(t_start1, (start.mkString("*"), end.mkString("*"), inc.mkString("*")).toString)
        myErrPrintDln((start.mkString("*"), end.mkString("*"), inc.mkString("*")).toString, result.toList.sortBy(_.bi).mkString("\n", "\n", "\n"))
        result
    }

    def loop2(start: List[BigInt], end: List[BigInt], inc: List[BigInt]) = {
        var t_start1 = Calendar.getInstance
        val size = (((end.product - start.product) / inc.product) + 1).toInt

        val inc1 = inc.product
        printIt(start, end.mkString("*"), inc.mkString("*"), size)

        val result = (0 to size).toList.par.map(BigInt(_)).map(bi => {
            if (bi % 10000 == 99) {
                print(".")
            }
            val dzj = new DoZeJob(bi * inc1)
            if (dzj.kp5) {
                print("+")
            }
            dzj
        }).filter(_.kp5)

        myErrPrintDln(((start.mkString("*"), end.mkString("*"), inc.mkString("*")).toString, size, result.toList.sortBy(_.bi).mkString("\n", "\n", "\n")))
        timeStamp(t_start1, (start.mkString("*"), end.mkString("*"), inc.mkString("*"), size).toString)
        result
    }

    def sigma(bi: BigInt) = (new EulerDivisors(new EulerDiv241(bi).primes).getFullDivisors).sum
    def sigma(bi: BigInt, ed: EulerDiv241) = (new EulerDivisors(ed.primes).getFullDivisors).sum
    def perfquot(bi: BigInt) = sigma(bi).toDouble / bi.toDouble
    def perfquot(bi: BigInt, sbi: BigInt) = sbi.toDouble / bi.toDouble
}

