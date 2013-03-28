package euler

import scala.collection.immutable.ListSet
import language.postfixOps

class Euler108Func(val when: Int, val whenMeq: Int, val offset: Int, val adder: List[BigInt]) {
	val troisNMoinsUn = new FunctionEuler110((it: Item) => new Item(Euler110.troisNMoinsUn(it.i,when,whenMeq)),"3n-1[M1stPrimeNumbers++"+adder+"/"+adder.product+"]")
	val cinqNMoinsDeux = new FunctionEuler110((it: Item) => new Item(Euler110.cinqNMoinsDeux(it.i,when,whenMeq)),"5n-2["+adder+"/"+adder.product+"]")
	val range = new Range(when,when+20,1).toList
	println(troisNMoinsUn.table2String(range.map(new Item(_)),tostr))
	val solutions = troisNMoinsUn.table(range.map(new Item(_)))
	val intersect = Euler110.solutions.intersect(solutions.map((z: (Item, Item)) => BigInt(z._2.i)).toSet)
	if(!intersect.isEmpty) {
		println("       already exist! "+intersect)
	}
	val solutions5nm2 = cinqNMoinsDeux.table(solutions.map(_._1))
			val intersect5nm2 = Euler110.solutions.intersect(solutions.map((z: (Item, Item)) => BigInt(z._2.i)).toSet)
			if(!intersect5nm2.isEmpty) {
				println("       5n-2 already exist! "+intersect5nm2)
			}
	Euler110.solutions = Euler110.solutions ++ solutions.map((z: (Item, Item)) => BigInt(z._2.i))
			var cpt = 0
			range.takeWhile(doZeJob(_))
			println("       validated "+cpt+" times.")

			def doZeJob(i: Int): Boolean = {
		val premsprod = (EulerPrime.premiers1000.take(i).toList++adder).product
				val eu108 = new Euler108(premsprod)
		//println("eu108.toString: "+eu108.toString)
		if(eu108.results.size>0) {
			cpt += 1
					require(eu108.results.size==(troisNMoinsUn.process(new Item(i+offset))).i,getClass.getName+"("+when+","+whenMeq+","+offset+","+adder+") "+eu108.results.size+"== exp "+(troisNMoinsUn.process(new Item(i+offset))).i)
			eu108.results.size<4000
		} else {
			false
		}
	}

	def tostr(xy: (Item,Item)): String = xy._1.i+" => "+xy._2.i



}

class Euler108Func3(when: Int, whenMeq: Int, offset: Int, adder: List[BigInt]) extends Euler108Func(when,whenMeq,offset,adder){
	override def doZeJob(i: Int): Boolean = {
			val premsprod = (EulerPrime.premiers10000.take(i).toList++adder).product
					val eu108 = new Euler108Length3(premsprod)
			//		println(eu108.toString)
			if(eu108.length>0) {
				cpt += 1
						require(eu108.length==(troisNMoinsUn.process(new Item(i+offset))).i,getClass.getName+"("+when+","+whenMeq+","+offset+","+adder+") "+eu108.length+"== exp "+(troisNMoinsUn.process(new Item(i+offset))).i)
				eu108.length<5000000
			} else {
				false
			}
	}

}


class Euler108(val n: BigInt) {
	var results = List[(BigInt,BigInt)]()
			if(n<500000) {
				val range = new Range(n.toInt+1,(n.toInt*2)+1,1).toList.map(BigInt(_))

						range.foreach((x: BigInt) => {
							if(n*x%(n-x)==0) {
								results = results :+ (x,y(x))
										//					println("n: "+n+", x: "+x+", n*x: "+(n*x)+", x-n: "+(x-n)+", y: "+y(x)+" rs: "+results.size)
							} else {
								//				println("n: "+n+", x: "+x+", n*x: "+(n*x)+", x-n: "+(x-n))
							}
						})
						//println(toString)
						results.map(check(_))
			}

	def y(x: BigInt): BigInt = {
			require((n*x)%(n-x)==0,getClass.getName+"("+n+") n: "+n+"("+n+"*"+x+")%("+n+"-"+x+")=="+((n*x)%(n-x))+" vs 0")
			(n*x)/(x-n)
	}

	def check(x_y: (BigInt, BigInt)) = {
		require(Math.abs((1.0/x_y._1.toDouble)+(1.0/x_y._2.toDouble)-(1.0/n.toDouble))<0.00000000000001,"1/"+x_y._1+"+1/"+x_y._2+"=="+((1.0/x_y._1.toDouble)+(1.0/x_y._2.toDouble))+"vs "+(1.0/n.toDouble)+" --- 1/"+n)
	}


	//	override def toString: String = "   n: "+n+", rs: "+results.size+", "+results
	//override def toString: String = "   n: "+n+" / "+(new EulerDiv(n)).primes+", rs: "+results.size+", "+results
	override def toString: String = "   n: "+n+" / "+(new EulerDiv(n)).primes+", rs: "+results.size
}

class Euler108Length3(val n: BigInt) {
	var length = 0
			if(n<40000000) {
				var x = n+1
						val end = (n*2)+1
						while(x<end) {
							if(n*x%(n-x)==0) {
								length += 1
							} 	
							x += 1
						}
			}

	override def toString: String = "   n: "+n+" / "+(new EulerDiv(n)).primes+", rs: "+length
}


object Euler110 {
	var solutions = ListSet[BigInt]()

