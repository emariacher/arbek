import org.scalatest._

import scala.math.BigInt


/*
https://www.coindesk.com/math-behind-bitcoin/
https://crypto.stackexchange.com/questions/44304/understanding-elliptic-curve-point-addition-over-a-finite-field
https://fr.wikipedia.org/wiki/Courbe_elliptique
 */

class Multiplication_Signature extends FlatSpec with Matchers {

  "Teste la multiplication part I" should "be OK" in {
    println("Teste la multiplication part I")
    val e = new Elliptique(67, 0, 7)
    (0 to 500).foreach(q => {
      e.mul((BigInt(0), BigInt(0)), q)._2 shouldEqual q
    })
  }

  "Teste la multiplication part II" should "be OK" in {
    println("Teste la multiplication part II")
    val a = 0
    val b = 7
    val modlo = 73
    println("y2 = x3 + " + a + "x + " + b + " modulo " + modlo + ": teste la multiplication part II")
    val e = new Elliptique(modlo, a, b)
    val p = (BigInt(6), BigInt(2))
    println(p,
      e.plus(p, p),
      e.plus(p, (BigInt(60), BigInt(0))),
      e.plus(p, (BigInt(6), BigInt(71)))
    )
    e.mul(p, 0)._3 shouldEqual(BigInt(0), BigInt(0))
    e.mul(p, 1)._3 shouldEqual p
    e.mul(p, 2)._3 shouldEqual e.plus(p, p)
    e.mul(p, 3)._3 shouldEqual(BigInt(6), BigInt(71))
    e.mul(p, 4)._3 shouldEqual(BigInt(0), BigInt(0))
    e.mul(p, 5)._3 shouldEqual p
  }

  "Teste la multiplication part III" should "be OK" in {
    println("Teste la multiplication part III")
    val a = 0
    val b = 7
    val modlo = 67
    println("y2 = x3 + " + a + "x + " + b + " modulo " + modlo + ": teste la multiplication part III")
    val e = new Elliptique(modlo, a, b)
    val p = e.curve.head
    e.mul(p, e.curve.size + 2)._3 shouldEqual p
  }

  "Signature 67" should "be OK" in {
    val a = 0
    val b = 7
    val modlo = 67
    val order = 79
    println("y2 = x3 + " + a + "x + " + b + " modulo " + modlo + ": Fais de la signature https://www.coindesk.com/math-behind-bitcoin/")
    val e = new Elliptique(modlo, a, b)
    val basepoint = (BigInt(2), BigInt(22))
    e.check(basepoint)
    val privateKey = 2
    val publicKey = e.mul(basepoint, privateKey)._3
    publicKey shouldEqual(BigInt(52), BigInt(7))
    val data = 17
    println("Compute signature")
    println("  step 0: basepointG [" + basepoint + "], privateKey [" + privateKey + "], publicKeyQ [" + publicKey + "], data [" + data + "]")
    val randomNumber_k = 3
    println("  step 1: pick random number " + randomNumber_k)
    val thePoint = e.mul(basepoint, randomNumber_k)._3
    println("  step 2: compute the Point " + thePoint)
    val r = thePoint._1
    r should not equal BigInt(0)
    println("  step 3: find r " + r)
    Elliptique.inverse(order, randomNumber_k) shouldEqual 53
    val s = ((data + (r * privateKey)) * Elliptique.inverse(order, randomNumber_k)) % order
    println("  step 4: find s " + s)
    val signature = (r, s)
    println("  step 5: signature " + signature)
    signature shouldEqual(62, 47)
    println("Verify signature")
    println("  step 1: Verify that r and s are between 1 and order-1")
    r >= 1 & r < order shouldBe true
    s >= 1 & s < order shouldBe true
    val w = Elliptique.inverse(order, s) % order
    println("  step 2: Calculate w = 1/s mod order = " + w)
    w shouldEqual 37
    val u = w * data % order
    println("  step 3: Calculate u = data * w mod order = " + u)
    u shouldEqual 76
    val v = r * w % order
    println("  step 4: Calculate v = r * w mod order = " + v)
    v shouldEqual 3
    val uG = e.mul(basepoint,u)._3
    uG shouldEqual (62,4)
    val vQ = e.mul(publicKey,v)._3
    val x_y = e.plus(uG,vQ)
    println("  step 5: Calculate uG["+uG+"] + vQ["+vQ+"] = " + x_y)
    x_y shouldEqual (BigInt(62),BigInt(63))
    println("  step 6: Verify that r["+r+"] == x mod order [" + x_y._1+"]. The signature is invalid if it is not.")
    x_y._1 shouldEqual r
  }


}
