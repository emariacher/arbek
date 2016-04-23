import java.util.Calendar

import org.scalatest._
import Euler._
import scala.collection.immutable.TreeSet

import scala.math.BigInt

class EulerMain extends FlatSpec with Matchers {
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