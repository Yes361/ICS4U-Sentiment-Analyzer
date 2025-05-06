import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;
import java.util.List;

public class Main {
    private static SentimentResult[] results;

    public static void main(String[] args) throws FileNotFoundException {
        MainFrame frame = new MainFrame();

        ConsoleDemo();
    }

    public static void ConsoleDemo() throws FileNotFoundException {
        Scanner input = new Scanner(System.in);

        String reportFileName = createTimestampedFileName("report-%s.txt", "yyyyMMdd-hhMMss");

        String OutputDirectoryPath = "C:\\Users\\NAZRU\\IdeaProjects\\ICS4U-Sentiment-Analyzer\\src\\output";
        File OutputPath = Paths.get(OutputDirectoryPath, reportFileName).toFile();

        SentimentAnalyzer analyzer = new SentimentAnalyzer();

        try {
            String dictionaryPath = "C:\\Users\\NAZRU\\IdeaProjects\\ICS4U-Sentiment-Analyzer\\src\\dictionary\\dict.txt";
            File dictionary = new File(dictionaryPath);

            analyzer.LoadDictionary(dictionary);

        } catch (FileNotFoundException err) {
            System.out.println("Invalid Dictionary File");
            return;
        }

        String[] fileNames = {
            "C:\\Users\\NAZRU\\IdeaProjects\\ICS4U-Sentiment-Analyzer\\src\\input\\qazi.txt",
        };


        SentimentScorer Scorer = new RatioScorer();
        SentimentResult[] sentimentResults = analyzer.BatchAnalyzeFromDict(fileNames, Scorer);

        WriteResultsToFile(OutputPath, sentimentResults);

        input.close();
    }

    public static void WriteResultsToFile(File OutputPath, SentimentResult[] Results) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(OutputPath)) {

            for (SentimentResult result : Results) {
                System.out.println(result);
                System.out.println();

                writer.write(result.getSummary());
                writer.write("\n");
            }

        }
    }

    public static String readLine(Scanner input, String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }

    public static String[] readLines(Scanner input, String prompt, String terminator) {
        System.out.print(prompt);

        ArrayList<String> lines = new ArrayList<>();
        String inputtedLine = input.nextLine();

        while (!inputtedLine.equals(terminator)) {
            lines.add(inputtedLine);
        }

        return (String[]) lines.toArray();
    }

    public static File GetAvailableFilePath(File directory, String FileName, String FileFormat) {
        int index = 1;
        File AvailableFile;

        do {
            String ModifiedFileName = String.format("%s%d.%s", FileName, index++, FileFormat);
            String FilePath = Paths.get(directory.getPath(), ModifiedFileName).toString();
            AvailableFile = new File(FilePath);

        } while (AvailableFile.exists() && AvailableFile.isFile());

        return AvailableFile;
    }

    public static String createTimestampedFileName(String fileName, String timestampFormat) {
        LocalDateTime currentTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timestampFormat);
        String timestamp = currentTime.format(formatter);

        return String.format(fileName, timestamp);
    }
}