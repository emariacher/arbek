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

object EulerMainNoScalaTest extends App {
    //MyLog.newMyLog("EulerMainNoScalaTest", new File("log"), "log")
    try {
        var t_start = Calendar.getInstance
        myPrintDln("Hello World!")

        new Euler346
        //new Euler133

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

class Euler346 {

    myAssert2(doZejob(1000), 15864)
    
    def doZejob(limit: Int) = {
        val result = (1 until limit).map(n => (n, isRepUnit(n))).filter(_._2.length > 1)
        myPrintln("\n" + result.map(_._1).sum + " " + result)
        result.map(_._1).sum + 1
    }
    def isRepUnit(n: Int) = {
        myPrint(".")
        val z = (2 until n).filter(b => shiftrec(n, b)._1).toList
        if (z.length > 1) {
            myPrint("\n." + n + " " + z)
        }
        z
    }
    def shiftrec(n: Int, b: Int): (Boolean, Int) = {
        var shifted = shift(n, b)
        shifted match {
            case 0 => (false, 0)
            case 1 => (true, 1)
            case _ => if (shifted >= b) {
                shiftrec(shifted, b)
            } else {
                (false, shifted)
            }
        }
    }
    def shift(n: Int, b: Int) = {
        if ((n - 1) % b == 0) {
            (n - 1) / b
        } else {
            0
        }
    }
}

class Euler133 {
    // 615963 10Millions :(
    // 453688666 10Millions mais correct
    val premiers = EulerPrime.premiers100000

    val list2test = List[Int](Math.pow(10, 4).toInt, Math.pow(10, 5).toInt, Math.pow(10, 6).toInt, Math.pow(2, 23).toInt, Math.pow(5, 10).toInt, Math.pow(10, 7).toInt)

    doZeJob

    def doZeJob {
        val primes2 = doZeJob6

        val result = premiers.filter(!primes2.contains(_)).sum

        myPrintIt(result)
        myPrintIt(primes2)
        myPrintIt(premiers.filter(!primes2.contains(_)).take(40))
        myPrintDln("Job done!")
    }

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
        list2test.map(i => {
            primes = doZeJob4(i, primes)
            primes2 = primes2 ++ primes
            val zprimes2 = primes2.toList.sorted.take(40)
            MyLog.waiting(1 second)
            myErrPrintln("\n  zprimes2: ", zprimes2, zprimes2.length)
            MyLog.waiting(1 second)
        })
        primes2
    }

}
