import org.scalatest._
import Stream.cons

class EulerMain extends FlatSpec with Matchers {
  "Euler27" should "be OK" in {
    println("Euler27")

    val premiers = EulerPrime.premiers100000
    val max = 10

    def quadratic(a: Int, b: Int, n: Int) = {
      ((n + a) * n) + b
    }

    def rangeFrom(a: Int, b: Int): Stream[Int] = a #:: rangeFrom(b,1 + b)
    def getprimesFrom(a: Int, b: Int): List[(Int,Int)] = {
      val r = rangeFrom(0, 1)
      val z = r.map(n => (n,quadratic(a, b, n))).takeWhile(q => {
        premiers.contains(q._2)
      }).toList
      //println("     "+a + " " + b + " " + z)
      z
    }

    var z = rangeFrom(0, 1).take(7)
    println(z.toList)

    (-5 to 5).map(a => {
      (-5 to 5).map(b => {
        println(a+" "+b+" "+getprimesFrom(a,b))
      })
    })


    val result = 0
    println("Euler27[" + result + "]")
    result should be === 0
  }
}