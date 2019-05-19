package graphlayout

import java.awt.Graphics2D

import graphlayout.Tableaux._

import scala.collection.immutable.List


class Carre(val rc: RowCol) {

  def this(r: Int, c: Int) = this(new RowCol(r, c))

  val row = rc.r
  val col = rc.c
  var depotPheromones = List[Depot]()
  val XY = {
    val horiz = tbx.size.getWidth.toInt / (tbx.maxCol * 2)
    val vert = tbx.size.getHeight.toInt / (tbx.maxRow * 2)
    val x = tbx.origin.getWidth.toInt + (horiz * ((2 * col) + 1))
    val y = tbx.origin.getHeight.toInt + (vert * ((2 * row) + 1))
    (x.toDouble, y.toDouble)
  }
  val XYInt = (XY._1.toInt, XY._2.toInt)
  val fn = new FixedNode(XY)

  override def toString: String = "{r" + row + ", c" + col + "}"

  def updatePheromone(tribu: Tribu) = {
    var z = depotPheromones.find(_.tribu == tribu)
    if (z.isEmpty) {
      depotPheromones = depotPheromones :+ new Depot(Depot.valeurDepot, tribu)
    } else {
      z.head.update(Depot.valeurDepot)
    }
  }

  def hasPheromone(tribu: Tribu) = {
    var z = depotPheromones.find(_.tribu == tribu)
    if (z.isEmpty) {
      .0
    } else {
      z.head.ph
    }

  }

  def egal(c: Carre): Boolean = c.fn.x == fn.x & c.fn.y == fn.y

  def getDepot(tribu: Tribu) = depotPheromones.filter(_.tribu == tribu).map(_.ph).sum

  def isLast() = (rc == tbx.maxRC.moinsUn)

  def evapore = {
    depotPheromones.foreach(_.evapore)
    depotPheromones = depotPheromones.filter(_.ph > Depot.evapore).sortBy(_.ph).reverse.take(2) // le 3eme depot de pheromone est gomme par les 2 premiers
  }

  def paint(g: Graphics2D) {
    depotPheromones.sortBy(_.ph).reverse.foreach(d => {
      g.setColor(d.tribu.c.color)
      val radius = Math.max((d.ph / Depot.display).toInt, 2)
      g.drawRect(XY._1.toInt - 3, XY._2.toInt - 3, radius, radius)
      //g.drawString(depotPheromones.map(_.toString).mkString("[", ",", "]"), XY._1.toInt - 3, XY._2.toInt - 3)
    })
  }

  def getVoisins(lc: List[Carre]) = lc.filter((cf: Carre) => Math.abs(cf.row - row) + Math.abs(cf.col - col) == 1)

  def getVoisins = tbx.lc.filter((cf: Carre) => Math.abs(cf.row - row) + Math.abs(cf.col - col) == 1)

  def getLeftCarre = tbx.lc.find((cf: Carre) => cf.row == row && cf.col == col - 1).getOrElse(null)

  def getUpCarre = tbx.lc.find((cf: Carre) => cf.row == row - 1 && cf.col == col).getOrElse(null)

  def getRightCarre = tbx.lc.find((cf: Carre) => cf.row == row && cf.col == col + 1).getOrElse(null)

  def getDownCarre = tbx.lc.find((cf: Carre) => cf.row == row + 1 && cf.col == col).getOrElse(null)

}

