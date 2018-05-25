import java.util.ArrayList;


public class Edge implements Comparable<Edge> {
	int from;
	int to;
	Node n_to=null;
	Node n_from=null;

	double len;
	Resistor r=null;

	double d_current=0.0;

	boolean active=false; // some current goes through row and/or col
	boolean b_notYetComputed=true;
	boolean b_diodeOnCol=false;

	int edge_type;
	private ArrayList<Edge> l_edge_chain;

	static final int EE_EDGE_TYPE = 0; // used for connecting rows and cols between themselves
	static final int ROW_EDGE_TYPE = 1;
	static final int COL_EDGE_TYPE = 2;
	static final int URI_EDGE_TYPE = 3;

	Edge(Key k_from, Key k_to, Resistor r) {
		n_to=k_to.node;
		n_from=k_from.node;
		this.r=r;
		edge_type=URI_EDGE_TYPE;
	}

	public Edge(int i_from, int i_to, int len2, int edgetype, double d) {
		from=i_from;
		to=i_to;
		len=len2;
		edge_type=edgetype;
		r=new Resistor(d);
	}

	public void spread_current() throws Exception {
		if(b_diodeOnCol) {
			if(n_to.key.isCol()&&!n_to.key.b_isExtremity) {
				return;
			}
			if(n_from.key.isCol()&&!n_from.key.b_isExtremity) {
				return;
			}
		}
		active=true;
		n_to.key.spread_current(edge_type);
		n_from.key.spread_current(edge_type);
	}

	public void spread_current(int zedgetype) throws Exception {
		if(b_diodeOnCol) {
			if(n_to.key.isCol()&&!n_to.key.b_isExtremity) {
				return;
			}
			if(n_from.key.isCol()&&!n_from.key.b_isExtremity) {
				return;
			}
		}
		active=true;
		n_to.key.spread_current(zedgetype);
		n_from.key.spread_current(zedgetype);
	}

	@Override
	public int compareTo(Edge e) {
		if((n_to==null)||(n_from==null)) {
			return (10000*(e.from-from))+(e.to-to);
		} else {
			return (10000*e.n_from.compareTo(n_from))+e.n_to.compareTo(n_to);
		}
	}

	//	@Override
	//	public boolean equals(Object o) {
	//		return compareTo((Edge)o)==0;
	//	}

	static String printEdgeType(int i_edge_type) {
		switch(i_edge_type) {
		case EE_EDGE_TYPE:
			return new String("EE");
		case COL_EDGE_TYPE:
			return new String("COL");
		case ROW_EDGE_TYPE:
			return new String("ROW");
		case URI_EDGE_TYPE:
			return new String("URI");
		default:
			return new String("NOGOOD: "+i_edge_type);	
		}
	}

	@Override
	public String toString() {
		return ("{" + n_to.toString() + " -> "+ n_from.toString() + " "+ printEdgeType(edge_type)+
				String.format(" :u%1$9.5f=r%2$s*i%3$9.5f}",tension(),r, d_current));
	}

	public double computeCurrent() {
		double d_newCurrent=(n_from.key.d_volt - n_to.key.d_volt)/r.d_ohms;
		double d_source_volt = n_from.key.d_source_volt;
		if(tension()>(d_source_volt-Key.SINK_VOLT)) {
			d_current=(d_source_volt-Key.SINK_VOLT)/r.d_ohms;
		} else if(tension()<(Key.SINK_VOLT-d_source_volt)) {
			d_current=(Key.SINK_VOLT-d_source_volt)/r.d_ohms;
		}
		double d_DeltaCurrent=d_newCurrent-d_current;
		d_current=d_newCurrent;
		if(d_DeltaCurrent>0.0) {
			return d_DeltaCurrent;
		} else {
			return 0.0 - d_DeltaCurrent;
		}
	}

	public double tension() {
		return r.d_ohms*d_current;
	}

	public void reset() {
		active=false;
		b_notYetComputed=true;
		d_current=0.0;
	}

	public String toStringStress() {
		//		return (String.format(" :u%1$9.5f=r%2$6.2f*i%3$9.5f}",tension(),r.d_ohms, d_current));
		return (String.format("r%1$6.2f", r.d_ohms));
	}

	public void updateURI() {
		for(Edge e : l_edge_chain) {
			e.d_current=d_current;
		}
	}

	public void add(ArrayList<Edge> l_edge_chain) {
		this.l_edge_chain = l_edge_chain;	
	}

	public String toStringCsv() {
		return new String(n_from.key.toString3()+","+n_to.key.toString3()+","+r.d_ohms);
	}

}
