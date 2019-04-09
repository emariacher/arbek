package graphlayout

import java.awt.{Graphics2D}

import scala.util.Random


abstract class Edge(val from: Node, val to: Node) {
  var len = .0
  var diff = .0
  var dist = .0

  def getNodesString = List(from.getID, to.getID).sortBy(_.hashCode)

  def getSign(d: Double) = {
    if (d > 0) 1.0
    else -1.0
  }

  def opTimize(rnd: Random) = { // quand il y a un lien, trouve la bonne distance
    //MyLog.myPrintln(toString)
    val deltaX = to.x - from.x
    val deltaY = to.y - from.y
    dist = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY))
    diff = len - dist
    val inc = Math.sqrt(Math.abs(diff))
    from.x = from.x - (getSign(diff) * getSign(deltaX) * inc * rnd.nextInt(3))
    from.y = from.y - (getSign(diff) * getSign(deltaY) * inc * rnd.nextInt(3))
    to.x = to.x + (getSign(diff) * getSign(deltaX) * inc * rnd.nextInt(3))
    to.y = to.y + (getSign(diff) * getSign(deltaY) * inc * rnd.nextInt(3))
  }

  def ecarte(rnd: Random) = { // quand il n'y a pas de lien, ecarte toi au maximum
    //MyLog.myPrintln(toString)
    val deltaX = to.x - from.x
    val deltaY = to.y - from.y
    val inc = 1
    from.x = from.x - (getSign(deltaX) * inc * rnd.nextInt(2))
    from.y = from.y - (getSign(deltaY) * inc * rnd.nextInt(2))
    to.x = to.x + (getSign(deltaX) * inc * rnd.nextInt(2))
    to.y = to.y + (getSign(deltaY) * inc * rnd.nextInt(2))
  }

  def paint(g: Graphics2D)
}
