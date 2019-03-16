package graphlayout

import ZePanel._

import scala.swing.Panel
import akka.actor._
import java.awt.Graphics2D
import java.awt.Dimension
import java.awt.Color
import scala.math._
import scala.swing.Label
import Tableaux._
//import kebra.LL._
import kebra._
import scala.concurrent.duration._
import scala.language.postfixOps

class ZeActor extends Actor {
  context.setReceiveTimeout(1 second)

  def receive = {
    case ReceiveTimeout =>
      if ((!ZePanel.zp.pause) && (!ZePanel.zp.step)) ZePanel.zp.repaint; tbx.doZeJob("timeout", true)
    case slider: (String, Int) =>
      val slider_timeout = min(max(1, (slider._2 * slider._2 * slider._2) / 100), 5000)
      MyLog.myPrintIt(slider._2, slider_timeout)
      context.setReceiveTimeout(slider_timeout millisecond)
      ZePanel.zp.pause = (slider._2 == 0)
      ZePanel.zp.run = !ZePanel.zp.pause
      ZePanel.zp.step = false
    case "step" =>
      LL.l.myErrPrintDln("step")
      ZePanel.zp.repaint
      tbx.doZeJob("step", true)
      ZePanel.zp.step = true
    case "bloque" =>
      //l.myErrPrintDln("bloque")
      tbx.doZeJob("bloque", true)
  }
}

object ZePanel {
  var zp: ZePanel = _
  var za: ActorRef = _
  implicit val system = MyLog.system

  def newZePanel(lbl: Label, maxRC: RowCol, ptype: PanelType.Value) {
    zp = new ZePanel(lbl, maxRC, ptype)
    //za = ActorDSL.actor(new ZeActor)
    za = system.actorOf(Props[ZeActor], "zePanelActor")
  }
}

class ZePanel(val lbl: Label, val maxRC: RowCol, val ptype: PanelType.Value) extends Panel {
  var pause = false
  var step = false
  var run = false
  var largeur = 1000
  val hauteur = 700
  var limit: Int = _
  preferredSize = new Dimension(largeur, hauteur)
  val origin = new Dimension(0, 0)
  newTbx(this, maxRC, preferredSize, origin)

  limit = ptype match {
    case PanelType.LABY => 1000
    case PanelType.FOURMI => 5000
    case PanelType.FOURMILIERES => 5000
  }

  override def paint(g: Graphics2D) {
    g.setColor(Color.white)
    g.fillRect(0, 0, largeur, hauteur)

    g.setColor(Color.black)
    tbx.lc.foreach(_.paint(g))
  }
}

object PanelType extends Enumeration {
  type PanelType = Value
  val LABY, FOURMI, FOURMILIERES = Value
  PanelType.values foreach println
}
