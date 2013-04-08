package testMacros

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import kebra.MyLog._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Await
import akka.actor.ActorSystem
import akka.actor.{ ActorSystem, Actor, ActorRef, Props }
import akka.pattern.ask
import akka.util.Timeout
import akka.actor.ActorDSL._
import akka.actor.ActorSystem

// scala org.scalatest.tools.Runner -o -s testMacros.TestUI

class TestUI extends FunSuite with ShouldMatchers {
	ignore("actor1") {
		val system = ActorSystem()
				val worldActor = system.actorOf(Props[WorldActor])
				implicit val timeout = Timeout(1 second)
				var future = worldActor ? "Zorg"
						var result = Await.result(future, timeout.duration).asInstanceOf[String]
								myPrintIt(result)
						result should equal("ZORG world!")
		future = worldActor ? "loop"
				result = Await.result(future, timeout.duration).asInstanceOf[String]
						myPrintIt(result)
				result should equal("looped!")
		worldActor ! "register"
		future = worldActor ? "getAnswer"
				result = Await.result(future, timeout.duration).asInstanceOf[String]
						myPrintIt(result)
				result should equal("zeAnswer")

	}

	test("actor2") {
		val ovall = new Overall
				implicit val timeout = Timeout(1 second)
				myPrintIt(ovall.result)
	}
}

class Overall {
  	var result = new SomeStuff("rien")

	myPrintDln("Here!")
	implicit val system = ActorSystem()
	implicit val timeout = Timeout(1 second)
	val zuiserver = system.actorOf(Props[ZuiServer])
	val zuiclient = system.actorOf(Props[ZuiClient])
	val a = actor(new Act { whenStarting { 
		myPrintDln("sending zobi la mouche!")
		zuiserver ! new SomeStuff("zobi la mouche!") 
		myPrintDln("sent    zobi la mouche!")
	} })
	myPrintDln("There!")

	class ZuiServer extends Actor {
  	  myPrintDln("HereServer!")
		def receive = {
		case "getParms" => myPrintDln("received getParms!")
		case stuff: SomeStuff => {
			myPrintIt(stuff)
			zuiclient ! stuff
		}
		}
	}

	class ZuiClient extends Actor {
		myPrintDln("sending getParms!")
		var future = zuiserver ? "getParms"
				result = Await.result(future, timeout.duration).asInstanceOf[SomeStuff]
						myPrintIt(result)

				def receive = {
				case s: String => zuiserver ! s
		}
	}
}

class SomeStuff(msg: String) {
	override def toString = "msg["+msg+"]"
}

class WorldActor extends Actor {
	var sender1: ActorRef = _
			def receive = {
			case "register" => sender1 = sender
			case "loop" => sender ! "looped!"
			case "getAnswer" => sender1 ! "zeAnswer"
			case s: String => sender ! s.toUpperCase + " world!"
}
}




