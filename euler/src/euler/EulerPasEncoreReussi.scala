package euler

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfter
import scala.collection.immutable.ListSet
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import scala.collection.immutable.TreeSet
import Math._
import language.postfixOps

class EulerPasEncoreReussi extends EulerOld {

	before {
		println("****avant****")
	}

	after {
		println("****apres****")
		Euler.timeStamp(Euler.t_start, "Euler")
	}

	test("euler60") {
		// http://projecteuler.net/problem=60
		val eu = new Euler60
				eu.euler60bis(8219, 2297) should be (false)
		eu.euler60bis(4159, 2377) should be (true)
		eu.euler60bis(4159, 67) should be (false)
		eu.euler60bis(11, 17) should be (false)
		println(new EulerDivisors((new EulerDiv(1117)).primes).divisors)
		println(new EulerDivisors((new EulerDiv(1711)).primes).divisors)
		eu.chklistbis(ListSet[BigInt](5281, 27823, 3, 7, 823))

		println("************************************")
		eu.doZeJob5

	}




	test("euler76") {
		// http://projecteuler.net/problem=76
		val eu = new Euler76(15)
	}


	test("euler91") {
		// http://projecteuler.net/problem=91
		new GeomTriangle(ListSet(new XY(0,0), new XY(1,0), new XY(0,1))).hasRightAngle should be (true)
		new GeomTriangle(ListSet(new XY(0,0), new XY(1,2), new XY(0,1))).hasRightAngle should be (false)
		new GeomTriangle(ListSet(new XY(0,0), new XY(1,1), new XY(0,2))).hasRightAngle should be (true)
		new GeomTriangle(ListSet(new XY(0,0), new XY(0,10), new XY(3,9))).hasRightAngle should be (true)

		new GeomTriangle(ListSet(new XY(0,0), new XY(0,10), new XY(3,9))).is00RightAngle should be (false)
		new GeomTriangle(ListSet(new XY(0,0), new XY(1,0), new XY(0,1))).is00RightAngle should be (true)

		new Euler91(1)
		new Euler91(2)
		new Euler91(3)
		new Euler91(4)
		new Euler91(5)
		new Euler91(6)
		new Euler91(7)
		new Euler91(8)
		new Euler91(9)
		new Euler91(10)
		new Euler91(11)
	}
	
		test("euler112") {
		val b0 = new Bouncy112(0)

		def getBouncy(bi: BigInt): List[Bouncy112] = (BigInt(0) to bi).toStream.map((bi2: BigInt) => new Bouncy112("%03d".format(bi2))).toList
		def getBouncy2(bi: BigInt): List[Bouncy112] = (BigInt(1) to bi).toStream.map((bi2: BigInt) => new Bouncy112(bi2)).toList.sortBy{_.bi}
		def getBouncy3(low: BigInt, high: BigInt): List[Bouncy112] = (low to (high-1)).toStream.map((bi2: BigInt) => new Bouncy112(bi2)).toList.sortBy{_.bi}

		var zeSum = BigInt(0)
				var lSums = List[(BigInt,BigInt)]((0,0))
				val z1to9 = getBouncy2(9)

				def findParent2(bi: BigInt, lb: List[Bouncy112]): Bouncy112 = {
			var b = b0
					var s = bi.toString
					while(b==b0) {
						lb.find(_.s == s) match {
						case Some(b1) => b = b1
						case _ => s = (BigInt.apply(s)/10).toString
						}
					}
			b
		}

		def findParent(bi: BigInt, lb: List[Bouncy112]): Bouncy112 = {
			var b = b0
					var s = bi.toString.substring(0,2)
					while(b==b0) {
						lb.find(_.s == s) match {
						case Some(b1) => b = b1
						case _ => s = (BigInt.apply(s)/10).toString
						}
					}
			b
		}

		def findNextAfterParent(b: Bouncy112, lb: List[Bouncy112]): Bouncy112 = {
			lb.drop(lb.indexOf(b)+1).find((b2: Bouncy112) => (b2.s.size==b.s.size)) match {
			case Some(b1) => b1
			case _ => b0
			}
		}


		def doZeJob(low: BigInt): List[Bouncy112] = {
			val y2 = z1to9.flatMap(_.getNexts)
					zeSum += y2.map(_.cptb).sum
					println("Euler122 solution: "+zeSum+" --- "+y2.size+" -+- "+y2.last.toString2+
							" %5.4f".format(zeSum.toDouble/y2.last.bi.toDouble))
			lSums = lSums :+ (y2.last.bi,zeSum)
			y2	  
		}

		def doZeJob4(percent: Double, low: Int, high: Int, inc: Int): List[(BigInt,Double)] = {
			var linc = inc
					var z = doZeJob3(percent,low,high,linc,false).filter((c: (BigInt,Double)) => c._2>percent)
					while(linc>1) {
						println(" z[%5.3f".format(percent)+"]["+linc+"]: "+z.size+" "+z.mkString("\n  ","\n  ","\n"))
						linc = linc/10
						z = z.flatMap(c => doZeJob3(percent,c._1.toInt-(9*linc),c._1.toInt+2,linc,true)).filter(_._2>percent)
					}
			println(" z[%5.3f".format(percent)+"]["+linc+"]: "+z.size+" "+z.mkString("\n  ","\n  ","\n"))
			z
		}

		def doZeJob3(percent: Double, low: Int, high: Int, inc: Int, stop: Boolean): List[(BigInt,Double)] = {
			println(" [%5.3f".format(percent)+"] from ["+low+"] to ["+high+"] by inc of ["+inc+"]")
			val r = new Range(low, high, inc).toList
			if(stop) {
				var lastOne = (BigInt(0),0.0)
						r.toStream.map(i => {lastOne = doZeJob2(i); lastOne}).takeWhile(_._2<=percent).toList :+ lastOne
			} else {
				r.map(doZeJob2(_))		
			}
		}

		def doZeJob2(low: BigInt): (BigInt,Double) = {
			val slow = low.toString
					val lastMilestone = lSums.takeWhile(_._1<low).last
					val w = z1to9.flatMap(_.getNexts).sortBy{_.s}
			val parent = findParent(low,w)
					var wlow = w.takeWhile(_.s.compareTo(slow)<0)			
					val localSumLow = lastMilestone._2 + wlow.map(_.cptb).sum					
					val nextone = findNextAfterParent(parent, w)
					println("  Parent of ["+low+"]: "+parent.toString2+" next one: "+nextone.toString2)
					println(wlow.map(_.toString2).mkString("wlow:\n  ","\n  ","\n"))
					val sizeOfLow = slow.size
					val low2 = {
				var result = parent.bi
						while(result.toString.size<sizeOfLow) {
							result = result*10
						}
				result
			}
			val high2 = {
				var result = nextone.bi
						while(result.toString.size<sizeOfLow) {
							result = result*10
						}
				result
			}
			val expanded = getBouncy3(low2, high2).filter(_.bi<=low)
					println(expanded.map(_.toString2).mkString("expanded:\n  ","\n  ","\n"))
					val localSum = localSumLow - parent.getCptb + expanded.count(_.b)
					println("  Euler122 solution["+low+"]: "+localSum+" %5.20f".format(localSum.toDouble/low.toDouble))
					localSum.toDouble<low.toDouble should be (true)
			(low,localSum.toDouble/low.toDouble)
		}

		getBouncy2(999).filter(_.b).size should equal (525)
		getBouncy2(538).filter(_.b).size should equal (269)
		getBouncy2(2100).filter(_.b).size should equal (1456) 
		getBouncy2(2101).filter(_.b).size should equal (1457) 
		getBouncy2(21780).filter(_.b).size should equal (19602) //0.9
		getBouncy2(9999).filter(_.b).size should equal (8325)
		//getBouncy2(99999).filter(_.b).size should equal (95046)



		var y = z1to9
		println("Euler122 solution: "+y.map(_.cptb).sum+" --- "+y.size+" -+- "+y.last.toString2)
		y.foreach(_.updateNexts)
		y =  z1to9.flatMap(_.getNexts)
		println("Euler122 solution: "+y.map(_.cptb).sum+" --- "+y.size+" -+- "+y.last.toString2)
		y.foreach(_.updateNexts)
		doZeJob2(538)._2 should equal (0.5)
		y = doZeJob(0)
		zeSum should equal(525)
		y.foreach(_.updateNexts)
		doZeJob2(2101)._2 should equal (1457.0/2101.0)
		doZeJob2(2100)._2 should equal (1456.0/2100.0)
		y = doZeJob(0)
		zeSum should equal(8325)
		y.foreach(_.updateNexts)
		doZeJob2(21780)._2 should equal (0.9)
		doZeJob2(21001)._2 should equal (18826.0/21001.0)
		doZeJob2(21000)._2 should equal (18825.0/21000.0)
		y = doZeJob(0)
		println(lSums)
		doZeJob2(21780)._2 should equal (0.9)
		doZeJob4(0.90,10999,25002,1000)
		zeSum should equal (95043)
		zeSum should equal (95046)
		y.foreach(_.updateNexts)
		y = doZeJob(0)
		y.foreach(_.updateNexts)
		doZeJob3(0.99,1665999,1666001,1,false)
		doZeJob3(0.99,1589899,1589901,1,false)
		doZeJob4(0.99,1489999,2000001,10000)
		y = doZeJob(0)
		zeSum should equal (9969183)
		println(lSums)

		//  Euler122 solution[1693000]: 1676070 0.99000000000000000000 NOGOOD! :(
	}



	test("euler132") {
		val premiers = (new CheckEulerPrime(460000,1000)).premiers
				var solutions = ListSet[BigInt]()

				def doZeJob(i: Int): List[BigInt] = {
			val repunit = BigInt.apply(List[Int]().padTo(i,1).mkString(""))
					val primes = new EulerDiv2(repunit,premiers).primes
					//primes.product should equal (repunit)
					(ListSet[BigInt]() ++ primes).size should equal(primes.size)
			println("\n"+i+" "+primes+(primes.product==repunit))
			if(!solutions.isEmpty) {
				if(!primes.filter(!solutions.contains(_)).isEmpty) {
					println("   new! "+primes.filter(!solutions.contains(_))+
							" Euler132 solution: "+solutions.size+" "+solutions.toList.sorted.take(45).size+" "+solutions.toList.sorted.take(45).sum+
							" "+solutions.toList.sorted.take(45).max+" "+solutions.max+" "+solutions.toList.sorted)
							println("primes: "+new EulerDiv2(i,premiers).primes)
				}
			}
			solutions = solutions ++ primes
					primes
		}

		val primes9 = new EulerDiv2(9,premiers).primes
				(ListSet[BigInt]() ++ primes9) should not equal(primes9.size)

		doZeJob(10) should equal (List(11, 41, 271, 9091))

		solutions = ListSet[BigInt]()
		val e2e5d = new EulerDivisors3(new EulerDiv2(BigInt(131072),premiers).primes++new EulerDiv2(BigInt(390625),premiers).primes).divisors.filter(_>60000).filter(_<600000)

		e2e5d.reverse.map((bi: BigInt) => doZeJob(bi.toInt))
		println(" Euler132 solution: "+solutions.size+" "+solutions.toList.sorted.take(45).size+" "+solutions.toList.sorted.take(45).sum+" "+solutions.toList.sorted.take(45).max+" "+solutions.max+" "+solutions.toList.sorted)
		solutions.toList.sorted.take(45).sum<1995504 should be (true)
		// Euler132 solution: 53 188504240117 List(11, 17, 41, 73, 101, 137, 251, 257, 271, 353, 401, 449, 641, 751, 1201, 1409, 1601, 3541, 4001, 4801, 5051, 9091, 10753, 15361, 16001, 19841, 21001, 21401, 24001, 25601, 27961, 40961, 43201, 60101, 69857, 76001, 76801, 160001, 162251, 453377, 524801, 544001, 670001, 952001, 976193, 980801, 1378001, 1610501, 1634881, 1676321, 5882353, 5964848081, 182521213001)
		// Euler132 solution: 53 5025919 976193 182521213001 List(11, 17, 41, 73, 101, 137, 251, 257, 271, 353, 401, 449, 641, 751, 1201, 1409, 1601, 3541, 4001, 4801, 5051, 9091, 10753, 15361, 16001, 19841, 21001, 21401, 24001, 25601, 27961, 40961, 43201, 60101, 69857, 76001, 76801, 160001, 162251, 453377, 524801, 544001, 670001, 952001, 976193, 980801, 1378001, 1610501, 1634881, 1676321, 5882353, 5964848081, 182521213001)
		// Euler132 solution: 52 45 2454767 524801 182521213001 List(11, 17, 41, 73, 101, 137, 251, 257, 271, 353, 401, 449, 641, 751, 1201, 1409, 1601, 3541, 4001, 4801, 5051, 9091, 10753, 15361, 16001, 19841, 21001, 21401, 24001, 25601, 27961, 37501, 40961, 43201, 60101, 62501, 69857, 76001, 76801, 160001, 162251, 163841, 307201, 453377, 524801, 544001, 670001, 952001, 976193, 980801, 5882353, 182521213001)
		// Euler132 solution: 48 45 1995503 453377 182521213001 List(11, 17, 41, 73, 101, 137, 251, 257, 271, 353, 401, 449, 641, 751, 1201, 1409, 1601, 3541, 4001, 4801, 5051, 9091, 10753, 15361, 16001, 19841, 21001, 21401, 24001, 25601, 27961, 37501, 40961, 43201, 60101, 62501, 65537, 69857, 76001, 76801, 160001, 162251, 163841, 307201, 453377, 524801, 5882353, 182521213001)

	}


