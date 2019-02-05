import scalax.chart.api._
import kebra.MyLog._
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import scalax.chart.XYChart

object JFreeChart_Example extends App with scalax.chart.module.Charting {
  val a = 0
  val b = 7
  val modlo = 67
  val e = new Elliptique(modlo, a, b)

  myPrintDln(e.title)
  val lp = e.curve.sortBy(p => (p._1 * modlo) + p._2)
  val chart = XYLineChart(lp)
  val plot =chart.plot
  plot.setRenderer(new XYLineAndShapeRenderer(false, true))
  val chartScatter = XYChart(plot, title = e.title, legend = true) (theme = ChartTheme.Default)

  chartScatter.show(e.title,(800,800))
}
