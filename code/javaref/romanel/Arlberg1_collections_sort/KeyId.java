
public class KeyId implements Comparable<KeyId> {
	/*struct KeyId
	{
	    TCHAR *name;
	    unsigned char vkey;    // VK_ codes
	    unsigned char dik;     // DIK_ codes (directinput, scancode)
	    WORD          hid;     // HID usage codes
	    bool     extended;     // the key is an extended key
	};

	struct IncorrectKey
	{
	    unsigned char vkey;    // VK_ codes
	    unsigned char dik;     // DIK_ codes (directinput, scancode)
	};*/
	String name=null;
	int vkey;
	int dik;
	int hid;
	boolean extended=false;
	boolean IncorrectKey=false;
	boolean SpecialKey=false;
	int i_keyId=0;

	public KeyId(String name, int vkey, int dik, int hid) throws Exception {
		this.name = new String(name);
		this.vkey=vkey;
		this.dik=dik;
		this.hid=hid;
		i_keyId=vkey + (0x10000*dik);
		if(!valid()) {
			throw new Exception("Invalid KeyId: " + toString3());
		}
	}

	public KeyId(String name, int vkey, int dik, int hid, boolean extended) throws Exception {
		this.name = new String(name);
		this.vkey=vkey;
		this.dik=dik;
		this.hid=hid;
		this.extended=extended;
		this.SpecialKey=true;
		i_keyId=vkey + (0x10000*dik);
		if(!valid()) {
			throw new Exception("Invalid KeyId: " + toString3());
		}
	}

	public KeyId(int vkey, int dik) throws Exception {
		this.vkey=vkey;
		this.dik=dik;
		this.IncorrectKey=true;
		i_keyId=vkey + (0x10000*dik);
		if(!valid()) {
			throw new Exception("Invalid KeyId: " + toString3());
		}
	}

	public KeyId(String s_keyId) throws Exception {
		i_keyId = Integer.valueOf(s_keyId.substring(2), 16);
		vkey=(int)(i_keyId & 0xFF);
		dik=(int)((i_keyId/0x10000) & 0xFF);
		if(!valid()) {
			throw new Exception("Invalid KeyId: " + toString3());
		}
	}

	public KeyId() {
		name = new String("Invalid");
		vkey=0xfff;
		dik=0xfff;
		hid=0;
		i_keyId=vkey + (0x10000*dik);
	}

	String toString3() {
		if(name==null){
			return new String("0x"+Integer.toHexString(i_keyId).toUpperCase());
		} else {
			return new String(name+ ": 0x"+Integer.toHexString(i_keyId).toUpperCase());
		}
	}

	public boolean valid(){
		return i_keyId!=0;
	}

	public boolean equals(Object o){
		return compareTo((KeyId)o)==0;
	}

	public int compareTo(KeyId o) {
		//		int i= (vkey*0x100)+(hid*0x10000)+dik;
		//		int oi= (o.vkey*0x100)+(o.hid*0x10000)+o.dik;
		return i_keyId-o.i_keyId;
	}

	public String toString2() {
		if(name==null){
			return new String("0x"+Integer.toHexString(i_keyId).toUpperCase());
		} else {
			return name;
		}
	}


}
