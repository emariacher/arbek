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
    myAssert2(new DoZeJob1(1).result, 1)
    myAssert2(new DoZeJob1(2).result, 1)
    myAssert2(new DoZeJob1(3).result, 2)
    myAssert2(new DoZeJob1(4).result, 4)
    myAssert2(new DoZeJob1(5).result, 7)
    myAssert2(new DoZeJob1(6).result, 11)
    myAssert2(new DoZeJob1(7).result, 17)
    myAssert2(new DoZeJob1(8).result, 27) // 4A3 & 3A4
    myAssert2(new DoZeJob1(9).result, 25)

    class DoZeJob1(len: Int) {
        myPrintDln("* " + len + " *****************************************************")
        val s1 = (1 to len).map(i => "A").toList.mkString
        val root = getRoot1
        myPrintDln(len, root)

        val byCountNotA = root.groupBy(z => z.count(_ != 'A'))
        myPrintDln(byCountNotA)
        val zfinal = getFinal1
        val result = getFinal1.sum
        myErrPrintDln(len, result)

        def getRoot1 = {
            ListSet.empty[String] ++ (3 to len).map(z => {
                val s2 = (1 to z).map(i => "A").toList.mkString
                s1.replaceAll(s2, z.toString)
            }) ++ (3 to (len - 3)).map(z => {
                val s2 = (1 to z).map(i => "A").toList.mkString
                s1.replaceFirst(s2, z.toString)
            }) + s1
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

