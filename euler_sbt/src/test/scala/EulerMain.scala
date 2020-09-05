import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {


  "Euler33" should "be OK" in {
    println("Euler33")

    val p100 = (10 to 99).filter(_.toString.indexOf("0") < 0)

    val z = p100.map(i => {
      p100.dropWhile(_ <= i).filter(j => i.toString.toList.intersect(j.toString.toList).nonEmpty).map(j => {
        val k = i.toString.toList.intersect(j.toString.toList).head
        (i, j, k)
      })
    }).flatten.filter(ijk => {
      ijk._1.toString.toList.exists(_ != ijk._3) && ijk._2.toString.toList.exists(_ != ijk._3)
    }).map(ijk => {
      val is = ijk._1.toString.toList.filter(_ != ijk._3).head.toString
      val js = ijk._2.toString.toList.filter(_ != ijk._3).head.toString
      (ijk._1, ijk._2, ijk._3, ijk._1.toDouble / ijk._2.toDouble, is, js, is.toDouble / js.toDouble)
    }).filter(u => u._4 == u._7)
    println(z.mkString("\n"))

    val result = z.map(_._2.toInt).product
    println("Euler33[" + result + "]")
    result shouldEqual 800
  }

}