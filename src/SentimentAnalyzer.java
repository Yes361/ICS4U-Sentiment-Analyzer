import javax.swing.*;
import java.util.*;
import java.io.*;

public class SentimentAnalyzer {
    private final SentimentDictionary Sentiments = new SentimentDictionary();

    public SentimentAnalyzer(File DefaultDictionaryPath) throws FileNotFoundException {
        LoadDictionary(DefaultDictionaryPath);
    }

    public SentimentAnalyzer() {
    }

    private static ArrayList<String> ReadWordsFromFile(File file) throws FileNotFoundException {
        Scanner FileReader = new Scanner(file);
        ArrayList<String> wordList = new ArrayList<>();

        while (FileReader.hasNextLine()) {
            // Remove Grammar Punctuation from words
            String[] words = FileReader.nextLine().toLowerCase().split("[ .,?!]");
            wordList.addAll(Arrays.asList(words));
        }

        return wordList;
    }

    /**
     * LoadDictionary
     */
    public void LoadDictionary(File file) throws FileNotFoundException {
        Scanner FileReader = new Scanner(file);
        Sentiments.clear();

        while (FileReader.hasNextLine()) {
            String contents = FileReader.nextLine().trim();
            String[] arguments = contents.split(" ");

            if (arguments.length != 3) {
                throw new RuntimeException(String.format("""
                Arguments for %s invalid
                SentimentDictionary arguments must follow schema:
                WORD   SENTIMENT INTENSITY
                String String    Double
                """, contents));
            }

            String word = arguments[0].trim();
            String sentiment = arguments[1].trim();
            double intensity = Double.parseDouble(arguments[2].trim());
            Sentiments.insert(word, sentiment, intensity);
        }
    }

    public SentimentDictionary getSentiments() {
        return Sentiments;
    }

    /**
     * AnalyzeFromDict
     */
    public SentimentResult AnalyzeFromDict(File FilePath, SentimentScorer Scorer) throws FileNotFoundException {
        ArrayList<String> wordList = ReadWordsFromFile(FilePath);
        Classifications classes = new Classifications();

        for (String word : wordList) {
            if (!Sentiments.ContainsWord(word)) {
                classes.insert("NEUTRAL", word, 0.0);
            } else {
                String classifier = Sentiments.getSentiment(word);
                double intensity = Sentiments.getIntensity(word);

                classes.insert(classifier, word, intensity);
            }
        }

        SentimentResult results = new SentimentResult(FilePath, classes);

        double score = Scorer.calculateScore(results);
        String sentiment = Scorer.classify(score);

        results.setScore(score);
        results.setSentiment(sentiment);

        return results;
    }

    public SentimentResult[] BatchAnalyzeFromDict(File[] files, SentimentScorer Scorer) throws FileNotFoundException {
        ArrayList<SentimentResult> results = new ArrayList<>();

        for (File file : files) {
            SentimentResult result = AnalyzeFromDict(file, Scorer);
            results.add(result);
        }

        return results.toArray(new SentimentResult[0]);
    }

    public SentimentResult[] BatchAnalyzeFromDict(String[] fileNames, SentimentScorer Scorer) throws FileNotFoundException {
        ArrayList<SentimentResult> results = new ArrayList<>();

        for (String fileName : fileNames) {
            File file = new File(fileName);

            SentimentResult result = AnalyzeFromDict(file, Scorer);
            results.add(result);
        }

        return results.toArray(new SentimentResult[0]);
    }
}

