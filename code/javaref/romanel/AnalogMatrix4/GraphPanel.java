/*
$Header: GraphPanel.java: Revision: 19: Author: emariacher: Date: Wednesday, July 08, 2009 6:33:13 PM$

$Log$
emariacher - Wednesday, July 08, 2009 6:33:13 PM
get all ghost keys. not debugged yet.
emariacher - Wednesday, July 08, 2009 3:04:00 PM
just some cosmetic when scanning 3 others keys
emariacher - Wednesday, July 08, 2009 2:16:41 PM
do some stats when pressing all the 3 other keys (in a ghost square).
emariacher - Tuesday, July 07, 2009 3:50:59 PM
clean "clear pressed keys". not 1 shot anymore...
emariacher - Thursday, July 02, 2009 6:00:01 PM
check actual vs computed.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

class GraphPanel extends JPanel
implements Runnable, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	static final int SYNCGRAPH = 1;
	static final int NOSYNCGRAPH = 2;
	static final int OTHERKEYS = 3;
	static final int ALLNOSYNCGRAPH = 4;
	static final int ONEQUARTORANDOM = 5;
	static final int ALLQUARTORANDOM = 6;
	static final int GETALLGHOSTS = 7;
	static final int OPTIMIZE = 8;

	static final double RESISTANCE_RANDSPAN = 0.1; // 10% at 3 sigma

	AnalogMatrix1 main;
	KbdMatrix km;
	Log L;
	int nnodes=0;
	int nnodes_backup;
	Node nodes[] = new Node[200];
	Node nodes_backup[] = new Node[200];

	int nedges=0;
	int nedges_backup;
	Edge edges[] = new Edge[400];
	Edge edges_backup[] = new Edge[400];

	Thread relaxer;
	boolean stress=false;
	boolean random=false;

	int numMouseButtonsDown = 0;
	int i_in_remove_dead_ends_mode=0;
	int i_keyPressed=0;

	Key k_startCol;
	Key k_endRow;
	AnalyzeMatrix am;

	GraphPanel(AnalogMatrix1 main) {
		this.main = main;
		addMouseListener(this);
	}

	private int findNode(Key k) {
		for (int i = 0 ; i < nnodes ; i++) {
			if(nodes[i]==null) {
				break;
			}
			if (nodes[i].lbl.equals(k.s_codeKey)) {
				return i;
			}
		}
		return addNode(k);
	}


	private int addNode(Key k) {
		Node n;
		if(k.node==null) {
			n = new Node();
			n.lbl = k.s_codeKey;
			n.key=k;
			k.node=n;
		} else {
			n=k.node;
		}
		if(k.isRow()) {
			n.x = 10;
		} else {
			n.x=120 + 60*n.key.i_col;
		}
		if(k.isCol()) {
			n.y = 10;
		} else {
			n.y=120 + 50*n.key.i_row;
		}
		nodes[nnodes] = n;
		return nnodes++;
	}


	public void addEdge(Key k_from, Key k_to, int len, int edgetype) throws Exception {
		//		L.myPrintln("addEdge("+k_from.s_codeKey+","+k_to.s_codeKey+","+len+");");
		int i_from = findNode(k_from);
		int i_to = findNode(k_to);
		Edge e = new Edge(i_from, i_to, len, edgetype, 1.0);
		nodes[i_from].key.l_edges.add(e);
		nodes[i_to].key.l_edges.add(e);
		e.n_from=nodes[i_from];
		e.n_to=nodes[i_to];
		edges[nedges++] = e;
	}

	public void addEdge(Key k_from, Key k_to, double d_resistor,
			int i_edgetype) throws Exception {
		int len;
		if(k_to.isMembraneNode()||k_from.isMembraneNode()) {
			len=40;
		} else {
			switch(i_edgetype) {
			case Edge.COL_EDGE_TYPE:
				len=(k_to.i_row-k_from.i_row)*40;
				break;
			case Edge.ROW_EDGE_TYPE:
				len=(k_to.i_col-k_from.i_col)*30;
				break;
			default:
				throw new Exception("Invalid Edge Type: "+i_edgetype+" between "+k_from+" ->"+k_to);
			}
		}
		if(len<0) {
			len=0-len;
		}
		while(len>300) {
			len=len/2;
		}

		//		L.myPrintln("addEdge("+k_from.s_codeKey+","+k_to.s_codeKey+","+len+");");
		int i_from = findNode(k_from);
		int i_to = findNode(k_to);
		Edge e = new Edge(i_from, i_to, len, i_edgetype, d_resistor);
		nodes[i_from].key.l_edges.add(e);
		nodes[i_to].key.l_edges.add(e);
		e.n_from=nodes[i_from];
		e.n_to=nodes[i_to];
		edges[nedges++] = e;
	}

	public void addEdge(Edge e) {
		int i_from = findNode(e.n_from.key);
		int i_to = findNode(e.n_to.key);
		e.n_from=nodes[i_from];
		e.n_to=nodes[i_to];
		edges[nedges++] = e;
	}

	public void run() {
		Thread me = Thread.currentThread();
		try {
			while (relaxer == me) {
				relax();
				if (random && (Math.random() < 0.03)) {
					Node n = nodes[(int)(Math.random() * nnodes)];
					if (!n.fixed) {
						n.x += 100*Math.random() - 50;
						n.y += 100*Math.random() - 50;
					}
				}
				if(i_in_remove_dead_ends_mode>0) {
					Thread.sleep(1);
				} else if(i_QuartoBatch>0) {		
					switch(i_QuartoBatch) {
					case SYNCGRAPH: // 1 quarto synchronize with graph
						am = new AnalyzeMatrix(L, this, false, 0);
						am.runQuartoBatch(0);
						break;
					case NOSYNCGRAPH: // 1 quarto don't synchronize with graph
						am = new AnalyzeMatrix(L, this, true, 0);
						am.runQuartoBatch(0);
						break;
					case OTHERKEYS: // get a feeling of ghost combinations vs regular combinations on a quarto
						am = new AnalyzeMatrix(L, this, true, 0);
						am.runQuartoBatch(0);
						am.runQuarto3OtherKeysBatch(0);
						break;
					case GETALLGHOSTS: // get the list of all ghosts combinations
						am = new AnalyzeMatrix(L, this, true, 0);
						am.runGetAllGhosts(0);
						break;
					case ALLNOSYNCGRAPH: // all quartos don't synchronize with graph
						am = new AnalyzeMatrix(L, this, true, AnalyzeMatrix.RUNALLQUARTOBATCH);
						am.runAllQuartoBatch(0);
						break;
					case ONEQUARTORANDOM: // 1 quarto random don't synchronize with graph
						am = new AnalyzeMatrix(L, this, true, AnalyzeMatrix.RUNALLQUARTOBATCH);
						am.runQuartoRandomBatch();
						break;
					case ALLQUARTORANDOM: // all quartos random don't synchronize with graph
						am = new AnalyzeMatrix(L, this, true, AnalyzeMatrix.RUNALLQUARTORANDOMBATCH);
						am.runAllQuartoRandomBatch();
						break;
					case OPTIMIZE: // all quartos random don't synchronize with graph
						am = new AnalyzeMatrix(L, this, true, AnalyzeMatrix.OPTIMIZE);
						am.optimize();
						break;
					}
					i_QuartoBatch=0;
				} else {
					Thread.sleep(100);
				}
			}
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
			main.AnalyzeLabel.setText("******G*Errors: not Built!********");
			try {
				PipedInputStream piErr = new PipedInputStream();
				PipedOutputStream poErr = new PipedOutputStream(piErr);
				e.printStackTrace(new PrintStream(poErr));
				System.setErr(new PrintStream(poErr, true));
				e.printStackTrace();
				e.printStackTrace(new PrintStream(poErr));
				L.myPrintln(e.toString());
				L.myErrPrintln(e.toString());
				int len = 0;
				byte[] buf = new byte[1024];
				while (true) {
					len = piErr.read(buf);
					if (len == -1) {
						break;
					}
					L.err.append(new String(buf, 0, len));
					L.myErrPrintln(new String(buf, 0, len));
					L.myPrintln(new String(buf, 0, len));
				}

			} catch (Exception ze) {}
		} finally {
		}

	}

	synchronized void relax() throws Exception {
		for (int i = 0 ; i < nedges ; i++) {
			Edge e = edges[i];
			double vx = nodes[e.to].x - nodes[e.from].x;
			double vy = nodes[e.to].y - nodes[e.from].y;
			double len = Math.sqrt(vx * vx + vy * vy);
			len = (len == 0) ? .0001 : len;
			double f = (edges[i].len - len) / (len * 3);
			double dx = f * vx;
			double dy = f * vy;

			nodes[e.to].dx += dx;
			nodes[e.to].dy += dy;
			nodes[e.from].dx += -dx;
			nodes[e.from].dy += -dy;
		}

		i_in_remove_dead_ends_mode=0;
		for (int i = 0 ; i < nnodes ; i++) {
			Node n1 = nodes[i];
			double dx = 0;
			double dy = 0;
			if(pick==null) {
				i_in_remove_dead_ends_mode += n1.key.remove_dead_ends();
			}

			for (int j = 0 ; j < nnodes ; j++) {
				if (i == j) {
					continue;
				}
				Node n2 = nodes[j];
				double vx = n1.x - n2.x;
				double vy = n1.y - n2.y;
				double len = vx * vx + vy * vy;
				if (len == 0) {
					dx += Math.random();
					dy += Math.random();
				} else if (len < 100*100) {
					dx += vx / len;
					dy += vy / len;
				}
			}
			double dlen = dx * dx + dy * dy;
			if (dlen > 0) {
				dlen = Math.sqrt(dlen) / 2;
				n1.dx += dx / dlen;
				n1.dy += dy / dlen;
			}
		}

		Dimension d = getSize();
		for (int i = 0 ; i < nnodes ; i++) {
			Node n = nodes[i];
			if (!n.fixed) {
				n.x += Math.max(-5, Math.min(5, n.dx));
				n.y += Math.max(-5, Math.min(5, n.dy));
			}
			if (n.x < 0) {
				n.x = 0;
			} else if (n.x > d.width-30) {
				n.x = d.width-30;
			}
			if (n.y < 0) {
				n.y = 0;
			} else if (n.y > d.height-20) {
				n.y = d.height-20;
			}
			n.dx /= 2;
			n.dy /= 2;
		}
		repaint();
	}

	Node pick;
	boolean pickfixed;
	Image offscreen;
	Dimension offscreensize;
	Graphics offgraphics;

	private final Color edgeColor = Color.black;
	private final Color stressColor = Color.darkGray;
	private final Color arcColorRow = new Color(225, 125, 175);
	private final Color arcColorCol = new Color(150, 200, 250);
	private final Color edgeRowColorActive = Color.red;
	private final Color edgeColColorActive = Color.blue;
	public int i_QuartoBatch=0;


	public void setI_QuartoBatch(int quartoBatch) {
		i_QuartoBatch = quartoBatch;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);       
		update(g);
	}

	public synchronized void update(Graphics g) {
		Dimension d = getSize();
		if ((offscreen == null) || (d.width != offscreensize.width) || (d.height != offscreensize.height)) {
			offscreen = createImage(d.width, d.height);
			offscreensize = d;
			if (offgraphics != null) {
				offgraphics.dispose();
			}
			offgraphics = offscreen.getGraphics();
			offgraphics.setFont(getFont());
		}

		offgraphics.setColor(getBackground());
		offgraphics.fillRect(0, 0, d.width, d.height);
		for (int i = 0 ; i < nedges ; i++) {
			Edge e = edges[i];
			int x1 = (int)nodes[e.from].x;
			int y1 = (int)nodes[e.from].y;
			int x2 = (int)nodes[e.to].x;
			int y2 = (int)nodes[e.to].y;
			//			int len = (int)Math.abs(Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)) - e.len);
			if(e.active) {
				switch(e.edge_type) {
				case Edge.COL_EDGE_TYPE:
					offgraphics.setColor(edgeColColorActive);
					break;
				case Edge.ROW_EDGE_TYPE:
					offgraphics.setColor(edgeRowColorActive);
					break;
				}

			} else {
				switch(e.edge_type) {
				case Edge.COL_EDGE_TYPE:
					offgraphics.setColor(arcColorCol);
					break;
				case Edge.ROW_EDGE_TYPE:
					offgraphics.setColor(arcColorRow);
					break;
				}
			}
			offgraphics.drawLine(x1, y1, x2, y2);
			if (stress) {
				String lbl = e.toStringStress();
				offgraphics.setColor(stressColor);
				offgraphics.drawString(lbl, x1 + (x2-x1)/2, y1 + (y2-y1)/2);
				offgraphics.setColor(edgeColor);
			}
		}

		FontMetrics fm = offgraphics.getFontMetrics();
		for (int i = 0 ; i < nnodes ; i++) {
			nodes[i].paintNode(offgraphics, fm, this);
		}
		g.drawImage(offscreen, 0, 0, null);
	}

	//1.1 event handling
	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		numMouseButtonsDown++;
		addMouseMotionListener(this);
		double bestdist = Double.MAX_VALUE;

		int x = e.getX();
		int y = e.getY();
		for (int i = 0 ; i < nnodes ; i++) {
			Node n = nodes[i];
			double dist = (n.x - x) * (n.x - x) + (n.y - y) * (n.y - y);
			if (dist < bestdist) {
				pick = n;
				bestdist = dist;
			}
		}
		pickfixed = pick.fixed;
		pick.fixed = true;
		pick.x = x;
		pick.y = y;
		if(!stress) {
			try {
				switch(e.getButton()) {
				case MouseEvent.BUTTON1:
					if(!pick.key_pressed) {
						pick.key_pressed=true;
						i_keyPressed++;
						i_in_remove_dead_ends_mode=777;
						k_startCol = pick.key.findCol();
						k_endRow = pick.key.findRow();
						start_spread_current();
					} else {
						pick.key_pressed=false;
						i_keyPressed--;
						i_in_remove_dead_ends_mode=777;	 
						start_spread_current();
					}
					break;
				case MouseEvent.BUTTON3:
					if(pick.key.isRow()) {
						k_endRow = pick.key.findRow();
						start_spread_current();
					} else if(pick.key.isCol()) {
						k_startCol = pick.key.findCol();				
						start_spread_current();
					} else {
						k_startCol = pick.key.findCol();	
						k_endRow = pick.key.findRow();
						start_spread_current();
					}
					break;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		repaint();
		e.consume();
	}

	public void mouseReleased(MouseEvent e) {
		numMouseButtonsDown--;
		removeMouseMotionListener(this);

		pick.fixed = pickfixed;
		pick.x = e.getX();
		pick.y = e.getY();
		if (numMouseButtonsDown == 0) {
			pick = null;
		}

		repaint();
		e.consume();
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		pick.x = e.getX();
		pick.y = e.getY();
		repaint();
		e.consume();
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void start() {
		relaxer = new Thread(this);
		relaxer.start();
	}

	public void stop() {
		relaxer = null;
	}

	void clearPressedKeys() {
		for (int i = 0 ; i < nedges ; i++) {
			Edge edge = edges[i];
			edge.active=false;
			edge.b_notYetComputed=true;
			edge.d_current=0.0;
		}
		for (int i = 0 ; i < nnodes ; i++) {
			Node n = nodes[i];
			n.key_pressed=false;
			n.reset();
		}	
		i_keyPressed=0;
	}

	void clearActiveKeys() {
		for (int i = 0 ; i < nedges ; i++) {
			Edge edge = edges[i];
			edge.reset();
		}
		for (int i = 0 ; i < nnodes ; i++) {
			Node n = nodes[i];
			n.reset();
		}		
	}

	ArrayList<Node> getActiveNodes() {
		ArrayList<Node> l_activeNodes = new ArrayList<Node>();
		for (int i = 0 ; i < nnodes ; i++) {
			Node n = nodes[i];
			if(n.active) {
				l_activeNodes.add(n);
			}
		}		
		return l_activeNodes;
	}

	void start_spread_current() throws Exception  {
		clearActiveKeys();
		k_startCol.b_isExtremity=true;
		k_endRow.b_isExtremity=true;
		for (int i = 0 ; i < nnodes ; i++) {
			Node n = nodes[i];
			if(n.key_pressed) {
				Key k_start = n.key.findCol();
				k_start.start_spread_current();		
			}
		}
		k_startCol.b_isExtremity=false;
		k_endRow.b_isExtremity=false;
	}

	void backup_and_reset() {
		nnodes_backup=nnodes;
		nodes_backup = new Node[200];
		for (int i = 0 ; i < nnodes ; i++) {
			nodes_backup[i]=nodes[i];
		}
		nnodes=0;
		nodes = new Node[200];

		nedges_backup=nedges;
		edges_backup = new Edge[400];
		for (int i = 0 ; i < nedges ; i++) {
			edges_backup[i]=edges[i];
		}
		nedges=0;
		edges = new Edge[400];
	}

	void restore() {
		if(nnodes_backup>0) {
			nnodes=nnodes_backup;
			for (int i = 0 ; i < nnodes ; i++) {
				nodes[i]=nodes_backup[i];
			}

			nedges=nedges_backup;
			for (int i = 0 ; i < nedges ; i++) {
				edges[i]=edges_backup[i];
			}	
			stress=false;
		}
		nnodes=0;
		nodes = new Node[200];
		nedges=0;
		edges = new Edge[400];
	}

	public boolean hasKeyPressed() {
		return i_keyPressed>0;
	}

	public void randomizeResistances(int i_seed) {
		if(i_seed!=0) {
			Random rand = new Random(i_seed);
			for (int i = 0 ; i < nedges ; i++) {
				double d_resist=edges[i].r.d_ohms_orig;
				edges[i].r.d_ohms=d_resist+(d_resist*RESISTANCE_RANDSPAN*0.33*rand.nextGaussian()); // 0.33 because 1 is 3 sigma
			}
			double d_sourve_volt_span = Key.DEFAULT_SOURCE_VOLT+((Key.DEFAULT_SOURCE_VOLT+Key.MAX_SOURCE_VOLT)/2)+
			(((Key.DEFAULT_SOURCE_VOLT+Key.MAX_SOURCE_VOLT)/2)*0.33*rand.nextGaussian());
			for (int i = 0 ; i < nnodes ; i++) {
				nodes[i].key.d_source_volt=d_sourve_volt_span;	
			}
		}
	}

	public void updateResistances() {
		for (int i = 0 ; i < nedges ; i++) {
			edges[i].r.d_ohms_orig=edges[i].r.d_ohms;
		}
	}
}
