import java.io.*;
import java.net.*;

public class KeynameThread extends Thread{
    
    private KeynameServer server;
    private Socket client;
    private String keyname;
    private BufferedReader in;
    private PrintWriter out;

    KeynameThread(KeynameServer s, Socket c){
        this.server = s;
        this.client = c;
        this.keyname = "";
    }

    public String getKeyname(){
        return this.keyname;
    }

    public synchronized void sendMessage(String m){
        out.println("Message incoming");
        out.flush();
        out.println(m);
        out.flush();
    }

    public void run(){
        try{
            this.in = new BufferedReader(new InputStreamReader(client.getInputStream(), "ASCII"));
            this.out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "ASCII"), true);

            out.println("Connection established, insert your keyname using: keyname <keyname>\n");
            out.flush();

            String str = in.readLine();
            String[] strParts = str.split(" ");
            if(strParts.length != 2 || !strParts[0].equals("keyname")){
                out.println("Wrong format was used, closing connection");
                out.flush();
                try{
                    client.close();
                }catch(IOException e){
                    System.out.println("Problems with closing connection with client");
                }
            }else{
                this.keyname = strParts[1];
                String outcome = server.insertKeyname(this);
                out.println(outcome);
                out.flush();
                
                if(outcome.equals("keyname already in use, shutting down")){
                    try{
                        client.close();
                    }catch(IOException e){
                        System.out.println("Problems with closing connection with client");
                    }
                }else{
                    String command = "";
                    do{
                        out.println("Insert command");
                        out.flush();

                        command = in.readLine();
                        String[] commandParts = command.split(" ");
                        if(commandParts.length == 1){
                            if(command.equals("LIST")){
                                out.println(this.server.getListToString());
                                out.flush();
                            }
                        }else if(commandParts.length == 2){
                            if(commandParts[0].equals("SEND") && commandParts[1].contains("#")){
                                String[] message = commandParts[1].split("#");
                                out.println(this.server.sendMessage(message[0], message[1]));
                                out.flush();
                            }
                        }
                
                    }while(!command.equals("EXIT"));
                    try{
                        this.server.getListOfThreads().remove(this);
                        client.close();
                    }catch(IOException e){
                       System.out.println("Problems with closing connection with client");
                    }      
                }
            }
        }catch(IOException e){
            System.out.println("Problems with establishing connection with client");
        }
    }
}