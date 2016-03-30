import java.util.Calendar

import org.scalatest._
import Euler._

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
  "Euler158" should "be OK" in {
    println("Euler158")
    val az = "abcdefghjklmnopqrstuvwxyz"
    //val tableau = List(0, 0, 0, 6, 36, 240, 1800, 15120, 141120)
    val tableau = List(0, 0, 0, 4, 11, 26, 57, 120, 247)

    def lexleft(s: String): Int = {
      //println("        ", s, s.sliding(2).toList, s.sliding(2).toList.map(s2 => (s2, s2.last > s2.head)))
      s.sliding(2).toList.count(s2 => {
        s2.last > s2.head
      })
    }

    def calcule1(m: Int, n: Int): BigInt = {
      val s = az.substring(0, m)
      val scomb = s.combinations(n).toList
      //println(scomb)
      val z1 = scomb.map(s2 => {
        val sperm = s2.permutations.toList
        //println("  ", sperm)
        val z2 = BigInt(sperm.map(s3 => {
          val z3 = lexleft(s3)
          //println("    z3", s3, z3)
          z3
        }).count(_ == 1))
        println("  z2", sperm.head, z2)
        z2
      }).sum
      println(m, n, s, z1)
      z1
    }

    def getpermlleft(n: Int): BigInt = {
      //BigInt(tableau.apply(n))
      if (n == 3) {
        BigInt(4)
      } else {
        (getpermlleft(n - 1) * 2) + n - 1
      }

    }

    def calcule2(m: Int, n: Int): BigInt = {
      //az.substring(0, m).combinations(n).toList.map(s => BigInt(s.permutations.toList.map(lexleft(_)).sum)).sum
      //BigInt(az.substring(0, n).permutations.toList.map(lexleft(_)).sum*az.substring(0, m).combinations(n).toList.length)
      //BigInt(az.substring(0, n).permutations.toList.map(lexleft(_)).sum) * (factorielle(m) / (factorielle(n) * factorielle(m - n)))
      getpermlleft(n) * factorielle(m) / (factorielle(n) * factorielle(m - n))
    }

    factorielle(5) should be === 120
    factorielle(26).toString() should be === "403291461126605635584000000"
    println(tableau.zipWithIndex.map(z => (z._2, z._1, new EulerDiv(z._1).primes)).mkString("\n  ", "\n  ", "\n  "))
    println((3 to 27).map(z => (z, getpermlleft(z))).mkString("\n  ", "\n  ", "\n  "))
    println(List("abc", "hat", "zyx").map(lexleft(_)))
    calcule1(5, 4) should be === calcule2(5, 4)
    calcule1(7, 4) should be === calcule2(7, 4)
    calcule1(3, 3) should be === calcule2(3, 3)
    calcule1(8, 8) should be === calcule2(8, 8)
    calcule1(4, 3) should be === calcule2(4, 3)
    calcule1(6, 5) should be === calcule2(6, 5)
    calcule1(7, 6) should be === calcule2(7, 6)
    calcule1(8, 7) should be === calcule2(8, 7)
    calcule2(26, 3) should be === 10400

    val t_ici = timeStamp(t_start, "ici!")
    (3 to 7).map(m => {
      (3 to m).map(n => {
        calcule1(m, n)
      })
    })
    val t_la = timeStamp(t_ici, "la! ******************************")
    (3 to 7).map(m => {
      (3 to m).map(n => {
        println(m, n, calcule2(m, n))
      })
    })
    val t_la2 = timeStamp(t_la, "la2! ******************************")
    val z4 = (3 to 26).map(n => {
      val u = (26, n, calcule2(26, n))
        println(u)
        u
    })
    val t_la3 = timeStamp(t_la2, "la3! ******************************")

    val result = z4.maxBy(_._3)._3
    println("Euler158[" + result + "]")
    result.toString should be === "409511334375"
  }
}