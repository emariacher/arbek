import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.TreeSet

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler211" should "be OK" in {
    println("Euler211")


    def sigma2(bi: BigInt): BigInt = new EulerDivisors(new EulerDiv(bi)).getFullDivisors.map(d => d * d).sum

    def sigma2v(bi: BigInt) = {
      val primes = new EulerDiv(bi).primes
      val div = new EulerDivisors(primes).getFullDivisors
      val res = div.map(d => d * d)
      val sqrt = BigDecimal(math.sqrt(res.sum.toDouble)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
      (bi, res.sum, sqrt, primes, new EulerDiv(sqrt).primes, div, res)
    }

    sigma2(10) should be === 130
    val z = (2 to 40000).map(sigma2v(_)).toList
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
}