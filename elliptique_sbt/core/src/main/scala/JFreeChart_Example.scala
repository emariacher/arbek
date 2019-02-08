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

  def ScatterPlot(title: String, ll: List[(String, List[(BigInt, BigInt)])]) = {
    val XYSeriesCollection = new XYSeriesCollection()
    ll.foreach(l => {
      val allPoints = new XYSeries(l._1)
      l._2.foreach(p => allPoints.add(p._1.toInt, p._2.toInt))
      XYSeriesCollection.addSeries(allPoints)
    })

    val plot = XYLineChart(XYSeriesCollection).plot
    plot.setRenderer(new XYLineAndShapeRenderer(false, true))

    val chartScatter = XYChart(plot, title = e.title, legend = true)(theme = ChartTheme.Default)

    chartScatter.show(title, (800, 800))
  }

  ScatterPlot("Zobi la mouche", List((e.title, lp),
    ("zorglub", (for (i <- 1 to 5) yield (BigInt(i), BigInt(i))).toList),
    ("bulgroz", (for (i <- 1 to 23) yield (BigInt(i+1)*2, BigInt(i-1)*2)).toList)))

}
