import java.io.*;
import java.net.*;

public class Es2Client{
    public static void main(String[] args){
        System.out.println("Inserire un intero k");
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "ASCII"));
            int k = Integer.parseInt(in.readLine());
            System.out.println("k = " + k);
    
            byte[] kdata = Integer.toString(k).getBytes();
            DatagramPacket ksendPack = new DatagramPacket(kdata, kdata.length, InetAddress.getLocalHost(), Es2.PORT);
            DatagramSocket sock = new DatagramSocket();
            sock.send(ksendPack);
            System.out.println("Inviato k");
    
            for(int i = 0; i<k; i++){
                System.out.println("Inserire stringa numero " + i);
                String s = in.readLine();
                if(s.length()<10){
                    byte[] data = s.getBytes();
                    DatagramPacket sendPack = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), Es2.PORT); 
                    sock.send(sendPack);
                    System.out.println("Inviata stringa " + i+"-esima");   
                }else{
                    System.out.println("Stringa troppo lunga, max 10 caratteri");
                }
            }    
            
           for(int j=0; j<k; j++){
                byte[] buffer = new byte[4];
                DatagramPacket recPack = new DatagramPacket(buffer, buffer.length);
                sock.receive(recPack);
                int result = Integer.parseInt(new String(recPack.getData(), 0, recPack.getLength()));
                System.out.println("Lunghezza stringa " + j + "-esima : " + result);
           }
           sock.close();
        }catch(IOException e){
            System.out.println("I/O Error");
        }
    }
}