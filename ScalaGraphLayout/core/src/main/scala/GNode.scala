package graphlayout

class GNode(val lbl: String) {
  var x = .0
  var y = .0

  var dx: Double = .0
  var dy: Double = .0

  var fixed = false

  override def toString: String = "{" + lbl + "}"

  override def hashCode: Int = lbl.hashCode
  override def equals(anyo: Any): Boolean = {
    anyo match {
      case g: GNode => hashCode.equals(g.hashCode)
      case _ => false
    }
  }

}
