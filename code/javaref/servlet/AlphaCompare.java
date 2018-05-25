import java.io.*;
import java.lang.*;

public class AlphaCompare extends Sort {

  public void compare(int left, int right, Object listObject[]) throws SecurityException {
      if((((File)(listObject[left])).getName().compareTo(((File)(listObject[right])).getName()) > 0)&&(left < right)) {
          /*System.out.println("  " + left  + ":" + ((File)(listObject[left])).getName() + " " +
          **                        + right + ":" + ((File)(listObject[right])).getName());*/
          Object tmp        = listObject[left];
          listObject[left]  = listObject[right];
          listObject[right] = tmp;
      }
  }

}
