import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {

  "Euler24" should "be OK" in {
    println("Euler24")

    def z(i: Int): List[String] = (0 to i).permutations.map(_.mkString("")).toList

    def factorial(n: Int): Int = n match {
      case 0 => 1
      case _ => n * factorial(n - 1)
    }

    def u(l: List[String], s: String, i: Int, check: Boolean = true): (String, Int, Int) = {
      if ((i != 0) & (l.indexWhere(_.startsWith(s)) >= 0) & check) {
        i shouldEqual l.indexWhere(_.startsWith(s))
      }
      (s, l.indexWhere(_.startsWith(s)), i)
    }

    println(z(2))
    (6 to 8).foreach(i => {
      val y = z(i)
      println(i, factorial(i), (0 to i).toList.map(j => u(y, j.toString, factorial(i) * j)))
      println("  2", (0 to 1).toList.map(j => u(y, "2" + j.toString, (factorial(i) * 2) + (factorial(i - 1) * j))))
      println("  2", (3 to i).toList.map(j => u(y, "2" + j.toString, (factorial(i) * 2) + (factorial(i - 1) * (j - 1)))))
      println("  27", (0 to 1).toList.map(j => u(y, "27" + j.toString, (factorial(i) * 2) + (factorial(i - 1) * 6) + (factorial(i - 2) * j))))
      println("  27", (3 to 6).toList.map(j => u(y, "27" + j.toString, (factorial(i) * 2) + (factorial(i - 1) * 6) + (factorial(i - 2) * (j - 1)))))
      println("  27", (8 to i).toList.map(j => u(y, "27" + j.toString, (factorial(i) * 2) + (factorial(i - 1) * 6) + (factorial(i - 2) * (j - 2)))))
      println("  278", (3 to 6).toList.map(j => u(y, "278" + j.toString, (factorial(i) * 2) + (factorial(i - 1) * 6) + (factorial(i - 2) * 6) + (factorial(i - 3) * (j - 1)), true)))
    })

    (6 to 9).foreach(i => {
      println(i, factorial(i), (0 to i).toList.map(j => (j.toString, factorial(i) * j)))
      println("  2", (3 to i).toList.map(j => ("2" + j.toString, (factorial(i) * 2) + (factorial(i - 1) * (j - 1)))))
      println("  27", (8 to i).toList.map(j => ("27" + j.toString, (factorial(i) * 2) + (factorial(i - 1) * 6) + (factorial(i - 2) * (j - 2)))))
      println("  278", (3 to 6).toList.map(j => ("278" + j.toString, (factorial(i) * 2) + (factorial(i - 1) * 6) + (factorial(i - 2) * 6) + (factorial(i - 3) * (j - 1)))))
    })

    var result = 0
    println("Euler24[" + result + "]")
    result shouldEqual 0
  }

  /*"Euler457" should "be OK" in {
    println("Euler457")

    def f(n: BigInt): BigInt = (n * n) - (3 * n) - 1

  def OK(p: BigInt, n: BigInt): Boolean = f(n) % (p * p) == 0

  var t_la = Calendar.getInstance()
  val premiers1000 = EulerPrime.premiers1000
  println(premiers1000.toList.map(p => {
    (p, p * p, (0 to 1000).map(n => (n, f(n), OK(p, n))).filter(_._3))
  }).filter(!_._3.isEmpty).mkString("\n"))
  t_la = timeStamp(t_la, "ici")
  println((0 to 1000).map(n => (n, f(n))).map(nn => {
    (nn._1, nn._2, premiers1000.filter(p => nn._2 % (p * p) == 0).mkString("-"))
  }).filter(!_._3.isEmpty).mkString("\n"))
  t_la = timeStamp(t_la, "là")

  var result = 0
  println("Euler457[" + result + "]")
  result shouldEqual 0
  }*/

}