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
import akka.util.Duration


object MyLog {

	val MatchFileLine = """.+\((.+)\..+:(\d+)\)""".r
			val MatchFunc = """(.+)\(.+""".r
			var L: MyLog = _ 

			def getMylog: MyLog = L
			def newMyLog(s_title: String, fil: File,errExt: String): MyLog = {
			L = new MyLog(s_title, fil,errExt)
			L.launchActorAndGui
			L
	}

	def printToday(fmt: String): String = printZisday(Calendar.getInstance(), fmt)

			def printToday: String =printToday("ddMMMyy")

			def printZisday(zisday: Calendar, fmt: String): String = new String(new SimpleDateFormat(fmt).format(zisday.getTime))

	def printZisday(date: Date, fmt: String): String = new String(new SimpleDateFormat(fmt).format(date))

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
				case unknown => unknown.getStackTrace.toList.apply(i_level).toString match {
				case MatchFileLine(file, line) => file+":"+line+" "+s_time
				case _ => s_time
				}
				} finally {
					s_time		
				}
	}

	def func(i_level: Int): String = {
		val s_rien = "functionNotFound"
				try {
					throw new Exception()
				} catch {
				case unknown => unknown.getStackTrace.toList.apply(i_level).toString match {
				case MatchFunc(funcs) => funcs.split('.').toList.last
				case _ => s_rien
				}
				} finally {
					s_rien		
				}
	}
	def waiting (d: Duration){
		val t0 =  System.currentTimeMillis()
				var t1 = t0
				do{
					t1 = System.currentTimeMillis()
				}
				while (t1 - t0 < d.toMillis)
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
					.foreach { (i: Int) => to += List.fromArray(buffer).take(i).mkString("") }
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
				Gui = new MyUI("",List[(String,String,Any)]())  
	}

	def createGui(l_parameters: List[(String,String,Any)]) {
		Gui = new MyUI(s_title, l_parameters)
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

	def timeStamp(c_t_start: Calendar, s_title: String): Calendar = {
			val t_end = Calendar.getInstance();
			myPrintDln("t_now: " + MyLog.printZisday(t_end,"ddMMMyy_HH_mm_ss_SSS [")+ s_title +
					"] t_diff: " + (t_end.getTimeInMillis() - c_t_start.getTimeInMillis()));
			t_end;
	}

	def timeStamp(s_title: String): Calendar = {
			timeStamp(t_start, s_title)
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
	case unknown => throw new Exception("File["+getCanonicalPath+"]: Maybe you did not create the directory...")
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
		find((y: java.awt.Component) => y.asInstanceOf[javax.swing.AbstractButton].getIcon()==UIManager.getIcon("FileChooser.detailsViewIcon"))
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
				z.foreach((y: java.awt.Component) => l_components = l_components++getComponents(y.asInstanceOf[java.awt.Container]))	  
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

class GrabFilter(s_extension: String) extends FileFilter {
	def accept(f: File) = f.getName.contains(s_extension) || f.isDirectory
			def getDescription(): String ="Just "+s_extension+" files"}

class MyUI(s_title : String, l_parameters: List[(String,String,Any)]) extends Frame {
	val gpanel = new gridpanel(l_parameters.size+1,2,l_parameters)
	if(!l_parameters.isEmpty) {
		title = s_title
				contents = gpanel
				pack
				visible=true
	}

	def get: List[(String,String)] = {
				while(gpanel.myText=="NotYet") {
					print("")
				}
				gpanel.getFromPanel
		}

		def getAndClose: List[(String,String)] = {
						val l_output = get
								close
								l_output
				}
}

class gridpanel(rows0: Int, cols0: Int, l_parameters: List[(String,String,Any)]) extends GridPanel(rows0, cols0){
	minimumSize = new java.awt.Dimension(500,500)
	var myText = "NotYet"
	l_parameters.foreach(add2Panel(_))
	contents += new Button {
		action = Action("Do the Stuff") {			 
			myText = this.text
					println("Someone clicked button \"" + action.title+"\" ["+myText+"]")
		}
	}
	val errLbl = new Label("")
	errLbl.foreground = Color.red
	contents += errLbl


	def add2Panel(field: (String,String,Any)) {
		val fname = field._1
				val fvalue = field._2
				val ftype = field._3
				contents += new Label(fname)
		if(ftype.isInstanceOf[PasswordField]) {
			contents += ftype.asInstanceOf[PasswordField]
		} else if(ftype.isInstanceOf[TextField]) {
			ftype.asInstanceOf[TextField].text = fvalue
					contents += ftype.asInstanceOf[TextField]
		}

	}

	def getFromPanel(): List[(String,String)] = {  
			var l_output = List[(String,String)]()
					l_parameters.foreach((field: (String,String,Any)) => l_output = l_output ::: List(getFromField(field)))
					l_output
	}

	def getFromField(field: (String,String,Any)): (String,String) = {
		val fname = field._1
				val fvalue = field._2
				val ftype = field._3
				if(ftype.isInstanceOf[PasswordField]) {
					(fname,ftype.asInstanceOf[PasswordField].password.toList.mkString(""))
				} else if(ftype.isInstanceOf[TextField]) {
					(fname,ftype.asInstanceOf[TextField].text)
				} else {
					("No good","No good")
				}
	}
}
