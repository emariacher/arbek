import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
//import com.sun.java.swing.*;
//import com.sun.java.swing.text.*;

class OneServeur extends Thread{
  private Socket              socket, socketToWeb;
  private PrintWriter         toWeb, toBrowser;
  private BufferedReader      fromWeb, fromBrowser;

  public OneServeur(Socket s) throws IOException {
        socket               = s;
        fromBrowser          = new BufferedReader( new InputStreamReader ( socket.getInputStream()));
        toBrowser            = new PrintWriter( new BufferedWriter ( new OutputStreamWriter ( socket.getOutputStream())),true);
        start();
  }

  public void run() {
    try {
        String str, targetHost="", httpHeader="";
        //----------- BROWSER -> PROXY
        while(true) {
             str = fromBrowser.readLine();
	     System.out.println(str);
             httpHeader += str + "\n";
             if ( str.startsWith("Host: " ) ) targetHost = str.substring(6);
             if ( str.length() == 0 ) break;
        }

        //----------- PROXY -> WEB
        System.out.println("*********************Nouvelle chaussette: " + targetHost);
        socketToWeb = new Socket(InetAddress.getByName(targetHost),3967);
        //socketToWeb = new Socket(InetAddress.getByName(targetHost),80);
        //socketToWeb = new Socket(InetAddress.getByName("proxy.austin.ibm.com"),80);
        //socketToWeb = new Socket(InetAddress.getByName("proxy.raleigh.ibm.com"),80);
        System.out.println(socketToWeb);
        fromWeb = new BufferedReader( new InputStreamReader ( socketToWeb.getInputStream()));
        toWeb = new PrintWriter( new BufferedWriter ( new OutputStreamWriter ( socketToWeb.getOutputStream())),true);

        toWeb.println(httpHeader);

        //----------- WEB -> PROXY -> BROWSER

        while ( true ) {
             str = fromWeb.readLine();
             if ( str == null ) break;
	     System.out.println(str);
             toBrowser.println(str);
        }
    } catch ( IOException e) {
        System.err.println("No good 1! " + e );
    } finally {
        try {
            socket.close();
        } catch ( IOException e) {
            System.err.println("No good 2! " + e );
        }
        System.out.println("**************************closing... ");
    }
  }
}

public class Proxy {
    public static final int PORT = 1080;

    public static void main(String args[]) throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Started " + s);

        try {
            while ( true ) {
                Socket socket = s.accept();
                try {
                    System.out.println("**********************connection acceptee " + socket);
                    new OneServeur(socket);
                } catch ( IOException e ) {
                    socket.close();
                }
            }
        } finally { s.close(); }
    }
}
