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