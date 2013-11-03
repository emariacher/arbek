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
    new DoZeJob1(11)

    class DoZeJob1(len: Int) {
        val lcheck = Map(1 -> 1, 2 -> 1, 3 -> 2, 4 -> 4, 5 -> 7, 6 -> 11, 7 -> 17, 8 -> 27, 9 -> 43, 10 -> 67)
        val s1 = (1 to len).map(i => "0").toList.mkString
        val root: ListSet[String] = getRoot2
        myPrintDln(len, root)

        val byCountNot0 = root.groupBy(z => z.count(_ != '0'))
        myPrintDln(byCountNot0.mkString("\n"))
        val zfinal = getFinal1
        val result = getFinal1.sum
        myErrPrintDln(len, result)
        myAssert2(result, lcheck.getOrElse(len, 0))

        def getRoot2 = {
            var root = ListSet.empty[String]
            if (len == 1) {
                root = ListSet("0")
            } else {
                val fromPrevRoot = new DoZeJob1(len - 1).root.map(s => s + "0")
                root = fromPrevRoot ++ fromPrevRoot.map(_.replaceFirst("000", "3"))
            }
            if (len > 2) {
                root = root + ubersetz(len)
            }
            root
        }

        def getFinal1 = {
            byCountNot0.toList.map((z: (Int, ListSet[String])) => {
                z._1 match {
                    case 0 => 1
                    case 1 => {
                        val y = z._2.toList.map(permLength)
                        myPrintln("count0["+z._1+"] ", y)
                        y.sum.toInt
                    }
                    case _ => {
                        val y = z._2.map(_.permutations.toList).flatten
                        val x = y.filter(_.toList.sliding(2).toList.filter(_.count(_ != '0') > 1).isEmpty).toList.sorted
                        //myPrintIt("count0["+z._1+"] ", y.toList.sorted)
                        myPrintIt("count0["+z._1+"] ", x.toList)
                        x.length
                    }
                }
            })
        }

        def ubersetz(i: Int) = vubersetz.substring(i, i + 1)
        def findValue(s: String) = vubersetz.indexOf(s.head)
        final val vubersetz = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    }
}

