import java.awt._
import java.awt.event.MouseEvent

class GraphPanel {
  var graph: Graph = null
  var nnodes = 0
  var nodes = new Array[Node](100)

  var nedges = 0
  var edges = new Array[Edge](200)

  var relaxer: Thread = null
  var stress = false
  var random = false

  def this(graph: Graph) {
    this()
    this.graph = graph
    //addMouseListener(this)
  }

  def findNode(lbl: String): Int = {
    var i = 0
    while ( {
      i < nnodes
    }) {
      if (nodes(i).lbl == lbl) return i

      {
        i += 1;
        i - 1
      }
    }
    addNode(lbl)
  }

  def addNode(lbl: String): Int = {
    val n = new Node
    /*n.x = 10  + 380 * Math.random
    n.y = 10  + 380 * Math.random
    n.lbl = lbl
    nodes(nnodes) = n {
      nnodes += 1;
      nnodes - 1
    }*/
    0
  }

  def addEdge(from: String, to: String, len: Int): Unit = {
    val e = new Edge
    e.from = findNode(from)
    e.to = findNode(to)
    e.len = len
    edges({
      nedges += 1;
      nedges - 1
    }) = e
  }

  def run(): Unit = {
    val me = Thread.currentThread
    while ( {
      relaxer eq me
    }) {
      relax()
      if (random && (Math.random < 0.03)) {
        val n = nodes((Math.random * nnodes).toInt)
        if (!n.fixed) {
          n.x += 100 * Math.random - 50
          n.y += 100 * Math.random - 50
        }
        //graph.play(graph.getCodeBase, "audio/drip.au")
      }
      try
        Thread.sleep(100)
      catch {
        case e: InterruptedException =>
        //break //todo: break is not supported
      }
    }
  }

  def relax(): Unit = {
    var i = 0
    while ( {
      i < nedges
    }) {
      val e = edges(i)
      val vx = nodes(e.to).x - nodes(e.from).x
      val vy = nodes(e.to).y - nodes(e.from).y
      var len = Math.sqrt(vx * vx + vy * vy)
      len = if (len == 0) .0001
      else len
      val f = (edges(i).len - len) / (len * 3)
      val dx = f * vx
      val dy = f * vy
      nodes(e.to).dx += dx
      nodes(e.to).dy += dy
      nodes(e.from).dx += -dx
      nodes(e.from).dy += -dy

      {
        i += 1;
        i - 1
      }
    }
    i = 0
    while ( {
      i < nnodes
    }) {
      val n1 = nodes(i)
      var dx = .0
      var dy = .0
      var j = 0
      while ( {
        j < nnodes
      }) {
        //if (i == j) continue //todo: continue is not supported
        val n2 = nodes(j)
        val vx = n1.x - n2.x
        val vy = n1.y - n2.y
        val len = vx * vx + vy * vy
        if (len == 0) {
          dx += Math.random
          dy += Math.random
        }
        else if (len < 100 * 100) {
          dx += vx / len
          dy += vy / len
        }

        {
          j += 1;
          j - 1
        }
      }
      var dlen = dx * dx + dy * dy
      if (dlen > 0) {
        dlen = Math.sqrt(dlen) / 2
        n1.dx += dx / dlen
        n1.dy += dy / dlen
      }

      {
        i += 1;
        i - 1
      }
    }
    val d = new Dimension()
    i = 0
    while ( {
      i < nnodes
    }) {
      val n = nodes(i)
      if (!n.fixed) {
        n.x += Math.max(-5, Math.min(5, n.dx))
        n.y += Math.max(-5, Math.min(5, n.dy))
      }
      if (n.x < 0) n.x = 0
      else if (n.x > d.width) n.x = d.width
      if (n.y < 0) n.y = 0
      else if (n.y > d.height) n.y = d.height
      n.dx /= 2
      n.dy /= 2

      {
        i += 1;
        i - 1
      }
    }
    //repaint()
  }

  var pick: Node = null
  var pickfixed = false
  var offscreen: Image = null
  var offscreensize: Dimension = null
  var offgraphics: Graphics = null

