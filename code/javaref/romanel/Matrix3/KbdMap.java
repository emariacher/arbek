import java.io.IOException;

/*
 * $Log$
 * emariacher - Wednesday, November 25, 2009 3:25:37 PM
 * kbd_map.c file generated
 * emariacher - Wednesday, November 18, 2009 9:47:53 AM
 * no bug in sight
 * today kbdmap.c
 * tomorrow kbdmap.c
 * emariacher - Tuesday, November 17, 2009 6:06:35 PM
 * still debugging...
 * emariacher - Tuesday, November 17, 2009 4:12:07 PM
 * added some key definitions
 * generate a csv file
 * emariacher - Wednesday, November 11, 2009 3:46:14 PM
 * emariacher - Wednesday, November 11, 2009 3:38:51 PM
 * surround keyword issue
 * emariacher - Wednesday, November 11, 2009 3:36:18 PM
 * $NoKeywords$
 */

public class KbdMap {

	private Log L;
	private Log.MyFile kbdmapTodayFile;
	private Log.MyFile kbdmapTomorrowFile;
	private Log.MyFile kbdtablecsvFile;
	KbdMatrix km;
	KbdMatrix kfnm;


	public KbdMap(KbdMatrix km, KbdMatrix kfnm, Log Lo) throws IOException, Exception {
		this.L = Lo;
		this.km=km;
		this.kfnm=kfnm;
		kbdmapTodayFile = L.getMyUniqueFile("kbd_map_today_","c");		
		writeHeader(kbdmapTodayFile);
		kbdmapTodayFile.writeFile(km.toString());
		kbdmapTodayFile.writeFile(kfnm.toString());
		kbdmapTodayFile.writeFile(km.toStringKbdMapToday());
		writeFooter(kbdmapTodayFile);
	
		kbdmapTomorrowFile = L.getMyUniqueFile("kbd_map_tomorrow_","c");
		kbdmapTomorrowFile.writeFile(km.toString());
		kbdmapTomorrowFile.writeFile(kfnm.toString());
		kbdmapTomorrowFile.writeFile(km.toStringKbdMapTomorrow());

		
		kbdtablecsvFile = L.getMyUniqueFile("kbd_table_","csv");
		kbdtablecsvFile.writeFile(km.krt.toStringCsv());
	}

	private void writeFooter(Log.MyFile kbdmapFile2) {
		// TODO Auto-generated method stub
		kbdmapTodayFile.writeFile("/* $Log$ */\n");
	}

	private void writeHeader(Log.MyFile kbdmapFile2) {
		// TODO Auto-generated method stub
		kbdmapTodayFile.writeFile("/* Footer */\n");
	}

}
