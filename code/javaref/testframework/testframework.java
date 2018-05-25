import java.util.Enumeration;
import java.util.Vector;


public class testframework {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Log L = new Log();
		try {
			L.knowPath("tmp");
			L.initLogFiles();
			L.myErrPrintln("Here!");

			Vector<testcase> v_testcase     = new Vector<testcase>();
			v_testcase.add(new testcase("1234","1235",L));
			v_testcase.add(new testcase("1234","1234",L));
			v_testcase.add(new testcase(" 1234","1234",L));
			v_testcase.add(new testcase("1234 ","1234",L));
			v_testcase.add(new testcase(" 1234 ","1234",L));
			v_testcase.add(new testcase(" +1234 ","1234",L));
			v_testcase.add(new testcase(" -1234 ","1234",L));
			v_testcase.add(new testcase("1234.56","1234",L));
			v_testcase.add(new testcase("aoxomoxoa","Zorglub.",L));
			v_testcase.add(new testcase("aoxomoxoa","java.lang.Exception: No digit found.",L));
			v_testcase.add(new testcase("11111111111111111111111111111111","java.lang.NumberFormatException: For input string: \"11111111111111111111111111111111\"",L));

			Enumeration<testcase> e_testcase=v_testcase.elements();
			while(e_testcase.hasMoreElements()){
				testcase t =e_testcase.nextElement();
				try {
					t.b_test(Integer.toString(Utils.findNextValidInteger(t.s_input)));
				} catch (Exception e) {
					t.b_test(e.toString());
				}	
			}
			L.myErrPrintln("Tests complete!");
			L.closeLogFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}


}

