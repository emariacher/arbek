package euler

import org.scalatest.FunSuite
import org.scalatest._
import org.scalatest.BeforeAndAfter
import scala.collection.immutable.ListSet
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import scala.collection.immutable.TreeSet
import EulerPrime._
import language.postfixOps
import kebra.MyLog._

// scala org.scalatest.tools.Runner -p . -o -s euler.EulerPrimeRegression

// parameters.getValue("45zob").toInt should equal (1)
// projects.map(_.getDatabase.getName).contains("COCO") should be (false)

class EulerPrimeRegression extends FunSuite with Matchers with BeforeAndAfter {

    before {
        Euler
        println("\n****avant****")
    }

    after {
        Euler.timeStamp(Euler.t_start, "Euler")
        println("****apres****")
    }

    test("EulerPrime") {
        val eu = new CheckEulerPrime(105000, 1000)
        eu.check should be(true)
    }

    test("Euler3") {
        new EulerDiv(BigInt.apply("600851475143")).primes.max should equal(6857)
    }

    test("euler5") {
        // http://projecteuler.net/problem=5
        val solution = List(2, 2, 2, 2, 3, 3, 5, 7, 11, 13, 17, 19).product
        println(new EulerDivisors((new EulerDiv(solution)).primes).divisors)
        println("euler5 solution: " + solution)
    }

    test("euler7") {
        val premiers = (new CheckEulerPrime(105000, 1000)).premiers
        premiers.toList.apply(6 - 1) should equal(13)
        val solution = premiers.toList.apply(10001 - 1)
        println("\nEuler7 solution: " + solution)
        solution should equal(104743)
    }

    test("euler10") {
        // http://projecteuler.net/problem=10
        // new CheckEulerPrime(100).check
        val sum = (new Euler10).getSum
        sum should equal(BigInt.apply("142913828922"))
    }

    test("euler41") {
        val y = new Range(1, 10, 1).toList.filter((limit: Int) => new Range(1, limit + 1, 1).sum % 3 != 0).reverse.flatMap((limit: Int) => {
            new Range(1, limit + 1, 1).toList.permutations.toList.filter((l: List[Int]) =>
                List(1, 3, 7, 9).contains(l.last)).filter((l: List[Int]) =>
                isPrime(BigInt.apply(l.mkString(""))))
        }).map((l: List[Int]) => BigInt.apply(l.mkString(""))).max
        println("Euler41 solution[" + y + "]: ")
        y should equal(7652413)
    }

    test("euler50") {
        // http://projecteuler.net/problem=50
        val cep = new Euler50
        //		  println(cep.premiers.grouped(10).mkString("\n"))
        cep.euler50GetConsecPrime(100)._2 should equal(41)
        val mille = cep.euler50GetConsecPrime(1000)
        mille._1.size should equal(21)
        mille._2 should equal(953)
        cep.euler50GetConsecPrime(1000000)
        cep.euler50GetConsecPrime(100000)
        cep.euler50GetConsecPrime(10000)
        cep.euler50GetConsecPrime(1000)
    }

    ignore("euler58") {
        // http://projecteuler.net/problem=58
        /*		val eu = new Euler58
				val level = 2
				val ne = eu.diag(3,2,level)
				val no = eu.diag(5,4,level)
				val so = eu.diag(7,6,level)
				println(ne)
				println(no)
				println(so)
				println(eu.total(level))
				println(eu.sideLength(level))
				eu.sideLength(3) should equal (7)
		require(math.abs(eu.ratio(3)-62)<0.5)

		eu.next((3,13)) should equal (31)
		eu.next((5,17)) should equal (37)
		eu.next((21,43)) should equal (73)

		eu.next((65,101)) should equal (145)
		eu.solve(5,4,145)
		eu.isOnDiag(5,4,145) should equal (5)
		eu.isOnDiag(5,4,146) should equal (0)*/

        val range = new Range(3, 720, 40).toList
        val t_start = Calendar.getInstance()
        /*				val eu = new Euler58
				var solution = 0.0
				range.takeWhile((level: Int) => {
					val ratio = eu.ratio(BigInt(level))
							println(level+ " " +ratio+" "+eu.sideLength(level))
							solution = ratio
							ratio > 10.0
				})*/
        val t_bis = Euler.timeStamp(t_start, "Euler58")
        /*				val eubis = new Euler58bis
				solution = 0.0
				range.takeWhile((level: Int) => {
					val ratio = eubis.ratio(BigInt(level))
							println(level+ " " +ratio+" last: "+eubis.primesDiags.last+" "+eubis.sideLength(level))
							solution = ratio
							ratio > 10.0
				})*/
        val t_ter = Euler.timeStamp(t_bis, "Euler58Bis")
        val euter = new Euler58ter
        euter.getSolution
        val t_quad = Euler.timeStamp(t_ter, "Euler58Ter")
        //		val euquad = new Euler58ter
        //		euquad.getSolution
        val t_ = Euler.timeStamp(t_quad, "Euler58Quad")
        new Euler58quinte
        val t_quinte = Euler.timeStamp(t_, "Euler58Quinte")
        //		require(euter.total==euquad.total)
    }

