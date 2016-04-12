import Euler._
import org.scalatest.{Matchers, FlatSpec}
import scala.collection.immutable.TreeSet
import scala.math.BigInt

/**
 * Created by emariacher on 21.03.2016.
 */
class EulerNotYetSolved extends FlatSpec with Matchers {
  "Euler211" should "be OK" in {
    println("Euler211")


    def sigma2(bi: BigInt): BigInt = new EulerDivisors(new EulerDiv(bi)).getFullDivisors.map(d => d * d).sum

    def sigma2v(bi: BigInt) = {
      val primes = new EulerDiv(bi).primes
      val div = new EulerDivisors(primes).getFullDivisors
      val res = div.map(d => d * d)
      val sqrt = BigDecimal(math.sqrt(res.sum.toDouble)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
      (bi, res.sum, sqrt, sqrt.toDouble / bi.toDouble, primes, sqrt - bi, div, res)
    }

    sigma2(10) should be === 130
    val z = (2 to 200000).map(sigma2v(_)).toList
    val max = z.maxBy(_._2)
    val sqrtmax = BigDecimal(math.sqrt(max._2.toDouble)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
    println("max", max, sqrtmax)
    val t_ici = timeStamp(t_start, "ici!")
    val lsq = TreeSet[BigInt]() ++ (0 to sqrtmax.toInt).map(d => BigInt(d * d))
    val x = z.filter(y => lsq.contains(y._2))
    println(x.mkString("\n  ", "\n  ", "\n  "))
    val t_la = timeStamp(t_ici, "la!")
    val sumlx = x.last._2
    val sqslx = BigDecimal(math.sqrt(sumlx.toDouble)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
    println(sumlx, sqslx)
    sqslx * sqslx should be === sumlx


    var result = 0
    println("Euler211[" + result + "]")
    result should be === 0

  }

  "Euler243" should "be OK" in {
    println("Euler243")
    val premiers = EulerPrime.premiers1000
    def resilience(d: Int) = {
      val primes = new EulerDiv(d).primes
      val divisors = new EulerDivisors(primes).divisors
      val resil = Range(1, d, 2).filter(i => {
        //divisors.filter(div => i % div == 0).isEmpty
        divisors.find(div => i % div == 0) match {
          case Some(i) => false
          case _ => true
        }
      })
      val pgcd_primes = new EulerDiv(resil.length).primes intersect primes
      println(d + "\t" + resil.length + "/" + (d - 1) + "\t" + (1.0 * resil.length / (d - 1)) + "\t" + primes + "\t" + new EulerDiv(resil.length).primes + "\t" + pgcd_primes)
      (resil.length, d - 1)
    }

    resilience(12) should be ===(4, 11)
    resilience(premiers.take(4).product.toInt)
    resilience(premiers.take(5).product.toInt)
    resilience(2 * premiers.take(5).product.toInt)
    resilience(2 * 2 * premiers.take(5).product.toInt)
    resilience(2 * 3 * premiers.take(5).product.toInt)
    resilience(2 * 2 * 3 * premiers.take(5).product.toInt)
    resilience(2 * 3 * 5 * premiers.take(5).product.toInt)
    resilience(2 * 2 * 3 * 3 * premiers.take(5).product.toInt)
    resilience(premiers.take(5).product.toInt)
    resilience(premiers.take(6).product.toInt)
    resilience(premiers.take(7).product.toInt)
    resilience(premiers.take(8).product.toInt)

    println(1.0 * 15499 / 94744)

    val result = 0
    println("Euler243[" + result + "]")
    result should be === 0
  }

  "Euler500" should "be OK" in {
    println("Euler500")
    val premiers = EulerPrime.premiers1000
    def numdiv(d: BigInt) = {
      val divisors = new EulerDivisors(new EulerDiv(d)).divisors
      println(d + "\t" + (divisors.length+2) + "\t" + ispowerof2(divisors.length+2) + "\t" + divisors)
    }

    def ispowerof2(d: BigInt) = {
      val divisorsDistinct = new EulerDivisors(new EulerDiv(d)).primesUnique.toList.distinct
      divisorsDistinct.length == 1 & divisorsDistinct.head == 2
    }

    numdiv(2)
    numdiv(4)
    numdiv(8)
    numdiv(12)
    numdiv(16)
    numdiv(24)
    numdiv(32)
    numdiv(48)
    numdiv(64)
    numdiv(96)
    numdiv(120)
    numdiv(120*2)
    numdiv(120*3)
    numdiv(120*5)
    numdiv(120*2*3)
    numdiv(120*7)
    numdiv(120*11)
    numdiv(120*5*2)
    numdiv(120*5*3)
    numdiv(120*5*7)
    numdiv(120*7*2)
    numdiv(120*7*3)
    numdiv(120*7*11)

    val result = 0
    println("Euler500[" + result + "]")
    result should be === 0
  }


}