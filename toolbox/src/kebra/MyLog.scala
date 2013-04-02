package kebra

import java.io._
import java.util.Calendar
import java.text.SimpleDateFormat
import java.io.FileOutputStream
import java.util.Scanner
import javax.swing.filechooser.FileFilter
import javax.swing.UIManager
import scala.swing._
import scala.swing.event._
import java.awt.Color
import akka.actor._
import java.text.ParsePosition
import java.util.Date
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import scala.language.experimental.macros
import scala.reflect.macros.Context
import java.net.URL
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor


object MyLog {

    val MatchFileLine = """.+\((.+)\..+:(\d+)\)""".r
            val MatchFunc = """(.+)\(.+""".r
            var L: MyLog = _ 
            var vierge = true

            def getMylog: MyLog = L
            def newMyLog(s_title: String, fil: File,errExt: String): MyLog = {
            if(vierge) {
                L = new MyLog(s_title, fil,errExt)
                L.launchActorAndGui
                vierge = false
            }
            L
    }

    def myPrint(s: String)   = if(vierge) print(s) else L.myPrint(s)
            def myPrintln(s: String) = myPrint(s + "\n")
            def myPrintD(s: String)   = myPrint(MyLog.tag(3)+" "+s)         
            def myPrintDln(s: String) = myPrintD(s + "\n")
            def myErrPrint(s: String)   = if(vierge) System.err.print(s) else L.myErrPrint(s)
            def myErrPrintln(s: String) = myErrPrint(s + "\n")
            def myErrPrintD(s: String)   = myErrPrint(MyLog.tag(3)+" "+s)   
            def myErrPrintDln(s: String) = myErrPrintD(s + "\n")
            def myHErrPrint(s: String)   = if(vierge) System.err.print(s) else L.myHErrPrint(s)
            def myHErrPrintln(s: String) = myHErrPrint(s + "\n")


            def printToday(fmt: String): String = printZisday(Calendar.getInstance(), fmt)

            def printToday: String =printToday("ddMMMyy")

            def printZisday(zisday: Calendar, fmt: String): String = new String(new SimpleDateFormat(fmt).format(zisday.getTime))
            def printZisday(date: Date, fmt: String): String = new String(new SimpleDateFormat(fmt).format(date))
            def printZisday(time: Long): String = MyLog.printZisday(new Date(time),"ddMMM HH:mm")

            def ParseDate(s_date: String, s_format: String): Calendar = {
                var cal = Calendar.getInstance()
                        cal.setTime(new SimpleDateFormat(s_format).parse(s_date, new ParsePosition(0)))
                        cal
            }

            def getExtension(f: File): String = {
                var ext  = new String
                        val s    = f.getName
                        val i       = s.lastIndexOf('.')
                        if(i > 0 && i < s.length() - 1) {
                            ext = s.substring(i + 1).toLowerCase
                        }
                ext
            }
            def tag(i_level: Int): String = {
                val s_time = new SimpleDateFormat("dd_HH:mm_ss,SSS").format(Calendar.getInstance.getTime)+" "
                        try {
                            throw new Exception()
                        } catch {
                        case unknown : Throwable => unknown.getStackTrace.toList.apply(i_level).toString match {
                        case MatchFileLine(file, line) => file+":"+line+" "+s_time
                        }
                        }
            }

            def tagnt(i_level: Int): String = {
                val s_time = new SimpleDateFormat("dd_HH:mm_ss,SSS").format(Calendar.getInstance.getTime)+" "
                        try {
                            throw new Exception()
                        } catch {
                        case unknown : Throwable => unknown.getStackTrace.toList.apply(i_level).toString match {
                        case MatchFileLine(file, line) => file+":"+line
                        }
                        }
            }

