package graphlayout

import graphlayout.Tableaux._

class RowCol(val r: Int, val c: Int) {
    override def toString: String = "rc_" + r + "_" + c
    override def equals(x: Any): Boolean = hashCode.equals(x.asInstanceOf[RowCol].hashCode)
    override def hashCode: Int = toString.hashCode
    def moinsUn = new RowCol(r - 1, c - 1)
    def haut = new RowCol(r - 1, c).valid
    def bas = new RowCol(r + 1, c).valid
    def gauche = new RowCol(r, c - 1).valid
    def droite = new RowCol(r, c + 1).valid

    def valid = {
        if ((r < 0) || (c < 0) || (r > (tbx.maxRow+1)) || (c > (tbx.maxCol+1))) {
            new RowCol(888,888)
        } else {
            this
        }
    }
}