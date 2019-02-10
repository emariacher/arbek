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
    myPrintDln(title)
    val lZeros = e.curve.filter(p => p._1 * p._2 == 0)

    if (!lZeros.isEmpty) {
      val ls1 = e.loopsum(lZeros.head)
      myPrintIt(lZeros.size, ls1)

      ScalaChartUtils.LineScatterPlot(title, List(
        new MySeries(e.title, false, true, e.curved.filter(p => ls1._5.find(_ == p).isEmpty)),
        new MySeries("loopsum(" + ls1._2 + ")", false, true, ls1._5)
      ))
    }
  }


  /*myPrintDln("****Choisis la bonne courbe****")
  myPrintDln(" **Montre une bonne courbe**")
  myPrintDln(" **Montre une mauvaise courbe**")*/
  myPrintDln("****Choisis le bon modulo****")
  analyseModulo(new Elliptique(67, 0, 7), " **Montre un bon modulo**")
  analyseModulo(new Elliptique(71, 0, 7), " **Montre un mauvais modulo**")
  analyseModulo(new Elliptique(73, 0, 7), " **Montre un mauvais modulo**")
  analyseModulo(new Elliptique(17, 0, 7), " **Montre un mauvais modulo**")
}
