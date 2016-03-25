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

    def rangeFrom(a: Int, b: Int): Stream[Int] = a #:: rangeFrom(b, 1 + b)
    def getprimesFrom(a: Int, b: Int): List[(Int, Int)] = {
      val r = rangeFrom(0, 1)
      val z = r.map(n => (n, quadratic(a, b, n))).takeWhile(q => {
        premiers.contains(q._2)
      }).toList
      //println("     "+a + " " + b + " " + z)
      z
    }

    var un_a_linfini = rangeFrom(0, 1).take(7)
    println(un_a_linfini.toList)

    val z = (-1000 to 1000).map(a => {
      //EulerPrime.premiers10000.filter(_ < 1602).filter(b => b + a > 0).map(b => {
        EulerPrime.premiers1000.filter(b => b + a > 0).map(b => {
        val gp = getprimesFrom(a, b.toInt)
        //println(a + " " + b + " " + gp)
        (a, b.toInt, gp.length, gp)
      })
    }).flatten.filter(_._3 > 1).sortBy(_._3)
    println(z.mkString("\n  ", "\n  ", "\n  "))
    println(EulerPrime.premiers1000)
    println(EulerPrime.premiers1000.last)
    println(EulerPrime.premiers10000.filter(_ < 1602).last)
    val gp = getprimesFrom(-79, 1601)
    println(-79,1601,gp.length, gp)

    val y = z.last
    val result = y._1*y._2
    println("Euler27[" + result + "]")
    result should be === (-61*971)
  }
}