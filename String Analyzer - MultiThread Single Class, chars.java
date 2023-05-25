/* Scrivere il codice Java di un piccolo server con le seguenti caratteristiche:
 * Il server gestisce più client contemporaneamente,
 * All’avvio il server si mette in ascolto sulla porta 2000;
 * Quando un client fa richiesta di connessione, il server risponde inviando il seguente messaggio 'Sei connesso: Inserisci una frase terminata da %';
 * Una volta ricevuta la frase, il server restituisce al client i seguenti messaggi: 
 *  Un messaggio che lo informa di quante parole (stringhe senza spazi) ci sono nella frase;ù
 *  Un messaggio che lo informa di quale è la parola più lunga e che gli dice anche da quanti caratteri è formata.
 *  Il server saluta e disconnette il client.
 * Se il client inserisce soltanto ‘%’, il server assume che sia stata inserita la sola stringa vuota 
*/

import java.io.*;
import java.net.*;

public class StringAnalyzerMultiThread extends Thread{
    
    private Socket client;

    StringAnalyzerMultiThread(Socket client){
        this.client = client;
    }

    public static void main(String[] args) throws IOException{

        ServerSocket server = startServer(2000);
        while(true){
            Socket client = server.accept();
            System.out.println("Connessione con nuovo client");
            StringAnalyzerMultiThread serverT = new StringAnalyzerMultiThread(client);
            serverT.start();
        }
    }

    public void run(){
        try{
            InputStreamReader in = new InputStreamReader(client.getInputStream(), "latin1");
            OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream(), "latin1");

            out.write("Sei connesso: Inserisci una frase terminata da % \r\n");
            out.flush();
            
            int c;
            String input = "";
            int numberOfWords = 0;
            String max = "";
            while ((c = in.read())!=-1){
                if(c == '%'){
                    input = input + (char)c;
                    break; // carattere di fine frase
                }else{
                    input = input + (char)c;
                } 
            }

            System.out.println(input);
            if(input.equals("%")){
                out.write("\r\nNumero di parole: 0 \r\n");
                out.flush();
                out.write("Parola piu' lunga: nessuna \r\n");
                out.flush();
            }else if(input.charAt(input.length()-1) != '%'){
                throw new IllegalArgumentException("wrong string format, use <string%>");
            }else{

            /* Tolgo il % perchè ha fatto il suo ruolo di delimitatore di frase.
             * Se considero lo spazio come "fine di una parola", quando leggo l'ultima parola, la faccio seguire da uno spazio 
             * per riutilizzare lo stesso if e non dover fare codice per gestire separatamente l'ultima parola 
             */
                String str = input.substring(0, input.length()-1) + " ";
                String temp = "";

                for(int i = 0; i<str.length(); i++){
                    char d = str.charAt(i);
                    if(d != ' '){
                        temp += d;
                    }else if(d == ' '){
                        numberOfWords++;
                        if(temp.length() > max.length()){
                            max = temp;
                        }
                        temp = "";
                    }
                }
                
                out.write("\r\nNumero di parole: " + numberOfWords + "\r\n");
                out.flush();
                out.write("Parola di lunghezza massima: " + max + "\r\n");
                out.flush();
                out.write("Numero di caratteri in questa parola: "+ max.length() + "\r\n");
                out.flush();
            }
            out.write("Fine sessione");
            out.flush();
        }catch(IOException e){
            System.out.println("Errore di connessione con il client");
        }catch(IllegalArgumentException e){
            System.out.println("Errore nella formattazione della stringa");
        }finally{
            System.out.println("Closing connection with client");
            try{
                client.close();
            }catch(IOException e){
                System.out.println("Errore nella chiusura della connessione");
                e.printStackTrace();
            }
        }
    }

    private static ServerSocket startServer(int port) throws IOException{
        ServerSocket server = new ServerSocket (port);
        System.out.println ("Listening on port " + port);
        return server;
    }
}