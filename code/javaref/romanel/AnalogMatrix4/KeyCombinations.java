/*
$Header: KeyCombinations.java: Revision: 2: Author: emariacher: Date: Tuesday, July 07, 2009 3:50:59 PM$

$Log$
emariacher - Tuesday, July 07, 2009 3:50:59 PM
clean "clear pressed keys". not 1 shot anymore...
 */
import java.util.ArrayList;
import java.util.Arrays;


public class KeyCombinations {
	GraphPanel graphPanel;
	Key k_startCol, k_endRow;

	public KeyCombinations(GraphPanel graphPanel) {
		this.graphPanel = graphPanel;
		k_startCol = graphPanel.k_startCol;
		k_endRow   = graphPanel.k_endRow;
	}

	ArrayList<Result> getKeysCombination2Press(ArrayList<Key> l_quarto) throws Exception {
		ArrayList<Result> l_KeysCombination2PressMaster = new ArrayList<Result>();
		ArrayList<Key> l_keys2Press= new ArrayList<Key>();

		// test 4 keys alone
		for(Key k : l_quarto) {
			l_keys2Press= new ArrayList<Key>();
			l_keys2Press.add(k);
			l_KeysCombination2PressMaster.add(new Result(l_keys2Press));
		}

		// test each couple
		for(Key k1 : l_quarto) {
			for(Key k2 : l_quarto) {
				if(k1.compareTo(k2)>0) {
					l_keys2Press= new ArrayList<Key>();
					l_keys2Press.add(k1);
					l_keys2Press.add(k2);
					l_KeysCombination2PressMaster.add(new Result(l_keys2Press));
				}
			}
		}

		// test each trio
		for(Key k1 : l_quarto) {
			for(Key k2 : l_quarto) {
				for(Key k3 : l_quarto) {
					if((k1.compareTo(k2)>0)&&(k2.compareTo(k3)>0)) {
						l_keys2Press= new ArrayList<Key>();
						l_keys2Press.add(k1);
						l_keys2Press.add(k2);
						l_keys2Press.add(k3);
						l_KeysCombination2PressMaster.add(new Result(l_keys2Press));
					}
				}
			}
		}

		// test the quarto
		if(l_quarto.size()>AnalyzeMatrix.MAX_KEY_PRESSED) {
			l_KeysCombination2PressMaster.add(new Result(l_quarto));
		}

		return l_KeysCombination2PressMaster;
	}

	ArrayList<Result> getKeysNonQuartoCombination2Press(Result l_quartoKeys2Press) throws Exception {
		ArrayList<Result> l_KeysCombination2Press = new ArrayList<Result>();

		Result l_keys2Press;
		// add reference combination
		//		l_KeysCombination2Press.add(l_quartoKeys2Press);


		// for each of previous combinations add 2, 3 or 4 keys of another quarto on the same row
		ArrayList<Result> l_nonQuartoKeysCombinationRow = getSameRowKeys(k_startCol,
				k_endRow);
		for(Result l_nonQuartoKeys2Press : l_nonQuartoKeysCombinationRow) {
			l_keys2Press= new Result(l_quartoKeys2Press,l_nonQuartoKeys2Press);
			if(l_keys2Press.l_key2press.size()<=AnalyzeMatrix.MAX_KEY_PRESSED) {
				l_KeysCombination2Press.add(l_keys2Press);
			}
		}

		// for each of previous combinations add 2, 3 or 4 keys of another quarto on the same col
		ArrayList<Result> l_nonQuartoKeysCombinationCol = getSameColKeys(k_startCol,
				k_endRow);
		for(Result l_nonQuartoKeys2Press : l_nonQuartoKeysCombinationCol) {
			l_keys2Press= new Result(l_quartoKeys2Press,l_nonQuartoKeys2Press);
			if(l_keys2Press.l_key2press.size()<=AnalyzeMatrix.MAX_KEY_PRESSED) {
				l_KeysCombination2Press.add(l_keys2Press);
			}
		}

		return l_KeysCombination2Press;
	}

	ArrayList<Key> getQuartoKeys(Key col, Key row) throws Exception {
		ArrayList<Key> l_QuartoKeys= new ArrayList<Key>();
		ArrayList<Node> l_nodes = new ArrayList<Node>(Arrays.asList(graphPanel.nodes));
		for(Node n: l_nodes) {
			if(n==null) {
				break;
			}
			Key k = n.key;
			if((!k.isRow())&&(!k.isCol())&&(!k.isMembraneNode())) {
				if((k.findRow()==row)&&(k.findCol()==col)) {
					if(l_QuartoKeys.contains(k)) {
						throw new Exception(l_QuartoKeys+" already contains "+k);
					}
					l_QuartoKeys.add(k);
				}
			}
		}
		return l_QuartoKeys;
	}

	Key findCol(int i_col) {
		ArrayList<Node> l_nodes = new ArrayList<Node>(Arrays.asList(graphPanel.nodes));
		for(Node n: l_nodes) {
			if(n==null) {
				break;
			}
			Key k = n.key;
			if(k.isCol()&&(k.i_col==i_col)) {
				return k;
			}
		}
		return null;
	}

	Key findRow(int i_row) {
		ArrayList<Node> l_nodes = new ArrayList<Node>(Arrays.asList(graphPanel.nodes));
		for(Node n: l_nodes) {
			if(n==null) {
				break;
			}
			Key k = n.key;
			if(k.isRow()&&(k.i_row==i_row)) {
				return k;
			}
		}
		return null;
	}



