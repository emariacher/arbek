package syracuse

import java.io._
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
import scala.collection.immutable.ListSet
import akka.actor.{Actor, PoisonPill}
import Actor._ 

object MyLog {
	val s_file = new File("out\\cadransolaire_input2.csv")
	val L = new MyLog(this.getClass.getName,s_file,"htm")

	val MatchFileLine = """.+\((.+)\..+:(\d+)\)""".r
	val MatchFunc = """(.+)\(.+""".r

	def printToday(fmt: String): String = {
		printZisday(Calendar.getInstance(), fmt)
	}

	def printToday(): String = {
			printToday("ddMMMyy");
	}

	def printZisday(zisday: Calendar, fmt: String): String = {
			printZisday(zisday.getTime(), fmt)
	}

	def printZisday(time: Long): String = {
			MyLog.printZisday(new Date(time),"ddMMM HH:mm")
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
	def waiting (n: Int){
		val t0 =  System.currentTimeMillis()
				var t1 = t0
				do{
					t1 = System.currentTimeMillis()
				}
				while (t1 - t0 < n)
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

	def getColor(hue: Float): Color = {
			val couleurStartHSB = Color.RGBtoHSB(0,0,255,null)
					Color.getHSBColor(couleurStartHSB.apply(0)+hue,couleurStartHSB.apply(1),couleurStartHSB.apply(2))
	}
}

class MyLog(s_title: String, fil: File,errExt: String) {
	var parameters = new ZeParameters
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
	val logfileName = working_directory + File.separatorChar+"log"+File.separatorChar+"logfile_" + 
			MyLog.printToday("ddMMMyy_HH_mm") + ".log"
			val errfileName = working_directory + File.separatorChar+"out_"+name_no_ext.replaceAll("\\W", "_")+"_" + 
					MyLog.printToday("ddMMMyy_HH_mm") + "."+errExt
					val MLA = actorOf(new MyLogActor(this)).start()
					var Gui = new MyUI("",new ZeParameters())
	var b_GuiActive = false
	var results = List[GetResult]()

	def createGui(parameters: ZeParameters) {
		Gui = new MyUI(s_title, parameters)
		b_GuiActive = true
	}

	def myPrintD(s: String) {
		myPrint(MyLog.tag(3)+" "+s)			
	}

	def myPrint(s: String) {
		MLA ! logMsg("L",s)
	}

	def myPrintDln(s: String) {
		myPrintD(s + "\n")
	}

	def myPrintln(s: String) {
		myPrint(s + "\n")
	}

	def myErrPrint(s: String) {
		MLA ! logMsg("E",s)
	}

	def myErrPrintDln(s: String) {
		myErrPrintD(s + "\n")
	}

	def myErrPrintD(s: String) {
		myErrPrint(MyLog.tag(3)+" "+s)	
	}

	def myErrPrintln(s: String) {
		myErrPrint(s + "\n")
	}

	def closeFiles() {
		val t_end = Calendar.getInstance()
				myPrintDln("t_end: " + MyLog.printZisday(t_end,"ddMMMyy_HH_mm_ss_SSS"))
				myErrPrintDln("l_diff: " + (t_end.getTimeInMillis() - t_start.getTimeInMillis()))
				MLA ! closeMsg
				Thread.sleep(100)
	}

	def myHErrPrint(s: String) {
		MLA ! logMsg("H",s)
	}

	def myHErrPrintln(s: String) {
		myHErrPrint(s + "\n")
	}

	def hcloseFiles(headerFileName: String, postProcessFunc: (List[String], String, MyLog) => String) {
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


	def getParametersMenu(params: List[(String,String)]) = {
		var act_parameters = new ZeParameters()
		if(MyLog.s_file.canRead) {
			scala.io.Source.fromFile(MyLog.s_file).getLines.foreach(act_parameters+=_)		  
		}
		myErrPrintDln("parameters from "+MyLog.s_file.getCanonicalPath()+":"+act_parameters)

		myPrintDln("params:"+params)
		var exp_parameters = new ZeParameters()
		params.foreach(exp_parameters%=_)
		myPrintDln("parameters:"+exp_parameters)

		exp_parameters.foreach((p: (String, MyParameter)) => p._2.value = act_parameters.getValue(p._1))
		exp_parameters.filter(_._2.value==null).foreach((p: (String, MyParameter)) => p._2.value = p._2.defaultValue)
		exp_parameters += ("Password", new MyParameter("","",new PasswordField)) 
		myPrintDln("parameters before UI:"+exp_parameters)

		createGui(exp_parameters)
		parameters= Gui.getAndClose

		myErrPrintDln("parameters from/after UI:"+parameters)
		parameters.saveTo(MyLog.s_file)

	}
}

sealed trait MyLogMsg
case object closeMsg extends MyLogMsg
case class hcloseMsg(javascriptHeader: String, postProcessFunc:(List[String], String, MyLog) => String) extends MyLogMsg
case class logMsg(errorType: String, msg: String) extends MyLogMsg


class MyLogActor(L: MyLog) extends Actor {
	var files = List[MyFile]()
			var hlines = List[String]()

			val logfile = new MyFile(L.logfileName)
	files:+logfile
	val errfile = new MyFile(L.errfileName)
			files:+errfile

			var b_filesActive = true

			/*	start
	myPrintln("t_start: " + MyLog.printZisday(t_start,"ddMMMyy_HH_mm_ss_SSS"))*/

			def receive =  {
			case hcloseMsg(javascriptHeader, postProcessFunc) =>
			b_filesActive = false
			errfile.writeFile(postProcessFunc(hlines, javascriptHeader, L))
			files.foreach(_.close)
			self ! PoisonPill
			exit
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
					self ! PoisonPill
					exit
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
	val f_defvardiff = new File(s_title)

	def justChooseFile(s_extension: String): File = {
		var parameters = new ZeParameters()
		if(f_defvardiff.canRead) scala.io.Source.fromFile(f_defvardiff).getLines.foreach(parameters+=_)
		val f = chooseFile(parameters, s_extension)
		parameters = parameters.setOrAddValue("(directory,"+f.getParent+")")
		val outputf = new FileOutputStream(f_defvardiff)
		outputf.write(parameters.toString.getBytes)
		outputf.close()
		f
	}
	def chooseFile(parameters: ZeParameters, s_extension: String): File = {
		var s_directory: String = parameters.getValue("directory")
				s_directory match {
				case null => s_directory = System.getProperty("user.home")
				case _ => 
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

class MyParameter(v: String, val defaultValue: String, val classtype: Component) {
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

class GrabFilter(s_extension: String) extends FileFilter {
	def accept(f: File) = f.getName.contains(s_extension) || f.isDirectory
			def getDescription(): String ="Just "+s_extension+" files"}

class MyUI(s_title : String, parameters: ZeParameters) extends Frame {
	val gpanel = new gridpanel(parameters.size+1,2,parameters)
	if(!parameters.isEmpty) {
		title = s_title
				contents = gpanel
				pack
				visible=true
	}

	def get(): ZeParameters = {
			while(gpanel.myText=="NotYet"){}
			gpanel.getFromPanel
	}

	def getAndClose(): ZeParameters = {
			val l_output = get
					close
					l_output
	}
}

class gridpanel(rows0: Int, cols0: Int, parameters: ZeParameters) extends GridPanel(rows0, cols0){
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

