package web.ser;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class HolaWWW extends HttpServlet 
{
	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException 
   {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String docType = "<!DOCTYPE HTML PUBLIC \" -//W3C//DTD HTML 4.0 Transitional//EN\">\n";

		out.println(docType + "<html>\n<head><title>Curso JSP (Java Server Pages)</title></head>\n" +  
                "<body>\n<h1>Hola WWW</h1>\n</body></html>");
	}
}