/*
$Header: ADitem.java: Revision: 10: Author: emariacher: Date: Wednesday, July 08, 2009 3:03:59 PM$

$Log$
emariacher - Wednesday, July 08, 2009 3:03:59 PM
just some cosmetic when scanning 3 others keys
emariacher - Thursday, July 02, 2009 3:39:32 PM
just logging changes...
emariacher - Wednesday, July 01, 2009 4:31:29 PM
when current inferior to 1st AD level, then GHostKey is reported.
emariacher - Wednesday, July 01, 2009 4:07:44 PM
static ghost key detection with "analyze key pressed" button
 */

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ADitem {
	ColRow cr;
	TreeSet<Result> ts_ADlevels = new TreeSet<Result>();
	KbdMatrix km;
	TreeSet<Result> ts_ad_increment = new TreeSet<Result>();

	public ADitem(ArrayList<String> l_skeys, KbdMatrix km) {
		this.km=km;
		cr = new ColRow(l_skeys.get(2),l_skeys.get(3));
		add(l_skeys);
		for(int inc=0;inc<java.lang.Math.pow(2,AnalyzeMatrix.AD_BIT_RESOLUTION);inc++) {
			ts_ad_increment.add(new Result(inc*AnalyzeMatrix.AD_INCREMENT,Result.TYPE_ADLEVEL));
		}
	}

	public void add(ArrayList<String> l_skeys) {
		ArrayList<Key> l_key2press =new ArrayList<Key>();
		boolean b_state_machine=false;
		for(String s : l_skeys) {
			if(Pattern.compile(".*].*").matcher(s.toUpperCase()).find()) {
				b_state_machine=false;
			}
			if(Pattern.compile(".*\\[.*").matcher(s.toUpperCase()).find()) {
				Matcher matcher = Pattern.compile("\\d+").matcher(s.toUpperCase());
				matcher.find();
				String s_i_codeKey = matcher.group();
				int i_codeKey = Integer.valueOf(s_i_codeKey);
				l_key2press.add(km.getKey(i_codeKey));
				b_state_machine=true;
			} else if(b_state_machine) {
				Matcher matcher = Pattern.compile("\\d+").matcher(s.toUpperCase());
				matcher.find();
				String s_i_codeKey = matcher.group();
				int i_codeKey = Integer.valueOf(s_i_codeKey);
				l_key2press.add(km.getKey(i_codeKey));
			}
		}
		ts_ADlevels.add(new Result(l_skeys.get(l_skeys.size()-1), l_skeys.get(l_skeys.size()-2), l_key2press));
	}

	public String toString() {
		return new String("ADItem: "+cr.toString()+ts_ADlevels);
	}

	public void updateRatio(double d_ratio_mean) {
		for(Result r : ts_ADlevels) {
			r.d_res = r.d_res_orig*d_ratio_mean;
		}
	}

	public void normalizeNearest() throws Exception {

		for(Result r : ts_ADlevels) {
			if(!ts_ad_increment.contains(r)) {
				Result r_higher = ts_ad_increment.higher(r);
				Result r_lower = ts_ad_increment.lower(r);
				if(r_higher==null) {
					throw new Exception("higher("+r+")==null"+
							String.format("[%1$6.3fV]",r.l_key2press.get(0).d_source_volt));
				}
				if(r_lower==null) {
					throw new Exception("lower("+r+")==null"+
							String.format("[%1$6.3fV]",r.l_key2press.get(0).d_source_volt));
				}
				if(r.getDelta(r_higher)<r.getDelta(r_lower)) {
					r.d_res=r_higher.d_res;
				} else {
					r.d_res=r_lower.d_res;
				}
			}
		}
	}

	public ArrayList<Key> compute_keys(double d_current) throws Exception {
		ArrayList<Key> l_ghostkey = new ArrayList<Key>();
		l_ghostkey.add(new Key(Key.i_GhostKey,km));
		Result r_found = new Result(l_ghostkey);
		r_found.updateResult(0.0);
		km.L.myPrintln(String.format("   ***d_current= %1$9.5f ",d_current));
		for(Result r : ts_ADlevels) {
			km.L.myPrintln("     "+r.toString());
			if(r.greater(d_current)) {
				km.L.myPrintln("   ***corresponds to: " + r_found.toString());
				break;		
			}
			r_found=r;
		}
		return r_found.l_key2press;
	}

	public String print_levels() throws Exception {
		StringBuffer sb = new StringBuffer("  "+cr);
		for(Result r : ts_ADlevels) {
			sb.append("\n     "+r.toString());
		}
		return sb.toString();
	}

}
