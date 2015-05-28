import org.scalatest._

import scala.collection.immutable.ListSet

class EulerMain extends FlatSpec with Matchers {
  "Euler89" should "be OK" in {
    println("Euler89")

    val M = 'M'
    val D = 'D'
    val C = 'C'
    val L = 'L'
    val X = 'X'
    val V = 'V'
    val I = 'I'
    var state = '0'
    var nombre = 0


    val url = "https://projecteuler.net/project/resources/p089_roman.txt"
    val romnumList = io.Source.fromURL(url).mkString.split("\n").toList
    //val romnumList = List("M", "I")

    //println(romnumList)
    romnumList.map(romnum => {
      nombre = 0
      state = '0'
      val z = romnum.map(c => {
        val value = c match {
          case M => 1000
          case D => 500
          case C => 100
          case L => 50
          case X => 10
          case V => 5
          case I => 1
        }
        state match {
          case C => c match {
            case M => nombre += 800
            case D => nombre += 300
            case _ => nombre += value
          }
          case X => c match {
            case C => nombre += 80
            case L => nombre += 30
            case _ => nombre += value
          }
          case I => c match {
            case X => nombre += 8
            case V => nombre += 3
            case _ => nombre += value
          }
          case _ => nombre += value
        }
        state = c
        (state, c, value, nombre)
      })
      println(romnum, nombre)
      (romnum, nombre)
    }).map(rn => {
      val mille = rn._2 / 1000
      val cent = (rn._2 - (mille*1000)) / 100
      val dix = (rn._2 - ((mille*1000) + (cent*100))) / 10
      val un = (rn._2 - ((mille*1000) + (cent*100) + (dix*10)))
      println(rn._1, rn._2, mille, cent, dix, un)
      (rn._1, rn._2, mille, cent, dix, un)
    })

    var result = 0
    println("Euler89[" + result + "]")
    result should be === 0

  }
}
