/**
***********************************************************************************************
 Copyright (C) 2007
 LOGITECH SA, CH-1122 ROMANEL/MORGES
 ---------------------------------------------------------------------------------------------
 \author    Didier Lanfranchi
 \file      kbdmap.c
 Date       December 2007
 Revision   
 
 \brief     Conversion tables RowColcode -> Scancode 
**********************************************************************************************/

// Notes:
//

// ============================================================================================
// Includes
// ============================================================================================

#include "kbd_map.h"
   
// ============================================================================================
// Constants
// ============================================================================================

// Key Matrix Definitions ---------------------------------

#define NB_ROW_12X12     12
#define NB_COL_12X12     12

#define IDX(i)            i

// --------------------------------------------------------
// ------ ALGOL KBD ---------------------------------------
   
const kbdm_keyMatrix_ts kbdm_ShortKey[] =
{
   {114    ,0             }, // Prev Track
   {115    ,0             }  // Next Track
};

const kbdm_keyMatrix_ts kbdm_LongKey[] =
{
   {145    ,0             }, // Rewind
   {146    ,0             }  // Fast Forward
};

const kbdm_keyMatrix_ts kbdm_DelayShort[] =
{
   {0      ,0             }  // NO_KEY
};

const kbdm_keyMatrix_ts kbdm_DelayLong[] =
{
   {158    ,0             }  // AC Eject
};

const kbdm_keyMatrix_ts kbdm_FnKeyOff[] =
{
   {0      ,0             }, // NO_KEY
   {2      ,0             }, // F1
   {3      ,0             }, // F2
   {4      ,0             }, // F3
   {5      ,0             }, // F4
   {6      ,0             }, // F5
   {7      ,0             }, // F6
   {8      ,0             }, // F7
   {9      ,0             }, // F8
   {10     ,0             }, // F9
   {11     ,0             }, // F10
   {12     ,0             }, // F11
   {13     ,0             }  // F12
};
	 
const kbdm_keyMatrix_ts kbdm_FnKeyOn[] =
{
   {0      ,0             }, // NO_KEY
   {153    ,0             }, // AC Home          [F1]
   {155    ,0             }, // AC Email         [F2]
   {156    ,0             }, // AC Search        [F3]
   {154    ,0             }, // AC Instant Mess  [F4]
   {134    ,0             }, // FLIP 3D          [F5]   
   {137    ,0             }, // Zoom (-)         [F6]
   {136    ,0             }, // Zoom (+)         [F7]
   {133    ,0             }, // Vista gadget     [F8]
   {131    ,0             }, // Music Player     [F9]      
   {132    ,0             }, // My Pictures      [F10]   
   {147    ,0             }, // AL Text Editor   [F11]
   {148    ,0             }  // AL SpreadSheet   [F12]
};
   
const kbdm_keyMatrix_ts kbdm_DualKeyOff[] =
{
   {14     ,0             }, // Print screen
   {16     ,0             }  // Pause | Break
};
	 
const kbdm_keyMatrix_ts kbdm_DualKeyOn[] =
{
   {112    ,0             }, // APP       [Print screen]
   {126    ,0             }  // Suspend   [Pause | Break]
};
    
