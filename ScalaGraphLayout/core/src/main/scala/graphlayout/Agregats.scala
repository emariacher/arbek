package graphlayout

import java.awt.Graphics2D

import graphlayout.Tableaux.tbx
import kebra.MyLog

import scala.util.Random

class Agregats extends GraphAbstract {
  var MouseState = MouseStateMachine.reset
  var nearestNode: ANode = _
  val number = 20

  val lOrangeNodes = (1 to number).toList.map(z => new ANode(Tribu.Orange))
  val lGreenNodes = (1 to number).toList.map(z => new ANode(Tribu.Vert))
  val lnodes = lOrangeNodes ++ lGreenNodes
  // believe me or not but vals are volatile
  var ledges = List[AEdge]()
  (lOrangeNodes.combinations(2) ++ lGreenNodes.combinations(2)).foreach(c => ledges = ledges :+ new AEdge(c.head, c.last))
  val ledges2 = (lOrangeNodes.combinations(2).map(c => new AEdge(c.head, c.last)) ++ lGreenNodes.combinations(2).map(c => new AEdge(c.head, c.last)))
  val ledges3 = (lOrangeNodes.combinations(2) ++ lGreenNodes.combinations(2)).map(c => new AEdge(c.head, c.last))
  MyLog.myPrintIt(ledges.mkString("\n -1-", "\n -1-", "\n -1-"), ledges2.mkString("\n -2-", "\n -2-", "\n -2-"), ledges3.mkString("\n -3-", "\n -3-", "\n -3-"))
  MyLog.myPrintIt(ledges.mkString("\n -1-", "\n -1-", "\n -1-"), ledges2.mkString("\n -2-", "\n -2-", "\n -2-"), ledges3.mkString("\n -3-", "\n -3-", "\n -3-"))
  val lallpossibleedges = lnodes.map(_.getID).combinations(2).map(_.sortBy(_.hashCode)).toList
  MyLog.myPrintIt(ledges.mkString("\n -"))
  val lzedges = ledges.map(_.getNodes.map(_.getID).sortBy(_.hashCode))
  MyLog.myPrintIt(ledges.mkString("\n -"))
  val lnoedges = lallpossibleedges.filter(e => lzedges.filter(_.mkString == e.mkString).isEmpty).map(c => {
    new AEdge(lnodes.filter(_.getID == c.head).head, lnodes.filter(_.getID == c.last).head)
  })

  MyLog.myPrintIt(ledges.mkString("\n -"))
  MyLog.myPrintIt(lnoedges.mkString("\n %"))

  def genere: StateMachine = {
    ledges.foreach(_.getDist)
    ledges.foreach(_.rassemble)
    lnoedges.filter(_.getDist._1 < 500).foreach(_.ecarte(200))
    lnodes.foreach(_.remetsDansLeTableau(tbx.zp.largeur, tbx.zp.hauteur, 10))
    if (nearestNode != null) {
      MyLog.myPrintln("/%.2f".format(nearestNode.slidingAverageDeltax), "/%.2f".format(nearestNode.slidingAverageDeltay))
    }
    StateMachine.genere
  }

  def reset: StateMachine = {
    tbx.seed = tbx.getNextSeed
    tbx.rnd = new Random(tbx.seed)
    lnodes.foreach(n => {
      n.x = tbx.rnd.nextDouble() * tbx.zp.largeur
      n.y = tbx.rnd.nextDouble() * tbx.zp.hauteur
    })
    ledges.foreach(_.len = 10)
    StateMachine.genere
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
              if (nearestNode != null) {
                nearestNode.selected = false
              }
              nearestNode = nearestFNode._1
              nearestNode.selected = true
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

  def paint(g: Graphics2D): Unit = {
    ledges.foreach(_.paint(g))
    lnodes.foreach(_.paint(g))
  }
}
