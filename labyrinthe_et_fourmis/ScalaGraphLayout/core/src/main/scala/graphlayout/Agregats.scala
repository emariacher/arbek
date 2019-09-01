package graphlayout

import java.awt.Graphics2D

import graphlayout.Tableaux.tbx
import kebra.MyLog._

import scala.math.{max, min}
import scala.util.Random

class Agregats extends GraphAbstract {
  var MouseState = MouseStateMachine.reset
  var nearestNode: ANode = _

  var compteurDAgregatsFormes = 0
  var compteurDAgregatsFormesOld = 0
  var compteurDeCompteur = 0 // Le nombre d'agregats de taille egale a number evolue t'il?
  var agitation = Tribu.tribus.length * 5 // Les noeuds qui ne sont pas dans des agregats de taille egale a number, bougent-ils beaucoup ?
  var agitationMoyenne = Tribu.tribus.length * 5

  val ltribus = Tribu.tribus.map(t => (1 to ParametresPourFourmi.nombreDefourmisParTribu).toList.map(z => new ANode(t)))
  val lnodes = ltribus.flatten
  var ledges = ltribus.map(tl => tl.combinations(2)).flatten.map(c => new AEdge(c.head, c.last))
  val lzedges = ledges.map(_.getNodes.map(_.getID).sortBy(_.hashCode))
  var lnoedges = lnodes.map(_.getID).combinations(2).map(_.sortBy(_.hashCode)).toList.filter(e => lzedges.filter(_.mkString == e.mkString).isEmpty).map(c => {
    new AEdge(lnodes.filter(_.getID == c.head).head, lnodes.filter(_.getID == c.last).head)
  })

  var listeDesAgregats: List[(Tribu, List[List[ANode]])] = _
  var listeDesFourmilieres = List[Fourmiliere]()
  var lCoins: List[FixedNode] = _
  var ledgesJaffe = List[Edge]()
  var lnoedgesJaffe = List[Edge]()
  var ljaffe = List.empty[JNode]
  var lfourmi = List.empty[Fourmi]
  var ldeadNodes = List.empty[ANode]
  var listCarreAvecPheronome = List[Carre]()
  var cptOnVaArreter = 0
  var cptRun = 1

  /*MyLog.myPrintIt(ledges.mkString("\n -"))
  MyLog.myPrintIt(lnoedges.mkString("\n %"))*/

  def finDuRun: Boolean = {
    (listeDesFourmilieres.map(_.retoursFourmiliere.map(_._2).sum).sum / ParametresPourFourmi.nombreDefourmisParTribu > 10) |
      (lfourmi.count(_.state == FourmiStateMachine.mort) > (lfourmi.length * 3 / 4))
  }

