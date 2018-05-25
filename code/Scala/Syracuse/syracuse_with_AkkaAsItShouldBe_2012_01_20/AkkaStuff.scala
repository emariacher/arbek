package syracuse
import scala.collection.immutable.TreeSet
import akka.actor.{Actor, ActorRef, PoisonPill}
import Actor._
import akka.routing.{Routing, CyclicIterator}
import Routing._
import akka.actor.UntypedChannel
import java.util.concurrent.CountDownLatch

class Param4Compute(val func: Function, val range: List[Int], val limit: Int)

sealed trait ComputeMsg
case class RequestCompute(p4c: Param4Compute) extends ComputeMsg
case object RequestStartRouter extends ComputeMsg
case class ResponseComputeDone(result: GetResult) extends ComputeMsg

class ComputeWorker extends Actor {
	def receive = {
	case RequestCompute(p4c) => {
		val result = new GetResult(p4c.func, p4c.range, p4c.limit)
		self reply ResponseComputeDone(result)
	}
	}
}

/**** This the actual useful main ****/
class Router(latch: CountDownLatch) extends Actor { 
	MyLog
	val L = MyLog.L

	var nrOfResponses = 0
	var expCount = 65535

	// create 3 workers
	val workers = Vector.fill(3)(actorOf[ComputeWorker].start())
	// wrap them with a load-balancing router
	val router = Routing.loadBalancerActor(CyclicIterator(workers)).start()

	def receive = {
	case RequestStartRouter => 
	expCount = launchCompute
	if(nrOfResponses==expCount) {
		latch.countDown()					
	}
	case ResponseComputeDone(result) => 
	nrOfResponses += 1
	L.myPrintDln("Expected count: "+expCount+ " vs "+nrOfResponses)
	L.results = L.results :+ result
	if(nrOfResponses==expCount) {
		// send a PoisonPill to all workers telling them to shut down themselves
		router ! Broadcast(PoisonPill)

		// send a PoisonPill to the router, telling him to shut himself down
		router ! PoisonPill

		L.myPrintDln("Expected count reached: "+nrOfResponses)
		self.stop()
	}
	}
	override def postStop() {
		L.myPrintDln("Compute done!")
		latch.countDown()
		L.closeFiles()
	}

	def compute(p4c: Param4Compute): Int = {
			router ! RequestCompute(p4c)
			1
	}

	def launchCompute: Int = {
				var expCount = 0

						expCount += compute(new Param4Compute(new Syracuse(3), (1 until 255).toList, 0))

						expCount += compute(new Param4Compute(new Syracuse(5), (1 until 50).toList, 1000))

						expCount += compute(new Param4Compute(new Syracuse(7), (1 until 50).toList, 1000))

						expCount
			}

}
