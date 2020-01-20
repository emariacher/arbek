import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {

  "Euler22" should "be OK" in {
    println("Euler22")

    val url = "https://projecteuler.net/project/resources/p022_names.txt"
    val data = io.Source.fromURL(url).mkString

    val l = data.split(",").map(_.replaceAll("\"", "")).sorted
    (l.indexOf("COLIN") + 1) shouldEqual 938
    
    var result = 0
    println("Euler22[" + result + "]")
    result shouldEqual 0
  }

}