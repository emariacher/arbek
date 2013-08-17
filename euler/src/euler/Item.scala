package euler

class Item(val bi: BigInt, val i: Int, val d: Double, val b: Boolean) {
    def this(i: BigInt) = this(i, 0, 0.0, true)
    def this(i: Int) = this(0, i, 0.0, true)
    def this(d: Double) = this(0, 0, d, true)
    def this(b: Boolean) = this(0, 0, 0.0, b)
    def this(it: Item) = this(it.bi, it.i, it.d, it.b)
    override def toString: String = "[" + i + " %4.15f]".format(d)
}

class ItemSyracuse(override val bi: BigInt) extends Item(bi) {

    var iteration = List[ItemSyracuse](this)

    def process(func: FunctionSyracuse): ItemSyracuse = {
        iteration = iteration :+ func.process(result).asInstanceOf[ItemSyracuse]
        result
    }

    def process(func: FunctionSyracuse, limit: Int): ItemSyracuse = {
        if (limit == 0) {
            while (!func.stop(iteration)) {
                process(func)
            }
        } else {
            while ((iteration.size < limit) & (!func.stop(iteration))) {
                process(func)
            }
        }
        iteration = iteration.dropRight(1)
        result
    }

    def process2(func: FunctionSyracuse): ItemSyracuse = {
        while (!func.stop2(iteration)) {
            process(func)
        }
        iteration = iteration.dropRight(1)
        new ItemSyracuse(0)
    }

    def process4(func: FunctionSyracuse): ItemSyracuse = {
        while (!func.stop4(iteration)) {
            process(func)
        }
        iteration = iteration.dropRight(1)
        new ItemSyracuse(0)
    }

    def getCycle: List[ItemSyracuse] = {
        iteration.dropWhile(_ != result).dropRight(1)
    }

    def reset = iteration = List[ItemSyracuse](this)

    def tempsDeVol = iteration.filter(_ >= new ItemSyracuse(1)).size

    def tempsDeVolEnAltitude = iteration.filter(_ > this).size - 1

    def altitudeMaximale = iteration.map(_.bi).max

    def >(other: Item): Boolean = bi > other.bi

    def >=(other: Item): Boolean = bi >= other.bi

    def >>(other: ItemSyracuse): Boolean = tempsDeVol > other.tempsDeVol

    def result = iteration.last

    def toStringSummary: String = "([" + bi.toString + "] TV" + tempsDeVol + ", TVA" + tempsDeVolEnAltitude + ", AM" + altitudeMaximale + ") " + getCycle

    override def toString: String = bi.toString

    override def equals(x: Any): Boolean = hashCode.equals(x.asInstanceOf[Item].hashCode)

    override def hashCode: Int = toString.hashCode
}

object ItemEuler92 {
    val fmt = "%03d"
}

class ItemEuler92(override val bi: BigInt, val li: List[Int]) extends Item(bi) {
    //	def this(bi: BigInt) = this(bi, bi.toString.toList.map(_.toString.toInt).sorted)
    def this(bi: BigInt) = this(bi, (ItemEuler92.fmt format bi).toList.map(_.toString.toInt).sorted)

    var iteration = List[ItemEuler92](this)

    def process(func: FunctionEuler92): ItemEuler92 = {
        iteration = iteration :+ func.process(result).asInstanceOf[ItemEuler92]
        result
    }

    def process(func: FunctionEuler92, limit: Int): ItemEuler92 = {
        if (limit == 0) {
            while (!func.stop(iteration)) {
                process(func)
            }
        } else {
            while ((iteration.size < limit) & (!func.stop(iteration))) {
                process(func)
            }
        }
        //	iteration = iteration.dropRight(1)
        result
    }

    def getCycle: List[ItemEuler92] = {
        iteration.dropWhile(_ != result).dropRight(1)
    }

    def reset = iteration = List[ItemEuler92](this)

    def tempsDeVol = iteration.filter(_ >= new ItemEuler92(1)).size

    def tempsDeVolEnAltitude = iteration.filter(_ > this).size - 1

    def altitudeMaximale = iteration.map(_.bi).max

    def >(other: Item): Boolean = bi > other.bi

    def >=(other: Item): Boolean = bi >= other.bi

    def >>(other: ItemSyracuse): Boolean = tempsDeVol > other.tempsDeVol

    def result = iteration.last

    def toStringSummary: String = "([" + bi.toString + "] TV" + tempsDeVol + ", TVA" + tempsDeVolEnAltitude + ", AM" + altitudeMaximale + ") " + getCycle

    override def toString: String = bi.toString

    override def equals(x: Any): Boolean = hashCode.equals(x.asInstanceOf[Item].hashCode)

    override def hashCode: Int = toString.hashCode
}