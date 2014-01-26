package piezo1

import kebra._
import kebra.MyLog._

object Piezo1Main extends App {
    myPrintln("Hello World!")
    val f = (new MyFileChooser("GetArduinoLogs")).justChooseFile("log");
    val s = copyFromFile(f.getCanonicalPath)
    toFileAndDisplay("zob.html", s)
}