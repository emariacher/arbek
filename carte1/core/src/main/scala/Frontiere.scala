package carte

import java.awt.Graphics2D

class Frontiere(val i: FrontiereV.Value) {
				def paint(g: Graphics2D, horiz: Int, vert: Int, x: Int, y: Int) {
		i match {
		case FrontiereV.NORD => g.drawLine(x,y,x,y-vert)
		case FrontiereV.EST => g.drawLine(x,y,x+horiz,y)
		case FrontiereV.SUD => g.drawLine(x,y,x,y+vert)
		case FrontiereV.OUEST => g.drawLine(x,y,x-horiz,y)
		}
	}
}

object FrontiereV extends Enumeration {
	type FrontiereV = Value
			val NORD, EST, SUD, OUEST = Value
			val nord = new Frontiere(FrontiereV.NORD)
			val est = new Frontiere(FrontiereV.EST)
			val sud = new Frontiere(FrontiereV.SUD)
			val ouest = new Frontiere(FrontiereV.OUEST)
	//FrontiereV.values foreach println
}

