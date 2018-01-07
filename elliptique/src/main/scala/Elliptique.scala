/**
 * Created by mariachere on 26.05.2015.
 */

import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat

import scala.math.BigInt

object Elliptique {
  val t_start = Calendar.getInstance()

  def printZisday(zisday: Calendar, fmt: String): String = printZisday(zisday.getTime(), fmt)

  def printZisday(date: Date, fmt: String): String = new String(new SimpleDateFormat(fmt).format(date))

  def timeStamp(c_t_start: Calendar, s_title: String): Calendar = {
    val t_end = Calendar.getInstance()
    println("t_now: " + printZisday(t_end, "ddMMMyy_HH_mm_ss_SSS [") + s_title +
      "] t_diff: " + (t_end.getTimeInMillis() - c_t_start.getTimeInMillis()))
    t_end
  }

  def timeStampS(c_t_start: Calendar, s_title: String): (Calendar, String) = {
    val t_end = Calendar.getInstance()
    (t_end, "t_now: " + printZisday(t_end, "ddMMMyy_HH_mm_ss_SSS [") + s_title +
      "] t_diff: " + (t_end.getTimeInMillis() - c_t_start.getTimeInMillis()))
  }

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
}

object Inverse67 {
  val li = new getInverse(67).li
  println("Inverses: ", li)

  def sub(a: BigInt, b: BigInt, modlo: BigInt) = {
    val diff1 = a - b
    if (diff1 < 0) diff1 + modlo else diff1
  }

  def mul(a: BigInt, b: BigInt, modlo: BigInt) = a * b % modlo

  def getLambda(p1: (BigInt, BigInt), p2: (BigInt, BigInt), modlo: BigInt) = {
    println(p1,p2)
    println(sub(p1._1, p2._1, modlo),sub(p1._2, p2._2, modlo))
    println(li.filter(_._1 == sub(p1._1, p2._1, modlo)))
    (sub(p2._2, p1._2, modlo) * li.filter(_._1 == sub(p2._1, p1._1, modlo)).head._2) % modlo
  }

  def plus(p1: (BigInt, BigInt), p2: (BigInt, BigInt), modlo: BigInt) = {
    val lambda = getLambda(p1, p2, modlo)
    println(p1,p2,lambda)
    val xr = sub(sub(mul(lambda, lambda, modlo), p1._1, modlo), p2._1, modlo)
    val yr = sub(mul(lambda, sub(p1._2, xr, modlo), modlo), p1._2, modlo)
    println(xr, yr)
    (xr, yr)
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

object modulo {
  val m = BigInt(67)
}

class Triplet(val a: BigInt, val b: BigInt) {
  val d = (a, b, b mod modulo.m)

  override def equals(x: Any): Boolean = d._3.equals(x.asInstanceOf[Triplet].d._3)

  override def toString: String = d.toString
}

class Doublon(val x: BigInt, val y: BigInt) {
  def check: Boolean = new Triplet(y, y * y).equals(new Triplet(x, (x * x * x) + 7))

  override def toString: String = (x, y, check).toString
}


