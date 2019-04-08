package graphlayout

import java.awt.Dimension
import java.util.Calendar

import graphlayout.Tableaux.tbx
import kebra._

import scala.collection.immutable._
import scala.util.Random

object Tableaux {
  var tbx: Tableaux = _

  def newTbx(zp: ZePanel, maxRC: RowCol, size: Dimension, origin: Dimension) {
    tbx = new Tableaux(zp, maxRC, size, origin)
  }
}

class Tableaux(val zp: ZePanel, val maxRC: RowCol, val size: Dimension, val origin: Dimension) {
  val maxRow = maxRC.r
  val maxCol = maxRC.c
  //val seeds = List(0, -1258712602, -2003116831, -2000188942, -2003116831, -1172155944) // valeurs interessantes pour un 10X10
  val seeds = (0 until 11).toList :+ (-1171074276)
  // valeurs interessantes pour un 20X20
  val maxWorkers = 5
  val mperfs = scala.collection.mutable.Map.empty[Int, List[Double]]
  var state = StateMachine.reset
  var oldstate = StateMachine.reset
  var MouseState = MouseStateMachine.reset
  var nearestNode: GNode = _
  var seedIndex = 0
  var seed: Int = _
  var rnd: Random = _
  var countGenere = 0
  var countAvance = 0
  var fourmilieres = zp.ptype match {
    case PanelType.FOURMILIERES => List(new Fourmiliere(new RowCol(maxRow * 2 / 5, maxCol * 2 / 5), "violet", RaceFourmi.ROND),
      new Fourmiliere(new RowCol(maxRow * 3 / 5, maxCol * 3 / 5), "pourpre", RaceFourmi.RECTROND))
    case _ => List(new Fourmiliere(new RowCol(maxRow / 2, maxCol / 2), "violet", RaceFourmi.ROND))
  }
  var lc = List.empty[Carre]
  val sinput = "un-deux, deux-trois, trois-un, un-quatre, cinq-quatre"
  val ledges1 = java.util.regex.Pattern.compile(",").split(sinput).map(t => {
    val ln = java.util.regex.Pattern.compile("-").split(t.trim).map(new GNode(_))
    new Edge(ln.head, ln.last)
  })
  val lnodes = ledges1.map(_.getNodes).flatten.distinct
  val ledges = ledges1.map(e => {
    new Edge(lnodes.filter(_ == e.from).head, lnodes.filter(_ == e.to).head)
  })
  val lallpossibleedges = lnodes.map(_.lbl).combinations(2).map(_.sortBy(_.hashCode)).toList
  val lzedges = ledges.map(_.getNodes.map(_.lbl).sortBy(_.hashCode))
  val lnoedges = lallpossibleedges.filter(e => lzedges.filter(_.mkString == e.mkString).isEmpty).map(c => {
    new Edge(lnodes.filter(_.lbl == c.head).head, lnodes.filter(_.lbl == c.last).head)
  })
  lnoedges.foreach(_.len = 500)
  MyLog.myPrintIt(lnoedges.mkString("+"))

  MyLog.myPrintIt(sinput, "[", lnodes.mkString("!"), "][", ledges.mkString("/"), "]")

  var ltimestamps = List[Long](0)
  var t_startAkka: Calendar = _
  var nrOfWorkers = 4

  def doZeMouseJob(mouse: (String, Int, Int)): Unit = {
    //MyLog.myPrintIt(MouseState, mouse)
    MouseState match {
      case MouseStateMachine.drag =>
        mouse._1 match {
          case "MouseR" =>
            //MyLog.myPrintIt(mouse)
            nearestNode = null
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
            val nearestFNode = tbx.lnodes.map(n => (n, n.pasLoin(mouse._2, mouse._3))).sortBy(_._2).head
            if (nearestFNode._2 < 80) {
              nearestNode = nearestFNode._1
              MyLog.myPrintIt(mouse, nearestNode)
              MouseState = MouseStateMachine.drag
            } else {
              MyLog.myPrintIt(tbx.lnodes.map(n => (n, n.pasLoin(mouse._2, mouse._3))).sortBy(_._2).mkString)
            }
          case _ => //MyLog.myPrintIt(mouse)
        }
      case _ =>
    }
  }

