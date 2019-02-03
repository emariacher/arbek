import org.scalatest._

import scala.math.BigInt

import scala.collection.mutable.ListBuffer

/*
https://www.coindesk.com/math-behind-bitcoin/
https://crypto.stackexchange.com/questions/44304/understanding-elliptic-curve-point-addition-over-a-finite-field
https://fr.wikipedia.org/wiki/Courbe_elliptique
 */

class Nombrepremiersquimarchent extends FlatSpec with Matchers {
  val premiers = EulerPrime.premiers1000

  def Trouve_les_nombres_premiers_qui_pourraient_marcher(a: BigInt, b: BigInt) = {
    println("y2 = x3 + " + a + "x + " + b, premiers.take(100).filter(modlo => {
      val e = new Elliptique(modlo, a, b)
      e.getDelta should not equal 0
      e.curve.size > modlo & e.curve.filter(p => p._1 * p._2 == 0).isEmpty
    }))
  }

  "Trouve les nombres premiers 0" should "be OK" in {
    (1 to 30).foreach(b => Trouve_les_nombres_premiers_qui_pourraient_marcher(0, b))
  }

  "Trouve les nombres premiers 1" should "be OK" in {
    (1 to 30).foreach(b => Trouve_les_nombres_premiers_qui_pourraient_marcher(1, b))
  }

  "Trouve les nombres premiers" should "be OK" in {
    val prems = premiers.take(70).toList.map(p => (p, ListBuffer[(BigInt, BigInt)]()))
    (0 to 10).foreach(a =>
      (1 to 10).foreach(b => {
        print("\ny2 = x3 + " + a + "x + " + b + " ")
        prems.take(70).foreach(modlo => {
          val e = new Elliptique(modlo._1, a, b)
          e.getDelta should not equal 0
          if (e.curve.size > modlo._1 & e.curve.filter(p => p._1 * p._2 == 0).isEmpty) {
            val zob = (BigInt(a), BigInt(b))
            prems.find(_._1 == modlo._1) match {
              case Some(pr) => {
                pr._2 += zob
                print(zob, " ")
              }
              case None =>
            }
          }
        })
      }))
    println(prems.mkString("\n"))
    prems.find(_._1 == 67) match {
      case Some(pr) => pr._2.contains((BigInt(0), BigInt(7))) shouldEqual true
      case None => false shouldEqual true
    }
    prems.find(_._1 == 241) match {
      case Some(pr) => pr._2.contains((BigInt(0), BigInt(7))) shouldEqual true
      case None => false shouldEqual true
    }
  }
}