    test("euler87") {
        Math.pow(3, 4) should equal(81)
        Math.pow(4, 0.5) should equal(2)
        Math.pow(27, 1.0 / 3.0) should equal(3)
        //x2+y3+z4
        val x = Math.sqrt(50000000 - 24).toInt
        println(x + " " + isPrime(x))
        val prime2exploreX = premiers100000.takeWhile(_ < x)
        println("prime2exploreX: " + prime2exploreX.size + " " + prime2exploreX)
        val y = Math.pow(50000000 - 20, 1.0 / 3.0).toInt
        println(y + " " + isPrime(y))
        val prime2exploreY = premiers100000.takeWhile(_ < y)
        println("prime2exploreY: " + prime2exploreY.size + " " + prime2exploreY)
        val z = Math.sqrt(Math.sqrt(50000000 - 12)).toInt
        println(z + " " + isPrime(z))
        val prime2exploreZ = premiers100000.takeWhile(_ < z)
        println("prime2exploreZ: " + prime2exploreZ.size + " " + prime2exploreZ)
        val solution = prime2exploreX.flatMap((x: BigInt) => {
            //println(" "+x)
            prime2exploreY.flatMap((y: BigInt) => {
                prime2exploreZ.map((z: BigInt) => {
                    (x * x) + (y * y * y) + (z * z * z * z)
                })
            })
        }).toList.sorted.takeWhile(_ < 50000000)
        println("\neuler87 solution: " + solution.size + " " + solution.take(5) + " " + solution.last)
    }

    test("EulerDiv3") {
            def testEulerDiv(bi: BigInt) {
                val primes = new EulerDiv(bi).primes.filter(_ != 1)
                println("new EulerDiv(" + bi + ").primes: " + primes + ", isPrime: " + isPrime(bi))
                if (isPrime(bi)) {
                    primes should equal(List(bi))
                }
            }

        new Range(1, 10, 1).foreach((i: Int) => testEulerDiv(i))

        val n = 120
        val eu = new EulerDiv(n)
        print(n + " " + eu.primes)
        eu.primes should equal(List(2, 2, 2, 3, 5))

        val divisors = new EulerDivisors(eu.primes)
        print(n + " " + divisors.divisors)
        print(n + " " + divisors.primesUnique)
        divisors.primesUnique.toString should equal(TreeSet(2, 2, 2, 3, 5).toString)

        val eudiv = new EulerDiv(194923)
        eudiv.primes.toString should equal(List(421, 463).toString)
        print("197137: " + new EulerDiv(197137).primes)
        new EulerDiv(197137).primes.toString should equal(List(197137).toString)
        isPrime(197137) should be(true)
        new EulerDiv(197136).primes.isEmpty should be(false)
        isPrime(197136) should be(false)
        new EulerDiv(1106999).primes.toString should equal(List(1106999).toString)
        isPrime(1106999) should be(true)

        val l2 = List[BigInt](194923, 9, 24, 49, 121)
        val eudiv2 = new EulerDiv(l2.product)
        println("\n" + l2 + " " + eudiv2.primes)
        eudiv2.primes.product should equal(l2.product)

        val l3 = List[BigInt](194923, 9, 24, 49, 1106999, 121)
        val eudiv3 = new EulerDiv(l3.product)
        println("\n" + l3 + " " + eudiv3.primes)
        eudiv3.primes.product should equal(l3.product)
    }

    ignore("euler108bis") {
        // http://projecteuler.net/problem=108
        EulerPrime
        val l = List(2, 2, 3, 5, 7, 11)
        val t_start = Calendar.getInstance()
        println((new Euler108(l.product)).toString)
        var t_end = Euler.timeStamp(t_start, "Euler108")
    }

