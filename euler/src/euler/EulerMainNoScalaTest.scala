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
    myAssert2(new DoZeJob1(7).result, 17)

    class DoZeJob1(len: Int) {
        val s1 = (1 to len).map(i => "A").toList.mkString
        val l = ListSet.empty[String] ++ (3 to len).map(z => {
            val s2 = (1 to z).map(i => "A").toList.mkString
            s1.replaceAll(s2, z.toString)
        }) ++ (3 to (len - 3)).map(z => {
            val s2 = (1 to z).map(i => "A").toList.mkString
            s1.replaceFirst(s2, z.toString)
        }) + s1
        myPrintDln(len, l)

        val byCountNotA = l.groupBy(z => z.count(_ != 'A'))
        myPrintDln(byCountNotA)
        val result = byCountNotA.toList.map((z: (Int, ListSet[String])) => {
            z._1 match {
                case 0 => 1
                case 1 => z._2.map(permLength).sum.toInt
                case 2 => {
                    val y = z._2.map(_.permutations.toList).flatten
                    myPrintDln(y)
                    y.filter(_.toList.sliding(2).toList.filter(_.count(_ != 'A') > 1).isEmpty).toList.length
                }
                case _ => 0
            }
        }).sum
        myErrPrintDln(len, result)
    }
}

