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

object EulerMainNoScalaTest extends App {
    //MyLog.newMyLog("EulerMainNoScalaTest", new File("log"), "log")
    try {
        myPrintDln("Hello World!")

        new Euler347

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

class Euler347 {
    val premiers = EulerPrime.premiers100000
    myPrintIt(findPow(1000, 2))
    myPrintIt(findMpq(100, 2, 3))
    timeStampIt(doZeJob1(100, 2262)._2)
    (1 until 30).foreach(doZeJob1(_, 0))
    timeStampIt(doZeJob3(100, 2262)._2)
    timeStampIt(doZeJob4(100, 0)._2)

    def doZeJob4(n: BigInt, expected: BigInt) = {
        if (n > EulerPrime.premiers100000.last * 2) {
            throw new Exception(n + " > " + EulerPrime.premiers100000.last * 2)
        }

        val onePrime = getOnePrimes(n)
        myPrintIt(onePrime)

        val combis = onePrime.filter(_._1 < Euler.sqrt(n)).map(op => (op, onePrime.dropWhile(_._1 < op._1).takeWhile(_._1 <= (n / op._1)).
            toList.filter(z => (z._1 % op._1) * (op._1 % z._1) != 0)))
        myPrintIt(combis.mkString("\n  ", "\n  ", "\n  "))
        /*val r = combis.map(z => z._2.map(findMpq(n, z._1, _)))
        myPrintIt(r.mkString("\n  ", "\n  ", "\n  "))
        val sum = r.flatten.map(_._3).sum
        myPrintln(n, sum, r)
        (n, sum, r)*/
        val result = 0
        if (expected != 0) {
            myAssert2(result, expected)
        }
        (n, result)
    }

    def doZeJob3(n: BigInt, expected: BigInt) = {
        if (n > EulerPrime.premiers100000.last * 2) {
            throw new Exception(n + " > " + EulerPrime.premiers100000.last * 2)
        }
        val onePrime = premiers.takeWhile(_ <= (n / 2)).toList
        myPrintIt(onePrime)

        val combis = onePrime.filter(_ < Euler.sqrt(n)).map(op => (op, onePrime.dropWhile(_ < op).takeWhile(_ <= (n / op)).
            toList.filter(z => (z % op) * (op % z) != 0)))

        myPrintIt(combis.mkString("\n  ", "\n  ", "\n  "))
        val r = combis.map(z => z._2.map(findMpq(n, z._1, _)))
        myPrintIt(r.mkString("\n  ", "\n  ", "\n  "))
        val sum = r.flatten.map(_._3).sum
        myPrintln(n, sum, r)
        val result = sum
        if (expected != 0) {
            myAssert2(result, expected)
        }

        (n, sum, r)
    }

    def doZeJob1(n: BigInt, expected: BigInt) = {
        if (n > EulerPrime.premiers100000.last * 2) {
            throw new Exception(n + " > " + EulerPrime.premiers100000.last * 2)

        }
        //var r = EulerPrime.premiers100000.takeWhile(_<=(n/2)).toList.combinations(2).toList.map(pq => findMpq(n, pq)).filter(_._3 > 0).sortBy(_._3)
        val pr = premiers.takeWhile(_ <= (n / 2)).toList
        var r = pr.combinations(2).toList.map(pq => findMpq(n, pq)).filter(_._3 > 0)
        val sum = r.map(_._3).sum
        myPrintln(n, sum, pr, r, "   ", new EulerDiv(n).primes)
        val result = sum
        if (expected != 0) {
            myAssert2(result, expected)
        }

        (n, sum, pr, r)
    }

    def findMpq(n: BigInt, pq: List[BigInt]): (BigInt, BigInt, BigInt) = findMpq(n, pq.head, pq.last)

    def findMpq(n: BigInt, p: BigInt, q: BigInt): (BigInt, BigInt, BigInt) = {
        myAssert(EulerPrime.isPrime(p))
        myAssert(EulerPrime.isPrime(q))
        if (p * q <= n) {
            val lp = findPow(n, p)
            val lq = findPow(n, q)

            //val llp = lp.map(pp => lq.reverse.dropWhile(_ * pp > n).head).max
            val llp = lp.map(pp => {
                val lz = lq.reverse.dropWhile(_ * pp > n)
                if (lz.isEmpty) {
                    BigInt(0)
                } else {
                    lz.head * pp
                }
            })
            //myPrintln(n, p, q, llp)
            val llq = lq.map(pq => {
                val lz = lp.reverse.dropWhile(_ * pq > n)
                if (lz.isEmpty) {
                    BigInt(0)
                } else {
                    lz.head * pq
                }
            })
            //myPrintln(n, p, q, llq)
            (p, q, BigInt(Math.max(llp.max.toDouble, llq.max.toDouble).toLong))
        } else {
            (0, 0, 0)
        }
    }

    def findPow(n: BigInt, p: BigInt) = {
        var lexp = List.empty[BigInt]
        var pexp = p
        while (pexp < n) {
            lexp = lexp :+ pexp
            pexp = pexp * p
        }
        lexp
    }

    def getOnePrimes(n: BigInt) = {
        if (n > BigInt(Integer.MAX_VALUE)) {
            throw new Exception(n + " > " + Integer.MAX_VALUE)
        }
        (2 until ((n / 2) + 1).toInt).map(i => {
            val primes = new EulerDiv(i).primes
            val primesUnique = TreeSet[BigInt]() ++ primes
            (BigInt(i), primesUnique)
        }).filter(_._2.toList.length == 1).sortBy { _._1 }.map(z => (z._1, z._2.head))
    }

    class Job(val n: BigInt, val sum: BigInt, val pr: List[BigInt], val r: List[(BigInt, BigInt, BigInt)]) {
        def this(j: Job) = this(j.n, j.sum, j.pr, j.r)
        if (n > EulerPrime.premiers100000.last * 2) {
            throw new Exception(n + " > " + EulerPrime.premiers100000.last * 2)
        }

    }
}