	test("euler153") {
		val l = new Range(1,Euler153bis.limit,1).map((u: Int) => {
			var lpdiv = new EulerDiv(u).primes.filter(_!=1)
					if(lpdiv.isEmpty) {
						lpdiv = List(u,1)
					}
			val eudiv = new EulerDivisors(lpdiv)
			var lidiv = ListSet(new ImBi(u,0),new ImBi(1,0))
			val intersection = Euler153bis.sumsqp.intersect( 
					eudiv.primesUnique.toList.map((p: BigInt) =>
					Euler153bis.primeEuler153.apply(Euler153bis.premiers.indexOf(p))).distinct)
					if(intersection.isEmpty) {
						lidiv = lidiv ++ eudiv.divisors.map((p: BigInt) => new ImBi(p,0))
					} else {
						intersection.foreach((y: Euler153) => {
							val eudiv2 = new EulerDivisors3(new EulerDiv(u/y.s).primes)
							lidiv = lidiv ++ y.lidiv.flatMap((im: ImBi) => eudiv2.divisors.map((bi: BigInt) => im*bi))
						})
					}
			val sum = lidiv.toList.map(_.r).sum
					println(u+" %8d".format(sum)+" %5d".format(lidiv.size)+" "+lpdiv+" "+lidiv)
			sum
		})

		/*val m = l.groupBy{_.lidiv.size}.toList.sortBy{_._1}
		println(m.mkString("\n"))*/

		println("Euler153 solution["+(Euler153bis.limit-1)+"]: "+l.sum)
	}



	val eu16 = new Euler185_16(List(
			(BigInt.apply("5616185650518293"),2),
			(BigInt.apply("3847439647293047"),1),
			(BigInt.apply("5855462940810587"),3),
			(BigInt.apply("9742855507068353"),3),
			(BigInt.apply("4296849643607543"),3),
			(BigInt.apply("3174248439465858"),1),
			(BigInt.apply("4513559094146117"),2),
			(BigInt.apply("7890971548908067"),3),
			(BigInt.apply("8157356344118483"),1),
			(BigInt.apply("2615250744386899"),2),
			(BigInt.apply("8690095851526254"),3),
			(BigInt.apply("6375711915077050"),1),
			(BigInt.apply("6913859173121360"),1),
			(BigInt.apply("6442889055042768"),2),
			(BigInt.apply("2321386104303845"),0),
			(BigInt.apply("2326509471271448"),2),
			(BigInt.apply("5251583379644322"),2),
			(BigInt.apply("1748270476758276"),3),
			(BigInt.apply("4895722652190306"),1),
			(BigInt.apply("3041631117224635"),3),
			(BigInt.apply("1841236454324589"),3),
			(BigInt.apply("2659862637316867"),2)
			))


	test("euler185ter") {
		println((ListSet[List[Int]]() ++ eu16.getDeuxAdeux1.map(_.yes)).head)
	}

	test("euler185bis") {
		// http://projecteuler.net/problem=185
		val test1 = new Euler185_5(List((70794,0),(34109,1),(12731,1),          (12555,2),(72666,2)))
		val test2 = new Euler185_5(List((70794,0),(34109,1),(12731,1),(12756,1),(12555,2),(72666,2)))
		val test3 = new Euler185_5(List((70794,0),(34109,1),(12731,1),(99996,0),(12555,2),(72666,2)))
		//		println("zeros:\n"+test1.zerosYesNo.mkString("\n"))
		//		println("uns:\n"+test1.unsYesNo.mkString("\n"))
		println("unsFiltered0:\n"+test1.unsFiltered0.mkString("\n"))
		//		println("deuxFiltered0:\n"+test1.deuxFiltered0.mkString("\n"))
		//		println("deuxFiltered01:\n"+test1.deuxFiltered01.mkString("\n"))

		test1.unsFiltered0.size should equal (test1.uns.flatMap(_.getPossible).size - 1)
		test1.deuxFiltered01grp.flatten.size should equal (test1.deux.flatMap(_.filter0(test1.zerosYesNo)).size - 1)

		val euc1 = new Euler185YesNo(ListSet(new Euler185Set5(0,0)), List(1,2,Euler185.rien,Euler185.rien,Euler185.rien),List(Euler185.rien,Euler185.rien,Euler185.rien,7,8))
		val euc11 = new Euler185YesNo(ListSet(new Euler185Set5(0,0)), List(1,2,Euler185.rien,Euler185.rien,Euler185.rien),List(Euler185.rien,Euler185.rien,Euler185.rien,7,8))
		val ls1 = ListSet(euc1,euc11)
		val l1 = List(euc1,euc11)
		/*		println("ls: "+ls1+" vs l: "+l1)
		println("ls: "+ls1.mkString("%")+" vs l: "+l1.mkString("%"))
		(ls1.mkString("%")) should equal (l1.mkString("%"))
		(ls1.head.toString) should equal (ls1.last.toString)
		(ls1.head) should equal (ls1.tail)*/
		(ls1.size+1) should equal (l1.size)
		var zero = new Euler185YesNo(ListSet(new Euler185Set5(0,0)), List(),List(1,3,3,3,3))
		var un = new Euler185YesNo(ListSet(new Euler185Set5(0,0)), List(1,Euler185.rien,Euler185.rien,Euler185.rien,Euler185.rien),List(Euler185.rien,2,2,2,2))
		println("euc1: "+euc1+" zero: "+zero+" un: "+un)
		println("euc1 chk zero: "+(euc1 chk zero))
		println("euc1 chk un: "+(euc1 chk un))
		(euc1 chk zero) should be (false)
		(euc1 chk un) should be (false)
		zero = new Euler185YesNo(ListSet(new Euler185Set5(0,0)), List(),List(2,3,3,3,3))
		un = new Euler185YesNo(ListSet(new Euler185Set5(0,0)), List(1,Euler185.rien,Euler185.rien,Euler185.rien,Euler185.rien),List(Euler185.rien,3,2,2,2))
		println("euc1: "+euc1+" zero: "+zero+" un: "+un)
		println("euc1 chk zero: "+(euc1 chk zero))
		println("euc1 chk un: "+(euc1 chk un))
		(euc1 chk zero) should be (true)
		(euc1 chk un) should be (true)

		println("*2**********************************************")
		println(test2.li)
		var d1 = test2.deux.head
		var d2 = test2.deux.last
		println(d1 + " & " + d2)
		println("    "+d1+ ".yesno: " + d1.yesno)
		println("    "+d2+ ".yesno: " + d2.yesno)
		var d1_d2 = d1 & d2
		println("  "+d1_d2)
		println("  "+test2.filter01(d1_d2))
		(test2.filter01(d1_d2).size + 1) should equal (d1_d2.size)
		test2.deuxFiltered01grp.toString should equal (test2.deux.map(_.yesno).toString)

		println("*3**********************************************")
		println(test3.li)
		d1 = test3.deux.head
		d2 = test3.deux.last
		println(d1 + " & " + d2)
		println("    "+d1+ ".yesno: " + d1.yesno)
		println("    "+d2+ ".yesno: " + d2.yesno)
		d1_d2 = d1 & d2
		println("  "+d1_d2)
		println("  "+test3.filter01(d1_d2))
		(test3.filter01(d1_d2).size) should equal (d1_d2.size)

		println("euc1: [y: 3954_, n: ____5] & euc2: [y: ___42, n: 9_3__]")
		val euc3954 = new Euler185YesNo(ListSet(new Euler185Set5(0,0)), List(3,9,5,4,Euler185.rien),List(Euler185.rien,Euler185.rien,Euler185.rien,Euler185.rien,5))
		val euc42 = new Euler185YesNo(ListSet(new Euler185Set5(0,0)), List(Euler185.rien,Euler185.rien,Euler185.rien,4,2),List(9,Euler185.rien,3,Euler185.rien,Euler185.rien))
		val et = (euc3954 & euc42)
		println("euc1: "+euc3954+" & euc2: "+euc42+" => "+et)
		et.head.yes.mkString("") should equal ("39542")

		// http://projecteuler.net/problem=185
		println("*39542**********************************************")
		Euler185.cpt = 0
		val eu = new Euler185_5(List((90342,2),(70794,0),(39458,2),(34109,1),(51545,2),(12531,1)))
		println(eu.l)
		val solution = (ListSet[List[Int]]() ++ eu.getDeuxAdeux1.map(_.yes)).head
		println(solution)
		solution.mkString("") should equal ("39542")
		println("Euler185.cpt: "+Euler185.cpt)

		println("*18701**********************************************")
		val test4 = new Euler185_5(List((34109,1),(88809,2),(82932,0),(12731,3)))
		println(test4.l)
		val solution4 = (ListSet[List[Int]]() ++ test4.getDeuxAdeux1.map(_.yes)).head
		println(solution4)
		solution4.mkString("") should equal ("18701")

		println("*ter1**********************************************")
		//	8690095851526254; 3 5616185650518293; 2   [y: _6____5_5_5__2__, n: 8_9009_8_1_26_54; 5_1618_6_0_18_93] 5 reponse: 3 eus.reponse: 2
		val tester1 = new Euler185_16(List(
				(BigInt.apply("2321386104303845"),0),
				(BigInt.apply("8690095851526254"),3),
				(BigInt.apply("5616185650518293"),2)
				))
		val XZ = (tester1.lnozeros.head X tester1.lnozeros.last)
		println("XZ "+XZ)

		val yesnoz = tester1.lnozeros.head.filterz2(tester1.lnozeros.head.yesno, XZ)
		println("deuxYesNosZ:\n  "+yesnoz.size+" "+yesnoz+"\n  "+(tester1.lnozeros.head.yesno  filterNot(  yesnoz contains)))
		(tester1.lnozeros.head.yesno  filterNot(  yesnoz contains)).isEmpty should be (true)

		val yesnoz2 = tester1.lnozeros.last.filterz2(tester1.lnozeros.last.yesno, XZ)
		println("deuxYesNosZ:\n  "+yesnoz2.size+" "+yesnoz2+"\n  "+(tester1.lnozeros.last.yesno  filterNot(  yesnoz2 contains)))
		(tester1.lnozeros.last.yesno  filterNot(  yesnoz2 contains)).size should equal (10)


		println("*5**********************************************")
		val test5 = new Euler185_16(List(
				(BigInt.apply("2615250744386899"),2),
				(BigInt.apply("2659862637316867"),2)
				))
		println(test5.deux.head X test5.deux.last)

		println("*6**********************************************")
		val test6 = new Euler185_16(List(
				(BigInt.apply("2321386104303845"),0),
				(BigInt.apply("2615250744386899"),2),
				(BigInt.apply("2659862637316867"),2)
				))
		println(test6.deux.head X test6.deux.last)

	}

	ignore("euler185") {
		// http://projecteuler.net/problem=185
		println(eu16.l)
		//		val solution = (ListSet[List[Int]]() ++ eu.getDeuxAdeux1.map(_.yes)).head
		//		println(solution) 
	}