	private ArrayList<Result> getSameColKeys(Key col, Key row) throws Exception {
		ArrayList<Result> l_KeysCombination2Press = new ArrayList<Result>();

		int i_col=col.i_col;
		int i_row=row.i_row;
		ArrayList<Key> l_Col = new ArrayList<Key>();

		ArrayList<Node> l_nodes = new ArrayList<Node>(Arrays.asList(graphPanel.nodes));
		for(Node n: l_nodes) {
			if(n==null) {
				break;
			}
			Key k = n.key;
			if((!k.isRow())&&(!k.isCol())&&(!k.isMembraneNode())) {
				if(k.i_col/2==i_col) {
					if(k.i_row/2!=i_row) {
						l_Col.add(k);
					}
				}
			}
		}

		// look for couple
		int i_size=l_Col.size();
		for(int i=0;i<i_size;i++) {
			Key k1 = l_Col.get(i);
			for(int j=i+1;j<i_size;j++) {
				Key k2 = l_Col.get(j);
				if(k1.i_row/2==k2.i_row/2) {
					ArrayList<Key> l_keys2press = new ArrayList<Key>();
					l_keys2press.add(k1);
					l_keys2press.add(k2);
					l_KeysCombination2Press.add(new Result(l_keys2press));
				}
			}

		}
		// look for trios
		for(int i=0;i<i_size;i++) {
			Key k1 = l_Col.get(i);
			for(int j=i+1;j<i_size;j++) {
				Key k2 = l_Col.get(j);
				for(int k=j+1;k<i_size;k++) {
					Key k3 = l_Col.get(k);
					if((k1.i_row/2==k2.i_row/2)&&(k1.i_row/2==k3.i_row/2)) {
						ArrayList<Key> l_keys2press = new ArrayList<Key>();
						l_keys2press.add(k1);
						l_keys2press.add(k2);
						l_keys2press.add(k3);
						l_KeysCombination2Press.add(new Result(l_keys2press));
					}
				}
			}
		}

		// look for quartos: not useful only test 4 keys combination

		return l_KeysCombination2Press;
	}
	private ArrayList<Result> getSameRowKeys(Key col, Key row) throws Exception {
		ArrayList<Result> l_KeysCombination2Press = new ArrayList<Result>();

		int i_col=col.i_col;
		int i_row=row.i_row;
		ArrayList<Key> l_Row = new ArrayList<Key>();

		ArrayList<Node> l_nodes = new ArrayList<Node>(Arrays.asList(graphPanel.nodes));
		for(Node n: l_nodes) {
			if(n==null) {
				break;
			}
			Key k = n.key;
			if((!k.isRow())&&(!k.isCol())&&(!k.isMembraneNode())) {
				if(k.i_row/2==i_row) {
					if(k.i_col/2!=i_col) {
						l_Row.add(k);
					}
				}
			}
		}

		// look for couple
		int i_size=l_Row.size();
		for(int i=0;i<i_size;i++) {
			Key k1 = l_Row.get(i);
			for(int j=i+1;j<i_size;j++) {
				Key k2 = l_Row.get(j);
				if(k1.i_col/2==k2.i_col/2) {
					ArrayList<Key> l_keys2press = new ArrayList<Key>();
					l_keys2press.add(k1);
					l_keys2press.add(k2);
					l_KeysCombination2Press.add(new Result(l_keys2press));
				}
			}

		}
		// look for trios
		for(int i=0;i<i_size;i++) {
			Key k1 = l_Row.get(i);
			for(int j=i+1;j<i_size;j++) {
				Key k2 = l_Row.get(j);
				for(int k=j+1;k<i_size;k++) {
					Key k3 = l_Row.get(k);
					if((k1.i_col/2==k2.i_col/2)&&(k1.i_col/2==k3.i_col/2)) {
						ArrayList<Key> l_keys2press = new ArrayList<Key>();
						l_keys2press.add(k1);
						l_keys2press.add(k2);
						l_keys2press.add(k3);
						l_KeysCombination2Press.add(new Result(l_keys2press));
					}
				}
			}
		}

		// look for quartos: not useful only test 4 keys combination


		return l_KeysCombination2Press;
	}

	public void addEdges(ArrayList<Key> l_keys) throws Exception {
		ArrayList<Edge> l_edges = new ArrayList<Edge>();
		for (int i = 0 ; i < graphPanel.nedges_backup ; i++) {
			Edge e = graphPanel.edges_backup[i];
			if(e.active) {
				l_edges.add(e);
			}
		}	
		ArrayList<Edge> l_uri_edges = new ArrayList<Edge>();
		for(Key k : l_keys) {
			for(Edge e : k.l_uri_edges) {
				if(!l_uri_edges.contains(e)) {
					l_uri_edges.add(e);
				}
			}
		}

		for(Edge e : l_edges) {
			//			graphPanel.addEdge(e);
			graphPanel.addEdge(e.n_from.key, e.n_to.key, e.r.d_ohms, e.edge_type);
		}
//		for(Edge e : l_uri_edges) {
//			e.updateURI();
//		}

		graphPanel.stress=true;
	}

	int removeDeadEnds() throws Exception {
		int i_in_remove_dead_ends_mode=0;
		for (int i = 0 ; i < graphPanel.nnodes ; i++) {
			Node n1 = graphPanel.nodes[i];
			i_in_remove_dead_ends_mode += n1.key.remove_dead_ends();
		}
		return i_in_remove_dead_ends_mode;
	}

}
