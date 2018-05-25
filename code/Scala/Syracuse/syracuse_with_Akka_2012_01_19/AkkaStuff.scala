package syracuse
import scala.collection.immutable.TreeSet
import akka.actor.{Actor, ActorRef, PoisonPill}
import Actor._
import akka.routing.{Routing, CyclicIterator}
import Routing._
import java.util.concurrent.CountDownLatch


class RouterSetup {
	val latch = new CountDownLatch(1)
	val router = actorOf(new Router(latch)).start()
	router ! RequestStartRouter
	latch.await()
}

class Param4Compute(val func: Function, val range: List[Int], val limit: Int)

sealed trait ComputeMsg
case class RequestCompute(p4c: Param4Compute) extends ComputeMsg
case object RequestStartRouter extends ComputeMsg
case object ResponseComputeDone extends ComputeMsg

class ComputeWorker extends Actor {
	def receive = {
	case RequestCompute(p4c) => {
		new GetResult(p4c.func, p4c.range, p4c.limit)
	}
	self reply ResponseComputeDone
	}

	def launchCompute(p4gc: Param4Compute) {
	}

}

class Router(latch: CountDownLatch) extends Actor {
	val L = MyLog.L
			var nrOfResponses = 0
			var expCount = 65535

			// create 3 workers
			val workers = Vector.fill(3)(actorOf[ComputeWorker].start())
			// wrap them with a load-balancing router
			val router = Routing.loadBalancerActor(CyclicIterator(workers)).start()
			// this latch is only plumbing to know when the calculation is completed

			def receive = {
			case RequestStartRouter => 
			expCount = launchCompute
			if(nrOfResponses==expCount) {
				latch.countDown()					
			}
			case ResponseComputeDone => 
			nrOfResponses += 1
			L.myPrintDln("Expected count: "+expCount+ " vs "+nrOfResponses)
			if(nrOfResponses==expCount) {
				latch.countDown()	
				// send a PoisonPill to all workers telling them to shut down themselves
				router ! Broadcast(PoisonPill)

				// send a PoisonPill to the router, telling him to shut himself down
				router ! PoisonPill

				L.myPrintDln("Expected count reached: "+nrOfResponses)
				self.stop()

			}
	}
	override def postStop() {
		L.myPrintDln("All gaming rules checked!")

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
