import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
public class Git{
    //instance variables
    private File git = new File("git");
    private File objects = new File("git/objects");
    private File index = new File("git/index");
    private File head = new File("git/HEAD");
    //getters
    public File getGit(){
        return git;
    }
    public File getObjects(){
        return objects;
    }
    public File getIndex(){
        return index;
    }
    public File getHead(){
        return head;
    }
    public void initRepo(){
        //basic Git files required for initialization
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
    }
        return "";
    }
    public void makeBlob(byte[] contents, String blobName){
        File output = new File("git/objects/"+blobName);
        try{
        output.createNewFile();
        Files.write(Paths.get("git/objects/"+blobName), contents);
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    public void updateIndex(Path filePath){
        try{
        byte[] contents = Files.readAllBytes(filePath);
        String blobName = hashFile(contents);
        String fileName = filePath.getFileName().toString();
        String output = "\n"+ blobName + " " + fileName;
        Files.write(Paths.get("git/index"), output.getBytes());
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}