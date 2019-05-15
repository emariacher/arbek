package labyrinthe

import kebra.MyLog._
import java.io.File

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
    def myPrint(s: String) = {
        if (traces) {
            L.myPrint(s)
        }
    }
    def myPrintln(a: Any) = myPrint(a + "\n")
    def myPrintD(a: Any) = myPrint(tag(3) + " " + a)
    def myPrintDln(a: Any) = myPrintD(a + "\n")
    def myErrPrint(a: Any) = {
        if (traces) {
            L.myErrPrint(a)
        }
    }
    def myErrPrintln(a: Any) = myErrPrint(a + "\n")
    def myErrPrintD(a: Any) = myErrPrint(tag(3) + " " + a)
    def myErrPrintDln(a: Any) = myErrPrintD(a + "\n")
}