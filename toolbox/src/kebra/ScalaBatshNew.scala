package kebra

import scala.actors.Actor
import kebra.MyLog._
import scala.util.Random
import java.io.FileWriter
import scala.concurrent.Future._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorSystem
import akka.actor.ActorDSL._
import akka.actor.ActorSystem
import scala.concurrent.duration._
import akka.util.Timeout
import language.postfixOps
import scala.concurrent.Future
import akka.pattern.ask
import scala.concurrent.Await

object ScalaBatshNew {
    var ls = List.empty[String];
    var lse = List.empty[String];
    var i_rc = 808;

    def exec(Tlvl: Int, s_cmd: String): (Int,List[String],List[String]) = execRemovePwd(Tlvl, s_cmd, "Cowabunga!")

            def execRemovePwd(Tlvl: Int, s_cmd: String, s_pwd: String): (Int,List[String],List[String]) = {
        myErrPrintln(MyLog.tag(Tlvl)+getClass.getName+".exec(<i>"+s_cmd.replaceAll(s_pwd,"##hidden##")+"</i>)")
        val s_filename = "zz"+new Random().nextInt(1000)+".bat";
        val fw = new FileWriter(s_filename, false) ; fw.write(s_cmd+"\n") ; fw.close
        ls = List.empty[String];
        lse = List.empty[String];
        i_rc = 808;


        scala.concurrent.Future({
        val p = Runtime.getRuntime.exec(s_filename)
                var input = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream))
        Stream.continually(input.readLine).takeWhile(_ != null).filter(_.length>1).foreach((l: String) => ls = ls ::: List(l.replaceAll(s_pwd,"##hidden##")))
        var error = new java.io.BufferedReader(new java.io.InputStreamReader(p.getErrorStream))
        Stream.continually(error.readLine).takeWhile(_ != null).filter(_.length>1).foreach((l: String) => lse = lse ::: List(l.replaceAll(s_pwd,"##hidden##")))
        i_rc = p.exitValue
        })
        myErrPrintln("  rc: {"+i_rc+"}")
        (i_rc,ls,lse)
    }

    def doItPwd(Tlvl: Int, s_cmd: String, s_pwd: String): (Int,List[String],List[String]) = {
        implicit val system = ActorSystem("demo")
                val a = actor(new Act {
                    become {
                    case "hello" ⇒ sender ! "hi"
                    case "zob" ⇒ sender ! "zobi"
                    case "doZeJob"  ⇒ sender ! "zobi"
                    }
                })
                implicit val timeout = Timeout(5 seconds)
                var future = a ? "zob" // enabled by the “ask” import
                        var result = Await.result(future, timeout.duration).asInstanceOf[String]
                                //myPrintIt(result)
                        (i_rc,ls,lse)
    }
}