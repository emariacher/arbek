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

// scala org.scalatest.tools.Runner -o -s testMacros.TestUI

class TestUI extends FunSuite with ShouldMatchers {
	test("actor1") {
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
}

class WorldActor extends Actor {
  var sender1: ActorRef = _
	def receive = {
	case "register" => sender1 = sender
	case "loop" => sender ! "looped!"
	case "getAnswer" => sender1 ! "zeAnswer"
	case s: String ⇒ sender ! s.toUpperCase + " world!"
	}
}