	test("euler204") {
		def getMax(n: Int,limit: BigInt): Int = new Range(1,30,1).toList.filter((i: Int) => Euler.powl(n,i)<=limit).max

				def getMaxes(n: Int, limit: BigInt): List[(Int,Int)] = {
			EulerPrime.premiers1000.toList.filter(_<=n).map((p: BigInt) => {
				//print("            p: "+p+" limit: "+limit)
				if(p<=limit) {
					val max = getMax(p.toInt,limit)
							//println(" max: "+max)
							(p.toInt,max)
				} else {
					//println(" max: "+0)
					(p.toInt,0)
				}
			})
		}

		def toString2(l: List[BigInt]): String = l.distinct.map((bi: BigInt) => {
			val c = l.count(_==bi)
					if(c==1) {
						""+bi
					} else {
						bi+"e"+ c
					}
		}).mkString("[",", ","]")
		def toString3(l: List[BigInt]): String = toString2(l)+"_"+l.product
		def toString4(l: List[BigInt]): String = "%09d".format(l.product)

		def getList(n: Int, max: Int): List[Int] = {
			new Range(1,max+1,1).map((i: Int) => n).toList
		}

		def getCombi1(i: Int, s: List[BigInt], maxes: List[(Int,Int)], limit: BigInt): List[List[BigInt]] = {
			s.combinations(i).toList.filter(_.product<=limit)
		}

		def getCombi2(i: Int, s: List[BigInt], maxes: List[(Int,Int)], limit: BigInt): List[List[BigInt]] = {
			if(i<=(maxes.map(_._2).min+1)) {
				println(" - "+i+" "+s.size+" "+toString2(s))
				s.combinations(i).toList.filter(_.product<=limit)
			} else {
				val biggerNumbers = maxes.filter(_._2<i)
						val smallerNumbers = maxes.filter(_._2>=i)
						var s3 = List[List[BigInt]]()
						val small1 = smallerNumbers.flatMap((c: (Int,Int)) => getList(c._1,c._2)).map(BigInt(_))
						biggerNumbers.foreach((c: (Int, Int)) => {							
							val newlimit = limit/c._1
									val newmaxes = getMaxes(5,newlimit)
									val s1 = newmaxes.flatMap((c: (Int,Int)) => getList(c._1,c._2)).map(BigInt(_))
									//println(" + "+i+" c: "+c._1+" "+s1.size+" "+toString2(s1))
									val s2 = s1.combinations(i).toList.filter(_.product<=limit)
									s3 = s3 ++ s2
						})
						s3.distinct.sortBy{(l: List[BigInt]) => l.product}
			}

		}

		def getCombi3(s: List[BigInt], maxes: List[(Int,Int)], limit: BigInt): List[List[BigInt]] = {
			var t4 = List[List[BigInt]]()
					val min = maxes.map(_._2).min
					val newmaxes1 = maxes.filter(_._2<=min)
					val sBiggerNumbers = newmaxes1.flatMap((c: (Int,Int)) => getList(c._1,c._2)).map(BigInt(_)) 
					//println("sBiggerNumbers: "+sBiggerNumbers)
					var t1 = new Range(1,maxes.map(_._2).max+1,1).flatMap((j: Int) => {
						val c = getCombi1(j,sBiggerNumbers,newmaxes1,limit)
								//println("    j: "+j+" c13: "+c.size+" "+c.map((l: List[BigInt]) => toString2(l)))
								c
					})
					//println("t1: "+t1.size+" "+t1.map((l: List[BigInt]) => toString2(l)))
					t1.foreach((l: List[BigInt]) => {
						val newlimit = limit/l.product
								val newmaxes2 = getMaxes(newmaxes1.map(_._1).min-1,newlimit)
								//println(" l: "+l+" newlimit: "+newlimit+" newmaxes2: "+ newmaxes2)
								val s1 = newmaxes2.flatMap((c: (Int,Int)) => getList(c._1,c._2)).map(BigInt(_))
								//println(" l: "+l+" "+s1.size+" "+toString2(s1))
								val t3 = new Range(1,newmaxes2.map(_._2).max+1,1).flatMap((j: Int) => {
									val c = getCombi1(j,s1,newmaxes2,newlimit)
											val d = c.map((lm: List[BigInt]) => lm ++ l)
											//println("    j: "+j+" d: "+d.size+" "+d.map((lm: List[BigInt]) => toString2(lm)))
											t4 = t4 ++ d
											d
								})
					})

					val smallerNumbers = maxes filterNot( newmaxes1 contains)
					val t5 = new Range(1,smallerNumbers.map(_._2).max+1,1).flatMap((i: Int) => {
						val c = getCombi1(i,s,maxes,limit)
								//println("  "+i+" c: "+c.size+" "+c.map((l: List[BigInt]) => toString2(l)))
								c
					})
					t4 = t4 ++ t5
					t4.distinct.sortBy{(l: List[BigInt]) => l.product}
		}

		def doZeJob1(hamming: Int, limit: BigInt): List[List[BigInt]] = {
			println("*******1* hamming: "+hamming+", limit: "+limit+" **************")
			val maxes = getMaxes(hamming,limit)
			println("maxes: "+ maxes)
			val s = maxes.flatMap((c: (Int,Int)) => getList(c._1,c._2)).map(BigInt(_))
			println("  s: "+s)
			println(s.size+" "+toString2(s))
			val t1 = new Range(1,maxes.map(_._2).max+1,1).flatMap((i: Int) => {
				val c = getCombi1(i,s,maxes,limit)
						//println("  "+i+" c: "+c.size+" "+c.map((l: List[BigInt]) => toString2(l)))
						c
			}) :+ List(BigInt(1))
			//println("\nt1: "+t1.size+" "+t1+"\n")
			t1.toList.distinct.sortBy{(l: List[BigInt]) => l.toString}
		}

		def doZeJob8(hamming: Int, limit: BigInt): List[BigInt] = {
			println("*******8* hamming: "+hamming+", limit: "+limit+" **************")
			var start = 100
			//assume(hamming*hamming<start)		  
			var lstart = doZeJob1(hamming,start)
			while(start<limit) {
				lstart = doZeJob6(hamming, start, lstart).map((l: List[BigInt]) => l.product).distinct.map((bi: BigInt) => new EulerDiv(bi).primes)
						start = start*10
			}			
			lstart.map((l: List[BigInt]) => l.product).distinct.sorted
		}

		def doZeJob6(hamming: Int, start: BigInt, lstart: List[List[BigInt]]): List[List[BigInt]] = {
			var startmaxes = getMaxes(hamming,start)
					var lstartmax = lstart.filter((l: List[BigInt]) => !startmaxes.filter((c: (Int, Int)) => l.count(_==c._1)==c._2).isEmpty)
					print("  start: "+start+" lstartmax: "+lstartmax.map((l: List[BigInt]) => toString2(l)))
					var next = start*10
					var nextmaxes = getMaxes(hamming,next)
					var diffmaxes = startmaxes.zip(nextmaxes).map((cc: ((Int, Int), (Int, Int))) => {
						(cc._1._1,cc._2._2-cc._1._2)
					})
					print("  diffmaxes: "+diffmaxes)
					val s = diffmaxes.flatMap((c: (Int,Int)) => getList(c._1,c._2)).map(BigInt(_))
					println("  s: "+s)
					val t1 = new Range(1,diffmaxes.map(_._2).max+1,1).flatMap((i: Int) => {
						val c = getCombi1(i,s,diffmaxes,next)
								//println("  "+i+" c: "+c.size+" "+c.map((l: List[BigInt]) => toString2(l)))
								c
					})
					//println("\nt1: "+t1.size+" "+t1.map((l: List[BigInt]) => toString2(l)))
					val t4 = t1.flatMap((l1: List[BigInt]) => {
						lstart.map((l2: List[BigInt]) => l1 ++ l2)
					}).distinct.sortBy{_.toString}
			//println("\nt4: "+t4.size+" "+t4.map((l: List[BigInt]) => toString2(l)))

			val t5 = (t4.toList.map((l: List[BigInt]) => l.filter(_!=1)) :+ List(BigInt(1))).filter(_.product<=next)
					t5

		}

		def filt(down: BigInt, up: BigInt, l: List[List[BigInt]]) = {
			val z = l.filter((l: List[BigInt]) => {
				val p = l.product
						p>=down&p<up
			})
			println(down+"-"+up+" "+z.size+" "+z)
		}

		def doZejob10(hamming: Int, limit: BigInt) = {
			var bi = BigInt(1)
					var cpt = BigInt(0)
					while(bi<=limit) {
						val primes = new EulerDiv(bi).primes
								if(primes.filter(_>hamming).isEmpty) {
									cpt += 1
								}
						//println("bi: "+bi+" primes   : "+primes+" "+primes.filter(_>hamming)+" "+cpt)
						if(bi%10000==0) {
							print("\n"+bi+" ")
						} else if(bi%1000==0) {
							print(bi+" ")
						}
						bi +=1
					}
			println("hamming: "+hamming+", limit: "+limit+" : "+cpt)
		}

		doZeJob1(5,Euler.powl(10,8)).size should equal (1105)
		var limit = Euler.powl(10,2)
		var hamming = 20
		var zx1 = doZeJob1(hamming,limit)
		var zx7 = doZeJob8(hamming,limit)
		println("\nzx1   : "+zx1.size+" "+zx1.map((l: List[BigInt]) => l.product).sorted)
		println("zx7   : "+zx7.size+" "+zx7)
		assume(zx1.map((l: List[BigInt]) => l.product).sorted==zx7)
		doZejob10(hamming,limit)

		var i = 3
		var s = List[BigInt](2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 5, 5, 7, 7, 11, 13, 17, 19)
		var maxes = List((2,6), (3,4), (5,2), (7,2), (11,1), (13,1), (17,1), (19,1))
		var qx1 = getCombi1(i, s, maxes, limit: BigInt)
		println("qx1   : "+qx1.size+" "+qx1)
		var qx2 = getCombi2(i, s, maxes, limit: BigInt)
		println("qx2   : "+qx2.size+" "+qx2)
		var qx3 = getCombi2(i, s, maxes, limit: BigInt)
		println("qx3   : "+qx3.size+" "+qx3)
	}

	test("euler272") {
		val limit = 10000
				val cubes = new Range(1,limit,1).toList.map(i => {
					val j = BigInt(i)
							val j3 = (j*j*j)-1
							(j,j3,new EulerDivisors(new EulerDiv(j3).primes).divisors)
				})

				def x3modn(bi: BigInt, n: Int): BigInt = (bi*bi*bi)%n

				def getList(n: Int): List[Int] = {
			new Range(2,n,1).toList.map(i => (i,x3modn(i,n))).filter(_._2==1).map(_._1.toInt)
		}

		def firstPass(limit: Int) = {
			var max=8
					var zprimes = ListSet[BigInt]()
					new Range(1,limit,1).toList.foreach( i => {
						val z = cubes.filter(t => t._3.contains(i)).map(_._1).filter(_<i)
								if(z.size>=(max-2)) {
									val primes = new EulerDiv(i).primes
											zprimes = ListSet[BigInt]() ++ (zprimes ++ primes).toList.sorted
											println("\n"+i+" "+z.size+" "+primes+"\n    "+zprimes.size+" "+zprimes)
									max=z.size

								} else if(i%1000==0) {
									print("\n"+i)
								} else if(i%100==0) {
									print(".")
								}
					})
					//ListSet(157, 151, 139, 127, 109, 103, 97, 79, 73, 67, 61, 43, 37, 31, 19, 13, 11, 7, 5, 3, 2)
		}

		cubes.filter(t => t._3.contains(91)).map(_._1).filter(_<91) should equal (getList(91))

		//firstPass(1000)

		val from1stPass = List(109, 103, 97, 79, 73, 67, 61, 43, 37, 31, 19, 13, 11, 7, 5, 3, 2) ++ List(3,3,3,7,7,7,2,2,2)
		val combi = from1stPass.combinations(5)
		var result = List[(Int,List[BigInt])]()
		combi.foreach(l => {
			val p = l.map(BigInt(_)).product
					val z = cubes.filter(t => t._3.contains(p)).map(_._1).filter(_<p)
					if(z.size>=8) {
						val primes = new EulerDiv(p).primes
								println(p+" "+z.size+" "+primes)
						result = result :+ (z.size,primes)
					}
		})
		println(result.groupBy{_._1}.mkString("\n"))
	}

	
	test("euler286") {
		new Euler286(50 + 0.001).doIt
		val range = new Range(1,10,1).toList
		range.foreach((i: Int) => {
			new Euler286(50 + (i.toDouble*i.toDouble)).doIt
		})
	}




	// http://projecteuler.net/problem=368
	ignore("euler368") {
		val precision = Math.pow(10,-10)
				val digits2check3 = (0 until 10).toList.map((z: Int) => z.toString+z.toString+z.toString).toString
				val inputRules = List(
						new Result(new Item(0,1,0.0,true), new PlusUnSurI,new NoFilter),
						new Result(new Item(0,1,0.0,true), new PlusUnSurI,new Function((it: Item) => new Item(it.i.toString.indexOf("9")<0),"No9")),
						new Result(new Item(0,1,0.0,true), new PlusUnSurI,new Function((it: Item) => new Item(
								{
									if(it.i.toString.size>=3) {
										val slided = it.i.toString.toList.sliding(3).toList
												val flat = slided.map(_.mkString(""))
												!flat.exists(digits2check3.indexOf(_)>0)
									} else {
										true
									}
								}
								),"NoSame3ConsecutiveDigits"))
						)

						var current = inputRules
						var log = List(current)

						while(current.head.start.i<=1200) {
							println(current)
							current = current.map(_.process)
							log = log :+ current
						}
		val same3ConsecutiveDigits = log.map(_.last).filter(_.sameAsPrevious)
				println(same3ConsecutiveDigits.mkString("\n"))
				require(same3ConsecutiveDigits.size==20)
	}

