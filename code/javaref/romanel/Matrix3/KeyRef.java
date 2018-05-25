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
 * emariacher - Tuesday, November 17, 2009 4:12:07 PM
 * added some key definitions
 * generate a csv file
 * emariacher - Friday, November 13, 2009 4:40:13 PM
 * still debugging checkduplicate keys
 * emariacher - Thursday, November 12, 2009 4:44:35 PM
 * suppressed the smblist
 * still some exceptions.
 * emariacher - Thursday, November 12, 2009 3:41:28 PM
*/

public class KeyRef {

	private int i_whqlpos;
	private String s_hidcode;
	private String s_doc;
	public int i_cpt = 0;
	public boolean b_isFnKey;

	public KeyRef(int i_whqlpos, String s_hidcode, String s_doc) {
		this.i_whqlpos = i_whqlpos;
		this.s_hidcode = s_hidcode;
		this.s_doc = s_doc;
	}

	public KeyRef(int i_whqlpos, String s_hidcode, String s_doc, boolean b_isFnKey) {
		this.i_whqlpos = i_whqlpos;
		this.s_hidcode = s_hidcode;
		this.s_doc = s_doc;
		this.b_isFnKey = b_isFnKey;
	}

	public String toStringDoc() {
		return String.format(" %1$6s ", s_doc);
	}

	public String toStringDecl(int i) {
		return String.format(" %1$35s, // %2$3d %3$3d/0x%3$02X %4$s", s_hidcode, i_whqlpos, i, s_doc);
	}
	
	public String toString() {
		return toStringDecl(i_cpt);
	}

	public String toStringCsv() {
		return String.format("%2$3d, %1$35s, \"%3$s\"", s_hidcode, i_whqlpos, s_doc);
	}

	public String toStringHIDcode(int i_index) {
		return s_hidcode;
	}

}
