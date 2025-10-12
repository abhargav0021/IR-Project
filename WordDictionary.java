import java.util.*;

public class WordDictionary {
    private Set<String> vocabulary; // Temporary storage for unique words
    private Map<String, Integer> wordToId;
    private Map<Integer, String> idToWord;

    public WordDictionary() {
        this.vocabulary = new HashSet<>();
        this.wordToId = new LinkedHashMap<>();
        this.idToWord = new LinkedHashMap<>();
    }

    //Add words during parsing (no IDs yet)
    public void addWordToVocab(String word) {
        vocabulary.add(word);
    }

    //After all documents processed, sort & assign IDs
    public void finalizeIds() {
        List<String> sortedWords = new ArrayList<>(vocabulary);
        Collections.sort(sortedWords);
        int id = 1;
        for (String w : sortedWords) {
            wordToId.put(w, id);
            idToWord.put(id, w);
            id++;
        }
    }

    public Map<String, Integer> getDictionary() {
        return wordToId;
    }

    public String getWordById(int id) {
        return idToWord.get(id);
    }
}
