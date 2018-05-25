package kbdmatrix
import scala.collection.immutable.SortedSet
import java.util.regex.Pattern
import scala.util.matching.Regex
import scala.collection.immutable.ListSet
import scala.collection.immutable.TreeSet
import kbdmatrix.KbdMatrix._
import kebra.MyLog._

object CheckEnglishLanguageMostCurrentCombinations2 {
	final val l_codes = (KeyDoc.m_keyref.filter((p:(Int, key)) => 
	List.fromString("Q,W,E,R,T,Z,U,I,O,P,A,S,D,F,G,H,J,K,L,Y,X,C,V,B,N,M",
	',').contains(p._2.s_key)).map((p:(Int, key)) => p._1)++List(41,61)).toList;
	//using "QWERTZUIOPASDFGHJKLYXCVBNM".toList will return List[Char] but we need List[String]

	val f_total = "29213800".toFloat
	val l_dico1=List("you","1222421","I","1052546","to","823661","the","770161","a","563578","and","480214","that","413389","it","388320","of","332038","me","312326","what","285826","is","282222","in","266544","this","249860","know","241548","I'm","230304","for","216535","no","212463","have","210523","my","210429","don't","206663","just","196810","not","195058","do","194183","be","191823","on","191646","your","188060","was","184170","we","181863","it's","176919","with","169586","so","169088","but","159109","all","158281","well","155201","are","152650","he","150009","oh","148780","about","142750","right","137218","you're","135738","get","126849","here","124368","out","121867","going","121435","like","121194","yeah","121148","if","120553","her","116265","she","110122","can","107652","up","103826","want","102822","think","102403","that's","102146","now","100909","go","100905","him","96536","at","93433","how","87908","got","86085","there","85662","one","84057","did","79053","why","78554","see","75968","come","72807","good","72798","they","69192","really","68311","as","67977","would","67718","look","67262","when","65117","time","64891","will","64811","okay","63509","back","63115","can't","62602","mean","61504","tell","61352","I'll","61169","from","59972","hey","58699","were","58127","he's","57551","could","57285","didn't","56732","yes","55828","his","55502","been","55235","or","55062","something","54736","who","53799","because","53566","some","53265","had","52561","then","52197","say","50213","ok","49968","take","49868","an","49832","way","49794","us","49492","little","47885","make","46154","need","46040","gonna","45124","never","44728","we're","43491","too","43360","love","42994","she's","42795","I've","42160","sure","40757","them","40366","more","40239","over","40001","our","39896","sorry","39854","where","39330","what's","39305","let","38858","thing","38121","am","37814","maybe","37653","down","37644","man","37355","has","37145","uh","36435","very","36140","by","35500","there's","35242","should","34829","anything","34635","said","34568","much","34270","any","34203","life","34192","even","33895","off","33887","please","33628","doing","32831","thank","31728","give","31073","only","30599","thought","30211","help","29825","two","29788","talk","28834","people","28517","god","28502","still","28308","wait","27920","into","27354","find","26757","nothing","26592","again","26472","things","26305","let's","26132","doesn't","25926","call","25880","told","25594","great","25435","before","25329","better","25282","ever","25127","night","24900","than","24815","away","24543","first","24509","believe","24210","other","23936","feel","23850","everything","23714","work","23683","you've","23483","fine","22993","home","22901","after","22182","last","21965","these","21929","day","21860","keep","21842","does","21731","put","21617","around","21591","stop","21341","they're","21309","I'd","21221","guy","21126","long","21086","isn't","20796","always","20581","listen","20142","wanted","20074","Mr","20053","guys","19953","huh","19871","those","19849","big","19520","lot","19421","happened","19282","thanks","19233","won't","19208","trying","19173","kind","19031","wrong","19025","through","18903","talking","18875","made","18872","new","18860","being","18666","guess","18473","hi","18285","care","18042","bad","17636","mom","17514","remember","17266","getting","17199","we'll","17077","together","17074","dad","17003","leave","16978","mother","16855","place","16796","understand","16724","wouldn't","16615","actually","16585","hear","16523","baby","16514","nice","16275","father","16233","else","16214","stay","16167","done","16010","wasn't","16007","their","15907","course","15866","might","15774","mind","15364","every","15350","enough","15322","try","15235","hell","15191","came","15142","someone","15035","you'll","14958","own","14849","family","14765","whole","14623","another","14541","house","14458","jack","14324","yourself","14291","idea","14203","ask","14194","best","14094","must","14089","coming","14082","old","14041","looking","13843","woman","13734","hello","13655","which","13652","years","13526","room","13467","money","13456","left","13428","knew","13357","tonight","13305","real","13161","son","13131","hope","13093","name","13044","same","13024","went","12892","um","12883","hmm","12819","happy","12788","pretty","12746","saw","12707","girl","12641","sir","12557","show","12392","friend","12245","already","12233","saying","12204","may","12199","next","12147","three","12088","job","12047","problem","11931","minute","11903","found","11875","world","11754","thinking","11721","haven't","11699","heard","11692","honey","11659","matter","11578","myself","11559","couldn't","11550","exactly","11502","having","11481","ah","11347","probably","11332","happen","11273","we've","11200","hurt","11145","boy","11050","both","11035","while","11006","dead","11002","gotta","11000","alone","10813","since","10757","excuse","10747","start","10704","kill","10676","hard","10608","you'd","10577","today","10561","car","10485","ready","10461","until","10432","without","10421","whatever","10363","wants","10269","hold","10254","wanna","10250","yet","10225","seen","10192","deal","10143","took","10083","once","9931","gone","9877","called","9871","morning","9823","supposed","9818","friends","9814","head","9794","stuff","9709","most","9699","used","9623","worry","9620","second","9605","part","9518","live","9507","truth","9470","school","9443","face","9441","forget","9322","true","9296","business","9295","each","9277","cause","9277","soon","9261","knows","9217","few","9164","telling","9129","wife","9125","who's","9109","use","9096","chance","9081","run","9076","move","9073","anyone","9050","person","8997","bye","8969","j","8942","somebody","8923","dr","8917","heart","8824","such","8813","miss","8806","married","8798","point","8707","later","8636","making","8623","meet","8608","anyway","8517","many","8504","phone","8424","reason","8395","damn","8367","lost","8348","looks","8337","bring","8320","case","8287","turn","8264","wish","8262","tomorrow","8254","kids","8247","trust","8187","check","8185","change","8176","end","8171","late","8164","anymore","8160","five","8137","least","8119","town","8099","aren't","8065","ha","8033","working","8016","year","8013","makes","8001","taking","7996","means","7980","brother","7952","play","7883","hate","7839","ago","7836","says","7821","beautiful","7813","gave","7759","fact","7654","crazy","7650","party","7633","sit","7625","open","7598","afraid","7587","between","7585","important","7582","rest","7417","fun","7404","kid","7380","word","7355","watch","7310","glad","7277","everyone","7214","days","7213","sister","7188","minutes","7185","everybody","7163","bit","7158","couple","7150","whoa","7081","either","7075","mrs","7064","feeling","7058","daughter","7057","wow","7036","gets","7023","asked","7009","under","6999","break","6941","promise","6926","door","6907","set","6878","close","6877","hand","6872","easy","6848","question","6841","doctor","6841","tried","6830","far","6728","walk","6619","needs","6555","trouble","6510","mine","6503","though","6502","times","6475","different","6464","killed","6390","hospital","6378","anybody","6376","sam","6354","alright","6348","wedding","6334","shut","6294","able","6289","die","6280","perfect","6276","police","6260","stand","6257","comes","6256","hit","6248","story","6231","ya","6212","mm","6192","waiting","6132","dinner","6099","against","6064","funny","6054","husband","6038","almost","6037","stupid","6020","pay","6016","answer","6016","four","6014","office","5955","cool","5954","eyes","5950","news","5936","child","5913","shouldn't","5874","half","5819","side","5813","yours","5805","moment","5770","sleep","5747","read","5743","where's","5711","started","5707","young","5680","men","5678","sounds","5665","sonny","5635","lucky","5631","pick","5610","sometimes","5596","em","5590","bed","5589","also","5583","date","5581","line","5564","plan","5557","hours","5503","lose","5459","fire","5452","free","5433","hands","5428","serious","5390","leo","5390","shit","5388","behind","5388","inside","5379","high","5377","ahead","5364","week","5356","wonderful","5341","t","5329","fight","5326","past","5315","cut","5305","quite","5304","number","5289","he'll","5262","sick","5256","s","5255","it'll","5217","game","5212","eat","5202","nobody","5195","goes","5194","death","5165","along","5147","save","5144","seems","5138","finally","5118","lives","5095","worried","5066","upset","5064","theresa","5056","carly","5055","ethan","5033","met","5032","book","5027","brought","5010","seem","5001","sort","5000","safe","4999","living","4999","children","4944","weren't","4896","leaving","4889","front","4868","shot","4866","loved","4851","asking","4851","running","4842","clear","4836","figure","4785","hot","4777","felt","4748","six","4737","parents","4725","drink","4706","absolutely","4704","how's","4652","daddy","4641","sweet","4628","alive","4628","paul","4616","sense","4597","meant","4596","happens","4589","david","4589","special","4573","bet","4548","blood","4542","ain't","4541","kidding","4539","lie","4536","full","4533","meeting","4532","dear","4516","coffee","4499","seeing","4495","sound","4488","fault","4484","water","4469","fuck","4453","ten","4448","women","4441","john","4413","welcome","4373","buy","4345","months","4333","hour","4325","speak","4322","lady","4301","jen","4301","thinks","4298","christmas","4284","body","4271","order","4270","outside","4260","hang","4243","possible","4228","worse","4218","company","4209","mistake","4185","ooh","4184","handle","4181","spend","4175","c","4166","totally","4157","giving","4143","control","4134","here's","4130","marriage","4129","realize","4114","d","4108","power","4094","president","4086","unless","4078","sex","4049","girls","4048","send","4045","needed","4043","o","4030","taken","3997","died","3996","scared","3991","picture","3984","talked","3975","jake","3962","al","3958","ass","3947","hundred","3942","changed","3929","completely","3919","explain","3913","playing","3899","certainly","3895","sign","3890","boys","3888","relationship","3880","michael","3878","loves","3876","fucking","3843","hair","3839","lying","3837","choice","3828","anywhere","3821","secret","3810","future","3798","weird","3797","luck","3792","she'll","3780","max","3773","luis","3773","turned","3770","known","3757","touch","3755","kiss","3750","crane","3744","questions","3741","obviously","3741","wonder","3735","pain","3720","calling","3714","somewhere","3707","throw","3697","straight","3695","grace","3693","cold","3690","white","3665","fast","3661","natalie","3660","words","3647","r","3636","food","3632","none","3615","drive","3607","feelings","3603","they'll","3593","worked","3577","marry","3564","light","3554","test","3547","drop","3546","cannot","3537","frank","3525","sent","3524","city","3515","dream","3471","protect","3465","twenty","3463","class","3458","lucy","3450","surprise","3439","its","3439","sweetheart","3434","forever","3432","poor","3431","looked","3431","mad","3407","except","3406","gun","3396","y'know","3395","dance","3394","takes","3386","appreciate","3379","especially","3376","situation","3359","besides","3355","weeks","3350","pull","3340","himself","3331","hasn't","3318","act","3307","worth","3303","Sheridan","3299","amazing","3294","top","3283","given","3283","expect","3277","ben","3262","rather","3241","Julian","3240","involved","3240","swear","3239","piece","3239","busy","3229","law","3225","decided","3224","black","3217","joey","3216","happening","3210","movie","3209","we'd","3205","catch","3194","antonio","3188","country","3187","less","3173","perhaps","3165","step","3161","fall","3157","watching","3152","kept","3152","darling","3146","dog","3141","ms","3131","win","3126","air","3126","honor","3106","personal","3103","moving","3102","till","3091","admit","3091","problems","3090","murder","3089","strong","3088","he'd","3087","evil","3078","definitely","3068","feels","3067","information","3063","honest","3063","eye","3063","broke","3044","missed","3043","longer","3043","dollars","3042","tired","3029","jason","3026","george","3013","evening","2998","human","2986","starting","2984","Ross","2981","red","2976","entire","2976","trip","2975","Brooke","2969","e","2964","club","2963","Niles","2956","suppose","2952","calm","2948","imagine","2946","todd","2943","fair","2941","caught","2933","b","2928","blame","2923","street","2920","sitting","2920","favor","2920","apartment","2920","court","2919","terrible","2914","clean","2914","tony","2913","learn","2912","Alison","2910","Rick","2909","works","2903","Rose","2890","Frasier","2887","relax","2877","york","2873","million","2873","charity","2865","accident","2860","wake","2857","prove","2852","danny","2848","smart","2847","message","2846","missing","2845","forgot","2845","small","2843","interested","2838","table","2835","nbsp","2824","become","2816","craig","2810","mouth","2807","pregnant","2805","middle","2802","billy","2797","ring","2786","careful","2785","shall","2779","dude","2775","team","2773","ride","2768","figured","2766","wear","2765","shoot","2759","stick","2753","ray","2752","follow","2752","bo","2751","angry","2747","instead","2744","buddy","2743","write","2741","stopped","2719","early","2719","angel","2701","nick","2699","ran","2692","war","2690","standing","2690","forgive","2690","jail","2684","wearing","2678","Miguel","2678","ladies","2670","kinda","2662","lunch","2660","Cristian","2659","eight","2655","Greenlee","2654","gotten","2640","hoping","2639","phoebe","2638","thousand","2633","ridge","2633","music","2631","luke","2614","paper","2607","tough","2606","tape","2605","Emily","2595","state","2592","count","2589","college","2589","boyfriend","2589","proud","2586","agree","2584","birthday","2580","bill","2572","seven","2569","they've","2566","Timmy","2565","history","2564","share","2563","offer","2563","hurry","2559","ow","2557","feet","2555","wondering","2549","simple","2549","decision","2548","building","2546","ones","2539","finish","2535","voice","2530","herself","2530","Chris","2529","would've","2523","list","2518","Kay","2517","mess","2516","deserve","2516","evidence","2514","cute","2504","Jerry","2503","dress","2501","Richard","2499","interesting","2497","Jesus","2487","James","2478","hotel","2463","enjoy","2463","Ryan","2461","Lindsay","2461","quiet","2454","concerned","2452","road","2448","eve","2445","staying","2443","short","2440","m","2440","beat","2440","sweetie","2439","mention","2434","clothes","2434","finished","2423","fell","2419","neither","2417","mmm","2414","fix","2414","victor","2412","respect","2410","spent","2406","prison","2401","attention","2397","holding","2394","calls","2389","near","2386","surprised","2381","bar","2376","beth","2375","pass","2374","keeping","2371","gift","2371","hadn't","2369","putting","2366","dark","2361","self","2358","owe","2354","using","2345","nora","2345","ice","2345","helping","2342","bitch","2341","normal","2339","aunt","2339","lawyer","2338","apart","2336","certain","2333","plans","2327","Jax","2326","girlfriend","2325","floor","2325","whether","2316","everything's","2316","present","2313","earth","2312","private","2311","Jessica","2310","box","2304","Dawson","2303","cover","2298","judge","2294","upstairs","2293","Alexis","2287","Shawn","2281","sake","2281","mommy","2281","possibly","2280","worst","2276")
	// val l_dico1=List("you","1222421","I","1052546","to","823661","the","770161","a","563578","and","480214","that","413389","right","77777")
	val it_dico2 = l_dico1.grouped(2)
	var hm_dico3 = scala.collection.mutable.HashMap.empty[String, Float];
	while (it_dico2.hasNext) {
		var l = it_dico2.next
		hm_dico3 += " "+l(0).toUpperCase+" " -> getNextFloat(l(1))
	}
	var hm_dico4 =scala.collection.mutable.HashMap.empty[String, Float];
	//	println("\nhm_dico3:"+hm_dico3)
	hm_dico3.foreach((m: (String, Float)) => update(m))
	//	println("\nhm_dico4:"+hm_dico4)
	var hm_dico5 =scala.collection.mutable.HashMap.empty[String, Float];
	hm_dico4.foreach((m: (String, Float)) => update5(m))




