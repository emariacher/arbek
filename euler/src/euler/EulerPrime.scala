package euler
import scala.collection.immutable.ListSet
import scala.collection.immutable.TreeSet
import EulerPrime._
import kebra.MyLog._

object EulerPrime {
    //  val premiers1000000 = (new CheckEulerPrime(1000000,1000)).premiers
    //  val premiers100000  = premiers1000000.takeWhile(_ < 100000)
    val premiers100000 = (new CheckEulerPrime(100000, 1000)).premiers
    //  val premiers100000  = premiers1000000.takeWhile(_ < 100000)
    val premiers10000 = premiers100000.takeWhile(_ < 10000)
    val premiers1000 = premiers10000.takeWhile(_ < 1000)
    println("")

    def isPrime(bi: BigInt): Boolean = {
        if (bi < 100000) {
            premiers100000.contains(bi)
        } else {
            var divisor = BigInt(0)
            premiers100000.takeWhile((premier: BigInt) => {
                if (bi % premier == 0) {
                    divisor = premier
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
        myRequire(high <= premiers.last)
        myRequire(low < high)
        premiers.dropWhile(_ < low).takeWhile(_ < high)
    }
}

class EulerPrime(val top: BigInt, val inc: Int) {
    var premiersForCompute = TreeSet[BigInt]()
    val sqtop = Math.sqrt(top.toInt).toInt + 1
    assume(sqtop * sqtop > top)
    assume((new Range(0, 10, 1)).last == 9)
    assume((new Range(1, 11, 2)).last == 9)
    assume(top % inc == 0)
    val main = new Range(0, (top / inc).toInt, 1)

    def init1er4compute = {
        inc match {
            case 100  => premiersForCompute = TreeSet(2, 3, 5, 7, 11)
            case 1000 => premiersForCompute = TreeSet(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37)
            case _    => require(false, inc + " not allowed!")
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
            var range = TreeSet[BigInt]() ++ (new Range(start, end, 2)).toList.map(BigInt(_))
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

}

class CheckEulerPrime(override val top: BigInt, override val inc: Int) extends EulerPrime(top, inc) {
    init1er4compute
    var premiers = premiersForCompute
    val callback = (index: Int, range: TreeSet[BigInt]) => {
        premiers = premiers ++ range
        0
    }
    computePrime(callback)

    def check: Boolean = {
        val prems = premiers.toList
        //println("\n  act 1000premiers="+premiers.filter(_<1000))
        println("\n  act 10000eme premier=" + prems.apply(9999))
        val ref10000PrimeNumbers = getref10000PrimeNumbersFromWeb
        println("\n  exp 10000eme premier=" + ref10000PrimeNumbers.apply(9999))
        val diff = prems.filterNot(ref10000PrimeNumbers.contains(_)).grouped(10).toList
        //println("\n  diff:\n " + diff.mkString("\n  "))
        myAssert2(prems.apply(9999),104729)
        myAssert2(prems.apply(9999), ref10000PrimeNumbers.apply(9999))
        myErrPrintDln("Check is OK!")
        true
    }

    def getref10000PrimeNumbersFromWeb: List[Int] = {
        val r_int = """(\d+)""".r;
        val url = "http://primes.utm.edu/lists/small/10000.txt"
        val data = io.Source.fromURL(url).mkString
        var premiersRef = List[Int]()
        data.split(" ").map(_ match {
            case r_int(i) => premiersRef = premiersRef :+ i.toInt
            case _        =>
        })
        premiersRef.sorted
    }
}

class Euler10 extends EulerPrime(2000000, 1000) {
    init1er4compute
    //var sum: BigInt = premiersForCompute.sum
    var sum = BigInt(0)

    def getSum: BigInt = {
        computePrime((index: Int, range: TreeSet[BigInt]) => {
            sum = sum + range.sum
            0
        })
        println("\n" + top + " Euler10 sum=" + sum)
        sum
    }
}

class Euler50 extends CheckEulerPrime(1000000, 1000) {
    computePrime(callback)
    var max = (TreeSet[BigInt](), BigInt(0))

    def euler50GetConsecPrime(limit: Int): (TreeSet[BigInt], BigInt) = {
        /*              val l = premiers.takeWhile((p: Int) => p <= limit).map((p: Int) => euler50GetConsecPrime2(premiers,p,limit))
                val max = l.map(_._1.size).max
                l.find(_._1.size==max) match {
                  case Some(z) => println("***"+z._1.size+" "+z); z
                  case _ => (List[Int](),0)
                }*/
        max = (TreeSet[BigInt](), 0)
        premiers.takeWhile((p: BigInt) => p <= limit).takeWhile((p: BigInt) => euler50GetConsecPrime2(premiers, p, limit))
        println("***" + max._1.size + " <" + max._2 + "> " + max)
        require(isPrime(max._2))
        require(max._2 == max._1.sum)
        max

    }

    def euler50GetConsecPrime2(premiers: TreeSet[BigInt], start: BigInt, limit: Int): Boolean = {
        val l = premiers.filter((p: BigInt) => limit >= p & p >= start)
        if (l.take(max._1.size).sum < limit) {
            var start2 = euler50GetSum(l, limit)
            var consecPrime = start2._2
            var consecPrimeList = start2._1
            while (!isPrime(consecPrime) & (max._1.size < consecPrimeList.size)) {
                consecPrime = consecPrime - consecPrimeList.last
                consecPrimeList = consecPrimeList.dropRight(1)
                require(consecPrime == consecPrimeList.sum, consecPrime + "==" + consecPrimeList.sum)
                print("." + consecPrime + "-" + (isPrime(consecPrime)))
            }
            if (max._1.size < consecPrimeList.size) {
                max = (consecPrimeList, consecPrime)
                if (max._1.size > 2) {
                    println("\n      New Max1 limit:" + limit + " start:" + start + " consecPrimeList.sum:" + (consecPrimeList.sum,
                        (consecPrimeList.head, consecPrimeList.last)) + " max:" + (max._2, (max._1.head, max._1.last)))
                } else {
                    println("\n      New Max2 limit:" + limit + " start:" + start + " consecPrimeList.sum:" + (consecPrimeList.sum,
                        consecPrimeList) + " max:" + (max._2, max))
                }
            } else {
                if (max._1.size > 2) {
                    println("\n      NoNew Max1 limit:" + limit + " start:" + start + " consecPrimeList.sum:" + (consecPrimeList.sum,
                        (consecPrimeList.head, consecPrimeList.last)) + " max:" + (max._2, (max._1.head, max._1.last)))
                } else {
                    println("\n      NoNew Max2 limit:" + limit + " start:" + start + " consecPrimeList.sum:" + (consecPrimeList.sum,
                        consecPrimeList) + " max:" + (max._2, max))
                }
                if (max._1.size != consecPrimeList.size) {
                    require(!isPrime(consecPrime), "!isPrime(" + consecPrime + ")")
                }
            }

            true
        } else {
            if (max._1.size > 2) {
                println("      Stopping1 limit:" + limit + " start:" + start + " l.take(max._1.size).sum:" + (l.take(max._1.size).sum,
                    (l.take(max._1.size).head, l.take(max._1.size).last)) + " max:" + (max._2, (max._1.head, max._1.last)))
            } else {
                println("      Stopping2 limit:" + limit + " start:" + start + " l.take(max._1.size).sum:" + (l.take(max._1.size).sum,
                    l.take(max._1.size)) + " max:" + (max._2, max))
            }

            false
        }
    }

    def euler50GetSum(premiers: TreeSet[BigInt], limit: Int): (TreeSet[BigInt], BigInt) = {
        var sum = BigInt(0)
        val l = premiers.takeWhile((p: BigInt) => {
            val b = sum <= (limit - p)
            if (b) {
                sum += p
            }
            b
        })
        if (max._1.size > 2) {
            print("  1 limit:" + limit + " start:" + premiers.head + " " + (l.size, (l.head, l.last), sum) + " max:" + (max._2, (max._1.head, max._1.last)))
        } else {
            print("  2 limit:" + limit + " start:" + premiers.head + " " + (l.size, l, sum) + " max:" + (max._2, max))
        }
        (l, sum)
    }

}

class Euler123(top: Int, inc: Int) extends EulerPrime(top, inc) {
    val prems = premiers100000.toList
    var n = 0
    var zerange = TreeSet[BigInt]()
    var zelastn = 0

    // 7037 71059 1000084366
    // toujours croissant
    val dix9 = Euler.powl(10, 9)
    val dix10 = Euler.powl(10, 10)

    def rfunc(n: Int, pn: BigInt): BigInt = {
        (Euler.powl(pn - 1, n) + Euler.powl(pn + 1, n)) % (pn * pn)
    }

    def rfunc2(n: Int, pn: BigInt, limit: BigInt): Boolean = {
        var r = rfunc(n, pn)
        //println("\n4["+n+"] "+pn+" "+r+" "+(r>dix9)+" "+(r>dix10))
        r > limit
    }

    val callback3 = (index: Int, range: TreeSet[BigInt]) => {
        n += range.size
        val pn = range.last
        //println("\n3["+n+"] "+pn+"    ")
        if (n % 2 != 0) {
            val r = rfunc(n, pn)
            //println("\n1["+n+"] "+pn+" "+r+" "+(r>dix9)+" "+(r>dix10))
            if ((r > dix10) & (zelastn == 0)) {
                println("\n1[" + n + "] " + pn + " " + r + " " + (r > dix9) + " " + (r > dix10))
                zerange = range
                zelastn = n
            }
        } else {
            val pn = range.toList.reverse.tail.head
            val r = rfunc(n - 1, pn)
            //println("\n2["+(n-1)+"] "+pn+" "+r+" "+(r>dix9)+" "+(r>dix10))
            if ((r > dix10) & (zelastn == 0)) {
                zerange = range.drop(1)
                zelastn = n - 1
            }
        }
        0
    }

    rfunc2(7037, 71059, dix9)
    /*	rfunc2(69095,869989, dix10)
	rfunc2(50005,611999, dix10)*/

    /*init1er4compute

	callback3(0,premiersForCompute)*/
    computePrime(callback3)
    //println("\n"+zerange+" * "+zelastn)
    n = zelastn - zerange.size
    var solution = 0
    zerange.takeWhile((pn: BigInt) => {
        n += 1
        if (n % 2 != 0) {
            val r = rfunc2(n, pn, dix10)
            if (r) {
                solution = n
                println("\neuler123 solution: " + solution)
            }
            !r
        } else {
            true
        }
    })

}

class Euler234 extends EulerPrime(1000000, 1000) {
    var solution = BigInt(0)
    var solution2 = List[BigInt]()
    var lastPrime = BigInt(2)
    var limit = BigInt(0)
    val primes2 = premiers100000.map((bi: BigInt) => (bi, bi * bi))
    var z = List[BigInt]()

    def petitNombre(limit: Int): (Int, BigInt) = {
        println("\n*Euler234********* petitNombre(" + limit + ")")
        val range = new Range(5, limit + 1, 1).toList.map(BigInt(_))
        solution2 = range.filter((bi: BigInt) => {
            val avant = primes2.takeWhile((c: (BigInt, BigInt)) => c._2 <= bi).toList
            assume(!avant.isEmpty, "bi: " + bi + " " + primes2.take(5))
            val lps = avant.last._1
            val ups = (primes2 -- avant).head._1
            val dlps = if (bi % lps == 0) 1 else 0
            val dups = if (bi % ups == 0) 1 else 0
            if ((dlps + dups == 1) & (lps * lps != bi)) {
                //println(lps+" ["+bi+"] "+ups+" "+(new EulerDiv(bi)).primes)
                true
            } else {
                false
            }
        })
        println("\nEuler234 solution: " + solution2.size + " " + solution2.sum + " " + solution2.reverse.take(5))
        (solution2.size, solution2.sum)
    }

    def check(bi: BigInt): Boolean = {
        val avant = primes2.takeWhile((c: (BigInt, BigInt)) => c._2 <= bi).toList
        assume(!avant.isEmpty, "bi: " + bi + " " + primes2.take(5))
        val lps = avant.last._1
        val ups = (primes2 -- avant).head._1
        val dlps = if (bi % lps == 0) 1 else 0
        val dups = if (bi % ups == 0) 1 else 0
        if ((dlps + dups == 1) & (lps * lps != bi)) {
            println(lps + "_" + (lps * lps) + " [" + bi + "] " + ups + "_" + (ups * ups) + " " + (new EulerDiv(bi)).primes)
            true
        } else {
            false
        }
    }

    def callback(index: Int, range: TreeSet[BigInt]): Int = {
        assume(!range.isEmpty, " index: " + index)
        val l = (range + lastPrime).toList.sliding(2).toList.filter((c: List[BigInt]) => c.head * c.head < limit)
        z = l.flatMap((c: List[BigInt]) => {
            //println("  lastPrime: "+lastPrime+" "+c)
            val bas = c.head
            val bas2 = bas * bas
            val haut = c.last
            val haut2 = haut * haut
            val diff = haut2 - bas2
            val rangeBas = new Range(1, ((diff / bas) + 1).toInt, 1).toList.map((i: Int) => bas2 + (i * bas))
            val rangeHaut = new Range(1, ((diff / haut) + 1).toInt, 1).toList.map((i: Int) => haut2 - (i * haut))
            //println("   "+rangeBas+" "+rangeBas.filter(_%haut!=0))
            //println("   "+rangeHaut+" "+rangeHaut.filter(_%bas!=0))

            rangeBas.filter(_ % haut != 0) ++ rangeHaut.filter(_ % bas != 0).distinct.filter(_ < limit)
        }).distinct.sorted.reverse
        solution += z.sum
        lastPrime = range.last
        0
    }

    def getSolution(limitIn: BigInt): BigInt = {
        println("\n*Euler234********* getSolution(" + limitIn + ")")

        limit = limitIn
        solution = 0
        lastPrime = BigInt(2)
        /*init1er4compute
		callback(0,premiersForCompute)*/
        computePrime(callback)
        println("\nEuler234 solution: " + solution + " " + lastPrime + " " + z.toList.reverse.take(5))
        solution
    }
}

