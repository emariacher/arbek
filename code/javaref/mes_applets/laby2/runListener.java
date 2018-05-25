// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Main.java

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

class runListener
    implements ActionListener
{

    runListener(Main main)
    {
        c = main;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(((Button)actionevent.getSource()).getLabel().equals("Stop"))
        {
            ((Button)actionevent.getSource()).setLabel("Run");
            c.runFreely = false;
            return;
        } else
        {
            ((Button)actionevent.getSource()).setLabel("Stop");
            c.runFreely = true;
            c.thread.resume();
            return;
        }
    }

    Main c;
}
