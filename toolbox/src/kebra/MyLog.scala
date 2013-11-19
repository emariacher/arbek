package kebra

import java.io._
import java.util.Calendar
import java.text.SimpleDateFormat
import akka.actor._
import java.text.ParsePosition
import java.util.Date
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import scala.language.experimental.macros
import scala.reflect.macros.Context
import language.reflectiveCalls

object MyLog {
    var system: ActorSystem = _

    val MatchFileLine = """.+\((.+)\..+:(\d+)\)""".r
    val MatchFunc = """(.+)\(.+""".r
    var L: MyLog = _
    var vierge = true
    
    var g_t_start = Calendar.getInstance();

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

    def closeFiles {
        if (!vierge) {
            L.closeFiles
        } else {
            myPrintDln("MyLog not activated")
        }
    }

    def myPrint(a: Any) = if (vierge) print(a) else L.myPrint(a)
    def myPrintln(a: Any) = myPrint(a + "\n")
    def myPrintD(a: Any) = myPrint(MyLog.tag(3) + " " + a)
    def myPrintDln(a: Any) = myPrintD(a + "\n")
    def myErrPrint(a: Any) = if (vierge) System.err.print(a) else L.myErrPrint(a)
    def myErrPrintln(a: Any) = myErrPrint(a + "\n")
    def myErrPrintD(a: Any) = myErrPrint(MyLog.tag(3) + " " + a)
    def myErrPrintDln(a: Any) = myErrPrintD(a + "\n")
    def myHErrPrint(a: Any) = if (vierge) System.err.print(a) else L.myHErrPrint(a)
    def myHErrPrintln(a: Any) = myHErrPrint(a + "\n")

    def printToday(fmt: String): String = printZisday(Calendar.getInstance(), fmt)

    def printToday: String = printToday("ddMMMyy")

    def printZisday(zisday: Calendar, fmt: String): String = new String(new SimpleDateFormat(fmt).format(zisday.getTime))
    def printZisday(date: Date, fmt: String): String = new String(new SimpleDateFormat(fmt).format(date))
    def printZisday(time: Long): String = MyLog.printZisday(new Date(time), "ddMMM HH:mm")

    def ParseDate(s_date: String, s_format: String): Calendar = {
        var cal = Calendar.getInstance()
        cal.setTime(new SimpleDateFormat(s_format).parse(s_date, new ParsePosition(0)))
        cal
    }

