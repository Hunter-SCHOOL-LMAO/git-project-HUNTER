import java.io.*;
public class Git{
    public void InitRepo(){
        File git = new File("git");
        File objects = new File("git/objects");
        File index = new File("git/index");
        File head = new File("git/HEAD");
        if (!git.exists() && !objects.exists() && !index.exists() && !head.exists()){
            git.mkdir();
            objects.mkdir();
            try{
                 index.createNewFile();
                head.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
            }
            System.out.println("Git Repository Created");
        }else{
            System.out.println("Git Repository Already Exists");
        }

    }
}