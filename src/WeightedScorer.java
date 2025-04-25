public class WeightedScorer extends SentimentScorer {
    @Override
    public double calculateScore(SentimentResults results) {
        return 0;
    }

    @Override
    public String classify(double score) {
        return "";
    }
}
