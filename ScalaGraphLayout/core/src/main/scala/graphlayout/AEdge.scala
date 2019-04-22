package graphlayout

import java.awt.{Color, Graphics2D}

class AEdge(from: ANode, to: ANode) extends Edge(from, to) {

  //override def toString: String = (from.x.toInt, from.y.toInt).toString + "->" + (to.x.toInt, to.y.toInt).toString + "[%.2f".format(len) + "/%.2f".format(dist._1) + "/%.2f]".format(diff)
  override def toString: String = (from.x.toInt, from.y.toInt).toString + "->" + (to.x.toInt, to.y.toInt).toString

  def getNodes = List(from, to)
}
