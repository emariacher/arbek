package euler

import scala.collection.immutable.TreeSet

class Euler28 {
    val carres = new Range(0, 500, 1)
    var total = 1
    val sqtop = ((carres.last * 2) + 5).toInt + 1

    carres.foreach((carre: Int) => {
        val deuxNPlus1 = (2 * carre) + 1
        val deuxNPlus3 = deuxNPlus1 + 2
        var start = deuxNPlus1 * deuxNPlus1
        var end = deuxNPlus3 * deuxNPlus3

        val QuatreN2 = 4 * carre * carre
        val trois = QuatreN2 + (6 * carre) + 3
        val cinq = QuatreN2 + (8 * carre) + 5
        val sept = QuatreN2 + (10 * carre) + 7
        val neuf = QuatreN2 + (12 * carre) + 9
        total += trois + cinq + sept + neuf

        //println(carre+" "+neuf+" "+total+" "+((2*carre)+3))
    })
}

class Euler58quinte {
    val carres = new Range(0, 20000, 1)
    var total = 0
    var ratio = 0.0
    val limit = 10.0
    val sqtop = ((carres.last * 2) + 5).toInt + 1

    carres.foreach((carre: Int) => {
        val deuxNPlus1 = (2 * carre) + 1
        val deuxNPlus3 = deuxNPlus1 + 2
        var start = deuxNPlus1 * deuxNPlus1
        var end = deuxNPlus3 * deuxNPlus3

        val QuatreN2 = 4 * carre * carre
        val trois = QuatreN2 + (6 * carre) + 3
        val cinq = QuatreN2 + (8 * carre) + 5
        val sept = QuatreN2 + (10 * carre) + 7
        val newStuff2add = (TreeSet[BigInt]() + trois + cinq + sept).filter(EulerPrime.isPrime(_))
        total += newStuff2add.size
        ratio = 100.0 * total / ((4.0 * (carre + 1.0)) + 1.0)

        if (math.abs(ratio - limit) < 0.1) {
            println("\n******** " + carre + " " + trois + " " + total + " " + ratio + " " + ((2 * carre) + 3))
            if (ratio < limit) {
                sys.exit
            }
        } else if (((2 * carre) + 3) % 40 == 7) {
            //				println((TreeSet[BigInt]()+trois+cinq+sept)+" "+newStuff2add)
            println(carre + " " + trois + " " + total + " " + ratio + " " + ((2 * carre) + 3))
        } else if (carre < 10) {
            //				println((TreeSet[BigInt]()+trois+cinq+sept)+" "+newStuff2add)
            println(carre + " " + trois + " " + total + " " + ratio + " " + ((2 * carre) + 3))
        }

    })

}

class Euler58quad extends Euler58ter {

    override def computePrime(callback: (Int, TreeSet[BigInt]) => Int) = {
        carres.foreach((carre: Int) => {
            val deuxNPlus1 = (2 * carre) + 1
            val deuxNPlus3 = (2 * carre) + 3
            var start = deuxNPlus1 * deuxNPlus1
            var end = deuxNPlus3 * deuxNPlus3
            var range = TreeSet[BigInt]() ++ (new Range(start, end, 2)).toList.map(BigInt(_))
            premiersForCompute.foreach((premier: BigInt) => range = range.filter(_ % premier != 0))
            require(!premiersForCompute.contains(194923))
            if (!range.isEmpty) {
                if (range.head < sqtop) {
                    premiersForCompute = premiersForCompute ++ range
                }
            }

            callback(carre, range)
            if (!range.isEmpty) {
                print("." + range.head + "-" + range.last)
            }
            if (((2 * carre) + 3) % 10 == 7) {
                println("\n" + carre + " " + total + " " + (100.0 * total / ((4.0 * (carre + 1.0)) + 1.0)) + " last: " + primesDiags.last + " " + ((2 * carre) + 3))
                println("  premiersForCompute.last: " + premiersForCompute.last + " " + premiersForCompute.contains(194923) + " " + range.contains(194923))
                require(total == primesDiags.size, total + "==" + primesDiags.size)
            }
            if (carre < 10) {
                println("\n" + carre + " " + total + " " + (100.0 * total / ((4.0 * (carre + 1.0)) + 1.0)) + " last: " + primesDiags.last + " " + ((2 * carre) + 3))
                println(primesDiags)
                require(total == primesDiags.size, total + "==" + primesDiags.size)
            }
        })
    }

    override def callback(index: Int, range: TreeSet[BigInt]): Int = {
        val QuatreN2 = 4 * index * index
        val trois = QuatreN2 + (6 * index) + 3
        val cinq = QuatreN2 + (8 * index) + 5
        val sept = QuatreN2 + (10 * index) + 7
        val newStuff2add = (TreeSet[BigInt]() + trois + cinq + sept).intersect(range)
        total += newStuff2add.size
        primesDiags = primesDiags ++ newStuff2add

        if (!range.isEmpty) {
            if (range.last == 196247) {
                println("  trois: " + trois)
                println("  range: " + range)
                println("  newStuff2add: " + newStuff2add)

            }
        }

        0
    }

}

