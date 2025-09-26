import java.io.*;
import java.nio.*;
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
    }
    public static boolean verifyInit(Git git){
        return git.getGit().exists() && git.getObjects().exists() && git.getIndex().exists() && git.getHead().exists();
    }
    public static boolean cleanUp(Git git){
        return git.getGit().delete();
    }
}
