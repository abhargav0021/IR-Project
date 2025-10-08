import java.util.*;

public class WordDictionary {
    private Map<String, Integer> wordToId;
    private int nextId;

    public WordDictionary() {
        this.wordToId = new LinkedHashMap<>();
        this.nextId = 1;
    }

    public int getOrAddWord(String word) {
        if (!wordToId.containsKey(word)) {
            wordToId.put(word, nextId++);
        }
        return wordToId.get(word);
    }

    public Map<String, Integer> getDictionary() {
        return wordToId;
    }
}
