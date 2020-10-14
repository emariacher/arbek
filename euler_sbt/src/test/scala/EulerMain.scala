import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {


  "Euler25" should "be OK" in {
    println("Euler25")

    def fib(h: BigInt, n: BigInt): LazyList[BigInt] = {
      h #:: fib(n, h + n)
    }

    def fib3(n: Int): Int = {
      def fib_tail(n: Int, a: Int, b: Int): Int = n match {
        case 0 => a
        case _ => fib_tail(n - 1, b, a + b)
      }

      return fib_tail(n, 0, 1)
    }

    println(fib3(12))

    def getIndex(n: BigInt): Int = {
      fib(0, 1).take(300).toList.zipWithIndex.map(a => (a, a._1.toString.length)).filter(_._2 == n).head._1._2
    }

    var x = fib(0, 1)
    println(s"results: ${(x take 100).toList}")
    var y = (x take 150).toList.zipWithIndex.map(a => (a, a._1.toString.length)).groupBy(_._2).toList.map(a => (a._1, a._2.length)).sortBy(_._1)
    //println(y.mkString("\n"))
    println("index of 1st fibonnacci with 7 digits: ", (x take 150).toList.zipWithIndex.map(a => (a, a._1.toString.length)).filter(_._2 == 7).head)
    println("index of 1st fibonnacci with 31 digits: ", (x take 150).toList.zipWithIndex.map(a => (a, a._1.toString.length)).filter(_._2 == 31).head)
    var z = (x take 200).toList.zipWithIndex.map(a => (a, a._1.toString.length)).groupBy(_._2).toList.map(a => (a._1, a._2.length)).sortBy(_._1).map(_._2)
    println(z)
    val pattern0 = List(1, 6, 5, 5)
    val pattern = List(4, 5, 5, 5, 4, 5, 5, 5, 5, 4, 5, 5, 5, 5)
    println(pattern, pattern.length, pattern.sum)

    def answer(n: Int): Int = {
      var indexInPattern = 0
      if (n <= 4) {
        indexInPattern = pattern0.take(n).sum
      } else {
        indexInPattern = pattern.take((n - 4) % 14).sum + 17 + (((n - 4) / 14) * 67)
      }
      indexInPattern
    }

    answer(1) shouldEqual 1
    answer(2) shouldEqual 7
    answer(3) shouldEqual 12
    answer(4) shouldEqual 17
    answer(5) shouldEqual getIndex(5)
    answer(7) shouldEqual (x take 150).toList.zipWithIndex.map(a => (a, a._1.toString.length)).filter(_._2 == 7).head._1._2
    answer(7) shouldEqual getIndex(7)
    answer(9) shouldEqual getIndex(9)
    answer(13) shouldEqual getIndex(13)
    answer(14) shouldEqual getIndex(14)
    answer(17) shouldEqual getIndex(17)
    answer(18) shouldEqual getIndex(18)
    answer(19) shouldEqual getIndex(19)
    answer(31) shouldEqual getIndex(31)
    println(31, answer(31))
    answer(52) shouldEqual getIndex(52)
    println(52, answer(52))

    val result = answer(1000) - 1
    println("Euler25[" + result + "]")
    result shouldEqual 4782
  }

}