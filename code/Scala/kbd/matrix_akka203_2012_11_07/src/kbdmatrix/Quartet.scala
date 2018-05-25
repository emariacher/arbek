package kbdmatrix
import scala.collection.immutable.ListSet
import scala.collection.immutable.TreeSet
import kbdmatrix.KbdMatrix._
import kebra.MyLog._


object Quartet {
	// P26 (**) Generate the combinations of K distinct objects chosen from the N
	//          elements of a list.
	//     In how many ways can a committee of 3 be chosen from a group of 12
	//     people?  We all know that there are C(12,3) = 220 possibilities (C(N,K)
	//     denotes the well-known binomial coefficient).  For pure mathematicians,
	//     this result may be great.  But we want to really generate all the possibilities.
	//
	//     Example:
	//     scala> combinations(3, List('a, 'b, 'c, 'd, 'e, 'f))
	//     res0: List[List[Symbol]] = List(List('a, 'b, 'c), List('a, 'b, 'd), List('a, 'b, 'e), ...


	// flatMapSublists is like list.flatMap, but instead of passing each element
	// to the function, it passes successive sublists of L.
	def flatMapSublists[A,B](ls: List[A])(f: (List[A]) => List[B]): List[B] = 
		ls match {
		case Nil => Nil
		case sublist@(_ :: tail) => f(sublist) ::: flatMapSublists(tail)(f)
	}

	def combinations[A](n: Int, ls: List[A]): List[List[A]] =
		if (n == 0) List(Nil)
		else flatMapSublists(ls) { sl =>
		combinations(n - 1, sl.tail) map {sl.head :: _}
		}

}

class Quartet(i_type: Int, k_topleft: KbdKey, k_botright: KbdKey) {
	var ts_quartet = new TreeSet[KbdKey]()(new CompareRowThenCol())
	update(k_topleft)
	update(k_botright)
	update(k_topleft.i_row, k_botright.i_col)
	update(k_botright.i_row, k_topleft.i_col)
	val i_rowTop = k_topleft.i_row
	val i_colLeft = k_topleft.i_col
	val i_rowBot = k_botright.i_row
	val i_colRight = k_botright.i_col
	val l_quartet = ts_quartet.groupBy(_.i_code).keySet.toList.sort((s, t) => s < t)


	def this(k_topleft: KbdKey, k_botright: KbdKey) = this(KbdKey.i_mainMatrix,
			k_topleft, k_botright);

	def update(k: KbdKey) {
		if(k.isValid) ts_quartet += k
	}

	def update(i_row: Int, i_col: Int) {
		km.find(i_row, i_col, i_type) match {
		case Some(k: KbdKey) => if(k.isValid) ts_quartet += k
		case _ => 
		}
	}

	def passWHQL(i_numberOfKeysInAQuartetThatMakeAGhostCondition: Int, f_mustShouldPassWHQL: KbdKey => Boolean) : Boolean = {
			if(size()<i_numberOfKeysInAQuartetThatMakeAGhostCondition) {
				true
			} else if(ts_quartet.filter(f_mustShouldPassWHQL(_)).size==0) {
				true
			} else {
				val l_hasModifiers = KbdMatrix.l_modifiers.intersect(l_quartet)
				if(l_hasModifiers.size > 2) {
					false
				} else if(l_hasModifiers.size < 2) {
					true
				} else if((l_hasModifiers.intersect(KbdMatrix.l_modifiersShift)).size==2) {
					true
				} else if((l_hasModifiers.intersect(KbdMatrix.l_modifiersCtrl)).size==2) {
					true
				} else if((l_hasModifiers.intersect(KbdMatrix.l_modifiersAlt)).size==2) {
					true
				} else {
					false
				}
			}
	}

	def checkGamingTier1(l_greenList: List[Int],l_redList: List[Int]): Boolean = {
			val i_2green = l_quartet.intersect(l_greenList).size
			val i_2red = l_quartet.intersect(l_redList).size
			if(i_2green*i_2red==2) {
				true
			} else  {
				false
			}
	}

	def checkGamingTier2(l_greenList1: List[Int],l_greenList2: List[Int],l_redLists: List[Int]): Boolean = {
			val i_2green1 = l_quartet.intersect(l_greenList1).size
			val i_2green2 = l_quartet.intersect(l_greenList2).size
			val i_2red = l_quartet.intersect(l_redLists).size
			if(i_2green1*i_2green2*i_2red>0) {
				if(l_quartet.intersect(l_greenList1++l_greenList2).size>2) {
					false
				} else {
					true
				}
			} else  {
				false
			}

	}

	def getAllStrings(): ListSet[String] = {
			val l_ValidCodes = l_quartet.filter(CheckEnglishLanguageMostCurrentCombinations.isValid(_))
			val l_ValidChars = l_ValidCodes.map(KeyDoc.m_keyref(_).toString1Char())
			var l_combinationlists = Quartet.combinations(3, l_ValidChars)
			var l_strings = ListSet.empty[String]
			                              l_combinationlists.foreach((l:List[String]) => l_strings += l.tail.foldLeft(l.head)(_ + _))
			                              L.myPrintln("  *2*"+toString+"---"+l_strings)
			                              l_strings
	}

	def size(): Int = ts_quartet.filter(k => km.ts_keys.contains(k) && k.isValid()).size

	override def toString(): String =  "{" + size + ":" + ts_quartet.mkString("(",", ",")") + "}"
}
