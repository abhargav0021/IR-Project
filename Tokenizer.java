import java.util.*;
import java.util.regex.*;

public class Tokenizer {
    private Set<String> stopwords;

    public Tokenizer(Set<String> stopwords) {
        this.stopwords = stopwords;
    }

    public List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = pattern.matcher(text.toLowerCase());

        while (matcher.find()) {
            String word = matcher.group();
            if (!stopwords.contains(word)) {
                tokens.add(word);
            }
        }
        return tokens;
    }
}