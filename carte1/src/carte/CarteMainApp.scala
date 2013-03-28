package carte

import scala.swing._
import scala.swing.event._
import scala.actors.Actor
import java.io.File


object CarteMainApp extends SimpleSwingApplication {

	def top = new MainFrame {
		title = "Colorie une carte en 4 couleurs"
				val sliderpp = new Slider {
			majorTickSpacing = 100
					minorTickSpacing = 10
					paintLabels = true
					paintTicks = true
		}
		val buttonstep = new Button {
			text = "step"
		}
		val label = new Label {
			text = "Idle Label"
		}	

		val tableau = new ZePanel(label,40,40)
		contents = new BoxPanel(Orientation.Vertical) {
			contents += label
					contents += tableau
					contents += new BoxPanel(Orientation.Horizontal) {
				contents += sliderpp
						contents += buttonstep
			}
			border = Swing.EmptyBorder(30, 30, 10, 30)
		}
		listenTo(sliderpp, buttonstep)
		reactions += {
		case ValueChanged(b) => tableau ! ("slider",sliderpp.value)
		case ButtonClicked(b) => tableau ! b.text
		case _ => 
		}
	}
}  

