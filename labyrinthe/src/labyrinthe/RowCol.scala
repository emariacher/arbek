package labyrinthe

class RowCol(val r: Int, val c: Int) {
    override def toString: String = "rc_" + r + "_" + c
    override def equals(x: Any): Boolean = hashCode.equals(x.asInstanceOf[RowCol].hashCode)
    override def hashCode: Int = toString.hashCode
    def moinsUn = new RowCol(r - 1, c - 1)
    def haut = new RowCol(r - 1, c)
    def bas = new RowCol(r + 1, c)
    def gauche = new RowCol(r, c - 1)
    def droite = new RowCol(r, c + 1)
}