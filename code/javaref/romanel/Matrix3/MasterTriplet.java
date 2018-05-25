/*
 * $Log$
 * emariacher - Tuesday, November 17, 2009 6:06:35 PM
 * still debugging...
 * emariacher - Wednesday, November 11, 2009 2:07:09 PM
 * emariacher - Monday, October 05, 2009 2:22:25 PM
 * CounterStrike rules added
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MasterTriplet {
	ArrayList<String> l_s_TripletList     = new ArrayList<String>();

	int i_topleft;
	int i_bottomright;
	int i_topright;
	int i_bottomleft;


	int i_occurence=0;
	Log L;
	KbdMatrix km;

	public MasterTriplet(String s, KbdMatrix km) {
		this.km=km;
		this.L=km.L;
		l_s_TripletList.add(s);
		l_s_TripletList.add(s.substring(0, 1)+s.substring(2, 3)+s.substring(1, 2) );
		l_s_TripletList.add(s.substring(1, 2)+s.substring(0, 1)+s.substring(2, 3) );
		l_s_TripletList.add(s.substring(1, 2)+s.substring(2, 3)+s.substring(0, 1) );
		l_s_TripletList.add(s.substring(2, 3)+s.substring(0, 1)+s.substring(1, 2) );
		l_s_TripletList.add(s.substring(2, 3)+s.substring(1, 2)+s.substring(0, 1) );
	}

	public MasterTriplet(int i_rowcpt, int i_colcpt, Integer i_row,
			Integer i_col, KbdMatrix km) {
		this.km=km;
		this.L=km.L;
		i_topleft= km.i_getValue(i_rowcpt,i_colcpt);
		i_bottomright= km.i_getValue(i_row,i_col);
		i_topright= km.i_getValue(i_rowcpt,i_col);
		i_bottomleft= km.i_getValue(i_row,i_colcpt);
	}

	public String checkBraille() {
		String s_masterTriplet=l_s_TripletList.get(0);
		int i_count=0;
		for(int i=0;i<3;i++) {
			String s_letter=s_masterTriplet.substring(i,i+1);
			if(KeyRefTable.s_Braille.indexOf(s_letter)>=0) {
				i_count++;
			}
		}
		if(i_count==3) {
			return new String("  ["+s_masterTriplet +"] is a ghost forbidden by Braille.\n");	
		}
		return new String("");
	}

	int countInList(ArrayList<Integer> l) {
		int i_count=0;
		if(l.indexOf(i_topleft)>=0) {
			i_count++;
		}
		if(l.indexOf(i_bottomright)>=0) {
			i_count++;
		}
		if(l.indexOf(i_topright)>=0) {
			i_count++;
		}
		if(l.indexOf(i_bottomleft)>=0) {
			i_count++;
		}
		return i_count;
	}
	
	public String checkGuitarHero() throws Exception {
		if(countInList(KeyRefTable.l_GuitarHero)>=3) {
			return new String("  ["+toString2() +"] is a ghost forbidden by GuitarHero.\n");	
		}
		return new String("");
	}

	public String checkCounterStrike() throws Exception {
		int i_countADSW=countInList(KeyRefTable.l_CounterStrikeADSW);
		int i_countMod=countInList(KeyRefTable.l_CounterStrikeMod);
		int i_countOther=countInList(KeyRefTable.l_CounterStrikeOther);
		if((i_countADSW>=1)&&(i_countMod>=1)&&(i_countOther>=1)) {
			return new String("  ["+toString2() +"] is a ghost forbidden by checkCounterStrike.\n");	
		}
		return new String("");
	}

	private String checkGaming(String s_game1Code, String s_game2Code,
			String s_game3Code, String s_game4Code, ArrayList<Integer> l_key, StringBuffer sb2, Log L) throws Exception{
		StringBuffer sb = new StringBuffer();
		int i_countGame1=0;
		int i_countGame2=0;
		int i_countGame3=0;
		int i_countGame4=0;
		StringBuffer sb_masterTriplet= new StringBuffer();
		for(int i=0;i<3;i++) {
			int i_code = l_key.get(i);
			String s_letter=key.toString(km, i_code);
			sb_masterTriplet.append(String.format("[%1$8s(%2$3d): ", s_letter, i_code));
			if(s_game1Code.indexOf(s_letter)>=0) {
				sb_masterTriplet.append("zone1]");
				i_countGame1++;
			} else if(s_game2Code.indexOf(s_letter)>=0) {
				sb_masterTriplet.append("zone2]");
				i_countGame2++;
			} else if(s_game3Code.indexOf(s_letter)>=0) {
				sb_masterTriplet.append("zone3]");
				i_countGame3++;
			} else if(s_game4Code.indexOf(s_letter)>=0) {
				sb_masterTriplet.append("zone4]");
				i_countGame4++;
			}
		}
		if((i_countGame1==2)&&(i_countGame4==1)) {
			sb.append("  {"+sb_masterTriplet+"} has "+i_countGame1+" keys in zone 1 (cyan) and "+i_countGame4+" key in zone 4 (green).\n"+sb2);
		} else if((i_countGame2==2)&&(i_countGame4==1)) {
			sb.append("  {"+sb_masterTriplet+"} has "+i_countGame2+" keys in zone 2 (magenta) and "+i_countGame4+" key in zone 4 (green).\n"+sb2);
		} else if((i_countGame3==2)&&(i_countGame4==1)) {
			sb.append("  {"+sb_masterTriplet+"} has "+i_countGame3+" keys in zone 3 (yellow) and "+i_countGame4+" key in zone 4 (green).\n"+sb2);
		}
		return sb.toString();	
	}


	public String checkGaming(String s_game1Code, String s_game2Code,
			String s_game3Code, String s_game4Code, Log L) throws Exception {
		StringBuffer sb = new StringBuffer();
		//		any combination of two keys in gaming zone 1 (cyan) and one key in gaming zone 4 (green) must not generate a ghost key,
		//		any combination of two keys in gaming zone 2 (magenta) and one key in gaming zone 4 (green) must not generate a ghost key, and
		//		any combination of two keys in gaming zone 3 (yellow) and one key in gaming zone 4 (green) must not generate a ghost key.

		String s_topleft=new String(String.format("[%1$8s(%2$3d)]", key.toString(km, i_topleft), i_topleft));
		String s_topright=new String(String.format("[%1$8s(%2$3d)]", key.toString(km, i_topright), i_topright));
		String s_bottomright=new String(String.format("[%1$8s(%2$3d)]", key.toString(km, i_bottomright), i_bottomright));
		String s_bottomleft=new String(String.format("[%1$8s(%2$3d)]", key.toString(km, i_bottomleft), i_bottomleft));


		ArrayList<Integer> l_key;
		if((key.b_validGhostCode(i_topleft))&(key.b_validGhostCode(i_bottomright))){
			if(key.b_validGhostCode(i_topright)) {
				l_key = new ArrayList<Integer>();
				l_key.add(i_topleft);
				l_key.add(i_topright);
				l_key.add(i_bottomright);
				StringBuffer sb2 = new StringBuffer("    "+s_topright+" is on the same row as "+s_topleft+"\n    "+
						s_topright+" is on the same column as "+s_bottomright+"\n\n");
				sb.append(checkGaming(s_game1Code, s_game2Code, s_game3Code, s_game4Code, l_key, sb2, L));
			}
			if(key.b_validGhostCode(i_bottomleft)) {
				l_key = new ArrayList<Integer>();
				l_key.add(i_topleft);
				l_key.add(i_bottomleft);
				l_key.add(i_bottomright);
				StringBuffer sb2 = new StringBuffer("    "+s_bottomleft+" is on the same row as "+s_bottomright+"\n    "+
						s_bottomleft+" is on the same column as "+s_topleft+"\n\n");
				sb.append(checkGaming(s_game1Code, s_game2Code, s_game3Code, s_game4Code, l_key, sb2, L));
			}
		}
		return sb.toString();
	}

	private ghostFrequency getGhostFrequency(
			String s_frequencyString) throws Exception {
		Iterator<String> i = l_s_TripletList.iterator();
		int i_index=0;
		L.myPrintln("\n*["+getMasterString()+"]*");
		while(i.hasNext()){
			String s_Triplet = i.next();
			do {
				i_index = s_frequencyString.indexOf(s_Triplet, i_index);
				if(i_index>0){
					// extract next valid number
					Matcher matcher = Pattern.compile("\\d+").matcher(s_frequencyString.substring(i_index));
					matcher.find();					
					i_occurence += Integer.valueOf(matcher.group());
					L.myPrintln("        *["+s_Triplet+"]:"+i_index+" "+i_occurence+" "+
							s_frequencyString.substring(i_index-20,i_index+20));
					i_index++;
				}
			} while (i_index>0);
		}
		//L.myPrintln("   "+getMasterString()+","+i_occurence);
		return new ghostFrequency(getMasterString(),i_occurence);
	}

	public ghostFrequency getGhostFrequency(String s_frequencyString,
			int i_totalWordsInFrequencyFile) throws Exception {
		ghostFrequency gf = getGhostFrequency(s_frequencyString);
		gf.i_totalWordsInFrequencyFile=i_totalWordsInFrequencyFile;
		return gf;
	}


	public String getMasterString() {
		return l_s_TripletList.get(0);
	}

	public String toString2() throws Exception {
		StringBuffer sb = new StringBuffer();
		if(i_topright!=KeyRefTable.i_Empty) {
			sb.append(key.toString(km,i_topleft)+key.toString(km,i_topright)+key.toString(km,i_bottomright)+"\n");
		}
		if(i_bottomleft!=KeyRefTable.i_Empty) {
			sb.append(key.toString(km,i_topleft)+key.toString(km, i_bottomleft)+key.toString(km,i_bottomright)+"\n");
		}
		return sb.toString();
	}

	public String toString3() throws Exception {
		StringBuffer sb = new StringBuffer();
		if((key.isCode4Frequency(i_topleft))&(key.isCode4Frequency(i_bottomright))){
			if(key.isCode4Frequency(i_topright)) {
				sb.append(toString4(i_topleft)+toString4(i_topright)+toString4(i_bottomright));
			}
			if(key.isCode4Frequency(i_bottomleft)) {
				sb.append(toString4(i_topleft)+toString4(i_bottomleft)+toString4(i_bottomright));
			}
		}
		return sb.toString();
	}

	public String toString4(int i_code) throws Exception {
		switch(i_code){
		case 41: return new String("'");
		case 61: return new String(" ");
		default: return new String(key.toString(km, i_code).toUpperCase().trim());
		}
	}

	public boolean isPartOfWHQLException(ArrayList<Integer> l_stdModKeyLst, ArrayList<ArrayList<Integer>> l_exceptLst) throws Exception {
		//		L.myPrintln("  1["+key.toString(km, i_topleft)+key.toString(km, i_bottomright)+key.toString(km, i_topright)+"]"+
		//				inList(i_topleft,i_bottomright,i_topright,l_stdModKeyLst, l_exceptLst));
		//		L.myPrintln("  2["+key.toString(km, i_topleft)+key.toString(km, i_bottomright)+key.toString(km, i_bottomleft)+"]"+
		//				inList(i_topleft,i_bottomright,i_bottomleft,l_stdModKeyLst, l_exceptLst));
		//		L.myPrintln("  3["+key.toString(km, i_topright)+key.toString(km, i_bottomleft)+key.toString(km, i_topleft)+"]"+
		//				inList(i_topright,i_bottomleft,i_topleft,l_stdModKeyLst, l_exceptLst));
		//		L.myPrintln("  4["+key.toString(km, i_topright)+key.toString(km, i_bottomleft)+key.toString(km, i_bottomright)+"]"+
		//				inList(i_topright,i_bottomleft,i_bottomright,l_stdModKeyLst, l_exceptLst));

		return inList(i_topleft,i_bottomright,i_topright,l_stdModKeyLst, l_exceptLst)&&
		inList(i_topleft,i_bottomright,i_bottomleft,l_stdModKeyLst, l_exceptLst)&&
		inList(i_topright,i_bottomleft,i_bottomright,l_stdModKeyLst, l_exceptLst)&&
		inList(i_topright,i_bottomleft,i_topleft,l_stdModKeyLst, l_exceptLst);
	}

	boolean inList(int i_code1, int i_code2, int i_code3,
			ArrayList<Integer> l_stdModKeyLst, ArrayList<ArrayList<Integer>> l_exceptLst) throws Exception{
		if((i_code1==i_code2)||(i_code1==i_code3)||(i_code3==i_code2)) {
			return true;
		}
		if(!key.b_validCode(i_code1)||!key.b_validCode(i_code2)||!key.b_validCode(i_code3)) {
			return true;
		}
		int i_count=0;
		if(l_stdModKeyLst.contains(i_code1)) {
			i_count++;
		}
		if(l_stdModKeyLst.contains(i_code2)) {
			i_count++;
		}
		if(l_stdModKeyLst.contains(i_code3)) {
			i_count++;
		}
		if(i_count<2) {
			return true;
		}

		Iterator<ArrayList<Integer>> it_exceptLst = l_exceptLst.iterator();
		while(it_exceptLst.hasNext()) {
			ArrayList<Integer> l_exceptionTriplet = it_exceptLst.next();
			if((l_exceptionTriplet.contains(i_code1))&&
					(l_exceptionTriplet.contains(i_code2))&&
					(l_exceptionTriplet.contains(i_code3))) {
				return true;
			}
		}
		return false;
	}
}
