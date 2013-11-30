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
import _root_.JFplot._
import _root_.JFplot.jFigure._
import java.awt.Color

object EulerMainNoScalaTest extends App {
    //MyLog.newMyLog("EulerMainNoScalaTest", new File("log"), "log")
    try {
        myPrintDln("Hello World!")

        timeStampIt(new Euler35)
    } catch {
        case ex: Exception => {
            println("\n" + ex)
            println("\n" + ex.getStackTrace.mkString("\n  ", "\n  ", "\n  ") +
                "\n")
            printTimeStampsList
        }
    } finally {
        println("\nHere!")
        printTimeStampsList
        MyLog.closeFiles
        println("\nThere!")
    }
}

class Euler35 {
    val premiers = new CheckEulerPrime(BigInt(1000000), 1000).premiers.filter(p => p.toString.toList.intersect("02468".toList).isEmpty).toList.map(_.toInt)
    //myPrintln(premiers.filter(isCircular(_)))
    myPrintln((ListSet(2) ++ premiers.map(isCircular(_)).flatten).toList.length, 55)
    def isCircular(p: Int) = {
        val rot = rotations(p)
        if (rot.intersect(premiers) == rot) {
            myPrintln(rot)
            rot
        } else {
            List.empty[Int]
        }
    }
    def rotations(p: Int) = {
        var s = p.toString()
        var out = ListSet(p)
        (1 to s.length).map(i => {
            s = s.substring(1)+s.head
            out = out + s.toInt
        })
        out.toList
    }
}
