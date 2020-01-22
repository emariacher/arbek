import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {

  "Euler632" should "be OK" in {
    println("Euler632")


    def squareprimes(bi: BigInt): Int = {
      /*val p = new EulerDiv(bi).primes
      println(p)
      println(p.groupBy(u => u))
      println(p.groupBy(u => u).toList.filter(_._2.length>1))*/
      new EulerDiv(bi).primes.groupBy(u => u).toList.filter(_._2.length > 1).length
    }

    squareprimes(1500)

    def Ck(k: BigInt, bi: BigInt): Int = {
      (1 to bi.toInt).map(z => squareprimes(z)).filter(_ == k).length
    }

    def Ck2(bi: BigInt) = {
      (1 to bi.toInt).map(z => squareprimes(z)).groupBy(u => u).toList.map(y => (y._1, y._2.length))
    }

    Ck(0, 10) shouldEqual 7
    Ck(1, 10) shouldEqual 3
    Ck(3, 100000) shouldEqual 297

    val l = (1 to 6).map(e => {
      val y = BigInt(math.pow(10, e).toInt)
      (y, Ck2(y))
    })

    l.foreach(e => {
      println(e._1 + e._2.mkString)
    })

    var result = 0
    println("Euler632[" + result + "]")
    result shouldEqual 0
  }

}