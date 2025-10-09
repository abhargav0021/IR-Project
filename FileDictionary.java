import java.util.*;

public class FileDictionary {
    private Map<String, Integer> fileToId;
    private Map<Integer, String> idToFile;

    public FileDictionary() {
        this.fileToId = new LinkedHashMap<>();
        this.idToFile = new LinkedHashMap<>();
    }

public int getOrAddFile(String fileName) {
    if (!fileToId.containsKey(fileName)) {
        int id = Integer.parseInt(fileName.substring(fileName.lastIndexOf('-') + 1));
        fileToId.put(fileName, id);
        idToFile.put(id, fileName);
    }
    return fileToId.get(fileName);
}


    public Map<String, Integer> getDictionary() {
        return fileToId;
    }

    public String getFileById(int id) {
        return idToFile.get(id);
    }
}
