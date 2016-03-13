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
  val label = new Label {
    text = couleur.toString
    foreground = couleur.color
  }
  val ventrePlein = 1000
  val ordreChoix: Circular
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
  var ventre = ventrePlein
  var indexBlocage = ventrePlein
  var aRameneDeLaJaffe = 0

  def this(s: String, rayon: Int) = this(new Couleur(s), rayon)

  def init = {
    setRowCol(tbx.maxRow / 2, tbx.maxCol / 2)
    visible = true
    statut = Pheronome.CHERCHE
    ventre = ventrePlein
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
        //l.myPrintln(MyLog.tagnt(1) + "NO: " + c)
        c.frontieres.filter((f: Frontiere) => f == sud || f == est)
      case _ => List.empty[Frontiere]
    }
    val SE = tbx.lc.find((c: Carre) => c.rc == rc) match {
      case Some(c) =>
        //l.myPrintln(MyLog.tagnt(1) + "SE: " + c)
        c.frontieres.filter((f: Frontiere) => f == nord || f == ouest)
      case _ => List.empty[Frontiere]
    }
    goNorth = NO.find(_ == est).isEmpty
    goSouth = SE.find(_ == ouest).isEmpty
    goWest = NO.find(_ == sud).isEmpty
    goEast = SE.find(_ == nord).isEmpty
    //l.myPrintln(MyLog.func(1) + " " + canGo)
  }

  def avance: StateMachine = {
    if (zp.ptype == PanelType.FOURMI) {
      ventre -= 1
    }
    if (cnt > zp.limit) {
      StateMachine.termine
    } else if (ventre < 1) {
      if (ventre == 0) {
        l.myErrPrintln(MyLog.tagnt(1) + " C est la faim! " + toString)
      }
      statut = Pheronome.MORT
      cnt = zp.limit + 1
      StateMachine.termine
    } else if ((next.r == 0) || (next.c == 0) || (next.r == (tbx.maxRow + 1)) || (next.c == (tbx.maxCol + 1))) {
      // a l'exterieur
      //l.myPrintln(MyLog.tagnt(1) + " " + toString)
      if (statut == Pheronome.CHERCHE) {
        if (zp.ptype == PanelType.LABY) {
          StateMachine.termine
        } else {
          statut = Pheronome.RAMENE
          aRameneDeLaJaffe += 1
          pasFini
        }
      } else {
        pasFini
      }
    } else if (rc.equals(new RowCol(tbx.maxRow / 2, tbx.maxCol / 2))) {
      // a la maison
      ventre = ventrePlein
      if (statut == Pheronome.REVIENS) {
        l.myErrPrintDln("******************************************************")
        l.myErrPrintDln("***************** Miracle! " + toString)
        l.myErrPrintDln("******************************************************")
        statut = Pheronome.CHERCHE
      }
      pasFini
    } else {
      pasFini
    }
  }

  def canGo = (if (goNorth) "N1" else "N0") + (if (goSouth) "S1" else "S0") + (if (goWest) "O1" else "O0") + (if (goEast) "E1" else "E0")

  def canGo2(z: RowCol) = {
    //l.myPrintDln(z + " " + toString)
    if (math.abs(z.r - row) == 1 || math.abs(z.c - col) == 1) {
      //l.myPrintDln((z.r == row - 1) + " " + (z.r == row + 1) + " " + (z.c == col - 1) + " " + (z.c == col + 1))
      if ((z.r == row - 1) && (goNorth)) {
        //l.myPrintDln(z + " " + toString)
        true
      } else if ((z.r == row + 1) && (goSouth)) {
        //l.myPrintDln(z + " " + toString)
        true
      } else if ((z.c == col - 1) && (goWest)) {
        //l.myPrintDln(z + " " + toString)
        true
      } else if ((z.c == col + 1) && (goEast)) {
        //l.myPrintDln(z + " " + toString)
        true
      } else {
        //l.myPrintDln(z + " " + toString)
        false
      }
    } else {
      false
    }
  }

  def paint(g: Graphics2D) {
    if (visible) {
      g.setColor(couleur.color)
      val horiz = tbx.size.getWidth.toInt / (tbx.maxCol * 2)
      val vert = tbx.size.getHeight.toInt / (tbx.maxRow * 2)
      var xg = tbx.origin.getWidth.toInt + (horiz * 2 * col)
      var yg = tbx.origin.getHeight.toInt + (vert * 2 * row)
      var rayonh = (tbx.size.getWidth.toInt * rayon) / (tbx.maxCol * 200)
      var rayonv = (tbx.size.getHeight.toInt * rayon) / (tbx.maxRow * 200)
      g.setColor(couleur.color)
      traces.foreach((rc: RowCol) => {
        xg = tbx.origin.getWidth.toInt + (horiz * 2 * rc.c)
        yg = tbx.origin.getHeight.toInt + (vert * 2 * rc.r)
        g.drawOval(xg - rayonh, yg - rayonv, rayonh * 2, rayonv * 2)
        //g.drawString(""+rc,xg,yg)
      })
      zp.ptype match {
        case PanelType.LABY =>
          g.fillOval(xg - rayonh, yg - rayonv, rayonh * 2, rayonv * 2)
        case PanelType.FOURMI =>
          rayonh = (tbx.size.getWidth.toInt * 60) / (tbx.maxCol * 200)
          rayonv = (tbx.size.getHeight.toInt * 60) / (tbx.maxRow * 200)
          lastDirection.f match {
            case NORD => g.fillOval(xg, yg - rayonv, rayonh, rayonv)
              g.fillOval(xg, yg, rayonh, rayonv * 2)
            case SUD => g.fillOval(xg, yg, rayonh, rayonv * 2)
              g.fillOval(xg, yg + rayonv, rayonh, rayonv)
            case OUEST => g.fillOval(xg - rayonh, yg, rayonh, rayonv)
              g.fillOval(xg, yg, rayonh * 2, rayonv)
            case EST => g.fillOval(xg, yg, rayonh * 2, rayonv)
              g.fillOval(xg + rayonh, yg, rayonh, rayonv)
          }
      }
      g.setColor(Color.black)
      statut match {
        case Pheronome.CHERCHE => g.drawString(" ", xg, yg)
        case Pheronome.RAMENE => g.drawString("+", xg, yg)
        case Pheronome.REVIENS => g.drawString("-", xg, yg)
        case Pheronome.MORT => g.drawString("*", xg, yg)
        case _ => g.drawString("?", xg, yg)
      }
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

  def pasFini: StateMachine = {
    //l.myPrintln(MyLog.tag(1) + " " + toString + " " + lastDirection)
    statut match {
      case Pheronome.REVIENS =>
        //l.myPrintln(MyLog.tag(1) + " " + toString + " " + lastDirection)
        next = firstStep
        /* verifie si tu ne retombes pas sur tes traces avant le blocage
        l.myPrintDln(toString + " " + indexBlocage)
        var possibles = findPossibles.filter(z => {
          traces.find(!_.equals(z)).isEmpty
        }).filter(z => {
          traces.indexOf(z) < indexBlocage
        }).sortBy(z => {
          traces.indexOf(z)
        })
        if (!possibles.isEmpty) {
          l.myErrPrintDln(toString + " " + indexBlocage + " " + possibles)
          next = possibles.head
        }*/
        var possibles = findPossibles.filter(z => {
          traces.find(_.equals(z)).isEmpty
        }).filter(z => {
          tbx.findCarre(z).calculePheromone > 0
        }).sortWith((a, b) => {
          tbx.findCarre(a).calculePheromone > tbx.findCarre(b).calculePheromone
        })

        if (!possibles.isEmpty) {
          //l.myPrintln(MyLog.func(1) + "possibles: " + possibles)
          next = new RowCol(possibles.head.r, possibles.head.c)
          // mais ne reviens pas sur tes pas!
          //l.myPrintln(MyLog.func(1) + "traces: " + traces)
          if (!traces.isEmpty) {
            //l.myPrintln(MyLog.func(1) + "traceslast: " + traces.last+" nextr "+next)
            if (next.equals(traces.last)) {
              possibles = possibles.drop(1)
              //l.myPrintln(MyLog.func(1) + "possibles: " + possibles)
              if (!possibles.isEmpty) {
                next = new RowCol(possibles.head.r, possibles.head.c)
              }
            }
          }
        }

        // sinon continue à essayer de sortir de revenir au centre du labyrinthe
        sortDuLabyrinthe
        //l.myPrintln(MyLog.tag(1) + couleur + " " + lastDirection + " " + rc + " -> " + next + " [" + traces.length + "] " + traces)
        traces = traces :+ rc
      case Pheronome.CHERCHE =>
        next = firstStep
        // essaye de trouver une case qui a le maximum de pheronomes[RAMENE]
        var possibles = findPossibles.filter(z => {
          traces.find(_.equals(z)).isEmpty
        }).filter(z => {
          tbx.findCarre(z).calculePheromone > 0
        }).sortWith((a, b) => {
          tbx.findCarre(a).calculePheromone > tbx.findCarre(b).calculePheromone
        })

        if (!possibles.isEmpty) {
          //l.myPrintln(MyLog.func(1) + "possibles: " + possibles)
          next = new RowCol(possibles.head.r, possibles.head.c)
          // mais ne reviens pas sur tes pas!
          //l.myPrintln(MyLog.func(1) + "traces: " + traces)
          if (!traces.isEmpty) {
            //l.myPrintln(MyLog.func(1) + "traceslast: " + traces.last+" nextr "+next)
            if (next.equals(traces.last)) {
              possibles = possibles.drop(1)
              //l.myPrintln(MyLog.func(1) + "possibles: " + possibles)
              if (!possibles.isEmpty) {
                next = new RowCol(possibles.head.r, possibles.head.c)
              }
            }
          }
        }

        // sinon continue à essayer de sortir du labyrinthe
        sortDuLabyrinthe
        traces = traces :+ rc
      case Pheronome.RAMENE => retourne
      case Pheronome.MORT => {
        // rien
      }
      case _ => l.myErrPrintDln(toString)
    }

    //l.myPrintln(MyLog.tag(1) + couleur + " " + lastDirection + " " + rc + " -> " + next + " [" + traces.length + "] " + traces)
    if (next.r == 888) {
      //l.myErrPrintDln(toString + " -> " + next + " [" + traces.length + "] " + traces)
      statut = Pheronome.MORT
    }
    if (zp.ptype == PanelType.FOURMI) {
      val zc = tbx.findCarre(rc)
      if (zc != null) {
        zc.depotPheronomes = zc.depotPheronomes :+ new Depot(tbx.countAvance, statut, this)
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

  def findPossibles: List[RowCol] = {
    var possibles = List.empty[RowCol]
    if (goNorth) {
      possibles = possibles :+ rc.haut
    }
    if (goSouth) {
      possibles = possibles :+ rc.bas
    }
    if (goEast) {
      possibles = possibles :+ rc.droite
    }
    if (goWest) {
      possibles = possibles :+ rc.gauche
    }
    possibles.filter(z => {
      z.r <= tbx.maxRow && z.c <= tbx.maxCol
    })
  }

  def sortDuLabyrinthe {
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
          while ((next.r == 888) && (cpt < zp.limit)) {
            // normalement c'est 4, mais avec le fou bleu, on ne sait jamais
            getNext(ordreChoix.nextf(ordreChoix), false, false)
            cpt += 1
          }
        }
      }
    }
  }

  def retourne() {

    if ((traces.isEmpty) || ((next.r == tbx.maxRow / 2) && (next.c == tbx.maxCol / 2))) {
      // tu es revenue au nid
      statut = Pheronome.CHERCHE;
      traces = List.empty[RowCol]
      /*} else {
        // retourne sur tes pas
        next = traces.last
        traces = traces.dropRight(1)
      }*/
    } else if (canGo2(traces.last)) {
      // retourne sur tes pas
      next = traces.last
      traces = traces.dropRight(1)
    } else {
      // bah maintenant il y a une barriere qui a ete creee
      l.myErrPrintDln("[" + toString + "] n'arrive plus a revenir sur mes traces car une barrierre vient d etre installee ")
      statut = Pheronome.REVIENS
      indexBlocage = traces.length - 1
      //traces = List.empty[RowCol]
    }
  }

  def raccourci() {
    /*next = traces.last
    traces = traces.dropRight(1)*/
    // liste les possibles et prends celui qui raccourci plus le chemin de retour
    if ((traces.isEmpty) || ((next.r == tbx.maxRow / 2) && (next.c == tbx.maxCol / 2))) {
      // tu es revenue au nid
      statut = Pheronome.CHERCHE;
      traces = List.empty[RowCol]
    } else {

      var possibles = List.empty[RowCol]
      if (goNorth) {
        possibles = possibles :+ rc.haut
      }
      if (goSouth) {
        possibles = possibles :+ rc.bas
      }
      if (goEast) {
        possibles = possibles :+ rc.droite
      }
      if (goWest) {
        possibles = possibles :+ rc.gauche
      }
      if (!possibles.isEmpty) {
        var zpossibles = possibles.map(z => (z, traces.indexOf(z))
        ).sortWith((a, b) => {
          a._2 > b._2
        })
        var znext = zpossibles.head
        var diff = traces.length - znext._2
        //l.myPrintln("------" + couleur + " " + diff + " zpossibles: " + zpossibles)
        next = znext._1
        traces = traces.dropRight(diff)
      }
    }
  }

  def getNext(f: Frontiere, newCarreOnly: Boolean, notBack: Boolean): RowCol = {
    next = new RowCol(888, 888)
    f.f match {
      case NORD => if (goNorth) next = rc.haut
      case SUD => if (goSouth) next = rc.bas
      case OUEST => if (goWest) next = rc.gauche
      case EST => if (goEast) next = rc.droite
    }
    //l.myPrintln(MyLog.tagnt(2) + couleur + "  *1*  (" + f + " nco: " + newCarreOnly + " nb: " + notBack + ") : " + rc + " -> " + next)
    if (newCarreOnly && !traces.filter(_ == next).isEmpty) {
      //l.myPrintln(MyLog.tagnt(2) + couleur + " **2** (" + f + " nco: " + newCarreOnly + ") : " + rc + " -> " + next)
      next = new RowCol(888, 888)
    }
    if (!traces.isEmpty) if (notBack && next == traces.last) {
      //l.myPrintln(MyLog.tagnt(2) + couleur + " **3** (" + f + " nb: " + notBack + ") : " + rc + " -> " + next)
      next = new RowCol(888, 888)
    }
    /*if(state==StateMachineJeton.cercleVicieux) {
    if(par2s.filter((c: (List[RowCol],Int)) => {})
    l.myPrintln(MyLog.tagnt(2)+couleur+" **4** ("+f+" cv: "+state+") : "+rc+" -> "+next)
    next = new RowCol(888,888)
    }*/
    //l.myPrintln(MyLog.tagnt(2) + couleur + "  *5*  (" + f + " nco: " + newCarreOnly + " nb: " + notBack + ") : " + rc + " -> " + next)
    next
  }

  override def toString = "{" + couleur + " " + rc + " " + canGo + " " + statut + " v" + ventre + "}"
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

class Circular(list: Seq[Frontiere], nextfIn: (Circular) => Frontiere, prevfIn: (Circular) => Frontiere) extends Iterator[Frontiere] {

  val elements = new Queue[Frontiere] ++= list
  var pos = 0

  def next = elements.head

  def nextf(c: Circular) = nextfIn(c)

  def prevf(c: Circular) = prevfIn(c)

  def hasNext = !elements.isEmpty

  def add(a: Frontiere): Unit = {
    elements += a
  }

  override def toString = elements.toString

}

case class StateMachineJeton private(state: String) {
  override def toString = "State_" + state
}

object StateMachineJeton {
  val normal = StateMachineJeton("normal")
  val cercleVicieux = StateMachineJeton("cercleVicieux")
}