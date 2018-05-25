package syracuse

class Item(val r: Int) {

	var iteration = List[Item](this)

			def process(func: Function): Item = {
		iteration = iteration :+ func.process(result)
				result
	}

	def process(func: Function, limit: Int): Item = {
		if(limit==0) {
			while(!func.stop(iteration)) {
				process(func)
			}
		} else {
			while((iteration.size < limit)&(!func.stop(iteration))) {
				process(func)
			}
		}
		result
	}

	def getCycle: List[Item] = {
				iteration.dropWhile(_ != result).dropRight(1)
		}

		def reset = iteration = List[Item](this)

				def tempsDeVol = iteration.dropRight(1).filter(_ > new Item(1)).size

				def tempsDeVolEnAltitude = iteration.dropRight(1).filter(_ > this).size-1

				def altitudeMaximale = iteration.map(_.r).max

				def >(other: Item): Boolean = r > other.r

				def >>(other: Item): Boolean = tempsDeVol > other.tempsDeVol

				def result = iteration.last

				def toStringSummary: String = "(["+r.toString+"] TV"+tempsDeVol+", TVA"+tempsDeVolEnAltitude+", AM"+altitudeMaximale+") "+getCycle

				override def toString: String = r.toString

				override def equals(x: Any): Boolean = hashCode.equals(x.asInstanceOf[Item].hashCode)

				override def hashCode: Int = toString.hashCode
}