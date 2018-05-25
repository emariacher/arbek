
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
  {(0x00E1 + kbdm_KBD_PAGE   ),0}, // r0c0  L-Shift 
  {IDX(0),kbdm_FKEY}, // r0c1  HID Button 257: AVR power on/off ---- Media Select VCR 
  {(0x01C0 + kbdm_CONSUM_PAGE),0}, // r0c2  AL Entertainment Content Browser 
  {(0x01BD + kbdm_CONSUM_PAGE),0}, // r0c3  AL OEM Features/Tips/Tutorial Browser 
  {(0x001A + kbdm_KBD_PAGE   ),0}, // r0c4       W 
  {(0x0052 + kbdm_KBD_PAGE   ),0}, // r0c5  UpArrow 
  {(0x0030 + kbdm_KBD_PAGE   ),kbdm_TILDA}, // r0c6      ]} 
  {(0x0028 + kbdm_KBD_PAGE   ),0}, // r0c7   Enter 
  {(0x0034 + kbdm_KBD_PAGE   ),0}, // r0c8  'dblquot 
  {(0x0033 + kbdm_KBD_PAGE   ),0}, // r0c9      ;: 
  {(0x0036 + kbdm_KBD_PAGE   ),0}, // r0c10   ,&lt; 
  {(0x000E + kbdm_KBD_PAGE   ),0}, // r0c11       K 
  {(0x0087 + kbdm_KBD_PAGE   ),0}, // r0c12      Ro 
}
{ // ROW 1
  {(0x00E5 + kbdm_KBD_PAGE   ),0}, // r1c0  R-Shift 
  {0,0}, // r1c1
  {0,0}, // r1c2
  {(0x0202 + kbdm_CONSUM_PAGE),0}, // r1c3  AC Open 
  {(0x0029 + kbdm_KBD_PAGE   ),0}, // r1c4     Esc 
  {(0x0008 + kbdm_KBD_PAGE   ),0}, // r1c5       E 
  {IDX(1),kbdm_FKEY}, // r1c6      5% ----     F5 
  {(0x000B + kbdm_KBD_PAGE   ),0}, // r1c7       H 
  {(0x0013 + kbdm_KBD_PAGE   ),0}, // r1c8       P 
  {(0x0011 + kbdm_KBD_PAGE   ),0}, // r1c9       N 
  {(0x0010 + kbdm_KBD_PAGE   ),0}, // r1c10       M 
  {(0x000D + kbdm_KBD_PAGE   ),0}, // r1c11       J 
  {(0x0091 + kbdm_KBD_PAGE   ),0}, // r1c12   Hanja 
}
{ // ROW 2
  {(0x00B5 + kbdm_CONSUM_PAGE),0}, // r2c0  Scan Next Track 
  {(0x00E0 + kbdm_KBD_PAGE   ),0}, // r2c1  L-Ctrl 
  {0,0}, // r2c2
  {(0x022A + kbdm_CONSUM_PAGE),0}, // r2c3  AC Bookmarks 
  {H31,0}, // r2c4     H31 
  {(0x004B + kbdm_KBD_PAGE   ),0}, // r2c5  PageUp 
  {(0x0031 + kbdm_KBD_PAGE   ),0}, // r2c6  Bkslash| 
  {(0x0051 + kbdm_KBD_PAGE   ),0}, // r2c7  DnArrow 
  {(0x0014 + kbdm_KBD_PAGE   ),0}, // r2c8       Q 
  {IDX(2),kbdm_FKEY}, // r2c9      -_ ----    F11 
  {IDX(3),kbdm_FKEY}, // r2c10      =+ ----    F12 
  {(0x000F + kbdm_KBD_PAGE   ),0}, // r2c11       L 
  {(0x0090 + kbdm_KBD_PAGE   ),0}, // r2c12  Hanguel 
}
{ // ROW 3
  {(0x00B6 + kbdm_CONSUM_PAGE),0}, // r3c0  Scan Previous Track 
  {(0x00E4 + kbdm_KBD_PAGE   ),0}, // r3c1  R-Ctrl 
  {0,0}, // r3c2
  {(0x029A + kbdm_CONSUM_PAGE),0}, // r3c3  AC Split 
  {H32,0}, // r3c4     H32 
  {IDX(4),kbdm_FKEY}, // r3c5      2@ ---- HID Button 261: Info Dialog 
  {(0x0005 + kbdm_KBD_PAGE   ),0}, // r3c6       B 
  {(0x0016 + kbdm_KBD_PAGE   ),0}, // r3c7       S 
  {(0x004F + kbdm_KBD_PAGE   ),0}, // r3c8  R-Arrow 
  {IDX(5),kbdm_FKEY}, // r3c9      3/ ---- HID Button 262: List Dialog 
  {IDX(6),kbdm_FKEY}, // r3c10      7& ----     F7 
  {(0x0037 + kbdm_KBD_PAGE   ),0}, // r3c11      .> 
  {(0x0088 + kbdm_KBD_PAGE   ),0}, // r3c12  KataHira 
}
{ // ROW 4
  {(0x00E9 + kbdm_CONSUM_PAGE),0}, // r4c0    Vol+ 
  {IDX(7),kbdm_FKEY}, // r4c1  HID Button 258: STB power on/off ---- Media Select Cable 
  {(0x00E2 + kbdm_KBD_PAGE   ),0}, // r4c2   L-Alt 
  {IDX(8),kbdm_FKEY}, // r4c3  HID Button 259: TV power on/off ---- Media Select TV 
  {H1,0}, // r4c4      H1 
  {(0x0018 + kbdm_KBD_PAGE   ),0}, // r4c5       U 
  {(0x0064 + kbdm_KBD_PAGE   ),0}, // r4c6   &lt;> 
  {(0x000A + kbdm_KBD_PAGE   ),0}, // r4c7       G 
  {(0x0007 + kbdm_KBD_PAGE   ),0}, // r4c8       D 
  {(0x0017 + kbdm_KBD_PAGE   ),0}, // r4c9       T 
  {(0x0015 + kbdm_KBD_PAGE   ),0}, // r4c10       R 
  {(0x002F + kbdm_KBD_PAGE   ),kbdm_GRAVE}, // r4c11      [{ 
  {(0x008B + kbdm_KBD_PAGE   ),0}, // r4c12  Muhenkan 
}
{ // ROW 5
  {(0x00EA + kbdm_CONSUM_PAGE),0}, // r5c0    Vol- 
  {(0x00B1 + kbdm_CONSUM_PAGE),0}, // r5c1   Pause 
  {(0x0040 + kbdm_CONSUM_PAGE),0}, // r5c2    Menu 
  {0,0}, // r5c3
  {IDX(9),kbdm_FKEY}, // r5c4      4$ ---- HID Button 263: User Dialog 
  {(0x000C + kbdm_KBD_PAGE   ),0}, // r5c5       I 
  {(0x001B + kbdm_KBD_PAGE   ),0}, // r5c6       X 
  {(0x0224 + kbdm_CONSUM_PAGE),0}, // r5c7  AC Back 
  {(0x0012 + kbdm_KBD_PAGE   ),0}, // r5c8       O 
  {(0x0009 + kbdm_KBD_PAGE   ),0}, // r5c9       F 
  {IDX(10),kbdm_FKEY}, // r5c10      8* ----     F8 
  {(0x002B + kbdm_KBD_PAGE   ),0}, // r5c11     Tab 
  {IDX(11),kbdm_FKEY}, // r5c12      6^ ----     F6 
}
{ // ROW 6
  {(0x00E2 + kbdm_CONSUM_PAGE),0}, // r6c0    Mute 
  {(0x00B0 + kbdm_CONSUM_PAGE),0}, // r6c1    Play 
  {0,0}, // r6c2
  {(0x0221 + kbdm_CONSUM_PAGE),0}, // r6c3  AC Search 
  {(0x0004 + kbdm_KBD_PAGE   ),0}, // r6c4       A 
  {(0x0223 + kbdm_CONSUM_PAGE),0}, // r6c5  AC Home 
  {(0x0039 + kbdm_KBD_PAGE   ),0}, // r6c6  CapsLck 
  {(0x0006 + kbdm_KBD_PAGE   ),0}, // r6c7       C 
  {(0x0038 + kbdm_KBD_PAGE   ),0}, // r6c8     / ? 
  {IDX(12),kbdm_FKEY}, // r6c9      0) ----    F10 
  {IDX(13),kbdm_FKEY}, // r6c10      9( ----     F9 
  {(0x0089 + kbdm_KBD_PAGE   ),0}, // r6c11      ¥| 
  {IDX(14),kbdm_FKEY}, // r6c12      1! ---- HID Button 260: Menu Dialog 
}
{ // ROW 7
  {0,0}, // r7c0
  {0,0}, // r7c1
  {0,0}, // r7c2
  {(0x001C + kbdm_KBD_PAGE   ),0}, // r7c3       Y 
  {(0x0050 + kbdm_KBD_PAGE   ),0}, // r7c4  L-Arrow 
  {IDX(15),kbdm_FKEY}, // r7c5  Bkspace ---- Delete 
  {(0x002C + kbdm_KBD_PAGE   ),0}, // r7c6   Space 
  {(0x004E + kbdm_KBD_PAGE   ),0}, // r7c7  PageDn 
  {(0x002C + kbdm_BUTTON_PAGE),kbdm_FN_KEY}, // r7c8    R-Fn 
  {(0x001D + kbdm_KBD_PAGE   ),0}, // r7c9       Z 
  {(0x0019 + kbdm_KBD_PAGE   ),0}, // r7c10       V 
  {(0x0032 + kbdm_KBD_PAGE   ),0}, // r7c11      $£ 
  {(0x008A + kbdm_KBD_PAGE   ),0}, // r7c12  Henkan 
}

