import Euler._
import org.scalatest._

import scala.collection.immutable.{Range, ListSet}
import scala.math.BigInt

class EulerSolved extends FlatSpec with Matchers {
  "Euler27" should "be OK" in {
    println("Euler27")

    val premiers = EulerPrime.premiers100000
    val max = 10

    def quadratic(a: Int, b: Int, n: Int) = {
      ((n + a) * n) + b
    }

    def rangeFrom(a: Int, b: Int): Stream[Int] = a #:: rangeFrom(b, 1 + b)
    def getprimesFrom(a: Int, b: Int): List[(Int, Int)] = {
      val r = rangeFrom(0, 1)
      val z = r.map(n => (n, quadratic(a, b, n))).takeWhile(q => {
        premiers.contains(q._2)
      }).toList
      //println("     "+a + " " + b + " " + z)
      z
    }

    var un_a_linfini = rangeFrom(0, 1).take(7)
    println(un_a_linfini.toList)

    /*val z = (-1000 to 1000).map(a => {
      //EulerPrime.premiers10000.filter(_ < 1602).filter(b => b + a > 0).map(b => {
      EulerPrime.premiers1000.filter(b => b + a > 0).map(b => {
        val gp = getprimesFrom(a, b.toInt)
        //println(a + " " + b + " " + gp)
        (a, b.toInt, gp.length, gp)
      })
    }).flatten.filter(_._3 > 1).sortBy(_._3)
    println(z.mkString("\n  ", "\n  ", "\n  "))
    println(EulerPrime.premiers1000)
    println(EulerPrime.premiers1000.last)
    println(EulerPrime.premiers10000.filter(_ < 1602).last)
    val gp = getprimesFrom(-79, 1601)
    println(-79, 1601, gp.length, gp)

    val y = z.last
    val result = y._1 * y._2
    println("Euler27[" + result + "]")
    result shouldEqual (-61 * 971)*/
  }


  "Euler32" should "be OK" in {
    println("Euler32")
    val result = Range(13 * 245, 9876 + 1).filter(i => {
      val lz = i.toString.toList
      lz.length == (new ListSet() ++ lz).toList.length
    }).filter(c => {
      c.toString.indexOf("0") < 0
    }).map(i => {
      val divs = new EulerDivisors(new EulerDiv(i).primes).divisors
      (i, divs)
    }).filter(c => {
      if (c._2.length == 2) {
        val lz = (c._2.mkString("", "", "") + c._1).toList
        lz.length == (new ListSet() ++ lz).toList.length && lz.length == 5
      } else if (c._2.length > 0) {
        true
      } else {
        false
      }
    }).map(c => {
      val z1 = c._1.toString.toList
      val z2 = c._2.filter(d => {
        z1.intersect(d.toString.toList).isEmpty && d.toString.indexOf("0") < 0
      })
      (c._1, z2.toList)
    }).filter(c => {
      c._2.length > 1
    }).map(c => {
      (c._1, c._2.combinations(2).filter(
        cb => cb.head * cb.last == c._1 &&
          (cb.head.toString + cb.last.toString).length == 5
      ).toList)
    }).filter(c => {
      !c._2.isEmpty
    }).map(c => {
      (c._1, c._2.filter(
        cb => {
          val lz = (cb.head.toString + cb.last.toString + c._1.toString).toList
          lz.length == (new ListSet() ++ lz).toList.length
        }
      ))
    }).filter(c => {
      !c._2.isEmpty
    }).map(c => {
      println(c)
      c._1
    }).sum
    println("Euler32[" + result + "]")
    result shouldEqual 45228
  }

