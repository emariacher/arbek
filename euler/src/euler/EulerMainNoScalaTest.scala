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
    new Euler203
    myPrintDln("Au revoir Monde!")
}

class Euler203 {
    myAssert2(105,distinctSquarefreeNumbersSum1(8))
    def distinctSquarefreeNumbersSum1(rowNumber: BigInt) = {
        (ListSet[BigInt]() ++ new TrianglePascal(rowNumber.toInt).triangle.flatten).
            map(bi => (bi, new EulerDiv(bi).primes)).filter(c => (ListSet[BigInt]() ++ c._2).toList.
                length == c._2.length).map(_._1).sum
    }
}
