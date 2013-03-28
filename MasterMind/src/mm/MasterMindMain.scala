package mm

import kebra.MyLog._
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

object MasterMindMain extends SimpleSwingApplication {
        val label = new Label { text = "Idle Label" } 
    myPrintDln("Hoi Welt!")

    def top = new MainFrame {
        title = "Master Mind"
                val buttonStep = new Button { text = "step" }
        val zepanel = new ZePanel(label)

        contents = new BoxPanel(Orientation.Vertical) {
            contents += label
                    contents += new BoxPanel(Orientation.Horizontal) {
                //contents ++=  tbx.lj.map(_.label)
            }
            contents += zepanel
                    contents += new BoxPanel(Orientation.Horizontal) {
                contents += buttonStep
            }
            border = Swing.EmptyBorder(30, 30, 10, 30)
        }
        listenTo(buttonStep)
        reactions += {
        case ButtonClicked(b) => printIt(b)
        case _ => 
        }

    }

}