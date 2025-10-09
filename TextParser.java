import java.io.*;
import java.util.*;

public class TextParser {

    private static Porter stemmer = new Porter();
    private static FileDictionary fileDict = new FileDictionary();
    private static WordDictionary wordDict= new WordDictionary();

    public static void main(String[] args) throws Exception {
        if (args.length<1) {
            System.out.println("Usage: java TextParser <inputFile1> <inputFile2> ...");
            return;
        }

        // Load stopwords and initialize tokenizer
        Set<String> stopwords = loadStopwords("stopwordlist.txt");
        Tokenizer tokenizer = new Tokenizer(stopwords);

        // Process each input file (e.g., ft911_1, ft911_4, etc.)
        for (String inputFile: args) {
            parseFile(inputFile, tokenizer);
        }

        // After processing all files, finalize word IDs
        wordDict.finalizeIds();

        // Write out the final dictionaries
        writeDictionaries(wordDict, fileDict, "parser_output.txt");
        System.out.println("Parsing complete. Output stored in parser_output.txt");
    }

    private static void parseFile(String inputFile, Tokenizer tokenizer) throws IOException {
        BufferedReader reader= new BufferedReader(new FileReader(inputFile));
        String line;
        StringBuilder docBuilder= new StringBuilder();
        String currentDocName= null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("<DOCNO>")) {
                // Process the previous document (if any)
                if (currentDocName !=null) {
                    processDocument(currentDocName, docBuilder.toString(), tokenizer);
                    docBuilder.setLength(0);
                }
                // Extract DOCNO like FT911-15 and register it
                currentDocName =line.replaceAll("<.*?>", "").trim();
                fileDict.getOrAddFile(currentDocName);
            } else if (!line.startsWith("<DOC>") &&!line.startsWith("</DOC>")
                    && !line.startsWith("<TEXT>")&& !line.startsWith("</TEXT>")) {
                // Append actual content only
                docBuilder.append(line).append(" ");
            }
        }

        // Process the last document in the file
        if (currentDocName!= null) {
            processDocument(currentDocName, docBuilder.toString(), tokenizer);
        }

        reader.close();
    }

    private static void processDocument(String docName, String text, Tokenizer tokenizer) {
        List<String> tokens = tokenizer.tokenize(text);
        for (String token : tokens) {
            String stemmed = stemmer.stripAffixes(token);
            if (stemmed != null && !stemmed.isEmpty()) {
                wordDict.addWordToVocab(stemmed);
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

        // Write Word Dictionary (alphabetically sorted IDs)
        writer.write("=== WORD DICTIONARY ===\n");
        for (Map.Entry<String, Integer> entry : wordDict.getDictionary().entrySet()) {
            writer.write(entry.getKey() + "\t" + entry.getValue() + "\n");
        }

        writer.write("\n=== FILE DICTIONARY ===\n");
        for (Map.Entry<String, Integer> entry : fileDict.getDictionary().entrySet()) {
            writer.write(entry.getKey() + "\t" + entry.getValue() + "\n");
        }

        writer.close();
    }
}