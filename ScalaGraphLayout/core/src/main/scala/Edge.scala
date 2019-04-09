package graphlayout

import java.awt.{Color, Graphics2D}

import scala.util.Random


class Edge(val from: GNode, val to: GNode) {
  var len = .0
  var diff = .0
  var dist = .0

  override def toString: String = from.lbl + (from.x.toInt, from.y.toInt).toString + "->" + to.lbl + (to.x.toInt, to.y.toInt).toString + "[%.2f".format(len) + "/%.2f".format(dist) + "/%.2f]".format(diff)

  def getNodes = List(from, to)

  def getNodesString = List(from.lbl, to.lbl).sortBy(_.hashCode)

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

  def paint(g: Graphics2D): Unit = {
    if (Math.abs(dist.toInt - len.toInt) > 40) {
      g.setColor(Color.red)
    } else if (Math.abs(dist.toInt - len.toInt) > 10) {
      g.setColor(Color.orange)
    } else {
      g.setColor(Color.green)
    }
    g.drawLine(from.x.toInt, from.y.toInt, to.x.toInt, to.y.toInt)
    g.drawString(dist.toInt + "/" + len.toInt, (from.x.toInt + to.x.toInt) / 2, (from.y.toInt + to.y.toInt) / 2)
  }
}
