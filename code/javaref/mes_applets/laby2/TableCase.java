// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TableCase.java

import java.awt.*;
import java.io.PrintStream;
import java.util.Random;

public class TableCase
    implements Include
{

    TableCase(Laby laby1)
    {
        tc = new Case[21][21];
        laby = laby1;
    }

    public void peint(Pion apion[], Graphics g)
    {
        Dimension dimension = laby.size();
        g.clearRect(0, 0, dimension.width, dimension.height);
        for(int i = 0; i <= 20; i++)
        {
            for(int j = 0; j <= 20; j++)
            {
                if(tc[i][j] == null)
                {
                    i = 777;
                    break;
                }
                tc[i][j].peint(i, j, apion, dimension, g);
            }

        }

    }

    public Random genereTableCase(int i)
        throws Exception
    {
        Random random = new Random(i);
        for(int j = 0; j <= 20; j++)
        {
            for(int k = 0; k <= 20; k++)
            {
                tc[j][k] = null;
                Case case1 = new Case();
                tc[j][k] = case1;
            }

        }

        tc[10][10].frontiere[Math.abs(random.nextInt()) % 4] = true;
        laby.updateMap(laby.speedFactor);
        return random;
    }

    public int updateTableCase(Random random, int i)
        throws Exception
    {
        int j = 0;
        for(int k = 0; k < 20; k++)
        {
            for(int l = 0; l < 20; l++)
            {
                Case case1 = tc[k][l];
                try
                {
                    j += case1.updateCase(this, k, l, random, i);
                }
                catch(Exception exception)
                {
                    System.err.println("[" + k + "][" + l + "]: " + "  - " + case1.frontiere[0] + case1.frontiere[1] + case1.frontiere[2] + case1.frontiere[3]);
                    throw exception;
                }
            }

        }

        laby.updateMap(laby.speedFactor);
        return j;
    }

    public void nettoieTableCase()
        throws Exception
    {
        for(int i = 0; i < 20; i++)
        {
            for(int j = 0; j < 20; j++)
            {
                Service.trace("-[" + i + "][" + j + "]: " + "  - " + tc[i][j].frontiere[0] + tc[i][j].frontiere[1] + tc[i][j].frontiere[2] + tc[i][j].frontiere[3]);
                for(int k = 0; k < 4; k++)
                    switch(k)
                    {
                    default:
                        break;

                    case 0: // '\0'
                        if(i < 19 && (!tc[i + 1][j].frontiere[k] || !tc[i][j].frontiere[(k + 2) % 4]))
                        {
                            tc[i + 1][j].frontiere[k] = false;
                            tc[i][j].frontiere[(k + 2) % 4] = false;
                        }
                        break;

                    case 1: // '\001'
                        if(j < 19 && (!tc[i][j + 1].frontiere[k] || !tc[i][j].frontiere[(k + 2) % 4]))
                        {
                            tc[i][j + 1].frontiere[k] = false;
                            tc[i][j].frontiere[(k + 2) % 4] = false;
                        }
                        break;

                    case 2: // '\002'
                        if(i > 0 && (!tc[i - 1][j].frontiere[k] || !tc[i][j].frontiere[(k + 2) % 4]))
                        {
                            tc[i - 1][j].frontiere[k] = false;
                            tc[i][j].frontiere[(k + 2) % 4] = false;
                        }
                        break;

                    case 3: // '\003'
                        if(j > 0 && (!tc[i][j - 1].frontiere[k] || !tc[i][j].frontiere[(k + 2) % 4]))
                        {
                            tc[i][j - 1].frontiere[k] = false;
                            tc[i][j].frontiere[(k + 2) % 4] = false;
                        }
                        break;
                    }

                Service.trace(" [" + i + "][" + j + "]: " + "  - " + tc[i][j].frontiere[0] + tc[i][j].frontiere[1] + tc[i][j].frontiere[2] + tc[i][j].frontiere[3]);
            }

        }

        laby.updateMap(1000);
    }

    public boolean nextCase(Pion pion)
    {
        int i = pion.ouSuisJe[0];
        int j = pion.ouSuisJe[1];
        int k = pion.douViensJe;
        int l = pion.inc;
        boolean flag = true;
        int i1 = 777;
        Service.trace("   " + k + " (" + l + ") [" + i + "][" + j + "]: ");
        if(i == 0 || j == 0 || i == 20 || j == 20) {
	    pion.updateStat(laby.main.labelStat[pion.index]);
            return true;
	}
        Service.trace("   " + tc[i][j].toString(i, j));
        Service.trace("   " + tc[i - 1][j - 1].toString(i - 1, j - 1));
        for(int j1 = k; (j1 + 8) % 4 < 4 && (j1 + 8) % 4 >= 0; j1 += l)
        {
            int k1 = (j1 + 8) % 4;
            boolean flag1 = false;
            switch(k1)
            {
            case 0: // '\0'
                flag1 = tc[i - 1][j - 1].frontiere[3];
                break;

            case 1: // '\001'
                flag1 = tc[i - 1][j - 1].frontiere[2];
                break;

            case 2: // '\002'
                flag1 = tc[i][j].frontiere[1];
                break;

            case 3: // '\003'
                flag1 = tc[i][j].frontiere[0];
                break;
            }
            Service.trace("    " + k1 + " " + flag1 + " d3,f0-" + tc[i - 1][j - 1].frontiere[0] + " d2,f1-" + tc[i][j].frontiere[1] + " d1,f2-" + tc[i][j].frontiere[2] + " d0,f3-" + tc[i - 1][j - 1].frontiere[3]);
            if(!flag1)
            {
                int ai[] = new int[2];
                ai[0] = i;
                ai[1] = j;
                switch(k1)
                {
                case 0: // '\0'
                    ai[0]--;
                    break;

                case 1: // '\001'
                    ai[1]--;
                    break;

                case 2: // '\002'
                    ai[0]++;
                    break;

                case 3: // '\003'
                    ai[1]++;
                    break;
                }
                if(flag && tc[ai[0]][ai[1]].pionDejaPasse[pion.index])
                {
                    if(i1 == k1)
                        flag = false;
                    if(i1 == '\u0309')
                        i1 = k1;
                } else
                {
                    tc[pion.ouSuisJe[0]][pion.ouSuisJe[1]].pionDejaPasse[pion.index] = true;
                    pion.ouSuisJe = ai;
                    pion.douViensJe = (k1 + 2) % 4;
                    return false;
                }
            }
        }

        return false;
    }

    Laby laby;
    Case tc[][];
}
