package euler

import kebra.MyLog._
import EulerFactorielle._

object Permutations extends App {
    List("AAAAA", "AOOOO", "AAOOO", "AAOOL", "AAAOL", "AAOL", "AAOOLL", "AAAOLL", "AAAALL", "AAAAOL","AAABBCCCDEE").foreach(s => myAssert2(permLength(s), s.permutations.toList.length))
    myPrintDln("Alles klar!")

    def permLength(s: String) = {
        var melem = Map[Char,Int]()
        var lastChar = s.head
        s.foreach((c: Char) =>melem = melem + (c -> (melem.getOrElse(c,0)+1)))
        val result = fact(s.length) / melem.toList.map((t: (Char, Int)) => fact(t._2)).product

        //myPrintIt((s, result, melem, s.permutations.toList.length))
       result
    }
}