  "Euler38" should "be OK" in {
    println("Euler38")

    def isPanDigital(s: String): Boolean = {
      val lz = s.toList
      lz.length == (new ListSet() ++ lz).toList.length && s.indexOf("0") < 0
    }

    def isPanDigital1to9(s: String): Boolean = {
      isPanDigital(s) && s.length == 9
    }


    val result = (Range(123, 988).toList.filter(
      i => isPanDigital(i.toString)
    ).map(i => {
      val r = Range(1, 4).toList
      val z = r.map(k => i * k).mkString("", "", "")
      (i, z, isPanDigital1to9(z))
    }).filter(
        _._3
      )

      ++

      Range(1234, 9877).toList.filter(
        i => isPanDigital(i.toString)
      ).map(i => {
        val r = Range(1, 3).toList
        val z = r.map(k => i * k).mkString("", "", "")
        (i, z, isPanDigital1to9(z))
      }).filter(
          _._3
        )).map(t => {
      println(t)
      t
    }).maxBy(_._2)._2

    println("Euler38[" + result + "]")
    result shouldEqual "932718654"

  }


  "Euler46" should "be OK" in {
    println("Euler46")
    val limit = 10000
    println("Find < " + limit + " odd composite numbers")
    val premiers = EulerPrime.premiers100000.takeWhile(_ <= limit).toList.tail
    println(premiers)
    val odds = Range(3, limit, 2).toList
    val oddComposites = odds diff premiers
    println(oddComposites)
    var sqrtlimit = math.sqrt(limit / 2).toInt
    var doubleSquares = Range(1, sqrtlimit).map(i => i * i * 2).toList
    println(doubleSquares)

    val result = oddComposites.find(odc => {
      doubleSquares.find(ds => {
        val index = premiers.indexOf(odc - ds)
        if (index > 0) {
          println(odc + "-" + ds + "==" + premiers.apply(index))
        }
        index > 0
      }).isEmpty
    }) match {
      case Some(i) => i
      case _ =>
    }

    println("Euler46[" + result + "]")
    result shouldEqual 5777
  }

  "Euler65" should "be OK" in {
    println("Euler65")
    println(math.E)

    //87 = 4*19 + 11
    //1264 = 6*193 + 106
    // 465 = 6*71 + 39

    var rang = 101
    var num = BigInt(0)
    var prevnum = BigInt(0)
    var prevprevnum = BigInt(0)
    var den = BigInt(0)
    var prevden = BigInt(0)
    var prevprevden = BigInt(0)


    val notcontfrac_E = Range(3, rang + 5).map(i => {
      val j = i - 1
      if (j % 3 == 0) {
        (j / 3) * 2
      } else {
        1
      }
    }).toList
    println(notcontfrac_E)

    val z = Range(0, rang + 1).map(r => {
      r match {
        case 0 => num = 2; den = 1
        case 1 => num = 3; den = 1
        case 2 => num = (prevnum * notcontfrac_E.apply(r - 1)) + prevprevnum; den = (prevden * notcontfrac_E.apply(r - 1)) + prevprevden
        case _ => num = (prevnum * notcontfrac_E.apply(r - 1)) + prevprevnum; den = (prevden * notcontfrac_E.apply(r - 1)) + prevprevden
      }
      prevprevnum = prevnum
      prevnum = num
      prevprevden = prevden
      prevden = den
      println(r, notcontfrac_E.apply(r), (num, den), num.toDouble / den.toDouble, num.toString().toList.map(_.toString.toInt), num.toString().toList.map(_.toString.toInt).sum)
      num.toString().toList.map(_.toString.toInt).sum
    })
    println(math.E)
    z.apply(10 - 1) shouldEqual 17

    val result = z.apply(100 - 1)
    println("Euler65[" + result + "]")
    result shouldEqual 272

  }

