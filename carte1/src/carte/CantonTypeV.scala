package carte

import java.awt.Graphics2D


class CantonType(val i: CantonTypeV.Value) {

	def paint(g: Graphics2D, horiz: Int, vert: Int, x: Int, y: Int, r: Region) {
		i match {
		case CantonTypeV.NORDOUEST => 
		g.setColor(r.couleur)
		g.fillRect(x-horiz,y-vert,horiz,vert)
		/*		g.setColor(Color.black)
		g.drawString("no"+r.id,x-(2*horiz/3),y-(vert/2))*/
		case CantonTypeV.NORDEST => 
		g.setColor(r.couleur)
		g.fillRect(x,y-vert,horiz,vert)
		/*		g.setColor(Color.black)
		g.drawString("ne"+r.id,x+(horiz/3),y-(vert/2))*/
		case CantonTypeV.SUDOUEST => 
		g.setColor(r.couleur)
		g.fillRect(x-horiz,y,horiz,vert)
		/*		g.setColor(Color.black)
		g.drawString("so"+r.id,x-(2*horiz/3),y+(vert/2))*/
		case CantonTypeV.SUDEST => 
		g.setColor(r.couleur)
		g.fillRect(x,y,horiz,vert)
		/*		g.setColor(Color.black)
		g.drawString("se"+r.id,x+(horiz/3),y+(vert/2))*/
		}

	}
}

object CantonTypeV extends Enumeration {
	type CantonTypeV = Value
			val NORDOUEST, NORDEST, SUDOUEST, SUDEST = Value
			CantonTypeV.values foreach println			
			val nordouest = new CantonType(CantonTypeV.NORDOUEST)
	val nordest   = new CantonType(CantonTypeV.NORDEST)
	val sudouest  = new CantonType(CantonTypeV.SUDOUEST)
	val sudest    = new CantonType(CantonTypeV.SUDEST)
}
