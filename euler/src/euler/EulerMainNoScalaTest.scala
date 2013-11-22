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

        timeStampIt(new Euler347)

    } catch {
        case ex: Exception => {
            println("\n" + ex)
            println("\n" + ex.getStackTrace.mkString("\n  ", "\n  ", "\n  ") +
                "\n")
        }
    } finally {
        println("\nHere!")
        MyLog.closeFiles
        println("\nThere!")
    }
}

class Euler347 {
    val premiers = EulerPrime.premiers100000
    myPrintIt(findPow(1000, 2))
    myPrintIt(findMpq(100, 2, 3))
    myAssert2(doZeJob1(100)._2, 2262)
    //waiting(1 second)
    (1 until 30).foreach(doZeJob1(_))
    myAssert2(doZeJob3(100)._2, 2262)

    def doZeJob3(n: BigInt) = {
        if (n > EulerPrime.premiers100000.last * 2) {
            throw new Exception(n + " > " + EulerPrime.premiers100000.last * 2)
        }
        val onePrime = premiers.takeWhile(_ <= (n / 2)).toList
        myPrintIt(onePrime)

        val combis = onePrime.filter(_ < Euler.sqrt(n)).map(op => (op, onePrime.dropWhile(_ < op).takeWhile(_ <= (n / op)).toList.filter(z => (z % op) * (op % z) != 0)))

        myPrintIt(combis.mkString("\n  ", "\n  ", "\n  "))
        val r = combis.map(z => z._2.map(findMpq(n, z._1, _)))
        myPrintIt(r.mkString("\n  ", "\n  ", "\n  "))
        val sum = r.flatten.map(_._3).sum
        myPrintln(n, sum, r)
        (n, sum, r)
    }

    def doZeJob2(jprev: Job) = {
        val n = jprev.n + 1
        var sum = BigInt(0)
        var pr = List.empty[BigInt]
        var r = List.empty[(BigInt, BigInt, BigInt)]
        if (n == 5) {
            sum = 0
            pr = List(2)
        } else if (EulerPrime.isPrime(n)) {
            sum = jprev.sum
            pr = jprev.pr
            r = jprev.r
        } else {
            pr = premiers.takeWhile(_ <= (n / 2)).toList
            r = pr.combinations(2).toList.map(pq => findMpq(n, pq)).filter(_._3 > 0)
            sum = r.map(_._3).sum
            myPrintln(n, sum, new EulerDiv(n).primes, pr, r)
        }
        new Job(n, sum, pr, r)
    }

    def doZeJob1(n: BigInt) = {
        if (n > EulerPrime.premiers100000.last * 2) {
            throw new Exception(n + " > " + EulerPrime.premiers100000.last * 2)

        }
        //var r = EulerPrime.premiers100000.takeWhile(_<=(n/2)).toList.combinations(2).toList.map(pq => findMpq(n, pq)).filter(_._3 > 0).sortBy(_._3)
        val pr = premiers.takeWhile(_ <= (n / 2)).toList
        var r = pr.combinations(2).toList.map(pq => findMpq(n, pq)).filter(_._3 > 0)
        val sum = r.map(_._3).sum
        myPrintln(n, sum, pr, r, "   ", new EulerDiv(n).primes)
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
    class Job(val n: BigInt, val sum: BigInt, val pr: List[BigInt], val r: List[(BigInt, BigInt, BigInt)]) {
        def this(j: Job) = this(j.n, j.sum, j.pr, j.r)
        if (n > EulerPrime.premiers100000.last * 2) {
            throw new Exception(n + " > " + EulerPrime.premiers100000.last * 2)
        }

    }
}