  def doZeJob(command: String, graphic: Boolean) {
    //l.myPrintDln(state + " cg: " + countGenere + " ca: " + countAvance + " " + command)
    if (graphic) {
      zp.lbl.text = "Seed: " + seed + ", CountSteps: " + countGenere
    }
    state match {
      case StateMachine.genere =>
        state = genere
        countGenere += 1
      case StateMachine.nettoie => state = nettoie
      case StateMachine.avance =>
        state = avance
        countAvance += 1
        if (command == "bloque") {
          var rayonBloqueDiv = 5
          LL.l.myErrPrintD("trouve le carre le plus actif")
          val carreLePlusActif = lc.filter(c => (math.abs(c.row - (maxRC.r / 2)) > (maxRC.r / rayonBloqueDiv)) ||
            (math.abs(c.col - (maxRC.c / 2)) > (maxRC.c / rayonBloqueDiv))).filter(!_.bloque).maxBy(_.calculePheromoneAll)
          LL.l.myErrPrintln(" et bloque le [" + carreLePlusActif + "]")
          carreLePlusActif.frontieres = List(FrontiereV.nord, FrontiereV.est, FrontiereV.sud, FrontiereV.ouest)
          carreLePlusActif.getUpCarre match {
            case Some(c) => c.frontieres = c.frontieres :+ FrontiereV.sud
              c.frontieres = (new ListSet[Frontiere]() ++ c.frontieres).toList
            case _ =>
          }
          carreLePlusActif.getRightCarre match {
            case Some(c) => c.frontieres = c.frontieres :+ FrontiereV.ouest
              c.frontieres = (new ListSet[Frontiere]() ++ c.frontieres).toList
            case _ =>
          }
          carreLePlusActif.getDownCarre match {
            case Some(c) => c.frontieres = c.frontieres :+ FrontiereV.nord
              c.frontieres = (new ListSet[Frontiere]() ++ c.frontieres).toList
            case _ =>
          }
          carreLePlusActif.getLeftCarre match {
            case Some(c) => c.frontieres = c.frontieres :+ FrontiereV.est
              c.frontieres = (new ListSet[Frontiere]() ++ c.frontieres).toList
            case _ =>
          }
          carreLePlusActif.bloque = true
        }
      case StateMachine.reset => state = reset
      case StateMachine.termine =>
        if (graphic) {
          LL.l.myErrPrintln(zp.lbl.text)
          if ((command == "step") || zp.run) {
            zp.pause = false
          } else {
            zp.pause = true
          }
        }
        state = StateMachine.reset
      case _ =>
    }
  }

  def nettoie: StateMachine = {
    lc.foreach(_.nettoie)

    StateMachine.avance
  }

  def avance: StateMachine = {
    StateMachine.avance
  }

  def genere: StateMachine = {
    //l.myPrintln(MyLog.tag(1) + " genere")
    val notFulls = lc.filter(_.notFull == true).map(_.genere).filter(_.notFull == true)
    //l.myPrintln(MyLog.tag(1) + " genere " + notFulls.size)
    ledges.foreach(_.opTimize(rnd))
    lnoedges.foreach(_.opTimize(rnd))
    //if (notFulls.isEmpty) StateMachine.nettoie else StateMachine.genere
    StateMachine.genere
  }

  def reset: StateMachine = {
    seed = getNextSeed
    rnd = new Random(seed)
    lnodes.foreach(n => {
      n.x = rnd.nextDouble() * zp.largeur
      n.y = rnd.nextDouble() * zp.hauteur
    })
    ledges.foreach(_.len = 100 + rnd.nextDouble() * 400)
    countGenere = 0
    countAvance = 0
    LL.l.myPrintln(seed)
    lc = (0 to maxRow).map((row: Int) => (0 to maxCol).map((col: Int) => new Carre(row, col))).flatten.toList
    fourmilieres.foreach(_.cntmp = 0)
    QA
    StateMachine.genere
  }

