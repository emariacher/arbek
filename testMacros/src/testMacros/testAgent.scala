package testMacros


import akka.actor.ActorSystem
import akka.agent.Agent
import scala.concurrent.duration._
import akka.util.Timeout
import kebra.MyLog._


object testAgent extends App {

	implicit val system = ActorSystem("app")
			implicit val timeout = Timeout(5 seconds)

			myPrintln("Hello World!")

			val agent = Agent(5)(system)

			myPrintIt(agent())
			myAssert2(agent(),5)


			// send a value
			agent send 7
			myPrintIt(agent.apply())
	myPrintIt(agent.get)
	agent update 8
	myPrintIt(agent.apply())
	myPrintIt(agent.get)

	myAssert2(agent(),5)
	var result = agent.await
	myPrintIt(result)
	myAssert2(result,8)

	agent send 10
	result = agent.await
	myPrintIt(result)

	// send a function
	agent send (_ + 1)
	myPrintIt(agent())
	result = agent.await
	myPrintIt(result)
	agent send (_ * 2)
	myPrintIt(agent())
	result = agent.await
	myPrintIt(result)

	myAssert2(result,22)
	agent.close
}