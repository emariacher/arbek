package kebra

import java.io.File
import java.net._
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
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

import akka.actor.{Actor, ActorRef, ActorSystem, Inbox, Props}

import scala.util.matching.Regex

class MyFile(fname: String) extends File(fname) {
  try {
    new FileOutputStream(this).close
  } catch {
    case unknown: Throwable => throw new Exception("File[" + getCanonicalPath + "]: Maybe you did not create the directory...")
  }

  val fos = new FileOutputStream(this)

  def writeFile(s: String): Unit = {
    fos.write(s.getBytes())
  }

  def close(): Unit = {
    fos.close()
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
        y.asInstanceOf[javax.swing.AbstractButton].getIcon == UIManager.getIcon("FileChooser.detailsViewIcon")) match {
      case Some(button) => button.asInstanceOf[javax.swing.AbstractButton].doClick()
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
    val z = container.getComponents.toList
    l_components = l_components ++ z
    z.foreach((y: java.awt.Component) => l_components =
      l_components ++ getComponents(y.asInstanceOf[java.awt.Container]))
    l_components
  }

  def readSavedParameters(): List[String] = {
    var l_parameters = List[String]()
    if (f_defvardiff.canRead) {
      val sc = new Scanner(f_defvardiff)
      while (sc.hasNextLine) {
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
  def accept(f: File): Boolean = f.getName.contains(s_extension) || f.isDirectory

  def getDescription: String = "Just " + s_extension + " files"
}


class getUrlFromClipboard {

  var clipboard: String = _
  var url: URL = _
  //var file: File = _
  var source: scala.io.BufferedSource = _
  try {
    clipboard = Toolkit.getDefaultToolkit.getSystemClipboard.getData(DataFlavor.stringFlavor).toString
    println("Trying to make a URL of:[" + clipboard + "]")
    url = new URL(clipboard)
    source = scala.io.Source.fromURL(url)
  } catch {
    case e: Exception => throw new Exception(e.toString + " [" + clipboard + "]")
  }
}


object MyParameter {
  val pair: Regex = """\((.+),(.*)\)""".r
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
