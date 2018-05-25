
public class Log implements Include {
    int     regionIndex;
    boolean choix;
    int     couleurChoix[]   = new int[5];
    int     couleurChoisie[] = new int[5];
    int     nbreCouleurChoix;

    Log(int regionIndex, int c[]) {
        this.regionIndex = regionIndex;
        couleurChoix[4] = ZOB;
        nbreCouleurChoix = 0;
        for(int i=0; i<4; i++) {
            couleurChoisie[i] = ZOB;
            couleurChoix[i]   = c[i];
            if(c[i]!=ZOB) nbreCouleurChoix++;
        }
        if(nbreCouleurChoix>0) choix = true;
        else                   choix = false;
    }

    void Reset() {
        regionIndex      = ZOB;
        choix            = false;
        nbreCouleurChoix = 0;
        for(int i=0; i<4; i++) {
            couleurChoix[i]   = ZOB;
            couleurChoisie[i] = ZOB;
        }
    }


    int nextCouleur() {
        for(int i=0; i<4; i++) {
            if((couleurChoix[i]!=ZOB)&&(couleurChoisie[i]==ZOB)) {
                couleurChoisie[i] = 1;
                nbreCouleurChoix--;
                if(nbreCouleurChoix>0) choix = true;
                else                   choix = false;
                return couleurChoix[i];
            }
        }
        return ZOB;
    }

    String toString(int logIndex) {
        String zurg = "Log[" + logIndex + "]: Region[" + regionIndex + "]: " ;

        for(int i=0;i<4;i++) {
            if(couleurChoix[i]!=ZOB) {
                zurg = zurg + ", " + i + ":" + couleurChoix[i] + "/" + couleurChoisie[i] ;
            }
        }
        if(choix) zurg = zurg + " choix possible.";
        else      zurg = zurg + " choix impossible.";

        return zurg;
    }
}