class Euler58ter extends EulerPrime(100000, 1000) {
    val carres = new Range(1, 500, 1)
    var primesDiags = TreeSet[BigInt](3, 5, 7, 13, 17, 31, 37)
    var total = primesDiags.size
    var ratio = 0.0
    val limit = 10.0
    override val sqtop = ((carres.last * 2) + 5).toInt + 1

    override def computePrime(callback: (Int, TreeSet[BigInt]) => Int) = {
        carres.foreach((carre: Int) => {
            val deuxNPlus1 = (2 * carre) + 1
            val deuxNPlus3 = deuxNPlus1 + 2
            var start = deuxNPlus1 * deuxNPlus1
            var end = deuxNPlus3 * deuxNPlus3
            var range = TreeSet[BigInt]() ++ (new Range(start, end, 2)).toList.map(BigInt(_))
            premiersForCompute.foreach((premier: BigInt) => range = range.filter(_ % premier != 0))
            if (!range.isEmpty) {
                if (range.head < sqtop) {
                    premiersForCompute = premiersForCompute ++ range
                }
                print("." + range.head + "-" + range.last)
            }

            callback(carre, range)
            ratio = 100.0 * total / ((4.0 * (carre + 1.0)) + 1.0)

            if (math.abs(ratio - limit) < 0.1) {
                println("\n******** " + carre + " " + total + " " + ratio + " " + ((2 * carre) + 3))
                if (ratio < limit) {
                    sys.exit
                }
            } else if (((2 * carre) + 3) % 20 == 7) {
                println("\n" + carre + " " + total + " " + ratio + " " + ((2 * carre) + 3))
            } else if (carre < 10) {
                println("\n" + carre + " " + total + " " + ratio + " " + ((2 * carre) + 3))
            }

        })
    }

    def callback(index: Int, range: TreeSet[BigInt]): Int = {
        val QuatreN2 = 4 * index * index
        val trois = QuatreN2 + (6 * index) + 3
        val cinq = QuatreN2 + (8 * index) + 5
        val sept = QuatreN2 + (10 * index) + 7
        val newStuff2add = (TreeSet[BigInt]() + trois + cinq + sept).intersect(range)
        total += newStuff2add.size
        0
    }

    def getSolution = {
        computePrime(callback)
    }

}

class Euler58bis extends Euler58 {
    var primesDiags = TreeSet[BigInt]()

    override def ratio(num: BigInt): Double = {
        val diags = diag(3, 2, num) ++ diag(5, 4, num) ++ diag(7, 6, num)
        val primes = diags.intersect(premiers)
        require(diags.last < premiers.last, getClass.getName + "(" + num + ") " + diags.last + "<" + premiers.last)
        println("                 " + diags.last)
        primesDiags = primesDiags ++ primes
        100.0 * primes.size.toDouble / total(num).toDouble
    }

}

class Euler58 extends CheckEulerPrime(2000000, 1000) {
    //	val premiers = EulerPrime.premiers1000000

    def diag(start: BigInt, inc: Int, num: BigInt): TreeSet[BigInt] = {
        var ts = TreeSet[BigInt](start)
        var cpt = 1
        val offset = inc + 8
        while (cpt < num) {
            ts = ts + (ts.last + (cpt * 8) + inc)
            cpt += 1
        }
        ts
    }

    def next(start: (BigInt, BigInt)): BigInt = start._2 + start._2 + 8 - start._1

    def ratio(num: BigInt): Double = {
        val diags = diag(3, 2, num) ++ diag(5, 4, num) ++ diag(7, 6, num)
        val primes = diags.intersect(premiers)
        require(diags.last < premiers.last, getClass.getName + "(" + num + ") " + diags.last + "<" + premiers.last)
        println("                 " + diags.last)
        100.0 * primes.size.toDouble / total(num).toDouble
    }

    def total(num: BigInt): BigInt = {
        (num * 4) + 1
    }

    def sideLength(num: BigInt): BigInt = {
        (2 * num) + 1
    }

    def solve(start: BigInt, inc: BigInt, bi: BigInt) = {
        QuadraticRoots.solve(4, (4 + inc).toDouble, (start - bi).toDouble)
    }

    def isOnDiag(start: BigInt, inc: BigInt, bi: BigInt): Int = {
        val a = 4.0
        val b = (4 + inc).toDouble
        val c = (start - bi).toDouble
        // solve ax2 + bx + c = 0
        assume(b > 0.0)
        assume(c <= 0.0)

        val d = b * b - 4.0 * a * c
        val aa = a + a

        if (d < 0.0) { // complex roots -> not interested
            0
        } else { // real roots -> only the positive one
            val re = (-b - math.sqrt(d)) / aa
            val solution = c / (a * re)
            require(solution > 0.0)
            if (solution - solution.toInt == 0.0) {
                solution.toInt
            } else {
                0
            }
        }
    }

}
