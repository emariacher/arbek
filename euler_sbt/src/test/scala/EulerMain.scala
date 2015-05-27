import org.scalatest._

import scala.collection.immutable.ListSet

class EulerMain extends FlatSpec with Matchers {
  "Euler32" should "be OK" in {
    println("Euler32")
    val result = Range(13 * 245, 9876 + 1).filter(i => {
      val lz = i.toString.toList
      lz.length == (new ListSet() ++ lz).toList.length
    }).filter(c => {
      c.toString.indexOf("0") < 0
    }).map(i => {
      val divs = new EulerDivisors(new EulerDiv(i).primes).divisors
      (i, divs)
    }).filter(c => {
      if (c._2.length == 2) {
        val lz = (c._2.mkString("", "", "") + c._1).toList
        lz.length == (new ListSet() ++ lz).toList.length && lz.length == 5
      } else if (c._2.length > 0) {
        true
      } else {
        false
      }
    }).map(c => {
      val z1 = c._1.toString.toList
      val z2 = c._2.filter(d => {
        z1.intersect(d.toString.toList).isEmpty && d.toString.indexOf("0") < 0
      })
      (c._1, z2.toList)
    }).filter(c => {
      c._2.length > 1
    }).map(c => {
      (c._1, c._2.combinations(2).filter(
        cb => cb.head * cb.last == c._1 &&
          (cb.head.toString + cb.last.toString).length == 5
      ).toList)
    }).filter(c => {
      !c._2.isEmpty
    }).map(c => {
      (c._1, c._2.filter(
        cb => {
          val lz = (cb.head.toString + cb.last.toString + c._1.toString).toList
          lz.length == (new ListSet() ++ lz).toList.length
        }
      ))
    }).filter(c => {
      !c._2.isEmpty
    }).map(c => {
      println(c)
      c._1
    }).sum
    println("Euler32[" + result + "]")
    result should be === 45228
  }
}
