import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.{ListSet, TreeSet}
import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {

  "Euler24" should "be OK" in {
    println("Euler24")

    def z(i: Int) = (0 to i).permutations.map(_.mkString("")).toList

    def factorial(n: Int): Int = n match {
      case 0 => 1
      case _ => n * factorial(n - 1)
    }

    (4 to 8).foreach(i => {
      val y = z(i)
      println(i, factorial(i + 1), y.length, Math.pow(i, i - 4).toInt, y.apply(Math.pow(i, i - 4).toInt))
      println("  ", y.indexWhere(_.startsWith("013")), y.indexWhere(_.startsWith("014")),
        y.indexWhere(_.startsWith("015")), y.indexWhere(_.startsWith("016")), y.indexWhere(_.startsWith("017")), y.indexWhere(_.startsWith("018")))
      println("  ", factorial(i - 2), factorial(i - 2) * 2, factorial(i - 2) * 3)
      println("    ", y.indexWhere(_.startsWith("021")), y.indexWhere(_.startsWith("023")),
        y.indexWhere(_.startsWith("024")), y.indexWhere(_.startsWith("025")), y.indexWhere(_.startsWith("026")))
      println("    ", factorial(i - 1), factorial(i - 1) + factorial(i - 2), factorial(i - 1) + (2 * factorial(i - 2)))
      println("      ", y.indexWhere(_.startsWith("031")), y.indexWhere(_.startsWith("032")), y.indexWhere(_.startsWith("034")), y.indexWhere(_.startsWith("035")))
      println("      ", factorial(i - 1) * 2, (factorial(i - 1) * 2) + factorial(i - 2), (factorial(i - 1) * 2) + (2 * factorial(i - 2)), (factorial(i - 1) * 2) + (3 * factorial(i - 2)))
      println("        ", y.indexWhere(_.startsWith("041")))
      println("041     ", factorial(i - 1) * 3)
    })

    (4 to 10).foreach(i => {
      println(i, factorial(i + 1), Math.pow(i, i - 4).toInt)
      println("    ", factorial(i - 1), factorial(i - 1) + factorial(i - 2), factorial(i - 1) + (2 * factorial(i - 2)))
      println("      ", factorial(i - 1) * 2, (factorial(i - 1) * 2) + factorial(i - 2), (factorial(i - 1) * 2) + (2 * factorial(i - 2)),
        (factorial(i - 1) * 2) + (3 * factorial(i - 2)), (factorial(i - 1) * 2) + (4 * factorial(i - 2)), (factorial(i - 1) * 2) + (5 * factorial(i - 2)))
      println("041     ", factorial(i - 1) * 3)
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