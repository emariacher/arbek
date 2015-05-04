package carte

import scala.swing._
import scala.swing.event._
import akka.actor._
import java.io.File
import carte.LL._


object CarteMainApp extends SimpleSwingApplication {
		newMyLL(this.getClass.getName, new File("out\\cowabunga"), "htm", true)

		def top = new MainFrame {
			title = "Colorie une carte en 4 couleurs"
			val sliderpp = new Slider {
				majorTickSpacing = 100
				minorTickSpacing = 10
				paintLabels = true
				paintTicks = true
			}
			val buttonStep = new Button { text = "step" }
			val label = new Label { text = "Idle Label" }

			//val tableau = new ZePanel(label, new RowCol(40,40))

			ZePanel.newZePanel(label, 40, 40)

			contents = new BoxPanel(Orientation.Vertical) {
				contents += label
				contents += ZePanel.zp
				contents += new BoxPanel(Orientation.Horizontal) {
					contents += sliderpp
					contents += buttonStep
				}
				border = Swing.EmptyBorder(30, 30, 10, 30)
			}
			listenTo(sliderpp, buttonStep)
			reactions += {
				case ValueChanged(b)  => ZePanel.za ! ("slider", sliderpp.value)
				case ButtonClicked(b) => ZePanel.za ! b.text
				case _                =>
			}
		}
}  

