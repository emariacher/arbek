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
    myErrPrintDln((2 until 1000000 by 2).map(new DoZeJob(_)).filter(_.kp5).mkString("\n"))
    myErrPrintDln((4320 until 10000000 by (24*9)).map(new DoZeJob(_)).filter(_.kp5).mkString("\n"))
    myErrPrintDln((4680 until 100000000 by (72*13)).map(new DoZeJob(_)).filter(_.kp5).mkString("\n"))

    class DoZeJob(bi: BigInt) {
        val div = new EulerDiv(bi)
        val sbi = sigma(bi, div)
        val pfbi = perfquot(bi, sbi)
        val spfbi = pfbi.toString
        val kp5 = spfbi.substring(spfbi.length - 2) == ".5"
        //myPrintln(bi, sbi, pfbi, kp5)

        def toString2 = "[" + bi + "," + div.primes + "," + (new EulerDivisors(div).getFullDivisors).sorted + "," + pfbi + "]"
        override def toString = "[" + bi + "," + div.primes + "," + pfbi + "]"
    }

    def sigma(bi: BigInt) = (new EulerDivisors(new EulerDiv(bi)).getFullDivisors).sum
    def sigma(bi: BigInt, ed: EulerDiv) = (new EulerDivisors(ed).getFullDivisors).sum
    def perfquot(bi: BigInt) = sigma(bi).toDouble / bi.toDouble
    def perfquot(bi: BigInt, sbi: BigInt) = sbi.toDouble / bi.toDouble
}