  def travaille: StateMachine = {
    ldeadNodes = lfourmi.filter(_.state == FourmiStateMachine.mort).map(_.anode)
    lnoedges = lnoedges.filter(e => ldeadNodes.intersect(e.getNodes).isEmpty)
    if (tbx.ts % 1000 == 0) {
      myPrintln(tbx.ts + " " + listeDesFourmilieres.map(f =>
        f.tribu.c.couleur + " {" + f.retoursFourmiliere.map(_._2).sum + "}").mkString("\n  ", "\n  ", "\n  ")
        + lfourmi.count(_.state == FourmiStateMachine.mort) + " vs " + lfourmi.length)
    }
    if (lfourmi.isEmpty) {
      lfourmi = lnodes.map(n => new Fourmi(n))
      lfourmi.foreach(f => {
        f.direction = tbx.rnd.nextDouble() * Math.PI * 2
        f.jnode = ljaffe.filter(_.tribu == f.anode.tribu).head
      })
      //myPrintIt(lfourmi.find(_.anode.selected))
      listeDesFourmilieres = listeDesAgregats.map(lln => lln._2.map(ln => (lln._1, ln))).flatten.map(fm => {
        (fm._1, new FixedNode(fm._2.map(_.x).sum / fm._2.length, fm._2.map(_.y).sum / fm._2.length), fm._2)
      }).map(fm => new Fourmiliere(fm._1, fm._2, fm._3.map(an => lfourmi.filter(_.anode == an).head)))
      //myPrintIt(Console.BLUE + listeDesFourmilieres.mkString("[\n  ", "\n  ", "\n]") + Console.RESET)
      listeDesFourmilieres.foreach(_.faisSavoirAuxFourmisQuEllesFontPartieDeLaFourmiliere)
    } else {
      var filterond = 0
      if (!listCarreAvecPheronome.isEmpty) {
        filterond = listCarreAvecPheronome.map(_.compteurTourneEnRond).max - 10
      }
      listCarreAvecPheronome.foreach(_.evapore(filterond))
      listCarreAvecPheronome = tbx.lc.filter(!_.depotPheromones.isEmpty)
      lfourmi.foreach(_.doZeJob(listCarreAvecPheronome))
    }
    lnoedges.filter(_.getDist._1 < 50).foreach(_.ecarte) // occupe toi seulement d'ecarter potentiellement les fourmis si elles sont près l'une de l'autre

    var lcompteurState = scala.collection.mutable.Map[FourmiStateMachine, Int]()
    lfourmi.foreach(f => {
      f.lcompteurState.foreach(s => {
        lcompteurState(s._1) = lcompteurState.getOrElse(s._1, 0) + s._2
      })
    })
    tbx.zp.lbl.text = tbx.ts + " " + cptRun + " " + tbx.state + lcompteurState.mkString(" ", ",", " - ")
    if (finDuRun) {
      cptOnVaArreter += 1
      if (cptOnVaArreter > ParametresPourFourmi.limiteArreteLeRun) {
        resultat.log1(tbx.ltimestamps, listeDesFourmilieres.map(_.retoursFourmiliere.getOrElse(FourmiStateMachine.retourne, 0)), lcompteurState)
        cptRun += 1
        cptOnVaArreter = 0
        StateMachine.reset
      } else {
        if (cptOnVaArreter % 20 == 1) {
          myPrintln("On va arrêter! " + cptOnVaArreter + " " + listeDesFourmilieres.map(f =>
            f.tribu.c.couleur + " {" + f.retoursFourmiliere.map(_._2).sum + "}").mkString("\n  ", "\n  ", "\n  ")
            + lfourmi.count(_.state == FourmiStateMachine.mort) + " vs " + lfourmi.length)
        }
        tbx.zp.lbl.text = tbx.ts + " " + cptRun + " " + tbx.state
        StateMachine.onVaArreter
      }
    } else {
      StateMachine.croisiere
    }
  }

  var countOuEstLaJaffe = 0

  def ouestlajaffe: StateMachine = {
    tbx.zp.lbl.text = tbx.zp.lbl.text + "[" + compteurDAgregatsFormes + "," + compteurDeCompteur + "] " + agitationMoyenne
    if (ljaffe.isEmpty) {
      countOuEstLaJaffe = tbx.ts
      lCoins = List(new FixedNode(.0, .0), new FixedNode(.0, tbx.zp.hauteur), new FixedNode(tbx.zp.largeur, .0), new FixedNode(tbx.zp.largeur, tbx.zp.hauteur))
      ljaffe = ltribus.map(ln => {
        val x = ln.map(_.x).sum / ln.length
        val y = ln.map(_.y).sum / ln.length
        val lpe = lePlusEloigne(x, y, tbx.zp.largeur, tbx.zp.hauteur, 40)
        lePlusProche(lpe, lCoins, ln.head.tribu)
      })
      ledgesJaffe.foreach(_.attraction = 0) // a quoi ca sert, ce truc?
      lnoedgesJaffe = ljaffe.combinations(2).toList.map(c => {
        new Edge(c.head, c.last)
      }).filter(_.getDist._1 < 200)
      lnoedgesJaffe.foreach(_.repulsion = 0) // a quoi ca sert, ce truc?
      ljaffe.foreach(_.desempile(ljaffe ++ lCoins, tbx.zp.largeur, tbx.zp.hauteur, tbx.rnd))
      /*MyLog.myPrintIt(ljaffe.mkString("\n  "))
      MyLog.myPrintIt(ledgesJaffe.mkString("\n  "))
      MyLog.myPrintIt(lnoedgesJaffe.mkString("\n  "))*/
      lnodes.foreach(_.log = List[(Int, Int)]())
    } else {
      ledgesJaffe.foreach(_.getDist)
      ledgesJaffe.foreach(_.opTimize)
      lnoedgesJaffe.foreach(_.ecarte)
      ljaffe.foreach(_.remetsDansLeTableau(tbx.zp.largeur, tbx.zp.hauteur, 20))
      //MyLog.myPrintIt(ljaffe.mkString("\n  "))
    }
    if (tbx.ts - countOuEstLaJaffe > 100) {
      StateMachine.travaille
    } else {
      StateMachine.ouestlajaffe
    }
  }

