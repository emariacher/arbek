package kebra 

import java.io._
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import scala.language.experimental.macros
import scala.language.reflectiveCalls

//import scala.reflect.macros.Context
import scala.reflect.macros.whitebox._
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import java.text.ParsePosition
//import scala.annotation.StaticAnnotation
import scala.reflect.runtime.{universe => ru}

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

trait LogFunction {
  def log(msg: Any, srcFile: String = "", srcLine: Int = -1, srcClass: String = "")
}

class myPrintDln extends LogFunction {
  val s_time = new SimpleDateFormat("dd_HH:mm_ss,SSS").format(Calendar.getInstance.getTime) + " "

  def log(msg: Any, srcFile: String = "", srcLine: Int = -1, srcClass: String = "") {
    MyLog.myPrint(srcFile + ":" + srcLine + " " + s_time + " " + msg + "\n")
  }
}

trait LogFunctionE {
  def logE(msg: Any, srcFile: String = "", srcLine: Int = -1, srcClass: String = "")
}

class myErrPrintDln extends LogFunctionE {
  val s_time = new SimpleDateFormat("dd_HH:mm_ss,SSS").format(Calendar.getInstance.getTime) + " "

  def logE(msg: Any, srcFile: String = "", srcLine: Int = -1, srcClass: String = "") {
    MyLog.myErrPrint(srcFile + ":" + srcLine + " " + s_time + " " + msg + "\n")
  }
}


object MyLog {
  implicit val myPrintDln = new kebra.myPrintDln
  implicit val myErrPrintDln = new kebra.myErrPrintDln
  var g_t_start = Calendar.getInstance()
  var g_t_end = Calendar.getInstance()



  def timeStamp(c_t_start: Calendar, s_title: String): Calendar = {
    val t_end = Calendar.getInstance()
    myPrintln("    t_now: " + MyLog.printZisday(t_end, "ddMMMyy_HH_mm_ss_SSS [") + s_title +
      "] t_diff: " + (t_end.getTimeInMillis() - c_t_start.getTimeInMillis()))
    timeStampsList = timeStampsList :+((t_end.getTimeInMillis() - c_t_start.getTimeInMillis()), s_title);
    t_end
  }

  def timeStamp(c_t_start: Calendar): Long = Calendar.getInstance().getTimeInMillis() - c_t_start.getTimeInMillis()

  def timeStamp(s_title: String): Calendar = timeStamp(Calendar.getInstance(), s_title)

