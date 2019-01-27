package mm

import java.awt.{Color, Dimension, Graphics2D}

import akka.actor._
import kebra._
import mm.Tableau._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.swing.{Label, Panel}

class ZeActor extends Actor {
  context.setReceiveTimeout(1 second)

  def receive = {
    case ReceiveTimeout =>
      ZePanel.zp.repaint
      tbl.doZeJob("step", true)
      ZePanel.zp.step = true
    case "step" =>
      LL.l.myErrPrintDln("step")
      ZePanel.zp.repaint
      tbl.doZeJob("step", true)
      ZePanel.zp.step = true
  }
}

object ZePanel {
  var zp: ZePanel = _
  var za: ActorRef = _
  implicit val system = MyLog.system

  def newZePanel(lbl: Label) {
    zp = new ZePanel(lbl)
    //za = ActorDSL.actor(new ZeActor)
    za = system.actorOf(Props[ZeActor], "zePanelActor")
  }
}

class ZePanel(val lbl: Label) extends Panel {
  var pause = false
  var step = false
  var run = false
  var largeur = 1000
  val hauteur = 700
  var timeout = 2000
  preferredSize = new Dimension(largeur, hauteur)
  val origin = new Dimension(0, 0)
  newTbl


  override def paint(g: Graphics2D) {
    g.setColor(Color.white)
    g.fillRect(0, 0, largeur, hauteur)

    g.setColor(Color.black)
  }
}
