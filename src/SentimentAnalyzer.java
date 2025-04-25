import java.util.*;
import java.io.*;

public class SentimentAnalyzer {
    private final ArrayList<String> wordList = new ArrayList<>();
    private final HashMap<String, String> DictionarySentiments = new HashMap<>();

    public void LoadFile(File file) throws FileNotFoundException {
        Scanner FileReader = new Scanner(file);
        wordList.clear();

        while (FileReader.hasNextLine()) {
            String[] words = FileReader.nextLine().trim().split(" ");

            wordList.addAll(Arrays.asList(words));
//            for (String word : words) {
//                wordList.add(word.replace())
//            }
        }
    }

    public void LoadDictionary(File file) throws FileNotFoundException {
        Scanner FileReader = new Scanner(file);
        DictionarySentiments.clear();

        while (FileReader.hasNextLine()) {
            String contents = FileReader.nextLine().trim();
            String[] arguments = contents.split(",");

            if (arguments.length != 2) {
                throw new RuntimeException(String.format("Arguments for %s wrong", contents));
            }

            String word = arguments[0].trim();
            String sentiment = arguments[1].trim();
            DictionarySentiments.put(word, sentiment);
        }
    }

    public SentimentResults AnalyzeFromDict(File FilePath, File DictionaryPath, SentimentScorer Scorer) throws FileNotFoundException {
        LoadFile(FilePath);
        LoadDictionary(DictionaryPath);

        Classifications classes = new Classifications();

        for (String word : wordList) {
            if (DictionarySentiments.containsKey(word)) {
                String classifier = DictionarySentiments.get(word);
                classes.insert(classifier, word);
            } else {
                classes.insert("Unclassified", word);
            }
        }

        SentimentResults results = new SentimentResults(FilePath.getName(), wordList.size(), classes);
        double score = Scorer.calculateScore(results);
        String sentiment = Scorer.classify(score);

        results.setScore(score);
        results.setSentiment(sentiment);

        return results;
    }
}
