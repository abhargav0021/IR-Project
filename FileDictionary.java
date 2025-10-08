import java.util.*;

public class FileDictionary {
    private Map<String, Integer> fileToId;
    private int nextId;

    public FileDictionary() {
        this.fileToId = new LinkedHashMap<>();
        this.nextId = 1;
    }

    public int getOrAddFile(String fileName) {
        if (!fileToId.containsKey(fileName)) {
            fileToId.put(fileName, nextId++);
        }
        return fileToId.get(fileName);
    }

    public Map<String, Integer> getDictionary() {
        return fileToId;
    }
}
