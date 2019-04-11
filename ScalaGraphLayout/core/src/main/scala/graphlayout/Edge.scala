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
    from.x = from.x - (getSign(diff) * dist._2 * inc * rnd.nextInt(3))
    from.y = from.y - (getSign(diff) * dist._3 * inc * rnd.nextInt(3))
    to.x = to.x + (getSign(diff) * dist._2 * inc * rnd.nextInt(3))
    to.y = to.y + (getSign(diff) * dist._3 * inc * rnd.nextInt(3))
  }

  def ecarte(rnd: Random) = { // quand il n'y a pas de lien, ecarte toi au maximum
    //MyLog.myPrintln(toString)
    val inc = 1
    from.x = from.x - (dist._2 * inc * rnd.nextInt(2))
    from.y = from.y - (dist._3 * inc * rnd.nextInt(2))
    to.x = to.x + (dist._2 * inc * rnd.nextInt(2))
    to.y = to.y + (dist._3 * inc * rnd.nextInt(2))
  }

  def paint(g: Graphics2D)
}
