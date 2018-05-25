package syracuse
import scala.collection.immutable.ListSet

class Function(val process: (Item) => Item, val tostring: String) {
	override def toString: String = tostring
			def stop(iteration: List[Item]):Boolean = false
}

class Syracuse(val mul: Int) extends Function((it: Item) => { 
	val i = it.r
			if((i/2)*2==i) { new Item(i/2) } else { new Item((mul*i)+1)}
},"syracuse"+mul) {
	//  override def stop(iteration: List[Item]):Boolean = iteration.contains(new Item(1))
	override def stop(iteration: List[Item]):Boolean =  iteration.toSet.size!=iteration.size 
}
