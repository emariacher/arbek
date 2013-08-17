package kebra

import scala.collection.immutable.ListSet
import _root_.scalaSci.math.plot.plotTypes._
import _root_.scalaSci.math.plot.plot._
import _root_.scalaSci.math.plot.canvas._
import java.awt.Color

object MyPlotUtils {
    def inc(min: Double, step: Double, max: Double): Array[Double] = {
        assume(max > min, "max > min")
        assume((max - min) > step, "(max-min)>step")
        assume(step > 0.0, "step>0.0")
        var l = List[Double]()
        var v = min + step
        l = l :+ min
        while (v < max) {
            l = l :+ v
            v += step
        }
        l = l :+ max
        l.toArray
    }
    def getColor(hue: Float): Color = {
        val couleurStartHSB = Color.RGBtoHSB(0, 0, 255, null)
        Color.getHSBColor(couleurStartHSB.apply(0) + hue, couleurStartHSB.apply(1), couleurStartHSB.apply(2))
    }
    def getCouples(length: Int, l: List[Double]): (List[Double], List[Double]) = {
        if ((length > 1) && (l.length > 1)) {
            val inc = (l.max - l.min) / length
            //L.myPrintDln("  inc: "+inc+" min: "+l.min+" max: "+l.max+"  length: "+length+"\n  l: "+l.sorted)
            var splitted = l.groupBy { (d: Double) => if (d == l.max) { ((d - (l.min + 0.001)) / inc).toInt } else { ((d - l.min) / inc).toInt } }
            //L.myPrintDln(splitted.mkString("\n     ","\n     ","\n     "))
            (0 until length).foreach((i: Int) => splitted = splitted + (i -> splitted.getOrElse(i, List.empty[Double])))
            //L.myPrintDln(splitted.mkString("\n     ","\n     ","\n     "))
            var serieY = splitted.toList.sortBy(_._1).map((c: (Int, List[Double])) => c._2.length)
            val serieX = new Range(0, serieY.length, 1).toList.map((i: Int) => i * inc + (l.min + (inc / 2)))
            //L.myPrintDln("  "+length+"\n  l: "+l.sorted+"\n  x["+serieX.length+"]: "+serieX+"\n  y["+serieY.length+"]: "+serieY)                    
            assert(serieX.length == serieY.length)
            //assert(serieX.length==length)

            (serieX, serieY.map(_.toDouble))
        } else {
            (List.empty[Double], List.empty[Double])
        }
    }

    def serieXDouble(PointMin: Double, PointMax: Double, nbrePoints: Int): List[Double] = {
        val inc = (PointMax - PointMin) / nbrePoints
        (0 until nbrePoints).toList.map(_ * inc + (PointMin + (inc / 2)))
    }

    def serieXInt(PointMin: Double, PointMax: Double, nbrePoints: Int): List[Double] = {
        val inc = (PointMax - PointMin) / nbrePoints
        (0 until nbrePoints).toList.map((x: Int) => Math.floor(x * inc + (PointMin + (inc / 2))))
    }

    def serieY(serieX: List[Double], l: List[Double]): List[Double] = {
        val inc2 = (serieX.last - serieX.head) / (serieX.length * 2)
        serieX.map((x: Double) => l.filter((lz: Double) => lz > (x - inc2) && lz < (x + inc2)).length).map(_.toDouble)
    }

    def serieZ(serieX: List[Double], pf: (Double) => Double): List[Double] = {
        serieX.map((x: Double) => pf(x))
    }
}

class LinearRegression(val pairs: List[(Double, Double)]) {
    val unzip = pairs.unzip
    var s = ""
    val size = pairs.size
    s += "pairs = " + pairs

    // first pass: read in data, compute xbar and ybar
    val sums = pairs.foldLeft(new X_X2_Y(0D, 0D, 0D))(_ + new X_X2_Y(_))
    val bars = (sums.x / size, sums.y / size)

    // second pass: compute summary statistics
    val sumstats = pairs.foldLeft(new X2_Y2_XY(0D, 0D, 0D))(_ + new X2_Y2_XY(_, bars))

    val beta1 = sumstats.xy / sumstats.x2
    val beta0 = bars._2 - (beta1 * bars._1)
    val betas = (beta0, beta1)
    val func = new BetaFunction(beta1, beta0)

    // analyze results
    val correlation = pairs.foldLeft(new RSS_SSR(0D, 0D))(_ + RSS_SSR.build(_, bars, func.func))
    val R2 = correlation.ssr / sumstats.y2
    val svar = correlation.rss / (size - 2)
    val svar1 = svar / sumstats.x2
    val svar0 = (svar / size) + (bars._1 * bars._1 * svar1)
    val svar0bis = svar * sums.x2 / (size * sumstats.x2)

