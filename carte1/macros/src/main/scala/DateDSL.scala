package kebra

import java.text.{ParsePosition, SimpleDateFormat}
import java.util.Calendar

import scala.language.postfixOps

class DateDSL(val cal: Calendar) {
  cal.clear(Calendar.HOUR)

  import DateDSL.Conjunction

  private var last = 0;

  def this(d: DateDSL) = this(d.cal)

  def +(num: Int) = plus(num)

  def plus(num: Int) = {
    last = num; this
  }

  def -(num: Int) = minus(num)

  def minus(num: Int) = {
    last = -num; this
  }

  def months(and: Conjunction): DateDSL = months

  def month = months

  def month(and: Conjunction): DateDSL = months

  def months = {
    cal.add(Calendar.MONTH, last); this
  }

  def years(and: Conjunction): DateDSL = years

  def years = {
    cal.add(Calendar.YEAR, last); this
  }

  def year = years

  def year(and: Conjunction): DateDSL = years

  def days(and: Conjunction): DateDSL = days

  def days = {
    cal.add(Calendar.DAY_OF_MONTH, last); this
  }

  def day = days

  def day(and: Conjunction): DateDSL = days

  def is(sthen: Calendar) = {
    cal.setTimeInMillis(sthen.getTimeInMillis)
    cal.clear(Calendar.HOUR)
    this
  }

  def is(sthen: String) = {
    val cthen = ParseDate(sthen, "MM/dd/yyyy")
    cal.setTimeInMillis(cthen.getTimeInMillis)
    cal.clear(Calendar.HOUR)
    this
  }

  def ParseDate(s_date: String, s_format: String): Calendar = {
    var cal = Calendar.getInstance()
    cal.setTime(new SimpleDateFormat(s_format).parse(s_date, new ParsePosition(0)))
    cal
  }

  def before(d: DateDSL): Boolean = cal.before(d.cal)

  def after(d: DateDSL): Boolean = cal.after(d.cal)

  override def toString = new String(new SimpleDateFormat("ddMMMyy").format(cal.getTime()))
}

object DateDSL {

  val and = new Conjunction

  def today = Today

  def tomorrow = Tomorrow

  def Tomorrow = Today + 1 day

  def yesterday = Yesterday

  def Yesterday = Today - 1 day

  def Today = new DateDSL(Calendar.getInstance)

  def Now = Today

  def now = Today

  class Conjunction
}