	test("euler381") {
		val start = 0
				val startn = 1
				//				val startn = EulerFactorielle.fact2(start)

				def S(bi: BigInt): BigInt = new Range(1,6,1).map((i: Int) => EulerFactorielle.fact(bi-i)).sum%bi

				def S2(bi: BigInt): BigInt = new Range(1,6,1).map((i: Int) => EulerFactorielle.fact2(bi.toInt-i)).sum%bi

				def range(bi: BigInt): List[BigInt] = new Range(1,6,1).map((i: Int) => EulerFactorielle.fact(bi-i)).toList

				def factmod(n: BigInt, bas: BigInt): BigInt = {
			var cpt = start+1
					var result = startn%bas
					while(cpt<=n) {
						result = (result*cpt)%bas
								cpt += 1			  
					}
			result
		}

		def S3(bi: BigInt): BigInt = {
			val z5 = factmod(bi-5,bi)
					val z4 = (z5*(bi-4))%bi
					val z3 = (z4*(bi-3))%bi
					val z2 = (z3*(bi-2))%bi
					val z1 = (z2*(bi-1))%bi
					(z1+z2+z3+z4+z5)%bi
		}

		EulerFactorielle.fact(0) should equal(1)
		EulerFactorielle.fact(1) should equal(1)
		EulerFactorielle.fact2(0) should equal(1)
		EulerFactorielle.fact2(1) should equal(1)
		EulerFactorielle.fact(15) should equal(BigInt.apply("1307674368000"))
		EulerFactorielle.fact2(15) should equal(BigInt.apply("1307674368000"))
		val x = new EulerDiv(BigInt.apply("1307674368000")).primes
		println(x+" "+x.sum)
		S(7) should equal(4)

		println(7+" "+EulerFactorielle.fact(7))
		var y = new Range(0,16,1).map((i: Int) => EulerFactorielle.fact(i))
		println(y)

		var limit = 100
		val z = new Range(5,limit+1,1).map((i: Int) => {
			val s = S(BigInt(i))
					val l = range(i)					
					val s2 = S2(BigInt(i))
					val s3 = S3(BigInt(i))								
					println(i+" "+s+" "+s2+" "+s3+" "+EulerPrime.isPrime(i)+" "+range(i).sum%i)
					s3 should equal (s)
			(l.sum-s)%i should equal (0)
			s2 should equal (s)
			(i,s)
		}).filter(_._2!=0)
		println(z)
		println(EulerPrime.premiers1000.filter(_<=limit))
		val cpt = z.map(_._2).sum
		println("cpt: "+cpt)
		cpt should equal(480)

		val x14e = EulerFactorielle.fact(14)%97
		val x15e = EulerFactorielle.fact(15)%97
		x15e should equal((x14e*15)%97)
	}


}


/* Euler60
 List(ListSet(7, 853, 8017, 8779), ListSet(7, 853, 2269, 8779), ListSet(7, 541, 2467, 8167), ListSet(7, 127, 6949, 7723), ListSet(7, 3019, 7351, 9613), ListSet(7, 19, 97, 4507), ListSet(7, 19, 97, 3727), ListSet(7, 1249, 3727, 6949), ListSet(7, 19, 3727, 5659), ListSet(7, 19, 1249, 3727), ListSet(7, 829, 2671, 3361), ListSet(7, 433, 1471, 3613), ListSet(7, 1237, 1549, 3019), ListSet(7, 61, 487, 8941), ListSet(7, 433, 3613, 9613), ListSet(7, 1249, 4441, 6949), ListSet(7, 2269, 3613, 5821), ListSet(7, 2089, 3181, 4219), ListSet(7, 2089, 2953, 3181), ListSet(7, 61, 1693, 3181))
 List(ListSet(3, 3923, 4919, 8783), ListSet(3, 37, 1237, 8713), ListSet(3, 1237, 6571, 8713), ListSet(3, 37, 1699, 8713), ListSet(3, 1699, 6571, 8713), ListSet(3, 331, 739, 8431), ListSet(3, 31, 7591, 8431), ListSet(3, 37, 5923, 7963), ListSet(3, 37, 4729, 7963), ListSet(3, 4729, 7963, 9181), ListSet(3, 73, 6793, 7159), ListSet(3, 17, 449, 6353), ListSet(3, 37, 67, 5923), ListSet(3, 467, 617, 4253), ListSet(3, 2503, 5281, 5869), ListSet(3, 37, 2377, 4159), ListSet(3, 37, 67, 2377), ListSet(3, 31, 1237, 6571), ListSet(3, 7, 109, 673), ListSet(3, 191, 2069, 8747), ListSet(3, 11, 2069, 8747), ListSet(3, 11, 701, 8747), ListSet(3, 11, 2069, 8219), ListSet(3, 17, 449, 6599), ListSet(3, 7, 541, 4159), ListSet(3, 17, 2069, 2297), ListSet(3, 11, 2069, 2297), ListSet(3, 17, 449, 2069))
 List(ListSet(23, 311, 677, 827))
 */

class Euler60 extends EulerPrime(10000, 1000) {
	val pr = EulerPrime.premiers100000.toList
			val spr = pr.map(_.toString)
			val plast = pr.last /10

			def doZeJob2 = {
		val lz: List[ListSet[BigInt]] = List[ListSet[BigInt]](ListSet(7, 853, 8017, 8779), ListSet(7, 853, 2269, 8779), ListSet(7, 541, 2467, 8167), ListSet(7, 127, 6949, 7723), ListSet(7, 3019, 7351, 9613), ListSet(7, 19, 97, 4507), ListSet(7, 19, 97, 3727), ListSet(7, 1249, 3727, 6949), ListSet(7, 19, 3727, 5659), 
				ListSet(7, 19, 1249, 3727), ListSet(7, 829, 2671, 3361), ListSet(7, 433, 1471, 3613), ListSet(7, 1237, 1549, 3019), 
				ListSet(7, 61, 487, 8941), ListSet(7, 433, 3613, 9613), ListSet(7, 1249, 4441, 6949), ListSet(7, 2269, 3613, 5821), 
				ListSet(7, 2089, 3181, 4219), ListSet(7, 2089, 2953, 3181), ListSet(7, 61, 1693, 3181)) ++ List[ListSet[BigInt]](ListSet(3, 3923, 4919, 8783), 
						ListSet(3, 37, 1237, 8713), ListSet(3, 1237, 6571, 8713), ListSet(3, 37, 1699, 8713), ListSet(3, 1699, 6571, 8713), 
						ListSet(3, 331, 739, 8431), ListSet(3, 31, 7591, 8431), ListSet(3, 37, 5923, 7963), ListSet(3, 37, 4729, 7963), 
						ListSet(3, 4729, 7963, 9181), ListSet(3, 73, 6793, 7159), ListSet(3, 17, 449, 6353), ListSet(3, 37, 67, 5923), 
						ListSet(3, 467, 617, 4253), ListSet(3, 2503, 5281, 5869), ListSet(3, 37, 2377, 4159), ListSet(3, 37, 67, 2377), 
						ListSet(3, 31, 1237, 6571), ListSet(3, 7, 109, 673), ListSet(3, 191, 2069, 8747), ListSet(3, 11, 2069, 8747), 
						ListSet(3, 11, 701, 8747), ListSet(3, 11, 2069, 8219), ListSet(3, 17, 449, 6599), ListSet(3, 7, 541, 4159), 
						ListSet(3, 17, 2069, 2297), ListSet(3, 11, 2069, 2297), ListSet(3, 17, 449, 2069)) ++ List[ListSet[BigInt]](ListSet(23, 311, 677, 827))

						println(lz.mkString("\n"))

						val allFlat = (ListSet[BigInt]() ++ lz.flatten).toList.sorted
						println(allFlat)
	val o = allFlat.map((bi: BigInt) => {
		lz.filter(_.contains(bi))
	}).filter(_.size>1).foreach((l: List[ListSet[BigInt]]) => {
		println("\n*1* "+l.size+l+" ")
		val q = l.combinations(2).filter((l2: List[ListSet[BigInt]]) => {
			val r  = chklistter(ListSet[BigInt]() ++ l2.flatten)
					if(r) {
						println("\n"+l2)
					} else {
						print(".")
					}
			r
		})
		println("\n*2* "+q.toString)
	})
	// println(o.mkString("\n"))



	var s = ""
	pr.foreach((p: BigInt) => {
		val y = lz.map((ls: ListSet[BigInt]) => ls + p).filter((ls: ListSet[BigInt]) => chklistter(ls)).filter(_.size>4)
				if(!y.isEmpty) {
					s = s + y.toString
				}
		println("\n"+p+" "+s)
	})
	}

	def doZeJob: List[ListSet[BigInt]] = {
		val step1 = (ListSet[List[BigInt]]() ++ pr.filter((p: BigInt) => p<plast ).flatMap((p: BigInt) => {
			val sp = p.toString
					val le = sp.size
					val lc = spr.filter((s: String) => s.indexOf(sp)>=0)
					val lc0 = lc.filter((s: String) => s.indexOf(sp)==0).filter(_!=sp)
					val lc1 = lc.filter((s: String) => s.indexOf(sp)==s.size-le).filter(_!=sp)
					val lc0remain = lc0.map((s: String) => {
						if(s.apply(le)!='0') {
							s.substring(le,s.size).toInt
						} else {
							0
						}
					}).filter((i: Int) => EulerPrime.isPrime(i)) 
					val lc1remain = lc1.map((s: String) => {
						if(s.size-le!=0) {
							s.substring(0,s.size-le).toInt
						} else {
							0
						}
					}).filter((i: Int) => EulerPrime.isPrime(i)) 
					//println(p+": "+lc+"\n lc0: "+lc0+"\n lc1: "+lc1)
					//println(p+": "+lc+"\n lc0r: "+lc0remain+"\n lc1r: "+lc1remain)
					(lc0remain.intersect(lc1remain).map(BigInt(_)).toList).map((bi: BigInt) => List(p,bi).sorted)
		})).toList.sortBy{_.head}
		println("step1:\n "+step1.mkString("\n "))
		val step2 = step1.groupBy{_.head}
		println("step2:\n "+step2.mkString("\n "))
		val step3 = step2.flatMap((m: (BigInt,List[List[BigInt]])) => {
			println("  "+m._1+" "+m._2.size)
			m._2.combinations(2).map((cl: List[List[BigInt]]) => ListSet[BigInt]()++(ListSet[BigInt]()++cl.head++cl.last).toList.sorted.reverse).filter(chklist(_)).toList.sortBy{_.head}
		}).toList
		println("step3:\n "+step3.mkString("\n "))
		step3
	}

	def doZeJob5 = {
		val anotherTry = List(
				List(ListSet(17209, 3, 37, 4159), ListSet(17209, 3, 31, 4159), ListSet(17209, 3, 541, 4159)),
				List(ListSet(17989, 7, 7489, 9043), ListSet(17989, 7, 7489, 8839)),
				List(ListSet(18433, 7, 109, 4567), ListSet(18433, 7, 1237, 2341), ListSet(18433, 7, 229, 433)),
				List(ListSet(14627, 3, 59, 1193), ListSet(14627, 3, 449, 2069)),
				List(ListSet(15643, 7, 5437, 5689), ListSet(15643, 79, 367, 613)),
				List(ListSet(13399, 3, 31, 8377), ListSet(13399, 3, 31, 1237)),
				List(ListSet(12409, 7, 1879, 2341), ListSet(12409, 7, 1237, 2341)),
				List(ListSet(22109, 3, 4253, 9521), ListSet(22109, 3, 617, 4253)),
				List(ListSet(22613, 3, 1907, 5099), ListSet(22613, 3, 191, 5099)),
				List(ListSet(22639, 7, 283, 1237), ListSet(22639, 7, 2161, 6949)),
				List(ListSet(22921, 3, 7, 673), ListSet(22921, 3, 7, 9901)),
				List(ListSet(23251, 3, 2503, 7591), ListSet(23251, 3, 2503, 5869)),
				List(ListSet(27823, 7, 6949, 7723), ListSet(27823, 7, 4027, 6949), ListSet(27823, 7, 2287, 8101), ListSet(27823, 7, 823, 8101), ListSet(27823, 3, 739, 6793), ListSet(27823, 3, 31, 3301), ListSet(27823, 3, 823, 5281), ListSet(27823, 3, 7, 823), ListSet(27823, 3, 31, 5281)),
				List(ListSet(28729, 7, 433, 9613), ListSet(28729, 7, 229, 433)),
				List(ListSet(29059, 7, 61, 3181), ListSet(29059, 3, 7, 109)),
				List(ListSet(30703, 3, 37, 7369), ListSet(30703, 3, 37, 6529), ListSet(30703, 3, 37, 4159), ListSet(30703, 3, 37, 2707)),
				List(ListSet(32299, 7, 8017, 9613), ListSet(32299, 7, 3019, 9613), ListSet(32299, 7, 3019, 8941)),
				List(ListSet(34687, 7, 487, 3433), ListSet(34687, 7, 1249, 4441)),
				List(ListSet(39827, 3, 3119, 9887), ListSet(39827, 3, 1667, 5051)),
				List(ListSet(40387, 7, 3181, 9043), ListSet(40387, 7, 1693, 3181)),
				List(ListSet(41443, 3, 31, 6073), ListSet(41443, 3, 31, 4159)))



				val step6 = anotherTry.filter((li: List[ListSet[Int]]) => {
					!li.combinations(2).map((li2: List[ListSet[Int]]) => ListSet[BigInt]() ++ li2.flatten.map(BigInt(_))).filter(chklistbis(_)).isEmpty
				}).toList
				println("step6:\n "+step6.toList.mkString("\n "))

	}

