package graphlayout

class Edge(val from: GNode, val to: GNode) {
  var len = .0

  override def toString: String = from.lbl + "->" + to.lbl

  def getNodes = List(from, to)
}
