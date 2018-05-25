// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PionFou.java

import java.awt.Color;
import java.util.Random;

class PionFou extends Pion
    implements Include
{

    PionFou(int i, int j, int k, int l, int i1, int j1, Color color, String titre)
    {
        super(i, j, k, l, i1, j1, color, titre);
        hasard = new Random(i1);
    }

    protected int getInc()
    {
        int i = 1 - (Math.abs(hasard.nextInt()) % 2) * 2;
        return i;
    }

    Random hasard;
}
