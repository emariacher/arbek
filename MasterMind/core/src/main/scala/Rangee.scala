package mm

import kebra.MyLog._
import mm.Tableau._

class Rangee(val l: List[Int]) {
  def this() = this((0 until tbl.maxJeton).toList.map((j: Int) => tbl.rnd.nextInt(tbl.maxCouleur)))

  var hypo = l
  var noirs = 0
  var blancs = 0
  myAssert2(l.length, tbl.maxJeton)

  def ?(r: Rangee): Boolean = {
    val z = hypo.zip(r.hypo)
    noirs = z.filter((c: (Int, Int)) => c._1 == c._2).length
    val uz = z.filter((c: (Int, Int)) => c._1 != c._2).unzip
    blancs = uz._1.intersect(uz._2).length
    myAssert2(noirs + blancs <= tbl.maxJeton, true)
    //myPrintIt("?",this,r)
    myPrint(".")
    noirs == r.noirs & blancs == r.blancs
  }

  override def toString: String = hypo.mkString("[[", ", ", "]n: ") + noirs + ", b: " + blancs + "]"
}