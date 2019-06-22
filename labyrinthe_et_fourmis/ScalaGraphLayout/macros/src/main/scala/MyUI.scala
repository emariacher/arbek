package kebra

import java.awt.datatransfer.DataFlavor
import java.awt.{Color, Toolkit}
import java.io.{BufferedWriter, File, FileOutputStream, FileWriter}
import java.net._
import java.util.Scanner

import javax.swing.UIManager
import javax.swing.filechooser.FileFilter

import scala.swing._

class MyFile(fname: String, append: Boolean) extends File(fname) {

  def this(fname: String) = this(fname, false)

  var fos: BufferedWriter = null

  try {
    fos = new BufferedWriter(new FileWriter(fname, append))
  } catch {
    case unknown: Throwable => throw new Exception("File[" + getCanonicalPath + "]: Maybe you did not create the directory...")
  }

  def writeFile(s: String): Unit = {
    fos.write(s)
  }

  def close(): Unit = {
    fos.close
  }
}

class MyFileChooser(s_title: String) extends Panel {
  final val DEFAULTDIRECTORY = 0
  val f_defvardiff = new File(s_title)
  var s_directory = new String()

  def justChooseFile(s_extension: String): File = {
    var l_parameters = readSavedParameters()
    val f = chooseFile(l_parameters, s_extension)
    if (l_parameters.isEmpty) {
      l_parameters = List(s_directory)
    } else {
      l_parameters = s_directory :: l_parameters.tail
    }
    writeSavedParameters(l_parameters)
    f
  }

  def justChooseFileOrDirectory(s_extension: String): File = {
    val f = justChooseFile(s_extension)
    if (f != null) {
      f
    } else {
      new File(s_directory)
    }
  }

  def chooseFile(l_parameters: List[String], s_extension: String): File = {
    if (l_parameters.isEmpty) {
      s_directory = System.getProperty("user.home")
    } else {
      s_directory = l_parameters.apply(DEFAULTDIRECTORY)
    }
    val fChooser = new FileChooser(new File(s_directory))
    // show "details list" of files
    val ab = new javax.swing.JButton()
    getComponents(fChooser.peer).filter(_.isInstanceOf[javax.swing.AbstractButton]).
      find((y: java.awt.Component) =>
        y.asInstanceOf[javax.swing.AbstractButton].getIcon() == UIManager.getIcon("FileChooser.detailsViewIcon")) match {
      case Some(button) => button.asInstanceOf[javax.swing.AbstractButton].doClick
      case _ =>
    }
    fChooser.title = s_title;
    fChooser.fileFilter = new GrabFilter(s_extension)
    fChooser.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
    val returnVal = fChooser.showOpenDialog(this)
    if (returnVal == FileChooser.Result.Approve) {
      s_directory = fChooser.selectedFile.getParent
      fChooser.selectedFile
    } else {
      null
    }
  }

  def getComponents(container: java.awt.Container): List[java.awt.Component] = {
    var l_components = List[java.awt.Component]()
    val z = container.getComponents().toList
    l_components = l_components ++ z
    z.foreach((y: java.awt.Component) => l_components =
      l_components ++ getComponents(y.asInstanceOf[java.awt.Container]))
    l_components
  }

  def readSavedParameters(): List[String] = {
    var l_parameters = List[String]()
    if (f_defvardiff.canRead()) {
      val sc = new Scanner(f_defvardiff)
      while (sc.hasNextLine()) {
        l_parameters = l_parameters :+ sc.nextLine()
      }
    } else {
      println("Cannot read " + f_defvardiff)
    }
    l_parameters
  }

  def writeSavedParameters(l_parameters: List[String]): Unit = {
    val outputf = new FileOutputStream(f_defvardiff)
    l_parameters.foreach((s: String) => outputf.write((s + "\n").getBytes()))
    outputf.close();
  }
}

class GrabFilter(val s_extension: String) extends FileFilter {
  def accept(f: File) = f.getName.contains(s_extension) || f.isDirectory

  def getDescription(): String = "Just " + s_extension + " files"
}

class MyUI(val s_title: String, val parameters: ZeParameters) extends Frame {
  val gpanel = new gridpanel(parameters.size + 1, 2, parameters, None)
  if (!parameters.isEmpty) {
    title = s_title
    contents = gpanel
    pack
    visible = true
  }

