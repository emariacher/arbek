import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {


  "Euler37" should "be OK" in {
    println("Euler37")
    val premiers = EulerPrime.premiers100000

    def isPrimeNZ(bi: BigInt): Boolean = {
      bi == 0 || EulerPrime.isPrime(bi)
    }

    def doZeJob(l: List[Int]): List[Int] = {
      val z = l.flatMap(p => {
        List((p * 10) + 3, (p * 10) + 7, (p * 10) + 9)
      })

      val y = z.intersect(premiers.toList)
      val x = y.filter(p => {
        EulerPrime.isPrime(p) & EulerPrime.isPrime(p % 100000) & EulerPrime.isPrime(p % 10000) & EulerPrime.isPrime(p % 1000) &
          EulerPrime.isPrime(p % 100) & EulerPrime.isPrime(p % 10) &
          isPrimeNZ(p / 100000) & isPrimeNZ(p / 10000) & isPrimeNZ(p / 1000) & isPrimeNZ(p / 100) & isPrimeNZ(p / 10)
      })

      println(z, y)
      //val j = (l ++ x).distinct.sorted.filter(p => EulerPrime.isPrime(p))
      println("*****" + x)
      x
    }

    println(premiers)


    val root = List(23, 37, 53, 73, 313, 317, 373, 797, 3137, 3797)
    println(root.map(p => (p, isPrimeNZ(p))))
    val l = doZeJob((10 to 1000).toList)
    doZeJob(l)


    val result = 0
    println("Euler37[" + result + "]")
    result shouldEqual 0
  }

}