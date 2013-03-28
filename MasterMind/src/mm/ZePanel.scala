package mm

import scala.swing.Panel
import scala.actors.Actor
import java.awt.Graphics2D
import java.awt.Dimension
import java.awt.Color
import scala.math._
import java.io.File
import scala.swing.Label
import kebra.MyLog._
import mm.Tableau._


class ZePanel(val lbl: Label) extends Panel with Actor {
	var pause = false
			var step = false
			var run = false
			var largeur = 1000
			val hauteur = 700
			var timeout = 2000
			preferredSize = new Dimension(largeur, hauteur)
	val origin  = new Dimension(0,0)
	newTbl
	start

	def act() {
		loop {
			reactWithin(timeout) {				
			case "step" => repaint; tbl.doZeJob("step", true); step = true
			case _ => repaint; tbl.doZeJob("step", true); step = true
			}
		}
	}

	override def paint(g: Graphics2D) {
		g.setColor(Color.white)
		g.fillRect(0,0,largeur, hauteur)

		g.setColor(Color.black)
		//tbx.lc.foreach(_.paint(g))
		//tbx.lj.foreach(_.paint(g))
	}
}