	def doZeJob4 = {
		val step3 = doZeJob
				var cpt = 0
				val step5 = pr.filter(_>9500).map((p: BigInt) => {
					val u = step3.map((l: ListSet[BigInt]) => l + p).filter(chklist(_))
							if(!u.isEmpty) {
								println("\n"+u)
							}
					cpt += 1
							if(cpt%100==59) {
								print(" "+p)
							}
					u
				})
				println("step5:\n "+step5.mkString("\n "))

	}

	def doZeJob3 = {
		val step3 = doZeJob
				val step4 = step3.groupBy{_.head}
		println("step4:\n "+step4.mkString("\n "))
		val step5 = step4.map((m: (BigInt,List[ListSet[BigInt]])) => {
			println("  "+m._1+" "+m._2.size)
			(ListSet[ListSet[BigInt]]() ++ m._2.combinations(2).map((cl: List[ListSet[BigInt]]) => ListSet[BigInt]()++(ListSet[BigInt]()++cl.head++cl.last).toList.sorted.reverse)).filter(chklist(_)).toList.sortBy{_.head}
		}).toList
		println("step5:\n "+step5.mkString("\n "))
	}

	def euler60(bi1: BigInt, bi2: BigInt): Boolean = {
		(EulerPrime.isPrime(BigInt.apply((bi1.toString+bi2.toString)))) & (EulerPrime.isPrime(BigInt.apply((bi2.toString+bi1.toString))))
	}

	def euler60bis(bi1: BigInt, bi2: BigInt): Boolean = {
		val o =	(EulerPrime.isPrime(BigInt.apply((bi1.toString+bi2.toString)))) & (EulerPrime.isPrime(BigInt.apply((bi2.toString+bi1.toString))))
				println("  "+bi1+" "+bi2+" : "+o)
				o
	}

	def euler60ter(bi1: BigInt, bi2: BigInt): Boolean = {
		val o =	(EulerPrime.isPrime(BigInt.apply((bi1.toString+bi2.toString)))) & (EulerPrime.isPrime(BigInt.apply((bi2.toString+bi1.toString))))
				if(!o) {
					print("  "+bi1+" "+bi2+" : "+o)
				}
		o
	}

	def chklistbis(li: ListSet[BigInt]): Boolean = {
		println("chklist "+li)
		val o = li.toList.combinations(2).filter((l: List[BigInt]) => !euler60bis(l.head,l.last)).toList
		println(" "+o.isEmpty)
		o.isEmpty
	}

	def chklistter(li: ListSet[BigInt]): Boolean = {
		li.toList.combinations(2).filter((l: List[BigInt]) => !euler60ter(l.head,l.last)).isEmpty
	}

	def chklist(li: ListSet[BigInt]): Boolean = {
		li.toList.combinations(2).filter((l: List[BigInt]) => !euler60(l.head,l.last)).isEmpty
	}

}


class Euler76ordering extends  Ordering[List[Int]] {
	def compare (x: List[Int], y:List[Int]): Int = {
			if(x.size==y.size) {
				x.sorted.toString.compare(y.sorted.toString)
			} else {
				x.size.compare(y.size)
			}

	}
}

object Euler76 {
	var queue = List[Int]()
}

class Euler76(val n: Int) {
	var ts = TreeSet[List[Int]]()(new Euler76ordering)

			var eunm1: Euler76 = _
			var split = List[Int]()
			if(n<2) {
			} else if(n==2) {
				ts = ts + List(1,1)
						println(n+" "+ts.size)
						eunm1 = new Euler76(n-1)
			} else {
				eunm1 = new Euler76(n-1)
				ts = ts ++ eunm1.ts.map((l: List[Int]) => l :+ 1) + List(n-1,1) 
				//				println("      ** "+ts)
				val rangenum = new Range(1,n-1,1).toList
				rangenum.foreach((num: Int) => {
					ts = ts ++ getIt(num,ts)
							//							println("      *"+num+"* "+ts)
				})
				ts = ts.filter(_.sum == n)
				println(n+" "+ts.size)
				split = (new Range(2,n+1,1)).toList.map((num: Int) => ts.filter(_.size==num).size)
				println("  split: "+split)
				//				println("  "+split.sliding(2).toList.map((l: List[Int]) => l.head-l.last))
				println("  rangenum: "+rangenum.tail.map(combi(_)))


				require(split.sum==ts.size)
				if(n%2==0) {
					Euler76.queue = Euler76.queue :+ split.apply((n/2))
							//							println("  queue: "+Euler76.queue.reverse)
				}
			}
	if(n<11) {
		//		println("    "+ts)
	}
	println("*************************************")

	def getIt(num: Int, ts: TreeSet[List[Int]]): TreeSet[List[Int]] = {
		val rangenum = new Range(1,num,1).toList

				val tso = TreeSet[List[Int]]()(new Euler76ordering) ++ ts.filter((l: List[Int]) => l.count(_==1)>=num).map((l: List[Int]) => {
					val count = l.count(_==1)
							if(count==num) {
								l.filter(_!=1) :+ num
							} else {
								var ls = l.filter(_!=1) :+ num
										val range = new Range(1,count-num,1).toList
										range.foreach((i: Int) => ls = ls :+ 1)
										ls
							}
				}).filter(_.sum==n)
				//				println("    "+num+" tso: "+tso)
				tso
	}

	def combi(num: Int): Int = {
		val limit = 105
				var sum = 0
				val lsi = ts.filter(_.size==num).map(_.sorted).toList.sortBy{_.toString}
		/*		if(num==4) {
			println("     num: "+num+" \n  "+lsi.mkString("\n  ")+"\n")
		}*/
		num match {
		case 2 => sum=n/2
		case 3 => {
			val range = new Range(2,limit,3).toList
					val l2 = (List((n-1)/2) ++ range.map((i: Int) => ((n-i)/2)-1)).filter(_>0)
					//			val l = List((n-1)/2,((n-2)/2)-1,((n-5)/2)-1,((n-8)/2)-1,((n-11)/2)-1,((n-14)/2)-1).filter(_>0)
					sum=l2.sum
					//				println("                 "+num+" - "+l2+" "+sum)
		}
		case 4 => {
			val ref = List(new Euler76Func(2,0,List(1,1)),
					new Euler76Func(3,List(1,1))
			,new Euler76Func(4,List(1,2)),
			new Euler76Func(6,List(1,3)),
			new Euler76Func(7,List(2,3)),
			new Euler76Func(8,List(3,3)),
			new Euler76Func(9,List(1,4)),
			new Euler76Func(10,List(2,4)),
			new Euler76Func(11,List(3,4)),
			new Euler76Func(12,List(1,5)),
			new Euler76Func(12,List(4,4)),
			new Euler76Func(13,List(2,5)),
			new Euler76Func(14,List(3,5)),
			new Euler76Func(15,List(4,5)),
			new Euler76Func(15,List(1,6)),
			new Euler76Func(16,List(5,5))
			,new Euler76Func(16,List(2,6))
			,new Euler76Func(17,List(3,6))
			,new Euler76Func(18,List(4,6))
			,new Euler76Func(18,List(1,7))
			,new Euler76Func(19,List(5,6))
			,new Euler76Func(19,List(2,7))
			,new Euler76Func(20,List(6,6))
			,new Euler76Func(20,List(3,7))
			,new Euler76Func(21,List(4,7))
			,new Euler76Func(21,List(1,8))
			,new Euler76Func(22,List(5,7))
			,new Euler76Func(22,List(2,8))
					)
					val l3 = ref.map(_.f(n)).filter(_>0)
					sum=l3.sum
					//			println("                 "+num+" - "+l3+" "+sum)
					var lse = lsi
					ref.foreach((f: Euler76Func) => {
						val sref = f.l.toString.dropRight(1)
								val l = lsi.filter((li: List[Int]) => {
									//						  println(sref+" **** "+li.toString+" "+li.toString.indexOf(sref))
									li.toString.indexOf(sref)==0
								})
								lse = lse filterNot( l contains)
								if(l.size > 0 | f.f(n) > 0) {
									//				println("       "+n+" "+num+" "+l+" "+l.size+" "+f.f(n))
								}
					})
					//		println("       "+n+" "+num+" reste: "+lse)
		}
		case 5 => {
			val ref = List(new Euler76Func(3,0,List(1,1,1)),
					new Euler76Func(4,List(1,1,2))
			,new Euler76Func(5,List(1,2,2)),
			new Euler76Func(6,List(2,2,2)),
			new Euler76Func(7,List(1,1,3)),
			new Euler76Func(8,List(1,2,3)),
			new Euler76Func(9,List(1,3,3)),
			new Euler76Func(9,List(2,2,3)),
			new Euler76Func(10,List(1,1,4)),
			new Euler76Func(10,List(2,3,3)),
			new Euler76Func(11,List(1,2,4))
			,new Euler76Func(11,List(3,3,3))
					)
					val l3 = ref.map(_.f(n)).filter(_>0)
					sum=l3.sum
					println("                 "+num+" - "+l3+" "+sum)
			var lse = lsi
			ref.foreach((f: Euler76Func) => {
				val sref = f.l.toString.dropRight(1)
						val l = lsi.filter((li: List[Int]) => {
							//						  println(sref+" **** "+li.toString+" "+li.toString.indexOf(sref))
							li.toString.indexOf(sref)==0
						})
						lse = lse  filterNot(  l contains)
						if(l.size > 0 | f.f(n) > 0) {
							println("       "+n+" "+num+" "+l+" "+l.size+" "+f.f(n))
						}
			})
			println("       "+n+" "+num+" reste: "+lse)
		}
		case _ => 0
		}
		if(sum!=0) {
			//		require(sum==ts.filter(_.size==num).size,sum+"==+ts.filter(_.size=="+num+").size / "+ts.filter(_.size==num).size)
			if(sum!=ts.filter(_.size==num).size) {
				println("%%%%%%%%%%%%%%%% "+sum+"==+ts.filter(_.size=="+num+").size / "+ts.filter(_.size==num).size)
			}
		}
		sum
	}
}

class Euler76Func(val i: Int, val j: Int, val l: List[Int]) {
	def this(i: Int, l: List[Int]) = this(i,1,l)
			override def toString: String = "[n-"+i+"/2-"+j+" & "+l.mkString(",")+"]"
			def f(n: Int): Int = ((n-i)/2)-j
}



