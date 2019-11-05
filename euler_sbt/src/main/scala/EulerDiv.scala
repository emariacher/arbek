/**
 * Created by mariachere on 26.05.2015.
 */
import scala.collection.immutable.TreeSet
import scala.collection.immutable.NumericRange

class EulerDiv(bi: BigInt) {
  var primes = List[BigInt]()
  val premiers =  EulerPrime.premiers100000
  var bic = bi
  var index = 0

  premiers.takeWhile((premier: BigInt) => {
    //println("*i1* bi: "+bi+" bic: "+bic+" premier: "+premier+" "+(bic%premier==0&bic>=premier)+" "+primes)
    while (bic % premier == 0 & bic >= premier) {
      primes = primes :+ premier
      bic = bic / premier
    }
    //println("*i2* bi: "+bi+" bic: "+bic+" premier: "+premier+" "+(bic%premier==0&bic>=premier)+" "+primes)
    if (bic == 1) {
      false
    } else if ((premier * premier) > bic) {
      //println("*i3* bi: "+bi+" bic: "+bic+" premier: "+premier+" "+(bic%premier==0&bic>=premier)+" "+primes)
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
    require(bi < (premiers.last * premiers.last), bi.toString + "<" + premiers.last + "*" + premiers.last + "(" + (premiers.last * premiers.last) + ")")
  } else {
    require(bic < (premiers.last * premiers.last), bic.toString + "<" + premiers.last + "*" + premiers.last + "(" + (premiers.last * premiers.last) + ")")
  }
}

class EulerDivisors(val l: List[BigInt]) {
  val range = new NumericRange(1, l.size, 1, true).toList
  def this(ed: EulerDiv) = this(ed.primes)
  val divisors = range.foldLeft(List[List[BigInt]]())(_ ++ l.combinations(_)).map(_.product).distinct.sorted
  val primesUnique = TreeSet[BigInt]() ++ l
  def getFullDivisors = divisors ++ List[BigInt](1,l.product)
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
    case _       => found = false
  }
}
