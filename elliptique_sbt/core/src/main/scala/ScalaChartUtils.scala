import kebra.MyLog._
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.util.ShapeUtilities
import scalax.chart.XYChart

/*
https://www.coindesk.com/math-behind-bitcoin/
https://crypto.stackexchange.com/questions/44304/understanding-elliptic-curve-point-addition-over-a-finite-field
https://fr.wikipedia.org/wiki/Courbe_elliptique
https://www.johannes-bauer.com/compsci/ecc/#anchor24
https://github.com/wookietreiber/scala-chart
 */

class MySeries(val label: String , val line: Boolean, val shape: Boolean, val data: List[(Double, Double)])

object ScalaChartUtils extends scalax.chart.module.Charting {
  def LineScatterPlot(title: String, ll: List[MySeries]): XYPlot = {
    val XYSeriesCollection = new XYSeriesCollection()
    ll.foreach(ms => {
      val allPoints = new XYSeries(ms.label, false)
      ms.data.foreach(p => allPoints.add(p._1.toInt, p._2.toInt))
      XYSeriesCollection.addSeries(allPoints)
    })

    val plot = XYLineChart(XYSeriesCollection).plot
    val r = plot.getRenderer.asInstanceOf[XYLineAndShapeRenderer]

    ll.zipWithIndex.foreach(l => {
      r.setSeriesLinesVisible(l._2, l._1.line)
      r.setSeriesShapesVisible(l._2, l._1.shape)
    })

    val chartScatter = XYChart(plot, title = title, legend = true)(theme = ChartTheme.Default)

    chartScatter.show(title, (800, 800))

    plot
  }
}
