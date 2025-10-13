import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class GitRekt {
    Git gitProject = new Git();

    /**
     * Initializes a new Git repository.
     * This should create the necessary directory structure
     * and initial files required for a Git repository.
     */
    public void init() {
        gitProject.initRepo();
    };

    /**
     * Stages a file for the next commit.
     *
     * @param filePath The path to the file to be staged.
     */
    public void add(String filePath) {
        gitProject.updateIndex(Path.of(filePath), true);
    };

    /**
     * Creates a commit with the given author and message.
     * It should capture the current state of the repository,
     * update the HEAD, and return the commit hash.
     *
     * @param author  The name of the author making the commit.
     * @param message The commit message describing the changes.
     * @return The SHA1 hash of the new commit.
     */
    String commit(String author, String message) {
        try {
            return gitProject.commit(author, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    };

    /**
     * EXTRA CREDIT: Checks out a specific commit given its hash.
     * This should update the working directory to match the
     * state of the repository at that commit.
     *
     * @param commitHash The SHA1 hash of the commit to check out.
     */
    public void checkout(String commitHash) {
        String currentCommit = "";
        try (BufferedReader br = new BufferedReader(new FileReader(new File("git/HEAD")))) {
            currentCommit = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File commitFile = new File("git/objects/" + currentCommit);
        while (!currentCommit.equals(commitHash)) {
            try (BufferedReader br = new BufferedReader(new FileReader(commitFile))) {
                br.readLine();
                String parentLine = br.readLine();
                if (parentLine.contains(" ")) {
                    currentCommit = parentLine.split(" ")[1];
                } else {
                    throw new IllegalArgumentException("Could not find commit with given hash");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            commitFile = new File("git/objects/" + currentCommit);
        }
        try (BufferedReader br = new BufferedReader(new FileReader(commitFile))) {
            String rootTree = br.readLine().split(" ")[1];
            rebuildTree(rootTree);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("git/HEAD"))) {
            bw.write(commitHash);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    public static void rebuildTree(String treeHash) {
        File treeFile = new File("git/objects/" + treeHash);
        try (BufferedReader br = new BufferedReader(new FileReader(treeFile))) {
            String dirElem = br.readLine();
            while (dirElem != null) {
                String[] elemInfo = dirElem.split(" ");
                String elemType = elemInfo[0];
                String elemHash = elemInfo[1];
                String elemPath = elemInfo[2];
                if (elemType.equals("blob")) {
                    File blobbedContent = new File("git/objects/" + elemHash);
                    File ogFile = new File(elemPath);
                    if (!ogFile.exists()) {
                        ogFile.createNewFile();
                    }
                    try (BufferedReader blobReader = new BufferedReader(new FileReader(blobbedContent))) {
                        try (BufferedWriter bw = new BufferedWriter(new PrintWriter(ogFile))) {
                            String line = blobReader.readLine();
                            while (line != null) {
                                bw.write(line);
                                line = blobReader.readLine();
                                if (line != null) {
                                    bw.write("\n");
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    File ogDir = new File(elemPath);
                    if (!ogDir.exists()) {
                        ogDir.mkdir();
                    }
                    rebuildTree(elemHash);
                }
                dirElem = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Tests the init, add, and commit methods (checkout optional)
     */
    public void testGitRekt() {

        reset();

        init();

        // to-do: programmatically create at least 3 directories and at least 5 files
        // with content to add
        File programDir = new File("myProgram");
        File innerDir = new File("myProgram/inner");
        File helloFile = new File("myProgram/hello.txt");
        File worldFile = new File("myProgram/inner/world.txt");
        File goodbyeFile = new File("myProgram/goodbye.txt");
        File dataFile = new File("myProgram/inner/data.txt");
        File outcastFile = new File("outcast.txt");

        createDirIfNeed(programDir);
        createDirIfNeed(innerDir);

        createFileIfNeed(helloFile);
        createFileIfNeed(worldFile);
        createFileIfNeed(goodbyeFile);
        createFileIfNeed(dataFile);
        createFileIfNeed(outcastFile);

        try {
            Files.write(helloFile.toPath(), "Hello world".getBytes());
            Files.write(worldFile.toPath(), "Turtles all the way down...".getBytes());
            Files.write(goodbyeFile.toPath(), "Goodbye world".getBytes());
            Files.write(dataFile.toPath(), "Super important\nStuff\nIn here".getBytes());
            Files.write(outcastFile.toPath(), "Hey, what're you doing out here?".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        add("myProgram/hello.txt");
        add("myProgram/inner/world.txt");
        add("myProgram/goodbye.txt");
        add("myProgram/inner/data.txt");
        add("outcast.txt");
        String firstCommitHash = commit("John Doe", "Initial commit");

        // to-do: update the content of at least 2 files and add them again to the index
        // make sure that the index is updated correctly
        // commit the changes, make sure that the commit is created correctly
        try {
            Files.write(dataFile.toPath(), "Uh 0h- D4ta C0rr*p1ed!".getBytes());
            Files.write(goodbyeFile.toPath(), "Parting is such sweet sorrow.".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        add("myProgram/goodbye.txt");
        add("myProgram/inner/data.txt");
        commit("Andrew Jo", "Silly little commit, hope it doesn't mess up the data...");

        checkout(firstCommitHash); // Should revert to first commit
    }

    public static void createDirIfNeed(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static void createFileIfNeed(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * Reset the test by deleting the git directory and all generated directories
     * and files (programmatically)
     */
    public void reset() {
        File objectDir = new File("git/objects");
        for (File object : objectDir.listFiles())
            if (!object.isDirectory()) {
                object.delete();
            }
        try (BufferedWriter bw = new BufferedWriter(new PrintWriter(new File("git/HEAD")))) {
            bw.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = new BufferedWriter(new PrintWriter(new File("git/index")))) {
            bw.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}