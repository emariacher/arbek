/**
 * Created by mariachere on 26.05.2015.
 */

import scala.collection.immutable.TreeSet
import scala.collection.immutable.NumericRange

class EulerDiv(bi: BigInt) {
  val primes = new EulerDiv2(bi, EulerPrime.premiers100000, false).primes
}

class EulerDiv2(bi: BigInt, premiers: TreeSet[BigInt], verbose: Boolean = false, nofail: Boolean = false) {
  var primes = List[BigInt]()
  var bic = bi
  var index = 0
  var solved = true

  premiers.takeWhile((premier: BigInt) => {
    while (bic % premier == 0 & bic >= premier) {
      primes = primes :+ premier
      bic = bic / premier
    }
    if (bic == 1) {
      false
    } else if ((premier * premier) > bic) {
      if (bic != bi) {
        primes = primes :+ bic
        require(primes.product == bi)
      } else if (primes.isEmpty) {
        primes = primes :+ bic
      }
      false
    } else {
      true
    }
  })
  if (primes.isEmpty) {
    if (!nofail) {
      require(bi < (premiers.last * premiers.last), bi.toString + "<" + premiers.last + "*" + premiers.last + "(" + ((premiers.last * premiers.last), math.sqrt(bic.toDouble)) + ")")
    } else if (bi >= (premiers.last * premiers.last)) {
      solved = false
      System.err.println("Can't decide1[" + bi + "]: " + bi.toString + "<" + premiers.last + "*" + premiers.last + "(" + ((premiers.last * premiers.last), math.sqrt(bic.toDouble)) + ")")
    }

  } else {
    if (verbose) {
      println("          EulerDiv " + bi, primes, bic, math.sqrt(bic.toDouble))
    }
    if (!nofail) {
      require(bic < (premiers.last * premiers.last), bic.toString + "<" + premiers.last + "*" + premiers.last + "(" + ((premiers.last * premiers.last), math.sqrt(bic.toDouble)) + ")")
    } else if (bic >= (premiers.last * premiers.last)) {
      System.err.println("Can't decide2[" + bi + "]: " + bic.toString + "<" + premiers.last + "*" + premiers.last + "(" + ((premiers.last * premiers.last), math.sqrt(bic.toDouble)) + ")")
      solved = false
    }
  }
}

class EulerDivisors(val l: List[BigInt]) {
  val range = new NumericRange(1, l.size, 1, true).toList

  def this(ed: EulerDiv) = this(ed.primes)

  val divisors = range.foldLeft(List[List[BigInt]]())(_ ++ l.combinations(_)).map(_.product).distinct.sorted
  val primesUnique = TreeSet[BigInt]() ++ l

  def getFullDivisors = divisors ++ List[BigInt](1, l.product)
}

class EulerDivisors3(l: List[BigInt]) {
  val range = new NumericRange(1, l.size + 1, 1, true).toList
  val divisors = range.foldLeft(List[List[BigInt]]())(_ ++ l.combinations(_)).map(_.product).distinct.sorted
  val primesUnique = TreeSet[BigInt]() ++ l
}

class EulerDivisors2(val l: List[BigInt], val bi: BigInt) {
  val range = new NumericRange(1, l.size, 1, true).toList
  val result = range.find((i: Int) => l.combinations(i).toList.map(_.product).contains(bi))
  var found = false
  result match {
    case Some(i) => found = true
    case _ => found = false
  }
}
