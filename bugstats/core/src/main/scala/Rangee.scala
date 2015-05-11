package bugstats

import java.util.Calendar
import kebra.MyLog._


class Rangee(val c: Calendar, val r: List[Int]) {
  val c2string = printZisday(c,"dMMMyyyy")
	override def toString: String = "["+c2string+": "+r+"]"
	def active: Boolean = r.sum!=0
	
}