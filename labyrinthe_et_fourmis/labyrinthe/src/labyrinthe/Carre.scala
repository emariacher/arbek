package labyrinthe

import java.awt.Dimension
import java.awt.Graphics2D
import scala.collection.immutable.ListSet
import java.awt.Color
import labyrinthe.FrontiereV._
import labyrinthe.Tableaux._
import kebra._
import labyrinthe.LL._

class Carre(val rc: RowCol) {
    def this(r: Int, c: Int) = this(new RowCol(r, c))
    val row = rc.r
    val col = rc.c
    val engendreThreshold = 998
    val prolongeThreshold = 550
    var frontieres = List[Frontiere]()

    def autre(maFrontiere: Frontiere) = {
        maFrontiere.f match {
            case NORD  => (getUpCarre, sud)
            case SUD   => (getDownCarre, nord)
            case EST   => (getRightCarre, ouest)
            case OUEST => (getLeftCarre, est)
        }
    }

    def nettoie {
            def nettoie2(maFrontiere: Frontiere) = {
                if (hasFrontiere(maFrontiere)) {
                    val (autreCarre, autreFrontiere) = autre(maFrontiere)
                    autreCarre match {
                        case Some(c) => if (!c.hasFrontiere(autreFrontiere)) frontieres = frontieres.filter(_ != maFrontiere)
                        case _       => frontieres = frontieres.filter(_ != maFrontiere)
                    }
                }
            }
        nettoie2(nord)
        nettoie2(sud)
        nettoie2(ouest)
        nettoie2(est)
    }

    def genere = {
            def decide(maFrontiere: Frontiere) = {
                val (autreCarre, autreFrontiere) = autre(maFrontiere)
                autreCarre match {
                    case Some(c) => if (frontieres.isEmpty) {
                        if (c hasFrontiere autreFrontiere) {
                            if (rnd > prolongeThreshold) frontieres = frontieres :+ maFrontiere
                        } else {
                            if (rnd > engendreThreshold) frontieres = frontieres :+ maFrontiere
                        }
                    } else if (!hasFrontiere(maFrontiere)) {
                        if (!c.hasFrontiere(autreFrontiere)) if (rnd > prolongeThreshold) frontieres = frontieres :+ maFrontiere
                    }
                    case _ =>
                }
            }
        decide(nord)
        decide(sud)
        decide(ouest)
        decide(est)
        l.myPrintln("  " + MyLog.tag(1) + toString)
        this
    }

    def rnd = tbx.rnd.nextInt(1000)

    def hasFrontiere(f: Frontiere) = !frontieres.filter(_ == f).isEmpty
    def notFull = {
        var sum = 0
        getUpCarre match {
            case Some(c) => if ((c hasFrontiere sud) || (hasFrontiere(nord))) sum += 1
            case _       => sum += 1
        }
        getDownCarre match {
            case Some(c) => if ((c hasFrontiere nord) || (hasFrontiere(sud))) sum += 1
            case _       => sum += 1
        }
        getRightCarre match {
            case Some(c) => if ((c hasFrontiere ouest) || (hasFrontiere(est))) sum += 1
            case _       => sum += 1
        }
        getLeftCarre match {
            case Some(c) => if ((c hasFrontiere est) || (hasFrontiere(ouest))) sum += 1
            case _       => sum += 1
        }
        l.myPrintln("  " + MyLog.tag(1) + toString + " " + sum)
        assert(sum <= 4)
        sum < 4
    }
    def isLast() = (rc == tbx.maxRC.moinsUn)
    def paint(g: Graphics2D) {
        val horiz = tbx.size.getWidth.toInt / (tbx.maxCol * 2)
        val vert = tbx.size.getHeight.toInt / (tbx.maxRow * 2)
        val x = tbx.origin.getWidth.toInt + (horiz * ((2 * col) + 1))
        val y = tbx.origin.getHeight.toInt + (vert * ((2 * row) + 1))

        g.setColor(Color.black)
        frontieres.foreach(_.paint(g, horiz, vert, x, y))
        //g.drawString(toString,x,y)
    }
    override def toString: String = {
        "Carre{" + rc + "}"
        //"Carre{("+row+","+col+"), "+frontieres+"}"
    }
    def getLeftCarre: Option[Carre] = {
        tbx.lc.find((cf: Carre) => cf.row == row && cf.col == col - 1)
    }
    def getUpCarre: Option[Carre] = {
        tbx.lc.find((cf: Carre) => cf.row == row - 1 && cf.col == col)
    }
    def getRightCarre: Option[Carre] = {
        tbx.lc.find((cf: Carre) => cf.row == row && cf.col == col + 1)
    }
    def getDownCarre: Option[Carre] = {
        tbx.lc.find((cf: Carre) => cf.row == row + 1 && cf.col == col)
    }

}

