import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.java.swing.*;
import com.sun.java.swing.text.*;

class OneServeur extends Thread{
  private Socket              socket, socketToWeb;
  private BufferedReader      fromBrowser;
  private PrintWriter         toWeb, toBrowser;
  private BufferedReader      fromWeb;
          ScrolledText        panel;
          ActionSendToWeb     actionSendToWeb     = new ActionSendToWeb();
          ActionSendToBrowser actionSendToBrowser = new ActionSendToBrowser();

  public OneServeur(Socket s, ScrolledText panel) throws IOException {
        socket               = s;
        fromBrowser          = new BufferedReader( new InputStreamReader ( socket.getInputStream()));
        toBrowser            = new PrintWriter( new BufferedWriter ( new OutputStreamWriter ( socket.getOutputStream())),true);
        this.panel           = panel;
        panel.sendToWeb.addActionListener(actionSendToWeb);
        panel.sendToBrowser.addActionListener(actionSendToBrowser);
        start();
  }


  public void run() {
    try {
        String str, targetHost="", httpHeader="";
        //----------- BROWSER -> WEB
        while(true) {
             str = fromBrowser.readLine();
             httpHeader += str + "\n";
             if ( str.startsWith("Host: " ) ) targetHost = str.substring(6);
             if ( str.length() == 0 ) break;
        }
        socketToWeb = new Socket(InetAddress.getByName(targetHost),80);
        //socketToWeb = new Socket(InetAddress.getByName("proxy.austin.ibm.com"),80);
        //socketToWeb = new Socket(InetAddress.getByName("proxy.raleigh.ibm.com"),80);
        System.out.println(socketToWeb);
        fromWeb = new BufferedReader( new InputStreamReader ( socketToWeb.getInputStream()));
        toWeb = new PrintWriter( new BufferedWriter ( new OutputStreamWriter ( socketToWeb.getOutputStream())),true);
        Document docToWeb = panel.toWeb.getDocument();
        try {
          docToWeb.remove(0,docToWeb.getLength());
        } catch ( Exception e ) {
          System.err.println("[31m (1) " + e + "[34m");
        }
        panel.toWeb.append(httpHeader);

        if(panel.autoToWeb) actionSendToWeb.actionPerformed(null);

        //----------- WEB -> BROWSER
        boolean echoing = true;

        Document docToBrowser = panel.toBrowser.getDocument();
        try {
          docToBrowser.remove(0,docToBrowser.getLength());
        } catch ( Exception e ) {
          System.err.println("[31m (2) " + e + "[34m");
        }
        while ( true ) {
             str = fromWeb.readLine();
             if ( str == null ) break;
             if ( str.length() == 0 ) echoing = false;
             if( echoing ) {
                System.out.println(str);
                // panel.toBrowser.append("\n" + str);
             }
             // toBrowser.println(str);
             panel.toBrowser.append(str + "\n");
        }
/*      int lg;
        char tab[] = new char[520];
        while ( true ) {
             lg = fromWeb.read(tab,0,512);
             System.out.print("..." + lg);
             toBrowser.write(tab,0,lg);
             if ( lg != 512 ) break;
        }
*/
      if(panel.autoToBrowser) actionSendToBrowser.actionPerformed(null);
      System.out.println("\nclosing ...");
    } catch ( IOException e) {
      System.err.println("[31m (5) " + e + "[34m");
    } finally {
      // System.err.println("[31m (6) [34m");
    }
  }


  class ActionSendToWeb implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      // System.out.println("[31m Send to Web [30m" + e + "[34m");
      Document docToWeb = panel.toWeb.getDocument();
      try {
        int length = docToWeb.getLength();
        if(length!=0) {
          toWeb.println(docToWeb.getText(0,docToWeb.getLength()));
          System.out.println("[30m" + docToWeb.getText(0,length) + "[34m");
          docToWeb.remove(0,length); /* or put a send status */
        } else {
          System.err.println("[31m Nothing sent to Web. [34m");
        }
      } catch ( Exception evt ) {
          System.err.println("[31m (3) " + evt + "[34m");
      }
    }
  }

  class ActionSendToBrowser implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      // System.out.println("[31m Send to Browser [30m" + e + "[34m");
      Document docToBrowser = panel.toBrowser.getDocument();
      try {
        int length = docToBrowser.getLength();
        if(length!=0) {
          toBrowser.println(docToBrowser.getText(0,docToBrowser.getLength()));
          System.out.println(docToBrowser.getText(0,length));
          socket.close();
          docToBrowser.remove(0,length); /* or put a send status */
        } else {
          System.err.println("[31m Nothing sent to Browser. [34m");
        }
        // System.out.println("\nclosing ...");
      } catch ( Exception evt ) {
          System.err.println("[31m (4) " + evt + "[34m");
      }
    }
  }

}

public class Proxy {

  public static final int PORT = 12345;

  public static void main(String args[]) throws IOException {
        ScrolledText   panel;
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Started " + s);

        panel = new ScrolledText("Browser<->Web");
        panel.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.out.println("[30m bye-bye! [34m"); System.exit(0); } });
        panel.pack();
        panel.show();



        try {
                while ( true ) {
                        Socket socket = s.accept();
                        try {
                                System.out.println("connection acceptee " + socket);
                                new OneServeur(socket, panel);
                        } catch ( IOException e ) {
                                socket.close();
                        }
                }
        } finally {
                s.close();
        }
  }
}








