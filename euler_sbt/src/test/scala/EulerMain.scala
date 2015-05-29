import org.scalatest._

class EulerMain extends FlatSpec with Matchers {
  "Euler243" should "be OK" in {
    println("Euler243")
    val premiers = EulerPrime.premiers1000
    def resilience(d: Int) = {
      val primes = new EulerDiv(d).primes
      val divisors = new EulerDivisors(primes).divisors
      val resil = Range(1, d, 2).filter(i => {
        divisors.filter(div => i % div == 0).isEmpty
      })
      println(d+"\t"+resil.length+"/"+(d - 1)+"\t"+(1.0 * resil.length / (d - 1))+"\t"+primes+"\t"+new EulerDiv(resil.length).primes)
      (resil.length, d - 1)
    }

    resilience(12) should be ===(4, 11)
    resilience(premiers.take(4).product.toInt)
    resilience(premiers.take(5).product.toInt)
    resilience(2 * premiers.take(5).product.toInt)
    resilience(2 * 2 * premiers.take(5).product.toInt)
    resilience(2 * 3 * premiers.take(5).product.toInt)
    resilience(2 * 2 * 3 * premiers.take(5).product.toInt)
    resilience(2 * 3 * 5 * premiers.take(5).product.toInt)
    resilience(2 * 2 * 3 * 3 * premiers.take(5).product.toInt)
    resilience(premiers.take(6).product.toInt)
    resilience(premiers.take(7).product.toInt)

    println(1.0 * 15499 / 94744)

    val result = 0
    println("Euler243[" + result + "]")
    result should be === 0
  }
}