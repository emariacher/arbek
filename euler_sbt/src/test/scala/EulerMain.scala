import java.util.Calendar

import org.scalatest._
import Euler._

class EulerMain extends FlatSpec with Matchers {
  "Euler100" should "be OK" in {
    println("Euler100")

    val limit = 1000000

    def is50pourcent(total: Double): (Boolean, String) = {
      val totcar = math.pow(total - 0.5, 2)
      val totsqrt = math.sqrt(totcar / 2)
      val blue = math.ceil(totsqrt)
      val stat = (blue / total) * ((blue - 1) / (total - 1))
      if (math.abs(stat - 0.5) < 0.00000001) {
        val totalInt = BigDecimal(total).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
        val blueInt = BigDecimal(blue).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
        val statInt = (blueInt * (blueInt - 1)) * 2 == (totalInt * (totalInt - 1))
        (statInt, (totalInt, blueInt, stat, new EulerDiv(totalInt).primes, new EulerDiv(totalInt-1).primes,
          new EulerDiv(blueInt).primes, new EulerDiv(blueInt-1).primes, "-----------------------------").toString)
      } else {
        (false, (total, totcar, totsqrt, blue, stat).toString)
      }
    }

    val t_ici = timeStamp(t_start, "ici!")
    val z1 = (1 to limit).map(b => is50pourcent(b.toDouble)).filter(_._1)
    timeStamp(t_ici, "la!")

    println(z1.mkString("\n  ", "\n  ", "\n  "))

    val result = 0
    println("Euler100[" + 0 + "]")
    result should be === 0
  }
}