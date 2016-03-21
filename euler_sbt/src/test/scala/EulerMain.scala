import org.scalatest._

class EulerMain extends FlatSpec with Matchers {
  "Euler500" should "be OK" in {
    println("Euler500")
    val premiers = EulerPrime.premiers1000
    def numdiv(d: BigInt) = {
      val divisors = new EulerDivisors(new EulerDiv(d)).divisors
      println(d + "\t" + (divisors.length+2) + "\t" + ispowerof2(divisors.length+2) + "\t" + divisors)
    }

    def ispowerof2(d: BigInt) = {
      val divisorsDistinct = new EulerDivisors(new EulerDiv(d)).primesUnique.toList.distinct
      divisorsDistinct.length == 1 & divisorsDistinct.head == 2
    }

    numdiv(2)
    numdiv(4)
    numdiv(8)
    numdiv(12)
    numdiv(16)
    numdiv(24)
    numdiv(32)
    numdiv(48)
    numdiv(64)
    numdiv(96)
    numdiv(120)
    numdiv(120*2)
    numdiv(120*3)
    numdiv(120*5)
    numdiv(120*2*3)
    numdiv(120*7)
    numdiv(120*11)
    numdiv(120*5*2)
    numdiv(120*5*3)
    numdiv(120*5*7)
    numdiv(120*7*2)
    numdiv(120*7*3)
    numdiv(120*7*11)

    val result = 0
    println("Euler500[" + result + "]")
    result should be === 0
  }
}