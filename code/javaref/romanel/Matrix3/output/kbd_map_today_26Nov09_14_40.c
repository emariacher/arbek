/* Footer */

          !    0     !    1     !    2     !    3     !    4     !    5     !    6     !    7     !    8     !    9     !   10     !   11     !   12     !
    0     ! L-Shift ! HID Button 257: AVR power on/off ! AL Entertainment Content Browser ! AL OEM Features/Tips/Tutorial Browser !      W ! UpArrow !     ]} !  Enter ! 'dblquot !     ;: !  ,&lt; !      K !     Ro !
    1     ! R-Shift !          !          ! AC Open !    Esc !      E !     5% !      H !      P !      N !      M !      J !  Hanja !
    2     ! Scan Next Track ! L-Ctrl !          ! AC Bookmarks !    H31 ! PageUp ! Bkslash| ! DnArrow !      Q !     -_ !     =+ !      L ! Hanguel !
    3     ! Scan Previous Track ! R-Ctrl !          ! AC Split !    H32 !     2@ !      B !      S ! R-Arrow !     3/ !     7& !     .> ! KataHira !
    4     !   Vol+ ! HID Button 258: STB power on/off !  L-Alt ! HID Button 259: TV power on/off !     H1 !      U !  &lt;> !      G !      D !      T !      R !     [{ ! Muhenkan !
    5     !   Vol- !  Pause !   Menu !          !     4$ !      I !      X ! AC Back !      O !      F !     8* !    Tab !     6^ !
    6     !   Mute !   Play !          ! AC Search !      A ! AC Home ! CapsLck !      C !    / ? !     0) !     9( !     ¥| !     1! !
    7     !          !          !          !      Y ! L-Arrow ! Bkspace !  Space ! PageDn !   R-Fn !      Z !      V !     $£ ! Henkan !

          !    0     !    1     !    2     !    3     !    4     !    5     !    6     !    7     !    8     !    9     !   10     !   11     !   12     !
    0     !          ! Media Select VCR !          !          !          !          !          !          !          !          !          !          !          !
    1     !          !          !          !          !          !          !     F5 !          !          !          !          !          !          !
    2     !          !          !          !          !          !          !          !          !          !    F11 !    F12 !          !          !
    3     !          !          !          !          !          ! HID Button 261: Info Dialog !          !          !          ! HID Button 262: List Dialog !     F7 !          !          !
    4     !          ! Media Select Cable !          ! Media Select TV !          !          !          !          !          !          !          !          !          !
    5     !          !          !          !          ! HID Button 263: User Dialog !          !          !          !          !          !     F8 !          !     F6 !
    6     !          !          !          !          !          !          !          !          !          !    F10 !     F9 !          ! HID Button 260: Menu Dialog !
    7     !          !          !          !          !          ! Delete !          !          !          !          !          !          !          !
/************************************/
{ // ROW 0
  {1,0}, // r0c0  L-Shift 
  {IDX(0),kbdm_FKEY}, // r0c1  HID Button 257: AVR power on/off ---- Media Select VCR 
  {4,0}, // r0c2  AL Entertainment Content Browser 
  {5,0}, // r0c3  AL OEM Features/Tips/Tutorial Browser 
  {6,0}, // r0c4       W 
  {7,0}, // r0c5  UpArrow 
  {8,kbdm_TILDA}, // r0c6      ]} 
  {9,0}, // r0c7   Enter 
  {10,0}, // r0c8  'dblquot 
  {11,0}, // r0c9      ;: 
  {12,0}, // r0c10   ,&lt; 
  {13,0}, // r0c11       K 
  {14,0}, // r0c12      Ro 
}
{ // ROW 1
  {15,0}, // r1c0  R-Shift 
  {0,0}, // r1c1
  {0,0}, // r1c2
  {16,0}, // r1c3  AC Open 
  {17,0}, // r1c4     Esc 
  {18,0}, // r1c5       E 
  {IDX(1),kbdm_FKEY}, // r1c6      5% ----     F5 
  {21,0}, // r1c7       H 
  {22,0}, // r1c8       P 
  {23,0}, // r1c9       N 
  {24,0}, // r1c10       M 
  {25,0}, // r1c11       J 
  {26,0}, // r1c12   Hanja 
}
{ // ROW 2
  {27,0}, // r2c0  Scan Next Track 
  {28,0}, // r2c1  L-Ctrl 
  {0,0}, // r2c2
  {29,0}, // r2c3  AC Bookmarks 
  {30,0}, // r2c4     H31 
  {31,0}, // r2c5  PageUp 
  {32,0}, // r2c6  Bkslash| 
  {33,0}, // r2c7  DnArrow 
  {34,0}, // r2c8       Q 
  {IDX(2),kbdm_FKEY}, // r2c9      -_ ----    F11 
  {IDX(3),kbdm_FKEY}, // r2c10      =+ ----    F12 
  {39,0}, // r2c11       L 
  {40,0}, // r2c12  Hanguel 
}
{ // ROW 3
  {41,0}, // r3c0  Scan Previous Track 
  {42,0}, // r3c1  R-Ctrl 
  {0,0}, // r3c2
  {43,0}, // r3c3  AC Split 
  {44,0}, // r3c4     H32 
  {IDX(4),kbdm_FKEY}, // r3c5      2@ ---- HID Button 261: Info Dialog 
  {47,0}, // r3c6       B 
  {48,0}, // r3c7       S 
  {49,0}, // r3c8  R-Arrow 
  {IDX(5),kbdm_FKEY}, // r3c9      3/ ---- HID Button 262: List Dialog 
  {IDX(6),kbdm_FKEY}, // r3c10      7& ----     F7 
  {54,0}, // r3c11      .> 
  {55,0}, // r3c12  KataHira 
}
{ // ROW 4
  {56,0}, // r4c0    Vol+ 
  {IDX(7),kbdm_FKEY}, // r4c1  HID Button 258: STB power on/off ---- Media Select Cable 
  {59,0}, // r4c2   L-Alt 
  {IDX(8),kbdm_FKEY}, // r4c3  HID Button 259: TV power on/off ---- Media Select TV 
  {62,0}, // r4c4      H1 
  {63,0}, // r4c5       U 
  {64,0}, // r4c6   &lt;> 
  {65,0}, // r4c7       G 
  {66,0}, // r4c8       D 
  {67,0}, // r4c9       T 
  {68,0}, // r4c10       R 
  {69,kbdm_GRAVE}, // r4c11      [{ 
  {70,0}, // r4c12  Muhenkan 
}
{ // ROW 5
  {71,0}, // r5c0    Vol- 
  {72,0}, // r5c1   Pause 
  {73,0}, // r5c2    Menu 
  {0,0}, // r5c3
  {IDX(9),kbdm_FKEY}, // r5c4      4$ ---- HID Button 263: User Dialog 
  {76,0}, // r5c5       I 
  {77,0}, // r5c6       X 
  {78,0}, // r5c7  AC Back 
  {79,0}, // r5c8       O 
  {80,0}, // r5c9       F 
  {IDX(10),kbdm_FKEY}, // r5c10      8* ----     F8 
  {83,0}, // r5c11     Tab 
  {IDX(11),kbdm_FKEY}, // r5c12      6^ ----     F6 
}
{ // ROW 6
  {86,0}, // r6c0    Mute 
  {87,0}, // r6c1    Play 
  {0,0}, // r6c2
  {88,0}, // r6c3  AC Search 
  {89,0}, // r6c4       A 
  {90,0}, // r6c5  AC Home 
  {91,0}, // r6c6  CapsLck 
  {92,0}, // r6c7       C 
  {93,0}, // r6c8     / ? 
  {IDX(12),kbdm_FKEY}, // r6c9      0) ----    F10 
  {IDX(13),kbdm_FKEY}, // r6c10      9( ----     F9 
  {98,0}, // r6c11      ¥| 
  {IDX(14),kbdm_FKEY}, // r6c12      1! ---- HID Button 260: Menu Dialog 
}
{ // ROW 7
  {0,0}, // r7c0
  {0,0}, // r7c1
  {0,0}, // r7c2
  {101,0}, // r7c3       Y 
  {102,0}, // r7c4  L-Arrow 
  {IDX(15),kbdm_FKEY}, // r7c5  Bkspace ---- Delete 
  {105,0}, // r7c6   Space 
  {106,0}, // r7c7  PageDn 
  {107,kbdm_FN_KEY}, // r7c8    R-Fn 
  {108,0}, // r7c9       Z 
  {109,0}, // r7c10       V 
  {110,0}, // r7c11      $£ 
  {111,0}, // r7c12  Henkan 
}
/************************************/
  {0,0}
// ROW 0
           (0x00E1 + kbdm_KBD_PAGE   ), //  44   1/0x01 L-Shift r0c0
           (0x0101 + kbdm_BUTTON_PAGE), // 1101   2/0x02 HID Button 257: AVR power on/off
           (0x0092 + kbdm_CONSUM_PAGE), // 459   3/0x03 Media Select VCR r0c1
           (0x01C0 + kbdm_CONSUM_PAGE), // 619   4/0x04 AL Entertainment Content Browser r0c2
           (0x01BD + kbdm_CONSUM_PAGE), // 616   5/0x05 AL OEM Features/Tips/Tutorial Browser r0c3
           (0x001A + kbdm_KBD_PAGE   ), //  18   6/0x06 W r0c4
           (0x0052 + kbdm_KBD_PAGE   ), //  83   7/0x07 UpArrow r0c5
           (0x0030 + kbdm_KBD_PAGE   ), //  28   8/0x08 ]} r0c6
           (0x0028 + kbdm_KBD_PAGE   ), //  43   9/0x09 Enter r0c7
           (0x0034 + kbdm_KBD_PAGE   ), //  41  10/0x0A 'dblquot r0c8
           (0x0033 + kbdm_KBD_PAGE   ), //  40  11/0x0B ;: r0c9
           (0x0036 + kbdm_KBD_PAGE   ), //  53  12/0x0C ,&lt; r0c10
           (0x000E + kbdm_KBD_PAGE   ), //  38  13/0x0D K r0c11
           (0x0087 + kbdm_KBD_PAGE   ), //  56  14/0x0E Ro r0c12
// ROW 1
           (0x00E5 + kbdm_KBD_PAGE   ), //  57  15/0x0F R-Shift r1c0
           (0x0202 + kbdm_CONSUM_PAGE), // 629  16/0x10 AC Open r1c3
           (0x0029 + kbdm_KBD_PAGE   ), // 110  17/0x11 Esc r1c4
           (0x0008 + kbdm_KBD_PAGE   ), //  19  18/0x12 E r1c5
           (0x0022 + kbdm_KBD_PAGE   ), //   6  19/0x13 5%
           (0x003E + kbdm_KBD_PAGE   ), // 116  20/0x14 F5 r1c6
           (0x000B + kbdm_KBD_PAGE   ), //  36  21/0x15 H r1c7
           (0x0013 + kbdm_KBD_PAGE   ), //  26  22/0x16 P r1c8
           (0x0011 + kbdm_KBD_PAGE   ), //  51  23/0x17 N r1c9
           (0x0010 + kbdm_KBD_PAGE   ), //  52  24/0x18 M r1c10
           (0x000D + kbdm_KBD_PAGE   ), //  37  25/0x19 J r1c11
           (0x0091 + kbdm_KBD_PAGE   ), // 135  26/0x1A Hanja r1c12
// ROW 2
           (0x00B5 + kbdm_CONSUM_PAGE), // 482  27/0x1B Scan Next Track r2c0
           (0x00E0 + kbdm_KBD_PAGE   ), //  58  28/0x1C L-Ctrl r2c1
           (0x022A + kbdm_CONSUM_PAGE), // 653  29/0x1D AC Bookmarks r2c3
                                   H31, // 154  30/0x1E H31 r2c4
           (0x004B + kbdm_KBD_PAGE   ), //  85  31/0x1F PageUp r2c5
           (0x0031 + kbdm_KBD_PAGE   ), //  29  32/0x20 Bkslash| r2c6
           (0x0051 + kbdm_KBD_PAGE   ), //  84  33/0x21 DnArrow r2c7
           (0x0014 + kbdm_KBD_PAGE   ), //  17  34/0x22 Q r2c8
           (0x002D + kbdm_KBD_PAGE   ), //  12  35/0x23 -_
           (0x0044 + kbdm_KBD_PAGE   ), // 122  36/0x24 F11 r2c9
           (0x002E + kbdm_KBD_PAGE   ), //  13  37/0x25 =+
           (0x0045 + kbdm_KBD_PAGE   ), // 123  38/0x26 F12 r2c10
           (0x000F + kbdm_KBD_PAGE   ), //  39  39/0x27 L r2c11
           (0x0090 + kbdm_KBD_PAGE   ), // 136  40/0x28 Hanguel r2c12
// ROW 3
           (0x00B6 + kbdm_CONSUM_PAGE), // 483  41/0x29 Scan Previous Track r3c0
           (0x00E4 + kbdm_KBD_PAGE   ), //  64  42/0x2A R-Ctrl r3c1
           (0x029A + kbdm_CONSUM_PAGE), // 765  43/0x2B AC Split r3c3
                                   H32, // 155  44/0x2C H32 r3c4
           (0x001F + kbdm_KBD_PAGE   ), //   3  45/0x2D 2@
           (0x0105 + kbdm_BUTTON_PAGE), // 1105  46/0x2E HID Button 261: Info Dialog r3c5
           (0x0005 + kbdm_KBD_PAGE   ), //  50  47/0x2F B r3c6
           (0x0016 + kbdm_KBD_PAGE   ), //  32  48/0x30 S r3c7
           (0x004F + kbdm_KBD_PAGE   ), //  89  49/0x31 R-Arrow r3c8
           (0x0020 + kbdm_KBD_PAGE   ), //   4  50/0x32 3/
           (0x0106 + kbdm_BUTTON_PAGE), // 1106  51/0x33 HID Button 262: List Dialog r3c9
           (0x0024 + kbdm_KBD_PAGE   ), //   8  52/0x34 7&
           (0x0040 + kbdm_KBD_PAGE   ), // 118  53/0x35 F7 r3c10
           (0x0037 + kbdm_KBD_PAGE   ), //  54  54/0x36 .> r3c11
           (0x0088 + kbdm_KBD_PAGE   ), // 134  55/0x37 KataHira r3c12
// ROW 4
           (0x00E9 + kbdm_CONSUM_PAGE), // 282  56/0x38 Vol+ r4c0
           (0x0102 + kbdm_BUTTON_PAGE), // 1102  57/0x39 HID Button 258: STB power on/off
           (0x0097 + kbdm_CONSUM_PAGE), // 464  58/0x3A Media Select Cable r4c1
           (0x00E2 + kbdm_KBD_PAGE   ), //  60  59/0x3B L-Alt r4c2
           (0x0103 + kbdm_BUTTON_PAGE), // 1103  60/0x3C HID Button 259: TV power on/off
           (0x0089 + kbdm_CONSUM_PAGE), // 450  61/0x3D Media Select TV r4c3
                                    H1, // 156  62/0x3E H1 r4c4
           (0x0018 + kbdm_KBD_PAGE   ), //  23  63/0x3F U r4c5
           (0x0064 + kbdm_KBD_PAGE   ), //  45  64/0x40 &lt;> r4c6
           (0x000A + kbdm_KBD_PAGE   ), //  35  65/0x41 G r4c7
           (0x0007 + kbdm_KBD_PAGE   ), //  33  66/0x42 D r4c8
           (0x0017 + kbdm_KBD_PAGE   ), //  21  67/0x43 T r4c9
           (0x0015 + kbdm_KBD_PAGE   ), //  20  68/0x44 R r4c10
           (0x002F + kbdm_KBD_PAGE   ), //  27  69/0x45 [{ r4c11
           (0x008B + kbdm_KBD_PAGE   ), // 132  70/0x46 Muhenkan r4c12
// ROW 5
           (0x00EA + kbdm_CONSUM_PAGE), // 281  71/0x47 Vol- r5c0
           (0x00B1 + kbdm_CONSUM_PAGE), // 478  72/0x48 Pause r5c1
           (0x0040 + kbdm_CONSUM_PAGE), // 425  73/0x49 Menu r5c2
           (0x0021 + kbdm_KBD_PAGE   ), //   5  74/0x4A 4$
           (0x0107 + kbdm_BUTTON_PAGE), // 1107  75/0x4B HID Button 263: User Dialog r5c4
           (0x000C + kbdm_KBD_PAGE   ), //  24  76/0x4C I r5c5
           (0x001B + kbdm_KBD_PAGE   ), //  47  77/0x4D X r5c6
           (0x0224 + kbdm_CONSUM_PAGE), // 647  78/0x4E AC Back r5c7
           (0x0012 + kbdm_KBD_PAGE   ), //  25  79/0x4F O r5c8
           (0x0009 + kbdm_KBD_PAGE   ), //  34  80/0x50 F r5c9
           (0x0025 + kbdm_KBD_PAGE   ), //   9  81/0x51 8*
           (0x0041 + kbdm_KBD_PAGE   ), // 119  82/0x52 F8 r5c10
           (0x002B + kbdm_KBD_PAGE   ), //  16  83/0x53 Tab r5c11
           (0x0023 + kbdm_KBD_PAGE   ), //   7  84/0x54 6^
           (0x003F + kbdm_KBD_PAGE   ), // 117  85/0x55 F6 r5c12
// ROW 6
           (0x00E2 + kbdm_CONSUM_PAGE), // 280  86/0x56 Mute r6c0
           (0x00B0 + kbdm_CONSUM_PAGE), // 477  87/0x57 Play r6c1
           (0x0221 + kbdm_CONSUM_PAGE), // 644  88/0x58 AC Search r6c3
           (0x0004 + kbdm_KBD_PAGE   ), //  31  89/0x59 A r6c4
           (0x0223 + kbdm_CONSUM_PAGE), // 646  90/0x5A AC Home r6c5
           (0x0039 + kbdm_KBD_PAGE   ), //  30  91/0x5B CapsLck r6c6
           (0x0006 + kbdm_KBD_PAGE   ), //  48  92/0x5C C r6c7
           (0x0038 + kbdm_KBD_PAGE   ), //  55  93/0x5D / ? r6c8
           (0x0027 + kbdm_KBD_PAGE   ), //  11  94/0x5E 0)
           (0x0043 + kbdm_KBD_PAGE   ), // 121  95/0x5F F10 r6c9
           (0x0026 + kbdm_KBD_PAGE   ), //  10  96/0x60 9(
           (0x0042 + kbdm_KBD_PAGE   ), // 120  97/0x61 F9 r6c10
           (0x0089 + kbdm_KBD_PAGE   ), //  14  98/0x62 ¥| r6c11
           (0x001E + kbdm_KBD_PAGE   ), //   2  99/0x63 1!
           (0x0104 + kbdm_BUTTON_PAGE), // 1104 100/0x64 HID Button 260: Menu Dialog r6c12
// ROW 7
           (0x001C + kbdm_KBD_PAGE   ), //  22 101/0x65 Y r7c3
           (0x0050 + kbdm_KBD_PAGE   ), //  79 102/0x66 L-Arrow r7c4
           (0x002A + kbdm_KBD_PAGE   ), //  15 103/0x67 Bkspace
           (0x004C + kbdm_KBD_PAGE   ), //  76 104/0x68 Delete r7c5
           (0x002C + kbdm_KBD_PAGE   ), //  61 105/0x69 Space r7c6
           (0x004E + kbdm_KBD_PAGE   ), //  86 106/0x6A PageDn r7c7
           (0x002C + kbdm_BUTTON_PAGE), // 152 107/0x6B R-Fn r7c8
           (0x001D + kbdm_KBD_PAGE   ), //  46 108/0x6C Z r7c9
           (0x0019 + kbdm_KBD_PAGE   ), //  49 109/0x6D V r7c10
           (0x0032 + kbdm_KBD_PAGE   ), //  42 110/0x6E $£ r7c11
           (0x008A + kbdm_KBD_PAGE   ), // 133 111/0x6F Henkan r7c12

kbdm_FnKeyOff[]
  {2,0}, // r0c1  HID Button 257: AVR power on/off ---- Media Select VCR 
  {19,0}, // r1c6      5% ----     F5 
  {35,0}, // r2c9      -_ ----    F11 
  {37,0}, // r2c10      =+ ----    F12 
  {45,0}, // r3c5      2@ ---- HID Button 261: Info Dialog 
  {50,0}, // r3c9      3/ ---- HID Button 262: List Dialog 
  {52,0}, // r3c10      7& ----     F7 
  {57,0}, // r4c1  HID Button 258: STB power on/off ---- Media Select Cable 
  {60,0}, // r4c3  HID Button 259: TV power on/off ---- Media Select TV 
  {74,0}, // r5c4      4$ ---- HID Button 263: User Dialog 
  {81,0}, // r5c10      8* ----     F8 
  {84,0}, // r5c12      6^ ----     F6 
  {94,0}, // r6c9      0) ----    F10 
  {96,0}, // r6c10      9( ----     F9 
  {99,0}, // r6c12      1! ---- HID Button 260: Menu Dialog 
  {103,0}, // r7c5  Bkspace ---- Delete 

kbdm_FnKeyOn[]
  {3,0}, // r0c1  Media Select VCR ---- HID Button 257: AVR power on/off 
  {20,0}, // r1c6      F5 ----     5% 
  {36,0}, // r2c9     F11 ----     -_ 
  {38,0}, // r2c10     F12 ----     =+ 
  {46,0}, // r3c5  HID Button 261: Info Dialog ----     2@ 
  {51,0}, // r3c9  HID Button 262: List Dialog ----     3/ 
  {53,0}, // r3c10      F7 ----     7& 
  {58,0}, // r4c1  Media Select Cable ---- HID Button 258: STB power on/off 
  {61,0}, // r4c3  Media Select TV ---- HID Button 259: TV power on/off 
  {75,0}, // r5c4  HID Button 263: User Dialog ----     4$ 
  {82,0}, // r5c10      F8 ----     8* 
  {85,0}, // r5c12      F6 ----     6^ 
  {95,0}, // r6c9     F10 ----     0) 
  {97,0}, // r6c10      F9 ----     9( 
  {100,0}, // r6c12  HID Button 260: Menu Dialog ----     1! 
  {104,0}, // r7c5  Delete ---- Bkspace 
/* $Log$ */
