package mm

import kebra.MyLog._

import scala.swing.{BoxPanel, Button, Label, MainFrame, Orientation, SimpleSwingApplication, Swing}
import scala.swing.event.ButtonClicked

object MasterMindMain extends SimpleSwingApplication {
  val label = new Label {
    text = "Idle Label"
  }
  myPrintDln("Hoi Welt!")

  def top = new MainFrame {
    title = "Master Mind"
    val buttonStep = new Button {
      text = "step"
    }
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