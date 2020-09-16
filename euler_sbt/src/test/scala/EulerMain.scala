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

    def OK(p: Int): Boolean = {
      EulerPrime.isPrime(p) & EulerPrime.isPrime(p % 100000) & EulerPrime.isPrime(p % 10000) & EulerPrime.isPrime(p % 1000) &
        EulerPrime.isPrime(p % 100) & EulerPrime.isPrime(p % 10) &
        isPrimeNZ(p / 100000) & isPrimeNZ(p / 10000) & isPrimeNZ(p / 1000) & isPrimeNZ(p / 100) & isPrimeNZ(p / 10)
    }

    val prems = premiers.filter(p => p.toString.indexOf("2") < 0 & p.toString.indexOf("4") < 0 & p.toString.indexOf("6") < 0 &
      p.toString.indexOf("8") < 0 & p.toString.indexOf("0") < 0)
    println(prems)
    val root = List(23, 37, 53, 73, 313, 317, 373, 537, 797, 3137, 3797, 3173, 31373, 31797, 37973, 73137, 73797)
    println(root.map(p => (p, OK(p))))
    println(premiers.filter(p => OK(p.toInt)))

    val result = 0
    println("Euler37[" + result + "]")
    result shouldEqual 0
  }

}