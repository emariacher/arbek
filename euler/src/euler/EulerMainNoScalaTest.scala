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
import akka.actor._
import akka.routing.RoundRobinRouter
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import _root_.JFplot._
import _root_.JFplot.jFigure._
import java.awt.Color

object EulerMainNoScalaTest extends App {
    //MyLog.newMyLog("EulerMainNoScalaTest", new File("log"), "log")
    try {
        myPrintDln("Hello World!")

        timeStampIt(new Euler448)
    } catch {
        case ex: Exception => {
            println("\n" + ex)
            println("\n" + ex.getStackTrace.mkString("\n  ", "\n  ", "\n  ") +
                "\n")
            printTimeStampsList
        }
    } finally {
        println("\nHere!")
        printTimeStampsList
        MyLog.closeFiles
        println("\nThere!")
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
        myPrintln(s2z.mkString("\n"))
        myPrintln(s2z.map(u => (u._1, u._2)))
        myPrintln("************", n, s, s1, s2.map(_._3).sum, "\n" + z.toList)
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
            myPrint(bi, ppcm2(n, mdn, bi))
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
            myPrintln("\n", bi, a, out.map(_._3).sum)
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
                myPrint(bi, ppcm2(n, mdn, bi))
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
            myPrintln("\n", bi, a, out)
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
