
public abstract class Sortable {
	  /* Return -1 if self less than other,
	    *         0 if same,
	    *         1 if self greater than other
	    */
	  /**
	   *  Description of the Method
	   *
	   *@param  other  Description of the Parameter
	   *@return        Description of the Return Value
	   */
	  public int compare(
	      Object other) {
	    if(hashCode() > other.hashCode()) {
	      return 1;
	    }
	    if(hashCode() < other.hashCode()) {
	      return -1;
	    }
	    return 0;
	  }

	public abstract int compare(Sortable b);
}
