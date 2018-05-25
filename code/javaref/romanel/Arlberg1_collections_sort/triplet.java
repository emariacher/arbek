import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;



public class triplet implements Comparable<triplet> {
	public List<KeyId> l_xplet_keyid = new ArrayList<KeyId>();
	public int count=0;
	Log L;

	public triplet(List<String> l_xplet, KeyMapping km, Log L) throws Exception {
		this.L=L;
		Iterator<String> i = l_xplet.iterator();

		while(i.hasNext()){
			String s_KeyId = (String) i.next();
			KeyId ki= new KeyId(s_KeyId);
			l_xplet_keyid.add(km.find(ki));
		}
		Collections.sort(l_xplet_keyid);
		L.myPrint(toString3());
	}

	public boolean equals(Object o){
		return compareTo((triplet)o)==0;
	}

	@Override
	public int compareTo(triplet o) {
		/*int compared = count-o.count;
		if(compared!=0) {
			return compared;
		}*/
		Iterator<KeyId> i = l_xplet_keyid.iterator();
		Iterator<KeyId> io = o.l_xplet_keyid.iterator();
		while(i.hasNext()){
			KeyId ki = (KeyId) i.next();
			KeyId kio = (KeyId) io.next();
			int compared = ki.compareTo(kio);
			if(compared!=0) {
				return compared;
			}
		}
		return 0;
	}

	public String toString2() {
		StringBuffer sb= new StringBuffer("\"[");
		Iterator<KeyId> i = l_xplet_keyid.iterator();
		while(i.hasNext()){
			KeyId ki = (KeyId) i.next();
			sb.append(ki.toString2()+",");
		}
		sb.append("]\","+ count);
		return sb.toString();
	}
	public String toString3() {
		StringBuffer sb= new StringBuffer("\"[");
		Iterator<KeyId> i = l_xplet_keyid.iterator();
		while(i.hasNext()){
			KeyId ki = (KeyId) i.next();
			sb.append(ki.toString3()+",");
		}
		sb.append("]\","+ count);
		return sb.toString();
	}

}
