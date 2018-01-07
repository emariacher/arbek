import Elliptique._
import org.scalatest._

import scala.collection.immutable.{Range, ListSet}
import scala.math.BigInt

class ElliptiqueTest extends FlatSpec with Matchers {
  def rangeStream2(a: BigInt, b: BigInt): Stream[BigInt] = (a*a) #:: rangeStream2(b, 1 + b)

  def stream_zero_a_linfini2: Stream[BigInt] = rangeStream2(0, 1)

  def rangeStream3(a: BigInt, b: BigInt): Stream[BigInt] = ((a*a*a)+7) #:: rangeStream3(b, 1 + b)

  def stream_zero_a_linfini3: Stream[BigInt] = rangeStream3(0, 1)

  "Elliptique" should "be OK" in {
    println("Elliptique")
    val t_ici = timeStamp(t_start, "ici!")
    stream_zero_a_linfini2 take 5 foreach println
    stream_zero_a_linfini3 take 5 foreach println

    val t_la = timeStamp(t_ici, "la! ******************************")

    val result = 0
    println("Elliptique[" + result + "]")
    result should be === 0
  }
}