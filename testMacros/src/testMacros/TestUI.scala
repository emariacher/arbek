package testMacros

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import kebra.MyActor4UI
import kebra.MyLog._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Await
import akka.actor.ActorSystem
import akka.actor.{ ActorSystem, Actor, Props }

// scala org.scalatest.tools.Runner -o -s testMacros.TestUI

class TestUI extends FunSuite with ShouldMatchers {
	implicit val system = ActorSystem("demo")
			test("actor1") {
		val a = new MyActor4UI

				myPrintIt(a.a ! "loop")
		val b = Future { a.a ! "loop" }
		var result = Await.result(b,4 seconds)
				myPrintIt(result)
				val c = a.a ! "loop"
				myPrintIt(c)
				true should be (false)
	}
}

case object Start

object ExampleAkka20 {
	def main(args: Array[String]): Unit = {
			val system = ActorSystem()
					system.actorOf(Props[HelloActor]) ! Start
	}
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
