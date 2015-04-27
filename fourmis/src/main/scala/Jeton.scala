package labyrinthe

import java.awt.Color
import java.awt.Graphics2D
import labyrinthe.Tableaux._
import labyrinthe.ZePanel._
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
    //var par4sorted = List.empty[(List[RowCol], Int)]
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
    var next = new RowCol(888, 888)
    var lastDirection = nord
    var statut = Pheronome.CHERCHE

    def init = {
        setRowCol(tbx.maxRow / 2, tbx.maxCol / 2)
        visible = true
        statut = Pheronome.CHERCHE
    }

    def avance: StateMachine = {
        if (cnt > StatJeton.limit) {
            StateMachine.termine
        } else if ((next.r == 0) || (next.c == 0) || (next.r == (tbx.maxRow+1)) || (next.c == (tbx.maxCol+1))) {
            l.myPrintln(MyLog.tagnt(1) + " "+couleur + " " + next)
            if(zp.ptype==PanelType.LABY) {
                StateMachine.termine
            } else {
                statut = Pheronome.RAMENE
                pasFini
            }
        } else {
            pasFini
        }
    }

    def setRowCol(r: Int, c: Int) {
        setRowCol(new RowCol(r, c))
    }

    def setRowCol(rci: RowCol) {
        rc = rci
        col = rc.c
        row = rc.r
        val NO = tbx.lc.find((c: Carre) => c.rc == rc.moinsUn) match {
            case Some(c) =>
                l.myPrintln(MyLog.tagnt(1) + "NO: " + c)
                c.frontieres.filter((f: Frontiere) => f == sud || f == est)
            case _ => List.empty[Frontiere]
        }
        val SE = tbx.lc.find((c: Carre) => c.rc == rc) match {
            case Some(c) =>
                l.myPrintln(MyLog.tagnt(1) + "SE: " + c)
                c.frontieres.filter((f: Frontiere) => f == nord || f == ouest)
            case _ => List.empty[Frontiere]
        }
        goNorth = NO.find(_ == est).isEmpty
        goSouth = SE.find(_ == ouest).isEmpty
        goWest = NO.find(_ == sud).isEmpty
        goEast = SE.find(_ == nord).isEmpty
        l.myPrintln(MyLog.func(1) + " " + canGo)
    }

    def canGo = (if (goNorth) "N1" else "N0") + (if (goSouth) "S1" else "S0") + (if (goWest) "O1" else "O0") + (if (goEast) "E1" else "E0")

    def paint(g: Graphics2D) {
        if (visible) {
            g.setColor(couleur.color)
            val horiz = tbx.size.getWidth.toInt / (tbx.maxCol * 2)
            val vert = tbx.size.getHeight.toInt / (tbx.maxRow * 2)
            val rayonh = (tbx.size.getWidth.toInt * rayon) / (tbx.maxCol * 200)
            val rayonv = (tbx.size.getHeight.toInt * rayon) / (tbx.maxRow * 200)
            var xg = tbx.origin.getWidth.toInt + (horiz * 2 * col)
            var yg = tbx.origin.getHeight.toInt + (vert * 2 * row)
            g.fillOval(xg - rayonh, yg - rayonv, rayonh * 2, rayonv * 2)
            g.setColor(Color.black)
            if(statut==Pheronome.CHERCHE) {
                g.drawString(canGo, xg, yg)
            } else {
                g.drawString("X", xg, yg)
            }
            g.setColor(couleur.color)
            traces.foreach((rc: RowCol) => {
                xg = tbx.origin.getWidth.toInt + (horiz * 2 * rc.c)
                yg = tbx.origin.getHeight.toInt + (vert * 2 * rc.r)
                g.drawOval(xg - rayonh, yg - rayonv, rayonh * 2, rayonv * 2)
                //g.drawString(""+rc,xg,yg)
            })
        }
    }
    def resetLocal {
        visible = false
        next = new RowCol(888, 888)
        traces = List.empty[RowCol]
        lastDirection = nord
        cnt = 0
    }
    def firstStep: RowCol
    val ordreChoix: Circular

    def pasFini: StateMachine = {
        l.myPrintln(MyLog.func(1) + " " + toString + " " + lastDirection)
        if(statut==Pheronome.CHERCHE) {
            next = firstStep
            // essaye de trouver une case qui a le maximum de pheronomes[RAMENE]
            var possibles = List.empty[RowCol]
            if(goNorth) {
                possibles = possibles :+ rc.haut
            }
            if(goSouth) {
                possibles = possibles :+ rc.bas
            }
            if(goEast) {
                possibles = possibles :+ rc.droite
            }
            if(goWest) {
                possibles = possibles :+ rc.gauche
            }
            l.myPrintln(MyLog.func(1) + " maxRow " + tbx.maxRow + " maxRow " + tbx.maxCol  + " tbxlcsize " + tbx.lc.length + " possibles: " + possibles)
            possibles = possibles.filter( z => {
                z.r<=tbx.maxRow && z.c<=tbx.maxCol && traces.find(_.equals(z)).isEmpty
            }).filter( z => {
                    var c = tbx.lc.find(_.rc.equals(z)).head
                    !c.depotPheronomes.filter(_.ph==Pheronome.RAMENE).isEmpty
            }).sortWith((a,b) => {
                var ca = tbx.lc.find(_.rc.equals(a)).head
                var cb = tbx.lc.find(_.rc.equals(b)).head
                ca.depotPheronomes.filter(_.ph==Pheronome.RAMENE).length>cb.depotPheronomes.filter(_.ph==Pheronome.RAMENE).length
            })
            l.myPrintln(MyLog.func(1) + "possibles: " + possibles)
            if(!possibles.isEmpty) {
                next = new RowCol(possibles.head.r,possibles.head.c)
                // mais ne reviens pas sur tes pas!
                l.myPrintln(MyLog.func(1) + "traces: " + traces)
                if(!traces.isEmpty) {
                    l.myPrintln(MyLog.func(1) + "traceslast: " + traces.last+" nextr "+next)
                    if (next.equals(traces.last)) {
                        possibles = possibles.drop(1)
                        l.myPrintln(MyLog.func(1) + "possibles: " + possibles)
                        if (!possibles.isEmpty) {
                            next = new RowCol(possibles.head.r, possibles.head.c)
                        }
                    }
                }
            }
            // essaye de trouver une case vierge en suivant l'ordre de priorite defini
            if (next.r == 888) {
                next = firstStep
                while (ordreChoix.nextf(ordreChoix) != lastDirection) {}
                var cpt = 0
                while ((next.r == 888) && (cpt < 3)) {
                    getNext(ordreChoix.nextf(ordreChoix), true, true)
                    cpt += 1
                }
                // accepte d'aller dans les cases deflorees en suivant l'ordre de priorite defini vu que tu n'as rien trouve de mieux
                if (next.r == 888) {
                    getNext(lastDirection, true, true)
                    while (ordreChoix.nextf(ordreChoix) != lastDirection) {}
                    cpt = 0
                    while ((next.r == 888) && (cpt < 4)) {
                        getNext(ordreChoix.prevf(ordreChoix), false, true)
                        cpt += 1
                    }
                    // mais si ca te fait revenir exactement sur tes pas essaye de trouver mieux quand meme si c'est possible
                    if (next.r == 888) {
                        getNext(lastDirection, true, true)
                        while (ordreChoix.nextf(ordreChoix) != lastDirection) {}
                        cpt = 0
                        while ((next.r == 888) && (cpt < 4)) {
                            getNext(ordreChoix.nextf(ordreChoix), false, false)
                            cpt += 1
                        }
                    }
                    //bon, si tu n'y arrive pas, reviens sur tes pas
                    if (next.r == 888) {
                        cpt = 0
                        while ((next.r == 888) && (cpt < StatJeton.limit)) {
                            // normalement c'est 4, mais avec le fou bleu, on sait jamais
                            getNext(ordreChoix.nextf(ordreChoix), false, false)
                            cpt += 1
                        }
                    }
                }
            }
            traces = traces :+ rc
        } else {
            if((traces.isEmpty)||((next.r==tbx.maxRow / 2)&&(next.c==tbx.maxCol / 2))) {
                statut=Pheronome.CHERCHE;
                traces  = List.empty[RowCol]
            } else {
                next = traces.last
                traces = traces.dropRight(1)
            }
        }

        l.myPrintln(MyLog.tag(1) + couleur + " " + lastDirection + " " + rc + " -> " + next + " " + traces)
        assert(next.r != 888)
        if(zp.ptype==PanelType.FOURMI) {
            if(!tbx.lc.find(_.rc.equals(rc)).isEmpty) {
                tbx.lc.find(_.rc.equals(rc)).head.depotPheronomes = tbx.lc.find(_.rc.equals(rc)).head.depotPheronomes :+ new Depot(0, statut, this)
            }
        }
        if (next.r > row) lastDirection = sud
        if (next.r < row) lastDirection = nord
        if (next.c > col) lastDirection = est
        if (next.c < col) lastDirection = ouest
        setRowCol(next)
        cnt += 1
        StateMachine.avance
    }

    def getNext(f: Frontiere, newCarreOnly: Boolean, notBack: Boolean): RowCol = {
        next = new RowCol(888, 888)
        f.f match {
            case NORD  => if (goNorth) next = rc.haut
            case SUD   => if (goSouth) next = rc.bas
            case OUEST => if (goWest) next = rc.gauche
            case EST   => if (goEast) next = rc.droite
        }
        l.myPrintln(MyLog.tagnt(2) + couleur + "  *1*  (" + f + " nco: " + newCarreOnly + " nb: " + notBack + ") : " + rc + " -> " + next)
        if (newCarreOnly && !traces.filter(_ == next).isEmpty) {
            l.myPrintln(MyLog.tagnt(2) + couleur + " **2** (" + f + " nco: " + newCarreOnly + ") : " + rc + " -> " + next)
            next = new RowCol(888, 888)
        }
        if (!traces.isEmpty) if (notBack && next == traces.last) {
            l.myPrintln(MyLog.tagnt(2) + couleur + " **3** (" + f + " nb: " + notBack + ") : " + rc + " -> " + next)
            next = new RowCol(888, 888)
        }
        /*if(state==StateMachineJeton.cercleVicieux) {
				if(par2s.filter((c: (List[RowCol],Int)) => {})
				l.myPrintln(MyLog.tagnt(2)+couleur+" **4** ("+f+" cv: "+state+") : "+rc+" -> "+next)
				next = new RowCol(888,888)			
			}*/
        l.myPrintln(MyLog.tagnt(2) + couleur + "  *5*  (" + f + " nco: " + newCarreOnly + " nb: " + notBack + ") : " + rc + " -> " + next)
        next
    }

    override def toString = "{" + couleur + " " + rc + " " + canGo + " " + statut + "}"
}

class Couleur(val couleur: String) {
    val color = couleur match {
        case "rouge"     => Color.red
        case "orange"    => Color.orange
        case "vertClair" => Color.green
        case "bleu"      => Color.blue
        case "bleuClair" => Color.cyan
        case "vertFonce" => new Color(0x008000)
        case _           => throw new Exception("NOGOOD!")
    }
    override def toString = couleur
}

class Circular(list: Seq[Frontiere], nextfIn: (Circular) => Frontiere, prevfIn: (Circular) => Frontiere) extends Iterator[Frontiere] {

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
    override def toString = "State_" + state
}

object StateMachineJeton {
    val normal = StateMachineJeton("normal")
    val cercleVicieux = StateMachineJeton("cercleVicieux")
}
