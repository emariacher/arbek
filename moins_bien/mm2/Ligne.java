import java.util.*;

class Ligne implements MMInclude {
    int     pion[]   = new int[NBRE_PION];
    boolean touche[] = new boolean[NBRE_PION];
    Random  hasard;

    Ligne(int graine) {
        hasard = new Random(graine);
        for(int i=0; i<NBRE_PION; i++) pion[i] = (Math.abs(hasard.nextInt()))%NBRE_COULEUR;
    }

    public void invalide() {
        for(int zob = 0; zob < NBRE_PION; zob++) {
            pion[zob] = INVALID;
        }
    }

    public boolean estInvalide() {
        for(int zob = 0; zob < NBRE_PION; zob++) {
            if(pion[zob] == INVALID) return true;
        }
        return false;
    }

    public boolean valide(Ligne precLigne, Reponse precReponse) {
        return(compare(precLigne).mieuxOuEgal(precReponse));
    }

    public boolean valide(int index, Ligne precLigne, Reponse precReponse) {
        Reponse resultat = compare(precLigne);
        System.out.println("   [" + index + "] " + precLigne.toString() + " "  + precReponse.toString() + " -> " + resultat.toString());
        return(resultat.mieuxOuEgal(precReponse));
    }

    public Reponse compare(Ligne essai) {
        Reponse resultat = new Reponse();
        for(int i=0; i<NBRE_PION; i++) {
                  touche[i] = false;
            essai.touche[i] = false;
        }
        for(int i=0; i<NBRE_PION; i++) {
            for(int j=0; j<NBRE_PION; j++) {
                if((essai.pion[i]==pion[j])&&(!touche[j])&&(!essai.touche[i])&&(i==j)) {
                    resultat.noir++;
                    essai.touche[i] = true;
                    touche[j]       = true;
                }
            }
        }
        for(int i=0; i<NBRE_PION; i++) {
            for(int j=0; j<NBRE_PION; j++) {
                if((essai.pion[i]==pion[j])&&(!touche[j])&&(!essai.touche[i])) {
                    resultat.blanc++;
                    essai.touche[i] = true;
                    touche[j]       = true;
                }
            }
        }
        return resultat;
    }

    public String toString() {
        String zob = new String(" ");
        for(int i=0; i<NBRE_PION; i++) zob = zob + pion[i] + " ";
        return zob;
    }
}

