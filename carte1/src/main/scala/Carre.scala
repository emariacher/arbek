package carte

import java.awt.Dimension
import java.awt.Graphics2D
import scala.collection.immutable.ListSet
import java.awt.Color
import carte.CantonTypeV._
import carte.FrontiereV._
import carte.Tableaux._

class Carre {
	val lcarres = tbx.cb.lcarres
			var row: Int = _
			var col: Int = _
			var frontieres = List[Frontiere]()
			val cno = new Canton(nordouest, this)
	val cne = new Canton(nordest, this)
	val cso = new Canton(sudouest, this)
	val cse = new Canton(sudest, this)
	var cantons = List[Canton](cno,cne,cso,cse)
	val p = lcarres.getNextRowCol
	//			// get row col
	row = p._1
	col = p._2
	// get Frontieres
	val leftCarre = getLeftCarre
	val upCarre = getUpCarre
	leftCarre match {
	case Some(c) => if(c.frontieres.contains(est)) {
		frontieres = frontieres :+ ouest 
	}
	case _ => tbx.rnd.nextInt(2) match {
	case 0 =>
	case 1 => frontieres = frontieres :+ ouest
	}
	}
	upCarre match {
	case Some(c) => if(c.frontieres.contains(sud)) {
		frontieres = frontieres :+ nord 
	}
	case _ => tbx.rnd.nextInt(2) match {
	case 0 =>
	case 1 => frontieres = frontieres :+ nord
	}
	}
	frontieres.size match {
	case 0 => frontieres = frontieres :+ sud
			frontieres = frontieres :+ est
	case 1 => tbx.rnd.nextInt(3) match {
	case 0 => frontieres = frontieres :+ sud
	case 1 => frontieres = frontieres :+ est
	case 2 => frontieres = frontieres :+ sud
	frontieres = frontieres :+ est
	}
	case 2 => tbx.rnd.nextInt(3) match {
	case 0 => 
	case 1 => frontieres = frontieres :+ sud
	case 2 => frontieres = frontieres :+ est
	}
	}

	assume(List(2,3).contains(frontieres.size),"List(2,3).contains(frontieres.size: "+frontieres.size+")")
	//	System.out.println(toString)

	def isLast(): Boolean = {
			((row==tbx.maxRow-1)&&(col==tbx.maxCol-1))
	}

	def paint(g: Graphics2D) {
		val horiz = tbx.size.getWidth.toInt/(tbx.maxCol*2)
				val vert = tbx.size.getHeight.toInt/(tbx.maxRow*2)
				val x = tbx.origin.getWidth.toInt+(horiz*((2*col)+1))
				val y = tbx.origin.getHeight.toInt+(vert*((2*row)+1))

				cantons.foreach(_.paint(g, horiz, vert, x, y))
				g.setColor(Color.black)
				frontieres.foreach(_.paint(g, horiz, vert, x, y))
				//				g.drawString(toString,x,y)
	}
	override def toString: String = {
			"Carre{("+row+","+col+"), "+frontieres+", "+cantons+"}"
		}
		def getRegions = {
			var lsr = ListSet[Region]()
					cantons.foreach((c: Canton) => lsr = lsr + c.r)
					lsr.foreach((r: Region) => r.voisins = r.voisins ++ lsr.filter(_ != r))
		}
		def allCantonsKnown: Boolean = {
				!cantons.exists(_.r==null)
			}
			def repandRegions =  {
				cantons.find(_.r==null) match { // to be optimized
				case Some(canton) => canton.repand
				case _ => require(false)
				}

			}
			def cleanRegions = {
				if(cno.r==cne.r) {
					frontieres = frontieres.filterNot(_==nord)
				}
				if(cso.r==cse.r) {
					frontieres = frontieres.filterNot(_==sud)
				}
				if(cno.r==cso.r) {
					frontieres = frontieres.filterNot(_==ouest)
				}
				if(cne.r==cse.r) {
					frontieres = frontieres.filterNot(_==est)
				}
			}
			def getLeftCarre: Option[Carre] = {
					lcarres.lc.find((cf: Carre) => cf.row==row && cf.col==col-1)
			}
			def getUpCarre: Option[Carre] = {
						lcarres.lc.find((cf: Carre) => cf.row==row-1 && cf.col==col)
					}
					def getRightCarre: Option[Carre] = {
							lcarres.lc.find((cf: Carre) => cf.row==row && cf.col==col+1)
						}
						def getDownCarre: Option[Carre] = {
								lcarres.lc.find((cf: Carre) => cf.row==row+1 && cf.col==col)
							}

}

