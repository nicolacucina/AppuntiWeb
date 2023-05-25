import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/*
 * <servlet>
			<servlet-name>RandomMessage</servlet-name>
			<servlet-class>RandomMessage</servlet-class>
		</servlet>
		
		<servlet-mapping>
			<servlet-name>RandomMessage</servlet-name>
			<url-pattern>/randmess</url-pattern>
		</servlet-mapping>
 */
public class RandomMessage extends HttpServlet{

    private int numVisitors;
    private String textPath;

    public void init(){
        this.numVisitors = 0;
        final String SERVLET_PATH = RandomMessage.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	    String servletPath = SERVLET_PATH.replace("%20"," ");
        this.textPath = servletPath + "/texts/";
    }

    public void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException{
        this.numVisitors++;
        
	    String name = rq.getParameter("name");

        String filename = textPath + "messages.txt";
        String route = new File(filename).getAbsolutePath();// whole path of the file has to be written within the double qoutes     
        String li= Line(route);
        
        rs.setContentType ("text/html");
        PrintWriter output = rs.getWriter();

        StringBuffer buffer = new StringBuffer();
        buffer.append ("<html>\n");
  	    buffer.append ("<head><title>Random Message</title></head>\n");
  	    buffer.append ("<body>\n");
        buffer.append ("<h1> Hi " + name +"</h1>\n");
        buffer.append ("<h2> You are the visitor number: " + numVisitors +"</h2>\n");
        buffer.append("<h3> Message for you </h3>\n");
        buffer.append("<p> " + li + " </p>");
        buffer.append ("</body>\n");
  	    buffer.append ("</html>");
	    output.println(buffer.toString());
        output.close();
    }

    private static String Line(String route) {
        List<String> l;
        try {
            l = Files.readAllLines(Paths.get(route));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Random random = new Random();
        return l.get(random.nextInt(l.size()));
    }

    public void doPost (HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException{

        String message = rq.getParameter("words");
        
        String filename = textPath + "messages.txt";
        PrintWriter pin = new PrintWriter(new FileWriter(filename, true));//mi serve di appendere
        pin.println(message);
        pin.flush();
        pin.close();
    }
}

/*
 * estendere il servizio, oltre a ricevere messaggio a caso, 
 * con una post aggiungo una nuova linea al file di testo.
 */