	def isValid(i: Int): Boolean = {
		l_codes.contains(i)
	}
	def getNextFloat(s: String): Float = {
		val m = Pattern.compile("(\\d+)").matcher(s)
		if (m.find) { 
			m.group(1).toFloat
		} else {
			0
		}
	}
	def getTriplets(s: String): ListSet[String] = {
		var ls = ListSet.empty[String];
		val it_s = s.sliding(3)
		while(it_s.hasNext) {
			ls += it_s.next
		}
		ls
	}
	def getMasterTriplet(s: String): String = {
		assert(s.size==3)
		val ls =  permutations(s.toList)
		//		println("    gtm ls["+s+"]:"+ls)
		var ts = new TreeSet[String]()(new CompareString())
		ts ++= ls
		//		println("    gtm ts["+s+"]:["+ts.head+"] "+ts)
		ts.head
	}
	def update(m: (String, Float)) {
		val ls = getTriplets(m._1)
		//		println("  *m._1["+m._1+"]")
		ls.foreach((s: String) => { 
			val f = hm_dico4.getOrElse(s, "0".toFloat)
			//		println("    *s["+s+"] f"+f)
			hm_dico4 += s -> (f+m._2)
			//	println("    hm_dico4:"+hm_dico4)
		})		
	}

	def update5(m: (String, Float)) {
		val mt = getMasterTriplet(m._1)
		val f = hm_dico5.getOrElse(mt, "0".toFloat)
		hm_dico5 += mt -> (f+m._2)
	}

