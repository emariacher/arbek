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
//import Factorielles._

object EulerMainNoScalaTest extends App {
    //MyLog.newMyLog("EulerMainNoScalaTest", new File("log"), "log")
    try {
        var t_start = Calendar.getInstance
        myPrintDln("Hello World!")

        new Euler114

        timeStamp(t_start, "Au revoir Monde!")
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

class Euler114 {
    new DoZeJob1(27)

    class DoZeJob1(len: Int) {
        myAssert2(sum1p2p3pN(9), 45)
        val lcheck = Map(1 -> 1, 2 -> 1, 3 -> 2, 4 -> 4, 5 -> 7, 6 -> 11, 7 ->
            17, 8 -> 27, 9 -> 44, 10 -> 72, 11 -> 117)
        val s1 = (1 to len).map(i => "0").toList.mkString
        val root: ListSet[String] = getRoot2
        //myErrPrintDln(len, root)

        val byLength = root.filter(s => (s.count(_ != '0') * 2) <= (s.length +
            1)).groupBy(_.length).toList.sortBy(_._1)
        myPrintDln(byLength.mkString("\n  ", "\n  ", "\n  "))
        val zfinal = getFinal2
        val result = zfinal.sum
        myErrPrintDln(len, result)
        if (lcheck.getOrElse(len, 0) != 0) {
            myAssert2(result, lcheck.getOrElse(len, 0))
        }
        printSum1p2p3pNHelper(10)

        def getRoot2 = {
            var root = ListSet.empty[String]
            if (len == 1) {
                root = ListSet("0")
            } else {
                val fromPrevRoot = new DoZeJob1(len - 1).root.map(s => s + "0")
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

        def getFinal2 = {
            val result = byLength.map((z: (Int, ListSet[String])) => {
                z._1 match {
                    case 1 => (1, 1)
                    case 2 => if (len > 2) (2, 2) else (2, 1)
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
                        val w = byCountNot0.map(y => guess(len, z._1, y._1))
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
}
