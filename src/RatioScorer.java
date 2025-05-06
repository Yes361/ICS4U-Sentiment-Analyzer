public class RatioScorer extends SentimentScorer {
    private double ClassificationThreshold = 0.05;
    @Override
    public double calculateScore(SentimentResult results) {
        int positiveCount = results.getClassifications().getFrequency("POSITIVE");
        int negativeCount = results.getClassifications().getFrequency("NEGATIVE");
        int totalWords = results.getClassifications().getTotalWords();

        return positiveCount > negativeCount ? (double) positiveCount / totalWords : (double) -negativeCount / totalWords;
    }

    @Override
    public String classify(double score) {
        if (score < -ClassificationThreshold) {
            return "NEGATIVE";
        } else if (score > ClassificationThreshold) {
            return "POSITIVE";
        } else {
            return "NEUTRAL";
        }
    }
}