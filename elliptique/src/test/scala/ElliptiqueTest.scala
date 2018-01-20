import Elliptique._
import org.scalatest._

import scala.collection.immutable.{Range, ListSet}
import scala.math.BigInt
import scala.util.Random


class ElliptiqueTest extends FlatSpec with Matchers {
  def rangeStream2(a: BigInt, b: BigInt): Stream[Triplet] = new Triplet(a, a * a) #:: rangeStream2(b, 1 + b)

  def stream_zero_a_linfini2: Stream[Triplet] = rangeStream2(0, 1)

  def rangeStream3p7(a: BigInt, b: BigInt): Stream[Triplet] = new Triplet(a, (a * a * a) + 7) #:: rangeStream3p7(b, 1 + b)

  def stream_zero_a_linfini3p7: Stream[Triplet] = rangeStream3p7(0, 1)

  "TrouveLesInverses" should "be OK" in {
    println("TrouveLesInverses")
    val li = new getInverse(67).li
    println(li)
    val result = li.filter(_._1 == 63).head._2
    println("TrouveLesInverses[" + result + "]")
    result should be === 50
  }

  "Elliptique" should "be OK" in {
    println("Elliptique")
    val t_ici = timeStamp(t_start, "ici!")

    val l2 = stream_zero_a_linfini2 take (67 + 2) toList
    val l3p7 = stream_zero_a_linfini3p7 take (67 + 2) toList

    l2.apply(22).d._3 should be === l3p7.apply(2).d._3
    l2.apply(28).d._3 should be === l3p7.apply(47).d._3
    l2.apply(39).d._3 should be === l3p7.apply(47).d._3
    (new Doublon(2, 22)).check should be === true
    (new Doublon(2, 23)).check should be === false

    /*l3p7.foreach(t => {
      println(t, l2.filter(u => u.d._3 == t.d._3))
    })*/

    val lp = l3p7.map(x => {
      val lz = l2.filter(y => y.d._3 == x.d._3).map(y => new Doublon(x.d._1 % 67, y.d._1 % 67))
      lz.length % 2 should be <= 2
      lz.length % 2 should be === 0
      if (lz.length == 2) {
        lz.head.y + lz.last.y should be === 67
      }
      lz
    }).flatten

    //println(lp)

    lp.filter(d => d.x == 2 & d.y == 22).isEmpty should be === false
    lp.filter(d => d.x == 2 & d.y == 23).isEmpty should be === true

    val t_la = timeStamp(t_ici, "la! ******************************")

    val result = lp.length
    println("Elliptique[" + result + "]")
    result should be === 78
  }

  " GetCurve" should "be OK" in {
    println("GetCurve")
    val lp = new getCurve(67).lp
    println(lp)
    lp.filter(d => d._1 == 2 & d._2 == 22).isEmpty should be === false
    lp.filter(d => d._1 == 2 & d._2 == 23).isEmpty should be === true
    val result = lp.length
    println("GetCurve[" + result + "]")
    result should be === 78
  }

  "Sub" should "be OK" in {
    println("Sub")
    val result = new Elliptique(67).sub(2, 6)
    println("Sub[" + result + "]")
    result should be === 63
  }

  "Plus" should "be OK" in {
    val e = new Elliptique(67)
    println("Plus", ((2, 22), (6, 25), 67))
    e.check((2, 22)) should be === true
    e.check((6, 25)) should be === true

    val result = e.plus((2, 22), (6, 25))
    println("Plus[" + result + "]")
    result._1 should be === 47
    result._2 should be === 28
    e.check(result) should be === true
  }

  "Doubling" should "be OK" in {
    val e = new Elliptique(67)
    println("Doubling", ((2, 22), (2, 22), 67))
    e.check((2, 22)) should be === true

    val result = e.plus((2, 22), (2, 22))
    println("Doubling[" + result + "]")
    result._1 should be === 52
    result._2 should be === 7
    e.check(result) should be === true
  }

  "CheckToutesLesAdditions" should "be OK" in {
    val e = new Elliptique(67)
    println("CheckToutesLesAdditions")
    val rnd = new Random(0)
    val lp = new getCurve(67).lp
    lp.combinations(2).toList.foreach(d => {
      if (d.head._1 == d.last._1) {
        println("")
      }
      print(", " + d.head + "+" + d.last)
      val r = e.plus(d.head, d.last)
      print("=" + r)
      e.check(r) should be === true
    })
    println("\n************ Check aussi la multiplication par deux")
    lp.zip(lp).foreach(d => {
      print(", " + d._1 + "+" + d._2)
      val r = e.plus(d._1, d._2)
      print("=" + r)
      e.check(r) should be === true
      if (rnd.nextInt(10) == 0) {
        println("")
      }
    })
  }

  "CheckLaBoucle67" should "be OK" in {
    println("CheckLaBoucle67: il semble y avoir des sous groupes")
    val e = new Elliptique(67)
    val lp = e.curve.sortBy(p => (p._1 * 100) + p._2)
    println(67, lp.size, lp)

    val lr1 = e.loopmul2(lp.head)
    val lr2 = e.loopmul2(lp.tail.head)
    (lr1 ++ lr2).sortBy(p => (p._1 * 100) + p._2) should be === lp

    val lr3 = e.loopmul3(lp.head)
    lr3.sortBy(p => (p._1 * 100) + p._2) should be === lp

    val lr4 = e.loopmul4(lp.head)
    val lr5 = e.loopmul4(lp.tail.head)
    (lr4 ++ lr5).sortBy(p => (p._1 * 100) + p._2) should be === lp
  }

  "CheckLaBoucle71" should "be OK" in {
    println("CheckLaBoucle71: il semble avoir des boucles infinies")
    val e = new Elliptique(71)
    val lp = e.curve.sortBy(p => (p._1 * 100) + p._2)
    println(71, lp.size, lp)
    lp.head should be ===(BigInt(1), BigInt(24))
    e.check(lp.head) should be === true
    e.plus(lp.head, lp.head) should be ===(BigInt(36), BigInt(67))
    e.check(e.plus(lp.head, lp.head)) should be === true
    e.check((BigInt(33), BigInt(35))) should be === true
    e.check((BigInt(33), BigInt(36))) should be === true

    val lr1 = e.loopmul2(lp.head)
    val lr2 = e.loopmul2(lp.tail.head)
    (lr1 ++ lr2).sortBy(p => (p._1 * 100) + p._2) should not equal lp

    val lr3 = e.loopmul3(lp.head)
    lr3.sortBy(p => (p._1 * 100) + p._2) should not equal lp

    val lr4 = e.loopmul4(lp.head)
    val lr5 = e.loopmul4(lp.tail.head)
    (lr4 ++ lr5).sortBy(p => (p._1 * 100) + p._2) should not equal lp
  }
}