/* Creare un programma che data una stringa di input restituisca la stringa rovesciata. 
 * Il programma deve sapere gestire più client contemporaneamente.
 * E' richiesto che il codice sia contenuto in una sola classe.
 */

import java.net.*;
import java.io.*;

public class ReverseStringMultiThreadServer extends Thread{
    
    private Socket client;

    public ReverseStringMultiThreadServer(Socket client){
        this.client = client;
    }

    public static void main(String[] args) throws IOException{
        if(args.length!=1){
            throw new IllegalArgumentException("usare ReverseStringMultiServer <port number>");
        }

        ServerSocket server = startServer(Integer.parseInt(args[0]));
        while(true){
            Socket client = server.accept();
            System.out.println("connessione con nuovo client");
            ReverseStringMultiThreadServer serverT = new ReverseStringMultiThreadServer(client);
            serverT.start();
        }
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), "latin1"));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "latin1"), true);
            out.write("Hello, you are connected\n\r");
            out.flush();
            String str = in.readLine();
            String rev = "";

            for (int i=str.length()-1; i>=0; i--){
                rev += str.charAt(i);
            }

            out.println(rev); 
            out.flush();
            out.println("bye bye"); 
            out.flush();
        }catch(IOException e){
            System.out.println ("Error in the connection with client");
        }finally{             
            System.out.println ("Closing connection with client");
            try{
                client.close();
            }catch(IOException e){
                System.out.println ("Problems while closing the connection..!");
                System.out.println (e);
            }
        }
    }

    private static ServerSocket startServer(int port) throws IOException{
        ServerSocket server = new ServerSocket (port);
        System.out.println ("Listening on port " + port);
        return server;
    }
    
}