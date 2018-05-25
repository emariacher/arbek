/*---------------------------------------------------------------------------*\
 *
 * Copyright (c) 1998-2000 Eric Mariacher. All Rights Reserved.
 *
 * Eric Mariacher grants you ("Licensee") a non-exclusive, royalty free
 * and license to use
 * this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Eric Mariacher.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. ERIC MARIACHER AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL ERIC MARIACHER OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF ERIC MARIACHER HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.

     Author: E.Mariacher
   Creation: 8 Juin 1998
   Abstract: Genealogie
 Assumption: -1- not Y10K ready!
             -2- branches must be given going from the oldest generation to the youngest
                 (no order required if 2 branches are of the same generation).

\*---------------------------------------------------------------------------*/
/*---------------------------------------------------------------------------*\
 *
 * Copyright (c) 1994-1996 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
\*---------------------------------------------------------------------------*/
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class addGen {
    dbGen db;
    int   compteurBalayage, vieuxCompteurBalayage;
    int   start_klampin_index;


    addGen(dbGen db) {
        this.db = db;
    }

    // filtre par morceau de chaine
    public int montre(String str) {
       int colle = 0;
       for (int i = 0; i < db.nklampin ; i++) {
           if(db.klampin[i].prenom.toUpperCase().indexOf(str.toUpperCase())>=0) {
               db.klampin[i].affiche = true;
               colle++;
           } else db.klampin[i].affiche = false;
       }
       return colle;
    }


    // filtre ascendants descendants
    public void select(int ki) {
       start_klampin_index = ki;
       resetLesKlampin(false);
       db.klampin[ki].affiche   = true;
       db.klampin[ki].affiche_A = true;
       trouveLesAscendants(ki);
       db.klampin[ki].affiche_D = true;
       trouveLesDescendants(ki);
    }

    public void trouveLesDescendantsImmediats(int ki) {
        for (int i = 0 ; i < db.nliens ; i++) {
            if((db.liens[i].de==ki)&&(!db.klampin[db.liens[i].vers].affiche_D)) {
                // System.out.println("    " + db.klampin[ki].prenom + " -> " + db.klampin[db.liens[i].vers].prenom + " " + compteurBalayage);
                db.klampin[db.liens[i].vers].affiche   = true;
                db.klampin[db.liens[i].vers].affiche_D = true;
                compteurBalayage++;
            }
        }
    }

    public void trouveLesDescendants(int ki) {
       int i=0,j=0;
       int zarg=0;
       while(db.klampin[i].generation<db.klampin[ki].generation) { j=i; i++; }
            compteurBalayage = 1;
       vieuxCompteurBalayage = 0;
       // System.out.println("Descendants! " + j);
       while(compteurBalayage != vieuxCompteurBalayage) {
           // System.out.println("  " + db.klampin[ki].prenom + " " + (zarg++) + " " + vieuxCompteurBalayage + "/" + compteurBalayage);
           vieuxCompteurBalayage = compteurBalayage;
           for (i = j; i < db.nklampin ; i++) {
               if(db.klampin[i].affiche_D) trouveLesDescendantsImmediats(i);
           }
       }
    }


    public void trouveLesAscendantsImmediats(int ki) {
        for (int i = 0 ; i < db.nliens ; i++) {
            if((db.liens[i].vers==ki)&&(!db.klampin[db.liens[i].de].affiche_A)&&(!db.klampin[db.liens[i].vers].epoux)) {
                // System.out.println("    " + db.klampin[ki].prenom + " -> " + db.klampin[db.liens[i].de].prenom + " " + compteurBalayage);
                db.klampin[db.liens[i].de].affiche   = true;
                db.klampin[db.liens[i].de].affiche_A = true;
                compteurBalayage++;
                if(((db.liens[i].type&2)==2)&&(db.liens[i].vers==start_klampin_index)) {
                    db.klampin[db.liens[i].de].epoux   = true;
                    db.klampin[db.liens[i].vers].epoux = true;
                } else {
                    db.klampin[db.liens[i].de].epoux   = false;
                    db.klampin[db.liens[i].vers].epoux = false;
                }
                // System.out.println("      |de " + db.klampin[db.liens[i].de].prenom     + ":" + db.klampin[db.liens[i].de].epoux  + " |vers " + db.klampin[db.liens[i].vers].prenom + ":" + db.klampin[db.liens[i].vers].epoux );
            }

        }
    }

    public void trouveLesAscendants(int ki) {
       int i=db.nklampin-1,j=db.nklampin-1;
       int zarg=0;
       while(db.klampin[i].generation>db.klampin[ki].generation) { j=i; i--; }
            compteurBalayage = 1;
       vieuxCompteurBalayage = 0;
       // System.out.println("Ascendants! " + j);
       while(compteurBalayage!=vieuxCompteurBalayage) {
           // System.out.println("  " + db.klampin[ki].prenom + " " + (zarg++) + " " + vieuxCompteurBalayage + "/" + compteurBalayage);
           vieuxCompteurBalayage = compteurBalayage;
           for (i = j; i >= 0 ; i--) {
               if((db.klampin[i].affiche_A)&&(!db.klampin[db.liens[i].de].epoux)) trouveLesAscendantsImmediats(i);
           }
       }
    }


    public void resetLesKlampin(boolean b) {
        for (int i = 0 ; i < db.nklampin ; i++) {
          db.klampin[i].affiche   = b;
          db.klampin[i].affiche_A = false;
          db.klampin[i].affiche_D = false;
          db.klampin[i].epoux     = false;
        }
    }
}



