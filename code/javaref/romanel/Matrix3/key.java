/*
 * $Log$
 * emariacher - Wednesday, November 25, 2009 4:54:17 PM
 * trying to kbdm_FnKey...
 * emariacher - Wednesday, November 25, 2009 3:25:37 PM
 * kbd_map.c file generated
 * emariacher - Tuesday, November 24, 2009 4:35:02 PM
 * debugging with Ren Matrix
 * emariacher - Tuesday, November 17, 2009 6:06:35 PM
 * still debugging...
 * emariacher - Tuesday, November 17, 2009 4:12:07 PM
 * added some key definitions
 * generate a csv file
 * emariacher - Tuesday, November 17, 2009 12:14:13 PM
 * more colors
 * emariacher - Friday, November 13, 2009 4:40:13 PM
 * still debugging checkduplicate keys
 * emariacher - Thursday, November 12, 2009 4:44:35 PM
 * suppressed the smblist
 * still some exceptions.
 * emariacher - Thursday, November 12, 2009 3:40:17 PM
 * YES!
 * emariacher - Thursday, November 12, 2009 10:21:58 AM
 * fails on [A] / calculator keys
 * emariacher - Wednesday, November 11, 2009 3:32:59 PM
 * whqlpos vs fwindex array of array
 * emariacher - Wednesday, November 11, 2009 2:07:09 PM
 * emariacher - Monday, October 05, 2009 2:22:25 PM
 * CounterStrike rules added
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class key {
	int i_row=777;
	int i_col=777;
	int i_codeKey;
	public String s_alternate_type=null;
	public key k_alt;


	public key(int i_row, int i_col, int i_codeKey) {
		this.i_row = i_row;
		this.i_col = i_col;
		this.i_codeKey = i_codeKey;
	}

	public boolean is(int i_code) {
		return i_codeKey==i_code;
	}
	public boolean is(key key1) {
		return is(key1.i_codeKey);
	}

	public static boolean b_validCode(int i_key) {
		switch(i_key) {
		case KeyRefTable.i_Empty:		return false;
		default:			return true;
		}
	}
	public static boolean b_validGhostCode(int i_key) {
		switch(i_key) {
		case KeyRefTable.i_Empty:		return false;
		case KeyRefTable.i_NonStdCode:	return false;
		default:			return true;
		}
	}

	public boolean sameRowCol(key k) throws Exception {
		if(k==null){
			return false;
		}
		if(is(k)) {
			return false;
		}
		if((k.i_row==777)||(k.i_col==777)||(i_row==777)||(i_col==777)){
			return false;
		}
		return ((k.i_row==i_row)||(k.i_col==i_col));
	}

	public boolean sameCol(key k) throws Exception {
		if(k==null){
			return false;
		}
		if(is(k)) {
			return false;
		}
		if((k.i_row==777)||(k.i_col==777)||(i_row==777)||(i_col==777)){
			return false;
		}
		return (k.i_col==i_col);
	}

	public boolean sameRow(key k) throws Exception {
		if(k==null){
			return false;
		}
		if(is(k)) {
			return false;
		}
		if((k.i_row==777)||(k.i_col==777)||(i_row==777)||(i_col==777)){
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
			return 777;
		}	
	}

	static int getCode(KbdMatrix km, String s_next) throws Exception {
		if(s_next.length()==0) return KeyRefTable.i_Empty;
		try {
			return Integer.valueOf(s_next);
		} catch (NumberFormatException e) {
			int i_weirdCodeIndex = KeyRefTable.s_weirdCodes.indexOf(s_next.trim().toLowerCase());
			if(i_weirdCodeIndex>=0) {
				int i_code = getIntegerInString(KeyRefTable.s_weirdCodes.substring(i_weirdCodeIndex));
				if(i_code<KeyRefTable.i_NonStdCode) {
					return i_code;
				} else {			
					km.l_nonStdCode.add(s_next);
					int i_newIndex=KeyRefTable.i_NonStdCode+km.l_nonStdCode.indexOf(s_next);
					km.krt.m_KeyRefTable.put(i_newIndex, 
							new KeyRef(i_newIndex, s_next, s_next));
					return i_newIndex;
				}
			} else if((s_next.toLowerCase().indexOf("kp ")>=0)||
					((s_next.trim().indexOf("F")==0)&&(s_next.trim().length()>1))
			) {
				return km.krt.m_KeyRefTableInv.get(s_next.trim());
			} else {
				km.l_nonStdCode.add(s_next);
				int i_newIndex=KeyRefTable.i_NonStdCode+km.l_nonStdCode.indexOf(s_next);
				km.krt.m_KeyRefTable.put(i_newIndex, 
						new KeyRef(i_newIndex, s_next, s_next));
				return i_newIndex;
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
		return (i_codeKey>0)&&(i_codeKey<KeyRefTable.i_WHQL_Limit);
	}
	boolean isStdKey(ArrayList<Integer> l_StdModCode){
		if(l_StdModCode.contains(i_codeKey)) {
			return false;
		}
		return (i_codeKey>0)&&(i_codeKey<KeyRefTable.i_NonStdCode);
	}

	public boolean isStdKeyNotWHQL() {
		return isStdKeyNotWHQL(i_codeKey);
	}

	public static boolean isStdKeyNotWHQL(int i_code) {
		return (i_code>=KeyRefTable.i_WHQL_Limit)&&(i_code<KeyRefTable.i_NonStdCode);
	}

	boolean sameRowAndCol(ArrayList<Integer> l_StdModRow, ArrayList<Integer> l_StdModCol){
		return l_StdModRow.contains(i_row)&&l_StdModCol.contains(i_col);
	}

	public String toString(KbdMatrix km) {
		if(i_codeKey<KeyRefTable.i_NonStdCode) {
			return new String("{[r:"+i_row+", c:"+i_col+"]["+i_codeKey+"] "+km.krt.toStringDoc(i_codeKey).trim()+"}");
		} else {
			return new String("{[r:"+i_row+", c:"+i_col+"]["+i_codeKey+"] "+km.l_nonStdCode.get(i_codeKey-KeyRefTable.i_NonStdCode).trim()+"}");
		}
	}

	public static String toString(KbdMatrix km, int i_code) throws Exception {
		try {
			KeyRef kr = km.krt.m_KeyRefTable.get(i_code);
			if(kr!=null) {
				return kr.toStringDoc();
			} else {
				String s_code = km.l_nonStdCode.get(i_code-KeyRefTable.i_NonStdCode);
				return new String(String.format(" [%1$6s] ",s_code));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.toString()+"["+i_code+"]");
		}
	}

	public static String list2String(KbdMatrix km, ArrayList<Integer> l_code) throws Exception {
		StringBuffer sb = new StringBuffer();
		Iterator<Integer> it_code = l_code.iterator();
		while(it_code.hasNext()) {
			int i_code = it_code.next();
			sb.append(key.toString(km, i_code));
		}
		return sb.toString();
	}

	public String getColor(KbdMatrix km) {
		if(s_alternate_type!=null) {
			return new String("FFCC00");
		} else if(km.StdModKeyLst.contains(i_codeKey)) {
			return new String("FFCC99");
		} else if(isStdKeyWHQL(km.StdModKeyLst)) {
			return new String("FFFFCC");
		} else {
			return new String("CCFFCC");	
		}
	}

	public boolean isAffectedByFnKey() {
		if(k_alt!=null) {
			if(s_alternate_type.compareToIgnoreCase("kbdm_FKEY")==0) {
				return true;
			}
		}
		return false;
	}

	public boolean isFnKey(KeyRefTable krt) {
		return krt.m_KeyRefTable.get(i_codeKey).b_isFnKey;
	}

	public boolean isAlternateKey() {
		return ((s_alternate_type!=null)||(k_alt!=null));
	}
}
