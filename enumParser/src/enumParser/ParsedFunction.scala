package enumParser

class ParsedFunction(val quarte: List[Any]) {
    val name = quarte.head.asInstanceOf[String].split(" ").toList.last
    
    override def toString = name
}