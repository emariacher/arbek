package labyrinthe

import java.awt.Color
import java.awt.Graphics2D
import labyrinthe.Tableaux._
import labyrinthe.FrontiereV._
import kebra._
import scala.swing.Label
import scala.collection.mutable.Queue
import scala.collection.immutable.ListSet
import labyrinthe.LL._

abstract class Jeton(val couleur: Couleur, val rayon: Int) {
    def this(s: String, rayon: Int) = this(new Couleur(s), rayon)

            val label = new Label { 
        text = couleur.toString 
                foreground = couleur.color
    }	
    var state = StateMachineJeton.normal
            var par4sorted = List.empty[(List[RowCol], Int)]
                    var cnt = 0
                    var visible = false
                    var rc: RowCol = _
                    var col: Int = _
                    var row: Int = _
                    var goNorth: Boolean = _
                    var goSouth: Boolean = _
                    var goWest: Boolean = _
                    var goEast: Boolean = _
                    var traces = List.empty[RowCol]
                            var next = new RowCol(888,888)
    var lastDirection = nord

    def avance: StateMachine = {
        if((next.r==0)||(next.c==0)||(next.r==tbx.maxRow)||(next.c==tbx.maxCol)||(cnt>StatJeton.limit)) {
            l.myPrintln(MyLog.tagnt(1)+couleur+" "+next)
            StateMachine.termine
            /*} else if(cnt>StatJeton.limit/2) {
			l.myErrPrintln(couleur+" est sans doute dans un cercle vicieux.")
			// la detection de cercle vicieux ne marche probablement pas pour le pion fou bleu
			// le cercle vicieux a au moins une dimension de 4 cellules
			val par4l = traces.sliding(4).toList
			val par4s = ListSet[List[RowCol]]() ++ par4l
			par4sorted = par4s.map((l: List[RowCol]) => (l,par4l.count(_==l))).toList.filter(_._2>5).sortBy{_._2}
			if(!par4sorted.isEmpty) {
				l.myErrPrintln(couleur+" est dans le cercle vicieux: "+ par4sorted.last)			
				tbx.zp.step = true
				//cnt = StatJeton.limit+1
				//StateMachine.termine
				pasFini
			} else {
				l.myPrintln(MyLog.tagnt(1)+couleur+" fausse alerte")			
				pasFini
			}*/
        } else {
            pasFini
        }
    }

    def setRowCol(r: Int, c: Int) {
        setRowCol(new RowCol(r,c))
    }

    def setRowCol(rci: RowCol) {
        rc = rci
                col = rc.c
                row = rc.r
                val NO = tbx.lc.find((c: Carre) => c.rc==rc.moinsUn) match {
                case Some(c) => 
                l.myPrintln(MyLog.tagnt(1)+"NO: "+c)
                c.frontieres.filter((f: Frontiere) => f==sud || f==est )
                case _ => List.empty[Frontiere]
        } 
        val SE = tbx.lc.find((c: Carre) => c.rc==rc) match {
        case Some(c) => 
        l.myPrintln(MyLog.tagnt(1)+"SE: "+c)
        c.frontieres.filter((f: Frontiere) => f==nord || f==ouest )
        case _ => List.empty[Frontiere]
        } 
        goNorth = NO.find(_==est).isEmpty
                goSouth = SE.find(_==ouest).isEmpty
                goWest = NO.find(_==sud).isEmpty
                goEast = SE.find(_==nord).isEmpty
                l.myPrintln(MyLog.func(1)+" "+canGo)
    }

    def canGo = (if(goNorth) "N1" else "N0")+(if(goSouth) "S1" else "S0")+(if(goWest) "O1" else "O0")+(if(goEast) "E1" else "E0")


            def paint(g: Graphics2D) {
        if(visible) {
            g.setColor(couleur.color)
            val horiz = tbx.size.getWidth.toInt/(tbx.maxCol*2)
            val vert = tbx.size.getHeight.toInt/(tbx.maxRow*2)
            val rayonh = (tbx.size.getWidth.toInt * rayon) / (tbx.maxCol*200)
            val rayonv = (tbx.size.getHeight.toInt * rayon) / (tbx.maxRow*200)
            var xg = tbx.origin.getWidth.toInt+(horiz*2*col)
            var yg = tbx.origin.getHeight.toInt+(vert*2*row)
            g.fillOval(xg-rayonh,yg-rayonv,rayonh*2,rayonv*2)
            g.setColor(Color.black)
            g.drawString(canGo,xg,yg)
            g.setColor(couleur.color)
            traces.foreach((rc: RowCol) => {
                xg = tbx.origin.getWidth.toInt+(horiz*2*rc.c)
                        yg = tbx.origin.getHeight.toInt+(vert*2*rc.r)
                        g.drawOval(xg-rayonh,yg-rayonv,rayonh*2,rayonv*2)
                        //g.drawString(""+rc,xg,yg)
            })
        }
    }
    def resetLocal {
        visible = false
                next = new RowCol(888,888)
        traces = List.empty[RowCol]
                lastDirection = nord
                cnt = 0				
    }
    def firstStep: RowCol
    val ordreChoix: Circular

