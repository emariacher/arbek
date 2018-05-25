import java.util.*;
import java.net.*;
import java.awt.Image;

public class Lieu implements EstherInclude {
    int    x, y;
    String nom;
    String laius;
    String imageSource;
    int    influence;
    int    nextIndex;

    Lieu(String zob, Lieu[] lieu, int maxIndex) {
        if(zob!=null) {
            int cpt = 0;
            for (StringTokenizer t = new StringTokenizer(zob, ",") ; t.hasMoreTokens() ; ) {
                switch(cpt) {
                    case 0: nom         = t.nextToken(); break;
                    case 1: x           = Integer.valueOf(t.nextToken()).intValue(); break;
                    case 2: y           = Integer.valueOf(t.nextToken()).intValue(); break;
                    case 3: influence   = Integer.valueOf(t.nextToken()).intValue(); break;
                    case 4: imageSource = t.nextToken(); break;
                    case 5: laius       = t.nextToken(); break;
                }
                cpt++;
            }
            nextIndex = maxIndex;
            for(cpt=0; lieu[cpt]!=null; cpt++) {
                if(lieu[cpt].nom.equals(nom)) {
                    nextIndex           = lieu[cpt].nextIndex;
                    lieu[cpt].nextIndex = maxIndex;
                    break;
                }
            }
        }
    }

    public String toString() {
       if(BRINGUP) return (new String(nom + " [" + x + "," + y + "] d:" + influence + " ni:" + nextIndex));
       else        return (new String(nom));
    }
}
