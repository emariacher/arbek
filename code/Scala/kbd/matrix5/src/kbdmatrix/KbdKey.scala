package kbdmatrix

import scala.collection.immutable.ListSet

import scala.collection.mutable.HashSet

import scala.collection.immutable.TreeSet

object KbdKey {
	val i_invalidCode: Int = 0xffff
	val i_emptyCode: Int = 0
	val i_mainMatrix: Int = 0
	val i_fnMatrix: Int = 1

	def typeToString(itype: Int): String = {
		if(itype==i_mainMatrix) { 
			"Main"
		} else if(itype==i_fnMatrix) {
			"Fn"
		} else {
			"UNKNOWN!"
		}
	}
	def toColorString2(i_code: Int): String = {
		val s = KeyDoc.m_keyref.getOrElse(i_code, i_code)
			if(List(151,152).contains(i_code)) { // Fn key			
				"<font color=\"green\"><b>%8s</b></font>".format(s)
			} else if(KbdMatrix.l_modifiers.contains(i_code)) {			
				"<font color=\"red\"><b>%8s</b></font>".format(s)
			} else if((KbdMatrix.l_arrows++KbdMatrix.l_WASD).contains(i_code)) {			
				"<font color=\"blue\"><b>%8s</b></font>".format(s)
			} else if((KbdMatrix.l_ESDF).contains(i_code)) {			
				"<font color=\"darkgreen\"><b>%8s</b></font>".format(s)
			} else if(KbdMatrix.l_notGreenOrRed.contains(i_code)) { 		
				"<font color=\"black\"><b>%8s</b></font>".format(s)
			} else if(KbdMatrix.l_notGreenOrRedF18keys.contains(i_code)) { 		
				"<font color=\"black\"><b>%8s</b></font>".format(s)
			} else if(i_code<KeyDoc.i_maxWHQL) { 		
				"<font color=\"purple\"><b>%8s</b></font>".format(s)
			} else {
				"<font color=\"#444444\"><b>%8s</b></font>".format(s)
			}	
}

def mustPassWHQL(k: KbdKey): Boolean = {
		k.i_code>KbdKey.i_emptyCode && k.i_code<KeyDoc.i_maxWHQL && !KbdMatrix.l_modifiers.contains(k.i_code)
}

def shouldPassWHQL(k: KbdKey): Boolean = { // MSE Buttons
		List(141, 142, 143).contains(k.i_code)
}
}

class KbdKey(val km: KbdMatrix, val i_row: Int, val i_col: Int, val i_type:Int, val i_code:Int) {
	def this(km: KbdMatrix, i_row: Int, i_col: Int, i_code:Int) = this(km, i_row, i_col, KbdKey.i_mainMatrix, i_code)
	def this(km: KbdMatrix, i_row: Int, i_col: Int) = this(km, i_row, i_col, KbdKey.i_mainMatrix, KbdKey.i_emptyCode)
	def this(km: KbdMatrix, i_code:Int) = this(km, 0xff, 0xff, KbdKey.i_mainMatrix, i_code)

	var l_quartets = ListSet.empty[Quartet];

	def generateQuartets(ts_sameRow: TreeSet[KbdKey],ts_keysMain: TreeSet[KbdKey]): ListSet[Quartet] = {
			var ts_nextKeys = ts_sameRow.from(this).tail.filter(_.isValid || this.isValid)
			ts_keysMain.filter((k: KbdKey) => k.i_col>i_col)
				.foreach((k: KbdKey) => l_quartets += new Quartet(km, this, k))
			val l_quartetsSup2 = l_quartets.filter(_.size>2)
			km.L.myPrintln(" "+toString+" Quartets "+l_quartetsSup2)
			l_quartetsSup2
	}

	def generateQuartetsOld(): ListSet[Quartet] = {
			var ts_nextKeys = km.ts_keys.filter(_.i_type==KbdKey.i_mainMatrix).from(this).tail.filter(_.isValid || this.isValid)
			ts_nextKeys.filter((k: KbdKey) => k.i_row>i_row && k.i_col>i_col)
				.foreach((k: KbdKey) => l_quartets += new Quartet(km, this, k))
			val l_quartetsSup2 = l_quartets.filter(_.size>2)
			km.L.myPrintln(" "+toString+" Quartets "+l_quartetsSup2)
			l_quartetsSup2
	}

	def tohtmlstring(): String = {
			if(i_code==KbdKey.i_emptyCode) {
				"<td/>"
			} else if(List(151,152).contains(i_code)) { // Fn key			
				"<td bgcolor=\"green\">"+KeyDoc.m_keyref(i_code).s_key+"</td>"
			} else if(KbdMatrix.l_modifiers.contains(i_code)) {			
				"<td bgcolor=\"wheat\">"+KeyDoc.m_keyref(i_code).s_key+"</td>"
			} else if((KbdMatrix.l_arrows++KbdMatrix.l_WASD).contains(i_code)) {			
				"<td bgcolor=\"lightblue\">"+KeyDoc.m_keyref(i_code).s_key+"</td>"
			} else if(hasFn()) {			
				"<td bgcolor=\"lightgreen\">"+KeyDoc.m_keyref.getOrElse(i_code, i_code)+"</td>"
			} else if(KbdMatrix.l_notGreenOrRed.contains(i_code)) { 		
				"<td bgcolor=\"#FFFF99\">"+KeyDoc.m_keyref.getOrElse(i_code, i_code)+"</td>"
			} else if(KbdMatrix.l_notGreenOrRedF18keys.contains(i_code)) { 		
				"<td bgcolor=\"#FFCC00\">"+KeyDoc.m_keyref.getOrElse(i_code, i_code)+"</td>"
			} else if(i_code<KeyDoc.i_maxWHQL) { 		
				"<td bgcolor=\"#FF9900\">"+KeyDoc.m_keyref.getOrElse(i_code, i_code)+"</td>"
			} else {
				"<td bgcolor=\"lightgrey\">"+KeyDoc.m_keyref.getOrElse(i_code, i_code)+"</td>"
			}
	}

	def toColorString(): String = {
		KbdKey.toColorString2(i_code)
	}

	def hasFn(): Boolean = {
			if(i_type==KbdKey.i_mainMatrix)
				km.findFn(i_row, i_col) match {
				case Some(k) => true
				case _ => false
			} else {
				false
			}
	}

	def isValid(): Boolean = {
			i_code>KbdKey.i_emptyCode && i_code<KbdKey.i_invalidCode
	}
	def fnWHQL(): Boolean = {
			i_code>KbdKey.i_emptyCode && i_code<KeyDoc.i_maxWHQL && i_type==KbdKey.i_fnMatrix
	}

	override def toString(): String = "[%dr%2dc%2d : %3d/<b>%s</b>]".format(i_type, i_row, i_col, i_code, toColorString())
	def value(): Int = (i_type*0x100000)+(i_row*0x1000)+i_col

}