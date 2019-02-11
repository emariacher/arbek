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

  def ParcoursToutesLesPossibilitesDAddition(e: Elliptique): Unit = {
    myPrintDln(e.title)
    e.curve.combinations(2).toList.foreach(pq => {
      //print(pq.head + " + " + pq.last + " = " + e.plus(pq.head, pq.last) + ", ")

      if (!e.lZeros.find(e.plus(pq.head, pq.last) == _).isEmpty) {
        myErrPrintDln("\n  " + e.lZeros + " contient " + pq.head + " + " + pq.last + " = " + e.plus(pq.head, pq.last))
      }
    })
  }

  "CheckToutesLesAdditions" should "be OK" in {
    ParcoursToutesLesPossibilitesDAddition(new Elliptique(67, 0, 7))
    ParcoursToutesLesPossibilitesDAddition(new Elliptique(17, 0, 7))
  }

}