  val fixedColor: Color = Color.red
  val selectColor: Color = Color.pink
  val edgeColor: Color = Color.black
  val nodeColor = new Color(250, 220, 100)
  val stressColor: Color = Color.darkGray
  val arcColor1: Color = Color.black
  val arcColor2: Color = Color.pink
  val arcColor3: Color = Color.red

  def paintNode(g: Graphics, n: Node, fm: FontMetrics): Unit = {
    val x = n.x.toInt
    val y = n.y.toInt
    g.setColor(if (n eq pick) selectColor
    else if (n.fixed) fixedColor
    else nodeColor)
    val w = fm.stringWidth(n.lbl) + 10
    val h = fm.getHeight + 4
    g.fillRect(x - w / 2, y - h / 2, w, h)
    g.setColor(Color.black)
    g.drawRect(x - w / 2, y - h / 2, w - 1, h - 1)
    g.drawString(n.lbl, x - (w - 10) / 2, (y - (h - 4) / 2) + fm.getAscent)
  }

  def update(g: Graphics): Unit = {
    val d = new Dimension()
    if ((offscreen == null) || (d.width != offscreensize.width) || (d.height != offscreensize.height)) {
      //offscreen = createImage(d.width, d.height)
      offscreensize = d
      if (offgraphics != null) offgraphics.dispose()
      offgraphics = offscreen.getGraphics
      //offgraphics.setFont(getFont)
    }
    //offgraphics.setColor(getBackground)
    offgraphics.fillRect(0, 0, d.width, d.height)
    var i = 0
    while ( {
      i < nedges
    }) {
      val e = edges(i)
      val x1 = nodes(e.from).x.toInt
      val y1 = nodes(e.from).y.toInt
      val x2 = nodes(e.to).x.toInt
      val y2 = nodes(e.to).y.toInt
      val len = Math.abs(Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) - e.len).toInt
      offgraphics.setColor(if (len < 10) arcColor1
      else if (len < 20) arcColor2
      else arcColor3)
      offgraphics.drawLine(x1, y1, x2, y2)
      if (stress) {
        val lbl = String.valueOf(len)
        offgraphics.setColor(stressColor)
        offgraphics.drawString(lbl, x1 + (x2 - x1) / 2, y1 + (y2 - y1) / 2)
        offgraphics.setColor(edgeColor)
      }

      {
        i += 1;
        i - 1
      }
    }
    val fm = offgraphics.getFontMetrics
    i = 0
    while ( {
      i < nnodes
    }) {
      paintNode(offgraphics, nodes(i), fm)

      {
        i += 1;
        i - 1
      }
    }
    g.drawImage(offscreen, 0, 0, null)
  }

  //1.1 event handling
  def mouseClicked(e: MouseEvent): Unit = {
  }

  def mousePressed(e: MouseEvent): Unit = {
    //addMouseMotionListener(this)
    var bestdist = Double.MaxValue
    val x = e.getX
    val y = e.getY
    var i = 0
    while ( {
      i < nnodes
    }) {
      val n = nodes(i)
      val dist = (n.x - x) * (n.x - x) + (n.y - y) * (n.y - y)
      if (dist < bestdist) {
        pick = n
        bestdist = dist
      }

      {
        i += 1;
        i - 1
      }
    }
    pickfixed = pick.fixed
    pick.fixed = true
    pick.x = x
    pick.y = y
    //repaint()
    e.consume()
  }

  def mouseReleased(e: MouseEvent): Unit = {
    //removeMouseMotionListener(this)
    if (pick != null) {
      pick.x = e.getX
      pick.y = e.getY
      pick.fixed = pickfixed
      pick = null
    }
    //repaint()
    e.consume()
  }

  def mouseEntered(e: MouseEvent): Unit = {
  }

  def mouseExited(e: MouseEvent): Unit = {
  }

  def mouseDragged(e: MouseEvent): Unit = {
    pick.x = e.getX
    pick.y = e.getY
    //repaint()
    e.consume()
  }

  def mouseMoved(e: MouseEvent): Unit = {
  }

  def start(): Unit = {
    //relaxer = new Thread(this)
    relaxer.start()
  }

  def stop(): Unit = {
    relaxer = null
  }

}
