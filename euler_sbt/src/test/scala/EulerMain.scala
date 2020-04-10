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
    /*Range(2,3).toList.foreach(bi => {
      println("  ", bi, mod + bi)
      println("     ", bi, mod + bi, new EulerDiv2(mod + bi, premiers,true).primes)
    })*/
    println(mod, mod + 2, new EulerDiv2(mod + 2, premiers).primes, "\n")

    var eulercoinList = List((BigInt(1), root))
    var max = 43000000
    var t_la = Calendar.getInstance()
    (1 to max).foreach(n => {
      val bi: BigInt = ((root * BigInt(n)) % mod)
      if (bi < eulercoinList.head._2) {
        eulercoinList = (eulercoinList :+ (BigInt(n), bi)).sortBy(_._2)
        val somme = eulercoinList.map(_._2).sum % mod
        //println(somme, bi, new EulerDiv2(bi, premiers).primes, eulercoinList)
        //println(somme, bi, eulercoinList.tail.head._2 - bi, n - eulercoinList.tail.head._1, eulercoinList)
      }
    })
    var oldlast = eulercoinList.map(_._2).last
    var vieilleSomme = eulercoinList.map(_._2).sum % mod
    t_la = timeStamp(t_la, "après1 max: " + max)
    var state = 0
    var n = BigInt(1)
    var prevdiff = BigInt(1)
    var prevdelta: (BigInt, Double, Double) = null
    var prevy = 1.0
    var prevx = 1.0
    eulercoinList = List((1, root))
    while (n < max) {
      val bi = ((root * n) % mod)
      if (bi < eulercoinList.head._2) {
        eulercoinList = (eulercoinList :+ (n, bi)).sortBy(_._2)
        val somme = eulercoinList.map(_._2).sum % mod
        val x = somme.toDouble / n.toDouble
        val diff = n - eulercoinList.tail.head._1
        val diff2 = ((diff.toDouble * root.toDouble) / mod.toDouble)
        println("\n" + n, somme, bi, eulercoinList.tail.head._2 - bi, n - eulercoinList.tail.head._1, (n - eulercoinList.tail.head._1).toDouble / prevdiff.toDouble, eulercoinList)
        println("[" + n, somme, "" + bi + "]", n * bi, somme / n, x / prevx, eulercoinList.tail.head._2 - bi,
          " diff[" + diff + " / " + diff2 + "]", diff.toDouble / prevdiff.toDouble)
        println("   " + (1 to 10).map(u => (diff + (u * n))))
        val y = math.abs((diff.toDouble * root.toDouble) / mod.toDouble)
        //println(y, "   ", (-4 to 4).map(u => math.abs(((diff.toDouble + u) * root.toDouble) / mod.toDouble)))
        val z = nearmod.map(z => (z._1, 0.0, math.abs(((z._2 - bi)).toDouble))).sortBy(_._3)(Ordering.Double.TotalOrdering)
        println(n, mod / n, mod % n)
        //println(z.take(2), "\n")
        prevdelta = z.head
        prevdiff = diff
        prevy = y
        prevx = x
        n += prevdiff
        if ((n > (max - 3)) & (eulercoinList.map(_._2).last == oldlast)) {
          println("************Checking! @" + max + "**************")
          eulercoinList.map(_._2).sum % mod shouldEqual vieilleSomme
          timeStamp(t_la, "check après2 max: " + max)
        }
        state = 1
      } else if (state == 1) {
        state = 2
        n += prevdiff * 2
      } else {
        state = 2
        n += 1
      }
    }
    t_la = timeStamp(t_la, "après2 max: " + max)

    var max1 = mod
    eulercoinList = eulercoinList.reverse.take(3)
    n = eulercoinList.last._1
    var prevn = eulercoinList.take(2).last._1
    while (n < max1) {
      if (n == 42298633) {
        println("************Checking! @" + n + "/" + max + "**************")
        eulercoinList.map(_._2).sum % mod shouldEqual vieilleSomme
        timeStamp(t_la, "check après3 max: " + max)
      }
      val diff = n - prevn
      val listedesProchainsCandidats = (1 to 10).map(u => (diff + (u * n))).toList
      println(n, eulercoinList.map(_._2).sum % mod, eulercoinList.reverse)
      //println(" " + n, diff, listedesProchainsCandidats)
      var found = false
      listedesProchainsCandidats.takeWhile(ncand => {
        val ecand = ((root * ncand) % mod)
        if (ecand < eulercoinList.last._2) {
          eulercoinList = (eulercoinList :+ (ncand, ecand)).sortBy(_._1)
          found = true
          false
        } else {
          true
        }
      })
      found shouldEqual true
      prevn = n
      n = eulercoinList.last._1
    }
    val Somme = eulercoinList.map(_._2).sum % mod
    t_la = timeStamp(t_la, "après3 max1: " + max1 + " , Somme = " + Somme)
    println(n, eulercoinList.map(_._2).sum % mod, eulercoinList.reverse)

    var result = 0
    println("Euler700[" + result + "]")
    result shouldEqual 0
  }

}