static const kbdm_keyMatrix_ts kbdm_KeyMatrix[NB_ROW_12X12][NB_COL_12X12] =
{
  // --- ROW 1 --------------------------------------------
  {
    {103    ,0            }, // Euro 1: $ £
    {88     ,0            }, // [KP] End / 1
    {72     ,0            }, // [KP] Arrow Left / 4
    {87     ,0            }, // Arrow Up
    {101    ,0            }, // [KP] Del / .
    {70     ,0            }, // " / '
    {IDX(1) ,kbdm_FKEY    }, // F1  [AC Home]
    {15     ,             }, // Scroll Lock
	 {0      ,0            }, // NO_KEY
    {IDX(3) ,kbdm_FKEY    }, // F3  [AC search]
	 {0      ,0            }, // NO_KEY
	 {121    ,0            }  // Calculator
    
  },
  // --- ROW 2 --------------------------------------------
  {
  	 {24     ,kbdm_APPROVAL}, // & / 7
  	 {23     ,kbdm_APPROVAL}, // ^ / 6
    {51     ,0            }, // } / ]
    {69     ,0            }, // : / ;
    {IDX(4) ,kbdm_FKEY    }, // F4  [AC Instant Mess]
    {36     ,0            }, // [KP] /
    {0      ,0            }, // NO_KEY
    {0      ,0            }, // NO_KEY
    {IDX(2) ,kbdm_FKEY    }, // F2  [AC Email]
    {158    ,0            }, // AC Eject 
    {110    ,0            }, // Left Win
	 {0      ,0            }  // NO_KEY
  },
  // --- ROW 3 --------------------------------------------
  {
    {44     ,kbdm_APPROVAL}, // T
	 {IDX(0), kbdm_DUAL_KEY}, // Print Screen
    {41     ,kbdm_APPROVAL}, // W
    {60     ,kbdm_APPROVAL}, // A
    {76     ,kbdm_APPROVAL}, // Z
    {104    ,0            }, // Euro 2: < >
    {IDX(7) ,kbdm_FKEY    }, // F7  [Zoom (+)]
    {39     ,0            }, // TAB
    {53     ,0            }, // END
    {17     ,0            }, // ` / ~
    {0      ,0            }, // NO_KEY
    {IDX(11),kbdm_FKEY    }  // F11 [AL Text Editor]
  },
  // --- ROW 4 --------------------------------------------
  {
    {56     ,0            }, // [KP] Arrow Up / 8
    {68     ,kbdm_APPROVAL}, // L
    {67     ,kbdm_APPROVAL}, // K
    {99     ,0            }, // Right Arrow
    {57     ,0            }, // [KP] Page Up / 9
    {90     ,0            }, // [KP] Pg-Down / 3
    {IDX(12),kbdm_FKEY    }, // F12  [AL Spreadsheet]
    {130    ,0            }, // Korea: Hanguel
    {IDX(10),kbdm_FKEY    }, // F10  [My Pictures]
    {58     ,0            }, // [KP] +
    {35     ,0            }, // Num Lock
    {91     ,0            }  // [KP] Enter
  },  
  // --- ROW 5 --------------------------------------------
  {    
    {48     ,kbdm_APPROVAL}, // O
    {49     ,kbdm_APPROVAL}, // P
    {84     ,0            }, // > / .
    {47     ,kbdm_APPROVAL}, // I
    {26     ,kbdm_APPROVAL}, // ( / 9
    {25     ,kbdm_APPROVAL}, // * / 8
    {83     ,0            }, // < / ,
    {32     ,0            }, // Insert
    {30     ,0            }, // US Backslash
    {0      ,0            }, // NO_KEY
    {38     ,0            }, // [KP] -
    {100    ,0            }  // [KP] Ins / 0
  },
  // --- ROW 6 --------------------------------------------
  {
    {45     ,kbdm_APPROVAL}, // Y
    {81     ,kbdm_APPROVAL}, // N
    {80     ,kbdm_APPROVAL}, // B
    {IDX(5) ,kbdm_FKEY    }, // F5  [Flip 3D]
    {IDX(6) ,kbdm_FKEY    }, // F6  [Zoom (-)]
    {31     ,0            }, // Back Space
    {40     ,kbdm_APPROVAL}, // Q
    {129    ,0            }, // Korea: Hanja
    {74     ,0            }, // [KP] Arrow Right / 6
    {94     ,0            }, // Space Bar
    {0      ,0            }, // NO_KEY
    {73     ,0            }  // [KP] 5
  },
  // --- ROW 7 --------------------------------------------
  {
    {62     ,kbdm_APPROVAL}, // D
    {66     ,kbdm_APPROVAL}, // J
    {63     ,kbdm_APPROVAL}, // F
    {28     ,0            }, // - / _
    {98     ,0            }, // Arrow Down
    {97     ,0            }, // Arrow Left
    {0      ,0            }, // NO_KEY
    {0      ,0            }, // NO_KEY
    {29     ,0            }, // + / =
    {86     ,0            }, // Right Shift
    {0      ,0            }, // NO_KEY
    {55     ,0            }  // [KP] Home / 7
  },
  // --- ROW 8 --------------------------------------------
  {
    {64     ,kbdm_APPROVAL}, // G
    {42     ,kbdm_APPROVAL}, // E
    {46     ,kbdm_APPROVAL}, // U
    {82     ,kbdm_APPROVAL}, // M
    {105    ,0            }, // Japan: Yen
    {20     ,kbdm_APPROVAL}, // # / 3
    {95     ,0            }, // Right Alt
    {0      ,0            }, // NO_KEY
    {59     ,0            }, // Caps Lock
    {0      ,0            }, // NO_KEY
    {0      ,0            }, // NO_KEY
    {34     ,0            }  // Page Up
  },
  // --- ROW 9 --------------------------------------------
  {
    {50     ,0            }, // { / [
    {106    ,0            }, // Japan: Ro
    {27     ,kbdm_APPROVAL}, // ) / 0
    {85     ,0            }, // ? / /
    {19     ,kbdm_APPROVAL}, // @ / 2
    {162    ,kbdm_FN_KEY  }, // Fn
    {0      ,0            }, // NO_KEY
    {96     ,0            }, // Right Ctrl
    {108    ,0            }, // Japan: Henkan
    {0      ,0            }, // NO_KEY
    {0      ,0            }, // NO_KEY
    {107    ,0            }  // Japan: MuHenkan
  },  
  // --- ROW 10 -------------------------------------------
  {
    {43     ,kbdm_APPROVAL}, // R
    {65     ,kbdm_APPROVAL}, // H
    {61     ,kbdm_APPROVAL}, // S
    {21     ,kbdm_APPROVAL}, // $ / 4
    {IDX(8) ,kbdm_FKEY    }, // F8  [Vista Gadget]
    {18     ,kbdm_APPROVAL}, // ! / 1
    {0      ,0            }, // NO_KEY
    {0      ,0            }, // NO_KEY
    {0      ,0            }, // NO_KEY
    {75     ,0            }, // Left Shift
    {0      ,0            }, // NO_KEY
  	 {IDX(1) ,kbdm_DUAL_KEY}  // Pause Break
  },
  // --- ROW 11 -------------------------------------------
  {
  	 {89     ,0            }, // [KP] Arrow Down / 2
    {77     ,kbdm_APPROVAL}, // X
    {22     ,kbdm_APPROVAL}, // % / 5
    {79     ,kbdm_APPROVAL}, // V
    {109    ,0            }, // Japan: Katagana / Hiragana
    {37     ,0            }, // [KP] *
    {93     ,0            }, // Left Alt
    {0      ,0            }, // NO_KEY
    {0      ,0            }, // NO_KEY
    {119    ,0            }, // Volume (+)
    {118    ,0            }, // Volume (-)
    {120    ,0            }  // Mute
  },  
  // --- ROW 12 -------------------------------------------
  {  
    {78     ,kbdm_APPROVAL}, // C
    {52     ,0            }, // Delete
    {71     ,0            }, // Enter
    {54     ,0            }, // Page Down
    {IDX(9) ,kbdm_FKEY    }, // F9  [AC Email]
    {33     ,0            }, // Home
    {0      ,0            }, // NO_KEY
    {92     ,0            }, // Left Ctrl
    {1      ,0            }, // ESC
    {IDX(1) ,kbdm_SHOLO   }, // Next Track
    {117    ,0            }, // Play / Pause  
    {IDX(0) ,kbdm_SHOLO   }  // Prev Track
  }
};


