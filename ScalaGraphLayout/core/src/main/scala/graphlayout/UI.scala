package graphlayout

import java.io.File

import graphlayout.Tableaux._
import kebra.LL._
import kebra.MyLog

import scala.swing._
import scala.swing.event._

class UI(graph: GraphAbstract) extends SimpleSwingApplication {
  val titre = "graphlayout."
  val buttonStepLbl = "step"
  val labelLbl = "Idle Label"
  newMyLL(this.getClass.getName, new File("out\\cowabunga"), "htm", true)

  def top = new MainFrame {
    title = titre
    val sliderpp = new Slider {
      majorTickSpacing = 100
      minorTickSpacing = 10
      paintLabels = true
      paintTicks = true
      value = 10
    }
    val buttonStep = new Button {
      text = buttonStepLbl
    }
    val label = new Label {
      text = labelLbl
    }

    ZePanel.newZePanel(label, new RowCol(40, 40), graph)

    contents = new BoxPanel(Orientation.Vertical) {
      contents += label
      contents += ZePanel.zp
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += sliderpp
        contents += buttonStep
      }
      border = Swing.EmptyBorder(30, 30, 10, 30)
      listenTo(mouse.clicks, mouse.moves)
      reactions += {
        case e: MouseEvent => ZePanel.za ! (e.toString.substring(0, 6), e.point.x, e.point.y)
        case _ => MyLog.myPrintIt("Ici!")
      }
    }
    listenTo(sliderpp, buttonStep)
    reactions += {
      case ValueChanged(b) => ZePanel.za ! ("slider", sliderpp.value)
      case ButtonClicked(b) => ZePanel.za ! b.text
      case _ =>
    }
  }

}
