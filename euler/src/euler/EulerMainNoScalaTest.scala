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
    doZeJob(3)
    doZeJob(4)
    doZeJob(5)
    doZeJob(6)
    doZeJob(7)
    doZeJob(8)
    doZeJob(9)
    doZeJob(10)
    //root2(x)=root2(x-1)+(x+2)

    def doZeJob(l: Int) {
        val root1 = (0 to l).toList.map((i: Int) => "AOL").mkString("")
        myPrintDln((l, root1))
        val root2 = root1.combinations(l).toList
        if (l < 6) {
            printIt((l, root2.toList.length, root2))
        } else {
            printIt((l, root2.toList.length))
        }
        val root4 = ListSet[String]() ++ root2.filter((s: String) => s.count(_ == 'L') < 2).map(_.permutations).flatten
        if (l < 6) {
            printIt((l, root4.toList.length, root4))
        } else {
            printIt((l, root4.toList.length))
        }
        val root5 = ListSet[String]() ++ root4.filter((s: String) => s.indexOf("AAA") < 0)
        if (l < 6) {
            printIt((l, root5.toList.length, root5))
        } else {
            printIt((l, root5.toList.length))
        }
        myErrPrintDln((l, root5.toList.length))
    }
}