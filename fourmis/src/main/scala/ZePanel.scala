package labyrinthe
import scala.swing.Panel
import akka.actor._
import akka.actor.ActorDSL._
import java.awt.Graphics2D
import java.awt.Dimension
import java.awt.Color
import scala.math._
import java.io.File
import scala.swing.Label
import labyrinthe.Tableaux._
import kebra.MyLog
import kebra.MyLog._
import scala.concurrent.duration._
import labyrinthe.LL._
import scala.language.postfixOps 

class ZeActor extends Actor {
    context.setReceiveTimeout(1 second)
    def receive = {
        case ReceiveTimeout =>
            if ((!ZePanel.zp.pause) && (!ZePanel.zp.step)) ZePanel.zp.repaint; tbx.doZeJob("timeout", true)
        case slider: (String, Int) =>
            ZePanel.zp.pause = slider._2 == 0
            ZePanel.zp.run = false
            if (slider._2 == 100) {
                context.setReceiveTimeout(1 millisecond)
                ZePanel.zp.run = true
            } else {
                context.setReceiveTimeout(max(50, slider._2 * 4) millisecond)
            }
            ZePanel.zp.step = false
        case "step" =>
            l.myErrPrintDln("step")
            ZePanel.zp.repaint
            tbx.doZeJob("step", true)
            ZePanel.zp.step = true
    }
}

object ZePanel {
    var zp: ZePanel = _
    var za: ActorRef = _
    implicit val system = MyLog.system
    def newZePanel(lbl: Label, maxRC: RowCol) {
        zp = new ZePanel(lbl, maxRC)
        za = ActorDSL.actor(new ZeActor)
    }
}

class ZePanel(val lbl: Label, val maxRC: RowCol) extends Panel {
    var pause = false
    var step = false
    var run = false
    var largeur = 1000
    val hauteur = 700
    preferredSize = new Dimension(largeur, hauteur)
    val origin = new Dimension(0, 0)
    newTbx(this, maxRC, preferredSize, origin)

    override def paint(g: Graphics2D) {
        g.setColor(Color.white)
        g.fillRect(0, 0, largeur, hauteur)

        g.setColor(Color.black)
        tbx.lc.foreach(_.paint(g))
        tbx.lj.foreach(_.paint(g))
    }
}

