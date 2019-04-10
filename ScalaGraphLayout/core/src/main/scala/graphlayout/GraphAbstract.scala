package graphlayout

import java.awt.Graphics2D

abstract class GraphAbstract {
  def reset: StateMachine

  def genere: StateMachine

  def doZeMouseJob(mouse: (String, Int, Int)): Unit

  def paint(g: Graphics2D)
}
