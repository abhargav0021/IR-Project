import java.util.*;

public class Tokenizer {
    private Set<String> stopwords;

    public Tokenizer(Set<String> stopwords) {
        this.stopwords = stopwords;
    }

    public List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();

        // Convert to lowercase
        text = text.toLowerCase();

        // Split on any sequence of non-alphanumeric characters
        String[] rawTokens = text.split("[^a-z0-9]+");

        for (String token : rawTokens) {
            if (token.isEmpty()) continue;

            // Ignore tokens that contain any digits
            if (token.matches(".*\\d.*")) continue;

            // Ignore stopwords
            if (stopwords.contains(token)) continue;

            tokens.add(token);
        }

        return tokens;
    }
}
