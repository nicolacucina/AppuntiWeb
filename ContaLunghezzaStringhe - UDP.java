/* La classe ContaLunghezzaStringhe contiene il seguente metodo statico:
 *      public static int[] leggiPacchettiUDP (int k, DatagramSocket sock)
 * che legge da sock un numero k di datagrammi (pacchetti UDP), ciascuno contenente una stringa di massimo 10 byte.
 * -Il metodo restituisce un array di interi a, tale che in a[i] è scritto il numero di byte della stringa nel pacchetto i-esimo (il primo pacchetto è il pacchetto 0).
 * -Scrivere la classe ContaLunghezzaStringhe,dotandola anche di un metodo main che  crea un DatagramSocket in ascolto 
 * sulla porta 50000, ed effettua una invocazione del metodo leggiPacchettiUDP con un valore k del parametro deciso dall’utente.
 * -Al fine di testare la classe ContaLunghezzaStringhe, scrivere anche un’applicazione che, una volta avviata la classe 
 * ContaLunghezzaStringhe, invia all’host su cui tale classe è stata avviata una sequenza di k di pacchetti UDP,
 * ciascuno contenente un messaggio di al più 10 byte.
 */
import java.io.*;
import java.net.*;

public class Es2{
    
    public final static int PORT = 50000;
    public static void main(String[] args) throws SocketException{
        byte[] kdata = new byte[4];
        DatagramPacket kpack = new DatagramPacket(kdata, kdata.length);
        DatagramSocket socket = new DatagramSocket(PORT);
        System.out.println("Server in ascolto sulla porta " + PORT);
        while(true){
            try{
                socket.receive(kpack);
                int k = Integer.parseInt(new String(kpack.getData(), 0, kpack.getLength()));
                System.out.println("Ricevuto: "+k);
    
                int[] result = leggiPacchettiUDP(k, socket);
                for(int i = 0; i< result.length; i++){
                    byte[] data = Integer.toString(result[i]).getBytes();
                    DatagramPacket sendPack = new DatagramPacket(data, data.length, kpack.getAddress(), kpack.getPort());
                    socket.send(sendPack);
                }
    
            }catch(IOException e){
                System.out.println("Errore di connessione");
            }
        }
    }

    public static int[] leggiPacchettiUDP(int k, DatagramSocket sock){
        String[] inputs = new String[k];
        int[] result = new int[k];
        for(int i = 0; i< inputs.length; i++){
            try{
                byte[] buffer = new byte[10];
                DatagramPacket recPack = new DatagramPacket(buffer, buffer.length);        
                sock.receive(recPack);
                inputs[i] = new String(recPack.getData(), 0, recPack.getLength());
                System.out.println("Ricevuto: "+inputs[i]);
                result[i] = inputs[i].length();
                System.out.println("Lunghezza: "+result[i]);
            }catch(IOException e){
                System.out.println("Errore di connessione");
            }
        }
        return result;
    }
}