// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Main.java

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class stepListener
    implements ActionListener
{

    stepListener(Main main)
    {
        c = main;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        c.thread.resume();
    }

    Main c;
}
