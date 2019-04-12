package graphlayout

import java.awt.Graphics2D

import scala.util.Random


abstract class Edge(val from: Node, val to: Node) {
  var len = .0
  var diff = .0
  var dist = (.0, .0, .0)

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


  def opTimize(rnd: Random) = { // quand il y a un lien, trouve la bonne distance
    //MyLog.myPrintln(toString)
    diff = len - dist._1
    val inc = Math.sqrt(Math.abs(diff))
    val dx = (dist._2 * inc * (rnd.nextInt(1)+3))
    val dy = (dist._3 * inc * (rnd.nextInt(1)+3))
    from.x = from.x - (getSign(diff) * from.updateAverageX(dx))
    from.y = from.y - (getSign(diff) * from.updateAverageY(dy))
    to.x = to.x + (getSign(diff) * to.updateAverageX(dx))
    to.y = to.y + (getSign(diff) * to.updateAverageY(dy))
  }

  def ecarte(rnd: Random) = { // quand il n'y a pas de lien, ecarte toi au maximum
    //MyLog.myPrintln(toString)
    val inc = 1
    val dx = (inc * (rnd.nextInt(1)+1))
    val dy = (inc * (rnd.nextInt(1)+1))
    if (from.x < to.x) {
      from.x = from.x - dx
      to.x = to.x + dx
    } else {
      from.x = from.x + dx
      to.x = to.x - dx
    }

    if (from.y < to.y) {
      from.y = from.y - dy
      to.y = to.y + dy
    } else {
      from.y = from.y + dy
      to.y = to.y - dy
    }
  }

  def paint(g: Graphics2D)
}
