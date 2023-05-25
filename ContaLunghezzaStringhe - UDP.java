import java.io.*;
import java.net.*;

public class ContaLunghezzaStringhe{

    static final int PORT = 50000;

    public static void main(String[] args){
        DatagramSocket sock = null;
        try{
            sock = new DatagramSocket(PORT);
            System.out.println("Listening on port " + PORT);
        }catch (SocketException e){
            System.out.println ("The socket cannot be opened");
            System.exit(1);
        }

        byte[] kBytes = new byte[4];
        DatagramPacket kPack = new DatagramPacket(kBytes, kBytes.length);

        while(true){
            try{
                sock.receive(kPack);
                int k  = convertByteToInt(kBytes);
                
                System.out.println("Expecting " + k + " packets");

                int[] risultato = leggiPacchettiUDP(k, sock);
                for(int i = 0; i < k; i++){
                    System.out.println("La stringa " + i + "-esima e' lunga " + risultato[i]);
                }
                
            }catch(IOException e){
                System.out.println("I/O Error");
            }   
        }
    }

    public static int[] leggiPacchettiUDP(int k, DatagramSocket sock) throws IOException{
        
        int[] result = new int[k];

        DatagramSocket sendSock = new DatagramSocket();
        for(int i = 0; i<k; i++){
            byte[] data = new byte[10];
            DatagramPacket recPack = new DatagramPacket(data, data.length);    
            sock.receive(recPack);

            //dataStrings[i] = new String(recPack.getData());
            //System.out.println(dataStrings[i]);
            
            result[i] = recPack.getLength(); //recPack è il pacchetto ricevuto, recPack.getData() è il buffer

            byte[] prova = ContaLunghezzaStringheClient.convertIntToByte(result[i]);

            DatagramPacket sendPack = new DatagramPacket(prova, prova.length, recPack.getAddress(), recPack.getPort());    
            sendSock.send(sendPack);
        }
        sendSock.close();
        return result;
    }

    public static int convertByteToInt(byte[] b){
        int b0 = b[0];
        int b1 = (b[1] & 255);
        int b2 = (b[2] & 255);
        int b3 = (b[3] & 255);
        int n = (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;

        return n;
    }
}