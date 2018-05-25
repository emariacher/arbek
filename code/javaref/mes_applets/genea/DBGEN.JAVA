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
import java.text.SimpleDateFormat;


/*---------------------------------------------------------------------------*\
      Class: dbGen
   Abstract: ?
\*---------------------------------------------------------------------------*/
class dbGen {

  // ARRAY SIZES
  final int MAX_KLAMPIN   = 200;
  final int MAX_LIEN      = (int)(MAX_KLAMPIN*1.5);
  final int MAX_GENE      = 40;

            int           nklampin;
            Klampin       klampin[]  = new Klampin[MAX_KLAMPIN+1];


            int           nliens;
            Lien          liens[]  = new Lien[MAX_LIEN+1];

            int           debut = 9999; // not Y10K ready!
            int           fin   = 0;    // ne supporte pas les annees de naissance avant Jesus-Christ!
            int           compteur_global_generation = 0;
            int           klampin_par_generation[] = new int[MAX_GENE+1];
            int           annee_graphique;
            boolean       freeze = false;
            addGen        zaddGen;
            int           moyenne_par_generation     = 1;
            int           nombre_generation_affiche  = 1;
            String        warningString, errorString, detectedString, generationMismatchString, marriageMismatchString, checkString, tropDeMondeString;

    dbGen() {
        zaddGen = new addGen(this);
    }

