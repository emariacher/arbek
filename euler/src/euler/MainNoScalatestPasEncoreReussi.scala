package euler

import kebra.MyLog._
import kebra.MyLog
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
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import Permutations._


object MainNoScalatestPasEncoreReussi extends App {
    new Euler133
    new Euler191
    new Euler241
    new Euler448
}

class Euler133 {
    // 615963 10Millions :(
    // 453688666 10Millions mais correct
    val premiers = EulerPrime.premiers100000

    val list2test = List[Int](Math.pow(10, 4).toInt, Math.pow(10, 5).toInt, Math.pow(10, 6).toInt, Math.pow(2, 23).toInt, Math.pow(5, 10).toInt, Math.pow(10, 7).toInt)

    doZeJob

    def doZeJob {
        val primes2 = doZeJob6

        val result = premiers.filter(!primes2.contains(_)).sum

        myPrintIt(result)
        myPrintIt(primes2)
        myPrintIt(premiers.filter(!primes2.contains(_)).take(40))
        myPrintDln("Job done!")
    }

    def doZeJob4(n: Int, primesI: List[BigInt]): List[BigInt] = {
        MyLog.waiting(1 second)
        myErrPrintln("doZeJob4 *********** " + n)
        MyLog.waiting(1 second)
        val bi = BigInt((1 until n + 1).map(z => "1").mkString)
        val bi2 = bi / primesI.product
        //val start = primesI.last
        val start = 1
        //myPrintIt("\n", n, start, bi2)
        val primes = new EulerDiv132(bi, premiers, start, 40).primes
        val justChecking = primes.product
        myPrintln("\n", n, primes.length, primes.sum, justChecking.toString.toList.length, "\n")
        val zprimes = primes.take(40)
        myPrintln("\n  zprimes: ", zprimes, zprimes.length)
        MyLog.waiting(1 second)
        myErrPrintln(n, primes.take(40).sum)
        MyLog.waiting(1 second)
        primes.sorted
    }
    def doZeJob6 = {
        var primes2 = ListSet.empty[BigInt]
        var primes = List[BigInt](1)
        list2test.map(i => {
            primes = doZeJob4(i, primes)
            primes2 = primes2 ++ primes
            val zprimes2 = primes2.toList.sorted.take(40)
            MyLog.waiting(1 second)
            myErrPrintln("\n  zprimes2: ", zprimes2, zprimes2.length)
            MyLog.waiting(1 second)
        })
        primes2
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

class Euler429 {
    val modulo = BigInt(1000000009)

    myPrintIt(modulo, new EulerDiv(modulo).primes)
    (2 until 100).map(doZeJob1(_))

    def doZeJob1(i: Int) = {
        val fbi = EulerFactorielle.fact2(i)
        val primes = new EulerDiv(fbi).primes
        val byPrime = primes.groupBy(_ + 0).map(_._2.product).toList.sorted
        val prod = products(byPrime)
        val res = result(i, prod)
        //myPrintln(i, res)
        res
    }

    def products(byPrime: List[BigInt]) = {
        val len = byPrime.length - 1
        ((1 to len).map(byPrime.combinations(_).toList.map(_.product)).flatten
            ++ List[BigInt](1, byPrime.product)).sorted.toList
    }
    def result(i: Int, products: List[BigInt]) = {
        val sumod = products.map(bi => bi * bi).sum % modulo
        myPrintln("  " + i + " " + sumod + " " +
            new EulerDiv(sumod).primes.groupBy(_ + 0).map(c => (c._1, c._2.length)).toList.sortBy(_._1))
        sumod
    }
}

class Euler448 {

    myAssert2(ppcm(45, 12), 15 * 12)
    myAssert2(ppcm(45, 48), 45 * 16)
    myAssert2(A(2), 2)
    myAssert2(A(10), 32)
    myAssert2(S1(100), 122726)
    //myPrintIt(new EulerDivisors(new EulerDiv(12)).divisors)
    myAssert2(h(15, 8), 43)
    (1 until 30).foreach(pf(_))

    def f(n: BigInt, bi: BigInt): BigInt = {
        val base = g(n, bi)
        val dn = new EulerDiv(n).primes
        val dbi = new EulerDiv(bi).primes
        val nmodbi = n % bi

            def low = {
                if (nmodbi < dbi.head) {
                    base
                } else {
                    base - ((bi / dbi.head) * (dbi.head - 1))
                }
            }

        bi.toInt match {
            case 1 => n
            case 4 => nmodbi.toInt match {
                case 3 => h(n, bi)
                case 2 => base - 2
                case _ => base
            }
            case 6 => nmodbi.toInt match {
                case 5 => h(n, bi)
                case 4 => base - 10
                case 3 => base - 7
                case _ => low
            }
            case 8 => nmodbi.toInt match {
                case 7 => h(n, bi)
                case 6 => base - 14
                case 5 => base - 10
                case 4 => base - 10
                case _ => low
            }
            case 9 => nmodbi.toInt match {
                case 8 => h(n, bi)
                case 7 => base - 12
                case 6 => base - 12
                case _ => low
            }
            case 10 => nmodbi.toInt match {
                case 9 => h(n, bi)
                case 8 => base - 28
                case 7 => base - 23
                case 6 => base - 23
                case 5 => base - 18
                case 4 => base - 10
                case _ => low
            }
            case 12 => nmodbi.toInt match {
                case 11 => h(n, bi)
                case 10 => base - 56
                case 9  => base - 50
                case 8  => base - 42
                case 7  => base - 33
                case 6  => base - 33
                case 5  => base - 23
                case 4  => base - 23
                case 3  => base - 14
                case _  => low
            }
            case 14 => nmodbi.toInt match {
                case 13 => h(n, bi)
                case 11 => base - 47
                case 10 => base - 47
                case 9  => base - 40
                case 8  => base - 40
                case 7  => base - 33
                case 6  => base - 21
                case 5  => base - 14
                case 4  => base - 14
                //case 3  => base - 7
                //case 2  => base - 7
                //case _  => base
                case _  => low
            }
            case 15 => nmodbi.toInt match {
                case 14 => h(n, bi)
                case 10 => base - 54
                case 9  => base - 42
                case 8  => base - 32
                case 7  => base - 32
                case 6  => base - 32
                case 5  => base - 22
                /*case 4  => base - 10
                case 3  => base - 10
                case _  => base*/
                case _  => low
            }
            case 16 => nmodbi.toInt match {
                case 15 => h(n, bi)
                case 9  => base - 42
                case 8  => base - 42
                case 7  => base - 28
                case 6  => base - 28
                case 5  => base - 20
                case 4  => base - 20
                case _  => low
            }
            case 18 => nmodbi.toInt match {
                case 17 => h(n, bi)
                case 7  => base - 45
                case 6  => base - 45
                case 5  => base - 30
                case 4  => base - 30
                case 3  => base - 21
                case _  => low
            }
            case 20 => nmodbi.toInt match {
                case 19 => h(n, bi)
                case 5  => base - 41
                case 4  => base - 25
                case _  => low
            }
            case 21 => nmodbi.toInt match {
                case 20 => h(n, bi)
                case _  => low
            }
            case 22 => nmodbi.toInt match {
                case 21 => h(n, bi)
                case _  => low
            }
            case _ => biprime(n, bi)
        }
    }

    def biprime(n: BigInt, bi: BigInt): BigInt = {
        if ((n % bi) == (bi - 1)) {
            ((n / bi) * (((bi - 1) * bi) + 1))
        } else {
            f(n - ((n % bi) + 1), bi) + 1 + ((n % bi) * bi)
        }
    }
    def g(n: BigInt, bi: BigInt): BigInt = h(n - ((n % bi) + 1), bi) + 1 + ((n % bi) * bi)
    def h(n: BigInt, bi: BigInt): BigInt = {
        val dn = new EulerDiv(bi).primes
        val mdn = dn.groupBy(biu => biu).map(bbi => bbi._1 -> bbi._2.length)
        var out = BigInt(0)
        var biz = BigInt(0)
        while (biz < bi) {
            biz = biz + 1
            val ppcmz = ppcm(bi, biz)
            if (ppcmz == (bi * biz)) {
                out += bi
            } else {
                out += ppcmz / biz
            }
        }
        out * (n / bi)
    }
    def h2(n: BigInt, bi: BigInt): BigInt = {
        if (n <= bi) {
            0
        } else {
            val divisors = new EulerDivisors(new EulerDiv(bi)).divisors // nogood for 8
            val z = divisors.map(d => bi / d)
            val y = ((n / bi) * (bi * (bi - (divisors.length + 1)))) + 1
            myPrintDln(n, bi, y + z.sum, y, z)
            y + z.sum
        }
    }

    def pf(n: BigInt) {
        val z = (1 until (n + 1).toInt).map(i => (i, f(n, i)))
        val s1 = S1(n)
        val s2 = S2(n)
        val s2z = s2.groupBy(_._2).toList.map(u => (u._1, u._2.map(_._3).sum, u._2)).sortBy(_._1)
        val s = z.map(_._2).sum
        //myPrintln(s2z.mkString("\n"))
        //myPrintln(s2z.map(u => (u._1, u._2)))
        myPrintln("************", n, s, s1, s2.map(_._3).sum,"   "+new EulerDiv(s1).primes, "\n" + z.toList)
        assert(s1 == s)
    }

    def A2(n: BigInt) = {
        val dn = new EulerDiv(n).primes
        val mdn = dn.groupBy(bi => bi).map(bbi => bbi._1 -> bbi._2.length)
        var out = List.empty[(BigInt, BigInt)]
        var bi = BigInt(0)
        while (bi < n) {
            bi = bi + 1
            //out += ppcm2(n, mdn, bi)
            out = out :+ (bi, ppcm2(n, mdn, bi))
            //myPrint(bi, ppcm2(n, mdn, bi))
        }
        out
    }

    def S2(n: BigInt) = {
        var out = List.empty[(BigInt, BigInt, BigInt)]
        var bi = BigInt(0)
        while (bi < n) {
            bi = bi + 1
            val a = A2(bi)
            out = out ++ a.map(b => (bi, b._1, b._2))
            //myPrintln("\n", bi, a, out.map(_._3).sum)
        }
        out
    }

    def A(n: BigInt) = {
        //myPrintln("  " + n + " *****************************************")
        /*if (EulerPrime.isPrime(n)) {
      myPrintln("    ", (n * (n - 1) / 2) + 1)
      (n * (n - 1) / 2) + 1
    } else */ {
            val dn = new EulerDiv(n).primes
            val mdn = dn.groupBy(bi => bi).map(bbi => bbi._1 -> bbi._2.length)
            var out = BigInt(0)
            var bi = BigInt(0)
            while (bi < n) {
                bi = bi + 1
                out += ppcm2(n, mdn, bi)
                //myPrint(bi, ppcm2(n, mdn, bi))
            }
            out
        }
    }

    def S1(n: BigInt) = {
        var out = BigInt(0)
        var bi = BigInt(0)
        while (bi < n) {
            bi = bi + 1
            val a = A(bi)
            out += a
            //myPrintln("\n", bi, a, out)
        }
        out
    }

    def ppcm2(n: BigInt, mdn: Map[BigInt, Int], q: BigInt) = {
        if (n % q == 0) {
            BigInt(1)
        } else {
            val dq = new EulerDiv(q).primes
            if (dq.length == 1) {
                q
            } else {

                val mdq = dq.groupBy(bi => bi).map(bbi => bbi._1 -> bbi._2.length)
                val zz = ListSet.empty[(BigInt, Int)] ++ mdn.map(kv => (kv._1, Math.max(mdq.getOrElse(kv._1, 0), kv._2))) ++ mdq.map(kv => (kv._1, Math.max(mdn.getOrElse(kv._1, 0), kv._2)))
                zz.map(bbi => Euler.powl(bbi._1, bbi._2)).product / n
            }
        }
    }

    def ppcm(p: BigInt, q: BigInt) = {
        if (p == q) {
            p
        } else {
            val dp = new EulerDiv(p).primes
            val mdp = dp.groupBy(bi => bi).map(bbi => bbi._1 -> bbi._2.length)
            val dq = new EulerDiv(q).primes
            val mdq = dq.groupBy(bi => bi).map(bbi => bbi._1 -> bbi._2.length)
            val zz = ListSet.empty[(BigInt, Int)] ++ mdp.map(kv => (kv._1, Math.max(mdq.getOrElse(kv._1, 0), kv._2))) ++ mdq.map(kv => (kv._1, Math.max(mdp.getOrElse(kv._1, 0), kv._2)))
            zz.map(bbi => Euler.powl(bbi._1, bbi._2)).product
        }
    }
}

