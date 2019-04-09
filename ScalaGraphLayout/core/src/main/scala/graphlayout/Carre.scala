package graphlayout

import java.awt.{Color, Graphics2D}

import graphlayout.FrontiereV._
import graphlayout.Tableaux._

import scala.collection.immutable.List

class Carre(val rc: RowCol) {
  def this(r: Int, c: Int) = this(new RowCol(r, c))

  val row = rc.r
  val col = rc.c
  val engendreThreshold = 998
  val prolongeThreshold = 550
  var frontieres = List[Frontiere]()
  var frontieresColor = scala.collection.mutable.Map.empty[Frontiere, Color]
  var depotPheromones = List[Depot]()
  var bloque = false

  def autre(maFrontiere: Frontiere) = {
    maFrontiere.f match {
      case NORD => (getUpCarre, sud)
      case SUD => (getDownCarre, nord)
      case EST => (getRightCarre, ouest)
      case OUEST => (getLeftCarre, est)
    }
  }

  def nettoie {
    def nettoie2(maFrontiere: Frontiere) = {
      if (hasFrontiere(maFrontiere)) {
        val (autreCarre, autreFrontiere) = autre(maFrontiere)
        autreCarre match {
          case Some(c) => if (!c.hasFrontiere(autreFrontiere)) frontieres = frontieres.filter(_ != maFrontiere)
          case _ => frontieres = frontieres.filter(_ != maFrontiere)
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
            if (rnd > prolongeThreshold) {
              frontieres = frontieres :+ maFrontiere
              frontieresColor(maFrontiere) = c.frontieresColor(autreFrontiere)
            }
          } else {
            if (rnd > engendreThreshold) {
              frontieres = frontieres :+ maFrontiere
              frontieresColor(maFrontiere) = new Color(tbx.rnd.nextInt(0xc0ffff))
            }
          }
        } else if (!hasFrontiere(maFrontiere)) {
          if (!c.hasFrontiere(autreFrontiere)) {
            if (rnd > prolongeThreshold) {
              frontieres = frontieres :+ maFrontiere
              if(frontieresColor.size==0) {
                frontieresColor(maFrontiere) = new Color(tbx.rnd.nextInt(0xC0ffff))
              } else {
                frontieresColor(maFrontiere) = frontieresColor.toList.map(_._2).head
              }
            }
          }
        }
        case _ =>
      }
    }
    decide(nord)
    decide(sud)
    decide(ouest)
    decide(est)
    //l.myPrintln("  " + MyLog.tag(1) + toString)
    this
  }

  def rnd = tbx.rnd.nextInt(1000)

  def hasFrontiere(f: Frontiere) = !frontieres.filter(_ == f).isEmpty
  def getFrontiere(f: Frontiere) = frontieres.filter(_ == f).head

  def notFull = {
    var sum = 0
    getUpCarre match {
      case Some(c) => if ((c hasFrontiere sud) || (hasFrontiere(nord))) sum += 1
      case _ => sum += 1
    }
    getDownCarre match {
      case Some(c) => if ((c hasFrontiere nord) || (hasFrontiere(sud))) sum += 1
      case _ => sum += 1
    }
    getRightCarre match {
      case Some(c) => if ((c hasFrontiere ouest) || (hasFrontiere(est))) sum += 1
      case _ => sum += 1
    }
    getLeftCarre match {
      case Some(c) => if ((c hasFrontiere est) || (hasFrontiere(ouest))) sum += 1
      case _ => sum += 1
    }
    //l.myPrintln("  " + MyLog.tag(1) + toString + " " + sum)
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
    if (bloque) {
      g.fillOval(x-3, y-3, 6, 6)
    }

    tbx.zp.ptype match {
      case PanelType.LABY => frontieres.foreach(f => f.paint(g, horiz, vert, x, y,frontieresColor(f)))
      case _ => frontieres.foreach(f => f.paint(g, horiz, vert, x, y))
    }

    g.setColor(Color.black)
    if (depotPheromones.length > 0) {
      //g.drawString("" + calculePheromone, x - horiz, y - vert)
      tbx.fourmilieres.zipWithIndex.map(fi =>{
        g.setColor(fi._1.couleur.color)
        g.fillOval(x - horiz+(fi._2*2), y - vert+(fi._2*2), math.log(calculePheromone(fi._1)).toInt+2, math.log(calculePheromone(fi._1)).toInt+2)
      })
      //g.setColor(Color.gray)
      //g.fillOval(x - horiz, y - vert, math.log(calculePheromone).toInt+2, math.log(calculePheromone).toInt+2)
    }
    //g.drawString(toString,x,y)
  }

  override def toString: String = {
    "{" + rc + " " + tbx.fourmilieres.map(f => f.toString+"("+calculePheromone(f)+")" ) + "}"
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

  def calculePheromone(fourmiliere: Fourmiliere): Int = {
    depotPheromones.filter(_.ph == Pheromone.RAMENE).filter(_.fourmi.fourmiliere == fourmiliere).map(d => 400.0 / (1.0 + (tbx.countAvance - d.ts))).sum.toInt
  }

  def calculePheromoneDesEnnemies(fourmiliere: Fourmiliere): Int = {
    depotPheromones.filter(_.fourmi.fourmiliere != fourmiliere).map(d => 400.0 / (1.0 + (tbx.countAvance - d.ts))).sum.toInt
  }

  def calculePheromoneAll: Int = {
    depotPheromones.filter(_.ph == Pheromone.RAMENE).map(d => 400.0 / (1.0 + (tbx.countAvance - d.ts))).sum.toInt
  }
}

