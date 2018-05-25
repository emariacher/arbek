/*
$Header: Ghost.java: Revision: 2: Author: emariacher: Date: Thursday, July 09, 2009 2:22:07 PM$

$Log$
emariacher - Thursday, July 09, 2009 2:22:07 PM
list all ghost keys seems OK.
emariacher - Wednesday, July 08, 2009 6:32:42 PM
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;


public class Ghost implements Comparable<Ghost> {

	private static final int GHOSTKEY_DETECTED = 1;
	private static final int GHOSTKEY_STDMOD = 3;
	private static final int GHOSTKEY_ACCEPTED = 2;
	ColRow cr;
	TreeSet<Key> ts_ghostKeys = new TreeSet<Key>();
	TreeSet<Key> ts_actualKeys = new TreeSet<Key>();
	TreeSet<Key> ts_computedKeys = new TreeSet<Key>();
	TreeSet<Key> ts_allKeys = new TreeSet<Key>();
	ArrayList<Key> l_stdmod = new ArrayList<Key>(Arrays.asList(
			new Key(Key.i_LShift,null), new Key(Key.i_RShift,null),
			new Key(Key.i_LCtrl,null), new Key(Key.i_RCtrl,null),
			new Key(Key.i_LGUI,null), new Key(Key.i_RGUI,null),
			new Key(Key.i_LShift,null), new Key(Key.i_RShift,null),
			new Key(Key.i_LAlt,null), new Key(Key.i_RAlt,null)
	));
	int i_ghostKeyLevel=0;
	Key kghost=new Key(Key.i_GhostKey,null);

	public Ghost(ColRow cr, ArrayList<Key> l_ghostKeys, ArrayList<Key> l_actual_keys, ArrayList<Key> l_computed_keys) {
		ts_ghostKeys.addAll(l_ghostKeys);
		ts_actualKeys.addAll(l_actual_keys);
		ts_computedKeys.addAll(l_computed_keys);
		ts_allKeys.addAll(ts_ghostKeys);
		ts_allKeys.addAll(ts_actualKeys);
		ts_allKeys.addAll(ts_computedKeys);
		this.cr = cr;

		i_ghostKeyLevel = GHOSTKEY_ACCEPTED;
		if(ts_computedKeys.contains(kghost)) {
			i_ghostKeyLevel = GHOSTKEY_DETECTED;
		} else {
			for(Key k_stdmod : l_stdmod) {
				if(ts_allKeys.contains(k_stdmod)) {
					i_ghostKeyLevel = GHOSTKEY_STDMOD;
					break;
				}
			}
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(cr.i_col+", "+cr.i_row+", "+i_ghostKeyLevel);
		for(Key k : ts_ghostKeys) {
			sb.append(", "+k.i_codeKey);
		}
		sb.append(",\""+ts_ghostKeys+"\",\""+ts_actualKeys+"\",\""+ts_computedKeys+"\"");
		return sb.toString();
	}

	@Override
	public int compareTo(Ghost g) {
		return toString().compareTo(g.toString());
	}

}
