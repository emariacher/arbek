package graphlayout

import java.awt.Graphics2D

import kebra.MyLog._

class Edge(val from: Node, val to: Node) {
  var len = .0
  var diff = .0
  var dist = (.0, .0, .0)
  var attraction = 1.0
  var repulsion = 200.0

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

  def checkInRange(to: Node, from: Node, tonew: Node, fromnew: Node) = {
    val lx = List(to.x, from.x, tonew.x, fromnew.x).sorted
    val ly = List(to.y, from.y, tonew.y, fromnew.y).sorted
    /*myPrintDln(to, from, tonew, fromnew)
    myPrintDln(lx.map("%.2f".format(_)).mkString("lx{", ",", "}"), ly.map("%.2f".format(_)).mkString("ly{", ",", "}"))*/
    myAssert2(lx.tail.contains(tonew.x), true)
    myAssert2(lx.reverse.tail.contains(tonew.x), true)
    myAssert2(lx.tail.contains(fromnew.x), true)
    myAssert2(lx.reverse.tail.contains(fromnew.x), true)
    myAssert2(ly.tail.contains(tonew.y), true)
    myAssert2(ly.reverse.tail.contains(tonew.y), true)
    myAssert2(ly.tail.contains(fromnew.y), true)
    myAssert2(ly.reverse.tail.contains(fromnew.y), true)
  }

  def opTimize = { // quand il y a un lien, trouve la bonne distance
    diff = len - dist._1
    val signdiff = getSign(diff)
    val inc = Math.abs(diff) / 3
    val dx = (dist._2 * inc)
    val dy = (dist._3 * inc)
    //myPrintIt(toString, len, diff, "%02f  %02f %02f".format(inc, dx, dy))
    from.update(from.x - (signdiff * from.updateAverageX(dx)), from.y - (signdiff * from.updateAverageY(dy)), Math.sqrt((dx * dx) + (dy * dy)))
    to.update(to.x + (signdiff * to.updateAverageX(dx)), to.y + (signdiff * to.updateAverageY(dy)), Math.sqrt((dx * dx) + (dy * dy)))
  }

  def rassemble = { // quand il y a un lien, rassemble
    //MyLog.myPrintln(toString)
    val oldto = new Node(to)
    val oldfrom = new Node(from)
    val signdiff = getSign(diff)
    val dx = (dist._2 * Math.min(attraction, Math.abs(to.x - from.x)))
    val dy = (dist._3 * Math.min(attraction, Math.abs(to.y - from.y)))
    myAssert2(dx.isNaN, false)
    myAssert2(dy.isNaN, false)
    from.update(from.x - dx, from.y - dy, Math.sqrt((dx * dx) + (dy * dy)))
    /*myPrintDln("         avant " + to, tbx.findCarre(to.x, to.y), from, tbx.findCarre(from.x, from.y))
    myPrintIt(dx, dy)*/
    to.update(to.x + dx, to.y + dy, Math.sqrt((dx * dx) + (dy * dy)))
    //myPrintDln("         apres " + to, tbx.findCarre(to.x, to.y))
    checkInRange(oldto, oldfrom, to, from)
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
