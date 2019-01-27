class Reponse implements MMInclude {
    int blanc;
    int noir;

    public boolean mieuxOuEgal(Reponse prec) {
        return((blanc==prec.blanc)&&(noir==prec.noir));
    }

    public boolean trouve() {
        if(noir==NBRE_PION) return true;
        else                return false;
    }

    public String toString() {
        return (new String("blanc:" + blanc + ", noir:" + noir));
    }
}

