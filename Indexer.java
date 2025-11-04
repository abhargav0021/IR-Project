// Indexer.java
import java.io.*;
import java.util.*;

public class Indexer {

    private Map<Integer, Map<Integer, Integer>> forwardIndex = new HashMap<>();
    private Map<Integer, Map<Integer, Integer>> invertedIndex = new HashMap<>();

    // Adds a word occurrence (docID, wordID)
    public void addWord(int docID, int wordID) {
        // --- Forward index ---
        forwardIndex.computeIfAbsent(docID, k -> new HashMap<>());
        Map<Integer, Integer> wordFreqs = forwardIndex.get(docID);
        wordFreqs.put(wordID, wordFreqs.getOrDefault(wordID, 0) + 1);

        // --- Inverted index ---
        invertedIndex.computeIfAbsent(wordID, k -> new HashMap<>());
        Map<Integer, Integer> docFreqs = invertedIndex.get(wordID);
        docFreqs.put(docID, docFreqs.getOrDefault(docID, 0) + 1);
    }

    // Write forward index to file
    public void writeForwardIndex(String fileName) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<Integer, Map<Integer, Integer>> docEntry : forwardIndex.entrySet()) {
                bw.write(docEntry.getKey() + ": ");
                for (Map.Entry<Integer, Integer> wordEntry : docEntry.getValue().entrySet()) {
                    bw.write(wordEntry.getKey() + ":" + wordEntry.getValue() + "; ");
                }
                bw.newLine();
            }
        }
    }

    // Write inverted index to file
    public void writeInvertedIndex(String fileName) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<Integer, Map<Integer, Integer>> wordEntry : invertedIndex.entrySet()) {
                bw.write(wordEntry.getKey() + ": ");
                for (Map.Entry<Integer, Integer> docEntry : wordEntry.getValue().entrySet()) {
                    bw.write(docEntry.getKey() + ":" + docEntry.getValue() + "; ");
                }
                bw.newLine();
            }
        }
    }

    // Optional: Retrieve info for a query term (by wordID)
    public Map<Integer, Integer> getPostingList(int wordID) {
        return invertedIndex.getOrDefault(wordID, new HashMap<>());
    }
}

