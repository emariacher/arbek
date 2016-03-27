import java.util.Calendar

import org.scalatest._
import Euler._

class EulerMain extends FlatSpec with Matchers {
  "Euler179" should "be OK" in {
    println("Euler179")

    val premiers = EulerPrime.premiers100000
    val limit = 100000

    val t_ici = timeStamp(t_start, "ici!")
    val z1 = stream_zero_a_linfini.map(b => {
      (b, new EulerDivisors(new EulerDiv(b).primes).getFullDivisors)
    }).drop(2).take(limit).toList.sliding(2).toList.filter(c => c.head._2.length == c.last._2.length)
    timeStamp(t_ici, "la!")

    //println(z1.mkString("\n  ", "\n  ", "\n  "), z1.length)

    var zstart = timeStamp(t_ici, "zstart")
    var cpt = 1
    var bi: BigInt = 15
    var prevnumdiv = 2

    while (bi < limit) {
      var cptprimes = new EulerDivisors(new EulerDiv(bi).primes).divisors.length

      if (cptprimes == prevnumdiv) {
        //println(bi - 1, bi, cptprimes)
        cpt += 1
      }
      prevnumdiv = cptprimes
      bi += 1
    }
    timeStamp(zstart, "zend")
    cpt should be === z1.length

    zstart = timeStamp(t_ici, "zstart2")
    cpt = 1
    bi = 15
    prevnumdiv = 2

    while (bi < limit) {
      if(EulerPrime.isPrime(bi)) {
        bi +=1
        prevnumdiv = 0
      } else {
        var cptprimes = new EulerDivisors(new EulerDiv(bi).primes).divisors.length

        if (cptprimes == prevnumdiv) {
          //println(bi - 1, bi, cptprimes)
          cpt += 1
        }
        prevnumdiv = cptprimes
        bi += 1
      }
    }
    timeStamp(zstart, "zend2")
    cpt should be === z1.length


    val result = 0
    println("Euler179[" + 0 + "]")
    result should be === 0
  }
}