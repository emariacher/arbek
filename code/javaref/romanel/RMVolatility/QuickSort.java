import java.util.Vector;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 20, 2004
 */
public class QuickSort {
          // Sorts entire array
  /**
   *  Description of the Method
   *
   *@param  array  Description of the Parameter
   */
  public static void sort(Vector array) {
    sort(array, 0, array.size() - 1);
  }

          // Sorts partial array
  /**
   *  Description of the Method
   *
   *@param  array  Description of the Parameter
   *@param  start  Description of the Parameter
   *@param  end    Description of the Parameter
   */
  public static void sort(Vector array, int start, int end) {
    int p;
    if (end > start) {
      p = partition(array, start, end);
      sort(array, start, p - 1);
      sort(array, p + 1, end);
    }
  }


  /**
   *  Description of the Method
   *
   *@param  a  Description of the Parameter
   *@param  b  Description of the Parameter
   *@return    Description of the Return Value
   */
  protected static int compare(Sortable a, Sortable b) {
    return a.compare(b);
  }


  /**
   *  Description of the Method
   *
   *@param  array  Description of the Parameter
   *@param  start  Description of the Parameter
   *@param  end    Description of the Parameter
   *@return        Description of the Return Value
   */
  protected static int partition(Vector array, int start, int end) {
    int left;
    int right;
    Sortable partitionElement;

          // Arbitrary partition start...there are better ways...
    partitionElement = (Sortable) array.elementAt(end);

    left = start - 1;
    right = end;
    for (; ; ) {
      while (compare(partitionElement, (Sortable) array.elementAt(++left)) == 1) {
        if (left == end) {
          break;
        }
      }
      while (compare(partitionElement, (Sortable) array.elementAt(--right)) == -1) {
        if (right == start) {
          break;
        }
      }
      if (left >= right) {
        break;
      }
      swap(array, left, right);
    }
    swap(array, left, end);

    return left;
  }


  /**
   *  Description of the Method
   *
   *@param  array  Description of the Parameter
   *@param  i      Description of the Parameter
   *@param  j      Description of the Parameter
   */
  protected static void swap(Vector array, int i, int j) {
    Object temp;

    temp = array.elementAt(i);
    array.setElementAt(array.elementAt(j), i);
    array.setElementAt(temp, j);
  }
}

