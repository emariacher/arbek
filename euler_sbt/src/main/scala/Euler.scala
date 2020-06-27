/**
 * Created by mariachere on 26.05.2015.
 */

import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat

import scala.collection.immutable.NumericRange
import scala.math.BigInt

object Euler {
  val t_start = Calendar.getInstance()

  def printZisday(zisday: Calendar, fmt: String): String = printZisday(zisday.getTime(), fmt)

  def printZisday(date: Date, fmt: String): String = new String(new SimpleDateFormat(fmt).format(date))

  def timeStamp(c_t_start: Calendar, s_title: String, verbose: Boolean = true): Calendar = {
    val t_end = Calendar.getInstance()
    val t_diff = t_end.getTimeInMillis - c_t_start.getTimeInMillis
    if (verbose) {
      println("t_now: " + printZisday(t_end, "ddMMMyy_HH_mm_ss_SSS [") + s_title +
        "] t_diff: " + t_diff)
    }
    t_end
  }

  def timeStampD(c_t_start: Calendar, s_title: String, verbose: Boolean = true): Long = {
    val t_end = Calendar.getInstance()
    val t_diff = t_end.getTimeInMillis - c_t_start.getTimeInMillis
    if (verbose) {
      println("t_now: " + printZisday(t_end, "ddMMMyy_HH_mm_ss_SSS [") + s_title +
        "] t_diff: " + t_diff)
    }
    t_diff
  }

  def timeStampS(c_t_start: Calendar, s_title: String): (Calendar, String) = {
    val t_end = Calendar.getInstance()
    val t_diff = t_end.getTimeInMillis - c_t_start.getTimeInMillis
    (t_end, "t_now: " + printZisday(t_end, "ddMMMyy_HH_mm_ss_SSS [") + s_title +
      "] t_diff: " + t_diff)
  }

  def powl(pn: BigInt, n: Int): BigInt = {
    val div = 40
    val z2 = new NumericRange(1, (n / div) + 1, 1, true).toList.map((i: Int) => pn).product
    val z4 = new NumericRange(1, (n % div) + 1, 1, true).toList.map((i: Int) => pn).product
    val z5 = new NumericRange(1, div + 1, 1, true).toList.map((i: Int) => z2).product
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

  def triangular(a: BigInt): Seq[BigInt] = {
    var l = List[BigInt](0)
    (1 to a.toInt).foreach(b => {
      l = l :+ (b + l.last)
    })
    l
  }

  def rangeStream(a: BigInt, b: BigInt): LazyList[BigInt] = a #:: rangeStream(b, 1 + b)

  def stream_zero_a_linfini: LazyList[BigInt] = rangeStream(0, 1)

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
