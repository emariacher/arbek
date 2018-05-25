package kbdmatrix

import java.util.Calendar

import scala.collection.mutable.HashSet
import scala.collection.immutable.ListSet
import scala.collection.immutable.SortedSet
import scala.collection.immutable.TreeSet
import collection.immutable.ListMap

object KbdMatrix {
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
}

class KbdMatrix(val L: MyLog) {
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
			ts_keys += new KbdKey(this, i_row, i_col, i_code)
			i_maxRow=Math.max(i_row,i_maxRow)
			i_maxCol=Math.max(i_col,i_maxCol)
			1
		} else {
			L.myErrPrint("  r"+i_row+"c"+i_col+" "+s_code+" = "+i_code+" dropped")
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
			val k_fn = new KbdKey(this, k_inMainMatrix.i_row, k_inMainMatrix.i_col, KbdKey.i_fnMatrix, i_fncode)
			L.myPrintln("  fn("+k_inMainMatrix+") = "+k_fn)
			ts_keys += k_fn
			case _ =>
			}
		}
	}

	def tostring(i_code: Int): String = "  "+i_code + " [" +KbdKey.toColorString2(i_code)+"], "

	def tostringList(l: List[Int]): String = {
		if(l.isEmpty) {
			""
		} else {
			l.tail.foldLeft(tostring(l.head))(_ + tostring(_) )
		}
	}

	def tostringList2(l: List[Int]): String = {
		if(l.isEmpty) {
			"[]"
		} else {
			"["+l.tail.foldLeft(KeyDoc.m_keyref.getOrElse(l.head,l.head).toString)(_ + KeyDoc.m_keyref.getOrElse(_,"Unknown").toString )+"]"
		}
	}

	def printMatrices() {
		val m_matrixGroupByType = ts_keys.groupBy((k: KbdKey) => k.i_type)
		m_matrixGroupByType.keySet.toList.sort((s, t) => s < t).foreach((t:Int) => myPrint(t))
	}

	def myPrint(i_type: Int) {
		L.myErrPrintln("<table border=\"1\"><tr><th colspan=\""+i_maxCol+"\">type: "+KbdKey.typeToString(i_type)+"</th></tr><tr><th/>")
		val lcol = (0 to i_maxCol)
		L.myErrPrintln(lcol.tail.foldLeft("<th>__Col" + lcol.head +"__")(_ + "</th><th>__Col" + _ +"__") + "</th></tr>")
		val ts_matrixFilteredByType = ts_keys.filter((k: KbdKey) => k.i_type==i_type)
		val m_matrixGroupByRow = ts_matrixFilteredByType.groupBy((k: KbdKey) => k.i_row)
		val m_matrixGroupBySortedRow = ListMap(m_matrixGroupByRow.toList.sortBy{_._1}:_*)
		m_matrixGroupBySortedRow.foreach((p:(Int, TreeSet[KbdKey])) => L.myErrPrintln(p._2.tail.foldLeft("<tr><th>Row "+p._1+"</th>" + p._2.head.tohtmlstring)(_ + _.tohtmlstring) + "</tr>"))
		L.myErrPrintln("</table>")
	}

	def complete(i_type: Int) {
		(0 to i_maxRow).foreach((r: Int) => (0 to i_maxCol).foreach((c: Int) => find(r, c, i_type) match {
		case Some(k) => 
		case None => ts_keys += new KbdKey(this, r, c, i_type, KbdKey.i_emptyCode)
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

	def generateQuartets() {
		val c_start = L.timeStamp("generateQuartets");
		var l_quartetsfn = ListSet.empty[Quartet];
		val k_fnleft = find(151)
		k_fnleft match {
		case Some(k_fn) => ts_keys.filter(_.fnWHQL()).foreach(l_quartetsfn += new Quartet(this, _, k_fn))
		ts_keys.filter(_.fnWHQL()).foreach(l_quartetsfn += new Quartet(this, KbdKey.i_fnMatrix, _, k_fn))
		case _ =>			
		}
		val k_fnright = find(152)
		k_fnright match {
		case Some(k_fn) => ts_keys.filter(_.fnWHQL()).foreach(l_quartetsfn += new Quartet(this, _, k_fn))
		ts_keys.filter(_.fnWHQL()).foreach(l_quartetsfn += new Quartet(this, KbdKey.i_fnMatrix, _, k_fn))
		case _ =>			
		}
		val ts_keysMain = ts_keys.filter(_.i_type==KbdKey.i_mainMatrix)
		val m_matrixGroupByRow = ts_keysMain.groupBy((k: KbdKey) => k.i_row)
		val m_matrixGroupBySortedRow = ListMap(m_matrixGroupByRow.toList.sortBy{_._1}:_*)
		var l_quartets = ListSet.empty[Quartet]
		m_matrixGroupBySortedRow.foreach((p:(Int, TreeSet[KbdKey])) => l_quartets ++= generateQuartetsByRow(p, ts_keysMain))
		ts_quartets ++= l_quartets ++ l_quartetsfn.filter(_.size>2)
		L.myPrintln(ts_quartets.mkString("\nts_quartets size="+ts_quartets.size+":\n  *","\n  *","\n"))
		L.timeStamp(c_start,"generateQuartetsNew")
	}
	
	def generateQuartetsByRow(p:(Int, TreeSet[KbdKey]), ts_keysMain: TreeSet[KbdKey]): ListSet[Quartet] = {
		var l_quartets = ListSet.empty[Quartet];
		p._2.foreach(l_quartets ++= _.generateQuartets(p._2,ts_keysMain.filter(_.i_row>p._1)))
		l_quartets
	}

	def generateQuartetsOld() {
		val c_start = L.timeStamp("generateQuartetsOld");
		var l_quartetsfn = ListSet.empty[Quartet];
		val k_fnleft = find(151)
		k_fnleft match {
		case Some(k_fn) => ts_keys.filter(_.fnWHQL()).foreach(l_quartetsfn += new Quartet(this, _, k_fn))
		ts_keys.filter(_.fnWHQL()).foreach(l_quartetsfn += new Quartet(this, KbdKey.i_fnMatrix, _, k_fn))
		case _ =>			
		}
		val k_fnright = find(152)
		k_fnright match {
		case Some(k_fn) => ts_keys.filter(_.fnWHQL()).foreach(l_quartetsfn += new Quartet(this, _, k_fn))
		ts_keys.filter(_.fnWHQL()).foreach(l_quartetsfn += new Quartet(this, KbdKey.i_fnMatrix, _, k_fn))
		case _ =>			
		}
		val ts_keysMain = ts_keys.filter(_.i_type==KbdKey.i_mainMatrix)
		val l_quartets = ts_keysMain.tail.foldLeft(ts_keysMain.head.generateQuartetsOld)(_ ++ _.generateQuartetsOld) ++ l_quartetsfn.filter(_.size>2)
		ts_quartets ++= l_quartets
		L.myPrintln(ts_quartets.mkString("\nts_quartets size="+ts_quartets.size+":\n  *","\n  *","\n"))
		L.timeStamp(c_start,"generateQuartetsOld")
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
		val c_start = L.timeStamp("checkGhostKeysRules");
		L.myErrPrintln("\n</pre><table border=\"1\"><tr><th bgcolor=\""+
				KbdMatrix.bgColor(i_ghost)
				+"\">numberOfKeysInAQuartetThatMakeAGhostCondition: "+
				i_ghost+"</th></tr><tr><td bgcolor=\""+
				KbdMatrix.bgColor(i_ghost)+"\">");
		val lts_quartets = ts_quartets.filter(_.size >= i_ghost)

		// Check WHQL
		checkWHQL(i_ghost)
		// Check Braille
		val l_braille = List(32,33,34,37,38,39,61)
		L.myErrPrintln("<tr><td bgcolor=\""+
				KbdMatrix.bgColor(i_ghost)+"\"><h3>Check Braille</h3><pre>(numberOfKeysInAQuartetThatMakeAGhostCondition:"+
				i_ghost+") : No ghost keys with {"+tostringList(l_braille)+"}:\n"+
				lts_quartets.filter(_.l_quartet.intersect(l_braille).size>=i_ghost).mkString("  ","\n  ",""));
		L.timeStamp(c_start,"braille")
		
		val c_start2 = L.timeStamp("111");
		new CheckEnglishLanguageMostCurrentCombinations2(this, i_ghost, lts_quartets)
		L.myErrPrintln("\n</td></tr></table>")
		L.timeStamp(c_start2,"CheckEnglishLanguage")
		L.timeStamp(c_start,"checkGhostKeysRules")
	}
	def checkGamingGhostKeysRules(i_ghost: Int) {
		val c_start = L.timeStamp("checkGamingGhostKeysRules");
		L.myErrPrintln("\n</pre><table border=\"1\"><tr><th bgcolor=\""+
				KbdMatrix.bgColor(i_ghost)
				+"\">numberOfKeysInAQuartetThatMakeAGhostCondition: "+
				i_ghost+"</th></tr><tr><td bgcolor=\""+
				KbdMatrix.bgColor(i_ghost)+"\">");
		val lts_quartets = ts_quartets.filter(_.size >= i_ghost)

		L.myErrPrintln("</tr><tr><td><h4>DISCLAIMER: For Gaming checks, this Scala program is more " +
		"strict than documented rules: == 2 keys is checked as > 1 key (i.e. 2, 3 or 4 keys).</h4></td></tr>")
		checkGaming(i_ghost, "FPS Game: Call of Duty (1)", 
				"2 keys in (WASD)+ " +
				"3 keys in (124 QERG L-Ctrl L-Shift CB Space) must be allowed", 
				lts_quartets, KbdMatrix.l_WASD, 
				List(2,3,5,17,19,20,35,44,58,48,50,61));
		checkGaming(i_ghost, "FPS Game: Game in Korea (2)", 
				"2 keys in (WASD)+ " +
				"3 keys in (1234 R L-Shift ZXC must be allowed", 
				lts_quartets, KbdMatrix.l_WASD, 
				List(2,3,4,5,20,44,46,47,48));
		checkGaming(i_ghost, "FPS Game: Game in Korea (3)", 
				"2 keys in (WASD)+ " +
				"3 keys in (Esc 1234 QERG L-Shift Space) must be allowed", 
				lts_quartets, KbdMatrix.l_WASD, 
				List(110,2,3,4,5,17,19,20,35,44,61));		
		checkGaming(i_ghost, "FPS Game: Game in Korea (5)", 
				"2 keys in (WASD)+ " +
				"3 keys in (F1-4 E L-Win L-Ctrl L-Shift CV Space Insert End PageUp PageDown) must be allowed",
				lts_quartets, KbdMatrix.l_WASD, 
				List(112,113,114,115,19,44,58,59,48,49,61,75,81,85,86));
		checkGaming(i_ghost, "FPS Game: Game in Korea (7)", 
				"2 keys in (WASD)+ " +
				"3 keys in (Esc 1234 tab R L-Ctrl L-Shift M Space) must be allowed", 
				lts_quartets, KbdMatrix.l_WASD, 
				List(110,2,3,4,5,16,20,44,58,52,61));
		checkGaming(i_ghost, "FPS Game: Medal of Honor (10)", 
				"2 keys in (WASD)+ " +
				"3 keys in (EFG L-Ctrl L-Shift ?) must be allowed", 
				lts_quartets, KbdMatrix.l_WASD, 
				List(19,34,35,44,58,55));
		checkGaming(i_ghost, "FPS Game: Quake4 (12)", 
				"2 keys in (WASD)+ " +
				"3 keys in (L-Ctrl L-Shift Insert) must be allowed", 
				lts_quartets, KbdMatrix.l_WASD, 
				List(44,58,75));


		checkGaming(i_ghost, "MMORPG Games", 
				"4 keys in (WASD 1234 tab L-Ctrl L-Shift Space)+ " +
				"2 keys in (F1-8 5 QERFZXCV L-Alt Insert Home Delete End PageUp PageDown) must be allowed", 
				lts_quartets, KbdMatrix.l_WASD++List(2,3,4,5,16,44,58,61),
				List(112,113,114,115,116,117,118,119,17,19,20,34,46,47,48,49,60,75,76,80,81,85,86));
		checkGaming(i_ghost, "Sports And Racing Games", 
				"4 keys in (Arrows AZ L-Alt L-Ctrl L-Shift) + 2 keys in (3QWERSDXCBN Space) must be allowed", 
				lts_quartets, KbdMatrix.l_arrows++List(31,46,44,58,60), List(4,17,18,19,20,32,33,47,48,50,51));
		checkGaming(i_ghost, "Action Games", 
				"2 Arrows + 2 keys in (IASFGH L-Shift ZXC L-Ctrl) must be allowed", 
				lts_quartets, KbdMatrix.l_arrows, List(24,31,32,34,35,36,44,46,47,48,58));
		checkGaming(i_ghost, "Online Rhythm Game in Korea", 
				"4 keys in (Arrows KP1-9) + 1 key in (F5-8 Space) must be allowed", 
				lts_quartets, KbdMatrix.l_arrows++List(91,92,93,96,97,98,101,102,103), List(61,116,117,118,119));
		checkGaming(i_ghost, "DJ max Rhythm Game", 
				"4 keys in (QWERTZU) + 1 key in (IOP Space) must be allowed", 
				lts_quartets, List(17,18,19,20,21,22,23), List(24,25,26,61));
		checkGaming(i_ghost, "Beat mania Rhythm Game", 
				"4 keys in (SDZXC Space) must be allowed", 
				lts_quartets, List(32,33,46,47,48,61), List());
		
		checkGamingTier1(i_ghost,  
				"2 green keys + 1 red key AND 1 green key + 2 red keys MUST be allowed", 
				lts_quartets, KbdMatrix.l_WASD, KbdMatrix.l_gaming2Left--KbdMatrix.l_WASD);
		checkGamingTier1(i_ghost,  
				"2 green keys + 1 red key AND 1 green key + 2 red keys MUST be allowed", 
				lts_quartets, KbdMatrix.l_ESDF, KbdMatrix.l_gaming2Left--KbdMatrix.l_ESDF);
		checkGamingTier1(i_ghost,  
				"2 green keys + 1 red key AND 1 green key + 2 red keys MUST be allowed", 
				lts_quartets, KbdMatrix.l_arrows, KbdMatrix.l_gaming2ArrowsRed);
		checkGamingTier1(i_ghost,  
				"2 green keys + 1 red key AND 1 green key + 2 red keys MUST be allowed", 
				lts_quartets, KbdMatrix.l_kpArrows, KbdMatrix.l_gaming2KpArrowsRed);
		
		checkGamingTier2(i_ghost,  
				"2 green from 2 different green areas+ 1 red key should be allowed", 
				lts_quartets, KbdMatrix.l_WASD, KbdMatrix.l_ESDF, 
				(KbdMatrix.l_gaming2Left--KbdMatrix.l_WASD)--KbdMatrix.l_ESDF);	
		checkGamingTier2(i_ghost,  
				"2 green from 2 different green areas+ 1 red key should be allowed", 
				lts_quartets, KbdMatrix.l_WASD, KbdMatrix.l_arrows, 
				(KbdMatrix.l_gaming2Left++KbdMatrix.l_gaming2ArrowsRed)--KbdMatrix.l_WASD);
		checkGamingTier2(i_ghost,  
				"2 green from 2 different green areas+ 1 red key should be allowed", 
				lts_quartets, KbdMatrix.l_ESDF, KbdMatrix.l_arrows, 
				(KbdMatrix.l_gaming2Left++KbdMatrix.l_gaming2ArrowsRed)--KbdMatrix.l_ESDF);
		checkGamingTier2(i_ghost,  
				"2 green from 2 different green areas+ 1 red key should be allowed", 
				lts_quartets, KbdMatrix.l_WASD, KbdMatrix.l_kpArrows, 
				(KbdMatrix.l_gaming2Left++KbdMatrix.l_gaming2KpArrowsRed)--KbdMatrix.l_WASD);
		checkGamingTier2(i_ghost,  
				"2 green from 2 different green areas+ 1 red key should be allowed", 
				lts_quartets, KbdMatrix.l_ESDF, KbdMatrix.l_kpArrows, 
				(KbdMatrix.l_gaming2Left++KbdMatrix.l_gaming2KpArrowsRed)--KbdMatrix.l_ESDF);
		checkGamingTier2(i_ghost,  
				"2 green from 2 different green areas+ 1 red key should be allowed", 
				lts_quartets, KbdMatrix.l_arrows, KbdMatrix.l_kpArrows, 
				(KbdMatrix.l_gaming2ArrowsRed++KbdMatrix.l_gaming2KpArrowsRed)--
				(KbdMatrix.l_arrows++KbdMatrix.l_kpArrows));
		
		checkGaming(i_ghost, 60, "1 Aion", 
				"2 keys in (WASD) + 2 keys in (1234567890 Esc F1 Tab QERTFG L-Shift CV L-Alt Space) must be allowed", 
				lts_quartets, KbdMatrix.l_WASD, List(110,112,2,3,4,5,6,7,8,9,10,11,16,17,19,20,21,24,
						34,35,44,48,49,60,61));
		checkGaming(i_ghost, 60, "2 Tera", 
				"2 keys in (WASD) + 2 keys in (1234567890-+ Esc F1-12 CNM L-Alt NumLock) must be allowed", 
				lts_quartets, KbdMatrix.l_WASD, List(110,112,113,114,115,116,117,118,119,120,121,122,123,
						2,3,4,5,6,7,8,9,10,11,12,13,
						48,51,52,60,90));
		checkGaming(i_ghost, 60, "3 Sudden Attack", 
				"2 keys in (WASD) + 2 keys in (Esc 1234 QERG L-Shift Space) must be allowed", 
				lts_quartets, KbdMatrix.l_WASD, List(110,2,3,4,5,17,19,20,35,44,61));
		checkGaming(i_ghost, 60, "4 and 6 Warcraft3 and WoW WASD", 
				"2 keys in (WASD) + 2 keys in (1234567890-+ F1-12 Tab QERTY " +
				"FGH L-Shift ZXCVBN L-Ctrl L-Alt Space) must be allowed", 
				lts_quartets, KbdMatrix.l_WASD, List(112,113,114,115,116,117,118,119,120,121,122,123,
						2,3,4,5,6,7,8,9,10,11,12,13,
						16,17,19,20,20,21,22,34,35,36,
						44,46,47,48,49,50,51,
						58,60,61));
		checkGaming(i_ghost, 60, "4 and 6 Warcraft3 and WoW ESDF", 
				"2 keys in (ESDF) + 2 keys in (1234567890-+ F1-12 Tab QWRTY AGH L-Shift ZXCVBN L-Ctrl L-Alt Space) must be allowed", 
				lts_quartets, KbdMatrix.l_ESDF, List(112,113,114,115,116,117,118,119,120,121,122,123,
						2,3,4,5,6,7,8,9,10,11,12,13,
						16,17,18,20,20,21,22,31,35,36,
						44,46,47,48,49,50,51,
						58,60,61));
		checkGaming(i_ghost, 60, "5 StarcraftASD", 
				"2 keys in (ASD) + 2 keys in (Tilde 1234567890 TIP GH L-Shift ZCVBNM L-Ctrl) must be allowed", 
				lts_quartets, List(31,32,33), List(1,2,3,4,5,6,7,8,9,10,11,
						21,24,26,35,36,
						44,46,48,49,50,51,52,58));
		checkGaming(i_ghost, 60, "7 FIFA online", 
				"2 keys in (Arrows) + 2 keys in (QWEASDZC) must be allowed", 
				lts_quartets, KbdMatrix.l_arrows, List(17,18,19,31,32,33,46,48));
		L.myErrPrintln("\n</td></tr></table>")
	}

	
	def checkWHQL(i_ghost: Int) {
		val c_start = L.timeStamp("checkWHQL");
		L.myErrPrintln("\n</pre><tr><td bgcolor=\""+KbdMatrix.bgColor(i_ghost+10)+"\">" +
				"<h3>checkWHQL(numberOfKeysInAQuartetThatMakeAGhostCondition:"+
				i_ghost+", MUST!)</h3><pre>\n"+
				ts_quartets.filterNot(_.passWHQL(i_ghost, 
						KbdKey.mustPassWHQL)).mkString("  ","\n  ",""));
		L.myErrPrintln("</pre><h3>checkWHQL(numberOfKeysInAQuartetThatMakeAGhostCondition:"+
				i_ghost+", Logitech guidelines)</h3><pre>\n"+
				ts_quartets.filterNot(_.passWHQL(i_ghost, 
						KbdKey.shouldPassWHQL)).mkString("  ","\n  ",""));
		L.timeStamp(c_start,"checkWHQL")
		L.myErrPrintln("\n</pre></td></tr>");
	}

	def checkGaming(i_ghost: Int, s_title: String, s_rule: String, lts_quartets: TreeSet[Quartet], 
			l_redList: List[Int], l_yellowList: List[Int]) {
		checkGaming(i_ghost, 20, s_title, s_rule, lts_quartets, l_redList, l_yellowList)
	}

	def checkGaming(i_ghost: Int, i_color: Int, s_title: String, s_rule: String, lts_quartets: TreeSet[Quartet], 
			l_redList: List[Int], l_yellowList: List[Int]) {
		val c_start = L.timeStamp("checkGaming");
		val ts_q = lts_quartets.filter(_.l_quartet.intersect(l_redList++l_yellowList).size>2)
		val ts_2red = ts_q.filter(_.l_quartet.intersect(l_redList).size>1)
		val ts_2yellow = ts_q.filter(_.l_quartet.intersect(l_yellowList).size>1)
		L.myErrPrintln("</pre><tr><td bgcolor=\""+
				KbdMatrix.bgColor(i_ghost+i_color)+"\"><h3>Check "+s_title+"</h3><h4>["+s_rule+"] (numberOfKeysInAQuartetThatMakeAGhostCondition: "+
				i_ghost+"):</h4><pre>\n"+
				ts_2red.mkString("\nl_redList</pre>{"+tostringList(l_redList)+"}&gt;1<pre>\n  ","\n  ","\n")+
				ts_2yellow.mkString("\nl_yellowList</pre>{"+tostringList(l_yellowList)+"}&gt;1<pre>\n  ","\n  ","</td></tr>\n"));
		L.timeStamp(c_start,s_title)
	}

	def checkGamingTier1(i_ghost: Int, s_rule: String, lts_quartets: TreeSet[Quartet], 
			l_greenList: List[Int], l_redList: List[Int]) {
		val c_start = L.timeStamp("checkGamingTier1");
		val ts_q = lts_quartets.filter(_.checkGamingTier1(l_greenList,l_redList))
		L.myErrPrintln("<tr><td bgcolor=\""+
				KbdMatrix.bgColor(i_ghost+40)+"\"><h3>Check Tier1 "+tostringList2(l_greenList)+"</h3><h4>["+s_rule+"] (numberOfKeysInAQuartetThatMakeAGhostCondition: "+
				i_ghost+"):</h4><ul><li>l_greenList{"+
				tostringList(l_greenList)+"}</li><li>l_redList{"+tostringList(l_redList)+"}</li></ul><pre>\n"+
				ts_q.mkString("  ","\n  ","\n")
				+"</pre></td></tr>\n");
		L.timeStamp(c_start,tostringList2(l_greenList))
	}

	def checkGamingTier2(i_ghost: Int, s_rule: String, lts_quartets: TreeSet[Quartet], 
			l_greenList1: List[Int], l_greenList2: List[Int], l_redLists: List[Int]) {
		val c_start = L.timeStamp("checkGamingTier2");
		val ts_q = lts_quartets.filter(_.checkGamingTier2(l_greenList1,l_greenList2,l_redLists))
		L.myErrPrintln("<tr><td bgcolor=\""+
				KbdMatrix.bgColor(i_ghost+50)+"\"><h3>Check Tier2 "+
				tostringList2(l_greenList1)+"   "+tostringList2(l_greenList2)+"</h3><h4>["+
				s_rule+"] (numberOfKeysInAQuartetThatMakeAGhostCondition: "+
				i_ghost+"):</h4><ul><li>l_greenList1{"+tostringList(l_greenList1)+
				"}</li><li>l_greenList2{"+tostringList(l_greenList2)+
				"}</li><li>l_redList{"+tostringList(l_redLists)+"}</li></ul><pre>\n"+
				ts_q.mkString("  ","\n  ","\n")
				+"</pre></td></tr>\n");
		L.timeStamp(c_start,tostringList2(l_greenList1++l_greenList2))
		}

}

class CompareToString extends Ordering[Quartet] {
	def compare(q1: Quartet, q2: Quartet) = q1.toString.compare(q2.toString)
}
