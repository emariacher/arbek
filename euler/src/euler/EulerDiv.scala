package euler
import scala.collection.immutable.ListSet
import scala.collection.immutable.TreeSet
import EulerPrime._

class EulerDiv(bi: BigInt) {
    var primes = List[BigInt]()
    val premiers = premiers100000
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
        require(bi < (premiers.last * premiers.last), bi + "<" + premiers.last + "*" + premiers.last + "(" + (premiers.last * premiers.last) + ")")
    } else {
        require(bic < (premiers.last * premiers.last), bic + "<" + premiers.last + "*" + premiers.last + "(" + (premiers.last * premiers.last) + ")")
    }
    //println("*e* "+bi+" "+bic+" "+primes)
}

class EulerDiv2(bi: BigInt, premiers: TreeSet[BigInt]) {
    var primes = List[BigInt]()
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
        //require(bi<(premiers.last*premiers.last),bi+"<"+premiers.last+"*"+premiers.last+"("+(premiers.last*premiers.last)+")")
        if (bi > (premiers.last * premiers.last)) {
            //println(bi+">"+premiers.last+"*"+premiers.last+"("+(premiers.last*premiers.last)+")")
        }

    } else {
        //require(bic<(premiers.last*premiers.last),bic+"<"+premiers.last+"*"+premiers.last+"("+(premiers.last*premiers.last)+")")
        if (bic > (premiers.last * premiers.last)) {
            //println(bic+">"+premiers.last+"*"+premiers.last+"("+(premiers.last*premiers.last)+")")
        }
    }
    //println(primes)
}

class EulerDivisors(val l: List[BigInt]) {
    val range = new Range(1, l.size, 1).toList
    def this(ed: EulerDiv) = this(ed.primes)
    val divisors = range.foldLeft(List[List[BigInt]]())(_ ++ l.combinations(_)).map(_.product).distinct.sorted
    val primesUnique = TreeSet[BigInt]() ++ l
}

class EulerDivisors3(l: List[BigInt]) {
    val range = new Range(1, l.size + 1, 1).toList
    val divisors = range.foldLeft(List[List[BigInt]]())(_ ++ l.combinations(_)).map(_.product).distinct.sorted
    val primesUnique = TreeSet[BigInt]() ++ l
}

class EulerDivisors2(val l: List[BigInt], val bi: BigInt) {
    val range = new Range(1, l.size, 1).toList
    val result = range.find((i: Int) => l.combinations(i).toList.map(_.product).contains(bi))
    var found = false
    result match {
        case Some(i) => found = true
        case _       => found = false
    }
}