    def getExtension(f: File): String = {
        var ext = new String
        val s = f.getName
        val i = s.lastIndexOf('.')
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase
        }
        ext
    }
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

    def tagnt(i_level: Int): String = {
        val s_time = new SimpleDateFormat("dd_HH:mm_ss,SSS").format(Calendar.getInstance.getTime) + " "
        try {
            throw new Exception()
        } catch {
            case unknown: Throwable => unknown.getStackTrace.toList.apply(i_level).toString match {
                case MatchFileLine(file, line) => file + ":" + line
            }
        }
    }

    def func(i_level: Int): String = {
        val s_rien = "functionNotFound"
        try {
            throw new Exception()
        } catch {
            case unknown: Throwable => unknown.getStackTrace.toList.apply(i_level).toString match {
                case MatchFunc(funcs) => funcs.split('.').toList.last
                case _                => s_rien
            }
        }
    }

    def km_func2(c: Context) = {
        import c.universe._
        c.enclosingDef match {
            case DefDef(mods, name, tparams, vparamss, tpt, rhs) =>
                c.universe.reify(println(
                    "\n  mods " + c.literal(mods.toString).splice
                        + "\n  name " + c.literal(name.toString).splice
                        + "\n  tparams " + c.literal(tparams.toString).splice
                        + "\n  vparamss " + c.literal(vparamss.toString).splice
                        + "\n  tpt " + c.literal(tpt.toString).splice
                        + "\n  rhs " + c.literal(rhs.toString).splice))
            case _ => c.abort(c.enclosingPosition, "NoEnclosingMethod")
        }
    }
    def km_func(c: Context) = {
        import c.universe._
        c.enclosingDef match {
            case DefDef(mods, name, tparams, vparamss, tpt, rhs) =>
                c.universe.reify(c.literal("#"+name.toString).splice+"# ")
            case _ => c.abort(c.enclosingPosition, "NoEnclosingMethod")
        }
    }
    def km_class3(c: Context) = {
        import c.universe._
        c.enclosingImpl match {
            case ClassDef(mods, name, tparams, impl) =>
                c.universe.reify(println(
                    "\n  mods " + c.literal(mods.toString).splice
                        + "\n  name " + c.literal(name.toString).splice
                        + "\n  tparams " + c.literal(tparams.toString).splice
                        + "\n  impl " + c.literal(impl.toString).splice))
            case _ => c.abort(c.enclosingPosition, "NoEnclosingClass")
        }
    }
    def __FUNC__ = macro km_func
    def __CLASS__ = macro km_class3

    def assertx(c: Context)(cond: c.Expr[Boolean]): c.Expr[Unit] = {
        import c.universe._
        val msg = cond.tree.toString
        reify(assert(cond.splice, c.Expr[String](Literal(Constant(msg))).splice))
    }
    /*def assert3(cond: Boolean, linecode: String) = {
                if(cond) {
                    println("assertOK("+linecode+")")
                } else {
                    assert(false,linecode)
                }
            }*/
    def myAssert(cond: Boolean) = macro assertx

    def assert2(c: Context)(act: c.Expr[Any], exp: c.Expr[Any]): c.Expr[Unit] = {
        import c.universe._
        val actm = act.tree.toString
        val expm = exp.tree.toString
        reify({
            if (act.splice != exp.splice) {
                try {
                    throw new Exception("AssertionError: " + c.Expr[String](Literal(Constant(actm))).splice + "[" + act.splice + "]==[" + exp.splice + "]" + c.Expr[String](Literal(Constant(expm))).splice)
                } catch {
                    case unknown: Throwable => System.err.println("" + unknown + unknown.getStackTrace.toList.filter(_.toString.indexOf("scala.") != 0).mkString("\n  ", "\n  ", "\n  ")); sys.exit
                }
            }
        })
    }
    def myAssert2(act: Any, exp: Any) = macro assert2
    def myRequire2(act: Any, exp: Any) = macro require2

    def require2(c: Context)(act: c.Expr[Any], exp: c.Expr[Any]): c.Expr[Unit] = {
        import c.universe._
        val actm = act.tree.toString
        val expm = exp.tree.toString
        reify({
            if (act.splice != exp.splice) {
                try {
                    throw new Exception("RequireError: " + c.Expr[String](Literal(Constant(actm))).splice + "[" + act.splice + "]==[" + exp.splice + "]" + c.Expr[String](Literal(Constant(expm))).splice)
                } catch {
                    case unknown: Throwable => System.err.println("" + unknown + unknown.getStackTrace.toList.filter(_.toString.indexOf("scala.") != 0).mkString("\n  ", "\n  ", "\n  ")); sys.exit
                }
            }
        })
    }

    def requirex(c: Context)(cond: c.Expr[Boolean]): c.Expr[Unit] = {
        import c.universe._
        val msg = cond.tree.toString
        reify(require(cond.splice, c.Expr[String](Literal(Constant(msg))).splice))
    }
    def myRequire(cond: Boolean) = macro requirex

    def printx(c: Context)(linecode: c.Expr[Any]): c.Expr[Unit] = {
        import c.universe._
        val msg = linecode.tree.toString
        reify(println(c.Expr[String](Literal(Constant(msg))).splice + " ---> " + linecode.splice))
    }
    def printIt(linecode: Any) = macro printx

    def mprintx(c: Context)(linecode: c.Expr[Any]): c.Expr[Unit] = {
        import c.universe._
        val msg = linecode.tree.toString
        reify(myPrintDln(c.Expr[String](Literal(Constant(msg))).splice + "\n    ---> " + linecode.splice))
    }
    def myPrintIt(linecode: Any) = macro mprintx

    def raise(msg: Any) = throw new AssertionError(msg)
    //def myAssert(c: Context)(cond: c.Expr[Boolean]) : c.Expr[Unit] =  <[ if (!cond) raise("NOGOOD!") ]>

    def waiting(d: Duration) {
        val t0 = System.currentTimeMillis()
        var t1 = t0
        do {
            t1 = System.currentTimeMillis()
        } while (t1 - t0 < d.toMillis)
    }

    def mtimeStampx(c: Context)(linecode: c.Expr[Any]): c.Expr[Unit] = {
        import c.universe._
        val msg = linecode.tree.toString
        reify({g_t_start = timeStamp(g_t_start, c.Expr[String](Literal(Constant(msg))).splice); linecode.splice;})
    }
    
        def timeStampIt(linecode: Any) = macro mtimeStampx
    
    def timeStamp(c_t_start: Calendar, s_title: String): Calendar = {
        val t_end = Calendar.getInstance();
        myPrintln("    t_now: " + MyLog.printZisday(t_end, "ddMMMyy_HH_mm_ss_SSS [") + s_title +
            "] t_diff: " + (t_end.getTimeInMillis() - c_t_start.getTimeInMillis()));
        t_end;
    }

    def timeStamp(c_t_start: Calendar): Long = Calendar.getInstance().getTimeInMillis() - c_t_start.getTimeInMillis()

    def timeStamp(s_title: String): Calendar = {
        timeStamp(L.t_start, s_title)
    }

    def checkException(L: MyLog, i_rc: Int, s: String) {
        if (i_rc != 0) {
            L.myErrPrintln("**Exception** [" + s + "] thrown!")
            throw new Exception(s)
        };
        L.myPrintln("[" + MyLog.tag(2) + "] checkException [" + s + "]")
    }
    def copy(from: String, to: String) {
        use(new FileInputStream(from)) { in =>
            use(new FileOutputStream(to)) { out =>
                val buffer = new Array[Byte](1024)
                Iterator.continually(in.read(buffer))
                    .takeWhile(_ != -1)
                    .foreach { out.write(buffer, 0, _) }
            }
        }
    }

    def copy(from: String, to: MyFile) {
        use(new FileInputStream(from)) { in =>
            val buffer = new Array[Byte](1024)
            Iterator.continually(in.read(buffer))
                .takeWhile(_ != -1)
                .foreach { to.fos.write(buffer, 0, _) }
        }
    }

    def copy(from: String): String = {
        var to = ""
        use(new FileReader(from)) { in =>
            val buffer = new Array[Char](1024)
            Iterator.continually(in.read(buffer))
                .takeWhile(_ != -1)
                .foreach { (i: Int) => to += buffer.toList.take(i).mkString("") }
        }
        to
    }

    def use[T <: { def close(): Unit }](closable: T)(block: T => Unit) {
        try {
            block(closable)
        } finally {
            closable.close()
        }
    }
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
    val logfileName = working_directory + "log" + File.separatorChar + "logfile_" +
        MyLog.printToday("ddMMMyy_HH_mm") + ".log"
    val errfileName = working_directory + File.separatorChar + "out_" + name_no_ext.replaceAll("\\W", "_") + "_" +
        MyLog.printToday("ddMMMyy_HH_mm") + "." + errExt
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
    def myPrintln(a: Any) = myPrint(a + "\n")
    def myPrintD(a: Any) = myPrint(MyLog.tag(3) + " " + a)
    def myPrintDln(a: Any) = myPrintD(a + "\n")
    def myErrPrint(a: Any) = MLA ! logMsg("E", a)
    def myErrPrintln(a: Any) = myErrPrint(a + "\n")
    def myErrPrintD(a: Any) = myErrPrint(MyLog.tag(3) + " " + a)
    def myErrPrintDln(a: Any) = myErrPrintD(a + "\n")
    def myHErrPrint(a: Any) = MLA ! logMsg("H", a)
    def myHErrPrintln(a: Any) = myHErrPrint(a + "\n")

    def closeFiles() {
        val t_end = Calendar.getInstance()
        myPrintDln("t_end: " + MyLog.printZisday(t_end, "ddMMMyy_HH_mm_ss_SSS"))
        myErrPrintDln("l_diff: " + (t_end.getTimeInMillis() - t_start.getTimeInMillis()))
        MLA ! closeMsg
        Thread.sleep(100)
    }

    def hcloseFiles(headerFileName: String, postProcessFunc: (List[String], String) => String) {
        val t_end = Calendar.getInstance()
        myPrintDln("t_end: " + MyLog.printZisday(t_end, "ddMMMyy_HH_mm_ss_SSS"))
        myPrintDln("l_diff: " + (t_end.getTimeInMillis() - t_start.getTimeInMillis()))
        MLA ! hcloseMsg(headerFileName, postProcessFunc)
        Thread.sleep(100)
    }

    def getJarFileCompilationDate(): String = {
        val jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        "[" + jarFile.getName() + "] Version: " + new Date(jarFile.lastModified()).toString();
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
            case _ => throw new Exception("wrong trace type: " + errorType.toString)
        }
        case closeMsg =>
            b_filesActive = false
            files.foreach(_.close)
            context.system.shutdown()
        //exit
    }

}

