package enumParser

class ParsedExecBlock(val title: String, val stuff: Parentheses, bloc: Block) {
    var returnedType = ""
    var name = ""
    var parms = List[String]()
    val title_splitted = title.split(" ").toList
    if (isFunction) {
        returnedType = title_splitted.head
        name = title_splitted.last
        parms = stuff.lines.map(_.toString)
    }

    def isFunction: Boolean = {
        title_splitted.size match {
            case 2 => title_splitted.head match {
                case "else" => false
                case _      => true
            }
            case _ => false
        }
    }

    override def toString = {
        if (isFunction) {
            "Function "+name+" ("+parms+")"
        } else {
            "ExecBlock "+title+" ("+stuff+")"
        }
    }
}