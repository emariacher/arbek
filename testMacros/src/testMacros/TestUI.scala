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
	test("actor0") {
		val system = ActorSystem()
				system.actorOf(Props[HelloActor]) ! Start1
				myPrintIt("YO!")
	}

	test("actor1") {
		implicit val timeout = Timeout(1 second)
				val system = ActorSystem()
				val ha = system.actorOf(Props[HelloActor])
				ha ! Start2
				ha ! Start3
				/*var future = ha ? Start3
						var result = Await.result(future, timeout.duration).asInstanceOf[String]
								myPrintIt(result)*/
				waiting(3 seconds)
	}

	test("actor2") {
		val cadre = new Cadre
				cadre.ha ! Start2
				cadre.ha ! Start3
				waiting(3 seconds)
	}
}

case object Start1
case object Start2
case object Start3
case object Start4
case object Start5

class HelloActor extends Actor {
	implicit val timeout = Timeout(1 second)
			val wa = context.actorOf(Props[WorldActor])
			def receive = {
			case Start1 => wa ! "Helloz"
			case Start2 => {
				myPrintIt("H Start2")
				wa ! Start2
			}
			case Start3 => {
				myPrintIt("H Start3")
				var future = wa ? Start3
						var result = Await.result(future, timeout.duration).asInstanceOf[String]
								myPrintIt(result)
			}
			case Start4 => {
				myPrintIt("H Start4")
				self ! Start5
			}
			case Start5 => {
				myPrintIt("H Start5")
				wa ! "zob"
			}
			case s: String =>
			myPrintIt(s)
			/*println("Received message: %s".format(s))
			myPrintIt("Received message: %s".format(s))*/
			context.system.shutdown()
	}
}

class WorldActor extends Actor {
	implicit val timeout = Timeout(2 seconds)
			var sender1: ActorRef = _
			def receive = {
			case Start2 => {
				myPrintIt("W Start2")
				sender1 = sender
				myPrintIt("W Start2:"+sender+"\n"+sender1)
			}
			case Start3 => {
				myPrintIt("W Start3:"+sender+"\n"+sender1)
				sender ! "yo!"
				var future = sender ? Start4
						var result = Await.result(future, timeout.duration).asInstanceOf[String]
								myPrintIt(result)
			}
			case s: String => sender ! s.toUpperCase + " world!"
	}
}

class Cadre {
	implicit val timeout = Timeout(1 second)
			val system = ActorSystem()
			val ha = system.actorOf(Props(new HelloActor))
			val wa = system.actorOf(Props(new WorldActor))

			class HelloActor extends Actor {
		implicit val timeout = Timeout(1 second)
				def receive = {
				case Start1 => wa ! "Helloz"
				case Start2 => {
					myPrintIt("H Start2")
					wa ! Start2
				}
				case Start3 => {
					myPrintIt("H Start3")
					var future = wa ? Start3
							var result = Await.result(future, timeout.duration).asInstanceOf[String]
									myPrintIt(result)
				}
				case Start4 => {
					myPrintIt("H Start4")
					self ! Start5
				}
				case Start5 => {
					myPrintIt("H Start5")
					wa ! "zob"
				}
				case s: String =>
				myPrintIt(s)
				/*println("Received message: %s".format(s))
			myPrintIt("Received message: %s".format(s))*/
				context.system.shutdown()
		}
	}

	class WorldActor extends Actor {
		implicit val timeout = Timeout(2 seconds)
				var sender1: ActorRef = _
				def receive = {
				case Start2 => {
					myPrintIt("W Start2")
					sender1 = sender
					myPrintIt("W Start2:"+sender+"\n"+sender1)
				}
				case Start3 => {
					myPrintIt("W Start3:"+sender+"\n"+sender1)
					sender ! "yo!"
					var future = sender ? Start4
							var result = Await.result(future, timeout.duration).asInstanceOf[String]
									myPrintIt(result)
				}
				case s: String => sender ! s.toUpperCase + " world!"
		}
	}

}

