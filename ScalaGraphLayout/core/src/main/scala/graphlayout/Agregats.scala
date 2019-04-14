package graphlayout

import java.awt.Graphics2D

import graphlayout.Tableaux.tbx
import kebra.MyLog

import scala.math.{max, min}
import scala.util.Random

class Agregats extends GraphAbstract {
  var MouseState = MouseStateMachine.reset
  var nearestNode: ANode = _
  val number = 20

  val ltribus = Tribu.tribus.map(t => (1 to number).toList.map(z => new ANode(t)))
  val lnodes = ltribus.flatten
  var ledges = ltribus.map(tl => tl.combinations(2)).flatten.map(c => new AEdge(c.head, c.last))
  val lzedges = ledges.map(_.getNodes.map(_.getID).sortBy(_.hashCode))
  val lnoedges = lnodes.map(_.getID).combinations(2).map(_.sortBy(_.hashCode)).toList.filter(e => lzedges.filter(_.mkString == e.mkString).isEmpty).map(c => {
    new AEdge(lnodes.filter(_.getID == c.head).head, lnodes.filter(_.getID == c.last).head)
  })

  MyLog.myPrintIt(ledges.mkString("\n -"))
  MyLog.myPrintIt(lnoedges.mkString("\n %"))

  def genere: StateMachine = {
    ledges.foreach(_.getDist)
    ledges.foreach(_.rassemble)
    lnoedges.filter(_.getDist._1 < 500).foreach(_.ecarte)
    lnodes.foreach(_.remetsDansLeTableau(tbx.zp.largeur, tbx.zp.hauteur, 10))
    if (nearestNode != null) {
      MyLog.myPrintln("/%.2f".format(nearestNode.slidingAverageDeltax), "/%.2f".format(nearestNode.slidingAverageDeltay))
    }
    MyLog.myPrintln("*******************************************")
    Tribu.tribus.foreach(t => {
      t.label.text = ", " + dispersion(t)
      MyLog.myPrintIt(t.c.couleur, listeAgregats(t, 200).mkString("\n  ", "\n  ", "\n"))
    })
    StateMachine.genere
  }

  def getEdges(tribu: Tribu) = ledges.filter(e => !e.getNodes.filter(n => n.tribu == tribu).isEmpty)

  def getEdges(l: List[AEdge], node: ANode) = l.filter(e => !e.getNodes.filter(n => node == n).isEmpty)

  def getEdges(node: ANode) = ledges.filter(e => !e.getNodes.filter(n => node == n).isEmpty)

  def getNodes(tribu: Tribu) = lnodes.filter(n => n.tribu == tribu)

  def dispersion(tribu: Tribu): Int = {
    (getEdges(tribu).map(_.dist._1).sum / number).toInt
  }

  def centre(tribu: Tribu): (Double, Double) = {
    var centrex = .0
    var centrey = .0

    lnodes.filter(n => n.tribu == tribu).map(n => (n.x, n.y)).foreach(p => {
      centrex += p._1
      centrey += p._2
    })
    (centrex, centrey)
  }

  def listeAgregats(tribu: Tribu, radius: Int) = {
    var lzedges = getEdges(tribu)
    MyLog.myPrintIt(tribu.c.couleur, getNodes(tribu).map(n => "[%.0f".format(n.x) + ",%.0f]".format(n.y)).mkString(", "))
    getNodes(tribu).map(n => {
      //MyLog.myPrintIt(tribu.c.couleur, "[%.0f".format(n.x) + ",%.0f]".format(n.y))
      val zorg = getEdges(lzedges, n).partition(e => e.dist._1 < radius)
      if (!zorg._1.isEmpty) {
        //MyLog.myPrintIt(zorg._1.mkString(", "))
        lzedges = lzedges.filter(e => !zorg._2.filter(e2 => e2 == e).isEmpty)
        //MyLog.myPrintIt(lzedges.mkString("~ "))
      }
      zorg._1.map(e => e.getNodes).flatten.distinct.map(n => "[%.0f".format(n.x) + ",%.0f]".format(n.y))
    }).filter(!_.isEmpty)
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

  override def doZeSliderJob(slider: (String, Int)): Unit = {
    MyLog.myPrintIt(slider._1, slider._2)
    slider._1 match {
      case "Repulsion" =>
        if (nearestNode != null) {
          lnoedges.filter(e => !e.getNodes.filter(n => n.tribu == nearestNode.tribu).isEmpty).foreach(e => e.repulsion = slider._2 * 20)
        } else {
          MyLog.myPrintIt(slider._1, slider._2, ledges.head.repulsion)
          lnoedges.foreach(e => e.repulsion = slider._2 * 20)
          MyLog.myPrintIt(slider._1, slider._2, ledges.head.repulsion)
        }
      case "Attraction" =>
        if (nearestNode != null) {
          ledges.filter(e => !e.getNodes.filter(n => n.tribu == nearestNode.tribu).isEmpty).foreach(e => e.attraction = max(slider._2 / 10, 1))
        } else {
          ledges.foreach(e => e.attraction = max(slider._2 / 10, 1))
        }
      case _ =>
        slider_timeout = min(max(1, (slider._2 * slider._2) / 100), 5000)
        MyLog.myPrintIt(slider._1, slider._2, slider_timeout)
        ZePanel.zp.pause = (slider._2 == 0)
        ZePanel.zp.run = !ZePanel.zp.pause
        ZePanel.zp.step = false
    }
  }

  def paint(g: Graphics2D): Unit = {
    ledges.foreach(_.paint(g))
    lnodes.foreach(_.paint(g))
  }
}
