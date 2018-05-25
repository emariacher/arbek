package fastlogsparsers

/**
C:\Users\11emariacher\workspace\fastlogsparsers\out
highlight Domain\HiApplier_hlp.c #e0e0f0
highlight DynTest #e0f0e0
*/

object Parameters {
  var p: Parameters = _
  def newParameters(l_parameters: List[String]) = {
    p =  new Parameters(l_parameters: List[String])
  }
}

class Parameters(val l_parameters: List[String]) {
  val r_highlight ="""highlight (\S+) (\#\p{XDigit}+)""".r; 
  val lparms = l_parameters.map ((s: String) => {
    s match {
      case r_highlight(string, bgcolor) => ("highlight", string, bgcolor)
      case _ => ("","","")
    }
  })  
  
  def highlight(lino: String): String = {
    val lmatched = lparms.filter(_._1=="highlight").filter((t: (String,String,String)) => lino.indexOf(t._2)>=0).
    sortBy{(t: (String,String,String)) => t._2.length}.reverse.map(_._3)
    if(lmatched.isEmpty) {
      ""
    } else {
      lmatched.head
    }
  }
}
