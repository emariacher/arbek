import java.io.*;
import java.util.*;
import java.lang.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class A extends HttpServlet {
  ServletOutputStream out;
  String rootServlet = new String("http://9.100.49.159:8088/servlet/A");
  String path   = new String();
  DateCompare dateCompare = new DateCompare();
  AlphaCompare alphaCompare = new AlphaCompare();

  public void regularFile(File file) throws IOException {
    out.println( "<pre>" );
    FileInputStream filin = new FileInputStream(file);
    int b;
    while((b = filin.read())!=-1) out.write(b);
    out.println( "</pre>" );
  }


  public void directoryFile(File dir) throws IOException {
    String liste[]    = dir.list();
    File   fileList[] = new File[liste.length];
    File   dirList[]  = new File[liste.length];
    int dirIndex = 0, fileIndex = 0;
    for(int i=0; i<liste.length; i++) {
      File localFile = new File(path + "/" + liste[i]);
      if(localFile.isDirectory()) dirList[dirIndex++]   = localFile;
      else                        fileList[fileIndex++] = localFile;
      // System.out.print(" " + liste[i] + "-" + dirIndex + "/" + fileIndex);
    }
    // System.out.println("");
    // System.out.println("dirList: " + dirIndex);
    // System.out.println("fileList: " + fileIndex);
    alphaCompare.sort(dirList, dirIndex);
    dateCompare.sort(fileList, fileIndex);
    out.println( "<MULTICOL COLS=3>" );
    out.println("Directories by alphabetic order:<ul>");

    String upDir = new String();
    if(path.lastIndexOf("/")==path.length()) {
        upDir = path.substring(0, path.substring(0, path.length()-1).lastIndexOf("/"));
    } else {
        upDir = path.substring(0, path.lastIndexOf("/"));
    }
    out.println("<li><a href=\"" + rootServlet + upDir + "\">UP</a><p>");

    for(int i=0; i<dirList.length; i++) {
        if(dirList[i]!=null) out.println("<li><a href=\"" + rootServlet + path + "/" + dirList[i].getName() + "\">" + dirList[i].getName() + "</a>");
        else break;
    }
    if(fileIndex>0) out.println("</ul>Files by last update order:<ul>");
    for(int i=0; i<fileList.length; i++) {
        if(fileList[i]!=null) out.println("<li><a href=\"" + rootServlet + path + "/" + fileList[i].getName() + "\">" + fileList[i].getName() + "</a>");
        else break;
    }
    if(liste.length==0) out.println( "<ul><li>empty directory!" );
    out.println( "</ul></MULTICOL>" );
  }


  public void doGet(HttpServletRequest req, HttpServletResponse res)
                   throws ServletException, IOException {
    path        = new String(req.getRequestURI().substring(req.getServletPath().length()));
    File fildir = new File(path);
    System.out.println(path);

    res.setContentType( "text/html" );
    out = res.getOutputStream();
    out.println( "<html>" );
    out.println( "<head><title>" + path + "</title></head>" );
    out.println( "<body>" );
    out.println( "<h1>" + path + "</h1>" );
         if(fildir.isFile())      regularFile(fildir);
    else if(fildir.isDirectory()) directoryFile(fildir);
    else out.println( "<FONT COLOR=RED>Can't find " + path + "!</FONT>");
    out.println( "</body>" );
    out.println( "</html>" );
    out.close();
  }
}
