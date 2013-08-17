package euler

object Fibonacci {
    def fib(bi: BigInt): BigInt = {
        if (bi <= 2) {
            1
        } else {
            var result = (BigInt(0), BigInt(1), BigInt(0))
            while (result._1 < bi) {
                result = rec(result)
            }
            result._3
        }
    }

    def rec(t: (BigInt, BigInt, BigInt)): (BigInt, BigInt, BigInt) = {
        (t._1 + 1, t._2 + t._3, t._2)
    }

    def fib2(bi: BigInt): List[BigInt] = {
        print("Computing Fibonacci [" + bi + "] ")
        var result = List[BigInt](0, 1, 1)
        var cpt = 0
        while (cpt <= bi) {
            result = result :+ (result.last + result.reverse.tail.head)
            if (cpt % 100 == 0) {
                print(".")
            }
            cpt += 1
        }
        println("")
        result
    }

    def fib3(bi: BigInt): List[BigInt] = {
        print("Computing Fibonacci [" + bi + "] ")
        var result = List[BigInt](0, 1, 1)
        var cpt = 0
        while (result.last <= bi) {
            result = result :+ (result.last + result.reverse.tail.head)
            if (cpt % 100 == 0) {
                print(".")
            }
            cpt += 1
        }
        println("")
        result.dropRight(1)
    }

    val fib10000 = fib2(10000)

}