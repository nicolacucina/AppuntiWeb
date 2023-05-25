/*
 * Server java che ascolta sulla porta TCP 20000, gestisce più client simultaneamente(multithreading). 
 * i messaggi tra server e client sono testuali e usano codifica ASCII.
 * Una volta stabilita la connessione, il server invia al client il messaggio "Connection established, insert your keyname".
 * Il client invia al server un messaggio con cui si qualifica tramite un keyname.
 * il messaggio inviato al server ha la forma "keyname <keyname>", dove <keyname> è una stringa senza spazi.
 * A questo punto il server verifica il valore di <keyname> ricevuto. se vi è già un client connesso con lo stesso keyname, il server invia 
 * al client un messaggio "keyname already in use" e chiude la connessione. altrimenti invia un messaggio al client "keyname valid".
  * Da questo momento in poi, il client può inviare al server, ripetutamente, uno tra i seguenti comandi:
 * -LIST: server invia al client una lista di keyname connessi.
 * -SEND <messaggio>#<keyname>: il server invia il messaggio al client con quel keyname.
 * -EXIT: chiude la connessione.
 */

import java.io.*;
import java.net.*;
import java.util.LinkedList;


public class KeynameServer{

    private static LinkedList<KeynameThread> keynames;

    KeynameServer(){
        keynames = new LinkedList<KeynameThread>();//istanzia la lista dove verranno salvati i vari clients
    }

    public static void main(String[] args) throws IOException{
        ServerSocket server = new ServerSocket(20000);//Socket TCP passivo
        System.out.println("Listening on port 20000");
        KeynameServer s = new KeynameServer();//creo il server per avere a disposizione i metodi per l'accesso alle risorse condivise nei thread
        while(true){
            Socket client = server.accept();//Socket da usare per comunicare con il client
            System.out.println("connessione con nuovo client");
            KeynameThread t = new KeynameThread(s, client);//Istanziazione thread, gli passo il socket da usare e il server
            t.start();
        }
    }

//due modi per accedere in modo consistente alle risorse condivise, nel corpo o nella intestazione di un metodo
    public String insertKeyname(KeynameThread k){
        boolean used = false;
        synchronized(keynames){
            for(KeynameThread s : keynames){
                if(s.getKeyname().equals(k.getKeyname())){
                    used = true;
                }
            }
        }
        if(used){
            return "keyname already in use";
        }else{
            keynames.add(k);
            System.out.println("new keyname added: " + k.getKeyname());
            synchronized(KeynameServer.keynames){
                for(KeynameThread s : KeynameServer.keynames){
                    System.out.println(s.getKeyname());
                }
            }
            return "keyname valid";
        }
    }

    public String getListToString(){
        String result = "";
        
        synchronized(KeynameServer.keynames){
            for(KeynameThread s : KeynameServer.keynames){
                result += s.getKeyname() + "\n";
            }
        }
        System.out.println("List Request received, the list is: " + result);
        return result;
    }

    public synchronized LinkedList<KeynameThread> getListOfThreads(){  
        return keynames;
    }

    public String sendMessage(String message, String keyname){
        synchronized(keyname){
            for(KeynameThread s : keynames){
                if(s.getKeyname().equals(keyname)){
                    s.sendMessage(message);
                    return "Message sent correctly";
                }
            }
        }
        return "client not found";
    }  
}