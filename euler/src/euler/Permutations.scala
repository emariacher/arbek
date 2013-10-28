package euler

import kebra.MyLog._

object Permutations extends App {
    perm3length("AAAAA")
    perm3length("AOOOO")
    perm3length("AAOOO")
    perm3length("AAOOL")
    def perm3length(s: String) {
        var lelem = List[(Char, String)]()
        val grouped1 = s.groupBy((c: Char) => c == s.head)
        lelem = lelem :+ (s.head, grouped1.getOrElse(true, ""))
        val grouped2 = grouped1.getOrElse(false, "")
        if (!grouped2.isEmpty) {
            val grouped3 = grouped2.groupBy((c: Char) => c == grouped2.head)
            lelem = lelem :+ (grouped2.head, grouped3.getOrElse(true, ""))
            val grouped4 = grouped3.getOrElse(false, "")
            if (!grouped4.isEmpty) {
                lelem = lelem :+ (grouped2.last, grouped4)
            }
        }
        myPrintIt(lelem)
    }
}