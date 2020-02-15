import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {

  "Euler700" should "be OK" in {
    println("Euler700")

    val root = BigInt("1504170715041707")
    val mod = BigInt("4503599627370517")
    ((root * 3) % mod) shouldEqual BigInt("8912517754604")
    ((root * 3) % mod) < root shouldEqual true
    ((root + ((root * 3) % mod)) % mod) shouldEqual BigInt("1513083232796311")

    var eulercoinList = List((1, root, root.toString.length))
    val biggestPrime = 220000
    //val premiers = (new CheckEulerPrime(biggestPrime, 10000)).premiers
    //println(eulercoinList.map(_._2).sum, new EulerDiv2(root, premiers).primes, eulercoinList)
    (1 to 2000000).foreach(n => {
      val bi = ((root * n) % mod)
      if (bi < eulercoinList.head._2) {
        eulercoinList = (eulercoinList :+ (n, bi, bi.toString.length)).sortBy(_._2)
        val somme = eulercoinList.map(_._2).sum % mod
        //println(somme, bi, new EulerDiv2(bi, premiers).primes, eulercoinList)
        println(somme, bi, eulercoinList.tail.head._2 - bi, eulercoinList)
      }
    })
    //println(EulerPrime.isPrime(mod, true))


    var result = 0
    println("Euler700[" + result + "]")
    result shouldEqual 0
  }

}