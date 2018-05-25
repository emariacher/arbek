/**
 *  Description of the Class
 *
 *@author     emariach
 *@created    May 19, 2008
 */
public class TestDataObject {
	private String s1;
	private boolean b1;
	private int i1;


	/**
	 *  Constructor for the TestDataObject object
	 *
	 *@param  s  Description of the Parameter
	 *@param  i  Description of the Parameter
	 *@param  b  Description of the Parameter
	 */
	public TestDataObject(String s, int i, boolean b) {
		this.s1 = s;
		this.i1 = i;
		this.b1 = b;
	}


	/**
	 *  Sets the boolean attribute of the TestDataObject object
	 *
	 *@param  b  The new boolean value
	 */
	public void setBoolean(boolean b) {
		this.b1 = b;
	}


	/**
	 *  Gets the boolean attribute of the TestDataObject object
	 *
	 *@return    The boolean value
	 */
	public boolean isBoolean() {
		return this.b1;
	}


	/**
	 *  Sets the iint attribute of the TestDataObject object
	 *
	 *@param  i  The new iint value
	 */
	public void setIint(int i) {
		this.i1 = i;
	}


	/**
	 *  Gets the int attribute of the TestDataObject object
	 *
	 *@return    The int value
	 */
	public int getInt() {
		return this.i1;
	}


	/**
	 *  Sets the string attribute of the TestDataObject object
	 *
	 *@param  s1  The new string value
	 */
	public void setString(String s1) {
		this.s1 = s1;
	}


	/**
	 *  Gets the string attribute of the TestDataObject object
	 *
	 *@return    The string value
	 */
	public String getString() {
		return this.s1;
	}


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String toString() {
		return this.s1 + ":" + this.i1 + ":" + this.b1;
	}
}