	def permutations2[T](xs: List[T]): List[List[T]] = xs match {
	case Nil => List(Nil)
	case _   => for(x <- xs;ys <- permutations2(xs diff List(x))) yield x::ys
	}

	def permutations[T](xs: List[T]): ListSet[String] = {
			val l = permutations2(xs)
			var l_out = ListSet.empty[String];
			l.foreach((zl: List[T]) => l_out += (zl.tail.foldLeft(zl.head.toString)(_ + _.toString)))
			l_out
	}
}

class CheckEnglishLanguageMostCurrentCombinations2(val i_ghost: Int, ts_quartets: TreeSet[Quartet]) {
	// check english language triplets occurences
	val ts_q1 = ts_quartets.filter(_.l_quartet.intersect(CheckEnglishLanguageMostCurrentCombinations2.l_codes).size>2)	
	var ts_strings1 = TreeSet[String]();
	ts_q1.foreach(ts_strings1 ++= _.getAllStrings())
	val ts_strings = ts_strings1.map((s: String) => CheckEnglishLanguageMostCurrentCombinations2.getMasterTriplet(s))
	L.myPrintln("*ts_strings*"+ts_strings.size+"**"+ts_strings)
	L.myPrintln("*f_total*"+CheckEnglishLanguageMostCurrentCombinations2.f_total+
			"*hm_dico3*"+CheckEnglishLanguageMostCurrentCombinations2.hm_dico3.mkString("\n  3 ","\n  3 ",""));
	var ts_occurrences = TreeSet[(String, Float)]()(new CompareOccurence())
	ts_strings.foreach((s: String) => ts_occurrences += ((s, getOccurrence(s))))
	L.myHErrPrintln("\n</pre><tr><td bgcolor=\""+KbdMatrix.bgColor(i_ghost+30)+"\">");
	if(ts_occurrences.until(("",0.5f)).isEmpty) {
		L.myHErrPrintln(ts_occurrences.until(("",0.02f)).mkString("Natural English language triplet Occurence<pre>\n(triplet,Occurence in %)\n  ","\n  ","\n"))
	} else {
		L.myPrintln(ts_occurrences.until(("",0.02f)).mkString("Natural English language triplet Occurence\n(triplet,Occurence in %)\n  ","\n  ","\n"))
		L.myHErrPrintln(ts_occurrences.until(("",0.5f)).mkString("Natural English language triplet Occurence<pre>\n(triplet,Occurence in %)\n  ","\n  ","\n"))
	}
	L.myHErrPrintln("\n</pre></td></tr>");

