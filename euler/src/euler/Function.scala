package euler

class Function(val process: (Item) => Item, val fallback: (Item) => Item, val tostring: String) {
	def this(process: (Item) => Item, tostring: String) = this(process, (it: Item) => new Item(true), tostring)
			def this(betafunc: BetaFunction) = this((it: Item) => new Item(betafunc.func(it.bi.toInt).toInt), betafunc.tostring)

			override def toString: String = tostring

			def table(range: List[Item]): List[(Item,Item)] = range.map((it: Item) => (it,process(it)))
			def table2String(range: List[Item], tostr: ((Item,Item)) => String): String = table(range).foldLeft("_"+tostring+"[")(_ + ", "+tostr(_))+"]"
}

class FunctionSyracuse(val mul: BigInt) extends Function((it: Item) => {
	val i = it.bi
			if((i/2)*2==i) { new ItemSyracuse(i/2) } else { new ItemSyracuse(((mul*i)+1)/2)}
},"syracuse"+mul) {
	def stop(iteration: List[ItemSyracuse]):Boolean =  iteration.toSet.size!=iteration.size
			def stop2(iteration: List[ItemSyracuse]):Boolean =  iteration.head>=iteration.last & iteration.size>1
			def stop3(iteration: List[ItemSyracuse], max: ItemSyracuse):Boolean =  (max.i>=iteration.last.i & iteration.size<max.tempsDeVolEnAltitude) | stop(iteration)
			def stop4(iteration: List[ItemSyracuse]):Boolean =  (iteration.head>=iteration.last & iteration.size>1&iteration.filter(_>=iteration.head).size<100) | stop(iteration)
}

class FunctionEuler92 extends Function((it: Item) => {
	val li = (ItemEuler92.fmt format it.bi).toList.map(_.toString.toInt).sorted
			new ItemEuler92(BigInt(li.map((i: Int) => i*i).sum), li)
},"FunctionEuler92") {
	//	def stop(iteration: List[ItemEuler92]):Boolean =  iteration.toSet.size!=iteration.size | iteration.last.bi<iteration.head.bi
	def stop(iteration: List[ItemEuler92]):Boolean =  iteration.toSet.size!=iteration.size
}