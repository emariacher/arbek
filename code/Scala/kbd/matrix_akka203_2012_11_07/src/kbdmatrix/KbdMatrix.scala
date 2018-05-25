package kbdmatrix

import java.util.Calendar
import scala.collection.mutable.HashSet
import scala.collection.immutable.ListSet
import scala.collection.immutable.SortedSet
import scala.collection.immutable.TreeSet
import collection.immutable.ListMap
import java.io.File
import scala.math.Ordering
import akka.actor._
import akka.routing.RoundRobinRouter
import akka.util.Duration
import akka.util.duration._
import akka.actor.{Actor, Props}
import java.util.concurrent.CountDownLatch
import kebra.MyLog._
import kebra._
import akka.dispatch.Await
import akka.util.Timeout
import akka.pattern.ask
import kbdmatrix.KbdMatrix._


object KbdMatrix {
  var km: KbdMatrix = _
  def newKbdMatrix(kmi: KbdMatrix): KbdMatrix = {
    km = kmi
    km
  }
  def getKbdMatrix: KbdMatrix = km
  
	def bgColor(i_ghost: Int): String = {
			Map(3 -> "#FFFFCC", 13 -> "lightyellow",23 -> "#FFFF99",33 -> "#DDDD99", 43 -> "#DDDDBB", 53 -> "#EEEECC", 63 -> "#EECCCC", 
					4 -> "lightblue",14 -> "#CCFFFF", 24 -> "#99CCFF", 34 -> "#CCCCFF", 44 -> "#BBBBDD", 54 -> "#CC99FF").getOrElse(i_ghost,"lightgrey")		
	}
	final val l_modifiersShift = List(44, 57)
			final val l_modifiersCtrl = List(58, 64)
			final val l_modifiersAlt = List(60, 62)
			final val l_modifiersWin = List(59, 63)
			final val l_modifiers = l_modifiersShift ++ l_modifiersCtrl ++ l_modifiersAlt ++ l_modifiersWin
			final val l_arrows = List(79,83,84,89)
			final val l_kpArrows = List(92,96,97,102)
			final val l_WASD = List(18,31,32,33)
			final val l_ESDF = List(19,32,33,34)
			final val l_gaming2Left = List(1,2,3,4,5,6,7,16,17,18,19,20,21,22,30,31,32,33,34,35,36,44,45,46,47,48,49,50,58,60,61)
			final val l_gaming2ArrowsRed = List(15,28,29,42,43,57,151,64,75,76,80,81,85,86,90,91,92,93,95,96,97,98,99)
			final val l_gaming2KpArrowsRed = List(75,76,80,81,85,86,90,91,93,95,98,99,100,101,103,104,105,106,108)++l_arrows
			final val l_notGreenOrRed = List(8,9,10,11,12,13,23,24,25,26,27,37,38,39,40,41,51,52,53,54,55,56,62,63,120,121,122,123,124,125)
			final val l_notGreenOrRedF18keys = List(110,111,112,113,114,115,116,117,118,119)

	def tostring(i_code: Int): String = "  "+i_code + " [" +KbdKey.toColorString2(i_code)+"], "

			def tostringList(l: List[Int]): String = {
		if(l.isEmpty) {
			""
		} else {
			l.tail.foldLeft(tostring(l.head))(_ + tostring(_) )
		}
	}

}

class KbdMatrix {
	val L = getMylog
			var ts_keys = new TreeSet[KbdKey]()(new CompareRowThenCol())
			var ts_quartets = new TreeSet[Quartet]()(new CompareToString());
	var i_maxRow = 0
			var i_maxCol = 0


			def find(i_row: Int, i_col: Int): Option[KbdKey] = ts_keys.find(k => k.i_row==i_row && k.i_col == i_col && k.i_type == KbdKey.i_mainMatrix)
			def find(i_row: Int, i_col: Int, i_type: Int): Option[KbdKey] = ts_keys.find(k => k.i_row==i_row && k.i_col == i_col && k.i_type == i_type)
			def findFn(i_row: Int, i_col: Int): Option[KbdKey] = ts_keys.find(k => k.i_row==i_row && k.i_col == i_col && k.i_type != KbdKey.i_mainMatrix && k.i_code != KbdKey.i_emptyCode)
			def find(i_code: Int): Option[KbdKey] = ts_keys.find(k => k.i_code==i_code)

			def update(i_row: Int, i_col: Int, s_code: String): Int = {
		val i_code = translate(s_code)
				if(i_code!=KbdKey.i_invalidCode) {
					L.myPrint("  r"+i_row+"c"+i_col+" "+s_code+" = "+i_code+" create")
					ts_keys += new KbdKey(i_row, i_col, i_code)
					i_maxRow=Math.max(i_row,i_maxRow)
					i_maxCol=Math.max(i_col,i_maxCol)
					1
				} else {
					L.myPrint("  r"+i_row+"c"+i_col+" "+s_code+" = "+i_code+" dropped")
					0
				}
	}

