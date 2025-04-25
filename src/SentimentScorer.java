public abstract class SentimentScorer {
    public abstract double calculateScore(SentimentResults results);
    public abstract String classify(double score);
}
