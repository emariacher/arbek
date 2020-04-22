import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {

  "Euler706" should "be OK" in {
    println("Euler706")

    def f(n: BigInt, verbose: Boolean = false): BigInt = {
      val s = n.toString
      val l = s.length
      val z = (1 to l).map(i => s.toSeq.sliding(i)).toList.flatten
      if (verbose) {
        println(n, z.filter(u => (u.sum % 3 == 0)).length, z)
      }
      z.filter(u => (u.sum % 3 == 0)).length
    }

    def F(d: Int): BigInt = {
      val start = BigInt("1" + (2 to d).map(z => "0").mkString)
      val end = start * 10
      val z = (start until end).toList.filter(bi => f(bi) % 3 == 0).length
      println(d, (start until end), z)
      z
    }

    f(2573, true) shouldEqual 3
    f(100, true) shouldEqual 3
    f(150, true) shouldEqual 3
    f(1000, true) shouldEqual 6
    /*val z = (2 to 6).map(i => (i, F(i)))
    z.apply(0) shouldEqual(2, 30)
    z.apply(4) shouldEqual(6, 290898)*/

    def f2(n: BigInt): List[BigInt] = {
      val s = n.toString
      val l = s.length
      val z = (1 to l).map(i => s.toSeq.sliding(i)).toList.flatten
      //println(n, z.filter(u => (u.sum % 3 == 0)).length, z)
      z.filter(u => (u.sum % 3 == 0)).map(u => BigInt(u.toString))
    }

    def F2(d: Int): BigInt = {
      val start = BigInt("1" + (2 to d).map(z => "0").mkString)
      val end = start * 10
      val y = (start until end).toList.map(bi => (bi, f2(bi))).filter(_._2.length % 3 == 0)
      val z = y.length
      println(d, (start until end), z, y.groupBy(_._2.length).mkString("\n  ", "\n  ", "\n"))
      z
    }

    F2(2) shouldEqual 30
    F2(3) shouldEqual F(3)

    def F3(start: BigInt, end: BigInt): BigInt = {
      val z = (start until end).toList.filter(bi => f(bi) % 3 == 0).length
      println((start until end), z)
      z
    }

    F3(10, 100) shouldEqual 30
    F3(10, 20) shouldEqual F3(40, 50)
    F3(20, 30) shouldEqual F3(50, 60)
    F3(30, 40) shouldEqual F3(90, 100)
    F3(100, 200) shouldEqual F3(400, 500)
    F3(200, 300) shouldEqual F3(800, 900)
    F3(300, 400) shouldEqual F3(600, 700)
    F3(100, 1000) shouldEqual ((F3(300, 400) * 3) + (F3(100, 200) * 6))
    F3(200010010, 200010020) shouldEqual F3(200010070, 200010080)

    var result = 0
    println("Euler706[" + result + "]")
    result shouldEqual 0
  }

}