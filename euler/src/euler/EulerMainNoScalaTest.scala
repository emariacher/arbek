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
    val premiers = (new CheckEulerPrime(20000000, 1000)).premiers
    myPrintIt(1000000000, new EulerDiv2(1000000000, premiers).primes)

    // 5169354 (10000) dernier 1378001
    // List(11, 17, 41, 73, 101, 137, 251, 257, 271, 353, 401, 449, 641, 751, 1201, 1409, 1601, 3541, 4001, 5051, 9091, 10753, 15361, 16001, 19841, 21001, 21401, 24001, 25601, 27961, 60101, 69857, 76001, 160001, 162251, 453377, 670001, 952001, 976193, 1378001)
    // 1822662 (100000) dernier 544001
    doZeJob1(10)
    /*doZeJob2(2, 11)
    doZeJob2(5, 6)
    doZeJob2(10, 5)*/
    var z = ListSet[BigInt]()
    z = z ++ doZeJob3(2, 10)
    MyLog.waiting(1 second)
    z = z ++ doZeJob3(5, 5)
    MyLog.waiting(1 second)
    z = z ++ doZeJob3(10, 5)
    val y = z.toList.sorted
    myErrPrintln(z)
    myErrPrintln(y, y.length)
    myErrPrintln(y.take(40))
    myErrPrintln(y.take(40).sum)

    def doZeJob1(n: Int) = {
        val bi = BigInt((1 until n + 1).map(z => "1").mkString)
        val primes = new EulerDiv2(bi, premiers).primes
        val justChecking = primes.product
        myPrintln("\n\n\n", n, primes, primes.length, primes.sum,
            justChecking.toString.toList.length, bi / justChecking, "\n\n\n")
        primes
    }
    def doZeJob2(n: Int, limit: Int) = {
        (2 until limit).toList.map(i => doZeJob1(Math.pow(n, i).toInt))
    }
    def doZeJob3(n: Int, limit: Int) = {
        doZeJob1(Math.pow(n, limit).toInt)
    }
}

