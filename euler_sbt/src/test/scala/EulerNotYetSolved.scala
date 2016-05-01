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

    sigma2(10) should be === 130
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
    sqslx * sqslx should be === sumlx


    var result = 0
    println("Euler211[" + result + "]")
    result should be === 0

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
      println(d + "\t" + resil.length + "/" + (d - 1) + "\t" + (1.0 * resil.length / (d - 1)) + "\t" + primes + "\t" + new EulerDiv(resil.length).primes + "\t" + pgcd_primes)
      (resil.length, d - 1)
    }

    resilience(12) should be ===(4, 11)
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
    result should be === 0
  }

  "Euler500" should "be OK" in {
    println("Euler500")
    val premiers = EulerPrime.premiers1000
    def numdiv(d: BigInt) = {
      val divisors = new EulerDivisors(new EulerDiv(d)).divisors
      println(d + "\t" + (divisors.length+2) + "\t" + ispowerof2(divisors.length+2) + "\t" + divisors)
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
    numdiv(120*2)
    numdiv(120*3)
    numdiv(120*5)
    numdiv(120*2*3)
    numdiv(120*7)
    numdiv(120*11)
    numdiv(120*5*2)
    numdiv(120*5*3)
    numdiv(120*5*7)
    numdiv(120*7*2)
    numdiv(120*7*3)
    numdiv(120*7*11)

    val result = 0
    println("Euler500[" + result + "]")
    result should be === 0
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

    doZeJob(15)._2 should be === 11
    doZeJob(100)._2 should be === 51
    doZeJob(7)._2 should be === 1
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
    z2.map(_._2).sum should be === z3
    val t_la3 = timeStamp(t_la2, "la3!")
    if (powerlimit < 11) {
      val z = (3 to limit).toList.map(doZeJob(_))
      z2.map(u => (u._1, u._2)) should be === z.map(u => (u._1, u._2))
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
    r.last._3 should be === z3


    println(EulerPrime.premiers1000.take(20))
    var result = 0
    println("Euler451[" + result + "]")
    result should be === 0

  }

}