	// check some specific gaming triplets
	var l_combiASDF = ListSet.empty[String];
	Quartet.combinations(3, List.fromString("A,S,D,F",',')).foreach((l: List[String]) => l_combiASDF += l.reduceLeft(_ + _))
	val l_specificGaming1 = (ListSet[String]("WAS","ASX","GHY","JKL","ERF")++l_combiASDF).map((s: String) => CheckEnglishLanguageMostCurrentCombinations2.getMasterTriplet(s))
	checkSpecificGamingTriplet(ts_strings, l_specificGaming1, "MUST (Tier1)", i_ghost)

	var l_combiQWEASD = ListSet.empty[String];
	Quartet.combinations(3, List.fromString("Q,W,E,A,S,D",',')).foreach((l: List[String]) => l_combiQWEASD += l.reduceLeft(_ + _))
	var l_combiWERSDF = ListSet.empty[String];
	Quartet.combinations(3, List.fromString("W,E,R,S,D,F",',')).foreach((l: List[String]) => l_combiWERSDF += l.reduceLeft(_ + _))
	val l_specificGaming2 = (ListSet[String]("CXI","XVI")++l_combiQWEASD++l_combiWERSDF).map((s: String) => CheckEnglishLanguageMostCurrentCombinations2.getMasterTriplet(s))
	checkSpecificGamingTriplet(ts_strings, l_specificGaming2, "Should (Tier2)", i_ghost)

