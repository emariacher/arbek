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

object EulerMainNoScalaTestReussi extends App {
    myPrintDln("Hello World!")
    new Euler6
    new Euler26
    new Euler114
    new Euler187
}
class Euler6 {
    val z10 = (1 until 11).toList
    val sum10 = z10.sum
    val sumSquare10 = sum10*sum10
    val squareSum10 = z10.map(i => i*i).sum
    printIt(z10,sumSquare10-squareSum10)
    myAssert2(sumSquare10-squareSum10,2640)
    val z100 = (1 until 101).toList
    val sum100 = z100.sum
    val sumSquare100 = sum100*sum100
    val squareSum100 = z100.map(i => i*i).sum
    printIt(z100,sumSquare100-squareSum100)
    myAssert2(sumSquare100-squareSum100,25164150)
}

class Euler203 {
    myAssert2(105, distinctSquarefreeNumbersSum1(8))
    val result203 = distinctSquarefreeNumbersSum1(51)
    printIt(51, result203)
    myAssert2("34029210557338", result203.toString)

    def distinctSquarefreeNumbersSum1(rowNumber: BigInt) = {
        (ListSet[BigInt]() ++ new TrianglePascal(rowNumber.toInt).triangle.flatten).
            map(bi => (bi, new EulerDiv(bi).primes)).filter(c => (ListSet[BigInt]() ++ c._2).toList.
                length == c._2.length).map(_._1).sum
    }
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


class Euler114(val len: Int) {
    def this() = this(50)
    //(50,16475640049)

    myAssert2(sum1p2p3pN(9), 45)
    val lcheck = Map(1 -> 1, 2 -> 1, 3 -> 2, 4 -> 4, 5 -> 7, 6 -> 11, 7 ->
        17, 8 -> 27, 9 -> 44, 10 -> 72, 11 -> 117)
    val s1 = (1 to len).map(i => "0").toList.mkString
    val root: ListSet[String] = getRoot2
    //myErrPrintDln(len, root)

    val byLength = root.filter(s => (s.count(_ != '0') * 2) <= (s.length +
        1)).groupBy(_.length).toList.sortBy(_._1)
    //myPrintDln(byLength.mkString("\n  ", "\n  ", "\n  "))
    val zfinal = getFinal2
    val result = zfinal.sum
    myErrPrintDln(len, result)
    if (lcheck.getOrElse(len, 0) != 0) {
        myAssert2(result, lcheck.getOrElse(len, 0))
    }
    if(len==50) {
        myAssert2(result,BigInt("16475640049"))
    }

    def getRoot2 = {
        var root = ListSet.empty[String]
        if (len == 1) {
            root = ListSet("0")
        } else {
            val fromPrevRoot = new Euler114(len - 1).root.map(s => s + "0")
            //printIt(len, fromPrevRoot)
            root = fromPrevRoot
            if (len > 4) {
                (3 to len - 1).map(i => {
                    //printIt(i, ubersetz(i))
                    val s = (1 to i).map(c => "0").mkString
                    root = root ++ fromPrevRoot.map(_.replaceFirst(s, ubersetz(i)))
                    //myPrintDln("  " + s + " " + root)
                })
            }
        }
        if (len > 2) {
            root = root + ubersetz(len)
        }
        root = ListSet.empty[String] ++
            root.map(_.toList.sorted.mkString).toList.sorted.reverse
        root
    }

    def getFinal2: List[BigInt] = {
        val result = byLength.map((z: (Int, ListSet[String])) => {
            z._1 match {
                case 1 => (1, BigInt(1))
                case 2 => if (len > 2) (2, BigInt(2)) else (2, BigInt(1))
                case _ => {
                    var byCountNot0 = z._2.groupBy(_.count(_ != '0'))
                    /*val y = byCountNot0.map(u => (u._1, u._2, u._2.map(_.permutations.toList.filter(_.toList.sliding(2).
                            toList.filter(_.count(_ != '0') > 1).isEmpty).toList.sorted)))*/
                    //val x = y.map(v => (len, z._1, v._1, v._2.toList.length,v._3.flatten.toList.length, guess(len, z._1, v._1), v._2,v._3)).toList.sortBy(_._3)
                    //val x = y.map(v => (len, z._1, v._1, v._2.toList.length,v._3.flatten.toList.length, guess(len, z._1, v._1),v._2)).toList.sortBy(_._3)
                    /*val x = y.map(v => (len, z._1, v._1, v._2.toList.length, v._3.flatten.toList.length, guess(len, z._1, v._1), v._2)).toList.sortBy(_._3)
                        myPrintDln(len, z._1, x.mkString("\n    ", "\n    ", "\n    "))
                        val u = x.dropWhile(t => {
                            val exp = t._5
                            val guessed = t._6
                            if (guessed != 0) {
                                exp == guessed
                            } else {
                                true
                            }
                        })
                        if (!u.isEmpty) {
                            printSum1p2p3pNHelper(20)
                            throw new Exception("" + (u.head._2, u.head._3) + " exp " +
                                u.head._5 + " vs act " + u.head._6)
                        }
                        val w = y.map(_._3).flatten.flatten.toList
                        (z._1, w.length)*/
                    val w = byCountNot0.map(y => BigInt(guess(len, z._1, y._1)))
                    (z._1, w.sum)
                }
            }
        })
        myPrintDln(result)
        result.map(_._2)
    }

    def guess(len: Int, lenString: Int, numChar: Int): Int = {
        numChar match {
            case 0 => 1
            case 1 => lenString
            case 2 => (len - (lenString + 3)) * sum1p2p3pN(lenString - 2)
            case 3 => if (len == (lenString + 5)) {
                0
            } else {
                guess(len - 1, lenString, numChar) + ((len - (lenString + 5)) *
                    (func3(lenString)))
            }
            case _ => sumXSum1p2p3pN(numChar - 2, len - (lenString + numChar + 2)) * sumXSum1p2p3pN(numChar - 1, lenString - numChar)
        }
    }

    def func3(lenString: Int): Int = {
        if (lenString == 5) {
            1
        } else {
            func3(lenString - 1) + sum1p2p3pN(lenString - 4)
        }
    }

    def sum1p2p3pN(n: Int): Int = (n * (n + 1)) / 2
    def sumXSum1p2p3pN(x: Int, n: Int): Int = {
        if (x == 1) {
            (n * (n + 1)) / 2
        } else {
            (1 until n).map(sumXSum1p2p3pN(x - 1, _)).sum
        }
    }

    def printSum1p2p3pNHelper(n: Int) {
        myPrintIt((1 until n).map(sum1p2p3pN(_)))
    }

    def ubersetz(i: Int) = vubersetz.substring(i, i + 1)
    def findValue(s: String) = vubersetz.indexOf(s.head)
    final val vubersetz =
        "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
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
