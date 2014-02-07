package piezo1

import kebra._
import kebra.MyLog._
import org.jfree.chart.{ ChartFactory, ChartPanel }
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.chart.plot.PlotOrientation
import scala.swing._
import java.awt.BorderLayout
import Math._
import org.jfree.chart.renderer.category.LineAndShapeRenderer
import org.jfree.data.category.CategoryDataset
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.chart.plot.CategoryPlot
import java.awt.BasicStroke
import org.jfree.util.ShapeUtilities
import org.jfree.chart.renderer.xy.StandardXYItemRenderer
import scala.concurrent.duration.Duration

object Piezo2Main extends SimpleSwingApplication {

    val data = new DefaultCategoryDataset

    val chart = ChartFactory.createLineChart(
        "Pulse", "Time", "Value",
        data, PlotOrientation.VERTICAL,
        true, true, true)

    val plot = chart.getPlot().asInstanceOf[CategoryPlot];
    // customise the renderer...
    val renderer = plot.getRenderer;
    //        renderer.setDrawShapes(true);

    /*renderer.setSeriesStroke(
            0, new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f,  Array[Float](10.0f, 6.0f), 0.0f
            )
        );
        renderer.setSeriesStroke(
            1, new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, Array[Float](6.0f, 6.0f), 0.0f
            )
        );
        renderer.setSeriesStroke(
            2, new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, Array[Float](2.0f, 6.0f), 0.0f
            )
        );*/

    val cross = ShapeUtilities.createDiagonalCross(3, 1);
    //renderer.setPlotShapes(true);
    renderer.setSeriesShape(3, cross);

    def top = new MainFrame {
        //val pz3 = new Piezo3NoGraphic
        val pz4 = new Piezo4NoGraphic

        draw(pz4.ldecimed.map(c => (c._1, (c._2 * pz4.ttick).toMicros.toInt)), pz4.curveName)

        pz4.doZeJob

        pz4.iffted.zipWithIndex.foreach(ifft => draw(ifft._1._2.map(c => ((c._1 * 4) + (100 * ifft._2),
            (c._2 * pz4.decimation * pz4.ttick).toMicros.toInt)), "filtered"+ifft._1._1))

        draw(pz4.fit.zeFitCurveRaw, pz4.fit.toString)
        if (!pz4.fit.zeFitCurveNo50Hz.isEmpty) {
            draw(pz4.fit.zeFitCurveNo50Hz.map(d => (d._1 + 100, d._2)), pz4.fit.toString+"No50Hz")
        }

        title = pz4.curveName+" %3.2f bpm".format(pz4.bpm)
        peer.setContentPane(new ChartPanel(chart))
        peer.setLocationRelativeTo(null)
    }

    def draw(l: List[(Double, Int)], name: String) = l.foreach(c => data.addValue(c._1, name, c._2))
}
