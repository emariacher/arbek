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
    val root1 = "AAAAOOOOLLLL"
    printIt(root1)
    val root2 = root1.combinations(4).toList
    printIt(root2)
    val root3 = ListSet[String]() ++ root2.map(_.permutations).flatten
    printIt((root3.toList.length, root3))
    val root4 = ListSet[String]() ++ root2.filter((s: String) => s.count(_ == 'L') < 2).map(_.permutations).flatten
    printIt((root4.toList.length, root4))
    val root5 = ListSet[String]() ++ root4.filter((s: String) => s.indexOf("AAA") < 0)
    printIt((root5.toList.length, root5))
}