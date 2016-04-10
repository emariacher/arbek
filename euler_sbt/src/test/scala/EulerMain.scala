import java.util.Calendar

import org.scalatest._
import Euler._

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler211" should "be OK" in {
    println("Euler211")

    def sigma2(bi: BigInt): BigInt = {
      val div = new EulerDivisors(new EulerDiv(bi)).getFullDivisors
      val res = div.map(d => d * d)
      println(bi, div, res, res.sum)
      res.sum
    }

    sigma2(10) should be === 130

    var result = 0
    println("Euler211[" + result + "]")
    result should be === 0

  }
}