/*
[36mRun starting. Expected test count is: 1[0m
[32mEuler:[0m
 ****avant****
2 1
    TreeSet()
    TreeSet(List(1, 1))
3 2
  List(1, 1)
  List(0)
    TreeSet(List(2, 1), List(1, 1, 1))
4 4
  List(2, 1, 1)
  List(1, 0)
    TreeSet(List(3, 1), List(2, 2), List(2, 1, 1), List(1, 1, 1, 1))
5 6
  List(2, 2, 1, 1)
  List(0, 1, 0)
    TreeSet(List(4, 1), List(2, 3), List(3, 1, 1), List(2, 2, 1), List(2, 1, 1, 1), List(1, 1, 1, 1, 1))
6 10
  List(3, 3, 2, 1, 1)
  List(0, 1, 1, 0)
    TreeSet(List(5, 1), List(2, 4), List(3, 3), List(4, 1, 1), List(2, 3, 1), List(2, 2, 2), List(3, 1, 1, 1), List(2, 2, 1, 1), List(2, 1, 1, 1, 1), List(1, 1, 1, 1, 1, 1))
7 14
  List(3, 4, 3, 2, 1, 1)
  List(-1, 1, 1, 1, 0)
    TreeSet(List(6, 1), List(2, 5), List(3, 4), List(5, 1, 1), List(2, 4, 1), List(3, 3, 1), List(2, 2, 3), List(4, 1, 1, 1), List(2, 3, 1, 1), List(2, 2, 2, 1), List(3, 1, 1, 1, 1), List(2, 2, 1, 1, 1), List(2, 1, 1, 1, 1, 1), List(1, 1, 1, 1, 1, 1, 1))
8 21
  List(4, 5, 5, 3, 2, 1, 1)
  List(-1, 0, 2, 1, 1, 0)
    TreeSet(List(7, 1), List(2, 6), List(3, 5), List(4, 4), List(6, 1, 1), List(2, 5, 1), List(3, 4, 1), List(2, 2, 4), List(2, 3, 3), List(5, 1, 1, 1), List(2, 4, 1, 1), List(3, 3, 1, 1), List(2, 2, 3, 1), List(2, 2, 2, 2), List(4, 1, 1, 1, 1), List(2, 3, 1, 1, 1), List(2, 2, 2, 1, 1), List(3, 1, 1, 1, 1, 1), List(2, 2, 1, 1, 1, 1), List(2, 1, 1, 1, 1, 1, 1), List(1, 1, 1, 1, 1, 1, 1, 1))
9 29
  List(4, 7, 6, 5, 3, 2, 1, 1)
  List(-3, 1, 1, 2, 1, 1, 0)
    TreeSet(List(8, 1), List(2, 7), List(3, 6), List(4, 5), List(7, 1, 1), List(2, 6, 1), List(3, 5, 1), List(4, 4, 1), List(2, 2, 5), List(2, 3, 4), List(3, 3, 3), List(6, 1, 1, 1), List(2, 5, 1, 1), List(3, 4, 1, 1), List(2, 2, 4, 1), List(2, 3, 3, 1), List(2, 2, 2, 3), List(5, 1, 1, 1, 1), List(2, 4, 1, 1, 1), List(3, 3, 1, 1, 1), List(2, 2, 3, 1, 1), List(2, 2, 2, 2, 1), List(4, 1, 1, 1, 1, 1), List(2, 3, 1, 1, 1, 1), List(2, 2, 2, 1, 1, 1), List(3, 1, 1, 1, 1, 1, 1), List(2, 2, 1, 1, 1, 1, 1), List(2, 1, 1, 1, 1, 1, 1, 1), List(1, 1, 1, 1, 1, 1, 1, 1, 1))
10 41
  List(5, 8, 9, 7, 5, 3, 2, 1, 1)
  List(-3, -1, 2, 2, 2, 1, 1, 0)
    TreeSet(List(9, 1), List(2, 8), List(3, 7), List(4, 6), List(5, 5), List(8, 1, 1), List(2, 7, 1), List(3, 6, 1), List(4, 5, 1), List(2, 2, 6), List(2, 3, 5), List(2, 4, 4), List(3, 3, 4), List(7, 1, 1, 1), List(2, 6, 1, 1), List(3, 5, 1, 1), List(4, 4, 1, 1), List(2, 2, 5, 1), List(2, 3, 4, 1), List(3, 3, 3, 1), List(2, 2, 2, 4), List(2, 2, 3, 3), List(6, 1, 1, 1, 1), List(2, 5, 1, 1, 1), List(3, 4, 1, 1, 1), List(2, 2, 4, 1, 1), List(2, 3, 3, 1, 1), List(2, 2, 2, 3, 1), List(2, 2, 2, 2, 2), List(5, 1, 1, 1, 1, 1), List(2, 4, 1, 1, 1, 1), List(3, 3, 1, 1, 1, 1), List(2, 2, 3, 1, 1, 1), List(2, 2, 2, 2, 1, 1), List(4, 1, 1, 1, 1, 1, 1), List(2, 3, 1, 1, 1, 1, 1), List(2, 2, 2, 1, 1, 1, 1), List(3, 1, 1, 1, 1, 1, 1, 1), List(2, 2, 1, 1, 1, 1, 1, 1), List(2, 1, 1, 1, 1, 1, 1, 1, 1), List(1, 1, 1, 1, 1, 1, 1, 1, 1, 1))
11 55
  List(5, 10, 11, 10, 7, 5, 3, 2, 1, 1)
  List(-5, -1, 1, 3, 2, 2, 1, 1, 0)
12 76
  List(6, 12, 15, 13, 11, 7, 5, 3, 2, 1, 1)
  List(-6, -3, 2, 2, 4, 2, 2, 1, 1, 0)
13 100
  List(6, 14, 18, 18, 14, 11, 7, 5, 3, 2, 1, 1)
  List(-8, -4, 0, 4, 3, 4, 2, 2, 1, 1, 0)
14 134
  List(7, 16, 23, 23, 20, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-9, -7, 0, 3, 5, 4, 4, 2, 2, 1, 1, 0)
15 175
  List(7, 19, 27, 30, 26, 21, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-12, -8, -3, 4, 5, 6, 4, 4, 2, 2, 1, 1, 0)
16 230
  List(8, 21, 34, 37, 35, 28, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-13, -13, -3, 2, 7, 6, 7, 4, 4, 2, 2, 1, 1, 0)
17 296
  List(8, 24, 39, 47, 44, 38, 29, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-16, -15, -8, 3, 6, 9, 7, 7, 4, 4, 2, 2, 1, 1, 0)
18 384
  List(9, 27, 47, 57, 58, 49, 40, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-18, -20, -10, -1, 9, 9, 10, 8, 7, 4, 4, 2, 2, 1, 1, 0)
19 489
  List(9, 30, 54, 70, 71, 65, 52, 41, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-21, -24, -16, -1, 6, 13, 11, 11, 8, 7, 4, 4, 2, 2, 1, 1, 0)
20 626
  List(10, 33, 64, 84, 90, 82, 70, 54, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-23, -31, -20, -6, 8, 12, 16, 12, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
21 791
  List(10, 37, 72, 101, 110, 105, 89, 73, 55, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-27, -35, -29, -9, 5, 16, 16, 18, 13, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
22 1001
  List(11, 40, 84, 119, 136, 131, 116, 94, 75, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-29, -44, -35, -17, 5, 15, 22, 19, 19, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
23 1254
  List(11, 44, 94, 141, 163, 164, 146, 123, 97, 76, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-33, -50, -47, -22, -1, 18, 23, 26, 21, 20, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
24 1574
  List(12, 48, 108, 164, 199, 201, 186, 157, 128, 99, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-36, -60, -56, -35, -2, 15, 29, 29, 29, 22, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
25 1957
  List(12, 52, 120, 192, 235, 248, 230, 201, 164, 131, 100, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-40, -68, -72, -43, -13, 18, 29, 37, 33, 31, 23, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
26 2435
  List(13, 56, 136, 221, 282, 300, 288, 252, 212, 169, 133, 101, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-43, -80, -85, -61, -18, 12, 36, 40, 43, 36, 32, 24, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
27 3009
  List(13, 61, 150, 255, 331, 364, 352, 318, 267, 219, 172, 134, 101, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-48, -89, -105, -76, -33, 12, 34, 51, 48, 47, 38, 33, 24, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
28 3717
  List(14, 65, 169, 291, 391, 436, 434, 393, 340, 278, 224, 174, 135, 101, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-51, -104, -122, -100, -45, 2, 41, 53, 62, 54, 50, 39, 34, 24, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
29 4564
  List(14, 70, 185, 333, 454, 522, 525, 488, 423, 355, 285, 227, 175, 135, 101, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-56, -115, -148, -121, -68, -3, 37, 65, 68, 70, 58, 52, 40, 34, 24, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
30 5603
  List(15, 75, 206, 377, 532, 618, 638, 598, 530, 445, 366, 290, 229, 176, 135, 101, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-60, -131, -171, -155, -86, -20, 40, 68, 85, 79, 76, 61, 53, 41, 34, 24, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
31 6841
  List(15, 80, 225, 427, 612, 733, 764, 732, 653, 560, 460, 373, 293, 230, 176, 135, 101, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-65, -145, -202, -185, -121, -31, 32, 79, 93, 100, 87, 80, 63, 54, 41, 34, 24, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
32 8348
  List(16, 85, 249, 480, 709, 860, 919, 887, 807, 695, 582, 471, 378, 295, 231, 176, 135, 101, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-69, -164, -231, -229, -151, -59, 32, 80, 112, 113, 111, 93, 83, 64, 55, 41, 34, 24, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
33 10142
  List(16, 91, 270, 540, 811, 1009, 1090, 1076, 984, 863, 725, 597, 478, 381, 296, 231, 176, 135, 101, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-75, -179, -270, -271, -198, -81, 14, 92, 121, 138, 128, 119, 97, 85, 65, 55, 41, 34, 24, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
34 12309
  List(17, 96, 297, 603, 931, 1175, 1297, 1291, 1204, 1060, 905, 747, 608, 483, 383, 297, 231, 176, 135, 101, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-79, -201, -306, -328, -244, -122, 6, 87, 144, 155, 158, 139, 125, 100, 86, 66, 55, 41, 34, 24, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
35 14882
  List(17, 102, 321, 674, 1057, 1367, 1527, 1549, 1455, 1303, 1116, 935, 762, 615, 486, 384, 297, 231, 176, 135, 101, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-85, -219, -353, -383, -310, -160, -22, 94, 152, 187, 181, 173, 147, 129, 102, 87, 66, 55, 41, 34, 24, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
36 17976
  List(18, 108, 351, 748, 1206, 1579, 1801, 1845, 1761, 1586, 1380, 1158, 957, 773, 620, 488, 385, 297, 231, 176, 135, 101, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-90, -243, -397, -458, -373, -222, -44, 84, 175, 206, 222, 201, 184, 153, 132, 103, 88, 66, 55, 41, 34, 24, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
37 21636
  List(18, 114, 378, 831, 1360, 1824, 2104, 2194, 2112, 1930, 1686, 1436, 1188, 972, 780, 623, 489, 385, 297, 231, 176, 135, 101, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-96, -264, -453, -529, -464, -280, -90, 82, 182, 244, 250, 248, 216, 192, 157, 134, 104, 88, 66, 55, 41, 34, 24, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
38 26014
  List(19, 120, 411, 918, 1540, 2093, 2462, 2592, 2534, 2331, 2063, 1763, 1478, 1210, 983, 785, 625, 490, 385, 297, 231, 176, 135, 101, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-101, -291, -507, -622, -553, -369, -130, 58, 203, 268, 300, 285, 268, 227, 198, 160, 135, 105, 88, 66, 55, 41, 34, 24, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
39 31184
  List(19, 127, 441, 1014, 1729, 2400, 2857, 3060, 3015, 2812, 2503, 2164, 1819, 1508, 1225, 990, 788, 626, 490, 385, 297, 231, 176, 135, 101, 77, 56, 42, 30, 22, 15, 11, 7, 5, 3, 2, 1, 1)
  List(-108, -314, -573, -715, -671, -457, -203, 45, 203, 309, 339, 345, 311, 283, 235, 202, 162, 136, 105, 88, 66, 55, 41, 34, 24, 21, 14, 12, 8, 7, 4, 4, 2, 2, 1, 1, 0)
 */

class Euler91(val n: Int) {
	val range = new Range(0,n+1,1).toList
			var triangles = ListSet[GeomTriangle]()

			range.foreach((x1: Int) => {
				range.foreach((y1: Int) => {
					range.foreach((x2: Int) => {
						range.foreach((y2: Int) => {
							val shape = new GeomShape(ListSet(new XY(0,0), new XY(x1,y1), new XY(x2,y2)))
							if(shape.isTriangle) {
								triangles = triangles + new GeomTriangle(shape)
							}
						})
					})
				})
			})

			val rightTriangles = triangles.filter(_.hasRightAngle).toList.sortBy{_.toString}



	println("********** "+n+" ********************")

	/*	println("\n"+rightTriangles.size+" eq: "+rightTriangles.filter(_.isEquiLateral).size+
			" eq00: "+rightTriangles.filter(_.isEquiLateral).filter(_.is00RightAngle).size+
			" eqhv: "+rightTriangles.filter(_.isEquiLateral).filter(_.isVertHoriz).size+
			" 00: "+rightTriangles.filter(!_.isEquiLateral).filter(_.is00RightAngle).size)*/

	val nohv = rightTriangles.filter(!_.isVertHoriz)
	val hv = rightTriangles filterNot( nohv contains)
	println("  getDeg45Triangles : "+getDeg45Triangles(n))
	require(hv.size==hvSize)
	val nohvs = nohv.groupBy{_.lpoints.tail.head.x}.toList.sortBy{_._1}.foreach((c: (Double,List[GeomTriangle])) => mprint(c._2))
	//	mprint(nohv)
	Euler.timeStamp(Euler.t_start, "Euler91_"+n)

	def mprint(l: List[GeomTriangle]) = {
		println("\n"+l.size+"\n "+l.mkString("\n "))
	}

	def hvSize: BigInt = (n*3) + ((n*(n-1))*3)
			def getDeg45Triangles(n: Int): Int = {
			if(n<=1) {
				0
			} else {
				val delta = (n/2)*2
						getDeg45Triangles(n-1)+delta
			}
		}
}

class Bouncy112(val s: String, val level: Int) {

	def this(s: String)=this(s,0)
			def this(bi: BigInt)=this(bi.toString)

			var cptb = BigInt(0)
			var nexts = ListSet[Bouncy112]()
			val bi = BigInt.apply(s)
			val lastDigit = bi%10
			val l = s.toList.map(_.toString.toInt)
			val z = l.sliding(2).toList
			val g = z.filter((c: List[Int]) => c.head>c.last).isEmpty
			val d = z.filter((c: List[Int]) => c.head<c.last).isEmpty
			val b = (!g)&(!d)

