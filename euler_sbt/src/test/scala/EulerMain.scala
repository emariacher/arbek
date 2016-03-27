import java.util.Calendar

import org.scalatest._
import Euler._

class EulerMain extends FlatSpec with Matchers {
  "Euler100" should "be OK" in {
    println("Euler100")

    val limit = 200

    def is50pourcent(total: Double): String = {
      val divpar7 = total / 7.0
      val blue = math.floor(divpar7 * 5)
      val stat = (blue / total) * ((blue - 1) / (total - 1))
      if(math.abs(stat-0.5)<0.0000000001) {
        val totalInt = BigDecimal(total).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
        val blueInt = BigDecimal(blue).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
        val statInt = (blueInt*(blueInt-1))*2==(totalInt*(totalInt-1))
        (total, blueInt, statInt, "-----------------------------").toString
      } else {
        (total, blue, stat).toString
      }
    }

    val z1 = (1 to limit).map(b => (b, is50pourcent(b.toDouble)))

    println(z1.mkString("\n  ", "\n  ", "\n  "))

    val result = 0
    println("Euler100[" + 0 + "]")
    result should be === 0
  }
}