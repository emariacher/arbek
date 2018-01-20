/**
 * Created by mariachere on 26.05.2015.
 */

import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat

import scala.math.BigInt
import scala.util.Random

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

class Elliptique(val modlo: BigInt) {
  def rangeStream(a: BigInt, b: BigInt): Stream[BigInt] = a #:: rangeStream(b, 1 + b)

  def stream_zero_a_linfini: Stream[BigInt] = rangeStream(0, 1)

  val l1 = stream_zero_a_linfini.take(modlo.toInt).toList
  val li = l1.tail.map(i => {
    (i, l1.filter(u => ((u * i) % modlo) == 1).head)
  })
  val l2 = l1.map(i => (i, (i * i) mod modlo))
  val l3p7 = l1.map(i => (i, ((i * i * i) + 7) mod modlo))
  val curve = l3p7.map(x => {
    l2.filter(y => y._2 == x._2).map(y => (x._1 % modlo, y._1 % modlo))
  }).flatten

  def add(a: BigInt, b: BigInt) = a + b % modlo

  def sub(a: BigInt, b: BigInt) = {
    val diff1 = a - b
    if (diff1 < 0) diff1 + modlo else diff1
  }

  def mul(a: BigInt, b: BigInt) = a * b % modlo

  def getLambda(p: (BigInt, BigInt), q: (BigInt, BigInt)) = {
    (sub(q._1, p._1) == 0, sub(q._2, p._2) == 0) match {
      case (true, true) => (mul(mul(p._1, p._1), 3) * li.filter(_._1 == mul(p._2, 2)).head._2) % modlo
      case _ => (sub(q._2, p._2) * li.filter(_._1 == sub(q._1, p._1)).head._2) % modlo
    }
  }

  def plus(p: (BigInt, BigInt), q: (BigInt, BigInt)) = {
    (sub(q._1, p._1) == 0 & sub(q._2, p._2) != 0) match {
      case true => (BigInt(0), BigInt(0))
      case _ => val lambda = getLambda(p, q)
        val xr = sub(sub(mul(lambda, lambda), p._1), q._1)
        val yr = sub(mul(lambda, sub(p._1, xr)), p._2)
        (xr, yr)
    }
  }

  def check(p: (BigInt, BigInt)): Boolean = {
    (p._1 == 0, p._2 == 0) match {
      case (true, true) => true
      case _ => {
        //println("\n[", modlo, "]", p._1, (p._1 * p._1 * p._1) + 7, ((p._1 * p._1 * p._1) + 7) % modlo, " vs ", p._2, (p._2 * p._2), (p._2 * p._2) % modlo)
        ((p._1 * p._1 * p._1) + 7) % modlo == (p._2 * p._2) % modlo
      }
    }
  }


  def loopmul2(first: (BigInt, BigInt)): List[(BigInt, BigInt)] = {
    val rnd = new Random(0)
    var lr = List[(BigInt, BigInt)]()
    var current = first
    println("*2*****************************************2*")
    do {
      print(". " + current + "*2")
      current = plus(current, current)
      print("=" + current)
      lr = lr :+ current
      if (rnd.nextInt(10) == 0) {
        print("\n *2  [", modlo, "]")
      }
    } while (current.toString != first.toString() & lr.distinct.size == lr.size)
    println("")
    lr
  }

  def loopmul3(first: (BigInt, BigInt)): List[(BigInt, BigInt)] = {
    val rnd = new Random(0)
    var lr = List[(BigInt, BigInt)]()
    var current = first
    println("*3*****************************************3*")
    do {
      print("- " + current + "*3")
      current = plus(plus(current, current), current)
      print("=" + current)
      lr = lr :+ current
      if (rnd.nextInt(10) == 0) {
        print("\n *3 [", modlo, "]")
      }

    } while (current.toString != first.toString() & lr.distinct.size == lr.size)
    println("")
    lr
  }

  def loopmul4(first: (BigInt, BigInt)): List[(BigInt, BigInt)] = {
    val rnd = new Random(0)
    var lr = List[(BigInt, BigInt)]()
    var current = first
    println("*4*****************************************4*")
    do {
      print("_ " + current + "*4")
      val double = plus(current, current)
      current = plus(double, double)
      print("=" + current)
      lr = lr :+ current
      if (rnd.nextInt(10) == 0) {
        print("\n *4 [", modlo, "]")
      }
    } while (current.toString != first.toString() & lr.distinct.size == lr.size)
      println("")
    lr
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


