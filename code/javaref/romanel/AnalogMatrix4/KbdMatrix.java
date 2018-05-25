/*
$Header: KbdMatrix.java: Revision: 14: Author: emariacher: Date: Friday, July 10, 2009 2:27:20 PM$

$Log$
emariacher - Friday, July 10, 2009 2:27:20 PM
improved logging for building ghost key detection algorythm
emariacher - Friday, July 10, 2009 1:50:59 PM
just some logging stuff
emariacher - Wednesday, July 08, 2009 2:16:41 PM
do some stats when pressing all the 3 other keys (in a ghost square).
emariacher - Thursday, July 02, 2009 3:55:43 PM
add log & header surround keywords
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;


public class KbdMatrix {
	// full key list
	ArrayList<Key> l_ZeKeys = new ArrayList<Key>();
	// full matrix with empty positions: entry by row
	ArrayList<ArrayList<Integer>> l_ZeMatrixFull = new ArrayList<ArrayList<Integer>>();

	// nonStd codes List
	ArrayList<String> l_nonStdCode = new ArrayList<String>();

	private int i_maxrow=0;
	private int i_maxcol=0;

	Log L;

	GraphPanel graphPanel;

	TreeMap<ColRow, ADitem> tm_ADtable = new TreeMap<ColRow, ADitem>();
	TreeMap<ColRow, ADitem> tm_Reftable = new TreeMap<ColRow, ADitem>();



	KbdMatrix(File f_MatrixCsv, Log L) throws Exception{
		this.L=L;
		int i_rowcpt=0;
		Scanner sc = new Scanner(f_MatrixCsv);
		while (sc.hasNextLine()) {
			String s_linef = sc.nextLine();
			if(Pattern.compile("ROW\\s*\\d+").matcher(s_linef.toUpperCase()).find()) {
				L.myPrintln(i_rowcpt+": "+s_linef);
				ArrayList<Integer> l_Row = new ArrayList<Integer>();
				l_ZeMatrixFull.add(l_Row);
				ArrayList<String> l_skeys = new ArrayList<String>(Arrays.asList(s_linef.split(",")));
				Iterator<String> col = l_skeys.iterator();
				int i_colcpt=0;
				while(col.hasNext()){
					int i_codeKey=Key.getCode(this, col.next());
					l_Row.add(i_codeKey);				
					i_colcpt++;
				}
				if(i_colcpt>i_maxcol){
					i_maxcol=i_colcpt-1;
				}
				l_Row.remove(0); // 1st cell contained Row XX
				i_rowcpt++;
			}
		}

		i_maxrow=i_rowcpt;
		// add a position to rows that have an empty position at last column
		Iterator<ArrayList<Integer>> it_row = l_ZeMatrixFull.iterator();
		while(it_row.hasNext()){
			ArrayList<Integer> l_row = it_row.next();
			while(l_row.size()<i_maxcol){
				//				L.myPrintln("Adding empty cells at end of row.");
				l_row.add(Key.i_Empty);
			}
		}
		buildKeyList();
	}

	void buildKeyList() {
		Iterator<ArrayList<Integer>> row = l_ZeMatrixFull.iterator();
		int cptrow=0;
		while(row.hasNext()){
			int cptcol=0;
			Iterator<Integer> col = row.next().iterator();
			while(col.hasNext()){
				l_ZeKeys.add(new Key(cptrow,cptcol,col.next(),this));
				cptcol++;		
			}
			cptrow++;
		}
		l_ZeKeys.add(new Key(Key.i_GhostKey, this));
	}

	public Key findCode(int i_code) {
		Iterator<Key> it_keys = l_ZeKeys.iterator();	
		while(it_keys.hasNext()){
			Key k = it_keys.next();
			if(k.is(i_code)){
				return k;
			}
		}
		return null;
	}


	public String toString() {
		Formatter formatter = new Formatter();
		StringBuffer sb= new StringBuffer();
		Iterator<Integer> col = l_ZeMatrixFull.get(0).iterator();
		sb.append("\n        !");
		int cptcol=0;
		while(col.hasNext()){
			formatter.format("  %2d    !",cptcol);
			col.next();
			cptcol++;
		}
		sb.append(formatter.toString());
		sb.append("\n");

		Iterator<ArrayList<Integer>> row = l_ZeMatrixFull.iterator();
		int cptrow=0;
		while(row.hasNext()){
			formatter = new Formatter();
			formatter.format("  %2d    !",cptrow);
			sb.append(formatter.toString());
			cptcol=0;
			col = row.next().iterator();
			while(col.hasNext()){
				try {
					sb.append(Key.toString(this,col.next())+"!");
				} catch (Exception e) {
					e.printStackTrace();
				}
				cptcol++;		
			}
			sb.append("\n");
			cptrow++;
		}
		return sb.toString();
	}

	public String toStringKeyCode() {
		Formatter formatter = new Formatter();
		StringBuffer sb= new StringBuffer();
		Iterator<Integer> col = l_ZeMatrixFull.get(0).iterator();
		sb.append("\n        !");
		int cptcol=0;
		while(col.hasNext()){
			formatter.format("  %2d    !",cptcol);
			col.next();
			cptcol++;
		}
		sb.append(formatter.toString());
		sb.append("\n");

		Iterator<ArrayList<Integer>> row = l_ZeMatrixFull.iterator();
		int cptrow=0;
		while(row.hasNext()){
			formatter = new Formatter();
			formatter.format("  %2d    !",cptrow);
			sb.append(formatter.toString());
			cptcol=0;
			col = row.next().iterator();
			while(col.hasNext()){
				try {
					sb.append(String.format("  %1$3d   !",col.next()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				cptcol++;		
			}
			sb.append("\n");
			cptrow++;
		}
		return sb.toString();
	}






	int getMaxRow() {
		return i_maxrow;
	}
	int getMaxCol() {
		return i_maxcol;
	}
	public int i_getValue(int i_rowcpt, int i_colcpt){
		return l_ZeMatrixFull.get(i_rowcpt).get(i_colcpt);
	}

	public String s_getValue(int i_rowcpt, int i_colcpt) throws Exception{
		//		L.myPrintln("s_getValue("+i_rowcpt+","+i_colcpt+");");
		//		L.myPrintln("s_getValue2("+i_rowcpt+","+i_colcpt+")="+
		//				l_ZeMatrixFull.get(i_rowcpt).get(i_colcpt));
		//		L.myPrintln("s_getValue3("+i_rowcpt+","+i_colcpt+")="+
		//				key.toString(this, l_ZeMatrixFull.get(i_rowcpt).get(i_colcpt)));
		return new String(Key.toString(this, l_ZeMatrixFull.get(i_rowcpt).get(i_colcpt))+i_rowcpt+i_colcpt);
	}

	private boolean b_validCode(int i_rowcpt, int i_colcpt) {
		try {
			return Key.b_validCode(l_ZeMatrixFull.get(i_rowcpt).get(i_colcpt));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void addToGraph(GraphPanel zgraphPanel) throws Exception {
		graphPanel=zgraphPanel;
		int i_maxrow=getMaxRow();
		int i_maxcol=getMaxCol();
		ArrayList<Integer> l_rowConnected = new ArrayList<Integer>();
		for(int i_row=0;i_row<i_maxrow;i_row++) {
			l_rowConnected.add(-1);
		}
		ArrayList<Integer> l_colConnected = new ArrayList<Integer>();
		for(int i_col=0;i_col<i_maxcol;i_col++) {
			l_colConnected.add(-1);
		}
		for(int i_row=0;i_row<i_maxrow;i_row++) {
			for(int i_col=0;i_col<i_maxcol;i_col++) {
				L.myPrintln("addToGraph("+i_row+","+i_col+")=");
				L.myPrintln("                  "+b_validCode(i_row,i_col));
				if(b_validCode(i_row,i_col)) {
					L.myPrintln("addToGraph("+i_row+","+i_col+")="+s_getValue(i_row,i_col));
					L.myPrintln("  l_rowConnected.get("+i_row+")="+l_rowConnected.get(i_row));
					if(l_rowConnected.get(i_row)==-1) {
						graphPanel.addEdge(new Key(i_row/2,Key.ROW_TYPE,this,0), new Key(i_row,i_col,this), 100, Edge.ROW_EDGE_TYPE);
						l_rowConnected.set(i_row, i_col);
					} else {
						int i_col_diff=i_col-l_rowConnected.get(i_row);
						//						L.myPrintln("  i_col_diff="+i_col_diff);
						graphPanel.addEdge(new Key(i_row,l_rowConnected.get(i_row),this), 
								new Key(i_row,i_col,this), i_col_diff*80, Edge.ROW_EDGE_TYPE);
						l_rowConnected.set(i_row, i_col);						
					}
					L.myPrintln("   l_rowConnected.set("+i_row+")="+l_rowConnected.get(i_row));
					L.myPrintln("  l_colConnected.get("+i_col+")="+l_colConnected.get(i_col));
					if(l_colConnected.get(i_col)==-1) {
						graphPanel.addEdge(new Key(i_col/2,Key.COL_TYPE,this,0), new Key(i_row,i_col,this), 100, Edge.COL_EDGE_TYPE);
						l_colConnected.set(i_col, i_row);
					} else {
						int i_row_diff=i_row-l_colConnected.get(i_col);
						//						L.myPrintln("  i_row_diff="+i_row_diff);
						graphPanel.addEdge(new Key(l_colConnected.get(i_col),i_col,this), 
								new Key(i_row,i_col,this), i_row_diff*40, Edge.COL_EDGE_TYPE);
						l_colConnected.set(i_col, i_row);						
					}
					L.myPrintln("   l_colConnected.set("+i_col+")="+l_colConnected.get(i_col));
				}
			}
		}
	}

	public void readMembrane(File file, GraphPanel graphPanel2) throws Exception {
		graphPanel=graphPanel2;
		int i_edgetype=0;
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String s_linef = sc.nextLine();
			if(Pattern.compile("\\S+,\\S+,\\d+").matcher(s_linef.toUpperCase()).find()) {
				if(Pattern.compile("ROW\\d+,\\S+,\\d+").matcher(s_linef.toUpperCase()).find()) {
					i_edgetype=Edge.ROW_EDGE_TYPE;
				}
				if(Pattern.compile("COL\\d+,\\S+,\\d+").matcher(s_linef.toUpperCase()).find()) {
					i_edgetype=Edge.COL_EDGE_TYPE;
				}
				//				L.myPrintln(s_linef);
				ArrayList<String> l_skeys = new ArrayList<String>(Arrays.asList(s_linef.split(",")));
				int i_s=Key.getCode(this, l_skeys.get(0));
				int i_e=Key.getCode(this, l_skeys.get(1));
				//				L.myPrintln("i_s:"+i_s+", i_e:"+i_e);
				Key k_KeyStart = new Key(i_s, this);
				Key k_KeyEnd = new Key(i_e, this);
				//				L.myPrintln("k_s:"+k_KeyStart+"!"+l_ZeKeys.indexOf(k_KeyStart)+
				//						", k_e:"+k_KeyEnd+"!"+l_ZeKeys.indexOf(k_KeyEnd));
				if(l_ZeKeys.indexOf(k_KeyStart)<0) {
					l_ZeKeys.add(k_KeyStart);
				}
				if(l_ZeKeys.indexOf(k_KeyEnd)<0) {
					l_ZeKeys.add(k_KeyEnd);
				}
				k_KeyStart = l_ZeKeys.get(l_ZeKeys.indexOf(k_KeyStart));
				k_KeyEnd = l_ZeKeys.get(l_ZeKeys.indexOf(k_KeyEnd));
				double d_resistor = Double.valueOf(l_skeys.get(2));

				graphPanel2.addEdge(k_KeyStart, k_KeyEnd, d_resistor, i_edgetype);
			}
		}
		//check that each key is connected to at least 1 row and 1 col
		for(Key k : l_ZeKeys) {
			k.get_active_edges();
			if(k.i_edges>0) {
				if((k.i_col_edges<1)||(k.i_row_edges<1)) {
					if(!k.isRow()&&!k.isCol()&&!k.isMembraneNode()) {
						throw new Exception(k.toString2()+" is not fully connected to the Matrix. edge_row="+
								k.i_row_edges+" edge_col="+k.i_col_edges);
					}
				}
			}
		}
	}

	ArrayList<Result> getNonSameRowColKeys(ArrayList<Result> combination2PressMaster) throws Exception {
		ArrayList<Result> l_KeysCombination2Press = new ArrayList<Result>();

		for(Result r : combination2PressMaster) {
			if(r.l_key2press.size()==1) {
				Key k = r.l_key2press.get(0);
				int i_kcol=k.i_col;
				int i_krow=k.i_row;

				for(int i_row=0;i_row<i_maxrow;i_row++) {
					if(i_row!=i_krow) {
						for(int i_col=0;i_col<i_maxcol;i_col++) {
							if(i_col!=i_kcol) {
								ArrayList<Key> l_keys2press = get4Keys(i_kcol, i_col, i_krow, i_row);
								if(l_keys2press.size()==4) {
									ColRow cr = new ColRow(i_kcol, i_krow);
									ColRow cr_other = new ColRow(i_col, i_row);
									l_KeysCombination2Press.add(new Result(r, l_keys2press,cr, cr_other));
								}
							}
						}
					}
				}
			}
		}
		return l_KeysCombination2Press;
	}

	ArrayList<Result> get3otherKeys(ArrayList<Key> l_keysQuarto) throws Exception {
		ArrayList<Result> l_KeysCombination2Press = new ArrayList<Result>();

		for(Key k : l_keysQuarto) {
			int i_kcol=k.i_col;
			int i_krow=k.i_row;

			for(int i_row=0;i_row<i_maxrow;i_row++) {
				if(i_row!=i_krow) {
					for(int i_col=0;i_col<i_maxcol;i_col++) {
						if(i_col!=i_kcol) {
							ArrayList<Key> l_keys2press = get3otherKeys(i_kcol, i_col, i_krow, i_row);
							if(l_keys2press.size()==3) {
								ColRow cr = new ColRow(i_kcol, i_krow);
								ColRow cr_other = new ColRow(i_col, i_row);
								l_KeysCombination2Press.add(new Result(l_keys2press,cr, cr_other));
							}
						}
					}
				}
			}
		}
		return l_KeysCombination2Press;
	}

	private ArrayList<Key> get4Keys(int i_kcol, int i_col, int i_krow, int i_row) {
		ArrayList<Key> l_keys2press = new ArrayList<Key>();
		Key k_first = findKey(i_krow, i_kcol);
		if(k_first!=null) {
			l_keys2press.add(k_first);

			Key k_row = findKey(i_krow, i_col);
			if(k_row!=null) {
				l_keys2press.add(k_row);
			}
			Key k_col = findKey(i_row, i_kcol);
			if(k_col!=null) {
				l_keys2press.add(k_col);
			}
			Key k_fourth = findKey(i_row, i_col);
			if(k_fourth!=null) {
				l_keys2press.add(k_fourth);
			}
		}
		return l_keys2press;
	}

	private ArrayList<Key> get3otherKeys(int i_kcol, int i_col, int i_krow, int i_row) {
		ArrayList<Key> l_keys2press = new ArrayList<Key>();
		Key k_first = findKey(i_krow, i_kcol);
		if(k_first!=null) {
			//l_keys2press.add(k_first);

			Key k_row = findKey(i_krow, i_col);
			if(k_row!=null) {
				l_keys2press.add(k_row);
			}
			Key k_col = findKey(i_row, i_kcol);
			if(k_col!=null) {
				l_keys2press.add(k_col);
			}
			Key k_fourth = findKey(i_row, i_col);
			if(k_fourth!=null) {
				l_keys2press.add(k_fourth);
			}
		}
		return l_keys2press;
	}

	private Key findKey(int i_row, int i_col) {
		for(Key k : l_ZeKeys) {
			if((k.i_row==i_row)&&(k.i_col==i_col)&&(k.i_codeKey>0)) {
				return k;
			}
		}
		return null;
	}

	public void readADtable(File ADtableFile) throws FileNotFoundException {
		Scanner sc = new Scanner(ADtableFile);
		while (sc.hasNextLine()) {
			String s_linef = sc.nextLine();
			if(Pattern.compile("\\d+,AD,\\d+,\\d+,.*").matcher(s_linef.toUpperCase()).find()) {
				ArrayList<String> l_skeys = new ArrayList<String>(Arrays.asList(s_linef.split(",")));
				ColRow cr = new ColRow(l_skeys.get(2),l_skeys.get(3));
				ADitem adi = tm_ADtable.get(cr);
				if(adi==null) {
					tm_ADtable.put(cr, new ADitem(l_skeys, this));
				} else {
					adi.add(l_skeys);
					tm_ADtable.put(cr,adi);
				}
			} else if(Pattern.compile("\\d+,REF,\\d+,\\d+,.*").matcher(s_linef.toUpperCase()).find()) {
				ArrayList<String> l_skeys = new ArrayList<String>(Arrays.asList(s_linef.split(",")));
				ColRow cr = new ColRow(l_skeys.get(2),l_skeys.get(3));
				ADitem adi = tm_Reftable.get(cr);
				if(adi==null) {
					tm_Reftable.put(cr, new ADitem(l_skeys, this));
				} else {
					adi.add(l_skeys);
					tm_Reftable.put(cr,adi);
				}
			}
		}
	}

	public Key getKey(int i_codeKey) {
		for(Key k : l_ZeKeys) {
			if(k.is(i_codeKey)) {
				return k;
			}
		}
		return null;
	}
}
