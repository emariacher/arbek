import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class KeyMapping {
	List<KeyId> l_keyid = new ArrayList<KeyId>();

	public KeyMapping() throws Exception {
		/*KeyId KeyMapping::m_keyboardKeys[] =
		{
		l_keyid.add(new KeyId("unknown"  	      , 0x00, 0x00, 0x00 ));
		l_keyid.add(new KeyId("paste"           , 0x00, 0x00, 0x7D )); 
		l_keyid.add(new KeyId("find"            , 0x00, 0x00, 0x7E )); 
		l_keyid.add(new KeyId("misc1"           , 0x00, 0x00, 0x32 ));  // Non-US # and ~
		l_keyid.add(new KeyId("misc2"           , 0x00, 0x00, 0x64 ));  // keypad Non-US \ and |
		l_keyid.add(new KeyId("miscMenu"        , 0x00, 0x00, 0x76 )); 
		l_keyid.add(new KeyId("again"           , 0x00, 0x00, 0x79 ));
		l_keyid.add(new KeyId("copy"            , 0x00, 0x00, 0x7C )); 
		l_keyid.add(new KeyId("cut"             , 0x00, 0x00, 0x7B )); */
		l_keyid.add(new KeyId("numpadEquals"    , 0x00, 0x8D, 0x67 ));  // nec
		l_keyid.add(new KeyId("undo"            , 0x00, 0x93, 0x7A ));
		l_keyid.add(new KeyId("numpadEnter"     , 0x00, 0x9C, 0x58 ));  
		l_keyid.add(new KeyId("numpadComma"     , 0x00, 0xB3, 0x85 ));  
		l_keyid.add(new KeyId("backspace"  	    , 0x08, 0x0E, 0x2A )); // backspace
		l_keyid.add(new KeyId("tab"       	    , 0x09, 0x0F, 0x2B ));
		l_keyid.add(new KeyId("clear"    	      , 0x0C, 0x00, 0x00 )); 
		l_keyid.add(new KeyId("enter"    	      , 0x0D, 0x1C, 0x28 )); // main return
		l_keyid.add(new KeyId("lShift"          , 0x10, 0x2A, 0xE1 ));
		l_keyid.add(new KeyId("lControl"        , 0x11, 0x1D, 0xE0 ));
		l_keyid.add(new KeyId("lAlt"            , 0x12, 0x38, 0xE2 ));
		l_keyid.add(new KeyId("pause"     	    , 0x13, 0xC5, 0x48 ));
		l_keyid.add(new KeyId("capsLock"   	    , 0x14, 0x3A, 0x39 )); // caps lock
		l_keyid.add(new KeyId("escape"    	    , 0x1B, 0x01, 0x29 ));
		l_keyid.add(new KeyId("space"     	    , 0x20, 0x39, 0x2C ));
		l_keyid.add(new KeyId("pageUp"     	    , 0x21, 0xC9, 0x4B )); // prior
		l_keyid.add(new KeyId("pageDown"        , 0x22, 0xD1, 0x4E ));  // next
		l_keyid.add(new KeyId("end"       	    , 0x23, 0xCF, 0x4D ));
		l_keyid.add(new KeyId("home"      	    , 0x24, 0xC7, 0x4A ));
		l_keyid.add(new KeyId("left"      	    , 0x25, 0xCB, 0x50 ));
		l_keyid.add(new KeyId("up"        	    , 0x26, 0xC8, 0x52 ));
		l_keyid.add(new KeyId("right"     	    , 0x27, 0xCD, 0x4F ));
		l_keyid.add(new KeyId("down"      	    , 0x28, 0xD0, 0x51 ));
		l_keyid.add(new KeyId("select"    	    , 0x29, 0x00, 0x77 ));
		l_keyid.add(new KeyId("print"           , 0x2A, 0xB7, 0x46 )); // print / sysreq
		l_keyid.add(new KeyId("execute"   	    , 0x2B, 0x00, 0x74 ));
		l_keyid.add(new KeyId("printScreen"     , 0x2C, 0x00, 0x00 )); // printscreen
		l_keyid.add(new KeyId("insert"    	    , 0x2D, 0xD2, 0x49 ));
		l_keyid.add(new KeyId("delete"    	    , 0x2E, 0xD3, 0x4C ));
		l_keyid.add(new KeyId("help"      	    , 0x2F, 0x00, 0x75 ));
		l_keyid.add(new KeyId("0"         	    , 0x30, 0x0B, 0x27 ));
		l_keyid.add(new KeyId("1"         	    , 0x31, 0x02, 0x1E ));
		l_keyid.add(new KeyId("2"         	    , 0x32, 0x03, 0x1F ));
		l_keyid.add(new KeyId("3"         	    , 0x33, 0x04, 0x20 ));
		l_keyid.add(new KeyId("4"         	    , 0x34, 0x05, 0x21 ));
		l_keyid.add(new KeyId("5"         	    , 0x35, 0x06, 0x22 ));
		l_keyid.add(new KeyId("6"         	    , 0x36, 0x07, 0x23 ));
		l_keyid.add(new KeyId("7"         	    , 0x37, 0x08, 0x24 ));
		l_keyid.add(new KeyId("8"         	    , 0x38, 0x09, 0x25 ));
		l_keyid.add(new KeyId("9"         	    , 0x39, 0x0A, 0x26 ));
		l_keyid.add(new KeyId("a"         	    , 0x41, 0x1E, 0x04 ));
		l_keyid.add(new KeyId("b"         	    , 0x42, 0x30, 0x05 ));
		l_keyid.add(new KeyId("c"         	    , 0x43, 0x2E, 0x06 ));
		l_keyid.add(new KeyId("d"         	    , 0x44, 0x20, 0x07 ));
		l_keyid.add(new KeyId("e"         	    , 0x45, 0x12, 0x08 ));
		l_keyid.add(new KeyId("f"         	    , 0x46, 0x21, 0x09 ));
		l_keyid.add(new KeyId("g"         	    , 0x47, 0x22, 0x0A ));
		l_keyid.add(new KeyId("h"         	    , 0x48, 0x23, 0x0B ));
		l_keyid.add(new KeyId("i"         	    , 0x49, 0x17, 0x0C ));
		l_keyid.add(new KeyId("j"         	    , 0x4A, 0x24, 0x0D ));
		l_keyid.add(new KeyId("k"         	    , 0x4B, 0x25, 0x0E ));
		l_keyid.add(new KeyId("l"         	    , 0x4C, 0x26, 0x0F ));
		l_keyid.add(new KeyId("m"         	    , 0x4D, 0x32, 0x10 ));
		l_keyid.add(new KeyId("n"         	    , 0x4E, 0x31, 0x11 ));
		l_keyid.add(new KeyId("o"         	    , 0x4F, 0x18, 0x12 ));
		l_keyid.add(new KeyId("p"         	    , 0x50, 0x19, 0x13 ));
		l_keyid.add(new KeyId("q"         	    , 0x51, 0x10, 0x14 ));
		l_keyid.add(new KeyId("r"         	    , 0x52, 0x13, 0x15 ));
		l_keyid.add(new KeyId("s"         	    , 0x53, 0x1F, 0x16 ));
		l_keyid.add(new KeyId("t"         	    , 0x54, 0x14, 0x17 ));
		l_keyid.add(new KeyId("u"         	    , 0x55, 0x16, 0x18 ));
		l_keyid.add(new KeyId("v"         	    , 0x56, 0x2F, 0x19 ));
		l_keyid.add(new KeyId("w"         	    , 0x57, 0x11, 0x1A ));
		l_keyid.add(new KeyId("x"         	    , 0x58, 0x2D, 0x1B ));
		l_keyid.add(new KeyId("y"         	    , 0x59, 0x15, 0x1C ));
		l_keyid.add(new KeyId("z"         	    , 0x5A, 0x2C, 0x1D ));
		l_keyid.add(new KeyId("lWin"      	    , 0x5B, 0xDB, 0xE3 ));
		l_keyid.add(new KeyId("rWin"  	        , 0x5C, 0xDC, 0xE7 ));
		l_keyid.add(new KeyId("apps"      	    , 0x5D, 0xDD, 0x65 )); // compose
		l_keyid.add(new KeyId("power"     	    , 0x5E, 0xDE, 0x66 ));
		l_keyid.add(new KeyId("sleep"     	    , 0x5F, 0x00, 0x00 ));
		l_keyid.add(new KeyId("numpad0"   	    , 0x60, 0x52, 0x62 ));
		l_keyid.add(new KeyId("numpad1"   	    , 0x61, 0x4F, 0x59 ));
		l_keyid.add(new KeyId("numpad2"   	    , 0x62, 0x50, 0x5A ));
		l_keyid.add(new KeyId("numpad3"   	    , 0x63, 0x51, 0x5B ));
		l_keyid.add(new KeyId("numpad4"   	    , 0x64, 0x4B, 0x5C ));
		l_keyid.add(new KeyId("numpad5"   	    , 0x65, 0x4C, 0x5D ));
		l_keyid.add(new KeyId("numpad6"   	    , 0x66, 0x4D, 0x5E ));
		l_keyid.add(new KeyId("numpad7"   	    , 0x67, 0x47, 0x5F ));
		l_keyid.add(new KeyId("numpad8"   	    , 0x68, 0x48, 0x60 ));
		l_keyid.add(new KeyId("numpad9"   	    , 0x69, 0x49, 0x61 ));
		l_keyid.add(new KeyId("numpadMultiply"  , 0x6A, 0x37, 0x55 )); // keypad *
		l_keyid.add(new KeyId("numpadAdd"       , 0x6B, 0x4E, 0x57 )); // keypad +
		l_keyid.add(new KeyId("separator"       , 0x6C, 0x00, 0x00 )); 
		l_keyid.add(new KeyId("numpadSubtract"  , 0x6D, 0x4A, 0x56 )); // keypad - 
		l_keyid.add(new KeyId("numpadDecimal"   , 0x6E, 0x53, 0x63 )); // keypad .
		l_keyid.add(new KeyId("numpadDivide"    , 0x6F, 0xB5, 0x54 )); // keypad /
		l_keyid.add(new KeyId("f1"        	    , 0x70, 0x3B, 0x3A ));
		l_keyid.add(new KeyId("f2"        	    , 0x71, 0x3C, 0x3B ));
		l_keyid.add(new KeyId("f3"        	    , 0x72, 0x3D, 0x3C ));
		l_keyid.add(new KeyId("f4"        	    , 0x73, 0x3E, 0x3D ));
		l_keyid.add(new KeyId("f5"        	    , 0x74, 0x3F, 0x3E ));
		l_keyid.add(new KeyId("f6"        	    , 0x75, 0x40, 0x3F ));
		l_keyid.add(new KeyId("f7"        	    , 0x76, 0x41, 0x40 ));
		l_keyid.add(new KeyId("f8"        	    , 0x77, 0x42, 0x41 ));
		l_keyid.add(new KeyId("f9"        	    , 0x78, 0x43, 0x42 ));
		l_keyid.add(new KeyId("f10"       	    , 0x79, 0x44, 0x43 ));
		l_keyid.add(new KeyId("f11"       	    , 0x7A, 0x57, 0x44 ));
		l_keyid.add(new KeyId("f12"       	    , 0x7B, 0x58, 0x45 ));
		l_keyid.add(new KeyId("f13"       	    , 0x7C, 0x64, 0x68 ));
		l_keyid.add(new KeyId("f14"       	    , 0x7D, 0x65, 0x69 ));
		l_keyid.add(new KeyId("f15"       	    , 0x7E, 0x66, 0x6A ));
		l_keyid.add(new KeyId("f16"       	    , 0x7F, 0x00, 0x6B ));
		l_keyid.add(new KeyId("f17"       	    , 0x80, 0x00, 0x6C ));
		l_keyid.add(new KeyId("f18"       	    , 0x81, 0x00, 0x6D ));
		l_keyid.add(new KeyId("f19"       	    , 0x82, 0x00, 0x6E ));
		l_keyid.add(new KeyId("f20"       	    , 0x83, 0x00, 0x6F ));
		l_keyid.add(new KeyId("f21"       	    , 0x84, 0x00, 0x70 ));
		l_keyid.add(new KeyId("f22"       	    , 0x85, 0x00, 0x71 ));
		l_keyid.add(new KeyId("f23"       	    , 0x86, 0x00, 0x72 ));
		l_keyid.add(new KeyId("f24"       	    , 0x87, 0x00, 0x73 ));
		l_keyid.add(new KeyId("numLock"   	    , 0x90, 0x45, 0x53 ));
		l_keyid.add(new KeyId("scrollLock" 	    , 0x91, 0x46, 0x47 ));  // scroll lock
		l_keyid.add(new KeyId("lShift"          , 0xA0, 0x2A, 0xE1 ));
		l_keyid.add(new KeyId("rShift"    	    , 0xA1, 0x36, 0xE5 ));
		l_keyid.add(new KeyId("lControl"        , 0xA2, 0x1D, 0xE0 ));
		l_keyid.add(new KeyId("rControl"  	    , 0xA3, 0x9D, 0xE4 ));
		l_keyid.add(new KeyId("lAlt"            , 0xA4, 0x38, 0xE2 ));
		l_keyid.add(new KeyId("rAlt"        	  , 0xA5, 0xB8, 0xE6 ));
		l_keyid.add(new KeyId("browserBack"     , 0xA6, 0x00, 0x00 ));
		l_keyid.add(new KeyId("browserForward"  , 0xA7, 0x00, 0x00 ));
		l_keyid.add(new KeyId("browserRefresh"  , 0xA8, 0x00, 0x00 ));
		l_keyid.add(new KeyId("browserStop"     , 0xA9, 0x00, 0x00 ));
		l_keyid.add(new KeyId("browserSearch"   , 0xAA, 0x00, 0x00 )); 
		l_keyid.add(new KeyId("browserFavorites", 0xAB, 0x00, 0x00 )); 
		l_keyid.add(new KeyId("browserHome"     , 0xAC, 0x00, 0x00 )); 
		l_keyid.add(new KeyId("mute"  	        , 0xAD, 0xA0, 0x7F )); 
		l_keyid.add(new KeyId("volumeDown"      , 0xAE, 0xAE, 0x81 ));
		l_keyid.add(new KeyId("volumeUp"        , 0xAF, 0xB0, 0x80 ));
		l_keyid.add(new KeyId("nextTrack"       , 0xB0, 0x00, 0xB5 )); 
		l_keyid.add(new KeyId("prevTrack"       , 0xB1, 0x00, 0xB6 )); 
		l_keyid.add(new KeyId("stopMedia"  	    , 0xB2, 0x95, 0x78 ));
		l_keyid.add(new KeyId("playMedia"	      , 0xB3, 0x00, 0xB0 ));
		l_keyid.add(new KeyId("mail"      	    , 0xB4, 0x00, 0xFF ));
		l_keyid.add(new KeyId("media"      	    , 0xB5, 0x00, 0x87 ));
		l_keyid.add(new KeyId("launch1"    	    , 0xB6, 0x00, 0x00 ));
		l_keyid.add(new KeyId("launch2"    	    , 0xB7, 0x00, 0x00 ));
		l_keyid.add(new KeyId("semicolon"  	    , 0xBA, 0x27, 0x33 )); //this key is removed from here since it depends on the layout
		l_keyid.add(new KeyId("equals"     	    , 0xBB, 0x0D, 0x2E ));
		l_keyid.add(new KeyId("comma"     	    , 0xBC, 0x33, 0x36 ));
		l_keyid.add(new KeyId("minus"  	        , 0xBD, 0x0C, 0x2D ));
		l_keyid.add(new KeyId("period"     	    , 0xBE, 0x34, 0x37 )); 
		l_keyid.add(new KeyId("slash"     	    , 0xBF, 0x35, 0x38 )); //this key is removed from here since it depends on the layout
		l_keyid.add(new KeyId("grave"     	    , 0xC0, 0x29, 0x35 )); //this key is removed from here since it depends on the layout
		l_keyid.add(new KeyId("lbracket"   	    , 0xDB, 0x1A, 0x2F )); //this key is removed from here since it depends on the layout
		l_keyid.add(new KeyId("backslash"  	    , 0xDC, 0x2B, 0x31 )); //this key is removed from here since it depends on the layout
		l_keyid.add(new KeyId("rbracket"   	    , 0xDD, 0x1B, 0x30 )); //this key is removed from here since it depends on the layout
		l_keyid.add(new KeyId("apostrophe"      , 0xDE, 0x28, 0x34 )); //this key is removed from here since it depends on the layout
		l_keyid.add(new KeyId("letter"   	      , 0xFF, 0x00, 0xFFFF ));
		/*};

		KeyId KeyMapping::m_specialKeys[] =
		{*/
		l_keyid.add(new KeyId("rShift"           , 0x10, 0x36, 0x00, false));
		l_keyid.add(new KeyId("rControl"         , 0x11, 0x00, 0x00, true ));
		l_keyid.add(new KeyId("rAlt"             , 0x12, 0x00, 0x00, true ));
		l_keyid.add(new KeyId("numpadEnter"      , 0x0D, 0x00, 0x00, true ));
		l_keyid.add(new KeyId("numpadPageUp"     , 0x21, 0x00, 0x00, false));
		l_keyid.add(new KeyId("numpadPageDown"   , 0x22, 0x00, 0x00, false));
		l_keyid.add(new KeyId("numpadEnd"        , 0x23, 0x00, 0x00, false));
		l_keyid.add(new KeyId("numpadHome"       , 0x24, 0x00, 0x00, false));
		l_keyid.add(new KeyId("numpadLeft"       , 0x25, 0x00, 0x00, false));
		l_keyid.add(new KeyId("numpadUp"         , 0x26, 0x00, 0x00, false));
		l_keyid.add(new KeyId("numpadRight"      , 0x27, 0x00, 0x00, false));
		l_keyid.add(new KeyId("numpadDown"       , 0x28, 0x00, 0x00, false));
		l_keyid.add(new KeyId("numpadInsert"     , 0x2D, 0x00, 0x00, false));
		l_keyid.add(new KeyId("numpadDelete"     , 0x2E, 0x00, 0x00, false));

		/*};

		IncorrectKey KeyMapping::m_incorrectKeys[] =
		{*/
		l_keyid.add(new KeyId(0x13, 0x1D)); //should be pause
		l_keyid.add(new KeyId(0x5F, 0x5F)); //should be Sleep
		l_keyid.add(new KeyId(0x90, 0x45)); //should be NumLock
		l_keyid.add(new KeyId(0xA6, 0x6A)); //should be Back
		l_keyid.add(new KeyId(0xA7, 0x69)); //should be Forward
		l_keyid.add(new KeyId(0xAA, 0x65)); //should be Search
		l_keyid.add(new KeyId(0xAB, 0x66)); //should be Favorites
		l_keyid.add(new KeyId(0xAC, 0x32)); //should be Home
		l_keyid.add(new KeyId(0xAD, 0x20)); //should be Mute
		l_keyid.add(new KeyId(0xAE, 0x2E)); //should be Volume down
		l_keyid.add(new KeyId(0xAF, 0x30)); //should be Volume up
		l_keyid.add(new KeyId(0xB0, 0x19)); //should be Next
		l_keyid.add(new KeyId(0xB1, 0x10)); //should be Prev
		l_keyid.add(new KeyId(0xB2, 0x24)); //should be Stop
		l_keyid.add(new KeyId(0xB3, 0x22)); //should be Play
		l_keyid.add(new KeyId(0xB4, 0x6C)); //should be Mail
		l_keyid.add(new KeyId(0xB5, 0x6D)); //should be Media
		/*};

		KeyId KeyMapping::m_consumerKeys[] =
		{
		l_keyid.add(new KeyId("help"             , 0x00, 0x00,  0x0095));//new
		l_keyid.add(new KeyId("nextTrack"        , 0x00, 0x00,  0x00B5));
		l_keyid.add(new KeyId("prevTrack"        , 0x00, 0x00,  0x00B6));
		l_keyid.add(new KeyId("stop"             , 0x00, 0x00,  0x00B7));
		l_keyid.add(new KeyId("playPause"        , 0x00, 0x00,  0x00CD));
		l_keyid.add(new KeyId("mute"             , 0x00, 0x00,  0x00E2));
		l_keyid.add(new KeyId("volumeUp"         , 0x00, 0x00,  0x00E9));
		l_keyid.add(new KeyId("volumeDown"       , 0x00, 0x00,  0x00EA));
		l_keyid.add(new KeyId("media"            , 0x00, 0x00,  0x0183));//AL Configuration
		l_keyid.add(new KeyId("word"             , 0x00, 0x00,  0x0184));
		l_keyid.add(new KeyId("excel"            , 0x00, 0x00,  0x0186));
		l_keyid.add(new KeyId("powerPoint"       , 0x00, 0x00,  0x0188));
		l_keyid.add(new KeyId("mail"             , 0x00, 0x00,  0x018A));
		l_keyid.add(new KeyId("calendar"         , 0x00, 0x00,  0x018E));
		l_keyid.add(new KeyId("calculator"       , 0x00, 0x00,  0x0192));
		l_keyid.add(new KeyId("myComputer"       , 0x00, 0x00,  0x0194));
		l_keyid.add(new KeyId("selectTaskApp"    , 0x00, 0x00,  0x01A2));
		l_keyid.add(new KeyId("messenger"        , 0x00, 0x00,  0x01BC));
		l_keyid.add(new KeyId("save"             , 0x00, 0x00,  0x0207));
		l_keyid.add(new KeyId("print"            , 0x00, 0x00,  0x0208));
		l_keyid.add(new KeyId("undo"             , 0x00, 0x00,  0x021A));
		l_keyid.add(new KeyId("Search"           , 0x00, 0x00,  0x0221));
		l_keyid.add(new KeyId("browserHome"      , 0x00, 0x00,  0x0223));
		l_keyid.add(new KeyId("browserBack"      , 0x00, 0x00,  0x0224));
		l_keyid.add(new KeyId("browserForward"   , 0x00, 0x00,  0x0225));
		l_keyid.add(new KeyId("browserStop"      , 0x00, 0x00,  0x0226));
		l_keyid.add(new KeyId("browserRefresh"   , 0x00, 0x00,  0x0227));
		l_keyid.add(new KeyId("browserFavorites" , 0x00, 0x00,  0x022A));
		l_keyid.add(new KeyId("zoomIn"           , 0x00, 0x00,  0x022D));
		l_keyid.add(new KeyId("zoomOut"          , 0x00, 0x00,  0x022E));
		l_keyid.add(new KeyId("zoom"             , 0x00, 0x00,  0x022F));
		l_keyid.add(new KeyId("redo"             , 0x00, 0x00,  0x0279));
		};

		Iterator<KeyId> i = l_keyid.iterator();
		while(i.hasNext()){
			KeyId ki = (KeyId) i.next();
			if(!ki.valid()){
				l_keyid.remove(ki);
			}
		}*/
		l_keyid.add(new KeyId()); // invalid KeyId
		Collections.sort(l_keyid);
	}

	public KeyId find(KeyId ki) {
		int index=l_keyid.indexOf(ki);
		if(index<0) {
			return l_keyid.get(l_keyid.indexOf(new KeyId()));
		} else{ 
			return l_keyid.get(index);
		}
	}


}
