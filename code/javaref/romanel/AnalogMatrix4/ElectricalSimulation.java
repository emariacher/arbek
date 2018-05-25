/*
$Header: ElectricalSimulation.java: Revision: 13: Author: emariacher: Date: Wednesday, July 08, 2009 6:33:13 PM$

$Log$
emariacher - Wednesday, July 08, 2009 6:33:13 PM
get all ghost keys. not debugged yet.
emariacher - Wednesday, July 08, 2009 3:46:31 PM
emariacher - Tuesday, July 07, 2009 5:39:50 PM
real matrix scan seems to be somewhat working...
emariacher - Friday, July 03, 2009 2:45:55 PM
regularly re-initialize log file.
emariacher - Thursday, July 02, 2009 6:00:01 PM
check actual vs computed.
emariacher - Wednesday, July 01, 2009 4:07:44 PM
static ghost key detection with "analyze key pressed" button
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeMap;


public class ElectricalSimulation {
	Log L;

	AnalyzeMatrix am;


	public ElectricalSimulation(Log l, AnalyzeMatrix am) {
		super();
		L = l;
		this.am = am;
	}


	public ArrayList<Key> simplifyNetwork(Key k_startCol) throws Exception {
		ArrayList<Key> l_key_level1= new ArrayList<Key>();
		ArrayList<Key> l_key_level2= new ArrayList<Key>();
		ArrayList<Key> l_key_level3= new ArrayList<Key>();
		ArrayList<Key> l_key_level4= new ArrayList<Key>();
		ArrayList<Key> l_key_level5= new ArrayList<Key>();
		ArrayList<Key> l_keys = new ArrayList<Key>();

		if(am.trace()) {
			L.myPrintln("**********************************************************");
			//		L.myPrintln("simplifyNetwork("+k_startCol.toString()+");");
			L.myPrintln("Key Pressed: "+getNodesPressed().toString());
			//		L.myPrintln("");
		}
		// level 0
		k_startCol.findNextNodeList();
		// level 1
		for(Edge e: k_startCol.l_uri_edges) {
			Key  k = e.n_to.key;
			if(!l_key_level1.contains(k)) {
				l_key_level1.add(k);
			}
		}
		if(am.trace()) {
			L.myPrintln("\nLevel1 keys: "+l_key_level1);
		}

		for(Key k: l_key_level1) {
			k.findNextNodeList();
		}
		// level 2
		for(Key k: l_key_level1) {
			for(Edge e2: k.l_uri_edges) {
				Key k2 = e2.n_to.key;
				if(!l_key_level2.contains(k2)) {
					l_key_level2.add(k2);
				}
			}
		}
		l_key_level2.removeAll(l_key_level1);
		if((l_key_level2.size()>0)&&am.trace()) {
			L.myPrintln("\nLevel2 keys: "+l_key_level2);
		}

		for(Key k2: l_key_level2) {
			k2.findNextNodeList();
		}
		// level 3
		for(Key k2: l_key_level2) {
			for(Edge e3: k2.l_uri_edges) {
				Key k3 = e3.n_to.key;
				if(!l_key_level3.contains(k3)) {
					l_key_level3.add(k3);
				}
			}
		}
		l_key_level3.removeAll(l_key_level2);
		if((l_key_level3.size()>0)&&am.trace()) {
			L.myPrintln("\nLevel3 keys: "+l_key_level3);
		}
		for(Key k3: l_key_level3) {
			k3.findNextNodeList();
		}

		// level 4
		for(Key k3: l_key_level3) {
			for(Edge e4: k3.l_uri_edges) {
				Key k4 = e4.n_to.key;
				if(!l_key_level4.contains(k4)) {
					l_key_level4.add(k4);
				}
			}
		}
		l_key_level4.removeAll(l_key_level3);
		if((l_key_level4.size()>0)&&am.trace()) {
			L.myPrintln("\nLevel4 keys: "+l_key_level4);
		}
		for(Key k4: l_key_level4) {
			k4.findNextNodeList();
		}

		// level 5
		for(Key k4: l_key_level4) {
			for(Edge e5: k4.l_uri_edges) {
				Key k5 = e5.n_to.key;
				if(!l_key_level5.contains(k5)) {
					l_key_level5.add(k5);
				}
			}
		}
		l_key_level5.removeAll(l_key_level4);
		if((l_key_level5.size()>0)&&am.trace()) {
			L.myPrintln("\nLevel5 keys: "+l_key_level5);
		}
		for(Key k5: l_key_level5) {
			k5.findNextNodeList();
		}

		l_keys.add(k_startCol);
		for(Key k: l_key_level1) {
			l_keys.add(k);
		}
		for(Key k: l_key_level2) {
			l_keys.add(k);
		}
		for(Key k: l_key_level3) {
			l_keys.add(k);
		}
		for(Key k: l_key_level4) {
			l_keys.add(k);
		}
		for(Key k: l_key_level5) {
			l_keys.add(k);
		}

		for(Key k: l_keys) {
			for(Edge e: k.l_uri_edges) {
				if(!e.n_to.key.l_uri_edges.contains(e)) {
					e.n_to.key.l_uri_edges.add(e);
				}
				if(!e.n_from.key.l_uri_edges.contains(e)) {
					e.n_from.key.l_uri_edges.add(e);
				}
			}
		}

		return l_keys;
	}

	private void computeVoltages(Key k_startCol, Key k_endRow) {
		ArrayList<Key> l_key_level1= new ArrayList<Key>();
		ArrayList<Key> l_key_level2= new ArrayList<Key>();
		ArrayList<Key> l_key_level3= new ArrayList<Key>();
		ArrayList<Key> l_key_level4= new ArrayList<Key>();
		ArrayList<Key> l_key_level5= new ArrayList<Key>();

		for(Edge e: k_startCol.l_uri_edges) {
			Key  k = e.n_to.key;
			if(k.compareTo(k_endRow)!=0) {
				k.updateVoltage(e,k_startCol);
			}
			if(!l_key_level1.contains(k)) {
				l_key_level1.add(k);
			}
		}
		for(Key k: l_key_level1) {
			for(Edge e2: k.l_uri_edges) {
				Key k2 = e2.n_to.key;
				if(k2.compareTo(k_endRow)!=0) {
					k2.updateVoltage(e2,k);
				}
				if(!l_key_level2.contains(k2)) {
					l_key_level2.add(k2);
				}
			}
		}
		l_key_level2.removeAll(l_key_level1);
		for(Key k2: l_key_level2) {
			for(Edge e3: k2.l_uri_edges) {
				Key k3 = e3.n_to.key;
				if(k3.compareTo(k_endRow)!=0) {
					k3.updateVoltage(e3,k2);
				}
				if(!l_key_level3.contains(k3)) {
					l_key_level3.add(k3);
				}
			}
		}
		l_key_level3.removeAll(l_key_level2);
		for(Key k3: l_key_level3) {
			for(Edge e4: k3.l_uri_edges) {
				Key k4 = e4.n_to.key;
				if(k4.compareTo(k_endRow)!=0) {
					k4.updateVoltage(e4,k3);
				}
				if(!l_key_level4.contains(k4)) {
					l_key_level4.add(k4);
				}
			}
		}
		l_key_level4.removeAll(l_key_level3);
		for(Key k4: l_key_level4) {
			for(Edge e5: k4.l_uri_edges) {
				Key k5 = e5.n_to.key;
				if(k5.compareTo(k_endRow)!=0) {
					k5.updateVoltage(e5,k4);
				}
				if(!l_key_level5.contains(k5)) {
					l_key_level5.add(k5);
				}
			}
		}
		l_key_level5.removeAll(l_key_level5);
		k_endRow.d_volt=Key.SINK_VOLT;
	}

	private void resetEdgeComputed(ArrayList<Key> l_keys) {
		for(Key k: l_keys) {
			for(Edge e:k.l_uri_edges) {
				e.b_notYetComputed=true;
			}
		}
	}

	private double equilibrateCurrents(Key k_startCol, Key k_endRow, ArrayList<Key> l_keys) {
		double d_currentDelta = 0.0;

		for(Key k: l_keys) {
			if((k.compareTo(k_startCol)!=0)&&(k.compareTo(k_endRow)!=0)) {
				d_currentDelta += k.equilibrateCurrents();
			}
		}
		return d_currentDelta;
	}

	private double computeCurrents(ArrayList<Key> l_keys) {
		resetEdgeComputed(l_keys);
		double d_currentDelta = 0.0;

		for(Key k: l_keys) {
			for(Edge e:k.l_uri_edges) {
				if(e.b_notYetComputed) {
					d_currentDelta += e.computeCurrent();
				}
			}
		}
		return d_currentDelta;
	}
	public double compute_current(Key k_startCol, Key k_endRow, ArrayList<Key> l_keys) throws Exception {
		double d_currentDelta=Key.PRECISION_CURRENT+1.0;
		if(!l_keys.contains(k_startCol)||!l_keys.contains(k_endRow)) {
			L.myErrPrintln("    ***"+k_startCol.node.lbl+" or "+k_endRow.node.lbl+
					" are not part of the key list.\n      Key pressed:"+getNodesPressed()+
					"\n      key list: "+ l_keys+
					"\n      active nodes: "+ am.graphPanel.getActiveNodes()
			);
			throw new Exception(k_startCol.node.lbl+" or "+k_endRow.node.lbl+
					" are not part of the key list.\n      Key pressed:"+getNodesPressed()+
					"\n      key list: "+ l_keys+
					"\n      active nodes: "+ am.graphPanel.getActiveNodes());
		}
		// initialize randomly voltages in the network except for source and sink
		for(Key k: l_keys) {
			if(k.compareTo(k_startCol)==0) {
				k.d_volt=k.d_source_volt;
			} else if(k.compareTo(k_endRow)==0) {
				k.d_volt=Key.SINK_VOLT;
			} else {
				k.d_volt=Key.SINK_VOLT+((new Random().nextDouble())*(k.d_source_volt-Key.SINK_VOLT));
			}
		}
		//		L.myPrint("    ["+k_startCol.node.lbl+"->"+k_endRow.node.lbl+"]: ");

		while(d_currentDelta>Key.PRECISION_CURRENT) {
			//			L.myPrint(String.format(", %1$7.3f",d_currentDelta));
			//			print("after compute voltages", d_currentDelta, l_keys);
			// compute current in each branch based on previous voltages
			d_currentDelta = computeCurrents(l_keys);
			//			print("after compute currents", d_currentDelta, l_keys);
			// equilibrate current in each node
			d_currentDelta += equilibrateCurrents(k_startCol, k_endRow, l_keys);
			//			print("after equilibrate currents", d_currentDelta, l_keys);
			// recompute voltages
			computeVoltages(k_startCol, k_endRow);
		}
		if(am.trace()) {
			print("["+k_startCol.node.lbl+"->"+k_endRow.node.lbl+"]", d_currentDelta, l_keys);
		}
		double d_currentOut = k_endRow.getInOutCurrent();
		//		L.myPrintln(String.format("i=%1$9.5f",d_currentOut));
		if(d_currentOut<Key.PRECISION_CURRENT) {
			L.myErrPrintln(String.format("***Current Error: i=%1$9.5f",d_currentOut));
			//			throw new Exception(String.format("***Current Error: i=%1$9.5f",k_endRow.getInCurrent()));
		} else {
			double d_currentIn=k_startCol.getInOutCurrent();
			double d_currentdiff=d_currentIn+d_currentOut;
			if (d_currentdiff<0.0) {
				d_currentdiff=0.0-d_currentdiff;
			}
			if(d_currentdiff>(6*Key.PRECISION_CURRENT)) {
				throw new Exception(String.format("i_in=%1$11.7f vs i_out=%2$11.7f (%3$11.7f %4$11.7f)",
						d_currentIn, d_currentOut, Key.PRECISION_CURRENT, d_currentdiff));
			}
		}
		return d_currentOut;

	}

	private void print(String s_title, double d_currentDelta, ArrayList<Key> l_keys) throws Exception {
		L.myPrintln("*****"+s_title+"*****"+String.format("%1$6.3f ",d_currentDelta));
		for(Key k: l_keys) {
			L.myPrintln("  "+k);
			for(Edge e:k.l_uri_edges) {
				L.myPrintln("        "+e);
			}
		}
	}

	public ArrayList<Node> getNodesPressed() {
		ArrayList<Node> l_NodesPressed= new ArrayList<Node>();
		ArrayList<Node> l_nodes = new ArrayList<Node>(Arrays.asList(am.graphPanel.nodes));
		for(Node n: l_nodes) {
			if(n==null) {
				break;
			}
			if(n.key_pressed) {
				l_NodesPressed.add(n);
			}
		}
		return l_NodesPressed;
	}


	public Result check_actual_vs_computed(Key kcol, Key krow, double d_current) throws Exception {
		ColRow cr = new ColRow(kcol,krow);
		ArrayList<Key> l_actual_keys = getKeysPressed(cr);
		if(!am.km.tm_ADtable.isEmpty()) {
			L.myPrintln(new String(cr+" Overall key pressed: "+getNodesPressed()+
					String.format("    d_current= %1$9.5f",d_current)));
			ArrayList<Key> l_computed_keys = compute_keys(cr, d_current);
			L.myPrintln(new String("   Chk!"+cr+" a:"+l_actual_keys+" vs c:"+l_computed_keys+
					"    overall key pressed: "+getNodesPressed()+
					String.format("    d_current= %1$9.5f",d_current)));
			if((l_computed_keys.containsAll(l_actual_keys))&&(l_actual_keys.containsAll(l_computed_keys))) {
				//				L.myErrPrintln(new String("OK!"+cr+" a:"+l_actual_keys+
				//						String.format("    d_current= %1$9.5f",d_current)));
				return new Result(new String("OK:"+cr+" "+l_actual_keys), true);
			} else {
				L.myErrPrintln(new String("KO!"+cr+" a:"+l_actual_keys+" vs c:"+l_computed_keys+
						"    overall key pressed: "+getNodesPressed()+
						String.format("    d_current= %1$9.5f",d_current)));
				Ghost g = new Ghost(cr, getKeysPressed(), l_actual_keys, l_computed_keys);
				am.ts_ghostKeys.add(g);
				return new Result(new String("KO!"+cr+" a:"+l_actual_keys+" vs c:"+l_computed_keys+
						"    overall key pressed: "+getNodesPressed()+
						String.format("    d_current= %1$9.5f",d_current)), false);
			}
		} else {
			return new Result(new String(cr+" "+l_actual_keys), false);
		}
	}


	private ArrayList<Key> getKeysPressed(ColRow cr) {
		ArrayList<Key> l_actual_keys = new ArrayList<Key>();
		ArrayList<Node> l_NodesPressed= getNodesPressed();
		for(Node n : l_NodesPressed) {
			if(n.belongs2(cr)) {
				l_actual_keys.add(n.key);
			}
		}
		return l_actual_keys;
	}

	ArrayList<Key> getKeysPressed() {
		ArrayList<Key> l_actual_keys = new ArrayList<Key>();
		ArrayList<Node> l_NodesPressed= getNodesPressed();
		for(Node n : l_NodesPressed) {
			l_actual_keys.add(n.key);
		}
		return l_actual_keys;
	}


	private ArrayList<Key> compute_keys(ColRow cr, double d_current) throws Exception {
		TreeMap<ColRow, ADitem> tm_ADtable = am.normalizeADtable(am.km.tm_ADtable);
		ADitem adi = tm_ADtable.get(cr);
		return adi.compute_keys(d_current);
	}



}
