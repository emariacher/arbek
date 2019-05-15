
package cadransolaire
import _root_.scalaSci.math.plot.plotTypes._
import _root_.scalaSci.math.plot.plot._
import _root_.scalaSci.math.plot.canvas._
import java.awt.Color
import scala.collection.immutable.ListSet
import kebra.MyLog._
import kebra.MyPlotUtils._

class Function(val func: (Double) => Double, val revfunc: (Double) => Double, val tostring: String) {

	override def toString: String = tostring

			def plotFunc (bounds :(Double,Double,Double,Double), step: Double, legend: String, hue: Float) {
	val xmin = bounds._1
			val xmax = bounds._2
			val ymin = bounds._3
			val ymax = bounds._4 

			assume(xmax > (xmin+step),"xmax > (xmin+step)")
	assume(ymax > ymin,"ymax > ymin")
	assume(step>0,"step>0")

	val xlow = xmin
	val xhigh = xmax
	val x = MyStatistics.inc(xlow, step, xhigh)
	linePlotsOn
	val y = x.map(func(_))
	plot(x, y, getColor(hue), legend)	
	require(y.min>=ymin)
	require(y.max<=ymax)
}


}

class Intersections(lines: List[BetaFunction]) {
	assume(lines.size>1,"lines.size>1, lines:\n"+lines.mkString("\n  "))
	val intersectionsIn = lines.foldLeft(List[XY]())(_ ++ _.getIntersections(lines))
	assume(!intersectionsIn.isEmpty,"!intersectionsIn.isEmpty, lines:\n"+lines.mkString("\n  "))
	val intersections =	intersectionsIn.filter(_.inBounds((-200.0,200.0,-200.0,200.0)))
	assume(!intersections.isEmpty,"!intersections.isEmpty")

	// 1st pass: get bars
	val sums = intersections.foldLeft(new XY(0D,0D))(_ + _)

	val bars = sums / intersections.size

	require(sums.x==intersections.map(_.x).sum)
	require(sums.y==intersections.map(_.y).sum)

	// 2nd pass: compute stdDev
	val stdDev = math.sqrt(intersections.foldLeft(new X2plusY2(0D))(_ + new X2plusY2(_,bars)).x2plusy2 / intersections.size)

//	L.myPrintDln(toString)

	override def toString: String = "stdDev: %2.2f".format(stdDev)

	def getResult: (XY,Double) = (bars,stdDev)

	def display = {
		scatterPlotsOn
		plot(intersections.map(_.x).toArray, intersections.map(_.y).toArray, Color.BLACK, bars.toString)
		linePlotsOn
		plot(List(bars.x).toArray, List(bars.y).toArray, Color.RED, bars.toString)
	}
}

class BetaFunction(val beta1: Double, val beta0: Double) extends 
Function((x: Double) => (beta1 * x) + beta0, (y: Double) => (y - beta0) / beta1,
"y = " + ("%4.3f" format beta1) + " * x + " + ("%4.3f" format beta0)) {
	def this(betas:(Double,Double)) = this(betas._1, betas._2)

			def &(other: BetaFunction): XY = { // intersection
		assume((other.beta1 - beta1)!=0.0,"(other.beta1 - beta1)!=0.0: ["+ toString + "] vs ["+other.toString+"]")
		val x = (beta0 - other.beta0) / (other.beta1 - beta1)
		val y = (beta1 * x) + beta0
		new XY(x, y)
	}

	def getIntersections(lines: List[BetaFunction]): List[XY] = lines.filterNot(_ == this).map(_ & this)

			override def equals(x: Any): Boolean = {
		beta0.equals(x.asInstanceOf[BetaFunction].beta0) | beta1.equals(x.asInstanceOf[BetaFunction].beta1)
	}

	override def plotFunc (bounds :(Double,Double,Double,Double), step: Double, legend: String, hue: Float) {
		val xmin = bounds._1
				val xmax = bounds._2
				val ymin = bounds._3
				val ymax = bounds._4 

				assume(xmax > (xmin+step),"xmax > (xmin+step)")
		assume(ymax > ymin,"ymax > ymin")
		assume(step>0,"step>0")

		var xminYbound = func(xmin)
		var xlow = 0.0
		if(xminYbound<ymin) {
			xlow = revfunc(ymin)
		}else if(xminYbound>ymax) {
			xlow = revfunc(ymax)
		} else {
			xlow = xmin
		}

		var xmaxYbound = func(xmax)
				var xhigh = 0.0
				if(xmaxYbound<ymin) {
					xhigh = revfunc(ymin)
				}else if(xmaxYbound>ymax) {
					xhigh = revfunc(ymax)
				} else {
					xhigh = xmax
				}

		if (xhigh<xlow) {
			val temp = xlow
					xlow = xhigh
					xhigh = temp
		}
		if(xhigh > (xlow+step)) {
			val x = MyStatistics.inc(xlow, step, xhigh)
					linePlotsOn
					val y = x.map(func(_))
					plot(x, y, getColor(hue), legend)		  
		} 
	}


} 