    int findKlampin(Klampin gus) {
        for (int i = 0 ; i < nklampin ; i++) {
            if((klampin[i].prenom.equals(gus.prenom)) && (klampin[i].naissance==gus.naissance)) return i;
        }
        return MAX_KLAMPIN;
    }
    int getGeneration(Klampin gus) {
        for (int i = 0 ; i < nklampin ; i++) {
            if((klampin[i].prenom.equals(gus.prenom)) && (klampin[i].naissance==gus.naissance)) return klampin[i].generation;
        }
        return compteur_global_generation;
    }
    public Klampin addKlampin(String str_klampin) {
        Klampin n = new Klampin();
        int i = str_klampin.indexOf('_');
        int j = str_klampin.lastIndexOf('_');
        int k = str_klampin.indexOf('/');
        if(i>0) n.prenom  = str_klampin.substring(0,i).trim();
        else {
          System.err.println("No Date of birth for " + str_klampin);
          return n;
        }

        if(k>0) n.type = Integer.valueOf(str_klampin.substring(k+1),10).intValue();
        else {
                n.type = 0;
                k = str_klampin.length();
        }
        if((j>0)&&(j!=i)) {
          n.deces = Integer.valueOf(str_klampin.substring(j+1,k),10).intValue();
          if(n.deces>fin) fin = n.deces; // in the case a branch terminated itself
        } else {
          n.deces = 9999; // this applet is not Y10K ready!
          j = k;
        }

        n.naissance = Integer.valueOf(str_klampin.substring(i+1,j),10).intValue();
        if(n.naissance<debut) debut = n.naissance;
        if(n.naissance>fin)   fin   = n.naissance;
        n.x            = (int)(n.naissance-debut + 50*Math.random());
        n.y            = (int)(n.naissance-debut + 50*Math.random());

        int klampin_index = findKlampin(n);
        if(klampin_index==MAX_KLAMPIN) {
          n.generation = compteur_global_generation;
          klampin[nklampin] = n;
          nklampin++;
          // System.out.println("add " + nklampin + ": " + n.prenom + " (" + n.generation + "," + klampin_par_generation[n.generation] + ")");
          return n;
        } else {
          klampin[klampin_index].type = n.type;
          return klampin[klampin_index];
        }
    }
    public void addLien(Klampin de, Klampin vers) {
        Lien e = new Lien();
        e.de   = findKlampin(de);
        e.vers = findKlampin(vers);
        e.type = vers.type;
        if((vers.type&2)==2) {
          e.longueur = 50;
          vers.generation = de.generation;
        } else {
          e.longueur = 100;
          vers.generation = de.generation + 1;
        }
        if((e.de<MAX_KLAMPIN)&&(e.vers<MAX_KLAMPIN)) {
          liens[nliens++] = e;
        }
    }
    public void parseParameter(String zob) {
        StringTokenizer exclamation   = new StringTokenizer(zob.trim(), "!");
        for( ; exclamation.hasMoreTokens() ; ) {
            String          branche       = exclamation.nextToken();
            StringTokenizer virgule       = new StringTokenizer(branche.trim(), ",");
            String          germe         = virgule.nextToken();
            Klampin         papa_ou_maman = addKlampin(germe.trim());
            compteur_global_generation    = getGeneration(papa_ou_maman);
            for( ; virgule.hasMoreTokens() ; ) {
                String  gus             = virgule.nextToken();
                Klampin enfant_ou_epoux = addKlampin(gus.trim());
                addLien(papa_ou_maman,enfant_ou_epoux);
                if((enfant_ou_epoux.type&2)==2) {
                    addLien(enfant_ou_epoux,papa_ou_maman);
                    liens[nliens-1].type=2;
                    papa_ou_maman.generation--;
                }
            }
        }
        // return(checkDbIntegrity());
    }
    public String checkDbIntegrity() {
        int rce = 0;
        int rcw = 0;
        String returnString;
        for (int i = 0 ; i < nliens ; i++) {
          // relations logiques
          if((liens[i].type&2)==2) {
              if(klampin[liens[i].de].generation!=klampin[liens[i].vers].generation) {
                  rce++; rcw++;
                  System.err.println(errorString + ": " + klampin[liens[i].de].prenom   + " (" + klampin[liens[i].de].generation   + ") != " +
                                     klampin[liens[i].vers].prenom + " (" + klampin[liens[i].vers].generation + "): " +
                                     generationMismatchString);
              }
          } else if(klampin[liens[i].de].generation!=(klampin[liens[i].vers].generation-1)) {
              rce++; rcw++;
              System.err.println(errorString + ": " + klampin[liens[i].de].prenom   + " (" + klampin[liens[i].de].generation   + ") > " +
                                 klampin[liens[i].vers].prenom + " (" + klampin[liens[i].vers].generation + "): " +
                                 generationMismatchString);
          }

          // relations d'ages
          if ((liens[i].type&2)==2) {
              if((klampin[liens[i].de].naissance>(klampin[liens[i].vers].naissance+20))||
                 (klampin[liens[i].de].naissance<(klampin[liens[i].vers].naissance-20))) {
                  rcw++;
                  System.err.println(warningString + ": " + klampin[liens[i].de].prenom   + " (" + klampin[liens[i].de].naissance   + "), " +
                                     klampin[liens[i].vers].prenom + " (" + klampin[liens[i].vers].naissance + ") " +
                                     marriageMismatchString);
              }
          } else if(klampin[liens[i].de].naissance>=klampin[liens[i].vers].naissance) {
              rce++; rcw++;
              System.err.println(errorString + ": " + klampin[liens[i].de].prenom   + " (" + klampin[liens[i].de].naissance   + ") >= " +
                                 klampin[liens[i].vers].prenom + " (" + klampin[liens[i].vers].naissance + ").");
          }
        }

        checkString = " (" + nklampin + "/" + MAX_KLAMPIN + "," + nliens + "/" + MAX_LIEN + ") " ;
        if((nklampin>=MAX_KLAMPIN)||(nliens>=MAX_LIEN)) {
          rce++; rcw++;
          System.err.println(errorString + ":" + checkString + tropDeMondeString);
        }


             if(rcw==0) returnString = checkString + "OK.";
        else if(rce==0) returnString = checkString + "KO:\n" + rcw + " " + warningString + detectedString;
        else            returnString = checkString + "KO:\n" + rce + "/" + (rcw-rce) + " " + errorString + "/" + warningString + detectedString;
        return returnString;
    }

    public int changeLaLangue(Vector vector, int index) {
      int i = index;

      warningString            = vector.elementAt(i++).toString();
      errorString              = vector.elementAt(i++).toString();
      detectedString           = vector.elementAt(i++).toString();
      generationMismatchString = vector.elementAt(i++).toString();
      marriageMismatchString   = vector.elementAt(i++).toString();
      tropDeMondeString        = vector.elementAt(i).toString();
      return i;
    }
}

