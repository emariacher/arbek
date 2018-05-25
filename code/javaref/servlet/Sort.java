// import java.io.*;

public abstract class Sort {

    public abstract void compare(int left, int right, Object listObject[]);


    public void sort(Object listObject[], int limit) {
        for(int i=0;i<limit;i++) {
            // System.out.print(i);
            for(int j=i+1;j<limit;j++) {
                compare(i, j, listObject);
                // System.out.print(".");
            }
            // System.out.println(" ");
        }
    }

}
