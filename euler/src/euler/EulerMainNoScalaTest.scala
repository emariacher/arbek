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

object EulerMainNoScalaTest extends App {
    myPrintDln("Hello World!")
    new Euler187
}

class Euler187 {
    myPrintIt(stuffIneed)
    val yo = getNumPrimes2Below(List(4, 100, 15486060, 15486210, 15486433, 32452920, 32453192))
    myErrPrintDln("\n" + yo)
    myAssert2(yo.getOrElse(15486210, null)._2, 1000020)
    myAssert2(yo.getOrElse(32453192, null)._2, 2000020)
    myAssert(EulerPrime.isPrime(15486433))
    myAssert2(yo.getOrElse(15486433, null)._1, 15486433)
    val premiers1million = (new CheckEulerPrime(1000000, 1000)).premiers
    val premiersCent = EulerPrime.premiers1000.takeWhile(_ < 100)

    var end = 1000
    while (checkJobs(end)) {
        end += 10000
    }

    def checkJobs(end: BigInt) = {
        var t_start = Calendar.getInstance()
        val j1 = doZeJob2(end, EulerPrime.premiers100000)
        val t1 = timeStamp(t_start, "doZeJob1")
        val j2 = doZeJob2(end, premiers1million)
        val t2 = timeStamp(t1, "doZeJob2")
        val j3 = doZeJob3(end, EulerPrime.premiers10000)
        val t3 = timeStamp(t2, "doZeJob2")
        myErrPrintDln("\n" + end + ": " + j1 + "/" + (t1.getTimeInMillis - t_start.getTimeInMillis) + " " + j2 + "/" + (t2.getTimeInMillis - t1.getTimeInMillis) + " " + j3 + "/" + (t3.getTimeInMillis - t2.getTimeInMillis))
        j1 == j2 & j2 == j3
    }

    def stuffIneed = {
        val centMillions = 100000000
        val unMillion = 1000000
        val primesIneed = EulerPrime.premiers1000.toList.takeWhile(centMillions / _ > unMillion)
        primesIneed.map(centMillions / _)
    }

    def doZeJob3(end: BigInt, premiers: TreeSet[BigInt]): Int = {
        val limit = end / 2
        val prems1 = premiers.takeWhile(_ < limit).zipWithIndex.toList

            def getNum(p: (BigInt, Int)): Int = {
                prems1.reverse.find((c: (BigInt, Int)) => c._1 * p._1 < end) match {
                    case Some(r) => r._2 - p._2 + 1
                    case _       => 0
                }
            }

        val z = prems1.takeWhile((c: (BigInt, Int)) => c._1 * c._1 < end).map((p: (BigInt, Int)) => { (p._1, getNum(p)) })
        //printIt(z)

        var sum = 0

        val de1a10 = List(2, 3, 5, 7)

        sum = de1a10.foldLeft(0)(_ + getNumbers(end, _, premiers))
        sum += z.map(_._2).sum
        sum
    }

    def getNumbers(end: BigInt, prime: BigInt, premiers: TreeSet[BigInt]): Int = {
        if (end.toDouble / prime.toDouble > premiers.last.toDouble + 1.0) {
            var upper = (end.toDouble / prime.toDouble).toInt
            if (EulerPrime.isPrime(upper)) {
                myErrPrintDln(upper + " isPrime!")
                upper += 1
            }
            val between = EulerPrime.getPrimesBetween(premiers.last + 1, upper, premiers1million)
            myPrintDln("\n" + end + " " + prime + " " + premiers.last + " " + upper + " " + EulerPrime.isPrime(upper) + " " + between.toList.length)
            between.toList.length
        } else {
            0
        }
    }

    def getNumPrimes2Below(lendin: List[BigInt]): Map[BigInt, (Int, Int)] = {
        var lend = lendin.sorted
        var lendout = Map[BigInt, (Int, Int)]()
        myErrPrintDln(lend)
        var answer = 0
        var prevanswer = 0
        var counter = 0
        var counter2 = 0
        var found = false
        val mfc = new MyFileChooser("TestOutput.txt")
        val f = mfc.justChooseFile("zip")
        myErrPrintDln(f.getName)
        val rootzip = new java.util.zip.ZipFile(f)

        rootzip.entries.filter(_.getName.endsWith(".txt")).find(e => {
            val lines = Source.fromInputStream(rootzip.getInputStream(e)).getLines
            myErrPrintDln(e.getName)
            lines.find((l: String) => {
                val y = l.split("\\s+").toList.filter(_ matches """\d+""").map(_.toInt)
                if (y.length == 8) {
                    //myPrintDln("  [" + y + "]")
                    val z = y.filter(_ <= lend.head)
                    if (z.isEmpty) {
                        if (!lendout.isEmpty) {
                            myAssert(prevanswer != answer) // primes too close to one another
                        }
                        lendout = lendout + (lend.head -> (answer, counter))
                        counter2 += y.length
                        counter = counter2
                        lend = lend.tail
                        myPrintln("\n" + lend + " " + lendout + " " + counter + " " + counter2)
                        found = lend.isEmpty
                    } else {
                        prevanswer = answer
                        answer = z.last
                        counter += z.length
                        counter2 += y.length
                    }
                }
                found
            })
            found
        })
        lendout
    }

    def doZeJob2(end: BigInt, premiers: TreeSet[BigInt]): Int = {
        val limit = end / 2

        val prems1 = premiers.takeWhile(_ < limit)
        var prems = prems1.toList
        var count = 0

        //printIt(prems1)
        prems1.takeWhile((p: BigInt) => {
            if ((p * p) < end) {
                val r = getlength(prems, end, p)
                prems = r._2
                count += r._1
                //printIt(p+": "+count+" "+r)
                true
            } else {
                false
            }
        })
        //printIt(end+" "+count)
        count
    }

    def getlength(prems: List[BigInt], end: BigInt, prime: BigInt) = {
        val newprems = prems.dropWhile(_ < prime).takeWhile(_.toDouble < end.toDouble / prime.toDouble)
        (newprems.length, newprems)
    }

    def doZeJob1(end: BigInt, premiers: TreeSet[BigInt]): Int = {
        val limit = end / 2

        val prems = premiers.takeWhile(_ < limit)
        //printIt(prems)

        val l = 1 until end.toInt toList

        val z = l.map((i: Int) => (i, new EulerDiv(i).primes)).filter(_._2.length == 2)
        //printIt(end+" "+z.length+" "+prems.toList.length+" "+z)
        z.length
    }
}