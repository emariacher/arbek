import java.util.Calendar

import org.scalatest._
import Euler._

class EulerMain extends FlatSpec with Matchers {
  "Euler158" should "be OK" in {
    println("Euler158")

    def lexleft(s: String) = {
      println(s, s.sliding(2).toList, s.sliding(2).toList.map(s2 => (s2, s2.last > s2.head)))
      s.sliding(2).toList.count(s2 => {
        s2.last > s2.head
      })
    }

    println(List("abc", "hat", "zyx").map(lexleft(_)))

    val result = 0
    println("Euler158[" + 0 + "]")
    result should be === 0
  }
}