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

object EulerMainNoScalaTestReussi extends App {
    myPrintDln("Hello World!")
    new Euler26
    new Euler187
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



class Euler187 {
    val centMillions = 100000000
    val unMillion = 1000000

    val yo = getNumPrimes2Below(List((0, 4), (1, 100), (2, 15486060), (3, 15486210), (4, 15486433), (5, 15486703), (6, 15486704), (7, 32452920), (8, 32453192)))
    myErrPrintDln("\n" + yo.mkString("\n     "))
    myAssert2(yo.getOrElse(1, null)._3, 25)
    myAssert2(yo.getOrElse(3, null)._3, 1000020)
    myAssert2(yo.getOrElse(5, null)._3, 1000048)
    myAssert2(yo.getOrElse(6, null)._3, 1000048)
    myAssert2(yo.getOrElse(8, null)._3, 2000020)
    myAssert(EulerPrime.isPrime(15486433))
    myAssert2(yo.getOrElse(4, null)._2, 15486433)
    val premiers1million = (new CheckEulerPrime(1000000, 1000)).premiers
    val premiersCent = EulerPrime.premiers1000.takeWhile(_ < 100)

    var end = 1000
    while (checkJobs(end)) {
        end += 10000
    }

    myErrPrintDln("****************************************************************************************************")
    val StuffIreallyNeed = getNumPrimes2Below(stuffIneed)
    myPrintIt(stuffIneed)
    myErrPrintDln(StuffIreallyNeed.toList.sortBy(_._1).mkString("\n         ", "\n         ", "\n"))
    val result187 = doZeJob4(centMillions)
    myPrintIt(result187)
    myAssert2(result187, 17427258)

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
        val primesIneed = EulerPrime.premiers1000.toList.takeWhile(centMillions / _ > unMillion)
        printIt(primesIneed)
        primesIneed.map((bi: BigInt) => (bi, centMillions / bi))
    }

    def doZeJob4(end: BigInt): Int = {
        val premiers = premiers1million
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

        val dernierPremierAvant1Million = (premiers1million.last, premiers1million.toList.length)
        myPrintIt(dernierPremierAvant1Million)
        var sum = StuffIreallyNeed.toList.foldLeft(0)(_ + getNumbers2(_, dernierPremierAvant1Million))

        /*val de1a10 = List(2, 3, 5, 7)

        sum = de1a10.foldLeft(0)(_ + getNumbers(end, _, premiers))*/
        sum += z.map(_._2).sum
        sum
    }

    def getNumbers2(prime: (BigInt, (BigInt, Int, Int)), premierslast: (BigInt, Int)): Int = {
        prime._2._3 - premierslast._2
    }

    def getNumbers(end: BigInt, prime: BigInt, premierslast: BigInt): Int = {
        if (end.toDouble / prime.toDouble > premierslast.toDouble + 1.0) {
            var upper = (end.toDouble / prime.toDouble).toInt
            if (EulerPrime.isPrime(upper)) {
                myErrPrintDln(upper + " isPrime!")
                upper += 1
            }
            val between = EulerPrime.getPrimesBetween(premierslast + 1, upper, premiers1million)
            myPrintDln("\n" + end + " " + prime + " " + premierslast + " " + upper + " " + EulerPrime.isPrime(upper) + " " + between.toList.length)
            between.toList.length
        } else {
            0
        }
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

        sum = de1a10.foldLeft(0)(_ + getNumbers(end, _, premiers.last))
        sum += z.map(_._2).sum
        sum
    }

    def getNumPrimes2Below(lendin: List[(BigInt, BigInt)]): Map[BigInt, (BigInt, Int, Int)] = {
        var lend = lendin.sortBy { _._2 }
        var lendout = Map[BigInt, (BigInt, Int, Int)]()
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
            myPrintDln(e.getName)
            lines.find((l: String) => {
                val y = l.split("\\s+").toList.filter(_ matches """\d+""").map(_.toInt)
                if (y.length == 8) {
                    val z = y.filter(_ <= lend.head._2)
                    if (y.last == lend.head._2) {
                        answer = y.last
                        lendout = lendout + (lend.head._1 -> (lend.head._2, answer, counter + z.length))
                        lend = lend.tail
                        myPrintDln(lend + " " + lendout + " " + counter)
                    } else if (z.length == y.length) {
                        answer = y.last
                    } else if (!z.isEmpty) {
                        answer = z.last
                        lendout = lendout + (lend.head._1 -> (lend.head._2, answer, counter + z.length))
                        lend = lend.tail
                        myPrintDln(lend + " " + lendout + " " + counter)
                    } else {
                        lendout = lendout + (lend.head._1 -> (lend.head._2, answer, counter))
                        lend = lend.tail
                        myPrintDln(lend + " " + lendout + " " + counter)
                    }
                    counter += y.length
                    found = lend.isEmpty
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
    /*
EulerMainNoScalaTest:39 27_18:05_59,973  
         (2,(50000000,49999991,3001134))
         (3,(33333333,33333331,2050943))
         (5,(20000000,19999999,1270607))
         (7,(14285714,14285693,927432))
         (11,(9090909,9090901,608113))
         (13,(7692307,7692301,520415))
         (17,(5882352,5882351,405279))
         (19,(5263157,5263109,365522))
         (23,(4347826,4347823,305944))
         (29,(3448275,3448273,246788))
         (31,(3225806,3225793,231959))
         (37,(2702702,2702701,196821))
         (41,(2439024,2439013,178920))
         (43,(2325581,2325571,171224))
         (47,(2127659,2127659,157710))
         (53,(1886792,1886783,141116))
         (59,(1694915,1694909,127775))
         (61,(1639344,1639307,123896))
         (67,(1492537,1492529,113642))
         (71,(1408450,1408417,107695))
         (73,(1369863,1369861,104960))
         (79,(1265822,1265813,97595))
         (83,(1204819,1204813,93267))
         (89,(1123595,1123589,87418))
         (97,(1030927,1030919,80767))
     * */
}
