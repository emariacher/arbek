/*
$Header: AnalyzeMatrix.java: Revision: 48: Author: emariacher: Date: Friday, July 10, 2009 2:27:20 PM$

$Log$
emariacher - Friday, July 10, 2009 2:27:20 PM
improved logging for building ghost key detection algorythm
emariacher - Thursday, July 09, 2009 2:22:07 PM
list all ghost keys seems OK.
emariacher - Wednesday, July 08, 2009 6:33:13 PM
get all ghost keys. not debugged yet.
emariacher - Wednesday, July 08, 2009 3:46:31 PM
emariacher - Wednesday, July 08, 2009 3:17:34 PM
ad_items are also ad_increments
emariacher - Wednesday, July 08, 2009 3:04:00 PM
just some cosmetic when scanning 3 others keys
emariacher - Wednesday, July 08, 2009 2:16:41 PM
do some stats when pressing all the 3 other keys (in a ghost square).
emariacher - Tuesday, July 07, 2009 5:39:50 PM
real matrix scan seems to be somewhat working...
emariacher - Tuesday, July 07, 2009 5:08:11 PM
matrix scan: pas encore au point.
emariacher - Friday, July 03, 2009 5:41:28 PM
some exception handling
emariacher - Friday, July 03, 2009 2:45:55 PM
regularly re-initialize log file.
emariacher - Thursday, July 02, 2009 6:00:01 PM
check actual vs computed.
emariacher - Wednesday, July 01, 2009 5:16:11 PM
1st AD table is now at 90% of 1st ADlevel when generating ADtable.
emariacher - Wednesday, July 01, 2009 4:07:44 PM
static ghost key detection with "analyze key pressed" button
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;


public class AnalyzeMatrix {
	static final int MAX_KEY_PRESSED = 4;

	static final int STRETCHEDGES = 2;
	static final int MAXRAND=1000;

	static final int RUNALLQUARTOBATCH = 1;
	static final int RUNALLQUARTORANDOMBATCH = 3;
	static final int OPTIMIZE = 4;
	int i_createCSVFile=RUNALLQUARTOBATCH;

	static final double AD_INCREMENT=0.004; // increment current for discrimination
	static final int AD_BIT_RESOLUTION=7;
	ArrayList<Result> l_ad_increment = new ArrayList<Result>();
	Log L;

	GraphPanel graphPanel;
	AnalogMatrix1 main;
	KbdMatrix km;


	private Log.MyFile csvfile_runAllQuartoBatch;
	private Log.MyFile csvfile_ADtable;
	private Log.MyFile csvfile_ghostKeys;

	ElectricalSimulation elecsimu;

	boolean b_doItFast=true;
	Key k_startCol, k_endRow;

	KeyCombinations kc;

	TreeMap<ColRow, ADitem> tm_ADtable;
	TreeSet<Ghost> ts_ghostKeys = new TreeSet<Ghost>();

	public AnalyzeMatrix(Log l, GraphPanel graphPanel, boolean b_doItFast, int i_createCSVFile) throws IOException, Exception {
		L = l;
		this.graphPanel = graphPanel;
		this.main=graphPanel.main;
		this.km=graphPanel.km;
		this.b_doItFast=b_doItFast;
		this.i_createCSVFile=i_createCSVFile;
		elecsimu = new ElectricalSimulation(l, this);
		if((i_createCSVFile>=RUNALLQUARTOBATCH)) {
			if(main.f_MembraneFile!=null) {
				csvfile_runAllQuartoBatch = L.getMyFile(main.f_MembraneFile.getCanonicalPath() + 
						MAX_KEY_PRESSED + "_out.csv");
				L.files.add(csvfile_runAllQuartoBatch);
				csvfile_runAllQuartoBatch.writeFile("\""+main.f_MembraneFile.getCanonicalPath()+"\",\"");
			} else {
				csvfile_runAllQuartoBatch = L.getMyFile(main.f_MainMatrixFile.getCanonicalPath() + 
						MAX_KEY_PRESSED + "_out.csv");
				L.files.add(csvfile_runAllQuartoBatch);
			}
			csvfile_runAllQuartoBatch.writeFile(main.f_MainMatrixFile.getCanonicalPath()+"\"\n");
			csvfile_runAllQuartoBatch.writeFile("\""+MAX_KEY_PRESSED +
					" max key pressed\",\"Cut inactive columns: "+Key.CUT_INACTIVE_COLUMNS +"\"\n");
			csvfile_runAllQuartoBatch.writeFile("\"random_seed\",\"source_volt\",\"startCol_endRow\"," +
					"\"keys_in_quarto\",\"alternances\"," +
			"\"delta_min_when_alterning\",\"min\",\"max\",\"delta/max\"\n");
		}
		csvfile_ADtable = L.getMyFile(main.f_MembraneFile.getCanonicalPath() + "_ADtable.csv");
		L.files.add(csvfile_ADtable);
		csvfile_ADtable.writeFile("\""+main.f_MembraneFile.getCanonicalPath()+"\",\"");
		csvfile_ADtable.writeFile(main.f_MainMatrixFile.getCanonicalPath()+"\"\n");
		csvfile_ADtable.writeFile("\"random_seed\",\"type\",\"startCol\",\"endRow\",\"keycodes\"," +
		"\"reference_current\",\"result_level\"\n");

		k_startCol = graphPanel.k_startCol;
		k_endRow   = graphPanel.k_endRow;
		kc = new KeyCombinations(graphPanel);
	}

	public ArrayList<Key> getActiveRows(ArrayList<Key> l_keys) {
		ArrayList<Key> l_rows= new ArrayList<Key>();
		for(Key k: l_keys) {
			if(k.isRow()) {
				l_rows.add(k);
			}
		}
		return l_rows;
	}

	public ArrayList<Key> getActiveCols(ArrayList<Key> l_keys) {
		ArrayList<Key> l_cols= new ArrayList<Key>();
		for(Key k: l_keys) {
			if(k.isCol()) {
				l_cols.add(k);
			} else {
				Key kc = k.findCol();
				if(!l_cols.contains(kc)) {
					l_cols.add(kc);
				}
			}
		}
		return l_cols;
	}

	public void runQuartoRandomBatch() throws Exception {
		csvfile_runAllQuartoBatch.writeFile(String.format("\"Resistance Ramdom span: %1$6.2f%%\"\n",
				GraphPanel.RESISTANCE_RANDSPAN*100));
		csvfile_runAllQuartoBatch.writeFile(String.format("\"Voltage Ramdom span: %1$6.2fV to %2$6.2fV\"\n",
				Key.DEFAULT_SOURCE_VOLT, Key.MAX_SOURCE_VOLT));
		csvfile_runAllQuartoBatch.writeFile("\"Reference: \"\n");
		runQuartoBatch(0);
		csvfile_runAllQuartoBatch.writeFile("\"Random Variations: \"\n");
		for(int i_count=1;i_count<MAXRAND;i_count++) {	
			main.jlblQuartoBatchRandom.setText(i_count+"/"+MAXRAND);
			runQuartoBatch(i_count);
		}
	}

	public void runAllQuartoRandomBatch() throws Exception {
		csvfile_runAllQuartoBatch.writeFile(String.format("\"Resistance Ramdom span: %1$6.2f%%\"\n",
				GraphPanel.RESISTANCE_RANDSPAN*100));
		csvfile_runAllQuartoBatch.writeFile(String.format("\"Voltage Ramdom span: %1$6.2fV to %2$6.2fV\"\n",
				Key.DEFAULT_SOURCE_VOLT, Key.MAX_SOURCE_VOLT));
		csvfile_runAllQuartoBatch.writeFile("\"Reference: \"\n");
		Result r_delta_base = runAllQuartoBatch(0);
		csvfile_runAllQuartoBatch.writeFile("\"Random Variations: \"\n");	
		Result r_delta_best = r_delta_base;
		for(int i_seed=1;i_seed<MAXRAND;i_seed++) {	
			Result r_delta = runAllQuartoBatch(i_seed);
			r_delta_best=r_delta_best.max_percent(r_delta, i_seed);
			main.jlblQuartoBatchRandom.setText(i_seed+"/"+MAXRAND+ " " +
					r_delta_base.printPerCent()+"/"+r_delta.printPerCent()+"/"+r_delta_best.printPerCentSeed());
			try {
				L.logfile.reinit();
			} catch(Exception e) {
				System.err.println(e);
				e.printStackTrace();
				System.err.println("Going on!");
			}
		}
	}


	public Result runAllQuartoBatch(int i_seed) throws Exception {
		if(i_seed!=0) {
			graphPanel.randomizeResistances(i_seed);
			if(!km.tm_Reftable.isEmpty()) {
				// compare currents on reference keys combinations from ref table
				double d_ratio_mean = computeRatio();
				L.myErrPrintln(String.format("[%1$d]>>>>>>>>d_ratio_mean= %2$9.5f<<<<<<<<<<<<<<<",
						i_seed,d_ratio_mean));
				// apply ratio on AD table
				tm_ADtable = applyRatio(km.tm_ADtable, d_ratio_mean);
			}

		} else {
			tm_ADtable = km.tm_ADtable;
		}
		// find nearest values for AD table
		tm_ADtable = normalizeADtable(tm_ADtable);
		Result r_delta_min = new Result(0.0,Double.MAX_VALUE);
		Result r_delta_max = new Result(0.0);
		int i_maxrow = km.getMaxRow()/2;
		int i_maxcol = km.getMaxCol()/2;
		for(int i_row=0;i_row<i_maxrow;i_row++) {
			setK_endRow(kc.findRow(i_row));
			for(int i_col=0;i_col<i_maxcol;i_col++) {
				setK_startCol(kc.findCol(i_col));
				main.jlblQuartoBatch.setText(k_startCol.node+", "+k_endRow.node);
				Result r_delta = runQuartoBatch(0);
				r_delta_min=r_delta_min.min_delta(r_delta);
				r_delta_max=r_delta_max.max_max(r_delta);
			}			 
		}
		r_delta_min.d_max=r_delta_max.d_max;
		if(i_createCSVFile>=RUNALLQUARTOBATCH) {
			csvfile_runAllQuartoBatch.writeFile("\""+i_seed+"\",\""+
					String.format("%1$6.3f",k_startCol.d_source_volt)+
					"\","+r_delta_min.toStringCsv()+"\n");		 
		}
		L.myErrPrintln("<<<<"+r_delta_min.toStringDelta()+
				String.format(" max=%1$9.5f",r_delta_min.d_max)+">>>>");
		return r_delta_min;
	}

	public Result runQuartoBatch(int i_seed) throws Exception {
		Result r_delta;
		double d_ratio_mean=0.0;
		if(i_seed!=0) {
			graphPanel.randomizeResistances(i_seed);
			if(!km.tm_Reftable.isEmpty()) {
				// compare currents on reference keys combinations from ref table
				d_ratio_mean = computeRatio();
				// apply ratio on AD table
				tm_ADtable = applyRatio(km.tm_ADtable, d_ratio_mean);
			}
		} else {
			tm_ADtable = km.tm_ADtable;
		}
		// find nearest values for AD table
		tm_ADtable = normalizeADtable(tm_ADtable);
		Result r_delta_allmin = new Result(0.0,Double.MAX_VALUE);
		if(k_endRow==null) {
			ArrayList<Key> l_keys1 = elecsimu.simplifyNetwork(k_startCol);
			ArrayList<Key> l_activeRows=getActiveRows(l_keys1);
			setK_endRow(l_activeRows.get(0));
		}
		L.myErrPrintln("*runQuartoBatch("+k_startCol.node+", "+k_endRow.node+
				") i_seed="+i_seed+String.format("[%1$6.3f]",d_ratio_mean)+
		" *********************************************************************");
		ArrayList<Key> l_keysQuarto = kc.getQuartoKeys(k_startCol, k_endRow);
		if(!l_keysQuarto.isEmpty()) {
			ArrayList<Result> l_keysCombination2PressMaster = kc.getKeysCombination2Press(l_keysQuarto);
			ArrayList<Result> l_keysCombination2PressSlaves = new ArrayList<Result>();
			if(b_doItFast) {
				processCombinationFast(l_keysCombination2PressMaster);
			} else {
				processCombination(l_keysCombination2PressMaster, false);
			}
			Result r_delta_min = printStatsMaster(l_keysCombination2PressMaster);
			Result r_delta_max = new Result(0.0);
			for(Result l_quartoKeys2Press : l_keysCombination2PressMaster) {
				ArrayList<Result> l_KeysCombination2Press =
					kc.getKeysNonQuartoCombination2Press(l_quartoKeys2Press);
				if(l_KeysCombination2Press.size()>1) {
					l_keysCombination2PressSlaves.addAll(l_KeysCombination2Press);
					L.myErrPrintln("    *runQuartoBatch("+l_quartoKeys2Press+")**");
					if(b_doItFast) {
						processCombinationFast(l_KeysCombination2Press);
					} else {
						processCombination(l_KeysCombination2Press, false);
					}
					r_delta = printStatsNonQuarto(l_KeysCombination2Press);
					if(r_delta.d_res==null) {
						throw new Exception("r_delta.d_res==null");
					}
					if(r_delta_max.d_res==null) {
						throw new Exception("r_delta_max.d_res==null");
					}
					r_delta_max=r_delta_min.max_res(r_delta);
					graphPanel.relax();
					Thread.sleep(1);
				}
			}

			// 4 keys forming a square like the "old ghost keys"
			if(MAX_KEY_PRESSED>=4) {
				ArrayList<Result> l_keysCombinationNonSameRowCol =
					km.getNonSameRowColKeys(l_keysCombination2PressMaster);
				if(!l_keysCombinationNonSameRowCol.isEmpty()) {
					l_keysCombination2PressSlaves.addAll(l_keysCombinationNonSameRowCol );
					if(b_doItFast) {
						processCombinationFast(l_keysCombinationNonSameRowCol);
					} else {
						processCombination(l_keysCombinationNonSameRowCol, false);
					}
					r_delta = printStatsNonQuarto(l_keysCombinationNonSameRowCol);
					if(r_delta.d_res==null) {
						throw new Exception("r_delta.d_res==null");
					}
					if(r_delta_max.d_res==null) {
						throw new Exception("r_delta_max.d_res==null");
					}
					r_delta_max=r_delta_min.max_res(r_delta);
				}
			}
			L.myErrPrintln("***runQuartoBatch("+k_startCol.node+", "+k_endRow.node+") "+
					"delta_min("+r_delta_min.toStringDelta()+") delta_max("+r_delta_max.toStringDelta()+
					")* Test only "+MAX_KEY_PRESSED+" keys pressed at the same time");
			//			if(b_createCSVFile) {
			//				csvfile_runAllQuartoBatch.writeFile("\""+k_startCol.node+"_"+k_endRow.node+"\""+
			//						String.format(",\"%1$9.5f\",\"%2$9.5f\"",r_delta_min.d_delta,r_delta_max.d_delta)+"\n");
			//			}
			r_delta_allmin = sortCurrents(l_keysCombination2PressMaster, l_keysCombination2PressSlaves, i_seed, false);
			r_delta_allmin.update(k_startCol, k_endRow);
		}
		return r_delta_allmin;
	}

	public Result runQuarto3OtherKeysBatch(int i_seed) throws Exception {
		//		Result r_delta;
		double d_ratio_mean=0.0;
		if(i_seed!=0) {
			graphPanel.randomizeResistances(i_seed);
			if(!km.tm_Reftable.isEmpty()) {
				// compare currents on reference keys combinations from ref table
				d_ratio_mean = computeRatio();
				// apply ratio on AD table
				tm_ADtable = applyRatio(km.tm_ADtable, d_ratio_mean);
			}
		} else {
			tm_ADtable = km.tm_ADtable;
		}
		// find nearest values for AD table
		tm_ADtable = normalizeADtable(tm_ADtable);
		Result r_delta_allmin = new Result(0.0,Double.MAX_VALUE);
		L.myErrPrintln("*runQuarto3OtherKeysBatch("+k_startCol.node+", "+k_endRow.node+
				") i_seed="+i_seed+String.format("[%1$6.3f]",d_ratio_mean)+
		" *********************************************************************");
		ColRow cr = new ColRow(k_startCol,k_endRow);
		ADitem adi = tm_ADtable.get(cr);
		ArrayList<Key> l_keysQuarto = kc.getQuartoKeys(k_startCol, k_endRow);
		if(!l_keysQuarto.isEmpty()) {
			L.myErrPrintln(l_keysQuarto+"\n"+adi.print_levels());
			Result r_delta_min = new Result(0.0);
			Result r_delta_max = new Result(0.0);

			ArrayList<Result> l_keysCombination2PressSlaves = new ArrayList<Result>();

			// 3 other keys forming a square like the "old ghost keys"
			if(MAX_KEY_PRESSED>=4) {
				ArrayList<Result> l_keysCombinationNonSameRowCol =
					km.get3otherKeys(l_keysQuarto);
				if(!l_keysCombinationNonSameRowCol.isEmpty()) {
					l_keysCombination2PressSlaves.addAll(l_keysCombinationNonSameRowCol );
					processCombination(l_keysCombinationNonSameRowCol, true);
					//					r_delta = printStatsNonQuarto(l_keysCombinationNonSameRowCol);
					//					if(r_delta.d_res==null) {
					//						throw new Exception("r_delta.d_res==null");
					//					}
					//					if(r_delta_max.d_res==null) {
					//						throw new Exception("r_delta_max.d_res==null");
					//					}
					//					r_delta_max=r_delta_min.max_res(r_delta);
				}
				ArrayList<Result> l_keysCombination2PressMaster = kc.getKeysCombination2Press(l_keysQuarto);
				l_keysCombinationNonSameRowCol =
					km.getNonSameRowColKeys(l_keysCombination2PressMaster);
				if(!l_keysCombinationNonSameRowCol.isEmpty()) {
					l_keysCombination2PressSlaves.addAll(l_keysCombinationNonSameRowCol );
					if(b_doItFast) {
						processCombinationFast(l_keysCombinationNonSameRowCol);
					} else {
						processCombination(l_keysCombinationNonSameRowCol, false);
					}
					//					r_delta = printStatsNonQuarto(l_keysCombinationNonSameRowCol);
					//					if(r_delta.d_res==null) {
					//						throw new Exception("r_delta.d_res==null");
					//					}
					//					if(r_delta_max.d_res==null) {
					//						throw new Exception("r_delta_max.d_res==null");
					//					}
					//					r_delta_max=r_delta_min.max_res(r_delta);
				}

			}
			L.myErrPrintln("***runQuarto3OtherKeysBatch("+k_startCol.node+", "+k_endRow.node+") "+
					"delta_min("+r_delta_min.toStringDelta()+") delta_max("+r_delta_max.toStringDelta()+
					")* Test only "+MAX_KEY_PRESSED+" keys pressed at the same time");
			//			if(b_createCSVFile) {
			//				csvfile_runAllQuartoBatch.writeFile("\""+k_startCol.node+"_"+k_endRow.node+"\""+
			//						String.format(",\"%1$9.5f\",\"%2$9.5f\"",r_delta_min.d_delta,r_delta_max.d_delta)+"\n");
			//			}
			r_delta_allmin = sortCurrents(null, l_keysCombination2PressSlaves, i_seed, true);
			r_delta_allmin.update(k_startCol, k_endRow);
		}
		return r_delta_allmin;
	}

	TreeMap<ColRow, ADitem> normalizeADtable(
			TreeMap<ColRow, ADitem> tm_ADtable2) throws Exception {
		// find nearest values for AD table
		for(ADitem adi : tm_ADtable2.values()) {
			adi.normalizeNearest();
		}
		return tm_ADtable2;
	}

	private TreeMap<ColRow, ADitem> applyRatio(
			TreeMap<ColRow, ADitem> tm_ADtable2, double d_ratio_mean) {
		// apply ratio on AD table
		for(ADitem adi : tm_ADtable2.values()) {
			adi.updateRatio(d_ratio_mean);
		}
		return tm_ADtable2;
	}

	private double computeRatio() throws Exception {
		// compare currents on reference keys combinations from ref table
		double d_ratio_mean = 0.0;
		int i_count=0;

		for(ADitem adi : km.tm_Reftable.values()) {
			for (Result r : adi.ts_ADlevels) {
				setK_startCol(adi.cr.i_col);
				setK_endRow(adi.cr.i_row);
				Result r2 = processKeyCombinationFast(r, true);
				r.d_ratio = r2.d_res / r.d_ref_current;
				d_ratio_mean = ((d_ratio_mean*i_count) + r.d_ratio) / (i_count+1);
				L.myErrPrintln("////"+r+", "+r2+" "+
						String.format("[%1$6.3fV] now=%2$6.3f mean=%3$6.3f",
								k_startCol.d_source_volt, r.d_ratio, d_ratio_mean));
				i_count++;
			}
		}
		return d_ratio_mean;
	}

	private Result sortCurrents(ArrayList<Result> combination2PressMaster,
			ArrayList<Result> combination2PressSlaves, int i_seed, boolean b_justLog) throws Exception {
		// TODO just 4 navigation
		boolean b_ADItemFound=false;
		TreeSet<Result> ts_MSvalues = new TreeSet<Result>();
		if(combination2PressMaster!=null) {
			ts_MSvalues.addAll(combination2PressMaster);
		}
		ts_MSvalues.addAll(combination2PressSlaves);

		TreeSet<Result> ts_allResults=new TreeSet<Result>();
		ts_allResults.addAll(ts_MSvalues);
		L.myErrPrintln("_______SORTED_"+k_startCol.node+
				"__"+k_endRow.node+"___"+
				kc.getQuartoKeys(k_startCol, k_endRow)+"_________________");
		if(!km.tm_ADtable.isEmpty()) {
			ColRow cr = new ColRow(k_startCol, k_endRow);
			ADitem adi = km.tm_ADtable.get(cr);
			if(adi!=null) { // means calibration has to be done for this quarto at manufacturing time
				ts_allResults.addAll(adi.ts_ADlevels);
				b_ADItemFound=true;
				L.myErrPrintln("    "+adi.toString());
			} else {
				L.myErrPrintln(">>>>>>CALIBRATION NOT CHECKED FOR"+k_startCol.node+
						"__"+k_endRow.node+"!<<<<<<");	
				main.jlblQuartoBatch.setText(k_startCol.node+", "+k_endRow.node+" needs manuf calibration");
			}
		}
		initializeADincs();
		ts_allResults.addAll(l_ad_increment);
		Result r_alternances = new Result(0.0);
		int i_alternances=0;
		Result r_previous = new Result(0.0, Double.MAX_VALUE);
		Result r_delta_min = new Result(0.0, Double.MAX_VALUE);
		boolean b_passedIncrement=false;
		boolean b_passedADItem=false;
		for(Result r : ts_allResults) {
			L.myErrPrint("     "+r.toString3());
			switch(r.i_type) {
			case Result.TYPE_MEASURE:
				if(!(r.brcontains(r_alternances)&&r_alternances.brcontains(r))) {
					r.previous=r_previous;
					r_alternances=r;
					i_alternances++;
					Double d_delta = r.getDelta(r_previous);
					L.myErrPrint("     "+i_alternances);
					if(d_delta<r_delta_min.d_delta) {
						r_delta_min=new Result(r, r_previous, d_delta);
						L.myErrPrintln(String.format(" alternance_delta_min=%1$9.5f", r_delta_min.d_delta));
					} else {
						L.myErrPrintln("");
					}
					if(!b_passedIncrement) {
						if(!b_justLog) {
							throw new Exception(k_startCol.node+
									"__"+k_endRow.node+"2 alternances between 2 increments!"+
									String.format("[%1$6.3fV]",k_startCol.d_source_volt));
						} else {
							L.myPrintln("!Exception! "+k_startCol.node+
									"__"+k_endRow.node+"2 alternances between 2 increments!"+
									String.format("[%1$6.3fV]",k_startCol.d_source_volt));
						}
					}
					if(b_ADItemFound) {
						if(!b_passedADItem) {
							if(!b_justLog) {
								throw new Exception(k_startCol.node+
										"__"+k_endRow.node+"2 alternances between 2 ADItems! ["+
										String.format("[%1$6.3fV]",k_startCol.d_source_volt));
							} else {
								L.myPrintln("!Exception! "+k_startCol.node+
										"__"+k_endRow.node+"2 alternances between 2 ADItems! ["+
										String.format("[%1$6.3fV]",k_startCol.d_source_volt));
							}
						}
					}
					b_passedIncrement=false;
					b_passedADItem=false;
				} else {
					L.myErrPrintln("");
				}
				r_previous=r;
				break;
			case Result.TYPE_ADLEVEL:
				L.myErrPrintln("");
				b_passedIncrement=true;
				break;
			case Result.TYPE_ADITEM:
				L.myErrPrintln("");
				b_passedADItem=true;
				b_passedIncrement=true; // ad_items are normalized
				break;
			}
			if(r.compareTo(ts_MSvalues.last())>0) {
				break;
			}
		}
		if(ts_allResults.last().i_type==Result.TYPE_MEASURE) {
			if(!b_justLog) {
				throw new Exception(k_startCol.node+
						"__"+k_endRow.node+"last increment: "+l_ad_increment.get(l_ad_increment.size()-1).toString3()+" < "+
						ts_allResults.last().toString3()+"["+
						String.format("%1$6.3f",k_startCol.d_source_volt)+"V]");
			} else {
				L.myErrPrintln("!Exception! "+k_startCol.node+
						"__"+k_endRow.node+"last increment: "+l_ad_increment.get(l_ad_increment.size()-1).toString3()+" < "+
						ts_allResults.last().toString3()+"["+
						String.format("%1$6.3f",k_startCol.d_source_volt)+"V]");
			}
		}
		L.myErrPrintln(" "+i_alternances+
				String.format(" alternances alternance_delta_min=%1$9.5f",
						r_delta_min.d_delta));
		Result r_first = findFirst(ts_allResults);
		Result r_last = findLast(ts_allResults);
		if(i_createCSVFile==RUNALLQUARTOBATCH) {
			csvfile_runAllQuartoBatch.writeFile("\""+i_seed+"\",\""+
					String.format("%1$6.3f",k_startCol.d_source_volt)+"\",\"\""+
					k_startCol.node+"_"+k_endRow.node+"\",\""+
					kc.getQuartoKeys(k_startCol, k_endRow).size()+
					String.format("\",\"%1$d\",\"%2$9.5f\",\"%3$9.5f\",\"%4$9.5f\"\n",
							i_alternances, r_delta_min.d_delta,
							r_first.d_res, r_last.d_res));
		}
		r_delta_min.d_max=r_last.d_res;
		r_delta_min.i_alternances=i_alternances;
		extract_ADtable(ts_allResults, ts_MSvalues, i_seed);
		return r_delta_min;
	}


	private Result findLast(TreeSet<Result> ts_allResults) {
		for(Result r : ts_allResults.descendingSet()) {
			if(r.l_key2press.size()>0) {
				return r;
			}		
		}
		return null;
	}


	private Result findFirst(TreeSet<Result> ts_allResults) {
		for(Result r : ts_allResults) {
			if(r.l_key2press.size()>0) {
				return r;
			}
		}
		return null;
	}


	private void extract_ADtable(TreeSet<Result> ts_allResults, TreeSet<Result> ts_MSvalues, int i_seed) throws Exception {
		//		L.myErrPrintln("******ADtable******");
		for(Result r : ts_allResults) {
			if(r.previous!=null) {
				Result r_bestADlevel = r.findBestADlevel(ts_allResults);
				csvfile_ADtable.writeFile(r_bestADlevel.toStringADtable(i_seed));
				//				L.myErrPrintln("  "+r_bestADlevel.toString3());
			}
			//			L.myErrPrintln("     "+r.toString3());
			if(r.compareTo(ts_MSvalues.last())>0) {
				break;
			}
		}
	}


	private void processCombination(ArrayList<Result> l_keysCombination2Press, boolean b_justLog) throws Exception {
		for(Result l_keys2Press : l_keysCombination2Press) {
			graphPanel.clearPressedKeys();
			graphPanel.relax();
			Thread.sleep(2);
			pressKeys(l_keys2Press);
			graphPanel.relax();
			Thread.sleep(2);
			graphPanel.start_spread_current();
			graphPanel.relax();
			Thread.sleep(2);
			int i_in_remove_dead_ends_mode=777;
			while(i_in_remove_dead_ends_mode>0) {
				i_in_remove_dead_ends_mode = kc.removeDeadEnds();
				Thread.sleep(2);
			}
			ArrayList<Key> l_keys = elecsimu.simplifyNetwork(k_startCol);
			l_keys2Press.update(k_startCol, k_endRow);
			double d_current = elecsimu.compute_current(k_startCol, k_endRow, l_keys);
			l_keys2Press.updateResult(d_current);
			Result r = elecsimu.check_actual_vs_computed(k_startCol, k_endRow, d_current);
			l_keys2Press.updateResult(r);
			if(!r.OK()) {
				if(!b_justLog) {
					throw new Exception(r.s_key_pressed_check);
				} else {
					L.myPrintln("!Exception! " + r.s_key_pressed_check);
				}
			}
		}
	}

	private void processCombinationFast(ArrayList<Result> l_keysCombination2Press) throws Exception {
		for(Result l_keys2Press : l_keysCombination2Press) {
			processKeyCombinationFast(l_keys2Press, false);
		}
	}

	private Result processKeyCombinationFast(Result l_keys2Press, boolean b_adi) throws Exception {
		graphPanel.clearPressedKeys();
		pressKeys(l_keys2Press);
		graphPanel.start_spread_current();
		int i_in_remove_dead_ends_mode=777;
		while(i_in_remove_dead_ends_mode>0) {
			i_in_remove_dead_ends_mode = kc.removeDeadEnds();
		}
		ArrayList<Key> l_keys = elecsimu.simplifyNetwork(k_startCol);
		if(!b_adi) {
			l_keys2Press.update(k_startCol, k_endRow);
			double d_current = elecsimu.compute_current(k_startCol, k_endRow, l_keys);
			l_keys2Press.updateResult(d_current);
			Result r = elecsimu.check_actual_vs_computed(k_startCol, k_endRow, d_current);
			if(!r.OK()) {
				throw new Exception(r.s_key_pressed_check);
			}
			return l_keys2Press;
		} else {
			Result r = new Result(l_keys2Press, l_keys2Press.l_key2press, null, null);
			r.update(k_startCol, k_endRow);
			r.updateResult(elecsimu.compute_current(k_startCol, k_endRow, l_keys));
			return r;
		}
	}

	private Result printStatsMaster(ArrayList<Result> l_keysCombination2Press) throws Exception {
		int i_size=l_keysCombination2Press.size();
		if(i_size>1) {
			Double d_min = Double.MAX_VALUE;
			Double d_max = Double.MIN_VALUE;

			for(Result r:l_keysCombination2Press) {
				Double d=r.d_res;
				if(d<d_min) {
					d_min=d;
				}
				if(d>d_max) {
					d_max=d;
				}
			}
			L.myErrPrint(String.format("    ++min=%1$9.5f,  max=%2$9.5f,  ",d_min,d_max));

			Result r_delta_min = new Result(Double.MAX_VALUE, true);
			int i_min_i=Integer.MIN_VALUE;
			int i_min_j=Integer.MIN_VALUE;
			for(int i=0;i<i_size;i++) {
				Double d1=l_keysCombination2Press.get(i).d_res;
				for(int j=i+1;j<i_size;j++) {
					Double d2=l_keysCombination2Press.get(j).d_res;
					Double d_delta=d1-d2;
					if(d_delta<0) {
						d_delta=0.0-d_delta;
					}
					if(d_delta<r_delta_min.d_delta) {
						//						L.myPrintln("   i="+i+" vs j="+j);
						r_delta_min=new Result(l_keysCombination2Press.get(i),l_keysCombination2Press.get(j), d_delta);
						i_min_i=i;
						i_min_j=j;
					}
				}
			}
			L.myErrPrintln(
					String.format(", delta_min( %1$s %2$9.5f%%)",
							r_delta_min.toStringDelta(),(100*r_delta_min.d_delta)/(d_max-d_min))+
							l_keysCombination2Press.get(i_min_i).toString()+ " vs " +							
							l_keysCombination2Press.get(i_min_j).toString()); 
			return r_delta_min;
		} else {
			L.myErrPrintln("    ++only one key in quarto: "+l_keysCombination2Press);
			return new Result(l_keysCombination2Press.get(0).d_res/2);
		}
	}

	private Result printStatsNonQuarto(ArrayList<Result> l_keysCombination2Press) throws Exception {
		Result r_delta_max = new Result(0.0, true);
		boolean b_found=false;
		for(Result r: l_keysCombination2Press) {
			r.checkConsistency();
			if(r.getDelta()>r_delta_max.d_delta) {
				r_delta_max=r;
				b_found=true;
			}
		}
		if(b_found) {
			return r_delta_max;
		} else {
			return l_keysCombination2Press.get(0);
		}
	}

	private void pressKeys(Result l_press) {
		pressKeys(l_press.l_key2press);
	}

	void pressKeys(ArrayList<Key> l_press) {
		for(Key k : l_press) {
			k.node.key_pressed=true;
		}
	}

	public void setK_startCol(Key col) {
		k_startCol = col;
		graphPanel.k_startCol = col;
		kc.k_startCol = col;
	}


	public void setK_endRow(Key row) {
		k_endRow = row;
		graphPanel.k_endRow = row;
		kc.k_endRow = row;
	}

	private void setK_endRow(int i_row) {
		ArrayList<Node> l_nodes= new ArrayList<Node>(Arrays.asList(km.graphPanel.nodes));
		for(Node n:l_nodes){
			if(n==null) {
				break;
			}
			if(n.key.isRow()&&n.key.i_row==i_row) {
				setK_endRow(n.key);
			}
		}		
	}

	private void setK_startCol(int i_col) {
		ArrayList<Node> l_nodes= new ArrayList<Node>(Arrays.asList(km.graphPanel.nodes));
		for(Node n:l_nodes){
			if(n==null) {
				break;
			}
			if(n.key.isCol()&&n.key.i_col==i_col) {
				setK_startCol(n.key);
			}
		}
	}


	void initializeADincs() {
		l_ad_increment = new ArrayList<Result>();
		for(int inc=0;inc<java.lang.Math.pow(2,AD_BIT_RESOLUTION);inc++) {
			l_ad_increment.add(new Result(inc*AD_INCREMENT,Result.TYPE_ADLEVEL));
		}

	}


	public boolean trace() {
		return true;
		//		return i_createCSVFile<RUNALLQUARTORANDOMBATCH;
	}


	public void optimize() throws Exception {
		csvfile_runAllQuartoBatch.writeFile(String.format("\"Ramdom span: %1$6.2f\"\n",GraphPanel.RESISTANCE_RANDSPAN));
		csvfile_runAllQuartoBatch.writeFile("\"Reference: \"\n");
		Result r_delta_base = runAllQuartoBatch(0);
		csvfile_runAllQuartoBatch.writeFile("\"Random Variations: \"\n");	
		Result r_delta_best = r_delta_base;
		for(int i_seed=1;i_seed<MAXRAND;i_seed++) {	
			Result r_delta = runAllQuartoBatch(i_seed);
			r_delta_best=r_delta_best.max_percent(r_delta, i_seed);
			main.jlblQuartoBatchRandom.setText(i_seed+"/"+MAXRAND+ " " +
					r_delta_base.printPerCent()+"/"+r_delta.printPerCent()+"/"+r_delta_best.printPerCentSeed());
			if(r_delta_best.i_seed==i_seed) {
				@SuppressWarnings("unused")
				MembraneFile mf = new MembraneFile(L,graphPanel,100*r_delta.d_delta/r_delta.d_max);
				graphPanel.updateResistances();
			}

		}
	}

	public void runGetAllGhosts(int i) throws Exception {
		int i_maxrow = km.getMaxRow()/2;
		int i_maxcol = km.getMaxCol()/2;
		for(int i_row=0;i_row<i_maxrow;i_row++) {
			setK_endRow(kc.findRow(i_row));
			for(int i_col=0;i_col<i_maxcol;i_col++) {
				setK_startCol(kc.findCol(i_col));
				main.jlblQuartoBatch.setText(k_startCol.node+", "+k_endRow.node);
				runQuarto3OtherKeysBatch(0);
			}
		}
		csvfile_ghostKeys = L.getMyFile(main.f_MembraneFile.getCanonicalPath() + "_ghostlist.csv");
		L.files.add(csvfile_ghostKeys);
		csvfile_ghostKeys.writeFile("\"COL\",\"ROW\",\"GhostSeverity\",\"Key1\",\"Key2\",\"Key3\"," +
		"\"GhostK\",\"ActualK\",\"ComputedK\"\n");
		for(Ghost ts_ghostKeyCombination : ts_ghostKeys) {
			csvfile_ghostKeys.writeFile(ts_ghostKeyCombination.toString()+"\n");
		}
		csvfile_ghostKeys.clean();
	}

}
