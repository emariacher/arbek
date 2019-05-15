package redyellow

class RedYellowDSL {

  import RedYellowDSL.NConjunction

  var p = new Param

  object State extends Enumeration {
    type State = Value
    val NONE, RED, YELLOW, GREEN = Value
  }

  import State._

  var state = NONE

  def red(l: List[Int]) = {
    p.red = p.red ++ l
    state = RED
    this
  }

  def yellow(l: List[Int]) = {
    p.yellow = p.yellow ++ l
    state = YELLOW
    this
  }

  def green(l: List[Int]) = {
    p.green = p.green ++ List(l)
    state = GREEN
    this
  }

  def threshold(t: Int) = {
    state match {
      case RED => p.redthreshold = t
      case YELLOW => p.yellowthreshold = t
      case _ => throw new IllegalArgumentException("Cannot assign a threshold to: " + state)
    }
    this
  }

  def target(d: DateDSL) = {
    p.targetDate = new DateDSL(d)
    this
  }

  def next = {
    state = NONE
    this
  }

  def next(nand: NConjunction): RedYellowDSL = next
}

object RedYellowDSL {

  class NConjunction

  val nand = new NConjunction

  def Rule = new RedYellowDSL
}

class Param {
  var red = List.empty[Int]
  var redthreshold = 0
  var yellow = List.empty[Int]
  var yellowthreshold = 0
  var green = List.empty[List[Int]]
  var targetDate: DateDSL = _

  override def toString = {
    var s = ""
    if (!red.isEmpty) {
      s = s + "RedList(" + red.mkString(",") + ") threshold: " + redthreshold + ", "
    }
    if (!yellow.isEmpty) {
      s = s + "YellowList(" + yellow.mkString(",") + ") threshold: " + yellowthreshold + ", "
    }
    if (!green.isEmpty) {
      s = s + "GreenLists(" + green.mkString(" - ") + "), "
    }
    if (targetDate != null) {
      s = s + "TargetDate(" + targetDate + "), "
    }
    s
  }
}