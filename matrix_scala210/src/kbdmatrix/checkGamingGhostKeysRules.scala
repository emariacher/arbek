package kbdmatrix
import scala.collection.immutable.TreeSet
import java.util.concurrent.CountDownLatch
import akka.actor._
import akka.routing.RoundRobinRouter
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import kebra.MyLog._
import kbdmatrix.KbdMatrix._


class checkGamingGhostKeysRules(i_ghost: Int) {  
	val latch = new CountDownLatch(1)
	val system = ActorSystem("Gaming")
	val computeGaming = system.actorOf(Props(new computeGamingRouter(i_ghost, latch)), name = "computeGaming")
	computeGaming ! RequestStartComputeGaming
	latch.await()
}

class param4gamingCompute(val gtype: String, val i_ghost: Int, val i_color: Int, val s_title: String, val s_rule: String, val lts_quartets: TreeSet[Quartet], val 
		l_redList: List[Int], val l_yellowList: List[Int], val l_greenList: List[Int], val l_Tier12redList: List[Int],
		val l_greenList1: List[Int], val l_greenList2: List[Int]) {  
}

sealed trait GamingMsg
case class RequestComputeGaming(p4gc: param4gamingCompute) extends GamingMsg
case class AllDone(duration: Duration) extends GamingMsg
case object RequestStartComputeGaming extends GamingMsg
case object ResponseComputeGamingDone extends GamingMsg


class computeGamingWorker extends Actor {
	def receive = {
	case RequestComputeGaming(p4gc) => checkGaming(p4gc)
			sender ! ResponseComputeGamingDone
	}

	def checkGaming(p4gc: param4gamingCompute) {
		val L = KbdMatrix.getKbdMatrix.L
				p4gc.gtype match {
				case "Gam1ng" => val ts_q = p4gc.lts_quartets.filter(_.l_quartet.intersect(p4gc.l_redList++p4gc.l_yellowList).size>2)
						val ts_2red = ts_q.filter(_.l_quartet.intersect(p4gc.l_redList).size>1)
						val ts_2yellow = ts_q.filter(_.l_quartet.intersect(p4gc.l_yellowList).size>1)
						L.myHErrPrintln("\n<div id=\""+(p4gc.gtype+"_"+p4gc.s_title+p4gc.i_ghost).replaceAll("\\W","_")+"\" style=\"display:none\"><table border=\"1\"><tr><td bgcolor=\""+
								KbdMatrix.bgColor(p4gc.i_ghost+p4gc.i_color)+"\"><h3>Check "+p4gc.s_title+"</h3><h4>["+p4gc.s_rule+"] (numberOfKeysInAQuartetThatMakeAGhostCondition: "+
								p4gc.i_ghost+"):</h4><pre>\n"+
								ts_2red.mkString("\nl_redList{"+tostringList(p4gc.l_redList)+"}&gt;1<pre>\n  ","\n  ","\n</pre>")+
								ts_2yellow.mkString("\nl_yellowList{"+tostringList(p4gc.l_yellowList)+"}&gt;1<pre>\n  ","\n  ","</pre></td></tr></tr></table></div>\n"))
				case "GamingTier1" => val ts_q = p4gc.lts_quartets.filter(_.checkGamingTier1(p4gc.l_greenList,p4gc.l_redList))
				L.myHErrPrintln("\n<div id=\""+(p4gc.gtype+tostringList2(p4gc.l_greenList)+p4gc.i_ghost).replaceAll("\\W","_")+"\" style=\"display:none\"><table border=\"1\"><tr><td bgcolor=\""+
						KbdMatrix.bgColor(p4gc.i_ghost+40)+"\"><h3>Check Tier1 "+tostringList2(p4gc.l_greenList)+"</h3><h4>["+p4gc.s_rule+"] (numberOfKeysInAQuartetThatMakeAGhostCondition: "+
						p4gc.i_ghost+"):</h4><ul><li>l_greenList{"+
						tostringList2(p4gc.l_greenList)+"}</li><li>l_redList{"+tostringList(p4gc.l_redList)+"}</li></ul><pre>\n"+
						ts_q.mkString("  ","\n  ","\n")
						+"</pre></td></tr></table></div>\n")
				case "GamingTier2" => val ts_q = p4gc.lts_quartets.filter(_.checkGamingTier2(p4gc.l_greenList1,p4gc.l_greenList2,p4gc.l_Tier12redList))
				L.myHErrPrintln("\n<div id=\""+(p4gc.gtype+tostringList2(p4gc.l_greenList1++p4gc.l_greenList2)+p4gc.i_ghost).replaceAll("\\W","_")+"\" style=\"display:none\"><table border=\"1\"><tr><td bgcolor=\""+
						KbdMatrix.bgColor(p4gc.i_ghost+50)+"\"><h3>Check Tier2 "+
						tostringList2(p4gc.l_greenList1)+"   "+tostringList2(p4gc.l_greenList2)+"</h3><h4>["+
						p4gc.s_rule+"] (numberOfKeysInAQuartetThatMakeAGhostCondition: "+
						p4gc.i_ghost+"):</h4><ul><li>l_greenList1{"+tostringList(p4gc.l_greenList1)+
						"}</li><li>l_greenList2{"+tostringList(p4gc.l_greenList2)+
						"}</li><li>l_redList{"+tostringList(p4gc.l_Tier12redList)+"}</li></ul><pre>\n"+
						ts_q.mkString("  ","\n  ","\n")
						+"</pre></td></tr></table></div>\n")	      
		}
	}
	def tostringList2(l: List[Int]): String = {
			if(l.isEmpty) {
				"[]"
			} else {
				"["+l.tail.foldLeft(KeyDoc.m_keyref.getOrElse(l.head,l.head).toString)(_ + KeyDoc.m_keyref.getOrElse(_,"Unknown").toString )+"]"
			}
	}

}

