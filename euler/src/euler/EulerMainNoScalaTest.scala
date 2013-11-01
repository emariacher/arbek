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
import Permutations._
import akka.actor._
import akka.routing.RoundRobinRouter
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object EulerMainNoScalaTest extends App {
    MyLog.newMyLog("EulerMainNoScalaTest", new File("log"), "log")
    try {
        var t_start = Calendar.getInstance
        myPrintDln("Hello World!")

        new Euler241

        timeStamp(t_start, "Au revoir Monde!")
    } catch {
        case ex: Exception => {
            println("\n" + ex)
            println("\n" + ex.getStackTrace.mkString("\n  ", "\n  ", "\n  ") + "\n")
        }
    } finally {
        println("\nHere!")
        MyLog.getMylog.closeFiles
        println("\nThere!")
    }
}

class Euler241 {
    val lj = List(
        (List(BigInt(2)), List(BigInt(100000)), List[BigInt](2)),
        (List(BigInt(4320)), List(BigInt(10000000)), List[BigInt](8, 9)),
        (List(BigInt(26208)), List(BigInt(1000000), BigInt(1000)), List[BigInt](32, 9, 13)),
        (List(BigInt(8910720)), List(BigInt(1000000), BigInt(10000)), List[BigInt](32, 9, 13, 35)),
        (List(BigInt(0)), List(BigInt(1000000), BigInt(1000)), List[BigInt](32, 9, 11))
        //(List(BigInt(8910720)), List(BigInt(10000000), BigInt(10000)), List[BigInt](128, 9, 13, 35)) 
        //(List[BigInt](0), List(BigInt(1000000000), BigInt(10000)), List[BigInt](1024, 9, 13, 35),(List(BigInt(0)), List(BigInt(1000000000), BigInt(10000)), List[BigInt](1024, 9, 11, 35))
        )

    //[8910720,List(2, 2, 2, 2, 2, 2, 2, 3, 3, 5, 7, 13, 17),4.5]
    //[8583644160,List(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 5, 7, 13, 23, 89),4.5]
    val result = ListSet[DoZeJob]() ++ lj.map(t => loop2(t._1, t._2, t._3))
    myErrPrintln(result.mkString("\n"))

    class DoZeJob(val bi: BigInt) {
        val div = new EulerDiv241(bi)
        val sbi = sigma(bi, div)
        val pfbi = perfquot(bi, sbi)
        val spfbi = pfbi.toString
        val kp5 = spfbi.substring(spfbi.length - 2) == ".5"
        //myPrintln(bi, sbi, pfbi, kp5)

        def toString2 = "[" + bi + "," + div.primes + "," + (new EulerDivisors(div.primes).getFullDivisors).sorted + "," + pfbi + "]"
        override def toString = "[" + bi + "," + div.primes + "," + pfbi + "]"
        override def equals(a: Any) = bi == a.asInstanceOf[DoZeJob].bi
        override def hashCode = bi.toInt
    }

    def loop(start: List[BigInt], end: List[BigInt], inc: List[BigInt]) = {
        var result = ListSet[DoZeJob]()
        var t_start1 = Calendar.getInstance
        printIt(start, end.mkString("*"), inc.mkString("*"))
        var count = start.product
        var count2 = 0
        val end1 = end.product
        val inc1 = inc.product
        while (count < end1) {
            val dzj = new DoZeJob(count)
            if (dzj.kp5) {
                myPrintln("\n    " + dzj)
                result = result + dzj
            }
            if (count2 % 1000 == 99) {
                print(".")
            }

            count += inc1
            count2 += 1
        }
        timeStamp(t_start1, (start.mkString("*"), end.mkString("*"), inc.mkString("*")).toString)
        myErrPrintDln((start.mkString("*"), end.mkString("*"), inc.mkString("*")).toString, result.toList.sortBy(_.bi).mkString("\n", "\n", "\n"))
        result
    }

    def loop2(start: List[BigInt], end: List[BigInt], inc: List[BigInt]) = {
        var t_start1 = Calendar.getInstance
        val size = (((end.product - start.product) / inc.product) + 1).toInt

        val inc1 = inc.product
        printIt(start, end.mkString("*"), inc.mkString("*"), size)

        val result = (0 to size).toList.par.map(BigInt(_)).map(bi => {
            if (bi % 10000 == 99) {
                print(".")
            }
            new DoZeJob(bi * inc1)
        }).filter(_.kp5)

        myErrPrintDln(((start.mkString("*"), end.mkString("*"), inc.mkString("*")).toString, size, result.toList.sortBy(_.bi).mkString("\n", "\n", "\n")))
        timeStamp(t_start1, (start.mkString("*"), end.mkString("*"), inc.mkString("*"), size).toString)
        result
    }

    def sigma(bi: BigInt) = (new EulerDivisors(new EulerDiv241(bi).primes).getFullDivisors).sum
    def sigma(bi: BigInt, ed: EulerDiv241) = (new EulerDivisors(ed.primes).getFullDivisors).sum
    def perfquot(bi: BigInt) = sigma(bi).toDouble / bi.toDouble
    def perfquot(bi: BigInt, sbi: BigInt) = sbi.toDouble / bi.toDouble
}