  def rassemble: StateMachine = {
    ledges.foreach(_.getDist)
    ledges.foreach(_.rassemble)
    lnoedges.filter(_.getDist._1 < 500).foreach(_.ecarte) // occupe toi seulement d'ecarter potentiellement les noeuds si ils a moins de 500 l'un de l'autre
    lnodes.foreach(_.remetsDansLeTableau(tbx.zp.largeur, tbx.zp.hauteur, 10))
    if (nearestNode != null) {
      myPrintln("/%.2f".format(nearestNode.slidingAverageDeltax), "/%.2f".format(nearestNode.slidingAverageDeltay))
    }

    listeDesAgregats = Tribu.tribus.map(t => (t, listeAgregats(t, 100)))

    listeDesAgregats.foreach(a => {
      a._1.label.text = ", " + a._2.map(_.length).mkString("[", "/", "]")
    })

    compteurDAgregatsFormes = listeDesAgregats.count(a => a._2.length == 1)
    if (compteurDAgregatsFormes == compteurDAgregatsFormesOld) {
      compteurDeCompteur += 1
    } else {
      compteurDeCompteur = 0
    }
    compteurDAgregatsFormesOld = compteurDAgregatsFormes
    agitation = listeDesAgregats.map(lln => {
      lln._2.map(ln => {
        ln.head.toString2.toDouble
      }).sum
    }).sum.toInt
    agitationMoyenne = ((agitationMoyenne * ParametresPourFourmi.stabilisationRassemble) + agitation) / (ParametresPourFourmi.stabilisationRassemble + 1)
    tbx.zp.lbl.text = tbx.zp.lbl.text + "[" + compteurDAgregatsFormes + "," + compteurDeCompteur + "] " + agitationMoyenne
    if ((agitationMoyenne < (Tribu.tribus.length * 3)) & (compteurDeCompteur > ParametresPourFourmi.stabilisationRassemble)
      & (listeDesAgregats.count(_._2.length == 1) > Math.min(2, listeDesAgregats.length - 1))) {
      StateMachine.ouestlajaffe
    } else {
      if (compteurDeCompteur > ParametresPourFourmi.stabilisationRassemble) {
        ledges.foreach(e => e.attraction += 1)
        lnoedges.foreach(e => e.repulsion -= 1)
        compteurDeCompteur = 0
        if (!ledges.isEmpty & !lnoedges.isEmpty) {
          myPrintD(Console.GREEN + tbx.state + " Aidons la Nature! attraction = " + ledges.head.attraction
            + ", repulsion = " + lnoedges.head.repulsion + "\n" + Console.RESET)
        }
      }
      StateMachine.rassemble
    }
  }

