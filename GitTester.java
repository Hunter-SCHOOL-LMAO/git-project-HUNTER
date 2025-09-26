import java.io.*;
import java.nio.file.*;
public class GitTester {
    public static void main(String[] args) {
        //initilization cycle
        Git git = new Git();
        git.initRepo();
        //testing that it will not init twice
        git.initRepo();
        //making sure it inits correctly
        verifyInit(git);
        //removes the repository
        cleanUp(git);
        File sampleFile = new File("sampleFile.txt");
        try{
        sampleFile.createNewFile();
        Files.write(sampleFile.toPath(), "THIS IS A TEST MESSAGE".getBytes());
        }catch(IOException e){
            e.printStackTrace();
        }
        /* NEXT STEPS
         * MAKE BLOB, COMPARE SHA
         * ADD TO INDEX
         * DELETE FILES OUTSIDE OF git FOLDER
         */
    }
    public static boolean verifyInit(Git git){
        return git.getGit().exists() && git.getObjects().exists() && git.getIndex().exists() && git.getHead().exists();
    }
    public static boolean cleanUp(Git git){
        return git.getGit().delete();
    }
}
