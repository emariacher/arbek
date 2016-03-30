import java.util.Calendar

import org.scalatest._
import Euler._

class EulerMain extends FlatSpec with Matchers {
  "Euler158" should "be OK" in {
    println("Euler158")
    val az = "abcdefghjklmnopqrstuvwxyz"

    def lexleft(s: String) = {
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
          //println("    ", s3, z3)
          z3
        }).sum)
        println("  ", sperm.head, z2)
        z2
      }).sum
      println(m, n, s, z1)
      z1
    }

    def getpermlleft(n: Int): BigInt = {
      val tableau = List(0, 0, 0, 6, 36, 240, 1800, 15120, 141120)
      BigInt(tableau.apply(n))
    }

    def calcule2(m: Int, n: Int): BigInt = {
      //az.substring(0, m).combinations(n).toList.map(s => BigInt(s.permutations.toList.map(lexleft(_)).sum)).sum
      //BigInt(az.substring(0, n).permutations.toList.map(lexleft(_)).sum*az.substring(0, m).combinations(n).toList.length)
      //BigInt(az.substring(0, n).permutations.toList.map(lexleft(_)).sum) * (factorielle(m) / (factorielle(n) * factorielle(m - n)))
      getpermlleft(n) * (factorielle(m) / (factorielle(n) * factorielle(m - n)))
    }

    factorielle(5) should be === 120
    println(List("abc", "hat", "zyx").map(lexleft(_)))
    calcule1(5, 4) should be === calcule2(5, 4)
    calcule1(7, 4) should be === calcule2(7, 4)
    calcule1(3, 3) should be === calcule2(3, 3)
    calcule1(8, 8) should be === calcule2(8, 8)
    calcule1(4, 3) should be === calcule2(4, 3)

    val t_ici = timeStamp(t_start, "ici!")
    (3 to 7).map(m => {
      (3 to m).map(n => {
        calcule1(m, n)
      })
    })
    val t_la = timeStamp(t_ici, "la! ******************************")
    (3 to 7).map(m => {
      (3 to m).map(n => {
        calcule2(m, n)
      })
    })
    val t_la2 = timeStamp(t_la, "la2! ******************************")

    val result = 0
    println("Euler158[" + 0 + "]")
    result should be === 0
  }
}