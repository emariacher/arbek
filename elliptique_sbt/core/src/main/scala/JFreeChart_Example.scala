import kebra.MyLog._

/*
https://www.coindesk.com/math-behind-bitcoin/
https://crypto.stackexchange.com/questions/44304/understanding-elliptic-curve-point-addition-over-a-finite-field
https://fr.wikipedia.org/wiki/Courbe_elliptique
https://www.johannes-bauer.com/compsci/ecc/#anchor24
https://github.com/wookietreiber/scala-chart
 */

import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import scalax.chart.XYChart

object JFreeChart_Example extends App with scalax.chart.module.Charting {
  val a = 0
  val b = 7
  val modlo = 67
  val e = new Elliptique(modlo, a, b)

  myPrintDln(e.title)
  val lp = e.curve
  val plot = XYLineChart(lp).plot
  plot.setRenderer(new XYLineAndShapeRenderer(false, true))

  val chartScatter = XYChart(plot, title = e.title, legend = true)(theme = ChartTheme.Default)

  chartScatter.show(e.title, (800, 800))

  val XYSeriesCollection1 = new XYSeriesCollection()
  val allPoints1st = new XYSeries(e.title)
  lp.foreach(p => allPoints1st.add(p._1.toInt, p._2.toInt))
  XYSeriesCollection1.addSeries(allPoints1st)
  val allPoints2nd = new XYSeries(e.title + " subset")
  lp.take(10).foreach(p => allPoints2nd.add(p._1.toInt + 1, p._2.toInt + 1))
  XYSeriesCollection1.addSeries(allPoints2nd)

  val plot2 = XYLineChart(XYSeriesCollection1).plot
  plot2.setRenderer(new XYLineAndShapeRenderer(false, true))

  val chartScatter2 = XYChart(plot2, title = e.title, legend = true)(theme = ChartTheme.Default)

  chartScatter2.show(e.title + " 2", (800, 800))

}