    s += "\n  " + toString
    s += "\n    R^2                 = " + R2
    s += "\n    std error of beta_1 = " + Math.sqrt(svar1)
    s += "\n    std error of beta_0 = " + Math.sqrt(svar0)
    s += "\n    std error of beta_0 = " + Math.sqrt(svar0bis)
    s += "\n    SSTO = " + sumstats.y2
    s += "\n    SSE  = " + correlation.rss
    s += "\n    SSR  = " + correlation.ssr + "\n"

    override def toString: String = "y = " + ("%4.3f" format beta1) + " * x + " + ("%4.3f" format beta0) + " r2: " + ("%1.3f" format R2)

    def plotFunc(legend: String, hue: Float) {
        val bounds = (unzip._1.min, unzip._1.max, unzip._2.min, unzip._2.max)
        val step = (unzip._1.max - unzip._1.min) / 10
        func.plotFunc(bounds, step, legend, hue)
    }
}

class Correlation(val pairs: List[(Double, Double)], fit: Function4Plot) {
    var s = ""
    val size = pairs.size
    s += "pairs = " + pairs

    // first pass: read in data, compute xbar and ybar
    val sums = pairs.foldLeft(new X_X2_Y(0D, 0D, 0D))(_ + new X_X2_Y(_))
    val bars = (sums.x / size, sums.y / size)

    // second pass: compute summary statistics
    val sumstats = pairs.foldLeft(new X2_Y2_XY(0D, 0D, 0D))(_ + new X2_Y2_XY(_, bars))

    // analyze results
    val correlation = pairs.foldLeft(new RSS_SSR(0D, 0D))(_ + RSS_SSR.build(_, bars, fit.func))
    val R2 = correlation.ssr / sumstats.y2

    s += "\n  " + fit
    s += "\n    R^2  = " + R2
    s += "\n    SSTO = " + sumstats.y2
    s += "\n    SSE  = " + correlation.rss
    s += "\n    SSR  = " + correlation.ssr + "\n"

    override def toString: String = fit + " r2: " + ("%1.3f" format R2)

}

object RSS_SSR {
    def build(p: (Double, Double), bars: (Double, Double), fit: (Double) => Double): RSS_SSR = {
        val fitResult = fit(p._1)
        val rss = (fitResult - p._2) * (fitResult - p._2)
        val ssr = (fitResult - bars._2) * (fitResult - bars._2)
        new RSS_SSR(rss, ssr)
    }
}

class RSS_SSR(val rss: Double, val ssr: Double) {
    def +(p: RSS_SSR): RSS_SSR = new RSS_SSR(rss + p.rss, ssr + p.ssr)
}

class X_X2_Y(val x: Double, val x2: Double, val y: Double) {
    def this(p: (Double, Double)) = this(p._1, p._1 * p._1, p._2)
    def +(p: X_X2_Y): X_X2_Y = new X_X2_Y(x + p.x, x2 + p.x2, y + p.y)
}

class X2_Y2_XY(val x2: Double, val y2: Double, val xy: Double) {
    def this(p: (Double, Double), bars: (Double, Double)) = this((p._1 - bars._1) * (p._1 - bars._1), (p._2 - bars._2) * (p._2 - bars._2), (p._1 - bars._1) * (p._2 - bars._2))
    def this(p: XY, bars: XY) = this((p.x - bars.x) * (p.x - bars.x), (p.y - bars.y) * (p.y - bars.y), (p.x - bars.x) * (p.y - bars.y))
    def +(p: X2_Y2_XY): X2_Y2_XY = new X2_Y2_XY(x2 + p.x2, y2 + p.y2, xy + p.xy)
}

class XY(val x: Double, val y: Double) {
    val hypot = math.sqrt((x * x) + (y * y))

    def this(p: (Double, Double)) = this(p._1, p._2)
    def +(p: XY): XY = new XY(x + p.x, y + p.y)
    def -(p: XY): XY = new XY(x - p.x, y - p.y)
    def *(p: XY): XY = new XY(x * p.x, y * p.y)
    def *(i: Double): XY = new XY(x * i, y * i)
    def /(i: Double): XY = new XY(x / i, y / i)
    override def toString: String = ("(x: %4.3f" format x) + (", y: %4.3f)" format y)
    def inBounds(bounds: (Double, Double, Double, Double)): Boolean = {
        val xmin = bounds._1
        val xmax = bounds._2
        val ymin = bounds._3
        val ymax = bounds._4
        assume(xmax > xmin, "xmax > xmin")
        assume(ymax > ymin, "ymax > ymin")
        (x > xmin) & (x < xmax) & (y > ymin) & (y < ymax)
    }

    def getBetaFunction(angle: Double): BetaFunction = {
        val betaFunction = new BetaFunction(math.tan(angle.toRadians), y - (x * math.tan(angle.toRadians)))
        //		L.myPrintDln(toString+" -- "+(" %4.3f" format angle)+"�� --- "+betaFunction.toString)
        betaFunction
    }

    def getAngle: Double = math.toDegrees(-math.atan2(x, y))

    override def hashCode: Int = toString.hashCode
    override def equals(o: Any): Boolean = toString == o.asInstanceOf[XY].toString

