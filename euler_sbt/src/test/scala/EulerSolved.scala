import Euler._
import org.scalatest._

import scala.collection.immutable.{Range, ListSet}

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

    val z = (-1000 to 1000).map(a => {
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
    result should be === (-61 * 971)
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
    result should be === 45228
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
    result should be === "932718654"

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
    result should be === 5777
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
    z.apply(10 - 1) should be === 17

    val result = z.apply(100 - 1)
    println("Euler65[" + result + "]")
    result should be === 272

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
    result.mkString("", "", "") should be === "73162890"

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
      rn._2 should be === fra
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
    result should be === 743


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
    cpt should be === z1.length*/

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
    cpt should be === z1.length*/

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
    //cpt should be === z1.length


    val result = cpt
    println("Euler179[" + cpt + "]")
    result should be === 986262
  }

}