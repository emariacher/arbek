import Elliptique._
import org.scalatest._

import scala.collection.immutable.{Range, ListSet}
import scala.math.BigInt


class Triplet(val a: BigInt, val b: BigInt) {
  val d = (a, b, b mod 13)

  override def equals(x: Any): Boolean = d._3.equals(x.asInstanceOf[Triplet].d._3)

  override def toString: String = d.toString
}

class ElliptiqueTest extends FlatSpec with Matchers {
  def rangeStream2(a: BigInt, b: BigInt): Stream[Triplet] = new Triplet(a, a * a) #:: rangeStream2(b, 1 + b)

  def stream_zero_a_linfini2: Stream[Triplet] = rangeStream2(0, 1)

  def rangeStream3p7(a: BigInt, b: BigInt): Stream[Triplet] = new Triplet(a, (a * a * a) + 7) #:: rangeStream3p7(b, 1 + b)

  def stream_zero_a_linfini3p7: Stream[Triplet] = rangeStream3p7(0, 1)

  "Elliptique" should "be OK" in {
    println("Elliptique")
    val t_ici = timeStamp(t_start, "ici!")

    val l2 = stream_zero_a_linfini2 take 100 toList
    val l3p7 = stream_zero_a_linfini3p7 take 100 toList

    println(l2)
    println(l3p7)

    l2.foreach(t => {
      println(t, l3p7.filter(u => u.d._3 == t.d._3))
    })

    val t_la = timeStamp(t_ici, "la! ******************************")

    val result = 0
    println("Elliptique[" + result + "]")
    result should be === 0
  }
}