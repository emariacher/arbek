package kebra

import scala.util._
import kebra.MyLog._
import java.io.File
import scala.util.matching.Regex
import scala.concurrent.duration._
import java.io.FileWriter
import scala.concurrent.Future
import scala.concurrent.Future._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.sys.process._

object WindowsStuff {

	def getEnvVar(name: String): String = Properties.envOrElse(name, "Rien["+name+"]")

			def getAllEnvVars: Map[String,String] = exec("set")._2.tail.map((line: String) => {
				val c = line.split("=").toList
						(c.head,c.last)
			}).toMap

			def getPathAsList = getEnvVar("Path").split(";").toList

			def recursiveListFiles(f: File, r: Regex) = recursiveArrayFiles(f,r).toList
			def recursiveArrayFiles(f: File, r: Regex): Array[File] = {
		val these = f.listFiles
				val good = these.filter(f => r.findFirstIn(f.getName).isDefined)
				good ++ these.filter(_.isDirectory).flatMap(recursiveListFiles(_,r))
	}

	def findExecutableInPath(fileName: String) = getPathAsList.find((dir: String) => new File(dir + File.separator + fileName).canExecute)

			def getDeltaPath(goal: String, start: String) = {
		val lgoal = goal.split("\\\\").toList
				val lstart = start.split("\\\\").toList
				val same = lgoal zip lstart takeWhile((c: (String, String)) => c._1==c._2)
		val deltaPath = (0 until (lstart.length - same.length)).map((i: Int) => "..").toList ++ lgoal.drop(same.length)
		if(deltaPath.isEmpty) {
			List(".").mkString("\\")
		} else {
			deltaPath.mkString("\\")
		}
	}

	def taskList: List[List[String]] = exec(4,"tasklist /V /FO CSV", 2 seconds)._2.tail.map(_.split(",").toList.map(_.tail.reverse.tail.reverse))
			def taskExist(tName: String): Boolean = taskList.filter(!_.isEmpty).exists(_.head==tName)
			def taskKill(tName: String): Int = exec("taskkill /F /IM "+tName+" /T")._1

			def regQuery(cmd: String) = exec("REG QUERY "+cmd)

			def exec(Tlvl: Int, s_cmd: String, timeOut: Duration) = new ScalaBatshNew4(Tlvl, s_cmd, "cowabunga", timeOut).result
			def exec(Tlvl: Int, s_cmd: String, s_pwd: String) = new ScalaBatshNew4(Tlvl, s_cmd, s_pwd, 1 second).result
			def exec(Tlvl: Int, s_cmd: String) = new ScalaBatshNew4(Tlvl, s_cmd, "cowabunga", 1 second).result
			def exec(s_cmd: String) = new ScalaBatshNew4(3, s_cmd, "cowabunga", 1 second).result
}

class ScalaBatshNew4(Tlvl: Int, s_cmd: String, s_pwd: String, timeOut: Duration) {
	def this(Tlvl: Int, s_cmd: String, timeOut: Duration) = this(Tlvl, s_cmd, "cowabunga", timeOut)
			def this(Tlvl: Int, s_cmd: String, s_pwd: String) = this(Tlvl, s_cmd, s_pwd, 1 second)
			def this(Tlvl: Int, s_cmd: String) = this(Tlvl, s_cmd, "cowabunga", 1 second)
			def this(s_cmd: String) = this(3, s_cmd, "cowabunga", 1 second)

			myErrPrintln(MyLog.tag(Tlvl)+getClass.getName+".exec(<i>"+
					s_cmd.replaceAll(s_pwd,"##hidden##")+"</i>)")
					val s_filename = "zz"+new Random().nextInt(1000)+".bat";
	val f = new File(s_filename)
	val fw = new FileWriter(f, false) ; fw.write(s_cmd+"\n") ; fw.close

	val future = Future { run(s_filename) } 
	var result = try {
		Await.result(future, timeOut)
	} catch {
	case e: java.util.concurrent.TimeoutException => (ScalaBatshNewRC.TIMEOUT3,List.empty[String],List.empty[String])
	case _: Throwable => (ScalaBatshNewRC.UNKNOWN3,List.empty[String],List.empty[String])
	}
	fw.close
	f.delete
	myErrPrintln("  rc: {"+result._1+"}")

	def run(in: String): (Int, List[String], List[String]) = {
		val qb = Process(in)
				var out = List[String]()
				var err = List[String]()
				val exit = qb ! ProcessLogger((s) => out ::= s.replaceAll(s_pwd,"##hidden##"), (s) => err ::= s.replaceAll(s_pwd,"##hidden##"))
				(exit, out.reverse, err.reverse)
	}
}

object ScalaBatshNewRC extends Enumeration {
	val OK = 0 
			val UNKNOWN1 = 1
			val TIMEOUT = 888 
			val TIMEOUT3 = 707
			val UNKNOWN3 = 777
}
