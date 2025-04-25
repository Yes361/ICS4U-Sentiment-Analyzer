import java.nio.file.Paths;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        SentimentAnalyzer analyzer = new SentimentAnalyzer();
        Scanner input = new Scanner(System.in);

//        String file_path = readLine(input, "File Path: ");
        String file_path = "C:\\Users\\NAZRU\\IdeaProjects\\ICS4U-Sentiment-Analyzer\\src\\input\\data1.txt";
        File file = new File(file_path);

//        String dictionary_path = readLine(input, "Dictionary Path: ");
        String dictionary_path = "C:\\Users\\NAZRU\\IdeaProjects\\ICS4U-Sentiment-Analyzer\\src\\dictionary\\input.txt";
        File dictionary = new File(dictionary_path);

        SentimentScorer Scorer = new RatioScorer();

        System.out.print(analyzer.AnalyzeFromDict(file, dictionary, Scorer));
    }

    public static String readLine(Scanner input, String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }
}