	def getOccurrence(s: String): Float = {
		L.myPrintln("["+s+"]")
		val f_sum = CheckEnglishLanguageMostCurrentCombinations2.hm_dico5.getOrElse(s, "0".toFloat);
		L.myPrintln("  sum["+s+"]="+f_sum+" "+(f_sum*100/CheckEnglishLanguageMostCurrentCombinations2.f_total))
		f_sum*100/CheckEnglishLanguageMostCurrentCombinations2.f_total
	}

	def list2String(l: List[String]): String = {
		//	 l.tail.foldLeft(l.head.toString)(_ + _.toString)
		l.reduceLeft(_ + _)
	}
	
	def checkSpecificGamingTriplet(ts: SortedSet[String], ls: ListSet[String], s_mustShould: String, i_ghost: Int) {
		L.myHErrPrintln("\n<tr><td bgcolor=\""+KbdMatrix.bgColor(i_ghost+30)+"\">");
		L.myHErrPrintln("\nCheck that some specific gaming triplets {"+ls+"} "+s_mustShould+" be allowed<pre>");
		L.myHErrPrintln(ts.intersect(ls).toString)
		L.myHErrPrintln("\n</pre></td></tr>");	
	}
}
class CompareString extends Ordering[String] {
	def compare(s1: String, s2: String) = s1.compare(s2)
}

