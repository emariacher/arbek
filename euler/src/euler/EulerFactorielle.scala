package euler

object EulerFactorielle {

    def fact(n: BigInt): BigInt = {
        if (n <= 1) {
            1
        } else {
            n * fact(n - 1)
        }
    }

    def fact2(n: Int): BigInt = {
        if (n <= 0) {
            1
        } else {
            (new Range(1, n + 1, 1)).toList.map(BigInt(_)).product
        }
    }

    def combinations(n: BigInt, k: BigInt): BigInt = fact(n) / (fact(n - k) * fact(k))
    def permutations(n: BigInt, k: BigInt): BigInt = fact(n) / fact(n - k)

    def fnext(i: Int, prev: BigInt): BigInt = i * prev
}

class EulerFactorielle(val i: Int) {
    def f: BigInt = (new Range(1, i + 1, 1)).toList.map(BigInt(_)).product
}

class EulerFactorielleNext(val i: Int, val prev: BigInt) {
    def f: BigInt = i * prev
}


