import kebra.MyLog._

/*
https://www.coindesk.com/math-behind-bitcoin/
https://crypto.stackexchange.com/questions/44304/understanding-elliptic-curve-point-addition-over-a-finite-field
https://fr.wikipedia.org/wiki/Courbe_elliptique
https://www.johannes-bauer.com/compsci/ecc/#anchor24
https://github.com/wookietreiber/scala-chart
 */

object FaisGaffeAuxBackDoorsackdoors extends App with scalax.chart.module.Charting {

  def analyseModulo(e: Elliptique, title: String) = {
    myPrintDln(title, e.title)

    ScalaChartUtils.LineScatterPlot(title + e.title, e.trouveLesBoucles.map(z => {
      new MySeries("loopsum(" + z.head + ")["+z.size+"]", true, true, z.map(bi => (bi._1.toDouble, bi._2.toDouble)))
    }))
  }

  /*myPrintDln("****Choisis la bonne courbe****")
  myPrintDln(" **Montre une bonne courbe**")
  myPrintDln(" **Montre une mauvaise courbe**")*/
  myPrintDln("****Choisis le bon modulo****")
  analyseModulo(new Elliptique(67, 0, 7), "Montre un bon modulo ")
  analyseModulo(new Elliptique(73, 0, 7), "Montre un mauvais modulo ")
}
