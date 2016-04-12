import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.TreeSet

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler451" should "be OK" in {
    println("Euler451")

    def coprimes(i: Int): (Int, List[BigInt], List[Int]) = {
      val primesi = new EulerDiv(i).primes
      (i, primesi, (1 until i).toList.filter(u => {
        val primesu = new EulerDiv(u).primes
        primesi.intersect(primesu).length == 0
      }))
    }

    def modinv(i: Int, j: Int) = {
      (j, (1 to i - 1).toList.dropWhile(u => u * j % i != 1).head)
    }

    def doZeJob(i: Int) = {
      val cp = coprimes(i)
      val md = cp._3.dropRight(1).map(j => modinv(i, j))
      (cp, md.reverse.filter(u => u._1 == u._2).head._1, md)
    }

    println(doZeJob(15))
    println(doZeJob(100))
    println(doZeJob(7))

    doZeJob(15)._2 should be === 11
    doZeJob(100)._2 should be === 51
    doZeJob(7)._2 should be === 1


    var result = 0
    println("Euler451[" + result + "]")
    result should be === 0

  }
}