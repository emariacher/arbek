/*
$Header: Node.java: Revision: 2: Author: emariacher: Date: Wednesday, July 01, 2009 4:07:44 PM$

$Log$
emariacher - Wednesday, July 01, 2009 4:07:44 PM
static ghost key detection with "analyze key pressed" button
 */

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;


public class Node implements Comparable<Node> {
	double x;
	double y;

	double dx;
	double dy;

	boolean key_pressed; // or key pressed or source of current
	boolean fixed; 
	boolean active=false; // some current goes through row and/or col

	String lbl;
	Key key;

	@Override
	public int compareTo(Node n) {
		if(n==null) {
			return 1;
		} else {
			return lbl.compareTo(n.lbl);
		}
	}
	@Override
	public String toString() {
		return ("{" + lbl + "}");
	}

	public void reset() {
		active=false;
		key.d_volt=Key.SINK_VOLT;
		key.l_uri_edges=new ArrayList<Edge>();
	}

	private final Color nodeColor = new Color(250, 220, 100);
	private final Color nodeColorActive = Color.cyan;
	private final Color nodeColorPressed = Color.green;
	private final Color membraneNodeColor = Color.white;
	private final Color k_startColColorPressed = Color.blue;
	private final Color k_endRowColorPressed = Color.blue;

	public void paintNode(Graphics g, FontMetrics fm, GraphPanel gp) {
		int i_x = (int)x;
		int i_y = (int)y;
		if(key.compareTo(gp.k_endRow)==0) {
			g.setColor(k_endRowColorPressed);
		} else if(key.compareTo(gp.k_startCol)==0) {
			g.setColor(k_startColColorPressed);
		} else if(key_pressed) {
			g.setColor(nodeColorPressed);
		} else if(key.isMembraneNode()||key.isRow()||key.isCol()) {
			g.setColor(membraneNodeColor);			
		} else if(active) {
			g.setColor(nodeColorActive);			
		} else {
			g.setColor(nodeColor);
		}
		String s_label;
		if(gp.stress) {
			s_label = key.toStringStress();
		} else {
			s_label = lbl;
		}
		int w = fm.stringWidth(s_label) + 10;
		int h = fm.getHeight() + 4;
		g.fillRect(i_x - w/2, i_y - h / 2, w, h);
		g.setColor(Color.black);
		g.drawRect(i_x - w/2, i_y - h / 2, w-1, h-1);
		g.drawString(s_label, i_x - (w-10)/2, (i_y - (h-4)/2) + fm.getAscent());
	}

	public boolean belongs2(ColRow cr) {
		return ((cr.i_col==(key.i_col/2))&&(cr.i_row==(key.i_row/2)));
	}

}
