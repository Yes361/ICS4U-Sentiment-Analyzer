/*
    Authors: Raiyan Islam
    Date: 05/06/2025
    Program Name: Sentiment Analyzer
    Description: This Program provides a Graphical User Interface (GUI) to perform
    sentiment analysis on files
*/

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);

        // Prompting user whether they wise to view the console or
        // gui demo
        System.out.println("""
        Launch Console Demo or GUI?
        [1] Console
        [2] GUI
        \s
        """);

        int option = input.nextInt();

        if (option == 1) {
            ConsoleDemo();
        } else {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        }

        input.close();

    }

    /**
     * Console Demo is the console version of the sentiment analysis
     *
     * @throws FileNotFoundException If the files to dictionary, output, or batch files are not defined an error is thrown
     */
    public static void ConsoleDemo() throws FileNotFoundException {

        String outputDirectory = "src\\output";
        String dictionaryPath = "src\\dictionary\\dict.txt";

        // Get report Filename
        String reportFileName = createTimestampedFileName("report-%s.txt", "yyyyMMdd-hhMMss");

        // Get the Output Path
        File OutputPath = Paths.get(System.getProperty("user.dir"), outputDirectory, reportFileName).toFile();

        // Get the dictionary path
        File dictionary = Paths.get(System.getProperty("user.dir"),dictionaryPath).toFile();

        // Instantiate a new Analyzer
        SentimentAnalyzer analyzer = new SentimentAnalyzer();

        // Load the dictionary and log the attempt
        try {
            analyzer.LoadDictionary(dictionary);
        } catch (FileNotFoundException err) {
            System.out.printf("Invalid Dictionary File for %s\n", dictionary.getAbsolutePath());
            return;
        }

        // Batch of fileNames to test
        File[] fileNames = {
            new File(System.getProperty("user.dir"), "src\\input\\data1.txt")
        };

        // Create a scorer and analyze the file batch
        SentimentScorer Scorer = new RatioScorer();
        SentimentResult[] sentimentResults = analyzer.BatchAnalyzeFromDict(fileNames, Scorer);

        WriteResultsToFile(OutputPath, sentimentResults);
    }

    public static void WriteResultsToFile(File OutputPath, SentimentResult[] Results) throws FileNotFoundException {
        // Create a PrintWriter and print the batch report
        try (PrintWriter writer = new PrintWriter(OutputPath)) {
            writer.write(SentimentAnalyzer.WriteBatchReport(Results));
        }
    }

    /**
     * createTimestampedFileName
     *
     * @param fileName FileName Schema
     * @param timestampFormat The format of the timestamp that should be incorporated in the FileName Schema
     *
     * createTimestampedFileName("report-%s.txt", "yyyyMMdd-hhMMss") returns "report-20250506-120509.txt
     */
    public static String createTimestampedFileName(String fileName, String timestampFormat) {
        // Get Current Time
        LocalDateTime currentTime = LocalDateTime.now();

        // Format current time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timestampFormat);
        String timestamp = currentTime.format(formatter);

        // Return a FileName that has a timestamp incorporated
        return String.format(fileName, timestamp);
    }
}