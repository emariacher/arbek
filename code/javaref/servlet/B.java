import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class B extends HttpServlet implements LabInclude {
  static Frame      aFrame;
  static Labyrinthe applet;

  public void init(ServletConfig config) throws ServletException {
      super.init(config);

      // cree l'applet
      applet = new Labyrinthe();
      aFrame = new Frame("ZOB!");
      aFrame.addWindowListener(
          new WindowAdapter() {
              public void windowClosing(WindowEvent e) {
                  System.out.println("Bye-bye: closing window!");
                  System.exit(0);
              }
          }
      );
      aFrame.add(applet, BorderLayout.CENTER);
      aFrame.setSize(700, 700);
      applet.init();
      applet.start();
      aFrame.setVisible(true);

  }

  public void doGet ( HttpServletRequest  req,
                      HttpServletResponse res ) throws ServletException,
                                                       IOException {
    res.setContentType( "text/html" );
    ServletOutputStream out = res.getOutputStream();

    out.println( "<html>" );
    out.println( "<head><title>B: Labyrinthe</title></head>" );
    out.println( "<body>" );
    out.println( "<h1>Labyrinthe</h1>" );
    out.println( "<p><table border=2><tr><th>pion</th><th>fois 1er</th><th>min</th><th>moy</th><th>max</th></tr>" );
    for(int i=0;i<MAXPION;i++) {
        if(applet.laby.tp[i]!=null) {
            out.println( " <tr><td><font color=" + applet.laby.tp[i].getColorName(2) + ">" + applet.laby.tp[i].getColorName(0) + "</font></td>");
            out.println( "  <td>" + applet.laby.tp[i].cpt + "</td>");
            out.println( "  <td>" + applet.laby.tp[i].min + "</td>");
            out.println( "  <td>" + applet.laby.tp[i].moy + "</td>");
            out.println( "  <td>" + applet.laby.tp[i].max + "</td></tr>");
        }
    }
    System.out.print(".");
    out.println( "</table></body>" );
    out.println( "</html>" );

    out.close();
  }
}
