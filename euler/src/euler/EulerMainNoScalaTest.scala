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

object EulerMainNoScalaTest extends App {
    myPrintDln("Hello World!")
    new Euler191
}

class Euler191 {
    myPrintIt("AAOOO".permutations.toList)
    myPrintIt("AAOOL".permutations.toList)
    myPrintIt("AOOOL".permutations.toList)
    val l191_1 = 3 until 6 map (new doZeJob191_1(_))
    myErrPrintDln(l191_1.mkString("\n  ", "\n  ", "\n  "))
    /*3 until 10 map (checkJob)

    def checkJob(l: Int) = myAssert2(doZeJob1(l), doZeJob2(l))*/

    class doZeJob191_1(l: Int) {
        val root1 = (0 to l).toList.map((i: Int) => "AOL").mkString("")
        val comb = root1.combinations(l).toList
        if (l < 6) {
            printIt((l, comb.toList.length, comb))
        } else {
            printIt((l, comb.toList.length))
        }
        val no2L = ListSet[String]() ++ comb.filter((s: String) => s.count(_ == 'L') < 2)
        val no2Lp = no2L.map(_.permutations).flatten
        if (l < 6) {
            printIt((l, no2L.toList.length, no2L))
        } else {
            printIt((l, no2L.toList.length))
        }
        val ou3A = no2L.partition((s: String) => s.count(_ == 'A') < 3)
        if (l < 6) {
            printIt((l, ou3A ))
        } 
        //val noAAA = ListSet[String]() ++ no2Lp.filter((s: String) => s.indexOf("AAA") < 0)
        val noAAA = ListSet[String]() ++ ou3A._2.map(_.permutations).flatten.filter((s: String) => s.indexOf("AAA") < 0)
        if (l < 6) {
            printIt((l, noAAA.toList.length, noAAA))
        } else {
            printIt((l, noAAA.toList.length))
        }
        val no3A = ou3A._1.map(_.permutations).flatten
         if (l < 6) {
            printIt((l, no3A.toList.length, no3A))
        } else {
            printIt((l, no3A.toList.length))
        }
        myErrPrintDln((1, l, noAAA.toList.length + no3A.toList.length))
        override def toString = "| %02d | %06d || %06d | %06d || %06d | %06d | ".format(l, comb.toList.length,
            no2L.toList.length, no2L.toList.length * 2,
            noAAA.toList.length, noAAA.toList.length * 2)
    }

    def doZeJob2(l: Int): BigInt = {
        var result = BigInt(0)
        if (l == 3) {
            result = 19
        } else {
            result = (doZeJob2(l - 1) * 2) + l + l - 2
        }
        myErrPrintDln((2, l, result))
        result
    }
}