            def func(i_level: Int): String = {
                val s_rien = "functionNotFound"
                        try {
                            throw new Exception()
                        } catch {
                        case unknown : Throwable => unknown.getStackTrace.toList.apply(i_level).toString match {
                        case MatchFunc(funcs) => funcs.split('.').toList.last
                        case _ => s_rien
                        }
                        }
            }

            def km_func2(c: Context) = {
                import c.universe._
                c.enclosingMethod match {
                case DefDef(mods, name, tparams, vparamss, tpt, rhs) =>
                c.universe.reify(println(
                        "\n  mods "+c.literal(mods.toString).splice
                        +"\n  name "+c.literal(name.toString).splice
                        +"\n  tparams "+c.literal(tparams.toString).splice
                        +"\n  vparamss "+c.literal(vparamss.toString).splice
                        +"\n  tpt "+c.literal(tpt.toString).splice
                        +"\n  rhs "+c.literal(rhs.toString).splice
                        ))
                case _ => c.abort(c.enclosingPosition, "NoEnclosingMethod")
                }
            }
            def km_class3(c: Context) = {
                import c.universe._
                c.enclosingClass match {
                case ClassDef(mods, name, tparams, impl) =>
                c.universe.reify(println(
                        "\n  mods "+c.literal(mods.toString).splice
                        +"\n  name "+c.literal(name.toString).splice
                        +"\n  tparams "+c.literal(tparams.toString).splice
                        +"\n  impl "+c.literal(impl.toString).splice
                        ))
                case _ => c.abort(c.enclosingPosition, "NoEnclosingClass")
                }
            }
            def __FUNC__ = macro km_func2
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

                    def assert2(c: Context)(act: c.Expr[Any],exp: c.Expr[Any]): c.Expr[Unit] = {
                import c.universe._
                val actm = act.tree.toString
                val expm = exp.tree.toString
                reify({
                    if(act.splice!=exp.splice) {
                        try {
                            throw new Exception("AssertionError: "+c.Expr[String](Literal(Constant(actm))).splice+"["+act.splice+"]==["+exp.splice+"]"+c.Expr[String](Literal(Constant(expm))).splice)
                        } catch {
                        case unknown: Throwable => System.err.println(""+unknown+unknown.getStackTrace.toList.filter(_.toString.indexOf("scala.")!=0).mkString("\n  ","\n  ","\n  ")); sys.exit
                        }
                    }
                })
            }
            def myAssert2(act: Any, exp: Any) = macro assert2

                    def requirex(c: Context)(cond: c.Expr[Boolean]): c.Expr[Unit] = {
                import c.universe._
                val msg = cond.tree.toString
                reify(require(cond.splice, c.Expr[String](Literal(Constant(msg))).splice))
            }
            def myRequire(cond: Boolean) = macro requirex

                    def printx(c: Context)(linecode: c.Expr[Any]): c.Expr[Unit] = {
                import c.universe._
                val msg = linecode.tree.toString
                reify(println(c.Expr[String](Literal(Constant(msg))).splice+" ---> "+linecode.splice))
            }
            def printIt(linecode: Any) = macro printx

                    def mprintx(c: Context)(linecode: c.Expr[Any]): c.Expr[Unit] = {
                import c.universe._
                val msg = linecode.tree.toString
                reify(L.myPrintDln(c.Expr[String](Literal(Constant(msg))).splice+" ---> "+linecode.splice))
            }
            def myPrintIt(linecode: Any) = macro printx


                    def raise(msg: Any) = throw new AssertionError(msg)
            //def myAssert(c: Context)(cond: c.Expr[Boolean]) : c.Expr[Unit] =  <[ if (!cond) raise("NOGOOD!") ]>


            def waiting (d: Duration){
                val t0 =  System.currentTimeMillis()
                        var t1 = t0
                        do{
                            t1 = System.currentTimeMillis()
                        }
                        while (t1 - t0 < d.toMillis)
            }

