package graphlayout

import java.awt.Graphics2D

import kebra.MyLog

import scala.math.{max, min}

abstract class GraphAbstract {
  var slider_timeout = 1

  def reset: StateMachine

  def genere: StateMachine

  def doZeMouseJob(mouse: (String, Int, Int)): Unit

  def doZeSliderJob(slider: (String, Int)): Unit = {
    slider_timeout = min(max(1, (slider._2 * slider._2) / 100), 5000)
    MyLog.myPrintIt(slider._1, slider._2, slider_timeout)
    ZePanel.zp.pause = (slider._2 == 0)
    ZePanel.zp.run = !ZePanel.zp.pause
    ZePanel.zp.step = false
  }

  def paint(g: Graphics2D)
}