// General key info versus HID code -----------------------
//
static const uint16_t kbdm_eQuadHidCode[] =
{
   // USAGE ID       // HID PAGE
   (0x0000         + kbdm_UNDEF_PAGE   ), // 0x00 // 0    NO_KEY
   (0x0029         + kbdm_KBD_PAGE     ), // 0x01 // 1    Esc
   (0x003A         + kbdm_KBD_PAGE     ), // 0x02 // 2    F1
   (0x003B         + kbdm_KBD_PAGE     ), // 0x03 // 3    F2
   (0x003C         + kbdm_KBD_PAGE     ), // 0x04 // 4    F3
   (0x003D         + kbdm_KBD_PAGE     ), // 0x05 // 5    F4
   (0x003E         + kbdm_KBD_PAGE     ), // 0x06 // 6    F5
   (0x003F         + kbdm_KBD_PAGE     ), // 0x07 // 7    F6
   (0x0040         + kbdm_KBD_PAGE     ), // 0x08 // 8    F7
   (0x0041         + kbdm_KBD_PAGE     ), // 0x09 // 9    F8
   (0x0042         + kbdm_KBD_PAGE     ), // 0x0A // 10   F9
   (0x0043         + kbdm_KBD_PAGE     ), // 0x0B // 11   F10
   (0x0044         + kbdm_KBD_PAGE     ), // 0x0C // 12   F11
   (0x0045         + kbdm_KBD_PAGE     ), // 0x0D // 13   F12
   (0x0046         + kbdm_KBD_PAGE     ), // 0x0E // 14   Print Screen
   (0x0047         + kbdm_KBD_PAGE     ), // 0x0F // 15   Scroll Lock
   (0x0048         + kbdm_KBD_PAGE     ), // 0x10 // 16   Pause | Break
   (0x0035         + kbdm_KBD_PAGE     ), // 0x11 // 17   Apostrophe
   (0x001E         + kbdm_KBD_PAGE     ), // 0x12 // 18   1
   (0x001F         + kbdm_KBD_PAGE     ), // 0x13 // 19   2
   (0x0020         + kbdm_KBD_PAGE     ), // 0x14 // 20   3
   (0x0021         + kbdm_KBD_PAGE     ), // 0x15 // 21   4
   (0x0022         + kbdm_KBD_PAGE     ), // 0x16 // 22   5
   (0x0023         + kbdm_KBD_PAGE     ), // 0x17 // 23   6
   (0x0024         + kbdm_KBD_PAGE     ), // 0x18 // 24   7
   (0x0025         + kbdm_KBD_PAGE     ), // 0x19 // 25   8
   (0x0026         + kbdm_KBD_PAGE     ), // 0x1A // 26   9
   (0x0027         + kbdm_KBD_PAGE     ), // 0x1B // 27   0
   (0x002D         + kbdm_KBD_PAGE     ), // 0x1C // 28   Minus
   (0x002E         + kbdm_KBD_PAGE     ), // 0x1D // 29   Equal
   (0x0031         + kbdm_KBD_PAGE     ), // 0x1E // 30   US Backslash
   (0x002A         + kbdm_KBD_PAGE     ), // 0x1F // 31   Back Space
   (0x0049         + kbdm_KBD_PAGE     ), // 0x20 // 32   Insert
   (0x004A         + kbdm_KBD_PAGE     ), // 0x21 // 33   Home
   (0x004B         + kbdm_KBD_PAGE     ), // 0x22 // 34   Page Up
   (0x0053         + kbdm_KBD_PAGE     ), // 0x23 // 35   Num Lock
   (0x0054         + kbdm_KBD_PAGE     ), // 0x24 // 36   [KP] Slash
   (0x0055         + kbdm_KBD_PAGE     ), // 0x25 // 37   [KP] Star
   (0x0056         + kbdm_KBD_PAGE     ), // 0x26 // 38   [KP] Minus
   (0x002B         + kbdm_KBD_PAGE     ), // 0x27 // 39   Tab
   (0x0014         + kbdm_KBD_PAGE     ), // 0x28 // 40   Q
   (0x001A         + kbdm_KBD_PAGE     ), // 0x29 // 41   W
   (0x0008         + kbdm_KBD_PAGE     ), // 0x2A // 42   E
   (0x0015         + kbdm_KBD_PAGE     ), // 0x2B // 43   R
   (0x0017         + kbdm_KBD_PAGE     ), // 0x2C // 44   T
   (0x001C         + kbdm_KBD_PAGE     ), // 0x2D // 45   Y
   (0x0018         + kbdm_KBD_PAGE     ), // 0x2E // 46   U
   (0x000C         + kbdm_KBD_PAGE     ), // 0x2F // 47   I
   (0x0012         + kbdm_KBD_PAGE     ), // 0x30 // 48   O
   (0x0013         + kbdm_KBD_PAGE     ), // 0x31 // 49   P
   (0x002F         + kbdm_KBD_PAGE     ), // 0x32 // 50   [
   (0x0030         + kbdm_KBD_PAGE     ), // 0x33 // 51   ]
   (0x004C         + kbdm_KBD_PAGE     ), // 0x34 // 52   Delete
   (0x004D         + kbdm_KBD_PAGE     ), // 0x35 // 53   End
   (0x004E         + kbdm_KBD_PAGE     ), // 0x36 // 54   Page Down
   (0x005F         + kbdm_KBD_PAGE     ), // 0x37 // 55   [KP] Home / 7
   (0x0060         + kbdm_KBD_PAGE     ), // 0x38 // 56   [KP] Up Arrow / 8
   (0x0061         + kbdm_KBD_PAGE     ), // 0x39 // 57   [KP] Page Up / 9
   (0x0057         + kbdm_KBD_PAGE     ), // 0x3A // 58   [KP] Plus
   (0x0039         + kbdm_KBD_PAGE     ), // 0x3B // 59   Caps Lock
   (0x0004         + kbdm_KBD_PAGE     ), // 0x3C // 60   A
   (0x0016         + kbdm_KBD_PAGE     ), // 0x3D // 61   S
   (0x0007         + kbdm_KBD_PAGE     ), // 0x3E // 62   D
   (0x0009         + kbdm_KBD_PAGE     ), // 0x3F // 63   F
   (0x000A         + kbdm_KBD_PAGE     ), // 0x40 // 64   G
   (0x000B         + kbdm_KBD_PAGE     ), // 0x41 // 65   H
   (0x000D         + kbdm_KBD_PAGE     ), // 0x42 // 66   J
   (0x000E         + kbdm_KBD_PAGE     ), // 0x43 // 67   K
   (0x000F         + kbdm_KBD_PAGE     ), // 0x44 // 68   L
   (0x0033         + kbdm_KBD_PAGE     ), // 0x45 // 69   Semi Colon
   (0x0034         + kbdm_KBD_PAGE     ), // 0x46 // 70   Apostrophe 1
   (0x0028         + kbdm_KBD_PAGE     ), // 0x47 // 71   Enter
   (0x005C         + kbdm_KBD_PAGE     ), // 0x48 // 72   [KP] Left Arrow / 4
   (0x005D         + kbdm_KBD_PAGE     ), // 0x49 // 73   [KP] 5
   (0x005E         + kbdm_KBD_PAGE     ), // 0x4A // 74   [KP] Right Arrow / 6
   (0x00E1         + kbdm_KBD_PAGE     ), // 0x4B // 75   Left Shift
   (0x001D         + kbdm_KBD_PAGE     ), // 0x4C // 76   Z
   (0x001B         + kbdm_KBD_PAGE     ), // 0x4D // 77   X
   (0x0006         + kbdm_KBD_PAGE     ), // 0x4E // 78   C
   (0x0019         + kbdm_KBD_PAGE     ), // 0x4F // 79   V
   (0x0005         + kbdm_KBD_PAGE     ), // 0x50 // 80   B
   (0x0011         + kbdm_KBD_PAGE     ), // 0x51 // 81   N
   (0x0010         + kbdm_KBD_PAGE     ), // 0x52 // 82   M
   (0x0036         + kbdm_KBD_PAGE     ), // 0x53 // 83   Comma
   (0x0037         + kbdm_KBD_PAGE     ), // 0x54 // 84   Full Stop
   (0x0038         + kbdm_KBD_PAGE     ), // 0x55 // 85   Slash
   (0x00E5         + kbdm_KBD_PAGE     ), // 0x56 // 86   Right Shift
   (0x0052         + kbdm_KBD_PAGE     ), // 0x57 // 87   Up Arrow
   (0x0059         + kbdm_KBD_PAGE     ), // 0x58 // 88   [KP] End / 1
   (0x005A         + kbdm_KBD_PAGE     ), // 0x59 // 89   [KP] Down Arrow / 2
   (0x005B         + kbdm_KBD_PAGE     ), // 0x5A // 90   [KP] Page Down / 3
   (0x0058         + kbdm_KBD_PAGE     ), // 0x5B // 91   [KP] Enter
   (0x00E0         + kbdm_KBD_PAGE     ), // 0x5C // 92   Left Ctrl
   (0x00E2         + kbdm_KBD_PAGE     ), // 0x5D // 93   Left Alt
   (0x002C         + kbdm_KBD_PAGE     ), // 0x5E // 94   Space Bar
   (0x00E6         + kbdm_KBD_PAGE     ), // 0x5F // 95   Right Alt
   (0x00E4         + kbdm_KBD_PAGE     ), // 0x60 // 96   Right Ctrl
   (0x0050         + kbdm_KBD_PAGE     ), // 0x61 // 97   Left Arrow
   (0x0051         + kbdm_KBD_PAGE     ), // 0x62 // 98   Down Arrow
   (0x004F         + kbdm_KBD_PAGE     ), // 0x63 // 99   Right Arrow
   (0x0062         + kbdm_KBD_PAGE     ), // 0x64 // 100  [KP] Ins / 0
   (0x0063         + kbdm_KBD_PAGE     ), // 0x65 // 101  [KP] Del / .
   (0x0085         + kbdm_KBD_PAGE     ), // 0x66 // 102  [KP] Comma
   (0x0032         + kbdm_KBD_PAGE     ), // 0x67 // 103  Euro 1: $ £ 
   (0x0064         + kbdm_KBD_PAGE     ), // 0x68 // 104  Euro 2: < >
   (0x0089         + kbdm_KBD_PAGE     ), // 0x69 // 105  Japan: Yen
   (0x0087         + kbdm_KBD_PAGE     ), // 0x6A // 106  Japan: Ro
   (0x008B         + kbdm_KBD_PAGE     ), // 0x6B // 107  Japan: Muhenkan
   (0x008A         + kbdm_KBD_PAGE     ), // 0x6C // 108  Japan: Henkan
   (0x0088         + kbdm_KBD_PAGE     ), // 0x6D // 109  Japan: Katagana/Hiragana
   (0x00E3         + kbdm_KBD_PAGE     ), // 0x6E // 110  Left Win
   (0x00E7         + kbdm_KBD_PAGE     ), // 0x6F // 111  Right Win
   (0x0065         + kbdm_KBD_PAGE     ), // 0x70 // 112  App
   
   (0x0225         + kbdm_CONSUM_PAGE  ), // 0x71 // 113  www Forward 	
   (0x00B6         + kbdm_CONSUM_PAGE  ), // 0x72 // 114  Prev Track
   (0x00B5         + kbdm_CONSUM_PAGE  ), // 0x73 // 115  Next Track
   (0x00B7         + kbdm_CONSUM_PAGE  ), // 0x74 // 116  Stop
   (0x00CD         + kbdm_CONSUM_PAGE  ), // 0x75 // 117  Play / Pause
   (0x00EA         + kbdm_CONSUM_PAGE  ), // 0x76 // 118  Volume Down
   (0x00E9         + kbdm_CONSUM_PAGE  ), // 0x77 // 119  Volume Up
   (0x00E2         + kbdm_CONSUM_PAGE  ), // 0x78 // 120  Mute 	
   (0x0192         + kbdm_CONSUM_PAGE  ), // 0x79 // 121  Calculator
   (0x018A         + kbdm_CONSUM_PAGE  ), // 0x7A // 122  Mail
   (0x0221         + kbdm_CONSUM_PAGE  ), // 0x7B // 123  WWW Search
   (0x0223         + kbdm_CONSUM_PAGE  ), // 0x7C // 124  WWW Home
   (0x022A         + kbdm_CONSUM_PAGE  ), // 0x7D // 125  Smart Favorites
   (0x0001         + kbdm_GENERIC_PAGE ), // 0x7E // 126  Wake / Suspend
   (0x0224         + kbdm_CONSUM_PAGE  ), // 0x7F // 127  www Backward
   (0x0000         + kbdm_UNDEF_PAGE   ), // 0x80 // 128  F Lock
   (0x0091         + kbdm_KBD_PAGE     ), // 0x81 // 129  Korea Hanja
   (0x0090         + kbdm_KBD_PAGE     ), // 0x82 // 130  Korea Hanguel
   
                                               // ---- Hot Key ----
   (0x0183         + kbdm_CONSUM_PAGE  ), // 0x83 // 131  Button 0 Music Player
   (0x01B6         + kbdm_CONSUM_PAGE  ), // 0x84 // 132  Button 1 My Pictures
   (0x003D         + kbdm_BUTTON_PAGE  ), // 0x85 // 133  Button 2 Vista Gadget
   (0x01A2         + kbdm_CONSUM_PAGE  ), // 0x86 // 134  Button 3 Flip 3D
   (0x000D         + kbdm_MEDIA_CENTER ), // 0x87 // 135  Button 4 Media Center
   (0x022D         + kbdm_CONSUM_PAGE  ), // 0x88 // 136  Button 5 Zoom In  (+) 
   (0x022E         + kbdm_CONSUM_PAGE  ), // 0x89 // 137  Button 6 Zoom Out (-)
   (0x0007         + kbdm_UNDEF_PAGE   ), // 0x8A // 138  Button 7
   (0x0008         + kbdm_UNDEF_PAGE   ), // 0x8B // 139  Button 8
   (0x0009         + kbdm_UNDEF_PAGE   ), // 0x8C // 140  Button 9
   (0x000A         + kbdm_UNDEF_PAGE   ), // 0x8D // 141  Button 10 
   (0x000B         + kbdm_UNDEF_PAGE   ), // 0x8E // 142  Button 11 
   (0x000C         + kbdm_UNDEF_PAGE   ), // 0x8F // 143  Button 12 
   (0x000D         + kbdm_UNDEF_PAGE   ), // 0x90 // 144  Button 13 
   (0x00B4         + kbdm_CONSUM_PAGE  ), // 0x91 // 145  Button 14 Rewind 	  
   (0x00B3         + kbdm_CONSUM_PAGE  ), // 0x92 // 146  Button 15 Fast Forward
   
                                               // ---- Fn Key On ----
   (0x0184         + kbdm_CONSUM_PAGE  ), // 0x93 // 147  Button 16 AL Text Editor
   (0x0186         + kbdm_CONSUM_PAGE  ), // 0x94 // 148  Button 17 AL SpreadSheet
   (0x018E         + kbdm_CONSUM_PAGE  ), // 0x95 // 149  Button 18 AL Calendar
   (0x0044         + kbdm_BUTTON_PAGE  ), // 0x96 // 150  Button 19 Funct A
   (0x0045         + kbdm_BUTTON_PAGE  ), // 0x97 // 151  Button 20 Funct B 
   (0x0046         + kbdm_BUTTON_PAGE  ), // 0x98 // 152  Button 21 Funct C
   (0x0223         + kbdm_CONSUM_PAGE  ), // 0x99 // 153  Button 22 AC Home
   (0x01BC         + kbdm_CONSUM_PAGE  ), // 0x9A // 154  Button 23 AC Instant Messanger
   (0x018A         + kbdm_CONSUM_PAGE  ), // 0x9B // 155  Button 24 AC Email	
   (0x0221         + kbdm_CONSUM_PAGE  ), // 0x9C // 156  Button 25 AC Search
   (0x0000         + kbdm_UNDEF_PAGE   ), // 0x9D // 157  Button 26 Battery Test
   (0x00B8         + kbdm_CONSUM_PAGE  ), // 0x9E // 158  Button 27 AC Eject 
   (0x003E         + kbdm_BUTTON_PAGE  ), // 0x9F // 159  Button 28 AL Presentation
   (0x001D         + kbdm_UNDEF_PAGE   ), // 0xA0 // 160  Button 29
   (0x001E         + kbdm_UNDEF_PAGE   ), // 0xA1 // 161  Button 30 
   (0x001F         + kbdm_UNDEF_PAGE   ), // 0xA2 // 162  Button 31 Fn Key for PC
   
                                               // ---- Key for Mac ----
   (0x002C         + kbdm_BUTTON_PAGE  ), // 0xA3 // 163  Button 32 Fn Key for MAC
   (0x0068         + kbdm_KBD_PAGE     ), // 0xA4 // 164  F13
   (0x0069         + kbdm_KBD_PAGE     ), // 0xA5 // 165  F14
   (0x006A         + kbdm_KBD_PAGE     ), // 0xA6 // 166  F15
   (0x006B         + kbdm_KBD_PAGE     ), // 0xA7 // 167  F16
   (0x006C         + kbdm_KBD_PAGE     ), // 0xA8 // 168  F17
   (0x006D         + kbdm_KBD_PAGE     ), // 0xA9 // 169  F18
   (0x006E         + kbdm_KBD_PAGE     ), // 0xAA // 170  F19   
   (0x006F         + kbdm_BUTTON_PAGE  ), // 0xAB // 171  Brightness -    [F1]
   (0x006E         + kbdm_BUTTON_PAGE  ), // 0xAC // 172  Brightness +    [F2]
   (0x003E         + kbdm_BUTTON_PAGE  ), // 0xAD // 173  Presentation    [F3]
   (0x003D         + kbdm_BUTTON_PAGE  ), // 0xAE // 174  Dashboard       [F4]
   (0x0045         + kbdm_BUTTON_PAGE  ), // 0xAF // 175  F5              [F5]
   (0x0046         + kbdm_BUTTON_PAGE  ), // 0xB0 // 176  F6              [F6]
   (0x0000         + kbdm_UNDEF_PAGE   ), // 0xB1 // 177  Prev Track      [F7]
   (0x00CD         + kbdm_CONSUM_PAGE  ), // 0xB2 // 178  Play / Pause    [F8]
   (0x0000         + kbdm_UNDEF_PAGE   ), // 0xB3 // 179  Next Track      [F9]
   (0x00E2         + kbdm_CONSUM_PAGE  ), // 0xB4 // 180  Mute            [F10]
   (0x00EA         + kbdm_CONSUM_PAGE  ), // 0xB5 // 181  Volume -        [F11]
   (0x00E9         + kbdm_CONSUM_PAGE  ), // 0xB6 // 182  Volume +        [F12]
   (0x006B         + kbdm_BUTTON_PAGE  ), // 0xB7 // 183  Cover Flow      [F13]
   (0x006C         + kbdm_BUTTON_PAGE  ), // 0xB8 // 184  QuickLook       [F14]
   (0x006D         + kbdm_BUTTON_PAGE  ), // 0xB9 // 185  Spaces          [F15]
   (0x0183         + kbdm_CONSUM_PAGE  ), // 0xBA // 186  iTunes          [F16]
   (0x018A         + kbdm_CONSUM_PAGE  ), // 0xBB // 187  Email           [F17]
   (0x0223         + kbdm_CONSUM_PAGE  ), // 0xBC // 188  Safari          [F18]
   (0x0192         + kbdm_CONSUM_PAGE  ), // 0xBD // 189  iCalc           [F19]  
   (0x0067         + kbdm_KBD_PAGE     ), // 0xBE // 190  [KP] =
   (0x0020         + kbdm_UNDEF_PAGE   ), // 0xBF // 191  Button 32
   (0x0021         + kbdm_UNDEF_PAGE   ), // 0xC0 // 192  Button 33
   (0x0022         + kbdm_UNDEF_PAGE   ), // 0xC1 // 193  Button 34
   (0x0023         + kbdm_UNDEF_PAGE   ), // 0xC2 // 194  Button 35 
   (0x0024         + kbdm_UNDEF_PAGE   ), // 0xC3 // 195  Button 36
    
                                               // ---- Key for PC -----   
   (0x004C         + kbdm_KBD_PAGE     )  // 0xC4 // 196  Delete
};

