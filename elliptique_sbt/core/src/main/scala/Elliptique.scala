/**
  * Created by mariachere on 26.05.2015.
  */

import kebra.MyLog._

import scala.language.postfixOps
import scala.math.BigInt
import scala.util.Random

/*
https://www.coindesk.com/math-behind-bitcoin/
https://crypto.stackexchange.com/questions/44304/understanding-elliptic-curve-point-addition-over-a-finite-field
https://fr.wikipedia.org/wiki/Courbe_elliptique
https://en.wikipedia.org/wiki/Elliptic_curve_point_multiplication#Point_addition
 */

object Elliptique {
  def powl(pn: BigInt, n: Int): BigInt = {
    val div = 40
    val z2 = new Range(1, (n / div) + 1, 1).toList.map((i: Int) => pn).product
    val z4 = new Range(1, (n % div) + 1, 1).toList.map((i: Int) => pn).product
    val z5 = new Range(1, div + 1, 1).toList.map((i: Int) => z2).product
    if (n % div == 0) {
      z5
    } else {
      z5 * z4
    }
  }

  def sqrt(number: BigInt) = {
    def next(n: BigInt, i: BigInt): BigInt = (n + i / n) >> 1

    val one = BigInt(1)

    var n = one
    var n1 = next(n, number)

    while ((n1 - n).abs > one) {
      n = n1
      n1 = next(n, number)
    }

    while (n1 * n1 > number) {
      n1 -= one
    }

    n1
  }

  def max(p: BigInt, q: BigInt) = {
    if (p > q) {
      p
    } else {
      q
    }
  }

  def min(p: BigInt, q: BigInt) = {
    if (p > q) {
      q
    } else {
      p
    }
  }

  def rangeStream(a: BigInt, b: BigInt): Stream[BigInt] = a #:: rangeStream(b, 1 + b)

  def stream_zero_a_linfini: Stream[BigInt] = rangeStream(0, 1)

  def factorielle(n: BigInt): BigInt = {
    /*n match {
      case 1 => BigInt(1)
      case _ => n * factorielle(n - 1)
    }*/
    if (n.toInt <= 1) {
      BigInt(1)
    } else {
      n * factorielle(n - 1)
    }
  }

  def inverse(modlo: BigInt, a: BigInt): BigInt = {
    val l1 = stream_zero_a_linfini.take(modlo.toInt).toList
    val li = stream_zero_a_linfini.take(modlo.toInt).toList.tail.map(i => {
      (i, l1.filter(u => ((u * i) % modlo) == 1).head)
    })
    li.filter(_._1 == a).head._2
  }
}

class Elliptique(val modlo: BigInt, val a: BigInt, val b: BigInt) {
  val ZERO = (BigInt(0), BigInt(0))

  def rangeStream(a: BigInt, b: BigInt): Stream[BigInt] = a #:: rangeStream(b, 1 + b)

  def stream_zero_a_linfini: Stream[BigInt] = rangeStream(0, 1)

  val lpow = (0 to 10).map(i => BigInt(Math.pow(2, i).toInt))
  val l1 = stream_zero_a_linfini.take(modlo.toInt).toList
  val li = l1.tail.map(i => {
    (i, l1.filter(u => ((u * i) % modlo) == 1).head)
  })
  val ly2 = l1.map(i => (i, (i * i) mod modlo))
  val lx3paxpb = l1.map(i => (i, ((i * i * i) + (a * i) + b) mod modlo))
  val curve = lx3paxpb.map(x => {
    ly2.filter(y => y._2 == x._2).map(y => (x._1 % modlo, y._1 % modlo))
  }).flatten

  val curved = curve.map(p => (p._1.toDouble, p._2.toDouble))

  val lZeros = curve.filter(p => p._1 * p._2 == 0)

  def add(a: BigInt, b: BigInt) = (a + b) % modlo

  def sub(a: BigInt, b: BigInt) = {
    val diff1 = a - b
    if (diff1 < 0) diff1 + modlo else diff1
  }

  def mul(a: BigInt, b: BigInt): BigInt = (a * b) % modlo

  def inverse(a: BigInt) = li.filter(_._1 == a).head._2

  def getLambda(p: (BigInt, BigInt), q: (BigInt, BigInt)): BigInt = {
    (sub(q._1, p._1) == 0, sub(q._2, p._2) == 0) match {
      case (true, true) => ((mul(mul(p._1, p._1), 3) + a) * inverse(mul(p._2, 2))) % modlo
      case _ => (sub(q._2, p._2) * inverse(sub(q._1, p._1))) % modlo
    }
  }

  def plus(p: (BigInt, BigInt), q: (BigInt, BigInt)): (BigInt, BigInt) = {
    (p == (BigInt(0), BigInt(0)), q == (BigInt(0), BigInt(0))) match {
      case (true, _) => q
      case (_, true) => p
      case _ => {
        (q._1 == p._1, q._2 == p._2, q._2 == 0) match {
          case (true, false, _) => (BigInt(0), BigInt(0))
          case (true, true, true) => (BigInt(0), BigInt(0))
          case _ => val lambda = getLambda(p, q)
            val xr = sub(sub(mul(lambda, lambda), p._1), q._1)
            val yr = sub(mul(lambda, sub(p._1, xr)), p._2)
            (xr, yr)
        }
      }
    }
  }

