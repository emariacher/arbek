import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {


  "Euler37" should "be OK" in {
    println("Euler37")
    val premiers = EulerPrime.premiers10000

    println(premiers)

    val root = List(23, 37, 53, 73, 313, 317, 373, 379)
    println(root)

    val z = root.flatMap(p => {
      List((p * 10) + 3, (p * 10) + 7, (p * 10) + 9)
    })

    println(z, z.intersect(premiers.toList))

    val result = 0
    println("Euler37[" + result + "]")
    result shouldEqual 0
  }

}