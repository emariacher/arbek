import Elliptique._
import org.scalatest._

import scala.collection.immutable.{Range, ListSet}
import scala.math.BigInt

class ElliptiqueTest extends FlatSpec with Matchers {
  "Elliptique27" should "be OK" in {
    println("Euler27")
    val t_ici = timeStamp(t_start, "ici!")
    val t_la = timeStamp(t_ici, "la! ******************************")

    val result = 0
    println("Elliptique27[" + result + "]")
    result should be === 0
  }
}