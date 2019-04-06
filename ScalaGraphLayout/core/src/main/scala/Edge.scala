package graphlayout

import java.awt.{Color, Graphics2D}

import graphlayout.Tableaux.tbx
import kebra.MyLog

class Edge(val from: GNode, val to: GNode) {
  var len = .0
  var diff = .0

  override def toString: String = from.lbl + (from.x.toInt, from.y.toInt).toString + "->" + to.lbl + (to.x.toInt, to.y.toInt).toString + "[%.2f".format(diff) +"/%.2f]".format(len)

  def getNodes = List(from, to)

  def getSign(d: Double) = {
    if (d > 0) 1.0
    else -1.0
  }

  def opTimize = {
    MyLog.myPrintln(toString)
    val deltaX = to.x - from.x
    val deltaY = to.y - from.y
    MyLog.myPrintln("                 ",deltaX,deltaY)
    diff = len - Math.sqrt((deltaX * deltaX) + (deltaY * deltaY))
    from.x = from.x - (getSign(diff) * getSign(deltaX))
    from.y = from.y - (getSign(diff) * getSign(deltaY))
    to.x = to.x + (getSign(diff) * getSign(deltaX))
    to.y = to.y + (getSign(diff) * getSign(deltaY))
    MyLog.myPrintln("                 ",toString)
  }

  def paint(g: Graphics2D): Unit = {
    g.setColor(Color.green)
    g.drawLine(from.x.toInt, from.y.toInt, to.x.toInt, to.y.toInt)
  }
}