  def mul(p: (BigInt, BigInt), q: BigInt): ((BigInt, BigInt), BigInt, (BigInt, BigInt)) = {
    var ql = q
    var lpowl = lpow
    var lpowneeded = List(BigInt(0))
    do {
      lpowl = lpowl.takeWhile(pow => pow <= ql)
      if (!lpowl.isEmpty) {
        val biggestpow = lpowl.last
        lpowneeded = lpowneeded :+ biggestpow
        ql -= biggestpow
      }
    } while (!lpowl.isEmpty)

    lpowneeded = lpowneeded.sortBy(i => i)
    //println(q, lpowneeded)

    var result = (BigInt(0), BigInt(0))
    if (q > 0) {
      val lpowneeded2compute = lpow.takeWhile(pow => pow <= lpowneeded.last).toList.tail
      //println("+++", lpowneeded.last, lpowneeded2compute)
      var doubling = p
      var lpowneededComputed = lpowneeded2compute.map(i => {
        doubling = plus(doubling, doubling)
        //println("    ", i, doubling)
        if (lpowneeded.contains(i)) {
          //println("    ---", i, doubling)
          doubling
        } else {
          (BigInt(0), BigInt(0))
        }
      }).filter(u => u != (BigInt(0), BigInt(0)))
      if (lpowneeded.contains(1)) {
        lpowneededComputed = lpowneededComputed :+ p
      }
      //println(lpowneededComputed)
      lpowneededComputed.foreach(u => {
        //print(result, u, "=")
        result = plus(result, u)
        //println(result)
      })
    }
    (p, lpowneeded.sum, result)
  }

  def check(p: (BigInt, BigInt)): Boolean = {
    (p._1 == 0, p._2 == 0) match {
      case (true, true) => true
      case _ => {
        ((p._1 * p._1 * p._1) + (a * p._1) + b) % modlo == (p._2 * p._2) % modlo
      }
    }
  }

  def checkVerbose(p: (BigInt, BigInt)): Boolean = {
    (p._1 == 0, p._2 == 0) match {
      case (true, true) => true
      case _ => {
        println("\n[", modlo, "]", p._1, (p._1 * p._1 * p._1), (a * p._1), b, "-->", (p._1 * p._1 * p._1) + (a * p._1) + b, ((p._1 * p._1 * p._1) + (a * p._1) + b) % modlo, " vs ", p._2, (p._2 * p._2), (p._2 * p._2) % modlo)
        ((p._1 * p._1 * p._1) + (a * p._1) + b) % modlo == (p._2 * p._2) % modlo
      }
    }
  }

  def getDelta = -16 * ((4 * a * a * a) + (27 * b * b))

  def liste_des_ordres_non_egaux_a_la_taille_de_la_courbe = curve.map(p => {
    var ordre = 0
    var somme = plus(p, p)
    var lsum = List[(BigInt, BigInt)]()
    (1 to curve.size + 4).toList.find(i => {
      somme = plus(somme, p)
      lsum = lsum :+ somme
      if (somme._1 == 0 & somme._2 == 0) {
        //println("===", p, i, somme, "===",lsum)
        ordre = i + 2
      }
      somme._1 == 0 & somme._2 == 0
    })
    (p, ordre)
  }).filter(_._2 != curve.size + 1)

  def loopsum(p: (BigInt, BigInt)) = {
    var ordre = 0
    var somme = plus(p, p)
    var lsum = List[(BigInt, BigInt)]()
    (1 to curve.size + 4).toList.find(i => {
      somme = plus(somme, p)
      lsum = lsum :+ somme
      if (somme._1 == 0 & somme._2 == 0) {
        //println("===", p, i, somme, "===",lsum)
        ordre = i + 2
      }
      somme._1 == 0 & somme._2 == 0
    })
    ("loopsum , p " + p + ", ordre " + ordre + ", plus(p, p )" + plus(p, p) + ", plus(plus(p, p), p) " + plus(plus(p, p), p) + ", lsum ", p, lsum.size, lsum,
      (lsum.map(p => (p._1.toDouble, p._2.toDouble)) :+ (p._1.toDouble, p._2.toDouble) :+ (plus(p, p)._1.toDouble, plus(p, p)._2.toDouble)).distinct)
  }

  def loopsum2(p: (BigInt, BigInt)): List[(BigInt, BigInt)] = List(p, plus(p, p)) ++ loopsum(p)._4

  def loopmul2(first: (BigInt, BigInt)): List[(BigInt, BigInt)] = {
    val rnd = new Random(0)
    var lr = List[(BigInt, BigInt)]()
    var current = first
    println("  *2*****************************************2*")
    do {
      print(". " + current + "*2")
      current = plus(current, current)
      print("=" + current)
      lr = lr :+ current
      if (rnd.nextInt(10) == 0) {
        print("\n  *2  [", modlo, "]")
      }
    } while (current.toString != first.toString() & lr.distinct.size == lr.size)
    println("")
    lr
  }

