import java.io.*;

public class DateCompare extends Sort {

  public void compare(int left, int right, Object listObject[]) throws SecurityException {
      if((((File)(listObject[left])).lastModified() > ((File)(listObject[right])).lastModified())&&(left < right)) {
          /*System.out.println("  " + left  + ":" + ((File)(listObject[left])).getName() + " " +
          **                        + right + ":" + ((File)(listObject[right])).getName());*/
          Object tmp        = listObject[left];
          listObject[left]  = listObject[right];
          listObject[right] = tmp;
      }
  }

}
