import scalax.chart.api._

object JFreeChart_Example extends App with scalax.chart.module.Charting {
  val data = for (i <- 1 to 5) yield (i, i)
  val chart = XYLineChart(data)
  chart.show()
}
