import java.util.*;

public class Region implements Include {
    int couleur;          // couleur
    int hcase;            // case_start h
    int wcase;            // case_start w
    int ccase;            // carre_start in case_start
    Vector voisin = null; // liste des voisins
    Vector vqp    = null; // liste des voisins interessants (quartes potentiels)
    int quarteCtr;
    int quarteVoisinCtr;


    Region(int couleur, int hcase, int wcase, int ccase) {
        this.couleur    = couleur;
        this.hcase      = hcase;
        this.wcase      = wcase;
        this.ccase      = ccase;
        voisin          = new Vector();
        vqp             = new Vector();
        quarteCtr       = 0;
        quarteVoisinCtr = 0;
    }


    public String toString(int i, boolean verbose) {
        String zurg = new String();
        if(vqp==null) zurg = "regiont[" + i + "]: " + couleur + ", Voisins: " + voisin.toString() + ", vqp==null";
        else {
           if(verbose) zurg = "regiont[" + i + "]: " + couleur + ", Voisins: " + voisin.toString() + ", vqp: " + vqp.toString() +
                              ", present dans " + quarteCtr + " quartes, et voisin " + quarteVoisinCtr + " fois de quartes.";
           else        zurg = "regiont[" + i + "]: " + couleur + ", vqp: " + vqp.toString() +
                              ", present dans " + quarteCtr + " quartes, et voisin " + quarteVoisinCtr + " fois de quartes.";
        }
        return zurg;
    }

    public void addVoisin(int regiont_index) {
        boolean added = false;

        /* cherche si le voisin est deja reference et ajoute le si necessaire */
        if(!voisin.contains((new Integer(regiont_index)))) {
            for(int i=0; i<voisin.size(); i++) {
                if(regiont_index<((Integer)voisin.elementAt(i)).intValue()) {
                    added = true;
                    voisin.insertElementAt((new Integer(regiont_index)), i);
                    vqp.insertElementAt((new Integer(regiont_index)), i);
                    break;
                }
            }
            if(!added) {
                voisin.addElement((new Integer(regiont_index)));
                vqp.addElement((new Integer(regiont_index)));
            }
            // System.out.print(regiont_index + ", ");
        }
    }


    public boolean removeVqp(int regiont_index) {
        boolean faitQuelquechose = false;
        if(vqp!=null) {
            if(regiont_index!=ZOB) faitQuelquechose = vqp.removeElement(new Integer(regiont_index));
            if(vqp.size()<4) {
                vqp.removeAllElements();
                vqp = null;
                faitQuelquechose = true;
            }
        }
        return faitQuelquechose;
    }
}
