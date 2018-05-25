
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
/*---------------------------------------------------------------------------*\
      Class: Klampin
   Abstract: person
\*---------------------------------------------------------------------------*/
class Klampin {
    int  x;
    int  y;

    double  dx;
    double  dy;
    boolean affiche;        // display
    boolean affiche_D;      // utilise pour l'affichage de la descendance...
    boolean affiche_A;      // utilise pour l'affichage de l'ascendance...
    boolean epoux;          // utilise pour l'affichage de l'ascendance...

    int     souris;         // mouse

    String  prenom;         // first name
    // String  pays;           // country
    String  commentaire;    // comment
    int     type;           // 3 female spouse, 2 male spouse, 1 female Mariacher , 0 male mariacher
    int     generation;

    int     naissance;      // born
    int     deces;          // death

  public String toString() {
    String printed_node;
    String sexe;
    if((type&1)==1) sexe = "F: ";
    else            sexe = "M: ";
    if(deces == 9999) printed_node = sexe + prenom + "\n" + naissance + ".";
    else              printed_node = sexe + prenom + "\n" + naissance + "-" + deces + ".";
    return printed_node;
  }

}
