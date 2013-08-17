package euler
import scala.collection.immutable.ListSet

class GeomShape(val points: ListSet[XY]) {
    val xy00 = new XY(0, 0)
    val lpoints = points.toList.sortBy { _.x }.sortBy { _.y }

    def isTriangle: Boolean = points.size == 3

    override def toString: String = lpoints.toString
    override def hashCode: Int = toString.hashCode
    override def equals(o: Any): Boolean = toString == o.asInstanceOf[GeomShape].toString
}

class GeomTriangle(points: ListSet[XY]) extends GeomShape(points) {
    assume(points.size == 3, points.size + "==" + 3)
    val segments = ListSet[XY]() ++ points.toList.combinations(2).map((lxy: List[XY]) => (lxy.head - lxy.last))
    val d = segments.toList.map(_.hypot).sorted

    def this(shape: GeomShape) = this(shape.points)

    def hasRightAngle: Boolean = math.abs((d.last * d.last) - ((d.head * d.head) + (d.tail.head * d.tail.head))) < 0.000000001
    def isEquiLateral: Boolean = d.distinct.size != d.size
    def is00RightAngle: Boolean = {
        var answer = false
        if (points.contains(xy00) & hasRightAngle) {
            val otherPoints = points.filterNot(_ == xy00)
            val a = (xy00 - otherPoints.head).hypot
            val b = (xy00 - otherPoints.last).hypot
            val c = (otherPoints.head - otherPoints.last).hypot
            answer = math.abs((a * a) + (b * b) - (c * c)) < 0.000000001
        }
        answer
    }

    def isVertHoriz: Boolean = segments.filter(_.isVertHoriz).size > 1

    def getProp: String = {
        var s = " "
        if (hasRightAngle) {
            s += "r1 "
        } else {
            s += "r0 "
        }
        if (is00RightAngle) {
            s += "1 "
        } else {
            s += "0 "
        }
        if (isEquiLateral) {
            s += "eq1 "
        } else {
            s += "eq0 "
        }
        s
    }

    override def toString: String = lpoints.map(_.toString91) + getProp
}

