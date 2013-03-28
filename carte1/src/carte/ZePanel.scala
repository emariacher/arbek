package carte
import scala.swing.Panel
import scala.actors.Actor
import java.awt.Graphics2D
import java.awt.Dimension
import java.awt.Color
import scala.math._
import java.io.File
import scala.swing.Label
import carte.Tableaux._


class ZePanel(val lbl: Label, val maxRow: Int, val maxCol: Int) extends Panel with Actor {
	var pause = false
			var run = false
			var timeout = 1000
			preferredSize = new Dimension(1000,400)
	val origin  = new Dimension(0,0)
	newTbx(this, maxRow, maxCol, preferredSize, origin)
	start

	def act() {
		loop {
			reactWithin(timeout) {
			case slider: (String,Int) => pause = slider._2 == 0
			run = false
					if(slider._2==100) {
						timeout = 1
								run = true
					} else if(slider._2<20) {
						timeout = slider._2								
					} else {
					  timeout = max(20,slider._2*20)
					}
			case "step" => repaint; tbx.doZeJob("step")
			case _ => if(!pause) { repaint; tbx.doZeJob("timeout") }
			}
		}
	}

	override def paint(g: Graphics2D) {
		g.setColor(Color.black)
		tbx.cb.lcarres.lc.foreach(_.paint(g))
	}
}

