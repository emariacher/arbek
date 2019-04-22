package graphlayout

import java.awt.Graphics2D

class Edge(val from: Node, val to: Node) {
  var len = .0
  var diff = .0
  var dist = (.0, .0, .0)
  var attraction = 1
  var repulsion = 200

  override def toString: String = from.toString + " -> " + to.toString

  def getNodesString = List(from.getID, to.getID).sortBy(_.hashCode)

  def getSign(d: Double) = {
    if (d > 0) 1.0
    else -1.0
  }

  def getDist = {
    val deltaX = to.x - from.x
    val deltaY = to.y - from.y
    dist = (Math.sqrt((deltaX * deltaX) + (deltaY * deltaY)), getSign(deltaX), getSign(deltaY))
    dist
  }


  def opTimize = { // quand il y a un lien, trouve la bonne distance
    //MyLog.myPrintln(toString)
    diff = len - dist._1
    val inc = Math.abs(diff) / 3
    val dx = (dist._2 * inc)
    val dy = (dist._3 * inc)
    from.update(from.x - (getSign(diff) * from.updateAverageX(dx)), from.y - (getSign(diff) * from.updateAverageY(dy)), Math.sqrt((dx * dx) + (dy * dy)))
    to.update(to.x + (getSign(diff) * to.updateAverageX(dx)), to.y + (getSign(diff) * to.updateAverageY(dy)), Math.sqrt((dx * dx) + (dy * dy)))
  }

  def rassemble = { // quand il y a un lien, rassemble
    //MyLog.myPrintln(toString)
    diff = len - dist._1
    val dx = (dist._2 * attraction)
    val dy = (dist._3 * attraction)
    from.update(from.x - (getSign(diff) * dx), from.y - (getSign(diff) * dy), Math.sqrt((dx * dx) + (dy * dy)))
    to.update(to.x + (getSign(diff) * dx), to.y + (getSign(diff) * dy), Math.sqrt((dx * dx) + (dy * dy)))
  }

  def ecarte = { // quand il n'y a pas de lien, ecarte toi au maximum
    //MyLog.myPrintln(toString)
    var inc = repulsion / dist._1 // plus ils sont loin l'un de l'autre, moins l'effet de repulsion est fort
    if (inc.isNaN) inc = 1
    if (inc.isInfinity) inc = 1
    val dx = inc
    val dy = inc
    var fromx = .0
    var fromy = .0
    var tox = .0
    var toy = .0
    if (from.x < to.x) {
      fromx = from.x - dx
      tox = to.x + dx
    } else {
      fromx = from.x + dx
      tox = to.x - dx
    }

    if (from.y < to.y) {
      fromy = from.y - dy
      toy = to.y + dy
    } else {
      fromy = from.y + dy
      toy = to.y - dy
    }
    from.update(fromx, fromy, Math.sqrt((dx * dx) + (dy * dy)))
    to.update(tox, toy, Math.sqrt((dx * dx) + (dy * dy)))
  }

  def paint(g: Graphics2D) {}
}
