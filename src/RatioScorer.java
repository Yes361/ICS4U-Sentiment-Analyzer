public class RatioScorer extends SentimentScorer {
    @Override
    public double calculateScore(SentimentResults results) {
        int positiveCount = results.getClasses().getFrequency("POSITIVE");
        int negativeCount = results.getClasses().getFrequency("NEGATIVE");
        int totalWords = positiveCount + negativeCount;

        return positiveCount > negativeCount ? (double) positiveCount / totalWords : (double) -negativeCount / totalWords;
    }

    @Override
    public String classify(double score) {
        if (score < 0) {
            return "NEGATIVE";
        } else if (score > 0) {
            return "POSITIVE";
        } else {
            return "NEUTRAL";
        }
    }
}