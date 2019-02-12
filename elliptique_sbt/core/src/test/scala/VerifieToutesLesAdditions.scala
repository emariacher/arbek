import kebra.MyLog._
import org.scalatest._

import scala.util.Random


/*
https://www.coindesk.com/math-behind-bitcoin/
https://crypto.stackexchange.com/questions/44304/understanding-elliptic-curve-point-addition-over-a-finite-field
https://fr.wikipedia.org/wiki/Courbe_elliptique
 */

class VerifieToutesLesAdditions extends FlatSpec with Matchers {
  val rnd = new Random(0)

  def ParcoursToutesLesPossibilitesDAddition(e: Elliptique): List[(BigInt, BigInt)] = {
    myPrintDln(e.title)
    e.curve.combinations(2).toList.foreach(pq => {
      //print(pq.head + " + " + pq.last + " = " + e.plus(pq.head, pq.last) + ", ")

      if (!e.lZeros.find(e.plus(pq.head, pq.last) == _).isEmpty) {
        myErrPrintDln(e.lZeros + " contient " + pq.head + " + " + pq.last + " = " + e.plus(pq.head, pq.last))
      }
    })
    e.lZeros
  }

  "CheckToutesLesAdditions" should "be OK" in {
    ParcoursToutesLesPossibilitesDAddition(new Elliptique(67, 0, 7)).isEmpty shouldEqual true
    ParcoursToutesLesPossibilitesDAddition(new Elliptique(17, 0, 7)).isEmpty shouldEqual false
  }

  "Check17" should "be OK" in {
    val e = new Elliptique(17, 0, 7)
    myPrintDln(e.title, e.curve)
    e.plus((BigInt(1), BigInt(5)), (BigInt(15), BigInt(4))) shouldEqual e.lZeros.head
    e.plus(e.lZeros.head, e.lZeros.head) shouldEqual e.ZERO
    myPrintIt(e.loopsum2(e.curve.head))
    myPrintIt(e.curve.filter(p => e.loopsum2(e.curve.head).find(p == _).isEmpty))
    e.curve.filter(p => e.loopsum2(e.curve.head).find(p == _).isEmpty).isEmpty shouldEqual false
  }

  "Check67" should "be OK" in {
    val e = new Elliptique(67, 0, 7)
    myPrintDln(e.title)
    myPrintIt(e.curve.head, e.plus(e.curve.head, e.curve.head))
    myPrintIt(e.curve.filter(p => e.loopsum2(e.curve.head).find(p == _).isEmpty))
    e.curve.filter(p => e.loopsum2(e.curve.head).find(p == _).isEmpty).isEmpty shouldEqual true
  }

}
