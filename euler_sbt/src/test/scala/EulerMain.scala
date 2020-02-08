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

    var limit = 2000
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
      var t_ici = timeStamp(t_start, "ici!", false)
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

    val lpair = l2.sliding(2).map(z => z.last - z.head).toList.lastIndexWhere(_ > 2)
    println("check that all even numbers are quickly always sum of 2 abundants numbers: " +
      lpair, l2.sliding(2).toList.apply(lpair), l2.length)

    val zlpairnosum = l2.sliding(2).toList.filter(z => z.last - z.head > 2)
    println(zlpairnosum)
    val lpairnosum = ((1 to 23).toList ++ List(26, 28, 34, 46)).filter(_ % 2 == 0)
    println("lpairnosum", lpairnosum)

    limit = zeLimit
    abundantNumbers = (1 to limit).filter(i => sumdiv(i) > i).toList

    val oddabundants = abundantNumbers.filter(_ % 2 != 0)
    println(oddabundants)

    def doZeJob3(abundantNumbers: List[Int], oddabundantNumbers: List[Int], limit: Int) = {
      var t_ici = timeStamp(t_start, "ici!", false)
      var l = List[Int]()
      oddabundantNumbers.foreach(i => {
        //println(i, abundantNumbers.map(j => j + i))
        l = (l ++ abundantNumbers.filter(j => j <= (limit - i)).map(j => j + i)).distinct.sorted
      })
      l = l.filter(_ <= limit)
      println("  " + l)

      t_ici = timeStamp(t_ici, "doZeJob3 la!")
      l
    }

    val l3 = doZeJob3(abundantNumbers, oddabundants, limit)

    val oddnumberswearelookingfor = (1 to limit).filter(i => i % 2 != 0).filter(i => !l3.contains(i)).toList
    println("oddnumberswearelookingfor", oddnumberswearelookingfor)

    var result = oddnumberswearelookingfor.sum + lpairnosum.sum
    println("Euler23[" + result + "]")
    result shouldEqual 4179871
  }

}