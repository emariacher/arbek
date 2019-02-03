import kebra.MyLog._
import org.scalatest._

import scala.math.BigInt

/*
https://www.coindesk.com/math-behind-bitcoin/
https://crypto.stackexchange.com/questions/44304/understanding-elliptic-curve-point-addition-over-a-finite-field
https://fr.wikipedia.org/wiki/Courbe_elliptique
https://www.johannes-bauer.com/compsci/ecc/#anchor24
 */

class Encryption_Decryption extends FlatSpec with Matchers {

  "Encryption Decryption 67" should "be OK" in {
    val a = 0
    val b = 7
    val modlo = 67
    val order = 79
    println("y2 = x3 + " + a + "x + " + b + " modulo " + modlo + ": Fais de l encryption https://www.johannes-bauer.com/compsci/ecc/#anchor24")
    val e = new Elliptique(modlo, a, b)
    val basepoint = (BigInt(2), BigInt(22))
    e.check(basepoint)
    val privateKey = 2
    val publicKey = e.mul(basepoint, privateKey)._3
    publicKey shouldEqual(BigInt(52), BigInt(7))
    val data = 17
    myPrintIt("Encrypte")
    println("  step 0: basepointG [" + basepoint + "], privateKeydA [" + privateKey + "], publicKeyQA [" + publicKey + "], data [" + data + "]")
    val randomNumber_k = 3
    println("  step 1: pick random number " + randomNumber_k)
    val R = e.mul(basepoint, randomNumber_k)._3
    val Se = e.mul(publicKey, randomNumber_k)._3
    println("  step 2: compute R " + R + ", compute S " + Se)
    val data_encryptee = data ^ Se._1
    println("  step 3: encrypt/xor data[" + data + "]-->" + data_encryptee)
    myPrintIt("Decrypte")
    val Sd = e.mul(R, privateKey)._3
    println("  step 4: compute S " + Sd)
    val data_decryptee = data_encryptee ^ Sd._1
    println("  step 5: decrypt/xor data[" + data_encryptee + "]-->" + data_decryptee)
    data_decryptee shouldEqual data
  }
}
