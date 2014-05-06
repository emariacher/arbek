package logparsing

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.io.FileOutputStream
import java.io.FileInputStream
import java.io.PrintWriter

object MainLogParsing extends App {
  println("Hello World!")
  val lines = copyFromFile("log3.log").split("\n").toStream.map(s => s.substring(0, s.length - 1))
  //val lines = copyFromFile("essai9.txt").split("\n").toStream.map(s => s.substring(0, s.length - 1))

  LineDeux.getTests2(lines)

  def copyFromFile(fileName: String): String = Some(scala.io.Source.fromFile(fileName)).map(p => { val s = p.mkString; p.close; s }).mkString
}

