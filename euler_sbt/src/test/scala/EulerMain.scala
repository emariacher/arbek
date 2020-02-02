import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {

  "Euler23" should "be OK" in {
    println("Euler23")

    val zeLimit = 28123

    def sumdiv(i: Int) = {
      (List(BigInt(1)) ++ new EulerDivisors(new EulerDiv(i)).divisors.dropRight(1)).sum
    }

    sumdiv(12) shouldEqual 16
    sumdiv(28) shouldEqual 28

    val limit = 10000
    var abundantNumbers = (1 to limit).filter(i => sumdiv(i) > i).toList
    println(abundantNumbers.take(50))
    println(abundantNumbers.filter(_ % 2 != 0))
    println(945, sumdiv(945), (List(BigInt(1)) ++ new EulerDivisors(new EulerDiv(945)).divisors.dropRight(1)))

    def doZeJob1(abundantNumbers: List[Int]) = {
      var t_ici = timeStamp(t_start, "ici!", false)
      var l = List[Int]()
      abundantNumbers.foreach(i => {
        //println(i, abundantNumbers.map(j => j + i))
        l = (l ++ abundantNumbers.map(j => j + i)).distinct.sorted
      })
      l = l.filter(_ <= limit)
      println("  " + l)

      t_ici = timeStamp(t_ici, "doZeJob1 la!")
      l
    }

    def doZeJob2(abundantNumbers: List[Int], limit: Int) = {
      var t_ici = timeStamp(t_start, "ici!")
      var l = List[Int]()
      abundantNumbers.foreach(i => {
        //println(i, abundantNumbers.map(j => j + i))
        l = (l ++ abundantNumbers.filter(j => j >= i & j <= (limit - i)).map(j => j + i)).distinct.sorted
      })
      l = l.filter(_ <= limit)
      println("  " + l)

      t_ici = timeStamp(t_ici, "doZeJob2 la!")
      l
    }

    val l1 = doZeJob1(abundantNumbers)
    val l2 = doZeJob2(abundantNumbers, limit)

    l1 shouldEqual l2

    println("check that all even numbers are quickly always sum of 2 abundants numbers: " +
      l2.sliding(2).map(z => z.last - z.head).toList.lastIndexWhere(_ > 2), l2.length)

    var result = 0
    println("Euler23[" + result + "]")
    result shouldEqual 0
  }

}