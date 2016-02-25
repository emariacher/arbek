package kebra
import java.io.File
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date
import java.io.FileOutputStream
import java.util.Scanner
import java.io.FileNotFoundException
import javax.swing.filechooser.FileFilter
import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.JComponent
import javax.swing.JButton
import scala.swing._
import scala.swing.event._
import java.awt.Color
import akka.actor.{ ActorRef, ActorSystem, Props, Actor, Inbox }

trait LogFunction {
    def log(msg: Any, srcFile: String = "", srcLine: Int = -1, srcClass: String = "")
}

class myPrintDln extends LogFunction {
    val s_time = new SimpleDateFormat("dd_HH:mm_ss,SSS").format(Calendar.getInstance.getTime)+" "
    def log(msg: Any, srcFile: String = "", srcLine: Int = -1, srcClass: String = "") {
        MyLog.myPrint(srcFile+":"+srcLine+" "+s_time+" "+msg+"\n")
    }
}

trait LogFunctionE {
    def logE(msg: Any, srcFile: String = "", srcLine: Int = -1, srcClass: String = "")
}

class myErrPrintDln extends LogFunctionE {
    val s_time = new SimpleDateFormat("dd_HH:mm_ss,SSS").format(Calendar.getInstance.getTime)+" "
    def logE(msg: Any, srcFile: String = "", srcLine: Int = -1, srcClass: String = "") {
        MyLog.myErrPrint(srcFile+":"+srcLine+" "+s_time+" "+msg+"\n")
    }
}


object MyLog {
    implicit val myPrintDln = new kebra.myPrintDln
    implicit val myErrPrintDln = new kebra.myErrPrintDln
    def timeStamp(c_t_start: Calendar, s_title: String): Calendar = {
    val t_end = Calendar.getInstance()
    myPrintln("    t_now: "+MyLog.printZisday(t_end, "ddMMMyy_HH_mm_ss_SSS [") + s_title+
      "] t_diff: "+(t_end.getTimeInMillis() - c_t_start.getTimeInMillis()))
    timeStampsList = timeStampsList :+ ((t_end.getTimeInMillis() - c_t_start.getTimeInMillis()), s_title);
    t_end
  }

  def timeStamp(c_t_start: Calendar): Long = Calendar.getInstance().getTimeInMillis() - c_t_start.getTimeInMillis()

  def timeStamp(s_title: String): Calendar = timeStamp(Calendar.getInstance(), s_title)

    def checkException(L: MyLog, i_rc: Int, s: String) {
        if (i_rc != 0) {
            L.myErrPrintln("**Exception** ["+s+"] thrown!")
            throw new Exception(s)
        };
        L.myPrintln("["+MyLog.tag(2)+"] checkException ["+s+"]")
    }

	def printToday(fmt: String): String = {
			printZisday(Calendar.getInstance(), fmt)
	}

	def printToday(): String = {
			printToday("ddMMMyy");
	}

	def printZisday(zisday: Calendar, fmt: String): String = {
			printZisday(zisday.getTime(), fmt)
	}

	def printZisday(date: Date, fmt: String): String = {
			new String(new SimpleDateFormat(fmt).format(date))
	}

	def getExtension(f: File): String = {
			var ext  = new String()
			val s    = f.getName()
			val i       = s.lastIndexOf('.')
			if(i > 0 && i < s.length() - 1) {
				ext = s.substring(i + 1).toLowerCase()
			}
			ext
	}

    var system: ActorSystem = _
var timeStampsList = List.empty[(Long, String)]
    val MatchFileLine = """.+\((.+)\..+:(\d+)\)""".r
    val MatchFunc = """(.+)\(.+""".r
    val r_name = """Expr\[String\]\(\"(.+)\"\)""".r
    var L: MyLog = _
    var vierge = true
	
	    def getMylog: MyLog = L
    def newMyLog(s_title: String, fil: File, errExt: String): MyLog = {
        if (vierge) {
            system = ActorSystem("MyLog")
            L = new MyLog(s_title, fil, errExt)
            L.launchActorAndGui
            vierge = false
        }
        L
    }

    	    def tag(i_level: Int): String = {
        val s_time = new SimpleDateFormat("dd_HH:mm_ss,SSS").format(Calendar.getInstance.getTime)+" "
        try {
            throw new Exception()
        } catch {
            case unknown: Throwable => unknown.getStackTrace.toList.apply(i_level).toString match {
                case MatchFileLine(file, line) => file+":"+line+" "+s_time+" "
            }
        }
    }

    def tagnt(i_level: Int): String = {
        val s_time = new SimpleDateFormat("dd_HH:mm_ss,SSS").format(Calendar.getInstance.getTime)+" "
        try {
            throw new Exception()
        } catch {
            case unknown: Throwable => unknown.getStackTrace.toList.apply(i_level).toString match {
                case MatchFileLine(file, line) => file+":"+line+" "
            }
        }
    }

    def func(i_level: Int): String = {
        val s_rien = "functionNotFound"
        try {
            throw new Exception()
        } catch {
            case unknown: Throwable => unknown.getStackTrace.toList.apply(i_level).toString match {
                case MatchFunc(funcs) => funcs.split('.').toList.last+" "
                case _                => s_rien
            }
        }
    }