	def fnupdate(r: scala.xml.NodeSeq) {
		val i_code = translate((r \\ "Cell" \\ "Data")(0).text)
				if(i_code!=KbdKey.i_invalidCode) {			
					val k_inMainMatrix = find(i_code)
							val i_fncode = translate((r \\ "Cell" \\ "Data")(1).text)
							k_inMainMatrix match {
							case Some(k_inMainMatrix) => 
							val k_fn = new KbdKey(k_inMainMatrix.i_row, k_inMainMatrix.i_col, KbdKey.i_fnMatrix, i_fncode)
							L.myPrintln("  fn("+k_inMainMatrix+") = "+k_fn)
							ts_keys += k_fn
							case _ =>
					}
				}
	}


	def printMatrices() {
		val m_matrixGroupByType = ts_keys.groupBy((k: KbdKey) => k.i_type)
				m_matrixGroupByType.keySet.toList.sort((s, t) => s < t).foreach((t:Int) => myPrint(t))
	}

	def myPrint(i_type: Int) {
		L.myHErrPrintln("<div id=\"NonGamingMatrice"+i_type+"\" style=\"display:none\"><table border=\"1\"><tr><th colspan=\""+i_maxCol+"\">type: "+KbdKey.typeToString(i_type)+"</th></tr><tr><th/>")
		val lcol = (0 to i_maxCol)
		L.myHErrPrintln(lcol.tail.foldLeft("<th>__Col" + lcol.head +"__")(_ + "</th><th>__Col" + _ +"__") + "</th></tr>")
		val ts_matrixFilteredByType = ts_keys.filter((k: KbdKey) => k.i_type==i_type)
		val m_matrixGroupByRow = ts_matrixFilteredByType.groupBy((k: KbdKey) => k.i_row)
		val m_matrixGroupBySortedRow = ListMap(m_matrixGroupByRow.toList.sortBy{_._1}:_*)
		m_matrixGroupBySortedRow.foreach((p:(Int, TreeSet[KbdKey])) => L.myHErrPrintln(p._2.tail.foldLeft("<tr><th>Row "+p._1+"</th>" + p._2.head.tohtmlstring)(_ + _.tohtmlstring) + "</tr>"))
		L.myHErrPrintln("</table></div>")
	}

	def CGSprintMatrices() {
		val m_matrixGroupByType = ts_keys.groupBy((k: KbdKey) => k.i_type)
				val cgsfile = new MyFile(L.working_directory + File.separatorChar+L.name_no_ext.replaceAll("\\W", "_") + "_"+
						MyLog.printToday("ddMMMyy_HH_mm") + ".cgs")
		L.files:+cgsfile
		cgsfile.writeFile("# Generated from ["+L.name+"] "+MyLog.printToday("ddMMMyy_HH_mm")+"\n")
		m_matrixGroupByType.keySet.toList.sort((s, t) => s < t).foreach((t:Int) => myCGSPrint(t, cgsfile))
	}

	def myCGSPrint(i_type: Int, cgsfile: MyFile) {
		cgsfile.writeFile("#type: "+KbdKey.typeToString(i_type)+"\n")
		val ts_matrixFilteredByType = ts_keys.filter((k: KbdKey) => k.i_type==i_type)
		val m_matrixGroupByRow = ts_matrixFilteredByType.groupBy((k: KbdKey) => k.i_row)
		val m_matrixGroupBySortedRow = ListMap(m_matrixGroupByRow.toList.sortBy{_._1}:_*)
		m_matrixGroupBySortedRow.foreach((p:(Int, TreeSet[KbdKey])) => cgsfile.writeFile(p._2.tail.foldLeft("# --------------- Row "+p._1+"------------------\n" + 
				p._2.head.tocgsstring)(_ + _.tocgsstring) + "\n"))
	}

	def complete(i_type: Int) {
		(0 to i_maxRow).foreach((r: Int) => (0 to i_maxCol).foreach((c: Int) => find(r, c, i_type) match {
		case Some(k) => 
		case None => ts_keys += new KbdKey(r, c, i_type, KbdKey.i_emptyCode)
		}))
		ts_keys.groupBy((k: KbdKey) => k.i_row).foreach((p:(Int, TreeSet[KbdKey])) => removeEmptyRow(p._2))
		ts_keys.groupBy((k: KbdKey) => k.i_col).foreach((p:(Int, TreeSet[KbdKey])) => removeEmptyColumn(p._2))
	}

