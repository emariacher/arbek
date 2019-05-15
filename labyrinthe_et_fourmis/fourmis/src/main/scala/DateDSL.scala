package kebra

import java.util.Calendar
import java.text.SimpleDateFormat
import java.text.ParsePosition
import language.postfixOps

class DateDSL(val cal: Calendar) {
    cal.clear(Calendar.HOUR)
    import DateDSL.Conjunction

    def this(d: DateDSL) = this(d.cal)

    private var last = 0;

    def plus(num: Int) = { last = num; this }
    def minus(num: Int) = { last = -num; this }

    def +(num: Int) = plus(num)
    def -(num: Int) = minus(num)

    def months = { cal.add(Calendar.MONTH, last); this }
    def months(and: Conjunction): DateDSL = months
    def month = months
    def month(and: Conjunction): DateDSL = months

    def years = { cal.add(Calendar.YEAR, last); this }
    def years(and: Conjunction): DateDSL = years
    def year = years
    def year(and: Conjunction): DateDSL = years

    def days = { cal.add(Calendar.DAY_OF_MONTH, last); this }
    def days(and: Conjunction): DateDSL = days
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
    class Conjunction
    val and = new Conjunction

    def Today = new DateDSL(Calendar.getInstance)
    def Tomorrow = Today + 1 day
    def Yesterday = Today - 1 day

    def today = Today
    def tomorrow = Tomorrow
    def yesterday = Yesterday

    def Now = Today
    def now = Today
}
