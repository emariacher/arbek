import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {


  "Euler37" should "be OK" in {
    println("Euler37")
    val premiers = EulerPrime.premiers1000

    println(premiers)

    println(23, 37, 53, 73, 97, 313, 317, 373, 379)

    val result = 0
    println("Euler37[" + result + "]")
    result shouldEqual 0
  }

}