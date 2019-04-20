package graphlayout

import java.awt.Graphics2D

import graphlayout.Tableaux.tbx
import kebra.MyLog

import scala.math.{max, min}
import scala.util.Random

class GraphLayout extends GraphAbstract {
  var MouseState = MouseStateMachine.reset
  var nearestNode: GNode = _

  val sinput = "un-deux, deux-trois, trois-un, un-quatre, cinq-quatre,dix-onze,six-sept,six-huit,six-neuf,sept-huit,sept-neuf,huit-neuf"
  val ledges1 = java.util.regex.Pattern.compile(",").split(sinput).map(t => {
    val ln = java.util.regex.Pattern.compile("-").split(t.trim).map(new GNode(_))
    new GEdge(ln.head, ln.last)
  })
  val lnodes = ledges1.map(_.getNodes).flatten.distinct
  val ledges = ledges1.map(e => {
    new GEdge(lnodes.filter(_ == e.from).head, lnodes.filter(_ == e.to).head)
  })
  val lallpossibleedges = lnodes.map(_.getID).combinations(2).map(_.sortBy(_.hashCode)).toList
  val lzedges = ledges.map(_.getNodes.map(_.getID).sortBy(_.hashCode))
  val lnoedges = lallpossibleedges.filter(e => lzedges.filter(_.mkString == e.mkString).isEmpty).map(c => {
    new GEdge(lnodes.filter(_.getID == c.head).head, lnodes.filter(_.getID == c.last).head)
  })
  lnoedges.foreach(_.len = 500)
  MyLog.myPrintIt(lnoedges.mkString("+"))

  MyLog.myPrintIt(sinput, "[", lnodes.mkString("!"), "][", ledges.mkString("/"), "]")

  def rassemble: StateMachine = {
    ledges.foreach(_.getDist)
    ledges.foreach(_.opTimize)
    lnoedges.filter(_.getDist._1 < 500).foreach(_.ecarte)
    lnodes.foreach(_.remetsDansLeTableau(tbx.zp.largeur, tbx.zp.hauteur, 10))
    if (nearestNode != null) {
      MyLog.myPrintln(nearestNode.lbl, nearestNode.slidingAverageDeltax, nearestNode.slidingAverageDeltay)
    }
    StateMachine.rassemble
  }

  def reset: StateMachine = {
    tbx.seed = tbx.getNextSeed
    tbx.rnd = new Random(tbx.seed)
    lnodes.foreach(n => {
      n.x = tbx.rnd.nextDouble() * tbx.zp.largeur
      n.y = tbx.rnd.nextDouble() * tbx.zp.hauteur
    })
    ledges.foreach(_.len = 50 + tbx.rnd.nextDouble() * 200)
    StateMachine.rassemble
  }

  def doZeMouseJob(mouse: (String, Int, Int)): Unit = {
    //MyLog.myPrintIt(MouseState, mouse)
    MouseState match {
      case MouseStateMachine.drag =>
        mouse._1 match {
          case "MouseR" =>
            //MyLog.myPrintIt(mouse)
            //nearestNode = null
            MouseState = MouseStateMachine.reset
          case "MouseD" =>
            nearestNode.x = mouse._2
            nearestNode.y = mouse._3
          //MyLog.myPrintIt(mouse, nearestNode)
          case _ => //MyLog.myPrintIt(mouse)
        }
      case MouseStateMachine.reset =>
        mouse._1 match {
          case "MouseP" =>
            val nearestFNode = lnodes.map(n => (n, n.pasLoin(mouse._2, mouse._3))).sortBy(_._2).head
            if (nearestFNode._2 < 80) {
              nearestNode = nearestFNode._1
              MyLog.myPrintIt(mouse, nearestNode)
              MouseState = MouseStateMachine.drag
            } else {
              MyLog.myPrintIt(lnodes.map(n => (n, n.pasLoin(mouse._2, mouse._3))).sortBy(_._2).mkString)
            }
          case _ => //MyLog.myPrintIt(mouse)
        }
      case _ =>
    }
  }

  def doZeSliderJob(slider: (String, Int)): Unit = {
    slider_timeout = min(max(1, (slider._2 * slider._2) / 100), 5000)
    MyLog.myPrintIt(slider._1, slider._2, slider_timeout)
    ZePanel.zp.pause = (slider._2 == 0)
    ZePanel.zp.run = !ZePanel.zp.pause
    ZePanel.zp.step = false
  }

  def paint(g: Graphics2D): Unit = {
    ledges.foreach(_.paint(g))
    lnodes.foreach(_.paint(g))
  }
}
