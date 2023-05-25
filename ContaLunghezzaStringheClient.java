import java.io.*;
import java.net.*;

public class ContaLunghezzaStringheClient{
    

    public static void main(String[] args) throws Exception{
        InetAddress server = InetAddress.getLocalHost();
        int port = ContaLunghezzaStringhe.PORT;
        
        if(args.length <= 0){
            throw new IllegalArgumentException("Wrong format, use ContaLunghezzaStringheClient <num-pacchetti>");

        }else{
            int k = Integer.parseInt(args[0]);
            int i=0;

            String[] inputs = new String[k];
            while(i < k){
                System.out.println("Inserire stringa numero " + i);
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in,"ASCII"));
                String input = in.readLine();
                if(input.length() > 10){
                    throw new IllegalArgumentException("Lunghezza massima supportata : 10 caratteri");
                }else{
                    inputs[i] = input;
                    i++;
                }
            }

            byte[] numberOfStrings = convertIntToByte(k);
            DatagramSocket sock1 = new DatagramSocket();
            DatagramPacket pack = new DatagramPacket(numberOfStrings,numberOfStrings.length,server,port);
            sock1.send(pack);

            for(int j = 0; j< k; j++){
                byte[] data = new byte[inputs[j].length()];
                data = inputs[j].getBytes();
                DatagramPacket sendPack = new DatagramPacket(data,data.length,server,port);
                sock1.send(sendPack);    
            }

            byte[] kBytes = new byte[4];
            DatagramPacket kPack = new DatagramPacket(kBytes, kBytes.length);

            for(int j = 0; j<k; j++){
                try{
                    sock1.receive(kPack);
                    int n  = ContaLunghezzaStringhe.convertByteToInt(kBytes);
                    System.out.println(n);                    
                }catch(IOException e){
                    System.out.println("I/O Error");
                }   
            }
            sock1.close();
        }
    }

    public static byte[] convertIntToByte(int i){
        byte[] b = new byte[4];
        b[0] = (byte)(i >> 24);
        b[1] = (byte)(i >> 16);
        b[2] = (byte)(i >> 8);
        b[3] = (byte)i;

        return b;
    }
}