    def pasFini: StateMachine = {
            l.myPrintln(MyLog.func(1)+" "+toString+" "+lastDirection)
            // essaye de trouver une case vierge en suivant l'ordre de priorite defini
            next = firstStep
            while(ordreChoix.nextf(ordreChoix)!=lastDirection) {}
            var cpt = 0
                    while((next.r==888)&&(cpt<3)) {			
                        getNext(ordreChoix.nextf(ordreChoix), true, true)
                        cpt+=1
                    }
            // accepte d'aller dans les cases deflorees en suivant l'ordre de priorite defini vu que tu n'as rien trouve de mieux
            if(next.r==888) {
                getNext(lastDirection, true, true)
                while(ordreChoix.nextf(ordreChoix)!=lastDirection) {}
                cpt = 0
                        while((next.r==888)&&(cpt<4)) { 
                            getNext(ordreChoix.prevf(ordreChoix), false, true)
                            cpt+=1
                        }
                // mais si ca te fait revenir exactement sur tes pas essaye de trouver mieux quand meme si c'est possible
                if(next.r==888) {
                    getNext(lastDirection, true, true)
                    while(ordreChoix.nextf(ordreChoix)!=lastDirection) {}
                    cpt = 0
                            while((next.r==888)&&(cpt<4)) {			
                                getNext(ordreChoix.nextf(ordreChoix), false, false)
                                cpt+=1
                            }
                }
                //bon, si tu n'y arrive pas, reviens sur tes pas
                if(next.r==888) {
                    cpt = 0
                            while((next.r==888)&&(cpt<StatJeton.limit)) { // normalement c'est 4, mais avec le fou bleu, on sait jamais			
                                getNext(ordreChoix.nextf(ordreChoix), false, false)
                                cpt+=1
                            }
                }
            }
            l.myPrintln(MyLog.tag(1)+couleur+" "+lastDirection+" "+rc+" -> "+next+" "+traces)
            assert(next.r!=888)
            traces = traces :+ rc
            if(next.r>row) lastDirection = sud
            if(next.r<row) lastDirection = nord
            if(next.c>col) lastDirection = est
            if(next.c<col) lastDirection = ouest
            setRowCol(next)
            cnt += 1
            StateMachine.avance
        }

        def getNext(f: Frontiere, newCarreOnly: Boolean, notBack: Boolean): RowCol = {
            next = new RowCol(888,888)
            f.f match {
            case NORD  => if(goNorth) next = rc.haut
            case SUD   => if(goSouth) next = rc.bas
            case OUEST => if(goWest)  next = rc.gauche
            case EST   => if(goEast)  next = rc.droite
            }
            l.myPrintln(MyLog.tagnt(2)+couleur+"  *1*  ("+f+" nco: "+newCarreOnly+" nb: "+notBack+") : "+rc+" -> "+next)
            if(newCarreOnly && !traces.filter(_==next).isEmpty) {
                l.myPrintln(MyLog.tagnt(2)+couleur+" **2** ("+f+" nco: "+newCarreOnly+") : "+rc+" -> "+next)
                next = new RowCol(888,888)
            }
            if(!traces.isEmpty) if(notBack && next==traces.last) {
                l.myPrintln(MyLog.tagnt(2)+couleur+" **3** ("+f+" nb: "+notBack+") : "+rc+" -> "+next)
                next = new RowCol(888,888)			
            }
            /*if(state==StateMachineJeton.cercleVicieux) {
				if(par2s.filter((c: (List[RowCol],Int)) => {})
				l.myPrintln(MyLog.tagnt(2)+couleur+" **4** ("+f+" cv: "+state+") : "+rc+" -> "+next)
				next = new RowCol(888,888)			
			}*/
            l.myPrintln(MyLog.tagnt(2)+couleur+"  *5*  ("+f+" nco: "+newCarreOnly+" nb: "+notBack+") : "+rc+" -> "+next)
            next
        }


        override def toString = "{"+ couleur + " "+rc+" "+canGo+"}"
}

class Couleur(val couleur: String) {
    val color = couleur match {
    case "rouge" => Color.red
    case "orange" => Color.orange
    case "vertClair" => Color.green
    case "bleu" => Color.blue
    case "bleuClair" => Color.cyan
    case "vertFonce" => new Color(0x008000)
    case _ => throw new Exception("NOGOOD!")
    }
    override def toString = couleur
}


class Circular(list: Seq[Frontiere], nextfIn: (Circular) => Frontiere, prevfIn: (Circular) => Frontiere) extends Iterator[Frontiere]{

    val elements = new Queue[Frontiere] ++= list
            var pos = 0

            def next = elements.head

            def nextf(c: Circular) = nextfIn(c)
            def prevf(c: Circular) = prevfIn(c)

            def hasNext = !elements.isEmpty
            def add(a: Frontiere): Unit = { elements += a }
    override def toString = elements.toString

}

case class StateMachineJeton private (state: String) {
    override def toString = "State_"+state
}

object StateMachineJeton {
    val normal = StateMachineJeton("normal")
            val cercleVicieux = StateMachineJeton("cercleVicieux")
}
