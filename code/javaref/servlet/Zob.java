// Copyright MageLang Institute; Version $Id: //depot/main/src/edu/modules/Servlets/magercises/ServletRunnerHosting/Solution/HelloWorldServlet.java#2 $
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Zob sends back a simple, static HTML page.
 */
public class Zob extends HttpServlet
{

  public void doGet
  (
    HttpServletRequest  req,   // This provides information sent by the browser
    HttpServletResponse res    // This is used to send information back to the browser
  ) throws
    ServletException,          // General exception that should be thrown if there
                               // is a unrecoverable error in the Servlet
    IOException

  {
    // Set the MIME type for the information being sent to the browser.
    // In this case, we are going to send back HTML
    res.setContentType( "text/html" );

    // Get a reference to the output stream.
    // Anything written to this stream is sent directly to the browser
    // (The browser sees this as its input).
    ServletOutputStream out = res.getOutputStream();
    // The following println statements create an HTML page.
    // Notice that the <html></html>, <head></head>, and <body></body>
    // tags are all properly formed HTML syntax.
    out.println( "<html>" );
    out.println( "<head><title>Hello World</title></head>" );
    out.println( "<body>" );
    out.println( "<h1>Hello World</h1>" );
    out.println( "<p>Zob!</p><pre>" );
    out.println( req.getQueryString().toString() );
    out.println( req.getPathTranslated().toString() );
    out.println( req.getRequestURI().toString() );
    out.println( req.getServletPath().toString() );
    out.println( req.getMethod().toString() );
    out.println( "</pre></body>" );
    out.println( "</html>" );
    out.close();
  }
}