  def listeAgregats(tribu: Tribu, radius: Int): List[List[ANode]] = {
    var ltedges = getEdges(tribu).filter(e => e.dist._1 < radius)
    var ltnodesPasEncoreTraites = getNodes(tribu)
    //MyLog.myPrintln(tribu.c.couleur, getNodes(tribu).length, getNodes(tribu).map(n => "[%.0f".format(n.x) + ",%.0f]".format(n.y)).mkString(", "))
    getNodes(tribu).map(n => {
      //MyLog.myPrintIt(tribu.c.couleur, "[%.0f".format(n.x) + ",%.0f]".format(n.y), lznodes.mkString("~ "))
      if (!ltnodesPasEncoreTraites.filter(_ == n).isEmpty) {
        var ledgesLinkingZeNode = getEdges(ltedges, n)
        if (!ledgesLinkingZeNode.isEmpty) {
          //MyLog.myPrintIt(tribu.c.couleur, "[%.0f".format(n.x) + ",%.0f]".format(n.y), zorg.mkString(", "))
          ltnodesPasEncoreTraites = ltnodesPasEncoreTraites.filter(n2 => ledgesLinkingZeNode.map(e => e.getNodes).flatten.filter(_ == n2).isEmpty)
          ledgesLinkingZeNode.map(e => e.getNodes).flatten.distinct
        } else {
          List(n)
        }
      } else {
        List[ANode]()
      }
    }).filter(!_.isEmpty).distinct
  }

  def reset: StateMachine = {
    listeDesAgregats = List.empty[(Tribu, List[List[ANode]])]
    listeDesFourmilieres = List[Fourmiliere]()
    lCoins = List.empty[FixedNode]
    ledgesJaffe = List[Edge]()
    lnoedgesJaffe = List[Edge]()
    ljaffe = List.empty[JNode]
    lfourmi = List.empty[Fourmi]
    listCarreAvecPheronome = List[Carre]()
    tbx.ltimestamps = scala.collection.mutable.Map[StateMachine, Int]()
    lnoedges = lnodes.map(_.getID).combinations(2).map(_.sortBy(_.hashCode)).toList.filter(e => lzedges.filter(_.mkString == e.mkString).isEmpty).map(c => {
      new AEdge(lnodes.filter(_.getID == c.head).head, lnodes.filter(_.getID == c.last).head)
    })

    tbx.seed = tbx.getNextSeed
    tbx.rnd = new Random(tbx.seed)
    lnodes.foreach(n => {
      n.x = tbx.rnd.nextDouble() * tbx.zp.largeur
      n.y = tbx.rnd.nextDouble() * tbx.zp.hauteur
    })
    lnodes.foreach(_.desempile(lnodes, tbx.zp.largeur, tbx.zp.hauteur, tbx.rnd)) // si jamais
    ledges.foreach(_.len = 10)
    tbx.lc = (0 to tbx.maxRow).map((row: Int) => (0 to tbx.maxCol).map((col: Int) => new Carre(row, col))).flatten.toList
    StateMachine.rassemble
  }

  def lePlusEloigne(x: Double, y: Double, largeur: Int, hauteur: Int, border: Int) = {
    val lpex = if (largeur - x > x) {
      largeur - border
    } else {
      border
    }
    val lpey = if (hauteur - y > y) {
      hauteur - border
    } else {
      border
    }
    (lpex.toDouble, lpey.toDouble)
  }

  def lePlusProche(coord: (Double, Double), ln: List[Node], tribu: Tribu) = {
    val coin = ln.sortBy(n => {
      val e = new Edge(n, new FixedNode(coord))
      e.getDist
    }).head
    val jn = new JNode(tribu)
    //jn.update(coord._1, coord._2, .0)
    //jn.update(coord._1 + tbx.rnd.nextInt(ln.length), coord._2 + tbx.rnd.nextInt(ln.length), .0)
    val e = new Edge(coin, jn)
    e.len = 100
    ledgesJaffe = ledgesJaffe :+ e
    jn
  }

