package carte

import carte.Tableaux._

class CarteBlanche {
	val lcarres = new ListCarres
	val lregions = new ListRegions
	def addCarre: StateMachine = {
			val c = new Carre
			lcarres.lc = lcarres.lc :+ c
			//			System.out.println(MyLog.tag(1)+" lcarres.l: "+lcarres.lc)
			if(c.isLast) {
				StateMachine.repandRegions
			} else {
				StateMachine.generate
			}
	}

	def repandRegions: StateMachine = {
			lcarres.lc.find(!_.allCantonsKnown) match {
			case Some(c) => c.repandRegions
					StateMachine.repandRegions
			case _ => StateMachine.cleanRegions
			}	    
	}

	def cleanRegions: StateMachine = {
			lcarres.lc.foreach(_.cleanRegions)
			StateMachine.getRegions
	}

	def getRegions: StateMachine = {
				lcarres.lc.foreach(_.getRegions)
//				System.out.println(MyLog.tag(1)+" lregions.l: "+lregions.lr)
				StateMachine.colorie
			}
			override def toString: String = {
					if(lregions.lr.isEmpty) {
						"("+lregions.lr.size+",)"
					} else {
						"("+lregions.lr.size+","+(lregions.lr.tail.foldLeft(lregions.lr.head.voisins.size)(_ + _.voisins.size))+")"
					}
				}
}