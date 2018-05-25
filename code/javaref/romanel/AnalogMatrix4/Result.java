/*
$Header: Result.java: Revision: 24: Author: emariacher: Date: Friday, July 10, 2009 2:27:20 PM$ 

$Log$
emariacher - Friday, July 10, 2009 2:27:20 PM
improved logging for building ghost key detection algorythm
emariacher - Friday, July 10, 2009 1:50:59 PM
just some logging stuff
emariacher - Wednesday, July 08, 2009 3:46:31 PM
emariacher - Thursday, July 02, 2009 6:00:01 PM
check actual vs computed.
emariacher - Wednesday, July 01, 2009 5:16:11 PM
1st AD table is now at 90% of 1st ADlevel when generating ADtable.
emariacher - Wednesday, July 01, 2009 4:07:44 PM
static ghost key detection with "analyze key pressed" button
emariacher - Wednesday, July 01, 2009 1:08:52 PM 
 */

import java.util.ArrayList;
import java.util.TreeSet;


public class Result implements Comparable<Result> {
	static final int TYPE_MEASURE = 0;
	static final int TYPE_ADLEVEL = 1;
	static final int TYPE_ADITEM = 2;

	Result baseResult=null;
	Key k_startCol;
	Key k_endRow;
	ArrayList<Key> l_key2press =new ArrayList<Key>();
	Double d_res;
	Double d_res_orig;
	Double d_delta;
	Double d_max=0.0;
	Double d_ratio;
	int i_alternances;
	int i_seed=0;
	public Result previous=null;
	//	private double d_delta2;
	int i_type=TYPE_MEASURE;
	Double d_ref_current;
	public String s_key_pressed_check=null;
	private boolean b_resultOK=true;
	@SuppressWarnings("unused")
	private ColRow cr=null;
	private ColRow cr_other=null;

	public Result(ArrayList<Key> press) {
		l_key2press.addAll(press); 
	}

	public Result(Result quarto, Result nonquarto) {
		baseResult=quarto;
		l_key2press.addAll(quarto.l_key2press);
		l_key2press.addAll(nonquarto.l_key2press);
	}

	public Result(Result quarto, Result nonquarto, double d_delta) {
		baseResult=quarto;
		l_key2press.addAll(nonquarto.l_key2press);
		d_res=nonquarto.d_res;
		updateDelta(d_delta);
	}

	public Result(double d) {
		d_res=d;
	}

	public Result(double d, boolean b) {
		d_delta=d;
	}

	public Result(double d, int i_type) {
		d_res=d;
		this.i_type=i_type;
	}

	public Result(double d, double delta) {
		d_res=d;
		d_delta=delta;
		//		d_delta2=delta;
	}

	public Result(Result r, ArrayList<Key> press, ColRow cr, ColRow cr_other) {
		l_key2press.addAll(press);
		baseResult=r;
		this.cr=cr;
		this.cr_other=cr_other;
	}

	public Result(String s, ArrayList<Key> l_key2press2) {
		d_res = Double.valueOf(s);
		d_res_orig = d_res;
		l_key2press.addAll(l_key2press2);
		i_type=TYPE_ADITEM;
	}

	public Result(String s, String s2, ArrayList<Key> l_key2press2) {
		d_res = Double.valueOf(s);
		d_res_orig = d_res;
		d_ref_current=Double.valueOf(s2);
		l_key2press.addAll(l_key2press2);
		i_type=TYPE_ADITEM;
	}

	public Result(String s, boolean b) {
		s_key_pressed_check = s;
		b_resultOK = b;
	}

	public Result(ArrayList<Key> l_keys2press, ColRow cr, ColRow cr_other) {
		l_key2press.addAll(l_keys2press);
		this.cr=cr;
		this.cr_other=cr_other;
	}

	public boolean OK() {
		return b_resultOK;
	}

	public void update(Key col, Key row) {
		k_startCol=col;
		k_endRow=row;
	}

	public void updateResult(double d_compute_current) {
		d_res=d_compute_current;
	}

	public void updateDelta(double d_delta) {
		this.d_delta=d_delta;
	}

	public String toString() {
		return new String(String.format(" %1$s=%2$9.5f",l_key2press,d_res));
	}

	private String toStringKeyCode() {
		StringBuffer sb = new StringBuffer("[");
		for(Key k : l_key2press) {
			sb.append(k.i_codeKey+", ");
		}
		sb.append("]");
		return sb.toString();
	}

	public String toString2() {
		return new String(l_key2press.toString());
	}

	public String toStringDelta() throws Exception {
		if(baseResult==null) {
			//			throw new Exception("baseResult==null at "+toString());
			return new String(toString() + " only 1 key in quarto");
		}
		return new String(toString()+" vs "+baseResult.toString()+String.format(" Delta=%1$9.5f",d_delta));
	}

