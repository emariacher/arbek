import java.util.Calendar

import Euler._
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.TreeSet
import scala.math.BigInt

/**
 * Created by emariacher on 21.03.2016.
 */
class EulerNotYetSolved extends FlatSpec with Matchers {
  "Euler211" should "be OK" in {
    println("Euler211")


    def sigma2(bi: BigInt): BigInt = new EulerDivisors(new EulerDiv(bi)).getFullDivisors.map(d => d * d).sum

    def sigma2v(bi: BigInt) = {
      val primes = new EulerDiv(bi).primes
      val div = new EulerDivisors(primes).getFullDivisors
      val res = div.map(d => d * d)
      val sqrt = BigDecimal(math.sqrt(res.sum.toDouble)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
      (bi, res.sum, sqrt, sqrt.toDouble / bi.toDouble, primes, sqrt - bi, div, res)
    }

    sigma2(10) shouldEqual 130
    val z = (2 to 200000).map(sigma2v(_)).toList
    val max = z.maxBy(_._2)
    val sqrtmax = BigDecimal(math.sqrt(max._2.toDouble)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
    println("max", max, sqrtmax)
    val t_ici = timeStamp(t_start, "ici!")
    val lsq = TreeSet[BigInt]() ++ (0 to sqrtmax.toInt).map(d => BigInt(d * d))
    val x = z.filter(y => lsq.contains(y._2))
    println(x.mkString("\n  ", "\n  ", "\n  "))
    val t_la = timeStamp(t_ici, "la!")
    val sumlx = x.last._2
    val sqslx = BigDecimal(math.sqrt(sumlx.toDouble)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
    println(sumlx, sqslx)
    sqslx * sqslx shouldEqual sumlx


    var result = 0
    println("Euler211[" + result + "]")
    result shouldEqual 0

  }

  "Euler243" should "be OK" in {
    println("Euler243")
    val premiers = (new CheckEulerPrime(2000000, 10000)).premiers

    def resilience(d: Int) = {
      var t_ici = Calendar.getInstance()
      val primes = new EulerDiv2(d, premiers, false).primes
      val divisors = new EulerDivisors(primes).divisors
      val resil = Range(1, d, 2).filter(i => {
        //divisors.filter(div => i % div == 0).isEmpty
        divisors.find(div => i % div == 0) match {
          case Some(i) => false
          case _ => true
        }
      })
      val pgcd_primes = new EulerDiv(resil.length).primes intersect primes
      println("" + d + "\t" + resil.length + "/" + (d - 1) + "\t" + (1.0 * resil.length / (d - 1)),
        "ici " + timeStampD(t_ici, "ici", false))
      //println(resil.toList)
      (d, "" + resil.length + "/" + (d - 1), (1.0 * resil.length / (d - 1)), resil.toList)
    }

    def resilience3(l: List[BigInt]) = {
      var t_ici = Calendar.getInstance()
      val d = l.product
      val ld = l.distinct
      val lp1 = List(BigInt(1)) ++ premiers.takeWhile(_ < d).filter(bi => !ld.contains(bi))
      val lp2 = lp1.takeWhile(p => (p * p) <= d).flatMap(p => {
        //println("in lp2", p, lp1.takeWhile(bi => bi.toDouble < d.toDouble / p.toDouble).map(_ * p))
        lp1.takeWhile(bi => bi.toDouble < d.toDouble / p.toDouble).map(_ * p)
      }).sorted.distinct
      val lp3 = lp2.takeWhile(p => (p * p * p) <= d).flatMap(p => {
        //println("in lp3", p, lp2.takeWhile(bi => bi.toDouble < d.toDouble / p.toDouble).map(_ * p))
        lp2.takeWhile(_.toDouble < d.toDouble / p.toDouble).map(_ * p)
      }).sorted.distinct
      val lp4 = lp3.takeWhile(p => (p * p * p * p) <= d).flatMap(p => {
        //println("in lp4", p, lp3.takeWhile(bi => bi.toDouble < d.toDouble / p.toDouble).map(_ * p))
        lp3.takeWhile(_.toDouble < d.toDouble / p.toDouble).map(_ * p)
      }).sorted.distinct
      /*println("  lp1", lp1)
      println("  lp2", lp2)
      println("  lp3", lp3)
      println("  lp4", lp4)*/
      val ls = lp4
      println("" + d + "\t" + ls.size + "/" + (d - 1) + "\t" + 1.0 * ls.size / (d.toDouble - 1),
        "ici3 " + timeStampD(t_ici, "ici", false))
      if (lp4.diff(lp3).nonEmpty) {
        val pp = lp2.tail.head
        println(pp, math.pow(pp.toDouble, 4), d, "lp4.diff(lp3)", lp4.diff(lp3).map(bi => (bi, new EulerDiv(bi).primes)).take(2))
        (math.pow(pp.toDouble, 4) < d.toDouble) shouldEqual true
        (math.pow(pp.toDouble, 5) < d.toDouble) shouldEqual false
      }
      (d, "" + ls.size + "/" + (d - 1), 1.0 * ls.size / (d.toDouble - 1), ls.sorted)
    }


    def resilience4(l: List[BigInt]) = {
      var t_ici = Calendar.getInstance()
      val d = l.product
      print("" + d, l.last, "")
      val ld = l.distinct
      var lp = List(BigInt(1)) ++ premiers.takeWhile(_ < d).filter(bi => !ld.contains(bi))
      var oldlp = List[BigInt]()
      var exponent = 2
      while (lp.diff(oldlp).nonEmpty) {
        oldlp = lp
        lp = lp.takeWhile(p => math.pow(p.toDouble, exponent) <= d.toDouble).flatMap(p => {
          val lp2 = lp.takeWhile(bi => bi.toDouble < d.toDouble / p.toDouble).map(_ * p)
          //println("  in while", exponent, p, lp.takeWhile(bi => bi.toDouble < d.toDouble / p.toDouble).map(_ * p))
          lp2
        }).sorted.distinct
        exponent += 1
      }
      val ls = lp
      println("\t" + ls.size + "/" + (d - 1) + "\t" + 1.0 * ls.size / (d.toDouble - 1), exponent,
        "ici4 " + timeStampD(t_ici, "ici", false))
      (d, "" + ls.size + "/" + (d - 1), 1.0 * ls.size / (d.toDouble - 1), ls.sorted)
    }

    var t_la = Calendar.getInstance()
    //resilience(12) shouldEqual(4, 11)
    println(1.0 * 15499 / 94744)

    resilience(2 * premiers.take(5).product.toInt)
    resilience(2 * 2 * 3 * 3 * premiers.take(5).product.toInt)
    var lresilience = List[Double]()
    (3 to 7).foreach(i => {
      val z = resilience(premiers.take(i).product.toInt)
      val y = resilience4(premiers.take(i).toList)
      val ldiff = z._4.diff(y._4)
      //println(premiers.take(i).product.toInt, "ldiff", ldiff.size, ldiff.map(bi => (bi, new EulerDiv(bi).primes)).take(40))
      z._4.diff(y._4).size shouldEqual 0
      val z3 = resilience(premiers.take(i).product.toInt * 3)
      val y3 = resilience4(premiers.take(i).toList :+ 3)
      val ldiff3 = z3._4.diff(y3._4)
      //println(premiers.take(i).product.toInt * 3, "ldiff3", ldiff3.size, ldiff3.map(bi => (bi, new EulerDiv(bi).primes)).take(40))
      z3._4.diff(y3._4).size shouldEqual 0
      lresilience = lresilience :+ y._3
    })
    t_la = timeStamp(t_la, "là 1")
    println(premiers.take(9).product.toInt)
    println(lresilience)
    println(lresilience.sliding(2).map(c => c.head / c.last).toList)
    println(lresilience.sliding(2).map(c => c.head / c.last).toList.sliding(2).map(c => c.head / c.last).toList)

    val result = 0
    println("Euler243[" + result + "]")
    result shouldEqual 0
  }


  "Euler457" should "be OK" in {
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
  }


  "Euler493" should "be OK" in {
    println("Euler493")

    def dozejob1(s: String, sampleSize: Int, prev: List[(Int, Int)]) = {
      val z = s.toSeq.combinations(sampleSize).toList
      val y = z.map(e => e.toSeq.distinct.size).groupBy(u => u)
      val x = y.toList.map(a => (a._1, a._2.size)).sortBy(_._1)
      val w = x.map(a => a._1 * a._2).sum
      w shouldEqual z.map(e => e.toSeq.distinct.size).sum
      println(sampleSize, w.toDouble / z.size.toDouble, z.size, z.take(5), z.last)
      println("  " + x)
      if (prev.nonEmpty) {
        val v = prev.dropWhile(c => c._1 < x.head._1)
        println("    " + x.zip(v).map(c => (c._1._1, c._1._2 - c._2._2)))
      }
      x.map(_._2).sum shouldEqual z.size
      x
    }

    def builds(couleur: Int, parCouleur: Int): String = {
      (1 to couleur).map(c => {
        (1 to parCouleur).map(u => c.toString).mkString("")
      }).mkString("")
    }

    def list2string(l: List[Int]) = {
      l.zipWithIndex.map(i => (1 to i._1).toList.map(u => i._2).mkString("")).mkString("")
    }

    def dozejob2(s: String, sampleSize: Int, prev: List[(Int, Int)]) = {
      val z = s.toSeq.combinations(sampleSize).toList
      val y = z.map(e => e.toSeq.distinct.size).groupBy(u => u)
      val x = z.map(e => e.groupBy(f => f).toList.map(g => g._2.size).sorted)
      println(sampleSize, z.map(e => e.toSeq.distinct.size).sum.toDouble / z.size.toDouble, z.size)
      //println("  " + z.zip(x).groupBy(_._2.toString))
      println("  " + z.zip(x).groupBy(_._2.toString).toList.map(i => (i._1, list2string(i._2.head._2), list2string(i._2.head._2).toSeq.permutations.length, i._2.length)).sortBy(_._1.toString))
      if (prev.nonEmpty) {
      }
      x
    }


    var s = builds(4, 5)
    println(s)
    (1 to 10).foreach(i => {
      dozejob2(s, i, List[(Int, Int)]())
    })

    /*s = builds(7, 10)
    println(s)
    var prev = List[(Int, Int)]()
    (1 to 20).foreach(i => {
      prev = dozejob1(s, i, prev)
    })*/

    val result = 0
    println("Euler493[" + result + "]")
    result shouldEqual 0
  }

  "Euler500" should "be OK" in {
    println("Euler500")
    val premiers = EulerPrime.premiers1000

    def numdiv(d: BigInt) = {
      val divisors = new EulerDivisors(new EulerDiv(d)).divisors
      println("" + d + "\t" + (divisors.length + 2) + "\t" + ispowerof2(divisors.length + 2) + "\t" + divisors)
    }

    def ispowerof2(d: BigInt) = {
      val divisorsDistinct = new EulerDivisors(new EulerDiv(d)).primesUnique.toList.distinct
      divisorsDistinct.length == 1 & divisorsDistinct.head == 2
    }

    numdiv(2)
    numdiv(4)
    numdiv(8)
    numdiv(12)
    numdiv(16)
    numdiv(24)
    numdiv(32)
    numdiv(48)
    numdiv(64)
    numdiv(96)
    numdiv(120)
    numdiv(120 * 2)
    numdiv(120 * 3)
    numdiv(120 * 5)
    numdiv(120 * 2 * 3)
    numdiv(120 * 7)
    numdiv(120 * 11)
    numdiv(120 * 5 * 2)
    numdiv(120 * 5 * 3)
    numdiv(120 * 5 * 7)
    numdiv(120 * 7 * 2)
    numdiv(120 * 7 * 3)
    numdiv(120 * 7 * 11)

    val result = 0
    println("Euler500[" + result + "]")
    result shouldEqual 0
  }

  "Euler451" should "be OK" in {
    println("Euler451")

    def coprimes(i: Int): (Int, List[BigInt], List[Int]) = {
      val primesi = new EulerDiv(i).primes
      (i, primesi, (1 until i).toList.filter(u => {
        val primesu = new EulerDiv(u).primes
        primesi.intersect(primesu).length == 0
      }))
    }

    def modinv(i: Int, j: Int) = {
      (j, (1 to i - 1).toList.dropWhile(u => u * j % i != 1).head)
    }

    def modinv2(i: Int, j: Int) = {
      val z = (i / 2 to i - 1).toList.dropWhile(u => u * j % i != 1)
      if (z.isEmpty) {
        (j, 1)
      } else {
        (j, z.head)
      }
    }

    def doZeJob(i: Int) = {
      val cp = coprimes(i)
      val md = cp._3.dropRight(1).map(j => modinv(i, j))
      val md1 = md.reverse.filter(u => u._1 == u._2)
      /*if (md1.head._1 > 1) {
        if (!EulerPrime.isPrime(md1.head._1)) {
          println(i, md1.head._1, "*******************************************")
        } else {
          println(i, md1.head._1, "++++++")
        }
      }*/
      (i, md1.head._1, md1.filter(_._1 != 1))
    }

    def doZeJob2(i: Int) = {
      val cp = coprimes(i)
      val md = cp._3.reverse.drop(1).find(j => {
        val u = modinv2(i, j)
        u._1 == u._2
      }) match {
        case Some(q) => q
        case _ => 0
      }
      (i, md)
    }

    println(doZeJob(15))
    println(doZeJob(100))
    println(doZeJob(7))

    doZeJob(15)._2 shouldEqual 11
    doZeJob(100)._2 shouldEqual 51
    doZeJob(7)._2 shouldEqual 1
    val prems100000 = EulerPrime.premiers100000.tail
    val prems10000 = EulerPrime.premiers10000.tail
    val prems1000 = EulerPrime.premiers1000.tail
    val powerlimit = 10
    val pow2 = (2 to powerlimit).map(Euler.powl(2, _).toInt)
    val pow3 = (2 to powerlimit).map(Euler.powl(3, _).toInt)
    val limit = Euler.powl(2, powerlimit).toInt
    val incs = (1 to limit / 2).toList
    val primesx2 = prems100000.map(_ * 2) ++ prems100000 ++ prems10000.map(p => p * p) ++ prems1000.map(p => p * p * p)
    val t_ici = timeStamp(t_start, "ici!")
    val t_la = timeStamp(t_start, "la!")
    val z2 = (3 to limit).toList.map(doZeJob2(_))
    val t_la2 = timeStamp(t_la, "la2!")
    var z3 = (3 to limit).toList.filter(!primesx2.contains(_)).map(doZeJob2(_)).map(_._2).sum + primesx2.takeWhile(_ <= limit).toList.length
    z2.map(_._2).sum shouldEqual z3
    val t_la3 = timeStamp(t_la2, "la3!")
    if (powerlimit < 11) {
      val z = (3 to limit).toList.map(doZeJob(_))
      z2.map(u => (u._1, u._2)) shouldEqual z.map(u => (u._1, u._2))
      println(z.mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => pow2.contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => pow2.map(_ * 3).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => pow2.map(_ * 5).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => pow3.contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => incs.map(_ * 2).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => incs.map(_ * 3).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => incs.map(_ * 5).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => incs.map(_ * 7).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => prems100000.map(_ * 1).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => prems100000.map(_ * 2).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => prems100000.map(_ * 3).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => prems100000.map(_ * 4).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => prems100000.map(_ * 5).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => prems100000.map(_ * 6).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => prems100000.map(_ * 7).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => prems100000.map(_ * 11).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => prems100000.map(v => v * v).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => prems100000.map(v => v * v * 2).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => prems100000.map(v => v * v * v).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
      println(z.filter(u => prems100000.map(v => v * v * v * 2).contains(u._1)).mkString("\n  ", "\n  ", "\n  "))
    }

    //var cpts = (0 to limit).map(i => (i, z2.map(_._2).count(_ == i))).grouped(16)
    //println(cpts.mkString("\n  ", "\n  ", "\n  "))


    val r = (2 to powerlimit).map(plmt => {
      val lmt = Euler.powl(2, plmt).toInt
      val rg = z2.take(lmt - 2)
      val s = rg.map(_._2).sum
      (plmt, lmt, s, rg.count(_._2 == 1))
    })
    println(r.mkString("\n  ", "\n  ", "\n  "))
    println(r.sliding(2).map(c => {
      (c.head._1, c.last._1,
        c.last._3.toDouble / c.head._3.toDouble,
        c.last._4.toDouble / c.head._4.toDouble)
    }).mkString("\n  ", "\n  ", "\n  "))
    r.last._3 shouldEqual z3


    println(EulerPrime.premiers1000.take(20))
    var result = 0
    println("Euler451[" + result + "]")
    result shouldEqual 0

  }

  "Euler518" should "be OK" in {
    println("Euler518")

    def div(l: List[BigInt]) = (l.sorted.apply(1) + 1).toDouble / (l.sorted.head + 1).toDouble

    def Yes(a: BigInt, b: BigInt, c: BigInt) = {
      var r = EulerPrime.isPrime(a) && EulerPrime.isPrime(b) && EulerPrime.isPrime(c) && (b + 1).toDouble / (a + 1).toDouble == (c + 1).toDouble / (b + 1).toDouble
      //if(r) println("           ", (a, b, c), (a + 1, b + 1, c + 1), (b + 1) / (a + 1), r)
      r
    }

    def YesV(a: BigInt, b: BigInt, c: BigInt) = {
      var r = EulerPrime.isPrime(a) && EulerPrime.isPrime(b) && EulerPrime.isPrime(c) && (b + 1).toDouble / (a + 1).toDouble == (c + 1).toDouble / (b + 1).toDouble
      println("   ", (a, b, c), (a + 1, b + 1, c + 1), (b + 1).toDouble / (a + 1).toDouble, r)
      println("           ", ((new EulerDiv(a + 1)).primes, (new EulerDiv(b + 1)).primes, (new EulerDiv(c + 1)).primes))
      r
    }

    def Yes2(l: List[BigInt]) = Yes(l.sorted.head, l.sorted.apply(1), l.sorted.last)

    def S(n: Int) = {
      //val t_iciS = timeStamp(t_start, "")
      val prems: List[BigInt] = EulerPrime.premiers10000.filter(_ < n).toList
      val z = prems.combinations(3).filter(Yes2(_)).toList
      val y = z.map(l => (l, div(l))).groupBy(_._1.head).toList.sortBy(_._1).map(q => (q._1, q._2.length, q._2))
      println("S(" + n + ")", prems.length, z.length, z.flatten.sum,
        y.mkString("\n   ", "\n   ", "\n   "), z.map(_.sum).sorted)
      (z.flatten.sum, y)
    }

    def S2(n: Int) = {
      var s = S(n)
      var lp: Set[BigInt] = Set()
      s._2.foreach(z => {
        println(z._1)
        z._3.foreach(a => {
          val b = a._1
          val d: List[(BigInt, BigInt, List[BigInt])] = b.map(c => (c, c + 1, (new EulerDiv(c + 1)).primes))
          lp = lp ++ d.map(_._3).flatten
          println("   ", a, d)
        })
      })
      println(lp.toList.sorted)
      s
    }


    def T(a: BigInt, n: Double, prems: List[BigInt]): (BigInt, BigInt, List[BigInt]) = {
      //val t_iciS = timeStamp(t_start, "")
      val a1 = a.toDouble + 1.0
      val z = prems.filter(b => {
        if (b > a) {
          val b1 = b.toDouble + 1.0
          val ratio = b1 / a1
          val c: Double = (b1 * ratio) - 1.0
          if (c < n) {
            if (c % 1 <= 0.00001) {
              //println("    ", a, b, c)
              (prems.contains(BigDecimal(c).setScale(0, BigDecimal.RoundingMode.HALF_DOWN).toBigInt))
            } else if (c % 1 >= 0.9999) {
              //println("    ", a, b, c)
              (prems.contains(BigDecimal(c).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt))
            } else false
          } else false
        } else false
      })
      //timeStamp(t_iciS, "la! T(" + a + ")")
      //println(a, n, z)
      (a, z.length, z)
    }

    def U(n: BigInt, prems: List[BigInt]): List[(BigInt, BigInt, List[BigInt])] = {
      val premsn: List[BigInt] = prems.takeWhile(_ < n)
      val z: List[(BigInt, BigInt, List[BigInt])] = premsn.map(a => {
        T(a, n.toDouble, premsn)
      })
      z.filter(!_._3.isEmpty)
    }

    def V(n: BigInt, prems: List[BigInt]): BigInt = {
      U(n, prems).map(z => {
        z._3.map(y => {
          //println(z, y, ((y + 1) * (y + 1) / (z._1 + 1)) - 1, z._1 + y + ((y + 1) * (y + 1) / (z._1 + 1)) - 1)
          z._1 + y + ((y + 1) * (y + 1) / (z._1 + 1)) - 1
        }).sum
      }).sum
    }

    def W(a: BigInt, n: Double, prems: List[BigInt]): (BigInt, BigInt, List[BigInt]) = {
      val a1 = a.toDouble + 1.0
      val limit = BigDecimal(math.sqrt(a1 * n)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
      val z = prems.dropWhile(_ <= a).takeWhile(_ <= limit).filter(b => {
        val b1 = b.toDouble + 1.0
        val ratio = b1 / a1
        val c: Double = (b1 * ratio) - 1.0
        if (c % 1 <= 0.00001) {
          (prems.contains(BigDecimal(c).setScale(0, BigDecimal.RoundingMode.HALF_DOWN).toBigInt))
        } else if (c % 1 >= 0.9999) {
          (prems.contains(BigDecimal(c).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt))
        } else false
      })
      (a, z.length, z)
    }

    def X(n: BigInt, prems: List[BigInt]): List[(BigInt, BigInt, List[BigInt])] = {
      val premsn: List[BigInt] = prems.takeWhile(_ < n)
      val z: List[(BigInt, BigInt, List[BigInt])] = premsn.map(a => {
        W(a, n.toDouble, premsn)
      })
      z.filter(!_._3.isEmpty)
    }

    def Y(n: BigInt, prems: List[BigInt]): BigInt = {
      X(n, prems).map(z => {
        z._3.map(y => {
          //println(z, y, ((y + 1) * (y + 1) / (z._1 + 1)) - 1, z._1 + y + ((y + 1) * (y + 1) / (z._1 + 1)) - 1)
          z._1 + y + ((y + 1) * (y + 1) / (z._1 + 1)) - 1
        }).sum
      }).sum
    }


    YesV(37, 151, 607) shouldEqual true
    YesV(71, 83, 97) shouldEqual true
    YesV(337, 389, 449) shouldEqual true
    YesV(449, 509, 577) shouldEqual true
    YesV(2887, 2963, 3041) shouldEqual true
    YesV(397, 1193, 3581) shouldEqual true
    Yes(2, 5, 11) shouldEqual true
    Yes(2, 5, 13) shouldEqual false
    Yes(31, 47, 71) shouldEqual true
    Yes(31, 53, 71) shouldEqual false
    Yes(2, 6, 11) shouldEqual false
    Yes(5, 2, 11) shouldEqual false
    Yes2(List(5, 2, 11)) shouldEqual true
    println("********************************")
    S(100)._1 shouldEqual 1035
    println("********************************")
    var S1000 = S2(1000)
    println("********************************")
    var prems: List[BigInt] = EulerPrime.premiers10000.toList
    println(U(100, prems))
    V(100, prems) shouldEqual 1035
    S1000._1 shouldEqual V(1000, prems)
    S1000._1 shouldEqual Y(1000, prems)

    val t_iciV = timeStamp(t_start, "")
    var j = 2000
    (1 until j).foreach(i => {
      V(i, prems)
    })
    val t_iciY = timeStamp(t_iciV, "la! V(" + j + ")")
    (1 until j).foreach(i => {
      Y(i, prems)
    })
    val t_laY = timeStamp(t_iciY, "la! Y(" + j + ")")
    j = 10000
    V(j, prems)
    var t_la = timeStamp(t_laY, "la! V(" + j + ")")
    Y(j, prems)
    t_la = timeStamp(t_la, "la! Y(" + j + ")")
    prems = EulerPrime.premiers100000.toList
    j = 50000
    println(2, j, W(2, j.toDouble, prems)._3)
    println(3, j, W(3, j.toDouble, prems)._3)
    println(5, j, W(5, j.toDouble, prems)._3)
    println(17, j, W(17, j.toDouble, prems)._3)
    println(19, j, W(19, j.toDouble, prems)._3)
    println(47, j, W(47, j.toDouble, prems)._3)
    t_la = timeStamp(t_la, "la! Y(" + j + ")")
    j = 100000
    Y(j, prems)
    t_la = timeStamp(t_la, "la! Y(" + j + ")")
    var i = 2
    var w = W(i, j.toDouble, prems)._3
    println(i, j, w)
    YesV(i, w.last, ((w.last + 1) * (w.last + 1) / (i + 1)) - 1) shouldEqual true
    println(i, j, BigDecimal(math.sqrt((i + 1) * j)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt)

    println(3, j, W(3, j.toDouble, prems)._3)
    println(5, j, W(5, j.toDouble, prems)._3)
    println(17, j, W(17, j.toDouble, prems)._3)
    println(19, j, W(19, j.toDouble, prems)._3)

    i = 47
    w = W(i, j.toDouble, prems)._3
    println(i, j, w)
    YesV(i, w.last, ((w.last + 1) * (w.last + 1) / (i + 1)) - 1) shouldEqual true
    println(i, j, BigDecimal(math.sqrt((i + 1) * j)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt)

    j = 4000
    S2(j)
    t_la = timeStamp(t_la, "la! Y(" + j + ")")
    var result = 0
    println("Euler518[" + result + "]")
    result shouldEqual 0

  }

  "Euler601" should "be OK" in {
    println("Euler601")

    def streak(n: BigInt, verbose: Boolean = false): Int = {
      val z = stream_zero_a_linfini.takeWhile(i => (n + i) % (i + 1) == 0).toList
      if (verbose) {
        z.foreach(i => print(", " + (n + i) + ": " + (i + 1)))
        println(" - streak(" + n + "): " + z.length)
      }
      z.length
    }

    def P(s: BigInt, N: BigInt): Int = {
      (2 until N.toInt).filter(streak(_) == s).length
    }

    streak(13, true) shouldEqual 4
    P(3, 14) shouldEqual 1
    P(6, 1000000) shouldEqual 14286
    println((2 until 100).map(streak(_)).max)
    streak(61, true)
    println((1 until 10).map(i => ("P(" + i + ",100)", P(i, 100))).mkString(", "))

    def Q3(sn: Int, elimit: Int) = {
      println("Q3", sn, elimit)
      var prev = BigInt(0)
      println("  " + (2 until elimit).map(e => ("P(" + sn + "," + math.pow(sn, e).toInt + ")[" + e + "]", P(sn, math.pow(sn, e).toInt))).mkString(", "))
    }

    def Q6(sn: Int, elimit: Int, verbose: Boolean = false, check: Boolean = false) = {
      println("Q6", sn, elimit)
      var prev = BigInt(0)
      if (verbose) {
        println("  " + (2 until elimit).map(e => ("P(" + sn + "," + math.pow(sn, e).toInt + ")[" + e + "]", P(sn, math.pow(sn, e).toInt))).mkString(", "))
        print("  ")
      }
      (2 until elimit).foreach(e => {
        prev = P6(sn, e, prev)
        val p = P(sn, math.pow(sn, e + 1).toInt)
        if (verbose) {
          print(" - ", sn, e, (e % sn), p, prev)
        }
        if (check) {
          prev shouldEqual p
        }
      })
      println("")
    }

    def P6(sn: Int, e: Int, prev: BigInt): BigInt = {
      (prev * sn) + ((e % 2) match {
        case 0 => 3
        case 1 => 0
        case _ => BigInt(999)
      })
    }

    def Q2(sn: Int, elimit: Int, verbose: Boolean = false, check: Boolean = false) = {
      println("Q2", sn, elimit)
      var prev = BigInt(0)
      if (verbose) {
        println("  " + (2 until elimit).map(e => ("P(" + sn + "," + math.pow(sn, e).toInt + ")[" + e + "]", P(sn, math.pow(sn, e).toInt))).mkString(", "))
        print("  ")
      }
      (1 until elimit).foreach(e => {
        prev = P2(sn, e, prev)
        val p = P(sn, math.pow(sn, e + 1).toInt)
        if (verbose) {
          print(" - ", sn, e, (e % sn), p, prev)
        }
        if (check) {
          prev shouldEqual p
        }
      })
      println("")
    }

    def P2(sn: Int, e: Int, prev: BigInt): BigInt = {
      (prev * sn) + ((sn % 2) match {
        case 0 => (e % 2)
        case 1 => ((e % 2) * 2) - 1
        case _ => BigInt(999)
      })
    }

    /*Q2(2, 15, false, true)
    Q2(3, 13, false, true)
    Q2(4, 8, false, true)
    Q3(5, 9)
    Q6(6, 9, true, true)
    Q3(7, 9)
    Q3(8, 8)
    Q3(9, 7)
    Q3(10, 7)
    Q3(11, 7)
    Q3(12, 7)
    Q3(13, 7)
    Q3(14, 7)
    Q3(15, 7)
    Q3(16, 7)
    Q3(17, 7)*/


    def T(N: BigInt): List[(Int, Int, Int)] = {
      val l = (3 until N.toInt by 2).toList.map(z => (z, streak(z))).groupBy(i => i._2)
      l.toList.map(i => (i._1, i._2.length, i._2.last._1)).sortBy(_._1)
    }

    println((3 until 16 by 2).toList)
    println((3 until 16 by 2).toList.map(streak(_)))
    println((3 until 16 by 2).toList.map(streak(_)).groupBy(i => i).toList.map(i => (i._1, i._2.length)))

    val T1million = T(1000000)
    println(T1million)
    T1million.apply(3)._2 shouldEqual 14286
    val tl = (2 until 25).toList.map(e => {
      val limit = BigDecimal(Math.pow(2, e)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
      val tz = T(limit)
      (e, tz)
    })

    tl.foreach(z => {
      val limit = BigDecimal(Math.pow(2, z._1)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
      println(z._1, limit, z._2.map(t => (t._1, t._2)), (z._1, z._2.last._3))
    })

    tl.foreach(z => {
      val limit = BigDecimal(Math.pow(2, z._1)).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
      println(z._1, limit, z._2.map(t => (t._1, t._2, t._3)))
    })


    var result = 0
    println("Euler601[" + result + "]")
    result shouldEqual 0
  }


  "Euler621" should "be OK" in {
    println("Euler621")
    val tr = Euler.triangular(500)

    def G1(a: BigInt, verbose: Boolean = false): (BigInt, BigInt, (BigInt, BigInt, BigInt)) = {
      var t_la = Calendar.getInstance()
      val t = tr.takeWhile(_ <= a)
      val l1 = t.filter(_ * 3 == a).toList
      val l2 = t.combinations(2).map(y => List(y :+ y.head, y :+ y.last)).flatten.filter(_.sum == a).toList
      val l3 = t.combinations(3).filter(_.sum == a).toList
      val result = BigInt(l1.size + (l2.size * 3) + (l3.size * 6))
      if (verbose) {
        println(a, result, " ", (l1.size, l2.size, l3.size), l1, l2, l3)
        t_la = timeStamp(t_la, "G1 fin " + a)
      }
      (a, result, (l1.size, l2.size, l3.size))
    }

    println(tr)
    println(tr.filter(_ % 10 == 0))

    G1(7, true)
    G1(9, true)._2 shouldEqual 7
    G1(10, true)._2
    G1(100, true)._2
    G1(1000, true)._2 shouldEqual 78
    G1(10000, true)._2 shouldEqual 252
    G1(100000, true)._2
    2106 shouldEqual 3 * 3 * 3 * 78
    //G1(1000000)._4 shouldEqual 2106

    val j = 100
    val a = (1 to j).map(G1(_)).groupBy(_._3).toList.
      sortBy(b => (b._1._1 * j * j) + (b._1._2 * j) + b._1._3).map(b => (b._1, b._2.map(_._1)))
    println(a.mkString("\n"))
    println(tr)
    (1 to j).map(G1(_, true))

    var result = 0
    println("Euler621[" + result + "]")
    result shouldEqual 0
  }

  "Euler632" should "be OK" in {
    println("Euler632")


    def squareprimes(bi: BigInt): Int = {
      /*val p = new EulerDiv(bi).primes
      println(p)
      println(p.groupBy(u => u))
      println(p.groupBy(u => u).toList.filter(_._2.length>1))*/
      new EulerDiv(bi).primes.groupBy(u => u).toList.filter(_._2.length > 1).length
    }

    squareprimes(1500)

    def Ck(k: BigInt, bi: BigInt): Int = {
      (1 to bi.toInt).map(z => squareprimes(z)).filter(_ == k).length
    }

    def Ck2(bi: BigInt) = {
      (1 to bi.toInt).map(z => squareprimes(z)).groupBy(u => u).toList.map(y => (y._1, y._2.length)).sortBy(_._1)
    }

    Ck(0, 10) shouldEqual 7
    Ck(1, 10) shouldEqual 3
    Ck(3, 100000) shouldEqual 297

    val l = (1 to 7).map(e => {
      val y = BigInt(math.pow(10, e).toInt)
      (y, Ck2(y))
    })

    l.foreach(e => {
      println("" + e._1 + e._2.mkString)
    })

    l.foreach(e => {
      println("" + e._1 + e._2.map(z => (z._1, new EulerDiv(z._2).primes.mkString(";"))).mkString("-"))
    })

    l.sliding(2).foreach(ee => {
      println("\n" + ee.last._1)
      ee.head._2.zip(ee.last._2).foreach(ff => {
        print(", " + ff._1._1 + " " + (ff._1._2, ff._2._2, (ff._1._2 * 10 - ff._2._2)))
      })
    })

    val primes = EulerPrime.premiers1000

    println()
    var prod = BigInt(1)
    primes.take(10).foreach(p => {
      prod = prod * p * p
      print(", " + (p, prod))
    })
    println()

    var result = 0
    println("Euler632[" + result + "]")
    result shouldEqual 0
  }

  "Euler650" should "be OK" in {
    println("Euler650")

    // explore how modulo addition & multiplication work
    def m(bi: BigInt, mod: BigInt = BigInt(23)): BigInt = bi % mod

    val mod = 23
    (m(27, mod) + m(37, mod)) shouldEqual m(27 + 37, mod)
    m(m(27, mod) * m(37, mod), mod) shouldEqual m(27 * 37, mod)
    EulerPrime.isPrime(1000000007) shouldBe true

    def binomial(n: BigInt, k: BigInt): BigInt = {
      factorielle(n) / ((factorielle(k) * factorielle(n - k)))
    }

    binomial(5, 0) shouldEqual 1
    binomial(5, 1) shouldEqual 5
    binomial(5, 2) shouldEqual 10
    binomial(5, 3) shouldEqual 10
    binomial(5, 4) shouldEqual 5
    binomial(5, 5) shouldEqual 1

    def B(n: BigInt, verbose: Boolean = false): List[BigInt] = {
      val r = (0 to n.toInt).map(binomial(n, _)).toList
      if (verbose) {
        println("B(" + n + "): " + r.product, r)
      }
      r
    }

    def B2(n: BigInt, prevl: List[BigInt], verbose: Boolean = false): List[BigInt] = {
      var r: List[BigInt] = List(BigInt(1))
      r = r ++ prevl.sliding(2).map(_.sum) :+ BigInt(1)
      if (verbose) {
        println("B2(" + n + "): " + r)
      }
      r
    }

    def B3(n: BigInt, prevl: List[BigInt], verbose: Boolean = false): List[BigInt] = {
      var r: List[BigInt] = List(BigInt(1))
      r = r ++ prevl.take((prevl.length + 2) / 2).sliding(2).map(_.sum)
      r = r ++ r.take((prevl.length + 1) / 2).reverse
      if (verbose) {
        println("B3(" + n + "): " + r)
      }
      r
    }

    B(5, true).product shouldEqual 2500

    var j = 5
    var prevl = List(BigInt(1), BigInt(1))
    (2 to j).foreach(n => {
      B3(n, prevl, true)
      B2(n, prevl) shouldEqual (B3(n, prevl))
      prevl = B2(n, prevl, true)
    })
    B(j, true).product shouldEqual prevl.product

    def D(n: BigInt, bn: List[BigInt], verbose: Boolean = false): List[BigInt] = {
      //val r = ((new EulerDivisors(new EulerDiv(bn.product).primes).divisors) :+ BigInt(1)).sorted
      val r = ((new EulerDivisors(bn.map(o => new EulerDiv(o).primes).flatten).divisors) :+ BigInt(1)).sorted
      if (verbose) {
        println("D(" + n + "): " + r.sum, r)
      }
      r
    }

    def D3(n: BigInt, bn: List[BigInt], verbose: Boolean = false): List[BigInt] = {
      val r = ((new EulerDivisors(bn.map(o => new EulerDiv(o).primes).flatten.map(m(_))).divisors.map(m(_))) :+ BigInt(1)).sorted
      if (verbose) {
        println("D(" + n + "): " + r.sum, r)
      }
      r
    }

    D(5, B(5)).sum shouldEqual 5467

    def S(n: BigInt): BigInt = {
      val r = (1 to n.toInt).map(o => D(o, B(o)))
      println("S(" + n + "): " + r.flatten.sum, r.flatten.groupBy(u => u).toList.map(y => (y._1, y._2.length)).sortBy(_._1))
      r.flatten.sum
    }

    S(5) shouldEqual 5736
    S(10) shouldEqual BigInt("141740594713218418")

    def B4(n: BigInt, prevl: List[BigInt], verbose: Boolean = false): List[BigInt] = {
      var r = List(BigInt(1)) ++ prevl.sliding(2).map(_.sum)
      r = r ++ r.take((n.toInt + 1) / 2).reverse
      val r2 = r.take((n.toInt + 3) / 2)
      if (verbose) {
        println("B4(" + n + "): " + r2)
      }
      r2
    }

    def D4(n: BigInt, bn: List[BigInt], verbose: Boolean = false): List[BigInt] = {
      val bn1 = bn.map(o => new EulerDiv(o).primes)
      val bn2 = bn1 ++ bn1.take(n.toInt + 1 - ((n.toInt + 3) / 2)).reverse
      val r = ((new EulerDivisors(bn2.flatten.map(m(_))).divisors.map(m(_))) :+ BigInt(1)).sorted
      if (verbose) {
        println("D4(" + n + "): " + r.sum, r)
      }
      r
    }


    val vb = 6
    j = 15
    var t_la = Calendar.getInstance()
    var r: BigInt = 1
    var lr = List[(BigInt, BigInt, List[BigInt])]().empty
    prevl = List(BigInt(1), BigInt(1))
    (2 to j).foreach(n => {
      prevl = B2(n, prevl, n < vb)
      r += D(n, prevl, n < vb).sum
      timeStamp(t_la, "D & B2 " + j + " : " + n + " -> " + r + " [" + prevl.product + "]")
      n match {
        case 5 => r shouldEqual 5736
        case 10 => r shouldEqual BigInt("141740594713218418")
        case _ =>
      }
      lr = lr :+ (n, prevl.product, new EulerDiv(prevl.product).primes)
    })
    println(lr.mkString("\n", "\n", "\n"))
    lr.tail.foreach(c => {
      println("  ", c._1, c._2, c._1.pow((c._1 - 2).toInt), c._2 % c._1.pow((c._1 - 2).toInt))
    })

    0 shouldEqual 1
    t_la = timeStamp(t_la, "D & B2 " + j)
    r = 1
    prevl = List(BigInt(1), BigInt(1))
    (2 to j).foreach(n => {
      prevl = B2(n, prevl, n < vb)
      r += m(D3(n, prevl, n < vb).sum)
      timeStamp(t_la, "D3 & B2 " + j + " : " + n + " -> " + m(r))
      n match {
        case 5 => m(r) shouldEqual m(5736)
        case 10 => m(r) shouldEqual m(BigInt("141740594713218418"))
        case _ =>
      }
    })
    t_la = timeStamp(t_la, "D3 & B2 " + j)
    r = 1
    prevl = List(BigInt(1), BigInt(1))
    (2 to j).foreach(n => {
      prevl = B3(n, prevl, n < vb)
      r += m(D3(n, prevl, n < vb).sum)
      timeStamp(t_la, "D3 & B3 " + j + " : " + n + " -> " + m(r))
      n match {
        case 5 => m(r) shouldEqual m(5736)
        case 10 => m(r) shouldEqual m(BigInt("141740594713218418"))
        case _ =>
      }
    })
    t_la = timeStamp(t_la, "D3 & B3 " + j)
    r = 1
    prevl = List(BigInt(1), BigInt(1))
    (2 to j).foreach(n => {
      prevl = B4(n, prevl, n < vb)
      r += m(D4(n, prevl, n < vb).sum)
      timeStamp(t_la, "D4 & B4 " + j + " : " + n + " -> " + m(r))
      n match {
        case 5 => m(r) shouldEqual m(5736)
        case 10 => m(r) shouldEqual m(BigInt("141740594713218418"))
        case _ =>
      }
    })
    t_la = timeStamp(t_la, "D4 & B4 " + j)

    var result = 0
    println("Euler650[" + result + "]")
    result shouldEqual 0
  }

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

    def F2(d: Int, DeltaStart: BigInt, delta: BigInt, verbose: Int = 0): BigInt = {
      val start = BigInt("1" + (2 to d).map(z => "0").mkString) + DeltaStart
      val end = start + delta
      val z = (start until end).toList.filter(bi => f(bi) % 3 == 0).length
      verbose match {
        case 0 => println(d, (start until end), z)
        case 1 =>
        case 50 => print("\n," + z)
        case _ => print("," + z)
      }

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
          //print(" ")
          r
        case _ => List(getMapResult(dozemap2(start, inc / 10, cpt1)._1),
          getMapResult(dozemap2(start + inc, inc / 10, cpt1)._1),
          getMapResult(dozemap2(start + inc + inc, inc / 10, cpt1)._1))
      }
      val s = (0 to cpt1).map(i => " ").mkString("")
      if (cpt < 0) {
        println(s + start + " - " + (start + (inc * 10)) + ": " +
          (troisPremiersIcrements, getMapResult(troisPremiersIcrements)))
        //print(" ")
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
      print(" -> " + getMapResults(deuxPremiersIcrements))
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

    def F6(d: Int): List[BigInt] = {
      val start = BigInt("1" + (2 to d).map(z => "0").mkString)
      val end = start * 10
      val delta = start / 10
      val inc = delta / 10
      val r0 = dozemaps2(start * 3, inc / 10)
      println("\n**[" + d + "][" + (start * 3) + "-" + ((start * 3) + inc) + "]: " + getMapResults(r0) + "         delta: " + delta + ", inc: " + inc)
      r0
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


    def FF2(d: Int, delta: BigInt): Unit = {
      println(F2(d, 0, delta))
      println(F2(d, 500 * 100, delta))
      (0 to 500).foreach(i => {
        F2(4, i * 100, delta, 2)
        if (i % 100 == 0) {
          println("")
        }
      })
      println("")
    }

    FF2(4, 100)
    FF2(7, 100)
    FF2(10, 100)
    //(5 to 14).foreach(d => F6(d))

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