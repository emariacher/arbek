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

        timeStampIt(new Euler265)
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

class Euler265 {

    doZeStuff("00010111", 3)
    doZeStuff("00011101", 3)
    doZeStuff("01010101", 3)
    generatePotentialSolutions(3)
    generatePotentialSolutions2(3)
    generatePotentialSolutions(4)
    generatePotentialSolutions2(4)

    def combperm(s: String, length: Int) = s.combinations(length).toList.map(_.permutations.toList)
    def rotations(s: String, length: Int) = {
        val sz = s + s.substring(0, length - 1)
        (0 to sz.length - length).map(i => sz.substring(i, i + length)).toList
    }

    def doZeStuff(s: String, length: Int) = {
        val sz = (1 to length).map(z => "01").mkString
        val cp = combperm(sz, length)
        val cpf = cp.flatten.sorted
        myPrintln(s, "\n  " + cpf)
        val r = rotations(s, length)
        val result = (r.intersect(cpf).sorted == cpf, r.length == cpf.length)
        myPrintln("\n  " + r, "\n  " + r.sorted, "\n  " + (ListSet.empty[String] ++ r.sorted), "\n  " + s, result)
        result
    }

    def generatePotentialSolutions2(length: Int) {
        val tl = Math.pow(2, length).toInt
        val root = (1 to ((tl / 2) - length)).map(z => "01").mkString
        val head0 = (1 to length).map(z => "0").mkString
        val head1 = (1 to length).map(z => "1").mkString
        myPrintln(root, head0, head1)
        val perms = root.permutations.toList.partition(z => z.head == '0')
        val z0 = perms._1.map(z => head0 + head1 + z)
        val z1 = perms._2.map(z => head0 + z + head1)
        val result = (z0 ++ z1).map(z => Integer.parseInt(z, 2))
        myPrintln(z0,z1)
        myPrintln(length, perms, "\n" + result, "\n" + result.sum)
    }

    def generatePotentialSolutions(length: Int) {
        val tl = Math.pow(2, length).toInt
        val root = ("u" ++ (1 to ((tl / 2) - length)).map(z => "01")).mkString
        val head0 = (1 to length).map(z => "0").mkString
        val head1 = (1 to length).map(z => "1").mkString
        myPrintln(root)
        val perms = root.permutations.toList.filter(z => z.indexOf("u1") < 0 && z.indexOf("1u") < 0 && z.indexOf(head1) < 0).
            map(z => head0 + z.replaceAll("u", head1))
        val result = perms.map(z => Integer.parseInt(z, 2))
        myPrintln(length, perms, "\n" + result, "\n" + result.sum)
    }
}