	public String toStringCsv() throws Exception {
		if(baseResult==null) {
			//			throw new Exception("baseResult==null at "+toString());
			return new String(toString() + " only 1 key in quarto");
		}
		return new String("\"[R"+k_endRow.i_row+"C"+k_startCol.i_col+"] "+
				toStringKeyCode()+" vs "+baseResult.toStringKeyCode()+
				String.format("\",,\""+i_alternances+"\",\"%1$9.5f\",,\"%2$9.5f\",\"%3$9.5f\"",
						d_delta,d_max,d_delta/d_max));
	}

	public void checkConsistency() throws Exception {
		if(d_res==null) {
			throw new Exception("d_res==null " + toString());
		}
		if(baseResult==null) {
			throw new Exception("baseResult==null " + toString());
		}
		if(baseResult.d_res==null) {
			throw new Exception("baseResult.d_res==null " + toString());
		}
		if(d_res<(baseResult.d_res-(Key.PRECISION_CURRENT*4))) {
			throw new Exception("    --NOGOOD: "+toString()+" < "+baseResult.toString());
		}
	}

	public Double getDelta() {
		d_delta=d_res - baseResult.d_res;
		return d_delta;
	}

	public Double getDelta(Result r) {
		double d_rdelta=d_res - r.d_res;
		if(d_rdelta<0.0) {
			d_rdelta=0.0-d_rdelta;
		}
		return d_rdelta;
	}

	public Double getDelta2() {
		return getDelta(previous);
	}

	@Override
	public int compareTo(Result r) {
		return Double.compare(d_res,r.d_res);
	}

	public boolean contains(Result r){
		return l_key2press.containsAll(r.l_key2press);
	}

	public boolean brcontains(Result r){
		if(baseResult==null) {
			if(r.baseResult==null) {
				return contains(r);
			} else {
				return r.baseResult.contains(this);
			}
		} else {
			if(r.baseResult==null) {
				return r.contains(baseResult);
			} else {
				return baseResult.contains(r.baseResult);
			}
		}
	}

	public String toString3() {
		StringBuffer sb = new StringBuffer(toString());
		if(i_type==TYPE_ADITEM) {
			sb.append(" +++++++++++++++++");
		} else if(i_type==TYPE_ADLEVEL) {
			sb.append(" =================");
		} else if(baseResult==null) {
			sb.append(" -----------------");
		} else {
			sb.append("    "+baseResult.toString2());
		}
		if(cr_other!=null) {
			sb.append(" "+cr_other+" ");
		}
		if(!b_resultOK) {
			sb.append(s_key_pressed_check);
		}
		return sb.toString();
	}

	public Result min_delta(Result r_delta) {
		if(r_delta.d_delta<d_delta) {
			return r_delta;
		}
		return this;
	}

	public Result max_max(Result r_delta) {
		if(r_delta.d_max>d_max) {
			return r_delta;
		}
		return this;
	}

	public Result max_res(Result r_delta) {
		if(r_delta.d_res>d_res) {
			return r_delta;
		}
		return this;
	}

	public Result max_percent(Result r_delta, int i_seed) {
		if((r_delta.d_delta/r_delta.d_max)>(d_delta/d_max)) {
			r_delta.i_seed=i_seed;
			return r_delta;
		}
		return this;
	}

	public String printPerCent() {
		return new String(String.format("%1$6.3f%%",100*d_delta/d_max));
	}

	public String printPerCentSeed() {
		return new String(printPerCent()+"("+i_seed+")");
	}

	public Result findBestADlevel(TreeSet<Result> ts_allResults) {
		// find AD level nearest to r.d_res -(delta/2)
		double d_delta2 = getDelta2();
		double d_ddelta = d_res - d_delta2;
		if(d_ddelta<0){
			d_ddelta = 0.0 - d_ddelta;
		}
		Result r_deltadiv2;
		if(d_ddelta<(Key.PRECISION_CURRENT*2)) {
			d_delta2=d_res*(1-GraphPanel.RESISTANCE_RANDSPAN);
			r_deltadiv2 = new Result(d_delta2);
		} else {
			r_deltadiv2 = new Result(d_res-(d_delta2/2));
		}

		r_deltadiv2.i_type=TYPE_ADLEVEL;
		r_deltadiv2.k_startCol=k_startCol;
		r_deltadiv2.k_endRow=k_endRow;
		r_deltadiv2.d_ref_current=d_res;
		if(baseResult==null) {
			r_deltadiv2.l_key2press=l_key2press;
		} else {
			r_deltadiv2.l_key2press=baseResult.l_key2press;
		}

		return r_deltadiv2;
	}

	public String toStringADtable(int i_seed2) {
		//		csvfile_ADtable.writeFile("\"type\",\"startCol\",\"endRow\",\"keycodes\","\"reference_current\",\"result_level\"\n"
		return new String(i_seed2+",AD,"+
				k_startCol.i_col+","+k_endRow.i_row+",\""+toStringKeyCode()+
				String.format("\",%1$9.5f,%2$9.5f\n",d_ref_current,d_res));
	}

	public boolean greater(double d_current) {
		return d_res>d_current;
	}

	public void updateResult(Result r) {
		s_key_pressed_check = r.s_key_pressed_check;
		b_resultOK = r.b_resultOK;
	}
}
