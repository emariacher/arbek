package testMacros


import akka.actor.ActorSystem
import akka.agent.Agent
import scala.concurrent.duration._
import akka.util.Timeout
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import akka.agent.Agent
import kebra.MyLog._


object testAgent extends App {

	implicit val system = ActorSystem("app")
			//implicit val timeout = Timeout(5 seconds)

			myPrintln("Hello World!")

			val agent = Agent(5)

			myPrintIt(agent())
			myAssert2(agent(),5)


			// send a value
			agent send 7
			myPrintIt(agent.apply())
	myPrintIt(agent.get)
	//agent update 8
	agent alter 8
	myPrintIt(agent.apply())
	myPrintIt(agent.get)

	myAssert2(agent(),5)
	var result = agent.get
	myPrintIt(result)
	myAssert2(result,8)

	agent send 10
	result = agent.get
	myPrintIt(result)

	// send a function
	agent send (_ + 1)
	myPrintIt(agent())
	result = agent.get
	myPrintIt(result)
	agent send (_ * 2)
	myPrintIt(agent())
	result = agent.get
	myPrintIt(result)

	myAssert2(result,22)
	//agent.close
}