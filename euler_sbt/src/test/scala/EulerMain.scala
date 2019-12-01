import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler621" should "be OK" in {
    println("Euler621")
    val t10= Euler.triangular(4)

    def G1(a: BigInt): BigInt = {
      BigInt(0)
    }

    println(t10)

    G1(9) shouldEqual 7
    G1(1000) shouldEqual 78
    //G1(1000000) shouldEqual 2106

    var result = 0
    println("Euler621[" + result + "]")
    result shouldEqual 0

  }
}