package piezo1

import org.jfree.chart.{ChartFactory, ChartPanel}
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.chart.plot.PlotOrientation
import scala.swing._
import java.awt.BorderLayout

object LineChartExample2 extends SimpleSwingApplication {

  val ATTENTION = "Attention"
  val MEDITATION = "Meditation"

  val data = new DefaultCategoryDataset  
  data.addValue(100.0, ATTENTION, 1)
  data.addValue(200.0, ATTENTION, 2)
  data.addValue(300.0, ATTENTION, 8)
  data.addValue(400.0, ATTENTION, 4)
  data.addValue(500.0, ATTENTION, 5)

  data.addValue(500.0, MEDITATION, 1)
  data.addValue(400.0, MEDITATION, 2)
  data.addValue(300.0, MEDITATION, 3)
  data.addValue(200.0, MEDITATION, 4)
  data.addValue(100.0, MEDITATION, 5)

  data.addValue(550.0, "zoom", 1)
  data.addValue(400.0, "zoom", 2)
  data.addValue(350.0, "zoom", 3)
  data.addValue(200.0, "zoom", 7)
  data.addValue(150.0, "zoom", 5)

  val chart = ChartFactory.createLineChart(
      "Brainwaves", "Time", "Value",
      data, PlotOrientation.VERTICAL,
      true, true, true)

  def top = new MainFrame { 
    title = "Brainwave Plotter"
    peer.setContentPane(new ChartPanel(chart))
    peer.setLocationRelativeTo(null)
  }

}