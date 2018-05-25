/*---------------------------------------------------------------------------*\
 *
 * Copyright (c) 1998-2000 Eric Mariacher. All Rights Reserved.
 *
 * Eric Mariacher grants you ("Licensee") a non-exclusive, royalty free
 * and license to use
 * this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Eric Mariacher.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. ERIC MARIACHER AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL ERIC MARIACHER OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF ERIC MARIACHER HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.

     Author: E.Mariacher
   Creation: 8 Juin 1998
   Abstract: Genealogie
 Assumption: -1- not Y10K ready!
             -2- branches must be given going from the oldest generation to the youngest
                 (no order required if 2 branches are of the same generation).

\*---------------------------------------------------------------------------*/
/*---------------------------------------------------------------------------*\
 *
 * Copyright (c) 1994-1996 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
\*---------------------------------------------------------------------------*/
import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;


/*
 * This class adds event handling to MenuLookDemo.
 */
public class menuGen extends JApplet
                      implements ActionListener, ItemListener {
    JTextArea            output;
    JScrollPane          scrollPane;
    String               zob, newline = "\n";
    String               zub = null;
    int                  inc;
    monSlider            annee1;
    monSlider            annee2;
    afficheGen           affGen;
    dbGen                databaseGen;
    JMenuItem            aide, apropos, resetSelect, poubelle;
    JCheckBoxMenuItem    gele;
    JRadioButtonMenuItem france, amerique;
    JMenu                menu, menu_action;
    JMenuBar             menuBar;
    Dimension            d;
    Vector               liste_objets      = null;
    Vector               chaine_americaine = null;
    Vector               chaine_francaise  = null;
    private int          FRANCAIS  = 1;
    private int          AMERICAIN = 2;
    int                  langage;
    ButtonGroup          group = new ButtonGroup();
    JTextArea            statusText;
    JTextField           inputText;
    JLabel               statusTextLabel, inputTextLabel;

    public void init() {
        String init_msg = "Eric Mariacher menuGen V1.0 Copyright 1998, 1999, 2000.\nInteractive Genealogy tree display.";

        System.out.println(init_msg);
        System.err.println(init_msg);
        showStatus("Init menuGen");
        setBackground(Color.lightGray);

        // liste des objets pour le changement de langue des textes
        liste_objets      = new Vector();
        chaine_americaine = new Vector();
        chaine_francaise  = new Vector();

        // create the database & panel
        showStatus("Create affGen & databaseGen.");
        databaseGen = new dbGen();
        ajoute_aux_listes_de_langages(databaseGen,"Avertissement","Warning");
        ajoute_aux_listes_de_langages(databaseGen,"Erreur","Error");
        ajoute_aux_listes_de_langages(databaseGen," detecte(s),\n ouvrez la console Java pour plus de details.",
                                                  " detected,\n open the Java console for more details.");
        ajoute_aux_listes_de_langages(databaseGen,"incoherence de generation.","generation mismatch.");
        ajoute_aux_listes_de_langages(databaseGen,"ont une grande difference d\'age\nbien qu\'ils soient marries.",
                                                  "are considerably older/younger to one another\nthough they are married.");
        ajoute_aux_listes_de_langages(databaseGen," trop de monde (personnes/maximum, liens_entre_personnes/maximum).",
                                                  " too many people (persons/maximum, links_between_persons/maximum).");
        affGen      = new afficheGen(this,databaseGen);
        ajoute_aux_listes_de_langages(affGen,"vivante","alive");
        ajoute_aux_listes_de_langages(affGen,"vivant","alive");
        ajoute_aux_listes_de_langages(affGen," personnes trouvees\n  contenant la chaine:  \n"," persons found matching:  \n");
        ajoute_aux_listes_de_langages(affGen,"Aucune personne\n  contenant la chaine:  \n","No one matching:  \n");

        //Add the panel
        final Container contentPane = getContentPane();
        contentPane.setBackground(Color.lightGray);
        contentPane.add(affGen, BorderLayout.CENTER);

        //Create the menu bar.
        showStatus("Create Menu bar.");
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.setBackground(Color.lightGray);

        //Build the action menu.
        showStatus("Create Action menu.");
        menu_action = new JMenu();
        menu_action.setBackground(Color.lightGray);
        ajoute_aux_listes_de_langages(menu_action,"Action","Action");
        menu_action.getPopupMenu().setLightWeightPopupEnabled(false);
        menuBar.add(menu_action);

        try {
            france      = new JRadioButtonMenuItem(new ImageIcon("FRAN001.GIF"));
        } catch(Exception e) {
            france      = new JRadioButtonMenuItem("Francais");
        }
        france.setBackground(Color.lightGray);
        france.addActionListener(this); menu_action.add(france); group.add(france);

        try {
            amerique    = new JRadioButtonMenuItem(new ImageIcon("UNST001.GIF"));
        } catch(Exception e) {
            amerique    = new JRadioButtonMenuItem("American");
        }
        amerique.setBackground(Color.lightGray);
        amerique.addActionListener(this); menu_action.add(amerique); group.add(amerique);

        gele        = new JCheckBoxMenuItem("Freeze",affGen.freeze);
        gele.setBackground(Color.lightGray);
        gele.addActionListener(this); menu_action.add(gele);
        ajoute_aux_listes_de_langages(gele,"Gele","Freeze");

        resetSelect = new JMenuItem();
        resetSelect.setBackground(Color.lightGray);
        resetSelect.addActionListener(this); menu_action.add(resetSelect);
        ajoute_aux_listes_de_langages(resetSelect,"Voir tout le monde","See Everyone");

        //Build the ? menu.
        showStatus("Create Help menu.");
        menu = new JMenu();
        menu.setBackground(Color.lightGray);
        ajoute_aux_listes_de_langages(menu,"?","?");
        menu.getPopupMenu().setLightWeightPopupEnabled(false);
        menuBar.add(menu);


        aide        = new JMenuItem();
        aide.setBackground(Color.lightGray);
        aide.addActionListener(this); menu.add(aide);
        ajoute_aux_listes_de_langages(aide,"Aide","Help");
        poubelle    = new JMenuItem();
        ajoute_aux_listes_de_langages(poubelle,"Visitez mon site","Visit me");

        apropos     = new JMenuItem();
        apropos.setBackground(Color.lightGray);
        apropos.addActionListener(this); menu.add(apropos);
        ajoute_aux_listes_de_langages(apropos,"A propos de","About");

        // create the panel for sliders & text field
        Panel gp  = new Panel(); // panel for sliders & text fields
        contentPane.add(gp, BorderLayout.SOUTH);
        d = getSize();

        GridBagLayout      gridbag = new GridBagLayout();
        GridBagConstraints c       = new GridBagConstraints();
        gp.setLayout(gridbag);

        // create text fields
        showStatus("Create Text fields.");
        inputTextLabel  = new JLabel();
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridbag.setConstraints(inputTextLabel, c);
        gp.add(inputTextLabel);
        ajoute_aux_listes_de_langages(inputTextLabel,"Selection","Select");

        statusTextLabel = new JLabel();
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(statusTextLabel, c);
        gp.add(statusTextLabel);
        ajoute_aux_listes_de_langages(statusTextLabel,"Statut","Status");

        inputText = new JTextField(20);
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridbag.setConstraints(inputText, c);
        gp.add(inputText);
        inputText.addActionListener(new monActionListener(affGen));
        statusText = new JTextArea(3,40);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(statusText, c);
        gp.add(statusText);

        // parse the input
        zob = getParameter("zob");
        if(zob!=null) {
            databaseGen.parseParameter(zob);
        } else {
            System.err.println("[30m 0 Klampin![34m");
            showStatus("0 Klampin!");
        }

        // create the sliders
        showStatus("Create sliders.");

             if((databaseGen.fin-databaseGen.debut)<200) inc = 20;
        else if((databaseGen.fin-databaseGen.debut)<500) inc = 50;
        else                                             inc = 100;
        // System.out.println(titre + ": " + fin + "-" + debut + ", " + inc);
        databaseGen.debut = databaseGen.debut - (databaseGen.debut%inc);
        databaseGen.fin   = databaseGen.fin   - (databaseGen.fin  %inc) + inc + 1;

        annee1 = jslider("anne1", databaseGen.debut, databaseGen.fin, databaseGen.debut, d);
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridbag.setConstraints(annee1.label, c);
        gp.add(annee1.label);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(annee1.slider, c);
        gp.add(annee1.slider);
        ajoute_aux_listes_de_langages(annee1.label,"annee1","year1");

        annee2 = jslider("anne2", databaseGen.debut, databaseGen.fin, databaseGen.fin  , d);
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridbag.setConstraints(annee2.label, c);
        gp.add(annee2.label);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(annee2.slider, c);
        gp.add(annee2.slider);
        ajoute_aux_listes_de_langages(annee2.label,"annee2","year2");

        annee1.slider.addChangeListener(new SliderListener(annee1, annee2, affGen));
        annee2.slider.addChangeListener(new SliderListener(annee1, annee2, affGen));
        affGen.borne_inf = databaseGen.debut;
        affGen.borne_sup = databaseGen.fin;
        annee1.borne    = databaseGen.debut;
        annee2.borne    = databaseGen.fin;

        // choose the default language
        zob = getParameter("langage");
        if((zob!=null)&&(zob.equals("americain"))) {
            change_de_langue(liste_objets, chaine_americaine);
            amerique.setSelected(true);
            langage = AMERICAIN;
        } else {
            change_de_langue(liste_objets, chaine_francaise);
            france.setSelected(true);
            langage = FRANCAIS;
        }


        // resultat du checking de l'integrite de la database
        zub = databaseGen.checkDbIntegrity();
        if(zub!=null) statusText.setText(zub);
    }

    void change_de_langue(Vector liste_des_objets, Vector chaine_de_msg) {
        showStatus("Change language.");
        Class laClasse, laSuperClasse, laClasseJMenuItem;
        for (int i = 0 ; i < liste_des_objets.size(); i++) {
            // System.out.println("[34m liste_des_objets[" + i + "]:");
            if(liste_des_objets.elementAt(i)!=null) {
                laClasse                = liste_des_objets.elementAt(i).getClass();
                laSuperClasse           = laClasse.getSuperclass();
                laClasseJMenuItem       = (new JMenuItem()).getClass();
                // System.out.println("[30m  " + laClasse.getName() + "/" + laSuperClasse.getName());
                if( (laSuperClasse.equals(laClasseJMenuItem)) || (laClasse.equals(laClasseJMenuItem)) ) { // JmenuItem superclass is abstractbutton which has the setText method defined
                    ((AbstractButton)liste_des_objets.elementAt(i)).setText(chaine_de_msg.elementAt(i).toString());
                } else if(laClasse.equals((new JLabel()).getClass())) {
                    ((JLabel)liste_des_objets.elementAt(i)).setText(chaine_de_msg.elementAt(i).toString());
                } else if(laClasse.equals(affGen.getClass())) {
                    i = ((afficheGen)liste_des_objets.elementAt(i)).changeLaLangue(chaine_de_msg,i);
                } else if(laClasse.equals(databaseGen.getClass())) {
                    i = ((dbGen)liste_des_objets.elementAt(i)).changeLaLangue(chaine_de_msg,i);
                } else {
                    System.err.println("------> No Good!");
                }
            } else System.err.println("[34m  liste_des_objets[" + i + "] is null! liste_des_objets has " + liste_des_objets.size()  + " elements.");
        }
    }

    public void start() { affGen.start(); }
    public void stop()  { affGen.stop();  }

    public void actionPerformed(ActionEvent e) {
        String msg = "ZOB!";
        JMenuItem source = (JMenuItem)(e.getSource());
        // String s = "Action event detected." + newline + "    Event source: " + source.getText() + " (an instance of " + getClassName(source) + ")";
        // output.append(s + newline);
        if(source.equals(apropos)) {
           URL         url  = null;
           JOptionPane pane = new JOptionPane();
           pane.setBackground(Color.lightGray);
           pane.setForeground(Color.red);
                 if(langage==FRANCAIS)  msg = "Cette appliquette \"d\'affichage de genealogie\" a ete ecrite par Eric Mariacher.\nV1.0";
            else if(langage==AMERICAIN) msg = "This \"genealogy display\" applet was written by Eric Mariacher.\nV1.0";
            Object[] options = { "OK", poubelle.getText(), "emariacher@france-mail.com" };
            showStatus("Click on window button and Open Java Console to see what\'s supposed to be displayed.");
            // System.out.println("This is what is supposed to be displayed when opening the window " + source.getText() + ":");
            // System.out.println(msg);
            // System.out.println("The 3 buttons are: OK, " + poubelle.getText() + "and " + "emariacher@france-mail.com");
            int n = pane.showOptionDialog(null, msg,
                                          (source.getText() + "..."),
                                          JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (n == JOptionPane.YES_OPTION) {
                // System.out.println("YES!");
            } else if (n == JOptionPane.NO_OPTION) {
                // System.out.println("NO!");
                try {
                    url = new URL("http", "www.multimania.com", 80, "/mariacr");
                } catch (MalformedURLException u) {
                    System.err.println(u);
                }
                getAppletContext().showDocument(url,"_blank");
            } else {
                // System.out.println("ELSE!");
                try {
                    url = new URL("mailto:emariacher@france-mail.com");
                } catch (MalformedURLException u) {
                    System.err.println(u);
                }
                getAppletContext().showDocument(url,"_blank");
            }
        } else if(source.equals(aide)) {
          JOptionPane pane = new JOptionPane();
          pane.setBackground(Color.lightGray);
          pane.setForeground(Color.red);
          if(langage==FRANCAIS)
                 msg = "- 2 personnes sont de la meme couleur si elles appartiennent a la meme generation.\n" +
                       "- Les ovales sont du sexe feminin & les rectangles sont du sexe masculin.\n" +
                       "- Quand 2 personnes sont reliees elles sont mariees, parent ou enfant de l\'une ou l\'autre.\n" +
                       "- Si 2 personnes sont proches l\'une de l\'autre, c'est qu\'elles sont mariees.\n" +
                       "- Cliquer avec le bouton gauche sur un rectangle ou un ovale vous permet de deplacer cet objet\n" +
                       "  et d'avoir plus d\'information:\n" +
                       "   * prenom & nom\n" +
                       "   * date de naissance\n" +
                       "   * date de deces ou vivant\n" +
                       "- Cliquer avec le bouton droit sur un rectangle ou un ovale vous permet de voir uniquement les ascendants\n" +
                       "  et descendants directs + l\'epoux de cette personne.\n" +
                       "- Le deplacement des curseurs (" + annee1.label.getText() + "&" + annee2.label.getText() + ") vous permet de voir qui etait vivant entre\n" +
                       "  les 2 dates.\n" +
                       "- Gelez tout quand vous etes contents!\n" +
                       "- Recherchez une ou plusieurs personnes en tapant une chaine de caracteres que leur nom contient\n" +
                       "  dans le champ " + inputTextLabel.getText() + ".\n" +
                       " ";
          else if (langage==AMERICAIN)
                 msg = "- 2 people have the same color if they belong to the same generation.\n" +
                       "- ovals are females & rectangles are males.\n" +
                       "- when 2 people are linked that means that they are either married, parent or child of one another.\n" +
                       "- if 2 people are close to one another it means they are married.\n" +
                       "- clicking with the left button on rectangle or an oval enables you to move this item and gives more info:\n" +
                       "   * firstname & name\n" +
                       "   * date of birth\n" +
                       "   * date of death or alive\n" +
                       "- clicking with the right button on rectangle or an oval enables you to see only direct ancestors and next generations\n" +
                       "  of this person.\n" +
                       "- moving the cursors (" + annee1.label.getText() + "&" + annee2.label.getText() + ") enables you to see who was alive between\n" +
                       "  the earliest date and the latest.\n" +
                       "- freeze everything when you are happy.\n" +
                       "- Find one or several people by typing a string of characters that their name contains" +
                       "  in the field " + inputTextLabel.getText() + ".\n" +
                       " ";
          pane.showMessageDialog(null, msg, (source.getText() + "..."), JOptionPane.INFORMATION_MESSAGE);
            showStatus("Click on window left button and Open Java Console to see what\'s supposed to be displayed.");
            // System.out.println("This is what is supposed to be displayed when opening the window " + source.getText() + ":");
            // System.out.println(msg);
            // System.out.println("The button is: OK.");
        } else if(source.equals(gele)) {
          affGen.freeze = !affGen.freeze; // toggle freeze
        } else if(source.equals(france)) {
          change_de_langue(liste_objets, chaine_francaise);
          langage = FRANCAIS;
        } else if(source.equals(amerique)) {
          change_de_langue(liste_objets, chaine_americaine);
          langage = AMERICAIN;
        } else if(source.equals(resetSelect)) {
          affGen.indexDuKlampinCentre = databaseGen.MAX_KLAMPIN;

          affGen.StrMatch = null;
          inputText.setText("");

          Integer zab = new Integer(databaseGen.debut); annee1.label.setText(zab.toString());
          annee1.slider.setValue(databaseGen.debut);
          Integer zub = new Integer(databaseGen.fin);   annee2.label.setText(zub.toString());
          annee2.slider.setValue(databaseGen.fin);
        } else {
          System.err.println("[30m" + source.getText() + "[34m" + e);
        }
    }

    public void itemStateChanged(ItemEvent e) { }

    monSlider jslider(String titre, int debut, int fin, int borne, Dimension din) {
        monSlider leSlider = new monSlider();

        JLabel label = new JLabel("");
        // JLabel label = new JLabel("", JLabel.CENTER);
        // label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider annee = new JSlider(JSlider.HORIZONTAL, debut, fin, borne);
        annee.setMajorTickSpacing(inc);
        annee.setMinorTickSpacing(inc/4);
        annee.setPaintTicks(true);
        annee.setPaintLabels(true);
        annee.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        annee.setName(titre);
        Dimension d = annee.getPreferredSize();
        d.width=(int)(din.width/1.5);
        annee.setPreferredSize(d);

        leSlider.label  = label;
        leSlider.slider = annee;
        leSlider.slider.setBackground(Color.lightGray);
        return leSlider;
    }
    void ajoute_aux_listes_de_langages(Object objet, String fr, String us) {
        liste_objets.addElement(objet);
        chaine_francaise.addElement(fr);
        chaine_americaine.addElement(us);
    }
}


class monSlider {
  JLabel  label;
  JSlider slider;
  int     borne;
}


class SliderListener implements ChangeListener {
    monSlider annee1, annee2;
    afficheGen           affGen;

    SliderListener(monSlider annee1, monSlider annee2, afficheGen affGen) {
        this.annee1 = annee1;
        this.annee2 = annee2;
        this.affGen = affGen;
    }

    public void stateChanged(ChangeEvent e) {
        String nom = ((JComponent)e.getSource()).getName();
        // System.out.println("[31m stateChanged. [30m" + nom + "[34m" + e);

        if(nom.equals(annee1.slider.getName())) {
            annee1.borne = (int)((JSlider)e.getSource()).getValue();
            Integer zob = new Integer(annee1.borne); annee1.label.setText(zob.toString());
        } else if(nom.equals(annee2.slider.getName())) {
            annee2.borne = (int)((JSlider)e.getSource()).getValue();
            Integer zob = new Integer(annee2.borne); annee2.label.setText(zob.toString());
        }
        else System.out.println("[30m Not a slider! [34m" + e);

        if(annee1.borne<annee2.borne) {
          affGen.borne_inf = annee1.borne;
          affGen.borne_sup = annee2.borne;
        } else {
          affGen.borne_inf = annee2.borne;
          affGen.borne_sup = annee1.borne;
        }
    }
}


class monActionListener implements ActionListener {
    afficheGen           affGen;

    monActionListener(afficheGen affGen) {
        this.affGen = affGen;
    }

    public void actionPerformed(ActionEvent evt) {
        affGen.StrMatch = ((JTextField)evt.getSource()).getText();
    }
}
