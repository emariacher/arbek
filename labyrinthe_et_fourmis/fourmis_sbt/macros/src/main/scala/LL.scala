package kebra

import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import MyLog._

object LL {
  var l: LL = _

  def getMyLL: LL = l

  def newMyLL(s_title: String, fil: File, errExt: String, traces: Boolean): LL = {
    newMyLog(this.getClass.getName, new File("out\\cowabunga"), "htm")
    l = new LL(traces)
    l
  }
}

class LL(val traces: Boolean) {
  def myPrint(s: String): Unit = {
    if (traces) {
      L.myPrint(s)
    }
  }

  def myPrintln(a: Any): Unit = myPrint(a.toString + "\n")

  def myPrintD(a: Any): Unit = myPrint(tag(3) + " " + a)

  def myPrintDln(a: Any): Unit = myPrintD(a.toString + "\n")

  def myErrPrint(a: Any): Unit = {
    if (traces) {
      L.myErrPrint(a)
    }
  }

  def myErrPrintln(a: Any): Unit = myErrPrint(a.toString + "\n")

  def myErrPrintD(a: Any): Unit = myErrPrint(tag(3) + " " + a)

  def myErrPrintDln(a: Any): Unit = myErrPrintD(a.toString + "\n")

  def tag(i_level: Int): String = {
    val s_time = new SimpleDateFormat("dd_HH:mm_ss,SSS").format(Calendar.getInstance.getTime) + " "
    try {
      throw new Exception()
    } catch {
      case unknown: Throwable => unknown.getStackTrace.toList.apply(i_level).toString match {
        case MatchFileLine(file, line) => file + ":" + line + " " + s_time
      }
    }
  }
}