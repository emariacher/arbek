package labyrinthe

import kebra.MyLog._
import java.io.File


object LL {
    var l: LL = _ 

            def getMyLL: LL = l
            def newMyLL(s_title: String, fil: File,errExt: String, traces: Boolean): LL = {
        newMyLog(this.getClass.getName,new File("out\\cowabunga"),"htm")
        l = new LL(traces)
        l
}
}

class LL(val traces: Boolean){
    def myPrint(s: String)   = {
        if(traces) {
            L.myPrint(s)
        }
    }
    def myPrintln(s: String) = myPrint(s + "\n")
            def myPrintD(s: String)   = myPrint(tag(3)+" "+s)         
            def myPrintDln(s: String) = myPrintD(s + "\n")
            def myErrPrint(s: String)   =  {
        if(traces) {
            L.myErrPrint(s)
        }
    }
    def myErrPrintln(s: String) = myErrPrint(s + "\n")
            def myErrPrintD(s: String)   = myErrPrint(tag(3)+" "+s)   
            def myErrPrintDln(s: String) = myErrPrintD(s + "\n")
}