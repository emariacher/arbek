package piezo1

import kebra._
import kebra.MyLog._

class PiezoList(val lpz: List[Piezo5NoGraphic]) {
    def nodeChart = {
        var fits = lpz.filter(_.fit.isNotEmpty).map(z => (z.curveName, z.fit, z.correlationStats.mean))
        var corrfits = List.empty[(Int, Int, Double)]
        if (fits.size > 1) {
            val maxcorrmean = fits.map(_._3).max
            fits = fits.map(z => (z._1, z._2, (maxcorrmean + 1 - z._3) * 20 / maxcorrmean))
            myPrintIt(fits.mkString("\n"))
            corrfits = fits.zipWithIndex.combinations(2).toList.map(z => (z.head._2, z.last._2, z.head._1._2.correlate(z.last._1._2)))
            myPrintIt(corrfits.mkString("\n"))
            val maxcorr2corr = corrfits.map(_._3).max
            corrfits = take1stCorrelations(fits, corrfits.map(z => (z._1, z._2, (maxcorr2corr + 1 - z._3) * 10)))
            myPrintIt(corrfits.mkString("\n"))
        }

        if (!corrfits.isEmpty) {
            D3Charts.ForceDiagram(fits.groupBy(_._1.substring(0, 4)).zipWithIndex.map(zl => zl._1._2.map(z => (z._1, (zl._2.toInt + 1).toString, z._3.toInt.toString))).flatten.toList,
                corrfits.map(z => (z._1.toString, z._2.toString, z._3.toInt.toString)))
        } else {
            ""
        }
    }

    def take1stCorrelations(fits: List[(String, piezo1.Fit, Double)], corrfitsIn: List[(Int, Int, Double)]) = {
        fits.zipWithIndex.map(fitz => corrfitsIn.filter(z => z._1 == fitz._2 | z._2 == fitz._2).sortBy(_._3).take(2)).flatten.distinct
    }

}