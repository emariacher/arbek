import Euler._
import org.scalatest.{Matchers, FlatSpec}
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
    val premiers = EulerPrime.premiers1000

    def resilience(d: Int) = {
      val primes = new EulerDiv(d).primes
      val divisors = new EulerDivisors(primes).divisors
      val resil = Range(1, d, 2).filter(i => {
        //divisors.filter(div => i % div == 0).isEmpty
        divisors.find(div => i % div == 0) match {
          case Some(i) => false
          case _ => true
        }
      })
      val pgcd_primes = new EulerDiv(resil.length).primes intersect primes
      println("" + d + "\t" + resil.length + "/" + (d - 1) + "\t" + (1.0 * resil.length / (d - 1)) + "\t" + primes + "\t" + new EulerDiv(resil.length).primes + "\t" + pgcd_primes)
      (resil.length, d - 1)
    }

    resilience(12) shouldEqual (4, 11)
    resilience(premiers.take(4).product.toInt)
    resilience(premiers.take(5).product.toInt)
    resilience(2 * premiers.take(5).product.toInt)
    resilience(2 * 2 * premiers.take(5).product.toInt)
    resilience(2 * 3 * premiers.take(5).product.toInt)
    resilience(2 * 2 * 3 * premiers.take(5).product.toInt)
    resilience(2 * 3 * 5 * premiers.take(5).product.toInt)
    resilience(2 * 2 * 3 * 3 * premiers.take(5).product.toInt)
    resilience(premiers.take(5).product.toInt)
    resilience(premiers.take(6).product.toInt)
    resilience(premiers.take(7).product.toInt)
    resilience(premiers.take(8).product.toInt)

    println(1.0 * 15499 / 94744)

    val result = 0
    println("Euler243[" + result + "]")
    result shouldEqual 0
  }

  "Euler500" should "be OK" in {
    println("Euler500")
    val premiers = EulerPrime.premiers1000

    def numdiv(d: BigInt) = {
      val divisors = new EulerDivisors(new EulerDiv(d)).divisors
      println(""+d + "\t" + (divisors.length + 2) + "\t" + ispowerof2(divisors.length + 2) + "\t" + divisors)
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
}