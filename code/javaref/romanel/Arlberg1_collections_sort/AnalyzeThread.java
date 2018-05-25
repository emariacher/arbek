import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalyzeThread extends Thread {
	public AnalyzeThread(Arlberg1 z) throws Exception {
		this.z = z;
		L = new Log(z.err, z.AnalyzeLabel);
		L.knowPath(z.ArlbergLogFile.getCanonicalPath());
		L.initLogFiles();
		start();
	}

	Arlberg1 z;
	Log L;
	private List<triplet> l_allGhosts=new ArrayList<triplet>();;
	private List<triplet> l_allGhosts4Stats=new ArrayList<triplet>();;
	public KeyMapping km = new KeyMapping();
	private Calendar startDate = Calendar.getInstance();
	private Calendar endDate = Calendar.getInstance();
	DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT);


	public void run() {
		try {
			// ANALYZE
			//L.myErrPrintln("running");
				endDate.set(Calendar.YEAR, 1965);
			parseFile(z.ArlbergLogFile);
			doZeStats();
			sortZeStats();
			printZeStats();
			z.AnalyzeLabel.setText("FINISHED!");
			L.closeLogFiles();
		} catch (Exception e) {
			z.AnalyzeLabel.setText("*******Errors: not Built!********");
			try {
				PipedInputStream piErr = new PipedInputStream();
				PipedOutputStream poErr = new PipedOutputStream(piErr);
				System.setErr(new PrintStream(poErr, true));
				e.printStackTrace();
				L.myErrPrintln(e.toString());
				int len = 0;
				byte[] buf = new byte[1024];
				while (true) {
					len = piErr.read(buf);
					if (len == -1) {
						break;
					}
					L.err.append(new String(buf, 0, len));
				}

			} catch (Exception ze) {}
		} finally {

		}
	}

	private void sortZeStats() {
		Collections.sort(l_allGhosts4Stats,new Comparator<triplet>() {
			public int compare(triplet o1, triplet o2) {
				return o2.count - o1.count;
			}});	
	}

	private void printZeStats() throws Exception {
		L.myErrPrint(z.ArlbergLogFile.getName());
		// count total number of ghosts
		int count=0;
		Iterator<triplet> i = l_allGhosts4Stats.iterator();
		while(i.hasNext()){
			triplet t = (triplet) i.next();
			count+=t.count;
		}
		L.myErrPrint(", "+count);
		// process duration of logging
		long ms = endDate.getTimeInMillis() - startDate.getTimeInMillis();
		L.myErrPrintln(" ghost conditions, "+(ms/(1000*3600))+" hrs");
		// print triplets
		i = l_allGhosts4Stats.iterator();
		while(i.hasNext()){
			triplet t = (triplet) i.next();
			L.myErrPrintln(t.toString2());
		}		
	}

	private void doZeStats() throws Exception {
		Iterator<triplet> i = l_allGhosts.iterator();
		while(i.hasNext()){
			triplet t = (triplet) i.next();
			int index = l_allGhosts4Stats.indexOf(t);
//			L.myPrintln("here01"+t.toString3()+ " "+(index!=-1));
			if(index==-1){
				l_allGhosts4Stats.add(t);
				Collections.sort(l_allGhosts4Stats);
				index = l_allGhosts4Stats.indexOf(t);
//				L.myPrintln("    added02"+t.toString3()+ " i="+index);
			}
			triplet ts = l_allGhosts4Stats.get(index);
			ts.count++;
//			L.myPrintln("  inced03"+ts.toString3());
		}
	}

	public void parseFile(File f) throws Exception {
		//try {
		if(f.canRead()) {
			BufferedReader inputf = new BufferedReader(new FileReader(f));
			String linef;
			while((linef = inputf.readLine()) != null) {
				parseLine(linef);
			}
			inputf.close();
			Collections.sort(l_allGhosts);
			L.myPrintln("\n");
		}
		/*} catch(FileNotFoundException e) {
			z.err.append("             *warning* no File found! expected at[" +
					f.getName() + "] continuing...");
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println(e);
			System.out.println(e);
			z.err.append(e.toString());
		}*/

	}

	private void parseLine(String linef) throws Exception {
		//		L.myErrPrintln(""+Pattern.matches(".*GHOST KEYS : 3 Keys.*", linef)+ " "+ linef);
		if(Pattern.matches(".*GHOST KEYS : 3 Keys.*", linef)) {
			// process triplet of ghost keys
			L.myPrint("GhostKeys: ");
			Pattern p_hex = Pattern.compile("0x\\p{XDigit}*");
			Matcher m_hex = p_hex.matcher(linef);
			List<String> l_xplet= new ArrayList<String>();
			while (m_hex.find()){
				L.myPrint(m_hex.group()+" ");				
				l_xplet.add(m_hex.group());
			}
			l_allGhosts.add(new triplet(l_xplet, km, L));

			// process duration of logging
			Pattern p_date = Pattern.compile("\\d*, \\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d");
			Matcher m_date = p_date.matcher(linef);
			Calendar tlog= Calendar.getInstance();
			while (m_date.find()){
				DateFormat formatter = new SimpleDateFormat("dd, yyyy HH:mm:ss");
				Date date = (Date)formatter.parse(m_date.group());
				tlog.setTime(date);
				L.myPrint("  "+m_date.group()+" ");	
				L.myPrintln(dateFormatter.format(date));
			}
			if (tlog.before(startDate )) {
				startDate=(Calendar)tlog.clone();
			}
			if (tlog.after(endDate)) {
				endDate=(Calendar)tlog.clone();
			}
		}
	}
}

