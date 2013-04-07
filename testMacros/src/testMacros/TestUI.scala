package testMacros

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import kebra.MyLog._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Await
import akka.actor.ActorSystem
import akka.actor.{ ActorSystem, Actor, Props }
import akka.pattern.ask
import akka.util.Timeout

// scala org.scalatest.tools.Runner -o -s testMacros.TestUI

class TestUI extends FunSuite with ShouldMatchers {
	test("actor1") {
		val system = ActorSystem()
				val worldActor = system.actorOf(Props[WorldActor])
				implicit val timeout = Timeout(5 seconds)
				val future = worldActor ? "Zorg"
						val result = Await.result(future, timeout.duration).asInstanceOf[String]
								myPrintIt(result)
						result should equal("ZORG world!")
	}
}

case object Start

object ExampleAkka20 extends App {
	val system = ActorSystem()
			system.actorOf(Props[HelloActor]) ! Start
}

object ExampleAkka20_2 extends App {
	val system = ActorSystem()
			val worldActor = system.actorOf(Props[WorldActor])
			implicit val timeout = Timeout(5 seconds)
			val future = worldActor ? "Zorg"
					val result = Await.result(future, timeout.duration).asInstanceOf[String]
							myPrintIt(result)
}

class HelloActor extends Actor {
	val worldActor = context.actorOf(Props[WorldActor])
			def receive = {
			case Start ⇒ worldActor ! "Helloz"
			case s: String ⇒
			println("Received message: %s".format(s))
			context.system.shutdown()
	}
}

class WorldActor extends Actor {
	def receive = {
	case s: String ⇒ sender ! s.toUpperCase + " world!"
	}
}