  def checkException(L: MyLog, i_rc: Int, s: String) {
    if (i_rc != 0) {
      L.myErrPrintln("**Exception** [" + s + "] thrown!")
      throw new Exception(s)
    };
    L.myPrintln("[" + MyLog.tag(2) + "] checkException [" + s + "]")
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

  def ParseDate(s_date: String, s_format: String): Calendar = {
    var cal = Calendar.getInstance()
    cal.setTime(new SimpleDateFormat(s_format).parse(s_date, new ParsePosition(0)))
    cal
  }

  def getExtension(f: File): String = {
    var ext = new String()
    val s = f.getName()
    val i = s.lastIndexOf('.')
    if (i > 0 && i < s.length() - 1) {
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
    val s_time = new SimpleDateFormat("dd_HH:mm_ss,SSS").format(Calendar.getInstance.getTime) + " "
    try {
      throw new Exception()
    } catch {
      case unknown: Throwable => unknown.getStackTrace.toList.apply(i_level).toString match {
        case MatchFileLine(file, line) => file + ":" + line + " " + s_time + " "
      }
    }
  }

  def tagnt(i_level: Int): String = {
    val s_time = new SimpleDateFormat("dd_HH:mm_ss,SSS").format(Calendar.getInstance.getTime) + " "
    try {
      throw new Exception()
    } catch {
      case unknown: Throwable => unknown.getStackTrace.toList.apply(i_level).toString match {
        case MatchFileLine(file, line) => file + ":" + line + " "
      }
    }
  }

  def func(i_level: Int): String = {
    val s_rien = "functionNotFound"
    try {
      throw new Exception()
    } catch {
      case unknown: Throwable => unknown.getStackTrace.toList.apply(i_level).toString match {
        case MatchFunc(funcs) => funcs.split('.').toList.last + " "
        case _ => s_rien
      }
    }
  }

  def myPrint(a: Any) = if (vierge) print(a) else L.myPrint(a)

  def myPrintln(a: Any) = myPrint(a + "\n")

  def myPrintD(a: Any) = myPrint(MyLog.tag(3) + " " + a)

  //def myPrintDln(a: Any) = myPrintD(a+"\n")
  def myErrPrint(a: Any) = if (vierge) System.err.print(a) else L.myErrPrint(a)

  def myErrPrintln(a: Any) = myErrPrint(a + "\n")

  def myErrPrintD(a: Any) = myErrPrint(MyLog.tag(3) + " " + a)

  //def myErrPrintDln(a: Any) = myErrPrintD(a+"\n")
  def myHErrPrint(a: Any) = if (vierge) System.err.print(a) else L.myHErrPrint(a)

  def myHErrPrintln(a: Any) = myHErrPrint(a + "\n")


  def printx(c: Context)(linecode: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._
    val msg = linecode.tree.toString
    reify(println(c.Expr[String](Literal(Constant(msg))).splice+" ---> "+linecode.splice))
  }
  def printIt(linecode: Any): Unit = macro printx

  def mprintx(c: scala.reflect.macros.whitebox.Context)(linecode: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._

    //val namez = (c.enclosingClass match {
    val namez = (c.internal.enclosingOwner match {
      case clazz @ ClassDef(_, _, _, _) => clazz.symbol.asClass.name
      case module @ ModuleDef(_, _, _)  => module.symbol.asModule.name
      case _                            => "" // not inside a class or a module. package object, REPL, somewhere else weird
    }).toString

    //val paramRep = show(s.tree)
    //c.Expr(q"""println($paramRep + " = " + $s)""")

    var f1rst = linecode.tree.productIterator.toList.head.toString
    if (f1rst.indexOf("scala") == 0) {
      f1rst = ""
    } else {
      f1rst = f1rst.replaceAll("scala.*\\]", "").replaceAll(namez+"\\.this\\.", "").replaceAll("List", "")
    }
    val s2cond = linecode.tree.productIterator.toList.last.toString.replaceAll("scala.*\\]", "").replaceAll(namez+"\\.this\\.", "").replaceAll("List", "")
    reify(myPrintDln(c.Expr[String](Literal(Constant(f1rst))).splice+" "+c.Expr[String](Literal(Constant(s2cond))).splice+" ---> "+linecode.splice+" ---> "+c.Expr[String](Literal(Constant(namez))).splice))
    //reify(println(c.Expr[String](Literal(Constant(f1rst))).splice+" "+c.Expr[String](Literal(Constant(s2cond))).splice+" ---> "+linecode.splice))
  }
  def myPrintIt2(linecode: Any): Any = macro mprintx

  def myPrintItMacro(c: Context)(s: c.Expr[Any]) : c.Expr[Unit] = {
    import c.universe._
    val paramRep = show(s.tree).replaceAll("scala.*\\]", "").replaceAll("List", "")
    //c.Expr(q"""println($paramRep + " = " + $s)""")
    c.Expr(q"""myPrintDln($paramRep + " ---> " + $s)""")
  }

  def myPrintIt(s: Any): Unit = macro myPrintItMacro


  def assert2(c: Context)(act: c.Expr[Any], exp: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._
    val paramRepAct = show(act.tree)
    val paramRepExp = show(exp.tree)

    //c.Expr(q"""myPrintDln($paramRepAct + "[" + $act + "] vs " + $paramRepExp + "[" + $exp + "]: "); assert($act==$exp,$paramRepAct + "[" + $act + "] vs " + $paramRepExp + "[" + $exp + "]: ")""")
    c.Expr(
      q"""if($act==$exp) myPrintDln("assert "+$paramRepAct + "[" + $act + "] vs " + $paramRepExp + "[" + $exp + "]: ") else assert($act==$exp,$paramRepAct + "[" + $act + "] vs " + $paramRepExp + "[" + $exp + "]: ")""")
  }

  def myAssert2(act: Any, exp: Any): Unit = macro assert2

  // trace function call with parameters
  def traceFuncMacro(c: Context)(f: c.Expr[(List[Any]) => Unit],p: c.Expr[Any]) : c.Expr[Unit] = {
    import c.universe._
    val funcRep = show(f.tree).split("\\.").toList.reverse.head.split("\\(").head
    val paramRep = show(p.tree).split("\\]").toList.reverse.head
    val line = Literal(Constant(c.enclosingPosition.line))
    val absolute = c.enclosingPosition.source.file.file.toURI
    val base = new File(".").toURI
    val path = Literal(Constant(c.enclosingPosition.source.file.file.getName()))
    val callfunc = Literal(Constant(c.internal.enclosingOwner.toString.split(" ")(1)))
    c.Expr(q"""myPrintln($path+":"+$line+" "+$callfunc+" "+$funcRep+"(" + $paramRep+"/"+$p+")"); $f($p)""")
  }

  def TraceFunc(f: (List[Any]) => Unit, p: Any): Unit = macro traceFuncMacro



  // get current line in source code
  def L_ : Int = macro lineImpl
  def lineImpl(c: Context): c.Expr[Int] = {
    import c.universe._
    val line = Literal(Constant(c.enclosingPosition.line))
    c.Expr[Int](line)
  }

  // get current file from source code (relative path)
  def F_ : String = macro fileImpl
  def fileImpl(c: Context): c.Expr[String] = {
    import c.universe._
    val absolute = c.enclosingPosition.source.file.file.toURI
    val base = new File(".").toURI
    val path = Literal(Constant(c.enclosingPosition.source.file.file.getName()))
    c.Expr[String](path)
  }

  // get current Method
  def C_ : String = macro classImpl
  def classImpl(c: Context): c.Expr[String] = {
    import c.universe._

    val class_ = Literal(Constant(c.internal.enclosingOwner.toString.split(" ")(1)))
    c.Expr[String](class_)
  }

  def myPrintDln(msg: Any)(implicit logFunc: LogFunction): Unit = macro logImpl
  def logImpl(c: Context)(msg: c.Expr[Any])(logFunc: c.Expr[LogFunction]): c.Expr[Unit] = {
    import c.universe._
    reify(logFunc.splice.log(msg.splice, srcFile = fileImpl(c).splice, srcLine = lineImpl(c).splice, srcClass = classImpl(c).splice))
  }

  def myErrPrintDln(msg: Any)(implicit logFunc: LogFunctionE): Unit = macro logImplE
  def logImplE(c: Context)(msg: c.Expr[Any])(logFunc: c.Expr[LogFunctionE]): c.Expr[Unit] = {
    import c.universe._
    reify(logFunc.splice.logE(msg.splice, srcFile = fileImpl(c).splice, srcLine = lineImpl(c).splice, srcClass = classImpl(c).splice))
  }

  def waiting(d: Duration) {
    val t0 = System.currentTimeMillis()
    var t1 = t0
    do {
      t1 = System.currentTimeMillis()
    } while (t1 - t0 < d.toMillis)
  }

  def mtimeStampx(c: scala.reflect.macros.whitebox.Context)(linecode: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._
    val msg = linecode.tree.toString
    reify({
      g_t_start = timeStamp(g_t_start, "---");
      linecode.splice;
      g_t_end = timeStamp(g_t_start, c.Expr[String](Literal(Constant(msg))).splice);
      timeStampsList = timeStampsList :+ ((g_t_end.getTimeInMillis() - g_t_start.getTimeInMillis()), c.Expr[String](Literal(Constant(msg))).splice);
    })
  }

  def timeStampIt(linecode: Any): Any = macro mtimeStampx

  def printTimeStampsList = if (!timeStampsList.isEmpty) myPrintln(timeStampsList.filter(_._2 != "---").map(t => (t._1+" ms", t._2.replaceAll(".this", ""))).distinct.mkString("TimeStampsList:\n  ", "\n  ", "\n  "))

  def toFileAndDisplay(fileName: String, htmlString: String) {
    val filo = new File(fileName)
    Some(new PrintWriter(filo)).foreach { p => p.write(htmlString); p.close }
    display(filo)
  }

  def display(filo: File) {
    java.awt.Desktop.getDesktop().browse(new java.net.URI("file:///"+filo.getCanonicalPath().replaceAll("\\\\", "/")))
  }

  def copyFromFile(fileName: String): String = Some(scala.io.Source.fromFile(fileName)).map(p => { val s = p.mkString; p.close; s }).mkString
  def copy2File(fileName: String, s: String) = Some(new PrintWriter(fileName)).foreach { p => p.write(s); p.close }

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

  def inverseMatrix(lin: List[List[Any]]): List[List[Any]] = {
    val size = lin.map(_.size).min
    if (lin.map(_.size).indexWhere(_ != size) >= 0) {
      myErrPrintln(MyLog.tag(3)+" Truncating size! "+lin.map(_.size))
    }
    lin.head.take(size).zipWithIndex.map(rangee => lin.map(_.apply(rangee._2)))
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
