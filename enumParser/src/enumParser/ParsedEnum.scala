package enumParser

class ParsedEnum {
    var name = ""
    var fields = List[Field]()

    def :+(f: Field) = fields = fields :+ f
    def updateName(z: String) = name = z

    override def toString = name+" -> "+fields

    def getN(value: Int): String = {
        val f = fields.find(_.value == value)
        f match {
            case Some(ff) => ff.name
            case _        => "("+name+")"+value.toString
        }
    }
}