    def isVertHoriz: Boolean = x == 0.0 | y == 0.0 //horizontal or vertical (used when XY is a segment)
    def toString91: String = ("(xy: %1.0f" format x) + (" %1.0f)" format y)
}

class X2plusY2(val x2plusy2: Double) {
    def this(p: XY, bars: XY) = this(((p.x - bars.x) * (p.x - bars.x)) + ((p.y - bars.y) * (p.y - bars.y)))
    def +(p: X2plusY2): X2plusY2 = new X2plusY2(x2plusy2 + p.x2plusy2)
}

object QuadraticRoots {
    def solve(a: Double, b: Double, c: Double): List[(Double, Double)] = {
        // solve ax2 + bx + c = 0
        assume(a != 0.0)

        var roots = ListSet[(Double, Double)]()
        val d = b * b - 4.0 * a * c
        val aa = a + a
        val minmax = new XY(-b / aa, -d / (aa + aa))

        if (d < 0.0) { // complex roots
            val re = -b / aa;
            val im = math.sqrt(-d) / aa;
            roots = roots + ((re, im)) + ((re, -im))
        } else { // real roots
            val re = if (b < 0.0) (-b + math.sqrt(d)) / aa else (-b - math.sqrt(d)) / aa
            roots = roots + ((re, 0.0)) + ((c / (a * re), 0.0))
        }
        println("%2.2f x2 + %2.2f x + %2.2f = 0 ".format(a, b, c) +
            roots.map((p: (Double, Double)) => ("(re: %4.3f" format p._1) + (if (d < 0.0) { ", im: %4.3f)" format p._2 } else { ")" })) +
            " min or max: " + minmax)
        roots.toList
    }
}

class Function4Plot(val func: (Double) => Double, val revfunc: (Double) => Double, val tostring: String) {

    override def toString: String = tostring

    def plotFunc(bounds: (Double, Double, Double, Double), step: Double, legend: String, hue: Float) = {
        val xmin = bounds._1
        val xmax = bounds._2
        val ymin = bounds._3
        val ymax = bounds._4

        assume(xmax > (xmin + step), "xmax > (xmin+step)")
        assume(ymax > ymin, "ymax > ymin")
        assume(step > 0, "step>0")

        val xlow = xmin
        val xhigh = xmax
        val x = MyPlotUtils.inc(xlow, step, xhigh)
        linePlotsOn
        val y = x.map(func(_))
        plot(x, y, MyPlotUtils.getColor(hue), legend)
        require(y.min >= ymin)
        require(y.max <= ymax)
    }
}

class BetaFunction(val beta1: Double, val beta0: Double) extends Function4Plot((x: Double) => (beta1 * x) + beta0, (y: Double) => (y - beta0) / beta1,
    "y = " + ("%4.3f" format beta1) + " * x + " + ("%4.3f" format beta0)) {
    def this(betas: (Double, Double)) = this(betas._1, betas._2)

    def &(other: BetaFunction): XY = { // intersection
        assume((other.beta1 - beta1) != 0.0, "(other.beta1 - beta1)!=0.0: [" + toString + "] vs [" + other.toString + "]")
        val x = (beta0 - other.beta0) / (other.beta1 - beta1)
        val y = (beta1 * x) + beta0
        new XY(x, y)
    }

    def getIntersections(lines: List[BetaFunction]): List[XY] = lines.filterNot(_ == this).map(_ & this)

    override def equals(x: Any): Boolean = {
        beta0.equals(x.asInstanceOf[BetaFunction].beta0) | beta1.equals(x.asInstanceOf[BetaFunction].beta1)
    }

    override def plotFunc(bounds: (Double, Double, Double, Double), step: Double, legend: String, hue: Float) = {
        val xmin = bounds._1
        val xmax = bounds._2
        val ymin = bounds._3
        val ymax = bounds._4

        assume(xmax > (xmin + step), "xmax > (xmin+step)")
        assume(ymax > ymin, "ymax > ymin")
        assume(step > 0, "step>0")

        var xminYbound = func(xmin)
        var xlow = 0.0
        if (xminYbound < ymin) {
            xlow = revfunc(ymin)
        } else if (xminYbound > ymax) {
            xlow = revfunc(ymax)
        } else {
            xlow = xmin
        }

        var xmaxYbound = func(xmax)
        var xhigh = 0.0
        if (xmaxYbound < ymin) {
            xhigh = revfunc(ymin)
        } else if (xmaxYbound > ymax) {
            xhigh = revfunc(ymax)
        } else {
            xhigh = xmax
        }

        if (xhigh < xlow) {
            val temp = xlow
            xlow = xhigh
            xhigh = temp
        }
        if (xhigh > (xlow + step)) {
            val x = MyPlotUtils.inc(xlow, step, xhigh)
            linePlotsOn
            val y = x.map(func(_))
            plot(x, y, MyPlotUtils.getColor(hue), legend)
        }
    }
}