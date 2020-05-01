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

    def F2(d: Int, DeltaStart: BigInt, delta: BigInt): BigInt = {
      val start = BigInt("1" + (2 to d).map(z => "0").mkString) + DeltaStart
      val end = start + delta
      val z = (start until end).toList.filter(bi => f(bi) % 3 == 0).length
      println(d, (start until end), z)
      z
    }


    def F3(start: BigInt, end: BigInt): BigInt = {
      val z = (start until end).toList.filter(bi => f(bi) % 3 == 0).length
      //print(" [" + start + "-" + end + "]: " + z + ",")
      //print("," + z)
      z
    }

    var t_la = Calendar.getInstance()

    def getMapResult(l: List[BigInt]): BigInt = {
      (l.head * 4) + (l.tail.sum * 3)
    }

    def getMapResults(l: List[BigInt]): BigInt = {
      (l.head * 4) + (l.last * 6)
    }

    def F4(d: Int, check: Boolean = true): BigInt = {
      val start = BigInt("1" + (2 to d).map(z => "0").mkString)
      val end = start * 10
      val delta = start / 10
      val inc = delta / 10
      val result = ((((F3(start * 3, (start * 3) + inc) * 40) + (F3((start * 3) + delta, (start * 3) + (delta * 2)) * 6)) * 3) +
        (((F3(start, start + delta) * 7) + (F3(start + delta, start + delta + inc) * 30)) * 6)
        )
      if (check) {
        F3(start, end) shouldEqual result
      }
      println("")
      F3(start * 3, (start * 3) + inc) shouldEqual dozemap(start * 3, inc / 10)._2
      F3((start * 3) + delta, (start * 3) + (delta * 2)) shouldEqual dozemap((start * 3) + delta, inc)._2
      F3(start, start + delta) shouldEqual dozemap(start, inc)._2
      F3(start + delta, start + delta + inc) shouldEqual dozemap(start + delta, inc / 10)._2
      println("**[" + start + "-" + end + "]: " + result + "               delta: " + delta + ", inc: " + inc)
      result
    }

    def dozemap(start: BigInt, inc: BigInt): (List[BigInt], BigInt) = {
      print("   dzm:")
      val troisPremiersIcrements = (0 until 3).map(i => F3(start + (i * inc), start + ((i + 1) * inc))).toList
      println(" -> " + getMapResult(troisPremiersIcrements))
      (troisPremiersIcrements, getMapResult(troisPremiersIcrements))
    }

    def dozemap2(start: BigInt, inc: BigInt, cpt: Int = 0): (List[BigInt], BigInt) = {
      //print("   dzm2:")
      val cpt1 = cpt + 1
      val troisPremiersIcrements = inc.toInt match {
        case 10 =>
          val r = (0 until 3).map(i => F3(start + (i * inc), start + ((i + 1) * inc))).toList
          print(" ")
          r
        case _ => List(getMapResult(dozemap2(start, inc / 10, cpt1)._1),
          getMapResult(dozemap2(start + inc, inc / 10, cpt1)._1),
          getMapResult(dozemap2(start + inc + inc, inc / 10, cpt1)._1))
      }
      val s = (0 to cpt1).map(i => " ").mkString("")
      if (cpt == 0) {
        println(s + start + " - " + (start + (inc * 10)) + ": " +
          (troisPremiersIcrements, getMapResult(troisPremiersIcrements)))
        //print(" ")
      } else {
        println(s + start + " - " + (start + (inc * 10)) + ": " +
          (troisPremiersIcrements, getMapResult(troisPremiersIcrements)))
      }
      (troisPremiersIcrements, getMapResult(troisPremiersIcrements))
    }

    def dozemaps(start: BigInt, inc: BigInt): List[BigInt] = {
      print("   dzms:")
      val deuxPremiersIcrements = (0 until 2).map(i => F3(start + (i * inc), start + ((i + 1) * inc))).toList
      println(" -> " + getMapResults(deuxPremiersIcrements))
      deuxPremiersIcrements
    }

    def dozemaps2(start: BigInt, inc: BigInt): List[BigInt] = {
      //print("   dzms2:")
      val deuxPremiersIcrements = inc.toInt match {
        case 10 => (0 until 2).map(i => F3(start + (i * inc), start + ((i + 1) * inc))).toList
        case _ => List(getMapResults(dozemaps2(start, inc / 10)), getMapResult(dozemap2(start + inc, inc / 10)._1))
      }
      println(" -> " + (deuxPremiersIcrements, getMapResults(deuxPremiersIcrements)))
      deuxPremiersIcrements
    }

    def dozemaps3(start: BigInt, inc: BigInt): List[BigInt] = {
      print("   dzms3:")
      val deuxPremiersIcrements = inc.toInt match {
        case 10 => (0 until 2).map(i => F3(start + (i * inc), start + ((i + 1) * inc))).toList
        case _ => List(getMapResult(dozemap2(start, inc / 10)._1), getMapResult(dozemap2(start + inc, inc / 10)._1))
      }
      println(" -> " + getMapResults(deuxPremiersIcrements))
      deuxPremiersIcrements
    }

    def getMilieu(start: BigInt, inc: BigInt): (List[BigInt], BigInt) = {
      val lm = List(dozemaps3(start, inc / 10), dozemaps3(start + inc, inc / 10))
      val result = List((lm.head.head * 7) + (lm.head.last * 3), (lm.last.head * 7) + (lm.last.last * 3),
        (((inc / 10 - lm.map(_.head).sum) * 7) + ((inc / 10 - lm.map(_.last).sum) * 3)))
      println("getMilieu : " + result)
      (result, getMapResult(result))
    }

    def F5(d: Int): BigInt = {
      val start = BigInt("1" + (2 to d).map(z => "0").mkString)
      val end = start * 10
      val delta = start / 10
      val inc = delta / 10
      println("")
      val lmilieu = ((d - 2) % 3) match {
        case 0 => dozemap2((start * 3) + delta, inc)
        case _ => getMilieu((start * 3) + delta, inc)
      }
      val r0 = dozemaps2(start * 3, inc / 10)
      println("\n" + d + " r0: " + r0)
      val r1 = dozemaps3(start + delta, inc / 10)
      println("\n" + d + " r1: " + r1)
      val result = ((((getMapResults(r0) * 40) + (getMapResult(lmilieu._1) * 6)) * 3) +
        (((getMapResult(lmilieu._1.reverse) * 7) + (getMapResults(r1) * 30)) * 6)
        )
      println("**[" + d + "][" + start + "-" + end + "]: " + result + "         delta: " + delta + ", inc: " + inc)
      result
    }

    F3(10, 100) shouldEqual 30
    dozemap2(1000, 10)
    dozemap2(10000, 100)
    dozemap2(100000, 100)

    (2 to 8).map(d => F2(d, 0, 10))
    (4 to 10).foreach(d => {
      val start = BigInt("1" + (2 to d).map(z => "0").mkString)
      println(start, start + 1000, getMapResult(List(
        F2(d, 0, 100),
        F2(d, 100, 100),
        F2(d, 200, 100))))
    })

    /*F4(4)
    F5(5) shouldEqual 30000
    F5(6) shouldEqual 290898
    F5(7) shouldEqual 3023178
    F5(8) shouldEqual 30000000
    F5(9)
    F5(10)
    F5(11)
    F5(12)*/
    t_la = timeStamp(t_la, "F5" + 6)


    var result = 0
    println("Euler706[" + result + "]")
    result shouldEqual 0
  }

}