package graphlayout

import Tableaux._

class RowCol(val r: Int, val c: Int) {
    override def toString: String = "rc_" + r + "_" + c
    override def equals(x: Any): Boolean = hashCode.equals(x.asInstanceOf[RowCol].hashCode)
    override def hashCode: Int = toString.hashCode
    def moinsUn = new RowCol(r - 1, c - 1)
    def haut = new RowCol(r - 1, c).valid
    def bas = new RowCol(r + 1, c).valid
    def gauche = new RowCol(r, c - 1).valid
    def droite = new RowCol(r, c + 1).valid
    /*def isNorth(rc: RowCol) = rc.equals(haut)
    def isSouth(rc: RowCol) = rc.equals(bas)
    def isWest(rc: RowCol) = rc.equals(gauche)
    def isEast(rc: RowCol) = rc.equals(droite)
    def direction(rc: RowCol): Frontiere = {
        if(isNorth(rc)) {
            return FrontiereV.nord
        } else if(isSouth(rc)) {
            return FrontiereV.sud
        } else if(isWest(rc)) {
            return FrontiereV.ouest
        } else {
            return FrontiereV.est
        }
    }*/
    def valid = {
        if ((r < 0) || (c < 0) || (r > (tbx.maxRow+1)) || (c > (tbx.maxCol+1))) {
            new RowCol(888,888)
        } else {
            this
        }
    }
}