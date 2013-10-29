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
    myPrintDln("Hello World!")
    new Euler191
}

class Euler191 {
    val l191_1 = 3 until 14 map (i => myAssert2(new doZeJob191_1(i).result, new doZeJob191_2(i).result))
    //myErrPrintDln(l191_1.mkString("\n  ", "\n  ", "\n  "))

    class doZeJob191_2(l: Int) {
        var t_start = Calendar.getInstance
        myPrintDln("*2* " + l + " ***********************************************************")
        var result = BigInt(0)
        val root1 = (0 to l).toList.map((i: Int) => "AOL").mkString("")
        val comb = root1.combinations(l).toList
        if (l < 6) {
            myPrintDln((l, comb.toList.length, comb))
        } else {
            myPrintIt((l, comb.toList.length))
        }
        val no2L = ListSet[String]() ++ comb.filter((s: String) => s.count(_ == 'L') < 2)

        val ou3A = no2L.partition((s: String) => s.count(_ == 'A') < 3)
        if (l < 6) {
            myPrintIt((l, ou3A))
        }

        result += ou3A._1.toList.map(permLength).sum

        val noAAA = ListSet[String]() ++ ou3A._2.map(_.permutations).flatten.filter((s: String) => s.indexOf("AAA") < 0)
        if (l < 6) {
            myPrintDln((l, noAAA.toList.length, noAAA))
        } else {
            myPrintIt((l, noAAA.toList.length))
        }

        result += noAAA.toList.length
        myPrintDln((2, l, result, timeStamp(t_start, this.getClass.getName).getTimeInMillis - t_start.getTimeInMillis()))
    }

    class doZeJob191_1(l: Int) {
        var t_start = Calendar.getInstance
        myPrintDln("*1* " + l + " ***********************************************************")
        val root1 = (0 to l).toList.map((i: Int) => "AOL").mkString("")
        val comb = root1.combinations(l).toList
        if (l < 6) {
            myPrintDln((l, comb.toList.length, comb))
        } else {
            myPrintIt((l, comb.toList.length))
        }
        val no2L = ListSet[String]() ++ comb.filter((s: String) => s.count(_ == 'L') < 2)
        val no2Lp = no2L.map(_.permutations).flatten
        if (l < 6) {
            myPrintDln((l, no2L.toList.length, no2L))
        } else {
            myPrintIt((l, no2L.toList.length))
        }
        val ou3A = no2L.partition((s: String) => s.count(_ == 'A') < 3)
        if (l < 6) {
            myPrintDln((l, ou3A))
        }
        //val noAAA = ListSet[String]() ++ no2Lp.filter((s: String) => s.indexOf("AAA") < 0)
        val noAAA = ListSet[String]() ++ ou3A._2.map(_.permutations).flatten.filter((s: String) => s.indexOf("AAA") < 0)
        if (l < 6) {
            myPrintDln((l, noAAA.toList.length, noAAA))
        } else {
            myPrintIt((l, noAAA.toList.length))
        }
        val no3A = ou3A._1.map(_.permutations).flatten
        if (l < 6) {
            myPrintDln((l, no3A.toList.length, no3A))
        } else {
            myPrintIt((l, no3A.toList.length))
        }

        var result = noAAA.toList.length + no3A.toList.length
        myPrintDln((1, l, result, timeStamp(t_start, this.getClass.getName).getTimeInMillis - t_start.getTimeInMillis()))

        override def toString = "| %02d | %06d || %06d | %06d || %06d | %06d | ".format(l, comb.toList.length,
            no2L.toList.length, no2L.toList.length * 2,
            noAAA.toList.length, noAAA.toList.length * 2)

    }
}