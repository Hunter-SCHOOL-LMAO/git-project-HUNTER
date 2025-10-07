import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.util.*;
public class Git{
    //instance variables
    private File git = new File("git");
    private File objects = new File("git/objects");
    private File index = new File("git/index");
    private File head = new File("git/HEAD");
    // private LinkedList<WorkingListObject> workingList;
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
    //lmao no setters
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
        //big check to see if the filePath already exists in the index
        while(line != null){
            line = lineReader.readLine();
            //isolates the filePath and blob name from the read line
            String indexFilePath = line.substring(newHash.length());
            String indexBlobName = line.substring(0, 41);
            //checks and appends the blob name if the file name is the same, but the hash is different
            if(indexFilePath.equals(filePath.toString())){
                if(!hashFile(Files.readAllBytes(filePath)).equals(indexBlobName)){
                    //lineOffset in necessary, or else it will just contine editing the first line
                    appender.append(newHash, lineOffset, lineOffset+newHash.length() + 1);
                    lineReader.close();
                    appender.close();
                    return true;
                }
            }
            lineOffset += line.length();
        }
        //For blob-making conveinence, may delete later
        if(createBlob){
            byte[] contents = Files.readAllBytes(filePath);
            makeBlob(contents, newHash);
        }
        //gets rid of the newline at the beginning of the file
        if(index.length() == 0){
            String output = newHash + " " + filePath.toString();
            Files.write(index.toPath(), output.getBytes(), StandardOpenOption.APPEND);
        }
        else{
             String output = "\n"+newHash + " " + filePath.toString();
             Files.write(index.toPath(), output.getBytes(), StandardOpenOption.APPEND);
        }
        lineReader.close();
        appender.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return true;
    }
    public String fileTree(String startPath){
        return Paths.get(fileTreeRecursive(startPath)).getFileName().toString();
    }
    //recursive function for fileTree
    public String fileTreeRecursive(String startPath){
        File startingFolder = new File(startPath);
        File[] fileList = startingFolder.listFiles();
        StringBuilder output = new StringBuilder();
        try{
            for(File e : fileList){
                Path ePath = e.toPath();
                //base case
                if(!e.isDirectory()){
                    //gets rid of starting newline
                    if(output.length() != 0){
                        output.append("blob "+ hashFile(Files.readAllBytes(ePath)) + " " + e.getName());
                    }else{
                        output.append("\nblob "+ hashFile(Files.readAllBytes(ePath)) + " " + e.getName());
                    }
                }
                //recursive case
                else{
                    //recursive call
                    File recursedTree = new File(objects + Paths.get(fileTreeRecursive(startPath)).getFileName().toString());
                    //gets rid of starting newline
                    if(output.length() != 0){
                        output.append("tree" + recursedTree.getName() + e.getName());
                    }
                    else{
                    output.append("\ntree" + recursedTree.getName() + e.getName());
                    }
                }
            }
            //turns the StringBuilder into an actual File
            File outputTree = new File(objects + hashFile(output.toString().getBytes()));
            outputTree.createNewFile();
            Files.write(outputTree.toPath(), output.toString().getBytes());
            return outputTree.getName();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    //shitty attempt
    // public void initWorkingList(){
    //     ArrayList<WorkingListObject> workingArray = new ArrayList<WorkingListObject>();
    //     try{
    //         BufferedReader lineReader = Files.newBufferedReader(index.toPath());
    //         while(lineReader.ready()){
    //             //adds WorkingListObjects (an empty containter class) to the list from the index file
    //             String line = lineReader.readLine();
    //             workingArray.add(new WorkingListObject("blob", line.substring(0, 41), Paths.get(line.substring(41))));
    //         }
    //         lineReader.close();
    //     }catch(IOException e){
    //         e.printStackTrace();
    //     }
    //     //god, I hope this works 
    //     Collections.sort(workingArray);
    //     workingList = new LinkedList<>(workingArray);
    //     recursiveColapse(workingList.getFirst());
    // }
    // ///GRAAAH I HATE IT I HATE IT I HATE IT
    // public void recursiveColapse(WorkingListObject previous){
    //     //breakaway boolean
    //     boolean stillEqual = true;
    //     workingList.removeFirst();
    //     //Stringbuilder init
    //     StringBuilder outputTree = new StringBuilder(previous.getType() + " " + previous.getHash() + " " + previous.getPath().getFileName());
    //     try{
    //         //for every object left in the array (if there are none, proceed to the base case)
    //         for(int i = 0; i < workingList.size() && stillEqual; i ++){
    //             //grab the next item
    //             WorkingListObject next = workingList.getFirst();
    //             //if the parent is equal to the start of the chain
    //             if(next.getPath().getParent().equals(previous.getPath().getParent())){
    //                 //add it to the next line on the sub-tree and remove it
    //                 outputTree.append("\n" + next.getType() + " " + next.getHash() + " " + next.getPath().getFileName());
    //                 workingList.removeFirst();
    //             }
    //             /*ELSE EXPLANATION
    //              * IF THE NEXT FILE DOES NOT SHARE THE SAME PARENT DIRECTORY
    //              * DO NOT TRIGGER BASE CASE
    //              * SAVE THE CURRENT TREE
    //              * START THE PROCESS OVER AGAIN WITH THE CURRENT TREE AS THE START OF NEXT RECURSE
    //              * IF THE SORTING ACTUALLY *WORKS*, THE NEXT RECURSION SHOULD BE THE STUFF IMMEDIATELY OUTSIDE THE CURRENT DIRECTORY
    //              */
    //             else{
    //                 stillEqual = false;
    //                 File leafTree = new File(objects + hashFile(outputTree.toString().getBytes()));
    //                 Files.write(leafTree.toPath(), outputTree.toString().getBytes());
    //                 workingList.addFirst(new WorkingListObject("tree", hashFile(outputTree.toString().getBytes()), previous.getPath().getParent()));
    //                 recursiveColapse(next);
    //             }
    //         }//base case
    //         if(stillEqual = true){
    //             //save the file (and update the directory i guess)
    //             File rootTree = new File(objects + hashFile(outputTree.toString().getBytes()));
    //             Files.write(rootTree.toPath(), outputTree.toString().getBytes());
    //             workingList.addFirst(new WorkingListObject("tree", hashFile(outputTree.toString().getBytes()), previous.getPath().getParent()));
    //         }
    //     }catch(IOException e){
    //         e.printStackTrace();
    //     }
    // }
    //I was honestly really stuck until I came across Shimone's code. After looking at what he did, I really don't know any better way to do it
    public void initIndexTree(){
        try{
        StringBuilder rootTreeContents = new StringBuilder();
        String indexFile = Files.readString(index.toPath());
        String[] entriesArr = indexFile.split("\n");
        ArrayList<String> workingList = new ArrayList<String>();
        HashSet<String> directories = new HashSet<String>();

        for (int i = 0; i < entriesArr.length; i++){
            String path = entriesArr[i].split(" ")[1];
            if (path.contains("/")){
                String directory = path.substring(0, path.indexOf("/"));
                directories.add(directory); //smart way to ensure no duplicate directories are being added
                workingList.add("blob " + entriesArr[i]);

            } else { //is a file
                rootTreeContents.append("blob " + entriesArr[i]);
                rootTreeContents.append("\n");
            }
        }

        for (String directory : directories){
            String treeHash = initIndexTreeHelper(workingList, directory);
            rootTreeContents.append("tree " + treeHash + " " + directory + "\n");
        }

        if (rootTreeContents.length() > 0){
            rootTreeContents.deleteCharAt(rootTreeContents.length() - 1); //gets rid of trailing newline
        }
        //output statements
        String contents = rootTreeContents.toString();
        File output = new File(objects + hashFile(contents.getBytes()));
        Files.write(output.toPath(), contents.getBytes());
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    //returns tree hash
    public String initIndexTreeHelper(ArrayList<String> entries, String directoryPrefix){
        ArrayList<String> subentries = new ArrayList<String>();
        for (String entry : entries){
            String path = entry.split(" ")[2]; //gets the file path
            if (path.contains(directoryPrefix)){
                subentries.add(entry);
            }
        }
        HashSet<String> treeEntryRows = new HashSet<String>(); //get only unqiue adds
        for (String subentry : subentries){
            String subpath = subentry.split(" ")[2].substring(directoryPrefix.length() + 1);
            if (subpath.contains("/")){ //a directory
                String firstFolder = subpath.substring(0, subpath.indexOf("/"));
                String subTreeHash = initIndexTreeHelper(subentries, directoryPrefix + "/" + firstFolder); //recursion
                treeEntryRows.add("tree " + subTreeHash + " " + directoryPrefix + "/" + firstFolder);
            } else { //a file (base case)
                treeEntryRows.add(subentry); //already formatted nicely
            }
        }
        StringBuilder entryContentSB = new StringBuilder(); //final put-together
        for (String s : treeEntryRows){
            entryContentSB.append(s);
            entryContentSB.append("\n");
        }
        if (entryContentSB.length() > 0){
            entryContentSB.deleteCharAt(entryContentSB.length() - 1);
        }
        String entryContent = entryContentSB.toString();
        String treeHash = hashFile(entryContent.getBytes());
        File output = new File(objects + treeHash);
        try{
            Files.write(output.toPath(), entryContent.getBytes());
        }catch(IOException e){
            e.printStackTrace();
        }
        return treeHash;
    }
}