  def doZeMouseJob(mouse: (String, Int, Int)): Unit = {
    //MyLog.myPrintIt(MouseState, mouse)
    MouseState match {
      case MouseStateMachine.drag =>
        mouse._1 match {
          case "MouseR" =>
            //MyLog.myPrintIt(mouse)
            //nearestNode = null
            MouseState = MouseStateMachine.reset
          case "MouseD" =>
            nearestNode.x = mouse._2
            nearestNode.y = mouse._3
          //MyLog.myPrintIt(mouse, nearestNode)
          case _ => //MyLog.myPrintIt(mouse)
        }
      case MouseStateMachine.reset =>
        mouse._1 match {
          case "MouseP" =>
            val nearestFNode = lnodes.map(n => (n, n.pasLoin(mouse._2.toDouble, mouse._3.toDouble))).sortBy(_._2).head
            if (nearestFNode._2 < 80) {
              if (nearestNode != null) {
                nearestNode.selected = false
              }
              nearestNode = nearestFNode._1
              nearestNode.selected = true
              myPrintIt(mouse, nearestNode)
              MouseState = MouseStateMachine.drag
            } else {
              myPrintIt(lnodes.map(n => (n, n.pasLoin(mouse._2.toDouble, mouse._3.toDouble))).sortBy(_._2).mkString)
            }
          case _ => //MyLog.myPrintIt(mouse)
        }
      case _ =>
    }
  }

  override def doZeSliderJob(slider: (String, Int)): Unit = {
    //MyLog.myPrintIt(slider._1, slider._2)
    slider._1 match {
      case "Repulsion" =>
        if (nearestNode != null) {
          lnoedges.filter(e => !e.getNodes.filter(n => n.tribu == nearestNode.tribu).isEmpty).foreach(e => e.repulsion = slider._2 * 20)
        } else {
          lnoedges.foreach(e => e.repulsion = slider._2 * 20)
        }
      case "Attraction" =>
        if (nearestNode != null) {
          ledges.filter(e => !e.getNodes.filter(n => n.tribu == nearestNode.tribu).isEmpty).foreach(e => e.attraction = max(slider._2 / 10, 1))
        } else {
          ledges.foreach(e => e.attraction = max(slider._2 / 10, 1))
        }
      case _ =>
        slider_timeout = min(max(1, (slider._2 * slider._2) / 100), 5000)
        //MyLog.myPrintIt(slider._1, slider._2, slider_timeout)
        ZePanel.zp.pause = (slider._2 == 0)
        ZePanel.zp.run = !ZePanel.zp.pause
        ZePanel.zp.step = false
    }
  }

  def getEdges(tribu: Tribu) = ledges.filter(e => !e.getNodes.filter(n => n.tribu == tribu).isEmpty)

  def getEdges(l: List[AEdge], node: ANode) = l.filter(e => !e.getNodes.filter(n => node == n).isEmpty)

  def getEdges(node: ANode) = ledges.filter(e => !e.getNodes.filter(n => node == n).isEmpty)

  def getNodes(tribu: Tribu) = lnodes.filter(n => n.tribu == tribu)

  def dispersion(tribu: Tribu): Int = {
    (getEdges(tribu).map(_.dist._1).sum / ParametresPourFourmi.nombreDefourmisParTribu).toInt
  }

  def triggerTraceNotAlreadyActivated = lfourmi.filter(_.anode.selected).isEmpty

  def paint(g: Graphics2D): Unit = {
    tbx.lc.foreach(_.paint(g))
    tbx.state match {
      case StateMachine.rassemble =>
        lnodes.foreach(_.paint(g))
      case StateMachine.ouestlajaffe =>
        lnodes.foreach(_.paint(g))
      case StateMachine.travaille =>
        lfourmi.foreach(_.paint(g))
      case StateMachine.croisiere =>
        lfourmi.foreach(_.paint(g))
      case StateMachine.onVaArreter =>
        lfourmi.foreach(_.paint(g))
      case _ =>
    }
    ljaffe.foreach(_.paint(g))
    listeDesFourmilieres.foreach(_.paint(g))
  }
}
