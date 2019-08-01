package graphlayout

import java.awt.{Color, Graphics2D}

import graphlayout.Tableaux._
import kebra.MyLog._

import scala.collection.immutable.List

class Carre(val rc: RowCol) {

  def this(r: Int, c: Int) = this(new RowCol(r, c))

  val row = rc.r
  val col = rc.c
  var depotPheromones = scala.collection.mutable.Map[Tribu, Double]()
  val XY = {
    val horiz = tbx.size.getWidth.toInt / (tbx.maxCol * 2)
    val vert = tbx.size.getHeight.toInt / (tbx.maxRow * 2)
    val x = tbx.origin.getWidth.toInt + (horiz * ((2 * col) + 1))
    val y = tbx.origin.getHeight.toInt + (vert * ((2 * row) + 1))
    (x.toDouble, y.toDouble)
  }
  val XYInt = (XY._1.toInt, XY._2.toInt)
  val fn = new FixedNode(XY)
  var compteurTourneEnRond = 0
  var mindir = 100.0
  var maxdir = -100.0

  override def toString: String = "{r" + row + ", c" + col + "}"

  def dist(c: Carre) = fn.dist(c.fn)

  def milieu(c: Carre): Carre = tbx.findCarre(fn.x + ((c.fn.x - fn.x) / 2), fn.y + ((c.fn.y - fn.y) / 2))

  def updatePheromone(tribu: Tribu) = depotPheromones(tribu) = depotPheromones.getOrElse(tribu, .0) + Depot.valeurDepot

  def hasPheromone(tribu: Tribu) = depotPheromones.getOrElse(tribu, .0)

  def egal(c: Carre): Boolean = c.fn.egal(fn)

  def equivalent(c: Carre): Boolean = row == c.row & col == c.col

  var compteurTourneEnRondLimit = 0

  def evapore(filterond: Int) = {
    depotPheromones = depotPheromones.map(depot => (depot._1, depot._2 * Depot.evaporation))
    val moyennePheromones = depotPheromones.values.sum / depotPheromones.toList.length
    depotPheromones = depotPheromones.filter(_._2 > Depot.evapore).filter(_._2 > moyennePheromones - 0.01) // les gros depots de pheromone gomment les petits
    compteurTourneEnRondLimit = filterond
  }

  def paint(g: Graphics2D) {
    depotPheromones.foreach(d => {
      g.setColor(d._1.c.color)
      val radius = Math.max((d._2 / Depot.display).toInt, 2)
      g.drawRect(XY._1.toInt - 3, XY._2.toInt - 3, radius, radius)
      //g.drawString(depotPheromones.map(_.toString).mkString("[", ",", "]"), XY._1.toInt - 3, XY._2.toInt - 3)
    })
    if ((compteurTourneEnRond > compteurTourneEnRondLimit) & (math.abs(mindir - maxdir) < 15)) {
      //g.fillRect(XY._1.toInt - 3, XY._2.toInt - 3, compteurTourneEnRond, compteurTourneEnRond)
      if (mindir != maxdir) {
        g.drawString(compteurTourneEnRond + " m%.2f M%.2f".format(mindir, maxdir), XY._1.toInt - 3, XY._2.toInt - 3)
      } else {
        g.setColor(Color.black)
        g.drawString(compteurTourneEnRond + " %.2f".format(mindir), XY._1.toInt - 3, XY._2.toInt - 3)
      }
    }
  }

  def getVoisins(lc: List[Carre]) = lc.filter((cf: Carre) => Math.abs(cf.row - row) + Math.abs(cf.col - col) == 1)

  def get9Voisins = tbx.lc.filter((cf: Carre) => Math.abs(cf.row - row) <= 1 & Math.abs(cf.col - col) <= 1)

  def get8Voisins = get9Voisins.filter((cf: Carre) => Math.abs(cf.row - row) + Math.abs(cf.col - col) != 0)

  def getVoisins = get9Voisins.filter((cf: Carre) => Math.abs(cf.row - row) + Math.abs(cf.col - col) == 1)

  def getLeftCarre = tbx.lc.find((cf: Carre) => cf.row == row && cf.col == col - 1).getOrElse(null)

  def getUpCarre = tbx.lc.find((cf: Carre) => cf.row == row - 1 && cf.col == col).getOrElse(null)

  def getRightCarre = tbx.lc.find((cf: Carre) => cf.row == row && cf.col == col + 1).getOrElse(null)

  def getDownCarre = tbx.lc.find((cf: Carre) => cf.row == row + 1 && cf.col == col).getOrElse(null)

}

class PatternCarre(var lc: List[Carre], var carreAEnlever: Int) {
  def similaire(lcac: List[Carre]): Boolean = {
    myAssert2(lc.length, lcac.length)
    val rowmin = lcac.map(_.row).min
    val colmin = lcac.map(_.col).min
    val lcacnormalise = lcac.map(c => new Carre(c.row - rowmin, c.col - colmin))
    val lcacnormaliseperpendiculaire = lcac.map(c => new Carre(c.col - colmin, c.row - rowmin))
    lcacnormalise.filter(c => !lc.contains(c)).isEmpty | lcacnormaliseperpendiculaire.filter(c => !lc.contains(c)).isEmpty
  }
}

