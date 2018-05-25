/*
* $Log$
* emariacher - Wednesday, November 25, 2009 4:54:17 PM
* trying to kbdm_FnKey...
* emariacher - Wednesday, November 25, 2009 3:25:37 PM
* kbd_map.c file generated
* emariacher - Tuesday, November 24, 2009 5:31:29 PM
* some table correction
* emariacher - Tuesday, November 24, 2009 4:35:02 PM
* debugging with Ren Matrix
* emariacher - Wednesday, November 18, 2009 9:47:53 AM
* no bug in sight
* today kbdmap.c
* tomorrow kbdmap.c
* emariacher - Tuesday, November 17, 2009 6:06:35 PM
* still debugging...
* emariacher - Tuesday, November 17, 2009 4:12:07 PM
* added some key definitions
* generate a csv file
* emariacher - Tuesday, November 17, 2009 11:47:07 AM
* matrix in html part I
* emariacher - Tuesday, November 17, 2009 11:29:56 AM
* cleaning html err area
* emariacher - Friday, November 13, 2009 6:12:38 PM
* added a lot of WHQL keys
* emariacher - Thursday, November 12, 2009 4:44:35 PM
* suppressed the smblist
* still some exceptions.
* emariacher - Thursday, November 12, 2009 3:41:28 PM
*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Map.Entry;


public class KeyRefTable {
	TreeMap<Integer, KeyRef> m_KeyRefTable = new TreeMap<Integer, KeyRef>(); ;
	TreeMap<String, Integer> m_KeyRefTableInv = new TreeMap<String, Integer>(); 
	ArrayList<ArrayList<Integer>> WHQL_ExceptLst = new ArrayList<ArrayList<Integer>>();
	
	
	
	int i_index(int k) {
		return m_KeyRefTable.headMap(k).size();
	}
	
	public String toStringDoc(Integer it_whqlpos) {
		try {
			return m_KeyRefTable.get(it_whqlpos).toStringDoc();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String("[whqlpos="+it_whqlpos+"]");
	}
	
	public String toStringDecl(Integer it_whqlpos, Integer i) {
		return m_KeyRefTable.get(it_whqlpos).toStringDecl(i);
	}
	
	public String toStringHIDcode(Integer it_whqlpos) {
	return m_KeyRefTable.get(it_whqlpos).toStringHIDcode(i_index(it_whqlpos));	}
	
	private TreeMap<String, Integer> generateInvTable() {
		for (Entry<Integer, KeyRef> keyref : m_KeyRefTable.entrySet()) {
			m_KeyRefTableInv.put(keyref.getValue().toStringDoc(), keyref.getKey());
		}
		return m_KeyRefTableInv;
	}
	
	public String toStringCsv() {
		StringBuffer sb = new StringBuffer();
		for(KeyRef kr : m_KeyRefTable.values()) {
			sb.append(kr.toStringCsv()+"\n");
		}
		return sb.toString();
	}
	
	
	
	
	KeyRefTable() {
		m_KeyRefTable.put(0,new KeyRef(0, "(0x0000 + kbdm_UNDEF_PAGE )","empty"));
		m_KeyRefTable.put(1,new KeyRef(1,  "(0x0035 + kbdm_KBD_PAGE   )","~"));
		m_KeyRefTable.put(2,new KeyRef(2,  "(0x001E + kbdm_KBD_PAGE   )","1!"));
		m_KeyRefTable.put(3,new KeyRef(3,  "(0x001F + kbdm_KBD_PAGE   )","2@"));
		m_KeyRefTable.put(4,new KeyRef(4,  "(0x0020 + kbdm_KBD_PAGE   )","3/"));
		m_KeyRefTable.put(5,new KeyRef(5,  "(0x0021 + kbdm_KBD_PAGE   )","4$"));
		m_KeyRefTable.put(6,new KeyRef(6,  "(0x0022 + kbdm_KBD_PAGE   )","5%"));
		m_KeyRefTable.put(7,new KeyRef(7,  "(0x0023 + kbdm_KBD_PAGE   )","6^"));
		m_KeyRefTable.put(8,new KeyRef(8,  "(0x0024 + kbdm_KBD_PAGE   )","7&"));
		m_KeyRefTable.put(9,new KeyRef(9,  "(0x0025 + kbdm_KBD_PAGE   )","8*"));
		m_KeyRefTable.put(10,new KeyRef(10,"(0x0026 + kbdm_KBD_PAGE   )","9("));
		m_KeyRefTable.put(11,new KeyRef(11,"(0x0027 + kbdm_KBD_PAGE   )","0)"));
		m_KeyRefTable.put(12,new KeyRef(12,"(0x002D + kbdm_KBD_PAGE   )","-_"));
		m_KeyRefTable.put(13,new KeyRef(13,"(0x002E + kbdm_KBD_PAGE   )","=+"));
		m_KeyRefTable.put(14,new KeyRef(14,"(0x0089 + kbdm_KBD_PAGE   )","¥|"));
		m_KeyRefTable.put(15,new KeyRef(15,"(0x002A + kbdm_KBD_PAGE   )","Bkspace"));
		m_KeyRefTable.put(16,new KeyRef(16,"(0x002B + kbdm_KBD_PAGE   )","Tab"));
		m_KeyRefTable.put(17,new KeyRef(17,"(0x0014 + kbdm_KBD_PAGE   )","Q"));
		m_KeyRefTable.put(18,new KeyRef(18,"(0x001A + kbdm_KBD_PAGE   )","W"));
		m_KeyRefTable.put(19,new KeyRef(19,"(0x0008 + kbdm_KBD_PAGE   )","E"));
		m_KeyRefTable.put(20,new KeyRef(20,"(0x0015 + kbdm_KBD_PAGE   )","R"));
		m_KeyRefTable.put(21,new KeyRef(21,"(0x0017 + kbdm_KBD_PAGE   )","T"));
		m_KeyRefTable.put(22,new KeyRef(22,"(0x001C + kbdm_KBD_PAGE   )","Y"));
		m_KeyRefTable.put(23,new KeyRef(23,"(0x0018 + kbdm_KBD_PAGE   )","U"));
		m_KeyRefTable.put(24,new KeyRef(24,"(0x000C + kbdm_KBD_PAGE   )","I"));
		m_KeyRefTable.put(25,new KeyRef(25,"(0x0012 + kbdm_KBD_PAGE   )","O"));
		m_KeyRefTable.put(26,new KeyRef(26,"(0x0013 + kbdm_KBD_PAGE   )","P"));
		m_KeyRefTable.put(27,new KeyRef(27,"(0x002F + kbdm_KBD_PAGE   )","[{"));
		m_KeyRefTable.put(28,new KeyRef(28,"(0x0030 + kbdm_KBD_PAGE   )","]}"));
		m_KeyRefTable.put(29,new KeyRef(29,"(0x0031 + kbdm_KBD_PAGE   )","Bkslash|"));
		m_KeyRefTable.put(30,new KeyRef(30,"(0x0039 + kbdm_KBD_PAGE   )","CapsLck"));
		m_KeyRefTable.put(31,new KeyRef(31,"(0x0004 + kbdm_KBD_PAGE   )","A"));
		m_KeyRefTable.put(32,new KeyRef(32,"(0x0016 + kbdm_KBD_PAGE   )","S"));
		m_KeyRefTable.put(33,new KeyRef(33,"(0x0007 + kbdm_KBD_PAGE   )","D"));
		m_KeyRefTable.put(34,new KeyRef(34,"(0x0009 + kbdm_KBD_PAGE   )","F"));
		m_KeyRefTable.put(35,new KeyRef(35,"(0x000A + kbdm_KBD_PAGE   )","G"));
		m_KeyRefTable.put(36,new KeyRef(36,"(0x000B + kbdm_KBD_PAGE   )","H"));
		m_KeyRefTable.put(37,new KeyRef(37,"(0x000D + kbdm_KBD_PAGE   )","J"));
		m_KeyRefTable.put(38,new KeyRef(38,"(0x000E + kbdm_KBD_PAGE   )","K"));
		m_KeyRefTable.put(39,new KeyRef(39,"(0x000F + kbdm_KBD_PAGE   )","L"));
		m_KeyRefTable.put(40,new KeyRef(40,"(0x0033 + kbdm_KBD_PAGE   )",";:"));
		m_KeyRefTable.put(41,new KeyRef(41,"(0x0034 + kbdm_KBD_PAGE   )","'dblquot"));
		m_KeyRefTable.put(42,new KeyRef(42,"(0x0032 + kbdm_KBD_PAGE   )","$£"));
		m_KeyRefTable.put(43,new KeyRef(43,"(0x0028 + kbdm_KBD_PAGE   )","Enter"));
		m_KeyRefTable.put(44,new KeyRef(44,"(0x00E1 + kbdm_KBD_PAGE   )","L-Shift"));
		m_KeyRefTable.put(45,new KeyRef(45,"(0x0064 + kbdm_KBD_PAGE   )","&lt;>"));
		m_KeyRefTable.put(46,new KeyRef(46,"(0x001D + kbdm_KBD_PAGE   )","Z"));
		m_KeyRefTable.put(47,new KeyRef(47,"(0x001B + kbdm_KBD_PAGE   )","X"));
		m_KeyRefTable.put(48,new KeyRef(48,"(0x0006 + kbdm_KBD_PAGE   )","C"));
		m_KeyRefTable.put(49,new KeyRef(49,"(0x0019 + kbdm_KBD_PAGE   )","V"));
		m_KeyRefTable.put(50,new KeyRef(50,"(0x0005 + kbdm_KBD_PAGE   )","B"));
		m_KeyRefTable.put(51,new KeyRef(51,"(0x0011 + kbdm_KBD_PAGE   )","N"));
		m_KeyRefTable.put(52,new KeyRef(52,"(0x0010 + kbdm_KBD_PAGE   )","M"));
		m_KeyRefTable.put(53,new KeyRef(53,"(0x0036 + kbdm_KBD_PAGE   )",",&lt;"));
		m_KeyRefTable.put(54,new KeyRef(54,"(0x0037 + kbdm_KBD_PAGE   )",".>"));
		m_KeyRefTable.put(55,new KeyRef(55,"(0x0038 + kbdm_KBD_PAGE   )","/ ?"));
		m_KeyRefTable.put(56,new KeyRef(56,"(0x0087 + kbdm_KBD_PAGE   )","Ro"));
		m_KeyRefTable.put(57,new KeyRef(57,"(0x00E5 + kbdm_KBD_PAGE   )","R-Shift"));
		m_KeyRefTable.put(58,new KeyRef(58,"(0x00E0 + kbdm_KBD_PAGE   )","L-Ctrl"));
		m_KeyRefTable.put(59,new KeyRef(59,"(0x00E3 + kbdm_KBD_PAGE   )","L-GUI"));
		m_KeyRefTable.put(60,new KeyRef(60,"(0x00E2 + kbdm_KBD_PAGE   )","L-Alt"));
		m_KeyRefTable.put(61,new KeyRef(61,"(0x002C + kbdm_KBD_PAGE   )","Space"));
		m_KeyRefTable.put(62,new KeyRef(62,"(0x00E6 + kbdm_KBD_PAGE   )","R-Alt"));
		m_KeyRefTable.put(63,new KeyRef(63,"(0x00XX + kbdm_KBD_PAGE   )","R-GUI"));
		m_KeyRefTable.put(64,new KeyRef(64,"(0x00E4 + kbdm_KBD_PAGE   )","R-Ctrl"));
		m_KeyRefTable.put(75,new KeyRef(75,"(0x0049 + kbdm_KBD_PAGE   )","Insert"));
		m_KeyRefTable.put(76,new KeyRef(76,"(0x004C + kbdm_KBD_PAGE   )","Delete"));
		m_KeyRefTable.put(79,new KeyRef(79,"(0x0050 + kbdm_KBD_PAGE   )","L-Arrow"));
		m_KeyRefTable.put(80,new KeyRef(80,"(0x004A + kbdm_KBD_PAGE   )","Home"));
		m_KeyRefTable.put(81,new KeyRef(81,"(0x004D + kbdm_KBD_PAGE   )","End"));
		m_KeyRefTable.put(83,new KeyRef(83,"(0x0052 + kbdm_KBD_PAGE   )","UpArrow"));
		m_KeyRefTable.put(84,new KeyRef(84,"(0x0051 + kbdm_KBD_PAGE   )","DnArrow"));
		m_KeyRefTable.put(85,new KeyRef(85,"(0x004B + kbdm_KBD_PAGE   )","PageUp"));
		m_KeyRefTable.put(86,new KeyRef(86,"(0x004E + kbdm_KBD_PAGE   )","PageDn"));
		m_KeyRefTable.put(89,new KeyRef(89,"(0x004F + kbdm_KBD_PAGE   )","R-Arrow"));
		m_KeyRefTable.put(90,new KeyRef(90,"(0x0053 + kbdm_KBD_PAGE   )","NumLck"));
		m_KeyRefTable.put(91,new KeyRef(91,"(0x005F + kbdm_KBD_PAGE   )","KP 7"));
		m_KeyRefTable.put(92,new KeyRef(92,"(0x005C + kbdm_KBD_PAGE   )","KP 4"));
		m_KeyRefTable.put(93,new KeyRef(93,"(0x0059 + kbdm_KBD_PAGE   )","KP 1"));
		m_KeyRefTable.put(95,new KeyRef(95,"(0x0054 + kbdm_KBD_PAGE   )","KP /"));
		m_KeyRefTable.put(96,new KeyRef(96,"(0x0060 + kbdm_KBD_PAGE   )","KP 8"));
		m_KeyRefTable.put(97,new KeyRef(97,"(0x005D + kbdm_KBD_PAGE   )","KP 5"));
		m_KeyRefTable.put(98,new KeyRef(98,"(0x005A + kbdm_KBD_PAGE   )","KP 2"));
		m_KeyRefTable.put(99,new KeyRef(99,"(0x0062 + kbdm_KBD_PAGE   )","KP 0"));
		m_KeyRefTable.put(100,new KeyRef(100,"(0x0055 + kbdm_KBD_PAGE   )","KP *"));
		m_KeyRefTable.put(101,new KeyRef(101,"(0x0061 + kbdm_KBD_PAGE   )","KP 9"));
		m_KeyRefTable.put(102,new KeyRef(102,"(0x005E + kbdm_KBD_PAGE   )","KP 6"));
		m_KeyRefTable.put(103,new KeyRef(103,"(0x005B + kbdm_KBD_PAGE   )","KP 3"));
		m_KeyRefTable.put(104,new KeyRef(104,"(0x0063 + kbdm_KBD_PAGE   )","KP ."));
		m_KeyRefTable.put(105,new KeyRef(105,"(0x0056 + kbdm_KBD_PAGE   )","KP -"));
		m_KeyRefTable.put(106,new KeyRef(106,"(0x0057 + kbdm_KBD_PAGE   )","KP +"));
		m_KeyRefTable.put(107,new KeyRef(107,"(0x00XX + kbdm_KBD_PAGE   )","KP ,"));
		m_KeyRefTable.put(108,new KeyRef(108,"(0x0058 + kbdm_KBD_PAGE   )","KP Enter"));
		m_KeyRefTable.put(110,new KeyRef(110,"(0x0029 + kbdm_KBD_PAGE   )","Esc"));
		m_KeyRefTable.put(112,new KeyRef(112,"(0x003A + kbdm_KBD_PAGE   )","F1"));
		m_KeyRefTable.put(113,new KeyRef(113,"(0x003B + kbdm_KBD_PAGE   )","F2"));
		m_KeyRefTable.put(114,new KeyRef(114,"(0x003C + kbdm_KBD_PAGE   )","F3"));
		m_KeyRefTable.put(115,new KeyRef(115,"(0x003D + kbdm_KBD_PAGE   )","F4"));
		m_KeyRefTable.put(116,new KeyRef(116,"(0x003E + kbdm_KBD_PAGE   )","F5"));
		m_KeyRefTable.put(117,new KeyRef(117,"(0x003F + kbdm_KBD_PAGE   )","F6"));
		m_KeyRefTable.put(118,new KeyRef(118,"(0x0040 + kbdm_KBD_PAGE   )","F7"));
		m_KeyRefTable.put(119,new KeyRef(119,"(0x0041 + kbdm_KBD_PAGE   )","F8"));
		m_KeyRefTable.put(120,new KeyRef(120,"(0x0042 + kbdm_KBD_PAGE   )","F9"));
		m_KeyRefTable.put(121,new KeyRef(121,"(0x0043 + kbdm_KBD_PAGE   )","F10"));
		m_KeyRefTable.put(122,new KeyRef(122,"(0x0044 + kbdm_KBD_PAGE   )","F11"));
		m_KeyRefTable.put(123,new KeyRef(123,"(0x0045 + kbdm_KBD_PAGE   )","F12"));
		m_KeyRefTable.put(124,new KeyRef(124,"(0x0046 + kbdm_KBD_PAGE   )","PrntScrn"));
		m_KeyRefTable.put(125,new KeyRef(125,"(0x0047 + kbdm_KBD_PAGE   )","ScrLck"));
		m_KeyRefTable.put(126,new KeyRef(126,"(0x0048 + kbdm_KBD_PAGE   )","PauseBrk"));
		m_KeyRefTable.put(131,new KeyRef(131,"(0x0065 + kbdm_KBD_PAGE   )","App"));
		m_KeyRefTable.put(132,new KeyRef(132,"(0x008B + kbdm_KBD_PAGE   )","Muhenkan"));
		m_KeyRefTable.put(133,new KeyRef(133,"(0x008A + kbdm_KBD_PAGE   )","Henkan"));
		m_KeyRefTable.put(134,new KeyRef(134,"(0x0088 + kbdm_KBD_PAGE   )","KataHira"));
		m_KeyRefTable.put(135,new KeyRef(135,"(0x0091 + kbdm_KBD_PAGE   )","Hanja"));
		m_KeyRefTable.put(136,new KeyRef(136,"(0x0090 + kbdm_KBD_PAGE   )","Hanguel"));
		m_KeyRefTable.put(137,new KeyRef(137,"(0x0067 + kbdm_KBD_PAGE   )","KP="));
		m_KeyRefTable.put(141,new KeyRef(141,"(0x00XX + kbdm_KBD_PAGE   )","L-Button"));
		m_KeyRefTable.put(142,new KeyRef(142,"(0x00XX + kbdm_KBD_PAGE   )","R-Button"));
		m_KeyRefTable.put(143,new KeyRef(143,"(0x00XX + kbdm_KBD_PAGE   )","M-Button"));
		m_KeyRefTable.put(151,new KeyRef(151,"(0x002C + kbdm_BUTTON_PAGE)","L-Fn",true));
		m_KeyRefTable.put(152,new KeyRef(152,"(0x002C + kbdm_BUTTON_PAGE)","R-Fn",true));
			
		/* For Mac */
		m_KeyRefTable.put(263,new KeyRef(263,"(0x002C + kbdm_BUTTON_PAGE)","FnKeyMAC",true));
		m_KeyRefTable.put(264,new KeyRef(264,"(0x0068 + kbdm_KBD_PAGE   )","F13"));
		m_KeyRefTable.put(265,new KeyRef(265,"(0x0069 + kbdm_KBD_PAGE   )","F14"));
		m_KeyRefTable.put(266,new KeyRef(266,"(0x006A + kbdm_KBD_PAGE   )","F15"));
		m_KeyRefTable.put(267,new KeyRef(267,"(0x006B + kbdm_KBD_PAGE   )","F16"));
		m_KeyRefTable.put(268,new KeyRef(268,"(0x006C + kbdm_KBD_PAGE   )","F17"));
		m_KeyRefTable.put(269,new KeyRef(269,"(0x006D + kbdm_KBD_PAGE   )","F18"));
		m_KeyRefTable.put(270,new KeyRef(270,"(0x006E + kbdm_KBD_PAGE   )","F19"));
		m_KeyRefTable.put(271,new KeyRef(271,"(0x006F + kbdm_BUTTON_PAGE)","Bright-"));
		m_KeyRefTable.put(272,new KeyRef(272,"(0x006E + kbdm_BUTTON_PAGE)","Bright+"));
		m_KeyRefTable.put(273,new KeyRef(273,"(0x003E + kbdm_BUTTON_PAGE)","Present"));
		m_KeyRefTable.put(274,new KeyRef(274,"(0x003D + kbdm_BUTTON_PAGE)","Dashbrd"));
		m_KeyRefTable.put(275,new KeyRef(275,"(0x0045 + kbdm_BUTTON_PAGE)","F5"));
		m_KeyRefTable.put(276,new KeyRef(276,"(0x0046 + kbdm_BUTTON_PAGE)","F6"));
		m_KeyRefTable.put(277,new KeyRef(277,"(0x0000 + kbdm_UNDEF_PAGE )","Prev Track"));
		m_KeyRefTable.put(278,new KeyRef(278,"(0x00CD + kbdm_CONSUM_PAGE)","Play / Pause"));
		m_KeyRefTable.put(279,new KeyRef(279,"(0x0000 + kbdm_UNDEF_PAGE )","Next Track"));
		m_KeyRefTable.put(280,new KeyRef(280,"(0x00E2 + kbdm_CONSUM_PAGE)","Mute"));
		m_KeyRefTable.put(281,new KeyRef(281,"(0x00EA + kbdm_CONSUM_PAGE)","Vol-"));
		m_KeyRefTable.put(282,new KeyRef(282,"(0x00E9 + kbdm_CONSUM_PAGE)","Vol+"));
		m_KeyRefTable.put(283,new KeyRef(283,"(0x006B + kbdm_BUTTON_PAGE)","CoverFlow"));
		m_KeyRefTable.put(284,new KeyRef(284,"(0x006C + kbdm_BUTTON_PAGE)","QuickLook"));
		m_KeyRefTable.put(285,new KeyRef(285,"(0x006D + kbdm_BUTTON_PAGE)","Spaces"));
		m_KeyRefTable.put(286,new KeyRef(286,"(0x0183 + kbdm_CONSUM_PAGE)","iTunes"));
		m_KeyRefTable.put(287,new KeyRef(287,"(0x018A + kbdm_CONSUM_PAGE)","Email"));
		m_KeyRefTable.put(289,new KeyRef(289,"(0x0192 + kbdm_CONSUM_PAGE)","iCalc"));
		
		
		m_KeyRefTable.put(400,new KeyRef(400,"(0x00D8 + kbdm_KBD_PAGE   )","Keypad Clear"));
		m_KeyRefTable.put(401,new KeyRef(401,"(0x00B6 + kbdm_KBD_PAGE   )","Keypad Left Parenthesis"));
		m_KeyRefTable.put(402,new KeyRef(402,"(0x00B7 + kbdm_KBD_PAGE   )","Keypad Right Parenthesis"));
		
		m_KeyRefTable.put(409,new KeyRef(409,"(0x0001 + kbdm_CONSUM_PAGE)","Consumer Control"));
		m_KeyRefTable.put(410,new KeyRef(410,"(0x0002 + kbdm_CONSUM_PAGE)","Numeric Key Pad"));
		m_KeyRefTable.put(411,new KeyRef(411,"(0x0003 + kbdm_CONSUM_PAGE)","Programmable Buttons"));
		m_KeyRefTable.put(412,new KeyRef(412,"(0x0004 + kbdm_CONSUM_PAGE)","Microphone"));
		m_KeyRefTable.put(413,new KeyRef(413,"(0x0005 + kbdm_CONSUM_PAGE)","Headphone"));
		m_KeyRefTable.put(414,new KeyRef(414,"(0x0006 + kbdm_CONSUM_PAGE)","Graphic Equalizer"));
		m_KeyRefTable.put(415,new KeyRef(415,"(0x0020 + kbdm_CONSUM_PAGE)","+10"));
		m_KeyRefTable.put(416,new KeyRef(416,"(0x0021 + kbdm_CONSUM_PAGE)","+100"));
		m_KeyRefTable.put(417,new KeyRef(417,"(0x0022 + kbdm_CONSUM_PAGE)","AM/PM"));
		m_KeyRefTable.put(418,new KeyRef(418,"(0x0030 + kbdm_CONSUM_PAGE)","Power"));
		m_KeyRefTable.put(419,new KeyRef(419,"(0x0031 + kbdm_CONSUM_PAGE)","Reset"));
		m_KeyRefTable.put(420,new KeyRef(420,"(0x0032 + kbdm_CONSUM_PAGE)","Sleep"));
		m_KeyRefTable.put(421,new KeyRef(421,"(0x0033 + kbdm_CONSUM_PAGE)","Sleep After"));
		m_KeyRefTable.put(422,new KeyRef(422,"(0x0034 + kbdm_CONSUM_PAGE)","Sleep Mode"));
		m_KeyRefTable.put(423,new KeyRef(423,"(0x0035 + kbdm_CONSUM_PAGE)","Illumination"));
		m_KeyRefTable.put(424,new KeyRef(424,"(0x0036 + kbdm_CONSUM_PAGE)","Function Buttons"));
		m_KeyRefTable.put(425,new KeyRef(425,"(0x0040 + kbdm_CONSUM_PAGE)","Menu"));
		m_KeyRefTable.put(426,new KeyRef(426,"(0x0041 + kbdm_CONSUM_PAGE)","Menu Pick"));
		m_KeyRefTable.put(427,new KeyRef(427,"(0x0042 + kbdm_CONSUM_PAGE)","Menu Up"));
		m_KeyRefTable.put(428,new KeyRef(428,"(0x0043 + kbdm_CONSUM_PAGE)","Menu Down"));
		m_KeyRefTable.put(429,new KeyRef(429,"(0x0044 + kbdm_CONSUM_PAGE)","Menu Left"));
		m_KeyRefTable.put(430,new KeyRef(430,"(0x0045 + kbdm_CONSUM_PAGE)","Menu Right"));
		m_KeyRefTable.put(431,new KeyRef(431,"(0x0046 + kbdm_CONSUM_PAGE)","Menu Escape"));
		m_KeyRefTable.put(432,new KeyRef(432,"(0x0047 + kbdm_CONSUM_PAGE)","Menu Value Increase"));
		m_KeyRefTable.put(433,new KeyRef(433,"(0x0048 + kbdm_CONSUM_PAGE)","Menu Value Decrease"));
		m_KeyRefTable.put(434,new KeyRef(434,"(0x0060 + kbdm_CONSUM_PAGE)","Data On Screen"));
		m_KeyRefTable.put(435,new KeyRef(435,"(0x0061 + kbdm_CONSUM_PAGE)","Closed Caption"));
		m_KeyRefTable.put(436,new KeyRef(436,"(0x0062 + kbdm_CONSUM_PAGE)","Closed Caption Select"));
		m_KeyRefTable.put(437,new KeyRef(437,"(0x0063 + kbdm_CONSUM_PAGE)","VCR/TV"));
		m_KeyRefTable.put(438,new KeyRef(438,"(0x0064 + kbdm_CONSUM_PAGE)","Broadcast Mode"));
		m_KeyRefTable.put(439,new KeyRef(439,"(0x0065 + kbdm_CONSUM_PAGE)","Snapshot"));
		m_KeyRefTable.put(440,new KeyRef(440,"(0x0066 + kbdm_CONSUM_PAGE)","Still"));
		m_KeyRefTable.put(441,new KeyRef(441,"(0x0080 + kbdm_CONSUM_PAGE)","Selection"));
		m_KeyRefTable.put(442,new KeyRef(442,"(0x0081 + kbdm_CONSUM_PAGE)","Assign Selection"));
		m_KeyRefTable.put(443,new KeyRef(443,"(0x0082 + kbdm_CONSUM_PAGE)","Mode Step"));
		m_KeyRefTable.put(444,new KeyRef(444,"(0x0083 + kbdm_CONSUM_PAGE)","Recall Last"));
		m_KeyRefTable.put(445,new KeyRef(445,"(0x0084 + kbdm_CONSUM_PAGE)","Enter Channel"));
		m_KeyRefTable.put(446,new KeyRef(446,"(0x0085 + kbdm_CONSUM_PAGE)","Order Movie"));
		m_KeyRefTable.put(447,new KeyRef(447,"(0x0086 + kbdm_CONSUM_PAGE)","Channel"));
		m_KeyRefTable.put(448,new KeyRef(448,"(0x0087 + kbdm_CONSUM_PAGE)","Media Selection"));
		m_KeyRefTable.put(449,new KeyRef(449,"(0x0088 + kbdm_CONSUM_PAGE)","Media Select Computer"));
		m_KeyRefTable.put(450,new KeyRef(450,"(0x0089 + kbdm_CONSUM_PAGE)","Media Select TV"));
		m_KeyRefTable.put(451,new KeyRef(451,"(0x008A + kbdm_CONSUM_PAGE)","Media Select WWW"));
		m_KeyRefTable.put(452,new KeyRef(452,"(0x008B + kbdm_CONSUM_PAGE)","Media Select DVD"));
		m_KeyRefTable.put(453,new KeyRef(453,"(0x008C + kbdm_CONSUM_PAGE)","Media Select Telephone"));
		m_KeyRefTable.put(454,new KeyRef(454,"(0x008D + kbdm_CONSUM_PAGE)","Media Select Program Guide"));
		m_KeyRefTable.put(455,new KeyRef(455,"(0x008E + kbdm_CONSUM_PAGE)","Media Select Video Phone"));
		m_KeyRefTable.put(456,new KeyRef(456,"(0x008F + kbdm_CONSUM_PAGE)","Media Select Games"));
		m_KeyRefTable.put(457,new KeyRef(457,"(0x0090 + kbdm_CONSUM_PAGE)","Media Select Messages"));
		m_KeyRefTable.put(458,new KeyRef(458,"(0x0091 + kbdm_CONSUM_PAGE)","Media Select CD"));
		m_KeyRefTable.put(459,new KeyRef(459,"(0x0092 + kbdm_CONSUM_PAGE)","Media Select VCR"));
		m_KeyRefTable.put(460,new KeyRef(460,"(0x0093 + kbdm_CONSUM_PAGE)","Media Select Tuner"));
		m_KeyRefTable.put(461,new KeyRef(461,"(0x0094 + kbdm_CONSUM_PAGE)","Quit"));
		m_KeyRefTable.put(462,new KeyRef(462,"(0x0095 + kbdm_CONSUM_PAGE)","Help"));
		m_KeyRefTable.put(463,new KeyRef(463,"(0x0096 + kbdm_CONSUM_PAGE)","Media Select Tape"));
		m_KeyRefTable.put(464,new KeyRef(464,"(0x0097 + kbdm_CONSUM_PAGE)","Media Select Cable"));
		m_KeyRefTable.put(465,new KeyRef(465,"(0x0098 + kbdm_CONSUM_PAGE)","Media Select Satellite"));
		m_KeyRefTable.put(466,new KeyRef(466,"(0x0099 + kbdm_CONSUM_PAGE)","Media Select Security"));
		m_KeyRefTable.put(467,new KeyRef(467,"(0x009A + kbdm_CONSUM_PAGE)","Media Select Home"));
		m_KeyRefTable.put(468,new KeyRef(468,"(0x009B + kbdm_CONSUM_PAGE)","Media Select Call"));
		m_KeyRefTable.put(469,new KeyRef(469,"(0x009C + kbdm_CONSUM_PAGE)","Channel Increment"));
		m_KeyRefTable.put(470,new KeyRef(470,"(0x009D + kbdm_CONSUM_PAGE)","Channel Decrement"));
		m_KeyRefTable.put(471,new KeyRef(471,"(0x009E + kbdm_CONSUM_PAGE)","Media Select"));
		m_KeyRefTable.put(472,new KeyRef(472,"(0x00A0 + kbdm_CONSUM_PAGE)","VCR Plus"));
		m_KeyRefTable.put(473,new KeyRef(473,"(0x00A1 + kbdm_CONSUM_PAGE)","Once"));
		m_KeyRefTable.put(474,new KeyRef(474,"(0x00A2 + kbdm_CONSUM_PAGE)","Daily"));
		m_KeyRefTable.put(475,new KeyRef(475,"(0x00A3 + kbdm_CONSUM_PAGE)","Weekly"));
		m_KeyRefTable.put(476,new KeyRef(476,"(0x00A4 + kbdm_CONSUM_PAGE)","Monthly"));
		m_KeyRefTable.put(477,new KeyRef(477,"(0x00B0 + kbdm_CONSUM_PAGE)","Play"));
		m_KeyRefTable.put(478,new KeyRef(478,"(0x00B1 + kbdm_CONSUM_PAGE)","Pause"));
		m_KeyRefTable.put(479,new KeyRef(479,"(0x00B2 + kbdm_CONSUM_PAGE)","Record"));
		m_KeyRefTable.put(480,new KeyRef(480,"(0x00B3 + kbdm_CONSUM_PAGE)","Fast Forward"));
		m_KeyRefTable.put(481,new KeyRef(481,"(0x00B4 + kbdm_CONSUM_PAGE)","Rewind"));
		m_KeyRefTable.put(482,new KeyRef(482,"(0x00B5 + kbdm_CONSUM_PAGE)","Scan Next Track"));
		m_KeyRefTable.put(483,new KeyRef(483,"(0x00B6 + kbdm_CONSUM_PAGE)","Scan Previous Track"));
		m_KeyRefTable.put(484,new KeyRef(484,"(0x00B7 + kbdm_CONSUM_PAGE)","Stop"));
		m_KeyRefTable.put(485,new KeyRef(485,"(0x00B8 + kbdm_CONSUM_PAGE)","Eject"));
		m_KeyRefTable.put(486,new KeyRef(486,"(0x00B9 + kbdm_CONSUM_PAGE)","Random Play"));
		m_KeyRefTable.put(487,new KeyRef(487,"(0x00BA + kbdm_CONSUM_PAGE)","Select Disc"));
		m_KeyRefTable.put(488,new KeyRef(488,"(0x00BB + kbdm_CONSUM_PAGE)","Enter Disc"));
		m_KeyRefTable.put(489,new KeyRef(489,"(0x00BC + kbdm_CONSUM_PAGE)","Repeat"));
		m_KeyRefTable.put(490,new KeyRef(490,"(0x00BD + kbdm_CONSUM_PAGE)","Tracking"));
		m_KeyRefTable.put(491,new KeyRef(491,"(0x00BE + kbdm_CONSUM_PAGE)","Track Normal"));
		m_KeyRefTable.put(492,new KeyRef(492,"(0x00BF + kbdm_CONSUM_PAGE)","Slow Tracking"));
		m_KeyRefTable.put(493,new KeyRef(493,"(0x00C0 + kbdm_CONSUM_PAGE)","Frame Forward"));
		m_KeyRefTable.put(494,new KeyRef(494,"(0x00C1 + kbdm_CONSUM_PAGE)","Frame Back"));
		m_KeyRefTable.put(495,new KeyRef(495,"(0x00C2 + kbdm_CONSUM_PAGE)","Mark"));
		m_KeyRefTable.put(496,new KeyRef(496,"(0x00C3 + kbdm_CONSUM_PAGE)","Clear Mark"));
		m_KeyRefTable.put(497,new KeyRef(497,"(0x00C4 + kbdm_CONSUM_PAGE)","Repeat From Mark"));
		m_KeyRefTable.put(498,new KeyRef(498,"(0x00C5 + kbdm_CONSUM_PAGE)","Return To Mark"));
		m_KeyRefTable.put(499,new KeyRef(499,"(0x00C6 + kbdm_CONSUM_PAGE)","Search Mark Forward"));
		m_KeyRefTable.put(500,new KeyRef(500,"(0x00C7 + kbdm_CONSUM_PAGE)","Search Mark Backwards"));
		m_KeyRefTable.put(501,new KeyRef(501,"(0x00C8 + kbdm_CONSUM_PAGE)","Counter Reset"));
		m_KeyRefTable.put(502,new KeyRef(502,"(0x00C9 + kbdm_CONSUM_PAGE)","Show Counter"));
		m_KeyRefTable.put(503,new KeyRef(503,"(0x00CA + kbdm_CONSUM_PAGE)","Tracking Increment"));
		m_KeyRefTable.put(504,new KeyRef(504,"(0x00CB + kbdm_CONSUM_PAGE)","Tracking Decrement"));
		m_KeyRefTable.put(505,new KeyRef(505,"(0x00CC + kbdm_CONSUM_PAGE)","Stop/Eject"));
		m_KeyRefTable.put(506,new KeyRef(506,"(0x00CD + kbdm_CONSUM_PAGE)","Play/Pause"));
		m_KeyRefTable.put(507,new KeyRef(507,"(0x00CE + kbdm_CONSUM_PAGE)","Play/Skip"));
		m_KeyRefTable.put(508,new KeyRef(508,"(0x00E0 + kbdm_CONSUM_PAGE)","Volume"));
		m_KeyRefTable.put(509,new KeyRef(509,"(0x00E1 + kbdm_CONSUM_PAGE)","Balance"));
		m_KeyRefTable.put(510,new KeyRef(510,"(0x00E2 + kbdm_CONSUM_PAGE)","Mute"));
		m_KeyRefTable.put(511,new KeyRef(511,"(0x00E3 + kbdm_CONSUM_PAGE)","Bass"));
		m_KeyRefTable.put(512,new KeyRef(512,"(0x00E4 + kbdm_CONSUM_PAGE)","Treble"));
		m_KeyRefTable.put(513,new KeyRef(513,"(0x00E5 + kbdm_CONSUM_PAGE)","Bass Boost"));
		m_KeyRefTable.put(514,new KeyRef(514,"(0x00E6 + kbdm_CONSUM_PAGE)","Surround Mode"));
		m_KeyRefTable.put(515,new KeyRef(515,"(0x00E7 + kbdm_CONSUM_PAGE)","Loudness"));
		m_KeyRefTable.put(516,new KeyRef(516,"(0x00E8 + kbdm_CONSUM_PAGE)","MPX"));
		m_KeyRefTable.put(517,new KeyRef(517,"(0x00E9 + kbdm_CONSUM_PAGE)","Volume Increment"));
		m_KeyRefTable.put(518,new KeyRef(518,"(0x00EA + kbdm_CONSUM_PAGE)","Volume Decrement"));
		m_KeyRefTable.put(519,new KeyRef(519,"(0x00F0 + kbdm_CONSUM_PAGE)","Speed Select"));
		m_KeyRefTable.put(520,new KeyRef(520,"(0x00F1 + kbdm_CONSUM_PAGE)","Playback Speed"));
		m_KeyRefTable.put(521,new KeyRef(521,"(0x00F2 + kbdm_CONSUM_PAGE)","Standard Play"));
		m_KeyRefTable.put(522,new KeyRef(522,"(0x00F3 + kbdm_CONSUM_PAGE)","Long Play"));
		m_KeyRefTable.put(523,new KeyRef(523,"(0x00F4 + kbdm_CONSUM_PAGE)","Extended Play"));
		m_KeyRefTable.put(524,new KeyRef(524,"(0x00F5 + kbdm_CONSUM_PAGE)","Slow"));
		m_KeyRefTable.put(525,new KeyRef(525,"(0x0100 + kbdm_CONSUM_PAGE)","Fan Enable"));
		m_KeyRefTable.put(526,new KeyRef(526,"(0x0101 + kbdm_CONSUM_PAGE)","Fan Speed"));
		m_KeyRefTable.put(527,new KeyRef(527,"(0x0102 + kbdm_CONSUM_PAGE)","Light Enable"));
		m_KeyRefTable.put(528,new KeyRef(528,"(0x0103 + kbdm_CONSUM_PAGE)","Light Illumination Level"));
		m_KeyRefTable.put(529,new KeyRef(529,"(0x0104 + kbdm_CONSUM_PAGE)","Climate Control Enable"));
		m_KeyRefTable.put(530,new KeyRef(530,"(0x0105 + kbdm_CONSUM_PAGE)","Room Temperature"));
		m_KeyRefTable.put(531,new KeyRef(531,"(0x0106 + kbdm_CONSUM_PAGE)","Security Enable"));
		m_KeyRefTable.put(532,new KeyRef(532,"(0x0109 + kbdm_CONSUM_PAGE)","Proximity"));
		m_KeyRefTable.put(533,new KeyRef(533,"(0x010A + kbdm_CONSUM_PAGE)","Motion"));
		m_KeyRefTable.put(534,new KeyRef(534,"(0x0150 + kbdm_CONSUM_PAGE)","Balance Right"));
		m_KeyRefTable.put(535,new KeyRef(535,"(0x0151 + kbdm_CONSUM_PAGE)","Balance Left"));
		m_KeyRefTable.put(536,new KeyRef(536,"(0x0152 + kbdm_CONSUM_PAGE)","Bass Increment"));
		m_KeyRefTable.put(537,new KeyRef(537,"(0x0153 + kbdm_CONSUM_PAGE)","Bass Decrement"));
		m_KeyRefTable.put(538,new KeyRef(538,"(0x0154 + kbdm_CONSUM_PAGE)","Treble Increment"));
		m_KeyRefTable.put(539,new KeyRef(539,"(0x0155 + kbdm_CONSUM_PAGE)","Treble Decrement"));
		m_KeyRefTable.put(540,new KeyRef(540,"(0x0160 + kbdm_CONSUM_PAGE)","Speaker System"));
		m_KeyRefTable.put(541,new KeyRef(541,"(0x0161 + kbdm_CONSUM_PAGE)","Channel Left"));
		m_KeyRefTable.put(542,new KeyRef(542,"(0x0162 + kbdm_CONSUM_PAGE)","Channel Right"));
		m_KeyRefTable.put(543,new KeyRef(543,"(0x0163 + kbdm_CONSUM_PAGE)","Channel Center"));
		m_KeyRefTable.put(544,new KeyRef(544,"(0x0164 + kbdm_CONSUM_PAGE)","Channel Front"));
		m_KeyRefTable.put(545,new KeyRef(545,"(0x0165 + kbdm_CONSUM_PAGE)","Channel Center Front"));
		m_KeyRefTable.put(546,new KeyRef(546,"(0x0166 + kbdm_CONSUM_PAGE)","Channel Side"));
		m_KeyRefTable.put(547,new KeyRef(547,"(0x0167 + kbdm_CONSUM_PAGE)","Channel Surround"));
		m_KeyRefTable.put(548,new KeyRef(548,"(0x0168 + kbdm_CONSUM_PAGE)","Channel Low Frequency Enhancement"));
		m_KeyRefTable.put(549,new KeyRef(549,"(0x0169 + kbdm_CONSUM_PAGE)","Channel Top"));
		m_KeyRefTable.put(550,new KeyRef(550,"(0x016A + kbdm_CONSUM_PAGE)","Channel Unknown"));
		m_KeyRefTable.put(551,new KeyRef(551,"(0x0170 + kbdm_CONSUM_PAGE)","Sub-channel"));
		m_KeyRefTable.put(552,new KeyRef(552,"(0x0171 + kbdm_CONSUM_PAGE)","Sub-channel Increment"));
		m_KeyRefTable.put(553,new KeyRef(553,"(0x0172 + kbdm_CONSUM_PAGE)","Sub-channel Decrement"));
		m_KeyRefTable.put(554,new KeyRef(554,"(0x0173 + kbdm_CONSUM_PAGE)","Alternate Audio Increment"));
		m_KeyRefTable.put(555,new KeyRef(555,"(0x0174 + kbdm_CONSUM_PAGE)","Alternate Audio Decrement"));
		m_KeyRefTable.put(556,new KeyRef(556,"(0x0180 + kbdm_CONSUM_PAGE)","Application Launch Buttons"));
		m_KeyRefTable.put(557,new KeyRef(557,"(0x0181 + kbdm_CONSUM_PAGE)","AL Launch Button Configuration Tool"));
		m_KeyRefTable.put(558,new KeyRef(558,"(0x0182 + kbdm_CONSUM_PAGE)","AL Programmable Button Configuration"));
		m_KeyRefTable.put(559,new KeyRef(559,"(0x0183 + kbdm_CONSUM_PAGE)","AL Consumer Control Configuration"));
		m_KeyRefTable.put(560,new KeyRef(560,"(0x0184 + kbdm_CONSUM_PAGE)","AL Word Processor"));
		m_KeyRefTable.put(561,new KeyRef(561,"(0x0185 + kbdm_CONSUM_PAGE)","AL Text Editor"));
		m_KeyRefTable.put(562,new KeyRef(562,"(0x0186 + kbdm_CONSUM_PAGE)","AL Spreadsheet"));
		m_KeyRefTable.put(563,new KeyRef(563,"(0x0187 + kbdm_CONSUM_PAGE)","AL Graphics Editor"));
		m_KeyRefTable.put(564,new KeyRef(564,"(0x0188 + kbdm_CONSUM_PAGE)","AL Presentation App"));
		m_KeyRefTable.put(565,new KeyRef(565,"(0x0189 + kbdm_CONSUM_PAGE)","AL Database App"));
		m_KeyRefTable.put(566,new KeyRef(566,"(0x018A + kbdm_CONSUM_PAGE)","AL Email Reader"));
		m_KeyRefTable.put(567,new KeyRef(567,"(0x018B + kbdm_CONSUM_PAGE)","AL Newsreader"));
		m_KeyRefTable.put(568,new KeyRef(568,"(0x018C + kbdm_CONSUM_PAGE)","AL Voicemail"));
		m_KeyRefTable.put(569,new KeyRef(569,"(0x018D + kbdm_CONSUM_PAGE)","AL Contacts/Address Book"));
		m_KeyRefTable.put(570,new KeyRef(570,"(0x018E + kbdm_CONSUM_PAGE)","AL Calendar/Schedule"));
		m_KeyRefTable.put(571,new KeyRef(571,"(0x018F + kbdm_CONSUM_PAGE)","AL Task/Project Manager"));
		m_KeyRefTable.put(572,new KeyRef(572,"(0x0190 + kbdm_CONSUM_PAGE)","AL Log/Journal/Timecard"));
		m_KeyRefTable.put(573,new KeyRef(573,"(0x0191 + kbdm_CONSUM_PAGE)","AL Checkbook/Finance"));
		m_KeyRefTable.put(574,new KeyRef(574,"(0x0192 + kbdm_CONSUM_PAGE)","AL Calculator"));
		m_KeyRefTable.put(575,new KeyRef(575,"(0x0193 + kbdm_CONSUM_PAGE)","AL A/V Capture/Playback"));
		m_KeyRefTable.put(576,new KeyRef(576,"(0x0194 + kbdm_CONSUM_PAGE)","AL Local Machine Browser"));
		m_KeyRefTable.put(577,new KeyRef(577,"(0x0195 + kbdm_CONSUM_PAGE)","AL LAN/WAN Browser"));
		m_KeyRefTable.put(578,new KeyRef(578,"(0x0196 + kbdm_CONSUM_PAGE)","AL Internet Browser"));
		m_KeyRefTable.put(579,new KeyRef(579,"(0x0197 + kbdm_CONSUM_PAGE)","AL Remote Networking/ISP Connect"));
		m_KeyRefTable.put(580,new KeyRef(580,"(0x0198 + kbdm_CONSUM_PAGE)","AL Network Conference"));
		m_KeyRefTable.put(581,new KeyRef(581,"(0x0199 + kbdm_CONSUM_PAGE)","AL Network Chat"));
		m_KeyRefTable.put(582,new KeyRef(582,"(0x019A + kbdm_CONSUM_PAGE)","AL Telephony/Dialer"));
		m_KeyRefTable.put(583,new KeyRef(583,"(0x019B + kbdm_CONSUM_PAGE)","AL Logon"));
		m_KeyRefTable.put(584,new KeyRef(584,"(0x019C + kbdm_CONSUM_PAGE)","AL Logoff"));
		m_KeyRefTable.put(585,new KeyRef(585,"(0x019D + kbdm_CONSUM_PAGE)","AL Logon/Logoff"));
		m_KeyRefTable.put(586,new KeyRef(586,"(0x019E + kbdm_CONSUM_PAGE)","AL Terminal Lock/Screensaver"));
		m_KeyRefTable.put(587,new KeyRef(587,"(0x019F + kbdm_CONSUM_PAGE)","AL Control Panel"));
		m_KeyRefTable.put(588,new KeyRef(588,"(0x01A0 + kbdm_CONSUM_PAGE)","AL Command Line Processor/Run"));
		m_KeyRefTable.put(589,new KeyRef(589,"(0x01A1 + kbdm_CONSUM_PAGE)","AL Process/Task Manager"));
		m_KeyRefTable.put(590,new KeyRef(590,"(0x01A2 + kbdm_CONSUM_PAGE)","AL Select Task/Application"));
		m_KeyRefTable.put(591,new KeyRef(591,"(0x01A3 + kbdm_CONSUM_PAGE)","AL Next Task/Application"));
		m_KeyRefTable.put(592,new KeyRef(592,"(0x01A4 + kbdm_CONSUM_PAGE)","AL Previous Task/Application"));
		m_KeyRefTable.put(593,new KeyRef(593,"(0x01A5 + kbdm_CONSUM_PAGE)","AL Preemptive Halt Task/Application"));
		m_KeyRefTable.put(594,new KeyRef(594,"(0x01A6 + kbdm_CONSUM_PAGE)","AL Integrated Help Center"));
		m_KeyRefTable.put(595,new KeyRef(595,"(0x01A7 + kbdm_CONSUM_PAGE)","AL Documents"));
		m_KeyRefTable.put(596,new KeyRef(596,"(0x01A8 + kbdm_CONSUM_PAGE)","AL Thesaurus"));
		m_KeyRefTable.put(597,new KeyRef(597,"(0x01A9 + kbdm_CONSUM_PAGE)","AL Dictionary"));
		m_KeyRefTable.put(598,new KeyRef(598,"(0x01AA + kbdm_CONSUM_PAGE)","AL Desktop"));
		m_KeyRefTable.put(599,new KeyRef(599,"(0x01AB + kbdm_CONSUM_PAGE)","AL Spell Check"));
		m_KeyRefTable.put(600,new KeyRef(600,"(0x01AC + kbdm_CONSUM_PAGE)","AL Grammar Check"));
		m_KeyRefTable.put(601,new KeyRef(601,"(0x01AD + kbdm_CONSUM_PAGE)","AL Wireless Status"));
		m_KeyRefTable.put(602,new KeyRef(602,"(0x01AE + kbdm_CONSUM_PAGE)","AL Keyboard Layout"));
		m_KeyRefTable.put(603,new KeyRef(603,"(0x01AF + kbdm_CONSUM_PAGE)","AL Virus Protection"));
		m_KeyRefTable.put(604,new KeyRef(604,"(0x01B0 + kbdm_CONSUM_PAGE)","AL Encryption"));
		m_KeyRefTable.put(605,new KeyRef(605,"(0x01B1 + kbdm_CONSUM_PAGE)","AL Screen Saver"));
		m_KeyRefTable.put(606,new KeyRef(606,"(0x01B2 + kbdm_CONSUM_PAGE)","AL Alarms"));
		m_KeyRefTable.put(607,new KeyRef(607,"(0x01B3 + kbdm_CONSUM_PAGE)","AL Clock"));
		m_KeyRefTable.put(608,new KeyRef(608,"(0x01B4 + kbdm_CONSUM_PAGE)","AL File Browser"));
		m_KeyRefTable.put(609,new KeyRef(609,"(0x01B5 + kbdm_CONSUM_PAGE)","AL Power Status"));
		m_KeyRefTable.put(610,new KeyRef(610,"(0x01B6 + kbdm_CONSUM_PAGE)","AL Image Browser"));
		m_KeyRefTable.put(611,new KeyRef(611,"(0x01B7 + kbdm_CONSUM_PAGE)","AL Audio Browser"));
		m_KeyRefTable.put(612,new KeyRef(612,"(0x01B8 + kbdm_CONSUM_PAGE)","AL Movie Browser"));
		m_KeyRefTable.put(613,new KeyRef(613,"(0x01B9 + kbdm_CONSUM_PAGE)","AL Digital Rights Manager"));
		m_KeyRefTable.put(614,new KeyRef(614,"(0x01BA + kbdm_CONSUM_PAGE)","AL Digital Wallet"));
		m_KeyRefTable.put(615,new KeyRef(615,"(0x01BC + kbdm_CONSUM_PAGE)","AL Instant Messaging"));
		m_KeyRefTable.put(616,new KeyRef(616,"(0x01BD + kbdm_CONSUM_PAGE)","AL OEM Features/Tips/Tutorial Browser"));
		m_KeyRefTable.put(617,new KeyRef(617,"(0x01BE + kbdm_CONSUM_PAGE)","AL OEM Help"));
		m_KeyRefTable.put(618,new KeyRef(618,"(0x01BF + kbdm_CONSUM_PAGE)","AL Online Community"));
		m_KeyRefTable.put(619,new KeyRef(619,"(0x01C0 + kbdm_CONSUM_PAGE)","AL Entertainment Content Browser"));
		m_KeyRefTable.put(620,new KeyRef(620,"(0x01C1 + kbdm_CONSUM_PAGE)","AL Online Shopping Browser"));
		m_KeyRefTable.put(621,new KeyRef(621,"(0x01C2 + kbdm_CONSUM_PAGE)","AL SmartCard Information/Help"));
		m_KeyRefTable.put(622,new KeyRef(622,"(0x01C3 + kbdm_CONSUM_PAGE)","AL Market Monitor/Finance Browser"));
		m_KeyRefTable.put(623,new KeyRef(623,"(0x01C4 + kbdm_CONSUM_PAGE)","AL Customized Corporate News Browser"));
		m_KeyRefTable.put(624,new KeyRef(624,"(0x01C5 + kbdm_CONSUM_PAGE)","AL Online Activity Browser"));
		m_KeyRefTable.put(625,new KeyRef(625,"(0x01C6 + kbdm_CONSUM_PAGE)","AL Research/Search Browser"));
		m_KeyRefTable.put(626,new KeyRef(626,"(0x01C7 + kbdm_CONSUM_PAGE)","AL Audio Player"));
		m_KeyRefTable.put(627,new KeyRef(627,"(0x0200 + kbdm_CONSUM_PAGE)","Generic GUI Application Controls"));
		m_KeyRefTable.put(628,new KeyRef(628,"(0x0201 + kbdm_CONSUM_PAGE)","AC New"));
		m_KeyRefTable.put(629,new KeyRef(629,"(0x0202 + kbdm_CONSUM_PAGE)","AC Open"));
		m_KeyRefTable.put(630,new KeyRef(630,"(0x0203 + kbdm_CONSUM_PAGE)","AC Close"));
		m_KeyRefTable.put(631,new KeyRef(631,"(0x0204 + kbdm_CONSUM_PAGE)","AC Exit"));
		m_KeyRefTable.put(632,new KeyRef(632,"(0x0205 + kbdm_CONSUM_PAGE)","AC Maximize"));
		m_KeyRefTable.put(633,new KeyRef(633,"(0x0206 + kbdm_CONSUM_PAGE)","AC Minimize"));
		m_KeyRefTable.put(634,new KeyRef(634,"(0x0207 + kbdm_CONSUM_PAGE)","AC Save"));
		m_KeyRefTable.put(635,new KeyRef(635,"(0x0208 + kbdm_CONSUM_PAGE)","AC Print"));
		m_KeyRefTable.put(636,new KeyRef(636,"(0x0209 + kbdm_CONSUM_PAGE)","AC Properties"));
		m_KeyRefTable.put(637,new KeyRef(637,"(0x021A + kbdm_CONSUM_PAGE)","AC Undo"));
		m_KeyRefTable.put(638,new KeyRef(638,"(0x021B + kbdm_CONSUM_PAGE)","AC Copy"));
		m_KeyRefTable.put(639,new KeyRef(639,"(0x021C + kbdm_CONSUM_PAGE)","AC Cut"));
		m_KeyRefTable.put(640,new KeyRef(640,"(0x021D + kbdm_CONSUM_PAGE)","AC Paste"));
		m_KeyRefTable.put(641,new KeyRef(641,"(0x021E + kbdm_CONSUM_PAGE)","AC Select All"));
		m_KeyRefTable.put(642,new KeyRef(642,"(0x021F + kbdm_CONSUM_PAGE)","AC Find"));
		m_KeyRefTable.put(643,new KeyRef(643,"(0x0220 + kbdm_CONSUM_PAGE)","AC Find and Replace"));
		m_KeyRefTable.put(644,new KeyRef(644,"(0x0221 + kbdm_CONSUM_PAGE)","AC Search"));
		m_KeyRefTable.put(645,new KeyRef(645,"(0x0222 + kbdm_CONSUM_PAGE)","AC Go To"));
		m_KeyRefTable.put(646,new KeyRef(646,"(0x0223 + kbdm_CONSUM_PAGE)","AC Home"));
		m_KeyRefTable.put(647,new KeyRef(647,"(0x0224 + kbdm_CONSUM_PAGE)","AC Back"));
		m_KeyRefTable.put(648,new KeyRef(648,"(0x0225 + kbdm_CONSUM_PAGE)","AC Forward"));
		m_KeyRefTable.put(649,new KeyRef(649,"(0x0226 + kbdm_CONSUM_PAGE)","AC Stop"));
		m_KeyRefTable.put(650,new KeyRef(650,"(0x0227 + kbdm_CONSUM_PAGE)","AC Refresh"));
		m_KeyRefTable.put(651,new KeyRef(651,"(0x0228 + kbdm_CONSUM_PAGE)","AC Previous Link"));
		m_KeyRefTable.put(652,new KeyRef(652,"(0x0229 + kbdm_CONSUM_PAGE)","AC Next Link"));
		m_KeyRefTable.put(653,new KeyRef(653,"(0x022A + kbdm_CONSUM_PAGE)","AC Bookmarks"));
		m_KeyRefTable.put(654,new KeyRef(654,"(0x022B + kbdm_CONSUM_PAGE)","AC History"));
		m_KeyRefTable.put(655,new KeyRef(655,"(0x022C + kbdm_CONSUM_PAGE)","AC Subscriptions"));
		m_KeyRefTable.put(656,new KeyRef(656,"(0x022D + kbdm_CONSUM_PAGE)","AC Zoom In"));
		m_KeyRefTable.put(657,new KeyRef(657,"(0x022E + kbdm_CONSUM_PAGE)","AC Zoom Out"));
		m_KeyRefTable.put(658,new KeyRef(658,"(0x022F + kbdm_CONSUM_PAGE)","AC Zoom"));
		m_KeyRefTable.put(659,new KeyRef(659,"(0x0230 + kbdm_CONSUM_PAGE)","AC Full Screen View"));
		m_KeyRefTable.put(660,new KeyRef(660,"(0x0231 + kbdm_CONSUM_PAGE)","AC Normal View"));
		m_KeyRefTable.put(661,new KeyRef(661,"(0x0232 + kbdm_CONSUM_PAGE)","AC View Toggle"));
		m_KeyRefTable.put(662,new KeyRef(662,"(0x0233 + kbdm_CONSUM_PAGE)","AC Scroll Up"));
		m_KeyRefTable.put(663,new KeyRef(663,"(0x0234 + kbdm_CONSUM_PAGE)","AC Scroll Down"));
		m_KeyRefTable.put(664,new KeyRef(664,"(0x0235 + kbdm_CONSUM_PAGE)","AC Scroll"));
		m_KeyRefTable.put(665,new KeyRef(665,"(0x0236 + kbdm_CONSUM_PAGE)","AC Pan Left"));
		m_KeyRefTable.put(666,new KeyRef(666,"(0x0237 + kbdm_CONSUM_PAGE)","AC Pan Right"));
		m_KeyRefTable.put(667,new KeyRef(667,"(0x0238 + kbdm_CONSUM_PAGE)","AC Pan"));
		m_KeyRefTable.put(668,new KeyRef(668,"(0x0239 + kbdm_CONSUM_PAGE)","AC New Window"));
		m_KeyRefTable.put(669,new KeyRef(669,"(0x023A + kbdm_CONSUM_PAGE)","AC Tile Horizontally"));
		m_KeyRefTable.put(670,new KeyRef(670,"(0x023B + kbdm_CONSUM_PAGE)","AC Tile Vertically"));
		m_KeyRefTable.put(671,new KeyRef(671,"(0x023C + kbdm_CONSUM_PAGE)","AC Format"));
		m_KeyRefTable.put(672,new KeyRef(672,"(0x023D + kbdm_CONSUM_PAGE)","AC Edit"));
		m_KeyRefTable.put(673,new KeyRef(673,"(0x023E + kbdm_CONSUM_PAGE)","AC Bold"));
		m_KeyRefTable.put(674,new KeyRef(674,"(0x023F + kbdm_CONSUM_PAGE)","AC Italics"));
		m_KeyRefTable.put(675,new KeyRef(675,"(0x0240 + kbdm_CONSUM_PAGE)","AC Underline"));
		m_KeyRefTable.put(676,new KeyRef(676,"(0x0241 + kbdm_CONSUM_PAGE)","AC Strikethrough"));
		m_KeyRefTable.put(677,new KeyRef(677,"(0x0242 + kbdm_CONSUM_PAGE)","AC Subscript"));
		m_KeyRefTable.put(678,new KeyRef(678,"(0x0243 + kbdm_CONSUM_PAGE)","AC Superscript"));
		m_KeyRefTable.put(679,new KeyRef(679,"(0x0244 + kbdm_CONSUM_PAGE)","AC All Caps"));
		m_KeyRefTable.put(680,new KeyRef(680,"(0x0245 + kbdm_CONSUM_PAGE)","AC Rotate"));
		m_KeyRefTable.put(681,new KeyRef(681,"(0x0246 + kbdm_CONSUM_PAGE)","AC Resize"));
		m_KeyRefTable.put(682,new KeyRef(682,"(0x0247 + kbdm_CONSUM_PAGE)","AC Flip horizontal"));
		m_KeyRefTable.put(683,new KeyRef(683,"(0x0248 + kbdm_CONSUM_PAGE)","AC Flip Vertical"));
		m_KeyRefTable.put(684,new KeyRef(684,"(0x0249 + kbdm_CONSUM_PAGE)","AC Mirror Horizontal"));
		m_KeyRefTable.put(685,new KeyRef(685,"(0x024A + kbdm_CONSUM_PAGE)","AC Mirror Vertical"));
		m_KeyRefTable.put(686,new KeyRef(686,"(0x024B + kbdm_CONSUM_PAGE)","AC Font Select"));
		m_KeyRefTable.put(687,new KeyRef(687,"(0x024C + kbdm_CONSUM_PAGE)","AC Font Color"));
		m_KeyRefTable.put(688,new KeyRef(688,"(0x024D + kbdm_CONSUM_PAGE)","AC Font Size"));
		m_KeyRefTable.put(689,new KeyRef(689,"(0x024E + kbdm_CONSUM_PAGE)","AC Justify Left"));
		m_KeyRefTable.put(690,new KeyRef(690,"(0x024F + kbdm_CONSUM_PAGE)","AC Justify Center H"));
		m_KeyRefTable.put(691,new KeyRef(691,"(0x0250 + kbdm_CONSUM_PAGE)","AC Justify Right"));
		m_KeyRefTable.put(692,new KeyRef(692,"(0x0251 + kbdm_CONSUM_PAGE)","AC Justify Block H"));
		m_KeyRefTable.put(693,new KeyRef(693,"(0x0252 + kbdm_CONSUM_PAGE)","AC Justify Top"));
		m_KeyRefTable.put(694,new KeyRef(694,"(0x0253 + kbdm_CONSUM_PAGE)","AC Justify Center V"));
		m_KeyRefTable.put(695,new KeyRef(695,"(0x0254 + kbdm_CONSUM_PAGE)","AC Justify Bottom"));
		m_KeyRefTable.put(696,new KeyRef(696,"(0x0255 + kbdm_CONSUM_PAGE)","AC Justify Block V"));
		m_KeyRefTable.put(697,new KeyRef(697,"(0x0256 + kbdm_CONSUM_PAGE)","AC Indent Decrease"));
		m_KeyRefTable.put(698,new KeyRef(698,"(0x0257 + kbdm_CONSUM_PAGE)","AC Indent Increase"));
		m_KeyRefTable.put(699,new KeyRef(699,"(0x0258 + kbdm_CONSUM_PAGE)","AC Numbered List"));
		m_KeyRefTable.put(700,new KeyRef(700,"(0x0259 + kbdm_CONSUM_PAGE)","AC Restart Numbering"));
		m_KeyRefTable.put(701,new KeyRef(701,"(0x025A + kbdm_CONSUM_PAGE)","AC Bulleted List"));
		m_KeyRefTable.put(702,new KeyRef(702,"(0x025B + kbdm_CONSUM_PAGE)","AC Promote"));
		m_KeyRefTable.put(703,new KeyRef(703,"(0x025C + kbdm_CONSUM_PAGE)","AC Demote"));
		m_KeyRefTable.put(704,new KeyRef(704,"(0x025D + kbdm_CONSUM_PAGE)","AC Yes"));
		m_KeyRefTable.put(705,new KeyRef(705,"(0x025E + kbdm_CONSUM_PAGE)","AC No"));
		m_KeyRefTable.put(706,new KeyRef(706,"(0x025F + kbdm_CONSUM_PAGE)","AC Cancel"));
		m_KeyRefTable.put(707,new KeyRef(707,"(0x0260 + kbdm_CONSUM_PAGE)","AC Catalog"));
		m_KeyRefTable.put(708,new KeyRef(708,"(0x0261 + kbdm_CONSUM_PAGE)","AC Buy/Checkout"));
		m_KeyRefTable.put(709,new KeyRef(709,"(0x0262 + kbdm_CONSUM_PAGE)","AC Add to Cart"));
		m_KeyRefTable.put(710,new KeyRef(710,"(0x0263 + kbdm_CONSUM_PAGE)","AC Expand"));
		m_KeyRefTable.put(711,new KeyRef(711,"(0x0264 + kbdm_CONSUM_PAGE)","AC Expand All"));
		m_KeyRefTable.put(712,new KeyRef(712,"(0x0265 + kbdm_CONSUM_PAGE)","AC Collapse"));
		m_KeyRefTable.put(713,new KeyRef(713,"(0x0266 + kbdm_CONSUM_PAGE)","AC Collapse All"));
		m_KeyRefTable.put(714,new KeyRef(714,"(0x0267 + kbdm_CONSUM_PAGE)","AC Print Preview"));
		m_KeyRefTable.put(715,new KeyRef(715,"(0x0268 + kbdm_CONSUM_PAGE)","AC Paste Special"));
		m_KeyRefTable.put(716,new KeyRef(716,"(0x0269 + kbdm_CONSUM_PAGE)","AC Insert Mode"));
		m_KeyRefTable.put(717,new KeyRef(717,"(0x026A + kbdm_CONSUM_PAGE)","AC Delete"));
		m_KeyRefTable.put(718,new KeyRef(718,"(0x026B + kbdm_CONSUM_PAGE)","AC Lock"));
		m_KeyRefTable.put(719,new KeyRef(719,"(0x026C + kbdm_CONSUM_PAGE)","AC Unlock"));
		m_KeyRefTable.put(720,new KeyRef(720,"(0x026D + kbdm_CONSUM_PAGE)","AC Protect"));
		m_KeyRefTable.put(721,new KeyRef(721,"(0x026E + kbdm_CONSUM_PAGE)","AC Unprotect"));
		m_KeyRefTable.put(722,new KeyRef(722,"(0x026F + kbdm_CONSUM_PAGE)","AC Attach Comment"));
		m_KeyRefTable.put(723,new KeyRef(723,"(0x0270 + kbdm_CONSUM_PAGE)","AC Delete Comment"));
		m_KeyRefTable.put(724,new KeyRef(724,"(0x0271 + kbdm_CONSUM_PAGE)","AC View Comment"));
		m_KeyRefTable.put(725,new KeyRef(725,"(0x0272 + kbdm_CONSUM_PAGE)","AC Select Word"));
		m_KeyRefTable.put(726,new KeyRef(726,"(0x0273 + kbdm_CONSUM_PAGE)","AC Select Sentence"));
		m_KeyRefTable.put(727,new KeyRef(727,"(0x0274 + kbdm_CONSUM_PAGE)","AC Select Paragraph"));
		m_KeyRefTable.put(728,new KeyRef(728,"(0x0275 + kbdm_CONSUM_PAGE)","AC Select Column"));
		m_KeyRefTable.put(729,new KeyRef(729,"(0x0276 + kbdm_CONSUM_PAGE)","AC Select Row"));
		m_KeyRefTable.put(730,new KeyRef(730,"(0x0277 + kbdm_CONSUM_PAGE)","AC Select Table"));
		m_KeyRefTable.put(731,new KeyRef(731,"(0x0278 + kbdm_CONSUM_PAGE)","AC Select Object"));
		m_KeyRefTable.put(732,new KeyRef(732,"(0x0279 + kbdm_CONSUM_PAGE)","AC Redo/Repeat"));
		m_KeyRefTable.put(733,new KeyRef(733,"(0x027A + kbdm_CONSUM_PAGE)","AC Sort"));
		m_KeyRefTable.put(734,new KeyRef(734,"(0x027B + kbdm_CONSUM_PAGE)","AC Sort Ascending"));
		m_KeyRefTable.put(735,new KeyRef(735,"(0x027C + kbdm_CONSUM_PAGE)","AC Sort Descending"));
		m_KeyRefTable.put(736,new KeyRef(736,"(0x027D + kbdm_CONSUM_PAGE)","AC Filter"));
		m_KeyRefTable.put(737,new KeyRef(737,"(0x027E + kbdm_CONSUM_PAGE)","AC Set Clock"));
		m_KeyRefTable.put(738,new KeyRef(738,"(0x027F + kbdm_CONSUM_PAGE)","AC View Clock"));
		m_KeyRefTable.put(739,new KeyRef(739,"(0x0280 + kbdm_CONSUM_PAGE)","AC Select Time Zone"));
		m_KeyRefTable.put(740,new KeyRef(740,"(0x0281 + kbdm_CONSUM_PAGE)","AC Edit Time Zones"));
		m_KeyRefTable.put(741,new KeyRef(741,"(0x0282 + kbdm_CONSUM_PAGE)","AC Set Alarm"));
		m_KeyRefTable.put(742,new KeyRef(742,"(0x0283 + kbdm_CONSUM_PAGE)","AC Clear Alarm"));
		m_KeyRefTable.put(743,new KeyRef(743,"(0x0284 + kbdm_CONSUM_PAGE)","AC Snooze Alarm"));
		m_KeyRefTable.put(744,new KeyRef(744,"(0x0285 + kbdm_CONSUM_PAGE)","AC Reset Alarm"));
		m_KeyRefTable.put(745,new KeyRef(745,"(0x0286 + kbdm_CONSUM_PAGE)","AC Synchronize"));
		m_KeyRefTable.put(746,new KeyRef(746,"(0x0287 + kbdm_CONSUM_PAGE)","AC Send/Receive"));
		m_KeyRefTable.put(747,new KeyRef(747,"(0x0288 + kbdm_CONSUM_PAGE)","AC Send To"));
		m_KeyRefTable.put(748,new KeyRef(748,"(0x0289 + kbdm_CONSUM_PAGE)","AC Reply"));
		m_KeyRefTable.put(749,new KeyRef(749,"(0x028A + kbdm_CONSUM_PAGE)","AC Reply All"));
		m_KeyRefTable.put(750,new KeyRef(750,"(0x028B + kbdm_CONSUM_PAGE)","AC Forward Msg"));
		m_KeyRefTable.put(751,new KeyRef(751,"(0x028C + kbdm_CONSUM_PAGE)","AC Send"));
		m_KeyRefTable.put(752,new KeyRef(752,"(0x028D + kbdm_CONSUM_PAGE)","AC Attach File"));
		m_KeyRefTable.put(753,new KeyRef(753,"(0x028E + kbdm_CONSUM_PAGE)","AC Upload"));
		m_KeyRefTable.put(754,new KeyRef(754,"(0x028F + kbdm_CONSUM_PAGE)","AC Download (Save Target As)"));
		m_KeyRefTable.put(755,new KeyRef(755,"(0x0290 + kbdm_CONSUM_PAGE)","AC Set Borders"));
		m_KeyRefTable.put(756,new KeyRef(756,"(0x0291 + kbdm_CONSUM_PAGE)","AC Insert Row"));
		m_KeyRefTable.put(757,new KeyRef(757,"(0x0292 + kbdm_CONSUM_PAGE)","AC Insert Column"));
		m_KeyRefTable.put(758,new KeyRef(758,"(0x0293 + kbdm_CONSUM_PAGE)","AC Insert File"));
		m_KeyRefTable.put(759,new KeyRef(759,"(0x0294 + kbdm_CONSUM_PAGE)","AC Insert Picture"));
		m_KeyRefTable.put(760,new KeyRef(760,"(0x0295 + kbdm_CONSUM_PAGE)","AC Insert Object"));
		m_KeyRefTable.put(761,new KeyRef(761,"(0x0296 + kbdm_CONSUM_PAGE)","AC Insert Symbol"));
		m_KeyRefTable.put(762,new KeyRef(762,"(0x0297 + kbdm_CONSUM_PAGE)","AC Save and Close"));
		m_KeyRefTable.put(763,new KeyRef(763,"(0x0298 + kbdm_CONSUM_PAGE)","AC Rename"));
		m_KeyRefTable.put(764,new KeyRef(764,"(0x0299 + kbdm_CONSUM_PAGE)","AC Merge"));
		m_KeyRefTable.put(765,new KeyRef(765,"(0x029A + kbdm_CONSUM_PAGE)","AC Split"));
		m_KeyRefTable.put(766,new KeyRef(766,"(0x029B + kbdm_CONSUM_PAGE)","AC Distribute Horizontally"));
		m_KeyRefTable.put(767,new KeyRef(767,"(0x029C + kbdm_CONSUM_PAGE)","AC Distribute Vertically"));
		
		m_KeyRefTable.put(775,new KeyRef(775,"(0x000E + kbdm_BUTTON_PAGE)","HID Button 14:  Preset,new KeyRef(4"));
		m_KeyRefTable.put(776,new KeyRef(776,"(0x0013 + kbdm_BUTTON_PAGE)","HID Button 19:  Webcam"));
		m_KeyRefTable.put(777,new KeyRef(777,"(0x0014 + kbdm_BUTTON_PAGE)","HID Button 20:  Status"));
		m_KeyRefTable.put(778,new KeyRef(778,"(0x0016 + kbdm_BUTTON_PAGE)","HID Button 22:  Burn"));
		m_KeyRefTable.put(779,new KeyRef(779,"(0x0018 + kbdm_BUTTON_PAGE)","HID Button 24MediaLife"));
		m_KeyRefTable.put(780,new KeyRef(780,"(0x0019 + kbdm_BUTTON_PAGE)","HID Button 25:  Preset 1"));
		m_KeyRefTable.put(781,new KeyRef(781,"(0x001A + kbdm_BUTTON_PAGE)","HID Button 26:  Preset 2"));
		m_KeyRefTable.put(782,new KeyRef(782,"(0x001B + kbdm_BUTTON_PAGE)","HID Button 27:  Preset 3"));
		m_KeyRefTable.put(783,new KeyRef(783,"(0x001D + kbdm_BUTTON_PAGE)","HID Button 29:  Cruise Control -"));
		m_KeyRefTable.put(784,new KeyRef(784,"(0x001E + kbdm_BUTTON_PAGE)","HID Button 30:  Cruise Control +"));
		m_KeyRefTable.put(785,new KeyRef(785,"(0x0025 + kbdm_BUTTON_PAGE)","HID Button 37:  Zoom Long -"));
		m_KeyRefTable.put(786,new KeyRef(786,"(0x0026 + kbdm_BUTTON_PAGE)","HID Button 38:  Zoom Long +"));
		m_KeyRefTable.put(787,new KeyRef(787,"(0x0026 + kbdm_BUTTON_PAGE)","HID Button 38:  Zoom Long +"));
		m_KeyRefTable.put(788,new KeyRef(788,"(0x0025 + kbdm_BUTTON_PAGE)","HID Button 45:  Fn key"));
		m_KeyRefTable.put(789,new KeyRef(789,"(0x0030 + kbdm_BUTTON_PAGE)","HID Button 48:  VoIP Dialler"));
		m_KeyRefTable.put(790,new KeyRef(790,"(0x0031 + kbdm_BUTTON_PAGE)","HID Button 49:  VoIP Call"));
		m_KeyRefTable.put(791,new KeyRef(791,"(0x0032 + kbdm_BUTTON_PAGE)","HID Button 50:  VoIP HangUp / Reject Call"));
		m_KeyRefTable.put(792,new KeyRef(792,"(0x0033 + kbdm_BUTTON_PAGE)","HID Button 51:  VoIP AutoAnswer"));
		m_KeyRefTable.put(793,new KeyRef(793,"(0x0034 + kbdm_BUTTON_PAGE)","HID Button 52:  VoIP SpeedDial1"));
		m_KeyRefTable.put(794,new KeyRef(794,"(0x0035 + kbdm_BUTTON_PAGE)","HID Button 53:  VoIP SpeedDial2"));
		m_KeyRefTable.put(795,new KeyRef(795,"(0x0036 + kbdm_BUTTON_PAGE)","HID Button 54:  VoIP SpeedDial3"));
		m_KeyRefTable.put(796,new KeyRef(796,"(0x0037 + kbdm_BUTTON_PAGE)","HID Button 55:  VoIP SpeedDial4"));
		m_KeyRefTable.put(797,new KeyRef(797,"(0x0038 + kbdm_BUTTON_PAGE)","HID Button 56:  Mic Mute"));
		m_KeyRefTable.put(798,new KeyRef(798,"(0x0039 + kbdm_BUTTON_PAGE)","HID Button 57:  Timer Event"));
		m_KeyRefTable.put(799,new KeyRef(799,"(0x003A + kbdm_BUTTON_PAGE)","HID Button 58:  Timer Event End"));
		m_KeyRefTable.put(800,new KeyRef(800,"(0x003B + kbdm_BUTTON_PAGE)","HID Button 59:  Alarm Event"));
		m_KeyRefTable.put(801,new KeyRef(801,"(0x003C + kbdm_BUTTON_PAGE)","HID Button 60:  Alarm Event End"));
		m_KeyRefTable.put(802,new KeyRef(802,"(0x003E + kbdm_BUTTON_PAGE)","HID Button 62:  Exposé"));
		m_KeyRefTable.put(803,new KeyRef(803,"(0x0041 + kbdm_BUTTON_PAGE)","HID Button 65:  Fn+F1"));
		m_KeyRefTable.put(804,new KeyRef(804,"(0x0042 + kbdm_BUTTON_PAGE)","HID Button 66:  Fn+F2"));
		m_KeyRefTable.put(805,new KeyRef(805,"(0x0043 + kbdm_BUTTON_PAGE)","HID Button 67:  Fn+F3"));
		m_KeyRefTable.put(806,new KeyRef(806,"(0x0044 + kbdm_BUTTON_PAGE)","HID Button 68:  Fn+F4"));
		m_KeyRefTable.put(807,new KeyRef(807,"(0x0045 + kbdm_BUTTON_PAGE)","HID Button 69:  Fn+F5"));
		m_KeyRefTable.put(808,new KeyRef(808,"(0x0046 + kbdm_BUTTON_PAGE)","HID Button 70:  Fn+F6"));
		m_KeyRefTable.put(809,new KeyRef(809,"(0x0047 + kbdm_BUTTON_PAGE)","HID Button 71:  Fn+F7"));
		m_KeyRefTable.put(810,new KeyRef(810,"(0x0048 + kbdm_BUTTON_PAGE)","HID Button 72:  Fn+F8"));
		m_KeyRefTable.put(811,new KeyRef(811,"(0x0049 + kbdm_BUTTON_PAGE)","HID Button 73:  Fn+F9"));
		m_KeyRefTable.put(812,new KeyRef(812,"(0x004A + kbdm_BUTTON_PAGE)","HID Button 74:  Fn+F10"));
		m_KeyRefTable.put(813,new KeyRef(813,"(0x004B + kbdm_BUTTON_PAGE)","HID Button 75:  Fn+F11"));
		m_KeyRefTable.put(814,new KeyRef(814,"(0x004C + kbdm_BUTTON_PAGE)","HID Button 76:  Fn+F12"));
		m_KeyRefTable.put(815,new KeyRef(815,"(0x004D + kbdm_BUTTON_PAGE)","HID Button 77:  Fn+PrintScreen"));
		m_KeyRefTable.put(816,new KeyRef(816,"(0x004E + kbdm_BUTTON_PAGE)","HID Button 78:  Fn+InsertScroll"));
		m_KeyRefTable.put(817,new KeyRef(817,"(0x004F + kbdm_BUTTON_PAGE)","HID Button 79:  Fn+PauseBreak"));
		m_KeyRefTable.put(818,new KeyRef(818,"(0x0055 + kbdm_BUTTON_PAGE)","HID Button 85:  Backlight -"));
		m_KeyRefTable.put(819,new KeyRef(819,"(0x0056 + kbdm_BUTTON_PAGE)","HID Button 86:  Backlight +"));
		m_KeyRefTable.put(820,new KeyRef(820,"(0x0057 + kbdm_BUTTON_PAGE)","HID Button 87:  Backlight Auto"));
		m_KeyRefTable.put(821,new KeyRef(821,"(0x006C + kbdm_BUTTON_PAGE)","HID Button 108: NumPad /"));
		m_KeyRefTable.put(822,new KeyRef(822,"(0x006D + kbdm_BUTTON_PAGE)","HID Button 109: NumPad *"));
		m_KeyRefTable.put(823,new KeyRef(823,"(0x006E + kbdm_BUTTON_PAGE)","HID Button 110: NumPad -"));
		m_KeyRefTable.put(824,new KeyRef(824,"(0x006F + kbdm_BUTTON_PAGE)","HID Button 111: NumPad +"));
		m_KeyRefTable.put(825,new KeyRef(825,"(0x0070 + kbdm_BUTTON_PAGE)","HID Button 112: NumPad 0"));
		m_KeyRefTable.put(826,new KeyRef(826,"(0x0071 + kbdm_BUTTON_PAGE)","HID Button 113: NumPad 1"));
		m_KeyRefTable.put(827,new KeyRef(827,"(0x0072 + kbdm_BUTTON_PAGE)","HID Button 114: NumPad 2"));
		m_KeyRefTable.put(828,new KeyRef(828,"(0x0073 + kbdm_BUTTON_PAGE)","HID Button 115: NumPad 3"));
		m_KeyRefTable.put(829,new KeyRef(829,"(0x0074 + kbdm_BUTTON_PAGE)","HID Button 116: NumPad,new KeyRef(4"));
		m_KeyRefTable.put(830,new KeyRef(830,"(0x0075 + kbdm_BUTTON_PAGE)","HID Button 117: NumPad,new KeyRef(5"));
		m_KeyRefTable.put(831,new KeyRef(831,"(0x0076 + kbdm_BUTTON_PAGE)","HID Button 118: NumPad,new KeyRef(6"));
		m_KeyRefTable.put(832,new KeyRef(832,"(0x0077 + kbdm_BUTTON_PAGE)","HID Button 119: NumPad,new KeyRef(7"));
		m_KeyRefTable.put(833,new KeyRef(833,"(0x0078 + kbdm_BUTTON_PAGE)","HID Button 120: NumPad,new KeyRef(8"));
		m_KeyRefTable.put(834,new KeyRef(834,"(0x0079 + kbdm_BUTTON_PAGE)","HID Button 121: NumPad 9"));
		m_KeyRefTable.put(835,new KeyRef(835,"(0x007A + kbdm_BUTTON_PAGE)","HID Button 122: NumPad ."));
		m_KeyRefTable.put(836,new KeyRef(836,"(0x007B + kbdm_BUTTON_PAGE)","HID Button 123: Disable NumLock OSD"));
		
		m_KeyRefTable.put(900,new KeyRef(900,"(0x000D+ kbdm_MSMediaRemote_PAGE)","Green Start Button Navigation"));
		m_KeyRefTable.put(901,new KeyRef(901,"(0x002B+ kbdm_MSMediaRemote_PAGE)","Closed Captioning AV Control Power"));
		m_KeyRefTable.put(902,new KeyRef(902,"(0x005A+ kbdm_MSMediaRemote_PAGE)","TeleText On / Off TeleText "));
		m_KeyRefTable.put(903,new KeyRef(903,"(0x005B+ kbdm_MSMediaRemote_PAGE)","TeleText Red TeleText "));
		m_KeyRefTable.put(904,new KeyRef(904,"(0x005C+ kbdm_MSMediaRemote_PAGE)","TeleText Green TeleText "));
		m_KeyRefTable.put(905,new KeyRef(905,"(0x005D+ kbdm_MSMediaRemote_PAGE)","TeleText Yellow TeleText "));
		m_KeyRefTable.put(906,new KeyRef(906,"(0x005E+ kbdm_MSMediaRemote_PAGE)","TeleText Blue TeleText "));
		m_KeyRefTable.put(907,new KeyRef(907,"(0x0025+ kbdm_MSMediaRemote_PAGE)","Shortcuts Live TV Media Center "));
		m_KeyRefTable.put(908,new KeyRef(908,"(0x0047+ kbdm_MSMediaRemote_PAGE)","Shortcuts Music Media Center "));
		m_KeyRefTable.put(909,new KeyRef(909,"(0x0048+ kbdm_MSMediaRemote_PAGE)","Shortcuts Recorded TV Media Center "));
		m_KeyRefTable.put(910,new KeyRef(910,"(0x0049+ kbdm_MSMediaRemote_PAGE)","Shortcuts Pictures Media Center "));
		m_KeyRefTable.put(911,new KeyRef(911,"(0x004A+ kbdm_MSMediaRemote_PAGE)","Shortcuts Videos Media Center "));
		m_KeyRefTable.put(912,new KeyRef(912,"(0x0050+ kbdm_MSMediaRemote_PAGE)","Shortcuts FM Radio Media Center "));
		m_KeyRefTable.put(913,new KeyRef(913,"(0x003C+ kbdm_MSMediaRemote_PAGE)","Shortcuts OnSpot Media Center "));
		m_KeyRefTable.put(914,new KeyRef(914,"(0x003D+ kbdm_MSMediaRemote_PAGE)","Shortcuts OnSpot App Media Center "));
		m_KeyRefTable.put(915,new KeyRef(915,"(0x0024+ kbdm_MSMediaRemote_PAGE)","DVD Menu DVD"));
		m_KeyRefTable.put(916,new KeyRef(916,"(0x004B+ kbdm_MSMediaRemote_PAGE)","DVD Angle DVD"));
		m_KeyRefTable.put(917,new KeyRef(917,"(0x004C+ kbdm_MSMediaRemote_PAGE)","DVD Audio DVD"));
		m_KeyRefTable.put(918,new KeyRef(918,"(0x004D+ kbdm_MSMediaRemote_PAGE)","DVD Subtitle DVD"));
		m_KeyRefTable.put(919,new KeyRef(919,"(0x0028+ kbdm_MSMediaRemote_PAGE)","Eject DVD"));
		m_KeyRefTable.put(920,new KeyRef(920,"(0x0043+ kbdm_MSMediaRemote_PAGE)","DVD Top Menu DVD"));
		m_KeyRefTable.put(921,new KeyRef(921,"(0x0032+ kbdm_MSMediaRemote_PAGE)","Ext0 Extensibility"));
		m_KeyRefTable.put(922,new KeyRef(922,"(0x0033+ kbdm_MSMediaRemote_PAGE)","Ext1 Extensibility"));
		m_KeyRefTable.put(923,new KeyRef(923,"(0x0034+ kbdm_MSMediaRemote_PAGE)","Ext2 Extensibility"));
		m_KeyRefTable.put(924,new KeyRef(924,"(0x0035+ kbdm_MSMediaRemote_PAGE)","Ext3 Extensibility"));
		m_KeyRefTable.put(925,new KeyRef(925,"(0x0036+ kbdm_MSMediaRemote_PAGE)","Ext4 Extensibility"));
		m_KeyRefTable.put(926,new KeyRef(926,"(0x0037+ kbdm_MSMediaRemote_PAGE)","Ext5 Extensibility"));
		m_KeyRefTable.put(927,new KeyRef(927,"(0x0038+ kbdm_MSMediaRemote_PAGE)","Ext6 Extensibility"));
		m_KeyRefTable.put(928,new KeyRef(928,"(0x0039+ kbdm_MSMediaRemote_PAGE)","Ext7 Extensibility"));
		m_KeyRefTable.put(929,new KeyRef(929,"(0x003A+ kbdm_MSMediaRemote_PAGE)","Ext8 Extensibility"));
		m_KeyRefTable.put(930,new KeyRef(930,"(0x0080+ kbdm_MSMediaRemote_PAGE)","Ext9 Extensibility"));
		m_KeyRefTable.put(931,new KeyRef(931,"(0x0081+ kbdm_MSMediaRemote_PAGE)","Ext10 Extensibility"));
		m_KeyRefTable.put(932,new KeyRef(932,"(0x006F+ kbdm_MSMediaRemote_PAGE)","Ext11 Extensibility"));
		m_KeyRefTable.put(933,new KeyRef(933,"(0x0027+ kbdm_MSMediaRemote_PAGE)","Zoom Other "));
		
		m_KeyRefTable.put(1101,new KeyRef(1101,"(0x0101 + kbdm_BUTTON_PAGE)","HID Button 257: AVR power on/off"));
		m_KeyRefTable.put(1102,new KeyRef(1102,"(0x0102 + kbdm_BUTTON_PAGE)","HID Button 258: STB power on/off"));
		m_KeyRefTable.put(1103,new KeyRef(1103,"(0x0103 + kbdm_BUTTON_PAGE)","HID Button 259: TV power on/off"));
		m_KeyRefTable.put(1104,new KeyRef(1104,"(0x0104 + kbdm_BUTTON_PAGE)","HID Button 260: Menu Dialog"));
		m_KeyRefTable.put(1105,new KeyRef(1105,"(0x0105 + kbdm_BUTTON_PAGE)","HID Button 261: Info Dialog"));
		m_KeyRefTable.put(1106,new KeyRef(1106,"(0x0106 + kbdm_BUTTON_PAGE)","HID Button 262: List Dialog"));
		m_KeyRefTable.put(1107,new KeyRef(1107,"(0x0107 + kbdm_BUTTON_PAGE)","HID Button 263: User Dialog"));
		
		
		WHQL_ExceptLst.add(new ArrayList<Integer>(Arrays.asList(44,58,30)));//"L-Shift "," L-Ctrl ","CapsLck ", 
		WHQL_ExceptLst.add(new ArrayList<Integer>(Arrays.asList(44,58,126)));//"L-Shift "," L-Ctrl ","PauseBrk", 
		WHQL_ExceptLst.add(new ArrayList<Integer>(Arrays.asList(57,64,30)));//"R-Shift "," R-Ctrl ","CapsLck ", 
		WHQL_ExceptLst.add(new ArrayList<Integer>(Arrays.asList(57,64,126)));//"R-Shift "," R-Ctrl ","PauseBrk", 
		WHQL_ExceptLst.add(new ArrayList<Integer>(Arrays.asList(58,63,30)));//" L-Ctrl "," R-GUI  ","CapsLck ", 
		WHQL_ExceptLst.add(new ArrayList<Integer>(Arrays.asList(64,63,30)));//" R-Ctrl "," R-GUI  ","CapsLck ", 
		WHQL_ExceptLst.add(new ArrayList<Integer>(Arrays.asList(60,59,107)));//" L-Alt  "," L-GUI  ","  KP ,  ", 
		WHQL_ExceptLst.add(new ArrayList<Integer>(Arrays.asList(60,63,56)));//" L-Alt  "," R-GUI  ","   Ro   ", 
		WHQL_ExceptLst.add(new ArrayList<Integer>(Arrays.asList(62,63,42)));//" R-Alt  "," R-GUI  "," $   £  ", 
		WHQL_ExceptLst.add(new ArrayList<Integer>(Arrays.asList(59,63,83)));//" L-GUI  "," R-GUI  ","UpArrow "));
		
		generateInvTable();
	}
	
	static final int i_AppCode = 131;
	static final int i_MuhenkanCode = 132;
	static final int i_HenkanCode = 133;
	static final int i_KataHiraCode = 134;
	static final int i_HanjaCode = 135;
	static final int i_HangEngCode = 136;
	static final int i_KP_EqualCode = 137;
	static final int i_StdKeyLim = 140;
	static final int i_Btn1Code = i_StdKeyLim + 1;
	static final int i_Btn2Code = i_StdKeyLim + 2;
	static final int i_Btn3Code = i_StdKeyLim + 3;
	static final int i_EmptyCode = 150;
	static final int i_LeftFnCode = 151;
	static final int i_RightFnCode = 152;
	static final int i_EjectCode = 485;
	static final int i_WHQL_Limit = 127;
	static final int i_NonStdCode = 154;
	static final int i_Empty = 0;
	static final int i_LShift = 44;
	static final int i_RShift = 57;
	static final int i_LCtrl = 58;
	static final int i_RCtrl = 64;
	static final int i_LAlt = 60;
	static final int i_RAlt = 62;
	static final int i_LGUI = 59;
	static final int i_RGUI = 63;
	
	static String s_error = new String(" Error ");
	
	static String s_weirdCodes = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ"+i_NonStdCode +"App "+i_AppCode+" Muh "+
		i_MuhenkanCode+" Hen "+i_HenkanCode+" K/H katahira"+i_KataHiraCode+
		" Han KRHanja "+i_HanjaCode+" H/E "+i_HangEngCode+" KP= "+i_KP_EqualCode+
		" Btn1 "+i_Btn1Code+" Btn2 "+i_Btn2Code+" Btn3 "+i_Btn3Code+
		" Fn LFn "+i_LeftFnCode+" RFn "+i_RightFnCode+" Ejct "+i_EjectCode+ 
		" LeftArrow Left Arrow "+79+" RightArrow Right Arrow "+89+
		" UpArrow Up Arrow "+83+" DownArrow DnArrow Down Arrow "+84+
		" Home "+80+" End "+81+" Enter "+43+" PageUp Page Up "+85+" PageDn PageDown Page Down "+86+
		" space "+61+" capslock capslck "+30+" numlock numlck "+90+" tab "+16+" esc "+110+
		" insert "+75+" delete "+76+" backspace Bkspace "+15+
		" LShift L-Shift "+44+" LCtrl L-Ctrl "+58+" LAlt L-Alt "+60+" LGUI L-GUI "+59+
		" RShift R-Shift "+57+" RCtrl R-Ctrl "+64+" RAlt R-Alt "+62+" RGUI R-GUI "+63+
		" ").toLowerCase(); // Fn is LFn
		
		//	the rules for Braille can be summarized as follows: any combination of the Space, D, F, J, K, L, and S must not generate a ghost key.
		static String s_Braille = new String(" SDFJKL");
		
		//	Ghost-key rules for Guitar-Hero: The default setup uses the V, C, X, Z and left shift keys as the green, red, yellow, blue, and orange frets, respectively.
		static ArrayList<Integer> l_GuitarHero = new ArrayList<Integer>(Arrays.asList(44,46,47,48,49));
		
		//	Ghost-key rules for CounterStrike: The default setup uses the A, D, W, S, Ctrl/Space+R, Ctrl/Space+R, Ctrl/Space+E, Ctrl/Space+Q, 
		//                              Ctrl/Space+1, Ctrl/Space+2, Ctrl/Space+3, .
		static ArrayList<Integer> l_CounterStrikeADSW = new ArrayList<Integer>(Arrays.asList(31,32,33,18));
		static ArrayList<Integer> l_CounterStrikeMod = new ArrayList<Integer>(Arrays.asList(58,61,64));
		static ArrayList<Integer> l_CounterStrikeOther = new ArrayList<Integer>(Arrays.asList(17,19,20,2,3,4));
		
		static ArrayList<Integer> l_Game1Code = new ArrayList<Integer>(Arrays.asList(18,19,31,32,33,34));
		static ArrayList<Integer> l_Game2Code = new ArrayList<Integer>(Arrays.asList(79,83,84,89));
		static ArrayList<Integer> l_Game3Code = new ArrayList<Integer>(Arrays.asList(92,93,96,97,98,102,103));
		static ArrayList<Integer> l_Game4Code = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,15,16,17,20,21,22,28,
			29,30,35,36,42,43,44, 45,46,47,48,49,50,57,58,59,60,61,64,75,76,80,81,85,86,90,
			91,95,99,100,101,104,105,106,108,i_AppCode));
		
		
		
		
		
}