			override def toString: String = {
					var offset = (0 to level).toList.flatMap((i: Int) => "  ").mkString("")
							var result = "["+s+" "
							if(g) {
								result += "g"
							}
					if(d) {
						result += "d"
					}
					if(b) {
						result += "b"
					}
					val z = nexts.toList.map(_.toString)
							if(z.isEmpty) {
								result + " "+cptb+ "]\n"					  
							} else {
								result + " "+cptb+" "+z.mkString("\n"+offset,offset,"")+ "]\n"
							}
					//result + " "+cptb+" "+nexts.toList.map(_.toString).mkString(offset,offset,offset)+ "]\n"
			}

			def toString2: String = {
					var offset = (0 to level).toList.flatMap((i: Int) => "  ").mkString("")
							var result = "["+s+" "
							if(g) {
								result += "g"
							}
					if(d) {
						result += "d"
					}
					if(b) {
						result += "b"
					}
					result+ " "+cptb + "]"
					//result + " "+cptb+" "+nexts.toList.map(_.toString).mkString(offset,offset,offset)+ "]\n"
			}

			def getNexts: List[Bouncy112] = nexts.toList ++ nexts.toList.flatMap(_.getNexts)

					def getCptb: BigInt = getNexts.map(_.cptb).sum + cptb

					def updateNexts= {
				cptb = cptb*10
						if((cptb==0)&(nexts.isEmpty)) {
							if(g) {
								nexts = nexts ++ (lastDigit to 9).map((bi2: BigInt) => new Bouncy112(bi.toString+bi2.toString, level+1))
							} 
							if(d) {
								nexts = nexts ++ (BigInt(0) to lastDigit).map((bi2: BigInt) => new Bouncy112(bi.toString+bi2.toString, level+1))
							}
							cptb = 10 - nexts.size
						}
			}


			override def equals(x: Any): Boolean = hashCode.equals(x.asInstanceOf[Bouncy112].hashCode)

					override def hashCode: Int = s.hashCode

}


object Euler153bis {
	val limit = 1001
			val premiers = EulerPrime.premiers100000.toList ++ List(BigInt(1))
			val sqp = new Range(1,sqrt(limit).toInt+1,1).map((p: Int) => BigInt(p)).map((p: BigInt) =>p*p).toList
			println(sqp.size+" "+sqp)
	val sumsq = ((sqp ++ sqp).combinations(2).map(new Euler153(_)).toList.sortBy{_.s}.reverse).filter(_.s<limit).distinct
	println(sumsq.size+" "+sumsq)
	val sumsqp = (sumsq.filter((e: Euler153) => EulerPrime.isPrime(e.s)).toList.sortBy{_.s}.reverse).distinct
	println(sumsqp.size+" "+sumsqp)
	println(sumsqp.size+"\n  "+sumsqp.map(_.toString3).mkString("\n  "))
	val primeEuler153 = premiers.map((p: BigInt) => new Euler153(List(p,0))) :+ new Euler153(List(1,0))
	println("primeEuler153: "+primeEuler153)
}


class Euler153(l: List[BigInt]) {
	val s = (l.head+l.last)
			val u = BigInt.apply(round(sqrt(l.head.toDouble)).toString)
			val v = BigInt.apply(round(sqrt(l.last.toDouble)).toString)
			var lidiv = ListSet(new ImBi(1,0),new ImBi(s,0),new ImBi(u,v),new ImBi(v,u),new ImBi(u,-v),new ImBi(v,-u))

			override def toString: String = "["+l.head+"+"+l.last+" "+s+"]"
			def toString2: String = s.toString
			def toString3: String = toString+" "+lidiv
			override def hashCode: Int = toString2.hashCode
			override def equals(o: Any): Boolean = toString2 == o.asInstanceOf[Euler153].toString2
}





object Euler185 {
	val rien = 99
			val nogood = 88
			val combi5_2 = new Range(0,5,1).toList.combinations(2).toList
			val combi5_3 = new Range(0,5,1).toList.combinations(3).toList
			val combi16_2 = new Range(0,16,1).toList.combinations(2).toList
			val combi16_3 = new Range(0,16,1).toList.combinations(3).toList
			var cpt = BigInt(0)
}

class Euler185_5(li: List[(BigInt,Int)]) extends Euler185(li) {
	override val l = li.sortBy{_._2}.reverse.map(new Euler185Set5(_))
			val zeros = l.filter(_.reponse==0)
			val uns = l.filter(_.reponse==1)
			override val deux = l.filter(_.reponse>=2)
			println(l)

			val zerosYesNo = zeros.flatMap(_.getPossible)

			val unsYesNo = uns.flatMap(_.getPossible)

			override val unsFiltered0 = uns.flatMap(_.filter0(zerosYesNo))

			val deuxFiltered0 = deux.flatMap(_.filter0(zerosYesNo))

			val deuxFiltered01grp = deux.map(_.filter1(unsFiltered0))

			println("deuxYesNos:\n  "+deux.map(_.yesno).mkString("\n  "))
			deuxSorted = deux.sortBy{_.yesno.size}
	println("deux: "+deux)
	println("deuxSorted: "+deuxSorted)

	val lnozeros = (l  filterNot(  zeros contains)).sortBy{_.yesno.size}
	val lnounszeros = (lnozeros  filterNot(  uns contains)).sortBy{_.yesno.size}
	var ll = lnozeros.head.plus
			lnozeros.tail.foreach((eus: Euler185Set) => ll = eus + ll)
			//			println("ll:\n  "+ll.mkString("\n  "))

			lnounszeros.foreach((eus: Euler185Set) => {
				eus.yesnoz = eus.yesno
						eus.X(lnounszeros)
						println(eus+" yn.sz: "+eus.yesno.size+" ynz.sz: "+eus.yesnoz.size)
						println("     compat2a2z: "+eus.compat2a2z)
			})
			println("deuxYesNoz:\n  "+deux.map(_.yesnoz).mkString("\n  "))
			compat2a2z = ListSet[Euler185YesNo]() ++ lnounszeros.flatMap(_.compat2a2z)
			println("compat2a2z:\n  "+compat2a2z.mkString("\n  "))
			deuxSorted = deux.sortBy{_.yesnoz.size}
}

class Euler185_16(li: List[(BigInt,Int)]) extends Euler185(li) {
	override val l = li.sortBy{_._2}.reverse.map(new Euler185Set16(_))
			val zeros = l.filter(_.reponse==0)
			val uns = l.filter(_.reponse==1)
			override val deux = l.filter(_.reponse>=2)
			println(l)

			val zerosYesNo = zeros.flatMap(_.getPossible)

			val unsYesNo = uns.flatMap(_.getPossible)

			override val unsFiltered0 = uns.flatMap(_.filter0(zerosYesNo))

			val deuxFiltered0 = deux.flatMap(_.filter0(zerosYesNo))

			val deuxFiltered01grp = deux.map(_.filter1(unsFiltered0))

			println("deuxYesNos:\n  "+deux.map((eus: Euler185Set) =>  eus.yesno.size+" "+eus.yesno).mkString("\n  "))

			println("deux: "+deux)
			println("deuxSorted: "+deuxSorted)

			val lnozeros = (l  filterNot(  zeros contains)).sortBy{_.yesno.size}
	val lnounszeros = (lnozeros  filterNot(  uns contains)).sortBy{_.yesno.size}
	var ll = lnozeros.head.plus
			lnozeros.tail.foreach((eus: Euler185Set) => ll = eus + ll)
			//			println("ll:\n  "+ll.mkString("\n  "))

			lnounszeros.foreach((eus: Euler185Set) => {
				eus.yesnoz = eus.yesno
						eus.X(lnounszeros)
						println(eus+" "+eus.yesno.size+" "+eus.yesnoz.size)
			})
			compat2a2z = ListSet[Euler185YesNo]() ++ lnounszeros.flatMap(_.compat2a2z)
			deuxSorted = deux.sortBy{_.yesnoz.size}
	println("compat2a2z:\n  "+compat2a2z.mkString("\n  "))

}

class Euler185(val li: List[(BigInt,Int)]) {
	val l =  List[euler.Euler185Set]()
			val deux = List[euler.Euler185Set]()
			val unsFiltered0 = List[euler.Euler185YesNo]()
			var deuxSorted = List[euler.Euler185Set]()
			var compat2a2z = ListSet[Euler185YesNo]()

			def getDeuxAdeux2(couple: (List[Euler185Set], List[Euler185YesNo])): (List[Euler185Set], List[Euler185YesNo]) = {
		var deuxAdeux = List[Euler185YesNo]()
				var deuxAdeuxTmp = List[Euler185YesNo]()
				val reste = couple._1.tail
				if(!reste.isEmpty) {
					val eus2 = reste.head
							couple._2.foreach((euc1: Euler185YesNo) => {
								eus2.yesnoz.foreach((euc2: Euler185YesNo) => deuxAdeuxTmp = deuxAdeuxTmp ++ (euc1 & euc2))
								if(deuxAdeuxTmp.size>10000) {
									println("  euc1: "+euc1+" "+euc1.getYesNumber+" & eus2: "+eus2+"\n    "+getRemaining(eus2)+" "+deuxAdeux.size+" "+deuxAdeuxTmp.size)
									Euler.timeStamp(Euler.t_start, "getDeuxAdeux2_")
									println(deuxSorted.map(_.toString2).mkString("\n"))
									deuxAdeux = deuxAdeux ++ filterz(filter01(deuxAdeuxTmp))
									deuxAdeuxTmp = List[Euler185YesNo]()
								}
							})
							deuxAdeux = deuxAdeux ++ filterz(filter01(deuxAdeuxTmp))
							eus2.deuxAdeuxSize = deuxAdeux.size
							eus2.time = Euler.timeStamp(Euler.t_start, "getDeuxAdeux2")
							eus2.cpt = Euler185.cpt
							println("eus2: "+eus2+" "+deuxAdeux.size)
				} else {
					deuxAdeux = couple._2
				}
		//		(reste,(ListSet[Euler185YesNo]()++deuxAdeux).toList.sortBy{_.toString})
		(reste,deuxAdeux.sortBy{_.toString})
	}

	def getRemaining(eus: Euler185Set): Int = deuxSorted.reverse.takeWhile(_.combi != eus.combi).map(_.reponse).sum

			def getDeuxAdeux1: List[Euler185YesNo] = {
			Euler185.cpt = 0
					var couple = (deuxSorted.asInstanceOf[List[euler.Euler185Set]],deuxSorted.head.yesnoz)
					while(!couple._1.isEmpty) {
						couple = getDeuxAdeux2(couple)
					}
			//			require(!couple._2.tail.contains(couple._2.head),couple._2.tail +" contains "+couple._2.head)
			println("couple:\n  "+couple)
			whenCompleteFilterNumberOfDigitsVsOnes(couple._2)
	}

	def whenCompleteFilterNumberOfDigitsVsOnes(yesOnly: List[Euler185YesNo]): List[Euler185YesNo] = {
			yesOnly.filter((euc: Euler185YesNo) => {
				l.filter((eus: Euler185Set) => {
					euc.finalChk(eus)
				})==l
			})
	}

	def filter01(li: List[Euler185YesNo]): List[Euler185YesNo] = li.filter((euc: Euler185YesNo) => unsFiltered0.filter((un: Euler185YesNo) => !(euc chk un)).isEmpty)

			def filterz(li: List[Euler185YesNo]): List[Euler185YesNo] = li.filter((euc: Euler185YesNo) => compat2a2z.filter((dadz: Euler185YesNo) => !(euc chk4 dadz)).isEmpty)
}

class Euler185Set5(combi: BigInt, reponse: Int) extends Euler185Set(combi, reponse) {
	def this(eus: Euler185Set5) = this(eus.combi, eus.reponse)
			def this(c: (BigInt,Int)) = this(c._1, c._2)


			def getPossible: List[Euler185YesNo] = {
					var l = List[Euler185YesNo]()
							reponse match {
							case 0 => l = List(new Euler185YesNo(ListSet(this), List[Int](),lcombi))
							case 1 => {
								var cpt = 0
										lcombi.foreach((i: Int) => {
											val ll = lcombi.splitAt(cpt)
													val avant = ll._1.map((j: Int) => Euler185.rien)
													val apres = ll._2.tail.map((j: Int) => Euler185.rien)
													l = l :+ new Euler185YesNo(ListSet(this), avant ++ List(i)++ apres,ll._1 ++ List(Euler185.rien) ++ ll._2.tail)
											cpt +=1
										})					  
							}
							case 2 => l = Euler185.combi5_2.filter((l: List[Int]) => !l.map((i: Int) => depart.apply(i)).contains(Euler185.rien)).map((l: List[Int]) => getYesNo(l))
							case 3 => l = Euler185.combi5_3.filter((l: List[Int]) => !l.map((i: Int) => depart.apply(i)).contains(Euler185.rien)).map((l: List[Int]) => getYesNo(l))
							case _ =>
					}
					yesno = l
							l
			}
}

class Euler185Set16(combi: BigInt, reponse: Int) extends Euler185Set(combi, reponse) {
	def this(eus: Euler185Set5) = this(eus.combi, eus.reponse)
			def this(c: (BigInt,Int)) = this(c._1, c._2)


