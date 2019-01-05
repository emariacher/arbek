package carte

import java.awt.{Color, Dimension, Graphics2D}

import akka.actor._
import carte.Tableaux._
import kebra.LL._
import kebra.MyLog

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.math._
import scala.swing.{Label, Panel}

class ZeActor extends Actor {
  context.setReceiveTimeout(1 second)

  def receive = {
    case ReceiveTimeout =>
      if ((!ZePanel.zp.pause) && (!ZePanel.zp.step)) ZePanel.zp.repaint; tbx.doZeJob("timeout")
    case slider: (String, Int) =>
      ZePanel.zp.pause = slider._2 == 0
      ZePanel.zp.run = false
      if (slider._2 == 100) {
        context.setReceiveTimeout(1 millisecond)
        ZePanel.zp.run = true
      } else {
        context.setReceiveTimeout(max(500, slider._2 * 10) millisecond)
      }
      ZePanel.zp.step = false
    case "step" =>
      l.myErrPrintDln("step")
      ZePanel.zp.repaint
      tbx.doZeJob("step")
      ZePanel.zp.step = true
  }
}

object ZePanel {
  var zp: ZePanel = _
  var za: ActorRef = _
  implicit val system = MyLog.system

  def newZePanel(lbl: Label, maxRow: Int, maxCol: Int) {
    zp = new ZePanel(lbl, maxRow, maxCol)
    za = ActorDSL.actor(new ZeActor)
  }
}

class ZePanel(val lbl: Label, val maxRow: Int, val maxCol: Int) extends Panel {
  var pause = false
  var step = false
  var run = false
  var timeout = 1000
  var largeur = 1000
  val hauteur = 700
  var limit: Int = _
  preferredSize = new Dimension(largeur, hauteur)
  val origin = new Dimension(0, 0)
  newTbx(this, maxRow, maxCol, preferredSize, origin)

  override def paint(g: Graphics2D) {
    g.setColor(Color.black)
    tbx.cb.lcarres.lc.foreach(_.paint(g))
  }
}
