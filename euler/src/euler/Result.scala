package euler

class Result(val start: Item, val func: Function, val filter: Function) {
  var sameAsPrevious = false
	def process: Result = {
	if(filter.process(start).b) {
		new Result(func.process(start), func, filter)
	} else {	  
		val r = new Result(func.fallback(start), func, filter)
		r.sameAsPrevious = true
		r
	}
}

override def toString: String = {
  var s = start.toString 
  if(sameAsPrevious) {
    s = s +"#"
  } else {
    s = s + " "
  }
  s
}
}