  def loopmul3(first: (BigInt, BigInt)): List[(BigInt, BigInt)] = {
    val rnd = new Random(0)
    var lr = List[(BigInt, BigInt)]()
    var current = first
    println("  *3*****************************************3*")
    do {
      print("- " + current + "*3")
      current = mul(current, 3)._3
      print("=" + current)
      lr = lr :+ current
      if (rnd.nextInt(10) == 0) {
        print("\n  *3 [", modlo, "]")
      }

    } while (current.toString != first.toString() & lr.distinct.size == lr.size)
    println("")
    lr
  }

  def loopmul4(first: (BigInt, BigInt)): List[(BigInt, BigInt)] = {
    val rnd = new Random(0)
    var lr = List[(BigInt, BigInt)]()
    var current = first
    println("  *4*****************************************4*")
    do {
      print("_ " + current + "*4")
      current = mul(current, 4)._3
      print("=" + current)
      lr = lr :+ current
      if (rnd.nextInt(10) == 0) {
        print("\n  *4 [", modlo, "]")
      }
    } while (current.toString != first.toString() & lr.distinct.size == lr.size)
    println("")
    lr
  }

  def title = "y2 = x3 + " + a + "x + " + b + "  Ordre " + modlo

  def SumLine(p: (BigInt, BigInt), q: (BigInt, BigInt)): (MySeries, Double, Double) = {
    val pd = (p._1.toDouble, p._2.toDouble)
    val qd = (q._1.toDouble, q._2.toDouble)
    val modlod = modlo.toDouble
    val diff_x = qd._1 - pd._1
    var _a_ = 0.toDouble
    var _b_ = 0.toDouble

    val ms = diff_x match {
      case 0 => new MySeries("Droite Verticale [" + p + "," + q + "]", true, false, List((pd._1, 0.toDouble), (pd._1, modlod)).sortBy(_._1))
      case _ => // compute a & b in y = ax + b
        _a_ = (q._2.toDouble - p._2.toDouble) / diff_x
        _b_ = q._2.toDouble - (q._1.toDouble * _a_)
        val atZero = (0.toDouble, _b_)
        var atModloY = (_a_ * modlod) + _b_
        while (atModloY < 0) {
          atModloY += modlod
        }
        while (atModloY > modlod) {
          atModloY -= modlod
        }
        val atModlo = (modlod, atModloY)

        val l = List(atZero, pd, qd, atModlo)

        myPrintIt(p, q, _a_, _b_, atZero, atModlo)
        myPrintIt(pd, qd)
        myPrintIt(_a_ * pd._1, (_a_ * pd._1) + _b_, pd._2)
        myPrintIt(_a_ * qd._1, (_a_ * qd._1) + _b_, qd._2)
        myPrintIt(_a_ * modlod, (_a_ * modlod) + _b_, atModloY)
        myPrintIt(p, q, "y = " + _a_ + "*x + " + _b_)

        /* 2 eme ligne
        l = l :+ (0.toDouble, _b_ + atModloY)
        l = l :+ (modlod, _b_ + atModloY + (_a_ * modlod))*/

        new MySeries("Droite [" + p + "," + q + "] y = " + _a_ + " *x + " + _b_, true, false, l.sortBy(_._1))
    }
    (ms, _a_, _b_)
  }
}

class getCurve(val modlo: BigInt) {
  def rangeStream(a: BigInt, b: BigInt): Stream[BigInt] = a #:: rangeStream(b, 1 + b)

  def stream_zero_a_linfini: Stream[BigInt] = rangeStream(0, 1)

  val l1 = stream_zero_a_linfini take (modlo.toInt + 2) toList
  val l2 = l1.map(i => (i, (i * i) mod modlo))
  val l3p7 = l1.map(i => (i, ((i * i * i) + 7) mod modlo))
  val lp = l3p7.map(x => {
    l2.filter(y => y._2 == x._2).map(y => (x._1 % modlo, y._1 % modlo))
  }).flatten
}

class getInverse(val modlo: BigInt) {
  def rangeStream(a: BigInt, b: BigInt): Stream[BigInt] = a #:: rangeStream(b, 1 + b)

  def stream_zero_a_linfini: Stream[BigInt] = rangeStream(0, 1)

  val l1 = stream_zero_a_linfini.take(modlo.toInt).toList
  val li = l1.tail.map(i => {
    (i, l1.filter(u => ((u * i) % modlo) == 1).head)
  })
}

class Triplet(val a: BigInt, val b: BigInt) {
  val d = (a, b, b mod 67)

  override def equals(x: Any): Boolean = d._3.equals(x.asInstanceOf[Triplet].d._3)

  override def toString: String = d.toString
}

class Doublon(val x: BigInt, val y: BigInt) {
  def check: Boolean = new Triplet(y, y * y).equals(new Triplet(x, (x * x * x) + 7))

  override def toString: String = (x, y, check).toString
}


