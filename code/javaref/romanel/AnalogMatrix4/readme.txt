
do analog matrix scanning with a new membrane for parrot
use a 6x6 matrix with 3 keys per quarto -> fail
use a 8x8 matrix with 2 keys per quarto -> fail
use a 6X10 matrix with 2 keys per quarto -> (10% 3keys) (6% 4 keys) discrimination achieved

execute jar file with java 1.6


Main parameters for tests:

* resistance can vary +-10% in same matrix (gaussian)
static final double RESISTANCE_RANDSPAN = 0.1; // 10% at 3 sigma

* ink resistance can vary from 1 to 5 resistance ratio (gaussian)
	double d_source_volt = DEFAULT_SOURCE_VOLT;
	static final double DEFAULT_SOURCE_VOLT = 1.0;
	static final double MAX_SOURCE_VOLT = 5.0;
	static final double SINK_VOLT = 0.0;

* precision current when simulating network of resistances
	static final double PRECISION_CURRENT = 0.000001;

* ADC definition
	static final double AD_INCREMENT=0.004; // increment current for discrimination
	static final int AD_BIT_RESOLUTION=7;

* max number of keys pressed at the same time
	static final int MAX_KEY_PRESSED = 4;

* number of runs when doing random simulation on resistance distributions accross tracks and batch (ink resistance)
	static final int MAXRAND=1000;



matrix file is parrot_matrix...csv
membrane file is parrotUS...csv
ADtable file is ADtable...csv
in ADtable AD are ADC levels used for discriminations
using REF to measure ink resistance and then multiply AD values
a quarto is a group of keys belonging to the same row and column


step 1 draw a matrix
step 2 measure tracks resistance's
step 3 find the best resistances values by optimizing it ("Search for best matrix")
step 4 on each quarto: run quartobatchrandom to get distribution when incertitude on resistances are +-10%
step 5 on each quarto: with the center of step 4 distribution compute ADC levels values
step 6 on whole matrix: run again quartobatchrandom against those values builiding a graph ADC accuracy vs resitance spread

===== are increment spaced 0.004
----- are actual changes between keys: recognizing F8 vs Esc
+++++ are the increment used to detect changes

_______SORTED_{COL0}__{ROW0}___[{Esc}, {F8}]_________________
    ADItem: C0R0[ [{Esc}]=  0.00400,  [{F8}]=  0.02400,  [{F8}, {Esc}]=  0.03600]
      []=  0.00000 =================
      [{Esc}]=  0.00400 +++++++++++++++++
      []=  0.00800 =================
      [{Esc}]=  0.01036 -----------------     1 alternance_delta_min=  0.01036
      [{Esc}, {L-Shift}, {,   <}]=  0.01135    [{Esc}]
      [{Esc}, {CapsLck}, {L}]=  0.01138    [{Esc}]
      [{Esc}, {Tab}, {P}]=  0.01143    [{Esc}]
      [{Esc}, {`   ~}, {0   )}]=  0.01146    [{Esc}]
      []=  0.01200 =================
      [{Esc}, {F6}, {PauseBrk}]=  0.01458    [{Esc}]
      [{Esc}, {F3}, {F11}]=  0.01465    [{Esc}]
      [{Esc}, {F1}, {F9}]=  0.01477    [{Esc}]
      [{Esc}, {F5}, {PrntScrn}]=  0.01483    [{Esc}]
      [{Esc}, {F2}, {F10}]=  0.01523    [{Esc}]
      [{Esc}, {F4}, {F12}]=  0.01554    [{Esc}]
      []=  0.01600 =================
      []=  0.02000 =================
      [{F8}]=  0.02400 +++++++++++++++++
      []=  0.02800 =================
      [{F8}]=  0.02958 -----------------     2
      [{F8}, {L-Shift}, {,   <}]=  0.03039    [{F8}]
      [{F8}, {CapsLck}, {L}]=  0.03042    [{F8}]
      [{F8}, {Tab}, {P}]=  0.03046    [{F8}]
      [{F8}, {F6}, {PauseBrk}]=  0.03046    [{F8}]
      [{F8}, {`   ~}, {0   )}]=  0.03048    [{F8}]
      [{F8}, {F5}, {PrntScrn}]=  0.03089    [{F8}]
      [{F8}, {F4}, {F12}]=  0.03157    [{F8}]
      [{F8}, {F3}, {F11}]=  0.03175    [{F8}]
      []=  0.03200 =================
      [{F8}, {F2}, {F10}]=  0.03249    [{F8}]
      [{F8}, {F1}, {F9}]=  0.03281    [{F8}]
      [{F8}, {Esc}]=  0.03600 +++++++++++++++++
      [{F8}, {Esc}]=  0.03993 -----------------     3 alternance_delta_min=  0.00713
      []=  0.04000 =================
 3 alternances alternance_delta_min=  0.00713
