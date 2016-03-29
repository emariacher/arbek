import java.util.Calendar

import org.scalatest._
import Euler._

class EulerMain extends FlatSpec with Matchers {
  "Euler100" should "be OK" in {
    println("Euler100")

    val limit: BigInt = powl(10, 11)

    def is50pourcent(total: Double): (Boolean, String) = {
      val totcar = math.pow(total - 0.5, 2)
      val totsqrt = math.sqrt(totcar / 2)
      val blue = math.ceil(totsqrt)
      val stat = (blue / total) * ((blue - 1) / (total - 1))
      if (math.abs(stat - 0.5) < 0.00000001) {
        val totalInt = BigDecimal(total).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
        val blueInt = BigDecimal(blue).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
        val statInt = (blueInt * (blueInt - 1)) * 2 == (totalInt * (totalInt - 1))
        (statInt, (totalInt, blueInt, stat, new EulerDiv(totalInt).primes, new EulerDiv(totalInt - 1).primes,
          new EulerDiv(blueInt).primes, new EulerDiv(blueInt - 1).primes, "-----------------------------").toString)
      } else {
        (false, (total, totcar, totsqrt, blue, stat).toString)
      }
    }

    def is50pourcent2(total: Double): (Boolean, BigInt) = {
      val totcar = math.pow(total - 0.5, 2)
      val totsqrt = math.sqrt(totcar / 2)
      val blue = math.ceil(totsqrt)
      val stat = (blue / total) * ((blue - 1) / (total - 1))
      if (math.abs(stat - 0.5) < 0.0000000000001) {
        val totalInt = BigDecimal(total).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
        val blueInt = BigDecimal(blue).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
        val statInt = (blueInt * (blueInt - 1)) * 2 == (totalInt * (totalInt - 1))
        if (statInt) {
          /*println(statInt, (totalInt, blueInt, stat, new EulerDiv(totalInt).primes, new EulerDiv(totalInt - 1).primes,
            new EulerDiv(blueInt).primes, new EulerDiv(blueInt - 1).primes))*/
          println(statInt, (totalInt, blueInt, stat, new EulerDiv(totalInt).primes, new EulerDiv(totalInt - 1).primes))
          (true, new EulerDiv(totalInt).primes.contains(2) match {
            case true => -3
            case _ => 3
          })
        } else {
          (false, 0)
        }
      } else {
        (false, 0)
      }
    }

    def is50pourcent3(total: Double, prev: List[BigInt]): (Boolean, BigInt, BigInt, List[BigInt]) = {
      val totcar = math.pow(total - 0.5, 2)
      val totsqrt = math.sqrt(totcar / 2)
      val blue = math.ceil(totsqrt)
      val stat = (blue / total) * ((blue - 1) / (total - 1))
      if (math.abs(stat - 0.5) < 0.0000000000001) {
        val totalInt = BigDecimal(total).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
        val blueInt = BigDecimal(blue).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
        val statInt = (blueInt * (blueInt - 1)) * 2 == (totalInt * (totalInt - 1))
        if (statInt) {
          val totprim = new EulerDiv(totalInt).primes
          val totprim1 = new EulerDiv(totalInt - 1).primes
          println("________________", totprim.contains(2))
          println(statInt, (totalInt, blueInt, stat, totprim, totprim1))
          (true, whichInc3(totprim1, prev), totalInt - 1, totprim)
        } else {
          (false, 0, 0, List.empty[BigInt])
        }
      } else {
        (false, 0, 0, List.empty[BigInt])
      }
    }

    def whichInc3(ll1: List[BigInt], ll2: List[BigInt]) = {
      val ll3 = ll1.filter(bi => !ll2.contains(bi))

      val nextinc = ll1.contains(2) match {
        case true => ll3.last * 4
        case _ => ll3.last
      }
      //println("whichInc3", ll1, ll2, ll3.last, nextinc)
      nextinc
    }

    def is50pourcent4(total: Double, prev: List[BigInt]): (Boolean, BigInt, BigInt, List[BigInt]) = {
      val totcar = math.pow(total - 0.5, 2)
      val totsqrt = math.sqrt(totcar / 2)
      val blue = math.ceil(totsqrt)
      val stat = (blue / total) * ((blue - 1) / (total - 1))
      if (math.abs(stat - 0.5) < 0.0000000000001) {
        val totalInt = BigDecimal(total).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
        val blueInt = BigDecimal(blue).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
        val statInt = (blueInt * (blueInt - 1)) * 2 == (totalInt * (totalInt - 1))
        if (statInt) {
          val totprim = new EulerDiv(totalInt).primes
          val totprim1 = new EulerDiv(totalInt - 1).primes
          println("________________", totprim.contains(2))
          println(statInt, (totalInt, blueInt, stat, totprim, totprim1))
          (true, whichInc4(totprim1, prev), totalInt - 1, totprim)
        } else {
          (false, 0, 0, List.empty[BigInt])
        }
      } else {
        (false, 0, 0, List.empty[BigInt])
      }
    }

    def whichInc4(ll1: List[BigInt], ll2: List[BigInt]) = {
      val ll3 = ll1.filter(bi => !ll2.contains(bi))

      val nextinc = ll1.contains(2) match {
        case true => ll3.last * 4
        case _ => ll3.reverse.take(2).product
      }
      ll1.contains(2) match {
        case true => println("whichInc4", ll1.contains(2), ll1, ll2, ll3.last, nextinc)
        case _ => println("whichInc4", ll1.contains(2), ll1, ll2, ll3.reverse.take(2).reverse, nextinc)
      }
      nextinc
    }

    val t_ici = timeStamp(t_start, "ici!")
    val z1 = (1 to 100000).map(b => is50pourcent(b.toDouble)).filter(_._1)
    println(z1.mkString("\n  ", "\n  ", "\n  "))
    val t_la = timeStamp(t_ici, "la!")

    var bi: BigInt = 0
    while (bi < 100000000) {
      val z = is50pourcent2(bi.toDouble)
      if (z._1) {
        bi += z._2
      }
      bi += 4
    }
    val t_la2 = timeStamp(t_la, "la2!")

    bi = 120
    var inc: BigInt = 4
    var prev: List[BigInt] = List(3, 7)
    while (bi < limit) {
      val z = is50pourcent3(bi.toDouble, prev)
      if (z._1) {
        bi = z._3
        inc = z._2
        prev = z._4
        println(bi, inc)
      }
      bi += inc
    }
    val t_la3 = timeStamp(t_la2, "la3!")

    bi = 137904
    inc = 4
    prev = List(3, 3, 11, 239)
    while (bi < limit) {
      val z = is50pourcent4(bi.toDouble, prev)
      if (z._1) {
        bi = z._3
        inc = z._2
        prev = z._4
        println(bi, inc)
      }
      bi += inc
    }
    val t_la4 = timeStamp(t_la3, "la4!")



    val result = 0
    println("Euler100[" + 0 + "]")
    result should be === 0
  }
}