        def myPrint(a: Any) = if (vierge) print(a) else L.myPrint(a)
    def myPrintln(a: Any) = myPrint(a+"\n")
    def myPrintD(a: Any) = myPrint(MyLog.tag(3)+" "+a)
    //def myPrintDln(a: Any) = myPrintD(a+"\n")
    def myErrPrint(a: Any) = if (vierge) System.err.print(a) else L.myErrPrint(a)
    def myErrPrintln(a: Any) = myErrPrint(a+"\n")
    def myErrPrintD(a: Any) = myErrPrint(MyLog.tag(3)+" "+a)
    //def myErrPrintDln(a: Any) = myErrPrintD(a+"\n")
    def myHErrPrint(a: Any) = if (vierge) System.err.print(a) else L.myHErrPrint(a)
    def myHErrPrintln(a: Any) = myHErrPrint(a+"\n")

}

class MyLog(s_title: String, fil: File, errExt: String) {

    var name_no_ext = new String
    var files = List[MyFile]()
    val t_start = Calendar.getInstance()
    val name = fil.getName()
    val CanonicalPath = fil.getCanonicalPath()
    val index = CanonicalPath.indexOf(name)
    val ext = MyLog.getExtension(fil)
    if ((ext != null) && (name.lastIndexOf(ext) > 1)) {
        name_no_ext = name.substring(0, name.lastIndexOf(ext) - 1)
    }
    val working_directory = new String(CanonicalPath.substring(0, index))
    val logfileName = working_directory+"log"+File.separatorChar+"logfile_"+
        MyLog.printToday("ddMMMyy_HH_mm")+".log"
    val errfileName = working_directory + File.separatorChar+"out_"+name_no_ext.replaceAll("\\W", "_")+"_"+
        MyLog.printToday("ddMMMyy_HH_mm")+"."+errExt
    var MLA: ActorRef = _
    var Gui: MyUI = _
    var b_GuiActive = false

    def launchActorAndGui {
        MLA = MyLog.system.actorOf(Props[MyLogActor], name = "MLA")
        Gui = new MyUI("", new ZeParameters())
    }

    def createGui(parameters: ZeParameters) {
        Gui = new MyUI(s_title, parameters)
        b_GuiActive = true
    }

    def myPrint(a: Any) = MLA ! logMsg("L", a)
    def myPrintln(a: Any) = myPrint(a+"\n")
    def myPrintD(a: Any) = myPrint(MyLog.tag(3)+" "+a)
    def myPrintDln(a: Any) = myPrintD(a+"\n")
    def myErrPrint(a: Any) = MLA ! logMsg("E", a)
    def myErrPrintln(a: Any) = myErrPrint(a+"\n")
    def myErrPrintD(a: Any) = myErrPrint(MyLog.tag(3)+" "+a)
    def myErrPrintDln(a: Any) = myErrPrintD(a+"\n")
    def myHErrPrint(a: Any) = MLA ! logMsg("H", a)
    def myHErrPrintln(a: Any) = myHErrPrint(a+"\n")

    def closeFiles() {
        val t_end = Calendar.getInstance()
        myPrintDln("t_end: "+MyLog.printZisday(t_end, "ddMMMyy_HH_mm_ss_SSS"))
        myErrPrintDln("l_diff: "+(t_end.getTimeInMillis() - t_start.getTimeInMillis()))
        MLA ! closeMsg
        Thread.sleep(100)
    }

    def hcloseFiles(headerFileName: String, postProcessFunc: (List[String], String) => String) {
        val t_end = Calendar.getInstance()
        myPrintDln("t_end: "+MyLog.printZisday(t_end, "ddMMMyy_HH_mm_ss_SSS"))
        myPrintDln("l_diff: "+(t_end.getTimeInMillis() - t_start.getTimeInMillis()))
        MLA ! hcloseMsg(headerFileName, postProcessFunc)
        Thread.sleep(100)
    }

    def getJarFileCompilationDate(): String = {
        val jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        "["+jarFile.getName()+"] Version: "+new Date(jarFile.lastModified()).toString();
    }
}

sealed trait MyLogMsg
case object closeMsg extends MyLogMsg
case class hcloseMsg(javascriptHeader: String, postProcessFunc: (List[String], String) => String) extends MyLogMsg
case class logMsg(errorType: String, msg: Any) extends MyLogMsg

class MyLogActor extends Actor {
    val L = MyLog.getMylog
    var files = List[MyFile]()
    var hlines = List[String]()
    val logfile = new MyFile(L.logfileName)
    files :+ logfile
    val errfile = new MyFile(L.errfileName)
    files :+ errfile

    var b_filesActive = true

    def receive = {
        case hcloseMsg(javascriptHeader, postProcessFunc) =>
            b_filesActive = false
            errfile.writeFile(postProcessFunc(hlines, javascriptHeader))
            files.foreach(_.close)
            context.system.shutdown()
        //exit
        case logMsg(errorType, msg) => errorType match {
            case "H" =>
                if (b_filesActive) {
                    hlines = hlines ++ msg.toString.split("\n").toList
                    logfile.writeFile(msg.toString)
                }
                System.err.print(msg)
            case "E" =>
                if (b_filesActive) {
                    errfile.writeFile(msg.toString)
                    logfile.writeFile(msg.toString)
                }
                System.err.print(msg)
            case "L" =>
                if (b_filesActive) {
                    logfile.writeFile(msg.toString)
                }
                System.out.print(msg)
            case _ => throw new Exception("wrong trace type: "+errorType.toString)
        }
        case closeMsg =>
            b_filesActive = false
            files.foreach(_.close)
            context.system.shutdown()
        //exit
    }

}