  def get(): ZeParameters = {
    while (gpanel.myText == "NotYet") {
      MyLog.myPrint("")
    }
    gpanel.getFromPanel
  }

  def getAndClose(): ZeParameters = {
    val l_output = get
    close
    l_output
  }
}

class gridpanel(val rows0: Int, val cols0: Int, val parameters: ZeParameters, val swingAnswerAgent: Any) extends GridPanel(rows0, cols0) {
  minimumSize = new java.awt.Dimension(500, 500)
  var myText = "NotYet"
  parameters.foreach(add2Panel(_))
  contents += new Button {
    action = Action("Do the Stuff") {
      println("Someone clicked button \"" + action.title + "\"")
      myText = this.text
      if (swingAnswerAgent != None) {
        //swingAnswerAgent.asInstanceOf[Agent[String]] send this.text
      }
    }
  }
  val errLbl = new Label("")
  errLbl.foreground = Color.red
  contents += errLbl

  def add2Panel(p: (String, MyParameter)): Unit = {
    contents += new Label(p._1)
    if (p._2.classtype.isInstanceOf[PasswordField]) {
      contents += p._2.classtype.asInstanceOf[PasswordField]
    } else if (p._2.classtype.isInstanceOf[TextField]) {
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
    if (p._2.classtype.isInstanceOf[PasswordField]) {
      (p._1, new MyParameter(p._2.classtype.asInstanceOf[PasswordField].password.toList.mkString(""), "", new PasswordField))
    } else if (p._2.classtype.isInstanceOf[TextField]) {
      (p._1, new MyParameter(p._2.classtype.asInstanceOf[TextField].text))
    } else {
      (p._1, new MyParameter(MyLog.tag(1) + "You most probably have an issue with [" + p + "]"))
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
    println("Trying to make a URL of:[" + clipboard + "]")
    url = new URL(clipboard)
    source = scala.io.Source.fromURL(url)
  } catch {
    case e: Exception => throw new Exception(e + " [" + clipboard + "]")
  }
}

class ZeParameters(val pairs: List[(String, MyParameter)] = Nil) extends Map[String, MyParameter] {
  /** ** Minimal Map stuff begin ****/
  lazy val keyLookup = Map() ++ pairs

  override def get(key: String): Option[MyParameter] = keyLookup.get(key)

  override def iterator: Iterator[(String, MyParameter)] = pairs.reverseIterator

  override def +[B1 >: MyParameter](kv: (String, B1)) = {
    val (key: String, value: MyParameter) = kv
    new ZeParameters((key, value) :: pairs)
  }

  override def -(key: String): ZeParameters = new ZeParameters(pairs.filterNot(_._1 == key))

  /** ** Minimal map stuff end ****/

  def +(s: String) = {
    s match {
      case MyParameter.pair(k, v) => new ZeParameters((k, new MyParameter(v)) :: pairs)
      case _ => new ZeParameters(pairs)
    }
  }

  def %(p: (String, String)) = new ZeParameters((p._1, new MyParameter("", p._2)) :: pairs) // with default values

  def getValue(field: String): String = {
    get(field) match {
      case Some(p) => p.value.replaceAll("!", ",")
      case _ => null
    }
  }

  def setValue(s: String): Boolean = {
    s match {
      case MyParameter.pair(k, v) => get(k) match {
        case Some(p) =>
          p.value = v; true
        case _ => false
      }
      case _ => false
    }
  }

  def setValue(name: String, v: String): Boolean = {
    get(name) match {
      case Some(p) =>
        p.value = v; true
      case _ => false
    }
  }

  def setOrAddValue(s: String): ZeParameters = {
    s match {
      case MyParameter.pair(k, v) => get(k) match {
        case Some(p) =>
          p.value = v; new ZeParameters(pairs)
        case _ => s match {
          case MyParameter.pair(k, v) => new ZeParameters((k, new MyParameter(v)) :: pairs)
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
  def this(v: String) = this(v, "", new TextField)

  def this(v: String, defval: String) = this(v, defval, new TextField)

  def this(v: String, classtype: Component) = this(v, "", classtype)

  var value: String = v

  override def toString: String = {
    classtype match {
      case pwd: PasswordField => "XXXX"
      case _ => value.replaceAll(",", "!")
    }

  }

  def toString2: String = {
    classtype match {
      case pwd: PasswordField => "[XXXX,,pwd]"
      case _ => "[" + value + "," + defaultValue + ",txt]"
    }
  }
}
