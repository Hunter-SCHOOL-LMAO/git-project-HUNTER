import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
public class Git{
    public void initRepo(){
        //basic Git files required for initialization
        File git = new File("git");
        File objects = new File("git/objects");
        File index = new File("git/index");
        File head = new File("git/HEAD");
        //If there is going to be an issue, its going to be here
        if (!git.exists() && !objects.exists() && !index.exists() && !head.exists()){
           //basic-ass file creation
            git.mkdir();
            objects.mkdir();
            try{
                 index.createNewFile();
                head.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
            }
            //output message
            System.out.println("Git Repository Created");
        }else{
            System.out.println("Git Repository Already Exists");
        }

    }
    public String hashFile(byte[] contents){
        try{
        //SHA-1 initialization
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        //byte array created here
        byte[] intermediaryArray = md.digest(contents);
        BigInteger hashResult = new BigInteger(1, intermediaryArray);
        StringBuilder output = new StringBuilder(hashResult.toString(16));
        //fills out trailing zeros on the output string
        while (output.length() < 40)
        {
            output.insert(0, '0');
        }
        return output.toString();
        //exception handeling
    } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
        return "";
    }
    public void makeBlob(byte[] contents, String blobName){
        File output = new File("git/objects/"+blobName);
        output.createnewFile();
        Files.write(Paths.get("git/objects/"+blobName), contents);
        
    }
    public void updateIndex(Path filePath){
        byte[] contents = Files.readString(filePath).getBytes();
        String blobName = hashFile(contents);
        String fileName = filePath.getFileName().toString();
        String output = "\n"+ blobName + " " + fileName;
        Files.write(Paths.get("git/index"), output.getBytes());
    }
}