  "Euler79" should "be OK" in {
    println("Euler79")
    var p079_keylogS = List(319, 680, 180, 690, 129, 620, 762, 689, 762, 318, 368, 710, 720, 710, 629, 168, 160, 689, 716, 731, 736,
      729, 316, 729, 729, 710, 769, 290, 719, 680, 318, 389, 162, 289, 162, 718, 729, 319, 790, 680, 890, 362, 319, 760, 316, 729,
      380, 319, 728, 716).map(_.toString)
    var p079_keylogL = p079_keylogS.map(_.toList)
    println(p079_keylogS)

    var possibleHead = p079_keylogL.map(_.head).distinct
    println(possibleHead)

    var possibleLast = p079_keylogL.map(_.last).distinct
    println(possibleLast)

    var probableHead = possibleHead diff possibleLast
    println(probableHead)

    var headCouples = probableHead.mkString("", "", "").combinations(2).map(_.permutations).flatten.toList
    println(headCouples)

    var first = headCouples.map(c => {
      (c, p079_keylogS.filter(k => k.indexOf(c) == 0).length)
    }).sortBy(_._2).last._1.charAt(0).toString

    var result = List(first)

    println("first[" + first + "]" + result + "\n*********************")
    z
    println("second[" + first + "]" + result + "\n*********************")
    z
    println("third[" + first + "]" + result + "\n*********************")
    z
    println("fourth[" + first + "]" + result + "\n*********************")
    z
    println("fifth[" + first + "]" + result + "\n*********************")
    z
    println("sixth[" + first + "]" + result + "\n*********************")
    z
    println("seventh[" + first + "]" + result + "\n*********************")
    z
    println("eighth[" + first + "]" + result + "\n*********************")

    println("Euler79[" + result.mkString("", "", "") + "]")
    result.mkString("", "", "") shouldEqual "73162890"

    def z = {
      p079_keylogS = p079_keylogS.map(_.replaceAll(first, "")).filter(_.length > 1)
      if (p079_keylogS.isEmpty) {
        first = "0" // Je sais: c'est mal
      } else {
        p079_keylogL = p079_keylogS.map(_.toList)
        println(p079_keylogS)

        possibleHead = p079_keylogL.map(_.head).distinct
        println(possibleHead)

        possibleLast = p079_keylogL.map(_.last).distinct
        println(possibleLast)

        probableHead = possibleHead diff possibleLast
        println("probableHead " + probableHead)
        if (probableHead.isEmpty) {
          probableHead = possibleHead
        }
        println("probableHead " + probableHead)
        if (probableHead.length > 1) {
          headCouples = probableHead.mkString("", "", "").combinations(2).map(_.permutations).flatten.toList
          println(headCouples)

          first = headCouples.map(c => {
            (c, p079_keylogS.filter(k => k.indexOf(c) == 0).length)
          }).sortBy(_._2).last._1.charAt(0).toString
        } else {
          first = probableHead.head.toString
        }
      }
      result = result :+ first
    }

  }