            def timeStamp(c_t_start: Calendar, s_title: String): Calendar = {
                    val t_end = Calendar.getInstance();
                    myPrintDln("t_now: " + MyLog.printZisday(t_end,"ddMMMyy_HH_mm_ss_SSS [")+ s_title +
                            "] t_diff: " + (t_end.getTimeInMillis() - c_t_start.getTimeInMillis()));
                    t_end;
            }

            def timeStamp(c_t_start: Calendar): Long = Calendar.getInstance().getTimeInMillis() - c_t_start.getTimeInMillis()

                    def timeStamp(s_title: String): Calendar = {
                timeStamp(L.t_start, s_title)
            }


            def checkException(L: MyLog, i_rc: Int, s: String) {
                if(i_rc!=0) {
                    L.myErrPrintln("**Exception** ["+s+"] thrown!")
                    throw new Exception(s)
                };
                L.myPrintln("["+MyLog.tag(2)+"] checkException ["+s+"]")
            }
            def copy(from: String, to: String) {
                use(new FileInputStream(from)) { in =>
                use(new FileOutputStream(to)) { out =>
                val buffer = new Array[Byte](1024)
                Iterator.continually(in.read(buffer))
                .takeWhile(_ != -1)
                .foreach { out.write(buffer, 0 , _) }
                }
                }
            }

            def copy(from: String, to: MyFile) {
                use(new FileInputStream(from)) { in =>
                val buffer = new Array[Byte](1024)
                Iterator.continually(in.read(buffer))
                .takeWhile(_ != -1)
                .foreach { to.fos.write(buffer, 0 , _) }
                }
            }

            def copy(from: String): String = {
                    var to=""
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
                }
                finally {
                    closable.close()
                }
            }
}

class MyLog(s_title: String, fil: File,errExt: String) {
    val system = ActorSystem("MyLog")

            var name_no_ext = new String
            var files = List[MyFile]()
            val t_start = Calendar.getInstance()	
            val name           = fil.getName()
            val CanonicalPath  = fil.getCanonicalPath()
            val index             = CanonicalPath.indexOf(name)
            val ext            = MyLog.getExtension(fil)
            if((ext != null) && (name.lastIndexOf(ext) > 1)) {
                name_no_ext = name.substring(0, name.lastIndexOf(ext) - 1)
            }
    val working_directory = new String(CanonicalPath.substring(0, index))
    val logfileName = working_directory+"log"+File.separatorChar+"logfile_" + 
            MyLog.printToday("ddMMMyy_HH_mm") + ".log"
            val errfileName = working_directory + File.separatorChar+"out_"+name_no_ext.replaceAll("\\W", "_")+"_" + 
                    MyLog.printToday("ddMMMyy_HH_mm") + "."+errExt
                    var MLA: ActorRef = _
                    var Gui: MyUI = _
                    var b_GuiActive = false

                    def launchActorAndGui {
        MLA = system.actorOf(Props[MyLogActor], name = "MLA")
                Gui = new MyUI("",new ZeParameters())  
    }

    def createGui(parameters: ZeParameters) {
        Gui = new MyUI(s_title, parameters)
        b_GuiActive = true
    }


    def myPrint(s: String)   = MLA ! logMsg("L",s)
            def myPrintln(s: String) = myPrint(s + "\n")
            def myPrintD(s: String)   = myPrint(MyLog.tag(3)+" "+s)         
            def myPrintDln(s: String) = myPrintD(s + "\n")
            def myErrPrint(s: String)   = MLA ! logMsg("E",s)
            def myErrPrintln(s: String) = myErrPrint(s + "\n")
            def myErrPrintD(s: String)   = myErrPrint(MyLog.tag(3)+" "+s)   
            def myErrPrintDln(s: String) = myErrPrintD(s + "\n")
            def myHErrPrint(s: String)   = MLA ! logMsg("H",s)
            def myHErrPrintln(s: String) = myHErrPrint(s + "\n")

