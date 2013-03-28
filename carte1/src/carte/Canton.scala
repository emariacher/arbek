package carte

import java.awt.Dimension
import java.awt.Graphics2D
import scala.collection.immutable.ListSet
import java.awt.Color
import carte.CantonTypeV._
import carte.FrontiereV._
import carte.Tableaux._

class Canton (val ct: CantonType, val c: Carre) {
	var r: Region = null
			val lcarres = tbx.cb.lcarres

			def paint(g: Graphics2D, horiz: Int, vert: Int, x: Int, y: Int) {
	if(r!=null) {
		ct.paint(g, horiz, vert, x, y, r)
	} else {
		g.setColor(Color.lightGray)
		g.fillRect(x-horiz,y-vert,horiz*2,vert*2)
	}
}

def repand = {
		r = new Region(tbx, this)
		tbx.cb.lregions.lr = tbx.cb.lregions.lr :+ r
		r.repand
}
def getCantonsVoisins: ListSet[Canton] = {
			var lscv = ListSet[Canton]()

					ct.i match {
					case NORDOUEST => lscv = lscv + c.cne
							lscv = lscv + c.cso
							c.getLeftCarre match {
							case Some(cr) => lscv = lscv + cr.cne
							case _ => 
					}
					c.getUpCarre match {
					case Some(cr) => lscv = lscv + cr.cso
					case _ => 
					}
					case NORDEST => lscv = lscv + c.cno
							lscv = lscv + c.cse
							c.getRightCarre match {
							case Some(cr) => lscv = lscv + cr.cno
							case _ => 
					}
					c.getUpCarre match {
					case Some(cr) => lscv = lscv + cr.cse
					case _ => 
					}
					case SUDOUEST => lscv = lscv + c.cno
							lscv = lscv + c.cse
							c.getLeftCarre match {
							case Some(cr) => lscv = lscv + cr.cse
							case _ => 
					}
					c.getDownCarre match {
					case Some(cr) => lscv = lscv + cr.cno
					case _ => 
					}
					case SUDEST => lscv = lscv + c.cne
							lscv = lscv + c.cso
							c.getRightCarre match {
							case Some(cr) => lscv = lscv + cr.cso
							case _ => 
					}
					c.getDownCarre match {
					case Some(cr) => lscv = lscv + cr.cne
					case _ => 
					}
			}
			lscv
		}



		def repandVoisin(i: Int): Int = {
			//			require(i<20)
			val lscv = getCantonsVoisins
					lscv.filter(_.r==null).filter(_.c==c).foreach((canton: Canton) => 			 
					{				
						//						System.out.println(" "+i+" "+MyLog.tag(1)+" "+toString+" canton: "+canton.toString)
						ct.i match {
						case NORDOUEST => canton.ct.i match {
						case NORDOUEST => require(false)
						case NORDEST => if(!c.frontieres.contains(nord)) {canton.r = r; canton.repandVoisin(i+1)}
						case SUDOUEST => if(!c.frontieres.contains(ouest)) {canton.r = r; canton.repandVoisin(i+1)}
						case SUDEST => 
						}
						case NORDEST => canton.ct.i match {
						case NORDOUEST => if(!c.frontieres.contains(nord)) {canton.r = r; canton.repandVoisin(i+1)}
						case NORDEST => require(false)
						case SUDOUEST => 
						case SUDEST => if(!c.frontieres.contains(est)) {canton.r = r; canton.repandVoisin(i+1)}
						}
						case SUDOUEST => canton.ct.i match {
						case NORDOUEST => if(!c.frontieres.contains(ouest)) {canton.r = r; canton.repandVoisin(i+1)}
						case NORDEST => 
						case SUDOUEST => require(false)
						case SUDEST => if(!c.frontieres.contains(sud)) {canton.r = r; canton.repandVoisin(i+1)}
						}
						case SUDEST => canton.ct.i match {
						case NORDOUEST => 
						case NORDEST => if(!c.frontieres.contains(est)) {canton.r = r; canton.repandVoisin(i+1)}
						case SUDOUEST => if(!c.frontieres.contains(sud)) {canton.r = r; canton.repandVoisin(i+1)}
						case SUDEST => require(false)
						}
						}	
					}
							)
							lscv.filter(_.r==null).filter(_.c!=c).foreach((canton: Canton) => 
							{ 		 
								//								System.out.println(" "+i+" "+MyLog.tag(1)+" "+toString+" canton: "+canton.toString)
								canton.r = r
										canton.repandVoisin(i+1)
							}
									)
									i+1
		}

		def cantonsVoisinsDefined: Boolean = {
				!getCantonsVoisins.exists(_.r==null)
			}

			override def toString: String = {
					"["+c.row+","+c.col+"]."+ct.i+": "+r
				}
}