			val troisNMoinsUnL = new FunctionEuler110((it: Item) => new Item((3*it.bi)-1),(it: Item) => new Item((it.bi+1)/3),"3n-1")
	val cinqNMoinsDeuxL = new FunctionEuler110((it: Item) => new Item((5*it.bi)-2),(it: Item) => new Item((it.bi+2)/5),"5n-2")
	val septNMoinsTroisL = new FunctionEuler110((it: Item) => new Item((7*it.bi)-3),(it: Item) => new Item((it.bi+3)/7),"7n-3")

	def troisNMoinsUn(m: Int,when: Int, whenMeq2: Int): Int = {
		assume(m>=when,m+">="+when)
		if(m==when) {
			whenMeq2
		} else {
			(troisNMoinsUn(m-1,when,whenMeq2)*3)-1
		}
	}

	def cinqNMoinsDeux(m: Int,when: Int, whenMeq2: Int): Int = {
		assume(m>=when,m+">="+when)
		if(m==when) {
			whenMeq2
		} else {
			(cinqNMoinsDeux(m-1,when,whenMeq2)*5)-2
		}
	}

	def tetesDeSeries: List[BigInt] = {
			val input = solutions.toList.filter((bi: BigInt) => bi>0 & bi<5000000).sorted
					var tetesDeSeries3 = input
					var tetesDeSeries5 = input
					var tetesDeSeries7 = input
					input.foreach((bi: BigInt) => {
						val trois = troisNMoinsUnL.table(tetesDeSeries3.map(new Item(_))).map((z: (Item, Item)) => z._2.bi)
								val cinq = cinqNMoinsDeuxL.table(tetesDeSeries3.map(new Item(_))).map((z: (Item, Item)) => z._2.bi)
								val sept = septNMoinsTroisL.table(tetesDeSeries3.map(new Item(_))).map((z: (Item, Item)) => z._2.bi)
								tetesDeSeries3 = tetesDeSeries3 filterNot( trois contains)
								tetesDeSeries5 = tetesDeSeries5 filterNot( trois contains)
								tetesDeSeries5 = tetesDeSeries5 filterNot( cinq contains)
								tetesDeSeries7 = tetesDeSeries7 filterNot( trois contains)
								tetesDeSeries7 = tetesDeSeries7 filterNot( cinq contains)
								tetesDeSeries7 = tetesDeSeries7 filterNot( sept contains)
					})
					println(input)
					println(tetesDeSeries3)
					println(tetesDeSeries5)
					println(tetesDeSeries7)
					tetesDeSeries7
	}

	def generateSeries(seed: List[BigInt]): List[BigInt] = {
			var sept2 = seed ++ septNMoinsTroisL.table(seed.map(new Item(_))).map((z: (Item, Item)) => z._2.bi)
					sept2 = (sept2 ++ septNMoinsTroisL.table(sept2.map(new Item(_))).map((z: (Item, Item)) => z._2.bi)).distinct
					sept2 = (sept2 ++ septNMoinsTroisL.table(sept2.map(new Item(_))).map((z: (Item, Item)) => z._2.bi)).distinct
					sept2 = (sept2 ++ septNMoinsTroisL.table(sept2.map(new Item(_))).map((z: (Item, Item)) => z._2.bi)).distinct
					var cinq2 = sept2 ++ cinqNMoinsDeuxL.table(sept2.map(new Item(_))).map((z: (Item, Item)) => z._2.bi)
					cinq2 = (cinq2 ++ cinqNMoinsDeuxL.table(cinq2.map(new Item(_))).map((z: (Item, Item)) => z._2.bi)).distinct
					cinq2 = (cinq2 ++ cinqNMoinsDeuxL.table(cinq2.map(new Item(_))).map((z: (Item, Item)) => z._2.bi)).distinct
					cinq2 = (cinq2 ++ cinqNMoinsDeuxL.table(cinq2.map(new Item(_))).map((z: (Item, Item)) => z._2.bi)).distinct
					var trois2 = cinq2 ++ troisNMoinsUnL.table(cinq2.map(new Item(_))).map((z: (Item, Item)) => z._2.bi)
					trois2 = (trois2 ++ troisNMoinsUnL.table(trois2.map(new Item(_))).map((z: (Item, Item)) => z._2.bi)).distinct
					trois2 = (trois2 ++ troisNMoinsUnL.table(trois2.map(new Item(_))).map((z: (Item, Item)) => z._2.bi)).distinct
					trois2 = (trois2 ++ troisNMoinsUnL.table(trois2.map(new Item(_))).map((z: (Item, Item)) => z._2.bi)).distinct
					/*					println("seed["+seed.size+"]: "+ seed)
					println(sept2.sorted)
			println(cinq2.sorted)*/
					println("output["+trois2.size+"]: "+trois2.sorted)
			trois2.sorted
	}
}

class FunctionEuler110(override val process: (Item) => Item, val reverse: (Item) => Item, override val tostring: String) extends Function(process, toString) {
	def this(process: (Item) => Item, tostring: String) = this(process, (it: Item) => new Item(true), tostring)

			override def table(range: List[Item]): List[(Item,Item)] = range.map((it: Item) => (it,process(it))).filter((z: (Item,Item)) => z._2.i < 7000000 & z._2.bi < 7000000 )

			def findValidReverse(it: Item): Item = {
		val Reverse = reverse(it)
				val Forward = process(Reverse)
				if(Forward.bi==it.bi & Forward.i==it.i) {
					println(toString+"Reverse("+it.bi+"/"+it.i+")="+Reverse.bi+"/"+Reverse.i)
					findValidReverse(Reverse)
				} else {
					println(toString+"Reverse("+it.bi+"/"+it.i+")=NOGOOD!")
					it
				}
	}
}






































