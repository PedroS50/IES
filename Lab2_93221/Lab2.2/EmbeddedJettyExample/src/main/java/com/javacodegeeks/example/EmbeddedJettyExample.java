package com.javacodegeeks.example;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
 
public class EmbeddedJettyExample {
 
    public static void main(String[] args) throws Exception {
         
        Server server = new Server(8680);       
         
        ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);

        servletHandler.addServletWithMapping(HelloServlet.class, "/HelloServlet");
        servletHandler.addServletWithMapping(CalendarServlet.class, "/CalendarServlet");
        
         
        server.start();
        server.join();
 
    }
     
    public static class HelloServlet extends HttpServlet 
    {
        /**
         *
         */
        private static final long serialVersionUID = 3548443767531153030L;

        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws ServletException, IOException
        {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>New Hello Simple Servlet</h1>"); 
        } 
    }

    public static class CalendarServlet extends HttpServlet {

        private static final long serialVersionUID = -1915463532411657451L;
    
        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws ServletException, IOException 
        {
            response.setContentType("text/html;charset=UTF-8");
            
            String username = request.getParameter("username");
            
            PrintWriter out = response.getWriter();
            
            try {
                // Write some content
                out.println("<html>");
                out.println("<head>");
                out.println("<title>CalendarServlet</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h2>Hello " + username + ", hope you enjoy your stay!</h2>");
                out.println("<h2>The time right now is : " + new Date() + "</h2>");
                out.println("</body>");
                out.println("</html>");
            } finally {
                out.close();
            }
        }
    }
 }