            def closeFiles() {
        val t_end = Calendar.getInstance()
                myPrintDln("t_end: " + MyLog.printZisday(t_end,"ddMMMyy_HH_mm_ss_SSS"))
                myErrPrintDln("l_diff: " + (t_end.getTimeInMillis() - t_start.getTimeInMillis()))
                MLA ! closeMsg
                Thread.sleep(100)
    }

    def hcloseFiles(headerFileName: String, postProcessFunc: (List[String], String) => String) {
        val t_end = Calendar.getInstance()
                myPrintDln("t_end: " + MyLog.printZisday(t_end,"ddMMMyy_HH_mm_ss_SSS"))
                myPrintDln("l_diff: " + (t_end.getTimeInMillis() - t_start.getTimeInMillis()))
                MLA ! hcloseMsg(headerFileName, postProcessFunc)
                Thread.sleep(100)
    }


    def getJarFileCompilationDate(): String = {
            val jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            "["+jarFile.getName() + "] Version: " + new Date(jarFile.lastModified()).toString();
    }
}

sealed trait MyLogMsg
case object closeMsg extends MyLogMsg
case class hcloseMsg(javascriptHeader: String, postProcessFunc:(List[String], String) => String) extends MyLogMsg
case class logMsg(errorType: String, msg: String) extends MyLogMsg


class MyLogActor extends Actor {
    val L = MyLog.getMylog
            var files = List[MyFile]()
            var hlines = List[String]()
            val logfile = new MyFile(L.logfileName)
    files:+logfile
    val errfile = new MyFile(L.errfileName)
            files:+errfile

            var b_filesActive = true

            def receive =  {
            case hcloseMsg(javascriptHeader, postProcessFunc) =>
            b_filesActive = false
            errfile.writeFile(postProcessFunc(hlines, javascriptHeader))
            files.foreach(_.close)
            context.system.shutdown()
            //exit
            case logMsg(errorType, msg) => errorType match {
            case "H" => 		
            if(b_filesActive) {
                hlines = hlines ++ msg.split("\n").toList
                        logfile.writeFile(msg)
            }
            System.err.print(msg)
            case "E" => 		
            if(b_filesActive) {
                errfile.writeFile(msg)
                logfile.writeFile(msg)
            }
            System.err.print(msg)
            case "L" => 
            if(b_filesActive) {
                logfile.writeFile(msg)
            }	
            System.out.print(msg)
            case _ => throw new Exception("wrong trace type: "+errorType.toString)
            }
            case closeMsg => b_filesActive = false
                    files.foreach(_.close)
                    context.system.shutdown()
                    //exit
    }


}

class MyFile(fname: String) extends File(fname) {
    try {
        new FileOutputStream(this).close
    } catch {
    case unknown : Throwable => throw new Exception("File["+getCanonicalPath+"]: Maybe you did not create the directory...")
    } 

    val fos = new FileOutputStream(this)
    def writeFile(s: String) {
        fos.write(s.getBytes()) 
    }
    def close() {
        fos.close
    }
}

class MyFileChooser(s_title: String) extends Panel {
    final val DEFAULTDIRECTORY = 0
            val f_defvardiff = new File(s_title)

    def justChooseFile(s_extension: String): File = {
        var l_parameters = readSavedParameters()
                val f = chooseFile(l_parameters, s_extension)
                if(l_parameters.isEmpty) {
                    l_parameters = List(f.getParent)
                } else {
                    l_parameters = f.getParent::l_parameters.tail
                }
        writeSavedParameters(l_parameters)
        f
    }
    def chooseFile(l_parameters: List[String], s_extension: String): File = {
        var s_directory = new String()
        if(l_parameters.length==0){
            s_directory = System.getProperty("user.home")
        } else {
            s_directory = l_parameters.apply(DEFAULTDIRECTORY)
        }
        val fChooser = new FileChooser(new File(s_directory))
        // show "details list" of files
        val ab = new javax.swing.JButton()
        getComponents(fChooser.peer).filter(_.isInstanceOf[javax.swing.AbstractButton]).
        find((y: java.awt.Component) => 
        y.asInstanceOf[javax.swing.AbstractButton].getIcon()==UIManager.getIcon("FileChooser.detailsViewIcon"))
        match {
        case Some(button) => button.asInstanceOf[javax.swing.AbstractButton].doClick
        case _ =>
        }
        fChooser.title = s_title;
        fChooser.fileFilter = new GrabFilter(s_extension)
        fChooser.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
        val returnVal = fChooser.showOpenDialog(this)
        if (returnVal == FileChooser.Result.Approve) {
            fChooser.selectedFile
        } else {
            null
        }
    }

