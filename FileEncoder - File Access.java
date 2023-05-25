import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

//crea un file e scrive le stringhe passate come argomento usando la codifica specificata
public class FileEncoder{

    public static void main(String[] args) throws IOException{
        if(args.length < 2){
            throw new IllegalArgumentException("usare FileEncode <file-name> <encode> [string 1] [string 2] ...");//args parsa in automatico con gli spazi
        }

        FileOutputStream fout = new FileOutputStream(args[0]);
        OutputStreamWriter foutWriter = new OutputStreamWriter(fout, args[1]);

        for(int i = 2; i<args.length; i++){
            foutWriter.write(args[i]);
            foutWriter.write('\n');
        }

        foutWriter.close();
        fout.close();
    }
}