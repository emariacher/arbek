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
        myPrintDln("Hello World!")

        new testTimeStampMacro
        timeStampIt(new Euler347)

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

class Euler347

class testTimeStampMacro {
    timeStampIt(doZeJob1(200))
    timeStampIt(doZeJob2(100))
    timeStampIt(myPrintln(doZeJob1(150)))
    timeStampIt(doZeJob1(200))

    def doZeJob1(n: Int) {
        (1 until n).map(i => i * i)
    }

    def doZeJob2(n: Int) {
        (1 until 2 * n).map(i => myPrint("." + (i * i * i)))
        myPrintln("")
    }
}