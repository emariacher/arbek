package kebra

import scala.actors.Actor
import kebra.MyLog._
import scala.util.Random
import java.io.FileWriter
import scala.concurrent.Future._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorDSL._
import scala.concurrent.duration._
import akka.util.Timeout
import language.postfixOps
import scala.concurrent.Future
import akka.pattern.ask
import scala.concurrent.Await
import java.io.File

object ScalaBatshNew {
	var ls = List.empty[String];
	var lse = List.empty[String];
	var i_rc = ScalaBatshNewRC.TIMEOUT;

	def exec(Tlvl: Int, s_cmd: String): (Int,List[String],List[String]) = execRemovePwd(Tlvl, s_cmd, "Cowabunga!")

			def execRemovePwd(Tlvl: Int, s_cmd: String, s_pwd: String): (Int,List[String],List[String]) = {
		myErrPrintln(MyLog.tag(Tlvl)+getClass.getName+".exec(<i>"+s_cmd.replaceAll(s_pwd,"##hidden##")+"</i>)")
		val s_filename = "zz"+new Random().nextInt(1000)+".bat";
		val f = new File(s_filename)
		val fw = new FileWriter(f, false) ; fw.write(s_cmd+"\n") ; fw.close
		ls = List.empty[String];
		lse = List.empty[String];
		i_rc = ScalaBatshNewRC.TIMEOUT;

		//scala.concurrent.Future({
		val p = Runtime.getRuntime.exec(s_filename)
				var input = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream))
		Stream.continually(input.readLine).takeWhile(_ != null).filter(_.length>1).foreach((l: String) => ls = ls ::: List(l.replaceAll(s_pwd,"##hidden##")))
		var error = new java.io.BufferedReader(new java.io.InputStreamReader(p.getErrorStream))
		Stream.continually(error.readLine).takeWhile(_ != null).filter(_.length>1).foreach((l: String) => lse = lse ::: List(l.replaceAll(s_pwd,"##hidden##")))
		i_rc = p.exitValue
		//})
		myErrPrintln("  rc: {"+i_rc+"}")
		fw.close
		f.delete
		(i_rc,ls,lse)
	}
	def printScalaBatshResult(t: (Int,List[String],List[String])) = "(rc: {"+t._1+"},\ni["+t._2.mkString("\n")+"],\ne["+t._2.mkString("\n")+"])"
}

class ScalaBatshActor extends Actor {
	start
	def act() {
		loop {
			react {
			case msg: String => {
				val rsp = ScalaBatshNew.exec(4,msg)
						myPrintDln("["+msg+"] -> ["+rsp+"]")
						reply(rsp)
			}
			case msg: (Int,String) => {
				val rsp = ScalaBatshNew.exec(msg._1,msg._2)
						myPrintDln("["+msg._2+"] -> ["+rsp+"]")
						reply(rsp)
			}
			case msg: (Int,String,String) => {
				val rsp = ScalaBatshNew.execRemovePwd(msg._1,msg._2,msg._3)
						myPrintDln("["+msg._2+"] -> ["+rsp+"]")
						reply(rsp)
			}
			case _ => myErrPrintDln("Not a String!")
			}
		}
	}
}

object ScalaBatshNew2 {
	var vierge = true
			var scalaBatshActor: ScalaBatshActor = _

			if(vierge) {
				scalaBatshActor = new ScalaBatshActor
						vierge = false
			}
}

class ScalaBatshNew3(Tlvl: Int, s_cmd: String, timeOut: Duration = 1 second) {
	def doSomeStuffOnSuccess: PartialFunction[(Int,List[String],List[String]), _] = {
	case x: (Int,List[String],List[String]) => myPrintDln(ScalaBatshNew.printScalaBatshResult(x))
	case _ => myPrintDln("NOGOOD!")
}
def doSomeStuffOnFailure: PartialFunction[Throwable, _] = {
case e: Throwable => myErrPrintDln("NOGOOD! ["+e+"]")
case _ => myErrPrintDln("NOGOOD!")
	}

	val future = Future { ScalaBatshNew.exec(Tlvl,s_cmd) } 
	var result = try {
				Await.result(future, timeOut)
			} catch {
			case e: java.util.concurrent.TimeoutException => (ScalaBatshNewRC.TIMEOUT3,List.empty[String],List.empty[String])
			case _: Throwable => (ScalaBatshNewRC.UNKNOWN3,List.empty[String],List.empty[String])
			}

	/*future onSuccess {
		myPrintDln("Success!")
		doSomeStuffOnSuccess
	}

	future onFailure {
		myErrPrintDln("Failure!")
		doSomeStuffOnFailure
	}*/
}

object ScalaBatshNewRC extends Enumeration {
			val OK = 0 
			val TIMEOUT = 888 
			val TIMEOUT3 = 707
			val UNKNOWN3 = 777
}
