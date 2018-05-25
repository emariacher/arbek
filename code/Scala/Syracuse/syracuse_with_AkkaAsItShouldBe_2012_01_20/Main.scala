package syracuse
import java.util.concurrent.CountDownLatch
import akka.actor.{Actor, ActorRef, PoisonPill}
import Actor._
import scala.collection.immutable.ListSet


object Main {
	def main(args: Array[String]): Unit = {
			val latch = new CountDownLatch(1)
	val router = actorOf(new Router(latch)).start
	router ! RequestStartRouter
	latch.await()
	}

	def doRegression = {
			val quatorze = new Item(14)
			quatorze.process(new Syracuse(3), 40)
			println(quatorze.iteration+quatorze.getCycle.toString)
			require(quatorze.getCycle==List(4,2,1).map(new Item(_)))

			quatorze.reset
			quatorze.process(new Syracuse(3), 7)
			println(quatorze.iteration+quatorze.getCycle.toString)
			require(quatorze.getCycle.isEmpty)

			val quinze = new Item(15)
			quinze.process(new Syracuse(3), 100)
			println(quinze.iteration+quinze.getCycle.toString)
			println(quinze.toStringSummary)
			require(quinze.tempsDeVol==17)
			require(quinze.tempsDeVolEnAltitude==10)
			require(quinze.altitudeMaximale==160)

			val centvingtsept = new Item(127)
			centvingtsept.process(new Syracuse(3), 100)
			println(centvingtsept.iteration+quinze.getCycle.toString)
			println(centvingtsept.toStringSummary)
			require(centvingtsept.tempsDeVol==46)
			require(centvingtsept.tempsDeVolEnAltitude==23)
			require(centvingtsept.altitudeMaximale==4372)


	}
}

class GetResult(val func: Function, val range: List[Int], val limit: Int) {
	val L = MyLog.L
			val items = range.map((i: Int) => new Item(i))
			items.foreach(_.process(func, limit))
			val cycles = items.foldLeft(ListSet[List[Item]]())(_ + _.getCycle.sortBy{_.r})

			val records = items.filter((it: Item) => {
				val l = items.takeWhile(it >> _)
						val index = items.indexOf(it)
						l.size == index
			})

			var s ="\n***** "+toString+"\n"
			s += items.map(_.toStringSummary).mkString("items:\n  ","\n  ","\n")
			//			s += cycles.mkString("cycles:\n  ","\n  ","\n")
			s += records.map(_.toStringSummary).mkString("records:\n  ","\n  ","\n")

			L.myPrintDln(s)

			override def toString: String = this.getClass.getName+ "("+func+", range, "+limit+") "+cycles

}