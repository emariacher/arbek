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
    new DoZeJob1(9)

    class DoZeJob1(len: Int) {
        val lcheck = Map(1 -> 1, 2 -> 1, 3 -> 2, 4 -> 4, 5 -> 7, 6 -> 11, 7 -> 17, 8 -> 27, 9 -> 0)
        val s1 = (1 to len).map(i => "A").toList.mkString
        val root: ListSet[String] = getRoot2
        myPrintDln(len, root)

        val byCountNotA = root.groupBy(z => z.count(_ != 'A'))
        myPrintDln(byCountNotA)
        val zfinal = getFinal1
        val result = getFinal1.sum
        myErrPrintDln(len, result)
        myAssert2(result, lcheck.getOrElse(len, 0))

        def getRoot2 = {
            var root = ListSet.empty[String]
            if (len == 1) {
                root = ListSet("A")
            } else {
                val fromPrevRoot = new DoZeJob1(len - 1).root.map(s => s + "A")
                root = fromPrevRoot ++ fromPrevRoot.map(_.replaceFirst("AAA", "3"))
            }
            if (len > 2) {
                root = root + len.toString
            }
            root
        }

        def getFinal1 = {
            byCountNotA.toList.map((z: (Int, ListSet[String])) => {
                z._1 match {
                    case 0 => 1
                    case 1 => {
                        val y = z._2.toList.map(permLength)
                        myPrintDln(z._1, y)
                        y.sum.toInt
                    }
                    case 2 => {
                        val y = z._2.map(_.permutations.toList).flatten
                        val x = y.filter(_.toList.sliding(2).toList.filter(_.count(_ != 'A') > 1).isEmpty).toList
                        myPrintDln(z._1, y, x)
                        x.length
                    }
                    case _ => 0
                }
            })
        }
    }
}

