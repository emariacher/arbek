import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {

  "Euler457" should "be OK" in {
    println("Euler457")

    var t_la = Calendar.getInstance()
    t_la = timeStamp(t_la, "ici")

    var result = 0
    println("Euler457[" + result + "]")
    result shouldEqual 0
  }

}