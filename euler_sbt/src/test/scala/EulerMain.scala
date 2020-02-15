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
    (1 to 1000000).foreach(n => {
      val bi = ((root * n) % mod)
      if (bi < eulercoinList.head._2) {
        eulercoinList = (eulercoinList :+ (n, bi, bi.toString.length)).sortBy(_._2)
        val somme = eulercoinList.map(_._2).sum
        println(somme, eulercoinList)
      }
    })
    println(root, EulerPrime.isPrime(root, true), root / 17, root % 17, BigInt("88480630296571") * 17)
    println(BigInt("88480630296571"), EulerPrime.isPrime(BigInt("88480630296571"), true), BigInt("88480630296571") / 1249)
    println(BigInt("70841177179"), EulerPrime.isPrime(BigInt("70841177179"), true), BigInt("70841177179") / 12043)
    println(BigInt("5882353"), EulerPrime.isPrime(BigInt("5882353"), true))
    //println(EulerPrime.isPrime(mod, true))

    var result = 0
    println("Euler700[" + result + "]")
    result shouldEqual 0
  }

}