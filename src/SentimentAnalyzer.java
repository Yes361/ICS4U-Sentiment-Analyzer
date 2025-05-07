/*
* SentimentAnalyzer includes all the code for processing a text file into
* sentiment analytics
*  */

import java.util.*;
import java.io.*;

public class SentimentAnalyzer {
    /// Instance Varaibles

    // SentimentDictionary holds each word and their given sentiment and intensity
    private final SentimentDictionary Sentiments = new SentimentDictionary();

    // Constructor for designating a default dictionary path
    public SentimentAnalyzer(File DefaultDictionaryPath) throws FileNotFoundException {
        LoadDictionary(DefaultDictionaryPath);
    }

    public SentimentAnalyzer() {}

    /**
     * Helper method to read and process words from a file
     *
     * @param file The File to read words from
     *
     * @return An ArrayList of Strings of the processed words
     *
     * @throws FileNotFoundException If the file does not exist an error is thrown
     */
    private static ArrayList<String> ReadWordsFromFile(File file) throws FileNotFoundException {
        // FileReader Scanner is initialized
        Scanner FileReader = new Scanner(file);
        ArrayList<String> wordList = new ArrayList<>();

        // Continue reading until the FileReader has no available lines
        while (FileReader.hasNextLine()) {
            // Split the line into space-separated words and
            // remove Grammar Punctuation from words
            String[] words = FileReader.nextLine().toLowerCase().split("[ .,?!]");

            // Adding the words from line directly to the WordList
            wordList.addAll(Arrays.asList(words));
        }

        return wordList;
    }

    /**
     * LoadDictionary loads the dictionary from a given file
     *
     * @throws FileNotFoundException If the file does not exist an error is thrown
     */
    public void LoadDictionary(File file) throws FileNotFoundException {
        // Create a file reader and clear the dictionary contents
        Scanner FileReader = new Scanner(file);
        Sentiments.clear();

        // Error Message dialog
        String ErrorMessage = """
        Arguments for %s invalid
        SentimentDictionary arguments must follow schema:
        WORD   SENTIMENT INTENSITY
        String String    Double
        """;

        // Continue reading till their is no more left to be read
        while (FileReader.hasNextLine()) {
            // split the line into space-seperated words
            String contents = FileReader.nextLine().trim();
            String[] arguments = contents.split(" ");

            // Throw an error if there is a mismatch in the number of
            if (arguments.length != 3) {
                throw new RuntimeException(String.format(ErrorMessage, contents));
            }

            // Attempt to parse the line into word, sentiment, and intensity
            try {
                String word = arguments[0].trim();
                String sentiment = arguments[1].trim();
                double intensity = Double.parseDouble(arguments[2].trim());

                Sentiments.insert(word, sentiment, intensity);
            } catch (NumberFormatException err) {
                // Throw a RuntimeException explaining the necessary requirements
                // for the SentimentDictionary
                throw new RuntimeException(String.format(ErrorMessage, contents));
            }
        }
    }

    public SentimentDictionary getSentiments() {
        return Sentiments;
    }

    /**
     * AnalyzeFromDict analyzes a file by cross-referencing words in the file to words
     * in the SentimentDictionary
     *
     * @param FilePath path of the file to be analyzed
     * @param Scorer the Scorer that will be used to score the results
     *
     * @throws FileNotFoundException If the file does not exist an error is thrown
     *
     * @return SentimentResult that will describe the result of the analysis
     */
    public SentimentResult AnalyzeFromDict(File FilePath, SentimentScorer Scorer) throws FileNotFoundException {
        // Read words
        ArrayList<String> wordList = ReadWordsFromFile(FilePath);

        // Create a new empty Classifications
        Classifications classes = new Classifications();

        for (String word : wordList) {
            // If a word isn't defined in the SentimentDictionary
            // assume it has a neutral sentiment with 0.0 intensity
            if (!Sentiments.ContainsWord(word)) {
                classes.insert("NEUTRAL", word, 0.0);
            } else {
                // Get the sentiment and intensity of the word and
                // add it to classifications
                String classifier = Sentiments.getSentiment(word);
                double intensity = Sentiments.getIntensity(word);

                classes.insert(classifier, word, intensity);
            }
        }

        // Create new results from the classifications and file metadata
        SentimentResult results = new SentimentResult(FilePath, classes);
        results.ScoreResults(Scorer);

        return results;
    }

    /**
     * BatchAnalyzeFromDict performs an analysis on a list/batch of files
     *
     * @param files list of files to be analyzed
     * @param Scorer the Scorer that will be used to score the results
     *
     * @throws FileNotFoundException If any of the files do not exist an error is thrown
     *
     * @return SentimentResult[] that will describe the results of the analysis
     */
    public SentimentResult[] BatchAnalyzeFromDict(File[] files, SentimentScorer Scorer) throws FileNotFoundException {
        ArrayList<SentimentResult> results = new ArrayList<>();

        // Iterate over the files and add the individual SentimentResult to the list
        for (File file : files) {
            SentimentResult result = AnalyzeFromDict(file, Scorer);
            results.add(result);
        }

        return results.toArray(new SentimentResult[0]);
    }

    /**
     * WriteBatchReport accepts a list of sentiment results and properly formats it
     * into a summary report
     *
     * @param results the list of sentiment results
     *
     * @return A string containing the report
     */
    public static String WriteBatchReport(SentimentResult[] results) {
        StringBuilder report = new StringBuilder();

        // Add hard-coded metadata
        report.append("===Overall Report===\n");
        report
            .append("Number of Report(s): ")
            .append(results.length)
            .append("\n");

        double averageScore = 0.0;

        // Append the summaries of each result to the report
        for (SentimentResult result : results) {
            report.append(result.getSummary());

            // Sum the score
            averageScore += result.getScore();
        }

        // Divide by result length to get average score
        averageScore /= results.length;

        // Append Statistics
        report
            .append("Average Score: ")
            .append(String.format("%.2f", averageScore))
            .append("\n");

        report.append("=====================");

        return report.toString();
    }
}

