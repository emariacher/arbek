package graphlayout

import java.awt.Graphics2D

abstract class GraphAbstract {
  var slider_timeout = 1

  def reset: StateMachine

  def rassemble: StateMachine

  def ouestlajaffe: StateMachine

  def travaille: StateMachine

  def doZeMouseJob(mouse: (String, Int, Int)): Unit

  def doZeSliderJob(slider: (String, Int)): Unit

  def triggerTraceNotAlreadyActivated: Boolean

  def paint(g: Graphics2D)
}