  def getNextSeed: Int = {
    seedIndex += 1
    if (seedIndex < seeds.size) {
      seeds.apply(seedIndex - 1)
    } else {
      Calendar.getInstance.getTimeInMillis.toInt
    }
  }

  def QA {
    /*if((seed==10)&(maxRC.r==20)&(maxRC.c==20)&(StatJeton.limit==200)) {
                                                              val ljs = mjs.toList.sorted(CompareStatJeton)
                                                                      L.myErrPrintDln(""+ljs)
                                                                      myAssert(ljs.head._2.min==43)
                                                                      myAssert(ljs.last._2.max==90)
                                                                      myAssert(ljs.last._2.couleur.toString=="turquoise")
                                                                      myAssert(ljs.head._2.cntDepasse==0)
                                                                      myAssert(ljs.apply(1)._2.cntDepasse==2)
                                                                      myAssert(ljs.apply(2)._2.cnt==10)
                                                                      //exit
                                                          } */
    LL.l.myPrint(".")
  }

  def doZeJob2 {
    //if(oldstate!=state) L.myPrintDln(" state: "+state)
    oldstate = state
    state match {
      case StateMachine.genere =>
        state = genere
        countGenere += 1
      case StateMachine.nettoie => state = nettoie
      case StateMachine.avance => state = avance2
      case StateMachine.attend =>
      case StateMachine.reset => state = reset2
      case StateMachine.termine => state = StateMachine.reset
      case _ =>
    }
  }

  def avance2: StateMachine = {
    if (seedIndex > 10) {
      nrOfWorkers = rnd.nextInt(maxWorkers) + 5
      t_startAkka = Calendar.getInstance();
    }
    AkkaJeton.goJetons(nrOfWorkers)
    StateMachine.attend
  }

  def reset2: StateMachine = {
    seed = getNextSeed
    rnd = new Random(seed)
    countGenere = 0
    countAvance = 0
    LL.l.myPrintln(seed)
    lc = (0 until maxRow).map((row: Int) => (0 until maxCol).map((col: Int) => new Carre(row, col))).flatten.toList
    QA
    StateMachine.genere
  }

  def updateStats(lrjc: List[(Couleur, Int, Int)]) {
    if (seedIndex > 10) {
      val ljArrivesAuBout = lrjc.filter((ci: (Couleur, Int, Int)) => ci._2 > 0 && ci._2 < zp.limit).map((ci: (Couleur, Int, Int)) => ci._2)
      val timeStamp = MyLog.timeStamp(t_startAkka)
      val perf = ljArrivesAuBout.sum * 1000.0 / (nrOfWorkers * ljArrivesAuBout.length * timeStamp)
      mperfs(nrOfWorkers) = mperfs.getOrElse(nrOfWorkers, List.empty[Double]) :+ perf
      //L.myPrintDln(seedIndex+" "+mperfs)
    }
    state = StateMachine.termine
  }

  def findCarre(rc2f: RowCol) = {
    var z = lc.find(_.rc.equals(rc2f))
    if (z.isEmpty) {
      null
    } else {
      z.head
    }
  }
}

case class StateMachine private(state: String) {
  override def toString = "State_" + state
}

object StateMachine {
  val genere = StateMachine("genere")
  val getJetons = StateMachine("getJetons")
  val nettoie = StateMachine("nettoie")
  val avance = StateMachine("avance")
  val attend = StateMachine("attend")
  val termine = StateMachine("termine")
  val reset = StateMachine("reset")
}

case class MouseStateMachine private(state: String) {
  override def toString = "MouseState_" + state
}

object MouseStateMachine {
  val drag = MouseStateMachine("drag")
  val reset = MouseStateMachine("reset")
}

object CompareStatJeton extends Ordering[(Couleur, StatJeton)] {
  def compare(x: (Couleur, StatJeton), y: (Couleur, StatJeton)): Int = {
    if (y._2.max == x._2.max) y._2.min - x._2.min
    else y._2.max - x._2.max
  }
}
