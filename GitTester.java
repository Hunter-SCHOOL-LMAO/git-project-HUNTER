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
        deleteFolder(git.getGit());
        git.initRepo();
        File sampleFile = new File("sampleFile.txt");
        File sampleFile2 = new File("sampleFile2.txt");
        File sampleFile3 = new File("sampleFile3.txt");
        //file and blob creation
        try{
        sampleFile.createNewFile();
        sampleFile2.createNewFile();
        sampleFile3.createNewFile();
        Files.write(sampleFile.toPath(), "THIS IS A TEST MESSAGE".getBytes());
        Files.write(sampleFile2.toPath(), "THIS IS ALSO A TEST MESSAGE".getBytes());
        Files.write(sampleFile3.toPath(), "THIS IS TRULY ANOTHER TEST MESSAGE".getBytes());
        git.makeBlob(Files.readAllBytes(sampleFile.toPath()), git.hashFile(Files.readAllBytes(sampleFile.toPath())));
        //Blob name testing
        System.out.println("Created blob name:\n"+git.getObjects().list()[0]);
        System.out.println("Correct blob name: \n"+git.hashFile(Files.readAllBytes(sampleFile.toPath())));
        }catch(IOException e){
            e.printStackTrace();
        }
        //object folder clearing
        clearObjects(git);
        git.updateIndex(sampleFile.toPath(), true);
        git.updateIndex(sampleFile2.toPath(), true);
        git.updateIndex(sampleFile3.toPath(), true);
        try{
        System.out.println("Index file contents:\n"+Files.readString(git.getIndex().toPath()));
        System.out.println("Objects folder contents:");
        for(String e : git.getObjects().list()){
            System.out.println(e);
        }
        }catch(IOException e){
            e.printStackTrace();
        }
        garbageCollector(git);
    }
    public static boolean verifyInit(Git git){
        return git.getGit().exists() && git.getObjects().exists() && git.getIndex().exists() && git.getHead().exists();
    }
    public static boolean clearObjects(Git git){
        for(File e : git.getObjects().listFiles()){
            e.delete();
        }
        return true;
    }
    public static boolean garbageCollector(Git git){
        deleteFolder(git.getGit());
        File project = new File("/home/hunter/HTCS_Projects/git-project-HUNTER");
        FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File f, String name)
                {
                    return !name.endsWith(".java") && !name.endsWith(".git") && !name.endsWith(".md") && !name.endsWith(".gitignore");
                }
            };
        for(File e : project.listFiles(filter)){
            e.delete();
        }
         return true;
    }
    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { // Some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f); // Recursively delete subdirectories
                } else {
                    f.delete(); // Delete files
                }
            }
        }
        folder.delete(); // Delete the now empty directory
    }
}
