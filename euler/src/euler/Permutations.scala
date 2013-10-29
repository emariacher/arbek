package euler

import kebra.MyLog._
import EulerFactorielle._

object Permutations extends App {
    List("AAAAA", "AOOOO", "AAOOO", "AAOOL", "AAAOL", "AAOL", "AAOOLL", "AAAOLL", "AAAALL", "AAAAOL").foreach(perm3length)
    
    def perm3length(s: String) = {
        var lelem = List[(Char, Int, String)]()
        val grouped1 = s.groupBy((c: Char) => c == s.head)
        val grouped1b = grouped1.getOrElse(true, "")
        lelem = lelem :+ (s.head, grouped1b.length, grouped1b)
        val grouped2 = grouped1.getOrElse(false, "")
        if (!grouped2.isEmpty) {
            val grouped3 = grouped2.groupBy((c: Char) => c == grouped2.head)
            val grouped3b = grouped3.getOrElse(true, "")
            lelem = lelem :+ (grouped2.head, grouped3b.length, grouped3b)
            val grouped4 = grouped3.getOrElse(false, "")
            if (!grouped4.isEmpty) {
                lelem = lelem :+ (grouped2.last, grouped4.length, grouped4)
            }
        }
        lelem = lelem.sortBy { _._2 }.reverse
        val result = lelem.length match {
            case 1 => 1
            case 2 => combinations(s.length, lelem.head._2)
            case 3 => fact(s.length) / (fact(lelem.head._2) * fact(lelem.tail.head._2) * fact(lelem.last._2))
            case _ => 0
        }
        myPrintIt((s, result, lelem, s.permutations.toList.length, s.permutations.toList))
        myAssert2(result, s.permutations.toList.length)
        result
    }
}