    def getComponents(container: java.awt.Container): List[java.awt.Component] = {
        var l_components = List[java.awt.Component]()
                val z = container.getComponents().toList
                l_components = l_components++z
                z.foreach((y: java.awt.Component) => l_components = 
                l_components++getComponents(y.asInstanceOf[java.awt.Container]))	  
                l_components
    }

    def readSavedParameters(): List[String] = {
            var l_parameters = List[String]()
                    if(f_defvardiff.canRead()) {
                        val sc = new Scanner(f_defvardiff)
                        while (sc.hasNextLine()) {
                            l_parameters = l_parameters:+sc.nextLine()
                        }
                    } else {
                        println("Cannot read "+f_defvardiff)
                    }
            l_parameters
    }

    def writeSavedParameters(l_parameters: List[String]) {
        val outputf = new FileOutputStream(f_defvardiff)
        l_parameters.foreach((s: String) => outputf.write((s + "\n").getBytes()))
        outputf.close();
    }
}

class GrabFilter(val s_extension: String) extends FileFilter {
    def accept(f: File) = f.getName.contains(s_extension) || f.isDirectory
            def getDescription(): String ="Just "+s_extension+" files"}


class MyUI(val s_title : String, val parameters: ZeParameters) extends Frame {
        val gpanel = new gridpanel(parameters.size+1,2,parameters)
        if(!parameters.isEmpty) {
                title = s_title
                                contents = gpanel
                                pack
                                visible=true
        }

        def get(): ZeParameters = {
                        while(gpanel.myText=="NotYet"){ MyLog.myPrint(".")}
                        gpanel.getFromPanel
        }

        def getAndClose(): ZeParameters = {
                        val l_output = get
                                        close
                                        l_output
        }
}


class gridpanel(val rows0: Int, val cols0: Int, val parameters: ZeParameters) extends GridPanel(rows0, cols0){
        minimumSize = new java.awt.Dimension(500,500)
        var myText = "NotYet"
        parameters.foreach(add2Panel(_))
        contents += new Button {action = Action("Do the Stuff") {println("Someone clicked button \"" + action.title+"\""); myText = this.text}}
        val errLbl = new Label("")
        errLbl.foreground = Color.red
        contents += errLbl


        def add2Panel(p: (String, MyParameter)) {
                contents += new Label(p._1)
                if(p._2.classtype.isInstanceOf[PasswordField]) {
                        contents += p._2.classtype.asInstanceOf[PasswordField]
                } else if(p._2.classtype.isInstanceOf[TextField]) {
                        p._2.classtype.asInstanceOf[TextField].text = p._2.value
                                        contents += p._2.classtype.asInstanceOf[TextField]
                }
        }

        def getFromPanel(): ZeParameters = {  
                        var p_output = new ZeParameters
                                        parameters.foreach(p_output += getFromField(_))
                                        p_output
        }

