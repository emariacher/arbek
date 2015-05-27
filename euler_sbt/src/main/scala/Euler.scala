/**
 * Created by mariachere on 26.05.2015.
 */
object Euler {
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
}