// ============================================================================================
// Type Definitions
// ============================================================================================

// ============================================================================================
// Exported Global Data
// ============================================================================================

// ============================================================================================
// Private Data
// ============================================================================================

#pragma DATA_SEG _DATA_ZEROPAGE

static const kbdm_keyMatrix_ts*  _pKeyMatrix;
static const uint16_t*           _pHidCode;

#pragma DATA_SEG DEFAULT

// ============================================================================================
// Inline Code
// ============================================================================================

// ============================================================================================
// Private Function Prototypes
// ============================================================================================

// ============================================================================================
// Functions
// ============================================================================================

/// \defgroup kbdmap_c. Conversion tables RowColcode -> Scancode
/// @{
        
#pragma CODE_SEG APPLICATION_CODE


//---------------------------------------------------------------------------------------------  
/// Select the matrix adapted to the hardware
///
/// RETURN: void
//---------------------------------------------------------------------------------------------   
//
void kbdm_InitModule(void)
{
   _pKeyMatrix = (kbdm_keyMatrix_ts*) kbdm_KeyMatrix;
   _pHidCode   = (uint16_t*) kbdm_eQuadHidCode; 
}
 
//---------------------------------------------------------------------------------------------
/// The suitable matrix is pointed by the global var _pKeyMatrix  
/// IN Row: row of the key
/// IN Col: col of the key
///
// RETURN VALUE: the RF / SCAN code
//---------------------------------------------------------------------------------------------   
//
const kbdm_keyMatrix_ts* kbdm_GetKbdScanCode(uint8_t Row, uint8_t Col)
{
  const kbdm_keyMatrix_ts* pkey_data;
  pkey_data = &_pKeyMatrix[(Row * NB_COL_12X12) + Col];
  
  return (pkey_data);
}

//---------------------------------------------------------------------------------------------
/// IN : Address of the Translate Matrix (e.g. Sholo_Short, Sholo_Long)
/// IN : Matrix Index
///
/// RETURN: Pointer on the new value
//---------------------------------------------------------------------------------------------
//
const kbdm_keyMatrix_ts*  kbdm_pTranslMatrix(const kbdm_keyMatrix_ts* pMatrix, uint8_t Index)
{ 
  pMatrix += Index;
  
  return (pMatrix);
}

//---------------------------------------------------------------------------------------------  
/// The suitable HID matrix is pointed by the global var _pHidCode
/// IN ScanCode:  Based on the current RF code
///
/// RETURN VALUE: HID code
//---------------------------------------------------------------------------------------------   
//
uint16_t kbdm_GetHidCode(uint16_t ScanCode)
{	
   return _pHidCode[ScanCode];
}

#pragma CODE_SEG DEFAULT

/// @}

