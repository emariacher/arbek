import Elliptique._
import org.scalatest._

import scala.math.BigInt
import scala.util.Random


/*
https://www.coindesk.com/math-behind-bitcoin/
https://crypto.stackexchange.com/questions/44304/understanding-elliptic-curve-point-addition-over-a-finite-field
https://fr.wikipedia.org/wiki/Courbe_elliptique
 */

class ElliptiqueTest2 extends FlatSpec with Matchers {
  val premiers = EulerPrime.premiers1000

  "Trouve les nombres premiers qui pourraient marcher pour y2 = x3 + 7" should "be OK" in {
    println("Trouve les nombres premiers qui pourraient marcher")
    println(premiers.take(100).filter(modlo => {
      val e = new Elliptique(modlo, 0, 7)
      e.curve.size > modlo & e.curve.filter(p => p._1 * p._2 == 0).isEmpty
    }))
  }

  "CheckLaBoucle241" should "be OK" in {
    val modlo = 241
    println("CheckLaBoucle" + modlo + ": ")
    val e = new Elliptique(modlo, 0, 7)
    e.getDelta should not equal 0
    val lp = e.curve.sortBy(p => (p._1 * 100) + p._2)
    println(modlo, lp.size, lp)
    lp.filter(p => p._1 * p._2 == 0).isEmpty shouldEqual true
    lp.size should be >= modlo

    println("\n***2*** " + modlo + " ***** 9 groupes pour les puissances de 2")
    var lgroupsmul2 = List[List[(BigInt, BigInt)]]()

    var lpm = lp
    while (!lpm.isEmpty) {
      val lmul = e.loopmul2(lpm.head)
      println("    " + lmul.size, lmul)
      lgroupsmul2 = lgroupsmul2 :+ lmul
      lpm = lpm.filter(p => !lmul.contains(p))
    }
    println(modlo, lgroupsmul2.size, lgroupsmul2.mkString("\n  ", "\n  ", "\n  "))
    e.checkVerbose((BigInt(70), BigInt(233))) shouldEqual true
    e.checkVerbose((BigInt(85), BigInt(233))) shouldEqual true
    e.checkVerbose((BigInt(86), BigInt(233))) shouldEqual true
    e.plus((BigInt(70), BigInt(233)), (BigInt(70), BigInt(233))) shouldEqual(BigInt(85), BigInt(233))
    lgroupsmul2.flatten.size shouldEqual lgroupsmul2.flatten.distinct.size
    lgroupsmul2.flatten.size shouldEqual lp.size
    lgroupsmul2.size shouldEqual 9

    println("\n***3*** " + modlo + " ***** 15 groupes pour les multiplications par 3")
    var lgroupsmul3 = List[List[(BigInt, BigInt)]]()

    lpm = lp
    while (!lpm.isEmpty) {
      val lmul = e.loopmul3(lpm.head)
      println("    " + lmul.size, lmul)
      lgroupsmul3 = lgroupsmul3 :+ lmul
      lpm = lpm.filter(p => !lmul.contains(p))
    }
    println(modlo, lgroupsmul3.size, lgroupsmul3.mkString("\n  ", "\n  ", "\n  "))
    lgroupsmul3.flatten.size shouldEqual lgroupsmul3.flatten.distinct.size
    lgroupsmul3.flatten.size shouldEqual lp.size
    lgroupsmul3.size shouldEqual 15

    println("\n***4*** " + modlo + " ***** 16 groupes pour les multiplications par 4")
    var lgroupsmul4 = List[List[(BigInt, BigInt)]]()

    lpm = lp
    while (!lpm.isEmpty) {
      val lmul = e.loopmul4(lpm.head)
      println("    " + lmul.size, lmul)
      lgroupsmul4 = lgroupsmul4 :+ lmul
      lpm = lpm.filter(p => !lmul.contains(p))
    }
    println(modlo, lgroupsmul4.size, lgroupsmul4.mkString("\n  ", "\n  ", "\n  "))
    lgroupsmul4.flatten.size shouldEqual lgroupsmul4.flatten.distinct.size
    lgroupsmul4.flatten.size shouldEqual lp.size
    lgroupsmul4.size shouldEqual 16
  }

