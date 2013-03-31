package kebra

import scala.util._
//import kebra.ScalaBatshNew4
import kebra.MyLog._
import java.io.File
import scala.util.matching.Regex

object WindowsStuff {

	def getEnvVar(name: String): String = Properties.envOrElse(name, "Rien["+name+"]")

			def getAllEnvVars: Map[String,String] = new ScalaBatshNew4("set").result._2.tail.map((line: String) => {
				val c = line.split("=").toList
						//myAssert2(c.size,2)
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

	def taskList: List[List[String]] = new ScalaBatshNew4("tasklist /V /FO CSV").result._2.tail.map(_.split(",").toList.map(_.tail.reverse.tail.reverse))
			def taskExist(tName: String): Boolean = taskList.exists(_.head==tName)
			def taskKill(tName: String): Int = new ScalaBatshNew4("taskkill /F /IM "+tName+" /T").result._1
			
			def regQuery(cmd: String) = new ScalaBatshNew4("REG QUERY "+cmd)
}