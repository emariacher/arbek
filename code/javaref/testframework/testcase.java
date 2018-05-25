public class testcase {
	String s_input;
	String s_expectedOutput;
	Log L;

	testcase(String s_input, String s_expectedOutput, Log L){
		this.s_input=s_input;
		this.s_expectedOutput=s_expectedOutput;
		this.L = L;
	}

	private boolean b_stest(String s_actualOutput) {
		return s_actualOutput.compareTo(s_expectedOutput)==0;
	}
	boolean b_test(String s_actualOutput) throws Exception {
		String s_log = new String("f("+s_input+"->actual["+s_actualOutput+"]");
		String s_logp= new String("   Test Passed: "+s_log+".");
		String s_logf= new String("***Test Failed: "+s_log+" vs expected["+s_expectedOutput+"].");

			if(!b_stest(s_actualOutput)) {
				L.myErrPrintln(s_logf);
				return false;
			} else {
				L.myPrintln(s_logp);
				return true;
			}		
	}

}