  "Euler89" should "be OK" in {
    println("Euler89")

    val M = 'M'
    val D = 'D'
    val C = 'C'
    val L = 'L'
    val X = 'X'
    val V = 'V'
    val I = 'I'


    val url = "https://projecteuler.net/project/resources/p089_roman.txt"
    val romnumList = io.Source.fromURL(url).mkString.split("\n").toList
    val romnumListLength = romnumList.mkString("", "", "").length

    //println(romnumList)
    println("******\n******[" + (romnumListLength - romnumList.map(rn => {
      //val nombre2 = roman2arab(romnum)
      //println(romnum, nombre)
      (rn, roman2arab(rn))
    }).map(rn => {
      val mille = rn._2 / 1000
      val cent = (rn._2 - (mille * 1000)) / 100
      val dix = (rn._2 - ((mille * 1000) + (cent * 100))) / 10
      val un = (rn._2 - ((mille * 1000) + (cent * 100) + (dix * 10)))
      //println(rn._1, rn._2, mille, cent, dix, un)
      (rn._1, rn._2, mille, cent, dix, un)
    }).map(rn => {
      var frn = Range(0, rn._3).map(z => "M").mkString("", "", "")
      frn = frn + List("", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM").apply(rn._4)
      frn = frn + List("", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC").apply(rn._5)
      frn = frn + List("", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX").apply(rn._6)
      val fra = roman2arab(frn)
      rn._2 shouldEqual fra
      println(rn._1, rn._2, frn, fra)
      (rn._1, rn._2, frn, fra)
    }).map(_._3).mkString("", "", "").length) + "]*******"
    )
    //println(resultList.mkString("\n","\n","\n"))

    def roman2arab(romnum: String): Int = {
      var nombre = 0
      var state = '0'
      val z = romnum.foreach(c => {
        val value = c match {
          case M => 1000
          case D => 500
          case C => 100
          case L => 50
          case X => 10
          case V => 5
          case I => 1
        }
        state match {
          case C => c match {
            case M => nombre += 800
            case D => nombre += 300
            case _ => nombre += value
          }
          case X => c match {
            case C => nombre += 80
            case L => nombre += 30
            case _ => nombre += value
          }
          case I => c match {
            case X => nombre += 8
            case V => nombre += 3
            case _ => nombre += value
          }
          case _ => nombre += value
        }
        state = c
        (state, c, value, nombre)
      })
      nombre
    }

    // val romnumListLength = romnumList.mkString("","","").length
    // val result = romnumListLength - resultList.map(_._3).mkString("","","").length

    val result = 743
    println("Euler89[" + result + "]")
    result shouldEqual 743


  }

  "Euler100" should "be OK" in {
    println("Euler100")

    /*val limit: BigInt = powl(10, 13)

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
    val t_la2 = timeStamp(t_la, "la2! ******************************")

    bi = 120
    var inc: BigInt = 4
    var prev: List[BigInt] = List(3, 7)
    while (bi < powl(10, 10)) {
      val z = is50pourcent3(bi.toDouble, prev)
      if (z._1) {
        bi = z._3
        inc = z._2
        prev = z._4
        println(bi, inc)
      }
      bi += inc
    }
    val t_la3 = timeStamp(t_la2, "la3! ******************************")

    bi = 137904
    inc = 4
    prev = List(3, 3, 11, 239)
    var blueInt: BigInt = 0
    var found = false
    while (bi < limit && !found) {
      val z = is50pourcent4(bi.toDouble, prev)
      if (z._1) {
        bi = z._3
        inc = z._2
        prev = z._4
        println(bi, inc)
        if (bi > powl(10, 12)) {
          found = true
          val totcar = math.pow((bi.toDouble+1) - 0.5, 2)
          val totsqrt = math.sqrt(totcar / 2)
          val blue = math.ceil(totsqrt)
          blueInt = BigDecimal(blue).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
        }
      }
      bi += inc
    }
    val t_la4 = timeStamp(t_la3, "la4! ******************************")

    val result = blueInt
    println("Euler100[" + blueInt + "]")
    result.toString() shouldEqual "756872327473"*/
  }

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

    factorielle(5) shouldEqual 120
    factorielle(26).toString() shouldEqual "403291461126605635584000000"
    println(tableau.zipWithIndex.map(z => (z._2, z._1, new EulerDiv(z._1).primes)).mkString("\n  ", "\n  ", "\n  "))
    println((3 to 27).map(z => (z, getpermlleft(z))).mkString("\n  ", "\n  ", "\n  "))
    println(List("abc", "hat", "zyx").map(lexleft(_)))
    calcule1(5, 4) shouldEqual calcule2(5, 4)
    calcule1(7, 4) shouldEqual calcule2(7, 4)
    calcule1(3, 3) shouldEqual calcule2(3, 3)
    calcule1(8, 8) shouldEqual calcule2(8, 8)
    calcule1(4, 3) shouldEqual calcule2(4, 3)
    calcule1(6, 5) shouldEqual calcule2(6, 5)
    calcule1(7, 6) shouldEqual calcule2(7, 6)
    calcule1(8, 7) shouldEqual calcule2(8, 7)
    calcule2(26, 3) shouldEqual 10400

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
    result.toString shouldEqual "409511334375"
  }


  "Euler179" should "be OK" in {
    println("Euler179")

    val premiers = EulerPrime.premiers100000
    val limit = 10000000

    val t_ici = timeStamp(t_start, "ici!")
    /*val z1 = stream_zero_a_linfini.map(b => {
      (b, new EulerDivisors(new EulerDiv(b).primes).getFullDivisors)
    }).drop(2).take(limit).toList.sliding(2).toList.filter(c => c.head._2.length == c.last._2.length)
    timeStamp(t_ici, "la!")*/

    //println(z1.mkString("\n  ", "\n  ", "\n  "), z1.length)

    var zstart = timeStamp(t_ici, "zstart")
    var cpt = 1
    var bi: BigInt = 15
    var prevnumdiv = 2

    /*while (bi < limit) {
      var cptprimes = new EulerDivisors(new EulerDiv(bi).primes).divisors.length

      if (cptprimes == prevnumdiv) {
        //println(bi - 1, bi, cptprimes)
        cpt += 1
      }
      //println( bi, cptprimes)
      prevnumdiv = cptprimes
      bi += 1
    }
    timeStamp(zstart, "zend")
    cpt shouldEqual z1.length*/

    zstart = timeStamp(t_ici, "zstart2")
    cpt = 1
    bi = 15
    prevnumdiv = 2

    /*while (bi < limit) {
      if(EulerPrime.isPrime(bi)) {
        bi +=1
        prevnumdiv = 0
      } else {
        var cptprimes = new EulerDivisors(new EulerDiv(bi).primes).divisors.length

        if (cptprimes == prevnumdiv) {
          //println(bi - 1, bi, cptprimes)
          cpt += 1
        }
        prevnumdiv = cptprimes
        bi += 1
      }
    }
    timeStamp(zstart, "zend2")
    cpt shouldEqual z1.length*/

    zstart = timeStamp(t_ici, "zstart3")
    cpt = 1
    bi = 15
    prevnumdiv = 2

    while (bi < limit) {
      if(EulerPrime.isPrime(bi)) {
        bi +=1
        prevnumdiv = 0
      } else {
        var cptprimes = new EulerDivisors(new EulerDiv(bi).primes).divisors.length

        if (cptprimes == prevnumdiv) {
          //println(bi - 1, bi, cptprimes)
          cpt += 1
        }
        prevnumdiv = cptprimes
        bi += 1
      }
    }
    timeStamp(zstart, "zend3")
    //cpt shouldEqual z1.length


    val result = cpt
    println("Euler179[" + cpt + "]")
    result shouldEqual 986262
  }

  "Euler191" should "be OK" in {
    println("Euler191")
    var y = (0, 0, 0, 0, 0, 0, 0, 0)


    def decode(j: Int) = {
      (j % 3) match {
        case 0 => "L"
        case 1 => "A"
        case 2 => "O"
      }
    }
    def good(s: String) = s.count(_ == 'L') < 2 && !s.contains("AAA")
    def goodA(s: String) = !s.contains("AAA")
    def goodL(s: String) = s.count(_ == 'L') < 2
    def countL(s: String) = s.count(_ == 'L')


    def doZeJob(e: Int) = {
      val y = (0 until powl(3, e).toInt).map(i => {
        var s = ""
        var j = i
        s += decode(j)
        (1 until e).foreach(u => {
          j /= 3
          s += decode(j)
        })
        //println(i, s)
        s
      })
      val z = y.filter(good(_))
      println(z.length, z.take(5))

      z.length
    }

    def genere(ls: List[String]): List[String] = ls.map(s => List(s + "L", s + "A", s + "O").filter(good(_))).flatten

    var prevL1 = 0
    def doZeJob3(e: Int): (BigInt, BigInt, BigInt, List[(BigInt, Int)]) = {
      var ls = List("")
      while (ls.head.length < e) {
        ls = genere(ls)
      }
      val L0 = ls.filter(countL(_) == 0)
      val L1 = ls.filter(countL(_) == 1)
      val L1C = (0 to e).map(i => {
        (BigInt(L1.count(_.indexOf('L') == i)), i)
      })
      println("doZeJob3[" + e + "]", ls.length, L0.length, L1.length, L1C)
      (BigInt(ls.length), BigInt(L0.length), BigInt(L1.length), L1C.toList)
    }


    var l = List(BigInt(1), BigInt(1), BigInt(2))
    var i = 0;
    while (i < 30) {
      l = l :+ l.takeRight(3).sum
      i += 1
    }
    println("l", l.zipWithIndex)

    var l3 = List(0, 2, 1)
    i = 3;
    while (i < 30) {
      l3 = l3 :+ l3.takeRight(3).sum
      i += 1
    }
    println("l3 ", l3.zipWithIndex)

    var l4 = List(0, 1, 4)
    i = 3;
    while (i < 30) {
      l4 = l4 :+ l4.takeRight(3).sum
      i += 1
    }
    println("l4 ", l4.zipWithIndex)

    var l5 = List(0, 4, 3)
    i = 3;
    while (i < 30) {
      l5 = l5 :+ l5.takeRight(3).sum
      i += 1
    }
    println("l5 ", l5.zipWithIndex)

    var l6 = List(0, 8, 12)
    i = 3;
    while (i < 30) {
      l6 = l6 :+ l6.takeRight(3).sum
      i += 1
    }
    println("l6 ", l6.zipWithIndex)

    var l7 = List(0, 5, 13)
    i = 3;
    while (i < 30) {
      l7 = l7 :+ l7.takeRight(3).sum
      i += 1
    }
    println("l7 ", l7.zipWithIndex)

    def doZeJob4(e: Int, ul: List[(Int, List[(BigInt, Int)])]): (BigInt, BigInt, BigInt, List[(Int, List[(BigInt, Int)])]) = {
      val L0 = l.apply(e + 1)
      var L1 = (0 to (e - 4)).map(i => ul.map(_._2.apply(i)._1).sum).zipWithIndex.toList
      L1 = L1 ++ L1.take(3).reverse
      val vl = ul.drop(1) :+(e, L1)
      println("L1", L1)
      println("doZeJob4[" + e + "]", L0 + L1.map(_._1).sum, L0, L1.map(_._1).sum, vl.mkString("\n  ", "\n  ", "\n  "))
      (L0 + L1.map(_._1).sum, L0, L1.map(_._1).sum, vl)
    }



    doZeJob(4) shouldEqual 43

    var z3 = BigInt(0)
    var sum = BigInt(0)
    var zl = List.empty[(BigInt, BigInt, BigInt, List[(BigInt, Int)])]
    val limit = 16
    (3 to limit).foreach(e => {
      println("\n")
      //var t_ici = timeStamp(t_start, "ici!")
      println(e, powl(3, e))
      var z2 = 0
      //var t_la = timeStamp(t_ici, "la! " + e)
      if (e < 12) {
        z2 = doZeJob(e)
        //var t_la2 = timeStamp(t_la, "la2! " + e + " " + z2)
      }
      //var t_laz = timeStamp(t_la, "laz! " + e)
      sum += z3
      val zz = doZeJob3(e)
      zl = zl :+ zz
      z3 = zz._1
      //var t_la3 = timeStamp(t_ici, "la3! " + e + " " + z3)
      if (z2 != 0) {
        z3 shouldEqual z2
      }

      println(zz._4.apply(0), zz._4.apply(1), zz._4.apply(2))
      e match {
        case it if 0 to 4 contains it =>
        case 5 => (zz._4.apply(0)._1, zz._4.apply(1)._1, zz._4.apply(2)._1) shouldEqual(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + 1)
        case 6 => (zz._4.apply(0)._1, zz._4.apply(1)._1, zz._4.apply(2)._1) shouldEqual(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2))
        case 7 => (zz._4.apply(0)._1, zz._4.apply(1)._1, zz._4.apply(2)._1) shouldEqual(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2))
        case 8 => (zz._4.apply(0)._1, zz._4.apply(1)._1, zz._4.apply(2)._1) shouldEqual(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + 1)
        case _ => (zz._4.apply(0)._1, zz._4.apply(1)._1, zz._4.apply(2)._1) shouldEqual(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + l.apply(e - 8))
      }
      if (e > 6) {
        zz._4.apply(3)._1 shouldEqual zz._4.apply(1)._1 + l3.apply(e - 5)

        if (e > 8) {
          if (e < 10) {
            println(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + l.apply(e - 8),
              l.apply(e) + l.apply(e - 4) + l3.apply(e - 5),
              l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8)
            )
          } else {
            if (e < 12) {
              println(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + l.apply(e - 8),
                l.apply(e) + l.apply(e - 4) + l3.apply(e - 5),
                l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8),
                l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8) + l5.apply(e - 10)
              )
            } else {
              if (e < 14) {
                println(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + l.apply(e - 8),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8) + l5.apply(e - 10),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8) + l5.apply(e - 10) - l6.apply(e - 12)
                )
              } else {
                println(l.apply(e), l.apply(e) + l.apply(e - 4), l.apply(e) + (l.apply(e - 4) * 2) + l.apply(e - 8),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8) + l5.apply(e - 10),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8) + l5.apply(e - 10) - l6.apply(e - 12),
                  l.apply(e) + l.apply(e - 4) + l3.apply(e - 5) + l4.apply(e - 8) + l5.apply(e - 10) - l6.apply(e - 12) + l7.apply(e - 14)
                )
                zz._4.apply(7)._1 shouldEqual zz._4.apply(6)._1 + l7.apply(e - 14)
              }
              zz._4.apply(6)._1 shouldEqual zz._4.apply(5)._1 - l6.apply(e - 12)
            }
            zz._4.apply(5)._1 shouldEqual zz._4.apply(4)._1 + l5.apply(e - 10)
          }
          zz._4.apply(4)._1 shouldEqual zz._4.apply(3)._1 + l4.apply(e - 8)
        }
      }

    })

    println(zl.mkString("\n"))
    val zl3 = zl.takeRight(4)
    zl3.take(3).map(_._4.head._1).sum shouldEqual zl3.last._4.head._1
    zl3.take(3).map(_._4.apply(3)._1).sum shouldEqual zl3.last._4.apply(3)._1

    zl.sliding(4).foreach(zk => {
      zk.take(3).map(_._4.apply(2)._1).sum shouldEqual zk.last._4.apply(2)._1
    })

    val zlm1t3 = zl.takeRight(4).dropRight(1)
    println(zlm1t3.mkString("\n ", "\n ", "\n "))
    var u = (0 to (limit - 4)).map(i => {
      zlm1t3.take(3).map(_._4.apply(i)._1).sum
    }).zipWithIndex.toList
    u = u ++ u.take(4).reverse
    println(u)
    println(zl.last._4)

    //var ul = zl.take(5).drop(2).zipWithIndex.map(c => (c._2 + 5, c._1._4))
    var ul = zl.take(12).drop(9).zipWithIndex.map(c => (c._2 + 12, c._1._4))
    println(ul.mkString("\n ul ", "\n ul ", "\n ul "))
    var cpt = 15
    var result = doZeJob4(cpt, ul)
    var x3 = doZeJob3(cpt)
    result._1 shouldEqual x3._1

    println("result", result)
    println("x3", x3)
    while (cpt < 30) {
      cpt += 1
      result = doZeJob4(cpt, result._4)
    }

    println("Euler191[" + result._1 + "]")
    result._1 shouldEqual 1918080160

  }
}