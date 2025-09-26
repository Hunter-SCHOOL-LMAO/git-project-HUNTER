import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
public class Git{
<<<<<<< HEAD
    public void initRepo(){
=======
    public void InitRepo(){
>>>>>>> origin/HashBrowns
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
<<<<<<< HEAD
    public String hashFile(byte[] contents){
=======
    public String HashFile(Path filePath){
>>>>>>> origin/HashBrowns
        try{
        //SHA-1 initialization
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        //byte array created here
<<<<<<< HEAD
        byte[] intermediaryArray = md.digest(contents);
=======
        byte[] intermediaryArray = md.digest(Files.readString(filePath, StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8));
>>>>>>> origin/HashBrowns
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
<<<<<<< HEAD
    public void makeBlob(byte[] contents){
        String fileName = hashFile(contents);
        File output = new File("/git/objects/"+fileName);
        output.createnewFile();
        Files.write(Paths.get("/git/objects/"+fileName), contents);
        
    }
=======
>>>>>>> origin/HashBrowns
}