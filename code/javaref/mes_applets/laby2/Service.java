// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Service.java

import java.io.PrintStream;

public class Service
    implements Include
{

    public static void trace(String s)
    {
    }

    public static void trace(int i, String s)
    {
        switch(i)
        {
        case 1: // '\001'
            System.out.println(s);
            return;

        case 2: // '\002'
            System.out.print(s);
            return;
        }
    }

    public Service()
    {
    }
}
