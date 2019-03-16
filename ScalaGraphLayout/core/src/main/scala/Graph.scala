import java.awt._
import java.awt.event.{ActionEvent, ItemEvent}
import java.util.StringTokenizer

class Graph {
  var panel: GraphPanel = null
  var controlPanel: Panel = null

  val scramble = new Button("Scramble")
  val shake = new Button("Shake")
  val stress = new Checkbox("Stress")
  val random = new Checkbox("Random")

  def init(): Unit = {
    //setLayout(new BorderLayout)
    panel = new GraphPanel(this)
    //add("Center", panel)
    controlPanel = new Panel
    //add("South", controlPanel)
    controlPanel.add(scramble)
    //scramble.addActionListener(this)
    controlPanel.add(shake)
    //shake.addActionListener(this)
    controlPanel.add(stress)
    //stress.addItemListener(this)
    controlPanel.add(random)
    //random.addItemListener(this)
    //val edges = getParameter("edges")
    val t = new StringTokenizer("", ",")
    while ( {
      t.hasMoreTokens
    }) {
      var str = t.nextToken
      val i = str.indexOf('-')
      if (i > 0) {
        var len = 50
        val j = str.indexOf('/')
        if (j > 0) {
          len = Integer.valueOf(str.substring(j + 1)).intValue
          str = str.substring(0, j)
        }
        panel.addEdge(str.substring(0, i), str.substring(i + 1), len)
      }
    }
    val d = new Dimension()
    val center = null
    if (center != null) {
      val n = panel.nodes(panel.findNode(center))
      n.x = d.width / 2
      n.y = d.height / 2
      n.fixed = true
    }
  }

  def destroy(): Unit = {
    //remove(panel)
    //remove(controlPanel)
  }

  def start(): Unit = {
    panel.start()
  }

  def stop(): Unit = {
    panel.stop()
  }

  def actionPerformed(e: ActionEvent): Unit = {
    val src = e.getSource
    if (src eq scramble) {
      //play(getCodeBase, "audio/computer.au")
      val d = new Dimension()
      var i = 0
      while ( {
        i < panel.nnodes
      }) {
        val n = panel.nodes(i)
        if (!n.fixed) {
          n.x = 10 + (d.width - 20) * Math.random
          n.y = 10 + (d.height - 20) * Math.random
        }

        {
          i += 1;
          i - 1
        }
      }
      return
    }
    if (src eq shake) {
      //play(getCodeBase, "audio/gong.au")
      val d = new Dimension()
      var i = 0
      while ( {
        i < panel.nnodes
      }) {
        val n = panel.nodes(i)
        if (!n.fixed) {
          n.x += 80 * Math.random - 40
          n.y += 80 * Math.random - 40
        }

        {
          i += 1;
          i - 1
        }
      }
    }
  }

  def itemStateChanged(e: ItemEvent): Unit = {
    val src = e.getSource
    val on = e.getStateChange == ItemEvent.SELECTED
    if (src eq stress) panel.stress = on
    else if (src eq random) panel.random = on
  }

  def getAppletInfo = "Title: GraphLayout \nAuthor: <unknown>"

  def getParameterInfo: Array[Array[String]] = {
    val info = Array(Array("edges", "delimited string", "A comma-delimited list of all the edges.  It takes the form of 'C-N1,C-N2,C-N3,C-NX,N1-N2/M12,N2-N3/M23,N3-NX/M3X,...' where C is the name of center node (see 'center' parameter) and NX is a node attached to the center node.  For the edges connecting nodes to eachother (and not to the center node) you may (optionally) specify a length MXY separated from the edge name by a forward slash."), Array("center", "string", "The name of the center node."))
    info
  }

}
