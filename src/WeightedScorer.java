public class WeightedScorer extends SentimentScorer {
    @Override
    public double calculateScore(SentimentResult results) {
        return 0;
    }

    @Override
    public String classify(double score) {
        return "not sigma";
    }
}
