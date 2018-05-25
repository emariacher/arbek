// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Laby.java

import java.awt.*;
import java.io.PrintStream;
import java.util.Date;
import java.util.Random;

public class Laby extends Panel
implements Runnable, Include {

	Laby(Main main1)
	{
		tableCase = new TableCase(this);
		tp = new Pion[MAXPION];
		main = main1;
		speedFactor = 100;
	}

	public void run()
	{
		try {
			Pion pion = new Pion(10, 10, 0, 0, -1, 8, Color.green, "vert va à tribord ");
			tp[0] = pion;
			pion = new Pion(10, 10, 0, 1, 1, 6, Color.red, "rouge va à babord ");
			tp[1] = pion;
			PionFou pionfou = new PionFou(10, 10, 0, 2, seed, 4, Color.blue, "bleu est fou ");
			tp[2] = pionfou;
			do {
				Date date = new Date();
				seed = date.getSeconds() + date.getMinutes() * 60 + date.getHours() * 24 * 60;
				System.out.println("\033[31m seed: " + seed + "\033[34m H=" + 20 + ", W=" + 20);
				hasard = tableCase.genereTableCase(seed);
				for (int i = 777; i > 1; i = tableCase.updateTableCase(hasard, i));
				Service.trace("fin!");
				tableCase.nettoieTableCase();
				Service.trace("nettoye!");
				for (int j = 0; j < MAXPION; j++)
					tp[j].set();
				int count = 0, wait;
				for (boolean flag = false; !flag;) {
					count++;
					main.labelCpt.setText(" " + count);
					for (int j = 0; j < MAXPION; j++)
						if (tp[j].next(tableCase))
							flag = true;
						if(speedFactor<=0) speedFactor=1;
					wait = (10*speedFactor) - (count*20);
					if (wait < speedFactor)	wait = speedFactor;
					updateMap(wait);
				}

				updateMap(count*speedFactor);
				for (int j = 0; j < MAXPION; j++)
					tp[j].reset();
			} while (true);
		} catch (Exception exception) {
			System.err.println(exception);
			exception.printStackTrace();
			return;
		}
	}

	public void updateMapNoSuspend(int i)
	throws Exception
	{
		Service.trace("updateMapNoSuspend");
		repaint();
		Thread.sleep(i);
	}

	public void updateMapNoSuspend()
	throws Exception
	{
		Service.trace("updateMapNoSuspend");
		repaint();
		Thread.sleep(500L);
	}

	public void updateMap(int i)
	throws Exception
	{
		Service.trace("updateMap");
		if (!main.runFreely)
			main.thread.suspend();
		repaint();
		Thread.sleep(i);
	}

	public void updateMap()
	throws Exception
	{
		Service.trace("updateMap");
		if (!main.runFreely)
			main.thread.suspend();
		repaint();
		Thread.sleep(500L);
	}

	public void update(Graphics g)
	{
		Service.trace("update");
		Dimension dimension = size();
		if (offscreen == null || dimension.width != offscreensize.width || dimension.height != offscreensize.height) {
			offscreen = createImage(dimension.width, dimension.height);
			offscreensize = dimension;
			offgraphics = offscreen.getGraphics();
			offgraphics.setFont(getFont());
		}
		tableCase.peint(tp, offgraphics);
		for (int i = 0; i < MAXPION; i++)
			if (tp[i].active)
				tp[i].peint(dimension, offgraphics);

		g.drawImage(offscreen, 0, 0, null);
	}

	Main main;
	TableCase tableCase;
	Pion tp[];
	final int default_wait = 500;
	Random hasard;
	int seed;
	int speedFactor;
	private Image offscreen;
	private Dimension offscreensize;
	private Graphics offgraphics;
}