			def getPossible: List[Euler185YesNo] = {
					var l = List[Euler185YesNo]()
							reponse match {
							case 0 => l = List(new Euler185YesNo(ListSet(this), List[Int](),lcombi))
							case 1 => {
								var cpt = 0
										lcombi.foreach((i: Int) => {
											val ll = lcombi.splitAt(cpt)
													val avant = ll._1.map((j: Int) => Euler185.rien)
													val apres = ll._2.tail.map((j: Int) => Euler185.rien)
													l = l :+ new Euler185YesNo(ListSet(this), avant ++ List(i)++ apres,ll._1 ++ List(Euler185.rien) ++ ll._2.tail)
											cpt +=1
										})					  
							}
							case 2 => l = Euler185.combi16_2.filter((l: List[Int]) => !l.map((i: Int) => depart.apply(i)).contains(Euler185.rien)).map((l: List[Int]) => getYesNo(l))
							case 3 => l = Euler185.combi16_3.filter((l: List[Int]) => !l.map((i: Int) => depart.apply(i)).contains(Euler185.rien)).map((l: List[Int]) => getYesNo(l))
							case _ =>
					}
					yesno = l
							l
			}
}

abstract class Euler185Set(val combi:BigInt, val reponse: Int) {
	val lcombi ="%d".format(combi).toList.map(_.toString.toInt)
			var depart = lcombi
			var yesno = List[Euler185YesNo]()
			var yesnoz = List[Euler185YesNo]()
			var deuxAdeuxSize = 0
			var cpt = BigInt(0)
			var time = Calendar.getInstance()
			var compat2a2z = List[Euler185YesNo]()

			def getPossible: List[Euler185YesNo]

					override def hashCode: Int = toString2.hashCode
					override def equals(o: Any): Boolean = toString2 == o.asInstanceOf[Euler185Set].toString2

					def getYesNo(l: List[Int]): Euler185YesNo = {
					var yes = List[Int]()
							var no = List[Int]()
							new Range(0,depart.size,1).toList.foreach((i: Int) => {
								if(l.contains(i)) {
									yes = yes :+ depart.apply(i)
											no = no :+ Euler185.rien
								} else {
									yes = yes :+ Euler185.rien
											no = no :+ depart.apply(i)      
								}
							})
							new Euler185YesNo(ListSet(this), yes, no)
			}

			def filter1(uns: List[Euler185YesNo]): List[Euler185YesNo] = {
					var l = List[Euler185YesNo]()
							reponse match {
							case 2 => 
							l = yesno.filter((euc2: Euler185YesNo) => {
								uns.filter((euc1: Euler185YesNo) => {
									//				  println("   euc2: "+euc2+", euc1: "+euc1+":::: "+euc1.no.zip(euc2.yes)+" - "+euc1.no.zip(euc2.yes).filter((c: (Int,Int)) => c._1==c._2).filter(_._1!=Euler185.rien)+" "+
									//				      (euc1.no.zip(euc2.yes).filter((c: (Int,Int)) => c._1==c._2).filter(_._1!=Euler185.rien).size!=2))
									euc1.no.zip(euc2.yes).filter((c: (Int,Int)) => c._1==c._2).filter(_._1!=Euler185.rien).size==2
								}).isEmpty
							})
							case 3 => 
							l = yesno.filter((euc2: Euler185YesNo) => {
								uns.filter((euc1: Euler185YesNo) => {
									//				  println("   euc2: "+euc2+", euc1: "+euc1+":::: "+euc1.no.zip(euc2.yes)+" - "+euc1.no.zip(euc2.yes).filter((c: (Int,Int)) => c._1==c._2).filter(_._1!=Euler185.rien)+" "+
									//				      (euc1.no.zip(euc2.yes).filter((c: (Int,Int)) => c._1==c._2).filter(_._1!=Euler185.rien).size!=2))
									euc1.no.zip(euc2.yes).filter((c: (Int,Int)) => c._1==c._2).filter(_._1!=Euler185.rien).size==3
								}).isEmpty
							})
							case _ => 
					}
					yesno = l
							l			  
			}

			def filter0(zeros: List[Euler185YesNo]): List[Euler185YesNo] = {
					var l = List[Euler185YesNo]()
							reponse match {
							case 1 => l = yesno.filter((euc1: Euler185YesNo) => {
								zeros.filter((euc0: Euler185YesNo) => {
									!euc0.no.zip(euc1.yes).filter((c: (Int,Int)) => c._1==c._2).isEmpty
								}).isEmpty
							})

							case 2 => 
							zeros.foreach((euc: Euler185YesNo) => {
								depart = getFilteredZero(depart, euc)
										println("  ("+this+").getFilteredZero(depart, "+euc+"): "+depart)
							})
							l =	getPossible

							case 3 => 
							zeros.foreach((euc: Euler185YesNo) => {
								depart = getFilteredZero(depart, euc)
										println("  ("+this+").getFilteredZero(depart, "+euc+"): "+depart)
							})
							l =	getPossible

							case _ => 
					}
					yesno = l
							l
			}

			def filterz2(li: List[Euler185YesNo], lf: List[Euler185YesNo]): List[Euler185YesNo] = {
					li.filter((euc: Euler185YesNo) => lf.filter ((un: Euler185YesNo) => ! (euc chk4 un)).isEmpty)
			}

			def filterz(lf: List[Euler185YesNo]): List[Euler185YesNo] = {
					yesnoz = yesnoz.filter((euc: Euler185YesNo) => lf.filter ((un: Euler185YesNo) => ! (euc chk4 un)).isEmpty)
							yesnoz
			}



			def getFilteredZero(li: List[Int], zero: Euler185YesNo): List[Int] = {
					li.zip(zero.no).map((c: (Int,Int))=> {
						if(c._1==c._2) {
							Euler185.rien
						} else {
							c._1
						}
					})
			}

			def X(eus: Euler185Set): List[Euler185YesNo] = {
					// TODO
					if(eus!=this) {
						val couple = depart.zip(eus.depart).map((c: (Int,Int))=> {
							if(c._2==c._1) {
								c._1
							} else {
								Euler185.rien
							}
						})
						val euc = new Euler185YesNo(ListSet(this,eus), couple,List[Int]())
						println(this+" "+eus+"   "+euc+" "+euc.getYesNumber+" reponse: "+ reponse + " eus.reponse: "+ eus.reponse)
						if(euc.getYesNumber!=0) {
							if(euc.getYesNumber>Math.max(reponse,eus.reponse)) {
								val min = Math.min(reponse,eus.reponse)
										val lcombis = new Range(0,euc.getYesNumber,1).toList.combinations(min).toList
										var lcombis2 = List[Int]()
										var cpt = 0
										couple.foreach((i: Int) => {
											if(i!=Euler185.rien) {
												lcombis2 = lcombis2 :+ cpt									    
											}
											cpt += 1
										})
										var toxor = List[List[Int]]()
										lcombis.foreach((lu: List[Int]) => {
											val lcombis3 = lu.map((i: Int) => lcombis2.apply(i))
													cpt = 0
													toxor = toxor :+ lcombi.map((i: Int) => {
														var mapped = Euler185.rien
																if(lcombis3.indexOf(cpt)>=0) {
																	mapped = i
																}
														cpt += 1
																mapped
													})
										})
										val leuc = toxor.map((lv: List[Int]) => {
											val eucx = new Euler185YesNo(ListSet(this,eus), lv,List[Int]())
											eucx.xor(euc)
											eucx
										})
										leuc
							} else if(euc.getYesNumber==Math.max(reponse,eus.reponse)) {
								euc.xor(this)
								euc.xor(eus)								
								List(euc)
							} else {
								List[Euler185YesNo]()
							}
						} else {
							List[Euler185YesNo]()
						}
					} else {
						List[Euler185YesNo]()
					}
			}

			def X(l: List[Euler185Set]): List[Euler185YesNo] = l.flatMap((eus: Euler185Set) => {
				val XZ = X(eus)
						compat2a2z = compat2a2z ++ XZ
						filterz(XZ)	
			})

			def plus: List[ListSet[Int]] = {
					lcombi.map((i: Int)=> ListSet(i))
			}

			def +(ll: List[ListSet[Int]]): List[ListSet[Int]] = {
					var cpt = 0
							var lo = List[ListSet[Int]]()
							ll.foreach((l: ListSet[Int]) => {
								val nl = (l + lcombi.apply(cpt)).toList.sorted
										lo = lo :+ (ListSet[Int]() ++nl)
										cpt += 1
							})
							lo
			}

			def &(eus: Euler185Set): List[Euler185YesNo] = {
					var l = List[Euler185YesNo]()
							reponse match {
							case 2 => eus.reponse match {
							case 2 => l = yesnoz.flatMap((euc: Euler185YesNo) => {
								eus.yesnoz.flatMap((euc2: Euler185YesNo) => {
									euc & euc2
								})
							})
							case _ => 
							}
							case _ => 
					}
					l
			}

			override def toString: String = combi +"; "+reponse
					def toString2: String = combi +"; "+reponse+" dadsz: "+deuxAdeuxSize+" cpt: "+cpt+" t: "+Euler.printZisday(time,"HH_mm_ss_SSS")
}

class Euler185YesNo(val eusl: ListSet[Euler185Set],val yes: List[Int], val no: List[Int]) {
	var lnos = ListSet(no)
			override def toString: String = "!y: "+yes.map(print(_)).mkString("") +", n: "+lnos.map(_.map(print(_)).mkString("")).mkString("; ")+"!"

			def print(i: Int): String = {
		if(i==Euler185.rien) {
			"_"
		} else if(i==Euler185.nogood) {
			"X"
		} else {
			i.toString
		}
	}

	override def hashCode: Int = toString.hashCode
			override def equals(o: Any): Boolean = toString == o.asInstanceOf[Euler185YesNo].toString
			def getYesNumber: Int = yes.filter(_ != Euler185.rien).size

			def finalChk(eus: Euler185Set): Boolean = {
				require(!yes.contains(Euler185.rien))
				eus.lcombi.zip(yes).filter((c: (Int,Int))=> (c._2==c._1)).size == eus.reponse
			}

			def chk(euc: Euler185YesNo): Boolean = {
				Euler185.cpt += 1
						var good = true
						if(!euc.yes.isEmpty) {
							if(yes.zip(euc.yes).exists((c: (Int,Int))=>((c._2==c._1)&(c._1!=Euler185.rien)))) {
								good = chk2(euc.lnos)
							}  
						} else {
							good = chk2(euc.lnos)
						}
				good
			}

			def chk4(euc: Euler185YesNo): Boolean = {
				var good = true
						if(!euc.yes.isEmpty) {
							if(yes.zip(euc.yes).filter((c: (Int,Int))=> ((c._2==c._1)&(c._1!=Euler185.rien))).size==euc.getYesNumber) {
								good = chk2(euc.lnos)
							}  
						} else {
							good = chk2(euc.lnos)
						}
				good
			}

			def chk(no2: List[Int]): Boolean = !yes.zip(no2).exists((c: (Int,Int))=> ((c._2==c._1)&(c._1!=Euler185.rien)))

					def chk2(lnos: ListSet[List[Int]]): Boolean = lnos.filter(!chk(_)).isEmpty


					def xor(eus: Euler185Set): Int = xor(eus.lcombi)

					def xor(euc: Euler185YesNo): Int = xor(euc.yes)

					def xor(lu: List[Int]): Int = {
				lnos = lnos + yes.zip(lu).map((c: (Int,Int))=> {
					if((c._1==Euler185.rien)) {
						c._2
					} else {
						Euler185.rien
					}
				})
				0
			}

			def &(euc: Euler185YesNo): List[Euler185YesNo] = {
				val yesyes = yes.zip(euc.yes).map((c: (Int,Int))=> {
					if(c._1==Euler185.rien) {
						c._2
					} else if(c._2==Euler185.rien) {
						c._1
					} else if(c._2==c._1) {
						c._1
					} else {
						Euler185.nogood
					}
				})
				if(yesyes.contains(Euler185.nogood)) {
					List[Euler185YesNo]()
				} else {
					val euco = new Euler185YesNo(eusl++euc.eusl, yesyes,List[Int]())
					if(euco.chk2(lnos++euc.lnos++List(no,euc.no))) {
						euco.lnos = (lnos++euc.lnos++List(no,euc.no)).filter(!_.isEmpty)
								List(euco)
					} else {
						List[Euler185YesNo]()
					}
				}
			}
}

class Euler286(q: Double) {
	val range = new Range(1,51,1).toList
			val p1 = new Function((it: Item) => new Item(1 -(it.i/q)),"1-x/q")
	val p0 = new Function((it: Item) => new Item(it.i/q),"x/q")
	val sigma = new Function((it: Item) => new Item(Math.sqrt((it.i/q)*(1 -(it.i/q)))),"sigma")


	def doIt: Double = {
			val result = range.map((x: Int) => p1.process(new Item(x)).d).sum
					println("f(x1-50, q:"+q+")= "+result)
			result
	}


}


