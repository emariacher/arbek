/*
 * $Log$
 * emariacher - Tuesday, November 17, 2009 6:06:35 PM
 * still debugging...
 * emariacher - Tuesday, November 17, 2009 11:29:56 AM
 * cleaning html err area
 * emariacher - Tuesday, November 17, 2009 10:45:31 AM
 * err textarea is now in html.
 * emariacher - Wednesday, November 11, 2009 3:36:00 PM
 * generate kbd_map.c
 * emariacher - Wednesday, November 11, 2009 2:07:09 PM
 * emariacher - Monday, October 05, 2009 2:22:25 PM
 * CounterStrike rules added
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;

public class AnalyzeThread extends Thread {
	public AnalyzeThread(Matrix3 z) throws Exception {
		this.z = z;
		L = new Log(z.err, z.AnalyzeLabel);
		L.knowPath(z.MainMatrixFile.getCanonicalPath());
		L.initLogFiles();
		start();
	}

	Matrix3 z;
	Log L;
	TreeMap<ghostFrequency, Integer> m_ghostFrequencyList = new TreeMap<ghostFrequency, Integer>(new ghostFrequency.compare2());
	String s_FrequencyString;
	int i_TotalWordsInFrequencyFile;



	public void run() {
		try {
			// ANALYZE
			z.err.setText("");
			L.myErrPrintln("<html><body><pre>\n");
			L.myErrPrintln("Main Matrix File: "+z.MainMatrixFile.getName()+
					"\nFn Matrix File: "+z.FnMatrixFile.getName()+
					"\nWords Frequency List File: "+z.LanguageStatsFile.getName());
			KbdMatrix km = new KbdMatrix(z.MainMatrixFile, L);
			KbdMatrix kfnm = new KbdMatrix(z.FnMatrixFile, km, L);
			L.myErrPrintln(km.toStringHTML());
			L.myErrPrintln(kfnm.toStringHTML());
			L.myErrPrintln("\n"+km.checkDuplicateKeys(kfnm));
			km.getAllGhosts();
			km.buildMasterTripletList();
			km.checkWHQL(kfnm);
			L.myErrPrintln("\n"+km.checkBraille());
			L.myErrPrintln("\n"+km.checkGuitarHero());
			L.myErrPrintln("\n"+km.checkCounterStrike());
			//l_MasterTripletList = buildMasterAllTripletList();
			s_FrequencyString = buildfrequencyString(z.LanguageStatsFile);
			L.myErrPrintln("\nTotal words in frequency list: "+i_TotalWordsInFrequencyFile);
			Iterator<String> it_mt = km.l_MasterTripletList.iterator();
			while(it_mt.hasNext()){
				MasterTriplet m_MasterTriplet = new MasterTriplet(it_mt.next(), km);
				z.AnalyzeLabel.setText(m_MasterTriplet.getMasterString());
				ghostFrequency gf=m_MasterTriplet.getGhostFrequency(s_FrequencyString, i_TotalWordsInFrequencyFile);
				m_ghostFrequencyList.put(gf, gf.i_occurence);
			}  
			if(m_ghostFrequencyList.headMap(new ghostFrequency(i_TotalWordsInFrequencyFile, new Float(0.005))).isEmpty()) {
				L.myErrPrintln("\nGhost key triplets by order of occurence in: "+
						z.LanguageStatsFile.getName()+"\n"+
						m_ghostFrequencyList.toString());
			} else {
				L.myPrintln("\nGhost key triplets by order of occurence in: "+
						z.LanguageStatsFile.getName()+"\n"+
						m_ghostFrequencyList.toString());
				L.myErrPrintln("\nGhost key triplets that are > 0.5% by order of occurence in: "+
						z.LanguageStatsFile.getName()+"\n"+
						m_ghostFrequencyList.headMap(new ghostFrequency(i_TotalWordsInFrequencyFile, new Float(0.005))).toString());

			}

			L.myErrPrintln("\n"+km.checkGaming());
			z.AnalyzeLabel.setText(L.errfile.getName() +" written!");
			new KbdMap(km,kfnm,L);
			L.myErrPrintln("</pre></body></html>\n");
			L.closeFiles();
		} catch (Exception e) {
			z.AnalyzeLabel.setText("*******Errors: not Built!********");
			try {
				PipedInputStream piErr = new PipedInputStream();
				PipedOutputStream poErr = new PipedOutputStream(piErr);
				System.setErr(new PrintStream(poErr, true));
				e.printStackTrace();
				L.myPrintln(e.toString());
				L.myErrPrintln(e.toString());
				int len = 0;
				byte[] buf = new byte[1024];
				while (true) {
					len = piErr.read(buf);
					if (len == -1) {
						break;
					}
					L.err.appendErr(new String(buf, 0, len));
					L.myPrintln(new String(buf, 0, len));
				}

			} catch (Exception ze) {}
		} finally {

		}
	}


	@SuppressWarnings("unused")
	private ArrayList<String> buildMasterAllTripletList() {
		String s_all = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ '");
		int i_all_length=s_all.length();
		ArrayList<String> l_MasterTripletList     = new ArrayList<String>();
		for(int i=0;i<(i_all_length-2);i++){
			for(int j=i+1;j<(i_all_length-1);j++){
				for(int k=j+1;k<i_all_length;k++){
					l_MasterTripletList.add(
							s_all.substring(i,i+1)+
							s_all.substring(j,j+1)+
							s_all.substring(k,k+1)
					);
				}
			}
		}
		return l_MasterTripletList;
	}


	private String buildfrequencyString(File f_File) throws IOException {
		StringBuffer b_FrequencyString     = new StringBuffer();
		if(f_File.canRead()) {
			Scanner sc = new Scanner(f_File);
			while (sc.hasNextLine()) {
				String s_linef = sc.nextLine();
				if(s_linef.startsWith("{")) {
					i_TotalWordsInFrequencyFile = Integer.valueOf(new String(s_linef.substring(s_linef.indexOf("{")+1, s_linef.indexOf("}"))));
				}
				b_FrequencyString.append(s_linef.toUpperCase());
			}
		}
		return b_FrequencyString.toString();
	}


}
