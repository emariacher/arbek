/*
 * $Log$
 * emariacher - Wednesday, November 25, 2009 4:54:17 PM
 * trying to kbdm_FnKey...
 * emariacher - Wednesday, November 25, 2009 3:25:37 PM
 * kbd_map.c file generated
 * emariacher - Wednesday, November 18, 2009 9:47:53 AM
 * no bug in sight
 * today kbdmap.c
 * tomorrow kbdmap.c
 * emariacher - Tuesday, November 17, 2009 6:06:35 PM
 * still debugging...
 * emariacher - Tuesday, November 17, 2009 12:41:58 PM
 * decl table corrected
 * emariacher - Tuesday, November 17, 2009 12:14:13 PM
 * more colors
 * emariacher - Tuesday, November 17, 2009 11:47:07 AM
 * matrix in html part I
 * emariacher - Friday, November 13, 2009 6:12:38 PM
 * added a lot of WHQL keys
 * emariacher - Friday, November 13, 2009 4:40:13 PM
 * still debugging checkduplicate keys
 * emariacher - Thursday, November 12, 2009 4:44:35 PM
 * suppressed the smblist
 * still some exceptions.
 * emariacher - Thursday, November 12, 2009 3:40:17 PM
 * YES!
 * emariacher - Thursday, November 12, 2009 1:47:40 PM
 * a version to generate some reusable table
 * emariacher - Thursday, November 12, 2009 12:22:58 PM
 * almost working... no exception anymore
 * emariacher - Thursday, November 12, 2009 10:21:58 AM
 * fails on [A] / calculator keys
 * emariacher - Wednesday, November 11, 2009 3:55:07 PM
 * OK with whqlpos
 * emariacher - Wednesday, November 11, 2009 2:07:09 PM
 * emariacher - Monday, October 05, 2009 2:22:25 PM
 * CounterStrike rules added
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class KbdMatrix {
	// full key list
	ArrayList<key> l_ZeKeys = new ArrayList<key>();
	// full matrix with empty positions: entry by row
	ArrayList<ArrayList<Integer>> l_ZeMatrixFull = new ArrayList<ArrayList<Integer>>();
	// allGhosts for the matrix
	ArrayList<MasterTriplet> l_allGhosts = new ArrayList<MasterTriplet>();
	// useful Ghosts for the matrix
	ArrayList<String> l_MasterTripletList     = new ArrayList<String>();

	// nonStd codes List
	ArrayList<String> l_nonStdCode = new ArrayList<String>();

	KeyRefTable krt = new KeyRefTable();


	int i_maxrow=0;
	int i_maxcol=0;

	Log L;

	// parse main matrix csv file
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
					String s_key = col.next();
					if(!Pattern.compile("ROW\\s*\\d+").matcher(s_key.toUpperCase()).find()) {
						int i_codeKey=key.getCode(this, s_key);
						l_Row.add(i_codeKey);				
						i_colcpt++;
					}
				}
				if(i_colcpt>i_maxcol){
					i_maxcol=i_colcpt-1;
				}
				//				l_Row.remove(0); // 1st cell contained Row XX
				i_rowcpt++;
			}
		}

		i_maxrow=i_rowcpt;
		// add a position to rows that have an empty position at last column
		Iterator<ArrayList<Integer>> it_row = l_ZeMatrixFull.iterator();
		while(it_row.hasNext()){
			ArrayList<Integer> l_row = it_row.next();
			while(l_row.size()<i_maxcol){
				L.myPrintln("Adding empty cells at end of row.");
				l_row.add(KeyRefTable.i_Empty);
			}
		}
		buildKeyList();
		initStdModLists();
	}

	void buildKeyList() {
		Iterator<ArrayList<Integer>> row = l_ZeMatrixFull.iterator();
		int cptrow=0;
		while(row.hasNext()){
			int cptcol=0;
			Iterator<Integer> col = row.next().iterator();
			while(col.hasNext()){
				key k = new key(cptrow,cptcol,col.next());
				if(k.isFnKey(krt)) {
					k.s_alternate_type = new String("kbdm_FN_KEY");
				}
				l_ZeKeys.add(k);
				cptcol++;		
			}
			cptrow++;
		}

	}

	// parse alternative matrix csv file, needs main matrix as an input
	KbdMatrix(File f_MatrixCsv, KbdMatrix km, Log L) throws Exception{
		this.L=L;
		Iterator<ArrayList<Integer>> it_kmrow = km.l_ZeMatrixFull.iterator();
		while(it_kmrow.hasNext()){
			ArrayList<Integer> l_col = new ArrayList<Integer>();
			l_ZeMatrixFull.add(l_col);	
			Iterator<Integer> it_kmcol = it_kmrow.next().iterator();
			while(it_kmcol.hasNext()){
				l_col.add(KeyRefTable.i_Empty);
				it_kmcol.next();
			}
		}
		Scanner sc = new Scanner(f_MatrixCsv);
		while (sc.hasNextLine()) {
			String s_linef = sc.nextLine();
			Matcher m_old_csv = Pattern.compile(",\\d+,.+").matcher(s_linef.toUpperCase());
			Matcher m_new_csv = Pattern.compile("\\w,\\d+,\\d*").matcher(s_linef.toUpperCase());
			if(m_new_csv.find()) {
				L.myPrintln("New Alternate Matrix: "+s_linef);
				ArrayList<String> l_skeys = new ArrayList<String>(Arrays.asList(s_linef.split(",")));
				Iterator<String> it = l_skeys.iterator();
				String s_alternate_type=it.next();
				int i_kmcode=0;
				int i_altcode=0;
				try {
					i_kmcode= Integer.valueOf(it.next());
				} catch (Exception e) {
					L.myErrPrintln(e.toString()+"["+s_linef+"] i_kmcode="+i_kmcode);
				}
				try {
					i_altcode= Integer.valueOf(it.next());
				} catch (Exception e) {
					L.myPrintln(e.toString()+"["+s_linef+"] i_altcode="+i_altcode);
				}
				key k= km.findCode(i_kmcode);
				l_ZeMatrixFull.get(k.i_row).set(k.i_col, i_altcode);
				key k_alt = new key(k.i_row, k.i_col, i_altcode);
				if(s_alternate_type!=null) {
					k.s_alternate_type = s_alternate_type;
					k.k_alt = k_alt;
				}
			} else if(m_old_csv.find()) {			
				L.myPrintln("Old Fn Matrix: "+s_linef);
				ArrayList<String> l_skeys = new ArrayList<String>(Arrays.asList(s_linef.split(",")));
				if(l_skeys.size()==3) {
					Iterator<String> it = l_skeys.iterator();
					it.next();
					int i_kmcode=0;
					int i_fncode=0;
					try {
						i_kmcode=Integer.valueOf(it.next());
						i_fncode = key.getCode(this, it.next());
						key k= km.findCode(i_kmcode);
						l_ZeMatrixFull.get(k.i_row).set(k.i_col, i_fncode);
					} catch (Exception e) {
						L.myErrPrintln(e.toString()+"["+s_linef+"] i_kmcode="+i_kmcode+" i_fncode="+ i_fncode);
						e.printStackTrace();
					}
				}
			}
		}
		buildKeyList();
		initStdModLists();
	}

	String checkDuplicateKeys(KbdMatrix kmfn) throws Exception{
		// initialize result list
		StringBuffer sbd= new StringBuffer("\nDuplicate keys:");
		StringBuffer sbm= new StringBuffer("\nMissing keys:");
		// process main matrix
		Iterator<ArrayList<Integer>> it_row = l_ZeMatrixFull.iterator();
		int i_row=0;
		int i_col;
		while(it_row.hasNext()){
			i_col=0;
			Iterator<Integer> it_col = it_row.next().iterator();
			while(it_col.hasNext()){
				//				L.myPrintln("    i_col="+i_col);
				int i_index=it_col.next();
				L.myPrintln(    "[r"+i_row+", c"+i_col+"]="+i_index);
				try {			
					krt.m_KeyRefTable.get(i_index).i_cpt++;
					L.myPrintln(    "[r"+i_row+", c"+i_col+"]="+i_index+ " "+
							krt.m_KeyRefTable.get(i_index).toString());
				} catch (NullPointerException e) {
					try {
						L.myErrPrintln("NullPointerException checkDuplicateKeys[r"+i_row+", c"+i_col+"]="+i_index);
						krt.m_KeyRefTable.put(i_index, 
								new KeyRef(i_index, 
										key.toString(this, i_index), 
										key.toString(this, i_index)));
						krt.m_KeyRefTable.get(i_index).i_cpt++;
						L.myPrintln(    "[r"+i_row+", c"+i_col+"]="+i_index+ " "+
								krt.m_KeyRefTable.get(i_index).toString());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				} 				
				i_col++;
			}
			i_row++;
		}

		// process Fn matrix
		it_row = kmfn.l_ZeMatrixFull.iterator();
		i_row=0;
		while(it_row.hasNext()){
			i_col=0;
			Iterator<Integer> it_col = it_row.next().iterator();	
			while(it_col.hasNext()){
				//				L.myPrintln("    i_col="+i_col);
				int i_index=it_col.next();
				try {
					krt.m_KeyRefTable.get(i_index).i_cpt++;
					L.myPrintln(    "[r"+i_row+", c"+i_col+"]="+i_index+ " "+
							krt.m_KeyRefTable.get(i_index).toString());
				} catch (NullPointerException e) {
					try {
						L.myErrPrintln("NullPointerException checkDuplicateKeys[r"+i_row+", c"+i_col+"]="+i_index);
						krt.m_KeyRefTable.put(i_index, 
								new KeyRef(i_index, 
										key.toString(this, i_index), 
										key.toString(this, i_index)));
						krt.m_KeyRefTable.get(i_index).i_cpt++;
						L.myPrintln(    "[r"+i_row+", c"+i_col+"]="+i_index+ " "+
								krt.m_KeyRefTable.get(i_index).toString());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				} 				
				i_col++;
			}
			i_row++;
		}

		// print result list
		for (Entry<Integer, KeyRef> keyref : krt.m_KeyRefTable.entrySet()) {
			int i_count = keyref.getValue().i_cpt;
			L.myPrintln(    "    ----  "+ keyref.toString());
			if(i_count>1){
				sbd.append("\n  \""+keyref.getValue().toStringDoc()+"\" (whqlpos:"+keyref.getKey()+"), ");
				L.myPrintln(    "*******"+ keyref.toString());
			} else if(i_count==0){
				if(key.isStdKeyNotWHQL(keyref.getKey())) {
					sbm.append("\n  \""+keyref.getValue().toStringDoc()+"\" (whqlpos:"+keyref.getKey()+") Not WHQL,");	
				} else if(keyref.getKey()<=KeyRefTable.i_WHQL_Limit) {
					sbm.append("\n  \""+keyref.getValue().toStringDoc()+"\" (whqlpos:"+keyref.getKey()+"), ");							
				}			
			}
		}
		sbd.append(".\n"+sbm.toString()+".\n");
		return sbd.toString();
	}

	public void initStdModLists() {
		StdModLst.add(new keyGroup(this, KeyRefTable.i_LShift, KeyRefTable.i_RShift));
		StdModLst.add(new keyGroup(this, KeyRefTable.i_LCtrl, KeyRefTable.i_RCtrl));
		StdModLst.add(new keyGroup(this, KeyRefTable.i_LAlt, KeyRefTable.i_RAlt));
		StdModLst.add(new keyGroup(this, KeyRefTable.i_LGUI, KeyRefTable.i_LGUI));
		StdModLst.add(new keyGroup(this, KeyRefTable.i_RGUI, KeyRefTable.i_RGUI));

		Iterator<keyGroup> it_StdModLst = StdModLst.iterator();
		while(it_StdModLst.hasNext()) {
			keyGroup kg = it_StdModLst.next();
			if(kg.key1!=null) {
				if(!StdModKeyLst.contains(kg.key1)) {
					StdModKeyLst.add(kg.key1.i_codeKey);
				}
			}
			if(kg.key2!=null) {
				if(!StdModKeyLst.contains(kg.key2)) {
					StdModKeyLst.add(kg.key2.i_codeKey);
				}
			}
		}

		// also include alternate-function (Fn) keys
		StdModLstWithFn = new ArrayList<keyGroup>(StdModLst);
		StdModLstWithFn.add(new keyGroup(this, KeyRefTable.i_LeftFnCode, KeyRefTable.i_RightFnCode));		
	}

	public void checkWHQL(KbdMatrix kfnm) throws Exception {
		L.myErrPrintln("Check WHQL: ");


		// Therefore, Logitech's recommendation: any combination of ANY (MAC) number of  "StdModLst" keys (at most one modifier key per group):
		// and one "standard" key or the eject key must not generate a ghost key
		// meaning [each group from "StdModLstWithFn" must be on a different column AND different row]
		Iterator<keyGroup> it_StdModLstWithFn = StdModLstWithFn.iterator();
		while(it_StdModLstWithFn.hasNext()) {
			keyGroup kg = it_StdModLstWithFn.next();
			keyGroup kgother=kg.sameRowCol(this);
			if(kgother!=null) {
				L.myErrPrintln("  WHQL Error/Logitech MAC Guidelines: each group from \"StdModLstWithFn\" must be on a different column and row:\n    "+
						kg.toString(this) +" vs "+ kgother.toString(this) +".");
			}
		}
		// meaning [each "StdKey"  must be on a different column OR different row than any "StdModLst"]
		ArrayList<Integer> l_StdModRow  = keyGroup.getStdModRowColCode(StdModLst,0); // list of rows occupied by "StdModLst" keys
		ArrayList<Integer> l_StdModCol  = keyGroup.getStdModRowColCode(StdModLst,1); // list of cols occupied by "StdModLst" keys
		ArrayList<Integer> l_StdModCode = keyGroup.getStdModRowColCode(StdModLst,2); // list of "StdModLst" keys codes
		Iterator<key> it_keys = l_ZeKeys.iterator();	
		while(it_keys.hasNext()){
			key k = it_keys.next();
			if(k.isStdKeyWHQL(l_StdModCode)) {
				if(k.sameRowAndCol(l_StdModRow, l_StdModCol)) {
					if(partOfWHQLException(k)) {
						L.myErrPrintln("  Logitech Guidelines: each \"WHQLStdKey\"  must be on a different column OR different row than any \"StdModLst\":\n    "+
								k.toString(this) +" permitted by WHQL exception List.");								
					} else {
						L.myErrPrintln("  WHQL Error: each \"WHQLStdKey\"  must be on a different column OR different row than any \"StdModLst\":\n    "+
								k.toString(this) +".");		
					}
				}
			} else if(k.isStdKeyNotWHQL()) {
				if(k.sameRowAndCol(l_StdModRow, l_StdModCol)) {
					if(partOfWHQLException(k)) {
						L.myErrPrintln("  Logitech Guidelines: each \"NotWHQLStdKey\"  must be on a different column OR different row than any \"StdModLst\":\n    "+
								k.toString(this) +" But permitted by WHQL exception List.");								
					} else {
						L.myErrPrintln("  Logitech Guidelines: each \"NotWHQLStdKey\"  must be on a different column OR different row than any \"StdModLst\":\n    "+
								k.toString(this) +".");		
					}
				}
			}
		}
		// meaning [each Fn key whose output is a "StdKey"  must be on a different column OR different row than any "StdModLstWithFn"]
		l_StdModRow  = keyGroup.getStdModRowColCode(StdModLstWithFn,0); // list of rows occupied by "StdModLstWithFn" keys
		l_StdModCol  = keyGroup.getStdModRowColCode(StdModLstWithFn,1); // list of cols occupied by "StdModLstWithFn" keys
		l_StdModCode = keyGroup.getStdModRowColCode(StdModLstWithFn,2); // list of "StdModLstWithFn" keys codes
		it_keys = kfnm.l_ZeKeys.iterator();	
		while(it_keys.hasNext()){
			key k = it_keys.next();
			if(k.isStdKey(l_StdModCode)) {
				if(k.sameRowAndCol(l_StdModRow, l_StdModCol)) {
					L.myErrPrintln("  Logitech Warning: each Fn key whose output is a \"StdKey\"  must be on a different column OR different row than any \"StdModLstWithFn\": "+
							k.toString(kfnm) +".");					
				}
			} 		}
	}


	private boolean partOfWHQLException(key k) throws Exception {
		ArrayList<MasterTriplet> l_Ghosts = getAllGhosts(k.i_row,k.i_col);
		Iterator<MasterTriplet> it_Ghosts = l_Ghosts.iterator();
		while(it_Ghosts.hasNext()) {
			MasterTriplet mt =it_Ghosts.next();
			if(!mt.isPartOfWHQLException(StdModKeyLst, krt.WHQL_ExceptLst)) {
				return false;
			}
		}
		return true;
	}

	public String checkGaming() throws Exception {
		//		any combination of two keys in gaming zone 1 (cyan) and one key in gaming zone 4 (green) must not generate a ghost key,
		//		any combination of two keys in gaming zone 2 (magenta) and one key in gaming zone 4 (green) must not generate a ghost key, and
		//		any combination of two keys in gaming zone 3 (yellow) and one key in gaming zone 4 (green) must not generate a ghost key.
		StringBuffer sb = new StringBuffer("\nCheck Gaming: \n");
		String s_Game1Code = key.list2String(this, KeyRefTable.l_Game1Code);
		String s_Game2Code = key.list2String(this, KeyRefTable.l_Game2Code);
		String s_Game3Code = key.list2String(this, KeyRefTable.l_Game3Code);
		String s_Game4Code = key.list2String(this, KeyRefTable.l_Game4Code);
		Iterator<MasterTriplet> it_mt = l_allGhosts.iterator();
		while(it_mt.hasNext()){
			MasterTriplet m_MasterTriplet = it_mt.next();
			sb.append(m_MasterTriplet.checkGaming(s_Game1Code, s_Game2Code, s_Game3Code, s_Game4Code, L));
		}
		sb.append(".\n");
		return sb.toString();


	}

	public String checkBraille() {
		StringBuffer sb = new StringBuffer("\nCheck Braille: \n");
		Iterator<String> it_mt = l_MasterTripletList.iterator();
		while(it_mt.hasNext()){
			MasterTriplet m_MasterTriplet = new MasterTriplet(it_mt.next(), this);
			sb.append(m_MasterTriplet.checkBraille());
		}
		sb.append(".\n");
		return sb.toString();
	}

	public String checkGuitarHero() throws Exception {
		StringBuffer sb = new StringBuffer("\nCheck GuitarHero: \n");
		for(MasterTriplet m : l_allGhosts){
			sb.append(m.checkGuitarHero());
		}
		sb.append(".\n");
		return sb.toString();
	}

	public String checkCounterStrike() throws Exception {
		StringBuffer sb = new StringBuffer("\nCheck CounterStrike: \n");
		for(MasterTriplet m : l_allGhosts){
			sb.append(m.checkCounterStrike());
		}
		sb.append(".\n");
		return sb.toString();
	}




	public key findCode(int i_code) {
		Iterator<key> it_keys = l_ZeKeys.iterator();	
		while(it_keys.hasNext()){
			key k = it_keys.next();
			if(k.is(i_code)){
				return k;
			}
		}
		return null;
	}


	public String toStringHTML() {
		Formatter formatter = new Formatter();
		StringBuffer sb= new StringBuffer("</pre><table border=\"1\"><tr><th/>");
		Iterator<Integer> col = l_ZeMatrixFull.get(0).iterator();
		int cptcol=0;
		while(col.hasNext()){
			formatter.format("<th bgcolor=\"#eeeeee\">_____%d_____</th>",cptcol);
			col.next();
			cptcol++;
		}
		sb.append(formatter.toString());
		sb.append("</tr>\n");

		Iterator<ArrayList<Integer>> row = l_ZeMatrixFull.iterator();
		int cptrow=0;
		while(row.hasNext()){
			formatter = new Formatter();
			formatter.format("<tr><th bgcolor=\"#eeeeee\">_%2d_</th>",cptrow);
			sb.append(formatter.toString());
			cptcol=0;
			col = row.next().iterator();
			while(col.hasNext()){
				try {
					int i = col.next();
					if(i==0){
						sb.append("<td/>");
					}else {
						//						sb.append("<td bgcolor=\"#FFFFCC\">"+key.toString(this,i)+"</td>");
						sb.append("<td bgcolor=\"#"+ findCode(i).getColor(this)+"\">"+key.toString(this,i)+"</td>");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				cptcol++;		
			}
			sb.append("</tr>\n");
			cptrow++;
		}
		sb.append("</table>\n");
		sb.append("<pre>\n");
		return sb.toString();
	}

	public String toString() {
		Formatter formatter = new Formatter();
		StringBuffer sb= new StringBuffer();
		Iterator<Integer> col = l_ZeMatrixFull.get(0).iterator();
		sb.append("\n          !");
		int cptcol=0;
		while(col.hasNext()){
			formatter.format("   %2d     !",cptcol);
			col.next();
			cptcol++;
		}
		sb.append(formatter.toString());
		sb.append("\n");

		Iterator<ArrayList<Integer>> row = l_ZeMatrixFull.iterator();
		int cptrow=0;
		while(row.hasNext()){
			formatter = new Formatter();
			formatter.format("   %2d     !",cptrow);
			sb.append(formatter.toString());
			cptcol=0;
			col = row.next().iterator();
			while(col.hasNext()){
				try {
					int i = col.next();
					if(i==0){
						sb.append("          !");
					}else {
						sb.append(key.toString(this,i)+"!");
					}
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


	public ArrayList<MasterTriplet> getAllGhosts() throws Exception {
		int i_rowMax=getMaxRow();
		int i_colMax=getMaxCol();
		for(int i_row=0;i_row<i_rowMax;i_row++){
			for(int i_col=0;i_col<i_colMax;i_col++){
				l_allGhosts.addAll(getGhosts(i_row,i_col));
			}
		}

		L.myErrPrintln("Number of GhostKeys: "+l_allGhosts.size());
		L.myPrintln(ghosts2String("GhostKeys", l_allGhosts));

		return l_allGhosts;
	}

	public String ghosts2String(String s_id, ArrayList<MasterTriplet> l_Ghosts) throws Exception{
		StringBuffer sb = new StringBuffer(s_id+":\n");
		Iterator<MasterTriplet> it_Ghosts = l_Ghosts.iterator(); 
		int i_cpt=0;
		while(it_Ghosts.hasNext()){
			sb.append(it_Ghosts.next().toString2());
			//			sb.append("  "+i_cpt+"--"+s_id+"--"+ it_Ghosts.next().toString2()+"\n");
			i_cpt++;
		}
		return sb.toString();
	}

	public ArrayList<MasterTriplet> getGhosts(int i_rowcpt, int i_colcpt) throws Exception {
		ArrayList<MasterTriplet> l_Ghosts = new ArrayList<MasterTriplet>();
		//		L.myPrintln("  i_rowcpt="+i_rowcpt+", i_colcpt="+i_colcpt+s_getValue(i_rowcpt,i_colcpt));
		if(b_validCode(i_rowcpt,i_colcpt)){
			int i_rowMax=getMaxRow();
			int i_colMax=getMaxCol();
			for(int i_row=0;i_row<i_rowMax;i_row++){
				if(i_row!=i_rowcpt){
					for(int i_col=i_colcpt+1;i_col<i_colMax;i_col++){
						if(b_validCode(i_row, i_col)){
							l_Ghosts.add(new MasterTriplet(i_rowcpt,i_colcpt,
									i_row,i_col, this));
						}
					}
				}
			}
		}
		return l_Ghosts;
	}

	public ArrayList<MasterTriplet> getAllGhosts(int i_rowcpt, int i_colcpt) throws Exception {
		ArrayList<MasterTriplet> l_Ghosts = new ArrayList<MasterTriplet>();
		//		L.myPrintln("  i_rowcpt="+i_rowcpt+", i_colcpt="+i_colcpt+s_getValue(i_rowcpt,i_colcpt));
		if(b_validCode(i_rowcpt,i_colcpt)){
			int i_rowMax=getMaxRow();
			int i_colMax=getMaxCol();
			for(int i_row=0;i_row<i_rowMax;i_row++){
				for(int i_col=0;i_col<i_colMax;i_col++){
					l_Ghosts.add(new MasterTriplet(i_rowcpt,i_colcpt,i_row,i_col, this));
				}
			}
		}
		return l_Ghosts;
	}

	public void buildMasterTripletList () throws Exception {
		Iterator<MasterTriplet> it_Ghosts = l_allGhosts.iterator();
		while(it_Ghosts.hasNext()){
			String s_triplet=it_Ghosts.next().toString3();
			int i_length= s_triplet.length();
			if(i_length>0) {
				L.myPrintln(s_triplet);
				l_MasterTripletList.add(s_triplet.substring(0,3));
				if(i_length>3){
					l_MasterTripletList.add(s_triplet.substring(3,6));
				}
			}
		}
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

	public String s_getValue(int i_rowcpt, int i_colcpt){
		return krt.m_KeyRefTable.get(l_ZeMatrixFull.get(i_rowcpt).get(i_colcpt)).toStringDoc();
		//		return key.SmbLst.get(l_ZeMatrixFull.get(i_rowcpt).get(i_colcpt));
	}

	private boolean b_validCode(int i_rowcpt, int i_colcpt) {
		try {
			return key.b_validCode(l_ZeMatrixFull.get(i_rowcpt).get(i_colcpt));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}



	// Modifier key list
	ArrayList<keyGroup> StdModLst = new ArrayList<keyGroup>();
	ArrayList<keyGroup> StdModLstWithFn;

	//	ArrayList<ArrayList<Integer>> StdModLst = new ArrayList<ArrayList<Integer>>((Collection<? extends ArrayList<Integer>>) Arrays.asList(
	//			Arrays.asList(i_LShift,i_RShift), Arrays.asList(i_LCtrl,i_RCtrl), Arrays.asList(i_LAlt,i_RAlt)
	//			, Arrays.asList(i_LGUI,i_LGUI), Arrays.asList(i_RGUI,i_RGUI)
	//	));

	// WHQL exception lists
	ArrayList<Integer> StdModKeyLst = new ArrayList<Integer>();

	String toStringKbdMapToday() throws Exception{
		// initialize result list
		StringBuffer sbmatrix= new StringBuffer("/************************************/\n");
		StringBuffer sbkeytable= new StringBuffer("/************************************/\n  {0,0}\n");
		StringBuffer sbfntable1= new StringBuffer("\nkbdm_FnKeyOff[]\n");
		StringBuffer sbfntable2= new StringBuffer("\nkbdm_FnKeyOn[]\n");

		// process main matrix
		int i_rowcpt=0;
		int i_colcpt;
		int i_keycpt=0;
		int i_fnIndex=0;
		for(ArrayList<Integer> it_row : l_ZeMatrixFull) {
			sbmatrix.append("{ // ROW "+i_rowcpt+"\n");
			sbkeytable.append("// ROW "+i_rowcpt+"\n");
			i_colcpt=0;
			for(Integer it_whqlpos : it_row) {
				try {
					if(it_whqlpos!=KeyRefTable.i_Empty) {
						key k= findCode(it_whqlpos);	
						key k_alt = k.k_alt;
						if(k.isAlternateKey()) {
							if(k.isAffectedByFnKey()) {
								int i_in_fnTable = k_alt.i_codeKey;
								sbmatrix.append("  {IDX("+i_fnIndex+"),"+k.s_alternate_type+"}, // r"+i_rowcpt+"c"+i_colcpt+" "+
										krt.toStringDoc(it_whqlpos)+"----"+krt.toStringDoc(i_in_fnTable)+"\n");
								sbfntable1.append("  {"+(i_keycpt+1)+",0}, // r"+i_rowcpt+"c"+i_colcpt+" "+
										krt.toStringDoc(it_whqlpos)+"----"+krt.toStringDoc(i_in_fnTable)+"\n");
								sbkeytable.append("  "+krt.toStringDecl(it_whqlpos,(i_keycpt+1))+"\n");
								i_keycpt++;
								sbfntable2.append("  {"+(i_keycpt+1)+",0}, // r"+i_rowcpt+"c"+i_colcpt+" "+
										krt.toStringDoc(i_in_fnTable)+"----"+krt.toStringDoc(it_whqlpos)+"\n");
								sbkeytable.append("  "+krt.toStringDecl(i_in_fnTable,(i_keycpt+1))+" r"+i_rowcpt+"c"+i_colcpt+"\n");
								i_fnIndex++;
							} else {
								sbmatrix.append("  {"+(i_keycpt+1)+","+k.s_alternate_type+"}, // r"+i_rowcpt+"c"+i_colcpt+" "+
										krt.toStringDoc(it_whqlpos)+"\n");	
								sbkeytable.append("  "+krt.toStringDecl(it_whqlpos,(i_keycpt+1))+" r"+i_rowcpt+"c"+i_colcpt+"\n");
							}
						} else {
							sbmatrix.append("  {"+(i_keycpt+1)+",0}, // r"+i_rowcpt+"c"+i_colcpt+" "+krt.toStringDoc(it_whqlpos)+"\n");
							sbkeytable.append("  "+krt.toStringDecl(it_whqlpos,(i_keycpt+1))+" r"+i_rowcpt+"c"+i_colcpt+"\n");
						}
						i_keycpt++;
					} else {
						sbmatrix.append("  {0,0}, // r"+i_rowcpt+"c"+i_colcpt+"\n");
					}
				} catch (Exception e) {
					sbmatrix.append(", {"+key.toString(this, it_whqlpos)+",0}\n");
				}
				//				L.myPrint(sbmatrix.toString());
				i_colcpt++;
			}
			sbmatrix.append("}\n");
			i_rowcpt++;
		}
		return sbmatrix.append(sbkeytable).append(sbfntable1).append(sbfntable2).toString();
	}

	public String toStringKbdMapTomorrow() throws Exception {
		// initialize result list
		StringBuffer sbmatrix= new StringBuffer("/************************************/\n");
		StringBuffer sbfntable1= new StringBuffer("\nkbdm_FnKeyOff[]\n");
		StringBuffer sbfntable2= new StringBuffer("\nkbdm_FnKeyOn[]\n");

		// process main matrix
		int i_rowcpt=0;
		int i_colcpt;
		int i_fnIndex=0;
		for(ArrayList<Integer> it_row : l_ZeMatrixFull) {
			sbmatrix.append("{ // ROW "+i_rowcpt+"\n");
			i_colcpt=0;
			for(Integer it_whqlpos : it_row) {
				try {
					if(it_whqlpos!=KeyRefTable.i_Empty) {
						key k= findCode(it_whqlpos);	
						key k_alt = k.k_alt;
						if(k.isAlternateKey()) {
							if(k.isAffectedByFnKey()) {
								int i_in_fnTable = k_alt.i_codeKey;
								sbmatrix.append("  {IDX("+i_fnIndex+"),"+k.s_alternate_type+"}, // r"+i_rowcpt+"c"+i_colcpt+" "+
										krt.toStringDoc(it_whqlpos)+"----"+krt.toStringDoc(i_in_fnTable)+"\n");
								sbfntable1.append("  {"+krt.toStringHIDcode(it_whqlpos)+",0}, // r"+i_rowcpt+"c"+i_colcpt+" "+
										krt.toStringDoc(it_whqlpos)+"----"+krt.toStringDoc(i_in_fnTable)+"\n");
								sbfntable2.append("  {"+krt.toStringHIDcode(i_in_fnTable)+",0}, // r"+i_rowcpt+"c"+i_colcpt+" "+
										krt.toStringDoc(i_in_fnTable)+"----"+krt.toStringDoc(it_whqlpos)+"\n");
								i_fnIndex++;
							} else {
								sbmatrix.append("  {"+krt.toStringHIDcode(it_whqlpos)+","+k.s_alternate_type+"}, // r"+i_rowcpt+"c"+i_colcpt+" "+
										krt.toStringDoc(it_whqlpos)+"\n");						
							}
						} else {
							sbmatrix.append("  {"+krt.toStringHIDcode(it_whqlpos)+",0}, // r"+i_rowcpt+"c"+i_colcpt+" "+krt.toStringDoc(it_whqlpos)+"\n");
						}
					} else {
						sbmatrix.append("  {0,0}, // r"+i_rowcpt+"c"+i_colcpt+"\n");
					}
				} catch (Exception e) {
					sbmatrix.append(", {"+key.toString(this, it_whqlpos)+",0}// r"+i_rowcpt+"c"+i_colcpt+" \n");
				}
				//				L.myPrint(sbmatrix.toString());
				i_colcpt++;
			}
			sbmatrix.append("}\n");
			i_rowcpt++;
		}
		return sbmatrix.append(sbfntable1).append(sbfntable2).toString();
	}
}
