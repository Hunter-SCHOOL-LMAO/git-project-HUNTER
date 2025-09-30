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
        //creates a file and writes the byte[] into it
        File output = new File("git/objects/"+blobName);
        try{
        output.createNewFile();
        Files.write(output.toPath(), contents);
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    public boolean updateIndex(Path filePath, boolean createBlob){
        try{
        BufferedReader lineReader = Files.newBufferedReader(index.toPath());
        BufferedWriter appender = Files.newBufferedWriter(filePath);
        String line = "";
        String newHash = hashFile(Files.readAllBytes(filePath));
        int lineOffset = 0;
        while(line != null){
            line = lineReader.readLine();
            //isolates the filePath and blob name from the read line
            String indexFilePath = line.substring(46);
            String indexBlobName = line.substring(6, 46);
            //checks and appends the blob name if the file path is different
            if(indexFilePath.equals(filePath.toString())){
                if(!hashFile(Files.readAllBytes(filePath)).equals(indexBlobName)){
                    //lineOffset in necessary, or else it will just contine editing the first line
                    appender.append(newHash, 6+lineOffset, 46+lineOffset);
                    lineReader.close();
                    appender.close();
                    return true;
                }
            }
            lineOffset += line.length();
        }
        //combines two methods into one, may delete later
        if(createBlob){
            byte[] contents = Files.readAllBytes(filePath);
            makeBlob(contents, newHash);
        }
        if(index.length() == 0){
            String output = "blob "+newHash + " " + filePath.toString();
            Files.write(index.toPath(), output.getBytes(), StandardOpenOption.APPEND);
        }
        else{
             String output = "\nblob "+ newHash + " " + filePath.toString();
             Files.write(index.toPath(), output.getBytes(), StandardOpenOption.APPEND);
        }
        lineReader.close();
        appender.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return true;
    }
}