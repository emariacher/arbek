package graphlayout

import java.awt.{Color, Graphics2D}

import graphlayout.Tableaux._

import scala.collection.immutable.List


class Carre(val rc: RowCol) {

  def this(r: Int, c: Int) = this(new RowCol(r, c))

  val row = rc.r
  val col = rc.c
  var depotPheromones = List[Depot]()


  def updatePheronome(tribu: Tribu) = {
    var z = depotPheromones.find(_.tribu == tribu)
    if (z.isEmpty) {
      depotPheromones = depotPheromones :+ new Depot(tbx.ts, Depot.valeurDepot, tribu)
    } else {
      z.head.update(tbx.ts, Depot.valeurDepot)
    }
  }

  def isLast() = (rc == tbx.maxRC.moinsUn)

  def paint(g: Graphics2D) {
    val horiz = tbx.size.getWidth.toInt / (tbx.maxCol * 2)
    val vert = tbx.size.getHeight.toInt / (tbx.maxRow * 2)
    val x = tbx.origin.getWidth.toInt + (horiz * ((2 * col) + 1))
    val y = tbx.origin.getHeight.toInt + (vert * ((2 * row) + 1))

    depotPheromones.sortBy(_.ph).reverse.foreach(d => {
      g.setColor(d.tribu.c.color)
      val radius = (d.ph / Depot.display).toInt
      g.fillOval(x - 3, y - 3, radius, radius)
      d.evapore
    })

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

