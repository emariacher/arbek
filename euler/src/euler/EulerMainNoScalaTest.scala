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
import Permutations._

object EulerMainNoScalaTest extends App {
    var t_start = Calendar.getInstance
    myPrintDln("Hello World!")

    new Euler241

    timeStamp(t_start, "Au revoir Monde!")
}

class Euler241 {
    /*myErrPrintDln((2     until 100000 by 2).map(new DoZeJob(_)).filter(_.kp5).mkString("\n"))
    myErrPrintDln((4320  until 10000000 by (8*9)).map(new DoZeJob(_)).filter(_.kp5).mkString("\n"))
    myErrPrintDln((4680  until 100000000 by (8*9*13)).map(new DoZeJob(_)).filter(_.kp5).mkString("\n"))
    myErrPrintDln((BigInt(26208) until BigInt(1000000)* BigInt(1000) by BigInt(32*9*13)).map(new DoZeJob(_)).filter(_.kp5).mkString("\n"))
    myErrPrintDln((BigInt(8910720) until BigInt(1000000)* BigInt(10000) by BigInt(32*9*13*35)).map(new DoZeJob(_)).filter(_.kp5).mkString("\n"))
    myErrPrintDln((BigInt(0) until BigInt(1000000)* BigInt(10000) by BigInt(32*9*11*35)).map(new DoZeJob(_)).filter(_.kp5).mkString("\n"))*/

    var result = ListSet[DoZeJob]() ++ loop(List(BigInt(2)), List(BigInt(100000)), List(2))
    myErrPrintln(result)
    result = result ++ loop(List(BigInt(4320)), List(BigInt(10000000)), List(8, 9))
    myErrPrintln(result)
    result = result ++ loop(List(BigInt(26208)), List(BigInt(1000000), BigInt(1000)), List(32, 9, 13))
    myErrPrintln(result)
    result = result ++ loop(List(BigInt(8910720)), List(BigInt(1000000), BigInt(10000)), List(32, 9, 13, 35))
    myErrPrintln(result)
    result = result ++ loop(List(BigInt(0)), List(BigInt(1000000), BigInt(1000)), List(32, 9, 11))
    myErrPrintln(result)
    result = result ++ loop(List(BigInt(8910720)), List(BigInt(10000000), BigInt(10000)), List(128, 9, 13, 35))
    myErrPrintln(result)
    result = result ++ loop(List(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 5, 7, 13, 23), List(BigInt(1000000000), BigInt(10000)), List(1024, 9, 13, 35))
    myErrPrintln(result)
    result = result ++ loop(List(BigInt(0)), List(BigInt(1000000000), BigInt(10000)), List(1024, 9, 11, 35))
    myErrPrintln(result.toList.sortBy(_.bi).mkString("\n"))

    class DoZeJob(val bi: BigInt) {
        val div = new EulerDiv(bi)
        val sbi = sigma(bi, div)
        val pfbi = perfquot(bi, sbi)
        val spfbi = pfbi.toString
        val kp5 = spfbi.substring(spfbi.length - 2) == ".5"
        //myPrintln(bi, sbi, pfbi, kp5)

        def toString2 = "[" + bi + "," + div.primes + "," + (new EulerDivisors(div).getFullDivisors).sorted + "," + pfbi + "]"
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
            if (count2 % 100 == 99) {
                print(".")
            }

            count += inc1
            count2 += 1
        }
        timeStamp(t_start1, (start.mkString("*"), end.mkString("*"), inc.mkString("*")).toString)
        myErrPrintln(result.toList.sortBy(_.bi).mkString("\n"))
        result
    }

    def sigma(bi: BigInt) = (new EulerDivisors(new EulerDiv(bi)).getFullDivisors).sum
    def sigma(bi: BigInt, ed: EulerDiv) = (new EulerDivisors(ed).getFullDivisors).sum
    def perfquot(bi: BigInt) = sigma(bi).toDouble / bi.toDouble
    def perfquot(bi: BigInt, sbi: BigInt) = sbi.toDouble / bi.toDouble
}

