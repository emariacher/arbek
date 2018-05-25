// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Pion.java

import java.awt.*;

class Pion
    implements Include
{

    Pion(int i, int j, int k, int l, int i1, int j1, Color color, String titre_)
    {
        ouSuisJe = new int[2];
        auDepart = new int[2];
        stat     = new int[3];
	stat[0]  = 7777;
        auDepart[0] = i;
        auDepart[1] = j;
        douViensJe = k;
        couleur = color;
        index = l;
        inc = i1;
        rad = j1;
	active = false;
	gagne = 0;
	titre = titre_;
        Service.trace(getClass().getName() + " " + toString());
    }

    public String toString()
    {
        return new String(index + " [" + ouSuisJe[0] + "," + ouSuisJe[1] + "] " + douViensJe + " (" + inc + ")");
    }

    public void peint(Dimension dimension, Graphics g)
    {
        double d = (ouSuisJe[1] * dimension.width) / 20;
        double d1 = (ouSuisJe[0] * dimension.height) / 20;
        g.setColor(couleur);
        g.fillOval((int)(d - (double)rad), (int)(d1 - (double)rad), 2 * rad, 2 * rad);
    }

    public void updateStat(Label label)
    {
	if(compteurCourant<stat[0]) stat[0]=compteurCourant;  
	if(compteurCourant>stat[2]) stat[2]=compteurCourant;
	stat[1] = ((stat[1] * gagne) + compteurCourant) / (gagne+1);
	gagne++;
	label.setForeground(couleur);
	label.setText(titre + gagne + " [" + stat[0] + "/" + stat[1] + "/" + stat[2] + "]");
    }

    public void set()
    {
        ouSuisJe[0] = auDepart[0];
        ouSuisJe[1] = auDepart[1];
	compteurCourant = 0;
	active = true;
    }

    public void reset()
    {
	active = false;
    }

    protected int getInc()
    {
        return inc;
    }

    public boolean next(TableCase tablecase)
    {
        inc = getInc();
	compteurCourant++;
        return tablecase.nextCase(this);
    }

    int douViensJe;
    int ouSuisJe[];
    int auDepart[];
    int stat[];
    int index;
    int inc;
    int rad;
    int gagne;
    int compteurCourant;
    boolean active;
    String titre;
    Color couleur;
}