class computeGamingRouter(i_ghost: Int, latch: CountDownLatch) extends Actor {
			var nrOfResponses = 0
			var expCount = 65535

			val start: Long = System.currentTimeMillis

			val nrOfWorkers = 3
			val router_gamers = context.actorOf(
					Props[computeGamingWorker].withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouter")

					def receive = {
					case RequestStartComputeGaming => 
					expCount = computeGaming(i_ghost)
					if(nrOfResponses==expCount) {
						latch.countDown()					
					}
					case ResponseComputeGamingDone => 
					nrOfResponses += 1
					L.myPrintDln(tag(1)+"Expected count: "+expCount+ " vs "+nrOfResponses)
					if(nrOfResponses==expCount) {
						latch.countDown()	
						context.system.shutdown()
					}
	}
	override def postStop() {
		L.myPrintDln("All gaming rules checked!")

	}
	def computeGaming(i_ghost: Int): Int = {
			val km = KbdMatrix.getKbdMatrix
					val lts_quartets = km.ts_quartets.filter(_.size >= i_ghost)
					var expCount = 0
					expCount += checkGam1ng(i_ghost, "FPS Game: Call of Duty (1)", 
							"2 keys in (WASD)+ " +
									"3 keys in (124 QERG L-Ctrl L-Shift CB Space) must be allowed", 
									lts_quartets, l_WASD, 
									List(2,3,5,17,19,20,35,44,58,48,50,61));

			expCount += checkGam1ng(i_ghost, "FPS Game: Game in Korea (2)", 
					"2 keys in (WASD)+ " +
							"3 keys in (1234 R L-Shift ZXC must be allowed", 
							lts_quartets, l_WASD, 
							List(2,3,4,5,20,44,46,47,48));
			expCount += checkGam1ng(i_ghost, "FPS Game: Game in Korea (3)", 
					"2 keys in (WASD)+ " +
							"3 keys in (Esc 1234 QERG L-Shift Space) must be allowed", 
							lts_quartets, l_WASD, 
							List(110,2,3,4,5,17,19,20,35,44,61));		
			expCount += checkGam1ng(i_ghost, "FPS Game: Game in Korea (5)", 
					"2 keys in (WASD)+ " +
							"3 keys in (F1-4 E L-Win L-Ctrl L-Shift CV Space Insert End PageUp PageDown) must be allowed",
							lts_quartets, l_WASD, 
							List(112,113,114,115,19,44,58,59,48,49,61,75,81,85,86));
			expCount += checkGam1ng(i_ghost, "FPS Game: Game in Korea (7)", 
					"2 keys in (WASD)+ " +
							"3 keys in (Esc 1234 tab R L-Ctrl L-Shift M Space) must be allowed", 
							lts_quartets, l_WASD, 
							List(110,2,3,4,5,16,20,44,58,52,61));
			expCount += checkGam1ng(i_ghost, "FPS Game: Medal of Honor (10)", 
					"2 keys in (WASD)+ " +
							"3 keys in (EFG L-Ctrl L-Shift ?) must be allowed", 
							lts_quartets, l_WASD, 
							List(19,34,35,44,58,55));
			expCount += checkGam1ng(i_ghost, "FPS Game: Quake4 (12)", 
					"2 keys in (WASD)+ " +
							"3 keys in (L-Ctrl L-Shift Insert) must be allowed", 
							lts_quartets, l_WASD, 
							List(44,58,75));


			expCount += checkGam1ng(i_ghost, "MMORPG Games", 
					"4 keys in (WASD 1234 tab L-Ctrl L-Shift Space)+ " +
							"2 keys in (F1-8 5 QERFZXCV L-Alt Insert Home Delete End PageUp PageDown) must be allowed", 
							lts_quartets, l_WASD++List(2,3,4,5,16,44,58,61),
							List(112,113,114,115,116,117,118,119,17,19,20,34,46,47,48,49,60,75,76,80,81,85,86));
			expCount += checkGam1ng(i_ghost, "Sports And Racing Games", 
					"4 keys in (Arrows AZ L-Alt L-Ctrl L-Shift) + 2 keys in (3QWERSDXCBN Space) must be allowed", 
					lts_quartets, l_arrows++List(31,46,44,58,60), List(4,17,18,19,20,32,33,47,48,50,51));
			expCount += checkGam1ng(i_ghost, "Action Games", 
					"2 Arrows + 2 keys in (IASFGH L-Shift ZXC L-Ctrl) must be allowed", 
					lts_quartets, l_arrows, List(24,31,32,34,35,36,44,46,47,48,58));
			expCount += checkGam1ng(i_ghost, "Online Rhythm Game in Korea", 
					"4 keys in (Arrows KP1-9) + 1 key in (F5-8 Space) must be allowed", 
					lts_quartets, l_arrows++List(91,92,93,96,97,98,101,102,103), List(61,116,117,118,119));
			expCount += checkGam1ng(i_ghost, "DJ max Rhythm Game", 
					"4 keys in (QWERTZU) + 1 key in (IOP Space) must be allowed", 
					lts_quartets, List(17,18,19,20,21,22,23), List(24,25,26,61));
			expCount += checkGam1ng(i_ghost, "Beat mania Rhythm Game", 
					"4 keys in (SDZXC Space) must be allowed", 
					lts_quartets, List(32,33,46,47,48,61), List());

			expCount += checkGamingTier1(i_ghost,  
					"2 green keys + 1 red key AND 1 green key + 2 red keys MUST be allowed", 
					lts_quartets, l_WASD, l_gaming2Left filterNot (l_WASD contains));
			expCount += checkGamingTier1(i_ghost,  
					"2 green keys + 1 red key AND 1 green key + 2 red keys MUST be allowed", 
					lts_quartets, l_ESDF, l_gaming2Left filterNot (l_ESDF contains));
			expCount += checkGamingTier1(i_ghost,  
					"2 green keys + 1 red key AND 1 green key + 2 red keys MUST be allowed", 
					lts_quartets, l_arrows, l_gaming2ArrowsRed);
			expCount += checkGamingTier1(i_ghost,  
					"2 green keys + 1 red key AND 1 green key + 2 red keys MUST be allowed", 
					lts_quartets, l_kpArrows, l_gaming2KpArrowsRed);

			expCount += checkGamingTier2(i_ghost,  
					"2 green from 2 different green areas+ 1 red key should be allowed", 
					lts_quartets, l_WASD, l_ESDF, 
					(l_gaming2Left filterNot (l_WASD contains)) filterNot (l_ESDF contains));	
			expCount += checkGamingTier2(i_ghost,  
					"2 green from 2 different green areas+ 1 red key should be allowed", 
					lts_quartets, l_WASD, l_arrows, 
					(l_gaming2Left++l_gaming2ArrowsRed) filterNot (l_WASD contains));
			expCount += checkGamingTier2(i_ghost,  
					"2 green from 2 different green areas+ 1 red key should be allowed", 
					lts_quartets, l_ESDF, l_arrows, 
					(l_gaming2Left++l_gaming2ArrowsRed) filterNot (l_ESDF contains));
			expCount += checkGamingTier2(i_ghost,  
					"2 green from 2 different green areas+ 1 red key should be allowed", 
					lts_quartets, l_WASD, l_kpArrows, 
					(l_gaming2Left++l_gaming2KpArrowsRed) filterNot (l_WASD contains));
			expCount += checkGamingTier2(i_ghost,  
					"2 green from 2 different green areas+ 1 red key should be allowed", 
					lts_quartets, l_ESDF, l_kpArrows, 
					(l_gaming2Left++l_gaming2KpArrowsRed) filterNot (l_ESDF contains));
			expCount += checkGamingTier2(i_ghost,  
					"2 green from 2 different green areas+ 1 red key should be allowed", 
					lts_quartets, l_arrows, l_kpArrows, 
					(l_gaming2ArrowsRed++l_gaming2KpArrowsRed) filterNot ((l_arrows++l_kpArrows) contains)
					);

			expCount += checkGam1ng(i_ghost, 60, "1 Aion", 
					"2 keys in (WASD) + 2 keys in (1234567890 Esc F1 Tab QERTFG L-Shift CV L-Alt Space) must be allowed", 
					lts_quartets, l_WASD, List(110,112,2,3,4,5,6,7,8,9,10,11,16,17,19,20,21,24,
							34,35,44,48,49,60,61));
			expCount += checkGam1ng(i_ghost, 60, "2 Tera", 
					"2 keys in (WASD) + 2 keys in (1234567890-+ Esc F1-12 CNM L-Alt NumLock) must be allowed", 
					lts_quartets, l_WASD, List(110,112,113,114,115,116,117,118,119,120,121,122,123,
							2,3,4,5,6,7,8,9,10,11,12,13,
							48,51,52,60,90));
			expCount += checkGam1ng(i_ghost, 60, "3 Sudden Attack", 
					"2 keys in (WASD) + 2 keys in (Esc 1234 QERG L-Shift Space) must be allowed", 
					lts_quartets, l_WASD, List(110,2,3,4,5,17,19,20,35,44,61));
			expCount += checkGam1ng(i_ghost, 60, "4 and 6 Warcraft3 and WoW WASD", 
					"2 keys in (WASD) + 2 keys in (1234567890-+ F1-12 Tab QERTY " +
							"FGH L-Shift ZXCVBN L-Ctrl L-Alt Space) must be allowed", 
							lts_quartets, l_WASD, List(112,113,114,115,116,117,118,119,120,121,122,123,
									2,3,4,5,6,7,8,9,10,11,12,13,
									16,17,19,20,20,21,22,34,35,36,
									44,46,47,48,49,50,51,
									58,60,61));
			expCount += checkGam1ng(i_ghost, 60, "4 and 6 Warcraft3 and WoW ESDF", 
					"2 keys in (ESDF) + 2 keys in (1234567890-+ F1-12 Tab QWRTY AGH L-Shift ZXCVBN L-Ctrl L-Alt Space) must be allowed", 
					lts_quartets, l_ESDF, List(112,113,114,115,116,117,118,119,120,121,122,123,
							2,3,4,5,6,7,8,9,10,11,12,13,
							16,17,18,20,20,21,22,31,35,36,
							44,46,47,48,49,50,51,
							58,60,61));
			expCount += checkGam1ng(i_ghost, 60, "5 StarcraftASD", 
					"2 keys in (ASD) + 2 keys in (Tilde 1234567890 TIP GH L-Shift ZCVBNM L-Ctrl) must be allowed", 
					lts_quartets, List(31,32,33), List(1,2,3,4,5,6,7,8,9,10,11,
							21,24,26,35,36,
							44,46,48,49,50,51,52,58));
			expCount += checkGam1ng(i_ghost, 60, "7 FIFA online", 
					"2 keys in (Arrows) + 2 keys in (QWEASDZC) must be allowed", 
					lts_quartets, l_arrows, List(17,18,19,31,32,33,46,48));
			expCount	  
	}
	def checkGam1ng(i_ghost: Int, s_title: String, s_rule: String, lts_quartets: TreeSet[Quartet], 
			l_redList: List[Int], l_yellowList: List[Int]): Int = {
			checkGam1ng(i_ghost, 20, s_title, s_rule, lts_quartets, l_redList, l_yellowList)
	}

	def checkGam1ng(i_ghost: Int, i_color: Int, s_title: String, s_rule: String, lts_quartets: TreeSet[Quartet], 
			l_redList: List[Int], l_yellowList: List[Int]): Int = {
			router_gamers ! RequestComputeGaming(new param4gamingCompute("Gam1ng", i_ghost, i_color, s_title, s_rule, lts_quartets, 
					l_redList, l_yellowList, List.empty[Int], List.empty[Int],
					List.empty[Int], List.empty[Int]))
					1
	}

	def checkGamingTier1(i_ghost: Int, s_rule: String, lts_quartets: TreeSet[Quartet], 
			l_greenList: List[Int], l_redList: List[Int]): Int = {
			router_gamers ! RequestComputeGaming(new param4gamingCompute("GamingTier1", i_ghost, 0, "", s_rule, lts_quartets, 
					List.empty[Int], List.empty[Int], l_greenList, l_redList,
					List.empty[Int], List.empty[Int]))
					1
	}

	def checkGamingTier2(i_ghost: Int, s_rule: String, lts_quartets: TreeSet[Quartet], 
			l_greenList1: List[Int], l_greenList2: List[Int], l_redLists: List[Int]): Int = {
			router_gamers ! RequestComputeGaming(new param4gamingCompute("GamingTier2", i_ghost, 0, "", s_rule, lts_quartets, 
					List.empty[Int], List.empty[Int], List.empty[Int], l_redLists,
					l_greenList1, l_greenList2))
					1
	}
}
