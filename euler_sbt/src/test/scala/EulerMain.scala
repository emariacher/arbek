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

    val biggestPrime = 220000
    val premiers = (new CheckEulerPrime(biggestPrime, 10000)).premiers
    val rootprimes = new EulerDiv2(root, premiers).primes
    val rootdivisors = new EulerDivisors(rootprimes).getFullDivisors
    val nearmod = rootdivisors.map(i => (i, mod / i, mod % i))
    val nearmod2 = nearmod.sortBy(_._3).tail
    println(root, rootprimes, rootdivisors)
    println("mod/root = ", mod.toDouble / root.toDouble)
    println(nearmod)
    println(nearmod2, "\n")

    var eulercoinList = List((1, root, root.toString.length))
    var max = 43000000
    var t_la = Calendar.getInstance()
    (1 to max).foreach(n => {
      val bi = ((root * n) % mod)
      if (bi < eulercoinList.head._2) {
        eulercoinList = (eulercoinList :+ (n, bi, bi.toString.length)).sortBy(_._2)
        val somme = eulercoinList.map(_._2).sum % mod
        //println(somme, bi, new EulerDiv2(bi, premiers).primes, eulercoinList)
        //println(somme, bi, eulercoinList.tail.head._2 - bi, n - eulercoinList.tail.head._1, eulercoinList)
      }
    })
    var vieilleSomme = eulercoinList.map(_._2).sum % mod
    t_la = timeStamp(t_la, "après1 ")
    var n = 1
    var prevdiff = 1
    var prevdelta: (BigInt, Double, Double) = null
    eulercoinList = List((1, root, root.toString.length))
    while (n < max) {
      val bi = ((root * n) % mod)
      if (bi < eulercoinList.head._2) {
        eulercoinList = (eulercoinList :+ (n, bi, bi.toString.length)).sortBy(_._2)
        val somme = eulercoinList.map(_._2).sum % mod
        //println(n, somme, bi, eulercoinList.tail.head._2 - bi, n - eulercoinList.tail.head._1, (n - eulercoinList.tail.head._1).toDouble / prevdiff.toDouble, eulercoinList)
        println(n, somme, bi, eulercoinList.tail.head._2 - bi, n - eulercoinList.tail.head._1, (n - eulercoinList.tail.head._1).toDouble / prevdiff.toDouble)
        val z = nearmod.map(z => (z._1, 0.0, math.abs(((z._2 - bi)).toDouble))).sortBy(_._3)(Ordering.Double.TotalOrdering)
        println(n, mod / n, mod % n)
        println(z.take(2), "\n")
        prevdelta = z.head
        prevdiff = n - eulercoinList.tail.head._1
        n += prevdiff
      } else {
        n += 1
      }
    }
    eulercoinList.map(_._2).sum % mod shouldEqual vieilleSomme
    t_la = timeStamp(t_la, "après2 ")

    n = 1
    prevdiff = 1
    prevdelta = (BigInt(0), 0.0, 0.0)
    eulercoinList = List((1, root, root.toString.length))
    t_la = timeStamp(t_la, "après3 ")


    var result = 0
    println("Euler700[" + result + "]")
    result shouldEqual 0
  }

}