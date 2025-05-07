public class WeightedScorer extends SentimentScorer {

    @Override
    public double calculateScore(SentimentResult results) {
        double positiveIntensity = results.getClassifications().getIntensity("POSITIVE");
        double negativeIntensity = results.getClassifications().getIntensity("NEGATIVE");
        int totalWords = results.getClassifications().getTotalWords();

        return (positiveIntensity + negativeIntensity) / totalWords;
    }

    @Override
    public String classify(double score) {
        double classificationThreshold = 0.05;
        if (score < -classificationThreshold) {
            return "NEGATIVE";
        } else if (score > classificationThreshold) {
            return "POSITIVE";
        } else {
            return "NEUTRAL";
        }
    }
}
