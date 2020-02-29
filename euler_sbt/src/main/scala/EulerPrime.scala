/**
 * Created by mariachere on 26.05.2015.
 */

import scala.collection.immutable.TreeSet
import scala.collection.immutable.NumericRange

object EulerPrime {
  //  val premiers1000000 = (new CheckEulerPrime(1000000,1000)).premiers
  //  val premiers100000  = premiers1000000.takeWhile(_ < 100000)
  val premiers100000 = (new CheckEulerPrime(100000, 1000)).premiers
  //  val premiers100000  = premiers1000000.takeWhile(_ < 100000)
  val premiers10000 = premiers100000.takeWhile(_ < 10000)
  val premiers1000 = premiers10000.takeWhile(_ < 1000)
  println("")

  def isPrime(bi: BigInt, verbose: Boolean = false): Boolean = {
    if (bi < 100000) {
      premiers100000.contains(bi)
    } else {
      var divisor = BigInt(0)
      premiers100000.takeWhile((premier: BigInt) => {
        if (bi % premier == 0) {
          divisor = premier
          if (verbose) {
            println("" + premier + " est un diviseur de " + bi)
          }
          false
        } else if ((premier * premier) > bi) {
          false
        } else {
          true
        }
      })
      if (divisor == 0) {
        require(bi < (premiers100000.last * premiers100000.last))
      }
      divisor == 0
    }
  }

  def getPrimesBetween(low: BigInt, high: BigInt, premiers: TreeSet[BigInt]) = {
    require(high <= premiers.last)
    require(low < high)
    premiers.dropWhile(_ < low).takeWhile(_ < high)
  }
}

class EulerPrime(val top: BigInt, val inc: Int) {
  var premiersForCompute = TreeSet[BigInt]()
  val sqtop = Euler.sqrt(top) + 1
  assume(sqtop * sqtop > top)
  assume((new NumericRange(0, 10, 1, false)).last == 9)
  assume((new NumericRange(1, 11, 2, false)).last == 9)
  assume(top % inc == 0)
  val main = new NumericRange(0, (top / inc).toInt, 1, true)

  def init1er4compute = {
    inc match {
      case 100 => premiersForCompute = TreeSet(2, 3, 5, 7, 11)
      case 1000 => premiersForCompute = TreeSet(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37)
      case 10000 => premiersForCompute = TreeSet(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101)
      case _ => require(false, inc.toString + " not allowed!")
    }
  }

  def computePrime(callback: (Int, TreeSet[BigInt]) => Int) = {
    init1er4compute
    assume((premiersForCompute.last * premiersForCompute.last) > inc)
    callback(0, premiersForCompute)
    main.foreach((index: Int) => {
      var start = (inc * index) + 1
      var end = inc + start

      //				val sqend = Math.sqrt(end).toInt+1
      var range = TreeSet[BigInt]() ++ (new NumericRange(start, end, 2, true)).toList.map(BigInt(_))
      //println("range: "+range)
      premiersForCompute.foreach((premier: BigInt) => {
        range = range.filter(_ % premier != 0)
        //println("  premier: "+premier+" range: "+range)
      })
      if (index == 0) {
        require(range.head == 1)
        range = range.tail
      }

      assume(!range.isEmpty, "increment[" + inc + "] start: " + start + " end: " + end + " " + premiersForCompute)
      if (range.head < sqtop) {
        premiersForCompute = (TreeSet[BigInt]() ++ premiersForCompute ++ range)
      }
      callback(index, range)
      print("." + range.head + "-" + range.last)
      if (index % 100 == 0) {
        println("\n" + index)
      }
    })
    println("\n")
  }

  def computePrime2(top: BigInt, inc: Int) = {
    //printIt(top, inc)
    init1er4compute
    assume((premiersForCompute.last * premiersForCompute.last) > inc)
    val premiersForCompute2 = premiersForCompute.toList
    val plength = premiersForCompute2.length
    var premiers = premiersForCompute ++ (premiersForCompute.last.toInt to inc by 2).map(BigInt(_)).filter(bi => {
      premiersForCompute2.takeWhile(p => bi % p != 0).length == plength
    })
    premiersForCompute = premiers
    assert(top % inc == 0)
    premiers = premiersForCompute ++ (1 to (top / inc).toInt - 1).map(mi => {
      val base = mi * inc
      val premiersForCompute2 = premiersForCompute.toList.takeWhile(_.toDouble < Math.sqrt(base + inc.toDouble) + 1)
      val plength = premiersForCompute2.length
      if (mi % 100 == 0) {
        println("\n" + mi)
      }
      val newPrimes = (base + 1 to base + inc + 1 by 2).map(BigInt(_)).filter(bi => {
        premiersForCompute2.takeWhile(p => bi % p != 0).length == plength
      })
      print("." + newPrimes.last)
      newPrimes
    }).flatten
    println("\n")
    premiers
  }
}

class CheckEulerPrime(override val top: BigInt, override val inc: Int) extends EulerPrime(top, inc) {
  init1er4compute
  var premiers = premiersForCompute
  val callback = (index: Int, range: TreeSet[BigInt]) => {
    premiers = premiers ++ range
    0
  }
  //computePrime(callback)
  premiers = computePrime2(top, inc)

  def check: Boolean = {
    val prems = premiers.toList
    //println("\n  act 1000premiers="+premiers.filter(_<1000))
    println("\n  act 10000eme premier=" + prems.apply(9999))
    assert(prems.apply(9999) == 104729)
    assert(premiers.last == 104999)
    val ref10000PrimeNumbers = getref10000PrimeNumbersFromWeb
    println("\n  exp 10000eme premier=" + ref10000PrimeNumbers.apply(9999))
    val diff = prems.filterNot(ref10000PrimeNumbers.contains(_)).grouped(10).toList
    //println("\n  diff:\n " + diff.mkString("\n  "))
    assert(prems.apply(9999) == ref10000PrimeNumbers.apply(9999))
    true
  }

  def getref10000PrimeNumbersFromWeb: List[Int] = {
    val r_int = """(\d+)""".r;
    val url = "http://primes.utm.edu/lists/small/10000.txt"
    val data = io.Source.fromURL(url).mkString
    var premiersRef = List[Int]()
    data.split(" ").map(_ match {
      case r_int(i) => premiersRef = premiersRef :+ i.toInt
      case _ =>
    })
    premiersRef.sorted
  }
}
