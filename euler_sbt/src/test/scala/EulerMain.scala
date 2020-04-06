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
    /*val root = BigInt("417")
    val mod = BigInt("1249")*/

    val biggestPrime = 220000
    val premiers = (new CheckEulerPrime(biggestPrime, 10000)).premiers
    val rootprimes = new EulerDiv2(root, premiers).primes
    val rootdivisors = new EulerDivisors(rootprimes).getFullDivisors
    println(root, rootprimes, rootdivisors)
    println("mod/root = ", mod.toDouble / root.toDouble)
    println("1/root = ", 1.0 / root.toDouble)
    println("mod: " + mod, mod + 2, new EulerDiv2(mod + 2, premiers).primes, "\n")
    /*Range(0, 7300).toList.foreach(bi => {
      val ed2 = new EulerDiv2(mod + bi, premiers, false, true)
      if (ed2.solved) {
        print(".")
        val intersect = ed2.primes.toList.intersect(rootprimes.tail)
        if (!intersect.isEmpty) {
          println("\n" + bi, mod + bi, ed2.primes, ed2.solved)
          println("     ", intersect)
        }
      }
    })*/

    var eulercoinList = List((1, root, root.toString.length))
    var max = 43000000
    var t_la = Calendar.getInstance()
    (1 to max).foreach(n => {
      val reste = ((root * n) % mod)
      val div = ((root.toDouble * n) / mod.toDouble) % 1
      if (reste < eulercoinList.head._2) {
        eulercoinList = (eulercoinList :+ (n, reste, reste.toString.length)).sortBy(_._2)
        val somme = eulercoinList.map(_._2).sum % mod
        //println(somme, bi, new EulerDiv2(bi, premiers).primes, eulercoinList)
        println(somme, reste, div, reste.toDouble / mod.toDouble,
          eulercoinList.tail.head._2 - reste,
          n - eulercoinList.tail.head._1,
          eulercoinList)
      }
    })
    var oldhead = eulercoinList.map(_._2).head
    var vieilleSomme = eulercoinList.map(_._2).sum % mod
    t_la = timeStamp(t_la, "après1 max: " + max + " " + vieilleSomme + " " + oldhead)

    var result = 0
    println("Euler700[" + result + "]")
    result shouldEqual 0
  }

}