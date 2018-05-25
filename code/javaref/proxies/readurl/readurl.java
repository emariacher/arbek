//java readurl http://www.ibm.com
//java readurl file:/afs/eds.lagaude.ibm.com/u/mariacr
//java -DproxySet=true -DproxyHost=proxy.austin.ibm.com -DproxyPort=80 readurl http://www.ibm.com
//java -DproxySet=true -DproxyHost=proxy.raleigh.ibm.com -DproxyPort=80 readurl http://www.ibm.com

        import java.net.*;
        import java.io.*;

        class readurl {
            public static void main(String[] args) {
                try {
                    URL yahoo = new URL(args[0]);
                    DataInputStream dis = new DataInputStream(yahoo.openStream());
                    String inputLine;

                    while ((inputLine = dis.readLine()) != null) {
                        System.out.println(inputLine);
                    }
                    dis.close();
                } catch (MalformedURLException me) {
                    System.out.println("MalformedURLException: " + me);
                } catch (IOException ioe) {
                    System.out.println("IOException: " + ioe);
                }
            }
        }
