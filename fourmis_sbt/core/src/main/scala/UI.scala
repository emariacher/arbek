package labyrinthe
import java.io.File

import kebra.LL._
import labyrinthe.Tableaux._

import scala.swing._
import scala.swing.event.{ButtonClicked, ValueChanged}

/**
 * Created by ericm_000 on 16.03.2016.
 */
class UI extends SimpleSwingApplication {
    val titre = "Sors du Labyrinthe. Vert va toujours a tribord, Rouge toujours a babord et bleu est fou."
    val buttonStepLbl = "step"
    val labelLbl = "Idle Label"
    val zpanelType = PanelType.LABY
    newMyLL(this.getClass.getName, new File("out\\cowabunga"), "htm", true)

    def top = new MainFrame {
        title = titre
        val sliderpp = new Slider {
            majorTickSpacing = 100
            minorTickSpacing = 10
            paintLabels = true
            paintTicks = true
        }
        val buttonStep = new Button { text = buttonStepLbl }
        val label = new Label { text = labelLbl }

        ZePanel.newZePanel(label, new RowCol(40, 40), zpanelType)

        contents = new BoxPanel(Orientation.Vertical) {
            contents += label
            contents += new BoxPanel(Orientation.Horizontal) {
                //contents ++= tbx.lj.map(_.label)
                //contents ++= tbx.fourmilieres.map(_.label)
                tbx.fourmilieres.foreach(fml => {
                    contents += fml.label
                    contents ++= tbx.lj.filter(_.fourmiliere == fml).map(_.label)
                })
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
            case ValueChanged(b)  => ZePanel.za ! ("slider", sliderpp.value)
            case ButtonClicked(b) => ZePanel.za ! b.text
            case _                =>
        }
    }

}
