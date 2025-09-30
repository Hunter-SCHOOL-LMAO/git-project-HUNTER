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
        File sampleFile2 = new File("sampleFile2.txt");
        File sampleFile3 = new File("sampleFile2.txt");
        //file and blob creation
        try{
        sampleFile.createNewFile();
        sampleFile2.createNewFile();
        sampleFile3.createNewFile();
        Files.write(sampleFile.toPath(), "THIS IS A TEST MESSAGE".getBytes());
        Files.write(sampleFile2.toPath(), "THIS IS ALSO A TEST MESSAGE".getBytes());
        Files.write(sampleFile3.toPath(), "THIS IS ALSO A TEST MESSAGE".getBytes());
        git.makeBlob(Files.readAllBytes(sampleFile.toPath()), git.hashFile(Files.readAllBytes(sampleFile.toPath())));
        //Blob name testing
        System.out.println("Created blob name:\n"+git.getObjects().list().toString());
        System.out.println("Correct blob name: \n"+git.hashFile(Files.readAllBytes(sampleFile.toPath())));
        }catch(IOException e){
            e.printStackTrace();
        }
        //object folder clearing
        clearObjects(git);
        git.updateIndex(sampleFile.toPath());
        git.updateIndex(sampleFile2.toPath());
        git.updateIndex(sampleFile3.toPath());
        try{
        System.out.println("Index file contents:\n"+Files.readString(git.getIndex().toPath()));
        System.out.println("Objects folder contents:\n"+git.getObjects().list().toString());
        }catch(IOException e){
            e.printStackTrace();
        }
        /* NEXT STEPS
         * DELETE FILES OUTSIDE OF git FOLDER
         */
    }
    public static boolean verifyInit(Git git){
        return git.getGit().exists() && git.getObjects().exists() && git.getIndex().exists() && git.getHead().exists();
    }
    public static boolean cleanUp(Git git){
        return git.getGit().delete();
    }
    public static boolean clearObjects(Git git){
        for(File e : git.getObjects().listFiles()){
            e.delete();
        }
        return true;
    }
}