    test("euler108") {
        // http://projecteuler.net/problem=108
        EulerPrime

        new Euler108Func(2, 5, 0, List[BigInt]())
        new Euler108Func(2, 8, 0, List(3))
        new Euler108Func(2, 11, 0, List(2, 2))
        new Euler108Func(2, 5, 1, List(2, 2, 2))
        new Euler108Func(2, 13, 0, List(2, 3))
        new Euler108Func(2, 18, 0, List(2, 2, 3))
        new Euler108Func(2, 25, 0, List(2, 3, 2, 3))

        new Euler108Func(3, 63, 0, List(2, 3, 5))
        new Euler108Func(3, 88, 0, List(2, 2, 3, 5))
        new Euler108Func(3, 13, 2, List(2, 2, 2, 3, 5))
        new Euler108Func(3, 123, 0, List(2, 3, 2, 3, 5))

        new Euler108Func(4, 313, 0, List(2, 3, 5, 7))
        new Euler108Func(5, (5 * 313) - 2, 0, List(2, 3, 5, 7, 11))

    }

    def euler108BottomUp = {
        val range = new Range(0, 10000, 30).toList
        println("range: " + range)

        var max = new Euler108(1)

        val answer = range.tail.dropWhile((n: Int) => {
            if (n % 300 == 0) {
                println(n)
            }
            val eu108 = new Euler108(n)
            if (max.results.size <= eu108.results.size) {
                max = eu108
                println("*** max: " + max.results.size + max)
            } else {
                //      require(max.ndivs.primesUnique.size>=eu108.ndivs.primesUnique.size,eu108)
                //  println("    max: "+max.results.size+eu108)
            }
            eu108.results.size < 1000
        })
        println("*euler108*\n" + new Euler108(answer.head))
    }

    test("euler110") {
        // http://projecteuler.net/problem=110
        EulerPrime
        Euler110

        new Euler108Func3(2, 5, 0, List[BigInt]())
        new Euler108Func3(2, 8, 0, List(3))
        new Euler108Func(2, 11, 0, List(2, 2))
        new Euler108Func(2, 5, 1, List(2, 2, 2))
        new Euler108Func(2, 13, 0, List(2, 3))
        new Euler108Func(2, 18, 0, List(2, 2, 3))
        new Euler108Func(2, 25, 0, List(2, 3, 2, 3))

        println("****3****")
        new Euler108Func(3, 63, 0, List(2, 3, 5))
        new Euler108Func(3, 88, 0, List(2, 2, 3, 5))
        new Euler108Func(3, 13, 2, List(2, 2, 2, 3, 5))
        new Euler108Func(3, 122, 0, List(2, 2, 2, 3, 3, 3))
        new Euler108Func(3, 123, 0, List(2, 2, 3, 3, 5))
        new Euler108Func(3, 158, 0, List(2, 2, 2, 3, 3, 5))
        new Euler108Func3(3, 172, 0, List(2, 2, 3, 3, 5, 5))
        new Euler108Func(3, 203, 0, List(2, 2, 2, 3, 3, 3, 5))

        println("****4****")
        new Euler108Func3(4, Euler110.cinqNMoinsDeux(4, 3, 63), 0, List(2, 3, 5, 7))
        new Euler108Func3(4, Euler110.cinqNMoinsDeux(4, 3, 88), 0, List(2, 2, 3, 5, 7))
        new Euler108Func3(4, Euler110.cinqNMoinsDeux(4, 3, 123), 0, List(2, 2, 3, 3, 5, 7))
        new Euler108Func3(4, Euler110.cinqNMoinsDeux(4, 3, 172), 0, List(2, 2, 3, 3, 5, 5, 7))

        println("****5****")
        new Euler108Func3(5, Euler110.cinqNMoinsDeux(5, 2, 13), 0, List(2, 3, 5, 7, 11))

        val bi = BigInt(1201)
        val troisR = Euler110.troisNMoinsUnL.findValidReverse(new Item(bi))
        val cinqR = Euler110.cinqNMoinsDeuxL.findValidReverse(new Item(bi))
        val septR = Euler110.septNMoinsTroisL.findValidReverse(new Item(bi))

        println("****ZeSolution!****")
        val fisrtPrimes = premiers10000.take(12).toList
        val adder = List(2, 2, 3, 3, 5, 7).map(BigInt(_))
        val premsprod = (fisrtPrimes ++ adder).product
        val eu108 = new Euler108Length3(premsprod)
        println("solution=" + premsprod + " " + eu108.length)
        premsprod should equal(BigInt.apply("9350130049860600"))

        val seed = Euler110.tetesDeSeries
        val firstPass = Euler110.generateSeries(seed)
        val secondPass = Euler110.generateSeries(firstPass)
        val thirdPass = Euler110.generateSeries(secondPass)
        val fourthPass = Euler110.generateSeries(thirdPass)
        println(fourthPass filterNot (Euler110.solutions.toList contains))
    }

    test("euler123") {
        val eu = new Euler123(240000, 1000)
        Euler.powl(2, 4) should equal(16)
        Euler.powl(2, 5) should equal(32)
        Euler.powl(2, 7) should equal(128)
        Euler.powl(2, 10) should equal(1024)
        eu.solution should equal(21035)
    }

