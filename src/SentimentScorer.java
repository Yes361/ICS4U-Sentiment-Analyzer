public abstract class SentimentScorer {
    public abstract double calculateScore(SentimentResult results);
    public abstract String classify(double score);
    public String getName() {
        return this.getClass().getName();
    }
    public String toString() {
        return this.getName();
    }
}
