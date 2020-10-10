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
      if (n < 4) {
        indexInPattern = pattern0.take(n).sum
      }
      indexInPattern
    }

    answer(1) shouldEqual 1
    answer(2) shouldEqual 7
    answer(3) shouldEqual 12
    answer(4) shouldEqual 17
    answer(7) shouldEqual (x take 150).toList.zipWithIndex.map(a => (a, a._1.toString.length)).filter(_._2 == 7).head
    answer(31) shouldEqual (x take 150).toList.zipWithIndex.map(a => (a, a._1.toString.length)).filter(_._2 == 31).head

    def fib3(n: Int): Int = {
      def fib_tail(n: Int, a: Int, b: Int): Int = n match {
        case 0 => a
        case _ => fib_tail(n - 1, b, a + b)
      }

      return fib_tail(n, 0, 1)
    }

    println(fib3(12))

    val result = 0
    println("Euler25[" + result + "]")
    result shouldEqual 0
  }

}