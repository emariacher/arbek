package euler

object EulerGrid {
	val InvalidCell = new EulerCell(0,121,121) 
}

abstract class EulerGrid(val igrid: List[List[Int]]) {
	var grid = List[EulerCell]()
			igrid.zipWithIndex.map((l: (List[Int],Int)) => l._1.zipWithIndex.map((m: (Int, Int)) => grid = grid :+ new EulerCell(m._1,l._2,m._2)))

			var max = 0
			var maxi = 0

			def get(r: Int, c: Int): EulerCell = {
		grid.find((cell: EulerCell) => cell.r == r & cell.c == c)  match {
		case Some(cell) => cell
		case _ => EulerGrid.InvalidCell
		}
	}

	def getRow(r: Int): List[EulerCell] = {
		grid.filter((cell: EulerCell) => cell.r == r)
	}

	def findMax: Int
}

class Euler18Grid(override val igrid: List[List[Int]]) extends EulerGrid(igrid) {
	def findMax: Int = {
	grid.foreach((cell: EulerCell) => {
		val c = cell.c
				val r = cell.r
				cell.sum = List(get(r-1,c).sum,get(r-1,c-1).sum).max + cell.i
				//println(cell+ " " + cell.sum)
	})
	grid.map((cell: EulerCell) => cell.sum).max
}
}

class Euler11Grid(override val igrid: List[List[Int]]) extends EulerGrid(igrid) {
	max = get(19,12).i*get(18,11).i*get(17,10).i*get(16,9).i
			maxi = max / (99*99*99)

			def getWest(r: Int, c: Int): Int =  get(r,c).i*get(r,c-1).i*get(r,c-2).i*get(r,c-3).i
			def getEast(r: Int, c: Int): Int =  get(r,c).i*get(r,c+1).i*get(r,c+2).i*get(r,c+3).i
			def getNorth(r: Int, c: Int): Int =  get(r,c).i*get(r-1,c).i*get(r-2,c).i*get(r-3,c).i
			def getSouth(r: Int, c: Int): Int =  get(r,c).i*get(r+1,c).i*get(r+2,c).i*get(r+3,c).i
			def getNorthWest(r: Int, c: Int): Int =  get(r,c).i*get(r-1,c-1).i*get(r-2,c-2).i*get(r-3,c-3).i
			def getNorthEast(r: Int, c: Int): Int =  get(r,c).i*get(r-1,c+1).i*get(r-2,c+2).i*get(r-3,c+3).i
			def getSouthWest(r: Int, c: Int): Int =  get(r,c).i*get(r+1,c-1).i*get(r+2,c-2).i*get(r+3,c-3).i
			def getSouthEast(r: Int, c: Int): Int =  get(r,c).i*get(r+1,c+1).i*get(r+2,c+2).i*get(r+3,c+3).i

			def findMax: Int = {
					val z =	grid.filter(_.i>=maxi).filter((cell: EulerCell) => {
						val c = cell.c
								val r = cell.r
								val l = List(getWest(r,c),getEast(r,c),getNorth(r,c),getSouth(r,c),getNorthWest(r,c),getNorthEast(r,c),getSouthWest(r,c),getSouthEast(r,c))
								cell.max = l.max
								//println("         "+cell+" "+l)
						cell.max > max
					})
					println("%%%\n"+z.mkString("\n   "))
					z.map(_.max).max
			}
}


class EulerCell(val i: Int, val r: Int, val c: Int) {
	var max = 0
			var sum = 0
			var prev = EulerGrid.InvalidCell

			override def toString: String = "[r%02d".format(r)+"c%02d".format(c)+" %02d]=".format(i)+max 

			def *(other: EulerCell): Int = i * other.i
			def *(other: Int): Int = i * other
}
