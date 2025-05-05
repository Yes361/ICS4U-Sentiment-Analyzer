import java.io.File;

public class SentimentResult {
    private final File file;
    private double score;
    private String sentiment;
    private final Classifications classifications;

    public SentimentResult(File file, Classifications classifications) {
        this.file = file;
        this.classifications = classifications;
    }

    public String getFileName() {
        return this.file.getName();
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

    public Classifications getClassifications() {
        return classifications;
    }

    public String toString() {
        return String.format(
            "SentimentResults{filename=%s, score=%.2f, sentiment=%s, classes=%s}",
            file.getName(),
            score,
            sentiment,
            classifications
        );
    }

    public String getSummary() {
        return String.format("""
        ====SUMMARY===
        File: %s
        Total Words: %d
        Score: %.2f
        Sentiment: %s
        \s
        Positive Words
        %s
        \s
        Negative Words
        %s
        \s
        """,
            file.getName(),
            classifications.getTotalWords(),
            score,
            sentiment,
            classifications.getClassifier("POSITIVE"),
            classifications.getClassifier("NEGATIVE")
        );
    }
}
