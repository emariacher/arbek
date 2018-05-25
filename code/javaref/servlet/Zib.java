// Copyright MageLang Institute; Version $Id: //depot/main/src/edu/modules/Servlets/magercises/ServletRunnerHosting/Solution/HelloWorldServlet.java#2 $
import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Zib sends back a simple, static HTML page.
 */
public class Zib extends HttpServlet
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
    out.println( "<p>Zib!</p><pre>" );
    if(req.getQueryString()   !=null)  out.println( "getQueryString "    + req.getQueryString() );
    if(req.getPathTranslated()!=null)  out.println( "getPathTranslated " + req.getPathTranslated() );
    if(req.getRequestURI()    !=null)  out.println( "getRequestURI "     + req.getRequestURI() );
    if(req.getServletPath()   !=null)  out.println( "getServletPath "    + req.getServletPath() );
    if(req.getMethod()        !=null)  out.println( "getMethod "         + req.getMethod() );
    if(req.getParameterNames()!=null)  {
        out.print( "getParameterNames: ");
        Enumeration e = req.getParameterNames();
        while(e.hasMoreElements()) out.print( ", " + (String)e.nextElement() );
        out.println( "." );
    }
    if(req.getParameter("ZURG")!=null) out.println( "getParameter(ZURG) " + req.getParameter("ZURG") );
    out.println( "</pre></body>" );
    out.println( "</html>" );
    out.close();
  }
}
