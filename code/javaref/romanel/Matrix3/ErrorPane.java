import javax.swing.JEditorPane;


/*
 * $Log$
 * emariacher - Tuesday, November 17, 2009 11:29:37 AM
 */
public class ErrorPane extends JEditorPane {
	StringBuffer sb_err=new StringBuffer();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ErrorPane() {
	}

	void appendErr(String s) {
		sb_err.append(s);
		setText(sb_err.toString());
	}


}