kbdm_FnKeyOff[]
  {(0x0101 + kbdm_BUTTON_PAGE),0}, // r0c1  HID Button 257: AVR power on/off ---- Media Select VCR 
  {(0x0022 + kbdm_KBD_PAGE   ),0}, // r1c6      5% ----     F5 
  {(0x002D + kbdm_KBD_PAGE   ),0}, // r2c9      -_ ----    F11 
  {(0x002E + kbdm_KBD_PAGE   ),0}, // r2c10      =+ ----    F12 
  {(0x001F + kbdm_KBD_PAGE   ),0}, // r3c5      2@ ---- HID Button 261: Info Dialog 
  {(0x0020 + kbdm_KBD_PAGE   ),0}, // r3c9      3/ ---- HID Button 262: List Dialog 
  {(0x0024 + kbdm_KBD_PAGE   ),0}, // r3c10      7& ----     F7 
  {(0x0102 + kbdm_BUTTON_PAGE),0}, // r4c1  HID Button 258: STB power on/off ---- Media Select Cable 
  {(0x0103 + kbdm_BUTTON_PAGE),0}, // r4c3  HID Button 259: TV power on/off ---- Media Select TV 
  {(0x0021 + kbdm_KBD_PAGE   ),0}, // r5c4      4$ ---- HID Button 263: User Dialog 
  {(0x0025 + kbdm_KBD_PAGE   ),0}, // r5c10      8* ----     F8 
  {(0x0023 + kbdm_KBD_PAGE   ),0}, // r5c12      6^ ----     F6 
  {(0x0027 + kbdm_KBD_PAGE   ),0}, // r6c9      0) ----    F10 
  {(0x0026 + kbdm_KBD_PAGE   ),0}, // r6c10      9( ----     F9 
  {(0x001E + kbdm_KBD_PAGE   ),0}, // r6c12      1! ---- HID Button 260: Menu Dialog 
  {(0x002A + kbdm_KBD_PAGE   ),0}, // r7c5  Bkspace ---- Delete 