    ignore("euler131") {
        // http://projecteuler.net/problem=131
        val eu = new Euler131
        eu.getSolution
        /*				val l = eu.l

				val derivees = l.sliding(2).map((z: List[Euler131PN]) => z.last.getDerivee(z.head)).toArray
				val dataDerivees1 = l.map(_.p.toDouble).zip(derivees)
				val dataDerivees = dataDerivees1.map((z: (Double,Double)) => (z._1,(z._1*z._2)+4))
				println(dataDerivees)
				val linearRegression = new LinearRegression(dataDerivees)
		println(linearRegression.s)*/
        Euler.timeStamp(Euler.t_start, "Euler")

        /*		figure(1)
		title("euler71")
		linePlotsOn
		plot(l.map(_.p.toDouble).toArray, l.map(_.n.toDouble).toArray, Color.RED,"n vs p")
		//		plot(l.map(_.p.toDouble).toArray, l.map((e: Euler131PN) => math.log(e.n.toDouble)*100).toArray, Color.GREEN,"log(n) vs p")

		plot(dataDerivees.map(_._1).toArray, dataDerivees.map(_._2).toArray, Color.BLUE,"n=ceil(p) calculee")

		val dataAppliquees = l.map((e: Euler131PN) => (e.p,Euler131.betaCeilFuncMaxN.func(e.p.toDouble)*e.p.toDouble))
		println(Euler131.betaCeilFuncMaxN.toString)
		println(dataAppliquees.zip(l.map((e: Euler131PN) => (e.n,Euler131.betaCeilFuncMaxN.func(e.p.toDouble)))))
		plot(dataAppliquees.map(_._1.toDouble).toArray, dataAppliquees.map(_._2).toArray, Color.GREEN,"n=ceil(p) appliquee")

		val dataAppliquees2 = l.map((e: Euler131PN) => (e.p,Euler131.betaCeilFuncMaxN2.func(e.p.toDouble)*e.p.toDouble))
		println(Euler131.betaCeilFuncMaxN2.toString)
		println(dataAppliquees2.zip(l.map((e: Euler131PN) => (e.n,Euler131.betaCeilFuncMaxN2.func(e.p.toDouble)))))
		plot(dataAppliquees2.map(_._1.toDouble).toArray, dataAppliquees2.map(_._2).toArray, Color.MAGENTA,"n=ceil(p) appliquee2")

		//	val bounds = (0.0,Euler131.limit.toDouble/3.0,0.0,Euler131.limit.toDouble)
		//		linearRegression.func.plotFunc (bounds, 10.0, "dn/dp ax+b", 0.4.toFloat)
		//		Euler131.betaCeilFuncMaxN.plotFunc (bounds, 10.0, Euler131.betaCeilFuncMaxN.toString, 0.6.toFloat)
		//		Euler131.betaBottomFuncMinN.plotFunc (bounds, 10.0, Euler131.betaBottomFuncMinN.toString, 0.8.toFloat)

		xlabel("x - p")
		ylabel("y - n")
		waiting(1000000)*/
    }

    test("EulerDiv2") {
        val n = 120
        val eu = new EulerDiv(n)
        print(n + " " + eu.primes)
        eu.primes should equal(List(2, 2, 2, 3, 5))

        val divisors = new EulerDivisors(eu.primes)
        print(n + " " + divisors.divisors)
        print(n + " " + divisors.primesUnique)
        divisors.primesUnique.toString should equal(TreeSet(2, 2, 2, 3, 5).toString)

        val thrown = intercept[IllegalArgumentException] {
            new EulerDiv(BigInt.apply(List[Int]().padTo(26, 1).mkString("")))
        }
        assert(thrown.getMessage === "requirement failed: 280846283204599997<99991*99991(9998200081)")
    }

    test("euler234") {
        val eu = new Euler234
        /*val max = BigInt.apply("999966663333")
				println(max+" "+Math.sqrt(max.toDouble))
				val solution15 = eu.petitNombre(15)
				solution15 should equal ((3,30))
		eu.check(1394) should be (true)
		val solution1000 = eu.petitNombre(1000)
		solution1000 should equal ((92,34825))
		eu.getSolution(1000) should equal (34825)
		println("\n"+eu.solution2.filter(_>129))
		eu.petitNombre(10000)._2 should equal (eu.getSolution(10000))*/
        val solution = eu.getSolution(BigInt.apply("999966663333"))
        solution should equal(BigInt.apply("1259187438574927161"))
    }

    test("test de fin") {
        println("That's all folks!")
    }

}