  "Ordre67" should "be OK" in {
    println("Ordre67: ils ont tous le meme ordre!")
    val e = new Elliptique(67, 0, 7)
    val lp = e.curve.sortBy(p => (p._1 * 100) + p._2)
    println(67, lp.size, lp)

    var ordre = 0
    lp.foreach(p => {
      var somme = e.plus(p, p)
      (1 to 300).toList.find(i => {
        somme = e.plus(somme, p)
        if (somme._1 * somme._2 == 0) {
          println("===", p, i, somme, "===")
          ordre = i + 1
          ordre shouldEqual e.curve.size
        }
        somme._1 * somme._2 == 0
      })
    })
  }

  "Ordre241" should "be OK" in {
    println("Ordre241: ils n\'ont tous le meme ordre!")
    val e = new Elliptique(241, 0, 7)
    val lp = e.curve.sortBy(p => (p._1 * 241) + p._2)
    println(241, lp.size, lp)
    e.checkVerbose((BigInt(16), BigInt(214))) shouldEqual true
    e.checkVerbose((BigInt(16), BigInt(27))) shouldEqual true
    e.plus((BigInt(16), BigInt(214)), (BigInt(16), BigInt(27))) shouldEqual(BigInt(0), BigInt(0))
    var ordre = 0
    println(e.curve.size, lp.map(p => {
      var somme = e.plus(p, p)
      var lsum = List[(BigInt, BigInt)]()
      (1 to e.curve.size).toList.find(i => {
        somme = e.plus(somme, p)
        lsum = lsum :+ somme
        if (somme._1 * somme._2 == 0) {
          //println("===", p, i, somme, "===",lsum)
          ordre = i + 1
          //ordre shouldEqual e.curve.size
        }
        somme._1 * somme._2 == 0
      })
      (p, ordre)
    }).filter(_._2 != e.curve.size))
  }

  "Trouve les nombres premiers qui pourraient marcher pour une autre courbe" should "be OK" in {
    println("Trouve les nombres premiers qui pourraient marcher")
    println(premiers.take(100).filter(modlo => {
      val e = new Elliptique(modlo, 3, 5)
      e.getDelta should not equal 0
      e.curve.size > modlo & e.curve.filter(p => p._1 * p._2 == 0).isEmpty
    }))
  }

  "Ordre223" should "be OK" in {
    val modlo = 223
    println("Ordre" + modlo + ": ils n\'ont tous le meme ordre!")
    val e = new Elliptique(223, 3, 5)
    val lp = e.curve.sortBy(p => (p._1 * modlo) + p._2)
    println(223, lp.size, lp)
    var ordre = 0
    println(modlo, e.curve.size, lp.map(p => {
      var somme = e.plus(p, p)
      var lsum = List[(BigInt, BigInt)]()
      (1 to e.curve.size).toList.find(i => {
        somme = e.plus(somme, p)
        lsum = lsum :+ somme
        if (somme._1 * somme._2 == 0) {
          //println("===", p, i, somme, "===",lsum)
          ordre = i + 1
        }
        somme._1 * somme._2 == 0
      })
      (p, ordre)
    }).filter(_._2 != e.curve.size))


    e.checkVerbose((BigInt(4), BigInt(9))) shouldEqual true
    val p = (BigInt(4), BigInt(9))
    var somme = e.plus(p, p)
    var lsum = List[(BigInt, BigInt)]()
    (1 to e.curve.size).toList.find(i => {
      somme = e.plus(somme, p)
      lsum = lsum :+ somme
      if (somme._1 * somme._2 == 0) {
        println("===", p, i, somme, "===", lsum)
        ordre = i + 1
      }
      somme._1 * somme._2 == 0
    })
    e.plus((BigInt(4), BigInt(9)), e.plus((BigInt(4), BigInt(9)), (BigInt(4), BigInt(9))))._2 shouldEqual BigInt(0)
    e.checkVerbose((BigInt(85), BigInt(0))) shouldEqual true
  }


}
