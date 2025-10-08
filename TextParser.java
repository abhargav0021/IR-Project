import java.io.*;
import java.util.*;

public class TextParser {

    // Make Porter stemmer and file dictionary global so they're shared across documents
    private static Porter stemmer = new Porter();
    private static FileDictionary fileDict = new FileDictionary();

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java TextParser <inputFile>");
            return;
        }

        String inputFile = args[0];
        Set<String> stopwords = loadStopwords("stopwordlist.txt");
        Tokenizer tokenizer = new Tokenizer(stopwords);
        WordDictionary wordDict = new WordDictionary();

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String line;
        StringBuilder docBuilder = new StringBuilder();
        String currentDocName = null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.startsWith("<DOCNO>")) {  // TREC format document start
                // Process previous document if exists
                if (currentDocName != null) {
                    processDocument(currentDocName, docBuilder.toString(), tokenizer, wordDict);
                    docBuilder.setLength(0);
                }

                // Extract document name (e.g., FT911-1)
                currentDocName = line.replaceAll("<.*?>", "").trim();
                fileDict.getOrAddFile(currentDocName);

            } else if (!line.startsWith("<DOC>") && !line.startsWith("</DOC>")
                    && !line.startsWith("<TEXT>") && !line.startsWith("</TEXT>")) {
                // Only append actual content
                docBuilder.append(line).append(" ");
            }
        }

        // Process the last document
        if (currentDocName != null) {
            processDocument(currentDocName, docBuilder.toString(), tokenizer, wordDict);
        }
        reader.close();

        writeDictionaries(wordDict, fileDict, "parser_output.txt");
        System.out.println("✅ Parsing complete. Output stored in parser_output.txt");
    }

    private static void processDocument(String docName, String text, Tokenizer tokenizer, WordDictionary wordDict) {
        List<String> tokens = tokenizer.tokenize(text);

        for (String token : tokens) {
            String stemmed = stemmer.stripAffixes(token);
            if (stemmed != null && !stemmed.isEmpty()) {
                wordDict.getOrAddWord(stemmed);
            }
        }
    }

    private static Set<String> loadStopwords(String filePath) throws IOException {
        Set<String> stopwords = new HashSet<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String word;
        while ((word = br.readLine()) != null) {
            stopwords.add(word.trim().toLowerCase());
        }
        br.close();
        return stopwords;
    }

    private static void writeDictionaries(WordDictionary wordDict, FileDictionary fileDict, String outputFile) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

        // Write word dictionary (token → tokenID)
        for (Map.Entry<String, Integer> entry : wordDict.getDictionary().entrySet()) {
            writer.write(entry.getKey() + "\t" + entry.getValue() + "\n");
        }

        writer.write("\n");

        // Write file dictionary (docName → docID)
        for (Map.Entry<String, Integer> entry : fileDict.getDictionary().entrySet()) {
            writer.write(entry.getKey() + "\t" + entry.getValue() + "\n");
        }

        writer.close();
    }
}

/** Tokenizer: lowercases, splits on non-alphabetic, removes stopwords */
class Tokenizer {
    private Set<String> stopwords;

    public Tokenizer(Set<String> stopwords) {
        this.stopwords = stopwords;
    }

    public List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        String[] parts = text.toLowerCase().split("[^a-zA-Z]+");
        for (String word : parts) {
            if (!word.isEmpty() && !stopwords.contains(word)) {
                tokens.add(word);
            }
        }
        return tokens;
    }
}

/** Word → Token ID mapping */
class WordDictionary {
    private Map<String, Integer> wordToId = new LinkedHashMap<>();
    private int nextId = 1;

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

/** Document → Document ID mapping */
class FileDictionary {
    private Map<String, Integer> fileToId = new LinkedHashMap<>();
    private int nextId = 1;

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