	def removeEmptyRow(ts: TreeSet[KbdKey]) {
		if(ts.filter(_.isValid()).size==0) {
			ts_keys --= ts
					i_maxRow -= 1
		}
	}

	def removeEmptyColumn(ts: TreeSet[KbdKey]) {
		if(ts.filter(_.isValid()).size==0) {
			ts_keys --= ts
					i_maxCol -= 1
		}
	}

	def generateQuartets2() {
		val c_start = L.timeStamp(MyLog.func(1));
		var l_quartetsfn = ListSet.empty[Quartet];
		val k_fnleft = find(151)
				k_fnleft match {
				case Some(k_fn) => ts_keys.filter(_.fnWHQL()).foreach(l_quartetsfn += new Quartet(_, k_fn))
						ts_keys.filter(_.fnWHQL()).foreach(l_quartetsfn += new Quartet(KbdKey.i_fnMatrix, _, k_fn))
				case _ =>			
		}
		val k_fnright = find(152)
				k_fnright match {
				case Some(k_fn) => ts_keys.filter(_.fnWHQL()).foreach(l_quartetsfn += new Quartet(_, k_fn))
						ts_keys.filter(_.fnWHQL()).foreach(l_quartetsfn += new Quartet(KbdKey.i_fnMatrix, _, k_fn))
				case _ =>			
		}
		val ts_keysMain = ts_keys.filter(_.i_type==KbdKey.i_mainMatrix)
				val m_matrixGroupByRow = ts_keysMain.groupBy((k: KbdKey) => k.i_row)
				val m_matrixGroupBySortedRow = ListMap(m_matrixGroupByRow.toList.sortBy{_._1}:_*)
				val expCount = m_matrixGroupBySortedRow.size
				L.myPrintDln("Expected count: "+expCount)
				val latch = new CountDownLatch(1)
		//val l_quartets = new SupervisorActor(expCount, latch).withArgsChildActorRef
		// Create an Akka system
    val system = ActorSystem("Quartets")
		val l_quartets = system.actorOf(Props(new quartetsList(expCount, latch)))
		L.myPrintDln("Here1! ")

		m_matrixGroupBySortedRow.foreach((p:(Int, TreeSet[KbdKey])) => generateQuartetsByRow(p, ts_keysMain, l_quartets))
		latch.await()

		implicit val timeout = Timeout(500 second)
		val future = l_quartets ? RequestResultQuartets // implicit timeout is required for the ?/ask (producer)
				val lql = Await.result(future, timeout.duration).asInstanceOf[ListSet[Quartet]]
						ts_quartets ++= lql ++ l_quartetsfn.filter(_.size>2)

						L.myPrintDln(ts_quartets.mkString("\nts_quartets size="+ts_quartets.size+":\n  *","\n  *","\n"))
						L.timeStamp(c_start,MyLog.func(1))
	}

	def generateQuartetsByRow(p:(Int, TreeSet[KbdKey]), ts_keysMain: TreeSet[KbdKey], router_gqbrs: ActorRef) {
		//		p._2.foreach(router_gqbrs ! RequestComputeQuartets(_, p, ts_keysMain))
		router_gqbrs ! RequestComputeQuartets(p, ts_keysMain)
	}



	def translate(s_code: String): Int = {
			try {
				if(s_code.length()>0) {
					Integer.parseInt(s_code)
				} else {
					KbdKey.i_emptyCode
				}
			} catch {
			case _ => KbdKey.i_invalidCode
			} finally {
				KbdKey.i_invalidCode
			}
	}
	def checkGhostKeysRules(i_ghost: Int) {
		val c_start = L.timeStamp(MyLog.func(1));
		L.myHErrPrintln("\n<div id=\"NonGamingGHost"+i_ghost+"\" style=\"display:none\"><table border=\"1\"><tr><th bgcolor=\""+
				KbdMatrix.bgColor(i_ghost)
				+"\">numberOfKeysInAQuartetThatMakeAGhostCondition: "+
				i_ghost+"</th></tr><tr><td bgcolor=\""+
				KbdMatrix.bgColor(i_ghost)+"\">");
		val lts_quartets = ts_quartets.filter(_.size >= i_ghost)

				// Check WHQL
				checkWHQL(i_ghost)
				// Check Braille
				val l_braille = List(32,33,34,37,38,39,61)
				L.myHErrPrintln("<tr><td bgcolor=\""+
						KbdMatrix.bgColor(i_ghost)+"\"><h3>Check Braille</h3><pre>(numberOfKeysInAQuartetThatMakeAGhostCondition:"+
						i_ghost+") : No ghost keys with {"+tostringList(l_braille)+"}:\n"+
						lts_quartets.filter(_.l_quartet.intersect(l_braille).size>=i_ghost).mkString("  ","\n  ",""));
		L.timeStamp(c_start,"braille")

		val c_start2 = L.timeStamp("CheckEnglishLanguage");
		new CheckEnglishLanguageMostCurrentCombinations2(i_ghost, lts_quartets)
		L.myHErrPrintln("\n</td></tr></table></div>")
		L.timeStamp(c_start2,"CheckEnglishLanguage")
		L.timeStamp(c_start,MyLog.func(1))
	}
	def checkWHQL(i_ghost: Int) {
		val c_start = L.timeStamp("checkWHQL");
		L.myHErrPrintln("\n</pre><tr><td bgcolor=\""+KbdMatrix.bgColor(i_ghost+10)+"\">" +
				"<h3>checkWHQL(numberOfKeysInAQuartetThatMakeAGhostCondition:"+
				i_ghost+", MUST!)</h3><pre>\n"+
				ts_quartets.filterNot(_.passWHQL(i_ghost, 
						KbdKey.mustPassWHQL)).mkString("  ","\n  ",""));
		L.myHErrPrintln("</pre><h3>checkWHQL(numberOfKeysInAQuartetThatMakeAGhostCondition:"+
				i_ghost+", Logitech guidelines)</h3><pre>\n"+
				ts_quartets.filterNot(_.passWHQL(i_ghost, 
						KbdKey.shouldPassWHQL)).mkString("  ","\n  ",""));
		L.timeStamp(c_start,"checkWHQL")
		L.myHErrPrintln("\n</pre></td></tr>");
	}


}



