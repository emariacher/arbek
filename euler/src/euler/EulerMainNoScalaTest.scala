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
import Permutations._

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
            println("\n" + ex.getStackTrace.mkString("\n  ", "\n  ", "\n  ") + "\n")
        }
    } finally {
        println("\nHere!")
        MyLog.closeFiles
        println("\nThere!")
    }
}

class Euler114 {
    new DoZeJob1(10)

    class DoZeJob1(len: Int) {
        val lcheck = Map(1 -> 1, 2 -> 1, 3 -> 2, 4 -> 4, 5 -> 7, 6 -> 11, 7 -> 17, 8 -> 27, 9 -> 43, 10 -> 67, 11 -> 102)
        val s1 = (1 to len).map(i => "0").toList.mkString
        val root: ListSet[String] = getRoot2
        myErrPrintDln(len, root)

        val byLength = root.filter(s => (s.count(_ != '0') * 2) <= (s.length + 1)).groupBy(_.length)
        myPrintDln(byLength.mkString("\n  ", "\n  ", "\n  "))
        val zfinal = getFinal2
        val result = zfinal.sum
        myErrPrintDln(len, result)
        if (lcheck.getOrElse(len, 0) != 0) {
            myAssert2(result, lcheck.getOrElse(len, 0))
        }

        def getRoot2 = {
            var root = ListSet.empty[String]
            if (len == 1) {
                root = ListSet("0")
            } else {
                val fromPrevRoot = new DoZeJob1(len - 1).root.map(s => s + "0")
                printIt(len, fromPrevRoot)
                root = fromPrevRoot
                if (len > 4) {
                    (3 to len - 1).map(i => {
                        printIt(i, ubersetz(i))
                        val s = (1 to i).map(c => "0").mkString
                        root = root ++ fromPrevRoot.map(_.replaceFirst(s, ubersetz(i)))
                        myPrintDln("  " + s + " " + root)
                    })
                }
            }
            if (len > 2) {
                root = root + ubersetz(len)
            }
            root
        }

        def getFinal2 = {
            val result = byLength.map((z: (Int, ListSet[String])) => {
                z._1 match {
                    case 1 => (1,1)
                    case 2 =>  if(len>2) (2,2) else (2,1)
                    case _ => {
                        var byCountNot0 = z._2.groupBy(_.count(_ != '0'))
                        myPrintDln(len, z._1, byCountNot0.mkString("\n    ", "\n    ", "\n    "))
                        
                        val y = byCountNot0.map(_._2.map(_.permutations.toList.filter(_.toList.sliding(2).toList.filter(_.count(_ != '0') > 1).isEmpty).toList.sorted))
                        val x = y.map(v => (v.flatten.toList.length,v))
                        myPrintDln(len, z._1, x.mkString("\n    ", "\n    ", "\n    "))
                        val w = y.flatten.flatten.toList
                        /*val y = z._2.map(_.permutations.toList).flatten
                        val x = y.filter(_.toList.sliding(2).toList.filter(_.count(_ != '0') > 1).isEmpty).toList.sorted*/
                        (z._1,w.length)
                    }
                }
            })
            myPrintDln(result.mkString("\n  ", "\n  ", "\n  "))
            result.map(_._2)
        }

        def getFinal1 = {
            byLength.map((z: (Int, ListSet[String])) => {
                z._1 match {
                    case 0 => 1
                    case 1 => {
                        val y = z._2.toList.map(_.length)
                        myPrintln("count0[" + z._1 + "] ", y.length, y)
                        y.sum.toInt
                    }
                    case _ => {
                        val y = z._2.map(_.permutations.toList).flatten
                        val x = y.filter(_.toList.sliding(2).toList.filter(_.count(_ != '0') > 1).isEmpty).toList.sorted
                        z._1 match {
                            case 2 => {
                                //myPrintDln("y count0[" + z._1 + "] ", y.toList.length, y.toList.sorted)
                                //val w = x.groupBy(_.toList.map(_.toInt).sum).map(c => (c._1, c._2.head, c._2.length)).toList.sortBy(_._3)
                                val w = x.groupBy(_.length).map(c => (c._2.length, c._2.head.length, c._2)).toList.sortBy(_._1)
                                myPrintDln("x count0[" + z._1 + "] ", x.length, w.mkString("\n  ", "\n  ", "\n  "))
                                //myPrintDln("x count0[" + z._1 + "] ", x.length)
                            }
                            case 3 => {
                                val w = x.groupBy(_.length).map(c => (c._2.head.length, c._2.length)).toList.sortBy(_._2)
                                myPrintDln("x count0[" + z._1 + "] ", x.length, w.mkString("\n  ", "\n  ", "\n  "))
                                val rcount3 = w.map(c => {
                                    val v = xCount0_3(c._1)
                                    //myAssert2(v, c._2)
                                    v
                                }).sum

                            }
                            case _ => {
                                //myPrintDln("y count0[" + z._1 + "] ", y.toList.length, y.toList.sorted)
                                //val w = x.groupBy(_.toList.map(_.toInt).sum).map(c => (c._1, c._2.head, c._2.length)).toList.sortBy(_._3)
                                val w = x.groupBy(_.length).map(c => (c._2.length, c._2.head.length, c._2)).toList.sortBy(_._1)
                                myPrintDln("x count0[" + z._1 + "] ", x.length, w.mkString("\n  ", "\n  ", "\n  "))
                                //myPrintDln("x count0[" + z._1 + "] ", x.length)
                            }
                        }

                        x.length
                    }
                }
            })
        }

        def xCount0_2 = {
            myPrintDln(__FUNC__)
            (7 to len).map(i => (i - 6) * (i - 6)).sum
        }
        def xCount0_3(slen: Int) = {
            var lres = 0
            if ((len > 10) & (slen == 5)) {
                lres += 3
            } else {
                lres += 1
            }
            if ((len > 12) & (slen == 7)) {
                lres += 30
            } else {
                lres += 10
            }
            if ((len > 13) & (slen == 8)) {
                lres += 60
            } else {
                lres += 20
            }
            if ((len > 14) & (slen == 9)) {
                lres += 105
            } else {
                lres += 35
            }
            if ((len > 15) & (slen == 10)) {
                lres += 168
            } else {
                lres += 56
            }
            myPrintDln(__FUNC__, len, slen, lres)
            lres
        }

        def ubersetz(i: Int) = vubersetz.substring(i, i + 1)
        def findValue(s: String) = vubersetz.indexOf(s.head)
        final val vubersetz = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    }
}

