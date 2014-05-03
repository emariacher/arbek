package enumParser

class ParsedEnum(val name: String, val bloc: Block) {
    var fields = bloc.lines.map((a: Any) => new Field(a.asInstanceOf[String]))

    override def toString = name+" -> "+fields

    def getN(value: Int): String = {
        val f = fields.find(_.value == value)
        f match {
            case Some(ff) => ff.name
            case _        => "("+name+")"+value.toString
        }
    }
}