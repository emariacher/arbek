// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Case.java

import java.awt.*;
import java.util.Random;

class Case
    implements Include
{

    public String toString(int i, int j)
    {
        String s = new String("[" + i + ", " + j + "]: ");
        for(int k = 0; k < 4; k++)
            s = s + frontiere[k];

        s = s + " ";
        for(int l = 0; l < 3; l++)
            s = s + pionDejaPasse[l];

        return s;
    }

    public void peint(int i, int j, Pion apion[], Dimension dimension, Graphics g)
    {
        double d = (((double)j + 0.5D) * (double)dimension.width) / 20D;
        double d1 = (((double)i + 0.5D) * (double)dimension.height) / 20D;
        double d2 = dimension.width / 40;
        double d3 = dimension.height / 40;
        g.setColor(Color.black);
        if(frontiere[0])
            g.drawLine((int)d, (int)d1, (int)d, (int)(d1 - d3));
        if(frontiere[1])
            g.drawLine((int)d, (int)d1, (int)(d - d2), (int)d1);
        if(frontiere[2])
            g.drawLine((int)d, (int)d1, (int)d, (int)(d1 + d3));
        if(frontiere[3])
            g.drawLine((int)d, (int)d1, (int)(d + d2), (int)d1);
        for(int k = 0; k < 3; k++)
            if(pionDejaPasse[k])
            {
                g.setColor(apion[k].couleur);
                int l = apion[k].rad;
                g.drawOval((int)(d - d2 - (double)l), (int)(d1 - d3 - (double)l), 2 * l, 2 * l);
            }

    }

    public int updateCase(TableCase tablecase, int i, int j, Random random, int k)
    {
        int l = 0;
        int i1 = 0;
        for(int l1 = 0; l1 < 4; l1++)
            if(frontiere[l1])
                l++;

        int j1 = l;
        Service.trace("-[" + i + "][" + j + "]: " + "(" + j1 + ") " + frontiere[0] + frontiere[1] + frontiere[2] + frontiere[3]);
        if(j1 == 0)
        {
            int i2 = Math.abs(random.nextInt());
            for(int k2 = 0; k2 < 4; k2++)
            {
                int j3 = (i2 + k2) % 4;
                switch(j3)
                {
                case 0: // '\0'
                    if(i < 19 && tablecase.tc[i + 1][j].frontiere[j3] && Math.abs(random.nextInt()) % 100 < 100)
                    {
                        frontiere[(j3 + 2) % 4] = true;
                        j1++;
                    }
                    break;

                case 1: // '\001'
                    if(j < 19 && tablecase.tc[i][j + 1].frontiere[j3] && Math.abs(random.nextInt()) % 100 < 100)
                    {
                        frontiere[(j3 + 2) % 4] = true;
                        j1++;
                    }
                    break;

                case 2: // '\002'
                    if(i > 0 && tablecase.tc[i - 1][j].frontiere[j3] && Math.abs(random.nextInt()) % 100 < 100)
                    {
                        frontiere[(j3 + 2) % 4] = true;
                        j1++;
                    }
                    break;

                case 3: // '\003'
                    if(j > 0 && tablecase.tc[i][j - 1].frontiere[j3] && Math.abs(random.nextInt()) % 100 < 100)
                    {
                        frontiere[(j3 + 2) % 4] = true;
                        j1++;
                    }
                    break;
                }
                if(j1 > 0)
                    break;
            }

            l = 5;
        }
        int k1 = Math.abs(random.nextInt());
        int j2 = k1 % 10000;
        Service.trace("zub=" + k1 + ", zab=" + j2 + ", cpt =" + j1);
        if(j1 == 0 && j2 < 2)
        {
            int l2 = Math.abs(random.nextInt()) % 4;
            switch(l2)
            {
            default:
                break;

            case 0: // '\0'
                if(i < 19)
                    frontiere[(l2 + 2) % 4] = true;
                j1++;
                break;

            case 1: // '\001'
                if(j < 19)
                    frontiere[(l2 + 2) % 4] = true;
                j1++;
                break;

            case 2: // '\002'
                if(i > 0)
                    frontiere[(l2 + 2) % 4] = true;
                j1++;
                break;

            case 3: // '\003'
                if(j > 0)
                    frontiere[(l2 + 2) % 4] = true;
                j1++;
                break;
            }
        }
        int i3;
        int k3;
        if(k != 777)
        {
            i3 = ((400 - k) * 30) / 400;
            k3 = ((400 - k) * 15) / 400;
        } else
        {
            i3 = 10;
            k3 = 2;
        }
        k1 = Math.abs(random.nextInt());
        if(j1 == 1 && k1 % 100 < 67 || j1 == 2 && k1 % 100 < i3 || j1 == 3 && k1 % 100 < k3)
        {
            int l3 = Math.abs(random.nextInt()) % 4;
            switch(l3)
            {
            default:
                break;

            case 0: // '\0'
                if(i < 19 && !tablecase.tc[i + 1][j].frontiere[l3])
                    frontiere[(l3 + 2) % 4] = true;
                break;

            case 1: // '\001'
                if(j < 19 && !tablecase.tc[i][j + 1].frontiere[l3])
                    frontiere[(l3 + 2) % 4] = true;
                break;

            case 2: // '\002'
                if(i > 0 && !tablecase.tc[i - 1][j].frontiere[l3])
                    frontiere[(l3 + 2) % 4] = true;
                break;

            case 3: // '\003'
                if(j > 0 && !tablecase.tc[i][j - 1].frontiere[l3])
                    frontiere[(l3 + 2) % 4] = true;
                break;
            }
        }
        Service.trace(" [" + i + "][" + j + "]: " + "(" + j1 + ") " + frontiere[0] + frontiere[1] + frontiere[2] + frontiere[3]);
        for(int i4 = 0; i4 < 4; i4++)
            if(frontiere[i4])
                i1++;

        return i1 == l ? 0 : 1;
    }

    Case()
    {
        frontiere = new boolean[4];
        pionDejaPasse = new boolean[3];
    }

    boolean frontiere[];
    boolean pionDejaPasse[];
}
