import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {


  "Euler33" should "be OK" in {
    println("Euler33")

    val premiers100 = EulerPrime.premiers1000.filter(_ < 100)
    val paspremiers100 = (10 to 99).filter(i => !premiers100.contains(i)).filter(_.toString.indexOf("0") < 0)

    println(paspremiers100)

    val z = paspremiers100.map(i => {
      paspremiers100.dropWhile(_ <= i).filter(j => i.toString.toList.intersect(j.toString.toList).nonEmpty).map(j => {
        (i, j, i.toString.toList.intersect(j.toString.toList).head, i.toDouble / j.toDouble)
      })
    })
    println(z.flatten.groupBy(_._4).filter(g => g._2.length > 1).toList.sortBy(_._1)(Ordering.Double.TotalOrdering).mkString("\n"))

    val result = 0
    println("Euler33[" + result + "]")
    result shouldEqual 0
  }

}