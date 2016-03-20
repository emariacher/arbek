package labyrinthe

import scala.util.Random
import java.awt.{Graphics, Graphics2D, Dimension, Color}
import java.util.Calendar
import kebra._
import kebra.MyLog._
import labyrinthe.LL._
import statlaby.AkkaJeton
import scala.collection.immutable._

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
  var seedIndex = 0
  var seed: Int = _
  var rnd: Random = _
  var countGenere = 0
  var countAvance = 0
  var fourmilieres = zp.ptype match {
    case PanelType.FOURMILIERES => List(new Fourmiliere(new RowCol(maxRow * 2 / 5, maxCol * 2 / 5), "violet",RaceFourmi.ROND),
      new Fourmiliere(new RowCol(maxRow * 3 / 5, maxCol * 3 / 5), "pourpre",RaceFourmi.RECTROND))
    case _ => List(new Fourmiliere(new RowCol(maxRow / 2, maxCol / 2), "violet",RaceFourmi.ROND))
  }
  var lc = List.empty[Carre]
  var lj = List(new Rouge("rouge", 80, fourmilieres.head), new Orange("orange", 75, fourmilieres.last),
    new VertFonce("vertFonce", 70, fourmilieres.last), new VertClair("vertClair", 65, fourmilieres.head),
    new Bleu("bleu", 60, fourmilieres.head), new BleuClair("bleuClair", 55, fourmilieres.last))

  zp.ptype match {
    case PanelType.FOURMILIERES => lj = lj :+ new Soldat("marron", 85, fourmilieres.head)
    case _ =>
  }
  lj = lj.sortBy(_.fourmiliere.hashCode)
  //lj = List(new Orange("orange", 75))
  val mj = lj.map((j: Jeton) => (j.couleur, j)).toMap
  val mjs = lj.map((j: Jeton) => (j.couleur, new StatJeton(j.couleur))).toMap
  var ltimestamps = List[Long](0)
  var t_startAkka: Calendar = _
  var nrOfWorkers = 4

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
          l.myErrPrintD("trouve le carre le plus actif")
          val carreLePlusActif = lc.filter(c => (math.abs(c.row - (maxRC.r / 2)) > (maxRC.r / rayonBloqueDiv)) ||
            (math.abs(c.col - (maxRC.c / 2)) > (maxRC.c / rayonBloqueDiv))).filter(!_.bloque).maxBy(_.calculePheromoneAll)
          l.myErrPrintln(" et bloque le [" + carreLePlusActif + "]")
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
        lj.filter(_.role == Role.SOLDAT).foreach(soldat => {
          lj.filter(_.statut != Pheromone.MORT).foreach(j => {
            if ((j.rc == soldat.rc) && (j.fourmiliere != soldat.fourmiliere)) {
              j.statut = Pheromone.MORT
              j.killed += 1
              l.myErrPrintln(MyLog.tagnt(1) + " " + soldat.toString + " a tue " + j.toString)
            }
          })
        })
      case StateMachine.reset => state = reset
      case StateMachine.termine =>
        if (graphic) {
          l.myErrPrintln(zp.lbl.text)
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
    lj.foreach(_.init)

    StateMachine.avance
  }

  def avance: StateMachine = {
    if (lj.map(_.avance).filter((sm: StateMachine) => sm == StateMachine.avance).isEmpty) {
      StateMachine.termine
    } else {
      StateMachine.avance
    }
  }

  def genere: StateMachine = {
    //l.myPrintln(MyLog.tag(1) + " genere")
    val notFulls = lc.filter(_.notFull == true).map(_.genere).filter(_.notFull == true)
    //l.myPrintln(MyLog.tag(1) + " genere " + notFulls.size)
    if (notFulls.isEmpty) StateMachine.nettoie else StateMachine.genere
  }

  def reset: StateMachine = {
    seed = getNextSeed
    rnd = new Random(seed)
    countGenere = 0
    countAvance = 0
    l.myPrintln(seed)
    lc = (0 to maxRow).map((row: Int) => (0 to maxCol).map((col: Int) => new Carre(row, col))).flatten.toList
    fourmilieres.foreach(_.cntmp = 0)
    mj.foreach((cj: (Couleur, Jeton)) => {
      // val cnt = cj._2.cnt
      val cnt = zp.ptype match {
        case PanelType.LABY => cj._2.cnt
        case _ => cj._2.aRameneDeLaJaffe
      }
      val js = mjs.getOrElse(cj._1, new StatJeton())
      if (cnt != 0) {
        cj._2.label.text = js.toString
      }
      js.update(cnt)
      cj._2.resetLocal
    })
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
    L.myPrint(".")
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
    AkkaJeton.goJetons(nrOfWorkers, lj)
    StateMachine.attend
  }

  def reset2: StateMachine = {
    seed = getNextSeed
    rnd = new Random(seed)
    countGenere = 0
    countAvance = 0
    l.myPrintln(seed)
    lc = (0 until maxRow).map((row: Int) => (0 until maxCol).map((col: Int) => new Carre(row, col))).flatten.toList
    lj.foreach(_.resetLocal)
    QA
    StateMachine.genere
  }

  def updateStats(lrjc: List[(Couleur, Int, Int)]) {
    lrjc.foreach((ci: (Couleur, Int, Int)) => {
      mjs.getOrElse(ci._1, new StatJeton()).update(ci._2)
    })
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

object CompareStatJeton extends Ordering[(Couleur, StatJeton)] {
  def compare(x: (Couleur, StatJeton), y: (Couleur, StatJeton)): Int = {
    if (y._2.max == x._2.max) y._2.min - x._2.min
    else y._2.max - x._2.max
  }
}
