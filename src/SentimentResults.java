import java.util.*;
import java.io.*;

public class SentimentResults {
    private String filename;
    private int totalWords;
    private double score;
    private String sentiment;
    private Classifications classes;

    public SentimentResults(String filename, int totalWords, Classifications classes) {
        this.filename = filename;
        this.totalWords = totalWords;
        this.classes = classes;
    }

    public String getFileName() {
        return this.filename;
    }

    public void setFileName(String filename) {
        this.filename = filename;
    }

    public int getTotalWords() {
        return this.totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getSentiment() {
        return this.sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public Classifications getClasses() {
        return classes;
    }

    public String toString() {
        return String.format("SentimentResults{filename=%s, totalWords=%d, score=%.2f, sentiment=%s, classes=%s}", this.filename, this.totalWords, this.score, this.sentiment, this.classes);
    }
}
