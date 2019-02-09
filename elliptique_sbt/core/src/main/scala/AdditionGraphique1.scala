import kebra.MyLog._

/*
https://www.coindesk.com/math-behind-bitcoin/
https://crypto.stackexchange.com/questions/44304/understanding-elliptic-curve-point-addition-over-a-finite-field
https://fr.wikipedia.org/wiki/Courbe_elliptique
https://www.johannes-bauer.com/compsci/ecc/#anchor24
https://github.com/wookietreiber/scala-chart
 */

object AdditionGraphique1 extends App with scalax.chart.module.Charting {
  val e = new Elliptique(67, 0, 7)

  val sumline = e.SumLine(e.curve.head, e.curve.last)

  ScalaChartUtils.LineScatterPlot("Zobi la mouche", List(
    new MySeries(e.title, false, true, e.curved),
    new MySeries("zorglub", false, true, (for (i <- 1 to 5) yield (i.toDouble, i.toDouble)).toList),
    new MySeries("bulgroz", true, false, (for (i <- 1 to 6) yield (i.toDouble, i.toDouble * i)).toList),
    sumline._1
  ))

  val sum = e.plus(e.curve.head, e.curve.last)
  val symsum = (sum._1, e.modlo - sum._2)
  myPrintDln(sum, symsum)
  myPrintIt(sumline._2, sumline._3)

  var atModloY = (sumline._2 * symsum._1.toDouble) + sumline._3
  while (atModloY < 0) {
    atModloY += e.modlo.toDouble
  }
  while (atModloY > e.modlo.toDouble) {
    atModloY -= e.modlo.toDouble
  }

  myPrintDln(sumline._2 * symsum._1.toDouble, (sumline._2 * symsum._1.toDouble) + sumline._3, atModloY, symsum._2)
  myPrintDln(e.curve.head + " + " + e.curve.last + " = " + sum)
  myPrintIt(e.title)
}
