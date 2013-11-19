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
import euler.EulerFactorielle
//import Factorielles._

object EulerMainNoScalaTest extends App {
    //MyLog.newMyLog("EulerMainNoScalaTest", new File("log"), "log")
    try {
        var t_start = Calendar.getInstance
        myPrintDln("Hello World!")

        new Euler132

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

class Euler132 {
    val premiers = (new CheckEulerPrime(200000, 1000)).premiers
    //myPrintIt(1000000000, new EulerDiv132(1000000000, premiers, 5).primes)

    // 5169354 (10000) dernier 1378001
    // List(11, 17, 41, 73, 101, 137, 251, 257, 271, 353, 401, 449, 641, 751, 1201, 1409, 1601, 3541, 4001, 5051, 9091, 10753, 15361, 16001, 19841, 21001, 21401, 24001, 25601, 27961, 60101, 69857, 76001, 160001, 162251, 453377, 670001, 952001, 976193, 1378001)

    // 1822662 (100000) dernier 544001
    // 4003610  (1000000) dernier 976193
    // List(11, 17, 41, 73, 101, 137, 251, 271, 353, 401, 449, 641, 751, 1201, 1409, 1601, 3541, 4001, 4801, 5051, 9091, 16001, 19841, 21001, 21401, 24001, 25601, 27961, 37501, 43201, 60101, 62501, 69857, 76001, 160001, 162251, 544001, 670001, 952001, 976193)
    // List(11, 17, 41, 73, 101, 137, 251, 271, 353, 401, 449, 641, 751, 1201, 1409, 1601, 3541, 4001, 4801, 5051, 9091, 16001, 19841, 21001, 21401, 24001, 25601, 27961, 37501, 43201, 60101, 62501, 69857, 76001, 160001, 162251, 544001, 670001, 952001, 976193, 980801),41)
    // 2millions
    // List(11, 17, 41, 73, 101, 137, 251, 271, 353, 401, 449, 641, 751, 1201, 1409, 1601, 3541, 4001, 4801, 5051, 9091, 16001, 19841, 21001, 21401, 24001, 25601, 27961, 37501, 43201, 60101, 62501, 69857, 76001, 76801, 160001, 162251, 524801, 544001, 670001, 952001),41)
    // 5millions
    // List(11, 17, 41, 73, 101, 137, 251, 271, 353, 401, 449, 641, 751, 1201, 1409, 1601, 3541, 4001, 4801, 5051, 9091, 16001, 19841, 21001, 21401, 24001, 25601, 27961, 37501, 43201, 60101, 62501, 69857, 76001, 160001, 162251, 544001, 670001, 952001, 976193, 980801),41)
    // 4millions
    // List(11, 17, 41, 73, 101, 137, 251, 257, 271, 353, 401, 449, 641, 751, 1201, 1409, 1601, 3541, 4001, 4801, 5051, 9091, 15361, 16001, 19841, 21001, 21401, 24001, 25601, 27961, 37501, 43201, 60101, 62501, 69857, 76001, 76801, 160001, 162251, 453377, 524801),41)
    // 8millions
    // List(11, 17, 41, 73, 101, 137, 251, 257, 271, 353, 401, 449, 641, 751, 1201, 1409, 1601, 3541, 4001, 4801, 5051, 9091, 10753, 15361, 16001, 19841, 21001, 21401, 24001, 25601, 27961, 37501, 40961, 43201, 60101, 62501, 69857, 76001, 76801, 160001, 162251),41)
    // 10millions
    // List(11, 17, 41, 73, 101, 137, 251, 257, 271, 353, 401, 449, 641, 751, 1201, 1409, 1601, 3541, 4001, 4801, 5051, 9091, 10753, 15361, 16001, 19841, 21001, 21401, 24001, 25601, 27961, 37501, 43201, 60101, 62501, 69857, 76001, 76801, 160001, 162251),40)

    val list8M = List(11, 17, 41, 73, 101, 137, 251, 257, 271, 353, 401, 449, 641, 751, 1201, 1409, 1601, 3541, 4001, 4801, 5051, 9091, 10753, 15361, 16001, 19841, 21001, 21401, 24001, 25601, 27961, 37501, 40961, 43201, 60101, 62501, 69857, 76001, 76801, 160001, 162251)

    val list10M = List(11, 17, 41, 73, 101, 137, 251, 257, 271, 353, 401, 449, 641, 751, 1201, 1409, 1601, 3541, 4001, 4801, 5051, 9091, 10753, 15361, 16001, 19841, 21001, 21401, 24001, 25601, 27961, 37501, 43201, 60101, 62501, 69857, 76001, 76801, 160001, 162251)

    val bothLists = ListSet.empty[Int] ++ list8M ++ list10M

    val result = bothLists.toList.sorted.take(40)

    myPrintln(result.sum, result)
    myAssert2(result.sum, 843296)

    //doZeJob6

    def doZeJob4(n: Int, primesI: List[BigInt]): List[BigInt] = {
        MyLog.waiting(1 second)
        myErrPrintln("doZeJob4 *********** " + n)
        MyLog.waiting(1 second)
        val bi = BigInt((1 until n + 1).map(z => "1").mkString)
        val bi2 = bi / primesI.product
        //val start = primesI.last
        val start = 1
        //myPrintIt("\n", n, start, bi2)
        val primes = new EulerDiv132(bi, premiers, start, 40).primes
        val justChecking = primes.product
        myPrintln("\n", n, primes.length, primes.sum, justChecking.toString.toList.length, "\n")
        val zprimes = primes.take(40)
        myPrintln("\n  zprimes: ", zprimes, zprimes.length)
        MyLog.waiting(1 second)
        myErrPrintln(n, primes.take(40).sum)
        MyLog.waiting(1 second)
        primes.sorted
    }
    def doZeJob6 = {
        var primes2 = ListSet.empty[BigInt]
        var primes = List[BigInt](1)
        List[Int](Math.pow(2, 9).toInt, Math.pow(5, 9).toInt, Math.pow(10, 6).toInt, Math.pow(10, 7).toInt).map(i => {
            primes = doZeJob4(i, primes)
            primes2 = primes2 ++ primes
            val zprimes2 = primes2.toList.sorted.take(40)
            MyLog.waiting(1 second)
            myErrPrintln("\n  zprimes2: ", zprimes2, zprimes2.length)
            MyLog.waiting(1 second)
        })
    }
}

