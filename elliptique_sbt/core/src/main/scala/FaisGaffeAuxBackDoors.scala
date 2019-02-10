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

    var lsp = List[MySeries]()
    val lZeros = e.curve.filter(p => p._1 * p._2 == 0).map(z => {
      val lsz = e.loopsum(z)
      myPrintDln(lsz)
      lsp = lsp :+ new MySeries("loopsum(" + z + ")", false, true, lsz._5)
      lsz._5
    }).flatten.distinct

    lsp = lsp :+ new MySeries(e.title, false, true, e.curved.filter(p => lZeros.find(_ == p).isEmpty))
    ScalaChartUtils.LineScatterPlot(title + e.title, lsp)
  }

  /*myPrintDln("****Choisis la bonne courbe****")
  myPrintDln(" **Montre une bonne courbe**")
  myPrintDln(" **Montre une mauvaise courbe**")*/
  myPrintDln("****Choisis le bon modulo****")
  analyseModulo(new Elliptique(67, 0, 7), "Montre un bon modulo ")
  analyseModulo(new Elliptique(73, 0, 7), "Montre un mauvais modulo ")
  analyseModulo(new Elliptique(17, 0, 7), "Montre un mauvais modulo ")
}
