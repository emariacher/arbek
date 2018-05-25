/*
 * $Log$
 * emariacher - Wednesday, November 25, 2009 4:49:47 PM
 * bug correction
 */
import java.util.ArrayList;
import java.util.Iterator;

public class keyGroup {
	key key1;
	key key2;
	String s_codeKeys;

	public keyGroup(key key1, key key2) {
		this.key1 = key1;
		this.key2 = key2;
		s_codeKeys=new String("{1["+key1.i_codeKey+"],2["+key2.i_codeKey+"]}");
	}

	public keyGroup(KbdMatrix km, int i_codeKey1,  int i_codeKey2) {
		key1 = km.findCode(i_codeKey1);
		key2 = km.findCode(i_codeKey2);
		s_codeKeys=new String("{1["+i_codeKey1+"],2["+i_codeKey2+"]}");
	}

	private boolean sameRowCol(keyGroup kg) throws Exception {
		boolean b_sameRowColAsKey1=false;
		boolean b_sameRowColAsKey2=false;
		if(key1!=null) {
			b_sameRowColAsKey1=key1.sameRowCol(kg.key1)||key1.sameRowCol(kg.key2);
		}
		if(key2!=null) {
			b_sameRowColAsKey2=key2.sameRowCol(kg.key1)||key2.sameRowCol(kg.key2);
		}
		return b_sameRowColAsKey1 || b_sameRowColAsKey2;
	}

	public keyGroup sameRowCol(KbdMatrix km) throws Exception {
		if((key1==null)&&(key2==null)) {
			return null;
		}
		Iterator<keyGroup> it_StdModLst = km.StdModLst.iterator();
		while(it_StdModLst.hasNext()) {
			keyGroup kg = it_StdModLst.next();
			if(is(kg))  {
				continue;
			}
			if(sameRowCol(kg)) {
				return kg;
			}
		}
		return null;
	}

	public boolean is(keyGroup kg){
		if((kg.key1==null)&&(kg.key2==null)) {
			return false;
		}
		if(kg.key1==null) {
			if(key1==null) {
				return kg.key2.is(key2);
			}
			return false;
		}
		if(key1==null) {
			if(kg.key1==null) {
				return kg.key2.is(key2);
			}
			return false;
		}
		if(kg.key2==null) {
			if(key2==null) {
				return kg.key1.is(key1);
			}
			return false;
		}
		if(key2==null) {
			if(kg.key2==null) {
				return kg.key1.is(key1);
			}
			return false;
		}
		return (kg.key1.is(key1)&&kg.key2.is(key2));
	}


	public static ArrayList<Integer> getStdModRowColCode(ArrayList<keyGroup> l_StdModLst, int i_row0col1code2) {
		ArrayList<Integer> l_StdModRowColCode = new ArrayList<Integer>(); // list of rowsor col occupied by "StdModLst" keys
		Iterator<keyGroup> it_StdModLst = l_StdModLst.iterator();
		while(it_StdModLst.hasNext()) {
			keyGroup kg = it_StdModLst.next();
			if((kg.key1==null)&&(kg.key2==null)) {
				continue;
			}
			int i_colrowcodeValue;
			if(i_row0col1code2==0) {
				if(kg.key1!=null) {
					i_colrowcodeValue = kg.key1.i_row;
					if(!l_StdModRowColCode.contains(i_colrowcodeValue)) {
						l_StdModRowColCode.add(i_colrowcodeValue);
					}
				}
				if(kg.key2!=null) {
					i_colrowcodeValue = kg.key2.i_row;
					if(!l_StdModRowColCode.contains(i_colrowcodeValue)) {
						l_StdModRowColCode.add(i_colrowcodeValue);
					}
				}
			} else if(i_row0col1code2==1) {
				if(kg.key1!=null) {
					i_colrowcodeValue = kg.key1.i_col;
					if(!l_StdModRowColCode.contains(i_colrowcodeValue)) {
						l_StdModRowColCode.add(i_colrowcodeValue);
					}
				}
				if(kg.key2!=null) {
					i_colrowcodeValue = kg.key2.i_col;
					if(!l_StdModRowColCode.contains(i_colrowcodeValue)) {
						l_StdModRowColCode.add(i_colrowcodeValue);
					}		
				}
			} else if(i_row0col1code2==2) {
				if(kg.key1!=null) {
					i_colrowcodeValue = kg.key1.i_codeKey;
					if(!l_StdModRowColCode.contains(i_colrowcodeValue)) {
						l_StdModRowColCode.add(i_colrowcodeValue);
					}
				}
				if(kg.key2!=null) {
					i_colrowcodeValue = kg.key2.i_codeKey;
					if(!l_StdModRowColCode.contains(i_colrowcodeValue)) {
						l_StdModRowColCode.add(i_colrowcodeValue);
					}
				}
			}
		}
		return l_StdModRowColCode;
	}

	public String toString(KbdMatrix km) {
		StringBuffer sb = new StringBuffer("[");
		if(key1!=null) {
			sb.append("1: "+key1.toString(km)+", ");
		}
		if(key2!=null) {
			if((key1!=null)&&(key1!=key2)) {
				sb.append("2: "+key2.toString(km)+", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}
}
