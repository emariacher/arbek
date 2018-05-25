package syracuse
import scala.collection.immutable.ListSet

object Main {
	def main(args: Array[String]): Unit = {
				MyLog
			val L = MyLog.L

/*			println("Hello World!")	
			doRegression*/

			/*new GetResult(new Syracuse(3), (1 until 255).toList, 0)
			new GetResult(new Syracuse(5), (1 until 50).toList, 1000)
			new GetResult(new Syracuse(7), (1 until 50).toList, 1000)*/
			new RouterSetup

			/*	L.createGui(new ZeParameters(List(("",new MyParameter("")))))
			L.Gui.getAndClose*/
			L.closeFiles()
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
			var s ="\n***** "+this.getClass.getName+ "("+func+", range, "+limit+")\n"
			val items = range.map((i: Int) => new Item(i))
			items.foreach(_.process(func, limit))
			s += items.map(_.toStringSummary).mkString("items:\n  ","\n  ","\n")
			val cycles = items.foldLeft(ListSet[List[Item]]())(_ + _.getCycle.sortBy{_.r})
			s += cycles.mkString("cycles:\n  ","\n  ","\n")

			val records = items.filter((it: Item) => {
				val l = items.takeWhile(it >> _)
						val index = items.indexOf(it)
						l.size == index
			})

			s += records.map(_.toStringSummary).mkString("records:\n  ","\n  ","\n")

			L.myPrintDln(s)

}