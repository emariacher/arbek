package euler

class TriangleRange(limit: Int) {
	val range = new Range(1,limit,1).toList
			var sum = BigInt(0)
			var triangle = List[BigInt]()

			range.foreach((i: Int) => {
				sum = sum + i
						triangle = triangle :+ sum
			})
			println(triangle)

}

class Triangle(val n: Int) {
	val triangle = n*(n+1)/2
			val div = new EulerDiv(triangle)
	val divs = new EulerDivisors(div.primes)
	val length = divs.divisors.size

	override def toString: String = "n: "+n+" triangle: "+triangle+" primes: "+div.primes+" divs: "+length+" "+divs.divisors
}

class Triangle2(val triangle: BigInt) {
	val pardeux = triangle*2
			val n= math.sqrt(pardeux.toDouble).toInt
			val div = new EulerDiv(triangle)
	val divs = new EulerDivisors(div.primes)
	val length = divs.divisors.size
	val divn = new EulerDiv(n)
	val divnp1 = new EulerDiv(n+1)

	def isTriangle: Boolean = (n*(n+1))/2==triangle

	override def toString: String = "n: "+n+" triangle: "+triangle+" divn: "+divn.primes.size+" "+divn.primes+" divnp1: "+divnp1.primes.size+" "+divnp1.primes+" divs: "+length+" "+divs.divisors
}

class TrianglePascal(limit: Int) {
	val range = new Range(3,limit+1,1).toList
			var triangle = List[List[BigInt]](List(1),List(1,1))

			range.foreach((i: Int) => {
				val newRow = triangle.last.sliding(2).toList.map(_.sum)
						triangle = triangle :+ (List(BigInt(1)) ++ newRow ++ List(BigInt(1)))
			})
}

class TrianglePascalCompute(limit: Int) {


	def computePascal(callback: (Int,List[BigInt]) => Int) = {
		callback(1,List(1))
		var triangle =List[BigInt](1,1)
		callback(2,triangle)
		var cpt = 3
		while(cpt<limit+1) {
			triangle = (List(BigInt(1)) ++ triangle.sliding(2).toList.map(_.sum) ++ List(BigInt(1)))
					callback(cpt,triangle)
					cpt += 1
		}
	}
}
