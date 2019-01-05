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
  var slider_timeout : Int = 20

  def receive = {
    case ReceiveTimeout =>
      if ((!ZePanel.zp.pause) && (!ZePanel.zp.step)) ZePanel.zp.repaint; tbx.doZeJob("timeout", slider_timeout)
    case slider: (String, Int) =>
      slider_timeout = min(max(1, (slider._2 * slider._2 * slider._2) / 100), 5000)
      MyLog.myPrintIt(slider._2, slider_timeout)
      context.setReceiveTimeout(slider_timeout millisecond)
      ZePanel.zp.pause = (slider._2 == 0)
      ZePanel.zp.run = !ZePanel.zp.pause
      ZePanel.zp.step = false
      ZePanel.zp.step = false
    case "step" =>
      l.myErrPrintDln("step")
      ZePanel.zp.repaint
      tbx.doZeJob("step",0)
      context.setReceiveTimeout(10 minutes)
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
