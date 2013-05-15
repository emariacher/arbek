package labyrinthe

import scala.swing.MainFrame
import scala.swing.Slider
import scala.swing.Button
import scala.swing.Label
import scala.swing.BoxPanel
import scala.swing.event.ValueChanged
import scala.swing.event.ButtonClicked
import scala.swing.Orientation
import scala.swing.Swing
import scala.swing.SimpleSwingApplication
import labyrinthe.Tableaux._
import labyrinthe.LL._
import java.io.File
import kebra.MyLog._

object LabyrintheMainApp extends SimpleSwingApplication {
	newMyLL(this.getClass.getName,new File("out\\cowabunga"), "htm", true)

	def top = new MainFrame {
		title = "Sors du Labyrinthe. Vert va toujours a tribord, Rouge toujours a babord et bleu est fou."
				val sliderpp = new Slider {
			majorTickSpacing = 100
					minorTickSpacing = 10
					paintLabels = true
					paintTicks = true
		}
		val buttonStep = new Button { text = "step" }
		val label = new Label { text = "Idle Label" }	

		//val tableau = new ZePanel(label, new RowCol(40,40))

		ZePanel.newZePanel(label, new RowCol(40,40))

		contents = new BoxPanel(Orientation.Vertical) {
			contents += label
					contents += new BoxPanel(Orientation.Horizontal) {
				contents ++=  tbx.lj.map(_.label)
			}
			contents += ZePanel.zp
					contents += new BoxPanel(Orientation.Horizontal) {
				contents += sliderpp
						contents += buttonStep
			}
			border = Swing.EmptyBorder(30, 30, 10, 30)
		}
		listenTo(sliderpp, buttonStep)
		reactions += {
		case ValueChanged(b) => ZePanel.za ! ("slider",sliderpp.value)
		case ButtonClicked(b) => ZePanel.za ! b.text
		case _ => 
		}
	}

}