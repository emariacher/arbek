/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 20, 2004
 */
public class Sortable extends XmlTag {
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
    if (hashCode() > other.hashCode()) {
      return 1;
    }
    if (hashCode() < other.hashCode()) {
      return -1;
    }
          //System.err.println("***************************************\n*********** 2 times the same tasks?");
    return 0;
  }
} // class Sortable