class CompareToString extends Ordering[Quartet] {
	def compare(q1: Quartet, q2: Quartet) = q1.toString.compare(q2.toString)
}



sealed trait GqbrMsg
case class RequestComputeQuartets(p:(Int, TreeSet[KbdKey]), ts_keysMain: TreeSet[KbdKey]) extends GqbrMsg
case class ResponseComputeQuartets(lq: ListSet[Quartet]) extends GqbrMsg
case class AllDoneQ(duration: Duration) extends GqbrMsg

case object RequestResultQuartets extends GqbrMsg

class generateQuartetsByRow extends Actor {
	def receive = {
	case RequestComputeQuartets(p, ts_keyMain) => 
	//	self reply ResponseComputeQuartets(p._2.foreach((k: KbdKey) => k.generateQuartets(p._2,ts_keyMain.filter(_.i_row>p._1)))
	sender ! ResponseComputeQuartets(p._2.tail.foldLeft(p._2.head.generateQuartets(p._2,ts_keyMain.filter(_.i_row>p._1)))(_ ++ _.generateQuartets(p._2,ts_keyMain.filter(_.i_row>p._1))))
	}
}

//ts_keys.tail.foldLeft(ts_keys.head.generateQuartets)(_ ++ _.generateQuartets)

class quartetsList(expCount : Int, latch: CountDownLatch) extends Actor {
	val L = getMylog
			var lql = ListSet.empty[Quartet]
					var nrOfResponses = 0

					/* create 3 workers
			val gqbrs = Vector.fill(3)(actorOf[generateQuartetsByRow].start())
			// wrap them with a load-balancing router
			val router_gqbrs = Routing.loadBalancerActor(CyclicIterator(gqbrs)).start()
			// this latch is only plumbing to know when the calculation is completed*/
					val start: Long = System.currentTimeMillis

					val nrOfWorkers = 3
					val router_gqbrs = context.actorOf(
							Props[generateQuartetsByRow].withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouterQ")


							def receive = {
							case RequestComputeQuartets(p, ts_keyMain) => 
							router_gqbrs ! RequestComputeQuartets(p, ts_keyMain)
							case ResponseComputeQuartets(lq) => lql ++= lq
							nrOfResponses += 1
							L.myPrintDln(MyLog.tag(1)+"Expected count: "+expCount+ " vs "+nrOfResponses)
							if(nrOfResponses==expCount) {
								latch.countDown()					
							}
							case RequestResultQuartets => if(nrOfResponses==expCount) {
								// Send the result to the listener
							    sender ! lql
								// Stops this actor and all its supervised children
								context.stop(self)

								L.myPrintDln("Expected count reached: "+nrOfResponses)
								//				self.stop()
								L.myPrintDln("\nQ Calculation time: %s"
										.format((System.currentTimeMillis - start).millis))
										context.system.shutdown()

							} else {
								L.myPrintDln("Expected count: "+expCount+ " vs "+nrOfResponses)
								sender ! "Expected count: "+expCount+ " vs "+nrOfResponses
							}

	}
	override def postStop() {
		L.myPrintDln("quartetsList built!")

	}
}

class ListenerQuartets extends Actor {
	def receive = {
	case AllDoneQ(duration) => 
	println("\nQ Calculation time: %s"
			.format(duration))
			context.system.shutdown()
	}
}