kbdm_FnKeyOn[]
  {(0x0092 + kbdm_CONSUM_PAGE),0}, // r0c1  Media Select VCR ---- HID Button 257: AVR power on/off 
  {(0x003E + kbdm_KBD_PAGE   ),0}, // r1c6      F5 ----     5% 
  {(0x0044 + kbdm_KBD_PAGE   ),0}, // r2c9     F11 ----     -_ 
  {(0x0045 + kbdm_KBD_PAGE   ),0}, // r2c10     F12 ----     =+ 
  {(0x0105 + kbdm_BUTTON_PAGE),0}, // r3c5  HID Button 261: Info Dialog ----     2@ 
  {(0x0106 + kbdm_BUTTON_PAGE),0}, // r3c9  HID Button 262: List Dialog ----     3/ 
  {(0x0040 + kbdm_KBD_PAGE   ),0}, // r3c10      F7 ----     7& 
  {(0x0097 + kbdm_CONSUM_PAGE),0}, // r4c1  Media Select Cable ---- HID Button 258: STB power on/off 
  {(0x0089 + kbdm_CONSUM_PAGE),0}, // r4c3  Media Select TV ---- HID Button 259: TV power on/off 
  {(0x0107 + kbdm_BUTTON_PAGE),0}, // r5c4  HID Button 263: User Dialog ----     4$ 
  {(0x0041 + kbdm_KBD_PAGE   ),0}, // r5c10      F8 ----     8* 
  {(0x003F + kbdm_KBD_PAGE   ),0}, // r5c12      F6 ----     6^ 
  {(0x0043 + kbdm_KBD_PAGE   ),0}, // r6c9     F10 ----     0) 
  {(0x0042 + kbdm_KBD_PAGE   ),0}, // r6c10      F9 ----     9( 
  {(0x0104 + kbdm_BUTTON_PAGE),0}, // r6c12  HID Button 260: Menu Dialog ----     1! 
  {(0x004C + kbdm_KBD_PAGE   ),0}, // r7c5  Delete ---- Bkspace 