        def getFromField(p: (String, MyParameter)): (String, MyParameter) = {
                if(p._2.classtype.isInstanceOf[PasswordField]) {
                        (p._1,new MyParameter(p._2.classtype.asInstanceOf[PasswordField].password.toList.mkString(""),"",new PasswordField))                                    
                } else if(p._2.classtype.isInstanceOf[TextField]) {
                        (p._1,new MyParameter(p._2.classtype.asInstanceOf[TextField].text))
                } else {
                        (p._1,new MyParameter(MyLog.tag(1)+"You most probably have an issue with ["+p+"]"))
                }
        }
}

class getUrlFromClipboard {

    var clipboard: String = _
            var url: URL = _
            //var file: File = _
            var source: scala.io.BufferedSource = _
            try {
                clipboard = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString
                        println("Trying to make a URL of:["+clipboard+"]")
                url = new URL(clipboard)
                source = scala.io.Source.fromURL(url)
            } catch {
            case e:Exception => throw new Exception(e+" ["+clipboard+"]")
            }
}

class ZeParameters(val pairs:List[(String,MyParameter)] = Nil) extends Map[String,MyParameter] {
    /**** Minimal Map stuff begin ****/
    lazy val keyLookup = Map() ++ pairs
            override def get(key: String): Option[MyParameter] = keyLookup.get(key)
            override def iterator: Iterator[(String, MyParameter)] = pairs.reverseIterator
            override def + [B1 >: MyParameter](kv: (String, B1)) = {
                val (key:String, value:MyParameter) = kv
                        new ZeParameters((key,value) :: pairs)
            }
            override def -(key: String): ZeParameters  = new ZeParameters(pairs.filterNot(_._1 == key))
            /**** Minimal map stuff end ****/

            def +(s: String) = {s match {
            case MyParameter.pair(k,v) => new ZeParameters((k, new MyParameter(v)) :: pairs)
            case _ => new ZeParameters(pairs)
            }
            }

            def %(p:(String,String)) = new ZeParameters((p._1, new MyParameter("",p._2)) :: pairs) // with default values

            def getValue(field: String): String = {
                get(field) match {
                case Some(p) => p.value.replaceAll("!",",")
                case _ => null
                }
            }
            def setValue(s: String): Boolean = {
                s match {
                case MyParameter.pair(k,v) => get(k) match {
                case Some(p) => p.value = v; true
                case _ => false
                }
                case _ => false
                }
            }

            def setValue(name: String, v: String): Boolean = {
                get(name) match {
                case Some(p) => p.value = v; true
                case _ => false
                }
            }

            def setOrAddValue(s: String): ZeParameters = {
                s match {
                case MyParameter.pair(k,v) => get(k) match {
                case Some(p) => p.value = v; new ZeParameters(pairs)
                case _ => s match {
                case MyParameter.pair(k,v) => new ZeParameters((k, new MyParameter(v)) :: pairs)
                case _ => new ZeParameters(pairs)
                }
                }
                case _ => new ZeParameters(pairs)
                }
            }

            def saveTo(f: File): ZeParameters = {
                val outputf = new FileOutputStream(f)
                val filteredNoPasswords = new ZeParameters(pairs.filterNot(_._2.classtype.isInstanceOf[PasswordField]))
                outputf.write(filteredNoPasswords.toString.getBytes)
                outputf.close()
                filteredNoPasswords
            }
            override def toString: String = pairs.mkString("\n")
}

object MyParameter {
    val pair = """\((.+),(.*)\)""".r
}

class MyParameter(val v: String, val defaultValue: String, val classtype: Component) {
    def this(v: String) = this(v,"",new TextField)
            def this(v: String, defval: String) = this(v,defval,new TextField)
            def this(v: String, classtype: Component) = this(v,"",classtype)
            var value: String = v
            override def toString: String = {         
                    classtype match {
                    case pwd: PasswordField => "XXXX"
                    case _ => value.replaceAll(",","!")
                    }

            }
            def toString2: String = {
                    classtype match {
                    case pwd: PasswordField => "[XXXX,,pwd]"
                    case _ => "["+value+","+defaultValue+",txt]"
                    }
            }
}


