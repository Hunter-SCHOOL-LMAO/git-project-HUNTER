import java.nio.file.*;
public class WorkingListObject implements Comparable<WorkingListObject>{
    private String type;
    private String hash;
    private Path path;
    public WorkingListObject(String type, String hash, Path path){
        this.type = type;
        this.hash = hash;
        this.path = path;
    }
    //getters and setters
    public String getType(){
        return type;
    }
    public String getHash(){
        return hash;
    }
    public Path getPath(){
        return path;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setHash(String hash){
        this.hash = hash;
    }
    public void setPath(Path path){
        this.path = path;
    }
    public int compareTo(WorkingListObject other){
        return path.compareTo(other.getPath());
    }
}
