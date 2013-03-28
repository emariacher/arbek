package labyrinthe
import scala.swing.Panel
import scala.actors.Actor
import java.awt.Graphics2D
import java.awt.Dimension
import java.awt.Color
import scala.math._
import java.io.File
import scala.swing.Label
import labyrinthe.Tableaux._
import kebra.MyLog


class ZePanel(val lbl: Label, val maxRC: RowCol) extends Panel with Actor {
	var pause = false
			var step = false
			var run = false
			var largeur = 1000
			val hauteur = 700
			var timeout = 2000
			preferredSize = new Dimension(largeur, hauteur)
	val origin  = new Dimension(0,0)
	newTbx(this, maxRC, preferredSize, origin)
	start

	def act() {
		loop {
			reactWithin(timeout) {				
			case slider: (String,Int) => pause = slider._2 == 0
					run = false
					if(slider._2==100) {
						timeout = 1
								run = true
					} else {
						timeout = max(50,slider._2*4)
					}
			step = false 
			case "step" => repaint; tbx.doZeJob("step", true); step = true
			case _ => if((!pause)&&(!step)) repaint; tbx.doZeJob("timeout", true)
			}
		}
	}

	override def paint(g: Graphics2D) {
		g.setColor(Color.white)
		g.fillRect(0,0,largeur, hauteur)

		g.setColor(Color.black)
		tbx.lc.foreach(_.paint(g))
		tbx.lj.foreach(_.paint(g))
	}
}

