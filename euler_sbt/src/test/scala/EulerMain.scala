import java.util.Calendar

import org.scalatest._
import Euler._

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler191" should "be OK" in {
    println("Euler191")

    def decode(j: Int) = {
      (j % 3) match {
        case 0 => "O"
        case 1 => "L"
        case 2 => "A"
      }
    }
    def good(s: String) = s.count(_=='L')<2 && !s.contains("AAA")

    val z = (0 until 81).map(i => {
      var s = ""
      var j = i
      s += decode(j)
      j /= 3
      s += decode(j)
      j /= 3
      s += decode(j)
      j /= 3
      s += decode(j)
      println(i, s)
      s
    }).filter(good(_))
    println(z, z.length)


    val result = 0
    println("Euler191[" + 0 + "]")
    result should be === 0
  }
}