/*
$Header: Key.java: Revision: 14: Author: emariacher: Date: Thursday, July 09, 2009 2:22:07 PM$

$Log$
emariacher - Thursday, July 09, 2009 2:22:07 PM
list all ghost keys seems OK.
emariacher - Tuesday, July 07, 2009 5:39:50 PM
real matrix scan seems to be somewhat working...
emariacher - Tuesday, July 07, 2009 5:08:11 PM
matrix scan: pas encore au point.
emariacher - Wednesday, July 01, 2009 4:31:29 PM
when current inferior to 1st AD level, then GHostKey is reported.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Key implements Comparable<Key> {
	int i_row=-1;
	int i_col=-1;
	int i_codeKey;
	String s_codeKey=null;
	Node node;
	SortedSet<Edge> l_edges= new TreeSet<Edge>();
	ArrayList<Edge> l_uri_edges= new ArrayList<Edge>();
	KbdMatrix km;
	int i_active_edges=0;
	int i_active_row_edges=0;
	int i_active_col_edges=0;
	int i_active_ee_edges=0;

	public double d_volt;
	int i_type=0;
	int i_row_edges;
	int i_col_edges;
	int i_edges;
	public boolean b_isExtremity=false;

	static final int ROW_TYPE = 147;
	static final int COL_TYPE = 148;
	double d_source_volt = DEFAULT_SOURCE_VOLT;
	static final double DEFAULT_SOURCE_VOLT = 1.0;
	static final double MAX_SOURCE_VOLT = 5.0;
	static final double SINK_VOLT = 0.0;
	static final double PRECISION_CURRENT = 0.000001;
	static final boolean CUT_INACTIVE_COLUMNS=false;

	public Key(int i_row, int i_col, int i_codeKey, KbdMatrix km) {
		this.i_row = i_row;
		this.i_col = i_col;
		this.i_codeKey = i_codeKey;
		this.km=km;
		this.s_codeKey=new String(toString(km));
	}

	public Key(int i_codeKey, KbdMatrix km) {
		this.i_codeKey = i_codeKey;
		this.km=km;
		this.s_codeKey=new String(toString(km));
		if(s_codeKey.toUpperCase().indexOf("ROW")==0) {
			this.i_type=ROW_TYPE;
			Matcher matcher = Pattern.compile("\\d+").matcher(s_codeKey);
			matcher.find();
			this.i_row = Integer.valueOf(matcher.group());
		} else	if(s_codeKey.toUpperCase().indexOf("COL")==0) {
			this.i_type=COL_TYPE;
			Matcher matcher = Pattern.compile("\\d+").matcher(s_codeKey);
			matcher.find();
			this.i_col = Integer.valueOf(matcher.group());
		}
	}

	public Key(int i_rowcol, int rowType, KbdMatrix km, int u) {
		this.km = km;
		switch(rowType) {
		case ROW_TYPE: i_row=i_rowcol; break;
		case COL_TYPE: i_col=i_rowcol; break;
		}
		this.i_codeKey=rowType;
		this.s_codeKey=new String(SmbLst.get(rowType)+i_rowcol);
	}


	public Key(int i_row, int i_col, KbdMatrix km) {
		this.i_row = i_row;
		this.i_col = i_col;
		this.km = km;
		this.i_codeKey = km.i_getValue(i_row, i_col);
		this.s_codeKey=new String(toString(km));
	}

	void start_spread_current() throws Exception {
		node.active=true;
		for(Edge e: l_edges) {
			e.spread_current();
		}
	}

	int get_active_edges() throws Exception {
		i_active_edges=0;
		i_active_row_edges=0;
		i_active_col_edges=0;
		i_row_edges=0;
		i_col_edges=0;
		i_active_ee_edges=0;
		i_edges=0;

		for(Edge e: l_edges) {
			if(e.active) {
				i_active_edges++;
				if(e.edge_type==Edge.ROW_EDGE_TYPE) {
					i_active_row_edges++;
				}
				if(e.edge_type==Edge.COL_EDGE_TYPE) {
					i_active_col_edges++;
				}
				if(e.edge_type==Edge.EE_EDGE_TYPE) {
					i_active_ee_edges++;
				}
			}
			if(e.edge_type==Edge.ROW_EDGE_TYPE) {
				i_row_edges++;
			}
			if(e.edge_type==Edge.COL_EDGE_TYPE) {
				i_col_edges++;
			}
			i_edges++;
			//			km.L.myPrintln("            3["+this+":"+e+"] "+e.active+"("+
			//					i_active_edges+","+i_active_row_edges+","+i_active_col_edges+","+i_row_edges+","+i_col_edges+
			//					","+i_active_ee_edges+","+i_edges+")");
		}	
		return i_active_edges;
	}

	int remove_dead_ends() throws Exception {
		int i_was_active_edges=0;
		int i_is_active_edges=0;
		if(node.active) {
			i_was_active_edges=get_active_edges();
			if(isRow()||isCol()) {
				node.key_pressed=false;	
				if(i_active_edges<1) {
					inactivate_node_and_edges();
				}
			} else {
				switch(i_active_edges) {
				case 1:
					inactivate_node_and_edges();
					break;
				case 2:
					if(!node.key_pressed) {
						if(i_active_row_edges==1) {
							inactivate_node_and_edges();
						}
					}
					break;
				case 3:
					if(!node.key_pressed) {
						if(i_active_row_edges==1) {
							inactivate_node_and_edges(Edge.ROW_EDGE_TYPE);
						}
						if(i_active_col_edges==1) {
							inactivate_node_and_edges(Edge.COL_EDGE_TYPE);
						}
					}
					break;
				}
			}
		}
		i_is_active_edges=get_active_edges();
		return i_was_active_edges-i_is_active_edges;
	}

	private void inactivate_node_and_edges() {
		for(Edge e: l_edges) {
			e.active=false;
		}
		node.active=false;
	}

	private void inactivate_node_and_edges(int edge_type) {
		for(Edge e: l_edges) {
			if(e.edge_type==edge_type) {
				e.active=false;
			}
		}
		int i_active_edge=0;
		for(Edge e: l_edges) {
			if(e.active) {
				i_active_edge++;
			}
		}
		if(i_active_edge==0) {
			node.active=false;
		}
	}

	public void spread_current(int edge_type) throws Exception {
		//		km.L.myPrintln("      "+this+" "+node.active+" "+Edge.printEdgeType(edge_type));
		if(CUT_INACTIVE_COLUMNS&&isCol()&&!isStartCol()) {
			//			km.L.myPrintln("======"+this+" "+node.active+" "+Edge.printEdgeType(edge_type)+" "+
			//					CUT_INACTIVE_COLUMNS+isCol()+(!isStartCol()));
			inactivate_node_and_edges();
			return;
		}
		if(node.active) {
			get_active_edges();
			if(i_active_row_edges==1) {
				for(Edge e: l_edges) {
					if(!e.active) {
						if(e.edge_type==Edge.ROW_EDGE_TYPE) {
							e.spread_current();
						}
					}
				}
			}
			if(i_active_col_edges==1) {
				for(Edge e: l_edges) {
					if(!e.active) {
						if(e.edge_type==Edge.COL_EDGE_TYPE) {
							e.spread_current();
						}
					}
				}
			}
		} else {
			node.active=true;
			for(Edge e: l_edges) {
				if(node.key_pressed) {
					e.spread_current();
				} else if(node.key.isCol()) {
					e.spread_current(Edge.COL_EDGE_TYPE);
				} else if(node.key.isRow()) {
					e.spread_current(Edge.ROW_EDGE_TYPE);
				} else if(e.edge_type==Edge.EE_EDGE_TYPE) {
					e.spread_current(Edge.COL_EDGE_TYPE);
					e.spread_current(Edge.ROW_EDGE_TYPE);
				} else if(e.edge_type==edge_type) {
					e.spread_current();
				}
			}		
		}
	}

	Key findCol() {
		if(isCol()) {
			return this;
		} else {

			ArrayList<Node> l_nodes= new ArrayList<Node>(Arrays.asList(km.graphPanel.nodes));
			for(Node n:l_nodes){
				if(n==null) {
					break;
				}
				if(n.key.isCol()&&n.key.i_col==(i_col/2)) {
					return n.key;
				}
			}
			return null;
		}
	}

	public Key findRow() {
		if(isRow()) {
			return this;
		} else {
			ArrayList<Node> l_nodes= new ArrayList<Node>(Arrays.asList(km.graphPanel.nodes));
			for(Node n:l_nodes){
				if(n==null) {
					break;
				}
				if(n.key.isRow()&&n.key.i_row==(i_row/2)) {
					return n.key;
				}
			}
			return null;
		}
	}

	public boolean is(int i_code) {
		return i_codeKey==i_code;
	}
	public boolean isRow() {
		return is(ROW_TYPE)||i_type==ROW_TYPE;
	}
	public boolean isCol() {
		return is(COL_TYPE)||i_type==COL_TYPE;
	}
	public boolean isStartCol() {
		return equals(km.graphPanel.k_startCol);
	}
	public boolean is(Key key1) {
		return is(key1.i_codeKey);
	}

	public void updateVoltage(Edge e, Key col) {
		Key k_to = e.n_to.key;
		Key k_from = e.n_from.key;

		if(compareTo(k_from)==0) {
			d_volt=k_to.d_volt + e.tension();
		} else {
			d_volt=k_from.d_volt - e.tension();
		}
		if(d_volt>d_source_volt) {
			d_volt=d_source_volt*0.99;
		} else if(d_volt<Key.SINK_VOLT) {
			d_volt=Key.SINK_VOLT+(d_source_volt*0.01);		
		}
	}


	public double equilibrateCurrents() {
		double d_sumCurrent=0.0;
		ArrayList<Double> l_currents = new ArrayList<Double>();
		for(Edge e:l_uri_edges) {
			double d_current=0.0;
			if(compareTo(e.n_from.key)==0) {
				d_current+=e.d_current;
			} else {
				d_current-=e.d_current;					
			}
			d_sumCurrent+=d_current;
			l_currents.add(d_current);
		}
		if((d_sumCurrent>PRECISION_CURRENT)||
				(d_sumCurrent<(0.0-PRECISION_CURRENT))) {
			double d_addCurrent=d_sumCurrent/l_currents.size();
			for(Edge e:l_uri_edges) {
				if(compareTo(e.n_from.key)==0) {
					e.d_current-=d_addCurrent/e.r.d_ohms;
				} else {
					e.d_current+=d_addCurrent/e.r.d_ohms;				
				}
			}
			d_sumCurrent=0.0;
			for(Edge e:l_uri_edges) {
				double d_current=0.0;
				if(compareTo(e.n_from.key)==0) {
					d_current+=e.d_current;
				} else {
					d_current-=e.d_current;					
				}
				d_sumCurrent+=d_current;
			}
			if(d_sumCurrent>0.0) {
				return d_sumCurrent;
			} else {
				return 0.0 - d_sumCurrent;
			}
		} else {
			return 0.0;
		}
	}

	public double getInOutCurrent() {
		double d_sumCurrent=0.0;
		for(Edge e:l_uri_edges) {
			if(compareTo(e.n_to.key)==0) {
				d_sumCurrent+=e.d_current;					
			} else {
				d_sumCurrent-=e.d_current;								
			}
		}
		return d_sumCurrent;
	}


	public Key findNextNode(Edge e_out, Resistor r, ArrayList<Edge> l_edge_chain) throws Exception {
		Key k_to = e_out.n_to.key;
		Key k_from = e_out.n_from.key;
		Key k_next;
		Key k_return=null;

		if(compareTo(k_from)==0) {
			k_next=k_to;
		} else {
			k_next=k_from;
		}
		if(compareTo(k_next)==0) {
			throw new Exception("BUG: " + toString()+" vs " +k_next.toString());
		}
		r.add(e_out.r);
		l_edge_chain.add(e_out);
		int i_numberActiveOfEdges=k_next.get_active_edges();
		//		km.L.myPrintln("      Edge: "+e_out+"         "+String.format("r_add: %1$5.1f",r.d_ohms));
		if(!e_out.b_notYetComputed) {
			//			km.L.myPrintln("        ["+e_out+"].b_notYetComputed=="+e_out.b_notYetComputed+" -> return null");
			return null;
		}
		e_out.b_notYetComputed=false;
		if(k_next.isRow()) {
			if(isRow()) {
				//				km.L.myPrintln("        ["+e_out+"] isAllRow -> return null");
				return null;
			} else {
				//				km.L.myPrintln("        ["+k_next+"] isRow -> return "+k_next);
				return k_next;
			}
		}
		//		if(k_next.isCol()) {
		//			if(isCol()) {
		//				//				km.L.myPrintln("        ["+e_out+"] isAllCol -> return null");
		//				return null;
		//			} else {
		//				//				km.L.myPrintln("        ["+k_next+"] isCol -> return "+k_next);
		//				return k_next;
		//			}
		//		}
		//		km.L.myPrintln("        ["+k_next+"].get_active_edges()=="+i_numberActiveOfEdges);
		switch(i_numberActiveOfEdges) {
		case 1:
			//			km.L.myPrintln("          ["+k_next+"].get_active_edges()=="+i_numberActiveOfEdges+" -> return null");
			return null;
		case 2:
			for(Edge e: k_next.l_edges) {
				if(e.active) {
					if(e.compareTo(e_out)!=0) {
						if(e.b_notYetComputed) {
							k_return=k_next.findNextNode(e, r, l_edge_chain);
							//							km.L.myPrintln("            1["+k_next+"].findNextNode("+e+")="+k_return);
							return k_return;
						}
					}
				}
			}
			//			km.L.myPrintln("          ["+k_next+"].get_active_edges()=="+i_numberActiveOfEdges+" -> return "+k_return);
			return k_return;
		case 3:
			//			km.L.myPrintln("          ["+k_next+"].get_active_edges()=="+i_numberActiveOfEdges+" -> return "+k_next);
			return k_next;
		default:
			if(k_next.node.key_pressed) {
				//				km.L.myPrintln("          ["+k_next+"].key_pressed .get_active_edges()=="+i_numberActiveOfEdges+" -> return "+k_next);
				return k_next;
			} else {
				//				km.L.myPrintln("          ["+k_next+"].i_active_row_edges("+k_next.i_active_row_edges+")? i_row_edges("+k_next.i_row_edges+")");
				//				km.L.myPrintln("          ["+k_next+"].i_active_col_edges("+k_next.i_active_col_edges+")? i_col_edges("+k_next.i_col_edges+")");
				switch(e_out.edge_type) {
				case Edge.ROW_EDGE_TYPE:
					if(k_next.i_active_row_edges<k_next.i_row_edges) {
						//						km.L.myPrintln("          ["+k_next+"].i_active_row_edges("+k_next.i_active_row_edges+")< i_row_edges("+k_next.i_row_edges+") -> return "+k_next);
						return k_next;
					}
					if(k_next.i_active_row_edges>2) {
						//						km.L.myPrintln("          ["+k_next+"].i_active_row_edges("+k_next.i_active_row_edges+")>2 -> return "+k_next);
						return k_next;
					}
				case Edge.COL_EDGE_TYPE:
					if(k_next.i_active_col_edges<k_next.i_col_edges) {
						//						km.L.myPrintln("          ["+k_next+"].i_active_col_edges("+k_next.i_active_col_edges+")< i_col_edges("+k_next.i_col_edges+") -> return "+k_next);
						return k_next;
					}
					if(k_next.i_active_col_edges>2) {
						//						km.L.myPrintln("          ["+k_next+"].i_active_col_edges("+k_next.i_active_col_edges+")>2 -> return "+k_next);
						return k_next;
					}
				}
				for(Edge e: k_next.l_edges) {
					if(e.active) {
						if((e.compareTo(e_out)!=0)&&(e.edge_type==e_out.edge_type)) {
							if(e.b_notYetComputed) {
								k_return=k_next.findNextNode(e, r, l_edge_chain);
								//								km.L.myPrintln("            2["+k_next+"].findNextNode("+e+")="+k_return);
								return k_return;
							}
						}
					}
				}
				//				km.L.myPrintln("          ["+k_next+"].NOT key_pressed .get_active_edges()=="+i_numberActiveOfEdges+" -> return "+k_return);
				return k_return;			
			}
		}	
	}

	public void findNextNodeList() throws Exception {
		Key k_next;
		ArrayList<Edge> l_edge_chain = new ArrayList<Edge>();
		for(Edge e: l_edges) {
			if(e.active) {
				//				km.L.myPrintln("  ****Edge: "+e.toString());
				Resistor r = new Resistor(0.0);
				k_next = findNextNode(e, r, l_edge_chain);
				if(k_next!=null) {
					if(!equals(k_next)) {
						Edge ez=new Edge(this, k_next, r);
						ez.add(l_edge_chain);
						l_uri_edges.add(ez);
						//						km.L.myPrintln("    **Edge: "+ez+" | "+l_uri_edges.size());
					} else {
						//						Edge ez=new Edge(this, k_next, r);
						//						km.L.myPrintln("    ** Dropped Edge: "+ez+" | "+l_uri_edges.size());			
					}
				}
			}
		}
	}


	public static boolean b_validCode(int i_key) {
		switch(i_key) {
		case i_Empty:		return false;
		default:			return true;
		}
	}
	public static boolean b_validGhostCode(int i_key) {
		switch(i_key) {
		case i_Empty:		return false;
		case i_NonStdCode:	return false;
		default:			return true;
		}
	}

	public boolean sameRowCol(Key k) throws Exception {
		if(k==null){
			return false;
		}
		if(is(k)) {
			return false;
		}
		if((k.i_row==(-1))||(k.i_col==(-1))||(i_row==(-1))||(i_col==(-1))){
			return false;
		}
		return ((k.i_row==i_row)||(k.i_col==i_col));
	}

	public boolean sameCol(Key k) throws Exception {
		if(k==null){
			return false;
		}
		if(is(k)) {
			return false;
		}
		if((k.i_row==(-1))||(k.i_col==(-1))||(i_row==(-1))||(i_col==(-1))){
			return false;
		}
		return (k.i_col==i_col);
	}

	public boolean sameRow(Key k) throws Exception {
		if(k==null){
			return false;
		}
		if(is(k)) {
			return false;
		}
		if((k.i_row==(-1))||(k.i_col==(-1))||(i_row==(-1))||(i_col==(-1))){
			return false;
		}
		return (k.i_row==i_row);
	}


	static int getIntegerInString(String s) {
		try {
			Matcher matcher = Pattern.compile("\\d+").matcher(s);
			matcher.find();					
			return Integer.valueOf(matcher.group());
		} catch (Exception e) {
			return (-1);
		}	
	}

	static int getCode(KbdMatrix km, String s_next) throws Exception {
		if(s_next.length()==0) return i_Empty;
		try {
			return Integer.valueOf(s_next);
		} catch (NumberFormatException e) {
			int i_weirdCodeIndex = s_weirdCodes.indexOf(s_next.trim().toLowerCase());
			if(i_weirdCodeIndex>=0) {
				int i_code = getIntegerInString(s_weirdCodes.substring(i_weirdCodeIndex));
				if(i_code<i_NonStdCode) {
					return i_code;
				} else {
					km.l_nonStdCode.add(s_next);
					return i_NonStdCode+km.l_nonStdCode.indexOf(s_next);
				}
			} else if((s_next.toLowerCase().indexOf("kp ")>=0)||
					((s_next.trim().indexOf("F")==0)&&(s_next.trim().length()>1))
			) {
				Iterator<String> it_SmbLst= SmbLst.iterator();
				while(it_SmbLst.hasNext()){
					String s_keycode=it_SmbLst.next();
					if(s_keycode.indexOf(s_next.trim())>=0) {
						return SmbLst.indexOf(s_keycode);
					}
				}
				km.l_nonStdCode.add(s_next);
				return i_NonStdCode+km.l_nonStdCode.indexOf(s_next);
			} else {
				int i_nonstdcodeIndex = km.l_nonStdCode.indexOf(s_next);
				if(i_nonstdcodeIndex<0) {
					km.l_nonStdCode.add(s_next);
				}
				return i_NonStdCode+km.l_nonStdCode.indexOf(s_next);
			}
		}
	}

	static boolean isCode4Frequency(int i_code){
		// key in "ABCDEFGHIJKLMNOPQRSTUVWXYZ '"
		if((i_code>16)&&(i_code<27)) { // 1st row of qwerty KBD
			return true;
		}
		if((i_code>30)&&(i_code<40)) { // 2nd row of qwerty KBD
			return true;
		}
		if(i_code==41) { // ' of qwerty KBD
			return true;
		}
		if((i_code>45)&&(i_code<53)) { // 3rd row of qwerty KBD
			return true;
		}
		if(i_code==61) { // space of qwerty KBD
			return true;
		}
		return false;
	}

	boolean isStdKeyWHQL(ArrayList<Integer> l_StdModCode){
		if(l_StdModCode.contains(i_codeKey)) {
			return false;
		}
		return (i_codeKey>0)&&(i_codeKey<i_WHQL_Limit);
	}
	boolean isStdKey(ArrayList<Integer> l_StdModCode){
		if(l_StdModCode.contains(i_codeKey)) {
			return false;
		}
		return (i_codeKey>0)&&(i_codeKey<i_NonStdCode);
	}

	public boolean isStdKeyNotWHQL() {
		return isStdKeyNotWHQL(i_codeKey);
	}

	public static boolean isStdKeyNotWHQL(int i_code) {
		return (i_code>=i_WHQL_Limit)&&(i_code<i_NonStdCode);
	}

	boolean sameRowAndCol(ArrayList<Integer> l_StdModRow, ArrayList<Integer> l_StdModCol){
		return l_StdModRow.contains(i_row)&&l_StdModCol.contains(i_col);
	}

	public String toString() {
		if(node!=null) {
			return new String(node.toString());
			//			return new String(
			//					node.toString()+String.format(":[u%1$7.3f]",d_volt));
			//			return new String("{[r"+i_row+",c"+i_col+"] "+
			//					node.toString()+String.format(":[u%1$7.3f] :",d_volt)+l_uri_edges.size()+"}");
		} else {
			return new String(toString(km));
		}
	}

	public String toString2() {
		if(node!=null) {
			return new String("{[r"+i_row+",c"+i_col+"] "+
					node.toString()+" "+i_codeKey);
		} else {
			return new String(toString(km));
		}
	}

	public String toStringStress() {
		StringBuffer sb = new StringBuffer("[r"+i_row+",c"+i_col+"] "+node.toString());
		if(d_volt!=SINK_VOLT) {
			sb.append(String.format(":[u%1$7.4f]",d_volt));
		}
		return sb.toString();
	}


	public String toString(KbdMatrix km) {
		if(i_codeKey<i_NonStdCode) {
			return new String(SmbLst.get(i_codeKey).trim());
		} else {
			return new String(km.l_nonStdCode.get(i_codeKey-i_NonStdCode).trim());
		}
		//		if(i_codeKey<i_NonStdCode) {
		//			return new String("{[r:"+i_row+", c:"+i_col+"]["+i_codeKey+"] "+SmbLst.get(i_codeKey).trim()+"}");
		//		} else {
		//			return new String("{[r:"+i_row+", c:"+i_col+"]["+i_codeKey+"] "+km.l_nonStdCode.get(i_codeKey-i_NonStdCode).trim()+"}");
		//		}
	}

	public static String toString(KbdMatrix km, int i_code) throws Exception {
		try {
			if(i_code<i_NonStdCode) {
				return new String(SmbLst.get(i_code));
			} else {
				String s_code = km.l_nonStdCode.get(i_code-i_NonStdCode);
				return new String(String.format(" [%1$4s] ",s_code));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.toString()+"["+i_code+"]");
		}
	}

	public String toString3() {
		if(i_codeKey<i_NonStdCode) {
			return new String(""+i_codeKey);
		} else {
			String s_code = km.l_nonStdCode.get(i_codeKey-i_NonStdCode);
			return new String(s_code);
		}
	}


	public static String list2String(KbdMatrix km, ArrayList<Integer> l_code) throws Exception {
		StringBuffer sb = new StringBuffer();
		Iterator<Integer> it_code = l_code.iterator();
		while(it_code.hasNext()) {
			int i_code = it_code.next();
			sb.append(Key.toString(km, i_code));
		}
		return sb.toString();
	}

	static final int i_AppCode = 131;
	static final int i_MuhenkanCode = 132;
	static final int i_HenkanCode = 133;
	static final int i_KataHiraCode = 134;
	static final int i_HanjaCode = 135;
	static final int i_HangEngCode = 136;
	static final int i_KP_EqualCode = 137;
	static final int i_StdKeyLim = 140;
	static final int i_Btn1Code = i_StdKeyLim + 1;
	static final int i_Btn2Code = i_StdKeyLim + 2;
	static final int i_Btn3Code = i_StdKeyLim + 3;
	static final int i_EmptyCode = 150;
	static final int i_LeftFnCode = 151;
	static final int i_RightFnCode = 152;
	static final int i_EjectCode = 153;
	static final int i_WHQL_Limit = 127;
	static final int i_GhostKey = 149;
	static final int i_NonStdCode = 154;
	static final int i_Empty = 0;
	static final int i_LShift = 44;
	static final int i_RShift = 57;
	static final int i_LCtrl = 58;
	static final int i_RCtrl = 64;
	static final int i_LAlt = 60;
	static final int i_RAlt = 62;
	static final int i_LGUI = 59;
	static final int i_RGUI = 63;

	static String s_error = new String(" Error ");

	static String s_weirdCodes = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ"+i_NonStdCode +"App "+i_AppCode+" Muh "+
			i_MuhenkanCode+" Hen "+i_HenkanCode+" K/H katahira"+i_KataHiraCode+
			" Han KRHanja "+i_HanjaCode+" H/E "+i_HangEngCode+" KP= "+i_KP_EqualCode+
			" Btn1 "+i_Btn1Code+" Btn2 "+i_Btn2Code+" Btn3 "+i_Btn3Code+
			" Fn LFn "+i_LeftFnCode+" RFn "+i_RightFnCode+" Ejct "+i_EjectCode+ 
			" LeftArrow Left Arrow "+79+" RightArrow Right Arrow "+89+
			" UpArrow Up Arrow "+83+" DownArrow DnArrow Down Arrow "+84+
			" Home "+80+" End "+81+" Enter "+43+" PageUp Page Up "+85+" PageDn PageDown Page Down "+86+
			" space "+61+" capslock capslck "+30+" numlock numlck "+90+" tab "+16+" esc "+110+
			" insert "+75+" delete "+76+" backspace Bkspace "+15+
			" LShift L-Shift "+44+" LCtrl L-Ctrl "+58+" LAlt L-Alt "+60+" LGUI L-GUI "+59+
			" RShift R-Shift "+57+" RCtrl R-Ctrl "+64+" RAlt R-Alt "+62+" RGUI R-GUI "+63+
	" ").toLowerCase(); // Fn is LFn

	//	the rules for Braille can be summarized as follows: any combination of the Space, D, F, J, K, L, and S must not generate a ghost key.
	static String s_Braille = new String(" SDFJKL");

	static ArrayList<Integer> l_Game1Code = new ArrayList<Integer>(Arrays.asList(18,19,31,32,33,34));
	static ArrayList<Integer> l_Game2Code = new ArrayList<Integer>(Arrays.asList(79,83,84,89));
	static ArrayList<Integer> l_Game3Code = new ArrayList<Integer>(Arrays.asList(92,93,96,97,98,102,103));
	static ArrayList<Integer> l_Game4Code = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,15,16,17,20,21,22,28,
			29,30,35,36,42,43,44, 45,46,47,48,49,50,57,58,59,60,61,64,75,76,80,81,85,86,90,
			91,95,99,100,101,104,105,106,108,Key.i_AppCode));

	static ArrayList<String> SmbLst = new ArrayList<String>(Arrays.asList(
			"        ",
			//   1          2          3          4          5
			" `   ~  "," 1   !  "," 2   @  "," 3   /  "," 4   $  ",
			//   6          7          8          9         10     \
			" 5   %  "," 6   ^  "," 7   &  "," 8   *  "," 9   (  ",
			//  11         12         13         14         15     \
			" 0   )  "," -   _  "," =   +  "," ¥   |  ","Bkspace ",
			//  16         17         18         19         20     \
			"  Tab   ","   Q    ","   W    ","   E    ","   R    ",
			//  21         22         23         24         25     \
			"   T    ","   Y    ","   U    ","   I    ","   O    ",
			//  26         27         28         29         30     \
			"   P    "," [   {  "," ]   }  "," \\   |  ","CapsLck ",
			//  31         32         33         34         35     \
			"   A    ","   S    ","   D    ","   F    ","   G    ",
			//  36         37         38         39         40     \
			"   H    ","   J    ","   K    ","   L    "," ;   :  ",
			//  41         42         43         44         45     \
			"  '  \"  "," $   £  "," Enter  ","L-Shift "," <   >  ",
			//  46         47         48         49         50     \
			"   Z    ","   X    ","   C    ","   V    ","   B    ",
			//  51         52         53         54         55     \
			"   N    ","   M    "," ,   <  "," .   >  ","   / ?  ",
			//  56         57         58         59         60     \
			"   Ro   ","R-Shift "," L-Ctrl "," L-GUI  "," L-Alt  ",
			//  61         62         63         64         65     \
			" Space  "," R-Alt  "," R-GUI  "," R-Ctrl ",  s_error,  
			//  66         67         68         69         70     \
			s_error,   s_error,   s_error,   s_error,   s_error,  
			//  71         72         73         74         75     \
			s_error,   s_error,   s_error,   s_error,  " Insert ",
			//  76         77         78         79         80     \
			" Delete ", s_error,   s_error,  "L-Arrow ","  Home  ",
			//  81         82         83         84         85     \
			"  End   ", s_error,  "UpArrow ","DnArrow "," PageUp ",
			//  86         87         88         89         90     \
			" PageDn ", s_error,   s_error,  "R-Arrow "," NumLck ",
			//  91         92         93         94         95     \
			"  KP 7  ","  KP 4  ","  KP 1  ",  s_error,  "  KP /  ",
			//  96         97         98         99        100     \
			"  KP 8  ","  KP 5  ","  KP 2  ","  KP 0  ","  KP *  ",
			// 101        102        103        104        105     \
			"  KP 9  ","  KP 6  ","  KP 3  ","  KP .  ","  KP -  ",
			// 106        107        108        109        110     \
			"  KP +  ","  KP ,  ","KP Enter",  s_error, "  Esc   ",
			// 111        112        113        114        115     \
			s_error,  "   F1   ","   F2   ","   F3   ","   F4   ",
			// 116        117        118        119        120     \
			"   F5   ","   F6   ","   F7   ","   F8   ","   F9   ",
			// 121        122        123        124        125     \
			"  F10   ","  F11   ","  F12   ","PrntScrn"," ScrLck ",
			// 126        127        128        129        130     \
			"PauseBrk",  s_error,   s_error,   s_error,   s_error,  
			// 131        132        133        134        135     \
			"  App   ","Muhenkan"," Henkan ","KataHira"," Hanja  ",
			// 136        137        138        139        140     \
			"HangEng ","  KP =  ",  s_error,   s_error,   s_error,  
			// 141        142        143        144        145     \
			" L-Button","R-Button","M-Button",  s_error,   s_error,  
			// 146        147        148        149        150     \
			s_error,     "ROW",     "COL",   "GhostKey",  "        ",
			// 151        152        153        154                \
			"  L-Fn  ","  R-Fn  "," Eject  "," NonStd "));


	@Override
	public int compareTo(Key k) {
		if(k==null) {
			return 1;
		} else if(k.node==null) {
			return k.i_codeKey-i_codeKey;
		} else if(node==null) {
			return k.i_codeKey-i_codeKey;
		} else {
			return node.compareTo(k.node);
		}
	}

	@Override
	public boolean equals(Object o) {
		Key k=(Key)o;
		return is(k);
	}

	public boolean isMembraneNode() {
		return ((i_row==-1)&&(i_col==-1));
	}

}